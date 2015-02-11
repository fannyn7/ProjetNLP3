package de.tudarmstadt.lt.teaching.nlp4web.project.WebRecipesProject.uimaComponent.annotator;

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
import de.tudarmstadt.ukp.dkpro.core.api.syntax.type.constituent.PRN;
import de.tudarmstadt.ukp.teaching.general.type.TextIngredients;

/** 
 * KO : Fix StandfordParser oversights for CARD annotations
 * @author Solene, Killian
 *
 */
public class PRNAnnotator extends JCasAnnotator_ImplBase {

	public static final boolean debug = false;
	
	@Override
	public void process(JCas jcas) throws AnalysisEngineProcessException {
		String prn = "\\([^(]*\\)";
		String regex = prn;
		Pattern p = Pattern.compile(regex);
		
		
		for (TextIngredients text : JCasUtil.select(jcas, TextIngredients.class)) {
			for (Sentence sentence : JCasUtil.selectCovered(jcas, Sentence.class, text)) {
				int begin = sentence.getBegin();
				Matcher m = p.matcher(sentence.getCoveredText());
				while(m.find()) {
					String expr = m.group(0);
					// Test if it is already annotated with PRN
					if (JCasUtil.selectCovered(jcas, PRN.class, m.start()+begin, m.end()+begin).size() > 0) {
						if (debug) {
							System.out.println("PRN already annotated : "+ expr);
						}
					} else {
						// Create the PRN annotation
						PRN a = new PRN(jcas);
						a.setBegin(m.start()+begin);
						a.setEnd(m.end()+begin);
						a.addToIndexes();
						if (debug) {
						System.out.println("[PRNAnnotator] expression in brackets found : "
										+ expr
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
