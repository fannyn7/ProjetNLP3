package de.tudarmstadt.lt.teaching.nlp4web.project.WebRecipesProject.ratatouilleApp.userinterface;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
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

import de.tudarmstadt.lt.teaching.nlp4web.project.WebRecipesProject.ratatouilleApp.model.Recipe;
import de.tudarmstadt.lt.teaching.nlp4web.project.WebRecipesProject.uimaComponent.ExtractionPipeline;
import de.tudarmstadt.lt.teaching.nlp4web.project.WebRecipesProject.uimaComponent.writer.RecipeSerializer;
import de.tudarmstadt.lt.teaching.nlp4web.project.WebRecipesProject.xml.Recipe2Xml;
import de.tudarmstadt.lt.teaching.nlp4web.project.WebRecipesProject.xml.XStreamFactory;
import de.tudarmstadt.ukp.dkpro.core.api.segmentation.type.Token;
import fr.enseeiht.libSwing.ListeDynamique;

public class Ratatouille {
	
	public final static int port = 2048;
	
	JTextField recipe_url =new JTextField("http://");
	JButton bAnalyze = new JButton("Add");
	JEditorPane jEditorPane = new JEditorPane();

	public final static String BOOK_FILE_NAME = "src/test/resources/ratatouille/myFavoriteRecipes.xml";
	public final static String BOOK_INIT_FILE_NAME = "src/test/resources/ratatouille/init.xml";
	
	List<Recipe> myRecipes;
	ListeDynamique<Recipe> lRecipes;
	
	JButton bSave = new JButton("Save book");
	
	public static final boolean debug = true;
	

	public static void main(String[] args) throws UIMAException, IOException {
		new Ratatouille();
		//ExtractionPipeline.executePipeline("http://allrecipes.com/Recipe/Amazing-Pork-Tenderloin-in-the-Slow-Cooker/Detail.aspx?evt19=1");// brackets 1 (2pound) -> pound (1) 
	}

	public Ratatouille()	{
	
		SwingUtilities.invokeLater(new Runnable()
		{
			public void run() {
				
				JFrame j = new JFrame("Project NLP Information extraction from online recipes");
				
				/** DATABASE **/
				uploadMyRecipes();
				lRecipes = new SwingRecipesList(myRecipes, jEditorPane);
				JPanel p = new JPanel(new BorderLayout());
				JScrollPane recipesPane = new JScrollPane(lRecipes, ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
				p.add(recipesPane, BorderLayout.CENTER);
				p.add(bSave, BorderLayout.SOUTH);
				j.getContentPane().add(p,
						BorderLayout.EAST);
				/** ANALYZER **/
				initializeAnalyser(j.getContentPane());
				
				j.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				//j.setSize(new Dimension(500,250));
				j.pack();
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
				if (bAnalyze.isEnabled()) {
					bAnalyze.setEnabled(false);
					// Get the user request
					try {
						// TODO Get the web link safely
						String weblink = recipe_url.getText();
						// Analyze webpage
						(new AnalysisSlave(weblink)).start();
					} catch (Exception ex) {
						// TODO
						ex.printStackTrace();
						bAnalyze.setEnabled(true);
					}
					
				}
			}
		});
		bSave.addActionListener(new ActionListener() {					
			@Override
			public void actionPerformed(ActionEvent e) {
				if (bSave.isEnabled()) {
					bSave.setEnabled(false);
					// Save book
					try {
						Recipe2Xml.generateRecipes(myRecipes, BOOK_FILE_NAME);
						bSave.setEnabled(true);
						if (debug) {
							System.out.println("Book saved.");
						}
					} catch (Exception ex) {
						// TODO
						ex.printStackTrace();
						bSave.setEnabled(true);
					}
					
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
		try {
		XStream xstream = XStreamFactory.createXStream();
		myRecipes = (List<Recipe>) xstream.fromXML(new File(BOOK_FILE_NAME));
		if (debug) {
			System.out.println("Number of favorite recipe : "+myRecipes.size());
			for(Recipe r : myRecipes) {
				System.out.println("Recipe\nname: "+r.getName()+"\nweb link: "+r.getWebLink()+"\nnumber of ingredients: "+r.getIngredients().size());
			}
		}
		} catch (Exception e1) {
			e1.printStackTrace();
			try {
			XStream xstream = XStreamFactory.createXStream();
			myRecipes = (List<Recipe>) xstream.fromXML(new File(BOOK_INIT_FILE_NAME));
			if (debug) {
				System.out.println("Couldn't load book. Start with training set");
				System.out.println("Number of favorite recipe : "+myRecipes.size());
				for(Recipe r : myRecipes) {
					System.out.println("Recipe\nname: "+r.getName()+"\nweb link: "+r.getWebLink()+"\nnumber of ingredients: "+r.getIngredients().size());
				}
			}
			} catch (Exception e2) {
				e2.printStackTrace();
				myRecipes = new ArrayList<Recipe>();
				if (debug) {
					System.out.println("Couldn't load book. Start with empty book");
				}
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
				XStream xstream = XStreamFactory.createXStream();
				Recipe r = (Recipe) xstream.fromXML(new File(RecipeSerializer.BUFFER_FILE_NAME));
				if (debug) {
					System.out.println("Recipe read in "+RecipeSerializer.BUFFER_FILE_NAME+"\nname: "+r.getName()+"\nweb link: "+r.getWebLink()+"\nnumber of ingredients: "+r.getIngredients().size());
				}
				myRecipes.add(r);
				if (debug) {
					System.out.println("Recipe added in favorites");
				}
				lRecipes.addElement(r);
				if (debug) {
					System.out.println("Recipe displayed in favorites");
				}
			} catch (Exception e) {
				// TODO 
				e.printStackTrace();
			} finally {
				bAnalyze.setEnabled(true);
			}
		}
	}
}
