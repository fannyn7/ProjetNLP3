package de.tudarmstadt.lt.teaching.nlp4web.project.WebRecipesProject.ratatouilleApp.model;

import de.tudarmstadt.ukp.teaching.general.type.IngredientAnnotation;

public class Ingredient {

	private String name;
	private String quantity;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getQuantity() {
		return quantity;
	}
	public void setQuantity(String quantity) {
		this.quantity = quantity;
	}
	
	public static Ingredient parseAnnotation(IngredientAnnotation a) {
		Ingredient i = new Ingredient();
		i.setName(a.getNormalizedName());
		i.setQuantity(a.getAmount());
		return i;
	}
	
	@Override
	public String toString() {
		return getName()+" ("+getQuantity()+")";
	}
	
	
}
