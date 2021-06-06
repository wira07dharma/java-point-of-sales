/**
 * Created by IntelliJ IDEA.
 * User: gadnyana
 * Date: Sep 3, 2005
 * Time: 9:50:44 AM
 * To change this template use Options | File Templates.
 */
package com.dimata.aiso.session.aktiva;

import com.dimata.aiso.entity.search.SrcModulAktiva;
import com.dimata.aiso.entity.masterdata.PstModulAktiva;
import com.dimata.aiso.entity.masterdata.PstAktiva;
import com.dimata.aiso.entity.masterdata.ModulAktiva;
import com.dimata.aiso.entity.masterdata.Aktiva;
import com.dimata.aiso.entity.aktiva.PstOrderAktiva;
import com.dimata.aiso.entity.aktiva.PstOrderAktivaItem;
import com.dimata.aiso.db.DBResultSet;
import com.dimata.aiso.db.DBHandler;
import com.dimata.aiso.entity.aktiva.PstPenyusutanAktiva;
import com.dimata.aiso.entity.aktiva.PstReceiveAktivaItem;
import com.dimata.aiso.entity.aktiva.PstSellingAktivaItem;

import java.util.Vector;
import java.sql.ResultSet;

public class SessListAktivaOwn {
    public static final String SESS_SEARCH_MODUL_AKTIVA = "SESS_SEARCH_MODUL_AKTIVA";

    /** gadnyana
     * untuk menampilkan list aktiva
     * @param srcModulAktiva
     * @param start
     * @param recordToGet
     * @return
     */
    public static Vector listAktivaOwn(SrcModulAktiva srcModulAktiva, int start, int recordToGet) {
        DBResultSet dbrs = null;
        Vector list = new Vector();
        try {
            String sql = "SELECT " +
                    " A." + PstModulAktiva.fieldNames[PstModulAktiva.FLD_AKTIVA_ID] +
                    ", A." + PstModulAktiva.fieldNames[PstModulAktiva.FLD_KODE] +
                    ", A." + PstModulAktiva.fieldNames[PstModulAktiva.FLD_NAME] +
                    /*", MA." + PstAktiva.fieldNames[PstAktiva.FLD_NAME] + " AS " + PstAktiva.fieldNames[PstAktiva.FLD_NAME] + "_A" +
                    ", MAA." + PstAktiva.fieldNames[PstAktiva.FLD_NAME] + " AS " + PstAktiva.fieldNames[PstAktiva.FLD_NAME] + "_AA" +
                    ", MAAA." + PstAktiva.fieldNames[PstAktiva.FLD_NAME] + " AS " + PstAktiva.fieldNames[PstAktiva.FLD_NAME] + "_AAA" +*/
                    ", A." + PstModulAktiva.fieldNames[PstModulAktiva.FLD_HARGA_PEROLEHAN] +
                    ", SUM(AP."+PstPenyusutanAktiva.fieldNames[PstPenyusutanAktiva.FLD_VALUE_PNY]+") AS VALUE_PENY" +
                    " FROM " + PstModulAktiva.TBL_AKTIVA + " AS A " +
                   /*" INNER JOIN " + PstAktiva.TBL_MASTER_AKTIVA + " AS MA " +
                    " ON MA." + PstAktiva.fieldNames[PstAktiva.FLD_MASTER_AKTIVA_ID] + " = A." + PstModulAktiva.fieldNames[PstModulAktiva.FLD_JENIS_AKTIVA_ID] +
                    " INNER JOIN " + PstAktiva.TBL_MASTER_AKTIVA + " AS MAA " +
                    "ON MAA." + PstAktiva.fieldNames[PstAktiva.FLD_MASTER_AKTIVA_ID] + " = A." + PstModulAktiva.fieldNames[PstModulAktiva.FLD_TYPE_PENYUSUTAN_ID] +
                    " INNER JOIN " + PstAktiva.TBL_MASTER_AKTIVA + " AS MAAA " +
                    "ON MAAA." + PstAktiva.fieldNames[PstAktiva.FLD_MASTER_AKTIVA_ID] + " = A." + PstModulAktiva.fieldNames[PstModulAktiva.FLD_METODE_PENYUSUTAN_ID] +*/
                    " INNER JOIN "+PstPenyusutanAktiva.TBL_PENYUSUTAN_AKTIVA+" AS AP"+
                    " ON AP."+PstPenyusutanAktiva.fieldNames[PstPenyusutanAktiva.FLD_AKTIVA_ID]+" = A."+PstModulAktiva.fieldNames[PstModulAktiva.FLD_AKTIVA_ID]+""+
                    " INNER JOIN " + PstReceiveAktivaItem.TBL_RECEIVE_AKTIVA_ITEM +" AS RAI "+
                    " ON RAI."+ PstReceiveAktivaItem.fieldNames[PstReceiveAktivaItem.FLD_AKTIVA_ID]+" = A."+PstModulAktiva.fieldNames[PstModulAktiva.FLD_AKTIVA_ID] +""; 

            if (srcModulAktiva.getOrderOid() != 0) {
                sql = sql + " INNER JOIN " + PstOrderAktivaItem.TBL_ORDER_AKTIVA_ITEM + " AS OI " +
                        " ON OI." + PstOrderAktivaItem.fieldNames[PstOrderAktivaItem.FLD_AKTIVA_ID] +
                        " = A." + PstModulAktiva.fieldNames[PstModulAktiva.FLD_AKTIVA_ID];
            }

            String where = "";
            
            where = " A."+ PstModulAktiva.fieldNames[PstModulAktiva.FLD_AKTIVA_ID]+"" +
                    " NOT IN "+
                    " (SELECT A."+PstModulAktiva.fieldNames[PstModulAktiva.FLD_AKTIVA_ID]+""+
                    " FROM "+PstModulAktiva.TBL_AKTIVA+" AS A"+
                    " INNER JOIN "+PstSellingAktivaItem.TBL_SELLING_AKTIVA_ITEM+" AS SAI"+
                    " ON SAI."+PstSellingAktivaItem.fieldNames[PstSellingAktivaItem.FLD_AKTIVA_ID]+" = A."+PstModulAktiva.fieldNames[PstModulAktiva.FLD_AKTIVA_ID]+")";

            // kode
            if (srcModulAktiva.getKodeAktiva().length() > 0) {
                if(where.length() > 0){
                where = where + " AND A." + PstModulAktiva.fieldNames[PstModulAktiva.FLD_KODE] + " LIKE '%" + srcModulAktiva.getKodeAktiva() + "%'";
                }else{
                where = " A." + PstModulAktiva.fieldNames[PstModulAktiva.FLD_KODE] + " LIKE '%" + srcModulAktiva.getKodeAktiva() + "%'";
                }
            }
            // nama
            if (srcModulAktiva.getNamaAktiva().length() > 0) {
                if (where.length() > 0) {
                    where = where + " AND A." + PstModulAktiva.fieldNames[PstModulAktiva.FLD_NAME] + " LIKE '%" + srcModulAktiva.getNamaAktiva() + "%'";
                } else {
                    where = " A." + PstModulAktiva.fieldNames[PstModulAktiva.FLD_NAME] + " LIKE '%" + srcModulAktiva.getNamaAktiva() + "%'";
                }
            }
            // jenis aktiva
            if (srcModulAktiva.getJenisAktivaId() != 0) {
                if (where.length() > 0) {
                    where = where + " AND A." + PstModulAktiva.fieldNames[PstModulAktiva.FLD_JENIS_AKTIVA_ID] + "=" + srcModulAktiva.getJenisAktivaId();
                } else {
                    where = " A." + PstModulAktiva.fieldNames[PstModulAktiva.FLD_JENIS_AKTIVA_ID] + "=" + srcModulAktiva.getJenisAktivaId();
                }
            }
            // tipe penyusutan
            if (srcModulAktiva.getTipePenyusutanId() != 0) {
                if (where.length() > 0) {
                    where = where + " AND A." + PstModulAktiva.fieldNames[PstModulAktiva.FLD_TYPE_PENYUSUTAN_ID] + "=" + srcModulAktiva.getTipePenyusutanId();
                } else {
                    where = " A." + PstModulAktiva.fieldNames[PstModulAktiva.FLD_TYPE_PENYUSUTAN_ID] + "=" + srcModulAktiva.getTipePenyusutanId();
                }
            }
            // tipe penyusutan
            if (srcModulAktiva.getMetodepenyusutanId() != 0) {
                if (where.length() > 0) {
                    where = where + " AND A." + PstModulAktiva.fieldNames[PstModulAktiva.FLD_METODE_PENYUSUTAN_ID] + "=" + srcModulAktiva.getMetodepenyusutanId();
                } else {
                    where = " A." + PstModulAktiva.fieldNames[PstModulAktiva.FLD_METODE_PENYUSUTAN_ID] + "=" + srcModulAktiva.getMetodepenyusutanId();
                }
            }

            if (srcModulAktiva.getOrderOid() != 0) {
                if (where.length() > 0) {
                    where = where + " AND OI." + PstOrderAktivaItem.fieldNames[PstOrderAktivaItem.FLD_ORDER_AKTIVA_ID] + "=" + srcModulAktiva.getOrderOid();
                } else {
                    where = " OI." + PstOrderAktivaItem.fieldNames[PstOrderAktivaItem.FLD_ORDER_AKTIVA_ID] + "=" + srcModulAktiva.getOrderOid();
                }
            }
            
                        
            if (where.length() > 0)
                sql = sql + " WHERE " + where;
                
            sql = sql + " GROUP BY A."+PstModulAktiva.fieldNames[PstModulAktiva.FLD_AKTIVA_ID]+""+
                ", A."+PstModulAktiva.fieldNames[PstModulAktiva.FLD_KODE]+""+
                ", A."+PstModulAktiva.fieldNames[PstModulAktiva.FLD_NAME]+""+
                ", A."+PstModulAktiva.fieldNames[PstModulAktiva.FLD_HARGA_PEROLEHAN]+"";
            
            sql = sql + " ORDER BY A." + PstModulAktiva.fieldNames[PstModulAktiva.FLD_KODE];
            
            switch (DBHandler.DBSVR_TYPE) {
                case DBHandler.DBSVR_MYSQL:
                    if (start == 0 && recordToGet == 0)
                        sql = sql + "";
                    else
                        sql = sql + " LIMIT " + start + "," + recordToGet;
                    break;

                case DBHandler.DBSVR_POSTGRESQL:
                    if (start == 0 && recordToGet == 0)
                        sql = sql + "";
                    else
                        sql = sql + " LIMIT " + recordToGet + " OFFSET " + start;

                    break;

                case DBHandler.DBSVR_SYBASE:
                    break;

                case DBHandler.DBSVR_ORACLE:
                    break;

                case DBHandler.DBSVR_MSSQL:
                    break;

                default:
                    break;
            }
           
            System.out.println("INI SQL DARI SESSION LIST AKTIVA OWN :::::: "+sql);
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while (rs.next()) {
                Vector vt = new Vector();
                ModulAktiva modulAktiva = new ModulAktiva();
                modulAktiva.setOID(rs.getLong(PstModulAktiva.fieldNames[PstModulAktiva.FLD_AKTIVA_ID]));
                modulAktiva.setKode(rs.getString(PstModulAktiva.fieldNames[PstModulAktiva.FLD_KODE]));
                modulAktiva.setName(rs.getString(PstModulAktiva.fieldNames[PstModulAktiva.FLD_NAME]));
                modulAktiva.setHargaPerolehan(rs.getDouble(PstModulAktiva.fieldNames[PstModulAktiva.FLD_HARGA_PEROLEHAN]));
                modulAktiva.setTotalPenyusutan(rs.getDouble("value_peny"));
                System.out.println("INI NILAI PENYUSUTAN ::::: "+modulAktiva.getTotalPenyusutan());
                if(modulAktiva.getTotalPenyusutan() > 0){
                    modulAktiva.setNilaiBuku(modulAktiva.getHargaPerolehan() - modulAktiva.getTotalPenyusutan());
                }else{
                    modulAktiva.setNilaiBuku(modulAktiva.getHargaPerolehan());
                }
                System.out.println("INI MODUL AKTIVA GET NILAI BUKU :::::: "+modulAktiva.getNilaiBuku()); 
                /*Aktiva aktiva = new Aktiva();
                aktiva.setNama(rs.getString(PstAktiva.fieldNames[PstAktiva.FLD_NAME] + "_A"));
                aktiva.setNamaTipepenyusutan(rs.getString(PstAktiva.fieldNames[PstAktiva.FLD_NAME] + "_AA"));
                aktiva.setNamaMetodepenyusutan(rs.getString(PstAktiva.fieldNames[PstAktiva.FLD_NAME] + "_AAA"));*/

                vt.add(modulAktiva);
                //vt.add(aktiva);
                list.add(vt);
            }

        } catch (Exception e) {
            System.out.println("err.SessModulAktiva - listModulAktiva " + e.toString());
        } finally {
            DBResultSet.close(dbrs);
        }
        return list;
    }

    /** gadnyana
     * untuk mencari jumlah row di daftar aktiva
     * @param srcModulAktiva
     * @return
     */
    public static int countAktivaOwn(SrcModulAktiva srcModulAktiva) {
        DBResultSet dbrs = null;
        int count = 0;
        try {
            String sql = "SELECT " +
                    " COUNT(A." + PstModulAktiva.fieldNames[PstModulAktiva.FLD_AKTIVA_ID] + ")" +
                    " FROM " + PstModulAktiva.TBL_AKTIVA + " AS A " +
                    " INNER JOIN " + PstAktiva.TBL_MASTER_AKTIVA + " AS MA " +
                    " ON MA." + PstAktiva.fieldNames[PstAktiva.FLD_MASTER_AKTIVA_ID] + " = A." + PstModulAktiva.fieldNames[PstModulAktiva.FLD_JENIS_AKTIVA_ID] +
                    " INNER JOIN " + PstAktiva.TBL_MASTER_AKTIVA + " AS MAA " +
                    " ON MAA." + PstAktiva.fieldNames[PstAktiva.FLD_MASTER_AKTIVA_ID] + " = A." + PstModulAktiva.fieldNames[PstModulAktiva.FLD_TYPE_PENYUSUTAN_ID] +
                    " INNER JOIN " + PstAktiva.TBL_MASTER_AKTIVA + " AS MAAA " +
                    " ON MAAA." + PstAktiva.fieldNames[PstAktiva.FLD_MASTER_AKTIVA_ID] + " = A." + PstModulAktiva.fieldNames[PstModulAktiva.FLD_METODE_PENYUSUTAN_ID]+
                    " INNER JOIN "+PstReceiveAktivaItem.TBL_RECEIVE_AKTIVA_ITEM+" AS RAI"+
                    " ON RAI."+PstReceiveAktivaItem.fieldNames[PstReceiveAktivaItem.FLD_AKTIVA_ID]+" = A."+PstModulAktiva.fieldNames[PstModulAktiva.FLD_AKTIVA_ID]+"";

            if (srcModulAktiva.getOrderOid() != 0) {
                sql = sql + " INNER JOIN " + PstOrderAktivaItem.TBL_ORDER_AKTIVA_ITEM + " AS OI " +
                        " ON OI." + PstOrderAktivaItem.fieldNames[PstOrderAktivaItem.FLD_AKTIVA_ID] +
                        " = A." + PstModulAktiva.fieldNames[PstModulAktiva.FLD_AKTIVA_ID];
            }

            String where = "";
            
            where = "A."+ PstModulAktiva.fieldNames[PstModulAktiva.FLD_AKTIVA_ID]+"" +
                    " NOT IN "+
                    " (SELECT A."+PstModulAktiva.fieldNames[PstModulAktiva.FLD_AKTIVA_ID]+""+
                    " FROM "+PstModulAktiva.TBL_AKTIVA+" AS A"+
                    " INNER JOIN "+PstSellingAktivaItem.TBL_SELLING_AKTIVA_ITEM+" AS SAI"+
                    " ON SAI."+PstSellingAktivaItem.fieldNames[PstSellingAktivaItem.FLD_AKTIVA_ID]+" = A."+PstModulAktiva.fieldNames[PstModulAktiva.FLD_AKTIVA_ID]+")";
            
            // kode
            if (srcModulAktiva.getKodeAktiva().length() > 0) {
                if(where.length() > 0){
                where = where +" AND A." + PstModulAktiva.fieldNames[PstModulAktiva.FLD_KODE] + " LIKE '%" + srcModulAktiva.getKodeAktiva() + "%'";
                }else {
                where = " A." + PstModulAktiva.fieldNames[PstModulAktiva.FLD_KODE] + " LIKE '%" + srcModulAktiva.getKodeAktiva() + "%'";               
                }
            }
            // nama
            if (srcModulAktiva.getNamaAktiva().length() > 0) {
                if (where.length() > 0) {
                    where = where + " AND A." + PstModulAktiva.fieldNames[PstModulAktiva.FLD_NAME] + " LIKE '%" + srcModulAktiva.getNamaAktiva() + "%'";
                } else {
                    where = " A." + PstModulAktiva.fieldNames[PstModulAktiva.FLD_NAME] + " LIKE '%" + srcModulAktiva.getNamaAktiva() + "%'";
                }
            }
            // jenis aktiva
            if (srcModulAktiva.getJenisAktivaId() != 0) {
                if (where.length() > 0) {
                    where = where + " AND A." + PstModulAktiva.fieldNames[PstModulAktiva.FLD_JENIS_AKTIVA_ID] + "=" + srcModulAktiva.getJenisAktivaId();
                } else {
                    where = " A." + PstModulAktiva.fieldNames[PstModulAktiva.FLD_JENIS_AKTIVA_ID] + "=" + srcModulAktiva.getJenisAktivaId();
                }
            }
            // tipe penyusutan
            if (srcModulAktiva.getTipePenyusutanId() != 0) {
                if (where.length() > 0) {
                    where = where + " AND A." + PstModulAktiva.fieldNames[PstModulAktiva.FLD_TYPE_PENYUSUTAN_ID] + "=" + srcModulAktiva.getTipePenyusutanId();
                } else {
                    where = " A." + PstModulAktiva.fieldNames[PstModulAktiva.FLD_TYPE_PENYUSUTAN_ID] + "=" + srcModulAktiva.getTipePenyusutanId();
                }
            }
            // tipe penyusutan
            if (srcModulAktiva.getMetodepenyusutanId() != 0) {
                if (where.length() > 0) {
                    where = where + " AND A." + PstModulAktiva.fieldNames[PstModulAktiva.FLD_METODE_PENYUSUTAN_ID] + "=" + srcModulAktiva.getMetodepenyusutanId();
                } else {
                    where = " A." + PstModulAktiva.fieldNames[PstModulAktiva.FLD_METODE_PENYUSUTAN_ID] + "=" + srcModulAktiva.getMetodepenyusutanId();
                }
            }

            if (srcModulAktiva.getOrderOid() != 0) {
                if (where.length() > 0) {
                    where = where + " AND OI." + PstOrderAktivaItem.fieldNames[PstOrderAktivaItem.FLD_ORDER_AKTIVA_ID] + "=" + srcModulAktiva.getOrderOid();
                } else {
                    where = " OI." + PstOrderAktivaItem.fieldNames[PstOrderAktivaItem.FLD_ORDER_AKTIVA_ID] + "=" + srcModulAktiva.getOrderOid();
                }
            }

            if (where.length() > 0)
                sql = sql + " WHERE " + where;
            
            System.out.println("INI SQL SELECT COUNT :::::: "+sql);
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while (rs.next()) {
                count = rs.getInt(1);
            }

        } catch (Exception e) {
            System.out.println("err.SessModulAktiva - listModulAktiva " + e.toString());
        } finally {
            DBResultSet.close(dbrs);
        }
        return count;
    }

}
