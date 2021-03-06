package de.tudarmstadt.lt.teaching.nlp4web.project.WebRecipesProject.uimaComponent.writer;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import org.apache.uima.UIMAFramework;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.fit.component.JCasAnnotator_ImplBase;
import org.apache.uima.fit.descriptor.ConfigurationParameter;
import org.apache.uima.fit.util.JCasUtil;
import org.apache.uima.jcas.JCas;
import org.apache.uima.util.Level;
import org.apache.uima.util.Logger;

import de.tudarmstadt.ukp.teaching.general.type.DirectivesAnnotation;
import de.tudarmstadt.ukp.teaching.general.type.IngredientAnnotation;

public class AnalyzeResults extends JCasAnnotator_ImplBase{

	public static final String PARAM_INPUT_FILE = "InputFile";
	public static final String PARAM_URL = "URL";


	@ConfigurationParameter(name = PARAM_INPUT_FILE, mandatory = true)
	private String inputFile;
	@ConfigurationParameter(name = PARAM_URL, mandatory = true)
	private String recipeLink;

	Logger logger = UIMAFramework.getLogger(AnalyzeResults.class);

	public static final String LF = System.getProperty("line.separator");

	private class Ingredient{
		@Override
		public String toString() {
			return "Ingredient [amount=" + amount + ", ingredient="
					+ ingredient + "]";
		}

		public String amount;
		public String unit;
		public String ingredient;
		
		public Ingredient(String amount,String unit, String ingredient) {
			this.amount = amount;
			this.unit = unit;
			this.ingredient = ingredient;
		}
		
		
		
	
	}
	
	@Override
	public void process(JCas jcas) throws AnalysisEngineProcessException {
		try {
			logger.log(Level.INFO, "Start analyzing results");
			StringBuilder sb = new StringBuilder();


			String line;
			BufferedReader reader = new BufferedReader(new FileReader(inputFile));
			
			String fileToWriteActions = "src/main/resources/globalEvaluationActions.txt";
			String fileToWriteIngredients = "src/main/resources/globalEvaluationIngredients.txt";
		
			int correct = 0;
			ArrayList<String> actionsList = new ArrayList<String>(); 
			HashSet<String> realActionsSet = new HashSet<String>();
			HashSet<Ingredient> realIngredientsSet = new HashSet<Ingredient>();
			while(!(line = reader.readLine()).equals(recipeLink)) ;
			reader.readLine(); // empty line
			line = reader.readLine(); // actions
			String[] actions = line.split("\\s");
			for (int i = 0; i<actions.length;i++) {
				actionsList.add(actions[i]);
				realActionsSet.add(actions[i].toLowerCase());
			}


			for (DirectivesAnnotation a : JCasUtil.select(jcas, DirectivesAnnotation.class)){
				if (realActionsSet.contains(a.getInstruction())){
					correct++;
				} 
			}
			

			try
			{
				
				FileWriter fw = new FileWriter(fileToWriteActions, true);
				BufferedWriter output = new BufferedWriter(fw);
				
				output.write(correct + " " + realActionsSet.size() + "\n");
				
				output.flush();				
				output.close();
			}
			catch(IOException ioe){
				System.out.print("Erreur : ");
				ioe.printStackTrace();
				}

			int indiceList = 0;
			for (DirectivesAnnotation a : JCasUtil.select(jcas, DirectivesAnnotation.class)) { 
				if (a.getInstruction()!=null){
					int currentIndice = indiceList;
					while(!(currentIndice == actionsList.size() || (a.getInstruction().equals(actionsList.get(currentIndice))))){
						currentIndice++;
					}
				}

			}

			
			reader.readLine(); // empty line
			int nbIngredients = 0;
			while ((!(line = reader.readLine()).isEmpty()) && line != null && !line.matches("\\s") && !line.matches("\\n")){
				String[] ingredient = line.split("\\s\\|\\s");
				if (ingredient[0].equals("null") && ingredient[1].equals("null")){
					realIngredientsSet.add(new Ingredient("","",ingredient[2]));					
				} else if (ingredient[0].equals("null")){
					realIngredientsSet.add(new Ingredient("",ingredient[1],ingredient[2]));					
				} else if (ingredient[1].equals("null")){
					realIngredientsSet.add(new Ingredient(ingredient[0],"",ingredient[2]));					
				} else {
					realIngredientsSet.add(new Ingredient(ingredient[0],ingredient[1],ingredient[2]));					
				}
			}
			int correctI = 0;
			for (IngredientAnnotation a : JCasUtil.select(jcas, IngredientAnnotation.class)){
				Boolean alreadyAdded = false;
				for ( Ingredient ing : realIngredientsSet){
					if(!alreadyAdded 
							&& (ing.ingredient.contains(a.getNormalizedName()))
							&& ( ((a.getAmount()==null) && ing.amount.equals("") && ing.unit.equals("")) 
									|| ( (a.getAmount() !=null) 
											&& a.getAmount().contains(ing.amount)
											&& a.getAmount().contains(ing.unit) ) ) ) {
						correctI++;
						alreadyAdded = true;
					}
				}
			}
			
			try
			{
				
				FileWriter fw = new FileWriter(fileToWriteIngredients, true);
				BufferedWriter output = new BufferedWriter(fw);
				
				output.write(correctI + " " + realIngredientsSet.size() + "\n");
				
				output.flush();				
				output.close();
			}
			catch(IOException ioe){
				System.out.print("Erreur : ");
				ioe.printStackTrace();
				}


			reader.close();
			getContext().getLogger().log(Level.INFO, sb.toString());
		} catch (FileNotFoundException e) {
			logger.log(Level.WARNING, e.getMessage());
		} catch (IOException e) {
			logger.log(Level.WARNING, e.getMessage());
		}
	}


}
