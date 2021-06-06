/*
 * SessOtherCostOnInv.java
 *
 * Created on February 3, 2005, 11:31 AM
 */

package com.dimata.ij.session.engine;
/**
 *
 * @author  gedhy
 */
public class SessOtherCostOnInv {

    // define journal note for transaction DP On Production Order 
    public static String strJournalNote[] = 
    {
        "Transaksi DP pada saat pencarian BG/Cheque/CC/LC",
        "BG/Cheque/CC/LC cliring transaction"
    };
    
    /**
    1. Ambil Buku Pembantu Hutang Lain-lain (refer to IJ-AISO Interface getOtherCostonInvoicing).
    2. Ambil Account Mapping Other Cost on Invoicing   3. Ambil Payment Mapping
    4. Create Jurnal :
        o Debet : Account Mapping-OtherCostonInvoicing
        o Kredit : Payment Mapping
    */
    /*
    public static int generateOtherCostOnInvoiceJournal(int language, int boSystem, int intBookTypeConf, int intTaxOnSalesConf, int dfType, Date selectedDate, Hashtable hashStandartRate, long currBookType)
    {
        int result = 0;        
        
        // 1. Ambil data DP (refer to IJ-Interface getDPonSalesOrder) 
        Vector vectOfOtherCostOnInvDoc = new Vector(1,1); 
        try
        {            
            I_BOSystem i_bosys = (I_BOSystem) Class.forName(I_BOSystem.implClassName).newInstance();                                                
            vectOfOtherCostOnInvDoc = i_bosys.getListPaymentOnInvoice(selectedDate,dfType);            
        }
        catch(Exception e)
        {
            System.out.println(".:: ERR : Exception when instantiate interface");
        }        
        
        
        if(vectOfOtherCostOnInvDoc!=null && vectOfOtherCostOnInvDoc.size()>0)
        {
            int maxPaymentOnInvDoc = vectOfOtherCostOnInvDoc.size();
            for(int i=0; i<maxPaymentOnInvDoc; i++)
            {
                IjPaymentOnInvDoc objIjPaymentOnInvDoc = (IjPaymentOnInvDoc) vectOfOtherCostOnInvDoc.get(i);                
                
                String strStandartRate = (hashStandartRate.get(""+objIjPaymentOnInvDoc.getDocTransCurrency())) != null ? ""+hashStandartRate.get(""+objIjPaymentOnInvDoc.getDocTransCurrency()) : "1";
                double standartRate = Double.parseDouble(strStandartRate);
                
                // object which handle data journal main that will insert into db
                IjJournalMain objIjJournalMain = new IjJournalMain();
                objIjJournalMain.setJurContact(objIjPaymentOnInvDoc.getDocContact());
                objIjJournalMain.setJurTransDate(objIjPaymentOnInvDoc.getDocTransDate());                
                objIjJournalMain.setJurBookType(intBookTypeConf);
                objIjJournalMain.setJurTransCurrency(objIjPaymentOnInvDoc.getDocTransCurrency());                
                objIjJournalMain.setJurStatus(I_DocStatus.DOCUMENT_STATUS_DRAFT);                                
                objIjJournalMain.setJurDesc(strJournalNote[language]);
                
                // object which handle data journal detail that will insert into db
                Vector vectOfObjIjJurDetail = new Vector(1,1);                                                
                
                
                    // --- start membuat detail utk posisi kredit 1 ---                
                    // 2. Ambil OID account chart's payment dari 'payment mapping'.
                    double totalTransValue = 0;
                    Vector vectPayment = objIjPaymentOnInvDoc.getListPayment();
                    if(vectPayment!=null && vectPayment.size()>0)
                    {
                        // jika payment type selain KAS atau BANK, berarti dianggap piutang (BG, CHEQUE, CC atau LC                        
                        long paymentTypeCashOid = Long.parseLong(String.valueOf(PstSystemProperty.getValueByName("PAYMENT_TYPE_CASH")));
                        long paymentTypeBankOid = Long.parseLong(String.valueOf(PstSystemProperty.getValueByName("PAYMENT_TYPE_BANK")));                        

                        int maxPayment = vectPayment.size();
                        PstIjPaymentMapping objPstIjPaymentMapping = new PstIjPaymentMapping();
                        for(int j=0; j<maxPayment; j++)
                        {
                            IjPaymentDoc objPaymentDoc = (IjPaymentDoc) vectPayment.get(j);                        

                            // pencarian account chart di sisi debet
                            String whereClause = objPstIjPaymentMapping.fieldNames[objPstIjPaymentMapping.FLD_PAYMENT_SYSTEM] + " = " + objPaymentDoc.getPayType();
                            if(objPaymentDoc.getPayCurrency() != 0)
                            {
                                whereClause = whereClause + " AND " + objPstIjPaymentMapping.fieldNames[objPstIjPaymentMapping.FLD_CURRENCY] + " = " + objPaymentDoc.getPayCurrency();
                            }                        

                            long paymentAccChart = objPstIjPaymentMapping.getAccountChart(whereClause);                                                                                               
                            String strStandartRatePay = (hashStandartRate.get(""+objPaymentDoc.getPayCurrency())) != null ? ""+hashStandartRate.get(""+objPaymentDoc.getPayCurrency()) : "1";
                            double standartRatePay = Double.parseDouble(strStandartRatePay);                            
                            
                            // pembuatan buku pembantu piutang GIRO, CHEQUE, CC atau LC  (karena belum dicarikan di bank)
                            Vector vectIjARSubledger = new Vector(1,1);                            
                            if( !((objPaymentDoc.getPayType()==paymentTypeCashOid) || (objPaymentDoc.getPayType()==paymentTypeBankOid)) )
                            {                            
                                IjARSubledger objIjARSubledger = new IjARSubledger();                    
                                objIjARSubledger.setArParent(0);                    
                                objIjARSubledger.setArTransDate(objIjPaymentOnInvDoc.getDocTransDate());
                                objIjARSubledger.setArExpiredDate(objIjPaymentOnInvDoc.getDocTransDate());
                                objIjARSubledger.setArNoBill(objIjPaymentOnInvDoc.getDocNumber());
                                objIjARSubledger.setArAccChart(paymentAccChart);
                                objIjARSubledger.setArContact(objIjPaymentOnInvDoc.getDocContact());
                                objIjARSubledger.setArTransCurrency(objPaymentDoc.getPayCurrency());
                                objIjARSubledger.setArTransRate(standartRate);
                                objIjARSubledger.setArDebet((objPaymentDoc.getPayNominal()*standartRate));
                                objIjARSubledger.setArCredit(0);
                                objIjARSubledger.setArPaidStatus(I_DocStatus.DOCUMENT_STATUS_DRAFT);  
                                vectIjARSubledger.add(objIjARSubledger);                            
                            }                            
                            
                            // membuat detail utk posisi debet                        
                            IjJournalDetail objIjJournalDetail = new IjJournalDetail();
                            objIjJournalDetail.setJdetAccChart(paymentAccChart);
                            objIjJournalDetail.setJdetTransCurrency(objPaymentDoc.getPayCurrency());                        
                            objIjJournalDetail.setJdetTransRate(standartRate);                        
                            objIjJournalDetail.setJdetDebet((objPaymentDoc.getPayNominal()*standartRatePay));
                            objIjJournalDetail.setJdetCredit(0); 
                            objIjJournalDetail.setListOfARSubledgers(vectIjARSubledger);
                            vectOfObjIjJurDetail.add(objIjJournalDetail); 

                            // menghitung total transaksi yang diconvert ke dalam book type untuk nilai nomila account di sisi kredit
                            totalTransValue = totalTransValue + (objPaymentDoc.getPayNominal()*standartRatePay);
                        }
                    }
                    // --- finish membuat detail utk posisi kredit 1 ---

                    
                    
                    
                    // --- start membuat detail utk posisi debet 1 ---                
                    // Ambil Account Mapping Other Cost On Inv                   PstIjAccountMapping objPstIjAccountMappingDb = new PstIjAccountMapping();
                    IjAccountMapping objIjAccountMappingDb = objPstIjAccountMappingDb.getObjIjAccountMapping(I_IJGeneral.TRANS_GOODS_RECEIVE, objIjPaymentOnLGRDoc.getDocTransCurrency());                
                    long goodReceiveAccChart = objIjAccountMappingDb.getAccount();

                    // panggil buku pembantu hutang utk dikurangi dengan total pembayaran ini
                    Vector vectAPSubledger = new Vector(1,1);                    
                    
                    IjJournalDetail objIjJournalDetailCr = new IjJournalDetail();
                    objIjJournalDetailCr.setJdetAccChart(goodReceiveAccChart);                            
                    objIjJournalDetailCr.setJdetTransCurrency(currBookType);                        
                    objIjJournalDetailCr.setJdetTransRate(1);                        
                    objIjJournalDetailCr.setJdetDebet(totalTransValue);                             
                    objIjJournalDetailCr.setJdetCredit(0);            
                    objIjJournalDetailCr.setListOfAPSubledgers(vectAPSubledger);                               
                    vectOfObjIjJurDetail.add(objIjJournalDetailCr);                            
                    // --- finish membuat detail utk posisi debet 1 ---                        
                    
                // proses jurnal
                try
                {
                    long oidJournalMain = PstIjJournalMain.insertExc(objIjJournalMain);                        
                    if(vectOfObjIjJurDetail!=null && vectOfObjIjJurDetail.size()>0)
                    {
                        int maxDetail = vectOfObjIjJurDetail.size();
                        for(int it=0; it<maxDetail; it++)
                        {
                            IjJournalDetail objDetail = (IjJournalDetail) vectOfObjIjJurDetail.get(it);
                            objDetail.setJdetMainOid(oidJournalMain);
                            long oidJournalDetail = PstIjJournalDetail.insertExc(objDetail);
                            
                            // proses insert bp utang (biaya dibayar dimuka)
                            Vector listARSubledger = (Vector) objDetail.getListOfARSubledgers();
                            if(listARSubledger!=null && listARSubledger.size()>0)
                            {
                                int maxARSubledger = listARSubledger.size();
                                for(int iAr=0; iAr<maxARSubledger; iAr++)
                                {
                                    IjARSubledger objARSubledger = (IjARSubledger) listARSubledger.get(iAr);
                                    objARSubledger.setArDetailOid(oidJournalDetail);
                                    long oidARSubledger = PstIjARSubledger.insertExc(objARSubledger);
                                }
                            }
                            
                            // proses insert bp piutang (biaya dibayar dimuka)
                            Vector listAPSubledger = (Vector) objDetail.getListOfAPSubledgers();
                            if(listAPSubledger!=null && listAPSubledger.size()>0)
                            {
                                int maxAPSubledger = listAPSubledger.size();
                                for(int iAr=0; iAr<maxAPSubledger; iAr++)
                                {
                                    IjAPSubledger objAPSubledger = (IjAPSubledger) listAPSubledger.get(iAr);
                                    objAPSubledger.setApDetailOid(oidJournalDetail);
                                    long oidAPSubledger = PstIjAPSubledger.insertExc(objAPSubledger);
                                } 
                            }
                            
                        }
                    }
                }
                catch(Exception e)
                {
                    System.out.println(".::ERR : Exception when insert IjJournal : " + e.toString());
                }                                    
            }
        }        
        else
        {
            System.out.println(".::MSG : Because no document found, journaling Sales on Invoice process skip ... ");
        }
        
        return result;         
    }               
     */
}
