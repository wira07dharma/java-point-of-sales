/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dimata.aiso.form.masterdata.transaksi;

import com.dimata.aiso.entity.masterdata.transaksi.Deposito;
import com.dimata.qdep.form.FRMHandler;
import com.dimata.qdep.form.I_FRMInterface;
import com.dimata.qdep.form.I_FRMType;
import com.dimata.util.lang.I_Language;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author HaddyPuutraa
 */
public class FrmDeposito extends FRMHandler implements I_Language, I_FRMType, I_FRMInterface{
    public static final String FRM_DEPOSITO_NAME = "DEPOSITO";
    
    public static final int FRM_FLD_ID_DEPOSITO = 0;
    public static final int FRM_FLD_ID_ANGGOTA = 1;
    public static final int FRM_FLD_ID_JENIS_DEPOSITO = 2;
    public static final int FRM_FLD_ID_KELOMPOK = 3;
    public static final int FRM_FLD_TGL_PENGAJUAN_DEPOSITO = 4;
    public static final int FRM_FLD_JUMLAH_DEPOSITO = 5;
    public static final int FRM_FLD_STATUS = 6;
    
    public static final String[] fieldNames = {
        "ID_DEPOSITO",//0
        "ID_ANGGOTA",//1
        "ID_JENIS_DEPOSITO",//2
        "ID_KELOMPOK",//3
        "TGL_PENGAJUAN_DEPOSITO",//4
        "JUMLAH_DEPOSITO",//5
        "STATUS"//6
    };
    
    public static final int[] fieldTypes = {
        TYPE_LONG,
        TYPE_LONG + ENTRY_REQUIRED,
        TYPE_LONG + ENTRY_REQUIRED,
        TYPE_LONG + ENTRY_REQUIRED,
        TYPE_DATE,
        TYPE_FLOAT + ENTRY_REQUIRED,
        TYPE_INT
    };
    
    private Deposito deposito;
    
    public FrmDeposito(Deposito deposito){
        this.deposito = deposito;
    }
    
    public FrmDeposito(HttpServletRequest req, Deposito deposito){
        super(new FrmDeposito(deposito),req);
        this.deposito = deposito;
    }
    
    public int getFieldSize() {
        return fieldNames.length;
    }

    public String getFormName() {
        return FRM_DEPOSITO_NAME;
    }

    public String[] getFieldNames() {
        return fieldNames;
    }

    public int[] getFieldTypes() {
        return fieldTypes;
    }
    
    public Deposito getEntityObject(){
        return deposito;
    }
    
    public void requestEntityObject(Deposito deposito){
        try{
            this.requestParam();
            
            deposito.setIdAnggota(this.getLong(FRM_FLD_ID_ANGGOTA));
            deposito.setIdJenisDeposito(this.getLong(FRM_FLD_ID_JENIS_DEPOSITO));
            deposito.setIdKelompokKoperasi(this.getLong(FRM_FLD_ID_KELOMPOK));
            //deposito.setTanggalPengajuanDeposito(this.getDate(FRM_FLD_TGL_PENGAJUAN_DEPOSITO));
            deposito.setJumlahDeposito(this.getDouble(FRM_FLD_JUMLAH_DEPOSITO));
            deposito.setStatus(this.getInt(FRM_FLD_STATUS));
            
            this.deposito = deposito;
        }catch(Exception e){
            this.deposito = new Deposito();
        }
    }
}
