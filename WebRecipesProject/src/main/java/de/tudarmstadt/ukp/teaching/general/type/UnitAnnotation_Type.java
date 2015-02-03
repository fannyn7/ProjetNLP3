
/* First created by JCasGen Mon Jan 12 18:31:51 CET 2015 */
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
 * Updated by JCasGen Thu Jan 15 15:08:17 CET 2015
 * @generated */
public class UnitAnnotation_Type extends Annotation_Type {
  /** @generated 
   * @return the generator for this type
   */
  @Override
  protected FSGenerator getFSGenerator() {return fsGenerator;}
  /** @generated */
  private final FSGenerator fsGenerator = 
    new FSGenerator() {
      public FeatureStructure createFS(int addr, CASImpl cas) {
  			 if (UnitAnnotation_Type.this.useExistingInstance) {
  			   // Return eq fs instance if already created
  		     FeatureStructure fs = UnitAnnotation_Type.this.jcas.getJfsFromCaddr(addr);
  		     if (null == fs) {
  		       fs = new UnitAnnotation(addr, UnitAnnotation_Type.this);
  			   UnitAnnotation_Type.this.jcas.putJfsFromCaddr(addr, fs);
  			   return fs;
  		     }
  		     return fs;
        } else return new UnitAnnotation(addr, UnitAnnotation_Type.this);
  	  }
    };
  /** @generated */
  @SuppressWarnings ("hiding")
  public final static int typeIndexID = UnitAnnotation.typeIndexID;
  /** @generated 
     @modifiable */
  @SuppressWarnings ("hiding")
  public final static boolean featOkTst = JCasRegistry.getFeatOkTst("de.tudarmstadt.ukp.teaching.general.type.UnitAnnotation");
 
  /** @generated */
  final Feature casFeat_Unit;
  /** @generated */
  final int     casFeatCode_Unit;
  /** @generated
   * @param addr low level Feature Structure reference
   * @return the feature value 
   */ 
  public String getUnit(int addr) {
        if (featOkTst && casFeat_Unit == null)
      jcas.throwFeatMissing("Unit", "de.tudarmstadt.ukp.teaching.general.type.UnitAnnotation");
    return ll_cas.ll_getStringValue(addr, casFeatCode_Unit);
  }
  /** @generated
   * @param addr low level Feature Structure reference
   * @param v value to set 
   */    
  public void setUnit(int addr, String v) {
        if (featOkTst && casFeat_Unit == null)
      jcas.throwFeatMissing("Unit", "de.tudarmstadt.ukp.teaching.general.type.UnitAnnotation");
    ll_cas.ll_setStringValue(addr, casFeatCode_Unit, v);}
    
  
 
  /** @generated */
  final Feature casFeat_Quantity;
  /** @generated */
  final int     casFeatCode_Quantity;
  /** @generated
   * @param addr low level Feature Structure reference
   * @return the feature value 
   */ 
  public String getQuantity(int addr) {
        if (featOkTst && casFeat_Quantity == null)
      jcas.throwFeatMissing("Quantity", "de.tudarmstadt.ukp.teaching.general.type.UnitAnnotation");
    return ll_cas.ll_getStringValue(addr, casFeatCode_Quantity);
  }
  /** @generated
   * @param addr low level Feature Structure reference
   * @param v value to set 
   */    
  public void setQuantity(int addr, String v) {
        if (featOkTst && casFeat_Quantity == null)
      jcas.throwFeatMissing("Quantity", "de.tudarmstadt.ukp.teaching.general.type.UnitAnnotation");
    ll_cas.ll_setStringValue(addr, casFeatCode_Quantity, v);}
    
  
 
  /** @generated */
  final Feature casFeat_TypeOf;
  /** @generated */
  final int     casFeatCode_TypeOf;
  /** @generated
   * @param addr low level Feature Structure reference
   * @return the feature value 
   */ 
  public String getTypeOf(int addr) {
        if (featOkTst && casFeat_TypeOf == null)
      jcas.throwFeatMissing("TypeOf", "de.tudarmstadt.ukp.teaching.general.type.UnitAnnotation");
    return ll_cas.ll_getStringValue(addr, casFeatCode_TypeOf);
  }
  /** @generated
   * @param addr low level Feature Structure reference
   * @param v value to set 
   */    
  public void setTypeOf(int addr, String v) {
        if (featOkTst && casFeat_TypeOf == null)
      jcas.throwFeatMissing("TypeOf", "de.tudarmstadt.ukp.teaching.general.type.UnitAnnotation");
    ll_cas.ll_setStringValue(addr, casFeatCode_TypeOf, v);}
    
  



  /** initialize variables to correspond with Cas Type and Features
	 * @generated
	 * @param jcas JCas
	 * @param casType Type 
	 */
  public UnitAnnotation_Type(JCas jcas, Type casType) {
    super(jcas, casType);
    casImpl.getFSClassRegistry().addGeneratorForType((TypeImpl)this.casType, getFSGenerator());

 
    casFeat_Unit = jcas.getRequiredFeatureDE(casType, "Unit", "uima.cas.String", featOkTst);
    casFeatCode_Unit  = (null == casFeat_Unit) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_Unit).getCode();

 
    casFeat_Quantity = jcas.getRequiredFeatureDE(casType, "Quantity", "uima.cas.String", featOkTst);
    casFeatCode_Quantity  = (null == casFeat_Quantity) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_Quantity).getCode();

 
    casFeat_TypeOf = jcas.getRequiredFeatureDE(casType, "TypeOf", "uima.cas.String", featOkTst);
    casFeatCode_TypeOf  = (null == casFeat_TypeOf) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_TypeOf).getCode();

  }
}



    