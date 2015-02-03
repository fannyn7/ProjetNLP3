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

/** 
 * KO : Fix StandfordParser oversights for CARD annotations
 * @author Solene, Killian
 *
 */
public class AmountAnnotator_backup extends JCasAnnotator_ImplBase {

	@Override
	public void process(JCas jcas) throws AnalysisEngineProcessException {
		String document = jcas.getDocumentText();
		
		String amount = "[1-9][0-9]*";//"|[[1-9][0-9]]*[\\s?][1-9]/[1-9])";
		String regex = amount;
		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher(document);

		while (m.find()) {
			// Test if the number is already annotated with CARD
			if (JCasUtil.selectCovered(jcas, CARD.class, m.start(), m.end()).size() != 0) {
				continue;
			}
				
			CARD a = new CARD(jcas);
			a.setBegin(m.start());
			a.setEnd(m.end());
			a.addToIndexes();
			System.out.println("[AmountAnnotator] number found : "+m.group()
					+"\n"+JCasUtil.selectCovered(jcas, Sentence.class, m.start(), m.end()).get(0).getCoveredText()
					+"\n-------------");

		}

	}	
	
}
