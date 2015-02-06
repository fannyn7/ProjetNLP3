package de.tudarmstadt.lt.teaching.nlp4web.project.WebRecipesProject.ratatouilleApp.model;

import java.util.ArrayList;
import java.util.List;

import de.tudarmstadt.ukp.teaching.general.type.DirectivesAnnotation;
import de.tudarmstadt.ukp.teaching.general.type.IngredientAnnotation;

public class CopyOfRecipe {
	
	private String webLink;
	private String name;
	
	private String textIngredients;
	
	private List<Ingredient> ingredients;
	
	private String textInstructions;
	
	private List<Directive> instructions;
	
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public List<Ingredient> getIngredients() {
		if (this.ingredients == null) {
			this.ingredients = new ArrayList<Ingredient>();
		}
		return ingredients;
	}
	
	public void setIngredients(List<Ingredient> ingredients) {
		this.ingredients = ingredients;
	}

	public String getWebLink() {
		return webLink;
	}

	public void setWebLink(String webLink) {
		this.webLink = webLink;
	}
	
	public static Ingredient parseAnnotation(IngredientAnnotation a) {
		Ingredient i = new Ingredient();
		i.setName(a.getNormalizedName());
		i.setQuantity(a.getAmount());
		return i;
	}

	public String getTextIngredients() {
		return textIngredients;
	}

	public void setTextIngredients(String textIngredients) {
		this.textIngredients = textIngredients;
	}

	public String getTextInstructions() {
		return textInstructions;
	}

	public void setTextInstructions(String textInstructions) {
		this.textInstructions = textInstructions;
	}

	public List<Directive> getInstructions() {
		if (this.instructions == null) {
			this.instructions = new ArrayList<Directive>();
		}
		return instructions;
	}

	public void setInstructions(List<Directive> instructions) {
		this.instructions = instructions;
	}

	public static Directive parseAnnotation(DirectivesAnnotation a) {
		Directive d = new Directive();
		d.setInstruction(a.getNormalizedName());
		d.setIngredient(a.getIngredient());
		d.setResultingEntity(a.getResultingEntity());
		return d;
	}

}
