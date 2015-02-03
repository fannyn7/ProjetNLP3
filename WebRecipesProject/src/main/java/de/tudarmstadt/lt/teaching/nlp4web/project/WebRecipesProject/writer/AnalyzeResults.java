package de.tudarmstadt.lt.teaching.nlp4web.project.WebRecipesProject.writer;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;

import org.apache.uima.UIMAFramework;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.fit.component.JCasAnnotator_ImplBase;
import org.apache.uima.fit.descriptor.ConfigurationParameter;
import org.apache.uima.jcas.JCas;
import org.apache.uima.util.Level;
import org.apache.uima.util.Logger;

public class AnalyzeResults extends JCasAnnotator_ImplBase{

	public static final String PARAM_INPUT_FILE = "InputFile";
	public static final String PARAM_URL = "URL";

	
	@ConfigurationParameter(name = PARAM_INPUT_FILE, mandatory = true)
	private String inputFile;
	@ConfigurationParameter(name = PARAM_URL, mandatory = true)
	private String recipeLink;
	
	Logger logger = UIMAFramework.getLogger(AnalyzeResults.class);

	private class Classification {
		int fp = 0;
		int tp = 0;
		int fn = 0;
	}
	
	
	@Override
	public void process(JCas jCas) throws AnalysisEngineProcessException {
		try {
			logger.log(Level.INFO, "Start analyzing results");

			HashMap<String, Classification> map = new HashMap<String, Classification>();
			
			
			String line;
			String[] splitLine;
			BufferedReader reader = new BufferedReader(new FileReader(inputFile));
			int correct = 0;

			while(!(line = reader.readLine()).equals(recipeLink)) ;
			System.out.println("LINK : " + line );

			

		} catch (FileNotFoundException e) {
			logger.log(Level.WARNING, e.getMessage());
		} catch (IOException e) {
			logger.log(Level.WARNING, e.getMessage());
		}
	}

	
}
