package com.dimata.aiso.entity.masterdata.anggota;

/* package java */
import com.dimata.qdep.entity.*;
import com.dimata.sedana.session.json.JSONObject;

/**
 * metod tersebut dipanggil bisa untuk semua class merupakan class yang memuat
 * metod main(), dan nama file disimpan dengan nama class public
 *
 * @author Dede Nuharta
 */
public class Vocation extends Entity implements Cloneable {

  /**
   * Mendefinisikan data
   */
  private String vocation_name = "";
  private String description = "";

  /**
   * mengambil data dan memberikan nilai, contohnya getname, kemudian meberi
   * return nama
   *
   * @return
   */
  public String getVocationName() {
    return vocation_name;
  }

  public String getDescription() {
    return description;
  }

  /**
   * konstruktor / sebuah fungsi yang otomatis akan dipanggil setiap kali
   * melakukan instasiasi terhadap suatu kelas
   *
   * @param nama
   */
  public void setVocationName(String vocation_name) {
    if (vocation_name == null) {
      vocation_name = "";
    }
    this.vocation_name = vocation_name;
  }

  public void setDescription(String description) {
    if (description == null) {
      description = "";
    }
    this.description = description;
  }

  public JSONObject historyNew() {
    JSONObject j = new JSONObject();
    j.put("Pekerjaan", getVocationName());
    j.put("Deskripsi", getDescription());
    return j;
  }

  public JSONObject historyCompare(Vocation o) {
    JSONObject j = new JSONObject();
    if (!getVocationName().equals(o.getVocationName())) {
      j.put("Pendidikan", "Dari " + getVocationName() + " menjadi " + o.getVocationName());
    }
    if (!getDescription().equals(o.getDescription())) {
      j.put("Pendidikan", "Dari " + getDescription() + " menjadi " + o.getDescription());
    }
    return j;
  }

  public Vocation clone() {
    Object o = null;
    try {
      o = super.clone();;
    } catch (CloneNotSupportedException ex) {
      System.err.println(ex);
    }
    return (Vocation) o;
  }

}
