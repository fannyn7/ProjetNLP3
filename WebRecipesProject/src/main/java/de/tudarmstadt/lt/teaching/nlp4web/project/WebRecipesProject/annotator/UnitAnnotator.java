package de.tudarmstadt.lt.teaching.nlp4web.project.WebRecipesProject.annotator;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.fit.component.JCasAnnotator_ImplBase;
import org.apache.uima.fit.util.JCasUtil;
import org.apache.uima.jcas.JCas;

import de.tudarmstadt.ukp.dkpro.core.api.lexmorph.type.pos.CARD;
import de.tudarmstadt.ukp.dkpro.core.api.lexmorph.type.pos.N;
import de.tudarmstadt.ukp.dkpro.core.api.lexmorph.type.pos.NN;
import de.tudarmstadt.ukp.dkpro.core.api.segmentation.type.Lemma;
import de.tudarmstadt.ukp.dkpro.core.api.segmentation.type.Sentence;
import de.tudarmstadt.ukp.dkpro.core.api.segmentation.type.Token;
import de.tudarmstadt.ukp.dkpro.core.api.syntax.type.chunk.NC;
import de.tudarmstadt.ukp.dkpro.core.api.syntax.type.constituent.NP;
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

	private List<String> unitDatabase;

	public List<String> getUnitDatabase(){
		return unitDatabase;	
	}

	private void initializeUnitDatabase() {
		unitDatabase = new ArrayList<String>();
		try {
			String f = FileUtils.readFileToString(new File("src/main/resources/databases/quantityUnit.txt"));
			String[] lines = f.split("(\r\n|\n)");
			for (String line : lines){
				if (!line.startsWith("<--!")) {
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

		initializeUnitDatabase();
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
				// for all cardinal number in this sentence
				for (CARD number : JCasUtil.selectCovered(jcas, CARD.class,
						sentence)) {
					if (number.getEnd() <= endLastUnit) { //JCasUtil.selectCovered(CARD.class, number).size() > 0) {
						// this number is included in a quantity (unit) already annotated
						System.out.println("Skipped : "+number.getCoveredText()+" / end : "+number.getEnd());
						continue;
					}
					try {
						System.out.println("---");
						System.out.println("number : "+number.getCoveredText());
						PRN prn = null;
						// check the next token
						Token unit_ingredient = JCasUtil.selectFollowing(jcas,
								Token.class, number, 1).get(0);
						System.out.println("Following token : "+unit_ingredient.getCoveredText()+" / end : "+unit_ingredient.getEnd());
						if (unit_ingredient.getCoveredText().startsWith("(")) {
							// find the next token which is not part of a PRN :
							// expression parenthesee
							prn = JCasUtil.selectFollowing(jcas, PRN.class,
									number, 1).get(0);
							System.out.println("PRN found : "+prn.getCoveredText());
							unit_ingredient = JCasUtil.selectFollowing(jcas,
									Token.class, prn, 1).get(0);
							System.out.println("Following token : "+unit_ingredient.getCoveredText()+" / end : "+unit_ingredient.getEnd());
						}

						// test that unit_ingredient is a noun
						String posValue = unit_ingredient.getPos()
								.getPosValue();
						// System.out.println("POS tag : "+ posValue);
						if (posValue == null) {
							throw new RuntimeException(
									"Fatal error : POS attribute of Token not initialized !! ");
						} else {
							if (posValue.equals("NN") || posValue.equals("NNS") || getUnitDatabase().contains(unit_ingredient.getCoveredText()) ) {

								// pattern : ( QUANTITY _ UNIT )
								// pattern : ( QUANTITY ) _ INGREDIENT
								endLastUnit = setUnitAnnotation(jcas, number,
										unit_ingredient, "splitByToken");
							} else {
								// pattern : ( QUANTITY ) _ QUALIFIERS

								int end = number.getEnd();
								if (prn != null) {
									end = prn.getEnd(); // cover the PRN ?
								}
								endLastUnit = setUnitAnnotation(jcas, number,
										end, "splitByToken");
							}
							System.out.println("endLastUnit : "+endLastUnit);
						}

					} catch (IndexOutOfBoundsException e) {
						System.out.println("IndexOutOfBoundsException");
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
		a.setBegin(number.getBegin());
		a.setEnd(endAnnotation);
		a.setTypeOf(type);
		a.setQuantity(number.getCoveredText());
		String unit_ingredient;
		// test that unit_ingredient is a noun
		if (JCasUtil.selectCovered(jcas, NN.class, token.getBegin(),
				token.getEnd()).size() > 0) {
			unit_ingredient = token.getLemma().getValue();
		} else {
			unit_ingredient = "";
			// except if token.getCoveredText \in {can, ...}
		}
		a.setUnit(unit_ingredient);

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
