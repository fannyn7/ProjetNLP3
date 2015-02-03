

/* First created by JCasGen Thu Jan 15 11:40:18 CET 2015 */
package de.tudarmstadt.ukp.teaching.general.type;

import org.apache.uima.jcas.JCas; 
import org.apache.uima.jcas.JCasRegistry;
import org.apache.uima.jcas.cas.TOP_Type;

import org.apache.uima.jcas.cas.StringList;
import org.apache.uima.jcas.tcas.Annotation;


/** 
 * Updated by JCasGen Thu Jan 29 11:32:16 CET 2015
 * XML source: /Users/simondif/Documents/workspace/ProjetNLP/WebRecipesProject/src/main/resources/desc/type/DirectivesAnnotation.xml
 * @generated */
public class DirectivesAnnotation extends Annotation {
  /** @generated
   * @ordered 
   */
  @SuppressWarnings ("hiding")
  public final static int typeIndexID = JCasRegistry.register(DirectivesAnnotation.class);
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
  protected DirectivesAnnotation() {/* intentionally empty block */}
    
  /** Internal - constructor used by generator 
   * @generated
   * @param addr low level Feature Structure reference
   * @param type the type of this Feature Structure 
   */
  public DirectivesAnnotation(int addr, TOP_Type type) {
    super(addr, type);
    readObject();
  }
  
  /** @generated
   * @param jcas JCas to which this Feature Structure belongs 
   */
  public DirectivesAnnotation(JCas jcas) {
    super(jcas);
    readObject();   
  } 

  /** @generated
   * @param jcas JCas to which this Feature Structure belongs
   * @param begin offset to the begin spot in the SofA
   * @param end offset to the end spot in the SofA 
  */  
  public DirectivesAnnotation(JCas jcas, int begin, int end) {
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
  //* Feature: Instruction

  /** getter for Instruction - gets 
   * @generated
   * @return value of the feature 
   */
  public String getInstruction() {
    if (DirectivesAnnotation_Type.featOkTst && ((DirectivesAnnotation_Type)jcasType).casFeat_Instruction == null)
      jcasType.jcas.throwFeatMissing("Instruction", "de.tudarmstadt.ukp.teaching.general.type.DirectivesAnnotation");
    return jcasType.ll_cas.ll_getStringValue(addr, ((DirectivesAnnotation_Type)jcasType).casFeatCode_Instruction);}
    
  /** setter for Instruction - sets  
   * @generated
   * @param v value to set into the feature 
   */
  public void setInstruction(String v) {
    if (DirectivesAnnotation_Type.featOkTst && ((DirectivesAnnotation_Type)jcasType).casFeat_Instruction == null)
      jcasType.jcas.throwFeatMissing("Instruction", "de.tudarmstadt.ukp.teaching.general.type.DirectivesAnnotation");
    jcasType.ll_cas.ll_setStringValue(addr, ((DirectivesAnnotation_Type)jcasType).casFeatCode_Instruction, v);}    
   
    
  //*--------------*
  //* Feature: NormalizedName

  /** getter for NormalizedName - gets 
   * @generated
   * @return value of the feature 
   */
  public String getNormalizedName() {
    if (DirectivesAnnotation_Type.featOkTst && ((DirectivesAnnotation_Type)jcasType).casFeat_NormalizedName == null)
      jcasType.jcas.throwFeatMissing("NormalizedName", "de.tudarmstadt.ukp.teaching.general.type.DirectivesAnnotation");
    return jcasType.ll_cas.ll_getStringValue(addr, ((DirectivesAnnotation_Type)jcasType).casFeatCode_NormalizedName);}
    
  /** setter for NormalizedName - sets  
   * @generated
   * @param v value to set into the feature 
   */
  public void setNormalizedName(String v) {
    if (DirectivesAnnotation_Type.featOkTst && ((DirectivesAnnotation_Type)jcasType).casFeat_NormalizedName == null)
      jcasType.jcas.throwFeatMissing("NormalizedName", "de.tudarmstadt.ukp.teaching.general.type.DirectivesAnnotation");
    jcasType.ll_cas.ll_setStringValue(addr, ((DirectivesAnnotation_Type)jcasType).casFeatCode_NormalizedName, v);}    
   
    
  //*--------------*
  //* Feature: Ingredient

  /** getter for Ingredient - gets 
   * @generated
   * @return value of the feature 
   */
  public String getIngredient() {
    if (DirectivesAnnotation_Type.featOkTst && ((DirectivesAnnotation_Type)jcasType).casFeat_Ingredient == null)
      jcasType.jcas.throwFeatMissing("Ingredient", "de.tudarmstadt.ukp.teaching.general.type.DirectivesAnnotation");
    return jcasType.ll_cas.ll_getStringValue(addr, ((DirectivesAnnotation_Type)jcasType).casFeatCode_Ingredient);}
    
  /** setter for Ingredient - sets  
   * @generated
   * @param v value to set into the feature 
   */
  public void setIngredient(String v) {
    if (DirectivesAnnotation_Type.featOkTst && ((DirectivesAnnotation_Type)jcasType).casFeat_Ingredient == null)
      jcasType.jcas.throwFeatMissing("Ingredient", "de.tudarmstadt.ukp.teaching.general.type.DirectivesAnnotation");
    jcasType.ll_cas.ll_setStringValue(addr, ((DirectivesAnnotation_Type)jcasType).casFeatCode_Ingredient, v);}    
   
    
  //*--------------*
  //* Feature: ResultingEntity

  /** getter for ResultingEntity - gets 
   * @generated
   * @return value of the feature 
   */
  public String getResultingEntity() {
    if (DirectivesAnnotation_Type.featOkTst && ((DirectivesAnnotation_Type)jcasType).casFeat_ResultingEntity == null)
      jcasType.jcas.throwFeatMissing("ResultingEntity", "de.tudarmstadt.ukp.teaching.general.type.DirectivesAnnotation");
    return jcasType.ll_cas.ll_getStringValue(addr, ((DirectivesAnnotation_Type)jcasType).casFeatCode_ResultingEntity);}
    
  /** setter for ResultingEntity - sets  
   * @generated
   * @param v value to set into the feature 
   */
  public void setResultingEntity(String v) {
    if (DirectivesAnnotation_Type.featOkTst && ((DirectivesAnnotation_Type)jcasType).casFeat_ResultingEntity == null)
      jcasType.jcas.throwFeatMissing("ResultingEntity", "de.tudarmstadt.ukp.teaching.general.type.DirectivesAnnotation");
    jcasType.ll_cas.ll_setStringValue(addr, ((DirectivesAnnotation_Type)jcasType).casFeatCode_ResultingEntity, v);}    
  }

    