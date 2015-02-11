package de.tudarmstadt.lt.teaching.nlp4web.project.WebRecipesProject.uimaComponent.writer;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Collection;
import java.util.List;

import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.fit.component.JCasConsumer_ImplBase;
import org.apache.uima.fit.descriptor.ConfigurationParameter;
import org.apache.uima.fit.util.JCasUtil;
import org.apache.uima.jcas.JCas;
import org.apache.uima.util.Level;

import de.tudarmstadt.lt.teaching.nlp4web.project.WebRecipesProject.ratatouilleApp.model.Directive;
import de.tudarmstadt.lt.teaching.nlp4web.project.WebRecipesProject.ratatouilleApp.model.Ingredient;
import de.tudarmstadt.lt.teaching.nlp4web.project.WebRecipesProject.ratatouilleApp.model.Recipe;
import de.tudarmstadt.lt.teaching.nlp4web.project.WebRecipesProject.xml.Recipe2Xml;
import de.tudarmstadt.ukp.teaching.general.type.DirectivesAnnotation;
import de.tudarmstadt.ukp.teaching.general.type.IngredientAnnotation;
import de.tudarmstadt.ukp.teaching.general.type.TextIngredients;
import de.tudarmstadt.ukp.teaching.general.type.TextInstructions;
import de.tudarmstadt.ukp.teaching.general.type.TitleAnnotation;
import de.tudarmstadt.ukp.teaching.general.type.UnitAnnotation;

/**
 * Serialize the analyzed recipe
 * Should be called only at the end of the pipeline
 * @author Solene
 *
 */
public class RecipeSerializer extends JCasConsumer_ImplBase {

    public static final String LF = System.getProperty("line.separator");

	public static final String PARAM_URL = "url";
	@ConfigurationParameter(
	name = PARAM_URL,
	description = "The URL of the recipe",
	mandatory = true)
	private String url;
	
	public static final String PARAM_SAVE_IN_BUFFER = "save_in_buffer";
	@ConfigurationParameter(
	name = PARAM_SAVE_IN_BUFFER,
	description = "Tells if the serialized recipe should be saved in the buffer file",
	mandatory = false,
	defaultValue="true")
	private boolean save_in_buffer;
	
	public static final String BUFFER_FILE_NAME = "src/test/resources/ratatouille/buffer_file.xml";
	
	
	@Override
	public void process(JCas jcas) throws AnalysisEngineProcessException {
		StringBuilder sb = new StringBuilder();
		sb.append("=== RecipeSerializer ==="); sb.append(LF);
		
		Recipe r = new Recipe();
		r.setWebLink(url);
		
		
		for(TitleAnnotation lt : JCasUtil.select(jcas, TitleAnnotation.class)) {
			//should have 0 or 1 element
			r.setName(lt.getTitle());
		}
		
		for(TextIngredients lt : JCasUtil.select(jcas, TextIngredients.class)) {
			//should have 0 or 1 element
			r.setTextIngredients(lt.getCoveredText());
		}
		
		for (IngredientAnnotation i : JCasUtil.select(jcas, IngredientAnnotation.class)) { 
			r.getIngredients().add(Ingredient.parseAnnotation(i));
		}
		

		for(TextInstructions lt : JCasUtil.select(jcas, TextInstructions.class)) {
			//should have 0 or 1 element
			r.setTextInstructions(lt.getCoveredText());
		}
		
		// TODO Uncomment following lines to take the instructions in account
		/*
		for (DirectivesAnnotation d : JCasUtil.select(jcas, DirectivesAnnotation.class)) { 
			r.getInstructions().add(Directive.parseAnnotation(d));
		}
		*/
		
		sb.append("Recipe object created"); sb.append(LF);
		sb.append("Recipe name : "+r.getName()); sb.append(LF);
		
		String filename =  "src/test/resources/"+"serializedRecipes/"+r.getName().replace("\\s", "_").replace("\\", "-")+".xml";
		try {
			String x = Recipe2Xml.generateRecipes(r, filename);
			sb.append("Recipe serialized"); sb.append(LF);
			sb.append("Xml file created : "+filename); sb.append(LF);
			// Save in favourites ?
			if (save_in_buffer) {
				try {
					copyInBuffer(x);
				} catch (IOException e) {
					e.printStackTrace();
					sb.append("Copy in buffer_file.xml failed");
					sb.append(LF);
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			sb.append("Recipe serialization failed"); sb.append(LF);
		} catch (IOException e) {
			e.printStackTrace();
			sb.append("Recipe serialization failed"); sb.append(LF);
		}
		
        getContext().getLogger().log(Level.INFO, sb.toString());
	}

	public void copyInBuffer(String recipeSerializationText) throws IOException{
		PrintStream ps = new PrintStream(BUFFER_FILE_NAME);
		ps.println(recipeSerializationText);
		ps.close();
		System.out.println("[RecipeSerializer] copy in buffer_file.xml");
	}
}
