/*
 * SrcWeeklyDbf.java
 *
 * Created on March 29, 2004, 5:47 PM
 */

package com.dimata.posbo.report;

import java.util.Date;
/**
 *
 * @author  Administrator
 * @version 
 */
public class SrcWeeklyDbf {

    /** Holds value of property kdLokasi. */   
    private String kdLokasi;
    
    /** Holds value of property kdKategori. */
    private String kdKategori;
    
    /** Holds value of property kdSubKategori. */
    private String kdSubKategori;
    
    /** Holds value of property tglPilih. */
    private Date tglPilih;
    
    /** Holds value of property tahun. */   
    private String tahun;

    private int type = 0;

    /** Creates new SrcWeeklyDbf */
    public SrcWeeklyDbf() {
    }

    /** Getter for property kdLokasi.
     * @return Value of property kdLokasi.
     */
    public String getKdLokasi() {
        return kdLokasi;
    }
    
    /** Setter for property kdLokasi.
     * @param kdLokasi New value of property kdLokasi.
     */
    public void setKdLokasi(String kdLokasi) {
        this.kdLokasi = kdLokasi;
    }
    
    /** Getter for property kdKategori.
     * @return Value of property kdKategori.
     */
    public String getKdKategori() {
        return kdKategori;
    }
    
    /** Setter for property kdKategori.
     * @param kdKategori New value of property kdKategori.
     */
    public void setKdKategori(String kdKategori) {
        this.kdKategori = kdKategori;
    }
    
    /** Getter for property kdSubKategori.
     * @return Value of property kdSubKategori.
     */
    public String getKdSubKategori() {
        return kdSubKategori;
    }
    
    /** Setter for property kdSubKategori.
     * @param kdSubKategori New value of property kdSubKategori.
     */
    public void setKdSubKategori(String kdSubKategori) {
        this.kdSubKategori = kdSubKategori;
    }
    
    /** Getter for property tglPilih.
     * @return Value of property tglPilih.
     */
    public Date getTglPilih() {
        return tglPilih;
    }
    
    /** Setter for property tglPilih.
     * @param tglPilih New value of property tglPilih.
     */
    public void setTglPilih(Date tglPilih) {
        this.tglPilih = tglPilih;
    }
    
    /** Getter for property tahun.
     * @return Value of property tahun.
     */
    public String getTahun() {
        return tahun;
    }
    
    /** Setter for property tahun.
     * @param tahun New value of property tahun.
     */
    public void setTahun(String tahun) {
        this.tahun = tahun;
    }

    public int getType() {
        return type;
    }

    public void setType(int tp) {
        this.type = tp;
    }
}
