/**
 * Created by IntelliJ IDEA.
 * User: gadnyana
 * Date: Sep 3, 2005
 * Time: 9:50:44 AM
 * To change this template use Options | File Templates.
 */
package com.dimata.aiso.session.aktiva;

import com.dimata.aiso.entity.arap.ArApItem;
import com.dimata.aiso.entity.arap.ArApMain;
import com.dimata.aiso.entity.search.SrcReceiveAktiva;
import com.dimata.aiso.entity.aktiva.*;
import com.dimata.aiso.entity.jurnal.JurnalUmum;
import com.dimata.aiso.entity.jurnal.JurnalDetail;
import com.dimata.aiso.entity.jurnal.PstJurnalDetail;
import com.dimata.aiso.entity.masterdata.ModulAktiva;
import com.dimata.aiso.entity.masterdata.PstModulAktiva;
import com.dimata.aiso.db.DBResultSet;
import com.dimata.aiso.db.DBHandler;
import com.dimata.aiso.entity.arap.PstArApItem;
import com.dimata.aiso.entity.arap.PstArApMain;
import com.dimata.aiso.entity.arap.PstArApPayment;
import com.dimata.aiso.entity.jurnal.PstJurnalUmum;
import com.dimata.aiso.entity.masterdata.PstAccountLink;
import com.dimata.aiso.entity.masterdata.PstPerkiraan;
import com.dimata.aiso.entity.periode.PstPeriode;
import com.dimata.aiso.session.jurnal.SessJurnal;
import com.dimata.aiso.form.jurnal.CtrlJurnalUmum;
import com.dimata.common.entity.contact.PstContactList;
import com.dimata.common.entity.contact.ContactList;
import com.dimata.common.entity.system.PstSystemProperty;
import com.dimata.qdep.entity.I_DocStatus;
import com.dimata.util.Formater;
import com.dimata.interfaces.journal.I_JournalType;
import com.dimata.interfaces.trantype.I_TransactionType;

import java.sql.Connection;
import java.util.Vector;
import java.util.Date;
import java.sql.ResultSet;

public class SessReceiveAktiva {
    public static final String SESS_SEARCH_RECEIVE_AKTIVA = "SESS_SEARCH_RECEIVE_AKTIVA";

    /** gadnyana
     * untuk menampilkan list aktiva
     * @param srcModulAktiva
     * @param start
     * @param recordToGet
     * @return
     */
    public static Vector listReceiveAktiva(SrcReceiveAktiva srcReceiveAktiva, int start, int recordToGet) {
        DBResultSet dbrs = null;
        Vector list = new Vector();
        try {
            String sql = "SELECT " +
                    " A." + PstReceiveAktiva.fieldNames[PstReceiveAktiva.FLD_RECEIVE_AKTIVA_ID] +
                    ", A." + PstReceiveAktiva.fieldNames[PstReceiveAktiva.FLD_NOMOR_RECEIVE] +
                    ", A." + PstReceiveAktiva.fieldNames[PstReceiveAktiva.FLD_TANGGAL_RECEIVE] +
                    ", CL." + PstContactList.fieldNames[PstContactList.FLD_COMP_NAME] +
                    " FROM " + PstReceiveAktiva.TBL_RECEIVE_AKTIVA + " AS A " +
                    " INNER JOIN " + PstContactList.TBL_CONTACT_LIST + " AS CL " +
                    " ON CL." + PstContactList.fieldNames[PstContactList.FLD_CONTACT_ID] + " = A." + PstReceiveAktiva.fieldNames[PstReceiveAktiva.FLD_SUPPLIER_ID];

            String where = "";

            if (srcReceiveAktiva.getNomorReceive().length() > 0) {
                where = " A." + PstReceiveAktiva.fieldNames[PstReceiveAktiva.FLD_NOMOR_RECEIVE] + " LIKE '%" + srcReceiveAktiva.getNomorReceive() + "%'";
            }

            if (srcReceiveAktiva.getNameSupplier().length() > 0) {
                if (where.length() > 0) {
                    where = where + " AND CL." + PstContactList.fieldNames[PstContactList.FLD_COMP_NAME] + " LIKE '%" + srcReceiveAktiva.getNameSupplier() + "%'";
                } else {
                    where = " CL." + PstContactList.fieldNames[PstContactList.FLD_COMP_NAME] + " LIKE '%" + srcReceiveAktiva.getNameSupplier() + "%'";
                }
            }

            if (srcReceiveAktiva.getType() != 0) {
                if (where.length() > 0) {
                    where = where + " AND A." + PstReceiveAktiva.fieldNames[PstReceiveAktiva.FLD_TANGGAL_RECEIVE] + " BETWEEN '" + Formater.formatDate(srcReceiveAktiva.getTanggalAwal(), "yyyy-MM-dd") + "' AND '" + Formater.formatDate(srcReceiveAktiva.getTanggalakhir(), "yyyy-MM-dd") + "'";
                } else {
                    where = " A." + PstReceiveAktiva.fieldNames[PstReceiveAktiva.FLD_TANGGAL_RECEIVE] + " BETWEEN '" + Formater.formatDate(srcReceiveAktiva.getTanggalAwal(), "yyyy-MM-dd") + "' AND '" + Formater.formatDate(srcReceiveAktiva.getTanggalakhir(), "yyyy-MM-dd") + "'";
                }
            }
            if (where.length() > 0)
                sql = sql + " WHERE " + where;

            sql = sql + " ORDER BY A." + PstReceiveAktiva.fieldNames[PstReceiveAktiva.FLD_NOMOR_RECEIVE];

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
                ReceiveAktiva orderAktiva = new ReceiveAktiva();
                orderAktiva.setOID(rs.getLong(PstReceiveAktiva.fieldNames[PstReceiveAktiva.FLD_RECEIVE_AKTIVA_ID]));
                orderAktiva.setNomorReceive(rs.getString(PstReceiveAktiva.fieldNames[PstReceiveAktiva.FLD_NOMOR_RECEIVE]));
                orderAktiva.setTanggalReceive(rs.getDate(PstReceiveAktiva.fieldNames[PstReceiveAktiva.FLD_TANGGAL_RECEIVE]));

                ContactList contactList = new ContactList();
                contactList.setCompName(rs.getString(PstContactList.fieldNames[PstContactList.FLD_COMP_NAME]));

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

    /** gadnyana
     * untuk mencari jumlah row di daftar aktiva
     * @param srcReceiveAktiva
     * @return
     */
    public static int countModulAktiva(SrcReceiveAktiva srcReceiveAktiva) {
        DBResultSet dbrs = null;
        int count = 0;
        try {
            String sql = "SELECT COUNT(" +
                    " A." + PstReceiveAktiva.fieldNames[PstReceiveAktiva.FLD_RECEIVE_AKTIVA_ID] + ") AS CNT " +
                    " FROM " + PstReceiveAktiva.TBL_RECEIVE_AKTIVA + " AS A " +
                    " INNER JOIN " + PstContactList.TBL_CONTACT_LIST + " AS CL " +
                    " ON CL." + PstContactList.fieldNames[PstContactList.FLD_CONTACT_ID] + " = A." + PstReceiveAktiva.fieldNames[PstReceiveAktiva.FLD_SUPPLIER_ID];

            String where = "";
            if (srcReceiveAktiva.getNomorReceive().length() > 0) {
                where = " A." + PstReceiveAktiva.fieldNames[PstReceiveAktiva.FLD_NOMOR_RECEIVE] + " LIKE '%" + srcReceiveAktiva.getNomorReceive() + "%'";
            }

            if (srcReceiveAktiva.getNameSupplier().length() > 0) {
                if (where.length() > 0) {
                    where = where + " AND CL." + PstContactList.fieldNames[PstContactList.FLD_COMP_NAME] + " LIKE '%" + srcReceiveAktiva.getNameSupplier() + "%'";
                } else {
                    where = " CL." + PstContactList.fieldNames[PstContactList.FLD_COMP_NAME] + " LIKE '%" + srcReceiveAktiva.getNameSupplier() + "%'";
                }
            }

            if (srcReceiveAktiva.getType() != 0) {
                if (where.length() > 0) {
                    where = where + " AND A." + PstReceiveAktiva.fieldNames[PstReceiveAktiva.FLD_TANGGAL_RECEIVE] + " BETWEEN '" + Formater.formatDate(srcReceiveAktiva.getTanggalAwal(), "yyyy-MM-dd") + "' AND '" + Formater.formatDate(srcReceiveAktiva.getTanggalakhir(), "yyyy-MM-dd") + "'";
                } else {
                    where = " A." + PstReceiveAktiva.fieldNames[PstReceiveAktiva.FLD_TANGGAL_RECEIVE] + " BETWEEN '" + Formater.formatDate(srcReceiveAktiva.getTanggalAwal(), "yyyy-MM-dd") + "' AND '" + Formater.formatDate(srcReceiveAktiva.getTanggalakhir(), "yyyy-MM-dd") + "'";
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
     * untuk memposting penerimaan aktiva
     * @param bookType
     * @param userOID
     * @param periodeOID
     * @param oidReceive
     */
    public static void postingReceiveAktiva(long bookType, long userOID, long periodeOID, long oidReceive){
        postingReceiveAktiva(bookType, userOID, periodeOID, oidReceive, null, null, null,null);
    }
    
    public static synchronized long postingReceiveAktiva(long bookType, long userOID, long periodeOID, long oidReceive, ReceiveAktiva receiveAktiva, ModulAktiva objModulAktiva, Date ownDate) {
	return postingReceiveAktiva(bookType, userOID, periodeOID, oidReceive, receiveAktiva, objModulAktiva, ownDate, null);
    }
    
    public static synchronized long postingReceiveAktiva(long bookType, long userOID, long periodeOID, long oidReceive, ReceiveAktiva receiveAktiva, ModulAktiva objModulAktiva, Date ownDate, Connection con) {
        long lResult = 0;
	try {
            String sPrefDesc = "";
	    int iTransType = 0;
	    
	    iTransType = objModulAktiva.getTransType();
	    
            if(receiveAktiva != null){
                if( iTransType == I_TransactionType.TIPE_TRANSACTION_AWAL){
                    sPrefDesc = "Journal Opening Balance Fixed Assets";
                }else{
                   //sPrefDesc = "Receive fixed assets: "+objModulAktiva.getName()+", invoice number: " +
			       //receiveAktiva.getNomorInvoice()+", Acquisition Date: "+Formater.formatDate(objModulAktiva.getTglPerolehan(), "dd MMM yyyy");
                    //kurangi deskripsi di jurnal umum by Mirah 19 Mei 2011
                    sPrefDesc = objModulAktiva.getName()+"," +
			       receiveAktiva.getNomorInvoice()+", "+Formater.formatDate(objModulAktiva.getTglPerolehan(), "dd MMM yyyy");

                }
            }
	    
            double dAkmPenyusutan = 0;
            /**
             * object untuk jurnal umum
             */
            JurnalUmum jurnalUmum = new JurnalUmum();
            jurnalUmum.setTglTransaksi(receiveAktiva.getTanggalReceive());
            jurnalUmum.setTglEntry(new Date());
            jurnalUmum.setKeterangan(sPrefDesc);
            jurnalUmum.setBookType(bookType);
            jurnalUmum.setCurrType(receiveAktiva.getIdCurrency());
            jurnalUmum.setUserId(userOID);
            jurnalUmum.setPeriodeId(periodeOID);
            jurnalUmum.setReferenceDoc(receiveAktiva.getNomorReceive());
            jurnalUmum.setJurnalType(I_JournalType.TIPE_JURNAL_UMUM);
            //String strVoucher = SessJurnal.generateVoucherNumber(periodeOID, jurnalUmum.getTglTransaksi());
            //jurnalUmum.setSJurnalNumber(strVoucher);
            //jurnalUmum.setVoucherNo(strVoucher.substring(0,4));
            //jurnalUmum.setVoucherCounter(Integer.parseInt(strVoucher.substring(5)));
            jurnalUmum.setContactOid(receiveAktiva.getSupplierId());
	    
            /**
             * apakah penerimaan berdasarkan order aktiva
             */
            JurnalDetail jurnaldetail = new JurnalDetail();
            OrderAktiva orderAktiva = new OrderAktiva();
	    double dDownPayment = 0.0;
	    String sAutoReceive = "";
	    try{
		sAutoReceive = PstSystemProperty.getValueByName("AUTO_REC_FA");
	    }catch(Exception e){}
	    
	    if(sAutoReceive != null && sAutoReceive.length() > 0 && sAutoReceive.equalsIgnoreCase("N")){
		if(receiveAktiva.getOrderAktivaId()!=0){
		    try{
			orderAktiva = PstOrderAktiva.fetchExc(receiveAktiva.getOrderAktivaId());
			dDownPayment = orderAktiva.getDownPayment();
		    }catch(Exception e){}

		    jurnaldetail.setIndex(0);
		    jurnaldetail.setIdPerkiraan(orderAktiva.getIdPerkiraanDp());
		    jurnaldetail.setDataStatus(PstJurnalDetail.DATASTATUS_ADD);
		    jurnaldetail.setCurrType(orderAktiva.getIdCurrency());
		    jurnaldetail.setCurrAmount(orderAktiva.getValueRate());
		    jurnaldetail.setDebet(0);
		    jurnaldetail.setKredit(dDownPayment);
		    jurnalUmum.addDetails(jurnaldetail.getIndex(), jurnaldetail);
		}
	    }else{
		if(receiveAktiva.getDownPayment() > 0){
		    dDownPayment = receiveAktiva.getDownPayment();
		    jurnaldetail.setIndex(0);
		    jurnaldetail.setIdPerkiraan(objModulAktiva.getIdPerkiraanDp());
		    jurnaldetail.setDataStatus(PstJurnalDetail.DATASTATUS_ADD);
		    jurnaldetail.setCurrType(receiveAktiva.getIdCurrency());
		    jurnaldetail.setCurrAmount(receiveAktiva.getValueRate());
		    jurnaldetail.setDebet(0);
		    jurnaldetail.setKredit(dDownPayment);
		    jurnalUmum.addDetails(jurnaldetail.getIndex(), jurnaldetail);
		}
	    }
	    
            Vector list = PstReceiveAktivaItem.getListItem(oidReceive);
	    
            double total = 0.0;
            int idx = 0;
            if(receiveAktiva.getOrderAktivaId()!=0){
                idx = 1;
            }
	    
            if(list!=null && list.size()>0){
                for(int k=0;k<list.size();k++){
                    Vector vect = (Vector)list.get(k);
                    ReceiveAktivaItem receiveAktivaItem = (ReceiveAktivaItem)vect.get(0);
                    ModulAktiva modulAktiva = (ModulAktiva)vect.get(1);

                    jurnaldetail = new JurnalDetail();
                    jurnaldetail.setIndex(idx);
                    jurnaldetail.setIdPerkiraan(modulAktiva.getIdPerkiraanAktiva());
                    jurnaldetail.setDataStatus(PstJurnalDetail.DATASTATUS_ADD);
                    jurnaldetail.setCurrType(receiveAktiva.getIdCurrency());
                    jurnaldetail.setCurrAmount(receiveAktiva.getValueRate());
                    jurnaldetail.setDebet(receiveAktivaItem.getTotalPriceReceive());
                    jurnaldetail.setKredit(0);
                    jurnalUmum.addDetails(jurnaldetail.getIndex(), jurnaldetail);
                    
                    total = total + receiveAktivaItem.getTotalPriceReceive();
                    idx++;

                    try{
                        ModulAktiva mdlAktiva = PstModulAktiva.fetchExc(receiveAktivaItem.getAktivaId());
                        if(ownDate != null && receiveAktiva.getTypePembayaran() == I_TransactionType.TIPE_TRANSACTION_AWAL){
                            mdlAktiva.setTglPerolehan(ownDate);
                        }else{
                            mdlAktiva.setTglPerolehan(receiveAktiva.getTanggalReceive());
                        }
                        PstModulAktiva.updateExc(mdlAktiva);
                    }catch(Exception e){}
                }
            }else{
		jurnaldetail = new JurnalDetail();
		jurnaldetail.setIndex(idx);
		jurnaldetail.setIdPerkiraan(objModulAktiva.getIdPerkiraanAktiva());
		jurnaldetail.setDataStatus(PstJurnalDetail.DATASTATUS_ADD);
		jurnaldetail.setCurrType(receiveAktiva.getIdCurrency());
		jurnaldetail.setCurrAmount(receiveAktiva.getValueRate());
		jurnaldetail.setDebet(objModulAktiva.getHargaPerolehan());
		jurnaldetail.setKredit(0);
		jurnalUmum.addDetails(jurnaldetail.getIndex(), jurnaldetail);
		idx++;
	    }
	    
            // item lawan
            jurnaldetail = new JurnalDetail();
            jurnaldetail.setIndex(idx);
            jurnaldetail.setIdPerkiraan(objModulAktiva.getIdPerkiraanPayment() == 0? receiveAktiva.getIdPerkiraanPayment() : objModulAktiva.getIdPerkiraanPayment());
            jurnaldetail.setDataStatus(PstJurnalDetail.DATASTATUS_ADD);
            jurnaldetail.setCurrType(receiveAktiva.getIdCurrency());
            jurnaldetail.setCurrAmount(receiveAktiva.getValueRate());
            jurnaldetail.setDebet(0);
	    if(sAutoReceive != null && sAutoReceive.length() > 0 && sAutoReceive.equalsIgnoreCase("N")){
		jurnaldetail.setKredit(dDownPayment == 0? total :  (total - dDownPayment));
	    }else{
		jurnaldetail.setKredit(dDownPayment == 0? objModulAktiva.getHargaPerolehan() :  (objModulAktiva.getHargaPerolehan() - dDownPayment));
	    }
	    jurnalUmum.addDetails(jurnaldetail.getIndex(), jurnaldetail);
            
          
            CtrlJurnalUmum ctrlJurnalUmum = new CtrlJurnalUmum();
	    int iJournalPosted = 0;
	    if(con == null){
		iJournalPosted = ctrlJurnalUmum.JournalPosted(CtrlJurnalUmum.POSTED_JD, 0, jurnalUmum);
	    }else{
		iJournalPosted = ctrlJurnalUmum.JournalPosted(CtrlJurnalUmum.POSTED_JD, 0, jurnalUmum, con);
	    }
	    
	    if(iJournalPosted == 0){
		lResult = jurnalUmum.getOID();
		lResult = autoGenerateAPBaseOnFAReceiveTrans(receiveAktiva, objModulAktiva);
	    }
	   
        } catch (Exception e) {
            System.out.println("err postingReceiveAktiva : "+e.toString());
        }
	return lResult;
    }

    public static synchronized long postingJournalAkmPeny(long bookType, long userOID, long periodeOID, long oidPenyusutan, ReceiveAktiva objReceiveAktiva) {
	    return postingJournalAkmPeny(bookType, userOID, periodeOID, oidPenyusutan, objReceiveAktiva, null);
    }
    
public static synchronized long postingJournalAkmPeny(long bookType, long userOID, long periodeOID, long oidPenyusutan, ReceiveAktiva objReceiveAktiva, Connection con) {
    long lResult = 0;
	    try {
            PenyusutanAktiva objPenyAktiva = new PenyusutanAktiva();
            ModulAktiva objModulAktiva = new ModulAktiva();
            
            long idPerkiraanPayment = 0;
            try{
                idPerkiraanPayment = PstAccountLink.getLinkAccountId(PstPerkiraan.ACC_GROUP_EQUITY);
            }catch(Exception e){}
            
            try{
                objPenyAktiva = PstPenyusutanAktiva.fetchExc(oidPenyusutan);
            }catch(Exception e){}
            
            if(objPenyAktiva.getAktivaId() != 0){
                try{
                    objModulAktiva = PstModulAktiva.fetchExc(objPenyAktiva.getAktivaId());
                }catch(Exception e){}
            }
            
            String sPrefDesc = "Journal Opening Balance Fixed Assets Accumulation";
            
            double dAkmPenyusutan = 0;
            if(objPenyAktiva != null && objPenyAktiva.getValue_pny() != 0){
                dAkmPenyusutan = objPenyAktiva.getValue_pny();
            }
            
            Date transDate = new Date();
            try{
                transDate = PstPeriode.getFirstDateOfPeriod(periodeOID);
            }catch(Exception e){}
            
            /**
             * object untuk jurnal umum
             */
            JurnalUmum jurnalUmum = new JurnalUmum();
            jurnalUmum.setTglTransaksi(transDate);
            jurnalUmum.setTglEntry(new Date());
            jurnalUmum.setKeterangan(sPrefDesc);
            jurnalUmum.setBookType(bookType);
            jurnalUmum.setCurrType(bookType);
            jurnalUmum.setUserId(userOID);
            jurnalUmum.setPeriodeId(periodeOID);
            jurnalUmum.setReferenceDoc("AKM-"+objReceiveAktiva.getNomorReceive());
            jurnalUmum.setJurnalType(I_JournalType.TIPE_JURNAL_UMUM);
            //String strVoucher = SessJurnal.generateVoucherNumber(periodeOID, jurnalUmum.getTglTransaksi());
            //jurnalUmum.setSJurnalNumber(strVoucher);
            //jurnalUmum.setVoucherNo(strVoucher.substring(0,4));
            //jurnalUmum.setVoucherCounter(Integer.parseInt(strVoucher.substring(5)));
            jurnalUmum.setContactOid(0);

            /**
             * apakah penerimaan berdasarkan order aktiva
             */
            JurnalDetail jurnaldetail = new JurnalDetail();           
          
            jurnaldetail = new JurnalDetail();
            jurnaldetail.setIndex(0);
            jurnaldetail.setIdPerkiraan(objModulAktiva.getIdPerkiraanPayment());
            jurnaldetail.setDataStatus(PstJurnalDetail.DATASTATUS_ADD);
            jurnaldetail.setCurrType(bookType);
            jurnaldetail.setCurrAmount(1);
            jurnaldetail.setDebet(dAkmPenyusutan);
            jurnaldetail.setKredit(0);
            jurnalUmum.addDetails(jurnaldetail.getIndex(), jurnaldetail);
            
            // item lawan
            jurnaldetail = new JurnalDetail();
            jurnaldetail.setIndex(1);
            jurnaldetail.setIdPerkiraan(objModulAktiva.getIdPerkiraanAkmPenyusutan());
            jurnaldetail.setDataStatus(PstJurnalDetail.DATASTATUS_ADD);
            jurnaldetail.setCurrType(bookType);
            jurnaldetail.setCurrAmount(1);
            jurnaldetail.setDebet(0);
            jurnaldetail.setKredit(dAkmPenyusutan);
            jurnalUmum.addDetails(jurnaldetail.getIndex(), jurnaldetail);
            
           
            CtrlJurnalUmum ctrlJurnalUmum = new CtrlJurnalUmum();
	    int iJournalPosted = 0;
	    if(dAkmPenyusutan > 0){
		if(con == null){
		    iJournalPosted = ctrlJurnalUmum.JournalPosted(CtrlJurnalUmum.POSTED_JD, 0, jurnalUmum);
		}else{
		    iJournalPosted = ctrlJurnalUmum.JournalPosted(CtrlJurnalUmum.POSTED_JD, 0, jurnalUmum, con);
		}
	    }
	    if(iJournalPosted != 0){
		lResult = jurnalUmum.getOID();
	    }
        } catch (Exception e) {
            System.out.println("err postingAkmAktiva : "+e.toString());
        }
	return lResult;
    }

    public static synchronized long updateJournalFixedAssets(long lJournalId, long lAktivaId, long lAccDpId){
	return updateJournalFixedAssets(lJournalId, lAktivaId, lAccDpId, null);
    }
    
    public static synchronized long updateJournalFixedAssets(long lJournalId, long lAktivaId, long lAccDpId, Connection con){
        long lResult = 0;
	try{
            JurnalUmum objJurnalUmum = new JurnalUmum(); 
            JurnalDetail objJurnalDetailDebet = new JurnalDetail();
            JurnalDetail objJurnalDetailCredit = new JurnalDetail();
	    ReceiveAktiva objReceiveAktiva = new ReceiveAktiva();
            ModulAktiva objModulAktiva = new ModulAktiva();
            long lJDetailDebetId = 0;
            long lJDetailCreditId = 0;  
	    long lAccPayableId = 0;
	    boolean haveDownPayment = false;
	    lAccPayableId = getAccountPayabelId(lAktivaId);
	   
            if(lAktivaId != 0){
               objModulAktiva = PstModulAktiva.fetchExc(lAktivaId);
	       objReceiveAktiva = getObjReceiveAktiva(lAktivaId);
            }
	    
            if(lJournalId != 0){
		haveDownPayment = isDownPaymentJournal(lJournalId);
                objJurnalUmum = PstJurnalUmum.fetchExc(lJournalId);
                lJDetailDebetId = getIdJDetailDebetByIdJUmum(lJournalId);
                lJDetailCreditId = getIdJDetailCreditByIdJUmum(lJournalId, lAccDpId, haveDownPayment);
                if(lJDetailDebetId != 0){
                    objJurnalDetailDebet = PstJurnalDetail.fetchExc(lJDetailDebetId);
                    if(objJurnalDetailDebet != null){
                        if(objModulAktiva != null){
                            objJurnalDetailDebet.setIdPerkiraan(objModulAktiva.getIdPerkiraanAktiva());
                            objJurnalDetailDebet.setDebet(objModulAktiva.getHargaPerolehan());
			    if(con == null){
				lResult = PstJurnalDetail.updateExc(objJurnalDetailDebet);
			    }else{
				lResult = PstJurnalDetail.updateExc(objJurnalDetailDebet,con);
			    }
                        }                       
                    }
                }
                if(lJDetailCreditId != 0){
                    objJurnalDetailCredit = PstJurnalDetail.fetchExc(lJDetailCreditId);
		     long lJDetailDp = 0;
		     lJDetailDp = getJournalDetailId(lJournalId, lAccDpId);
                    if(objJurnalDetailCredit != null){                        
                        if(objModulAktiva != null){
			    if(con == null){
				if(objModulAktiva.getTransType() != I_TransactionType.TIPE_TRANSACTION_KREDIT){
				    if(lAccPayableId != 0){
					lResult = deleteAccountPayable(lAktivaId);
					if(lJDetailDp != 0){
					    try{
						lResult = PstJurnalDetail.deleteExc(lJDetailDp);
					    }catch(Exception e){}
					}
				    }
				    objJurnalDetailCredit.setIdPerkiraan(objModulAktiva.getIdPerkiraanPayment());
				    objJurnalDetailCredit.setKredit(objModulAktiva.getHargaPerolehan());
				    lResult = PstJurnalDetail.updateExc(objJurnalDetailCredit);
				    
				}else{
				    if(lAccPayableId == 0){
					lResult = autoGenerateAPBaseOnFAReceiveTrans(objReceiveAktiva, objModulAktiva);
					objJurnalDetailCredit.setIdPerkiraan(objModulAktiva.getIdPerkiraanPayment());
					if(objModulAktiva.getDownPayment() > 0){
					    objJurnalDetailCredit.setKredit(objModulAktiva.getHargaPerolehan() - objModulAktiva.getDownPayment());
					    JurnalDetail objJDetail = new JurnalDetail();
					    setObjJournalDetail(lJournalId, objJDetail, objModulAktiva, objReceiveAktiva);
					    try{
						lResult = PstJurnalDetail.insertExc(objJDetail);
					    }catch(Exception e){}
					}else{
					    objJurnalDetailCredit.setKredit(objModulAktiva.getHargaPerolehan());
					}
					try{
					    lResult = PstJurnalDetail.updateExc(objJurnalDetailCredit);
					}catch(Exception e){}
				    }else{
					lResult = procesUpdateAccountPayabel(objReceiveAktiva, objModulAktiva);
					if(lJDetailDp != 0){
					    if(objModulAktiva.getDownPayment() > 0){
						try{
						    objJurnalDetailCredit.setKredit(objModulAktiva.getHargaPerolehan() - objModulAktiva.getDownPayment());
						    JurnalDetail objJDetailDp = PstJurnalDetail.fetchExc(lJDetailDp);
						    objJDetailDp.setKredit(objModulAktiva.getDownPayment());
						    lResult = PstJurnalDetail.updateExc(objJDetailDp);
						    lResult = PstJurnalDetail.updateExc(objJurnalDetailCredit);
						}catch(Exception e){}
					    }else{
						objJurnalDetailCredit.setKredit(objModulAktiva.getHargaPerolehan());
						lResult = PstJurnalDetail.updateExc(objJurnalDetailCredit);
						lResult = PstJurnalDetail.deleteExc(lJDetailDp);
					    }
					}else{
					    objJurnalDetailCredit.setKredit(objModulAktiva.getHargaPerolehan());
					    lResult = PstJurnalDetail.updateExc(objJurnalDetailCredit);
					}
				    }
				}
			    }else{
				if(objModulAktiva.getTransType() != I_TransactionType.TIPE_TRANSACTION_KREDIT){				    
				    lResult = deleteAccountPayable(objModulAktiva.getOID());
				    objJurnalDetailCredit.setKredit(objModulAktiva.getHargaPerolehan());
				    lResult = PstJurnalDetail.updateExc(objJurnalDetailCredit);
				    if(lJDetailDp != 0){
					try{
					    lResult = PstJurnalDetail.deleteExc(lJDetailDp, con);
					}catch(Exception e){}
				    }
				}else{
				    lResult = procesUpdateAccountPayabel(objReceiveAktiva, objModulAktiva);
				    if(lJDetailDp != 0){
					objJurnalDetailCredit.setKredit(objModulAktiva.getHargaPerolehan() - objModulAktiva.getDownPayment());
					try{
					    JurnalDetail objJDetailDp = PstJurnalDetail.fetchExc(lJDetailDp);
					    objJDetailDp.setKredit(objModulAktiva.getDownPayment());
					    lResult = PstJurnalDetail.updateExc(objJDetailDp, con);
					}catch(Exception e){}
				    }else{
					objJurnalDetailCredit.setKredit(objModulAktiva.getHargaPerolehan());
					lResult = PstJurnalDetail.updateExc(objJurnalDetailCredit, con);
				    }
				}
			    }
                        }                       
                    }
                }
            }
            
        }catch(Exception e){}
	return lResult;
    }
    
     public static synchronized long updateJournalAkmFixedAssets(long lJournalId, long lAktivaId, String sRefNo, Connection con){
        long lResult = 0;
	 try{
            JurnalUmum objJurnalUmum = new JurnalUmum();
            JurnalDetail objJurnalDetailDebet = new JurnalDetail();
            JurnalDetail objJurnalDetailCredit = new JurnalDetail();
            ModulAktiva objModulAktiva = new ModulAktiva();
            long lJDetailDebetId = 0;
            long lJDetailCreditId = 0;
            double dPenyusutan = 0;
            if(lAktivaId != 0){
               objModulAktiva = PstModulAktiva.fetchExc(lAktivaId);
               dPenyusutan = PstPenyusutanAktiva.getTotalNilaiSusut(lAktivaId);
            }                
            if(lJournalId != 0){
                objJurnalUmum = PstJurnalUmum.fetchExc(lJournalId);
		if(objJurnalUmum.getReferenceDoc().length() == 0){
		    if(sRefNo != null && sRefNo.length() > 0){
			objJurnalUmum.setReferenceDoc(sRefNo);
			lResult = PstJurnalUmum.updateExc(objJurnalUmum, con);
		    }
		}
                lJDetailDebetId = getIdJDetailDebetByIdJUmum(lJournalId);
                lJDetailCreditId = getIdJDetailCreditByIdJUmum(lJournalId, objModulAktiva.getIdPerkiraanDp(), false);
                if(lJDetailDebetId != 0){
                    objJurnalDetailDebet = PstJurnalDetail.fetchExc(lJDetailDebetId);
                    if(objJurnalDetailDebet != null){
                        if(objModulAktiva != null){                            
                            objJurnalDetailDebet.setDebet(dPenyusutan);
                            lResult = PstJurnalDetail.updateExc(objJurnalDetailDebet,con);
                        }                       
                    }
                }
                if(lJDetailCreditId != 0){
                    objJurnalDetailCredit = PstJurnalDetail.fetchExc(lJDetailCreditId);
                    if(objJurnalDetailCredit != null){                        
                        if(objModulAktiva != null){
                            objJurnalDetailCredit.setIdPerkiraan(objModulAktiva.getIdPerkiraanAkmPenyusutan());
                            objJurnalDetailCredit.setKredit(dPenyusutan);
                            lResult = PstJurnalDetail.updateExc(objJurnalDetailCredit,con);
                        }                       
                    }
                }
            }
            
        }catch(Exception e){}
	return lResult;
    }
     
    public static synchronized long getIdJDetailDebetByIdJUmum(long lIdJUmum){
        DBResultSet dbrs = null;
        long lResult = 0;
        try{
            String sql = "SELECT "+PstJurnalDetail.fieldNames[PstJurnalDetail.FLD_JDETAILID]+
                        " FROM "+PstJurnalDetail.TBL_JURNAL_DETAIL+
                        " WHERE "+PstJurnalDetail.fieldNames[PstJurnalDetail.FLD_JURNALID]+" = "+lIdJUmum+
                        " AND "+PstJurnalDetail.fieldNames[PstJurnalDetail.FLD_KREDIT]+" = 0";           
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while(rs.next()){
                lResult = rs.getLong(PstJurnalDetail.fieldNames[PstJurnalDetail.FLD_JDETAILID]);
            }
        }catch(Exception e){}finally{DBResultSet.close(dbrs);}
        return lResult;
    }
    
     public static synchronized long getIdJDetailCreditByIdJUmum(long lIdJUmum, long lAccDpId, boolean haveDownPayment){
        DBResultSet dbrs = null;
        long lResult = 0;
        try{
            String sql = "SELECT "+PstJurnalDetail.fieldNames[PstJurnalDetail.FLD_JDETAILID]+
                        " FROM "+PstJurnalDetail.TBL_JURNAL_DETAIL+
                        " WHERE "+PstJurnalDetail.fieldNames[PstJurnalDetail.FLD_JURNALID]+" = "+lIdJUmum+
                        " AND "+PstJurnalDetail.fieldNames[PstJurnalDetail.FLD_DEBET]+" = 0";
	    if(haveDownPayment){
		    sql += " AND "+PstJurnalDetail.fieldNames[PstJurnalDetail.FLD_IDPERKIRAAN]+" != "+lAccDpId;
	    }
	    
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while(rs.next()){
                lResult = rs.getLong(PstJurnalDetail.fieldNames[PstJurnalDetail.FLD_JDETAILID]);
            }
        }catch(Exception e){}finally{DBResultSet.close(dbrs);}
        return lResult;
    }
     
     public static synchronized boolean isDownPaymentJournal(long lIdJUmum){
        DBResultSet dbrs = null;
        boolean bResult = false;
        try{
            String sql = "SELECT "+PstJurnalDetail.fieldNames[PstJurnalDetail.FLD_JDETAILID]+
                        " FROM "+PstJurnalDetail.TBL_JURNAL_DETAIL+
                        " WHERE "+PstJurnalDetail.fieldNames[PstJurnalDetail.FLD_JURNALID]+" = "+lIdJUmum+
                        " AND "+PstJurnalDetail.fieldNames[PstJurnalDetail.FLD_DEBET]+" = 0";
	    
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while(rs.next()){
                if(rs.getRow() == 2){
		    bResult = true;
		}
            }
        }catch(Exception e){}finally{DBResultSet.close(dbrs);}
        return bResult;
    }
     
     public static synchronized int getQtyReceiveByIdAktiva(long lAktivaId){
        DBResultSet dbrs = null;
        int iResult = 0;
        try{
            String sql = " SELECT SUM("+PstReceiveAktivaItem.fieldNames[PstReceiveAktivaItem.FLD_QTY]+") AS JUMLAH "+
                        " FROM "+PstReceiveAktivaItem.TBL_RECEIVE_AKTIVA_ITEM+
                        " WHERE "+PstReceiveAktivaItem.fieldNames[PstReceiveAktivaItem.FLD_AKTIVA_ID]+
                        " = "+lAktivaId;
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while(rs.next()){
                iResult = rs.getInt("JUMLAH");
            }
        }catch(Exception e){}finally{DBResultSet.close(dbrs);}
        return iResult;
     }
     
     public static int getOpeningBalanceQty(long lAktivaId, Date aqcDate){
        DBResultSet dbrs = null;
        int iResult = 0;
        try{
            String sql = " SELECT AT."+PstReceiveAktivaItem.fieldNames[PstReceiveAktivaItem.FLD_QTY]+" AS JUMLAH "+
                         " FROM "+PstReceiveAktiva.TBL_RECEIVE_AKTIVA+" AS MN "+
                         " INNER JOIN "+PstReceiveAktivaItem.TBL_RECEIVE_AKTIVA_ITEM+" AS AT "+
                         " ON MN."+PstReceiveAktiva.fieldNames[PstReceiveAktiva.FLD_RECEIVE_AKTIVA_ID]+
                         " = AT."+PstReceiveAktivaItem.fieldNames[PstReceiveAktivaItem.FLD_RECEIVE_AKTIVA_ID]+
                         " WHERE AT."+PstReceiveAktivaItem.fieldNames[PstReceiveAktivaItem.FLD_AKTIVA_ID]+
                         " = "+lAktivaId+" AND MN."+PstReceiveAktiva.fieldNames[PstReceiveAktiva.FLD_TANGGAL_RECEIVE]+" = '"+aqcDate+"'";
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while(rs.next()){
                iResult = rs.getInt("JUMLAH");
            }
        }catch(Exception e){}
        return iResult;
     }
     
      public static synchronized long deleteJournalDetail(long lJournalMainId){
	    return deleteJournalDetail(lJournalMainId, null);
      }
     public static synchronized long deleteJournalDetail(long lJournalMainId, Connection con){
	long lResult = 0;
	Vector vDetail = new Vector(1,1);
	JurnalDetail objJurnalDetail = new JurnalDetail();
	if(lJournalMainId != 0){
	    try{
		String whClause = PstJurnalDetail.fieldNames[PstJurnalDetail.FLD_JURNALID]+" = "+lJournalMainId;
		vDetail = (Vector)PstJurnalDetail.list(0, 0, whClause, "");
		if(vDetail != null && vDetail.size() > 0){
		    for(int i = 0; i < vDetail.size(); i++){
			objJurnalDetail = (JurnalDetail)vDetail.get(i);
			if(objJurnalDetail.getOID() != 0){
			    try{
				if(con == null){
				    lResult = PstJurnalDetail.deleteExc(objJurnalDetail.getOID());
				}else{
				    lResult = PstJurnalDetail.deleteExc(objJurnalDetail.getOID(), con);
				}
			    }catch(Exception e){}
			}
		    }
		}
	    }catch(Exception e){}
	}
	return lResult;
     }
     
     public static synchronized long generateAPMain(ReceiveAktiva objReceiveAktiva, ModulAktiva objModulAktiva){
	long lResult = 0;
	try{
	    double dAmount = 0.0;
	    dAmount = objModulAktiva.getHargaPerolehan() - objModulAktiva.getDownPayment();
	    ArApMain objArapMain = new ArApMain();                   
	    objArapMain.setAmount(dAmount);
	    objArapMain.setArApDocStatus(PstArApMain.STATUS_CLOSED);
	    objArapMain.setArApType(PstArApMain.TYPE_AP);
	    objArapMain.setContactId(objModulAktiva.getIdSupplier());
	    objArapMain.setDescription("Auto generate Account Payabale base on transaction receive fixed assets: "+objModulAktiva.getName()+
		                       " Invoice No. : "+objReceiveAktiva.getNomorReceive()+", Invoice Date: "+Formater.formatDate(objReceiveAktiva.getTanggalReceive(), "dd MMM yyyy"));
	    objArapMain.setIdCurrency(objReceiveAktiva.getIdCurrency());
	    objArapMain.setIdPerkiraan(objReceiveAktiva.getIdPerkiraanPayment());
	    objArapMain.setIdPerkiraanLawan(objModulAktiva.getIdPerkiraanAktiva());
	    objArapMain.setNotaDate(objReceiveAktiva.getTanggalReceive());
	    objArapMain.setNotaNo(objReceiveAktiva.getNomorReceive());
	    objArapMain.setNumberOfPayment(1);
	    objArapMain.setRate(objReceiveAktiva.getValueRate());
	    objArapMain.setVoucherDate(objReceiveAktiva.getTanggalReceive());
	    objArapMain.setArApMainStatus(I_DocStatus.DOCUMENT_STATUS_POSTED);
	    try{
		objArapMain = PstArApMain.createOrderNomor(objArapMain);
		lResult = PstArApMain.insertExc(objArapMain);
	    }catch(Exception e){}
	}catch(Exception e){}
	return lResult;
     }
     
     public static synchronized long autoGenerateAPBaseOnFAReceiveTrans(ReceiveAktiva objReceiveAktiva, ModulAktiva objModulAktiva){
	long lResult = 0;
	long lInsertAPMain = 0;
	double dAmount = 0.0;
	try{
	    if(objModulAktiva.getTransType() == I_TransactionType.TIPE_TRANSACTION_KREDIT){
		lInsertAPMain = generateAPMain(objReceiveAktiva, objModulAktiva);
	    }
	    
	    if(lInsertAPMain != 0){
		ArApItem objArApItem = new ArApItem();
		dAmount = objModulAktiva.getHargaPerolehan() - objModulAktiva.getDownPayment();
		objArApItem.setAngsuran(dAmount);
		objArApItem.setArApMainId(lInsertAPMain);
		objArApItem.setArApItemStatus(0);
		objArApItem.setCurrencyId(objReceiveAktiva.getIdCurrency());
		objArApItem.setDescription("Plan payment account payable for invoice number: "+objReceiveAktiva.getNomorInvoice()+", Issued Date: "+objReceiveAktiva.getTanggalReceive());
		objArApItem.setDueDate(objReceiveAktiva.getTanggalReceive());
		objArApItem.setLeftToPay(dAmount);
		objArApItem.setRate(objReceiveAktiva.getValueRate());
		objArApItem.setReceiveAktivaId(0);
		objArApItem.setSellingAktivaId(0);
		try{
		    lResult = PstArApItem.insertExc(objArApItem);
		}catch(Exception e){}
	    }
	}catch(Exception e){}
	return lResult;
     }
     
     public static synchronized long procesUpdateAccountPayabel(ReceiveAktiva objReceiveAktiva, ModulAktiva objModulAktiva){
	long lResult = 0;
	long lUpdateMain = 0;
	try{
	    lUpdateMain = updateAccountPayableMain(objReceiveAktiva, objModulAktiva);
	    if(lUpdateMain != 0){
		lResult = updateAccountPayableDetail(lUpdateMain, objModulAktiva);
	    }
	}catch(Exception e){}
	return lResult;
     }
     
     public static synchronized long updateAccountPayableMain(ReceiveAktiva objReceiveAktiva, ModulAktiva objModulAktiva){
	long lResult = 0;
	long lAPMainId = 0;
	ArApMain objArapMain = new ArApMain();
	try{
	    lAPMainId = getAccountPayabelId(objReceiveAktiva);
	    if(lAPMainId != 0){
		try{
		    objArapMain = PstArApMain.fetchExc(lAPMainId);
		    double dAmount = objModulAktiva.getHargaPerolehan() - objModulAktiva.getDownPayment();
		    objArapMain.setAmount(dAmount);
		    objArapMain.setContactId(objModulAktiva.getIdSupplier());
		    objArapMain.setNotaDate(objReceiveAktiva.getTanggalReceive());
		    objArapMain.setVoucherDate(objReceiveAktiva.getTanggalReceive());
		    
		    lResult = PstArApMain.updateExc(objArapMain);
		}catch(Exception e){}
	    }
	}catch(Exception e){}
	return lResult;
     }
     
     public static synchronized long updateAccountPayableDetail(long lArapMainId, ModulAktiva objModulAktiva){
	long lResult = 0;
	long lArapDetailId = 0;
	ArApItem objArApItem = new ArApItem();
	double dAmount = 0;
	if(lArapMainId != 0){
	    try{
		lArapDetailId = getAccountPayableDetailId(lArapMainId);
		if(lArapDetailId != 0){
		    try{
			objArApItem = PstArApItem.fetchExc(lArapDetailId);
			dAmount = objModulAktiva.getHargaPerolehan() - objModulAktiva.getDownPayment();
			objArApItem.setAngsuran(dAmount);
			objArApItem.setLeftToPay(dAmount);
			
			lResult = PstArApItem.updateExc(objArApItem);
		    }catch(Exception e){}
		}
	    }catch(Exception e){}
	}
	return lResult;
     }
     
     public static synchronized long getAccountPayabelId(ReceiveAktiva objReceiveAktiva){
	DBResultSet dbrs = null;
	long lResult = 0;
	if(objReceiveAktiva.getNomorReceive() != null && objReceiveAktiva.getNomorReceive().length() > 0 
	   && objReceiveAktiva.getSupplierId() != 0){
	    try{
		String sql = " SELECT "+PstArApMain.fieldNames[PstArApMain.FLD_ARAP_MAIN_ID]+
			     " FROM "+PstArApMain.TBL_ARAP_MAIN+
			     " WHERE "+PstArApMain.fieldNames[PstArApMain.FLD_NOTA_NO]+" = '"+objReceiveAktiva.getNomorReceive()+"' "+
			     " AND "+PstArApMain.fieldNames[PstArApMain.FLD_CONTACT_ID]+" = "+objReceiveAktiva.getSupplierId();
		
		dbrs = DBHandler.execQueryResult(sql);
		ResultSet rs = dbrs.getResultSet();
		while(rs.next()){
		    lResult = rs.getLong(PstArApMain.fieldNames[PstArApMain.FLD_ARAP_MAIN_ID]);
		}
		rs.close();
	    }catch(Exception e){}
	}
	return lResult;
     }
     
     public static synchronized long getAccountPayabelId(long lFixedAssetsId){
	long lResult = 0;
	ReceiveAktiva objReceiveAktiva = new ReceiveAktiva();
	try{
	    objReceiveAktiva = getObjReceiveAktiva(lFixedAssetsId);
	    if(objReceiveAktiva.getNomorReceive() != null && objReceiveAktiva.getNomorReceive().length() > 0
	       && objReceiveAktiva.getSupplierId() != 0){
		lResult = getAccountPayabelId(objReceiveAktiva);
	    }
	}catch(Exception e){}
	return lResult;
     }
     
     public static synchronized ReceiveAktiva getObjReceiveAktiva(long lFixedAssetsId){
	DBResultSet dbrs = null;
	ReceiveAktiva objReceiveAktiva = new ReceiveAktiva();
	try{
	    String sql = " SELECT "+PstReceiveAktivaItem.fieldNames[PstReceiveAktivaItem.FLD_RECEIVE_AKTIVA_ID]+
			 " FROM "+PstReceiveAktivaItem.TBL_RECEIVE_AKTIVA_ITEM+
			 " WHERE "+PstReceiveAktivaItem.fieldNames[PstReceiveAktivaItem.FLD_AKTIVA_ID]+
			 " = "+lFixedAssetsId;
	   
	    dbrs = DBHandler.execQueryResult(sql);
	    ResultSet rs = dbrs.getResultSet();
	    while(rs.next()){
		long lReceiveAktivaId = 0;
		lReceiveAktivaId = rs.getLong(PstReceiveAktiva.fieldNames[PstReceiveAktiva.FLD_RECEIVE_AKTIVA_ID]);
		if(lReceiveAktivaId != 0 && rs.isFirst()){
		    objReceiveAktiva = PstReceiveAktiva.fetchExc(lReceiveAktivaId);
		}
	    }
	    rs.close();
	}catch(Exception e){}
	return objReceiveAktiva;
     }
     
     public static synchronized long getAccountPayableDetailId(long lAPMainId){
	DBResultSet dbrs = null;
	long lResult = 0;
	if(lAPMainId != 0){
	    try{
		String sql = " SELECT "+PstArApItem.fieldNames[PstArApItem.FLD_ARAP_ITEM_ID]+
			     " FROM "+PstArApItem.TBL_ARAP_ITEM+
			     " WHERE "+PstArApMain.fieldNames[PstArApMain.FLD_ARAP_MAIN_ID]+" = "+lAPMainId;
		dbrs = DBHandler.execQueryResult(sql);
		ResultSet rs = dbrs.getResultSet();
		while(rs.next()){
		    lResult = rs.getLong(PstArApItem.fieldNames[PstArApItem.FLD_ARAP_ITEM_ID]);
		}
		rs.close();
	    }catch(Exception e){}
	}
	return lResult;
     }
     
     public static synchronized boolean isPaid(long lArapMainId){
	DBResultSet dbrs = null;
	boolean bResult = false;
	if(lArapMainId != 0){
	    try{
		String sql = " SELECT SUM("+PstArApPayment.fieldNames[PstArApPayment.FLD_AMOUNT]+") AS PAYMENT "+
			     " FROM "+PstArApPayment.TBL_ARAP_PAYMENT+
			     " WHERE "+PstArApPayment.fieldNames[PstArApPayment.FLD_ARAP_MAIN_ID]+" = "+lArapMainId;
		dbrs = DBHandler.execQueryResult(sql);
		ResultSet rs = dbrs.getResultSet();
		while(rs.next()){
		    double dPayment = 0;
		    dPayment = rs.getDouble("PAYMENT");
		    if(dPayment > 0){
			bResult = true;
		    }
		}
		rs.close();
	    }catch(Exception e){}
	}
	return bResult;
     }
     
     public static synchronized boolean isPayablePaid(long lArapMainId, ReceiveAktiva objReceiveAktiva){
	boolean bResult = false;
	try{
	    boolean bPaid = false;
	    bPaid = isPaid(lArapMainId);
	    if(objReceiveAktiva.getTypePembayaran() == I_TransactionType.TIPE_TRANSACTION_KREDIT && bPaid){
		bResult = true;
	    }
	}catch(Exception e){}
	return bResult;
     }
     
     public static synchronized long deleteAccountPayable(long lFixedAssetsId){
	long lResult = 0;
	long lAPMain = 0;
	if(lFixedAssetsId != 0){
	    try{
		lAPMain = getAccountPayabelId(lFixedAssetsId);
	    }catch(Exception e){}
	}
	if(lAPMain != 0){
	    try{
		lResult = PstArApMain.deleteExc(lAPMain);
		lResult = deleteAccountPayableDetail(lAPMain);
	    }catch(Exception e){}
	}
	return lResult;
     }
     
     public static synchronized long deleteAccountPayableDetail(long lAPMain){
	DBResultSet dbrs = null;
	long lResult = 0;
	if(lAPMain != 0){
	    try{
		String sql = " SELECT "+PstArApItem.fieldNames[PstArApItem.FLD_ARAP_ITEM_ID]+
			     " FROM "+PstArApItem.TBL_ARAP_ITEM+
			     " WHERE "+PstArApItem.fieldNames[PstArApItem.FLD_ARAP_MAIN_ID]+" = "+lAPMain;
		dbrs = DBHandler.execQueryResult(sql);
		ResultSet rs = dbrs.getResultSet();
		while(rs.next()){
		    long lArapItemId = 0;
		    lArapItemId = rs.getLong(PstArApItem.fieldNames[PstArApItem.FLD_ARAP_ITEM_ID]);
		    if(lArapItemId != 0){
			try{
			    lResult = PstArApItem.deleteExc(lArapItemId);
			}catch(Exception e){}
		    }
		}
		rs.close();
	    }catch(Exception e){}
	}
	return lResult;
     }
     
     public static synchronized long getJournalDetailId(long lJournalId, long lAccDpId){
	DBResultSet dbrs = null;
	long lResult = 0;
	if(lJournalId != 0 && lAccDpId != 0){
	    try{
		String sql = " SELECT "+PstJurnalDetail.fieldNames[PstJurnalDetail.FLD_JDETAILID]+
			     " FROM "+PstJurnalDetail.TBL_JURNAL_DETAIL+
			     " WHERE "+PstJurnalDetail.fieldNames[PstJurnalDetail.FLD_JURNALID]+" = "+lJournalId+
			     " AND "+PstJurnalDetail.fieldNames[PstJurnalDetail.FLD_IDPERKIRAAN]+" = "+lAccDpId;
		
		dbrs = DBHandler.execQueryResult(sql);
		ResultSet rs = dbrs.getResultSet();
		while(rs.next()){
		    lResult = rs.getLong(PstJurnalDetail.fieldNames[PstJurnalDetail.FLD_JDETAILID]);
		}
		rs.close();
	    }catch(Exception e){}
	}
	return lResult;
     }
     
     public static synchronized void setObjJournalDetail(long lJournalId, JurnalDetail objJDetail, ModulAktiva objModulAktiva, ReceiveAktiva objReceiveAktiva){
	try{
	    objJDetail.setCurrAmount(objReceiveAktiva.getValueRate());
	    objJDetail.setCurrType(objReceiveAktiva.getIdCurrency());
	    objJDetail.setDataStatus(PstJurnalUmum.DATASTATUS_ADD);
	    objJDetail.setDebet(0);
	    objJDetail.setDetailLinks(null);
	    objJDetail.setIdPerkiraan(objModulAktiva.getIdPerkiraanDp());
	    objJDetail.setIndex(2);
	    objJDetail.setJurnalIndex(lJournalId);
	    objJDetail.setKredit(objModulAktiva.getDownPayment());
	    objJDetail.setWeight(0);
	}catch(Exception e){}
     }
     
     public static synchronized boolean checkReceivedFA(long lIdFA){
	boolean bResult = false;
	String sWhClause = "";
	Vector vRecDetail = new Vector(1,1);
	if(lIdFA != 0){
	    try{
		sWhClause = PstReceiveAktivaItem.fieldNames[PstReceiveAktivaItem.FLD_AKTIVA_ID]+" = "+lIdFA;
		vRecDetail = (Vector)PstReceiveAktivaItem.list(0, 0, sWhClause, "");
		if(vRecDetail.size() > 0){
		    bResult = true;
		}
	    }catch(Exception e){}
	}
	return bResult;
     }
}
