/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dimata.posbo.session.masterdata;

import com.dimata.posbo.entity.masterdata.*;



import com.dimata.posbo.entity.warehouse.*;
import com.dimata.posbo.db.DBHandler;

import com.dimata.common.entity.periode.Periode;
import com.dimata.common.entity.periode.PstPeriode;

//import cash cashier

//import system property

//import costing

//adding import for reposting stok
import com.dimata.posbo.entity.search.*;
import com.dimata.posbo.session.warehouse.*;
import com.dimata.posbo.session.masterdata.SessMaterialReposting;


import java.io.Serializable;
import java.util.Vector;
import com.dimata.util.Formater;
import java.sql.Connection;
import java.sql.Statement;
import java.util.Date;


/**
 *
 * @author Dimata 007
 */
public class SessRepostingThread implements Runnable, Serializable {
 private Material mat=null;
    private int threadSleep=40;
    private int maxNumber=10;
    private boolean runThread=true;
    private int errCode=0;
    private boolean pauseTransfer=false;
    private String statusTxt="";
    private SessRepostingThread previousThread= null;
    
    private RepostingProgress repostingProgress = null;
    
    private SrcMaterialRepostingStock srcMaterialRepostingStock = null;
    
    int countStock = 0;
    

    
    


    public SessRepostingThread(){

    }

    //public SessRepostingThread(Material mat, SrcMaterialRepostingStock srcMaterialRepostingStock ) {
     public SessRepostingThread(SrcMaterialRepostingStock srcMaterialRepostingStock ) {
        try{
            //this.mat = mat;
            this.srcMaterialRepostingStock = srcMaterialRepostingStock;
            this.repostingProgress= new RepostingProgress();

        }catch(Exception e){
            System.out.println(" ! EXC : initiate thread =  "+e.toString());

        }

    }

    //private SessPostingNewThread postingThread = new SessPostingNewThread();

    public void run() {
        //this.setRunThread(true);
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
            
            
            con = DBHandler.getDBConnection();
            con.setAutoCommit(false);
            
           
            Periode objPeriode = PstPeriode.getPeriodeRunning();
            long oidPeriode = objPeriode.getOID();

            
            //jumlah doc material to reposting
            setStatusTxt(" get count list material reposting....");
            try{
                 //int countMat = SessMaterial.getCountSearchRepostingStok(srcMaterialRepostingStock);
                int countMat = SessMaterialReposting.getCountSearchRepostingStok(srcMaterialRepostingStock);
                 repostingProgress.setSumMatRePosting(countMat);
            }catch(Exception exc){
                  System.out.println(exc);
            }

            //detail doc Rec to reposting
            setStatusTxt(" process list material reposting begin....");
            try{

                 Vector matReposting = new Vector();
                 Vector matRepostingGagal = new Vector();
                 //matReposting =SessMaterial.searchMaterialRepostingStock(srcMaterialRepostingStock,0, 0);
                 matReposting =SessMaterialReposting.searchMaterialRepostingStock(srcMaterialRepostingStock,0, 0);
                 if(matReposting!=null && matReposting.size()>0) {
                     for(int i=0;i<matReposting.size(); i++){
                        Vector temp = (Vector)matReposting.get(i);
                            Material material = (Material)temp.get(0);
                            Category category = (Category)temp.get(1);
                            Merk merk = (Merk)temp.get(3);
                            
                            //long materialId = material.getOID();
                            
                            con = DBHandler.getDBConnection();
                            con.setAutoCommit(false);
                            
                           
                           double qtyStock =0.0;
                           int status = 5;
                           boolean hasil = false;
                            qtyStock = qtyMaterialBasedOnStockCard(material.getOID(), srcMaterialRepostingStock.getLocationId(), srcMaterialRepostingStock.getDateTo(), status, threadSleep);
                            
                             try {
                                    hasil = updateQtyMaterialStockReposting(srcMaterialRepostingStock.getLocationId(), oidPeriode,con, qtyStock,material.getOID(), con);
                                    Material mat = new Material();
                                    if (hasil == true ){
                                        con.commit();
                                        countStock = countStock+1;
                                        repostingProgress.setNoteSumMatReposting("Material : "+material.getSku()+" :"+material.getName());
                                        repostingProgress.setSumMatRePostingDone(countStock);
                                        repostingProgress.setNoteUpdateStock("Update Stock : Kode : " +material.getSku()+" :"+material.getName()+ " Success ");
                                    } else if(hasil == false) {
                                        repostingProgress.setNoteSumMatReposting("Id Material : "+material.getSku()+" :"+material.getName()+ " FAILED UPDATED");
                                        repostingProgress.setNoteUpdateStock("Update Stock : Kode : " +material.getSku()+" :"+material.getName()+ " Failed ");
                                       
                                        mat.setOID(material.getOID());
                                        mat.setSku(material.getSku());
                                        mat.setBarCode(material.getBarCode());
                                        mat.setName(material.getName());
                                        matRepostingGagal.add(mat);
                                        repostingProgress.setMatReposting(matRepostingGagal);
                                        con.rollback();
                                        
                                        
                                        
                                        
                                       
                                        
                                    }
                                   
                                   //repostingProgress.setMatReposting(matRepostingGagal);
                                    
                                    
                            }catch (Exception e) {
                                    System.out.println("Exc. update(#,#,#,#) >> " + e.toString());
                                    //setStatusTxt(" update Stock Failed ...."+ e.toString());
                            }
                     }
                     
                     
                } else {
                    con.rollback();
                    setStatusTxt(" process get list Material to Reposting FAILED....");
                 }
                 
                
           } finally {
                if(con !=null)
                    try {
                        con.close();
                    }catch (Exception e) {
                        System.out.println("Exc. update(#,#,#,#) >> " + e.toString());
                }
            }
            
            setStatusTxt(" process reposting done...");
                    

            
           /**try {
            boolean hasil = false;
            double qtyStock = 0.0;
            //long materialId = 0;
            //Date dtStartReposting = null;
            int status = 5;
            
            
            //qtyStock = qtyMaterialBasedOnStockCard(this.mat.getOID(), this.srcMaterialRepostingStock.getLocationId(), this.srcMaterialRepostingStock.getDateTo(), status) ;
             qtyStock = qtyMaterialBasedOnStockCard(this.mat.getOID(), srcMaterialRepostingStock.getLocationId(), srcMaterialRepostingStock.getDateTo(), status) ;
             
             try {
                hasil = updateQtyMaterialStockReposting(srcMaterialRepostingStock.getLocationId(), oidPeriode,con, qtyStock,this.mat.getOID(), con);
                if (hasil == true ){
                    con.commit();
                    countStock = countStock+1;
                    repostingProgress.setNoteSumMatReposting("Id Material : "+this.mat.getOID());
                    repostingProgress.setSumMatRePostingDone(countStock);
                    repostingProgress.setNoteUpdateStock("Update Stock" +this.mat.getOID()+ "Success");
                } else {
                    con.rollback();
                    repostingProgress.setNoteSumMatReposting("Id Material : "+this.mat.getOID()+ "FAILED UPDATED");
                    repostingProgress.setNoteUpdateStock("Update Stock" +this.mat.getOID()+ "Failed");
              }
        }catch (Exception e) {
           System.out.println("Exc. update(#,#,#,#) >> " + e.toString());
           //setStatusTxt(" update Stock Failed ...."+ e.toString());
        }
             
            

             } catch (Exception e) {
                System.out.println("Exc. update(#,#,#,#) >> " + e.toString());
            }**/
           
           
        Thread.sleep(threadSleep);
        

            
        }catch(Exception e){
            System.out.println(" ! EXC : PrinterDriverLoader > run =  "+e.toString());
        }
        runThread=false;
           
    }
    
    private static double qtyMaterialBasedOnStockCard(long materialOid, long locationOid, Date dtstartReposting, int status, int threadSleep) {
        double qtyReal = 0.0;
        try {
            Formater.formatDate(dtstartReposting, "dd-MM-yyyy", "dd-mm-yyyy");
	    Date dtstart = dtstartReposting;
            //Date dtend = new Date();
            Vector LastOpname = PstMatStockOpname.getLastDateOpnameReposting(locationOid, materialOid, dtstart);
            Date dtLastOpnameDate = null;
            double qtyLastOpname = 0.0;

           if(LastOpname!=null && LastOpname.size()>0){   
                for(int i=0; i<1; i++) {
                    Vector vetTemp = (Vector)LastOpname.get(i);
                    MatStockOpname matStockOpname = (MatStockOpname)vetTemp.get(0);
                    MatStockOpnameItem matStockOpnameItem = (MatStockOpnameItem)vetTemp.get(1);
                    dtLastOpnameDate = matStockOpname.getStockOpnameDate();
                    qtyLastOpname = matStockOpnameItem.getQtyOpname();
                }
           }

            System.out.println(" LastOpnameDate " +dtLastOpnameDate);
            System.out.println(" QtyLastOpname " +qtyLastOpname);
            if (dtLastOpnameDate != null){
                dtstart = dtLastOpnameDate;
            } else {
                Periode periode = PstPeriode.getPeriodeRunning();
                dtstart = periode.getStartDate();
            }

	    


            SrcMaterialRepostingStock srcMaterialRepostingStock = new SrcMaterialRepostingStock();
            Vector vectSt = new Vector(1,1);
            Vector listDocStatus = new Vector(1, 1);
            String strPrStatus = "";

            //vectSt = PstDocStatus.getDocStatusForQtyLostOpname(); 
            //update opi-eyek agar reposting bisa cocok dengan stock
//            vectSt = PstDocStatus.getDocStatusForQtyReposting();
//            for(int i=0; i<vectSt.size(); i++) {
//               Vector vetTemp = (Vector)vectSt.get(i);
//                strPrStatus= String.valueOf(vetTemp.get(0));
//                listDocStatus.add(strPrStatus);
//            }
            listDocStatus.add("5");
            listDocStatus.add("7");
            
            srcMaterialRepostingStock.setDateFrom(dtstart);
            srcMaterialRepostingStock.setDateTo(dtstartReposting);
            srcMaterialRepostingStock.setMaterialId(materialOid);
            srcMaterialRepostingStock.setLocationId(locationOid);
            srcMaterialRepostingStock.setDocStatus(listDocStatus);

            StockCardReport stockCardReportRec = new StockCardReport();
            double qtyOpname = 0.0;
            double qtyReceive = 0.0;
            double qtyDispatch = 0.0;
            double qtyReturn = 0.0;
            double qtySale =0.0;
            double qtySaleReturn =0.0;
            double qtyCosting = 0.0;

                SessMatReceive.getQtyStockMaterialReposting(srcMaterialRepostingStock);
                qtyReceive = srcMaterialRepostingStock.getQty();
                SessMatDispatch.getQtyStockMaterialReposting(srcMaterialRepostingStock);
                qtyDispatch = srcMaterialRepostingStock.getQty();
                SessMatReturn.getQtyStockMaterialReposting(srcMaterialRepostingStock);
                qtyReturn = srcMaterialRepostingStock.getQty();
                //SessMatStockOpname.getQtyStockMaterial(srcStockCard);
                SessReportSale.getQtyStockMaterialReposting(srcMaterialRepostingStock);
                qtySale = srcMaterialRepostingStock.getQty();
                SessReportSale.getQtyStockMaterialRepostingReturn(srcMaterialRepostingStock);
                qtySaleReturn = srcMaterialRepostingStock.getQty();
                SessMatCosting.getQtyStockMaterialReposting(srcMaterialRepostingStock);
                qtyCosting = srcMaterialRepostingStock.getQty();

                qtyReal = qtyLastOpname + qtyReceive - qtyDispatch - qtyReturn - qtySale +qtySaleReturn  - qtyCosting;
                
                Thread.sleep(threadSleep);

            //Vector list = SessStockCard.createHistoryStockCardWithTime(srcStockCard);
            //qtyReal = SessStockCard.prosesGetPrivousDataStockCard(list);

        } catch (Exception ee) {
            System.out.println (ee.toString());
        }
        return qtyReal;
    }
    
     private static boolean updateQtyMaterialStockReposting(long oidLocation, long oidPeriode, Connection con, double qtyStok, long oidMaterial, Connection con1) {
        boolean hasil = false;
        try {
            // Select all receive today with detail
            String sql = " UPDATE " + PstMaterialStock.TBL_MATERIAL_STOCK +
                         " SET " + PstMaterialStock.fieldNames[PstMaterialStock.FLD_QTY] +
                         " = " + qtyStok;
                        // " = " + (materialStock.getQty() + materialStock.getQtyIn()- materialStock.getQtyOut());
                   sql = sql + " WHERE " + PstMaterialStock.fieldNames[PstMaterialStock.FLD_LOCATION_ID] +
                            " = " + oidLocation +
                            " AND " + PstMaterialStock.fieldNames[PstMaterialStock.FLD_PERIODE_ID] +
                            " = " + oidPeriode +
                           " AND " + PstMaterialStock.fieldNames[PstMaterialStock.FLD_MATERIAL_UNIT_ID] +
                            " = " + oidMaterial;

            double a = DBHandler.execUpdate(sql, con);
            hasil = true;
        } catch (Exception exc) {
            System.out.println("eRR >>= UpdStokQtyRepost : " + exc);
        }
        return hasil;
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
    public RepostingProgress getRepostingProgress() {
        return repostingProgress;
    }

    /**
     * @param postingProgres the postingProgres to set
     */
    public void setRepostingProgress(RepostingProgress repostingProgress) {
        this.repostingProgress = repostingProgress;
    }

    /**
     * @return the previousThread
     */
    public SessRepostingThread getPreviousThread() {
        return previousThread;
    }

    /**
     * @param previousThread the previousThread to set
     */
    public void setPreviousThread(SessRepostingThread previousThread) {
        this.previousThread = previousThread;
    }
    
}
