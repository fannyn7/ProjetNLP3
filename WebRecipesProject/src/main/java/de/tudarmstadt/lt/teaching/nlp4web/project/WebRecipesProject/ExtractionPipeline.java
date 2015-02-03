package de.tudarmstadt.lt.teaching.nlp4web.project.WebRecipesProject;

import static org.apache.uima.fit.factory.AnalysisEngineFactory.createEngine;
import static org.apache.uima.fit.factory.CollectionReaderFactory.createReader;

import java.io.IOException;

import org.apache.uima.UIMAException;
import org.apache.uima.analysis_engine.AnalysisEngine;
import org.apache.uima.collection.CollectionReader;
import org.apache.uima.fit.pipeline.SimplePipeline;

import de.tudarmstadt.lt.teaching.nlp4web.project.WebRecipesProject.annotator.AmountAnnotator;
import de.tudarmstadt.lt.teaching.nlp4web.project.WebRecipesProject.annotator.DirectivesAnnotator;
import de.tudarmstadt.lt.teaching.nlp4web.project.WebRecipesProject.annotator.IngredientsAnnotator;
import de.tudarmstadt.lt.teaching.nlp4web.project.WebRecipesProject.annotator.UnitAnnotator;
import de.tudarmstadt.lt.teaching.nlp4web.project.WebRecipesProject.reader.WebPageReader;
import de.tudarmstadt.lt.teaching.nlp4web.project.WebRecipesProject.writer.IngredientWriter;
import de.tudarmstadt.lt.teaching.nlp4web.project.WebRecipesProject.writer.UnitWriter;
import de.tudarmstadt.lt.teaching.nlp4web.project.WebRecipesProject.writer.WebPageConsumer;
import de.tudarmstadt.ukp.dkpro.core.stanfordnlp.StanfordParser;
import de.tudarmstadt.ukp.dkpro.core.stanfordnlp.StanfordSegmenter;

public class ExtractionPipeline {

	   public static void main(String[] args)
		    	throws UIMAException, IOException
		    {
		   		//http://allrecipes.com/Recipe/Alisons-Slow-Cooker-Vegetable-Beef-Soup/Detail.aspx?event8=1&prop24=SR_Thumb&e11=alison%20slow%20cooker&e8=Quick%20Search&event10=1&e7=Home%20Page&soid=sr_results_p1i1	
		        String webpage = "http://allrecipes.com/recipe/almond-buttercrunch/";

		        CollectionReader reader = createReader(
		                WebPageReader.class,  WebPageReader.PARAM_URL, webpage 
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

		        SimplePipeline.runPipeline(
		        		reader,
		        		seg,
		        		parse,
		        		amountAnnotator,
		        		unitAnnotator,
		        		unitWriter,
		        		ingredientAnnotator,
		        		directivesAnnotator,
		        		//writer
		        		unitWriter,
		        		ingredientWriter
		        		);
		    }

}
