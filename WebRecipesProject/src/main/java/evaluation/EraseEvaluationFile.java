package evaluation;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.fit.component.JCasAnnotator_ImplBase;
import org.apache.uima.fit.descriptor.ConfigurationParameter;
import org.apache.uima.jcas.JCas;

public class EraseEvaluationFile extends JCasAnnotator_ImplBase{

	public static final String PARAM_INPUT_FILE = "InputFile";

	@ConfigurationParameter(name = PARAM_INPUT_FILE, mandatory = true)
	private String fileToWrite;
	
	@Override
	public void process(JCas jcas) throws AnalysisEngineProcessException {
		try
		{
			
			FileWriter fw = new FileWriter(fileToWrite, false);
			BufferedWriter output = new BufferedWriter(fw);
			
			PrintWriter pw = new PrintWriter(output);; 
			pw.print("");
			pw.close();
			
			output.flush();				
			output.close();
		}
		catch(IOException ioe){
			System.out.print("Erreur : ");
			ioe.printStackTrace();
			}
	}
	
}
