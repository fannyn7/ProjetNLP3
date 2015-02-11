package de.tudarmstadt.lt.teaching.nlp4web.project.WebRecipesProject.uimaComponent.writer;

import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.fit.component.JCasConsumer_ImplBase;
import org.apache.uima.fit.util.JCasUtil;
import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.tcas.Annotation;
import org.apache.uima.util.Level;

import de.tudarmstadt.lt.teaching.nlp4web.project.WebRecipesProject.uimaComponent.ExtractionPipeline;
import de.tudarmstadt.ukp.dkpro.core.api.lexmorph.type.pos.POS;
import de.tudarmstadt.ukp.teaching.general.type.DirectivesAnnotation;
import de.tudarmstadt.ukp.teaching.general.type.IngredientAnnotation;
import de.tudarmstadt.ukp.teaching.general.type.UnitAnnotation;

public class UnitWriter extends JCasConsumer_ImplBase {

    public static final String LF = System.getProperty("line.separator");

	@Override
	public void process(JCas jcas) throws AnalysisEngineProcessException {
		StringBuilder sb = new StringBuilder();
		sb.append("=== CAS ==="); sb.append(LF);
		sb.append("-- Document Text --"); sb.append(LF);
		sb.append(jcas.getDocumentText()); sb.append(LF);
		sb.append("-- UnitAnnotation --"); sb.append(LF);

		try {
		for (UnitAnnotation a : JCasUtil.select(jcas, UnitAnnotation.class)) {
			sb.append("[" + a.getType().getShortName() + "] ");
			sb.append("(" + a.getBegin() + ", " + a.getEnd() + ") ");
			sb.append("[Quantity: "+a.getQuantity()+"]");
			sb.append("[Unit: "+a.getUnit()+"]");
			sb.append("[TypeOf: "+a.getTypeOf()+"] ");
			sb.append(a.getCoveredText());
			sb.append(LF);
		}
		sb.append(LF);
		
        getContext().getLogger().log(Level.INFO, sb.toString());
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("[Error in UnitWriter] sb = "+sb);
			throw new RuntimeException("Stop");
		}
	}
}