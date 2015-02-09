package de.tudarmstadt.lt.teaching.nlp4web.project.WebRecipesProject.ratatouilleApp.model;

import java.util.ArrayList;
import java.util.List;

import de.tudarmstadt.ukp.teaching.general.type.DirectivesAnnotation;
import de.tudarmstadt.ukp.teaching.general.type.IngredientAnnotation;

public class Recipe {
	
	private String webLink;
	private String name;
	
	private String textIngredients;
	
	private List<Ingredient> ingredients;
	
	private String textInstructions;
	
	public static final String DEFAULT_NAME = "No name";

	// TODO Uncomment following lines to take the instructions in account
	/*
	private List<Directive> instructions;
	*/
	
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
	
	public Ingredient containsIngredient(String ingredient_lemma) {
		Ingredient ingFound = null;
		// TODO : improve the performance using WordNet
		for(Ingredient i : getIngredients() ) {
			if (i.getName().equalsIgnoreCase(ingredient_lemma)) {
				ingFound = new Ingredient();
				ingFound.setName(i.getName());
				ingFound.setQuantity(i.getQuantity());
			}
		}
			
		return ingFound;
	}
	
	@Override
	public String toString() {
		if (getName()==null || getName().isEmpty()) {
			return DEFAULT_NAME;
		} else {
			return getName();
		}
	}

	public String getTextInstructions() {
		return textInstructions;
	}

	public void setTextInstructions(String textInstructions) {
		this.textInstructions = textInstructions;
	}
	
	// TODO Uncomment following lines to take the instructions in account
	/*
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
	*/

	
	public String toHTML() {
		String s = "<h3>Link : "+getWebLink()+"</h3>\n";
		s += "<h3>Name : "+getName()+"</h3>\n";
		s += "<h3>Ingredients : "+"</h3>\n";
		for (Ingredient i : getIngredients()) {
			s += "\t"+i+"<br/>";
		}
		s += "<h3>Original list of ingredients (from the website) : </h3>\n"
				+ getTextIngredients();
		s += "<h3>Original instructions (from the website) : </h3>\n"
				+ getTextInstructions();
		return s;
		
	}
}
