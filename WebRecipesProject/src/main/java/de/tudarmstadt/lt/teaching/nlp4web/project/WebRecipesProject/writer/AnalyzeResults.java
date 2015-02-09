package de.tudarmstadt.lt.teaching.nlp4web.project.WebRecipesProject.writer;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import org.apache.uima.UIMAFramework;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.fit.component.JCasAnnotator_ImplBase;
import org.apache.uima.fit.descriptor.ConfigurationParameter;
import org.apache.uima.fit.util.JCasUtil;
import org.apache.uima.jcas.JCas;
import org.apache.uima.util.Level;
import org.apache.uima.util.Logger;

import de.tudarmstadt.ukp.teaching.general.type.DirectivesAnnotation;

public class AnalyzeResults extends JCasAnnotator_ImplBase{

	public static final String PARAM_INPUT_FILE = "InputFile";
	public static final String PARAM_URL = "URL";


	@ConfigurationParameter(name = PARAM_INPUT_FILE, mandatory = true)
	private String inputFile;
	@ConfigurationParameter(name = PARAM_URL, mandatory = true)
	private String recipeLink;

	Logger logger = UIMAFramework.getLogger(AnalyzeResults.class);

	public static final String LF = System.getProperty("line.separator");

	@Override
	public void process(JCas jcas) throws AnalysisEngineProcessException {
		try {
			logger.log(Level.INFO, "Start analyzing results");
			StringBuilder sb = new StringBuilder();


			String line;
			BufferedReader reader = new BufferedReader(new FileReader(inputFile));
			int correct = 0;
			ArrayList<String> actionsList = new ArrayList<String>(); 
			HashSet<String> realActionsSet = new HashSet<String>();

			while(!(line = reader.readLine()).equals(recipeLink)) ;
			System.out.println("LINK : " + line );
			reader.readLine(); // empty line
			line = reader.readLine(); // actions
			String[] actions = line.split("\\s");
			for (int i = 0; i<actions.length;i++) {
				actionsList.add(actions[i]);
				realActionsSet.add(actions[i].toLowerCase());
			}
			System.out.println("ACTIONS : " + actionsList.toString());
			//HashSet<String> actionsSet = new HashSet<String>();


			for (DirectivesAnnotation a : JCasUtil.select(jcas, DirectivesAnnotation.class)){
				if (realActionsSet.contains(a.getInstruction())){
					correct++;
				} 
			}
			System.out.println("Correctly found actions : " + correct + " out of " + realActionsSet.size());


			int indiceList = 0;
			for (DirectivesAnnotation a : JCasUtil.select(jcas, DirectivesAnnotation.class)) { 
				if (a.getInstruction()!=null){
					int currentIndice = indiceList;
					//System.out.println("annotation : " +a.getInstruction());
					while(!(currentIndice == actionsList.size() || (a.getInstruction().equals(actionsList.get(currentIndice))))){
						//System.out.println("currentIndice " + currentIndice + "  " + actionsList.get(currentIndice));
						currentIndice++;
					}
					if (currentIndice == actionsList.size()) System.out.println("Action " + a.getInstruction() + " doesn't belong to actionsList" );					
				}

				//sb.append(LF);
			}



			reader.close();
			getContext().getLogger().log(Level.INFO, sb.toString());
		} catch (FileNotFoundException e) {
			logger.log(Level.WARNING, e.getMessage());
		} catch (IOException e) {
			logger.log(Level.WARNING, e.getMessage());
		}
	}


}
