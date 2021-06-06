package com.dimata.hanoman.form.masterdata;

import com.dimata.posbo.entity.masterdata.CaCommission;
import com.dimata.qdep.form.FRMHandler;
import com.dimata.qdep.form.I_FRMInterface;
import com.dimata.qdep.form.I_FRMType;
import javax.servlet.http.HttpServletRequest;

public class FrmCatCommission extends FRMHandler implements I_FRMInterface, I_FRMType {

    private CaCommission entCatCommission;
    public static final String FRM_NAME_COMMISSION		=  "FRM_COMMISSION_OUTLET_PIC" ;
        
    public static final int FRM_FIELD_CAT_COMMISSION_ID = 0;
    public static final int FRM_FIELD_CATEGORY_ID = 1;
    public static final int FRM_FIELD_PERCENTAGE_COMMISSION = 2;
    public static final int FRM_FIELD_START_DATE = 3;
    public static final int FRM_FIELD_END_DATE = 4;
    public static final int FRM_FIELD_DESCRIPTION = 5;
    public static final int FRM_FIELD_STATUS = 6;
    
    

    public static final String[] fieldNames
            = {
                "FRM_FIELD_POS_CATEG_COMMISSION_ID",
                "FRM_FIELD_CATEGORY_ID",
                "FRM_FIELD_PERCENTAGE_COMMISSION",
                "FRM_FIELD_START_DATE",
                "FRM_FIELD_END_DATE",
                "FRM_FIELD_DESCRIPTION",
                "FRM_FIELD_STATUS",
            };

    public static final int[] fieldTypes
            = {
                TYPE_LONG,
                TYPE_LONG + ENTRY_REQUIRED,
                TYPE_FLOAT + ENTRY_REQUIRED,
                TYPE_DATE + ENTRY_REQUIRED,
                TYPE_DATE + ENTRY_REQUIRED,
                TYPE_STRING + ENTRY_REQUIRED,
                TYPE_INT
            };

    public FrmCatCommission() {
    }

    public FrmCatCommission(CaCommission entCatCommission) {
        this.entCatCommission = entCatCommission;
    }

    public FrmCatCommission(HttpServletRequest request, CaCommission entCatCommission) {
        super(new FrmCatCommission(entCatCommission), request);
        this.entCatCommission = entCatCommission;
    }

    public String getFormName() {
          return FRM_NAME_COMMISSION;
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

    public CaCommission getEntityObject() {
        return entCatCommission;
    }

    public void requestEntityObject(CaCommission entCatCommission) {
        try {
            String tmpDateFormat = this.getDateFormat();
            this.setDateFormat("yyyy-MM-dd");
            this.requestParam();
            entCatCommission.setOID(getLong(FRM_FIELD_CAT_COMMISSION_ID));
            entCatCommission.setPercentage(getFloat(FRM_FIELD_PERCENTAGE_COMMISSION));
            entCatCommission.setStartDate(getDate(FRM_FIELD_START_DATE));
            entCatCommission.setEndDate(getDate(FRM_FIELD_END_DATE));
            this.setDateFormat(tmpDateFormat);
            entCatCommission.setDedcription(getString(FRM_FIELD_DESCRIPTION));
            entCatCommission.setComStatus(getInt(FRM_FIELD_STATUS));
            entCatCommission.setCatId(getLong(FRM_FIELD_CATEGORY_ID));
            
        } catch (Exception e) {
            System.out.println("Error on requestEntityObject : " + e.toString());
        }
    }

}
