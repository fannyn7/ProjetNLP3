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


public class DirectivesAnnotator_backup extends JCasAnnotator_ImplBase {

	@Override
	public void process(JCas jcas) throws AnalysisEngineProcessException {
		String doc = jcas.getDocumentText();
		String[] documents = doc.split("\\$\\$\\$");
		System.out.println("documents[1] " + documents[0]);
		System.out.println("documents[2]" + documents[1]);
		System.out.println("documents[3]" + documents[2]);
		int len = documents[1].length();
		int begin = documents[0].length();
		int end = begin + len;

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
		



		for (Annotation a : JCasUtil.selectCovered(jcas, Annotation.class, begin, end)){
			//System.out.println(a.getCoveredText() + " "+ a.getType().getShortName());
			if (a.getType().getShortName().equals("VP")){
				DirectivesAnnotation d = new DirectivesAnnotation(jcas);
				d.setBegin(a.getBegin());
				d.setEnd(a.getEnd());
				ArrayList<String> ingredients = new ArrayList<String>();
				String textIngredientLemma = "";
				for (Annotation textIngredient : JCasUtil.selectCovered(jcas, Annotation.class, a.getBegin(), a.getEnd())){
					for (Lemma l : JCasUtil.selectCovered(jcas, Lemma.class,textIngredient)){
						textIngredientLemma = l.getValue();
					}
					////// check verb to find instructions
					if (textIngredient.getType().getShortName().equals("V")){
						d.setInstruction(textIngredientLemma);
					}
					/////// check noun to find ingredients
					if (textIngredient.getType().getShortName().equals("NN")){
						for (IngredientAnnotation ingredient : JCasUtil.select(jcas,IngredientAnnotation.class)){
							
							////// ingredients / recipes match
							if(textIngredientLemma.equals(ingredient.getNormalizedName()))
								ingredients.add(textIngredientLemma);
							else {
								Boolean hypernymFound = false;
								////// ingredient's hypernym / recipes match
								try {
									IndexWord indexWord = null;
									indexWord = dictionary.lookupIndexWord(POS.NOUN, textIngredientLemma);
									//System.out.println("indexword : " + indexWord);
									if (indexWord != null) {
										Synset[] set = indexWord.getSenses();
										if (set != null) {
											for (Synset s:set) {
												Pointer[] pointerArr = s.getPointers(PointerType.HYPERNYM);
												if (pointerArr != null)
													for (Pointer x : pointerArr) {
														for (Word hypernym:x.getTargetSynset().getWords()){
															//System.out.println(hypernym.getLemma()+ " = "+ ingredient.getCoveredText() + "?????????");
															if(ingredient.getCoveredText().equals(hypernym.getLemma())){
																//System.out.println(hypernym.getLemma()+ " = "+ ingredient.getNormalizedName()+ " ajouté");
																ingredients.add(textIngredientLemma);
																hypernymFound = true;
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
								///////// ingredient / recipes's hypernym match
								 if(!hypernymFound){
										System.out.println("!hypernymFound" + textIngredientLemma);

									try {
										IndexWord indexWord = null;
										indexWord = dictionary.lookupIndexWord(POS.NOUN, ingredient.getNormalizedName());
										//System.out.println("indexword : " + indexWord);
										if (indexWord != null) {
											Synset[] set = indexWord.getSenses();
											Boolean alreadyAdded = false;																
											if (set != null) {
												for (Synset s:set) {
													Pointer[] pointerArr = s.getPointers(PointerType.HYPERNYM);
													if (pointerArr != null)
														for (Pointer x : pointerArr) {
															for (Word w:x.getTargetSynset().getWords()){
																for (Lemma l : JCasUtil.selectCovered(jcas, Lemma.class,textIngredient)){
																	//System.out.println(ingredient.getNormalizedName() + " = " + w.getLemma()+ " = "+ l.getValue());
																	if (((l.getValue().equals(w.getLemma())) || (w.getLemma().contains(l.getValue()))) && !alreadyAdded){
																		System.out.println(ingredient+" ajouté");
																		ingredients.add(ingredient.getNormalizedName());
																		alreadyAdded = true;
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
				d.setIngredient(ingredients.toString());
				d.addToIndexes();
			}  
		}
	}
}

