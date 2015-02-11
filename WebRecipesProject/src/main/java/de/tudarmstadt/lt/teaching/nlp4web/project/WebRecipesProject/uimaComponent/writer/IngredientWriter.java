package de.tudarmstadt.lt.teaching.nlp4web.project.WebRecipesProject.uimaComponent.writer;

import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.fit.component.JCasConsumer_ImplBase;
import org.apache.uima.fit.util.JCasUtil;
import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.tcas.Annotation;
import org.apache.uima.util.Level;

import de.tudarmstadt.ukp.dkpro.core.api.lexmorph.type.pos.POS;
import de.tudarmstadt.ukp.teaching.general.type.DirectivesAnnotation;
import de.tudarmstadt.ukp.teaching.general.type.IngredientAnnotation;

public class IngredientWriter extends JCasConsumer_ImplBase {

    public static final String LF = System.getProperty("line.separator");

	@Override
	public void process(JCas jcas) throws AnalysisEngineProcessException {
		StringBuilder sb = new StringBuilder();
		sb.append("=== CAS ==="); sb.append(LF);
		sb.append("-- Document Text --"); sb.append(LF);
		sb.append(jcas.getDocumentText()); sb.append(LF);
		sb.append("-- IngredientAnnotation --"); sb.append(LF);

		for (IngredientAnnotation a : JCasUtil.select(jcas, IngredientAnnotation.class)) { 
			sb.append("[" + a.getType().getShortName() + "] ");
			sb.append("(" + a.getBegin() + ", " + a.getEnd() + ") ");
			sb.append("[Amount: "+a.getAmount()+"]");
			sb.append("[NormalizedName: "+a.getNormalizedName()+"] ");
			sb.append(a.getCoveredText());

			sb.append(LF);
		}
		sb.append(LF);

		sb.append("-- DirectivesAnnotation --"); sb.append(LF);
		for (DirectivesAnnotation a : JCasUtil.select(jcas, DirectivesAnnotation.class)) { 
			sb.append("[" + a.getType().getShortName() + "] ");
			sb.append("(" + a.getBegin() + ", " + a.getEnd() + ") ");
			sb.append(a.getInstruction() + " :" + a.getIngredient());
			sb.append(LF);
		}
		sb.append(LF);
		
        getContext().getLogger().log(Level.INFO, sb.toString());
	}
}