package de.tudarmstadt.lt.teaching.nlp4web.project.WebRecipesProject.annotator;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
import de.tudarmstadt.ukp.teaching.general.type.UnitAnnotation;

public class IngredientsAnnotator extends JCasAnnotator_ImplBase{
 
	@Override
	public void process(JCas jcas) throws AnalysisEngineProcessException {
//        String NOMBRE = "[[1-9][0-9]*]"; //| [0-9].[0-9]* | y \in Numbers_in_letters
//        String FRACTION = "[[1-9]/[1-9]]";// | y \in Fraction_in_letters
//        String UNIT = y \in Measure_unit | y \in Measure_tools
//        String QUANTITY = FRACTION+"|"+NOMBRE+"|["+NOMBRE+" "+FRACTION+"]";// | y \in Other_quantities
        
//        String REGEX1 = QUANTITY+" "+UNIT+" "+QUALIFIERS+" "+INGREDIENT;
//        String REGEX2 = QUANTITY+" "+QUALIFIERS+" "+INGREDIENT; 
//        String REGEX3 = UNIT+" "+QUALIFIERS+" "+INGREDIENT;
//        String REGEX4 = QUALIFIERS+" "+INGREDIENT;
//        String REGEX5 = INGREDIENT+" "+QUALIFIERS;
        
		// we are processing the Ingredient patterns sentence by sentence
		for (TextIngredients text : JCasUtil.select(jcas, TextIngredients.class))
		for (Sentence sentence : JCasUtil.selectCovered(jcas, Sentence.class, text)) {

			// for all UnitAnnotation in this sentence
			for (UnitAnnotation quantity : JCasUtil.selectCovered(jcas, UnitAnnotation.class,
					sentence)) {
//				if (JCasUtil.selectCovered(UnitAnnotation.class, quantity).size() > 0) {
//					continue;
//				}
				try {
					// check the covered NP (noun chunk) but don't consider the expressions
					// in brackets
					int beginSearch;
					List<PRN> prns = JCasUtil.selectCovered(jcas, PRN.class,
							quantity);
					if (prns.size() > 0 ) {
						beginSearch = prns.get(prns.size()-1).getEnd();
					} else {
						beginSearch = quantity.getBegin();
					}
					List<NP> nps = JCasUtil.selectCovered(jcas, NP.class,
							beginSearch, sentence.getEnd());
					NP np = nps.get(0);
					NN ingredient = checkNP(jcas, np, quantity.getEnd()+1, quantity, sentence);
					setIngredientAnnotation(jcas, ingredient, quantity);
				} catch (IndexOutOfBoundsException e) {
					System.out.println("IndexOutOfBoundsException");
					// empty select() calls arrive here

				} // catch

				System.out.println("---------");
			} // for all noun chunks in the sentence

		} // for all sentences

	}
	
	/**
	 * Look for the NN that correspond to the ingredient associate to a quantity
	 * @param jcas
	 * @param np chunk where to look
	 * @param searchAreaStart
	 * @param quantity 
	 */
	private NN checkNP(JCas jcas, NP np, int searchAreaStart, UnitAnnotation quantity, Sentence sentence) {
		System.out.println("NP ingredient : "
				+ np.getCoveredText());

		{
			// get the list of NN between the quantity and the end of the
			// chunk
			List<NN> nouns = JCasUtil.selectCovered(jcas,
					NN.class, searchAreaStart,
					np.getEnd());
			int nbNN = nouns.size(); 
			System.out.println("nouns.size() "+quantity.getCoveredText()+" = "+nbNN);
			if (nbNN == 0) {
				// !! maybe mistake in unit of UnitAnnotation (e.g. 2 potatoes -> [2][potato] instead of [2][null]
				
				// look in NP just after the current one
				NP nextNP = JCasUtil.selectFollowing(jcas,
						NP.class, np, 1).get(0);
				// if there is a CARD/UnitAnnotation between quantity and nextNP then roll back
				List<UnitAnnotation> units = JCasUtil.selectCovered(jcas,
						UnitAnnotation.class, quantity.getEnd(),
						nextNP.getEnd());
				if (units.size() > 0) {
					System.out.println("units.size() > 0 "+quantity.getCoveredText()+"<>"+nextNP.getCoveredText());
					return null;
				}
				// if nextNP is outside of the quantity sentence then roll back
				if (nextNP.getEnd() > sentence.getEnd()) {
					System.out.println("nextNP out of sentence "+quantity.getCoveredText()+"<>"+nextNP.getCoveredText());
					return null;
				}
				// else check the NP found
				return checkNP(jcas, nextNP, nextNP.getBegin(), quantity, sentence);
			} else  if (nbNN == 1) {
				return nouns.get(0);
			} else {
				// many NN
				// 1. pick the last one
				return nouns.get(nbNN - 1);
			}
		}
	}

	private void setIngredientAnnotation(JCas jcas, NN ingredient, UnitAnnotation quantity) {
		/*
		 * create a new IngredientAnnotation
		 */
		IngredientAnnotation a = new IngredientAnnotation(jcas);
		if (ingredient == null) {
			// roll back
			// get the wrong unit from the UnitAnnotation 
			ingredient = JCasUtil.selectCovered(jcas, NN.class,
								quantity.getBegin(), quantity.getEnd()).get(0);
			// remove it from text covered by the UnitAnnotation
			System.out.println("NN removed : "+ingredient.getCoveredText());
			quantity.setUnit(null);
			quantity.setEnd(ingredient.getBegin() - 1);
		} 
		// get the lemmata
		Lemma lemma = JCasUtil.selectCovered(jcas, Lemma.class,
				ingredient.getBegin(), ingredient.getEnd()).get(0);
		a.setBegin(ingredient.getBegin());
		a.setEnd(ingredient.getEnd());
		a.setAmount(quantity.getCoveredText());
		a.setNormalizedName(lemma.getValue());
		a.addToIndexes();

	} // setIngredientAnnotation()
		
}