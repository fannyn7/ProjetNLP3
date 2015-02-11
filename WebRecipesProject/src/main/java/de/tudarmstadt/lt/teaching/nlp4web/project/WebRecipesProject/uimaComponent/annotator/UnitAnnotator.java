package de.tudarmstadt.lt.teaching.nlp4web.project.WebRecipesProject.uimaComponent.annotator;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.fit.component.JCasAnnotator_ImplBase;
import org.apache.uima.fit.util.JCasUtil;
import org.apache.uima.jcas.JCas;

import de.tudarmstadt.ukp.dkpro.core.api.lexmorph.type.pos.CARD;
import de.tudarmstadt.ukp.dkpro.core.api.segmentation.type.Sentence;
import de.tudarmstadt.ukp.dkpro.core.api.segmentation.type.Token;
import de.tudarmstadt.ukp.dkpro.core.api.syntax.type.constituent.PRN;
import de.tudarmstadt.ukp.teaching.general.type.TextIngredients;
import de.tudarmstadt.ukp.teaching.general.type.UnitAnnotation;

/**
 * Questions :
 * 	.Q1 : cover the expression in bracket ? ex: 1 (16 ounce) small onion -> "1" or "1 (16 ounce)"
 *  .A1 : No   
 * 
 * Examples : 1 teaspoon 1/2 onion 1 cup
 * 
 * @author Solene, Killian
 *
 */
public class UnitAnnotator extends JCasAnnotator_ImplBase {

	private static final boolean debug = false;
	private List<String> unitDatabase;
	private List<String> ingredientDatabase;

	public List<String> getUnitDatabase(){
		return unitDatabase;	
	}

	public List<String> getIngredientDatabase(){
		return ingredientDatabase;	
	}

	private void initializeIngredientDatabase() {
		ingredientDatabase = new ArrayList<String>();
		try {
			String f = FileUtils.readFileToString(new File("src/main/resources/databases/ingredient.txt"));
			String[] lines = f.split("(\r\n|\n)");
			for (String line : lines){
				if (!line.startsWith("<!--")) {
					// quantity unit word  
					ingredientDatabase.add(line.toLowerCase());
				} // else source acknowledgement
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void initializeUnitDatabase() {
		unitDatabase = new ArrayList<String>();
		try {
			String f = FileUtils.readFileToString(new File("src/main/resources/databases/quantityUnit.txt"));
			String[] lines = f.split("(\r\n|\n)");
			for (String line : lines){
				if (!line.startsWith("<!--")) {
					// quantity unit word  
					unitDatabase.add(line.toLowerCase());
				} // else source acknowledgement
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void process(JCas jcas) throws AnalysisEngineProcessException {

		// Initialize databases
		initializeUnitDatabase();
		initializeIngredientDatabase();
		
		// process text
		splitByToken(jcas);
		// splitByCoveredNP(jcas);

	} // process

	
	
	private void splitByToken(JCas jcas) throws AnalysisEngineProcessException {
		// we are processing the Unit patterns sentence by sentence only in the text that 
		// gives the list of ingredients
		int endLastUnit = 0;
		for (TextIngredients text : JCasUtil
				.select(jcas, TextIngredients.class)) {
			endLastUnit = text.getBegin();
			for (Sentence sentence : JCasUtil.selectCovered(jcas,
					Sentence.class, text)) {
				List<CARD> lquantities = JCasUtil.selectCovered(jcas,
						CARD.class, sentence);
				if ( (lquantities == null) || lquantities.isEmpty() ) {
					// pattern : QUANTITY -> ^ 
					// use rote learning technique
					List<Token> tokens = JCasUtil.selectCovered(jcas,
							Token.class, sentence);
					boolean found = false;
					int j = 0;
					while (j < tokens.size()) {
						if (debug) {
							System.out.println("[MODIF] POS : "+tokens.get(j).getPos().getPosValue()+" "+tokens.get(j).getCoveredText());
						}
						found = getUnitDatabase().contains(
								tokens.get(j).getCoveredText());
						if (found) {
							setUnitAnnotation(jcas, null, tokens.get(j), "roteLearning");
							if (debug) {
								System.out.println("[MODIF] Found : POS : "+tokens.get(j).getPos().getPosValue()+" "+tokens.get(j).getCoveredText());
							}
							found = false;
						}
						j++;
						
					}
				}
				// for all cardinal number in this sentence
				for (CARD number : lquantities) {
					if (number.getEnd() <= endLastUnit) { //JCasUtil.selectCovered(CARD.class, number).size() > 0) {
						// this number is included in a quantity (unit) already annotated
						if (debug) {
							System.out.println("Skipped : "+number.getCoveredText()+" / end : "+number.getEnd());
						}
						continue;
					}
					try {
						if (debug) {
							System.out.println("---");
							System.out.println("number : "+number.getCoveredText());
						}
						PRN prn = null;
						// check the next token
						Token unit_ingredient = JCasUtil.selectFollowing(jcas,
								Token.class, number, 1).get(0);
						if (debug) {
							System.out.println("Following token : "+unit_ingredient.getCoveredText()+" / end : "+unit_ingredient.getEnd());
						}
						if (unit_ingredient.getCoveredText().startsWith("(")) {
							// find the next token which is not part of a PRN :
							// expression parenthesee
							prn = JCasUtil.selectFollowing(jcas, PRN.class,
									number, 1).get(0);
							if (debug) {
								System.out.println("PRN found : "+prn.getCoveredText());
							}
							unit_ingredient = JCasUtil.selectFollowing(jcas,
									Token.class, prn, 1).get(0);
							if (debug) {
								System.out.println("Following token : "+unit_ingredient.getCoveredText()+" / end : "+unit_ingredient.getEnd());
							}
						}

						// test if unit_ingredient is a noun or is in unitdatabase
						String posValue = unit_ingredient.getPos()
								.getPosValue();
						if (posValue == null) {
							throw new RuntimeException(
									"Fatal error : POS attribute of Token not initialized !! ");
						} else {
							if (posValue.matches("NN*") || getUnitDatabase().contains(unit_ingredient.getCoveredText()) ) {

								if (getIngredientDatabase().contains(unit_ingredient.getCoveredText().toLowerCase())) {
									// pattern : ( QUANTITY ) _ INGREDIENT
									endLastUnit = setUnitAnnotation(jcas, number,
											number.getEnd(), "feature");
								} else { 
									// high probability for pattern : ( QUANTITY _ UNIT )
									endLastUnit = setUnitAnnotation(jcas, number,
										unit_ingredient, "feature");
								}
								
							} else {
								// pattern : ( QUANTITY ) _ QUALIFIERS

								int end = number.getEnd();
								if (prn != null) {
									end = prn.getEnd(); // cover the PRN ?
								}
								endLastUnit = setUnitAnnotation(jcas, number,
										end, "splitByToken");
							}
							if (debug) {
								System.out.println("endLastUnit : "+endLastUnit);
							}
						}

					} catch (IndexOutOfBoundsException e) {
						System.out.println("[UnitAnnotator] IndexOutOfBoundsException");
						// empty select() calls arrive here

					} // catch

				} // for all CARD in the sentence

			} // for all sentences
		} // in the text giving the list of ingredients
	}

	/**
	 * Create a new UnitAnnotation
	 * @param jcas
	 * @param number
	 * @param token
	 * @param type
	 * @return the index of the end of the created annotation
	 */
	private int setUnitAnnotation(JCas jcas, CARD number, Token token,
			String type) {
		
		int endAnnotation = token.getEnd();
		UnitAnnotation a = new UnitAnnotation(jcas);
		int begin = (number == null) ? token.getBegin() : number.getBegin();
		a.setBegin(begin);
		a.setEnd(endAnnotation);
		a.setTypeOf(type);
		a.setQuantity((number == null) ? "" : number.getCoveredText());
		String unit_ingredient;
		unit_ingredient = token.getLemma().getValue();
		a.setUnit(unit_ingredient);
		a.setUnitToken(token);

		a.addToIndexes();
		return endAnnotation;

	} // setUnitAnnotation()

	/**
	 * Create a new UnitAnnotation
	 * @param jcas
	 * @param number
	 * @param end
	 * @param type
	 * @return the index of the end of the created annotation
	 */
	public int setUnitAnnotation(JCas jcas, CARD number, int end, String type) {
		int endAnnotation = end;
		UnitAnnotation a = new UnitAnnotation(jcas);
		a.setBegin(number.getBegin());
		a.setEnd(endAnnotation);
		a.setTypeOf(type);
		a.setQuantity(number.getCoveredText());
		// a.setUnit(""); -> null
		a.addToIndexes();
		return endAnnotation;
	} // setUnitAnnotation()
	

}
