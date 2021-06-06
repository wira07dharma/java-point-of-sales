/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.dimata.posbo.session.masterdata;

import com.dimata.posbo.entity.search.SrcStockCard;
import com.dimata.posbo.entity.warehouse.*;
import com.dimata.posbo.entity.masterdata.*;
import com.dimata.qdep.entity.I_DocStatus; 
import com.dimata.qdep.entity.I_PstDocType;
import com.dimata.qdep.entity.I_DocType;
import com.dimata.util.Formater;


import com.dimata.pos.entity.billing.*;

import com.dimata.posbo.entity.warehouse.*;
import com.dimata.posbo.entity.purchasing.PurchaseOrder;
import com.dimata.posbo.entity.purchasing.PstPurchaseOrder;
import com.dimata.posbo.entity.purchasing.PurchaseOrderItem;
import com.dimata.posbo.entity.purchasing.PstPurchaseOrderItem;
import com.dimata.posbo.db.DBResultSet;
import com.dimata.posbo.db.DBHandler;

import com.dimata.common.entity.periode.Periode;
import com.dimata.common.entity.periode.PstPeriode;
import com.dimata.common.entity.logger.PstDocLogger;
import com.dimata.common.entity.location.PstLocation;
import com.dimata.common.entity.payment.StandartRate;
import com.dimata.common.entity.payment.PstStandartRate;
import com.dimata.common.entity.location.Location;

//import cash cashier
import com.dimata.pos.entity.balance.*;

//import system property
import com.dimata.common.entity.system.*;

//import costing
import com.dimata.posbo.entity.warehouse.PstMatCosting;
import com.dimata.posbo.entity.warehouse.MatCostingItem;
import com.dimata.posbo.entity.warehouse.PstMatCostingItem;
import com.dimata.posbo.entity.warehouse.MatCosting;
import com.dimata.posbo.session.warehouse.SessMatCosting;


import java.io.Serializable;
import java.util.Vector;
import com.dimata.util.Formater;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.DriverManager;
import java.sql.Statement;


/**
 *
 * @author Dimata
 */
public class SessPostingNewThread implements Runnable, Serializable {
    private Location loc=null;
    private int threadSleep=40;
    private int maxNumber=10;
    private boolean runThread=true;
    private int errCode=0;
    private boolean pauseTransfer=false;
    private String statusTxt="";
    private SessPostingNewThread previousThread= null;
    
    private PostingProgress postingProgress = null;
    
   

    public SessPostingNewThread(){

    }

    public SessPostingNewThread(Location loc) {
        try{
            this.loc = loc;
            this.postingProgress= new PostingProgress();

        }catch(Exception e){
            System.out.println(" ! EXC : initiate thread =  "+e.toString());

        }

    }

    //private SessPostingNewThread postingThread = new SessPostingNewThread();

    public void run() {
        //update opie-eyek 20121024
        Vector vecDbConn = PstConnection.listAll();
        SessPosting_1.setVecDbConn(vecDbConn);
        Connection con = null;
        try{
            while(getPreviousThread()!=null && getPreviousThread().isRunThread()&& isRunThread()){
                Thread.sleep(3000L);
            }

            if(!isRunThread()){
                return;
            }

            String sql =null;
            Statement statement=null;
            long start =0;
            long recordToGet=10;
            
            //con = DBHandler.getDBConnection();
            //con.setAutoCommit(false);
            

            //if Dokumen Opname not Posted, cannot Posting for this location
            String where = PstMatStockOpname.fieldNames[PstMatStockOpname.FLD_STOCK_OPNAME_STATUS]+
                "="+I_DocStatus.DOCUMENT_STATUS_FINAL+
                " AND "+PstMatStockOpname.fieldNames[PstMatStockOpname.FLD_LOCATION_ID]+"="+this.loc.getOID();
            Vector cList = PstMatStockOpname.list(0,0,where,"");
        if(cList!=null && cList.size()>0) {
            setStatusTxt("Proses posting transaksi tidak dapat dilanjutkan, <br> karena masih ada Dokumen Opname yang belum terposted");

        }
        else {
            setStatusTxt("delete data di temporary table...");
            //delete history posting sebelumnya
            PstTempPostDoc.deleteHistoryPosting();
            
                //Connection con = DBHandler.getDBConnection();
                //con.setAutoCommit(false);

            //insert select document yang make table temporary
            setStatusTxt("insertDoc to temporary table...");
            try {
                System.out.println("masukkan data transaksi ke tabel "+PstTempPostDoc.TBL_TEMP_POST_DOC);
                // mengambil data trnasaksi sesuai dengan periode yang di select
                //System.out.println(" proses transaksi receive....");
                setStatusTxt(" process insert transaction receive....");
                if(PstTempPostDoc.insertSelectReceivePosting(this.loc.getOID())==false){
                //con.rollback();
                setStatusTxt(" process insert transaction receive FAILED....");
                //runThread=false;
                //return ;
                };
                //System.out.println(" proses transaksi dispatch....");
                setStatusTxt(" process insert transaction dispatch....");
                if(PstTempPostDoc.insertSelectDispatchPosting(this.loc.getOID())==false){
               // con.rollback();
                setStatusTxt(" process insert transaction dispatch FAILED....");
                //runThread=false;
                //return ;
                }
                //System.out.println(" proses transaksi sales....");
                setStatusTxt(" process insert transaction sales....");
                if(PstTempPostDoc.insertSelectSalesPosting(this.loc.getOID())==false){
                //   con.rollback();
                setStatusTxt(" process insert transaction sales FAILED...."); 
                //runThread=false;
                //return ;
                }
                //System.out.println(" proses transaksi return....");
                setStatusTxt(" process insert transaction return....");
                if(PstTempPostDoc.insertSelectReturnPosting(this.loc.getOID())==false){
                // con.rollback();
                setStatusTxt(" process insert transaction return FAILED....");   
                //runThread=false;
                //return ;
                }
                //System.out.println(" proses transaksi costing....");
                setStatusTxt(" process insert transaction costing....");
                if(PstTempPostDoc.insertSelectCostingPosting(this.loc.getOID())==false){
                // con.rollback();
                 setStatusTxt(" process insert transaction costing FAILED....");
                 //runThread=false;
                 //return ;
                };
                
                //jika semua proses insert berakhir, eksekusi dengan commit
               // con.commit();
            } catch (Exception e) {
                //pada saat insert mengalamin exception, proses rollback tidak terjadi penyimpanan data
                //System.out.println("Exc. insertTo table temp_post_doc(#,#,#,#) >> " + e.toString());
                setStatusTxt("Exc. insertTo table temp_post_doc(#,#,#,#) >> " + e.toString());
                //con.rollback();
                //jika gagal mestinya threadnya berhenti
                // runThread=false;
                //return;
            } finally {
               /*if(con !=null)
                 try {
                        con.setAutoCommit(true);
                        con.close();
                    }catch (Exception e) {
                        System.out.println("Exc. update(#,#,#,#) >> " + e.toString());
              }*/
             }
            
           
            Periode objPeriode = PstPeriode.getPeriodeRunning();
            long oidPeriode = objPeriode.getOID();

            SessPosting_1 sessPosting = new SessPosting_1();

            //set Qty In Out nol
            con = DBHandler.getDBConnection();
            con.setAutoCommit(false);
            try{
                SessPosting_1.setQtyInOutStokNolCon(this.loc.getOID(), oidPeriode, con);
                con.commit();
            }catch (Exception exc){
                  System.out.println(exc);
                  setStatusTxt("Exc. set Qty In Out nol >> " + exc.toString());
                  con.rollback();
                  return;
           } finally {
               if(con !=null)
                 try {  con.setAutoCommit(true);
                        con.close();
                    }catch (Exception e) {
                        System.out.println("Exc. update(#,#,#,#) >> " + e.toString());
              }
             }
            
            /**
             * Get Stock, update Doc Receive
             */
            
            //jumlah doc Rec to postings
            setStatusTxt(" get count transaksi receive....");
            int countRec=0;
            try{
                 countRec = SessPosting_1.getCountRec(this.loc.getOID());
                 postingProgress.setSumDocReceive(countRec);
            }catch(Exception exc){
                  System.out.println(exc);
            }
            //update opie-eyek 20121010
            if(countRec>0){
            //detail doc Rec to posting
            setStatusTxt(" get stock transaksi receive....");
            try{
                 //awalproses receive
                con = DBHandler.getDBConnection();
                con.setAutoCommit(false);

                 //int stockRec = sessPosting.getUnPostedLGRDocument(this.loc.getOID(), oidPeriode);
                 //postingProgress.setSumDocReceiveDone(stockRec + postingProgress.getSumDocReceiveDone());
                if(sessPosting.getUnPostedLGRDocument(this.loc.getOID(), oidPeriode, postingProgress, threadSleep, con)==false){
                    con.commit(); // update by opie 20121010
                    con.rollback();
                    setStatusTxt(" process get stock transaksi receive FAILED....");
                    //runThread=false;
                    //return ;
                } else {
                postingProgress.setNoteSumReceive("Get Stock Receive Done");
              
                
                //update stock perdokumen 24082012
                //try {
                    boolean hasil = false;
                    hasil = SessPosting_1.updateQtyStockAndDocPerDoc(this.loc.getOID(), oidPeriode, postingProgress, con,SessPosting_1.DOC_TYPE_RECEIVE);
                        if(hasil == true){
                            con.commit();
                            setStatusTxt(" update stock & status Document Receive closed finish....");
                            setStatusTxt("Posting Stock Receive Finish");

                        } else {
                            con.commit();// update by opie 20121010
                            con.rollback();
                            //runThread=false;
                            setStatusTxt(" update stock &  status Document Receive closed failed....");
                            setStatusTxt("Posting Stock Receive Failed");
                        }

                    //} catch (Exception e) {
                   //     System.out.println("Exc. update(#,#,#,#) >> " + e.toString());
                //}
             }
              
            }catch(Exception exc){
                  System.out.println(exc);
                  postingProgress.setNoteSumReceive("Get Stock Receive FAILED");
                   con.rollback();
                  /**try{
                       //con.setAutoCommit(false);
                       con = DBHandler.getDBConnection(); 
                       con.setAutoCommit(false);
                        SessPosting_1.setQtyInOutStokNolCon(this.loc.getOID(), oidPeriode, con);
                    }catch (Exception excDel){
                        System.out.println(excDel);
                    }**/
                  
           } finally {
                if(con !=null)
                    try {
                        con.setAutoCommit(true);
                        con.close();
                    }catch (Exception e) {
                        System.out.println("Exc. update(#,#,#,#) >> " + e.toString());
                }
            }
            
            //postingProgress.setNoteSumReceive("Get Stock Receive Done");
            
             con = DBHandler.getDBConnection();
             con.setAutoCommit(false);
            
             try{
                SessPosting_1.setQtyInOutStokNolCon(this.loc.getOID(), oidPeriode, con);
                con.commit();
            }catch (Exception exc){
                  System.out.println(exc);
                   setStatusTxt("Exc. set Qty In Out nol >> " + exc.toString());
                  con.rollback();
                  return;
                  
           } finally {
               if(con !=null)
                 try {  con.setAutoCommit(true);
                        con.close();
                    }catch (Exception e) {
                        System.out.println("Exc. update(#,#,#,#) >> " + e.toString());
              }
             }
          }else{
                setStatusTxt(" no transaksi receive found....");
          }
            
            /**
             * Get Stock, update Doc Dispatch
             */
            
            //con.setAutoCommit(false);
            //con = DBHandler.getDBConnection();
            //con.setAutoCommit(false);
            
            
            //update sampai disini 20121010
            //jumlah doc Df to posting
            
            setStatusTxt(" get count transaksi dispatch....");
            int countDf=0;
            try{
                 countDf = SessPosting_1.getCountDf(this.loc.getOID());
                 postingProgress.setSumDocDispatch(countDf);
                 
            }catch(Exception exc){
                  System.out.println(exc);
            }

           if(countDf>0){
                //detail doc df to posting
                setStatusTxt(" get stock transaksi dispatch....");
            try{
                 //int stockDf = sessPosting.getUnPostedDFDocument(this.loc.getOID(), oidPeriode);
                 //postingProgress.setSumDocDispatchDone(stockDf + postingProgress.getSumDocDispatchDone());
                 //update opie-eyek 20121022
                 con = DBHandler.getDBConnection();
                 con.setAutoCommit(false);

                 if(sessPosting.getUnPostedDFDocument(this.loc.getOID(), oidPeriode, postingProgress, threadSleep, con)==false){
                     con.commit(); //update opie-eyek 20121022
                     con.rollback();
                     setStatusTxt(" process get stock transaksi dispatch FAILED....");
                     //runThread=false;
                     //return ;
                 } else {
                   postingProgress.setNoteSumDispatch("Get Stock Dispatch Done");

                 //update stock perdokumen 24082012
                //try { opie-eyek 20121022
                    boolean hasil = false;
                    hasil = SessPosting_1.updateQtyStockAndDocPerDoc(this.loc.getOID(), oidPeriode, postingProgress, con,SessPosting_1.DOC_TYPE_DISPATCH);
                    if(hasil == true){
                        con.commit();
                        setStatusTxt(" update stock & status Document Dispatch closed finish....");
                        setStatusTxt("Posting Stock Finish");

                    } else {
                        con.commit(); //update opie-eyek 20121022
                        con.rollback();
                        //runThread=false;
                        setStatusTxt(" update stock &  status Document Dispatch closed failed....");
                        setStatusTxt("Posting Stock Failed");

                    }

                  //} catch (Exception e) {opie-eyek 20121022
                  //      System.out.println("Exc. update(#,#,#,#) >> " + e.toString());
                //}
             }
            }catch(Exception exc){
                  System.out.println(exc);
                  postingProgress.setNoteSumDispatch("Get Stock Dispatch FAILED");
                  con.rollback();
                 /** try{
                        //con.setAutoCommit(false);
                        con = DBHandler.getDBConnection();
                        con.setAutoCommit(false);
                        SessPosting_1.setQtyInOutStokNolCon(this.loc.getOID(), oidPeriode, con);
                    }catch (Exception excDel){
                        System.out.println(excDel);
                    }**/

           } finally {
                if(con !=null)
                    try {con.setAutoCommit(true); //update opie-eyek 20121022
                        con.close();
                    }catch (Exception e) {
                        System.out.println("Exc. update(#,#,#,#) >> " + e.toString());
                    }
            }   
                //postingProgress.setNoteSumDispatch("Get Stock Dispatch Done");
           
                con = DBHandler.getDBConnection();
                con.setAutoCommit(false);
                try{
                    SessPosting_1.setQtyInOutStokNolCon(this.loc.getOID(), oidPeriode, con);
                    con.commit();
                }catch (Exception exc){
                      System.out.println(exc);
                      con.rollback();
                      return;
               } finally {
                   if(con !=null)
                     try {  con.setAutoCommit(true);
                            con.close();
                        }catch (Exception e) {
                            System.out.println("Exc. update(#,#,#,#) >> " + e.toString());
                  }
                 }
            }else{
                setStatusTxt(" no transaksi dispatch found....");
           }
           
         
           /**
             * Get Stock, update Doc Sales
             */
            //con.setAutoCommit(false);
            
           //jumlah doc sales to posting
            setStatusTxt(" get count transaksi sales....");
            int countSales=0; //update opie-eyek 20121023
            try{
                 //int countSalesReg = SessPosting_1.getCountSales(this.loc.getOID());
                 //int countSalesComposite = SessPosting_1.getCountSalesComposit(this.loc.getOID());
                 //int countSales = countSalesReg + countSalesComposite;
                 countSales = SessPosting_1.getCountSales(this.loc.getOID());
                 postingProgress.setSumDocSales(countSales);
            }catch(Exception exc){
                  System.out.println(exc);
            }
            if(countSales>0){
                //detail doc sales to posting
                setStatusTxt(" get stock transaksi sales....");
               try{
                      //awal proses sales update opie-eyek 20121023
                      con = DBHandler.getDBConnection();
                      con.setAutoCommit(false);

                     //int stockSales = sessPosting.getUnPostedSalesDocument(this.loc.getOID(), oidPeriode);
                     //postingProgress.setSumDocSalesDone(stockSales + postingProgress.getSumDocSalesDone());
                       if(sessPosting.getUnPostedSalesDocument(this.loc.getOID(), oidPeriode, postingProgress, threadSleep, con)==false){
                         con.commit(); // update by opie 20121023
                         con.rollback();
                         setStatusTxt(" process get stock transaction sales FAILED....");
                         //runThread=false;
                         //return ;
                       } else {
                        postingProgress.setNoteSumSales("Get Stock Sales Done");

                        //update stock perdokumen 24082012
                        // update by opie 20121023
                        //try {
                            boolean hasil = false;
                            hasil = SessPosting_1.updateQtyStockAndDocPerDoc(this.loc.getOID(), oidPeriode, postingProgress, con,SessPosting_1.DOC_TYPE_SALE_REGULAR);
                            if(hasil == true){
                                con.commit();
                                setStatusTxt(" update stock & status Document sales closed finish....");
                                setStatusTxt("Posting Stock sales Finish");

                                //set costing from sales recipe
                                //by mirahu 20120913
                                //opie 28012013 disini proses posting penjualan composite
                                //disini nanti di buatkan commit, untuk sekarang di simpan dlu
                                //disini proses membuat document costing dari penjualan yang terjadi, document costing di buat per shift kasir.
                                Vector vErrUpdateStockBySaleItem = sessPosting.updateCompositBySalesItem(this.loc.getOID(), oidPeriode, con);

                            } else {
                                con.commit();
                                con.rollback();
                                //runThread=false;
                                setStatusTxt(" update stock &  status Document sales closed failed....");
                                setStatusTxt("Posting Stock sales Failed");
                            }
                         // update by opie 20121023
                        //} catch (Exception e) {
                         //   System.out.println("Exc. update(#,#,#,#) >> " + e.toString());
                        //}
                     }
                }catch(Exception exc){
                      System.out.println(exc);
                      postingProgress.setNoteSumSales("Get Stock Sales FAILED");
                      con.rollback();
                      /**try{
                            //con.setAutoCommit(false);
                            con = DBHandler.getDBConnection();
                            con.setAutoCommit(false);
                            SessPosting_1.setQtyInOutStokNolCon(this.loc.getOID(), oidPeriode, con);
                        }catch (Exception excDel){
                            System.out.println(excDel);
                        }**/

               } finally {
                    if(con !=null)
                        try {con.setAutoCommit(true);  // update by opie 20121023
                            con.close();
                        }catch (Exception e) {
                            System.out.println("Exc. update(#,#,#,#) >> " + e.toString());
                    }
                }
                //postingProgress.setNoteSumSales("Get Stock Sales Done");

                con = DBHandler.getDBConnection();
                con.setAutoCommit(false);
                try{
                    SessPosting_1.setQtyInOutStokNolCon(this.loc.getOID(), oidPeriode, con);
                    con.commit();
                }catch (Exception exc){
                      System.out.println(exc);
                      setStatusTxt("Exc. set Qty In Out nol >> " + exc.toString());
                      con.rollback();
                      return;
               } finally {
                   if(con !=null)
                     try {  con.setAutoCommit(true);
                            con.close();
                        }catch (Exception e) {
                            System.out.println("Exc. update(#,#,#,#) >> " + e.toString());
                  }
                 }
            }else{
                 setStatusTxt(" no transaksi sales found....");
            }
           
           
           /**
             * Get Stock, update Doc Return
             */
            //con.setAutoCommit(false);
           // con = DBHandler.getDBConnection();
            //con.setAutoCommit(false);
            

          //jumlah doc return to posting
            setStatusTxt(" get count transaksi return....");
            int countRet=0;
            try{
                 countRet = SessPosting_1.getCountRet(this.loc.getOID());
                 postingProgress.setSumDocReturn(countRet);

            }catch(Exception exc){
                  System.out.println(exc);
            }

            if(countRet>0){
                 //detail doc return to posting
              setStatusTxt(" get stock transaksi return....");
              try{
                     //awal proses return
                     con = DBHandler.getDBConnection();
                     con.setAutoCommit(false);
                     //int stockRet =  sessPosting.getUnPostedReturnDocument(this.loc.getOID(), oidPeriode);
                     //postingProgress.setSumDocReturnDone(stockRet + postingProgress.getSumDocReturnDone());
                     if(sessPosting.getUnPostedReturnDocument(this.loc.getOID(), oidPeriode, postingProgress, threadSleep, con)== false){
                         con.commit(); // update by opie 20121010
                         con.rollback();
                         setStatusTxt(" process get stock transaction return FAILED....");
                         //runThread=false;
                         //return ;
                     } else {
                     postingProgress.setNoteSumReturn("Get Stock Return Done");

                     //update stock perdokumen 24082012
                    //try {
                        boolean hasil = false;
                        hasil = SessPosting_1.updateQtyStockAndDocPerDoc(this.loc.getOID(), oidPeriode, postingProgress, con,SessPosting_1.DOC_TYPE_RETURN);
                        if(hasil == true){
                            con.commit();
                            setStatusTxt(" update stock & status Document Return closed finish....");
                            setStatusTxt("Posting Stock Return Finish");

                        } else {
                            con.commit();// update by opie 20121010
                            con.rollback();
                            //runThread=false;
                            setStatusTxt(" update stock &  status Document Return closed failed....");
                            setStatusTxt("Posting Stock Return Failed");

                        }

                    //} catch (Exception e) {
                    //    System.out.println("Exc. update(#,#,#,#) >> " + e.toString());
                    //}
                 }

                }catch(Exception exc){
                      System.out.println(exc);
                      postingProgress.setNoteSumReturn("Get Stock Return FAILED");
                      con.rollback();
                      /**try{
                            //con.setAutoCommit(false);
                            con = DBHandler.getDBConnection();
                            con.setAutoCommit(false);
                            SessPosting_1.setQtyInOutStokNolCon(this.loc.getOID(), oidPeriode, con);
                        }catch (Exception excDel){
                            System.out.println(excDel);
                        }**/

               } finally {
                    if(con !=null)
                        try {con.setAutoCommit(true);
                            con.close();
                        }catch (Exception e) {
                            System.out.println("Exc. update(#,#,#,#) >> " + e.toString());
                    }
                }
                //postingProgress.setNoteSumReturn("Get Stock Return Done");
                con = DBHandler.getDBConnection();
                con.setAutoCommit(false);
               try{
                    SessPosting_1.setQtyInOutStokNolCon(this.loc.getOID(), oidPeriode, con);
                    con.commit();
                }catch (Exception exc){
                      System.out.println(exc);
                      con.rollback();
                      return;
               } finally {
                   if(con !=null)
                     try {  con.setAutoCommit(true);
                            con.close();
                        }catch (Exception e) {
                            System.out.println("Exc. update(#,#,#,#) >> " + e.toString());
                  }
                 }
            }else{
                setStatusTxt(" no transaksi return found....");
            }

           /**
             * Get Stock, update Doc Costing
             */
            //con.setAutoCommit(false);
            //con = DBHandler.getDBConnection();
            //con.setAutoCommit(false);

           //jumlah doc costing to posting
            setStatusTxt(" get count transaksi costing....");
            int countCost=0;
            try{
                 countCost = SessPosting_1.getCountCosting(this.loc.getOID());
                 postingProgress.setSumDocCosting(countCost);
            }catch(Exception exc){
                  System.out.println(exc);
            }

            if(countCost>0){
              //detail doc costing to
              setStatusTxt(" get stock transaksi costing....");
              
              try{
                     //int stockCost = sessPosting.getUnPostedCostDocument(this.loc.getOID(), oidPeriode);
                     //postingProgress.setSumDocCostingDone(stockCost + postingProgress.getSumDocCostingDone());
                    //awal proses costing opie-eyek 20121023
                    con = DBHandler.getDBConnection();
                    con.setAutoCommit(false);
                    
                    if(sessPosting.getUnPostedCostDocument(this.loc.getOID(), oidPeriode, postingProgress, threadSleep, con)== false) {
                         con.commit(); // update by opie 20121010
                         con.rollback();
                         setStatusTxt(" process get stock transaction costing FAILED....");
                         //runThread=false;
                         //return ;
                     } else {
                     postingProgress.setNoteSumCosting("Get Stock Costing Done");

                     //update stock perdokumen 24082012
                    //try {
                        boolean hasil = false;
                        hasil = SessPosting_1.updateQtyStockAndDocPerDoc(this.loc.getOID(), oidPeriode, postingProgress, con,SessPosting_1.DOC_TYPE_COSTING);
                        if(hasil == true){
                            con.commit();
                            setStatusTxt(" update stock & status Document costing closed finish....");
                            setStatusTxt("Posting Stock costing Finish");

                        } else {
                            con.commit();
                            con.rollback();
                            //runThread=false;
                            setStatusTxt(" update stock &  status Document costing closed failed....");
                            setStatusTxt("Posting Stock costing Failed");

                        }

                   // } catch (Exception e) {
                   //     System.out.println("Exc. update(#,#,#,#) >> " + e.toString());

                   //}
                  }

                }catch(Exception exc){
                      System.out.println(exc);
                      postingProgress.setNoteSumCosting("Get Stock Costing FAILED");
                      con.rollback();
                      /**try{
                            //con.setAutoCommit(false);
                            con = DBHandler.getDBConnection();
                            con.setAutoCommit(false);
                            SessPosting_1.setQtyInOutStokNolCon(this.loc.getOID(), oidPeriode,con);
                        }catch (Exception excDel){
                            System.out.println(excDel);
                        }**/

               } finally {
                    if(con !=null)
                        try {con.setAutoCommit(true);
                            con.close();
                        }catch (Exception e) {
                            System.out.println("Exc. update(#,#,#,#) >> " + e.toString());
                        }
                }
            }else{
                 setStatusTxt(" no transaksi return costing....");
            }

          
          
          setStatusTxt(" Process Posting done....");
          
          /**try {
            boolean hasil = false;
            hasil = SessPosting_1.updateQtyStockAndDoc(this.loc.getOID(), oidPeriode, postingProgress, con);
            if(hasil == true){
              setStatusTxt(" update stock & status Document closed finish....");
              setStatusTxt("Posting Stock Finish");

           } else {
              con.rollback();
              runThread=false;
              setStatusTxt(" update stock &  status Document closed failed....");
              setStatusTxt("Posting Stock Failed");
              
           }

             } catch (Exception e) {
                System.out.println("Exc. update(#,#,#,#) >> " + e.toString());
            }
          
          
            //postingProgress.setNoteSumCosting("Get Stock Costing Done");

            //update Stok from summary existing qty, qty in and qty out
           setStatusTxt(" update Stock and status document....");
           try {
            boolean hasil = false;
            hasil = SessPosting_1.updateQtyStockAndDoc(this.loc.getOID(), oidPeriode, postingProgress, con);
            if(hasil == true){
              setStatusTxt(" update stock & status Document closed finish....");
              setStatusTxt("Posting Stock Finish");

           } else {
              con.rollback();
              runThread=false;
              setStatusTxt(" update stock &  status Document closed failed....");
              setStatusTxt("Posting Stock Failed");
              
           }

             } catch (Exception e) {
                System.out.println("Exc. update(#,#,#,#) >> " + e.toString());
            }*/
           
         /* try {
               boolean hasil = false;
                System.out.println("update Stok ");
                hasil = SessPosting_1.updateQtyStock(this.loc.getOID(), oidPeriode);
                 if (hasil == true ){
                    postingProgress.setNoteUpdateStock("Update Stock Success");

                     //set Document Status
                      setStatusTxt(" set Document Status....");
                    //set Document Receive to closed
                    try {
                        boolean hasilRec = false;
                        System.out.println("update status Document Receive :");
                        setStatusTxt(" update Status Document Receive....");
                        hasilRec = SessPosting_1.updateDocStatus(sessPosting.DOC_TYPE_RECEIVE);
                        if (hasilRec == true ){
                            postingProgress.setNoteUpdateDocReceive("Update Document Receive Success");
                        } else {
                            postingProgress.setNoteUpdateDocReceive("Update Document Receive Failed");
                        }

                    } catch (Exception e) {
                        //System.out.println("Exc. update(#,#,#,#) >> " + e.toString());
                        setStatusTxt(" update Status Document Receive Failed ...."+ e.toString());
                      }

                      //set Document Dispatch to closed
                    try {
                        boolean hasilDf = false;
                        System.out.println("update status Document Dispatch :");
                        setStatusTxt(" update Status Document Dispatch....");
                        hasilDf = SessPosting_1.updateDocStatus(sessPosting.DOC_TYPE_DISPATCH);
                        if (hasilDf == true ){
                            postingProgress.setNoteUpdateDocDispatch("Update Document Dispatch Success");
                        } else {
                            postingProgress.setNoteUpdateDocDispatch("Update Document Dispatch Failed");
                        }

                    } catch (Exception e) {
                        //System.out.println("Exc. update(#,#,#,#) >> " + e.toString());
                        setStatusTxt(" update Status Document Dispatch Failed ...."+ e.toString());
                      }

                        //set Document Sales to closed
                    try {
                        boolean hasilSales = false;
                        System.out.println("update status Document Sales :");
                        setStatusTxt(" update Status Document Sales....");
                        hasilSales = SessPosting_1.updateDocStatus(sessPosting.DOC_TYPE_SALE_REGULAR);
                        if (hasilSales == true ){
                            postingProgress.setNoteUpdateDocSales("Update Document Sales Success");
                        } else {
                            postingProgress.setNoteUpdateDocSales("Update Document Sales Failed");
                        }

                    } catch (Exception e) {
                        //System.out.println("Exc. update(#,#,#,#) >> " + e.toString());
                        setStatusTxt(" update Status Document Sales Failed ...."+ e.toString());
                }

                        //set Document Return to closed
                try {
                        boolean hasilRet = false;
                        System.out.println("update status Document Return :");
                        setStatusTxt(" update Status Document Return....");
                        hasilRet = SessPosting_1.updateDocStatus(sessPosting.DOC_TYPE_RETURN);
                        if (hasilRet == true ){
                            postingProgress.setNoteUpdateDocReturn("Update Document Return Success");
                        } else {
                            postingProgress.setNoteUpdateDocReturn("Update Document Return Failed");
                        }

                } catch (Exception e) {
                //System.out.println("Exc. update(#,#,#,#) >> " + e.toString());
                 setStatusTxt(" update Status Document Return Failed ...."+ e.toString());
                 }

                        //set Document Costing to closed
                try {
                        boolean hasilCost = false;
                        System.out.println("update status Document Costing :");
                        setStatusTxt(" update Status Document Costing....");
                        hasilCost = SessPosting_1.updateDocStatus(sessPosting.DOC_TYPE_COSTING);
                        if (hasilCost == true ){
                            postingProgress.setNoteUpdateDocCosting("Update Document Costing Success");
                        } else {
                            postingProgress.setNoteUpdateDocCosting("Update Document Costing Failed");
                        }

                } catch (Exception e) {
                    //System.out.println("Exc. update(#,#,#,#) >> " + e.toString());
                 setStatusTxt(" update Status Document Costing Failed ...."+ e.toString());
                }

                setStatusTxt(" update status Document closed finish....");

                setStatusTxt("Posting Stock Finish");

           } else {
                    postingProgress.setNoteUpdateStock("Update Stock Failed");
           }
             // setStatusTxt("Posting Stock Failed");

            } catch (Exception e) {
                System.out.println("Exc. update(#,#,#,#) >> " + e.toString());
            }*/
        
        Thread.sleep(threadSleep);
        //con.commit();
        }
        //con.rollback();
            
            
        }catch(Exception e){
            System.out.println(" ! EXC : PrinterDriverLoader > run =  "+e.toString());
            
            
        }
        //finally {
            //if(con !=null)
                 //try {
                        //con.close();
                    //}catch (Exception e) {
                        //System.out.println("Exc. update(#,#,#,#) >> " + e.toString());
              //}
        //}
        runThread=false;
           
    }

    public int getThreadSleep(){
        return threadSleep;
    }

    public void setThreadSleep(int threadSleep){
        this.threadSleep = threadSleep;
    }

    public int getErrCode(){ return errCode; }

    public void setErrCode(int errCode){ this.errCode = errCode; }

    /**
     * @return the runThread
     */
    public boolean isRunThread() {
        return runThread;
    }

    /**
     * @param runThread the runThread to set
     */
    public void setRunThread(boolean runThread) {
        this.runThread = runThread;
    }

    /**
     * @return the pauseTransfer
     */
    public boolean isPauseTransfer() {
        return pauseTransfer;
    }

    /**
     * @param pauseTransfer the pauseTransfer to set
     */
    public void setPauseTransfer(boolean pauseTransfer) {
        this.pauseTransfer = pauseTransfer;
    }

    /**
     * @return the maxNumber
     */
    public int getMaxNumber() {
        return maxNumber;
    }

    /**
     * @param maxNumber the maxNumber to set
     */
    public void setMaxNumber(int maxNumber) {
        this.maxNumber = maxNumber;
    }

    /**
     * @return the statusTxt
     */
    public String getStatusTxt() {
        return statusTxt;
    }

    /**
     * @param statusTxt the statusTxt to set
     */
    public void setStatusTxt(String statusTxt) {
        this.statusTxt = statusTxt;
    }

    /**
     * @return the postingProgres
     */
    public PostingProgress getPostingProgress() {
        return postingProgress;
    }

    /**
     * @param postingProgres the postingProgres to set
     */
    public void setPostingProgress(PostingProgress postingProgress) {
        this.postingProgress = postingProgress;
    }

    /**
     * @return the previousThread
     */
    public SessPostingNewThread getPreviousThread() {
        return previousThread;
    }

    /**
     * @param previousThread the previousThread to set
     */
    public void setPreviousThread(SessPostingNewThread previousThread) {
        this.previousThread = previousThread;
    }




}
