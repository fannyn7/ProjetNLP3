

/* First created by JCasGen Mon Dec 15 18:17:26 CET 2014 */
package de.tudarmstadt.ukp.teaching.general.type;

import org.apache.uima.jcas.JCas; 
import org.apache.uima.jcas.JCasRegistry;
import org.apache.uima.jcas.cas.TOP_Type;

import org.apache.uima.jcas.tcas.Annotation;


/** 
 * Updated by JCasGen Tue Dec 30 13:27:43 CET 2014
 * XML source: /home/monordi/Cours/3A/TU/NLP/Exercices/ProjetNLP/WebRecipesProject/src/resources/desc/type/IngredientAnnotation.xml
 * @generated */
public class IngredientAnnotation extends Annotation {
  /** @generated
   * @ordered 
   */
  @SuppressWarnings ("hiding")
  public final static int typeIndexID = JCasRegistry.register(IngredientAnnotation.class);
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
  protected IngredientAnnotation() {/* intentionally empty block */}
    
  /** Internal - constructor used by generator 
   * @generated
   * @param addr low level Feature Structure reference
   * @param type the type of this Feature Structure 
   */
  public IngredientAnnotation(int addr, TOP_Type type) {
    super(addr, type);
    readObject();
  }
  
  /** @generated
   * @param jcas JCas to which this Feature Structure belongs 
   */
  public IngredientAnnotation(JCas jcas) {
    super(jcas);
    readObject();   
  } 

  /** @generated
   * @param jcas JCas to which this Feature Structure belongs
   * @param begin offset to the begin spot in the SofA
   * @param end offset to the end spot in the SofA 
  */  
  public IngredientAnnotation(JCas jcas, int begin, int end) {
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
  //* Feature: Amount

  /** getter for Amount - gets 
   * @generated
   * @return value of the feature 
   */
  public String getAmount() {
    if (IngredientAnnotation_Type.featOkTst && ((IngredientAnnotation_Type)jcasType).casFeat_Amount == null)
      jcasType.jcas.throwFeatMissing("Amount", "de.tudarmstadt.ukp.teaching.general.type.IngredientAnnotation");
    return jcasType.ll_cas.ll_getStringValue(addr, ((IngredientAnnotation_Type)jcasType).casFeatCode_Amount);}
    
  /** setter for Amount - sets  
   * @generated
   * @param v value to set into the feature 
   */
  public void setAmount(String v) {
    if (IngredientAnnotation_Type.featOkTst && ((IngredientAnnotation_Type)jcasType).casFeat_Amount == null)
      jcasType.jcas.throwFeatMissing("Amount", "de.tudarmstadt.ukp.teaching.general.type.IngredientAnnotation");
    jcasType.ll_cas.ll_setStringValue(addr, ((IngredientAnnotation_Type)jcasType).casFeatCode_Amount, v);}    
   
    
  //*--------------*
  //* Feature: NormalizedName

  /** getter for NormalizedName - gets 
   * @generated
   * @return value of the feature 
   */
  public String getNormalizedName() {
    if (IngredientAnnotation_Type.featOkTst && ((IngredientAnnotation_Type)jcasType).casFeat_NormalizedName == null)
      jcasType.jcas.throwFeatMissing("NormalizedName", "de.tudarmstadt.ukp.teaching.general.type.IngredientAnnotation");
    return jcasType.ll_cas.ll_getStringValue(addr, ((IngredientAnnotation_Type)jcasType).casFeatCode_NormalizedName);}
    
  /** setter for NormalizedName - sets  
   * @generated
   * @param v value to set into the feature 
   */
  public void setNormalizedName(String v) {
    if (IngredientAnnotation_Type.featOkTst && ((IngredientAnnotation_Type)jcasType).casFeat_NormalizedName == null)
      jcasType.jcas.throwFeatMissing("NormalizedName", "de.tudarmstadt.ukp.teaching.general.type.IngredientAnnotation");
    jcasType.ll_cas.ll_setStringValue(addr, ((IngredientAnnotation_Type)jcasType).casFeatCode_NormalizedName, v);}    
  }

    