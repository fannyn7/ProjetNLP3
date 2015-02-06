package de.tudarmstadt.lt.teaching.nlp4web.project.WebRecipesProject.ratatouilleApp.model;

import de.tudarmstadt.ukp.teaching.general.type.DirectivesAnnotation;

public class Directive {
	
    private String instruction;
    private String ingredient;
    private String resultingEntity;
    
	public String getInstruction() {
		return instruction;
	}
	public void setInstruction(String instruction) {
		this.instruction = instruction;
	}
	public String getIngredient() {
		return ingredient;
	}
	public void setIngredient(String ingredient) {
		this.ingredient = ingredient;
	}
	public String getResultingEntity() {
		return resultingEntity;
	}
	public void setResultingEntity(String resultingEntity) {
		this.resultingEntity = resultingEntity;
	}
	
	public static Directive parseAnnotation(DirectivesAnnotation a) {
		Directive d = new Directive();
		d.setInstruction(a.getInstruction());
		d.setIngredient(a.getIngredient());
		d.setResultingEntity(a.getResultingEntity());
		return d;
	}
	
    
}
