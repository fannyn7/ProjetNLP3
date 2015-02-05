package de.tudarmstadt.lt.teaching.nlp4web.project.WebRecipesProject.ratatouilleApp.userinterface;

import java.awt.BorderLayout;
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

public class Ratatouille extends JFrame {
	
	public final static int port = 2048;
	
	JTextField recipe_url;
	JButton bAnalyze;
	JEditorPane jEditorPane;
	
	
	public Ratatouille()	{
	
		SwingUtilities.invokeLater(new Runnable()
		{
			public void run()
			{
				jEditorPane = new JEditorPane();
				recipe_url=new JTextField();
				bAnalyze=new JButton("Analyze");
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
							
							// TODO display analysis result in the frame
							//displayAnalysis(s, jEditorPane);
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

				JFrame j = new JFrame("Project Information extraction from online recipes");
				j.getContentPane().add(pane,BorderLayout.NORTH);				
				j.getContentPane().add(scrollPane, BorderLayout.CENTER);
				j.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				j.setSize(new Dimension(500,250));
				j.setLocationRelativeTo(null);
				j.setVisible(true);
			}
		});
	}


	public static void main(String[] args) {

		new Ratatouille();
		
		// load the recipe from a file
		// initialize the XStream if a xml file is given:
		/*
		XStream xstream = XStreamFactory.createXStream();
		Recipe r = (Recipe) xstream.fromXML(new File("src/test/resources/serializedRecipes/Slow Cooker Pulled Pork Recipe - Allrecipes.com.xml"));
		System.out.println("Recipe\nname: "+r.getName()+"\nweb link: "+r.getWebLink()+"\nnumber of ingredients: "+r.getIngredients().size());
		*/
	
	}
	
	
	
	class AnalysisSlave extends Thread {
		
		String url;
		
		public AnalysisSlave(String link){
			this.url = link;
		}


		public void run() {
			try {
				ExtractionPipeline.executePipeline(url);
			} catch (UIMAException | IOException e) {
				// TODO 
				e.printStackTrace();
			}
		}
	}
}
