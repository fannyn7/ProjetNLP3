
/* First created by JCasGen Thu Jan 15 11:40:18 CET 2015 */
package de.tudarmstadt.ukp.teaching.general.type;

import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.JCasRegistry;
import org.apache.uima.cas.impl.CASImpl;
import org.apache.uima.cas.impl.FSGenerator;
import org.apache.uima.cas.FeatureStructure;
import org.apache.uima.cas.impl.TypeImpl;
import org.apache.uima.cas.Type;
import org.apache.uima.cas.impl.FeatureImpl;
import org.apache.uima.cas.Feature;
import org.apache.uima.jcas.tcas.Annotation_Type;

/** 
 * Updated by JCasGen Thu Jan 29 11:32:16 CET 2015
 * @generated */
public class DirectivesAnnotation_Type extends Annotation_Type {
  /** @generated 
   * @return the generator for this type
   */
  @Override
  protected FSGenerator getFSGenerator() {return fsGenerator;}
  /** @generated */
  private final FSGenerator fsGenerator = 
    new FSGenerator() {
      public FeatureStructure createFS(int addr, CASImpl cas) {
  			 if (DirectivesAnnotation_Type.this.useExistingInstance) {
  			   // Return eq fs instance if already created
  		     FeatureStructure fs = DirectivesAnnotation_Type.this.jcas.getJfsFromCaddr(addr);
  		     if (null == fs) {
  		       fs = new DirectivesAnnotation(addr, DirectivesAnnotation_Type.this);
  			   DirectivesAnnotation_Type.this.jcas.putJfsFromCaddr(addr, fs);
  			   return fs;
  		     }
  		     return fs;
        } else return new DirectivesAnnotation(addr, DirectivesAnnotation_Type.this);
  	  }
    };
  /** @generated */
  @SuppressWarnings ("hiding")
  public final static int typeIndexID = DirectivesAnnotation.typeIndexID;
  /** @generated 
     @modifiable */
  @SuppressWarnings ("hiding")
  public final static boolean featOkTst = JCasRegistry.getFeatOkTst("de.tudarmstadt.ukp.teaching.general.type.DirectivesAnnotation");
 
  /** @generated */
  final Feature casFeat_Instruction;
  /** @generated */
  final int     casFeatCode_Instruction;
  /** @generated
   * @param addr low level Feature Structure reference
   * @return the feature value 
   */ 
  public String getInstruction(int addr) {
        if (featOkTst && casFeat_Instruction == null)
      jcas.throwFeatMissing("Instruction", "de.tudarmstadt.ukp.teaching.general.type.DirectivesAnnotation");
    return ll_cas.ll_getStringValue(addr, casFeatCode_Instruction);
  }
  /** @generated
   * @param addr low level Feature Structure reference
   * @param v value to set 
   */    
  public void setInstruction(int addr, String v) {
        if (featOkTst && casFeat_Instruction == null)
      jcas.throwFeatMissing("Instruction", "de.tudarmstadt.ukp.teaching.general.type.DirectivesAnnotation");
    ll_cas.ll_setStringValue(addr, casFeatCode_Instruction, v);}
    
  
 
  /** @generated */
  final Feature casFeat_NormalizedName;
  /** @generated */
  final int     casFeatCode_NormalizedName;
  /** @generated
   * @param addr low level Feature Structure reference
   * @return the feature value 
   */ 
  public String getNormalizedName(int addr) {
        if (featOkTst && casFeat_NormalizedName == null)
      jcas.throwFeatMissing("NormalizedName", "de.tudarmstadt.ukp.teaching.general.type.DirectivesAnnotation");
    return ll_cas.ll_getStringValue(addr, casFeatCode_NormalizedName);
  }
  /** @generated
   * @param addr low level Feature Structure reference
   * @param v value to set 
   */    
  public void setNormalizedName(int addr, String v) {
        if (featOkTst && casFeat_NormalizedName == null)
      jcas.throwFeatMissing("NormalizedName", "de.tudarmstadt.ukp.teaching.general.type.DirectivesAnnotation");
    ll_cas.ll_setStringValue(addr, casFeatCode_NormalizedName, v);}
    
  
 
  /** @generated */
  final Feature casFeat_Ingredient;
  /** @generated */
  final int     casFeatCode_Ingredient;
  /** @generated
   * @param addr low level Feature Structure reference
   * @return the feature value 
   */ 
  public String getIngredient(int addr) {
        if (featOkTst && casFeat_Ingredient == null)
      jcas.throwFeatMissing("Ingredient", "de.tudarmstadt.ukp.teaching.general.type.DirectivesAnnotation");
    return ll_cas.ll_getStringValue(addr, casFeatCode_Ingredient);
  }
  /** @generated
   * @param addr low level Feature Structure reference
   * @param v value to set 
   */    
  public void setIngredient(int addr, String v) {
        if (featOkTst && casFeat_Ingredient == null)
      jcas.throwFeatMissing("Ingredient", "de.tudarmstadt.ukp.teaching.general.type.DirectivesAnnotation");
    ll_cas.ll_setStringValue(addr, casFeatCode_Ingredient, v);}
    
  
 
  /** @generated */
  final Feature casFeat_ResultingEntity;
  /** @generated */
  final int     casFeatCode_ResultingEntity;
  /** @generated
   * @param addr low level Feature Structure reference
   * @return the feature value 
   */ 
  public String getResultingEntity(int addr) {
        if (featOkTst && casFeat_ResultingEntity == null)
      jcas.throwFeatMissing("ResultingEntity", "de.tudarmstadt.ukp.teaching.general.type.DirectivesAnnotation");
    return ll_cas.ll_getStringValue(addr, casFeatCode_ResultingEntity);
  }
  /** @generated
   * @param addr low level Feature Structure reference
   * @param v value to set 
   */    
  public void setResultingEntity(int addr, String v) {
        if (featOkTst && casFeat_ResultingEntity == null)
      jcas.throwFeatMissing("ResultingEntity", "de.tudarmstadt.ukp.teaching.general.type.DirectivesAnnotation");
    ll_cas.ll_setStringValue(addr, casFeatCode_ResultingEntity, v);}
    
  



  /** initialize variables to correspond with Cas Type and Features
	 * @generated
	 * @param jcas JCas
	 * @param casType Type 
	 */
  public DirectivesAnnotation_Type(JCas jcas, Type casType) {
    super(jcas, casType);
    casImpl.getFSClassRegistry().addGeneratorForType((TypeImpl)this.casType, getFSGenerator());

 
    casFeat_Instruction = jcas.getRequiredFeatureDE(casType, "Instruction", "uima.cas.String", featOkTst);
    casFeatCode_Instruction  = (null == casFeat_Instruction) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_Instruction).getCode();

 
    casFeat_NormalizedName = jcas.getRequiredFeatureDE(casType, "NormalizedName", "uima.cas.String", featOkTst);
    casFeatCode_NormalizedName  = (null == casFeat_NormalizedName) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_NormalizedName).getCode();

 
    casFeat_Ingredient = jcas.getRequiredFeatureDE(casType, "Ingredient", "uima.cas.String", featOkTst);
    casFeatCode_Ingredient  = (null == casFeat_Ingredient) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_Ingredient).getCode();

 
    casFeat_ResultingEntity = jcas.getRequiredFeatureDE(casType, "ResultingEntity", "uima.cas.String", featOkTst);
    casFeatCode_ResultingEntity  = (null == casFeat_ResultingEntity) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_ResultingEntity).getCode();

  }
}



    