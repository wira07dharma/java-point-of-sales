/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dimata.aiso.form.masterdata.mastertabungan;

import com.dimata.aiso.entity.masterdata.mastertabungan.TabunganBerjangka;
import com.dimata.qdep.form.FRMHandler;
import com.dimata.qdep.form.I_FRMInterface;
import com.dimata.qdep.form.I_FRMType;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author dw1p4
 */
public class FrmTabunganBerjangka extends FRMHandler implements I_FRMInterface, I_FRMType {
     public static final String FRM_TABUNGAN_BERJANGKA = "TABUNGAN_BERJANGKA";
    
    public static final int FRM_TABUNGAN_BERJANGKA_ID=0;
    public static final int FRM_NAME=1;
    public static final int FRM_NILAI_TABUNGAN=2;
    public static final int FRM_PROSENTASE_NILAI=3;
    
    public static String[] fieldNames =
            {
                "FRM_TABUNGAN_BERJANGKA_ID",
                "FRM_NAME",
                "FRM_NILAI_TABUGAN",
                "FRM_PROSENTASE_NILAI"
            };
    
    public static int[] fieldTypes =
            {   
                TYPE_LONG,
                TYPE_STRING,
                TYPE_FLOAT,
                TYPE_FLOAT
            };
    
    private TabunganBerjangka tabunganBerjangka;

    public FrmTabunganBerjangka(TabunganBerjangka tabunganBerjangka) {
        this.tabunganBerjangka = tabunganBerjangka;
    }

    public FrmTabunganBerjangka(HttpServletRequest request, TabunganBerjangka tabunganBerjangka) {
        super(new FrmTabunganBerjangka(tabunganBerjangka), request);
        this.tabunganBerjangka = tabunganBerjangka;
    }

    public String getFormName() {
        return FRM_TABUNGAN_BERJANGKA;
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

    public TabunganBerjangka getEntityObject() {
        return tabunganBerjangka;
    }
    
    public void requestEntityObject(TabunganBerjangka tabunganBerjangka) {
        try {
            this.requestParam();
            tabunganBerjangka.setName(this.getString(FRM_NAME));
            tabunganBerjangka.setNilaiTabungan(this.getDouble(FRM_NILAI_TABUNGAN));
            tabunganBerjangka.setProsentaseNilai(this.getDouble(FRM_PROSENTASE_NILAI));
            
            this.tabunganBerjangka = tabunganBerjangka;
        } catch (Exception e) {
            tabunganBerjangka = new TabunganBerjangka();
        }
    }
    
}
