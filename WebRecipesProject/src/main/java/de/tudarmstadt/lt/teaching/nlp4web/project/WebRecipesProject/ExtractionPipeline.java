package de.tudarmstadt.lt.teaching.nlp4web.project.WebRecipesProject;

import static org.apache.uima.fit.factory.AnalysisEngineFactory.createEngine;
import static org.apache.uima.fit.factory.CollectionReaderFactory.createReader;

import java.awt.Desktop;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URI;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.uima.UIMAException;
import org.apache.uima.analysis_engine.AnalysisEngine;
import org.apache.uima.collection.CollectionReader;
import org.apache.uima.fit.pipeline.SimplePipeline;
import org.apache.uima.util.Level;

import de.tudarmstadt.lt.teaching.nlp4web.project.WebRecipesProject.annotator.AmountAnnotator;
import de.tudarmstadt.lt.teaching.nlp4web.project.WebRecipesProject.annotator.DirectivesAnnotator;
import de.tudarmstadt.lt.teaching.nlp4web.project.WebRecipesProject.annotator.IngredientsAnnotator;
import de.tudarmstadt.lt.teaching.nlp4web.project.WebRecipesProject.annotator.PRNAnnotator;
import de.tudarmstadt.lt.teaching.nlp4web.project.WebRecipesProject.annotator.UnitAnnotator;
import de.tudarmstadt.lt.teaching.nlp4web.project.WebRecipesProject.reader.WebPageReader;
import de.tudarmstadt.lt.teaching.nlp4web.project.WebRecipesProject.writer.AnalyzeResults;
import de.tudarmstadt.lt.teaching.nlp4web.project.WebRecipesProject.writer.IngredientWriter;
import de.tudarmstadt.lt.teaching.nlp4web.project.WebRecipesProject.writer.RecipeSerializer;
import de.tudarmstadt.lt.teaching.nlp4web.project.WebRecipesProject.writer.UnitWriter;
import de.tudarmstadt.lt.teaching.nlp4web.project.WebRecipesProject.writer.WebPageConsumer;
import de.tudarmstadt.ukp.dkpro.core.stanfordnlp.StanfordParser;
import de.tudarmstadt.ukp.dkpro.core.stanfordnlp.StanfordSegmenter;


public class ExtractionPipeline {

	
	 public static void main(String[] args)
		    	throws UIMAException, IOException{

		//String webpage = "http://allrecipes.com/Recipe/Awesome-Slow-Cooker-Pot-Roast/Detail.aspx?evt19=1";
		 String webpage = "http://allrecipes.com/Recipe/Best-Brownies/Detail.aspx?evt19=1";


		 String recipesFile = "src/main/resources/recipesEvaluation.txt";
		 String fileToWriteActions = "src/main/resources/globalEvaluationActions.txt";
		 String fileToWriteIngredients = "src/main/resources/globalEvaluationIngredients.txt";
		 
		 FileWriter fw1 = new FileWriter(fileToWriteActions, false);
			BufferedWriter output1 = new BufferedWriter(fw1);
			
			PrintWriter pw1 = new PrintWriter(output1);; 
			pw1.print("");
			pw1.flush();	
			output1.flush();				
			pw1.close();
			
			output1.close();
			
			
			FileWriter fw = new FileWriter(fileToWriteIngredients, false);
			BufferedWriter output = new BufferedWriter(fw);
			
			PrintWriter pw = new PrintWriter(output);; 
			pw.print("");
			pw.flush();	
			output.flush();				
			pw.close();
			
			output.close();
			
		 
		 String line = "";
		 BufferedReader readerRecipesFile = new BufferedReader(new FileReader(recipesFile));
		 Pattern p = Pattern.compile("http://allrecipes.com/Recipe*");
		 while(!(line = readerRecipesFile.readLine()).equals("$$$$$$$")){
			 Matcher matcher = p.matcher(line);
			 if(matcher.find(0)){			 
				 executePipeline(line, recipesFile, fileToWriteActions);
			 }
		 }

		 readerRecipesFile.close();
		 
			BufferedReader reader1;
			try {
				reader1 = new BufferedReader(new FileReader(fileToWriteActions));

			/*Pattern p = Pattern.compile("[0-9][0-9] [0-9][0-9]*");
			Matcher matcher;
				while(!(matcher = p.matcher(line = reader.readLine())).find(0));
			 */
			int correctsA = 0;
			int nbRecipesA = 0;
			int nbTotalActions = 0;
//String line ;
			while ((line = reader1.readLine()) != null){
				System.out.println("ligne : " + line);
				nbRecipesA++;
				String[] numbersA = line.split("\\s");
				correctsA += Integer.parseInt(numbersA[0]);
				nbTotalActions += Integer.parseInt(numbersA[1]);
			}
			System.out.println("Correctly found actions : " + correctsA + " out of " + nbTotalActions);
			System.out.println("precision = " + 100*correctsA/nbTotalActions);
			System.out.println("Test Set : " +nbRecipesA + " recipes");

				reader1.close();
				
				
				

				BufferedReader reader2;
					reader2 = new BufferedReader(new FileReader(fileToWriteIngredients));

				/*Pattern p = Pattern.compile("[0-9][0-9] [0-9][0-9]*");
				Matcher matcher;
					while(!(matcher = p.matcher(line = reader.readLine())).find(0));
				 */
				int correctsI = 0;
				int nbRecipesI = 0;
				int nbTotalIngredients = 0;
	//String line ;
				while ((line = reader2.readLine()) != null){
					System.out.println("ligne : " + line);
					nbRecipesI++;
					String[] numbers = line.split("\\s");
					correctsI += Integer.parseInt(numbers[0]);
					nbTotalIngredients += Integer.parseInt(numbers[1]);
				}
				System.out.println("Correctly found actions : " + correctsI + " out of " + nbTotalIngredients);
				System.out.println("precision = " + 100*correctsI/nbTotalIngredients);
				System.out.println("Test Set : " +nbRecipesI + " recipes");

					reader2.close();

			} catch (FileNotFoundException e) {
			} catch (IOException e) {
			}


	 }
	
	   private static void executePipeline(String webpage, String recipesFile, String fileToWrite)
		    	throws UIMAException, IOException
		    {
		   		//String webpage ="http://allrecipes.com/Recipe/Alisons-Slow-Cooker-Vegetable-Beef-Soup/Detail.aspx?event8=1&prop24=SR_Thumb&e11=alison%20slow%20cooker&e8=Quick%20Search&event10=1&e7=Home%20Page&soid=sr_results_p1i1";	
		        CollectionReader reader = createReader(
		                WebPageReader.class,  WebPageReader.PARAM_URL, webpage 
		        );
		        
		        AnalysisEngine prnAnnotator = createEngine(
		        		PRNAnnotator.class
			        );
		        
		        AnalysisEngine amountAnnotator = createEngine(
		        		AmountAnnotator.class
			        );
		        
		        
		        AnalysisEngine unitAnnotator = createEngine(
	        		UnitAnnotator.class
		        );
		        
		        AnalysisEngine ingredientAnnotator = createEngine(
	        		IngredientsAnnotator.class
		        );
		        
		        AnalysisEngine directivesAnnotator = createEngine(
	        		DirectivesAnnotator.class
		        );
		        
		        AnalysisEngine seg =  createEngine(StanfordSegmenter.class);
		    	AnalysisEngine parse =  createEngine(StanfordParser.class);

		/*        AnalysisEngine jazzy =
		        		createEngine
		        		(SpellChecker.class,
		        		SpellChecker.PARAM_MODEL_LOCATION,
		        		"/Users/Fanny/Documents/2014_2015_Darmstadt/NLP_and_the_Web/dict/words");*/
		        	
		        AnalysisEngine ingredientWriter = createEngine(
		                IngredientWriter.class
		        );
		   
		        AnalysisEngine unitWriter = createEngine(
		                UnitWriter.class
		        );
		        
		        AnalysisEngine writer = createEngine(
		                WebPageConsumer.class
		        );
		        
		        AnalysisEngine analyzeResults = createEngine(
		                AnalyzeResults.class, AnalyzeResults.PARAM_INPUT_FILE, recipesFile, AnalyzeResults.PARAM_URL, webpage
		        );

		        SimplePipeline.runPipeline(
		        		reader,
		        		seg,
		        		parse,
		        		prnAnnotator,
		        		amountAnnotator,
		        		unitAnnotator,
		        		//unitWriter,
		        		ingredientAnnotator,
		        		directivesAnnotator,
		        		//unitWriter,
		        		ingredientWriter,
		        		analyzeResults
		        		);
		    }

	   /** 
	    * Analyze a web recipe and serialize in a xml file
	    * @param webpage
	    * @throws UIMAException
	    * @throws IOException
	    */
	   public static void executePipeline(String webpage)
		    	throws UIMAException, IOException
		    {
		   		//String webpage ="http://allrecipes.com/Recipe/Alisons-Slow-Cooker-Vegetable-Beef-Soup/Detail.aspx?event8=1&prop24=SR_Thumb&e11=alison%20slow%20cooker&e8=Quick%20Search&event10=1&e7=Home%20Page&soid=sr_results_p1i1";	
		        CollectionReader reader = createReader(
		                WebPageReader.class,  WebPageReader.PARAM_URL, webpage 
		        );
		        
		        AnalysisEngine amountAnnotator = createEngine(
		        		AmountAnnotator.class
			        );
		        AnalysisEngine prnAnnotator = createEngine(
		        		PRNAnnotator.class
			        );
		        
		        AnalysisEngine unitAnnotator = createEngine(
	        		UnitAnnotator.class
		        );
		        
		        AnalysisEngine ingredientAnnotator = createEngine(
	        		IngredientsAnnotator.class
		        );
		        
		        AnalysisEngine directivesAnnotator = createEngine(
	        		DirectivesAnnotator.class
		        );
		        
		        AnalysisEngine serializer = createEngine(
		        		RecipeSerializer.class,  WebPageReader.PARAM_URL, webpage 
			        );
		        
		        AnalysisEngine seg =  createEngine(StanfordSegmenter.class);
		    	AnalysisEngine parse =  createEngine(StanfordParser.class);

		        AnalysisEngine ingredientWriter = createEngine(
		                IngredientWriter.class
		        );
		   
		        AnalysisEngine unitWriter = createEngine(
		                UnitWriter.class
		        );
		        
		        AnalysisEngine writer = createEngine(
		                WebPageConsumer.class
		        );

		        /*
		        Desktop desktop = Desktop.isDesktopSupported() ? Desktop.getDesktop() : null;
		        if (desktop != null && desktop.isSupported(Desktop.Action.BROWSE)) {
		            try {
		                desktop.browse(URI.create(webpage));
		            } catch (Exception e) {
		                e.printStackTrace();
		            }
		        } else {
		        	System.out.println("desktop is null");
		        }
		        */
		        SimplePipeline.runPipeline(
		        		reader,
		        		seg,
		        		parse,
		        		prnAnnotator,
		        		amountAnnotator,
		        		unitAnnotator,
		        		unitWriter,
		        		ingredientAnnotator,
		        		directivesAnnotator,
		        		unitWriter,
		        		ingredientWriter,
		        		serializer
		        		);
		    }
}
