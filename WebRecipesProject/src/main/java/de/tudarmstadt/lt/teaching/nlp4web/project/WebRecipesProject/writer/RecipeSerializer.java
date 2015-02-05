package de.tudarmstadt.lt.teaching.nlp4web.project.WebRecipesProject.writer;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Collection;
import java.util.List;

import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.fit.component.JCasConsumer_ImplBase;
import org.apache.uima.fit.descriptor.ConfigurationParameter;
import org.apache.uima.fit.util.JCasUtil;
import org.apache.uima.jcas.JCas;
import org.apache.uima.util.Level;

import de.tudarmstadt.lt.teaching.nlp4web.project.WebRecipesProject.ratatouilleApp.model.Ingredient;
import de.tudarmstadt.lt.teaching.nlp4web.project.WebRecipesProject.ratatouilleApp.model.Recipe;
import de.tudarmstadt.lt.teaching.nlp4web.project.WebRecipesProject.xml.Recipe2Xml;
import de.tudarmstadt.ukp.teaching.general.type.IngredientAnnotation;
import de.tudarmstadt.ukp.teaching.general.type.TextIngredients;
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
		sb.append("Recipe object created"); sb.append(LF);
		sb.append("Recipe name : "+r.getName()); sb.append(LF);
		
		String filename =  "src/test/resources/"+"serializedRecipes/"+r.getName().replace("\\s", "_").replace("\\", "-")+".xml";
		try {
			Recipe2Xml.generateRecipes(r, filename);
			sb.append("Recipe serialized"); sb.append(LF);
			sb.append("Xml file created : "+filename); sb.append(LF);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			sb.append("Recipe serialization failed"); sb.append(LF);
		} catch (IOException e) {
			e.printStackTrace();
			sb.append("Recipe serialization failed"); sb.append(LF);
		}
		
        getContext().getLogger().log(Level.INFO, sb.toString());
	}
}
