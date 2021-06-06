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
public class Position extends Entity implements Cloneable {

  /**
   * Mendefinisikan data
   */
  private String position_name = "";

  /**
   * mengambil data dan memberikan nilai, contohnya getname, kemudian meberi
   * return nama
   *
   * @return
   */
  public String getPositionName() {
    return position_name;
  }

  /**
   * konstruktor / sebuah fungsi yang otomatis akan dipanggil setiap kali
   * melakukan instasiasi terhadap suatu kelas
   *
   * @param nama
   */
  public void setPositionName(String position_name) {
    if (position_name == null) {
      position_name = "";
    }
    this.position_name = position_name;
  }

  public JSONObject historyNew() {
    JSONObject j = new JSONObject();
    j.put("Posisi", getPositionName());
    return j;
  }

  public JSONObject historyCompare(Position o) {
    JSONObject j = new JSONObject();
    if (!getPositionName().equals(o.getPositionName())) {
      j.put("Pendidikan", "Dari " + getPositionName() + " menjadi " + o.getPositionName());
    }
    return j;
  }

  public Position clone() {
    Object o = null;
    try {
      o = super.clone();;
    } catch (CloneNotSupportedException ex) {
      System.err.println(ex);
    }
    return (Position) o;
  }
}
