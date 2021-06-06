/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dimata.sedana.session;

import com.dimata.aiso.entity.masterdata.anggota.Anggota;
import com.dimata.aiso.entity.masterdata.anggota.EmpRelevantDoc;
import com.dimata.aiso.entity.masterdata.anggota.PstAnggota;
import com.dimata.aiso.entity.masterdata.anggota.PstEmpRelevantDoc;
import com.dimata.common.entity.system.PstSystemProperty;
import com.dimata.qdep.db.DBHandler;
import com.dimata.qdep.db.DBResultSet;
import com.dimata.util.blob.ImageLoader;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 *
 * @author dedy_blinda
 */
public class SessEmpRelevantDoc {
    public static final String SESKEY_PICTURE = "RELEVANT_DOC_ID";
    private static String IMGCACHE_REALPATH = "imgdoc" + System.getProperty("file.separator");
    private static String IMGCACHE_ABSPATH = "";
    private static String IMG_PREFIX = "";
    private static String IMG_POSTFIX = "";
    
    
    
    /** Creates a new instance of SessEmpRelevantDoc */
    public SessEmpRelevantDoc() {
          // IMG_POSTFIX = PstSystemProperty.getValueByName("IMG_POSTFIX");
        IMGCACHE_ABSPATH = PstSystemProperty.getValueByName("IMGDOC");
        //System.out.println("IMG_POSTFIX = : "+IMG_POSTFIX);
    }
    
     public int updateImage(Object obj, long oid) {
        DBResultSet dbrs = null;
        try {
            if (obj == null) return -1;
            PreparedStatement pstmt = null;
            
            byte b[] = null;
            b = (byte[]) obj;
            
            String sql = "UPDATE " + PstEmpRelevantDoc.TBL_HR_EMP_RELEVANT_DOC + " SET " +
            PstEmpRelevantDoc.fieldNames[PstEmpRelevantDoc.FLD_DOC_ATTACH_FILE] + " = ? WHERE " +
            PstEmpRelevantDoc.fieldNames[PstEmpRelevantDoc.FLD_DOC_ATTACH_FILE] + " = ?";
            
            System.out.println("SessEmpRelevantDoc.updateImage --- > " + sql);
            dbrs = DBHandler.getPSTMTConnection(sql);
            pstmt = dbrs.getPreparedStatement();
            
            pstmt.setBytes(1, b);
            pstmt.setLong(2, oid);
            
            DBHandler.execUpdatePreparedStatement(pstmt);
            ImageLoader.deleteChace(IMGCACHE_ABSPATH + IMG_PREFIX + oid + IMG_POSTFIX);
            
        } catch (Exception e) {
            System.out.println(e.toString());
        } finally {
            DBResultSet.closePstmt(dbrs);
        }
        return 0;
    }
     
      public int deleteImage(long oid) {
        boolean exist = false;
        try {
            String sql = " DELETE FROM  " + PstEmpRelevantDoc.TBL_HR_EMP_RELEVANT_DOC + " " +
            " WHERE " + PstEmpRelevantDoc.fieldNames[PstEmpRelevantDoc.FLD_DOC_RELEVANT_ID] + " = '" + oid + "'";
            
            System.out.println("SessEmpRelevantDoc.deleteImage : " + sql);
            DBHandler.execUpdate(sql);
            
            ImageLoader.deleteChace(IMGCACHE_ABSPATH + IMG_PREFIX + oid + IMG_POSTFIX);
            
            return 0;
        } catch (Exception e) {
            System.out.println(e.toString());
        }
        return -1;
    }
      
       public String fetchImage(long oid) {
        int resCode = -1;
        
        String absImgPath = IMGCACHE_ABSPATH + IMG_PREFIX + oid + IMG_POSTFIX;
        java.io.File flImg = new java.io.File(absImgPath);
        
        if (flImg.exists()) {
            System.out.println("....." + IMGCACHE_REALPATH + IMG_PREFIX + oid + IMG_POSTFIX);
            return IMGCACHE_REALPATH + IMG_PREFIX + oid + IMG_POSTFIX;
        }
        
        if (queryImage(oid) == 0) {
            System.out.println("....." + IMGCACHE_REALPATH + IMG_PREFIX + oid + IMG_POSTFIX);
            return IMGCACHE_REALPATH + IMG_PREFIX + oid + IMG_POSTFIX;
        } else
            return "";
        
    }
       
        /**
     * @param oid
     * @return
     */
    public String fetchImagePeserta(long oid) {
        try {
            //update by satrya 2012-11-23
         if(oid!=0){
            Anggota objAnggota = PstAnggota.fetchExc(oid);
            return fetchImagePeserta(objAnggota.getNoAnggota());
         }else{
              return "";
         }   
            //Employee objEmployee = PstEmployee.fetchExc(oid);
           // return fetchImagePeserta(objEmployee.getEmployeeNum());
        } catch (Exception e) {
            System.out.println("Exc when fetchImagePeserta : " + e.toString());
        }
        return "";
    }
    
    
    public String fetchImageRelevantDoc(long oid) {
        System.out.println("oid....."+oid);
        try {
            EmpRelevantDoc objEmpRelevantDoc = PstEmpRelevantDoc.fetchExc(oid);
            
            return fetchImageRelevantDocPath(objEmpRelevantDoc.getOID());
        } catch (Exception e) {
            System.out.println("Exc when fetchImageRelevantDoc : " + e.toString());
        }
        return "";
    }
    
    /**
     * @param noPeserta
     * @return
     */
    public String fetchImagePeserta(String empNum) {
        int resCode = -1;
        //java.io.File flImg = null;
        //try{
            String absImgPath = IMGCACHE_ABSPATH + IMG_PREFIX + empNum + IMG_POSTFIX;
            System.out.println("..... " + absImgPath);
            java.io.File flImg = new java.io.File(absImgPath);
        /*}catch(Exception e){
            System.out.println(e.toString());
        }*/
        if (flImg.exists()) {
            System.out.println("....." + IMGCACHE_REALPATH + IMG_PREFIX + empNum + IMG_POSTFIX);
            return IMGCACHE_REALPATH + IMG_PREFIX + empNum + IMG_POSTFIX;
        } else {
            return "";
        }
    }
    
    
    /**
     * @param noPeserta
     * @return
     */
    public String fetchImageRelevantDocPath(long oid) {
        int resCode = -1;
        //java.io.File flImg = null;
        //try{
            String absImgPath = IMGCACHE_ABSPATH + IMG_PREFIX + oid + IMG_POSTFIX;
            System.out.println("..... " + absImgPath);
            java.io.File flImg = new java.io.File(absImgPath);
        /*}catch(Exception e){
            System.out.println(e.toString());
        }*/
        if (flImg.exists()) {
            System.out.println("....." + IMGCACHE_REALPATH + IMG_PREFIX + oid + IMG_POSTFIX);
            return IMGCACHE_REALPATH + IMG_PREFIX + oid + IMG_POSTFIX;
        } else {
            return "";
        }
    }
    
     public boolean checkOID(long oid) {
        DBResultSet dbrs = null;
        long count = 0;
        try {
            String sql = "SELECT " + PstEmpRelevantDoc.fieldNames[PstEmpRelevantDoc.FLD_DOC_RELEVANT_ID] +
            " FROM " + PstEmpRelevantDoc.TBL_HR_EMP_RELEVANT_DOC +
            " WHERE " + PstEmpRelevantDoc.fieldNames[PstEmpRelevantDoc.FLD_DOC_RELEVANT_ID] +
            " = '" + oid + "'";
            
            System.out.println("SessEmpRelevantDoc.checkOID sql : " + sql);
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            
            while (rs.next()) {
                count = rs.getLong(1);
            }
        } catch (Exception e) {
            System.out.println("exc when select checkOID : " + e.toString());
        } finally {
            DBResultSet.close(dbrs);
            if (count > 0) {
                return true;
            }
            return false;
        }
    }
     
     private int queryImage(long oid) {
        DBResultSet dbrs = null;
        java.io.InputStream ins = null;
        try {
            String sql = " SELECT " + PstEmpRelevantDoc.fieldNames[PstEmpRelevantDoc.FLD_DOC_RELEVANT_ID] +
            " FROM " + PstEmpRelevantDoc.TBL_HR_EMP_RELEVANT_DOC +
            " WHERE " + PstEmpRelevantDoc.fieldNames[PstEmpRelevantDoc.FLD_DOC_RELEVANT_ID] +
            " = '" + oid + "'";
            
            System.out.println("sql SessEmpRelevantDoc.queryImage : " + sql);
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            
            while (rs.next()) {
                ins = rs.getBinaryStream(1);
                break;
            }
        } catch (Exception e) {
            System.out.println(e.toString());
        } finally {
            DBResultSet.close(dbrs);
            return ImageLoader.writeCache(ins, IMGCACHE_ABSPATH + IMG_PREFIX + oid + IMG_POSTFIX, true);
        }
    }
     
      /**
     * get absolute path and file name
     *
     * @param photoOid
     * @return
     */
    public String getAbsoluteFileName(String empNum) {
        return IMGCACHE_ABSPATH + IMG_PREFIX + empNum + IMG_POSTFIX;
    }
     
    
     public String getAbsoluteFileName(long oidDoc) {
        System.out.println("oidDoc..........."+oidDoc);
        return IMGCACHE_ABSPATH + IMG_PREFIX + oidDoc + IMG_POSTFIX;
    }
    
    
    /**
     * get real path and file name
     *
     * @param photoOid
     * @return
     */
    public String getRealFileName(String empNum) {
        return IMGCACHE_REALPATH + IMG_PREFIX + empNum + IMG_POSTFIX;
    }
    
    public static void main(String args[]) {
        //SessEmployeePicture objSessEmployeePicture = new SessEmployeePicture();
        //System.out.println("objSessEmployeePicture.IMGCACHE_ABSPATH : " + objSessEmployeePicture.getAbsoluteFileName("3453"));
    }
}
