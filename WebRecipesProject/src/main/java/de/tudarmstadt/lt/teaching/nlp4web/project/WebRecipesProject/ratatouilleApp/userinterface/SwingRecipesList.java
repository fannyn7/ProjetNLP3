package de.tudarmstadt.lt.teaching.nlp4web.project.WebRecipesProject.ratatouilleApp.userinterface;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Collections;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JButton;
import javax.swing.JEditorPane;
import javax.swing.JTextArea;

import de.tudarmstadt.lt.teaching.nlp4web.project.WebRecipesProject.ratatouilleApp.model.Recipe;
import fr.enseeiht.libSwing.ListeDynamique;
 
public class SwingRecipesList extends ListeDynamique<Recipe> {

	private static final JButton bSee = new JButton("See");
	private JEditorPane pVisualization;
	private static final boolean debug = Ratatouille.debug; 
	
	public SwingRecipesList(List<Recipe> liste, JEditorPane visualizationArea) {
		super(liste);//, Collections.singletonList(bSee));
		pVisualization = visualizationArea;
		MouseListener mouseListener = new MouseAdapter() {
		     public void mouseClicked(MouseEvent e) {
		         if (e.getClickCount() == 2) {
		             int index = jListe.locationToIndex(e.getPoint());
		             pVisualization.setText("<html>\n"
						+ "<body>\n"
						+ "<h1>Ratatouille App</h1>\n"
						+ "<h2>Recipe selected</h2>\n"
						+ ((Recipe)dlm.elementAt(index)).toHTML()
						+ "</body>\n");
		             if (debug) {
		            	 System.out.println("Double clicked on Item " + index);
		             }
		          }
		     }
		 };
		 jListe.addMouseListener(mouseListener);
	}

}
