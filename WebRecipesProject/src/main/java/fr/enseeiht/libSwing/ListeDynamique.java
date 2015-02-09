package fr.enseeiht.libSwing;
//package elements_interface;
//package LancerDeRayon;

import javax.swing.*;

import java.awt.*;

import javax.swing.event.*;

import java.awt.event.*;
import java.util.*;
import java.util.List;

/** 
 * Modelise un composant de fenetre graphique contenant une liste
 * d'elements que l'on peut supprimer un a un ou en groupe en cliquant
 * sur le bouton Supprimer.
 * Il faut ajouter la ListeDynamique à la liste d'éléments que l'on 
 * represente pour que l'on puisse tenir compte des ajouts eventuels 
 * d'elements
 * @author EHOLIE Solene
 */
public class ListeDynamique<E> extends JPanel {

  	protected List<E> 	laListe;
  	protected JList		jListe;
  	protected DefaultListModel dlm;
	private JButton boutonSupprimer = new JButton("Delete");

  	public ListeDynamique (List<E> liste, List<JButton> boutonsCommandes) {
	  	super(new BorderLayout());
		// créer une JList et la remplir avec les éléments de la liste
		this.dlm = new DefaultListModel();
		this.jListe = new JList(this.dlm);
		initialiserListe(liste);
		this.add(this.jListe, BorderLayout.CENTER);
		// Boutons de commande
		JPanel pBoutons = new JPanel();
		for (JButton b : boutonsCommandes) {
			pBoutons.add(b);
		}
		// ajouter le bouton supprimer
		pBoutons.add(this.boutonSupprimer);
		this.add(pBoutons, BorderLayout.SOUTH);
		
		// ------------------------------ Le Controleur
		// configurer le bouton supprimer
		this.boutonSupprimer.addMouseListener(new ActionSupprimer());
	}

  	public ListeDynamique (List<E> liste) {
  		this(liste, new ArrayList<JButton>());
  	}
  	
	/** Remplir la ListeSupprimable avec les éléments d'une liste donnée.
	 * @param liste la liste donnée en parametre
	 */
	public void initialiserListe(List<E> liste) {
	  	this.laListe = liste;
		if (liste != null) {
			for (E e : this.laListe) {
			  	this.dlm.addElement(e);
			}
		}
	}

	/** Supprimer les éléments sélectionnés.
	 */
	public void supprimerSelection() {
	  	Object[] a_supprimer = this.jListe.getSelectedValues();
		// supprimer les objets sélectionnés de laListe et de jListe
		for(int i=0; i < a_supprimer.length ; i++) {
		  	if (this.dlm.removeElement(a_supprimer[i])) {
			  	boolean retour = this.laListe.remove(a_supprimer[i]);
		  		System.out.println("[ListeDynamique] Un item supprimé "+retour);
			}
		}
	}
	
	/** Ajouter à la jListe l'élément qui a été ajouté à la liste modélisée
	 * @param arg l'objet à ajouter
	 */
	public void addElement(Object arg) {
		this.dlm.addElement(arg);
	}


// ---------------
// MouseListener
// ---------------
	private class ActionSupprimer extends MouseAdapter {
	  	public void mouseClicked(MouseEvent e) {
			if (! dlm.isEmpty()) {
			  	supprimerSelection();
				System.out.println("Sélection supprimée\n");
			}
		}
	}

}
		  	

