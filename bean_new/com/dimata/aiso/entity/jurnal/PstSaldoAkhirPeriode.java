
package com.dimata.aiso.entity.jurnal;

/* import java */
import java.io.*;
import java.sql.*;
import java.util.*;

/* import qdep  */
import com.dimata.util.*;
import com.dimata.aiso.db.*;
import com.dimata.qdep.entity.Entity;
import com.dimata.qdep.entity.I_PersintentExc;

public class PstSaldoAkhirPeriode extends DBHandler implements I_DBInterface, I_DBType, I_PersintentExc {
    
    public static final String TBL_SALDO_AKHIR = "aiso_saldo_akhir_perd";
    public static final String TBL_SALDO_AKHIR_BUFFER = "aiso_saldo_akhir_perd_buffer";
    
    public static final int FLD_IDPERIODE        = 0;
    public static final int FLD_IDPERKIRAAN      = 1;
    public static final int FLD_DEBET            = 2;
    public static final int FLD_KREDIT		 = 3;

    public static String[] fieldNames = {
        "PERIODE_ID",
        "ID_PERKIRAAN",
        "DEBET",
        "KREDIT"
    };
    
    public static int[] fieldTypes = {
        TYPE_PK + TYPE_FK + TYPE_LONG,
        TYPE_PK + TYPE_FK + TYPE_LONG,
        TYPE_FLOAT,
        TYPE_FLOAT
    };
    
    
    public static long NO_ERR = 0;
    public static long ERR_INVALID_DATE_TO_CLOSE_PERIOD = 1;
    public static long ERR_TRANSFER_BALANCE_TO_SA = 2;    
    public static long ERR_INVALID_EARNING_ACCOUNT = 3;    
    public static String[][] arrErrClosingPeriod = 
    {
        {
            "Proses tutup periode sukses", 
            "Proses tutup periode gagal, hari ini bukan tanggal yang valid untuk tutup periode", 
            "Proses tutup periode gagal, proses transfer saldo jurnal ke periode baru gagal", 
            "Proses tutup periode gagal, perkiraan earning belum di-set. Silahkan check account link", 
        },
        {
            "Congratulation, closing period success", 
            "Closing period failed, today is not valid date to closing period", 
            "Closing period failed, transfer journal balance to new period failed", 
            "Closing period failed, earning account haven't setting yet. Please check master data account link", 
        }
    };
    
    public PstSaldoAkhirPeriode() {
    }
    
    public PstSaldoAkhirPeriode(int i) throws DBException {
        super(new PstSaldoAkhirPeriode());
    }
    
    
    public PstSaldoAkhirPeriode(String sOid) throws DBException {
        super(new PstSaldoAkhirPeriode(0));
        if(!locate(sOid))
            throw new DBException(this, DBException.RECORD_NOT_FOUND);
        else
            return;
    }
    
    
    public PstSaldoAkhirPeriode(long periodeId, long idPerkiraan) throws DBException {
        super(new PstSaldoAkhirPeriode(0));
        if(!locate(periodeId,idPerkiraan))
            throw new DBException(this, DBException.RECORD_NOT_FOUND);
        else
            return;
        
    }
    
    public int getFieldSize() {
        return fieldNames.length;
    }
    
    public String getTableName() {
        return TBL_SALDO_AKHIR;
    }
    
    public String[] getFieldNames() {
        return fieldNames;
    }
    
    public int[] getFieldTypes() {
        return fieldTypes;
    }
    
    public String getPersistentName() {
        return new PstSaldoAkhirPeriode().getClass().getName();
    }
    
    public long fetchExc(Entity ent) throws Exception{
        SaldoAkhirPeriode saldoakhirperiode = fetchExc(ent.getOID(0),ent.getOID(1));
        ent = (Entity)saldoakhirperiode;
        return saldoakhirperiode.getOID(0);
    }
    
    public long insertExc(Entity ent) throws Exception {
        return insertExc((SaldoAkhirPeriode) ent);
    }
    
    public long updateExc(Entity ent) throws Exception {
        return updateExc((SaldoAkhirPeriode) ent);
    }
    
    public long deleteExc(Entity ent) throws Exception {
        if(ent==null){
            throw  new DBException(this, DBException.RECORD_NOT_FOUND) ;
        }
        return deleteExc((SaldoAkhirPeriode) ent);
    }
    
    public static SaldoAkhirPeriode fetchExc(long periodeId, long idPerkiraan) throws DBException {
        try{
            SaldoAkhirPeriode saldoakhirperiode = new SaldoAkhirPeriode();
            PstSaldoAkhirPeriode pSaldoAkhirPeriode = new PstSaldoAkhirPeriode(periodeId, idPerkiraan);
            saldoakhirperiode.setPeriodeId(periodeId);
            saldoakhirperiode.setIdPerkiraan(idPerkiraan);
            saldoakhirperiode.setDebet(pSaldoAkhirPeriode.getdouble(FLD_DEBET));
            saldoakhirperiode.setKredit(pSaldoAkhirPeriode.getdouble(FLD_KREDIT));
            
            return saldoakhirperiode;
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException( new PstSaldoAkhirPeriode(0), DBException.UNKNOWN);
        }
    }
    
    
    public static long insertExc(SaldoAkhirPeriode saldoakhirperiode) throws DBException {
        try {
            PstSaldoAkhirPeriode pSaldoAkhirPeriode = new PstSaldoAkhirPeriode(0);
            
            pSaldoAkhirPeriode.setLong(FLD_IDPERIODE, saldoakhirperiode.getPeriodeId());
            pSaldoAkhirPeriode.setLong(FLD_IDPERKIRAAN, saldoakhirperiode.getIdPerkiraan());
            pSaldoAkhirPeriode.setDouble(FLD_DEBET, saldoakhirperiode.getDebet());
            pSaldoAkhirPeriode.setDouble(FLD_KREDIT, saldoakhirperiode.getKredit());
            
            pSaldoAkhirPeriode.insert();
            return saldoakhirperiode.getPeriodeId();
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException( new PstSaldoAkhirPeriode(0), DBException.UNKNOWN);
        }
    }
    
    public static long updateExc(SaldoAkhirPeriode saldoakhirperiode) throws DBException {
        try {
            if(saldoakhirperiode != null && saldoakhirperiode.getOID() != 0) {
                PstSaldoAkhirPeriode pSaldoAkhirPeriode = new PstSaldoAkhirPeriode(saldoakhirperiode.getPeriodeId(), saldoakhirperiode.getIdPerkiraan());
                
                pSaldoAkhirPeriode.setDouble(FLD_DEBET, saldoakhirperiode.getDebet());
                pSaldoAkhirPeriode.setDouble(FLD_KREDIT, saldoakhirperiode.getKredit());
                
                pSaldoAkhirPeriode.update();
                return saldoakhirperiode.getPeriodeId();
            }
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException( new PstSaldoAkhirPeriode(0), DBException.UNKNOWN);
        }
        return 0;
    }
    
    
    public static long deleteExc(long periodeId, long idPerkiraan)  throws DBException {
        try {
            PstSaldoAkhirPeriode pSaldoAkhirPeriode = new PstSaldoAkhirPeriode(periodeId,idPerkiraan);
            pSaldoAkhirPeriode.delete();
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException( new PstSaldoAkhirPeriode(0), DBException.UNKNOWN);
        }
        return periodeId;
    }
    
    
    public static Vector listAll() {
        return list(0, 500, "","");
    }
    
    
    public static Vector list(int limitStart, int recordToGet, String whereClause, String order) {
        Vector lists = new Vector();
        DBResultSet dbrs = null;
        try {
            
            String sql = "SELECT * FROM " + TBL_SALDO_AKHIR + " ";
            
            if(whereClause != null && whereClause.length() > 0)
                sql = sql + " WHERE " + whereClause;
            
            if(order != null && order.length() > 0)
                sql = sql + " ORDER BY " + order;
            
            if(limitStart == 0 && recordToGet == 0)
                sql = sql + "" ;
            else
                sql = sql + " LIMIT " + limitStart + ","+ recordToGet ;
            
            //System.out.println("SQL LIST SALDO AWAL :::: "+sql);
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            
            while(rs.next()) {
                SaldoAkhirPeriode saldoakhirperiode = new SaldoAkhirPeriode();
                resultToObject(rs, saldoakhirperiode);
                lists.add(saldoakhirperiode);
            }
            rs.close();
            return lists;
            
        }catch(Exception e) {
            System.out.println(e);
        }finally {
            DBResultSet.close(dbrs);
        }
        return new Vector();
    }
    
    
    private static void resultToObject(ResultSet rs, SaldoAkhirPeriode saldoakhirperiode) {
        try {
            saldoakhirperiode.setOID(rs.getLong(PstSaldoAkhirPeriode.fieldNames[PstSaldoAkhirPeriode.FLD_IDPERIODE]));
            saldoakhirperiode.setIdPerkiraan(rs.getLong(PstSaldoAkhirPeriode.fieldNames[PstSaldoAkhirPeriode.FLD_IDPERKIRAAN]));
            saldoakhirperiode.setDebet(rs.getDouble(PstSaldoAkhirPeriode.fieldNames[PstSaldoAkhirPeriode.FLD_DEBET]));
            saldoakhirperiode.setKredit(rs.getDouble(PstSaldoAkhirPeriode.fieldNames[PstSaldoAkhirPeriode.FLD_KREDIT]));
        }catch(Exception e){ }
    }
    
    public static int getCount(String whereClause) {
        DBResultSet dbrs = null;
        try {
            String sql = "SELECT COUNT("+PstSaldoAkhirPeriode.fieldNames[PstSaldoAkhirPeriode.FLD_IDPERIODE]+") "+
            " FROM " + TBL_SALDO_AKHIR;
            if(whereClause != null && whereClause.length() > 0)
                sql = sql + " WHERE " + whereClause;
            
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            
            int count = 0;
            while(rs.next()) {
                count = rs.getInt(1);
            }
            rs.close();
            return count;
            
        }catch(Exception e) {
            System.out.println(e);
            return 0;
        }finally {
            DBResultSet.close(dbrs);
        }
    }
    
    
    /**
     * Used to get account with it's appropriate value
     * @param <CODE>idPeriode</CODE>OID of selected Period
     * @param <CODE>currency</CODE>selected currency
     */
    /*
    public static Vector getSumAccountValue(long idPeriode, int currency) {
        
        PstSaldoAkhirPeriode pstSaldoAkhirPeriode = new PstSaldoAkhirPeriode();
        DBResultSet dbrs = null;
        Vector result = new Vector(1, 1);
        
        // get OID of earning year account
        long earnYearId = 0;//PstAccountLink.getLinkAccountId(CtrlAccountLink.TYPE_CURRENT_EARNING_YR);
        
        // get previous period OID of 'idPeriode'
        long prevPeriodId = PstPeriode.getPeriodIdJustBefore(idPeriode);
        
        // last periode is unavailable
        if (prevPeriodId == 0) {
            
            try {
                
                // insert data from selected period into table "SALDO_TMP"
                pstSaldoAkhirPeriode.fillTemporary(idPeriode, currency);
                double earningOpening = 0;//PstPerkiraan.getPeriodEarningAccount(currency);
                
                
                // hide by edhy on 08 09, 2005
                //                if (currency == PstJurnalUmum.CURRENCY_RUPIAH) {
                //                    Vector vectCurrEarnIrd = new Vector(1,1); //SessPLPeriodic.getPeriodCurrentEarning(idPeriode, PstJurnalUmum.CURRENCY_RUPIAH, 0);
                //
                //                    if (vectCurrEarnIrd != null && vectCurrEarnIrd.size() > 0) {
                //                        Vector tempResult = (Vector) vectCurrEarnIrd.get(0);
                //                        SaldoAkhirPeriode sa = (SaldoAkhirPeriode) tempResult.get(1);
                //                        pstSaldoAkhirPeriode.updateCurrEarning(earnYearId, PstJurnalUmum.CURRENCY_RUPIAH, sa.getDebet(), (sa.getKredit() + earningOpening));
                //                    }
                //                } else {
                //                    Vector vectCurrEarnUsd = new Vector(1,1); //SessPLPeriodic.getPeriodCurrentEarning(idPeriode, PstJurnalUmum.CURRENCY_DOLLAR, 0);
                //                    if (vectCurrEarnUsd != null && vectCurrEarnUsd.size() > 0) {
                //                        Vector tempResult = (Vector) vectCurrEarnUsd.get(0);
                //                        SaldoAkhirPeriode sa = (SaldoAkhirPeriode) tempResult.get(1);
                //                        pstSaldoAkhirPeriode.updateCurrEarning(earnYearId, PstJurnalUmum.CURRENCY_DOLLAR, sa.getDebet(), (sa.getKredit() + earningOpening));
                //                    }
                //                }
                
                //                String sql = "SELECT DISTINCT ACC.ID_PERKIRAAN, ";
                //                if(currency==PstJurnalUmum.CURRENCY_RUPIAH){
                //                    sql = sql + " IF(ISNULL(ST.ID_PERKIRAAN)=0, SUM(ACC.OB_DEBET_IDR+ST.DEBET), ACC.OB_DEBET_IDR) AS DEBET," +
                //                                " IF(ISNULL(ST.ID_PERKIRAAN)=0, SUM(ACC.OB_KREDIT_IDR+ST.KREDIT), ACC.OB_KREDIT_IDR) AS KREDIT";
                //                }else{
                //                    sql = sql + " IF(ISNULL(ST.ID_PERKIRAAN)=0, SUM(ACC.OB_DEBET_USD+ST.DEBET), ACC.OB_DEBET_USD) AS DEBET," +
                //                                " IF(ISNULL(ST.ID_PERKIRAAN)=0, SUM(ACC.OB_KREDIT_USD+ST.KREDIT), ACC.OB_KREDIT_USD) AS KREDIT";
                //                }
                //                sql = sql + " FROM " + PstPerkiraan.TBL_PERKIRAAN + " AS ACC " +
                //                      " LEFT JOIN SALDO_TMP AS ST " +
                //                      " ON ACC." + PstPerkiraan.fieldNames[PstPerkiraan.FLD_IDPERKIRAAN] + " = " + " ST.ID_PERKIRAAN " +
                //                      " AND ST.MATAUANG = "  + currency +
                //                      " WHERE ACC." + PstPerkiraan.fieldNames[PstPerkiraan.FLD_POSTABLE] + " = " + PstPerkiraan.ACC_POSTED +
                //                      " GROUP BY ACC." + PstPerkiraan.fieldNames[PstPerkiraan.FLD_IDPERKIRAAN];
                //
                //                IF(SUBSTRING(ACC.NOMOR_PERKIRAAN,1,1) IN (1,2,3),
                //                IF(ISNULL(ST.ID_PERKIRAAN)=0, SUM(SA.DEBET+ST.DEBET), SA.DEBET),
                //                IF(ISNULL(ST.ID_PERKIRAAN)=0, SUM(ST.DEBET), 0)
                //                ) AS DEBET,
                
                
                String debetSql = "INSERT INTO AISO_SALDO(ID_PERKIRAAN, DEBET) SELECT DISTINCT ACC." + PstPerkiraan.fieldNames[PstPerkiraan.FLD_IDPERKIRAAN]
                + ", ";
                String kreditSql = "INSERT INTO AISO_SALDO(ID_PERKIRAAN, KREDIT) SELECT DISTINCT ACC." + PstPerkiraan.fieldNames[PstPerkiraan.FLD_IDPERKIRAAN]
                + ", ";
                
                /*
                 * by pass by edhy on 20050729
                if (currency == PstJurnalUmum.CURRENCY_RUPIAH) {
                    debetSql += "IF (SUBSTRING(ACC." + PstPerkiraan.fieldNames[PstPerkiraan.FLD_NOPERKIRAAN]
                              + ", 1, 1) IN (1, 2, 3), IF (ISNULL(ST.ID_PERKIRAAN) = 0, SUM(ACC." + PstPerkiraan.fieldNames[PstPerkiraan.FLD_OP_BALANCE_DEBET]
                              + " + ST.DEBET), ACC." + PstPerkiraan.fieldNames[PstPerkiraan.FLD_OP_BALANCE_DEBET]
                              + "), IF (ISNULL(ST.ID_PERKIRAAN) = 0, SUM(ST.DEBET), 0)) AS DEBET ";
                    kreditSql += "IF (SUBSTRING(ACC." + PstPerkiraan.fieldNames[PstPerkiraan.FLD_NOPERKIRAAN]
                               + ", 1, 1) IN (1, 2, 3), IF (ISNULL(ST.ID_PERKIRAAN) = 0, SUM(ACC." + PstPerkiraan.fieldNames[PstPerkiraan.FLD_OP_BALANCE_KREDIT]
                               + " + ST.KREDIT), ACC." + PstPerkiraan.fieldNames[PstPerkiraan.FLD_OP_BALANCE_KREDIT]
                               + "), IF (ISNULL(ST.ID_PERKIRAAN) = 0, SUM(ST.KREDIT), 0)) AS KREDIT ";
                } else {
                    debetSql += "IF (SUBSTRING(ACC." + PstPerkiraan.fieldNames[PstPerkiraan.FLD_NOPERKIRAAN]
                              + ", 1, 1) IN (1, 2, 3), IF (ISNULL(ST.ID_PERKIRAAN) = 0, SUM(ACC." + PstPerkiraan.fieldNames[PstPerkiraan.FLD_OB_DEBET_USD]
                              + " + ST.DEBET), ACC." + PstPerkiraan.fieldNames[PstPerkiraan.FLD_OB_DEBET_USD]
                              + "), IF (ISNULL(ST.ID_PERKIRAAN) = 0, SUM(ST.DEBET), 0)) AS DEBET ";
                    kreditSql += "IF (SUBSTRING(ACC." + PstPerkiraan.fieldNames[PstPerkiraan.FLD_NOPERKIRAAN]
                               + ", 1, 1) IN (1, 2, 3), IF (ISNULL(ST.ID_PERKIRAAN) = 0, SUM(ACC." + PstPerkiraan.fieldNames[PstPerkiraan.FLD_OB_KREDIT_USD]
                               + " + ST.KREDIT), ACC." + PstPerkiraan.fieldNames[PstPerkiraan.FLD_OB_KREDIT_USD]
                               + "), IF (ISNULL(ST.ID_PERKIRAAN) = 0, SUM(ST.KREDIT), 0)) AS KREDIT ";
                }
                 */
                
    /*
                String lastSql = "FROM " + PstPerkiraan.TBL_PERKIRAAN
                + " AS ACC LEFT JOIN SALDO_TMP AS ST ON ACC." + PstPerkiraan.fieldNames[PstPerkiraan.FLD_IDPERKIRAAN]
                + " = ST.ID_PERKIRAAN AND ST.MATAUANG = " + currency
                + " WHERE ACC." + PstPerkiraan.fieldNames[PstPerkiraan.FLD_POSTABLE]
                + " = " + PstPerkiraan.ACC_POSTED
                + " GROUP BY ACC." + PstPerkiraan.fieldNames[PstPerkiraan.FLD_IDPERKIRAAN];
                debetSql += lastSql;
                kreditSql += lastSql;
                
                DBHandler.execUpdate("DELETE FROM AISO_SALDO");
                DBHandler.execUpdate(debetSql);
                DBHandler.execUpdate(kreditSql);
                
                String sql = "SELECT DISTINCT ID_PERKIRAAN, SUM(DEBET) AS DEBET, SUM(KREDIT) AS KREDIT FROM AISO_SALDO GROUP BY ID_PERKIRAAN ORDER BY ID_PERKIRAAN";
                
                //System.out.println("getSumAccountValue && lastPeriodId==0 SQL : " + kreditSql);
                dbrs = DBHandler.execQueryResult(sql);
                ResultSet rs = dbrs.getResultSet();
                while (rs.next()) {
                    SaldoAkhirPeriode sap = new SaldoAkhirPeriode();
                    sap.setPeriodeId(idPeriode);
                    sap.setIdPerkiraan(rs.getLong("ID_PERKIRAAN"));                    
                    sap.setDebet(rs.getDouble("DEBET"));
                    sap.setKredit(rs.getDouble("KREDIT"));
                    result.add(sap);
                }
                pstSaldoAkhirPeriode.emptyTemporary();
            } catch (Exception e) {
                System.out.println("err getSumAccountValue : " + e.toString());
            } finally {
                DBResultSet.close(dbrs);
            }
            
            // not the first periode, get previous data from saldo_akhir_periode table
            
        } else {
            try {
                pstSaldoAkhirPeriode.fillTemporary(idPeriode, currency);
                
                // hide by edhy on 08 09, 2005
                //                if (currency == PstJurnalUmum.CURRENCY_RUPIAH) {
                //                    Vector vectCurrEarnIrd = new Vector(1,1);// SessPLPeriodic.getPeriodCurrentEarning(idPeriode, PstJurnalUmum.CURRENCY_RUPIAH, 0);
                //                    if (vectCurrEarnIrd != null && vectCurrEarnIrd.size() > 0) {
                //                        Vector tempResult = (Vector) vectCurrEarnIrd.get(0);
                //                        SaldoAkhirPeriode sa = (SaldoAkhirPeriode) tempResult.get(1);
                //                        pstSaldoAkhirPeriode.updateCurrEarning(earnYearId, PstJurnalUmum.CURRENCY_RUPIAH, sa.getDebet(), sa.getKredit());
                //                    }
                //                } else {
                //                    Vector vectCurrEarnUsd = new Vector(1,1);// SessPLPeriodic.getPeriodCurrentEarning(idPeriode, PstJurnalUmum.CURRENCY_DOLLAR, 0);
                //                    if (vectCurrEarnUsd != null && vectCurrEarnUsd.size() > 0) {
                //                        Vector tempResult = (Vector) vectCurrEarnUsd.get(0);
                //                        SaldoAkhirPeriode sa = (SaldoAkhirPeriode) tempResult.get(1);
                //                        pstSaldoAkhirPeriode.updateCurrEarning(earnYearId, PstJurnalUmum.CURRENCY_DOLLAR, sa.getDebet(), sa.getKredit());
                //                    }
                //                }
                
                
                //                String sql = "SELECT DISTINCT SA.ID_PERKIRAAN, SA.MATAUANG, " +
                //                " IF(ISNULL(ST.ID_PERKIRAAN)=0, SUM(SA.DEBET+ST.DEBET), SA.DEBET) AS DEBET," +
                //                " IF(ISNULL(ST.ID_PERKIRAAN)=0, SUM(SA.KREDIT+ST.KREDIT), SA.KREDIT) AS KREDIT" +
                //                " FROM " + PstSaldoAkhirPeriode.TBL_SALDO_AKHIR + " AS SA " +
                //                " LEFT JOIN SALDO_TMP AS ST " +
                //                " ON SA." + PstSaldoAkhirPeriode.fieldNames[PstSaldoAkhirPeriode.FLD_IDPERKIRAAN] + " = " + " ST.ID_PERKIRAAN " +
                //                " AND SA." + PstSaldoAkhirPeriode.fieldNames[PstSaldoAkhirPeriode.FLD_MATAUANG] + " = " + " ST.MATAUANG " +
                //                " INNER JOIN " + PstPerkiraan.TBL_PERKIRAAN + " AS ACC " +
                //                " ON SA." + PstSaldoAkhirPeriode.fieldNames[PstSaldoAkhirPeriode.FLD_IDPERKIRAAN] + " = " +
                //                " ACC." + PstPerkiraan.fieldNames[PstPerkiraan.FLD_IDPERKIRAAN] +
                //                " WHERE SA." + PstSaldoAkhirPeriode.fieldNames[PstSaldoAkhirPeriode.FLD_IDPERIODE] + " = "  + prevPeriodId +
                //                " AND SA." + PstSaldoAkhirPeriode.fieldNames[PstSaldoAkhirPeriode.FLD_MATAUANG] + " = "  + currency +
                //                " GROUP BY SA." + PstSaldoAkhirPeriode.fieldNames[PstSaldoAkhirPeriode.FLD_IDPERKIRAAN];
                
                
                String debetSql = "INSERT INTO AISO_SALDO(ID_PERKIRAAN, DEBET) SELECT DISTINCT SA." + PstSaldoAkhirPeriode.fieldNames[PstSaldoAkhirPeriode.FLD_IDPERKIRAAN]
                + ", IF (SUBSTRING(ACC." + PstPerkiraan.fieldNames[PstPerkiraan.FLD_NOPERKIRAAN]
                + ", 1, 1) IN (1, 2, 3), IF (ISNULL(ST.ID_PERKIRAAN) = 0, SUM(SA." + PstSaldoAkhirPeriode.fieldNames[PstSaldoAkhirPeriode.FLD_DEBET]
                + " + ST.DEBET), SA." + PstSaldoAkhirPeriode.fieldNames[PstSaldoAkhirPeriode.FLD_DEBET]
                + "), IF (ISNULL(ST.ID_PERKIRAAN) = 0, SUM(ST.DEBET), 0)) AS DEBET ";
                String kreditSql = "INSERT INTO AISO_SALDO(ID_PERKIRAAN, KREDIT) SELECT DISTINCT SA." + PstSaldoAkhirPeriode.fieldNames[PstSaldoAkhirPeriode.FLD_IDPERKIRAAN]
                + ", IF (SUBSTRING(ACC." + PstPerkiraan.fieldNames[PstPerkiraan.FLD_NOPERKIRAAN]
                + ", 1, 1) IN (1, 2, 3), IF (ISNULL(ST.ID_PERKIRAAN) = 0, SUM(SA." + PstSaldoAkhirPeriode.fieldNames[PstSaldoAkhirPeriode.FLD_KREDIT]
                + " + ST.KREDIT), SA." + PstSaldoAkhirPeriode.fieldNames[PstSaldoAkhirPeriode.FLD_KREDIT]
                + "), IF (ISNULL(ST.ID_PERKIRAAN) = 0, SUM(ST.KREDIT), 0)) AS KREDIT ";
                
                //                String sql = "SELECT DISTINCT SA." + PstSaldoAkhirPeriode.fieldNames[PstSaldoAkhirPeriode.FLD_IDPERKIRAAN]
                //                           + ", SA." + PstSaldoAkhirPeriode.fieldNames[PstSaldoAkhirPeriode.FLD_MATAUANG]
                //                           + ", IF (SUBSTRING(ACC." + PstPerkiraan.fieldNames[PstPerkiraan.FLD_NOPERKIRAAN]
                //                           + ", 1, 1) IN (1, 2, 3), IF (ISNULL(ST.ID_PERKIRAAN) = 0, SUM(SA." + PstSaldoAkhirPeriode.fieldNames[PstSaldoAkhirPeriode.FLD_DEBET]
                //                           + " + ST.DEBET), SA." + PstSaldoAkhirPeriode.fieldNames[PstSaldoAkhirPeriode.FLD_DEBET]
                //                           + "), IF (ISNULL(ST.ID_PERKIRAAN) = 0, SUM(ST.DEBET), 0)) AS DEBET, IF (SUBSTRING(ACC." + PstPerkiraan.fieldNames[PstPerkiraan.FLD_NOPERKIRAAN]
                //                           + ", 1, 1) IN (1, 2, 3), IF (ISNULL(ST.ID_PERKIRAAN) = 0, SUM(SA." + PstSaldoAkhirPeriode.fieldNames[PstSaldoAkhirPeriode.FLD_KREDIT]
                //                           + " + ST.KREDIT), SA." + PstSaldoAkhirPeriode.fieldNames[PstSaldoAkhirPeriode.FLD_KREDIT]
                //                           + "), IF (ISNULL(ST.ID_PERKIRAAN) = 0, SUM(ST.KREDIT), 0)) AS KREDIT FROM " + PstSaldoAkhirPeriode.TBL_SALDO_AKHIR
                //                           + " AS SA LEFT JOIN SALDO_TMP AS ST ON SA." + PstSaldoAkhirPeriode.fieldNames[PstSaldoAkhirPeriode.FLD_IDPERKIRAAN]
                //                           + " = ST.ID_PERKIRAAN AND SA." + PstSaldoAkhirPeriode.fieldNames[PstSaldoAkhirPeriode.FLD_MATAUANG]
                //                           + " = ST.MATAUANG INNER JOIN " + PstPerkiraan.TBL_PERKIRAAN
                //                           + " AS ACC ON SA." + PstSaldoAkhirPeriode.fieldNames[PstSaldoAkhirPeriode.FLD_IDPERKIRAAN]
                //                           + " = ACC." + PstPerkiraan.fieldNames[PstPerkiraan.FLD_IDPERKIRAAN]
                //                           + " WHERE SA." + PstSaldoAkhirPeriode.fieldNames[PstSaldoAkhirPeriode.FLD_IDPERIODE]
                //                           + " = " + prevPeriodId
                //                           + " AND SA." + PstSaldoAkhirPeriode.fieldNames[PstSaldoAkhirPeriode.FLD_MATAUANG]
                //                           + " = " + currency
                //                           + " GROUP BY SA." + PstSaldoAkhirPeriode.fieldNames[PstSaldoAkhirPeriode.FLD_IDPERKIRAAN];
                
                String lastSql = "FROM " + PstSaldoAkhirPeriode.TBL_SALDO_AKHIR
                + " AS SA LEFT JOIN SALDO_TMP AS ST ON SA." + PstSaldoAkhirPeriode.fieldNames[PstSaldoAkhirPeriode.FLD_IDPERKIRAAN]
                + " = ST.ID_PERKIRAAN AND SA." + PstSaldoAkhirPeriode.fieldNames[PstSaldoAkhirPeriode.FLD_MATAUANG]
                + " = ST.MATAUANG INNER JOIN " + PstPerkiraan.TBL_PERKIRAAN
                + " AS ACC ON SA." + PstSaldoAkhirPeriode.fieldNames[PstSaldoAkhirPeriode.FLD_IDPERKIRAAN]
                + " = ACC." + PstPerkiraan.fieldNames[PstPerkiraan.FLD_IDPERKIRAAN]
                + " WHERE SA." + PstSaldoAkhirPeriode.fieldNames[PstSaldoAkhirPeriode.FLD_IDPERIODE]
                + " = " + prevPeriodId
                + " AND SA." + PstSaldoAkhirPeriode.fieldNames[PstSaldoAkhirPeriode.FLD_MATAUANG]
                + " = " + currency
                + " GROUP BY SA." + PstSaldoAkhirPeriode.fieldNames[PstSaldoAkhirPeriode.FLD_IDPERKIRAAN];
                debetSql += lastSql;
                kreditSql += lastSql;
                
                DBHandler.execUpdate("DELETE FROM AISO_SALDO");
                DBHandler.execUpdate(debetSql);
                DBHandler.execUpdate(kreditSql);
                
                String sql = "SELECT DISTINCT ID_PERKIRAAN, SUM(DEBET) AS DEBET, SUM(KREDIT) AS KREDIT FROM AISO_SALDO GROUP BY ID_PERKIRAAN ORDER BY ID_PERKIRAAN";
                
                //System.out.println("getSumAccountValue && lastPeriodId!=0 SQL : "+sql);
                dbrs = DBHandler.execQueryResult(sql);
                ResultSet rs = dbrs.getResultSet();
                while (rs.next()) {
                    SaldoAkhirPeriode sa = new SaldoAkhirPeriode();
                    sa.setPeriodeId(idPeriode);
                    sa.setIdPerkiraan(rs.getLong("ID_PERKIRAAN"));
                    sa.setMataUang(currency);
                    sa.setDebet(rs.getDouble("DEBET"));
                    sa.setKredit(rs.getDouble("KREDIT"));
                    result.add(sa);
                }
                pstSaldoAkhirPeriode.emptyTemporary();
            } catch (Exception e) {
                System.out.println("PstSaldoAkhirPeriode.getSumAccountValue() err : " + e.toString());
            } finally {
                DBResultSet.close(dbrs);
            }
        }
        return result;
    }
    */
    
    
    /**
     * this method used to save transaction data into 'temporary table' called saldo_temp
     */
    /*
    public void fillTemporary(long idPeriode, int currency) {
        /*
        DBResultSet dbrs = null;
         
        try {
            String sql = "INSERT INTO SALDO_TMP SELECT DISTINCT " +
                         " JT." + PstJurnalDetail.fieldNames[PstJurnalDetail.FLD_IDPERKIRAAN] + ","  +
                         " JU." + PstJurnalUmum.fieldNames[PstJurnalUmum.FLD_MATAUANG] + ","  +
                         " SUM(JT." + PstJurnalDetail.fieldNames[PstJurnalDetail.FLD_DEBET] + ") AS DEBET," +
                         " SUM(JT." + PstJurnalDetail.fieldNames[PstJurnalDetail.FLD_KREDIT] + ") AS KREDIT" +
                         " FROM " + PstJurnalDetail.TBL_JURNAL_DETAIL + " AS JT " +
                         " INNER JOIN " + PstJurnalUmum.TBL_JURNAL_UMUM + " AS JU " +
                         " ON JT." + PstJurnalDetail.fieldNames[PstJurnalDetail.FLD_JURNALID] +
                         " = JU." + PstJurnalUmum.fieldNames[PstJurnalUmum.FLD_JURNALID] +
                         " WHERE JU." + PstJurnalUmum.fieldNames[PstJurnalUmum.FLD_PERIODEID]  + " = " + idPeriode +
                         " AND JU." + PstJurnalUmum.fieldNames[PstJurnalUmum.FLD_MATAUANG] + " = " + currency +
                         " GROUP BY JT." + PstJurnalDetail.fieldNames[PstJurnalDetail.FLD_IDPERKIRAAN] +
                         ", " + PstJurnalUmum.fieldNames[PstJurnalUmum.FLD_MATAUANG];
         
            System.out.println("PstSaldoAkhirPeriode.fillTemporary() sql : " + sql);
            int status = DBHandler.execUpdate(sql);
        }catch(Exception e){
            System.out.println("PstSaldoAkhirPeriode.fillTemporary() err : " + e.toString());
        }finally{
            DBResultSet.close(dbrs);
        }
         */
    /*
    }
     **/
    
    /**
     * this method used to insert "period current earning" to temporary table
     */
    /*
    public void updateCurrEarning(long idCurrEarning, int currency, double valDebet, double valCredit){
        DBResultSet dbrs = null;
        
        // check if current earning period account already exist
        boolean isExist = checkAccInSaldoTemp(idCurrEarning,currency);
        try{
            String sql = "";
            if(!isExist){
                sql = "INSERT INTO SALDO_TMP" +
                " VALUES("+idCurrEarning+","+currency+","+
                Formater.formatNumber(valDebet,"###")+","+
                Formater.formatNumber(valCredit,"###")+")";
            }else{
                sql = "UPDATE SALDO_TMP" +
                " SET DEBET = DEBET + " + Formater.formatNumber(valDebet,"###") + ", " +
                " KREDIT = KREDIT + " + Formater.formatNumber(valCredit,"###") +
                " WHERE ID_PERKIRAAN = " + idCurrEarning +
                " AND MATAUANG = " + currency;
            }
            System.out.println("PstSaldoAkhirPeriode.insertCurrEarning() sql : " + sql);
            int status = DBHandler.execUpdate(sql);
        }catch(Exception e){
            System.out.println("PstSaldoAkhirPeriode.insertCurrEarning() err :" + e.toString());
        }finally{
            DBResultSet.close(dbrs);
        }
    }
    */
    
    /**
     * this method used to insert "period current earning" to temporary table
     */
    /*
    public static void updateCurrEarn(int intInsTo, long idCurrEarning, double valDebet, double valCredit){
        DBResultSet dbrs = null;
        
        // get table name of destination table
        //String tableName = intInsTo == SessTrialBalance.PER_PREV ? "TRIALPREV" : "TRIALCURR";
        String tableName = "TRIALCURR";
        try{
            String sql = "UPDATE " + tableName +
            " SET DEBET = DEBET - " + Formater.formatNumber(valDebet,"###") + ", " +
            " KREDIT = KREDIT - " + Formater.formatNumber(valCredit,"###") +
            " WHERE ID_PERKIRAAN = " + idCurrEarning;
            System.out.println("PstSaldoAkhirPeriode.updateCurrEarn() sql : " + sql);
            int status = DBHandler.execUpdate(sql);
        }catch(Exception e){
            System.out.println("PstSaldoAkhirPeriode.updateCurrEarn() err :" + e.toString());
        }finally{
            DBResultSet.close(dbrs);
        }
    }
    */
    
    /**
     * this method used to data in 'temporary table' called saldo_temp
     */
    /*
    public static boolean checkAccInSaldoTemp(long accId, int currency){
        DBResultSet dbrs = null;
        boolean result = false;
        try{
            String sql = "SELECT ID_PERKIRAAN FROM SALDO_TMP WHERE ID_PERKIRAAN = " + accId +
            " AND MATAUANG = " + currency;
            System.out.println("PstSaldoAkhirPeriode.checkAccInSaldoTemp() sql : "+sql);
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while(rs.next()){
                result = true;
            }
        }catch(Exception e){
            System.out.println("PstSaldoAkhirPeriode.checkAccInSaldoTemp() err : "+e.toString());
        }finally{
            DBResultSet.close(dbrs);
            
            
        }
        return result;
    }
    */
    
    /**
     * this method used to data in 'temporary table' called saldo_temp
     */
    /*
    public void emptyTemporary() {
        DBResultSet dbrs = null;
        try {
            String sql = "DELETE FROM SALDO_TMP ";
            System.out.println("PstSaldoAkhirPeriode.emptyTemporary() sql : " + sql);
            int status = DBHandler.execUpdate(sql);
        } catch (Exception e) {
            System.out.println("PstSaldoAkhirPeriode.emptyTemporary() err : " + e.toString());
        } finally {
            DBResultSet.close(dbrs);
        }
    }
    */
    
    /**
     * This method used to get account with it's appropriate value
     */
    /*
    public static Vector getSumAccountValueCurrPeriod(long idPeriode){
        Vector result = new Vector(1,1);
        /*
        DBResultSet dbrs = null;
        try{
            String sql = "SELECT DISTINCT JU." +
                         PstJurnalUmum.fieldNames[PstJurnalUmum.FLD_PERIODEID] + ", " +
                         PstJurnalDetail.fieldNames[PstJurnalDetail.FLD_IDPERKIRAAN] + ","  +
                         PstJurnalUmum.fieldNames[PstJurnalUmum.FLD_MATAUANG] + ","  +
                         PstJurnalUmum.fieldNames[PstJurnalUmum.FLD_RATE] + ","  +
                         " SUM(" + PstJurnalDetail.fieldNames[PstJurnalDetail.FLD_DEBET] + ") AS " +
                         PstJurnalDetail.fieldNames[PstJurnalDetail.FLD_DEBET] + "," +
                         " SUM(" + PstJurnalDetail.fieldNames[PstJurnalDetail.FLD_KREDIT] + ") AS " +
                         PstJurnalDetail.fieldNames[PstJurnalDetail.FLD_KREDIT] +
                         " FROM " + PstJurnalDetail.TBL_JURNAL_DETAIL + " AS JT " +
                         " INNER JOIN " + PstJurnalUmum.TBL_JURNAL_UMUM + " AS JU " +
                         " ON JT." + PstJurnalDetail.fieldNames[PstJurnalDetail.FLD_JURNALID] +
                         " = JU." + PstJurnalUmum.fieldNames[PstJurnalUmum.FLD_JURNALID] +
                         " INNER JOIN " + PstPeriode.TBL_PERIODE + " AS PER " +
                         " ON PER." + PstPeriode.fieldNames[PstPeriode.FLD_IDPERIODE] +
                         " = JU." + PstJurnalUmum.fieldNames[PstJurnalUmum.FLD_PERIODEID] +
                         " WHERE PER." + PstPeriode.fieldNames[PstPeriode.FLD_IDPERIODE]  +
                         " = " + idPeriode +
                         " GROUP BY JT." + PstJurnalDetail.fieldNames[PstJurnalDetail.FLD_IDPERKIRAAN] +
                         ", " + PstJurnalUmum.fieldNames[PstJurnalUmum.FLD_MATAUANG];
         
            System.out.println("PstSaldoAkhirPeriode.getSumAccountValueCurrPeriod sql : "+sql);
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while(rs.next()){
                 Vector tempSaldo = new Vector(1,1);
                 JurnalUmum jUmum = new JurnalUmum();
                 JurnalDetail jDetail = new JurnalDetail();
         
                 jUmum.setPeriodeId(rs.getLong(PstJurnalUmum.fieldNames[PstJurnalUmum.FLD_PERIODEID]));
                 jUmum.setMataUang(rs.getInt(PstJurnalUmum.fieldNames[PstJurnalUmum.FLD_MATAUANG]));
                 jUmum.setKurs(rs.getDouble(PstJurnalUmum.fieldNames[PstJurnalUmum.FLD_RATE]));
         
                 jDetail.setIdPerkiraan(rs.getLong(PstJurnalDetail.fieldNames[PstJurnalDetail.FLD_IDPERKIRAAN]));
                 jDetail.setDebet(rs.getDouble(PstJurnalDetail.fieldNames[PstJurnalDetail.FLD_DEBET]));
                 jDetail.setKredit(rs.getDouble(PstJurnalDetail.fieldNames[PstJurnalDetail.FLD_KREDIT]));
         
                 tempSaldo.add(jUmum);
                 tempSaldo.add(jDetail);
         
                 result.add(tempSaldo);
            }
            rs.close();
         
        }catch(Exception e){
            System.out.println("PstSaldoAkhirPeriode.getSumAccountValueCurrPeriod err : " + e.toString());
        } finally{
            DBResultSet.close(dbrs);
         
        }
         */
    /*
        return result;
    }
     */
    
    /*
    public static boolean insertNewAccountToSa(long idPerkiraan){
        boolean result = false;
        if(idPerkiraan != 0){
            long periodIdBeforeCurrent = PstPeriode.getPeriodIdJustBefore(PstPeriode.getCurrPeriodId());
            if(periodIdBeforeCurrent!=0) {
                for(int i=0; i<2; i++){
                    SaldoAkhirPeriode saldoAkhir = new SaldoAkhirPeriode();
                    saldoAkhir.setPeriodeId(periodIdBeforeCurrent);
                    saldoAkhir.setIdPerkiraan(idPerkiraan);
                    saldoAkhir.setMataUang(i);
                    saldoAkhir.setDebet(0);
                    saldoAkhir.setKredit(0);
                    
                    try{
                        PstSaldoAkhirPeriode pstSaldoAkhir = new PstSaldoAkhirPeriode();
                        long oid = pstSaldoAkhir.insertExc(saldoAkhir);
                        if(oid!=0){
                            result = true;
                        }
                    }catch (Exception exc){
                        System.out.println("Exception when insertNewAccountToSa : "+exc.toString());
                        result = false;
                        break;
                    }
                }
            }
        }
        return result;
    }
    */
    
    /*
    public static boolean insertSumDataToSa(Vector vectSumData) {
        boolean result = false;
        if (vectSumData != null && vectSumData.size() > 0) {
            for (int i = 0; i < vectSumData.size(); i++) {
                SaldoAkhirPeriode sa = (SaldoAkhirPeriode) vectSumData.get(i);
                SaldoAkhirPeriode saldoAkhir = new SaldoAkhirPeriode();
                saldoAkhir.setPeriodeId(sa.getPeriodeId());
                saldoAkhir.setIdPerkiraan(sa.getIdPerkiraan());
                saldoAkhir.setMataUang(sa.getMataUang());
                saldoAkhir.setDebet(sa.getDebet());
                saldoAkhir.setKredit(sa.getKredit());
                
                try {
                    PstSaldoAkhirPeriode pstSaldoAkhir = new PstSaldoAkhirPeriode();
                    long oid = pstSaldoAkhir.insertExc(saldoAkhir);
                    if (oid !=0 ) {
                        result = true;
                    }
                } catch (Exception exc){
                    System.out.println("Exception when Insert into SA : " + exc.toString());
                    result = false;
                    break;
                }
            }
        }
        return result;
    }
    */
    
    /*
    public static void main(String args[]){
           /*Vector vectLastPeriod = SessPeriode.getLastPeriodId();
           if(vectLastPeriod!=null && vectLastPeriod.size()>0){
                Periode per = (Periode)vectLastPeriod.get(0);
                System.out.println("last Periode Id : " + per.getOID());
           }*/
           /*if(vectSum!=null && vectSum.size()>0){
             for(int i=0; i<vectSum.size(); i++){
                Vector tempResult = (Vector)vectSum.get(i);
                JurnalUmum ju = (JurnalUmum)tempResult.get(0);
                JurnalDetail jd = (JurnalDetail)tempResult.get(1);
            
                System.out.println(ju.getPeriodeId() + " " + jd.getIdPerkiraan() + " " + ju.getMataUang() + " " +jd.getDebet() + " " + jd.getKredit());
             }
           }*/
        //PstSaldoAkhirPeriode saldoAkhir = new PstSaldoAkhirPeriode();
        //saldoAkhir.fillTemporary(504404209856215557L,PstJurnalUmum.CURRENCY_RUPIAH);
        
        //getSumAccountValue(long idPeriode, int currency)
        
        //        Vector vectResult = getSumAccountValue(504404190600984649L,PstJurnalUmum.CURRENCY_RUPIAH);
        //        if(vectResult!=null && vectResult.size()>0){
        //            System.out.println("vectResult : " + vectResult.size());
        //            PstSaldoAkhirPeriode.insertSumDataToSa(vectResult);
        //        }else{
        //            System.out.println("vectResult empty");
        //        }
        
        /*
        double dbl = PstPerkiraan.getPeriodEarningAccount(PstJurnalUmum.CURRENCY_RUPIAH);
        System.out.println("result : "+dbl);
         */
        
    //}
    
}
