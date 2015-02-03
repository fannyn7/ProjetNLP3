
/* First created by JCasGen Mon Dec 15 18:17:26 CET 2014 */
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
 * Updated by JCasGen Tue Dec 30 13:27:43 CET 2014
 * @generated */
public class IngredientAnnotation_Type extends Annotation_Type {
  /** @generated 
   * @return the generator for this type
   */
  @Override
  protected FSGenerator getFSGenerator() {return fsGenerator;}
  /** @generated */
  private final FSGenerator fsGenerator = 
    new FSGenerator() {
      public FeatureStructure createFS(int addr, CASImpl cas) {
  			 if (IngredientAnnotation_Type.this.useExistingInstance) {
  			   // Return eq fs instance if already created
  		     FeatureStructure fs = IngredientAnnotation_Type.this.jcas.getJfsFromCaddr(addr);
  		     if (null == fs) {
  		       fs = new IngredientAnnotation(addr, IngredientAnnotation_Type.this);
  			   IngredientAnnotation_Type.this.jcas.putJfsFromCaddr(addr, fs);
  			   return fs;
  		     }
  		     return fs;
        } else return new IngredientAnnotation(addr, IngredientAnnotation_Type.this);
  	  }
    };
  /** @generated */
  @SuppressWarnings ("hiding")
  public final static int typeIndexID = IngredientAnnotation.typeIndexID;
  /** @generated 
     @modifiable */
  @SuppressWarnings ("hiding")
  public final static boolean featOkTst = JCasRegistry.getFeatOkTst("de.tudarmstadt.ukp.teaching.general.type.IngredientAnnotation");
 
  /** @generated */
  final Feature casFeat_Amount;
  /** @generated */
  final int     casFeatCode_Amount;
  /** @generated
   * @param addr low level Feature Structure reference
   * @return the feature value 
   */ 
  public String getAmount(int addr) {
        if (featOkTst && casFeat_Amount == null)
      jcas.throwFeatMissing("Amount", "de.tudarmstadt.ukp.teaching.general.type.IngredientAnnotation");
    return ll_cas.ll_getStringValue(addr, casFeatCode_Amount);
  }
  /** @generated
   * @param addr low level Feature Structure reference
   * @param v value to set 
   */    
  public void setAmount(int addr, String v) {
        if (featOkTst && casFeat_Amount == null)
      jcas.throwFeatMissing("Amount", "de.tudarmstadt.ukp.teaching.general.type.IngredientAnnotation");
    ll_cas.ll_setStringValue(addr, casFeatCode_Amount, v);}
    
  
 
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
      jcas.throwFeatMissing("NormalizedName", "de.tudarmstadt.ukp.teaching.general.type.IngredientAnnotation");
    return ll_cas.ll_getStringValue(addr, casFeatCode_NormalizedName);
  }
  /** @generated
   * @param addr low level Feature Structure reference
   * @param v value to set 
   */    
  public void setNormalizedName(int addr, String v) {
        if (featOkTst && casFeat_NormalizedName == null)
      jcas.throwFeatMissing("NormalizedName", "de.tudarmstadt.ukp.teaching.general.type.IngredientAnnotation");
    ll_cas.ll_setStringValue(addr, casFeatCode_NormalizedName, v);}
    
  



  /** initialize variables to correspond with Cas Type and Features
	 * @generated
	 * @param jcas JCas
	 * @param casType Type 
	 */
  public IngredientAnnotation_Type(JCas jcas, Type casType) {
    super(jcas, casType);
    casImpl.getFSClassRegistry().addGeneratorForType((TypeImpl)this.casType, getFSGenerator());

 
    casFeat_Amount = jcas.getRequiredFeatureDE(casType, "Amount", "uima.cas.String", featOkTst);
    casFeatCode_Amount  = (null == casFeat_Amount) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_Amount).getCode();

 
    casFeat_NormalizedName = jcas.getRequiredFeatureDE(casType, "NormalizedName", "uima.cas.String", featOkTst);
    casFeatCode_NormalizedName  = (null == casFeat_NormalizedName) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_NormalizedName).getCode();

  }
}



    