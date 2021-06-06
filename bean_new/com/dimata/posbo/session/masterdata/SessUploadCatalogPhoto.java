/**
 * Created by IntelliJ IDEA.
 * User: gadnyana
 * Date: Jan 17, 2006
 * Time: 10:43:00 AM
 * To change this template use Options | File Templates.
 */
package com.dimata.posbo.session.masterdata;

// core java package

import java.util.*;
import java.sql.*;
import java.io.InputStream;
import java.io.FileOutputStream;

import com.dimata.util.blob.*;
import com.dimata.common.entity.system.PstSystemProperty;
import com.dimata.posbo.db.DBResultSet;
import com.dimata.posbo.db.DBHandler;
import com.dimata.posbo.entity.masterdata.Material;
import com.dimata.posbo.entity.masterdata.PstMaterial;

public class SessUploadCatalogPhoto {

    public static final String SESKEY_PICTURE = "PHOTO_PICT_ID";
    //private static String IMGCACHE_REALPATH = "imgcache" + System.getProperty("file.separator");
    private static String IMGCACHE_REALPATH = "imgcache" + '/';
    private static String IMGCACHE_ABSPATH = "";
    private static String IMG_PREFIX = "";
    private static String IMG_POSTFIX = ".jpg";

    public SessUploadCatalogPhoto() {
        IMGCACHE_ABSPATH = PstSystemProperty.getValueByName("PROP_IMGCACHE");
    }

    public int updateImage(Object obj, long oid) {
        DBResultSet dbrs = null;
        try {
            if (obj == null) return -1;
            PreparedStatement pstmt = null;

            byte b[] = null;
            b = (byte[]) obj;

            String sql = ""; //UPDATE " + PstPesertaJkjPhoto.TBL_JKJ_PESERTA_PHOTO +" SET "+
            //PstPesertaJkjPhoto.fieldNames[PstPesertaJkjPhoto.FLD_PESERTA_JKJ_PHOTO] +" = ? WHERE "+
            //PstPesertaJkjPhoto.fieldNames[PstPesertaJkjPhoto.FLD_PESERTA_JKJ_BLOB_ID] + " = ?";

            System.out.println("update --- > " + sql);
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
            /*String sql = " DELETE FROM  " + PstPesertaJkjPhoto.TBL_JKJ_PESERTA_PHOTO + " " +
                    " WHERE " + PstPesertaJkjPhoto.fieldNames[PstPesertaJkjPhoto.FLD_PESERTA_JKJ_BLOB_ID] + " = '" + oid + "'";

            System.out.println("del pic sql : " + sql);
            DBHandler.execUpdate(sql);*/

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
        File file = null;


        if (flImg.exists()) {
            System.out.println("....." + IMGCACHE_REALPATH + IMG_PREFIX + oid + IMG_POSTFIX);
            return IMGCACHE_REALPATH + IMG_PREFIX + oid + IMG_POSTFIX;
        }

        //if (queryImage(oid) == 0) {
        //    System.out.println("....." + IMGCACHE_REALPATH + IMG_PREFIX + oid + IMG_POSTFIX);
        //    return IMGCACHE_REALPATH + IMG_PREFIX + oid + IMG_POSTFIX;
        //} else
        return "";

    }

    /**
     * @param oid
     * @return
     */
    public String fetchImagePeserta(long oid) {
        try {
            Material objMaterial = PstMaterial.fetchExc(oid);
            return fetchImage(objMaterial.getOID());
        } catch (Exception e) {
            System.out.println("Exc when fetchImagePeserta : " + e.toString());
        }
        return "";
    }


    /**
     * @param noPeserta
     * @return
     */
    public String fetchImagePeserta(String noPeserta) {
        int resCode = -1;
        String absImgPath = IMGCACHE_ABSPATH + IMG_PREFIX + noPeserta + IMG_POSTFIX;
        java.io.File flImg = new java.io.File(absImgPath);

        if (flImg.exists()) {
            System.out.println("....." + IMGCACHE_REALPATH + IMG_PREFIX + noPeserta + IMG_POSTFIX);
            return IMGCACHE_REALPATH + IMG_PREFIX + noPeserta + IMG_POSTFIX;
        } else {
            return "";
        }
    }


    public boolean checkOID(long oid) {
        DBResultSet dbrs = null;
        long count = 0;
        try {
            String sql = ""; /*SELECT " + PstPesertaJkjPhoto.fieldNames[PstPesertaJkjPhoto.FLD_PESERTA_JKJ_BLOB_ID] +
                    " FROM " + PstPesertaJkjPhoto.TBL_JKJ_PESERTA_PHOTO +
                    " WHERE " + PstPesertaJkjPhoto.fieldNames[PstPesertaJkjPhoto.FLD_PESERTA_JKJ_BLOB_ID] +
                    " = '" + oid + "'";*/

            System.out.println("------ checkOID sql : " + sql);
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
            String sql = " "; /*SELECT " + PstPesertaJkjPhoto.fieldNames[PstPesertaJkjPhoto.FLD_PESERTA_JKJ_PHOTO] +
                    " FROM " + PstPesertaJkjPhoto.TBL_JKJ_PESERTA_PHOTO +
                    " WHERE " + PstPesertaJkjPhoto.fieldNames[PstPesertaJkjPhoto.FLD_PESERTA_JKJ_BLOB_ID] +
                    " = '" + oid + "'"; */

            System.out.println("queryImage sql : " + sql);
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
     * for
     * @return
     */
    public static int writeRenameCache(String nameOld, String nameNew) {
        try {
            SessUploadCatalogPhoto sessUploadCatalogPhoto = new SessUploadCatalogPhoto();
            String pictPath = sessUploadCatalogPhoto.fetchImagePeserta(nameOld);
            if (pictPath.length() > 0) {
                String destFileName = IMGCACHE_ABSPATH + IMG_PREFIX + nameNew + IMG_POSTFIX;
                java.io.File fileImg = new java.io.File(IMGCACHE_ABSPATH + IMG_PREFIX + nameOld + IMG_POSTFIX);
                fileImg.renameTo(new java.io.File(destFileName));
            }
        } catch (Exception e) {
            System.out.println("errr : " + e.toString());
        }
        return -1;
    }


    /**
     * get absolute path and file name
     * @return
     */
    public String getAbsoluteFileName(String noPeserta) {
        return IMGCACHE_ABSPATH + IMG_PREFIX + noPeserta + IMG_POSTFIX;
    }

    /**
     * get real path and file name
     * @param photoOid
     * @return
     */
    public String getRealFileName(String noPeserta) {
        return IMGCACHE_REALPATH + IMG_PREFIX + noPeserta + IMG_POSTFIX;
    }

    /** gadnyana
     * in use for rename file
     * @param nm1
     * @param nm2
     */
    public static void renameFile(String nm1, String nm2) {
        try {
            writeRenameCache(nm1, nm2);
        } catch (Exception e) {
        }
    }

    public static void main(String args[]) {
        SessUploadCatalogPhoto objSessPesertaPhoto = new SessUploadCatalogPhoto();

        //writeRenameCache();

        //System.out.println("objSessPesertaPhoto.IMGCACHE_ABSPATH : " + objSessPesertaPhoto.getAbsoluteFileName("3453"));
    }
}
