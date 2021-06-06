/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dimata.aiso.form.masterdata.mastertabungan;

import com.dimata.aiso.entity.masterdata.mastertabungan.JenisDeposito;
import com.dimata.qdep.form.FRMHandler;
import com.dimata.qdep.form.I_FRMInterface;
import com.dimata.qdep.form.I_FRMType;
import com.dimata.util.lang.I_Language;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author HaddyPuutraa
 */
public class FrmJenisDeposito extends FRMHandler implements I_Language, I_FRMType, I_FRMInterface{
    public static final String FRM_JENIS_DEPOSITO_NAME = "JENIS_DEPOSITO";
    
    public static final int FRM_ID_JENIS_DEPOSITO = 0;
    public static final int FRM_NAMA_JENIS_DEPOSITO = 1;
    public static final int FRM_MIN_DEPOSITO = 2;
    public static final int FRM_MAX_DEPOSITO = 3;
    public static final int FRM_BUNGA = 4;
    public static final int FRM_JANGKA_WAKTU = 5;
    public static final int FRM_PROVISI = 6;
    public static final int FRM_BIAYA_ADMIN = 7;
    public static final int FRM_KETERANGAN = 8;
    
    public static final String[] fieldNames = {
        "ID_JENIS_DEPOSITO",//0
        "NAMA_JENIS_DEPOSITO",//1
        "MIN_DEPOSITO",//2
        "MAX_DEPOSITO",//3
        "BUNGA",//4
        "JANGKA_WAKTU",//5
        "PROVISI",//6
        "BIAYA_ADMIN",//7
        "KETERANGAN"//8
    };
    
    public static final int[] fieldTypes={
        TYPE_LONG,
        TYPE_STRING + ENTRY_REQUIRED,
        TYPE_FLOAT,
        TYPE_FLOAT,
        TYPE_FLOAT,
        TYPE_INT + ENTRY_REQUIRED,
        TYPE_FLOAT,
        TYPE_FLOAT,
        TYPE_STRING
    };
    
    private JenisDeposito jenisDeposito;
    
    public FrmJenisDeposito(JenisDeposito jenisDeposito){
        this.jenisDeposito = jenisDeposito;
    }
    
    public FrmJenisDeposito(HttpServletRequest req, JenisDeposito jenisDeposito){
        super(new FrmJenisDeposito(jenisDeposito),req);
        this.jenisDeposito = jenisDeposito;
    }
    
    public int getFieldSize() {
        return fieldNames.length;
    }

    public String getFormName() {
        return FRM_JENIS_DEPOSITO_NAME;
    }

    public String[] getFieldNames() {
        return fieldNames;
    }

    public int[] getFieldTypes() {
        return fieldTypes;
    }
    
    public JenisDeposito getEntityObject(){
        return jenisDeposito;
    }
    
    public void requestEntityObject(JenisDeposito jenisDeposito){
        try{
            this.requestParam();
            
            jenisDeposito.setNamaJenisDeposito(this.getString(FRM_NAMA_JENIS_DEPOSITO));
            jenisDeposito.setMinDeposito(this.getFloat(FRM_MIN_DEPOSITO));
            jenisDeposito.setMaxDeposito(this.getFloat(FRM_MAX_DEPOSITO));
            jenisDeposito.setBunga(this.getFloat(FRM_BUNGA));
            jenisDeposito.setJangkaWaktu(this.getInt(FRM_JANGKA_WAKTU));
            jenisDeposito.setProvisi(this.getFloat(FRM_PROVISI));
            jenisDeposito.setBiayaAdmin(this.getFloat(FRM_BIAYA_ADMIN));
            jenisDeposito.setKeterangan(this.getString(FRM_KETERANGAN));
            
            this.jenisDeposito = jenisDeposito;
        }catch(Exception e){
            this.jenisDeposito = new JenisDeposito();
        }
    }
}
