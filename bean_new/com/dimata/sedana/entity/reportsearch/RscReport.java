/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dimata.sedana.entity.reportsearch;

import com.dimata.qdep.entity.Entity;
import java.util.Vector;

/**
 *
 * @author Adi
 */
public class RscReport extends Entity{
  private String startDate = null;
  private String endDate = null;
  private String namaNasabah = null;
  private long noAnggotaStart = 0;
  private long noAnggotaEnd = 0;
  private long noRekeningStart = 0;
  private long noRekeningEnd = 0;
  private Vector<Long> tabungan = new Vector<Long>();
  private Vector<Long> jenisKredit = new Vector<Long>();
  private Vector<Long> tingkatKolektibilitas = new Vector<Long>();
  private Vector<Long> sumberDana = new Vector<Long>();
  private Vector<Long> contactType = new Vector<Long>();
  private int listSort = 0;

  /**
   * @return the startDate
   */
  public String getStartDate() {
    return startDate;
  }

  /**
   * @param startDate the startDate to set
   */
  public void setStartDate(String startDate) {
    this.startDate = startDate;
  }

  /**
   * @return the endDate
   */
  public String getEndDate() {
    return endDate;
  }

  /**
   * @param endDate the endDate to set
   */
  public void setEndDate(String endDate) {
    this.endDate = endDate;
  }

  /**
   * @return the noAnggotaStart
   */
  public long getNoAnggotaStart() {
    return noAnggotaStart;
  }

  /**
   * @param noAnggotaStart the noAnggotaStart to set
   */
  public void setNoAnggotaStart(long noAnggotaStart) {
    this.noAnggotaStart = noAnggotaStart;
  }

  /**
   * @return the noAnggotaEnd
   */
  public long getNoAnggotaEnd() {
    return noAnggotaEnd;
  }

  /**
   * @param noAnggotaEnd the noAnggotaEnd to set
   */
  public void setNoAnggotaEnd(long noAnggotaEnd) {
    this.noAnggotaEnd = noAnggotaEnd;
  }

  /**
   * @return the noRekeningStart
   */
  public long getNoRekeningStart() {
    return noRekeningStart;
  }

  /**
   * @param noRekeningStart the noRekeningStart to set
   */
  public void setNoRekeningStart(long noRekeningStart) {
    this.noRekeningStart = noRekeningStart;
  }

  /**
   * @return the noRekeningEnd
   */
  public long getNoRekeningEnd() {
    return noRekeningEnd;
  }

  /**
   * @param noRekeningEnd the noRekeningEnd to set
   */
  public void setNoRekeningEnd(long noRekeningEnd) {
    this.noRekeningEnd = noRekeningEnd;
  }

  /**
   * @return the tabungan
   */
  public Vector<Long> getTabungan() {
    return tabungan;
  }

  /**
   * @param tabungan the tabungan to set
   */
  public void setTabungan(String[] tabungan) {
    for (String t : tabungan) {
      this.getTabungan().add(Long.parseLong(t));
    }
  }
  /**
   * @return the jenisKredit
   */
  public Vector<Long> getJenisKredit() {
    return jenisKredit;
  }

  /**
   * @param jenisKredit the jenisKredit to set
   */
  public void setJenisKredit(String[] jenisKredit) {
    for (String t : jenisKredit) {
      this.jenisKredit.add(Long.parseLong(t));
    }
  }

  /**
   * @return the tingkatKolektibilitas
   */
  public Vector<Long> getTingkatKolektibilitas() {
    return tingkatKolektibilitas;
  }

  /**
   * @param tingkatKolektibilitas the tingkatKolektibilitas to set
   */
  public void setTingkatKolektibilitas(String[] tingkatKolektibilitas) {
    for (String t : tingkatKolektibilitas) {
      this.tingkatKolektibilitas.add(Long.parseLong(t));
    }
  }

  /**
   * @return the sumberDana
   */
  public Vector<Long> getSumberDana() {
    return sumberDana;
  }

  /**
   * @param sumberDana the sumberDana to set
   */
  public void setSumberDana(String[] sumberDana) {
    for (String t : sumberDana) {
      this.sumberDana.add(Long.parseLong(t));
    }
  }

  /**
   * @return the contactType
   */
  public Vector<Long> getContactType() {
    return contactType;
  }

  /**
   * @param sumberDana the sumberDana to set
   */
  public void setContactType(String[] contactType) {
    for (String s : contactType) {
      this.contactType.add(Long.parseLong(s));
    }
  }

  /**
   * @return the listSort
   */
  public int getListSort() {
    return listSort;
  }

  /**
   * @param listSort the listSort to set
   */
  public void setListSort(int listSort) {
    this.listSort = listSort;
  }
  

  /**
   * @return the namaNasabah
   */
  public String getNamaNasabah() {
    return namaNasabah;
  }

  /**
   * @param namaNasabah the namaNasabah to set
   */
  public void setNamaNasabah(String namaNasabah) {
    this.namaNasabah = namaNasabah;
  }
  
}
