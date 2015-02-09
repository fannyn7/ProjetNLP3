package de.tudarmstadt.lt.teaching.nlp4web.project.WebRecipesProject.annotator;

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
				if (!line.startsWith("<--!")) {
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
		// String NOMBRE = "[[1-9][0-9]*]"; //| [0-9].[0-9]* | y \in
		// Numbers_in_letters
		// String FRACTION = "[[1-9]/[1-9]]";// | y \in Fraction_in_letters
		// String UNIT = y \in Measure_unit | y \in Measure_tools
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

				// for all UnitAnnotation in this sentence
				for (UnitAnnotation quantity : JCasUtil.selectCovered(jcas,
						UnitAnnotation.class, sentence)) {
					// if (JCasUtil.selectCovered(UnitAnnotation.class,
					// quantity).size() > 0) {
					// continue;
					// }
					/* try { */
					// check the covered NP (noun chunk) but don't consider the
					// expressions
					// in brackets
					int beginSearch;
					List<PRN> prns = JCasUtil.selectCovered(jcas, PRN.class,
							quantity);
					if (prns.size() > 0) {
						beginSearch = prns.get(prns.size() - 1).getEnd();
					} else {
						beginSearch = quantity.getBegin();
					}
					List<NP> nps = JCasUtil.selectCovered(jcas, NP.class,
							beginSearch, sentence.getEnd());
					Annotation ingredient = null;
					if (!nps.isEmpty()) {
						NP np = nps.get(0);
						ingredient = checkNP(jcas, np, quantity.getEnd() + 1,
								quantity, sentence);
					}
					// PARTIE SENSIBLE
					if (ingredient == null) {
						// use Joker : database!
						List<Token> tokens = JCasUtil.selectCovered(jcas,
								Token.class, beginSearch, sentence.getEnd());
						boolean found = false;
						int j = 0;
						while (!found && (j < tokens.size())) {
							found = getIngredientDatabase().contains(
									tokens.get(j).getCoveredText());
							j++;
						}

						if (found) {
							ingredient = tokens.get(j - 1);
						} else {
							// get the unit from the UnitAnnotation
							// TODO : quantity unit should have type annotation
							// in UnitAnnotation
							List<Token> l = JCasUtil.selectCovered(jcas,
									Token.class, quantity.getBegin(),
									quantity.getEnd());
							Annotation quantityUnit = l.get(l.size() - 1); // skip
																			// the
																			// point
							// if the current unit quantity is in the
							// unitDatabase
							if (getUnitDatabase().contains(
									quantityUnit.getCoveredText())) {
								// TODO  then : ?? for now, take the last token of the
								// sentence
								
								List<Token> tokens_ing = JCasUtil.selectCovered(jcas,
										Token.class, quantity.getBegin(),sentence.getEnd() );
								
								List<Token> tokens_ins = JCasUtil.selectCovered(jcas,
										Token.class, instructions.getBegin(),instructions.getEnd() );
								 
							if (!(tokens_ing == null) && !(tokens_ins == null)){
								
								for (Token a : tokens_ing) {
									for (Token b : tokens_ins) {
										if (a.getCoveredText().equals(b.getCoveredText()) && a.getCoveredText().length()>2){
											
											ingredient = tokens.get(a.getBegin() );
											
										}
									}
								}
							
							}
								
								
							if (ingredient==null) {
								
							
							 ingredient = tokens.get(tokens.size() - 2); // skip
																		// the
																			// point
							}
							} else {
								// else roll back :
								// remove it from text covered by the
								// UnitAnnotation
								// and set it as ingredient
								System.out.println("quantity unit removed : "
										+ quantityUnit.getCoveredText());
								quantity.setUnit(null);
								ingredient = quantityUnit;
								quantity.setEnd(ingredient.getBegin() - 1);

							}
						}

					}// PARTIE SENSIBLE . FIN

					setIngredientAnnotation(jcas, ingredient, quantity);
					/*
					 * } catch (IndexOutOfBoundsException e) {
					 * System.out.println("IndexOutOfBoundsException"); // empty
					 * select() calls arrive here
					 * 
					 * } // catch
					 */
					System.out.println("---------");
				} // for all noun chunks in the sentence

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
		System.out.println("NP ingredient : " + np.getCoveredText());

		{
			// get the list of NN between the quantity and the end of the
			// chunk
			List<NN> nouns = JCasUtil.selectCovered(jcas, NN.class,
					searchAreaStart, np.getEnd());
			int nbNN = nouns.size();
			System.out.println("nouns.size() " + quantity.getCoveredText()
					+ " = " + nbNN);
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
					System.out.println("units.size() > 0 "
							+ quantity.getCoveredText() + "<>"
							+ nextNP.getCoveredText());
					return null;
				}
				// if nextNP is outside of the quantity sentence
				// then roll back
				if (nextNP.getEnd() > sentence.getEnd()) {
					System.out.println("nextNP out of sentence "
							+ quantity.getCoveredText() + "<>"
							+ nextNP.getCoveredText());
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
			a.setAmount(quantity.getCoveredText());
			a.setNormalizedName(lemma.getValue());
			a.addToIndexes();
		} catch (IndexOutOfBoundsException e) {
			System.out.println("IndexOutOfBoundsException : " + ingredient);
		}

	} // setIngredientAnnotation()

}