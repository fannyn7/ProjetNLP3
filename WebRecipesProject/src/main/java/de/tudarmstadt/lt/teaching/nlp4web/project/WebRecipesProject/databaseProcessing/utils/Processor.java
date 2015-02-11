package de.tudarmstadt.lt.teaching.nlp4web.project.WebRecipesProject.databaseProcessing.utils;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.io.FileUtils;


public class Processor {
	
	private static Set<String> database;
	private static String FILENAME = "src/main/resources/databases/quantityUnit.txt";
	
	private static void initializeIngredientDatabase() {
		database = new HashSet<String>();
		try {
			String f = FileUtils.readFileToString(new File(
					FILENAME));
			String[] lines = f.split("(\r\n|\n)");
			for (String line : lines) {
				if (!line.startsWith("<!--") && !(line.matches(("(\\s)*")))) {
					// quantity unit word
					database.add(line);
				} // else source acknowledgement
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public static void main(String[] args) throws FileNotFoundException {
		initializeIngredientDatabase();
		String x = "";
		for (String s : database) {
			x +=s+"\n";
		}
		PrintStream ps = new PrintStream(FILENAME);
		ps.println(x);
		ps.close();
				
	}
	
}
