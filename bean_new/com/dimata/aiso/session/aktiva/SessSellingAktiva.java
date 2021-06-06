/**
 * Created by IntelliJ IDEA.
 * User: gadnyana
 * Date: Sep 3, 2005
 * Time: 9:50:44 AM
 * To change this template use Options | File Templates.
 */
package com.dimata.aiso.session.aktiva;

import com.dimata.aiso.entity.aktiva.*;
import com.dimata.aiso.entity.search.SrcSellingAktiva;
import com.dimata.aiso.entity.jurnal.JurnalUmum;
import com.dimata.aiso.entity.jurnal.JurnalDetail;
import com.dimata.aiso.entity.jurnal.PstJurnalDetail;
import com.dimata.aiso.entity.masterdata.ModulAktiva;
import com.dimata.aiso.entity.masterdata.PstModulAktiva;
import com.dimata.aiso.db.DBResultSet;
import com.dimata.aiso.db.DBHandler;
import com.dimata.aiso.entity.jurnal.PstJurnalUmum;
import com.dimata.aiso.entity.masterdata.Aktiva;
import com.dimata.aiso.entity.masterdata.PstAktiva;
import com.dimata.aiso.entity.periode.Periode;
import com.dimata.aiso.entity.periode.PstPeriode;
import com.dimata.aiso.session.jurnal.SessJurnal;
import com.dimata.aiso.form.jurnal.CtrlJurnalUmum;
import com.dimata.aiso.session.report.SessWorkSheet;
import com.dimata.common.entity.contact.PstContactList;
import com.dimata.common.entity.contact.ContactList;
import com.dimata.util.Formater;
import com.dimata.interfaces.journal.I_JournalType;

import java.util.Vector;
import java.util.Date;
import java.sql.ResultSet;
import java.util.Hashtable;

public class SessSellingAktiva {
    public static final String SESS_SEARCH_SELLING_AKTIVA = "SESS_SEARCH_SELLING_AKTIVA";

    /**
     * gadnyana
     * untuk menampilkan list aktiva
     *
     * @param srcModulAktiva
     * @param start
     * @param recordToGet
     * @return
     */
    public static synchronized Vector listSellingAktiva(SrcSellingAktiva srcSellingAktiva, int start, int recordToGet) {
        DBResultSet dbrs = null;
        Vector list = new Vector();
        try {
            String sql = "SELECT " +
                    " A." + PstSellingAktiva.fieldNames[PstSellingAktiva.FLD_SELLING_AKTIVA_ID] +
                    ", A." + PstSellingAktiva.fieldNames[PstSellingAktiva.FLD_NOMOR_SELLING] +
                    ", A." + PstSellingAktiva.fieldNames[PstSellingAktiva.FLD_TANGGAL_SELLING] +
                    ", A." + PstSellingAktiva.fieldNames[PstSellingAktiva.FLD_SELLING_STATUS] +
                    ", CL." + PstContactList.fieldNames[PstContactList.FLD_COMP_NAME] +
                    ", CL." + PstContactList.fieldNames[PstContactList.FLD_PERSON_NAME] +
                    " FROM " + PstSellingAktiva.TBL_SELLING_AKTIVA + " AS A " +
                    " INNER JOIN " + PstContactList.TBL_CONTACT_LIST + " AS CL " +
                    " ON CL." + PstContactList.fieldNames[PstContactList.FLD_CONTACT_ID] + " = A." + PstSellingAktiva.fieldNames[PstSellingAktiva.FLD_KONSUMEN_ID];

            String where = "";

            if (srcSellingAktiva.getNomorSelling().length() > 0) {
                where = " A." + PstSellingAktiva.fieldNames[PstSellingAktiva.FLD_NOMOR_SELLING] + " LIKE '%" + srcSellingAktiva.getNomorSelling() + "%'";
            }

            if (srcSellingAktiva.getNameKonsumen().length() > 0) {
                if (where.length() > 0) {
                    where = where + " AND CL." + PstContactList.fieldNames[PstContactList.FLD_COMP_NAME] + " LIKE '%" + srcSellingAktiva.getNameKonsumen() + "%'";
                } else {
                    where = " CL." + PstContactList.fieldNames[PstContactList.FLD_COMP_NAME] + " LIKE '%" + srcSellingAktiva.getNameKonsumen() + "%'";
                }
            }

            if (srcSellingAktiva.getType() != 0) {
                if (where.length() > 0) {
                    where = where + " AND A." + PstSellingAktiva.fieldNames[PstSellingAktiva.FLD_TANGGAL_SELLING] + " BETWEEN '" + Formater.formatDate(srcSellingAktiva.getTanggalAwal(), "yyyy-MM-dd") + "' AND '" + Formater.formatDate(srcSellingAktiva.getTanggalakhir(), "yyyy-MM-dd") + "'";
                } else {
                    where = " A." + PstSellingAktiva.fieldNames[PstSellingAktiva.FLD_TANGGAL_SELLING] + " BETWEEN '" + Formater.formatDate(srcSellingAktiva.getTanggalAwal(), "yyyy-MM-dd") + "' AND '" + Formater.formatDate(srcSellingAktiva.getTanggalakhir(), "yyyy-MM-dd") + "'";
                }
            }
            if (where.length() > 0)
                sql = sql + " WHERE " + where;

            sql = sql + " ORDER BY A." + PstSellingAktiva.fieldNames[PstSellingAktiva.FLD_NOMOR_SELLING];

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
                SellingAktiva orderAktiva = new SellingAktiva();
                orderAktiva.setOID(rs.getLong(PstSellingAktiva.fieldNames[PstSellingAktiva.FLD_SELLING_AKTIVA_ID]));
                orderAktiva.setNomorSelling(rs.getString(PstSellingAktiva.fieldNames[PstSellingAktiva.FLD_NOMOR_SELLING]));
                orderAktiva.setTanggalSelling(rs.getDate(PstSellingAktiva.fieldNames[PstSellingAktiva.FLD_TANGGAL_SELLING]));
                orderAktiva.setSellingStatus(rs.getInt(PstSellingAktiva.fieldNames[PstSellingAktiva.FLD_SELLING_STATUS]));

                ContactList contactList = new ContactList();
                System.out.println("INI RS GET COMPANY NAME :::::: "+ rs.getString(PstContactList.fieldNames[PstContactList.FLD_COMP_NAME]).length() );
                if(rs.getString(PstContactList.fieldNames[PstContactList.FLD_COMP_NAME]).length() > 0){
                    contactList.setCompName(rs.getString(PstContactList.fieldNames[PstContactList.FLD_COMP_NAME]));                               
                }else{
                    System.out.println("MASUK KE ELSE :::::::");
                    contactList.setCompName(rs.getString(PstContactList.fieldNames[PstContactList.FLD_PERSON_NAME]));
                }
                
                System.out.println("INI KOMPANY NAME DARI BEAN ::::: "+contactList.getCompName());

                vt.add(orderAktiva);
                vt.add(contactList);
                list.add(vt);
            }          
            
        } catch (Exception e) {
            System.out.println("err.SessModulAktiva - listModulAktiva " + e.toString());
        } finally {
            DBResultSet.close(dbrs);
        }
        return list;
    }

    /**
     * gadnyana
     * untuk mencari jumlah row di daftar aktiva
     *
     * @param srcSellingAktiva
     * @return
     */
    public static synchronized int countModulAktiva(SrcSellingAktiva srcSellingAktiva) {
        DBResultSet dbrs = null;
        int count = 0;
        try {
            String sql = "SELECT COUNT(" +
                    " A." + PstSellingAktiva.fieldNames[PstSellingAktiva.FLD_SELLING_AKTIVA_ID] + ") AS CNT " +
                    " FROM " + PstSellingAktiva.TBL_SELLING_AKTIVA + " AS A " +
                    " INNER JOIN " + PstContactList.TBL_CONTACT_LIST + " AS CL " +
                    " ON CL." + PstContactList.fieldNames[PstContactList.FLD_CONTACT_ID] + " = A." + PstSellingAktiva.fieldNames[PstSellingAktiva.FLD_KONSUMEN_ID];

            String where = "";
            if (srcSellingAktiva.getNomorSelling().length() > 0) {
                where = " A." + PstSellingAktiva.fieldNames[PstSellingAktiva.FLD_NOMOR_SELLING] + " LIKE '%" + srcSellingAktiva.getNomorSelling() + "%'";
            }

            if (srcSellingAktiva.getNameKonsumen().length() > 0) {
                if (where.length() > 0) {
                    where = where + " AND CL." + PstContactList.fieldNames[PstContactList.FLD_COMP_NAME] + " LIKE '%" + srcSellingAktiva.getNameKonsumen() + "%'";
                } else {
                    where = " CL." + PstContactList.fieldNames[PstContactList.FLD_COMP_NAME] + " LIKE '%" + srcSellingAktiva.getNameKonsumen() + "%'";
                }
            }

            if (srcSellingAktiva.getType() != 0) {
                if (where.length() > 0) {
                    where = where + " AND A." + PstSellingAktiva.fieldNames[PstSellingAktiva.FLD_TANGGAL_SELLING] + " BETWEEN '" + Formater.formatDate(srcSellingAktiva.getTanggalAwal(), "yyyy-MM-dd") + "' AND '" + Formater.formatDate(srcSellingAktiva.getTanggalakhir(), "yyyy-MM-dd") + "'";
                } else {
                    where = " A." + PstSellingAktiva.fieldNames[PstSellingAktiva.FLD_TANGGAL_SELLING] + " BETWEEN '" + Formater.formatDate(srcSellingAktiva.getTanggalAwal(), "yyyy-MM-dd") + "' AND '" + Formater.formatDate(srcSellingAktiva.getTanggalakhir(), "yyyy-MM-dd") + "'";
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


    /**
     * untuk posting penjualan aktiva
     *
     * @param bookType
     * @param userOID
     * @param periodeOID
     * @param oidReceive
     */
    public synchronized void postingSellingAktiva(long bookType, long userOID, long periodeOID, long oidSelling) 
    {
        try 
        {
            SellingAktiva receiveAktiva = new SellingAktiva();
            receiveAktiva = PstSellingAktiva.fetchExc(oidSelling);

            Hashtable hash = new Hashtable();

            /**
             * object untuk jurnal umum
             */
            JurnalUmum jurnalUmum = new JurnalUmum();
            jurnalUmum.setTglTransaksi(receiveAktiva.getTanggalSelling());
            jurnalUmum.setTglEntry(new Date());
            jurnalUmum.setKeterangan("Penjualan aktiva dgn nomor nota : " + receiveAktiva.getNomorSelling());
            jurnalUmum.setBookType(bookType);
            jurnalUmum.setCurrType(receiveAktiva.getIdCurrency());
            jurnalUmum.setUserId(userOID);
            jurnalUmum.setPeriodeId(periodeOID);
            jurnalUmum.setReferenceDoc(receiveAktiva.getNomorSelling());
            jurnalUmum.setJurnalType(I_JournalType.TIPE_JURNAL_UMUM);
           //String strVoucher = SessJurnal.generateVoucherNumber(periodeOID, jurnalUmum.getTglTransaksi());
            //jurnalUmum.setSJurnalNumber(strVoucher);
            //jurnalUmum.setVoucherNo(strVoucher.substring(0, 4));
            //jurnalUmum.setVoucherCounter(Integer.parseInt(strVoucher.substring(5)));
            jurnalUmum.setContactOid(receiveAktiva.getSupplierId());
            System.out.println("-- proses posting penjualan 1");
            // item lawan
            JurnalDetail jurnaldetail = new JurnalDetail();
            Vector list = PstSellingAktivaItem.getListItem(oidSelling);
            
            double total = 0.0;            
            double aktiva = 0.0;
            double totaktiva = 0.0;
            double akumulasiPenyusutan = 0.0;
            double totakumulasiPenyusutan = 0.0;
            if (list != null && list.size() > 0) 
            {
                for (int k = 0; k < list.size(); k++) 
                {
                    Vector vect = (Vector) list.get(k);
                    SellingAktivaItem receiveAktivaItem = (SellingAktivaItem) vect.get(0);
                    ModulAktiva modulAktiva = (ModulAktiva) vect.get(1);

                    // total penjualan
                    total = receiveAktivaItem.getTotalPriceSelling();
                    jurnaldetail = new JurnalDetail();                    
                    jurnaldetail.setIdPerkiraan(receiveAktiva.getIdPerkiraanPayment());
                    jurnaldetail.setDataStatus(PstJurnalDetail.DATASTATUS_ADD);
                    jurnaldetail.setCurrType(receiveAktiva.getIdCurrency());
                    jurnaldetail.setCurrAmount(receiveAktiva.getValueRate());
                    jurnaldetail.setDebet(total);
                    jurnaldetail.setKredit(0);
                    updateHashFromVector(hash, jurnaldetail);                    
                    
                    // nilai perolehan aktiva
                    aktiva = modulAktiva.getHargaPerolehan();
                    jurnaldetail = new JurnalDetail();                    
                    jurnaldetail.setIdPerkiraan(modulAktiva.getIdPerkiraanAktiva());
                    jurnaldetail.setDataStatus(PstJurnalDetail.DATASTATUS_ADD);
                    jurnaldetail.setCurrType(receiveAktiva.getIdCurrency());
                    jurnaldetail.setCurrAmount(receiveAktiva.getValueRate());
                    jurnaldetail.setDebet(0);
                    jurnaldetail.setKredit(aktiva);
                    updateHashFromVector(hash, jurnaldetail);                    
                    
                    // nilai akumulasipenyusutan
                    akumulasiPenyusutan = PstPenyusutanAktiva.getTotalNilaiSusut(modulAktiva.getOID());
                    if (akumulasiPenyusutan > 0)
                    {
                        jurnaldetail = new JurnalDetail();                        
                        jurnaldetail.setIdPerkiraan(modulAktiva.getIdPerkiraanAktiva());
                        jurnaldetail.setDataStatus(PstJurnalDetail.DATASTATUS_ADD);
                        jurnaldetail.setCurrType(receiveAktiva.getIdCurrency());
                        jurnaldetail.setCurrAmount(receiveAktiva.getValueRate());
                        jurnaldetail.setDebet(akumulasiPenyusutan);
                        jurnaldetail.setKredit(0);
                        updateHashFromVector(hash, jurnaldetail);                        
                    }
                    
                    // laba rugi penjualan                    
                    if (total + akumulasiPenyusutan > aktiva)
                    {
                        jurnaldetail = new JurnalDetail();                        
                        jurnaldetail.setIdPerkiraan(modulAktiva.getIdPerkiraanLbPenjAktiva());
                        jurnaldetail.setDataStatus(PstJurnalDetail.DATASTATUS_ADD);
                        jurnaldetail.setCurrType(receiveAktiva.getIdCurrency());
                        jurnaldetail.setCurrAmount(receiveAktiva.getValueRate());
                        jurnaldetail.setDebet(0);
                        jurnaldetail.setKredit(total + akumulasiPenyusutan - aktiva);
                        updateHashFromVector(hash, jurnaldetail);                        
                    }
                    
                    // laba rugi penjualan                    
                    if (total + akumulasiPenyusutan < aktiva)                    
                    {
                        jurnaldetail = new JurnalDetail();                        
                        jurnaldetail.setIdPerkiraan(modulAktiva.getIdPerkiraanRgPenjAktiva());
                        jurnaldetail.setDataStatus(PstJurnalDetail.DATASTATUS_ADD);
                        jurnaldetail.setCurrType(receiveAktiva.getIdCurrency());
                        jurnaldetail.setCurrAmount(receiveAktiva.getValueRate());
                        jurnaldetail.setDebet(aktiva - total + akumulasiPenyusutan);
                        jurnaldetail.setKredit(0);
                        updateHashFromVector(hash, jurnaldetail);                        
                    }                    
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
            System.out.println("err postingSellingAktiva : " + e.toString());
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


    /**
     * untuk mencari laporan labarugi aktiva
     * sesuai dengan parameter pencarian
     *
     * @return
     */
    public static synchronized Vector getReportLabaRugiAktiva(long periodeOid, long typeOid, long metodeOid) {
        DBResultSet dbrs = null;
        Vector list = new Vector();
        try {
	    Periode objPeriode = new Periode();
	    Date startPeriodDate = new Date();
	    Date endPeriodDate = new Date();
	    if(periodeOid != 0){
		try{
		    objPeriode = PstPeriode.fetchExc(periodeOid);
		    if(objPeriode != null){
			startPeriodDate = (Date)objPeriode.getTglAwal();
			endPeriodDate = (Date)objPeriode.getTglAkhir();
		    }
		}catch(Exception e){}
	    }
	    
            String sql = "SELECT " +
                    " A." + PstModulAktiva.fieldNames[PstModulAktiva.FLD_AKTIVA_ID] +
                    ",A." + PstModulAktiva.fieldNames[PstModulAktiva.FLD_KODE] +
                    ",A." + PstModulAktiva.fieldNames[PstModulAktiva.FLD_NAME] +
		    ",S." + PstSellingAktiva.fieldNames[PstSellingAktiva.FLD_TANGGAL_SELLING] +
                    ",SUM(A." + PstModulAktiva.fieldNames[PstModulAktiva.FLD_NILAI_RESIDU] +") AS RESIDU "+
                    ",SUM(A." + PstModulAktiva.fieldNames[PstModulAktiva.FLD_HARGA_PEROLEHAN] +") AS PEROLEHAN "+
                    ",SUM(I." + PstSellingAktivaItem.fieldNames[PstSellingAktivaItem.FLD_QTY] +") AS QTY "+
                    ",SUM(I." + PstSellingAktivaItem.fieldNames[PstSellingAktivaItem.FLD_TOTAL_PRICE] +") AS TOTAL_PRICE "+
                    ",SUM(P." + PstPenyusutanAktiva.fieldNames[PstPenyusutanAktiva.FLD_VALUE_PNY] +") AS PENYUSUTAN "+
                    " FROM " + PstModulAktiva.TBL_AKTIVA + " AS A " +
                    " INNER JOIN " + PstSellingAktivaItem.TBL_SELLING_AKTIVA_ITEM + " AS I " +
                    " ON A." + PstModulAktiva.fieldNames[PstModulAktiva.FLD_AKTIVA_ID] +
                    " = I." + PstSellingAktivaItem.fieldNames[PstSellingAktivaItem.FLD_AKTIVA_ID] +
                    " LEFT JOIN " + PstPenyusutanAktiva.TBL_PENYUSUTAN_AKTIVA + " AS P " +
                    " ON A." + PstModulAktiva.fieldNames[PstModulAktiva.FLD_AKTIVA_ID] +
                    " = P." + PstPenyusutanAktiva.fieldNames[PstPenyusutanAktiva.FLD_AKTIVA_ID] +
		    " INNER JOIN "+PstSellingAktiva.TBL_SELLING_AKTIVA+" AS S "+
		    " ON I."+PstSellingAktivaItem.fieldNames[PstSellingAktivaItem.FLD_SELLING_AKTIVA_ID]+
		    " = S."+PstSellingAktiva.fieldNames[PstSellingAktiva.FLD_SELLING_AKTIVA_ID]+
                    " WHERE S." + PstSellingAktiva.fieldNames[PstSellingAktiva.FLD_TANGGAL_SELLING] +
		    " BETWEEN '" + Formater.formatDate(startPeriodDate,"yyyy-MM-dd")+"' AND '"+Formater.formatDate(endPeriodDate,"yyyy-MM-dd")+"'";

            if (typeOid != 0) {
                sql = sql + " AND A." + PstModulAktiva.fieldNames[PstModulAktiva.FLD_TYPE_PENYUSUTAN_ID] + "=" + typeOid;
            }
            if (metodeOid != 0) {
                sql = sql + " AND A." + PstModulAktiva.fieldNames[PstModulAktiva.FLD_METODE_PENYUSUTAN_ID] + "=" + metodeOid;
            }
                sql += " GROUP BY "+
                       " A." + PstModulAktiva.fieldNames[PstModulAktiva.FLD_AKTIVA_ID] +
                       ",A." + PstModulAktiva.fieldNames[PstModulAktiva.FLD_KODE] +
		       ",S." + PstSellingAktiva.fieldNames[PstSellingAktiva.FLD_TANGGAL_SELLING] +
                       ",A." + PstModulAktiva.fieldNames[PstModulAktiva.FLD_NAME];
                
            //System.out.println("SessSellingAktiva.getReportLabaRugiAktiva() :::::: "+sql);
	    dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while (rs.next()) {
                Vector vt = new Vector();
                ModulAktiva modulAktiva = new ModulAktiva();
                SellingAktivaItem sellingAktivaItem = new SellingAktivaItem();
		SellingAktiva objSellAktiva = new SellingAktiva();
                PenyusutanAktiva penyusutanAktiva = new PenyusutanAktiva();

                modulAktiva.setOID(rs.getLong(PstModulAktiva.fieldNames[PstModulAktiva.FLD_AKTIVA_ID]));
                modulAktiva.setKode(rs.getString(PstModulAktiva.fieldNames[PstModulAktiva.FLD_KODE]));
                modulAktiva.setName(rs.getString(PstModulAktiva.fieldNames[PstModulAktiva.FLD_NAME]));
                modulAktiva.setNilaiResidu(rs.getDouble("RESIDU"));
                modulAktiva.setHargaPerolehan(rs.getDouble("PEROLEHAN"));
                vt.add(modulAktiva);

                sellingAktivaItem.setQty(rs.getInt("QTY"));
                sellingAktivaItem.setTotalPriceSelling(rs.getInt("TOTAL_PRICE"));
                vt.add(sellingAktivaItem);

                penyusutanAktiva.setValue_pny(rs.getDouble("PENYUSUTAN"));
                vt.add(penyusutanAktiva);
		
		objSellAktiva.setTanggalSelling(rs.getDate(PstSellingAktiva.fieldNames[PstSellingAktiva.FLD_TANGGAL_SELLING]));
                vt.add(objSellAktiva);
		list.add(vt);
            }
        } catch (Exception e) {
            System.out.println("err getReportLabaRugiAktiva : " + e.toString());
        }finally{DBResultSet.close(dbrs);}
        return list;
    }

    public static synchronized int getQtySellingByIdAktiva(long lAktivaId){
        DBResultSet dbrs = null;
        int iResult = 0;
            try{
                String sql = " SELECT SUM("+PstSellingAktivaItem.fieldNames[PstSellingAktivaItem.FLD_QTY]+") AS JUMLAH "+
                            " FROM "+PstSellingAktivaItem.TBL_SELLING_AKTIVA_ITEM+
                            " WHERE "+PstSellingAktivaItem.fieldNames[PstSellingAktivaItem.FLD_AKTIVA_ID]+
                            " = "+lAktivaId;
                dbrs = DBHandler.execQueryResult(sql);
                ResultSet rs = dbrs.getResultSet();
                while(rs.next()){
                    iResult = rs.getInt("JUMLAH");                   
                }
                rs.close();
            }catch(Exception e){           
            }finally{DBResultSet.close(dbrs);}
        return iResult;
    }
}
