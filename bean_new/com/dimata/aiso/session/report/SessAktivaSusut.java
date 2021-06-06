/**
 * Created by IntelliJ IDEA.
 * User: gadnyana
 * Date: Nov 8, 2005
 * Time: 3:05:17 PM
 * To change this template use Options | File Templates.
 */
package com.dimata.aiso.session.report;

import com.dimata.aiso.db.DBResultSet;
import com.dimata.aiso.db.DBHandler;
import com.dimata.aiso.entity.masterdata.ModulAktiva;
import com.dimata.aiso.entity.masterdata.PstModulAktiva;
import com.dimata.aiso.entity.aktiva.*;
import com.dimata.aiso.entity.periode.Periode;
import com.dimata.aiso.entity.periode.PstPeriode;
import com.dimata.aiso.entity.report.AktivaSusut;
import com.dimata.util.Formater;

import com.dimata.aiso.entity.masterdata.Aktiva;
import com.dimata.aiso.entity.masterdata.PstAktiva;
import com.dimata.aiso.entity.jurnal.JurnalUmum;
import com.dimata.aiso.entity.jurnal.PstJurnalUmum;
import com.dimata.aiso.session.jurnal.SessJurnal;
import com.dimata.aiso.entity.jurnal.JurnalDetail;
import com.dimata.aiso.entity.jurnal.PstJurnalDetail;
import com.dimata.aiso.entity.masterdata.PstAktivaLocation;
import com.dimata.aiso.entity.report.PstReportFixedAssets;
import com.dimata.aiso.entity.report.ReportFixedAssets;
import com.dimata.aiso.form.jurnal.CtrlJurnalUmum;
import com.dimata.aiso.session.aktiva.SessReceiveAktiva;
import com.dimata.aiso.session.aktiva.SessSellingAktiva;

import java.util.Vector;
import java.util.Date;
import java.util.Hashtable;
import java.sql.ResultSet;

public class SessAktivaSusut {

    /** gadnyana
     * untuk mencari laporan keluar masknya aktiva
     * dan penyusutan serta laha rugi aktiva
     * @param periodeOid
     * @param typeOid
     * @param metodeOid
     * @return
     */
    public static Vector getReportAktivaAndPenyusutan(long periodeOid, long typeOid, long metodeOid, int start, int recordToGet) {
        DBResultSet dbrs = null;        
        Vector list = new Vector(); 
        Periode periode = new Periode();
        long preOidPeriod = 0;
        try {
            if(periodeOid != 0){
                try{
                    preOidPeriod = SessWorkSheet.getOidPeriodeLalu(periodeOid);
                }catch(Exception e){}
            }
            
            if(periodeOid != 0){
                try{
                    periode = PstPeriode.fetchExc(periodeOid);
                }catch(Exception e){}
            }
            
            String sql = "select temp.metode_penyusutan_id,temp.code,temp.name,temp.tanggal_receive,temp.masa_manfaat,sum(temp.beli) as total_beli,sum(temp.jual) as total_jual, sum(temp.total_susut) as t_susut,sum(temp.value_pny) as total_value_pny " +
                    " from (select ak.metode_penyusutan_id,ak.code,ak.name,rec.tanggal_receive,ak.masa_manfaat,ak.harga_perolehan as beli,0 as jual, 0 as total_susut,0 as value_pny , 0 as pny_jual" +
                    " from aiso_aktiva_receive_item as it " +
                    " inner join aiso_aktiva_receive as rec on it.receive_aktiva_id = rec.receive_aktiva_id " +
                    " inner join aiso_aktiva ak on it.aktiva_id = ak.aktiva_id " +
                    " where rec.tanggal_receive between '" + Formater.formatDate(periode.getTglAwal(), "yyyy-MM-dd") + "' and '" + Formater.formatDate(periode.getTglAkhir(), "yyyy-MM-dd") + "'";
            if (metodeOid != 0)
                sql = sql + " and ak.metode_penyusutan_id = " + metodeOid;
            if (typeOid != 0)
                sql = sql + " and ak.type_penyusutan_id = " + typeOid;

            sql = sql + " union " +
                    " select ak.metode_penyusutan_id,ak.code,ak.name,ak.tgl_perolehan,ak.masa_manfaat, 0 as beli ,ak.harga_perolehan as jual, 0 as total_susut, 0 as value_pny, 0 as pny_jual" +
                    " from aiso_aktiva_selling_item as it " +
                    " inner join aiso_aktiva_selling as rec on it.selling_aktiva_id = rec.selling_aktiva_id " +
                    " inner join aiso_aktiva ak on it.aktiva_id = ak.aktiva_id " +
                    " where rec.tanggal_selling between '" + Formater.formatDate(periode.getTglAwal(), "yyyy-MM-dd") + "' and '" + Formater.formatDate(periode.getTglAkhir(), "yyyy-MM-dd") + "'";
            if (metodeOid != 0)
                sql = sql + " and ak.metode_penyusutan_id = " + metodeOid;
            if (typeOid != 0)
                sql = sql + " and ak.type_penyusutan_id = " + typeOid;
            sql = sql + " union " +
                    " select ak.metode_penyusutan_id, ak.code,ak.name,ak.tgl_perolehan, ak.masa_manfaat, 0 as beli,0 as jual,sum(it.value_pny) as total_susut, 0 as value_pny, 0 as pny_jual" +
                    " from aiso_aktiva_penyusutan as it " +
                    " inner join aiso_aktiva ak on it.aktiva_id = ak.aktiva_id " +
                    " where it.periode_id = " + preOidPeriod;
            if (metodeOid != 0)
                sql = sql + " and ak.metode_penyusutan_id = " + metodeOid;
            if (typeOid != 0)
                sql = sql + " and ak.type_penyusutan_id = " + typeOid;
            sql = sql + " group by ak.metode_penyusutan_id, ak.code,ak.name,ak.tgl_perolehan,ak.masa_manfaat ";

            sql = sql + " union " +
                    " select ak.metode_penyusutan_id, ak.code,ak.name,ak.tgl_perolehan, ak.masa_manfaat, 0 as beli,0 as jual,0 as total_susut, value_pny ,0 as pny_jual" +
                    " from aiso_aktiva_penyusutan as it " +
                    " inner join aiso_aktiva ak on it.aktiva_id = ak.aktiva_id " +
                    " where it.periode_id = " + periodeOid;
            
             sql = sql + " union " +
                    " select ak.metode_penyusutan_id, ak.code,ak.name,ak.tgl_perolehan, ak.masa_manfaat, 0 as beli,0 as jual,0 as total_susut, value_pny ,0 as pny_jual" +
                    " from aiso_aktiva_penyusutan as it " +
                    " inner join aiso_aktiva ak on it.aktiva_id = ak.aktiva_id " +
                    " where it.periode_id = " + periodeOid;
            
            if (metodeOid != 0)
                sql = sql + " and ak.metode_penyusutan_id = " + metodeOid;
            if (typeOid != 0)
                sql = sql + " and ak.type_penyusutan_id = " + typeOid;
            //sql = sql + " group by ak.metode_penyusutan_id, ak.code,ak.name,ak.tgl_perolehan,ak.masa_manfaat " +

            sql = sql + " ) as temp " +
                    " group by temp.metode_penyusutan_id,temp.code,temp.name,temp.tanggal_receive,temp.masa_manfaat " +
                    " order by temp.code";          
                        
            
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
                        
            System.out.println("sql : "+sql);                       
            dbrs = DBHandler.execQueryResult(sql);           
            ResultSet rs = dbrs.getResultSet();
            Hashtable has = new Hashtable();
            while (rs.next()) {
                AktivaSusut aktivaSusut = new AktivaSusut();

                aktivaSusut.setKode(rs.getString(PstModulAktiva.fieldNames[PstModulAktiva.FLD_KODE]));
                aktivaSusut.setNamaaktiva(rs.getString(PstModulAktiva.fieldNames[PstModulAktiva.FLD_NAME]));
                aktivaSusut.setMasaManfaat(rs.getInt(PstModulAktiva.fieldNames[PstModulAktiva.FLD_MASA_MANFAAT]));
                aktivaSusut.setTglPerolehan(rs.getDate(PstReceiveAktiva.fieldNames[PstReceiveAktiva.FLD_TANGGAL_RECEIVE]));
                aktivaSusut.setMutasiTambah(rs.getDouble("total_beli"));
                aktivaSusut.setMustasiKurang(rs.getDouble("total_jual"));
                aktivaSusut.setTotalBulanIni(aktivaSusut.getMutasiTambah() - aktivaSusut.getMustasiKurang());

                aktivaSusut.setSusutBulanlalu(rs.getDouble("t_susut"));
                if (aktivaSusut.getMutasiTambah() != 0)
                    aktivaSusut.setSusutTambah(rs.getDouble("total_value_pny"));
                if (aktivaSusut.getMustasiKurang() != 0)
                    aktivaSusut.setSusutKurang(rs.getDouble("total_value_pny"));
                aktivaSusut.setTotalSusutBulanIni(aktivaSusut.getSusutTambah() - aktivaSusut.getSusutKurang());

                // penghitungan nilai buku
                aktivaSusut.setNilaiBuku(aktivaSusut.getTotalBulanIni() - aktivaSusut.getTotalSusutBulanIni());

                has.put(aktivaSusut.getKode(), aktivaSusut);
                //list.add(aktivaSusut);
            }

            list = getReportAktivaAndPenyusutan(has, periode.getTglAwal(), typeOid, metodeOid, preOidPeriod);

        } catch (Exception e) {
            System.out.println("err getReportAktivaAndPenyusutan : " + e.toString());
        }
        return list;
    }


    public static Vector getReportAktivaAndPenyusutan(Hashtable has, Date startdate, long typeOid, long metodeOid, long preOidPeriod) {
        DBResultSet dbrs = null;
        Vector list = new Vector();
        try {
            String sql = "select temp.code,temp.name,temp.tgl_perolehan,temp.masa_manfaat,sum(temp.beli) as total_beli,sum(temp.tsusut) as total_susut from" +
                    " (select ak.code,ak.name,ak.tgl_perolehan,ak.masa_manfaat,ak.harga_perolehan as beli,0 as tsusut " +
                    " from aiso_aktiva_receive_item as it " +
                    " inner join aiso_aktiva_receive as rec on it.receive_aktiva_id = rec.receive_aktiva_id " +
                    " inner join aiso_aktiva ak on it.aktiva_id = ak.aktiva_id " +
                    " where rec.tanggal_receive <= '" + Formater.formatDate(startdate, "yyyy-MM-dd") + "' ";
            if (metodeOid != 0)
                sql = sql + " and ak.metode_penyusutan_id = " + metodeOid;
            if (typeOid != 0)
                sql = sql + " and ak.type_penyusutan_id = " + typeOid;
            sql = sql + " and it.aktiva_id not in(select tt.aktiva_id from aiso_aktiva_selling_item as tt " +
                    " inner join aiso_aktiva_selling as sell on tt.selling_aktiva_id = sell.selling_aktiva_id " +
                    " inner join aiso_aktiva akk on tt.aktiva_id = akk.aktiva_id " +
                    " where sell.tanggal_selling <= '" + Formater.formatDate(startdate, "yyyy-MM-dd") + "') ";

            sql = sql + " union select ak.code,ak.name,ak.tgl_perolehan, ak.masa_manfaat, 0 as beli,sum(it.value_pny) as tsusut" +
                    " from aiso_aktiva_penyusutan as it " +
                    " inner join aiso_aktiva ak on it.aktiva_id = ak.aktiva_id " +
                    " where it.periode_id = " + preOidPeriod;
            if (metodeOid != 0)
                sql = sql + " and ak.metode_penyusutan_id = " + metodeOid;
            if (typeOid != 0)
                sql = sql + " and ak.type_penyusutan_id = " + typeOid;
            sql = sql + " group by ak.code,ak.name,ak.tgl_perolehan,ak.masa_manfaat";

            sql = sql + " ) as temp " +
                    " group by temp.code,temp.name,temp.tgl_perolehan,temp.masa_manfaat " +
                    " order by temp.code";

            System.out.println(sql);
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while (rs.next()) {
                AktivaSusut aktivaSusut = new AktivaSusut();

                aktivaSusut.setKode(rs.getString(PstModulAktiva.fieldNames[PstModulAktiva.FLD_KODE]));
                aktivaSusut.setNamaaktiva(rs.getString(PstModulAktiva.fieldNames[PstModulAktiva.FLD_NAME]));
                aktivaSusut.setMasaManfaat(rs.getInt(PstModulAktiva.fieldNames[PstModulAktiva.FLD_MASA_MANFAAT]));
                aktivaSusut.setTglPerolehan(rs.getDate(PstModulAktiva.fieldNames[PstModulAktiva.FLD_TGL_PEROLEHAN]));
                aktivaSusut.setMutasiProlehBulanLalu(rs.getDouble("total_beli"));
                aktivaSusut.setTotalBulanIni(rs.getDouble("total_beli"));

                aktivaSusut.setSusutBulanlalu(rs.getDouble("total_susut"));

                if (has.contains(aktivaSusut.getKode())) {
                    AktivaSusut akSusut = (AktivaSusut) has.get(aktivaSusut.getKode());
                    akSusut.setMutasiProlehBulanLalu(aktivaSusut.getMutasiProlehBulanLalu());
                    akSusut.setTotalBulanIni(akSusut.getMutasiProlehBulanLalu() + akSusut.getTotalBulanIni());
                    has.put(aktivaSusut.getKode(), akSusut);
                } else {
                    has.put(aktivaSusut.getKode(), aktivaSusut);
                }
            }
            list = new Vector(has.values());
        } catch (Exception e) {
            System.out.println("err getReportAktivaAndPenyusutan : " + e.toString());
        }
        return list;
    }

    public static Vector getReportAktivaAndPenyusutan(long periodeOid, long typeOid, long metodeOid, long aktivaGroupOid, long perkAktivaOid, long lLocationId, int start, int recordToGet) {
        DBResultSet dbrs = null;
        Vector list = new Vector();
        long preOidPeriod = 0;
        Periode periode = new Periode();
        Date startDate = new Date();
        Date endDate = new Date();
        try {
            if(periodeOid != 0){
                try{
                    preOidPeriod = SessWorkSheet.getOidPeriodeLalu(periodeOid);            
                }catch(Exception e){}
                
                try{
                    periode = PstPeriode.fetchExc(periodeOid);
                    if(periode.getTglAwal() != null){
                        startDate = (Date)periode.getTglAwal();
                    }
                    if(periode.getTglAkhir() != null){
                        endDate = (Date)periode.getTglAkhir();
                    }
                }catch(Exception e){}
            }
            
            String sql = "select temp.metode_penyusutan_id,temp.code,temp.name,temp.aktiva_loc_id,temp.tanggal_receive,temp.masa_manfaat," +
                    " sum(temp.beli) as total_beli,sum(temp.jual) as total_jual, sum(temp.total_susut) as t_susut," +
                    " sum(temp.value_pny) as total_value_pny, sum(temp.qty_beli) as qty_beli, sum(temp.qty_jual) as qty_jual" +
                    " from (select ak.metode_penyusutan_id,ak.code,ak.name,ak.aktiva_loc_id,rec.tanggal_receive,ak.masa_manfaat," +
                    " ak.harga_perolehan as beli,0 as jual, 0 as total_susut,0 as value_pny, it.qty as qty_beli, 0 as qty_jual " +
                    " from aiso_aktiva_receive_item as it " +
                    " inner join aiso_aktiva_receive as rec on it.receive_aktiva_id = rec.receive_aktiva_id " +
                    " inner join aiso_aktiva ak on it.aktiva_id = ak.aktiva_id " +
                    " where rec.tanggal_receive between '" + Formater.formatDate(startDate, "yyyy-MM-dd") + "' and '" + Formater.formatDate(endDate, "yyyy-MM-dd") + "'";
            if (metodeOid != 0)
                sql = sql + " and ak.metode_penyusutan_id = " + metodeOid;
            if (typeOid != 0)
                sql = sql + " and ak.type_penyusutan_id = " + typeOid;
            if (aktivaGroupOid != 0)
                sql = sql + " and ak.aktiva_group_id = " + aktivaGroupOid;
            if (perkAktivaOid != 0)
                sql = sql + " and id_perkiraan_aktiva = " + perkAktivaOid;            
            if(lLocationId != 0)
                sql = sql + " and ak.aktiva_loc_id = " + lLocationId;
            
            sql = sql + " union " +
                    " select ak.metode_penyusutan_id,ak.code,ak.name,ak.aktiva_loc_id,ak.tgl_perolehan,ak.masa_manfaat, 0 as beli ," +
                    " ak.harga_perolehan as jual, 0 as total_susut, 0 as value_pny, 0 as qty_beli, it.qty as qty_jual" +
                    " from aiso_aktiva_selling_item as it " +
                    " inner join aiso_aktiva_selling as rec on it.selling_aktiva_id = rec.selling_aktiva_id " +
                    " inner join aiso_aktiva ak on it.aktiva_id = ak.aktiva_id " +
                    " where rec.tanggal_selling between '" + Formater.formatDate(startDate, "yyyy-MM-dd") + 
                    "' and '" + Formater.formatDate(endDate, "yyyy-MM-dd") + "'";
            if (metodeOid != 0)
                sql = sql + " and ak.metode_penyusutan_id = " + metodeOid;
            if (typeOid != 0)
                sql = sql + " and ak.type_penyusutan_id = " + typeOid;
            if (aktivaGroupOid != 0)
                sql = sql + " and ak.aktiva_group_id = " + aktivaGroupOid;
            if (perkAktivaOid != 0)
                sql = sql + " and id_perkiraan_aktiva = " + perkAktivaOid;
            if(lLocationId != 0)
                sql = sql + " and ak.aktiva_loc_id = " + lLocationId;
            
            sql = sql + " union " +
                    " select ak.metode_penyusutan_id, ak.code,ak.name,ak.aktiva_loc_id,ak.tgl_perolehan, ak.masa_manfaat, 0 as beli," +
                    " 0 as jual,sum(it.value_pny) as total_susut, 0 as value_pny, 0 as qty_beli, 0 as qty_jual" +
                    " from aiso_aktiva_penyusutan as it " +
                    " inner join aiso_aktiva ak on it.aktiva_id = ak.aktiva_id " +
                    " where it.periode_id = " + preOidPeriod;
            if (metodeOid != 0)
                sql = sql + " and ak.metode_penyusutan_id = " + metodeOid;
            if (typeOid != 0)
                sql = sql + " and ak.type_penyusutan_id = " + typeOid;
            if (aktivaGroupOid != 0)
                sql = sql + " and ak.aktiva_group_id = " + aktivaGroupOid;
            if (perkAktivaOid != 0)
                sql = sql + " and ak.id_perkiraan_aktiva = " + perkAktivaOid;
            if(lLocationId != 0)
                sql = sql + " and ak.aktiva_loc_id = " + lLocationId;
            
            sql = sql + " group by ak.metode_penyusutan_id, ak.code,ak.name,ak.aktiva_loc_id,ak.tgl_perolehan,ak.masa_manfaat ";

            sql = sql + " union " +
                    " select ak.metode_penyusutan_id, ak.code,ak.name,ak.aktiva_loc_id,ak.tgl_perolehan, ak.masa_manfaat, 0 as beli,0 as jual," +
                    " 0 as total_susut, it.value_pny, 0 as qty_beli, 0 as qty_jual " +
                    " from aiso_aktiva_penyusutan as it " +
                    " inner join aiso_aktiva ak on it.aktiva_id = ak.aktiva_id " +
                    " where it.periode_id = " + periodeOid;
            if (metodeOid != 0)
                sql = sql + " and ak.metode_penyusutan_id = " + metodeOid;
            if (typeOid != 0)
                sql = sql + " and ak.type_penyusutan_id = " + typeOid;
            if (aktivaGroupOid != 0)
                sql = sql + " and ak.aktiva_group_id = " + aktivaGroupOid;
            if (perkAktivaOid != 0)
                sql = sql + " and ak.id_perkiraan_aktiva = " + perkAktivaOid;
            if(lLocationId != 0)
                sql = sql + " and ak.aktiva_loc_id = " + lLocationId;
            
            sql = sql + " ) as temp " +
                    " group by temp.metode_penyusutan_id,temp.code,temp.name,temp.aktiva_loc_id,temp.tanggal_receive,temp.masa_manfaat " +
                    " order by temp.code";

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

            //System.out.println("SQL UNTUK MENCARI PENYUSUTAN DAN AKUMULASINYA ::: "+sql);
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            Hashtable has = new Hashtable();
            int iQtyBeli = 0;
            int iQtyJual = 0;
            while (rs.next()) {
                AktivaSusut aktivaSusut = new AktivaSusut();
                iQtyBeli = rs.getInt("qty_beli");
                iQtyJual = rs.getInt("qty_jual");
                aktivaSusut.setKode(rs.getString(PstModulAktiva.fieldNames[PstModulAktiva.FLD_KODE]));
                aktivaSusut.setNamaaktiva(rs.getString(PstModulAktiva.fieldNames[PstModulAktiva.FLD_NAME]));
                aktivaSusut.setMasaManfaat(rs.getInt(PstModulAktiva.fieldNames[PstModulAktiva.FLD_MASA_MANFAAT]));
                aktivaSusut.setLocationId(rs.getLong(PstModulAktiva.fieldNames[PstModulAktiva.FLD_AKTIVA_LOC_ID]));
                aktivaSusut.setTglPerolehan(rs.getDate(PstReceiveAktiva.fieldNames[PstReceiveAktiva.FLD_TANGGAL_RECEIVE]));                
                aktivaSusut.setMutasiTambah(rs.getDouble("total_beli"));
                aktivaSusut.setMustasiKurang(rs.getDouble("total_jual"));
                aktivaSusut.setTotalBulanIni(aktivaSusut.getMutasiTambah() - aktivaSusut.getMustasiKurang());                
                aktivaSusut.setSusutBulanlalu(rs.getDouble("t_susut"));
                aktivaSusut.setQuantity(iQtyBeli - iQtyJual);
                aktivaSusut.setSusutTambah(rs.getDouble("total_value_pny")); 
                if(aktivaSusut.getMustasiKurang() != 0){
                    aktivaSusut.setSusutKurang(aktivaSusut.getSusutBulanlalu() + aktivaSusut.getSusutTambah());
                }    
                aktivaSusut.setTotalSusutBulanIni(aktivaSusut.getSusutTambah() - aktivaSusut.getSusutKurang());
                aktivaSusut.setNilaiBuku(aktivaSusut.getTotalBulanIni() - aktivaSusut.getTotalSusutBulanIni());                
                has.put(aktivaSusut.getKode(), aktivaSusut);
            }

           list = getReportAktivaAndPenyusutan(has, startDate, typeOid, metodeOid, preOidPeriod, aktivaGroupOid, perkAktivaOid , lLocationId, start, recordToGet);      
            
        } catch (Exception e) {
            System.out.println("err getReportAktivaAndPenyusutan atas : " + e.toString());
        }
        return list;
    }
    
    public static Vector getReportAktivaAndPenyusutan(Hashtable has, Date startdate, long typeOid, long metodeOid, long preOidPeriod, long aktivaGroupOid, long perkAktivaOid, long locationId, int start, int recordToGet) {
        DBResultSet dbrs = null;
        Vector list = new Vector();
        try {
            String sql = "select temp.code,temp.name,temp.aktiva_loc_id, tgl_perolehan,temp.masa_manfaat,sum(temp.beli) as total_beli," +
                    " sum(temp.tsusut) as total_susut, sum(temp.qty_beli) as qty_beli, sum(temp.qty_jual) as qty_jual from" +
                    " (select ak.code,ak.name,ak.aktiva_loc_id, ak.tgl_perolehan,ak.masa_manfaat,ak.harga_perolehan as beli,0 as tsusut, " +
                    " it.qty as qty_beli, 0 as qty_jual"+
                    " from aiso_aktiva_receive_item as it " +
                    " inner join aiso_aktiva_receive as rec on it.receive_aktiva_id = rec.receive_aktiva_id " +
                    " inner join aiso_aktiva ak on it.aktiva_id = ak.aktiva_id " +
                    " where rec.tanggal_receive < '" + Formater.formatDate(startdate, "yyyy-MM-dd") + "' ";
            if (metodeOid != 0)
                sql = sql + " and ak.metode_penyusutan_id = " + metodeOid;
            if (typeOid != 0)
                sql = sql + " and ak.type_penyusutan_id = " + typeOid;
            if (aktivaGroupOid != 0)
                sql = sql + " and ak.aktiva_group_id = " + aktivaGroupOid;
            if (perkAktivaOid != 0)
                sql = sql + " and ak.id_perkiraan_aktiva = " + perkAktivaOid;
            if (locationId != 0)
                sql = sql + " and ak.aktiva_loc_id = " + locationId;
            
            sql = sql + " and it.aktiva_id not in(select tt.aktiva_id from aiso_aktiva_selling_item as tt " +
                    " inner join aiso_aktiva_selling as sell on tt.selling_aktiva_id = sell.selling_aktiva_id " +
                    " inner join aiso_aktiva akk on tt.aktiva_id = akk.aktiva_id " +
                    " where sell.tanggal_selling < '" + Formater.formatDate(startdate, "yyyy-MM-dd") + "') ";

            sql = sql + " union select ak.code,ak.name,ak.aktiva_loc_id,ak.tgl_perolehan, ak.masa_manfaat, 0 as beli,sum(it.value_pny) as tsusut," +
                    " 0 as qty_beli, 0 as qty_jual"+
                    " from aiso_aktiva_penyusutan as it " +
                    " inner join aiso_aktiva ak on it.aktiva_id = ak.aktiva_id " +
                    " where it.periode_id = " + preOidPeriod;
            if (metodeOid != 0)
                sql = sql + " and ak.metode_penyusutan_id = " + metodeOid;
            if (typeOid != 0)
                sql = sql + " and ak.type_penyusutan_id = " + typeOid;
            if (aktivaGroupOid != 0)
                sql = sql + " and ak.aktiva_group_id = " + aktivaGroupOid;
            if (perkAktivaOid != 0)
                sql = sql + " and ak.id_perkiraan_aktiva = " + perkAktivaOid;
            if (locationId != 0)
                sql = sql + " and ak.aktiva_loc_id = " + locationId;
            
            sql = sql + " group by ak.code,ak.name,ak.aktiva_loc_id,ak.tgl_perolehan,ak.masa_manfaat";

            sql = sql + " ) as temp " +
                    " group by temp.code,temp.aktiva_loc_id,temp.name,temp.tgl_perolehan,temp.masa_manfaat " +
                    " order by temp.code";
            
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

            //System.out.println("INI SQL KE DUA UNTUK ISI VEKTOR LIST ::: "+sql);
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();            
            while (rs.next()) {
                AktivaSusut aktivaSusut = new AktivaSusut();

                aktivaSusut.setKode(rs.getString(PstModulAktiva.fieldNames[PstModulAktiva.FLD_KODE]));
                aktivaSusut.setNamaaktiva(rs.getString(PstModulAktiva.fieldNames[PstModulAktiva.FLD_NAME]));
                aktivaSusut.setLocationId(rs.getLong(PstModulAktiva.fieldNames[PstModulAktiva.FLD_AKTIVA_LOC_ID]));
                aktivaSusut.setMasaManfaat(rs.getInt(PstModulAktiva.fieldNames[PstModulAktiva.FLD_MASA_MANFAAT]));
                aktivaSusut.setTglPerolehan(rs.getDate(PstModulAktiva.fieldNames[PstModulAktiva.FLD_TGL_PEROLEHAN]));
                aktivaSusut.setMutasiProlehBulanLalu(rs.getDouble("total_beli"));
                aktivaSusut.setTotalBulanIni(rs.getDouble("total_beli"));
                aktivaSusut.setSusutBulanlalu(rs.getDouble("total_susut"));               
                
                
                if (has.containsKey(aktivaSusut.getKode())) { 
                    AktivaSusut akSusut = (AktivaSusut) has.get(aktivaSusut.getKode());
                    
                    akSusut.setMutasiProlehBulanLalu(aktivaSusut.getMutasiProlehBulanLalu());
                    akSusut.setTotalBulanIni(akSusut.getMutasiProlehBulanLalu() + akSusut.getTotalBulanIni());
                    
                    akSusut.setNilaiBuku(akSusut.getTotalBulanIni() - akSusut.getTotalSusutBulanIni());                    
                    
                    has.put(aktivaSusut.getKode(), akSusut);  
                    
                }else {
                    aktivaSusut.setNilaiBuku(aktivaSusut.getTotalBulanIni() - aktivaSusut.getSusutBulanIni());
                    has.put(aktivaSusut.getKode(), aktivaSusut);                    
                }
            }
            list = new Vector(has.values());            
        } catch (Exception e) {
            System.out.println("err getReportAktivaAndPenyusutan bawah : " + e.toString());
        }
        return list;
    }
    
public static Vector getCountReportAktivaAndPenyusutan(long periodeOid, long typeOid, long metodeOid, long aktivaGroupOid, long perkAktivaOid, int start, int recordToGet) {
        DBResultSet dbrs = null;
        DBResultSet dbrs1 = null;
        Vector list = new Vector();
        try {            
            long preOidPeriod = SessWorkSheet.getOidPeriodeLalu(periodeOid);            
            Periode periode = PstPeriode.fetchExc(periodeOid);            
            String sql = "select sum(temp.beli) as total_beli,sum(temp.jual) as total_jual, sum(temp.total_susut) as t_susut,sum(temp.value_pny) as total_value_pny" +
                    " from (select ak.metode_penyusutan_id,ak.code,ak.name,rec.tanggal_receive,ak.masa_manfaat,ak.harga_perolehan as beli,0 as jual, 0 as total_susut, 0 as value_pny" +
                    " from aiso_aktiva_receive_item as it " +
                    " inner join aiso_aktiva_receive as rec on it.receive_aktiva_id = rec.receive_aktiva_id " +
                    " inner join aiso_aktiva ak on it.aktiva_id = ak.aktiva_id " +
                    " where rec.tanggal_receive between '" + Formater.formatDate(periode.getTglAwal(), "yyyy-MM-dd") + "' and '" + Formater.formatDate(periode.getTglAkhir(), "yyyy-MM-dd") + "'";
            if (metodeOid != 0)
                sql = sql + " and ak.metode_penyusutan_id = " + metodeOid;
            if (typeOid != 0)
                sql = sql + " and ak.type_penyusutan_id = " + typeOid;
            if (aktivaGroupOid != 0)
                sql = sql + " and ak.aktiva_group_id = " + aktivaGroupOid;
            if (perkAktivaOid != 0)
                sql = sql + " and id_perkiraan_aktiva = " + perkAktivaOid;
            
            sql = sql + " union " +
                    " select ak.metode_penyusutan_id,ak.code,ak.name,ak.tgl_perolehan,ak.masa_manfaat, 0 as beli ,ak.harga_perolehan as jual, 0 as total_susut, 0 as value_pny" +
                    " from aiso_aktiva_selling_item as it " +
                    " inner join aiso_aktiva_selling as rec on it.selling_aktiva_id = rec.selling_aktiva_id " +
                    " inner join aiso_aktiva ak on it.aktiva_id = ak.aktiva_id " +
                    " where rec.tanggal_selling between '" + Formater.formatDate(periode.getTglAwal(), "yyyy-MM-dd") + "' and '" + Formater.formatDate(periode.getTglAkhir(), "yyyy-MM-dd") + "'";
            if (metodeOid != 0)
                sql = sql + " and ak.metode_penyusutan_id = " + metodeOid;
            if (typeOid != 0)
                sql = sql + " and ak.type_penyusutan_id = " + typeOid;
            if (aktivaGroupOid != 0)
                sql = sql + " and ak.aktiva_group_id = " + aktivaGroupOid;
            if (perkAktivaOid != 0)
                sql = sql + " and id_perkiraan_aktiva = " + perkAktivaOid;
            
            sql = sql + " union " +
                    " select ak.metode_penyusutan_id, ak.code,ak.name,ak.tgl_perolehan, ak.masa_manfaat, 0 as beli,0 as jual,sum(it.value_pny) as total_susut, 0 as value_pny" +
                    " from aiso_aktiva_penyusutan as it " +
                    " inner join aiso_aktiva ak on it.aktiva_id = ak.aktiva_id " +
                    " where it.periode_id = " + preOidPeriod;
            if (metodeOid != 0)
                sql = sql + " and ak.metode_penyusutan_id = " + metodeOid;
            if (typeOid != 0)
                sql = sql + " and ak.type_penyusutan_id = " + typeOid;
            if (aktivaGroupOid != 0)
                sql = sql + " and ak.aktiva_group_id = " + aktivaGroupOid;
             if (perkAktivaOid != 0)
                sql = sql + " and id_perkiraan_aktiva = " + perkAktivaOid;
            
            sql = sql + " group by ak.metode_penyusutan_id, ak.code,ak.name,ak.tgl_perolehan,ak.masa_manfaat ";

            sql = sql + " union " +
                    " select ak.metode_penyusutan_id, ak.code,ak.name,ak.tgl_perolehan, ak.masa_manfaat, 0 as beli,0 as jual,0 as total_susut, it.value_pny" +
                    " from aiso_aktiva_penyusutan as it " +
                    " inner join aiso_aktiva ak on it.aktiva_id = ak.aktiva_id " +
                    " where it.periode_id = " + periodeOid;           
                         
            if (metodeOid != 0)
                sql = sql + " and ak.metode_penyusutan_id = " + metodeOid;
            if (typeOid != 0)
                sql = sql + " and ak.type_penyusutan_id = " + typeOid;
            if (aktivaGroupOid != 0)
                sql = sql + " and ak.aktiva_group_id = " + aktivaGroupOid;
            if (perkAktivaOid != 0)
                sql = sql + " and id_perkiraan_aktiva = " + perkAktivaOid;
            
                sql = sql + " ) as temp " ;
                
                
                
                String sql_1 = "select sum(it.value_pny) as t_susut_kurang"+
                            " from aiso_aktiva_penyusutan as it"+
                            " inner join aiso_aktiva_selling_item as sel"+
                            " on it.aktiva_id = sel.aktiva_id"+
                            " inner join aiso_aktiva_selling as slm "+
                            " on slm.selling_aktiva_id = sel.selling_aktiva_id"+
                            " where slm.tanggal_selling between '" + Formater.formatDate(periode.getTglAwal(), "yyyy-MM-dd") + "'"+
                            "and '" + Formater.formatDate(periode.getTglAkhir(), "yyyy-MM-dd") + "'";
                
            
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

            //System.out.println("INI SQL PERTAMA UNTUK VEKTOR COUNT TOTAL : "+sql);
            dbrs = DBHandler.execQueryResult(sql);
            dbrs1 = DBHandler.execQueryResult(sql_1);
            ResultSet rs = dbrs.getResultSet();
            ResultSet rs1 = dbrs1.getResultSet();
            Hashtable has = new Hashtable();            
            AktivaSusut aktivaSusut = new AktivaSusut();
            while (rs.next()) {                
                aktivaSusut.setMutasiTambah(rs.getDouble("total_beli"));
                aktivaSusut.setMustasiKurang(rs.getDouble("total_jual"));
                aktivaSusut.setTotalBulanIni(aktivaSusut.getMutasiTambah() - aktivaSusut.getMustasiKurang());
                aktivaSusut.setSusutBulanlalu(rs.getDouble("t_susut"));               
                aktivaSusut.setSusutTambah(rs.getDouble("total_value_pny"));                                  
              
            }//End rs
            
            while (rs1.next()){
                if(aktivaSusut.getMustasiKurang() != 0)
                aktivaSusut.setSusutKurang(rs1.getDouble("t_susut_kurang"));
            
            }//End rs1
                //Hitung total penyusutan bulan ini
                aktivaSusut.setTotalSusutBulanIni(aktivaSusut.getSusutTambah() - aktivaSusut.getSusutKurang());                
                // penghitungan nilai buku
                aktivaSusut.setNilaiBuku(aktivaSusut.getTotalBulanIni() - aktivaSusut.getTotalSusutBulanIni());
            
                //Memasukan data periode lalu ke vector list
                list = getCountReportAktivaAndPenyusutan(aktivaSusut, periode.getTglAwal(), typeOid, metodeOid, preOidPeriod, aktivaGroupOid, perkAktivaOid);
                
                AktivaSusut rstAktiva = (AktivaSusut)list.get(0);                            
                            
        } catch (Exception e) {
            System.out.println("err getReportAktivaAndPenyusutan : " + e.toString());
        }
        return list;
        
    }

public static Vector getCountReportAktivaAndPenyusutan(AktivaSusut aktivaSusut, Date startdate, long typeOid, long metodeOid, long preOidPeriod, long aktivaGroupOid, long perkAktivaOid) {
        DBResultSet dbrs = null;
        Vector list = new Vector();
        try {
            String sql = "select sum(temp.beli) as total_beli,sum(temp.tsusut) as total_susut from" +
                    " (select ak.code,ak.name,ak.tgl_perolehan,ak.masa_manfaat,ak.harga_perolehan as beli,0 as tsusut " +
                    " from aiso_aktiva_receive_item as it " +
                    " inner join aiso_aktiva_receive as rec on it.receive_aktiva_id = rec.receive_aktiva_id " +
                    " inner join aiso_aktiva ak on it.aktiva_id = ak.aktiva_id " +
                    " where rec.tanggal_receive < '" + Formater.formatDate(startdate, "yyyy-MM-dd") + "' ";
            if (metodeOid != 0)
                sql = sql + " and ak.metode_penyusutan_id = " + metodeOid;
            if (typeOid != 0)
                sql = sql + " and ak.type_penyusutan_id = " + typeOid;
            if(aktivaGroupOid != 0)
                sql = sql + " and ak.aktiva_group_id = " + aktivaGroupOid;
             if (perkAktivaOid != 0)
                sql = sql + " and id_perkiraan_aktiva = " + perkAktivaOid;
            
            sql = sql + " and it.aktiva_id not in(select tt.aktiva_id from aiso_aktiva_selling_item as tt " +
                    " inner join aiso_aktiva_selling as sell on tt.selling_aktiva_id = sell.selling_aktiva_id " +
                    " inner join aiso_aktiva akk on tt.aktiva_id = akk.aktiva_id " +
                    " where sell.tanggal_selling < '" + Formater.formatDate(startdate, "yyyy-MM-dd") + "') ";

            sql = sql + " union select ak.code,ak.name,ak.tgl_perolehan, ak.masa_manfaat, 0 as beli,sum(it.value_pny) as tsusut" +
                    " from aiso_aktiva_penyusutan as it " +
                    " inner join aiso_aktiva ak on it.aktiva_id = ak.aktiva_id " +
                    " where it.periode_id = " + preOidPeriod;
            if (metodeOid != 0)
                sql = sql + " and ak.metode_penyusutan_id = " + metodeOid;
            if (typeOid != 0)
                sql = sql + " and ak.type_penyusutan_id = " + typeOid;
            
            if(aktivaGroupOid != 0)
                sql = sql + " and ak.aktiva_group_id = " + aktivaGroupOid;
            if (perkAktivaOid != 0)
                sql = sql + " and id_perkiraan_aktiva = " + perkAktivaOid;
            
             sql = sql + " group by ak.code,ak.name,ak.tgl_perolehan,ak.masa_manfaat";

            sql = sql + " ) as temp ";

            
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while (rs.next()) {
                aktivaSusut.setMutasiProlehBulanLalu(rs.getDouble("total_beli"));
                //aktivaSusut.setTotalBulanIni(rs.getDouble("total_beli"));
                aktivaSusut.setSusutBulanlalu(rs.getDouble("total_susut"));
                aktivaSusut.setMutasiProlehBulanLalu(aktivaSusut.getMutasiProlehBulanLalu());
                aktivaSusut.setTotalBulanIni(aktivaSusut.getMutasiProlehBulanLalu() + aktivaSusut.getTotalBulanIni());
                aktivaSusut.setNilaiBuku(aktivaSusut.getTotalBulanIni() - aktivaSusut.getTotalSusutBulanIni());
                
            }                        
            list.add(aktivaSusut);            
        } catch (Exception e) {
            System.out.println("err getReportAktivaAndPenyusutan : " + e.toString());
        }
        return list;
    }

    public static Vector getMonthAktivaAndPenyusutan(Date lastDate, long typeOid, long metodeOid) {
        DBResultSet dbrs = null;
        Vector list = new Vector();
        try {
            String sql = "SELECT " +
                    " A." + PstModulAktiva.fieldNames[PstModulAktiva.FLD_AKTIVA_ID] +
                    ",A." + PstModulAktiva.fieldNames[PstModulAktiva.FLD_KODE] +
                    ",A." + PstModulAktiva.fieldNames[PstModulAktiva.FLD_NAME] +
                    ",A." + PstModulAktiva.fieldNames[PstModulAktiva.FLD_AKTIVA_GROUP_ID] +
                    ",A." + PstModulAktiva.fieldNames[PstModulAktiva.FLD_HARGA_PEROLEHAN] +
                    ",A." + PstModulAktiva.fieldNames[PstModulAktiva.FLD_TGL_PEROLEHAN] +
                    ",A." + PstModulAktiva.fieldNames[PstModulAktiva.FLD_MASA_MANFAAT] +
                    ",SUM(IT." + PstReceiveAktivaItem.fieldNames[PstReceiveAktivaItem.FLD_TOTAL_PRICE] + ")" +
                    " FROM " + PstModulAktiva.TBL_AKTIVA + " AS A " +
                    " INNER JOIN " + PstReceiveAktivaItem.TBL_RECEIVE_AKTIVA_ITEM + " AS IT ON " +
                    " A." + PstModulAktiva.fieldNames[PstModulAktiva.FLD_AKTIVA_ID] +
                    " = IT." + PstReceiveAktivaItem.fieldNames[PstReceiveAktivaItem.FLD_AKTIVA_ID] +
                    " INNER JOIN " + PstReceiveAktiva.TBL_RECEIVE_AKTIVA + " AS RA ON " +
                    " IT." + PstReceiveAktivaItem.fieldNames[PstReceiveAktivaItem.FLD_RECEIVE_AKTIVA_ID] +
                    " = RA." + PstReceiveAktiva.fieldNames[PstReceiveAktiva.FLD_RECEIVE_AKTIVA_ID] +
                    " WHERE RA." + PstReceiveAktiva.fieldNames[PstReceiveAktiva.FLD_TANGGAL_RECEIVE] + " < '" + Formater.formatDate(lastDate, "yyyy-MM-dd") + "'";

            //" WHERE "; // P." + PstPenyusutanAktiva.fieldNames[PstPenyusutanAktiva.FLD_PERIODE_ID] + "=" + periodeOid;

            if (typeOid != 0) {
                sql = sql + " WHERE A." + PstModulAktiva.fieldNames[PstModulAktiva.FLD_TYPE_PENYUSUTAN_ID] + "=" + typeOid;
            }
            if (metodeOid != 0) {
                if (typeOid != 0) {
                    sql = sql + " AND A." + PstModulAktiva.fieldNames[PstModulAktiva.FLD_METODE_PENYUSUTAN_ID] + "=" + metodeOid;
                } else {
                    sql = sql + " WHERE A." + PstModulAktiva.fieldNames[PstModulAktiva.FLD_METODE_PENYUSUTAN_ID] + "=" + metodeOid;
                }
            }

            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            double totalTambah = 0.0;
            double totalKeluar = 0.0;
            double susutTotalTambah = 0.0;
            double susutTotalKeluar = 0.0;
            while (rs.next()) {
                Vector vtlist = new Vector();
                totalKeluar = 0.0;
                totalTambah = 0.0;
                AktivaSusut aktivaSusut = new AktivaSusut();

                aktivaSusut.setKode(rs.getString(PstModulAktiva.fieldNames[PstModulAktiva.FLD_KODE]));
                aktivaSusut.setNamaaktiva(rs.getString(PstModulAktiva.fieldNames[PstModulAktiva.FLD_NAME]));
                aktivaSusut.setMasaManfaat(rs.getInt(PstModulAktiva.fieldNames[PstModulAktiva.FLD_MASA_MANFAAT]));
                aktivaSusut.setTglPerolehan(rs.getDate(PstModulAktiva.fieldNames[PstModulAktiva.FLD_TGL_PEROLEHAN]));

                // total penambahan dan pengurangan aktiva
                totalTambah = 0; //getTotalTerimaMutasi(rs.getLong(PstModulAktiva.fieldNames[PstModulAktiva.FLD_AKTIVA_ID]), periode.getTglAwal(), periode.getTglAkhir(), start, recordToGet);
                totalKeluar = 0; //getTotalPenguranganMutasi(rs.getLong(PstModulAktiva.fieldNames[PstModulAktiva.FLD_AKTIVA_ID]), periode.getTglAwal(), periode.getTglAkhir(), start, recordToGet);
                aktivaSusut.setMutasiTambah(totalTambah);
                aktivaSusut.setMustasiKurang(totalKeluar);

                aktivaSusut.setTotalBulanIni(totalTambah + totalKeluar);
                // total susut penambahan dan pengurangan susut aktiva
                susutTotalTambah = 0; //getTotalTerimaMutasi(rs.getLong(PstModulAktiva.fieldNames[PstModulAktiva.FLD_AKTIVA_ID]), periode.getTglAwal(), periode.getTglAkhir(), start, recordToGet);
                susutTotalKeluar = 0; //getTotalPenguranganMutasi(rs.getLong(PstModulAktiva.fieldNames[PstModulAktiva.FLD_AKTIVA_ID]), periode.getTglAwal(), periode.getTglAkhir(), start, recordToGet);
                aktivaSusut.setSusutTambah(susutTotalTambah);
                aktivaSusut.setSusutKurang(susutTotalKeluar);
                aktivaSusut.setTotalSusutBulanIni(susutTotalTambah + susutTotalKeluar);

                list.add(aktivaSusut);
            }
        } catch (Exception e) {
            System.out.println("err getReportAktivaAndPenyusutan : " + e.toString());
        }
        return list;
    }


    /**gadnyana
     * untuk mencari total aktiva yang terjual
     * @param oidAktiva
     * @param startDate
     * @param endDate
     * @return
     */
    public static double getTotalPenguranganMutasi(long oidAktiva, Date startDate, Date endDate, int start, int recordToGet) {
        DBResultSet dbrs = null;
        double total = 0.0;
        try {
            String sql = "select sum(item." + PstSellingAktivaItem.fieldNames[PstSellingAktivaItem.FLD_TOTAL_PRICE] + ") " +
                    " from " + PstSellingAktivaItem.TBL_SELLING_AKTIVA_ITEM + " as item " +
                    " inner join " + PstSellingAktiva.TBL_SELLING_AKTIVA + " as sell " +
                    " on item." + PstSellingAktivaItem.fieldNames[PstSellingAktivaItem.FLD_SELLING_AKTIVA_ID] +
                    " = sell." + PstSellingAktiva.fieldNames[PstSellingAktiva.FLD_SELLING_AKTIVA_ID] +
                    " where item." + PstSellingAktivaItem.fieldNames[PstSellingAktivaItem.FLD_AKTIVA_ID] +
                    " = " + oidAktiva + " and sell." + PstSellingAktiva.fieldNames[PstSellingAktiva.FLD_TANGGAL_SELLING] +
                    " between '" + Formater.formatDate(startDate, "yyyy-MM-dd") + "' and '" + Formater.formatDate(endDate, "yyy-MM-dd") + "'";

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
            System.out.println("sql : " + sql);
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while (rs.next()) {
                total = rs.getDouble(1);
            }

        } catch (Exception e) {
            System.out.println("getTotalTerimaMutasi - : " + e.toString());
        }
        return total;
    }


    /** gadnyana
     * untuk mencari total penerimaan per-aktiva
     * @return
     */
    public static double getTotalTerimaMutasi(long oidAktiva, Date startDate, Date endDate, int start, int recordToGet) {
        DBResultSet dbrs = null;
        double total = 0.0;
        try {
            String sql = "select sum(item." + PstReceiveAktivaItem.fieldNames[PstReceiveAktivaItem.FLD_TOTAL_PRICE] + ") " +
                    " from " + PstReceiveAktivaItem.TBL_RECEIVE_AKTIVA_ITEM + " as item " +
                    " inner join " + PstReceiveAktiva.TBL_RECEIVE_AKTIVA + " as rec " +
                    " on item." + PstReceiveAktivaItem.fieldNames[PstReceiveAktivaItem.FLD_RECEIVE_AKTIVA_ID] +
                    " = rec." + PstReceiveAktiva.fieldNames[PstReceiveAktiva.FLD_RECEIVE_AKTIVA_ID] +
                    " where item." + PstReceiveAktivaItem.fieldNames[PstReceiveAktivaItem.FLD_AKTIVA_ID] +
                    " = " + oidAktiva + " and rec." + PstReceiveAktiva.fieldNames[PstReceiveAktiva.FLD_TANGGAL_RECEIVE] +
                    " between '" + Formater.formatDate(startDate, "yyyy-MM-dd") + "' and '" + Formater.formatDate(endDate, "yyy-MM-dd") + "'";

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

            System.out.println("sql : " + sql);
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while (rs.next()) {
                total = rs.getDouble(1);
            }

        } catch (Exception e) {
            System.out.println("getTotalTerimaMutasi - : " + e.toString());
        } 
        return total;
    }

    
    public static Vector listPenyusutan(long periodOid, long tipePenyusutanOid, long metodePenyusutanOid) 
    {
        Vector lists = new Vector();
        DBResultSet dbrs = null;
        try 
        {
            Periode periode = PstPeriode.fetchExc(periodOid); 
            
            String sql = "select ak.*" + 
                         " from aiso_aktiva_receive_item as it" + 
                         " inner join aiso_aktiva_receive as rec on it.receive_aktiva_id = rec.receive_aktiva_id" +  
                         " inner join aiso_aktiva ak on it.aktiva_id = ak.aktiva_id" +  
                         " where rec.tanggal_receive <= '" + Formater.formatDate(periode.getTglAkhir(), "yyyy-MM-dd") + "'";  
                         
                         if (tipePenyusutanOid != 0)                             
                         {
                            sql = sql + " and ak.metode_penyusutan_id = " + metodePenyusutanOid;  
                         }

                         if (tipePenyusutanOid != 0)                             
                         {            
                            sql = sql + " and ak.type_penyusutan_id = " + tipePenyusutanOid; 
                         }
                         
                         sql = sql + " and it.aktiva_id not in (select tt.aktiva_id from aiso_aktiva_selling_item as tt" +  
                         " inner join aiso_aktiva_selling as sell on tt.selling_aktiva_id = sell.selling_aktiva_id" +  
                         " inner join aiso_aktiva akk on tt.aktiva_id = akk.aktiva_id" +  
                         " where sell.tanggal_selling <= '" + Formater.formatDate(periode.getTglAkhir(), "yyyy-MM-dd") + "')"; 

            System.out.println("listPenyusutan : " + sql);   
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while (rs.next()) 
            {
                ModulAktiva aktiva = new ModulAktiva();

                aktiva.setOID(rs.getLong(PstModulAktiva.fieldNames[PstModulAktiva.FLD_AKTIVA_ID]));
                aktiva.setKode(rs.getString(PstModulAktiva.fieldNames[PstModulAktiva.FLD_KODE]));
                aktiva.setName(rs.getString(PstModulAktiva.fieldNames[PstModulAktiva.FLD_NAME]));
                aktiva.setJenisAktivaOid(rs.getLong(PstModulAktiva.fieldNames[PstModulAktiva.FLD_JENIS_AKTIVA_ID]));
                aktiva.setTypePenyusutanOid(rs.getLong(PstModulAktiva.fieldNames[PstModulAktiva.FLD_TYPE_PENYUSUTAN_ID]));
                aktiva.setMetodePenyusutanOid(rs.getLong(PstModulAktiva.fieldNames[PstModulAktiva.FLD_METODE_PENYUSUTAN_ID]));
                aktiva.setMasaManfaat(rs.getInt(PstModulAktiva.fieldNames[PstModulAktiva.FLD_MASA_MANFAAT]));
                aktiva.setPersenPenyusutan(rs.getDouble(PstModulAktiva.fieldNames[PstModulAktiva.FLD_PERSEN_PENYUSUTAN]));

                aktiva.setHargaPerolehan(rs.getDouble(PstModulAktiva.fieldNames[PstModulAktiva.FLD_HARGA_PEROLEHAN]));
                aktiva.setNilaiResidu(rs.getDouble(PstModulAktiva.fieldNames[PstModulAktiva.FLD_NILAI_RESIDU]));
                aktiva.setIdPerkiraanAktiva(rs.getLong(PstModulAktiva.fieldNames[PstModulAktiva.FLD_ID_PERKIRAAN_AKTIVA]));
                aktiva.setIdPerkiraanByaPenyusutan(rs.getLong(PstModulAktiva.fieldNames[PstModulAktiva.FLD_ID_PERKIRAAN_BYA_PENYUSUTAN]));
                aktiva.setIdPerkiraanAkmPenyusutan(rs.getLong(PstModulAktiva.fieldNames[PstModulAktiva.FLD_ID_PERKIRAAN_AKM_PENYUSUTAN]));
                aktiva.setIdPerkiraanLbPenjAktiva(rs.getLong(PstModulAktiva.fieldNames[PstModulAktiva.FLD_ID_PERKIRAAN_LB_PENJ_AKTIVA]));
                aktiva.setIdPerkiraanRgPenjAktiva(rs.getLong(PstModulAktiva.fieldNames[PstModulAktiva.FLD_ID_PERKIRAAN_RG_PENJ_AKTIVA]));
                aktiva.setTglPerolehan(rs.getDate(PstModulAktiva.fieldNames[PstModulAktiva.FLD_TGL_PEROLEHAN]));
                aktiva.setTotalPenyusutan(rs.getDouble(PstModulAktiva.fieldNames[PstModulAktiva.FLD_TOTAL_PENYUSUTAN]));
                aktiva.setAktivaGroupOid(rs.getLong(PstModulAktiva.fieldNames[PstModulAktiva.FLD_AKTIVA_GROUP_ID]));                
                
                lists.add(aktiva);
            }
        }
        catch (Exception error) 
        {
            System.out.println(".:: " + new PstModulAktiva().getClass().getName() + ".list() : " + error.toString());
        }
        finally 
        {
            DBResultSet.close(dbrs);
        }
        return lists;
    }

    public static synchronized Vector listFADepreciation(long periodOid, long tipePenyusutanOid, long metodePenyusutanOid, int start, int recordToGet){
	DBResultSet dbrs = null;
	Vector vResult = new Vector(1,1);
	String sWhereClause = "";
	String sql = "";
	try{
	    sWhereClause = stringWhClause(periodOid, tipePenyusutanOid, metodePenyusutanOid);
	    if(sWhereClause != null && sWhereClause.length() > 0){
		sWhereClause += " AND "+PstReportFixedAssets.fieldNames[PstReportFixedAssets.FLD_DEP_INCREMENT]+" > 0 ";
	    }else{
		sWhereClause += PstReportFixedAssets.fieldNames[PstReportFixedAssets.FLD_DEP_INCREMENT]+" > 0 ";
	    }
	    sql = PstReportFixedAssets.getStringQuery(start, recordToGet, sWhereClause, "");
	    System.out.println("SQL SessAktivaSusut.listFADepreciation :::::::::::::: "+sql);
	    dbrs = DBHandler.execQueryResult(sql);
	    ResultSet rs = dbrs.getResultSet();
	    while(rs.next()){
		 ModulAktiva aktiva = new ModulAktiva();
		 long lFAId = 0;
		 double dResiduValue = 0.0;
		 double dDepreciation = 0.0;
		 dDepreciation = rs.getDouble(PstReportFixedAssets.fieldNames[PstReportFixedAssets.FLD_DEP_INCREMENT]);
		 lFAId = rs.getLong(PstReportFixedAssets.fieldNames[PstReportFixedAssets.FLD_FIXED_ASSETS_ID]);
		 dResiduValue = getResiduValue(lFAId);
		 aktiva.setKode(rs.getString(PstReportFixedAssets.fieldNames[PstReportFixedAssets.FLD_CODE]));
		 aktiva.setName(rs.getString(PstReportFixedAssets.fieldNames[PstReportFixedAssets.FLD_NAME]));
		 aktiva.setMasaManfaat(rs.getInt(PstReportFixedAssets.fieldNames[PstReportFixedAssets.FLD_LIFE]));
		 aktiva.setHargaPerolehan(rs.getDouble(PstReportFixedAssets.fieldNames[PstReportFixedAssets.FLD_AQC_THIS_MONTH]));
		 aktiva.setNilaiResidu(dResiduValue);
		 aktiva.setTotalPenyusutan(dDepreciation);
		 
		 vResult.add(aktiva);		 
	    }
	    rs.close();
	}catch(Exception e){}
	return vResult;
    }
    
    public static synchronized int depProcess(long periodOid, long tipePenyusutanOid, long metodePenyusutanOid){
	DBResultSet dbrs = null;
	int iResult = 0;
	long lReportFAId = 0;
	boolean bStillDepProcess = false;
	String sWhereClause = "";
	try{
	    sWhereClause = stringWhClause(periodOid, tipePenyusutanOid, metodePenyusutanOid);
	    String sql = PstReportFixedAssets.getStringQueryClosingReportFA(sWhereClause);
	    dbrs = DBHandler.execQueryResult(sql);
	    ResultSet rs = dbrs.getResultSet();
	    while(rs.next()){
		PenyusutanAktiva objPenyusutanAktiva = new PenyusutanAktiva();
		ReportFixedAssets objReportFixedAssets = new ReportFixedAssets();
		double dAqcValue = 0.0;
		double dBookValue = 0.0;
		double dDepThisMonth = 0.0;
		double dDepreciation = 0.0;
		double dResiduValue = 0.0;
		long lFAId = 0;
		int iLife = 0;
		Date aqcDate = new Date();
		dAqcValue = rs.getDouble(PstReportFixedAssets.fieldNames[PstReportFixedAssets.FLD_AQC_THIS_MONTH]);
		dBookValue = rs.getDouble(PstReportFixedAssets.fieldNames[PstReportFixedAssets.FLD_BOOK_VALUE]);		
		dDepThisMonth = rs.getDouble(PstReportFixedAssets.fieldNames[PstReportFixedAssets.FLD_DEP_THIS_MONTH]);
		iLife = rs.getInt(PstReportFixedAssets.fieldNames[PstReportFixedAssets.FLD_LIFE]);
		aqcDate = rs.getDate(PstReportFixedAssets.fieldNames[PstReportFixedAssets.FLD_AQC_DATE]);
		lFAId = rs.getLong(PstReportFixedAssets.fieldNames[PstReportFixedAssets.FLD_FIXED_ASSETS_ID]);
		lReportFAId = rs.getLong(PstReportFixedAssets.fieldNames[PstReportFixedAssets.FLD_REPORT_FIXED_ASSETS_ID]);
		dResiduValue = getResiduValue(lFAId);
		bStillDepProcess = isStillDepProcess(aqcDate, dBookValue, dResiduValue, iLife, periodOid);
		
		if(bStillDepProcess){
		    dDepreciation = countFADep(metodePenyusutanOid, dAqcValue, dBookValue, iLife, dResiduValue);
		}
		
		if(lReportFAId != 0 && dDepreciation > 0){
		    try{
			objReportFixedAssets = PstReportFixedAssets.fetchExc(lReportFAId);
			objReportFixedAssets.setDepIncrement(dDepreciation);
			objReportFixedAssets.setDepThisMonth(dDepThisMonth + dDepreciation);
			objReportFixedAssets.setBookValue(dBookValue - (dDepThisMonth + dDepreciation));
			long lUpdate = PstReportFixedAssets.updateExc(objReportFixedAssets);
		    }catch(Exception e){}
		}
		if(lFAId != 0 && dDepreciation > 0 && periodOid != 0){
		    objPenyusutanAktiva.setAktivaId(lFAId);
		    objPenyusutanAktiva.setPeriodeId(periodOid);
		    objPenyusutanAktiva.setValue_pny(dDepreciation);
		    try{
			long lInsert = PstPenyusutanAktiva.insertExc(objPenyusutanAktiva);
			iResult = 1;
		    }catch(Exception e){}
		}
	    }
	    rs.close();
	}catch(Exception e){}
	return iResult;
    }
    
    public static synchronized double countFADep(long metodePenyusutanOid, double aqcValue, double bookValue, int iLife, double dResidu){
	double dResult = 0.0;
	boolean bDepType = false;
	try{
	    bDepType = isStraightLine(metodePenyusutanOid);
	    if(bDepType){
		dResult = (aqcValue - dResidu) / (iLife * 12);
	    }else{
		dResult = ((bookValue - dResidu) * (1 / iLife)) / 12;
	    }
	}catch(Exception e){}
	return dResult;
    }
    
    public static boolean isStillDepProcess(Date aqcDate, double dBookValue, double dResidu, int iLife, long lPeriodId){
	boolean bResult = false;
	Periode objPeriod = new Periode();
	Date endPeriodDate = new Date();
	Date startPeriodDate = new Date();
	int iYear = 0;	
	try{
	    iYear = aqcDate.getYear() + iLife;
	    
	    Date endProcessDate = (Date)aqcDate.clone();
	    endProcessDate.setYear(iYear);
		   
	    if(lPeriodId != 0){
		try{
		    objPeriod = PstPeriode.fetchExc(lPeriodId);
		    endPeriodDate = objPeriod.getTglAkhir();
		    startPeriodDate = objPeriod.getTglAwal();
		}catch(Exception e){}
	    }
	    
	    if((endProcessDate.after(startPeriodDate) || endProcessDate.equals(startPeriodDate))&& (aqcDate.before(endPeriodDate) || aqcDate.equals(endPeriodDate))){
	         if(dBookValue > dResidu){
			bResult = true;
		 }
	    }
	}catch(Exception e){}
	return bResult;
    }
    
    public static synchronized boolean isStraightLine(long metodePenyusutanOid){
	DBResultSet dbrs = null;
	boolean bResult = true;
	int iDepType = 0;
	try{
	    String sql = " SELECT "+PstAktiva.fieldNames[PstAktiva.FLD_TYPE_METODE_PENYUSUTAN]+
			 " FROM "+PstAktiva.TBL_MASTER_AKTIVA+
			 " WHERE "+PstAktiva.fieldNames[PstAktiva.FLD_MASTER_AKTIVA_ID]+" = "+metodePenyusutanOid;
	    
	    dbrs = DBHandler.execQueryResult(sql);
	    ResultSet rs = dbrs.getResultSet();
	    while(rs.next()){
		iDepType = rs.getInt(PstAktiva.fieldNames[PstAktiva.FLD_TYPE_METODE_PENYUSUTAN]);
		if(iDepType == PstAktiva.METODE_PENYUSUTAN_SALDO_MENURUN){
		    bResult = false;
		}
	    }
	    rs.close();
	}catch(Exception e){}
	return bResult;
    }
    
    public static synchronized String stringWhClause(long periodOid, long tipePenyusutanOid, long metodePenyusutanOid){
	String sResult = "";
	try{
	   if(periodOid != 0){
		sResult += PstReportFixedAssets.fieldNames[PstReportFixedAssets.FLD_PERIOD_ID]+" = "+periodOid;
	   }
	   
	   if(tipePenyusutanOid != 0){
		if(sResult != null && sResult.length() > 0){
		    sResult += " AND "+PstReportFixedAssets.fieldNames[PstReportFixedAssets.FLD_DEP_TYPE_ID]+" = "+tipePenyusutanOid;
		}else{
		    sResult += PstReportFixedAssets.fieldNames[PstReportFixedAssets.FLD_DEP_TYPE_ID]+" = "+tipePenyusutanOid;
		}
	   }
	   	   
	   if(metodePenyusutanOid != 0){
		if(sResult != null && sResult.length() > 0){
		    sResult += " AND "+PstReportFixedAssets.fieldNames[PstReportFixedAssets.FLD_DEP_METHOD_ID]+" = "+metodePenyusutanOid;
		}else{
		    sResult += PstReportFixedAssets.fieldNames[PstReportFixedAssets.FLD_DEP_METHOD_ID]+" = "+metodePenyusutanOid;
		}
	   }
	}catch(Exception e){}
	return sResult;
    }
    
    public static synchronized double getResiduValue(long lFAId){
	DBResultSet dbrs = null;
	double dResult = 0.0;
	try{
	    String sql = " SELECT "+PstModulAktiva.fieldNames[PstModulAktiva.FLD_NILAI_RESIDU]+
			 " FROM "+PstModulAktiva.TBL_AKTIVA+
			 " WHERE "+PstModulAktiva.fieldNames[PstModulAktiva.FLD_AKTIVA_ID]+" = "+lFAId;
	    dbrs = DBHandler.execQueryResult(sql);
	    ResultSet rs = dbrs.getResultSet();
	    while(rs.next()){
		dResult = rs.getDouble(PstModulAktiva.fieldNames[PstModulAktiva.FLD_NILAI_RESIDU]);
	    }
	    rs.close();
	}catch(Exception e){}
	return dResult;
    }
    
    public static synchronized int procesTransferFAData(){
	DBResultSet dbrs = null;
	int iResult = 0;
	long lStartPeriodId = 0;
	try{
	    try{
		lStartPeriodId = getStartPeriodId();
	    }catch(Exception e){}
	    
	    String sql = " SELECT "+PstPeriode.fieldNames[PstPeriode.FLD_IDPERIODE]+
			 " FROM "+PstPeriode.TBL_PERIODE;
	    if(lStartPeriodId != 0){
		   sql += " WHERE "+PstPeriode.fieldNames[PstPeriode.FLD_IDPERIODE]+" >= "+lStartPeriodId;
	     }	     
	    dbrs = DBHandler.execQueryResult(sql);
	    ResultSet rs = dbrs.getResultSet();
	    while(rs.next()){
		long lPeriodId = 0;
		lPeriodId = rs.getLong(PstPeriode.fieldNames[PstPeriode.FLD_IDPERIODE]);
		
		if(lPeriodId != 0){
		    if(rs.isFirst()){
			iResult = transferFAData(lPeriodId, false);
		    }else{
			iResult = transferFAData(lPeriodId, true);
		    }
		}
	    }
	    rs.close();
	   
	}catch(Exception e){}
	return iResult;
    }
    
    public static synchronized int transferFAData(long lPeriodId, boolean isNextPeriod){
	DBResultSet dbrs = null;
	int iResult = 0;
	try{
	    String sql = queryTransferFAData();
	    dbrs = DBHandler.execQueryResult(sql);
	    ResultSet rs = dbrs.getResultSet();
	    while(rs.next()){
		ReportFixedAssets objReportFixedAssets = new ReportFixedAssets();
		double dDepValue = 0;
		long lFAId = 0;
		
		lFAId = rs.getLong(PstModulAktiva.fieldNames[PstModulAktiva.FLD_AKTIVA_ID]);
		if(lPeriodId != 0 && lFAId != 0){
		    try{
			dDepValue = getDepreciationValue(lPeriodId, lFAId);
		    }catch(Exception e){}
		}
		resultToObject(rs, isNextPeriod, dDepValue, lPeriodId, objReportFixedAssets);
		try{
		    long lInsert = PstReportFixedAssets.insertExc(objReportFixedAssets);
		    iResult = 1;
		}catch(Exception e){}
	    }
	    rs.close();
	}catch(Exception e){}
	return iResult;
    }
    
    public static synchronized void resultToObject(ResultSet rs, boolean isNextPeriod, double dDepValue, long lPeriodId, ReportFixedAssets objReportFixedAssets){
	try{
	    double dAqcValue = 0.0;
	    double dLastDepValue = 0.0;
	    boolean isCurrentAqc = false;
	    long lFAId = 0;
	    Date aqcDate = new Date();
	    
	    aqcDate = rs.getDate(PstModulAktiva.fieldNames[PstModulAktiva.FLD_TGL_PEROLEHAN]);
	    lFAId = rs.getLong(PstModulAktiva.fieldNames[PstModulAktiva.FLD_AKTIVA_ID]);
	    if(lPeriodId != 0){
		try{
		    isCurrentAqc = isCurrentAquisation(aqcDate, lPeriodId);
		}catch(Exception e){}
	    }
	    dAqcValue = rs.getDouble(PstModulAktiva.fieldNames[PstModulAktiva.FLD_HARGA_PEROLEHAN]);
	    objReportFixedAssets.setCode(rs.getString(PstModulAktiva.fieldNames[PstModulAktiva.FLD_KODE]));
	    objReportFixedAssets.setName(rs.getString(PstModulAktiva.fieldNames[PstModulAktiva.FLD_NAME]));
	    objReportFixedAssets.setLocation(rs.getString(PstAktivaLocation.fieldNames[PstAktivaLocation.FLD_AKTIVA_LOC_NAME]));
	    objReportFixedAssets.setAqcDate(aqcDate);
	    objReportFixedAssets.setLife(rs.getInt(PstModulAktiva.fieldNames[PstModulAktiva.FLD_MASA_MANFAAT]));
	    if(isCurrentAqc){
		objReportFixedAssets.setAqcLastMonth(0);
		objReportFixedAssets.setAqcIncrement(dAqcValue);
	    }else{
		objReportFixedAssets.setAqcLastMonth(dAqcValue);
		objReportFixedAssets.setAqcIncrement(0);
	    }
	    objReportFixedAssets.setAqcDecrement(0);
	    objReportFixedAssets.setAqcThisMonth(dAqcValue);
	    if(isNextPeriod){
		dLastDepValue = getOBDepreciation(lFAId, lPeriodId);
		objReportFixedAssets.setDepLastMonth(dLastDepValue);
	    }else{
		objReportFixedAssets.setDepLastMonth(0);
	    }
	    objReportFixedAssets.setDepIncrement(dDepValue);
	    objReportFixedAssets.setDepDecrement(0);
	    objReportFixedAssets.setDepThisMonth(dDepValue + dLastDepValue);
	    objReportFixedAssets.setBookValue(dAqcValue - (dLastDepValue + dDepValue));
	    objReportFixedAssets.setLocationId(rs.getLong(PstModulAktiva.fieldNames[PstModulAktiva.FLD_AKTIVA_LOC_ID]));
	    objReportFixedAssets.setPeriodId(lPeriodId);
	    objReportFixedAssets.setAccountId(rs.getLong(PstModulAktiva.fieldNames[PstModulAktiva.FLD_ID_PERKIRAAN_AKTIVA]));
	    objReportFixedAssets.setGroupId(rs.getLong(PstModulAktiva.fieldNames[PstModulAktiva.FLD_AKTIVA_GROUP_ID]));
	    objReportFixedAssets.setDepMethodId(rs.getLong(PstModulAktiva.fieldNames[PstModulAktiva.FLD_METODE_PENYUSUTAN_ID]));
	    objReportFixedAssets.setDepTypeId(rs.getLong(PstModulAktiva.fieldNames[PstModulAktiva.FLD_TYPE_PENYUSUTAN_ID]));
	    objReportFixedAssets.setFixedAssetsId(lFAId);
	    objReportFixedAssets.setQty(rs.getInt(PstReceiveAktivaItem.fieldNames[PstReceiveAktivaItem.FLD_QTY]));
	}catch(Exception e){}
    }
    
    public static synchronized String queryTransferFAData(){
	String sResult = "";
	try{
	    sResult = " SELECT M."+PstModulAktiva.fieldNames[PstModulAktiva.FLD_KODE]+
		      ", M."+PstModulAktiva.fieldNames[PstModulAktiva.FLD_NAME]+
		      ", M."+PstModulAktiva.fieldNames[PstModulAktiva.FLD_TGL_PEROLEHAN]+
		      ", M."+PstModulAktiva.fieldNames[PstModulAktiva.FLD_MASA_MANFAAT]+
		      ", M."+PstModulAktiva.fieldNames[PstModulAktiva.FLD_AKTIVA_LOC_ID]+
		      ", M."+PstModulAktiva.fieldNames[PstModulAktiva.FLD_ID_PERKIRAAN_AKTIVA]+
		      ", M."+PstModulAktiva.fieldNames[PstModulAktiva.FLD_AKTIVA_GROUP_ID]+
		      ", M."+PstModulAktiva.fieldNames[PstModulAktiva.FLD_METODE_PENYUSUTAN_ID]+
		      ", M."+PstModulAktiva.fieldNames[PstModulAktiva.FLD_TYPE_PENYUSUTAN_ID]+
		      ", M."+PstModulAktiva.fieldNames[PstModulAktiva.FLD_AKTIVA_ID]+
		      ", M."+PstModulAktiva.fieldNames[PstModulAktiva.FLD_HARGA_PEROLEHAN]+
		      ", R."+PstReceiveAktivaItem.fieldNames[PstReceiveAktivaItem.FLD_QTY]+
		      ", L."+PstAktivaLocation.fieldNames[PstAktivaLocation.FLD_AKTIVA_LOC_NAME]+
		      " FROM "+PstModulAktiva.TBL_AKTIVA+" AS M "+
		      " INNER JOIN "+PstReceiveAktivaItem.TBL_RECEIVE_AKTIVA_ITEM+" AS R "+
		      " ON M."+PstModulAktiva.fieldNames[PstModulAktiva.FLD_AKTIVA_ID]+
		      " = R."+PstReceiveAktivaItem.fieldNames[PstReceiveAktivaItem.FLD_AKTIVA_ID]+
		      " INNER JOIN "+PstAktivaLocation.TBL_AISO_AKTIVA_LOCATION+" AS L "+
		      " ON M."+PstModulAktiva.fieldNames[PstModulAktiva.FLD_AKTIVA_LOC_ID]+
		      " = L."+PstAktivaLocation.fieldNames[PstAktivaLocation.FLD_AKTIVA_LOCATION_ID];
	}catch(Exception e){}
	return sResult;
    }
    
    public static synchronized double getDepreciationValue(long lPeriodId, long lFAId){
	DBResultSet dbrs = null;
	double dResult = 0.0;
	if(lPeriodId != 0 && lFAId != 0){
	    try{
		String sql = " SELECT "+PstPenyusutanAktiva.fieldNames[PstPenyusutanAktiva.FLD_VALUE_PNY]+
			     " FROM "+PstPenyusutanAktiva.TBL_PENYUSUTAN_AKTIVA+
			     " WHERE "+PstPenyusutanAktiva.fieldNames[PstPenyusutanAktiva.FLD_AKTIVA_ID]+" = "+lFAId+
			     " AND "+PstPenyusutanAktiva.fieldNames[PstPenyusutanAktiva.FLD_PERIODE_ID]+" = "+lPeriodId;
		dbrs = DBHandler.execQueryResult(sql);
		ResultSet rs = dbrs.getResultSet();
		while(rs.next()){
		    dResult = rs.getDouble(PstPenyusutanAktiva.fieldNames[PstPenyusutanAktiva.FLD_VALUE_PNY]);
		}
		rs.close();
	    }catch(Exception e){}
	}
	return dResult;
    }
    
    public static synchronized long getStartPeriodId(){
	DBResultSet dbrs = null;
	long lPeriodId = 0;
	try{
	    String sql = " SELECT DISTINCT "+PstPenyusutanAktiva.fieldNames[PstPenyusutanAktiva.FLD_PERIODE_ID]+
			 " FROM "+PstPenyusutanAktiva.TBL_PENYUSUTAN_AKTIVA;
	    dbrs = DBHandler.execQueryResult(sql);
	    ResultSet rs = dbrs.getResultSet();
	    while(rs.next()){
		if(rs.isFirst()){
		    lPeriodId = rs.getLong(PstPenyusutanAktiva.fieldNames[PstPenyusutanAktiva.FLD_PERIODE_ID]);		    
		}
	    }
	    rs.close();
	}catch(Exception e){}
	return lPeriodId;
    }
    
    public static synchronized double getOBDepreciation(long lFAId, long lPeriodId){
	DBResultSet dbrs = null;
	double dResult = 0.0;
	long lLastPeriodId = 0;
	if(lPeriodId != 0){
	    try{
		lLastPeriodId = PstPeriode.getLastPeriodeOid(lPeriodId);
	    }catch(Exception e){}
	}
	
	if(lFAId != 0 && lLastPeriodId != 0){
	    try{
		String sql = " SELECT "+PstReportFixedAssets.fieldNames[PstReportFixedAssets.FLD_DEP_THIS_MONTH]+
			     " FROM "+PstReportFixedAssets.TBL_REPORT_FIXED_ASSETS+
			     " WHERE "+PstReportFixedAssets.fieldNames[PstReportFixedAssets.FLD_PERIOD_ID]+" = "+lLastPeriodId+
			     " AND "+PstReportFixedAssets.fieldNames[PstReportFixedAssets.FLD_FIXED_ASSETS_ID]+" = "+lFAId;
		dbrs = DBHandler.execQueryResult(sql);
		ResultSet rs = dbrs.getResultSet();
		while(rs.next()){
		    dResult = rs.getDouble(PstReportFixedAssets.fieldNames[PstReportFixedAssets.FLD_DEP_THIS_MONTH]);
		}
		rs.close();
	    }catch(Exception e){}
	}
	return dResult;
    }
    
    public static synchronized boolean isCurrentAquisation(Date aqcDate, long lPeriodId){
	boolean bResult = false;
	Periode objPeriod = new Periode();
	Date startPeriodDate = new Date();
	Date endPeriodDate = new Date();
	if(lPeriodId != 0){
	    try{
		objPeriod = PstPeriode.fetchExc(lPeriodId);
		startPeriodDate = objPeriod.getTglAwal();
		endPeriodDate = objPeriod.getTglAkhir();
		if((aqcDate.after(startPeriodDate) || aqcDate.equals(startPeriodDate)) && (aqcDate.before(endPeriodDate) || aqcDate.equals(endPeriodDate))){
		    bResult = true;
		}
	    }catch(Exception e){}
	}
	return bResult;
    }
    
        /**
     * untuk posting penyusutan aktiva
     *
     * @param bookType
     * @param userOID
     * @param periodeOID
     * @param oidReceive
     */
    public void postingPenyusutanAktiva(long bookType, long userOID, long periodeOID, Vector list) 
    {
        try 
        {
            Periode periode = PstPeriode.fetchExc(periodeOID);
            Hashtable hash = new Hashtable();

            /**
             * object untuk jurnal umum
             */
            JurnalUmum jurnalUmum = new JurnalUmum();
            jurnalUmum.setTglTransaksi(periode.getTglAkhir());
            jurnalUmum.setTglEntry(new Date());
            jurnalUmum.setKeterangan("Penyusutan aktiva periode : " + periode.getNama());
            jurnalUmum.setBookType(bookType);
            jurnalUmum.setCurrType(bookType);
            jurnalUmum.setUserId(userOID);
            jurnalUmum.setPeriodeId(periodeOID);
            jurnalUmum.setReferenceDoc("-");
            jurnalUmum.setJurnalType(PstJurnalUmum.TIPE_JURNAL_UMUM);
            //String strVoucher = SessJurnal.generateVoucherNumber(periodeOID, jurnalUmum.getTglTransaksi());
            //jurnalUmum.setSJurnalNumber(strVoucher);
            //jurnalUmum.setVoucherNo(strVoucher.substring(0, 4));
            //jurnalUmum.setVoucherCounter(Integer.parseInt(strVoucher.substring(5)));            
            
            JurnalDetail jurnaldetail = new JurnalDetail();            
            if (list != null && list.size() > 0) 
            {
                long preOidPeriod = SessWorkSheet.getOidPeriodeLalu(periodeOID);
                for (int k = 0; k < list.size(); k++) 
                {
                    ModulAktiva mdlAktiva = (ModulAktiva) list.get(k);

                    Aktiva aktiva = new Aktiva();
                    try
                    {
                        aktiva = PstAktiva.fetchExc(mdlAktiva.getMetodePenyusutanOid());
                    }
                    catch(Exception e)
                    {
                    }
                    
                    double nilaiPenyu = 0.0;
                    Date dateNow = new Date();

                    switch(aktiva.getTypeMetodePenyusutan())
                    {
                        case PstAktiva.METODE_PENYUSUTAN_GARIS_LURUS:
                            nilaiPenyu = ((mdlAktiva.getHargaPerolehan() - mdlAktiva.getNilaiResidu()) / mdlAktiva.getMasaManfaat()) / 12;
                            if(mdlAktiva.getTglPerolehan()!=null)
                            {
                                Date bts = mdlAktiva.getTglPerolehan();
                                bts.setYear(bts.getYear()+mdlAktiva.getMasaManfaat() + 1);
                                if(dateNow.after(bts))
                                {
                                    nilaiPenyu = 0;
                                }
                            }
                            break;
                            
                        case PstAktiva.METODE_PENYUSUTAN_SALDO_MENURUN:
                            String where = PstPenyusutanAktiva.fieldNames[PstPenyusutanAktiva.FLD_PERIODE_ID] + "=" + preOidPeriod+
                                    " AND "+PstPenyusutanAktiva.fieldNames[PstPenyusutanAktiva.FLD_AKTIVA_ID] + "=" + mdlAktiva.getOID();
                            Vector vect = PstPenyusutanAktiva.list(0,0,where,"");
                            PenyusutanAktiva penyusutanAktiva = new PenyusutanAktiva();
                            if(vect!=null && vect.size()>0)
                            {
                                penyusutanAktiva = (PenyusutanAktiva)vect.get(0);
                            }
                            
                            nilaiPenyu = ((mdlAktiva.getHargaPerolehan() - penyusutanAktiva.getValue_pny()) * mdlAktiva.getPersenPenyusutan() / 100) / 12;
                            if(mdlAktiva.getTglPerolehan()!=null)
                            {
                                Date btss = mdlAktiva.getTglPerolehan();
                                btss.setYear(btss.getYear()+mdlAktiva.getMasaManfaat());
                                if(dateNow.after(btss))
                                {
                                    double totalSusut = PstPenyusutanAktiva.getTotalNilaiSusut(mdlAktiva.getOID());
                                    nilaiPenyu = mdlAktiva.getHargaPerolehan() - totalSusut;
                                }
                            }
                            break;
                    }
                                        
                    // total biaya                    
                    jurnaldetail = new JurnalDetail();                    
                    jurnaldetail.setIdPerkiraan(mdlAktiva.getIdPerkiraanByaPenyusutan());
                    jurnaldetail.setDataStatus(PstJurnalDetail.DATASTATUS_ADD);
                    jurnaldetail.setCurrType(bookType);
                    jurnaldetail.setCurrAmount(1);
                    jurnaldetail.setDebet(nilaiPenyu);
                    jurnaldetail.setKredit(0);
                    updateHashFromVector(hash, jurnaldetail);                     

                    // total akumulasi penyusutan                    
                    jurnaldetail = new JurnalDetail();                    
                    jurnaldetail.setIdPerkiraan(mdlAktiva.getIdPerkiraanAkmPenyusutan());
                    jurnaldetail.setDataStatus(PstJurnalDetail.DATASTATUS_ADD);
                    jurnaldetail.setCurrType(bookType);
                    jurnaldetail.setCurrAmount(1);
                    jurnaldetail.setDebet(0);
                    jurnaldetail.setKredit(nilaiPenyu);
                    updateHashFromVector(hash, jurnaldetail);                                      
                }
            }           

            Vector vJDetail = new Vector(hash.values());    
            if (vJDetail != null && vJDetail.size()>0)
            {
                for (int i=0; i<vJDetail.size(); i++)
                {
                    JurnalDetail objJurnalDetail = (JurnalDetail) vJDetail.get(i);
                    objJurnalDetail.setIndex(i);
                    jurnalUmum.addDetails(i, objJurnalDetail);                
                }
            }
            
            System.out.println("UKURAN JUMUM            : " + jurnalUmum.getJurnalDetails().size());
            CtrlJurnalUmum ctrlJurnalUmum = new CtrlJurnalUmum();
            ctrlJurnalUmum.JournalPosted(CtrlJurnalUmum.POSTED_JD, 0, jurnalUmum);
        } catch (Exception e) {
            System.out.println("err postingPenyusutanAktiva : " + e.toString());
        }
    }
 
    
    private static void updateHashFromVector(Hashtable hash, JurnalDetail objJurnalDetail) 
    {
        if (hash != null && objJurnalDetail != null) 
        {
            JurnalDetail objHash = new JurnalDetail();
            if (hash.containsKey("" + objJurnalDetail.getIdPerkiraan())) 
            {
                objHash = (JurnalDetail) hash.get("" + objJurnalDetail.getIdPerkiraan());
                objHash.setDebet(objHash.getDebet() + objJurnalDetail.getDebet());
                objHash.setKredit(objHash.getKredit() + objJurnalDetail.getKredit());
            }
            else 
            {
                hash.put("" + objJurnalDetail.getIdPerkiraan(), objJurnalDetail);
            }
        }
    } 
    
    /*
     *Create report fixed assets By Dwi
     *Each row of report declared as an object
     *Total of column report is sixtheen
     *Column is property of the object.
     *The column of report are : no, code, name, location, own date, quantity, own amount till last period,
     *current increment, current decrement, own amount till today, depreciation till last period,
     *current increment, currenct decrement, depreciation till today, book value
     *algoritm :
     *each object get :
     *- code, name, location, own date from aiso_aktiva table
     *- qty in (In) from aiso_aktiva_receive_item table
     *- qty out (Out) from aiso_aktiva_selling_item table
     *- quantity is calculated : In - Out
     *- own amount till last period(O) from aiso_aktiva_receive table
     *- current increment(I) from aiso_aktiva_receive table especially for this period
     *- current decrement(D) from aiso_aktiva_selling table especially for this period
     *- own amount till today(TD) is calculate : O + I - D
     *- depreciation till last period(OD) from aiso_penyusutan table
     *- current increment (CI) from aiso_penyusutan table especially for this period
     *- current decrement (CD) is calculate : OD + CI if current decrement > 0
     *- depreciation till today(TI) is calculate : OD + CI - CD
     *- book value is calculate : TD - TI  
     **/
    
    public static synchronized Vector reportFixedAssets(long periodeOid, long typeOid, long metodeOid, long aktivaGroupOid, long perkAktivaOid, long lLocationId, int start, int recordToGet){
        DBResultSet dbrs = null;
        Vector vResult = new Vector();
        if(periodeOid != 0){
            try{
                String sql = " SELECT DT."+PstReceiveAktivaItem.fieldNames[PstReceiveAktivaItem.FLD_AKTIVA_ID]+
                            " FROM "+PstReceiveAktivaItem.TBL_RECEIVE_AKTIVA_ITEM+" AS DT "+
                            " INNER JOIN "+PstModulAktiva.TBL_AKTIVA+" AS AK "+
                            " ON DT."+PstReceiveAktivaItem.fieldNames[PstReceiveAktivaItem.FLD_AKTIVA_ID]+
                            " = AK."+PstModulAktiva.fieldNames[PstModulAktiva.FLD_AKTIVA_ID];
                String whClause = "";
                if(typeOid != 0){
                    whClause += " WHERE "+PstModulAktiva.fieldNames[PstModulAktiva.FLD_TYPE_PENYUSUTAN_ID]+
                                " = "+typeOid;
                }
                
                if(metodeOid != 0){
                    if(whClause != null && whClause.length() > 0){
                        whClause += " AND "+PstModulAktiva.fieldNames[PstModulAktiva.FLD_METODE_PENYUSUTAN_ID]+
                                    " = "+metodeOid;
                    }else{
                        whClause += " WHERE "+PstModulAktiva.fieldNames[PstModulAktiva.FLD_METODE_PENYUSUTAN_ID]+
                                    " = "+metodeOid;
                    }
                }
                
                if(aktivaGroupOid != 0){
                    if(whClause != null && whClause.length() > 0){
                        whClause += " AND "+PstModulAktiva.fieldNames[PstModulAktiva.FLD_AKTIVA_GROUP_ID]+
                                    " = "+aktivaGroupOid;
                    }else{
                        whClause += " WHERE "+PstModulAktiva.fieldNames[PstModulAktiva.FLD_AKTIVA_GROUP_ID]+
                                    " = "+aktivaGroupOid;
                    }
                }
                
                if(perkAktivaOid != 0){
                    if(whClause != null && whClause.length() > 0){
                        whClause += " AND "+PstModulAktiva.fieldNames[PstModulAktiva.FLD_ID_PERKIRAAN_AKTIVA]+
                                    " = "+perkAktivaOid;
                    }else{
                        whClause += " WHERE "+PstModulAktiva.fieldNames[PstModulAktiva.FLD_ID_PERKIRAAN_AKTIVA]+
                                    " = "+perkAktivaOid;
                    }
                }
                
                if(lLocationId != 0){
                    if(whClause != null && whClause.length() > 0){
                        whClause += " AND "+PstModulAktiva.fieldNames[PstModulAktiva.FLD_AKTIVA_LOC_ID]+
                                    " = "+lLocationId;
                    }else{
                        whClause += " WHERE "+PstModulAktiva.fieldNames[PstModulAktiva.FLD_AKTIVA_LOC_ID]+
                                    " = "+lLocationId;
                    }
                }
                
                if(whClause != null && whClause.length() > 0){
                    sql += whClause;
                }
                
		System.out.println("SessAktivaSusut.DBSVR_TYPE :::::: "+DBHandler.DBSVR_TYPE);
                switch (DBHandler.DBSVR_TYPE) {
                    case DBHandler.DBSVR_MYSQL:
                        if (start == 0 && recordToGet == 0)
                            sql = sql + "";
                        else
                            sql = sql + " LIMIT " + start + "," + recordToGet;
                        break;

                    case DBHandler.DBSVR_POSTGRESQL:
			System.out.println("SQL SessAktivaSusut.start :: "+sql+" recordToGet ::: "+recordToGet);
                        if (start == 0 && recordToGet == 0){
                            sql = sql + "";
			}else{
                            sql = sql + " LIMIT " + recordToGet + " OFFSET " + start;
			    System.out.println("SQL SessAktivaSusut.reportFixedAssets() 1 :::: "+sql);
			}    
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
                    
		System.out.println("SQL SessAktivaSusut.reportFixedAssets() :::: "+sql);
                dbrs = DBHandler.execQueryResult(sql);
                ResultSet rs = dbrs.getResultSet();
                while(rs.next()){
                    AktivaSusut objAktivaSusut = new AktivaSusut();
                    long lAktivaId = 0;
                    double dLastOwnAmount = 0;
                    double dIncreaseOwnAmount = 0;
                    double dDecreaseOwnAmount = 0;
                    double dTotOwnAmount = 0;
                    double dLastDepAmount = 0;
                    double dIncreaseDepAmount = 0;
                    double dDecreaseDepAmount = 0;
                    double dTotDepAmount = 0;
                    double dBookValue = 0;
                    int qtyJual = 0;
                    int qtyBeli = 0;
                    
                    lAktivaId = rs.getLong(PstModulAktiva.fieldNames[PstModulAktiva.FLD_AKTIVA_ID]);
                    dLastOwnAmount = getLastOwnAmount(lAktivaId,periodeOid);
                    dIncreaseOwnAmount = getIncreaseOwnAmount(lAktivaId, periodeOid);
                    dDecreaseOwnAmount = getDecreaseOwnAmount(lAktivaId, periodeOid);
                    dTotOwnAmount = dLastOwnAmount + dIncreaseOwnAmount - dDecreaseOwnAmount;
                    dLastDepAmount = getLastDepAmount(lAktivaId, periodeOid);
                    dIncreaseDepAmount = getIncreaseDepAmount(lAktivaId, periodeOid);
                    qtyJual = SessSellingAktiva.getQtySellingByIdAktiva(lAktivaId);
                    qtyBeli = SessReceiveAktiva.getQtyReceiveByIdAktiva(lAktivaId);
                    
                    if(dDecreaseOwnAmount > 0){
                        dDecreaseDepAmount = dLastDepAmount + dIncreaseDepAmount;
                    }
                    dTotDepAmount = dLastDepAmount + dIncreaseDepAmount - dDecreaseDepAmount;
                    dBookValue = dTotOwnAmount - dTotDepAmount;
                    objAktivaSusut = getMaster(lAktivaId);
                    objAktivaSusut.setQuantity(qtyBeli - qtyJual);
                    objAktivaSusut.setMutasiProlehBulanLalu(dLastOwnAmount);
                    objAktivaSusut.setMutasiTambah(dIncreaseOwnAmount);
                    objAktivaSusut.setMustasiKurang(dDecreaseOwnAmount);
                    objAktivaSusut.setTotalBulanIni(dTotOwnAmount);
                    objAktivaSusut.setSusutBulanlalu(dLastDepAmount);
                    objAktivaSusut.setSusutTambah(dIncreaseDepAmount);
                    objAktivaSusut.setSusutKurang(dDecreaseDepAmount);
                    objAktivaSusut.setTotalSusutBulanIni(dTotDepAmount);
                    objAktivaSusut.setNilaiBuku(dBookValue);
                    
                    vResult.add(objAktivaSusut);
                }
		rs.close();
		System.out.println("SessAktivaSusut.vResult.size() :::::::::: "+vResult.size());
            }catch(Exception e){
	    }finally{
		//DBResultSet.close(dbrs);
	    }
        }    
        return vResult;
    }
    
    public static synchronized int getCountFAReport(long periodeOid, long typeOid, long metodeOid, long aktivaGroupOid, long perkAktivaOid, long lLocationId){
	int iResult = 0;
	try{
	    String whClause = "";
                if(typeOid != 0){
                    whClause += " WHERE "+PstModulAktiva.fieldNames[PstModulAktiva.FLD_TYPE_PENYUSUTAN_ID]+
                                " = "+typeOid;
                }
                
                if(metodeOid != 0){
                    if(whClause != null && whClause.length() > 0){
                        whClause += " AND "+PstModulAktiva.fieldNames[PstModulAktiva.FLD_METODE_PENYUSUTAN_ID]+
                                    " = "+metodeOid;
                    }else{
                        whClause += " WHERE "+PstModulAktiva.fieldNames[PstModulAktiva.FLD_METODE_PENYUSUTAN_ID]+
                                    " = "+metodeOid;
                    }
                }
                
                if(aktivaGroupOid != 0){
                    if(whClause != null && whClause.length() > 0){
                        whClause += " AND "+PstModulAktiva.fieldNames[PstModulAktiva.FLD_AKTIVA_GROUP_ID]+
                                    " = "+aktivaGroupOid;
                    }else{
                        whClause += " WHERE "+PstModulAktiva.fieldNames[PstModulAktiva.FLD_AKTIVA_GROUP_ID]+
                                    " = "+aktivaGroupOid;
                    }
                }
                
                if(perkAktivaOid != 0){
                    if(whClause != null && whClause.length() > 0){
                        whClause += " AND "+PstModulAktiva.fieldNames[PstModulAktiva.FLD_ID_PERKIRAAN_AKTIVA]+
                                    " = "+perkAktivaOid;
                    }else{
                        whClause += " WHERE "+PstModulAktiva.fieldNames[PstModulAktiva.FLD_ID_PERKIRAAN_AKTIVA]+
                                    " = "+perkAktivaOid;
                    }
                }
                
                if(lLocationId != 0){
                    if(whClause != null && whClause.length() > 0){
                        whClause += " AND "+PstModulAktiva.fieldNames[PstModulAktiva.FLD_AKTIVA_LOC_ID]+
                                    " = "+lLocationId;
                    }else{
                        whClause += " WHERE "+PstModulAktiva.fieldNames[PstModulAktiva.FLD_AKTIVA_LOC_ID]+
                                    " = "+lLocationId;
                    }
                }
	    
	    iResult = PstReceiveAktivaItem.getCount(whClause);
	}catch(Exception e){}
	return iResult;
    }
    
    public static synchronized Vector getGrandTotalReport(long periodeOid, long typeOid, long metodeOid, long aktivaGroupOid, long perkAktivaOid, long lLocationId, int start, int recordToGet){
        DBResultSet dbrs = null;
        Vector vResult = new Vector();
        if(periodeOid != 0){
            try{
                String sql = " SELECT DT."+PstReceiveAktivaItem.fieldNames[PstReceiveAktivaItem.FLD_AKTIVA_ID]+
                            " FROM "+PstReceiveAktivaItem.TBL_RECEIVE_AKTIVA_ITEM+" AS DT "+
                            " INNER JOIN "+PstModulAktiva.TBL_AKTIVA+" AS AK "+
                            " ON DT."+PstReceiveAktivaItem.fieldNames[PstReceiveAktivaItem.FLD_AKTIVA_ID]+
                            " = AK."+PstModulAktiva.fieldNames[PstModulAktiva.FLD_AKTIVA_ID];
                String whClause = "";
                if(typeOid != 0){
                    whClause += " WHERE "+PstModulAktiva.fieldNames[PstModulAktiva.FLD_TYPE_PENYUSUTAN_ID]+
                                " = "+typeOid;
                }
                
                if(metodeOid != 0){
                    if(whClause != null && whClause.length() > 0){
                        whClause += " AND "+PstModulAktiva.fieldNames[PstModulAktiva.FLD_METODE_PENYUSUTAN_ID]+
                                    " = "+metodeOid;
                    }else{
                        whClause += " WHERE "+PstModulAktiva.fieldNames[PstModulAktiva.FLD_METODE_PENYUSUTAN_ID]+
                                    " = "+metodeOid;
                    }
                }
                
                if(aktivaGroupOid != 0){
                    if(whClause != null && whClause.length() > 0){
                        whClause += " AND "+PstModulAktiva.fieldNames[PstModulAktiva.FLD_AKTIVA_GROUP_ID]+
                                    " = "+aktivaGroupOid;
                    }else{
                        whClause += " WHERE "+PstModulAktiva.fieldNames[PstModulAktiva.FLD_AKTIVA_GROUP_ID]+
                                    " = "+aktivaGroupOid;
                    }
                }
                
                if(perkAktivaOid != 0){
                    if(whClause != null && whClause.length() > 0){
                        whClause += " AND "+PstModulAktiva.fieldNames[PstModulAktiva.FLD_ID_PERKIRAAN_AKTIVA]+
                                    " = "+perkAktivaOid;
                    }else{
                        whClause += " WHERE "+PstModulAktiva.fieldNames[PstModulAktiva.FLD_ID_PERKIRAAN_AKTIVA]+
                                    " = "+perkAktivaOid;
                    }
                }
                
                if(lLocationId != 0){
                    if(whClause != null && whClause.length() > 0){
                        whClause += " AND "+PstModulAktiva.fieldNames[PstModulAktiva.FLD_AKTIVA_LOC_ID]+
                                    " = "+lLocationId;
                    }else{
                        whClause += " WHERE "+PstModulAktiva.fieldNames[PstModulAktiva.FLD_AKTIVA_LOC_ID]+
                                    " = "+lLocationId;
                    }
                }
                
                if(whClause != null && whClause.length() > 0){
                    sql += whClause;
                }
                
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
                AktivaSusut objAktivaSusut = new AktivaSusut();
                double dTotLastOwnAmount = 0;
                double dTotIncreaseOwnAmount = 0;
                double dTotDecreaseOwnAmount = 0;
                double dGrandTotOwnAmount = 0;
                double dTotLastDepAmount = 0;
                double dTotIncreaseDepAmount = 0;
                double dTotDecreaseDepAmount = 0;
                double dGrandTotDepAmount = 0;
                double dTotBookValue = 0;
                int iTotQty = 0;
                while(rs.next()){                    
                    long lAktivaId = 0;
                    double dLastOwnAmount = 0;
                    double dIncreaseOwnAmount = 0;
                    double dDecreaseOwnAmount = 0;
                    double dTotOwnAmount = 0;
                    double dLastDepAmount = 0;
                    double dIncreaseDepAmount = 0;
                    double dDecreaseDepAmount = 0;
                    double dTotDepAmount = 0;
                    double dBookValue = 0;
                    int iQtyBeli = 0;
                    int iQtyJual = 0;
                    int iQty = 0;
                    lAktivaId = rs.getLong(PstModulAktiva.fieldNames[PstModulAktiva.FLD_AKTIVA_ID]);
                    iQtyBeli = SessReceiveAktiva.getQtyReceiveByIdAktiva(lAktivaId);
                    iQtyJual = SessSellingAktiva.getQtySellingByIdAktiva(lAktivaId);
                    iQty = iQtyBeli - iQtyJual;
                    dLastOwnAmount = getLastOwnAmount(lAktivaId,periodeOid);
                    dIncreaseOwnAmount = getIncreaseOwnAmount(lAktivaId, periodeOid);
                    dDecreaseOwnAmount = getDecreaseOwnAmount(lAktivaId, periodeOid);
                    dTotOwnAmount = dLastOwnAmount + dIncreaseOwnAmount - dDecreaseOwnAmount;
                    dLastDepAmount = getLastDepAmount(lAktivaId, periodeOid);
                    dIncreaseDepAmount = getIncreaseDepAmount(lAktivaId, periodeOid);
                    if(dDecreaseOwnAmount > 0){
                        dDecreaseDepAmount = dLastDepAmount + dIncreaseDepAmount;
                    }
                    dTotDepAmount = dLastDepAmount + dIncreaseDepAmount - dDecreaseDepAmount;
                    dBookValue = dTotOwnAmount - dTotDepAmount;
                    
                     
                     dTotLastOwnAmount += dLastOwnAmount;
                     dTotIncreaseOwnAmount += dIncreaseOwnAmount;
                     dTotDecreaseOwnAmount += dDecreaseOwnAmount;
                     dGrandTotOwnAmount += dTotOwnAmount;
                     dTotLastDepAmount += dLastDepAmount;
                     dTotIncreaseDepAmount += dIncreaseDepAmount;
                     dTotDecreaseDepAmount += dDecreaseDepAmount;
                     dGrandTotDepAmount += dTotDepAmount;
                     dTotBookValue += dBookValue;
                     iTotQty += iQty;
                }
               
                objAktivaSusut.setMutasiProlehBulanLalu(dTotLastOwnAmount);
                objAktivaSusut.setMutasiTambah(dTotIncreaseOwnAmount);
                objAktivaSusut.setMustasiKurang(dTotDecreaseOwnAmount);
                objAktivaSusut.setTotalBulanIni(dGrandTotOwnAmount);
                objAktivaSusut.setSusutBulanlalu(dTotLastDepAmount);
                objAktivaSusut.setSusutTambah(dTotIncreaseDepAmount);
                objAktivaSusut.setSusutKurang(dTotDecreaseDepAmount);
                objAktivaSusut.setTotalSusutBulanIni(dGrandTotDepAmount);
                objAktivaSusut.setNilaiBuku(dTotBookValue);
                objAktivaSusut.setQuantity(iTotQty);

                vResult.add(objAktivaSusut);
            }catch(Exception e){
            
            }finally{
                DBResultSet.close(dbrs);
            }
        }    
        return vResult;
    }
    
    public static synchronized AktivaSusut getMaster(long lIdAktiva){
        DBResultSet dbrs = null;
        AktivaSusut objAktivaSusut = new AktivaSusut();
        if(lIdAktiva != 0){
            try{
                String sql = "SELECT "+PstModulAktiva.fieldNames[PstModulAktiva.FLD_KODE]+
                            ", "+PstModulAktiva.fieldNames[PstModulAktiva.FLD_NAME]+
                            ", "+PstModulAktiva.fieldNames[PstModulAktiva.FLD_AKTIVA_LOC_ID]+
                            ", "+PstModulAktiva.fieldNames[PstModulAktiva.FLD_TGL_PEROLEHAN]+
                            ", "+PstModulAktiva.fieldNames[PstModulAktiva.FLD_MASA_MANFAAT]+
                            " FROM "+PstModulAktiva.TBL_AKTIVA+
                            " WHERE "+PstModulAktiva.fieldNames[PstModulAktiva.FLD_AKTIVA_ID]+
                            " = "+lIdAktiva;
		//System.out.println("SQL SessAktivaSusut.getMaster :::::::: "+sql);
                dbrs = DBHandler.execQueryResult(sql);
                ResultSet rs = dbrs.getResultSet();
                while(rs.next()){
                    objAktivaSusut.setKode(rs.getString(PstModulAktiva.fieldNames[PstModulAktiva.FLD_KODE]));
                    objAktivaSusut.setNamaaktiva(rs.getString(PstModulAktiva.fieldNames[PstModulAktiva.FLD_NAME]));
                    objAktivaSusut.setLocationId(rs.getLong(PstModulAktiva.fieldNames[PstModulAktiva.FLD_AKTIVA_LOC_ID]));
                    objAktivaSusut.setTglPerolehan(rs.getDate(PstModulAktiva.fieldNames[PstModulAktiva.FLD_TGL_PEROLEHAN]));
                    objAktivaSusut.setMasaManfaat(rs.getInt(PstModulAktiva.fieldNames[PstModulAktiva.FLD_MASA_MANFAAT]));
                }
		rs.close();
            }catch(Exception e){}finally{//DBResultSet.close(dbrs);
	    }
        }
        return objAktivaSusut;
    }
    
    public static synchronized double getLastOwnAmount(long lAktivaId, long lPerodeId){
        DBResultSet dbrs = null;
        double lastOwnAmount = 0.0;
        Periode objPeriode = new Periode();
        if(lPerodeId != 0){
            try{
                objPeriode = PstPeriode.fetchExc(lPerodeId);
            }catch(Exception e){}
        }
        
        if(lAktivaId != 0 && lPerodeId != 0){
            try{
                String sql = "SELECT DT."+PstReceiveAktivaItem.fieldNames[PstReceiveAktivaItem.FLD_TOTAL_PRICE]+
                            " FROM "+PstReceiveAktivaItem.TBL_RECEIVE_AKTIVA_ITEM+" AS DT "+
                            " INNER JOIN "+PstReceiveAktiva.TBL_RECEIVE_AKTIVA+" AS MN "+
                            " ON DT."+PstReceiveAktivaItem.fieldNames[PstReceiveAktivaItem.FLD_RECEIVE_AKTIVA_ID]+
                            " = MN."+PstReceiveAktiva.fieldNames[PstReceiveAktiva.FLD_RECEIVE_AKTIVA_ID]+
                            " WHERE DT."+PstReceiveAktivaItem.fieldNames[PstReceiveAktivaItem.FLD_AKTIVA_ID]+
                            " = "+lAktivaId+" AND MN."+PstReceiveAktiva.fieldNames[PstReceiveAktiva.FLD_TANGGAL_RECEIVE]+
                            " < '"+Formater.formatDate(objPeriode.getTglAwal(),"yyyy-MM-dd")+"'";
		//System.out.println("SQL SessAktivaSusut.getLastOwnAmount :::::::: "+sql);
                dbrs = DBHandler.execQueryResult(sql);
                ResultSet rs = dbrs.getResultSet();
                while(rs.next()){
                    lastOwnAmount = rs.getDouble(1);
                }
		rs.close();
            }catch(Exception e){}finally{//DBResultSet.close(dbrs);
	    }
        }
        return lastOwnAmount;
    }
    
        public static synchronized double getIncreaseOwnAmount(long lAktivaId, long lPerodeId){
        DBResultSet dbrs = null;
        double increaseOwnAmount = 0.0;
        Periode objPeriode = new Periode();
        if(lPerodeId != 0){
            try{
                objPeriode = PstPeriode.fetchExc(lPerodeId);
            }catch(Exception e){}
        }
        
        if(lAktivaId != 0 && lPerodeId != 0){
            try{
                String sql = "SELECT DT."+PstReceiveAktivaItem.fieldNames[PstReceiveAktivaItem.FLD_TOTAL_PRICE]+
                            " FROM "+PstReceiveAktivaItem.TBL_RECEIVE_AKTIVA_ITEM+" AS DT "+
                            " INNER JOIN "+PstReceiveAktiva.TBL_RECEIVE_AKTIVA+" AS MN "+
                            " ON DT."+PstReceiveAktivaItem.fieldNames[PstReceiveAktivaItem.FLD_RECEIVE_AKTIVA_ID]+
                            " = MN."+PstReceiveAktiva.fieldNames[PstReceiveAktiva.FLD_RECEIVE_AKTIVA_ID]+
                            " WHERE DT."+PstReceiveAktivaItem.fieldNames[PstReceiveAktivaItem.FLD_AKTIVA_ID]+
                            " = "+lAktivaId+" AND MN."+PstReceiveAktiva.fieldNames[PstReceiveAktiva.FLD_TANGGAL_RECEIVE]+
                            " BETWEEN '"+Formater.formatDate(objPeriode.getTglAwal(),"yyyy-MM-dd")+"' "+
                            " AND '"+Formater.formatDate(objPeriode.getTglAkhir(),"yyyy-MM-dd")+"' ";
                //System.out.println("SQL SessAktivaSusut.getIncreaseOwnAmount :::::::: "+sql);
		dbrs = DBHandler.execQueryResult(sql);
                ResultSet rs = dbrs.getResultSet();
                while(rs.next()){
                    increaseOwnAmount = rs.getDouble(1);
                }
		rs.close();
            }catch(Exception e){}finally{//DBResultSet.close(dbrs);
	    }
        }
        return increaseOwnAmount;
    }
    
   
        public static synchronized double getDecreaseOwnAmount(long lAktivaId, long lPerodeId){
        DBResultSet dbrs = null;
        double decreaseOwnAmount = 0.0;
        Periode objPeriode = new Periode();
        if(lPerodeId != 0){
            try{
                objPeriode = PstPeriode.fetchExc(lPerodeId);
            }catch(Exception e){}
        }
        
        if(lAktivaId != 0 && lPerodeId != 0){
            try{
                String sql = "SELECT DT."+PstSellingAktivaItem.fieldNames[PstSellingAktivaItem.FLD_TOTAL_PRICE]+
                            " FROM "+PstSellingAktivaItem.TBL_SELLING_AKTIVA_ITEM+" AS DT "+
                            " INNER JOIN "+PstSellingAktiva.TBL_SELLING_AKTIVA+" AS MN "+
                            " ON DT."+PstSellingAktivaItem.fieldNames[PstSellingAktivaItem.FLD_SELLING_AKTIVA_ID]+
                            " = MN."+PstSellingAktiva.fieldNames[PstSellingAktiva.FLD_SELLING_AKTIVA_ID]+
                            " WHERE DT."+PstSellingAktivaItem.fieldNames[PstSellingAktivaItem.FLD_AKTIVA_ID]+
                            " = "+lAktivaId+" AND MN."+PstSellingAktiva.fieldNames[PstSellingAktiva.FLD_TANGGAL_SELLING]+
                            " BETWEEN '"+Formater.formatDate(objPeriode.getTglAwal(),"yyyy-MM-dd")+"' "+
                            " AND '"+Formater.formatDate(objPeriode.getTglAkhir(),"yyyy-MM-dd")+"' ";
                //System.out.println("SQL SessAktivaSusut.getDecreaseOwnAmount :::::::: "+sql);
		dbrs = DBHandler.execQueryResult(sql);
                ResultSet rs = dbrs.getResultSet();
                while(rs.next()){
                    decreaseOwnAmount = rs.getDouble(1);
                }
		rs.close();
            }catch(Exception e){
            }finally{
                //DBResultSet.close(dbrs);
            }
        }
        return decreaseOwnAmount;
    }
    
 public static synchronized double getLastDepAmount(long lAktivaId, long lPerodeId){
    DBResultSet dbrs = null;
    double lastDepAmount = 0.0;
   
    if(lAktivaId != 0 && lPerodeId != 0){
        try{
            String sql = "SELECT SUM("+PstPenyusutanAktiva.fieldNames[PstPenyusutanAktiva.FLD_VALUE_PNY]+") AS TOT_SUSUT "+
                        " FROM "+PstPenyusutanAktiva.TBL_PENYUSUTAN_AKTIVA+
                        " WHERE "+PstPenyusutanAktiva.fieldNames[PstPenyusutanAktiva.FLD_PERIODE_ID]+
                        " != "+lPerodeId+
                        " AND "+PstPenyusutanAktiva.fieldNames[PstPenyusutanAktiva.FLD_AKTIVA_ID]+" = "+lAktivaId;
            //System.out.println("SQL SessAktivaSusut.getLastDepAmount :::::::: "+sql);
	    dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while(rs.next()){
                lastDepAmount = rs.getDouble("TOT_SUSUT");
            }
	    rs.close();
        }catch(Exception e){}finally{//DBResultSet.close(dbrs);
	}
    }
        return lastDepAmount;
}
 
  public static synchronized double getIncreaseDepAmount(long lAktivaId, long lPerodeId){
    DBResultSet dbrs = null;
    double incDepAmount = 0.0;
   
    if(lAktivaId != 0 && lPerodeId != 0){
        try{
            String sql = "SELECT SUM("+PstPenyusutanAktiva.fieldNames[PstPenyusutanAktiva.FLD_VALUE_PNY]+") AS TOT_SUSUT "+
                        " FROM "+PstPenyusutanAktiva.TBL_PENYUSUTAN_AKTIVA+
                        " WHERE "+PstPenyusutanAktiva.fieldNames[PstPenyusutanAktiva.FLD_PERIODE_ID]+
                        " = "+lPerodeId+
                        " AND "+PstPenyusutanAktiva.fieldNames[PstPenyusutanAktiva.FLD_AKTIVA_ID]+" = "+lAktivaId;
            //System.out.println("SQL SessAktivaSusut.getIncreaseDepAmount :::::::: "+sql);
	    dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while(rs.next()){
                incDepAmount = rs.getDouble("TOT_SUSUT");
            }
	    rs.close();
        }catch(Exception e){
        }finally{
            //DBResultSet.close(dbrs);
        }
    }
        return incDepAmount;
}
  
    public static void main(String[] args){
        String strPeriod = "504404311048387164";
        long oidPeriod = Long.parseLong(strPeriod);       
       
        long preOidPeriod = SessWorkSheet.getOidPeriodeLalu(oidPeriod);
        System.out.println("oidPeriod = "+oidPeriod);
        System.out.println("preOidPeriod = "+preOidPeriod);
    }
    
}
