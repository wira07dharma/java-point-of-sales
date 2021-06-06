/*
 * FrmBudgeting.java
 * @author  rusdianta
 * Created on March 7, 2005, 6:01 PM
 */

package com.dimata.aiso.form.masterdata;

//import javax.servlet.*;
import javax.servlet.http.*;

import com.dimata.qdep.form.*;
import com.dimata.aiso.entity.masterdata.*;

public class FrmBudgeting extends FRMHandler implements I_FRMInterface, I_FRMType {
    
    public static final String FRM_BUDGETING = "FRM_BUDGETING";
    
    public static final int FRM_BUDGET = 0;    
    public static String fieldNames[] = {        
        "BUDGET"
    };
    
    public static int fieldTypes[] = {        
        TYPE_FLOAT
    };    
    
    private AisoBudgeting aisoBudgeting;
    
    /** Creates a new instance of FrmBudgeting */
    public FrmBudgeting() {        
    }
    
    public FrmBudgeting(AisoBudgeting aisoBudgeting) {
        this.aisoBudgeting = aisoBudgeting;
    }
    
    public FrmBudgeting(HttpServletRequest request,
                        AisoBudgeting aisoBudgeting)
    {
        super(new FrmBudgeting(aisoBudgeting), request);
        this.aisoBudgeting = aisoBudgeting;
    }    
    
    public int getFieldSize() {
        return fieldNames.length;
    }
    
    public String[] getFieldNames() {
        return fieldNames;
    }    
    
    public int[] getFieldTypes() {
        return fieldTypes;
    }
    
    public String getFormName() {
        return FRM_BUDGETING;
    }
    
    public AisoBudgeting getEntityObject() {
        return aisoBudgeting;
    }
    
    public void requestEntityObject(AisoBudgeting aisoBudgeting) {
        try {
            this.requestParam();
            aisoBudgeting.setBudget(this.getDouble(FRM_BUDGET));            
            this.aisoBudgeting = aisoBudgeting;
        } catch (Exception error) {
            aisoBudgeting = new AisoBudgeting();
        }
    }    
}
