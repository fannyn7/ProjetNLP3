package de.tudarmstadt.lt.teaching.nlp4web.project.WebRecipesProject.annotator;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import net.didion.jwnl.JWNL;
import net.didion.jwnl.JWNLException;
import net.didion.jwnl.data.IndexWord;
import net.didion.jwnl.data.POS;
import net.didion.jwnl.data.Pointer;
import net.didion.jwnl.data.PointerType;
import net.didion.jwnl.data.Synset;
import net.didion.jwnl.data.Word;
import net.didion.jwnl.dictionary.Dictionary;

import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.fit.component.JCasAnnotator_ImplBase;
import org.apache.uima.fit.util.JCasUtil;
import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.cas.StringList;
import org.apache.uima.jcas.tcas.Annotation;

import de.tudarmstadt.ukp.dkpro.core.api.segmentation.type.Lemma;
import de.tudarmstadt.ukp.dkpro.core.api.syntax.type.constituent.VP;
import de.tudarmstadt.ukp.teaching.general.type.DirectivesAnnotation;
import de.tudarmstadt.ukp.teaching.general.type.IngredientAnnotation;
import de.tudarmstadt.ukp.teaching.general.type.TextInstructions;


public class DirectivesAnnotator extends JCasAnnotator_ImplBase {

	@Override
	public void process(JCas jcas) throws AnalysisEngineProcessException {
		/* 
		 * DEPRECATED
		String doc = jcas.getDocumentText();
		String[] documents = doc.split("\\$\\$\\$");
		System.out.println("documents[1] " + documents[0]);
		System.out.println("documents[2]" + documents[1]);
		System.out.println("documents[3]" + documents[2]);
		//int len = documents[1].length();
		//int begin = documents[0].length();

		//int end = begin + len;*/

		int vEnd = 0;


		try {
			JWNL.initialize(new FileInputStream("src/main/resources/properties.xml"));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JWNLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Dictionary dictionary = Dictionary.getInstance();


		String resultingEntity = "";
		for (TextInstructions text : JCasUtil.select(jcas,
				TextInstructions.class)) {

			for (Annotation a : JCasUtil.selectCovered(jcas, Annotation.class,
					text)) {

				// a.getType().getShortName());
				//System.out.println("annotation type : "+a.getType().getShortName()+" : " + a.getCoveredText());
				if (a.getType().getShortName().equals("VP")) {
					List<Annotation> list = JCasUtil.selectCovered(jcas, Annotation.class, a.getBegin(), a.getEnd());
					int nbVP = 0;
					for (Annotation b : list){
						if (b.getType().getShortName().equals("VP")) nbVP++;
						if (b.getType().getShortName().equals("NEG")) a.setEnd(a.getBegin());
					}
					if (nbVP==1){
						String previous = resultingEntity;
						boolean addPrevious = false;

						//System.out.println("VP type : "+a.getType().getShortName()+" : " + a.getCoveredText());
						//addPrevious = false;
						DirectivesAnnotation d = new DirectivesAnnotation(jcas);
						d.setBegin(a.getBegin());
						d.setEnd(a.getEnd());
						ArrayList<String> ingredients = new ArrayList<String>();
						String textIngredientLemma = "";
						for (Annotation textIngredient : JCasUtil.selectCovered(
								jcas, Annotation.class, a.getBegin(), a.getEnd())) {
							for (Lemma l : JCasUtil.selectCovered(jcas,
									Lemma.class, textIngredient)) {
								textIngredientLemma = l.getValue();
							}
							// //// check verb to find instructions

							if (textIngredient.getType().getShortName().equals("V")) {
								vEnd = textIngredient.getEnd();
								d.setInstruction(textIngredientLemma);
								resultingEntity = textIngredientLemma+"ed[" + previous;
							}
							int ppBegin = textIngredient.getBegin();
							if ((ppBegin == (vEnd+1)) && (textIngredient.getType().getShortName().equals("PP"))) {
								vEnd = textIngredient.getBegin();
								addPrevious = true;
							}
							// ///// check noun to find ingredients
							if (textIngredient.getType().getShortName().equals("NN")) {
								for (IngredientAnnotation ingredient : JCasUtil
										.select(jcas, IngredientAnnotation.class)) {

									// //// ingredients / recipes match
									if (textIngredientLemma.equals(ingredient
											.getNormalizedName())){
										ingredients.add(textIngredientLemma);
										resultingEntity += " " +  textIngredientLemma;
									}
									else {
										Boolean hypernymFound = false;
										// //// ingredient's hypernym / recipes
										// match
										try {
											IndexWord indexWord = null;
											indexWord = dictionary.lookupIndexWord(
													POS.NOUN, textIngredientLemma);
											// System.out.println("indexword : " +
											// indexWord);
											if (indexWord != null) {
												Synset[] set = indexWord
														.getSenses();
												if (set != null) {
													for (Synset s : set) {
														Pointer[] pointerArr = s
																.getPointers(PointerType.HYPERNYM);
														if (pointerArr != null)
															for (Pointer x : pointerArr) {
																for (Word hypernym : x
																		.getTargetSynset()
																		.getWords()) {
																	// System.out.println(hypernym.getLemma()+
																	// " = "+
																	// ingredient.getCoveredText()
																	// +
																	// "?????????");
																	if (ingredient
																			.getCoveredText()
																			.equals(hypernym
																					.getLemma())) {
																		// System.out.println(hypernym.getLemma()+
																		// " = "+
																		// ingredient.getNormalizedName()+
																		// " ajouté");
																		ingredients
																		.add(textIngredientLemma);
																		hypernymFound = true;
																		resultingEntity = resultingEntity + " " + ingredient.getNormalizedName();
																	}
																}
															}
													}
												}
											}
										} catch (JWNLException e) {
											// TODO Auto-generated catch block
											e.printStackTrace();
										}
										// /////// ingredient / recipes's hypernym
										// match
										if (!hypernymFound) {
											//System.out.println("!hypernymFound" + textIngredientLemma);

											try {
												IndexWord indexWord = null;
												indexWord = dictionary
														.lookupIndexWord(
																POS.NOUN,
																ingredient
																.getNormalizedName());
												// System.out.println("indexword : "
												// + indexWord);
												if (indexWord != null) {
													Synset[] set = indexWord
															.getSenses();
													Boolean alreadyAdded = false;
													if (set != null) {
														for (Synset s : set) {
															Pointer[] pointerArr = s
																	.getPointers(PointerType.HYPERNYM);
															if (pointerArr != null)
																for (Pointer x : pointerArr) {
																	for (Word w : x
																			.getTargetSynset()
																			.getWords()) {
																		for (Lemma l : JCasUtil
																				.selectCovered(
																						jcas,
																						Lemma.class,
																						textIngredient)) {
																			// System.out.println(ingredient.getNormalizedName()
																			// +
																			// " = "
																			// +
																			// w.getLemma()+
																			// " = "+
																			// l.getValue());
																			if (((l.getValue()
																					.equals(w
																							.getLemma())) || (w
																									.getLemma()
																									.contains(l
																											.getValue())))
																											&& !alreadyAdded) {
																				//System.out.println(ingredient+ " ajouté");
																				ingredients
																				.add(ingredient
																						.getNormalizedName());
																				alreadyAdded = true;
																				resultingEntity = resultingEntity + " " + ingredient.getNormalizedName();
																			}
																		}
																	}
																}
														}
													}
												}
											} catch (JWNLException e) {
												// TODO Auto-generated catch block
												e.printStackTrace();
											}
										}
									}
								}
							}

						}
						String ing = "";
						resultingEntity = resultingEntity + " ]";
						if (addPrevious){
							if (!previous.equals("")){
								ing = " "+previous;								
							} else {
								ing = previous;
							}						
						}

						for (int i=0;i<ingredients.size();i++){
							ing = ing + " " + ingredients.get(i);
							//System.out.println("AAAA " + ingredients.get(i));
							//System.out.println("BBBB " + ing);
						}
						
						if (ing == ""){
							ing = " " +resultingEntity;
						}


						d.setIngredient(ing);

						d.setResultingEntity(resultingEntity);
						d.addToIndexes();
						previous = resultingEntity;
					}
				}
			}
		}
	}
}

