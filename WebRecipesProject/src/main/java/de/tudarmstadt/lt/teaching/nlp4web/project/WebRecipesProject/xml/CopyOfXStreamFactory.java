package de.tudarmstadt.lt.teaching.nlp4web.project.WebRecipesProject.xml;

import java.util.ArrayList;

import com.thoughtworks.xstream.XStream;

import de.tudarmstadt.lt.teaching.nlp4web.project.WebRecipesProject.ratatouilleApp.model.Directive;
import de.tudarmstadt.lt.teaching.nlp4web.project.WebRecipesProject.ratatouilleApp.model.Ingredient;
import de.tudarmstadt.lt.teaching.nlp4web.project.WebRecipesProject.ratatouilleApp.model.Recipe;

public class CopyOfXStreamFactory {
	public static XStream createXStream() {
		//define alias so the xml file can be read easier
		XStream xstream = new XStream();
		
		xstream.alias("Recipe", Recipe.class);
		xstream.alias("Ingredient", Ingredient.class);
		xstream.alias("Instruction", Directive.class);

		return xstream;
	}
}
