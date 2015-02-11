

/* First created by JCasGen Mon Jan 12 18:31:51 CET 2015 */
package de.tudarmstadt.ukp.teaching.general.type;

import org.apache.uima.jcas.JCas; 
import org.apache.uima.jcas.JCasRegistry;
import org.apache.uima.jcas.cas.TOP_Type;

import org.apache.uima.jcas.tcas.Annotation;


/** 
 * Updated by JCasGen Tue Feb 10 13:52:39 CET 2015
 * XML source: /home/monordi/Cours/3A/TU/NLP/Exercices/ProjetNLP3/WebRecipesProject/src/main/resources/desc/type/UnitAnnotation.xml
 * @generated */
public class UnitAnnotation extends Annotation {
  /** @generated
   * @ordered 
   */
  @SuppressWarnings ("hiding")
  public final static int typeIndexID = JCasRegistry.register(UnitAnnotation.class);
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
  protected UnitAnnotation() {/* intentionally empty block */}
    
  /** Internal - constructor used by generator 
   * @generated
   * @param addr low level Feature Structure reference
   * @param type the type of this Feature Structure 
   */
  public UnitAnnotation(int addr, TOP_Type type) {
    super(addr, type);
    readObject();
  }
  
  /** @generated
   * @param jcas JCas to which this Feature Structure belongs 
   */
  public UnitAnnotation(JCas jcas) {
    super(jcas);
    readObject();   
  } 

  /** @generated
   * @param jcas JCas to which this Feature Structure belongs
   * @param begin offset to the begin spot in the SofA
   * @param end offset to the end spot in the SofA 
  */  
  public UnitAnnotation(JCas jcas, int begin, int end) {
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
  //* Feature: Unit

  /** getter for Unit - gets 
   * @generated
   * @return value of the feature 
   */
  public String getUnit() {
    if (UnitAnnotation_Type.featOkTst && ((UnitAnnotation_Type)jcasType).casFeat_Unit == null)
      jcasType.jcas.throwFeatMissing("Unit", "de.tudarmstadt.ukp.teaching.general.type.UnitAnnotation");
    return jcasType.ll_cas.ll_getStringValue(addr, ((UnitAnnotation_Type)jcasType).casFeatCode_Unit);}
    
  /** setter for Unit - sets  
   * @generated
   * @param v value to set into the feature 
   */
  public void setUnit(String v) {
    if (UnitAnnotation_Type.featOkTst && ((UnitAnnotation_Type)jcasType).casFeat_Unit == null)
      jcasType.jcas.throwFeatMissing("Unit", "de.tudarmstadt.ukp.teaching.general.type.UnitAnnotation");
    jcasType.ll_cas.ll_setStringValue(addr, ((UnitAnnotation_Type)jcasType).casFeatCode_Unit, v);}    
   
    
  //*--------------*
  //* Feature: UnitToken

  /** getter for UnitToken - gets 
   * @generated
   * @return value of the feature 
   */
  public Annotation getUnitToken() {
    if (UnitAnnotation_Type.featOkTst && ((UnitAnnotation_Type)jcasType).casFeat_UnitToken == null)
      jcasType.jcas.throwFeatMissing("UnitToken", "de.tudarmstadt.ukp.teaching.general.type.UnitAnnotation");
    return (Annotation)(jcasType.ll_cas.ll_getFSForRef(jcasType.ll_cas.ll_getRefValue(addr, ((UnitAnnotation_Type)jcasType).casFeatCode_UnitToken)));}
    
  /** setter for UnitToken - sets  
   * @generated
   * @param v value to set into the feature 
   */
  public void setUnitToken(Annotation v) {
    if (UnitAnnotation_Type.featOkTst && ((UnitAnnotation_Type)jcasType).casFeat_UnitToken == null)
      jcasType.jcas.throwFeatMissing("UnitToken", "de.tudarmstadt.ukp.teaching.general.type.UnitAnnotation");
    jcasType.ll_cas.ll_setRefValue(addr, ((UnitAnnotation_Type)jcasType).casFeatCode_UnitToken, jcasType.ll_cas.ll_getFSRef(v));}    
   
    
  //*--------------*
  //* Feature: Quantity

  /** getter for Quantity - gets 
   * @generated
   * @return value of the feature 
   */
  public String getQuantity() {
    if (UnitAnnotation_Type.featOkTst && ((UnitAnnotation_Type)jcasType).casFeat_Quantity == null)
      jcasType.jcas.throwFeatMissing("Quantity", "de.tudarmstadt.ukp.teaching.general.type.UnitAnnotation");
    return jcasType.ll_cas.ll_getStringValue(addr, ((UnitAnnotation_Type)jcasType).casFeatCode_Quantity);}
    
  /** setter for Quantity - sets  
   * @generated
   * @param v value to set into the feature 
   */
  public void setQuantity(String v) {
    if (UnitAnnotation_Type.featOkTst && ((UnitAnnotation_Type)jcasType).casFeat_Quantity == null)
      jcasType.jcas.throwFeatMissing("Quantity", "de.tudarmstadt.ukp.teaching.general.type.UnitAnnotation");
    jcasType.ll_cas.ll_setStringValue(addr, ((UnitAnnotation_Type)jcasType).casFeatCode_Quantity, v);}    
   
    
  //*--------------*
  //* Feature: TypeOf

  /** getter for TypeOf - gets 
   * @generated
   * @return value of the feature 
   */
  public String getTypeOf() {
    if (UnitAnnotation_Type.featOkTst && ((UnitAnnotation_Type)jcasType).casFeat_TypeOf == null)
      jcasType.jcas.throwFeatMissing("TypeOf", "de.tudarmstadt.ukp.teaching.general.type.UnitAnnotation");
    return jcasType.ll_cas.ll_getStringValue(addr, ((UnitAnnotation_Type)jcasType).casFeatCode_TypeOf);}
    
  /** setter for TypeOf - sets  
   * @generated
   * @param v value to set into the feature 
   */
  public void setTypeOf(String v) {
    if (UnitAnnotation_Type.featOkTst && ((UnitAnnotation_Type)jcasType).casFeat_TypeOf == null)
      jcasType.jcas.throwFeatMissing("TypeOf", "de.tudarmstadt.ukp.teaching.general.type.UnitAnnotation");
    jcasType.ll_cas.ll_setStringValue(addr, ((UnitAnnotation_Type)jcasType).casFeatCode_TypeOf, v);}    
  }

    