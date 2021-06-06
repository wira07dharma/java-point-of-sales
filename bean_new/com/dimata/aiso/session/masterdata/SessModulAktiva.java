/**
 * Created by IntelliJ IDEA.
 * User: gadnyana
 * Date: Sep 3, 2005
 * Time: 9:50:44 AM
 * To change this template use Options | File Templates.
 */
package com.dimata.aiso.session.masterdata;

import com.dimata.aiso.entity.search.SrcModulAktiva;
import com.dimata.aiso.entity.masterdata.PstModulAktiva;
import com.dimata.aiso.entity.masterdata.PstAktiva;
import com.dimata.aiso.entity.masterdata.ModulAktiva;
import com.dimata.aiso.entity.masterdata.Aktiva;
import com.dimata.aiso.entity.aktiva.PstOrderAktiva;
import com.dimata.aiso.entity.aktiva.PstOrderAktivaItem;
import com.dimata.aiso.db.DBResultSet;
import com.dimata.aiso.db.DBHandler;

import com.dimata.aiso.entity.periode.Periode;
import com.dimata.aiso.entity.periode.PstPeriode;
import com.dimata.aiso.session.periode.SessPeriode;
import com.dimata.util.Formater;
import java.util.Vector;
import java.sql.ResultSet;
import java.util.Date;

public class SessModulAktiva {
    public static final String SESS_SEARCH_MODUL_AKTIVA = "SESS_SEARCH_MODUL_AKTIVA";

    /** gadnyana
     * untuk menampilkan list aktiva
     * @param srcModulAktiva
     * @param start
     * @param recordToGet
     * @return
     */
    public static Vector listModulAktiva(SrcModulAktiva srcModulAktiva, int start, int recordToGet) {
        DBResultSet dbrs = null;
        Vector list = new Vector();
        try {
            String sql = "SELECT " +
                    " A." + PstModulAktiva.fieldNames[PstModulAktiva.FLD_AKTIVA_ID] +
                    ",A." + PstModulAktiva.fieldNames[PstModulAktiva.FLD_KODE] +
                    ",A." + PstModulAktiva.fieldNames[PstModulAktiva.FLD_NAME] +
                    ",MA." + PstAktiva.fieldNames[PstAktiva.FLD_NAME] + " AS " + PstAktiva.fieldNames[PstAktiva.FLD_NAME] + "_A" +
                    ",MAA." + PstAktiva.fieldNames[PstAktiva.FLD_NAME] + " AS " + PstAktiva.fieldNames[PstAktiva.FLD_NAME] + "_AA" +
                    ",MAAA." + PstAktiva.fieldNames[PstAktiva.FLD_NAME] + " AS " + PstAktiva.fieldNames[PstAktiva.FLD_NAME] + "_AAA" +
                    ",A." + PstModulAktiva.fieldNames[PstModulAktiva.FLD_HARGA_PEROLEHAN] +
                    " FROM " + PstModulAktiva.TBL_AKTIVA + " AS A " +
                    " INNER JOIN " + PstAktiva.TBL_MASTER_AKTIVA + " AS MA " +
                    " ON MA." + PstAktiva.fieldNames[PstAktiva.FLD_MASTER_AKTIVA_ID] + " = A." + PstModulAktiva.fieldNames[PstModulAktiva.FLD_JENIS_AKTIVA_ID] +
                    " INNER JOIN " + PstAktiva.TBL_MASTER_AKTIVA + " AS MAA " +
                    "ON MAA." + PstAktiva.fieldNames[PstAktiva.FLD_MASTER_AKTIVA_ID] + " = A." + PstModulAktiva.fieldNames[PstModulAktiva.FLD_TYPE_PENYUSUTAN_ID] +
                    " INNER JOIN " + PstAktiva.TBL_MASTER_AKTIVA + " AS MAAA " +
                    "ON MAAA." + PstAktiva.fieldNames[PstAktiva.FLD_MASTER_AKTIVA_ID] + " = A." + PstModulAktiva.fieldNames[PstModulAktiva.FLD_METODE_PENYUSUTAN_ID];

            if (srcModulAktiva.getOrderOid() != 0) {
                sql = sql + " INNER JOIN " + PstOrderAktivaItem.TBL_ORDER_AKTIVA_ITEM + " AS OI " +
                        " ON OI." + PstOrderAktivaItem.fieldNames[PstOrderAktivaItem.FLD_AKTIVA_ID] +
                        " = A." + PstModulAktiva.fieldNames[PstModulAktiva.FLD_AKTIVA_ID];
            }

            String where = "";
            // kode
            if (srcModulAktiva.getKodeAktiva().length() > 0) {
                where = " A." + PstModulAktiva.fieldNames[PstModulAktiva.FLD_KODE] + " LIKE '%" + srcModulAktiva.getKodeAktiva() + "%'";
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

            sql = sql + " ORDER BY A." + PstModulAktiva.fieldNames[PstModulAktiva.FLD_KODE];

            System.out.println(sql);
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

            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while (rs.next()) {
                Vector vt = new Vector();
                ModulAktiva modulAktiva = new ModulAktiva();
                modulAktiva.setOID(rs.getLong(PstModulAktiva.fieldNames[PstModulAktiva.FLD_AKTIVA_ID]));
                modulAktiva.setKode(rs.getString(PstModulAktiva.fieldNames[PstModulAktiva.FLD_KODE]));
                modulAktiva.setName(rs.getString(PstModulAktiva.fieldNames[PstModulAktiva.FLD_NAME]));
                modulAktiva.setHargaPerolehan(rs.getDouble(PstModulAktiva.fieldNames[PstModulAktiva.FLD_HARGA_PEROLEHAN]));

                Aktiva aktiva = new Aktiva();
                aktiva.setNama(rs.getString(PstAktiva.fieldNames[PstAktiva.FLD_NAME] + "_A"));
                aktiva.setNamaTipepenyusutan(rs.getString(PstAktiva.fieldNames[PstAktiva.FLD_NAME] + "_AA"));
                aktiva.setNamaMetodepenyusutan(rs.getString(PstAktiva.fieldNames[PstAktiva.FLD_NAME] + "_AAA"));

                vt.add(modulAktiva);
                vt.add(aktiva);
                list.add(vt);
            }
	    rs.close();
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
    public static int countModulAktiva(SrcModulAktiva srcModulAktiva) {
        DBResultSet dbrs = null;
        int count = 0;
        try {
            String sql = "SELECT " +
                    " COUNT(A." + PstModulAktiva.fieldNames[PstModulAktiva.FLD_AKTIVA_ID] + ")" +
                    " FROM " + PstModulAktiva.TBL_AKTIVA + " AS A " +
                    " INNER JOIN " + PstAktiva.TBL_MASTER_AKTIVA + " AS MA " +
                    " ON MA." + PstAktiva.fieldNames[PstAktiva.FLD_MASTER_AKTIVA_ID] + " = A." + PstModulAktiva.fieldNames[PstModulAktiva.FLD_JENIS_AKTIVA_ID] +
                    " INNER JOIN " + PstAktiva.TBL_MASTER_AKTIVA + " AS MAA " +
                    "ON MAA." + PstAktiva.fieldNames[PstAktiva.FLD_MASTER_AKTIVA_ID] + " = A." + PstModulAktiva.fieldNames[PstModulAktiva.FLD_TYPE_PENYUSUTAN_ID] +
                    " INNER JOIN " + PstAktiva.TBL_MASTER_AKTIVA + " AS MAAA " +
                    "ON MAAA." + PstAktiva.fieldNames[PstAktiva.FLD_MASTER_AKTIVA_ID] + " = A." + PstModulAktiva.fieldNames[PstModulAktiva.FLD_METODE_PENYUSUTAN_ID];

            if (srcModulAktiva.getOrderOid() != 0) {
                sql = sql + " INNER JOIN " + PstOrderAktivaItem.TBL_ORDER_AKTIVA_ITEM + " AS OI " +
                        " ON OI." + PstOrderAktivaItem.fieldNames[PstOrderAktivaItem.FLD_AKTIVA_ID] +
                        " = A." + PstModulAktiva.fieldNames[PstModulAktiva.FLD_AKTIVA_ID];
            }

            String where = "";
            // kode
            if (srcModulAktiva.getKodeAktiva().length() > 0) {
                where = " A." + PstModulAktiva.fieldNames[PstModulAktiva.FLD_KODE] + " LIKE '%" + srcModulAktiva.getKodeAktiva() + "%'";
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

    public static synchronized double countDepAccumulation(long lFixedAssets){
	double dResult = 0.0;
	long lCurrPeriodId = 0;
	int iTotMonth = 0;
	int iLife = 0;
	double dAqcValue = 0.0;
	Date startPeriod = new Date();
	Date aqcDate = new Date();
	Periode objPeriode = new Periode();
	ModulAktiva objModulAktiva = new ModulAktiva();
	try{
	    lCurrPeriodId = PstPeriode.getCurrPeriodId();
	    if(lCurrPeriodId != 0){
		objPeriode = PstPeriode.fetchExc(lCurrPeriodId);
		startPeriod = (Date)objPeriode.getTglAwal();
	    }
	}catch(Exception e){}
	
	if(lFixedAssets != 0){
	    try{
		objModulAktiva = PstModulAktiva.fetchExc(lFixedAssets);
		aqcDate = (Date)objModulAktiva.getTglPerolehan();
		if(aqcDate == null){
		    aqcDate = new Date();
		}
		iLife = objModulAktiva.getMasaManfaat();
		dAqcValue = objModulAktiva.getHargaPerolehan();
	    }catch(Exception e){}
	}
	
	iTotMonth = SessPeriode.getTotMonth(aqcDate, startPeriod);
	if(iTotMonth > 0){
	    dResult = (dAqcValue / (iLife * 12)) * iTotMonth;
	}
	return dResult;
    }
}
