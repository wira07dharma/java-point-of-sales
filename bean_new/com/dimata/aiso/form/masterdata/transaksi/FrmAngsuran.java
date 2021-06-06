/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dimata.aiso.form.masterdata.transaksi;

import com.dimata.aiso.entity.masterdata.transaksi.Angsuran;
import com.dimata.qdep.form.FRMHandler;
import com.dimata.qdep.form.I_FRMInterface;
import com.dimata.qdep.form.I_FRMType;
import com.dimata.util.lang.I_Language;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author HaddyPuutraa
 */
public class FrmAngsuran extends FRMHandler implements I_Language, I_FRMType, I_FRMInterface{
    public static final String FRM_ANGSURAN_NAME = "ANGSURAN";
    
    public static final int FRM_FLD_ID_ANGSURAN = 0;
    public static final int FRM_FLD_ID_PINJAMAN = 1;
    public static final int FRM_FLD_TANGGAL_ANGSURAN = 2;
    public static final int FRM_FLD_JUMLAH_ANGSURAN = 3;
    
    public static final String[] fieldNames = {
        "ID_ANGSURAN",
        "ID_PINJAMAN",
        "TANGGAL_ANGSURAN",
        "JUMLAH_ANGSURAN"
    };
    
    public static final int[] fieldTypes = {
        TYPE_LONG,
        TYPE_LONG + ENTRY_REQUIRED,
        TYPE_DATE,
        TYPE_FLOAT + ENTRY_REQUIRED
    };
    
    private Angsuran angsuran;
    
    public FrmAngsuran(Angsuran angsuran){
        this.angsuran = angsuran;
    }  
    
    public FrmAngsuran(HttpServletRequest req, Angsuran angsuran){
        super(new FrmAngsuran(angsuran),req);
        this.angsuran = angsuran;
    }
    
    public int getFieldSize() {
        return fieldNames.length;
    }

    public String getFormName() {
        return FRM_ANGSURAN_NAME;
    }

    public String[] getFieldNames() {
        return fieldNames;
    }

    public int[] getFieldTypes() {
        return fieldTypes;
    }
    
    public Angsuran getEntityObject(){
        return angsuran;
    }
    
    public void requestEntityObject(Angsuran angsuran){
        try{
            this.requestParam();
            
            angsuran.setIdPinjaman(this.getLong(FRM_FLD_ID_PINJAMAN));
            //angsuran.setTanggalAngsuran(this.getDate(FRM_FLD_TANGGAL_ANGSURAN));
            angsuran.setJumlahAngsuran(this.getFloat(FRM_FLD_JUMLAH_ANGSURAN));
            
            this.angsuran = angsuran;
        }catch(Exception e){
            this.angsuran = new Angsuran();
        }
    }
}
