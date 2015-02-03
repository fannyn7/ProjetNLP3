

/* First created by JCasGen Mon Jan 26 18:38:21 CET 2015 */
package de.tudarmstadt.ukp.teaching.general.type;

import org.apache.uima.jcas.JCas; 
import org.apache.uima.jcas.JCasRegistry;
import org.apache.uima.jcas.cas.TOP_Type;

import org.apache.uima.jcas.tcas.Annotation;


/** 
 * Updated by JCasGen Mon Jan 26 18:40:07 CET 2015
 * XML source: /home/monordi/Cours/3A/TU/NLP/Exercices/ProjetNLP/WebRecipesProject/src/main/resources/desc/type/TitleAnnotation.xml
 * @generated */
public class TitleAnnotation extends Annotation {
  /** @generated
   * @ordered 
   */
  @SuppressWarnings ("hiding")
  public final static int typeIndexID = JCasRegistry.register(TitleAnnotation.class);
  /** @generated
   * @ordered 
   */
  @SuppressWarnings ("hiding")
  public final static int type = typeIndexID;
  /** @generated
   * @return index of the type  
   */
  @Override
  public              int getTypeIndexID() {return typeIndexID;}
 
  /** Never called.  Disable default constructor
   * @generated */
  protected TitleAnnotation() {/* intentionally empty block */}
    
  /** Internal - constructor used by generator 
   * @generated
   * @param addr low level Feature Structure reference
   * @param type the type of this Feature Structure 
   */
  public TitleAnnotation(int addr, TOP_Type type) {
    super(addr, type);
    readObject();
  }
  
  /** @generated
   * @param jcas JCas to which this Feature Structure belongs 
   */
  public TitleAnnotation(JCas jcas) {
    super(jcas);
    readObject();   
  } 

  /** @generated
   * @param jcas JCas to which this Feature Structure belongs
   * @param begin offset to the begin spot in the SofA
   * @param end offset to the end spot in the SofA 
  */  
  public TitleAnnotation(JCas jcas, int begin, int end) {
    super(jcas);
    setBegin(begin);
    setEnd(end);
    readObject();
  }   

  /** 
   * <!-- begin-user-doc -->
   * Write your own initialization here
   * <!-- end-user-doc -->
   *
   * @generated modifiable 
   */
  private void readObject() {/*default - does nothing empty block */}
     
  //*--------------*
  //* Feature: Title

  /** getter for Title - gets 
   * @generated
   * @return value of the feature 
   */
  public String getTitle() {
    if (TitleAnnotation_Type.featOkTst && ((TitleAnnotation_Type)jcasType).casFeat_Title == null)
      jcasType.jcas.throwFeatMissing("Title", "de.tudarmstadt.ukp.teaching.general.type.TitleAnnotation");
    return jcasType.ll_cas.ll_getStringValue(addr, ((TitleAnnotation_Type)jcasType).casFeatCode_Title);}
    
  /** setter for Title - sets  
   * @generated
   * @param v value to set into the feature 
   */
  public void setTitle(String v) {
    if (TitleAnnotation_Type.featOkTst && ((TitleAnnotation_Type)jcasType).casFeat_Title == null)
      jcasType.jcas.throwFeatMissing("Title", "de.tudarmstadt.ukp.teaching.general.type.TitleAnnotation");
    jcasType.ll_cas.ll_setStringValue(addr, ((TitleAnnotation_Type)jcasType).casFeatCode_Title, v);}    
  }

    