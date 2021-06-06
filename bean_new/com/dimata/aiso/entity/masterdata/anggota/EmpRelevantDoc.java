/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dimata.aiso.entity.masterdata.anggota;

import com.dimata.qdep.db.DBException;
import com.dimata.qdep.entity.Entity;
import com.dimata.sedana.entity.masterdata.EmpRelevantDocGroup;
import com.dimata.sedana.entity.masterdata.PstEmpRelevantDocGroup;
import com.dimata.sedana.session.json.JSONObject;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author dedy_blinda
 */
public class EmpRelevantDoc extends Entity implements Cloneable {
    private Long docRelevantId;
    private Long anggotaId;
    private String docTitle;
    private String docDescription="";
    private String fileName="";
    private String docAttachFile="";
    private Long relvtDocGrpId;

    /**
     * @return the docRelevantId
     */
    public Long getDocRelevantId() {
        return docRelevantId;
    }

    /**
     * @param docRelevantId the docRelevantId to set
     */
    public void setDocRelevantId(Long docRelevantId) {
        this.docRelevantId = docRelevantId;
    }

    /**
     * @return the anggotaId
     */
    public Long getAnggotaId() {
        return anggotaId;
    }

    /**
     * @param anggotaId the anggotaId to set
     */
    public void setAnggotaId(Long anggotaId) {
        this.anggotaId = anggotaId;
    }

    /**
     * @return the docTitle
     */
    public String getDocTitle() {
        return docTitle;
    }

    /**
     * @param docTitle the docTitle to set
     */
    public void setDocTitle(String docTitle) {
        this.docTitle = docTitle;
    }

    /**
     * @return the docDescription
     */
    public String getDocDescription() {
        return docDescription;
    }

    /**
     * @param docDescription the docDescription to set
     */
    public void setDocDescription(String docDescription) {
        this.docDescription = docDescription;
    }

    /**
     * @return the fileName
     */
    public String getFileName() {
        return fileName;
    }

    /**
     * @param fileName the fileName to set
     */
    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    /**
     * @return the docAttachFile
     */
    public String getDocAttachFile() {
        return docAttachFile;
    }

    /**
     * @param docAttachFile the docAttachFile to set
     */
    public void setDocAttachFile(String docAttachFile) {
        this.docAttachFile = docAttachFile;
    }

    /**
     * @return the relvtDocGrpId
     */
    public Long getRelvtDocGrpId() {
        return relvtDocGrpId;
    }

    /**
     * @param relvtDocGrpId the relvtDocGrpId to set
     */
    public void setRelvtDocGrpId(Long relvtDocGrpId) {
        this.relvtDocGrpId = relvtDocGrpId;
    }

  public JSONObject historyNew() {
    JSONObject j = new JSONObject();
    try {
      Anggota anggota = PstAnggota.fetchExc(getAnggotaId());
      EmpRelevantDocGroup empRelevantDocGroup = PstEmpRelevantDocGroup.fetchExc(getRelvtDocGrpId());
      j.put("Anggota", anggota.getName());
      j.put("Kelompok Dokumen", empRelevantDocGroup.getDocGroup());
      j.put("Judul Dokumen", getDocTitle());
      j.put("Deskripsi Dokumen", getDocDescription());
      j.put("Attachment", getDocAttachFile());
      j.put("Nama File", getFileName());
    } catch (DBException ex) {
      Logger.getLogger(EmpRelevantDoc.class.getName()).log(Level.SEVERE, null, ex);
    }
    return j;
  }

  public JSONObject historyCompare(EmpRelevantDoc o) {
    JSONObject j = new JSONObject();
    try {
      if (getAnggotaId() != (o.getAnggotaId())) {
        Anggota oAnggota = PstAnggota.fetchExc(getAnggotaId());
        Anggota nAnggota = PstAnggota.fetchExc(o.getAnggotaId());
        j.put("Anggota", "Dari " + oAnggota.getName() + " menjadi " + nAnggota.getName());
      }
      if (getRelvtDocGrpId() != (o.getRelvtDocGrpId())) {
        EmpRelevantDocGroup oRelevantDocGroup = PstEmpRelevantDocGroup.fetchExc(getRelvtDocGrpId());
        EmpRelevantDocGroup nRelevantDocGroup = PstEmpRelevantDocGroup.fetchExc(o.getRelvtDocGrpId());
        j.put("Kelompok Dokumen", "Dari " + oRelevantDocGroup.getDocGroup() + " menjadi " + nRelevantDocGroup.getDocGroup());
      }
      if (!getDocTitle().equals(o.getDocTitle())) {
        j.put("Judul Dokumen", "Dari " + getDocTitle() + " menjadi " + getDocTitle());
      }
      if (!getDocDescription().equals(o.getDocDescription())) {
        j.put("Deskripsi Dokumen", "Dari " + getDocDescription() + " menjadi " + getDocDescription());
      }
      if (!getDocAttachFile().equals(o.getDocAttachFile())) {
        j.put("Attachment", "Dari " + getDocAttachFile() + " menjadi " + getDocAttachFile());
      }
      if (!getFileName().equals(o.getFileName())) {
        j.put("Nama File", "Dari " + getFileName() + " menjadi " + getFileName());
      }
    } catch (DBException ex) {
      Logger.getLogger(EmpRelevantDoc.class.getName()).log(Level.SEVERE, null, ex);
    }
    return j;
  }

  public EmpRelevantDoc clone() {
    Object o = null;
    try {
      o = super.clone();;
    } catch (CloneNotSupportedException ex) {
      System.err.println(ex);
    }
    return (EmpRelevantDoc) o;
  }

}
