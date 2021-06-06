/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.dimata.aiso.form.masterdata;

import com.dimata.aiso.entity.masterdata.BussinessCenterBudget;
import com.dimata.qdep.form.FRMHandler;
import com.dimata.qdep.form.I_FRMInterface;
import com.dimata.qdep.form.I_FRMType;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author dwi
 */
public class FrmBussCenterBudget extends FRMHandler implements I_FRMInterface, I_FRMType{
    public static final String FRM_BUSSINESS_CENTER_BUDGET = "FRM_BUSSINESS_CENTER_BUDGET";      
   
    public static final int FRM_ID_PERKIRAAN = 0;
    public static final int FRM_PERIODE_ID = 1;
    public static final int FRM_BUDGET = 2;
    public static final int FRM_BUSS_CENTER_ID = 3;

    public static String[] fieldNames =
            {
                "FRM_ID_PERKIRAAN",
                "FRM_PERIODE_ID",
                "FRM_BUDGET",
                "FRM_BUSS_CENTER_ID"
            };

    public static int[] fieldTypes =
            {
                TYPE_STRING + ENTRY_REQUIRED,
                TYPE_LONG,
                TYPE_FLOAT,
                TYPE_LONG
            };

    private BussinessCenterBudget objBussinessCenterBudget;

    public FrmBussCenterBudget(BussinessCenterBudget objBussinessCenterBudget) {
        this.objBussinessCenterBudget = objBussinessCenterBudget;
    }

    public FrmBussCenterBudget(HttpServletRequest request, BussinessCenterBudget objBussinessCenterBudget) {
        super(new FrmBussCenterBudget(objBussinessCenterBudget), request);
        this.objBussinessCenterBudget = objBussinessCenterBudget;
    }

    public String getFormName() {
        return FRM_BUSSINESS_CENTER_BUDGET;
    }

    public int[] getFieldTypes() {
        return fieldTypes;
    }

    public String[] getFieldNames() {
        return fieldNames;
    }

    public int getFieldSize() {
        return fieldNames.length;
    }

    public BussinessCenterBudget getEntityObject() {
        return objBussinessCenterBudget;
    }

    public void requestEntityObject(BussinessCenterBudget objBussinessCenterBudget) {
        try {
            this.requestParam();
            objBussinessCenterBudget.setIdPerkiraan(this.getLong(FRM_ID_PERKIRAAN));
            objBussinessCenterBudget.setPeriodeId(this.getLong(FRM_PERIODE_ID));
            objBussinessCenterBudget.setBudget(this.getDouble(FRM_BUDGET));
            objBussinessCenterBudget.setBussCenterId(this.getLong(FRM_BUSS_CENTER_ID));
            
            this.objBussinessCenterBudget = objBussinessCenterBudget;
        } catch (Exception e) {
            objBussinessCenterBudget = new BussinessCenterBudget();
        }
    }
}
