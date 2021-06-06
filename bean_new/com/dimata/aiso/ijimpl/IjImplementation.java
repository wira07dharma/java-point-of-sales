/*
 * IjImplementation.java
 *
 * Created on January 5, 2005, 9:15 AM
 */

package com.dimata.aiso.ijimpl;

// import core java package

import com.dimata.aiso.entity.arap.*;
import com.dimata.aiso.entity.jurnal.JurnalDetail;
import com.dimata.aiso.entity.jurnal.JurnalUmum;
import com.dimata.aiso.entity.jurnal.PstJurnalDetail;
import com.dimata.aiso.entity.jurnal.PstJurnalUmum;
import com.dimata.aiso.entity.masterdata.Perkiraan;
import com.dimata.aiso.entity.masterdata.PstPerkiraan;
import com.dimata.aiso.session.jurnal.SessJurnal;
import com.dimata.common.entity.system.PstSystemProperty;
import com.dimata.ij.I_IJGeneral;
import com.dimata.ij.iaiso.I_Aiso;
import com.dimata.ij.iaiso.IjAccountChart;
import com.dimata.ij.iaiso.IjJournalDetail;
import com.dimata.ij.iaiso.IjJournalMain;
import com.dimata.qdep.entity.I_DocStatus;

import java.util.Date;
import java.util.Vector;

/**
 * @author gedhy
 */
public class IjImplementation implements I_Aiso {

    // ***** --------- Start implement I_Aiso
    /**
     * this method used to get list account chart defend on 'account group' selected
     *
     * @param <CODE>vectOfGroupAccount</CODE>
     *         Vector of account chart group
     *         return 'vector of obj account chart'
     */
    public Vector getListAccountChart(Vector vectOfGroupAccount) {
        Vector listAccountChart = new Vector(1, 1);
        
        // process getListAccountChart
        String strAccGroup = "";
        if (vectOfGroupAccount != null && vectOfGroupAccount.size() > 0) {
            int maxAccGroup = vectOfGroupAccount.size();
            for (int i = 0; i < maxAccGroup; i++) {
                strAccGroup = strAccGroup + "(" + PstPerkiraan.fieldNames[PstPerkiraan.FLD_ACCOUNT_GROUP] + " = " + String.valueOf(vectOfGroupAccount.get(i)) + ") OR";
            }
        }

        if (strAccGroup != null && strAccGroup.length() > 3) {
            strAccGroup = strAccGroup.substring(0, strAccGroup.length() - 3);
            String strOrder = PstPerkiraan.fieldNames[PstPerkiraan.FLD_NOPERKIRAAN];
            Vector listPerkiraan = PstPerkiraan.list(0, 0, strAccGroup, strOrder);

            if (listPerkiraan != null && listPerkiraan.size() > 0) {
                int maxPerkiraan = listPerkiraan.size();
                for (int i = 0; i < maxPerkiraan; i++) {
                    Perkiraan objPerkiraan = (Perkiraan) listPerkiraan.get(i);

                    IjAccountChart objAccountChart = new IjAccountChart();
                    objAccountChart.setAccOid(objPerkiraan.getOID());
                    objAccountChart.setAccGroup(objPerkiraan.getAccountGroup());
                    objAccountChart.setAccNumber(objPerkiraan.getNoPerkiraan());
                    objAccountChart.setAccName(objPerkiraan.getNama());
                    objAccountChart.setAccParent(objPerkiraan.getIdParent());
                    objAccountChart.setAccLevel(objPerkiraan.getLevel());
                    objAccountChart.setAccNormalSign(objPerkiraan.getTandaDebetKredit());
                    objAccountChart.setAccStatus(objPerkiraan.getPostable());

                    listAccountChart.add(objAccountChart);
                }
            }
        }

        return listAccountChart;
    }


    /**
     * this method used to get account chart object defend on 'oid' selected
     *
     * @param <CODE>accountChartOid</CODE> oid of selected account chart
     *                                     return 'obj of account chart'
     */
    public IjAccountChart getAccountChart(long accountChartOid) {
        IjAccountChart objAccountChart = new IjAccountChart();

        try {
            Perkiraan objPerkiraan = PstPerkiraan.fetchExc(accountChartOid);
            objAccountChart.setAccOid(objPerkiraan.getOID());
            objAccountChart.setAccGroup(objPerkiraan.getAccountGroup());
            objAccountChart.setAccNumber(objPerkiraan.getNoPerkiraan());
            objAccountChart.setAccName(objPerkiraan.getNama());
            objAccountChart.setAccParent(objPerkiraan.getIdParent());
            objAccountChart.setAccLevel(objPerkiraan.getLevel());
            objAccountChart.setAccNormalSign(objPerkiraan.getTandaDebetKredit());
            objAccountChart.setAccStatus(objPerkiraan.getPostable());
        } catch (Exception e) {
            System.out.println(".::ERR : Exception when fetch Account Chart object : " + e.toString());
        }

        return objAccountChart;
    }


    /**
     * this method used to save object journal into AISO.
     * this process will trigger if 'journal engine conf' set into 'full automatic'
     *
     * @param <CODE>vectOfObjJournal</CODE> vector of object journal that wil be save into AISO
     *                                      return 'status of save journal process' 0 ==>no journal inserted; >0 ==> oid journal inserted
     */
    public int saveJournal(Vector vectOfObjJournal, Date dStartDatePeriode, Date dEndDatePeriode, Date dLastEntryDatePeriode) {
        int statusSaveJournal = 0;

        try {
            // --- 1. start process jurnal ---
            if (vectOfObjJournal != null && vectOfObjJournal.size() > 0) {
                int maxObjJournal = vectOfObjJournal.size();
                for (int i = 0; i < maxObjJournal; i++) {
                    IjJournalMain objJournal = (IjJournalMain) vectOfObjJournal.get(i);

                    long oidJu = saveJournal(objJournal, dStartDatePeriode, dEndDatePeriode, dLastEntryDatePeriode);
                    statusSaveJournal++;
                }
            }
            // --- 1. end process jurnal ---        
        } catch (Exception e) {
            System.out.println("Exc when save journal : " + e.toString());
        }

        return statusSaveJournal;
    }


    /**
     * this method used to save object journal into AISO.
     * this process will trigger if 'journal engine conf' set into 'full automatic'
     *
     * @param <CODE>vectOfObjJournal</CODE> vector of object journal that wil be save into AISO
     *                                      return 'status of save journal process' 0 ==>no journal inserted; >0 ==> oid journal inserted
     */
    public long saveJournal(IjJournalMain objJournal, Date dStartDatePeriode, Date dEndDatePeriode, Date dLastEntryDatePeriode) {
        long lAisoJournalOid = 0;

        try {
            // --- 1. start process jurnal umum ---
            // start pengecekan valid tidaknya entry journal berdasarkan periode
            boolean bEntryDateValid = false;
            Date dTransDate = objJournal.getJurTransDate();
            Date dPostingDate = objJournal.getJurEntryDate();
            if (dTransDate.compareTo(dStartDatePeriode) == 0 || dTransDate.compareTo(dEndDatePeriode) == 0 || (dTransDate.after(dStartDatePeriode) && dTransDate.before(dEndDatePeriode))) {
                if (dPostingDate.compareTo(dTransDate) == 0 || (dPostingDate.after(dTransDate) && dPostingDate.before(dLastEntryDatePeriode))) {
                    bEntryDateValid = true;
                }
            }

            System.out.println("bEntryDateValid : " + bEntryDateValid);
            if (bEntryDateValid) {
                lAisoJournalOid = saveJournalMain(objJournal);

                // --- 2. start process jurnal detail ---
                Vector vectJDetail = objJournal.getListOfDetails();
                if (vectJDetail != null && vectJDetail.size() > 0) {
                    int maxJDetail = vectJDetail.size();
                    for (int j = 0; j < maxJDetail; j++) {
                        IjJournalDetail objJdetail = (IjJournalDetail) vectJDetail.get(j);
                        long oidJd = saveJournalDetail(objJdetail, lAisoJournalOid);
                    }
                }                    
                // --- 2. end process jurnal detail ---

                saveArAp(objJournal);
            }
            // end pengecekan valid tidaknya entry journal berdasarkan periode
            // --- 1. end process jurnal umum ---        
        } catch (Exception e) {
            System.out.println("Exc when save journal : " + e.toString());
        }

        return lAisoJournalOid;
    }


    /**
     * this method used to get OID of book type used in AISO system.
     * return OID of aiso's book type
     */
    public long getBookType() {
        long lResult = 0;
        
        // proses pencarian book type di aiso
        String stBookType = PstSystemProperty.getValueByName("BOOK_TYPE");
        if (stBookType != null && stBookType.length() > 0) {
            lResult = Long.parseLong(stBookType);
        }

        return lResult;
    }
    
    // ------------------ start added method for store journal -----------------
    /**
     * @param objJournal
     * @return
     * @created by Edhy
     */
    private long saveJournalMain(IjJournalMain objJournal) {
        long oidJu = 0;
        
        // insert jurnal umum
        PstJurnalUmum objPstJurnalUmum = new PstJurnalUmum();
        JurnalUmum objJurnalUmum = new JurnalUmum();
        objJurnalUmum.setUserId(objJournal.getJurUser());
        objJurnalUmum.setTglTransaksi(objJournal.getJurTransDate());
        objJurnalUmum.setPeriodeId(objJournal.getJurPeriod());
        objJurnalUmum.setTglEntry(objJournal.getJurEntryDate());
        objJurnalUmum.setKeterangan(objJournal.getJurDesc());
        objJurnalUmum.setCurrType(objJournal.getJurTransCurrency());
        objJurnalUmum.setReferenceDoc(objJournal.getRefBoDocNumber());
        objJurnalUmum.setContactOid(objJournal.getContactOid());

        //String sVoucherNumber = SessJurnal.generateVoucherNumber(objJournal.getJurPeriod(), objJournal.getJurTransDate());
        //objJurnalUmum.setVoucherNo(sVoucherNumber.substring(0, 4));
        //objJurnalUmum.setVoucherCounter(Integer.parseInt(sVoucherNumber.substring(5)));

        try {
            oidJu = objPstJurnalUmum.insertExcGenerateVoucher(objJurnalUmum);
        } catch (Exception ej) {
            System.out.println("Exc when saveJournalMain : " + ej.toString());
        }

        return oidJu;
    }


    /**
     * @param objJdetail
     * @param oidJu
     * @return
     * @created by edhy
     */
    private long saveJournalDetail(IjJournalDetail objJdetail, long oidJu) {
        long oidJd = 0;
        
        // insert jurnal detail        
        PstJurnalDetail objPstJurnalDetail = new PstJurnalDetail();
        JurnalDetail objJurnalDetail = new JurnalDetail();
        objJurnalDetail.setJurnalIndex(oidJu);
        objJurnalDetail.setIdPerkiraan(objJdetail.getJdetAccChart());
        objJurnalDetail.setCurrType(objJdetail.getJdetTransCurrency());
        objJurnalDetail.setCurrAmount(objJdetail.getJdetTransRate());
        objJurnalDetail.setDebet(objJdetail.getJdetDebet());
        objJurnalDetail.setKredit(objJdetail.getJdetCredit());

        try {
            oidJd = objPstJurnalDetail.insertExc(objJurnalDetail);
        } catch (Exception ed) {
            System.out.println("Exc when saveJournalDetail : " + ed.toString());
        }

        return oidJd;
    }
    // ------------------- end added method for store journal ------------------

    private void saveArAp(IjJournalMain objJournal) {
        switch (objJournal.getRefBoDocType()) {
            case I_IJGeneral.DOC_TYPE_SALES_ON_INV:
                if (objJournal.getRefBoTransacTionType() == I_IJGeneral.TRANSACTION_TYPE_CREDIT) {
                    ArApMain main = new ArApMain();
                    main.setArApDocStatus(I_DocStatus.DOCUMENT_STATUS_POSTED);
                    main.setArApType(PstArApMain.TYPE_AR);
                    main.setArApMainStatus(PstArApMain.STATUS_CLOSED);
                    main.setContactId(objJournal.getContactOid());
                    main.setDescription("Generated from BO");
                    main.setNotaDate(objJournal.getJurTransDate());
                    main.setNotaNo(objJournal.getRefBoDocNumber());
                    main.setNumberOfPayment(1);
                    main.setVoucherDate(objJournal.getJurEntryDate());
                    IjJournalDetail jdetail = maxAtDebet(objJournal);
                    main.setIdPerkiraan(jdetail.getJdetAccChart());
                    main.setIdCurrency(jdetail.getJdetTransCurrency());
                    main.setRate(jdetail.getJdetTransRate());
                    main.setAmount(jdetail.getJdetDebet());
                    jdetail = maxAtCredit(objJournal);
                    main.setIdPerkiraanLawan(jdetail.getJdetAccChart());
                    PstArApMain.createOrderNomor(main);

                    ArApItem item = new ArApItem();
                    item.setAngsuran(main.getAmount());
                    item.setArApItemStatus(PstArApMain.STATUS_CLOSED);
                    item.setCurrencyId(main.getIdCurrency());
                    item.setDueDate(main.getNotaDate());
                    item.setLeftToPay(main.getAmount());
                    item.setRate(main.getRate());
                    item.setDescription("Generated from BO");

                    try {
                        long oid = PstArApMain.insertExc(main);
                        oid = PstArApMain.updateSynchOID(oid, objJournal.getRefBoDocOid());
                        item.setArApMainId(oid);
                        PstArApItem.insertExc(item);
                    } catch (Exception e) {
                        System.out.println("err on saveArAp() : " + e.toString());
                    }
                }
                break;
            case I_IJGeneral.DOC_TYPE_PURCHASE_ON_LGR:
                if (objJournal.getRefBoTransacTionType() == I_IJGeneral.TRANSACTION_TYPE_CREDIT) {
                    ArApMain main = new ArApMain();
                    main.setArApDocStatus(I_DocStatus.DOCUMENT_STATUS_POSTED);
                    main.setArApType(PstArApMain.TYPE_AP);
                    main.setArApMainStatus(PstArApMain.STATUS_CLOSED);
                    main.setContactId(objJournal.getContactOid());
                    main.setDescription("Generated from BO");
                    main.setNotaDate(objJournal.getJurTransDate());
                    main.setNotaNo(objJournal.getRefBoDocNumber());
                    main.setNumberOfPayment(1);
                    main.setVoucherDate(objJournal.getJurEntryDate());
                    IjJournalDetail jdetail = maxAtCredit(objJournal);
                    main.setIdPerkiraan(jdetail.getJdetAccChart());
                    main.setIdCurrency(jdetail.getJdetTransCurrency());
                    main.setRate(jdetail.getJdetTransRate());
                    main.setAmount(jdetail.getJdetCredit());
                    jdetail = maxAtDebet(objJournal);
                    main.setIdPerkiraanLawan(jdetail.getJdetAccChart());
                    PstArApMain.createOrderNomor(main);

                    ArApItem item = new ArApItem();
                    item.setAngsuran(main.getAmount());
                    item.setArApItemStatus(PstArApMain.STATUS_CLOSED);
                    item.setCurrencyId(main.getIdCurrency());
                    item.setDueDate(main.getNotaDate());
                    item.setLeftToPay(main.getAmount());
                    item.setRate(main.getRate());
                    item.setDescription("Generated from BO");

                    try {
                        long oid = PstArApMain.insertExc(main);
                        oid = PstArApMain.updateSynchOID(oid, objJournal.getRefBoDocOid());
                        item.setArApMainId(oid);
                        PstArApItem.insertExc(item);
                    } catch (Exception e) {
                        System.out.println("err on saveArAp() : " + e.toString());
                    }
                }
                break;
            case I_IJGeneral.DOC_TYPE_PAYMENT_ON_INV:
                for (int i = 0; i < objJournal.getListOfDetails().size(); i++) {
                    IjJournalDetail jdetail = (IjJournalDetail) objJournal.getListOfDetails().get(i);
                    if (jdetail.getJdetCredit() > 0) {
                        ArApPayment payment = new ArApPayment();
                        payment.setArapMainId(objJournal.getRefBoDocOid());
                        payment.setArApType(PstArApMain.TYPE_AR);
                        payment.setContactId(objJournal.getContactOid());
                        payment.setLeftToPay(0);
                        payment.setPaymentDate(objJournal.getJurTransDate());
                        payment.setPaymentStatus(I_DocStatus.DOCUMENT_STATUS_POSTED);
                        payment.setIdPerkiraanPayment(jdetail.getJdetAccChart());
                        payment.setIdCurrency(jdetail.getJdetTransCurrency());
                        payment.setRate(jdetail.getJdetTransRate());
                        payment.setAmount(jdetail.getJdetCredit());
                        try {
                            long oid = PstArApPayment.insertExc(payment);
                        } catch (Exception e) {
                            System.out.println("err on saveArAp() : " + e.toString());
                        }
                    }
                }
                break;
            case I_IJGeneral.DOC_TYPE_PAYMENT_ON_LGR:
                for (int i = 0; i < objJournal.getListOfDetails().size(); i++) {
                    IjJournalDetail jdetail = (IjJournalDetail) objJournal.getListOfDetails().get(i);
                    if (jdetail.getJdetDebet() > 0) {
                        ArApPayment payment = new ArApPayment();
                        payment.setArapMainId(objJournal.getRefBoDocOid());
                        payment.setArApType(PstArApMain.TYPE_AR);
                        payment.setContactId(objJournal.getContactOid());
                        payment.setLeftToPay(0);
                        payment.setPaymentDate(objJournal.getJurTransDate());
                        payment.setPaymentStatus(I_DocStatus.DOCUMENT_STATUS_POSTED);
                        payment.setIdPerkiraanPayment(jdetail.getJdetAccChart());
                        payment.setIdCurrency(jdetail.getJdetTransCurrency());
                        payment.setRate(jdetail.getJdetTransRate());
                        payment.setAmount(jdetail.getJdetDebet());
                        try {
                            long oid = PstArApPayment.insertExc(payment);
                        } catch (Exception e) {
                            System.out.println("err on saveArAp() : " + e.toString());
                        }
                    }
                }
                break;
            default:
                break;
        }
    }

    private IjJournalDetail maxAtDebet(IjJournalMain objJournal) {
        IjJournalDetail result = new IjJournalDetail();
        for (int i = 0; i < objJournal.getListOfDetails().size(); i++) {
            IjJournalDetail temp = (IjJournalDetail) objJournal.getListOfDetails().get(i);
            if (temp.getJdetDebet() > result.getJdetDebet()) {
                result = temp;
            }
        }
        return result;
    }

    private IjJournalDetail maxAtCredit(IjJournalMain objJournal) {
        IjJournalDetail result = new IjJournalDetail();
        for (int i = 0; i < objJournal.getListOfDetails().size(); i++) {
            IjJournalDetail temp = (IjJournalDetail) objJournal.getListOfDetails().get(i);
            if (temp.getJdetCredit() > result.getJdetDebet()) {
                result = temp;
            }
        }
        return result;
    }
    // ***** --------- Finish implement I_Aiso

	@Override
	public boolean isMultiDepartmentReport() {
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public boolean isCoaInDifferentDepartment(long firstCoaId, long secondCoaId) {
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public boolean isTransactionOnSamePeriod(Date firstTransDate, Date secondTransDate) {
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public double getApBalance(long lContactOid) {
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public double getArBalance(long lContactOid) {
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

}
