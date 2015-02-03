package de.tudarmstadt.lt.teaching.nlp4web.project.WebRecipesProject.annotator;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.fit.component.JCasAnnotator_ImplBase;
import org.apache.uima.fit.util.JCasUtil;
import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.tcas.Annotation;

import de.tudarmstadt.ukp.dkpro.core.api.lexmorph.type.pos.CARD;
import de.tudarmstadt.ukp.dkpro.core.api.segmentation.type.Sentence;
import de.tudarmstadt.ukp.dkpro.core.api.segmentation.type.Token;
import de.tudarmstadt.ukp.teaching.general.type.TextIngredients;

/** 
 * KO : Fix StandfordParser oversights for CARD annotations
 * @author Solene, Killian
 *
 */
public class AmountAnnotator extends JCasAnnotator_ImplBase {

	@Override
	public void process(JCas jcas) throws AnalysisEngineProcessException {
		String document = jcas.getDocumentText();
		
		String amount = "[1-9][0-9]*";//"|[[1-9][0-9]]*[\\s?][1-9]/[1-9])";
		String regex = amount;
		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher(document);
		
		for (TextIngredients text : JCasUtil.select(jcas, TextIngredients.class)) {
			for (Sentence sentence : JCasUtil.selectCovered(jcas, Sentence.class, text)) {
				for (Token t : JCasUtil.selectCovered(jcas, Token.class, sentence)) {
					// Test if is a number
					if (t.getCoveredText().matches(regex)) {
						System.out.println("POS : "+t.getPos().getClass().getName());
						// Test if the number is already annotated with CARD
						if (! (t.getPos() instanceof CARD) ) { //JCasUtil.selectCovered(jcas, CARD.class, t.getBegin(), t.getEnd()).size() == 0) {
							// Create the CARD annotation
							CARD a = new CARD(jcas);
							a.setBegin(t.getBegin());
							a.setEnd(t.getEnd());
							a.addToIndexes();
							System.out.println("[AmountAnnotator] number found : "
											+ t.getCoveredText()
											+ "\n"
											+ sentence.getCoveredText()
											+ "\n-------------");
						}
					}
				}
			}

		}

	}

}
