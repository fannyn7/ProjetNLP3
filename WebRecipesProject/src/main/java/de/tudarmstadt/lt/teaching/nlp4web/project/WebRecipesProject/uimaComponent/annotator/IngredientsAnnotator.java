package de.tudarmstadt.lt.teaching.nlp4web.project.WebRecipesProject.uimaComponent.annotator;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.FileUtils;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.fit.component.JCasAnnotator_ImplBase;
import org.apache.uima.fit.util.JCasUtil;
import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.tcas.Annotation;

import de.tudarmstadt.ukp.dkpro.core.api.lexmorph.type.pos.CARD;
import de.tudarmstadt.ukp.dkpro.core.api.lexmorph.type.pos.NN;
import de.tudarmstadt.ukp.dkpro.core.api.lexmorph.type.pos.POS;
import de.tudarmstadt.ukp.dkpro.core.api.segmentation.type.Lemma;
import de.tudarmstadt.ukp.dkpro.core.api.segmentation.type.Sentence;
import de.tudarmstadt.ukp.dkpro.core.api.segmentation.type.Token;
import de.tudarmstadt.ukp.dkpro.core.api.syntax.type.constituent.NP;
import de.tudarmstadt.ukp.dkpro.core.api.syntax.type.constituent.PRN;
import de.tudarmstadt.ukp.dkpro.core.api.syntax.type.constituent.VP;
import de.tudarmstadt.ukp.teaching.general.type.IngredientAnnotation;
import de.tudarmstadt.ukp.teaching.general.type.TextIngredients;
import de.tudarmstadt.ukp.teaching.general.type.TextInstructions;
import de.tudarmstadt.ukp.teaching.general.type.UnitAnnotation;

public class IngredientsAnnotator extends JCasAnnotator_ImplBase {

	private static final boolean debug = false;
	
	private List<String> ingredientDatabase;

	public List<String> getIngredientDatabase() {
		return ingredientDatabase;
	}

	private void initializeIngredientDatabase() {
		ingredientDatabase = new ArrayList<String>();
		try {
			String f = FileUtils.readFileToString(new File(
					"src/main/resources/databases/ingredient.txt"));
			String[] lines = f.split("(\r\n|\n)");
			for (String line : lines) {
				if (!line.startsWith("<!--")) {
					// quantity unit word
					ingredientDatabase.add(line.toLowerCase());
				} // else source acknowledgement
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private List<String> unitDatabase;

	public List<String> getUnitDatabase() {
		return unitDatabase;
	}

	private void initializeUnitDatabase() {
		unitDatabase = new ArrayList<String>();
		try {
			String f = FileUtils.readFileToString(new File(
					"src/main/resources/databases/quantityUnit.txt"));
			String[] lines = f.split("(\r\n|\n)");
			for (String line : lines) {
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
		// String NOMBRE = "[[1-9][0-9]*]"; //| [0-9].[0-9]* | y \in
		// Numbers_in_letters
		// String FRACTION = "[[1-9]/[1-9]]";// | y \in Fraction_in_letters
		// String UNIT = y \in Measure_unit
		// String QUANTITY = FRACTION+"|"+NOMBRE+"|["+NOMBRE+" "+FRACTION+"]";//
		// | y \in Other_quantities

		// String REGEX1 = QUANTITY+" "+UNIT+" "+QUALIFIERS+" "+INGREDIENT;
		// String REGEX2 = QUANTITY+" "+QUALIFIERS+" "+INGREDIENT;
		// String REGEX3 = UNIT+" "+QUALIFIERS+" "+INGREDIENT;
		// String REGEX4 = QUALIFIERS+" "+INGREDIENT;
		// String REGEX5 = INGREDIENT+" "+QUALIFIERS;

		// Initialize databases
		initializeIngredientDatabase();
		initializeUnitDatabase();

		TextInstructions instructions = new TextInstructions(jcas);
		for (TextInstructions text : JCasUtil
				.select(jcas, TextInstructions.class)) {
			// Should be 0 or 1
			instructions = text;
		}
		
		// we are processing the Ingredient patterns sentence by sentence
		for (TextIngredients text : JCasUtil
				.select(jcas, TextIngredients.class))
			for (Sentence sentence : JCasUtil.selectCovered(jcas,
					Sentence.class, text)) {
				List<UnitAnnotation> lquantities = JCasUtil.selectCovered(jcas,
						UnitAnnotation.class, sentence);
				
				if ( (lquantities == null) || lquantities.isEmpty() ) {
					// pattern : QUANTITY -> ^ &&  UNIT -> ^
					// ie QUALIFIERS* INGREDIENT
					// use rote learning technique
					List<Token> tokens = JCasUtil.selectCovered(jcas,
							Token.class, sentence);
					boolean found = false;
					int j = 0;
					while (j < tokens.size()) {
						if (debug) {
							System.out.println("[MODIF] POS : "+tokens.get(j).getPos().getPosValue()+" "+tokens.get(j).getCoveredText());
						}
						found = getIngredientDatabase().contains(
								tokens.get(j).getCoveredText());
						if (found) {
							Annotation ingredient = tokens.get(j);
							setIngredientAnnotation(jcas, ingredient, null);
							if (debug) {
								System.out.println("[MODIF] Found : POS : "+((Token)ingredient).getPos().getPosValue()+" "
										+((Token)ingredient).getCoveredText());
							}
							found = false;
						}
						j++;
						
					}
				}
				// for all UnitAnnotation in this sentence
				for (UnitAnnotation quantity : lquantities) {
					try { 
					
						Annotation ingredient = null;
						int beginSearch;
						// Look in priority for NP : given up
						/****************************************/
						/*
						// don't consider the
						// expressions
						// in brackets in the Unit Annotation
						
						List<PRN> prns = JCasUtil.selectCovered(jcas, PRN.class,
								quantity);
						if (prns.size() > 0) {
							beginSearch = prns.get(prns.size() - 1).getEnd();
						} else {
							beginSearch = quantity.getEnd();
						}
						
						
						
						List<NP> nps = JCasUtil.selectCovered(jcas, NP.class,
								beginSearch, sentence.getEnd());
						if (!nps.isEmpty()) {
							NP np = nps.get(0);
							ingredient = checkNP(jcas, np, quantity.getEnd() + 1,
									quantity, sentence);
						}
						*/
						/***************************************/
						beginSearch = quantity.getEnd(); // new after giving up code above
					if (ingredient == null) {
						// use Joker : database!
						List<Token> tokens = JCasUtil.selectCovered(jcas,
								Token.class, beginSearch, sentence.getEnd());
						boolean found = false;
						int j = 0;
						while (!found && (j < tokens.size())) {
							if (debug) {
								System.out.println("[MODIF] POS : "+tokens.get(j).getPos().getPosValue()+" "
										+tokens.get(j).getCoveredText());
							}
							found = getIngredientDatabase().contains(
									tokens.get(j).getCoveredText()) || 
									tokens.get(j).getPos().getPosValue().matches("NN*");
							j++;
						}

						if (found) {
							ingredient = tokens.get(j - 1);
							if (debug) {
								System.out.println("[MODIF] Found : POS : "+((Token)ingredient).getPos().getPosValue()+" "+((Token)ingredient).getCoveredText());
							}
						} else {
							// get the unit from the UnitAnnotation
							// TODO : quantity unit should have type annotation
							// in UnitAnnotation
							/* old : version checkNP
							List<Token> l = JCasUtil.selectCovered(jcas,
									Token.class, quantity.getBegin(),
									quantity.getEnd());
							Annotation quantityUnit = l.get(l.size() - 1);
							*/ 
							Annotation quantityUnit = quantity.getUnitToken();
							
							if ( (quantityUnit == null) 
									// UNIT -> ^
									// ie pattern : (QUANTITY)?  QUALIFIERS* INGREDIENT QUALIFIERS*
								||
									getUnitDatabase().contains(quantityUnit.getCoveredText()))
								// pattern : ( QUANTITY? UNIT ) QUALIFIERS* INGREDIENT QUALIFIERS* 
								 
							 {
								// TODO Look for words of sentence than also occur in the instructions
								
								if (ingredient==null) {
									// pick the last word because 
									// pattern : ( QUANTITY? UNIT ) QUALIFIERS* INGREDIENT 
									// is more often than
									// pattern : ( QUANTITY? UNIT ) QUALIFIERS* INGREDIENT QUALIFIERS+
									ingredient = tokens.get(tokens.size() - 2); // skip the point
							  }
							
							} else {
								// else certainly false unit classification
								// so roll back :
								// remove it from text covered by the
								// UnitAnnotation
								// and set it as ingredient
								if (debug) {
									System.out.println("quantity unit removed : "
										+ quantityUnit.getCoveredText());
								}
								ingredient = quantityUnit;
								quantity.setUnit(null);
								quantity.setUnitToken(null);
								quantity.setEnd(ingredient.getBegin() - 1);

							}
						}

					}// 

					setIngredientAnnotation(jcas, ingredient, quantity);
					} catch (IndexOutOfBoundsException e) {
						System.out.println("[IngredientsAnnotator] IndexOutOfBoundsException"); // empty  select() calls arrive here
					} // catch
					
					if (debug) {
						System.out.println("---------");
					}
					
				} // for all UnitAnnotation in the sentence

			} // for all sentences

	}
	


	/**
	 * Look for the NN that correspond to the ingredient associate to a quantity
	 * 
	 * @param jcas
	 * @param np
	 *            chunk where to look
	 * @param searchAreaStart
	 * @param quantity
	 */
	private NN checkNP(JCas jcas, NP np, int searchAreaStart,
			UnitAnnotation quantity, Sentence sentence) {
		if (debug) {
			System.out.println("NP ingredient : " + np.getCoveredText());
		}

		{
			// get the list of NN between the quantity and the end of the
			// chunk
			List<NN> nouns = JCasUtil.selectCovered(jcas, NN.class,
					searchAreaStart, np.getEnd());
			int nbNN = nouns.size();
			if (debug) {
				System.out.println("nouns.size() " + quantity.getCoveredText()
						+ " = " + nbNN);
			}
			if (nbNN == 0) {
				// !! maybe mistake in unit of UnitAnnotation (e.g. 2 potatoes
				// -> [2][potato] instead of [2][null]

				// look in NP just after the current one
				NP nextNP = JCasUtil.selectFollowing(jcas, NP.class, np, 1)
						.get(0);
				// if there is a CARD/UnitAnnotation between quantity and nextNP
				// then roll back
				List<UnitAnnotation> units = JCasUtil.selectCovered(jcas,
						UnitAnnotation.class, quantity.getEnd(),
						nextNP.getEnd());
				if (units.size() > 0) {
					if (debug) {
						System.out.println("units.size() > 0 "
								+ quantity.getCoveredText() + "<>"
								+ nextNP.getCoveredText());
					}
					return null;
				}
				// if nextNP is outside of the quantity sentence
				// then roll back
				if (nextNP.getEnd() > sentence.getEnd()) {
					if (debug) {
						System.out.println("nextNP out of sentence "
								+ quantity.getCoveredText() + "<>"
								+ nextNP.getCoveredText());
					}
					return null;
				}
				// else check the NP found
				return checkNP(jcas, nextNP, nextNP.getBegin(), quantity,
						sentence);
			} else if (nbNN == 1) {
				return nouns.get(0);
			} else {
				// many NN
				// 1. pick the last one
				return nouns.get(nbNN - 1);
			}
		}
	}

	private void setIngredientAnnotation(JCas jcas, Annotation ingredient,
			UnitAnnotation quantity) {
		/*
		 * create a new IngredientAnnotation
		 */
		IngredientAnnotation a = new IngredientAnnotation(jcas);
		// get the lemmata
		try {

			Lemma lemma = JCasUtil.selectCovered(jcas, Lemma.class,
					ingredient.getBegin(), ingredient.getEnd()).get(0);

			a.setBegin(ingredient.getBegin());
			a.setEnd(ingredient.getEnd());
			a.setAmount( (quantity == null)? null : quantity.getCoveredText());
			a.setNormalizedName(lemma.getValue());
			a.addToIndexes();
		} catch (IndexOutOfBoundsException e) {
			System.out.println("IndexOutOfBoundsException : " + ingredient);
		}

	} // setIngredientAnnotation()

}