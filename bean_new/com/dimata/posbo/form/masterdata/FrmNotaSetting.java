
package com.dimata.posbo.form.masterdata;

/**
 *
 * @author Witar
 */

import com.dimata.posbo.entity.masterdata.NotaSetting;
import com.dimata.qdep.form.FRMHandler;
import com.dimata.qdep.form.I_FRMInterface;
import com.dimata.qdep.form.I_FRMType;
import javax.servlet.http.HttpServletRequest;

public class FrmNotaSetting 
extends FRMHandler 
implements I_FRMInterface, I_FRMType{

    private NotaSetting notaSetting;
    public static final String FRM_NOTA_SETTING = "FRM_NOTA_SETTING";
    
    public static final int FRM_FLD_POS_NOTA_SETTING_ID=0;
    public static final int FRM_FLD_LOCATION_ID=1;
    public static final int FRM_FLD_FOOTER_TEXT=2;

    public static String[] fieldNames = {
        "FRM_POS_NOTA_SETTING_ID",
        "FRM_LOCATION_ID",
        "FRM_FOOTER_TEXT"
    };
    
    public static int[] fieldTypes = {
        TYPE_LONG,
        TYPE_LONG,
        TYPE_STRING
    };
    public FrmNotaSetting(){
    }
    public FrmNotaSetting(NotaSetting notaSetting){
        this.notaSetting = notaSetting;
    }
    public FrmNotaSetting(HttpServletRequest request, NotaSetting notaSetting){
        super(new FrmNotaSetting(notaSetting),request);
        this.notaSetting = notaSetting;
    }
    public String getFormName() { return FRM_NOTA_SETTING; }
    public int[] getFieldTypes() { return fieldTypes; }
    public String[] getFieldNames() { return fieldNames; }
    public int getFieldSize() { return fieldNames.length; }
    public NotaSetting getEntityObject(){ return notaSetting; }
   
    public void requestEntityObject(NotaSetting notaSetting) { //melakukan 
        try{
            this.requestParam();
                notaSetting.setLocationId(getLong(FRM_FLD_LOCATION_ID)); 
                notaSetting.setFooterText(getString(FRM_FLD_FOOTER_TEXT));
            }catch(Exception e){
                System.out.println("Error on requestEntityObject : "+e.toString());
            }
    }
    
    
}
