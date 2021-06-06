/* Generated by Together */

package com.dimata.aiso.entity.jurnal;

/* import qdep */
import com.dimata.qdep.entity.*; 

public class SaldoAkhirPeriode extends Entity {
    private double debet = 0;
    private double kredit = 0;

    public int getTandaDebetKredit() {
        return tandaDebetKredit;
    }

    public void setTandaDebetKredit(int tandaDebetKredit) {
        this.tandaDebetKredit = tandaDebetKredit;
    }

    public int getiNormalSign() {
        return iNormalSign;
    }

    public void setiNormalSign(int iNormalSign) {
        this.iNormalSign = iNormalSign;
    }

    private int tandaDebetKredit = 0;

    /** Holds value of property iNormalSign. */
    private int iNormalSign;
    
    public long getPeriodeId(){  
        return getOID(0);
    }

    public void setPeriodeId(long periodeId){
        this.setOID(0,periodeId);
    }

    public long getIdPerkiraan(){
        return getOID(1);
    }

    public void setIdPerkiraan(long idPerkiraan){
        this.setOID(1,idPerkiraan);
    }

    public double getDebet(){ return debet; }

    public void setDebet(double debet){ this.debet = debet; }

    public double getKredit(){ return kredit; }

    public void setKredit(double kredit){ this.kredit = kredit; }
    
}
