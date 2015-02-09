package de.tudarmstadt.lt.teaching.nlp4web.project.WebRecipesProject.ratatouilleApp.userinterface;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingUtilities;
import javax.swing.text.Document;
import javax.swing.text.html.HTMLEditorKit;
import javax.swing.text.html.StyleSheet;

import org.apache.uima.UIMAException;

import com.thoughtworks.xstream.XStream;

import de.tudarmstadt.lt.teaching.nlp4web.project.WebRecipesProject.ExtractionPipeline;
import de.tudarmstadt.lt.teaching.nlp4web.project.WebRecipesProject.ratatouilleApp.model.Recipe;
import de.tudarmstadt.lt.teaching.nlp4web.project.WebRecipesProject.xml.XStreamFactory;
import de.tudarmstadt.ukp.dkpro.core.api.segmentation.type.Token;
import fr.enseeiht.libSwing.ListeDynamique;

// TODO
// 1. Save the session
// 2. Add analyzed recipes to the list (communication per files using a buffer file + lock to have access to it)
// 3. Check if all files are correctly used
public class Ratatouille {
	
	public final static int port = 2048;
	
	JTextField recipe_url =new JTextField();
	JButton bAnalyze = new JButton("Add");
	JEditorPane jEditorPane = new JEditorPane();

	List<Recipe> myRecipes;
	ListeDynamique<Recipe> lRecipes;
	
	public boolean debug = true;
	

	public static void main(String[] args) {
		new Ratatouille();
	}

	public Ratatouille()	{
	
		SwingUtilities.invokeLater(new Runnable()
		{
			public void run() {
				
				JFrame j = new JFrame("Project Information extraction from online recipes");
				
				/** DATABASE **/
				uploadMyRecipes();
				lRecipes = new SwingRecipesList(myRecipes, jEditorPane);
				JScrollPane recipesPane = new JScrollPane(lRecipes, ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
				j.getContentPane().add(recipesPane,
						BorderLayout.SOUTH);
				/** ANALYZER **/
				initializeAnalyser(j.getContentPane());
				
				j.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				j.setSize(new Dimension(500,250));
				j.setLocationRelativeTo(null);
				j.setVisible(true);
			}
		});
	}

	private void initializeAnalyser(Container contentPane) {
		JPanel pane=new JPanel(new BorderLayout());
		pane.add(recipe_url,BorderLayout.CENTER);
		pane.add(bAnalyze, BorderLayout.EAST);
		bAnalyze.addActionListener(new ActionListener() {					
			@Override
			public void actionPerformed(ActionEvent e) {	
				// Create a web client
				// Get the user request
				try {
					// TODO Get the web link safely
					String weblink = recipe_url
							.getText();
					// Analyze webpage
					(new AnalysisSlave(weblink)).start();
					
				} catch (Exception ex) {
				  ex.printStackTrace();
				}
			}
		});
						
		jEditorPane.setEditable(false);
		JScrollPane scrollPane = new JScrollPane(jEditorPane);
		HTMLEditorKit kit = new HTMLEditorKit();
		jEditorPane.setEditorKit(kit);
		StyleSheet styleSheet = kit.getStyleSheet();
		styleSheet.addRule("body {color:#000; font-family:times; margin: 4px; }");
		styleSheet.addRule("h1 {color: blue;}");
		styleSheet.addRule("h2 {color: #ff0000;}");
		styleSheet.addRule("pre {font : 10px monaco; color : black; background-color : #fafafa; }");							
		// Greetings
		String htmlString = "<html>\n"
			+ "<body>\n"
			+ "<h1>Ratatouille App</h1>\n"
			+ "Tape the link of the online recipe to analyze"
			+ "</body>\n";
		Document doc = kit.createDefaultDocument();
		jEditorPane.setDocument(doc);
		jEditorPane.setText(htmlString);
		contentPane.add(pane,BorderLayout.NORTH);				
		contentPane.add(scrollPane, BorderLayout.CENTER);
		
	}
	
	private void uploadMyRecipes() {
		XStream xstream = XStreamFactory.createXStream();
		myRecipes = (List<Recipe>) xstream.fromXML(new File("src/test/resources/ratatouille/myFavoriteRecipes.xml"));
		if (debug) {
			System.out.println("Number of favorite recipe : "+myRecipes.size());
			for(Recipe r : myRecipes) {
				System.out.println("Recipe\nname: "+r.getName()+"\nweb link: "+r.getWebLink()+"\nnumber of ingredients: "+r.getIngredients().size());
			}
		}
	}
	
	
	class AnalysisSlave extends Thread {		
		String url;
		
		public AnalysisSlave(String link){
			this.url = link;
		}

		public void run() {
			try {
				ExtractionPipeline.executePipeline(url);
			} catch (Exception e) {
				// TODO 
				e.printStackTrace();
			}
		}
	}
}
