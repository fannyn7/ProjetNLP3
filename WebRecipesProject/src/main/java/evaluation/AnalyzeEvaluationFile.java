package evaluation;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.uima.UIMAFramework;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.fit.component.JCasAnnotator_ImplBase;
import org.apache.uima.fit.descriptor.ConfigurationParameter;
import org.apache.uima.fit.util.JCasUtil;
import org.apache.uima.jcas.JCas;
import org.apache.uima.util.Level;
import org.apache.uima.util.Logger;

import de.tudarmstadt.lt.teaching.nlp4web.project.WebRecipesProject.writer.AnalyzeResults;
import de.tudarmstadt.ukp.teaching.general.type.DirectivesAnnotation;

public class AnalyzeEvaluationFile extends JCasAnnotator_ImplBase{

	public static final String PARAM_INPUT_FILE = "InputFile";

	@ConfigurationParameter(name = PARAM_INPUT_FILE, mandatory = true)
	private String fileToWrite;
	
	Logger logger = UIMAFramework.getLogger(AnalyzeResults.class);

	@Override
	public void process(JCas jcas) throws AnalysisEngineProcessException {



		StringBuilder sb = new StringBuilder();


		String line;
		BufferedReader reader;
		try {
			reader = new BufferedReader(new FileReader(fileToWrite));

		Pattern p = Pattern.compile("[0-9][0-9] [0-9][0-9]*");
		/*Matcher matcher;
			while(!(matcher = p.matcher(line = reader.readLine())).find(0));
		 */
		int corrects = 0;
		int nbRecipes = 0;
		int totalActions = 0;

		while (!(line = reader.readLine()).equals("")){
			System.out.println("ligne : " + line);
			nbRecipes++;
			String[] numbers = line.split("\\s");
			corrects += Integer.parseInt(numbers[0]);
			totalActions += Integer.parseInt(numbers[1]);
		}
		System.out.println(nbRecipes + " recipes");

			reader.close();
			getContext().getLogger().log(Level.INFO, sb.toString());

		} catch (FileNotFoundException e) {
			logger.log(Level.WARNING, e.getMessage());
		} catch (IOException e) {
			logger.log(Level.WARNING, e.getMessage());
		}
	}

}
