package com.dimata.aiso.form.masterdata;

// import servlet
import javax.servlet.*;
import javax.servlet.http.*;

// import qdep 
import com.dimata.qdep.form.*;

// import aiso 
import com.dimata.aiso.entity.masterdata.*;
import com.dimata.interfaces.chartofaccount.I_ChartOfAccountGroup;
 
public class FrmPerkiraan extends FRMHandler implements I_FRMInterface, I_FRMType
{

    public static final String FRM_PERKIRAAN = "FRM_PERKIRAAN";  

    public static final int FRM_IDPARENT             = 0;
    public static final int FRM_NOPERKIRAAN          = 1;    
    public static final int FRM_LEVEL                = 2;
    public static final int FRM_NAMA		     = 3;
    public static final int FRM_TANDA_DEBET_KREDIT   = 4;
    public static final int FRM_POSTABLE             = 5;
    public static final int FRM_DEPARTMENT_ID        = 6;
    public static final int FRM_ACCOUNT_NAME_ENGLISH = 7;
    public static final int FRM_WEIGHT               = 8;
    public static final int FRM_GENERAL_ACCOUNT_LINK = 9;    
    public static final int FRM_ACCOUNT_GROUP        = 10;
    public static final int FRM_COMPANY_ID = 11;    
    public static final int FRM_ARAP_ACCOUNT = 12;    
    public static final int FRM_EXPENSE_TYPE = 13;    
    public static final int FRM_EXPENSE_FIXED_VARIABLE = 14;    

    public static String[] fieldNames = {
        "ID_PARENT",
        "NOMOR_PERKIRAAN",        
        "LEVEL",
        "NAMA",
        "TANDA_DEBET_KREDIT",
        "POSTABLE",
        "DEPARTMENT_ID",
        "ACCOUNT_NAME_ENGLISH",
        "WEIGHT",
        "GENERAL_ACCOUNT_LINK",
        "ACCOUNT_GROUP",
        "COMPANY_ID",
        "ARAP_ACCOUNT",
        "EXPENSE_TYPE",
        "EXPENSE_FIXED_VARIABLE"
    };
    
    public static int[] fieldTypes = {
        TYPE_LONG,
        TYPE_STRING + ENTRY_REQUIRED,
        TYPE_INT,
        TYPE_STRING + ENTRY_REQUIRED,
        TYPE_INT,
        TYPE_INT,
        TYPE_LONG,
        TYPE_STRING + ENTRY_REQUIRED,
        TYPE_FLOAT,
        TYPE_LONG,
        TYPE_INT,
        TYPE_LONG,
        TYPE_INT,
        TYPE_INT,
        TYPE_INT
    };

    private Perkiraan perkiraan;

    public FrmPerkiraan(Perkiraan perkiraan){
        this.perkiraan = perkiraan;
    }
    
    public FrmPerkiraan(HttpServletRequest request, Perkiraan perkiraan) {
        super(new FrmPerkiraan(perkiraan), request);
        this.perkiraan = perkiraan;
    }

    public String getFormName() {
        return FRM_PERKIRAAN;
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

    public Perkiraan getEntityObject(){
        return perkiraan;
    }

    public void requestEntityObject(Perkiraan perkiraan)
    {
        try 
        {
            this.requestParam();
            
            perkiraan.setIdParent(this.getLong(FRM_IDPARENT));            
            perkiraan.setNoPerkiraan(this.getString(FRM_NOPERKIRAAN));            
            perkiraan.setLevel(this.getInt(FRM_LEVEL));
            perkiraan.setNama(this.getString(FRM_NAMA));
            perkiraan.setTandaDebetKredit(this.getInt(FRM_TANDA_DEBET_KREDIT));
            perkiraan.setPostable(this.getInt(FRM_POSTABLE));
            perkiraan.setDepartmentId(this.getLong(FRM_DEPARTMENT_ID));
            perkiraan.setAccountNameEnglish(this.getString(FRM_ACCOUNT_NAME_ENGLISH));
            perkiraan.setWeight(this.getDouble(FRM_WEIGHT));
            perkiraan.setGeneralAccountLink(this.getLong(FRM_GENERAL_ACCOUNT_LINK));
            perkiraan.setAccountGroup(this.getInt(FRM_ACCOUNT_GROUP));
            perkiraan.setCompanyId(this.getLong(FRM_COMPANY_ID));
            perkiraan.setArapAccount(this.getInt(FRM_ARAP_ACCOUNT));
            if(this.getInt(FRM_ACCOUNT_GROUP) == I_ChartOfAccountGroup.ACC_GROUP_EXPENSE){
                perkiraan.setExpenseType(this.getInt(FRM_EXPENSE_TYPE));
                if(this.getInt(FRM_EXPENSE_TYPE) == PstPerkiraan.DIRECT_EXPENSE){
                    perkiraan.setExpenseFixedVar(this.getInt(FRM_EXPENSE_FIXED_VARIABLE));                    
                }else{
                    perkiraan.setExpenseFixedVar(PstPerkiraan.OTHER);
                }
            }else{
                perkiraan.setExpenseType(PstPerkiraan.NON_OPERATING_EXPENSE);            
            }
            
            this.perkiraan = perkiraan; 
        }
        catch(Exception e) 
        {
            perkiraan = new Perkiraan();
        }       
    }
}
