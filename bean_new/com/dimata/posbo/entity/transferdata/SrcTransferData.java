/*
 * SrcTransferData.java
 *
 * Created on December 21, 2004, 3:11 PM
 */

package com.dimata.posbo.entity.transferdata;

/**
 *
 * @author  yogi
 */
import java.util.Date;

public class SrcTransferData {
    
    /** Holds value of property dateFrom. */
    private Date dateFrom;
    
    /** Holds value of property dateTo. */
    private Date dateTo;
    
    /** Holds value of property indexTable. */
    private int indexTable = -1;
    
    /** Holds value of property typeTransfer. */
    private int typeTransfer;
    
    public static final int TO_CASHIER_OUTLET = 0;
    
    public static final int FROM_CASHIER_OUTLET = 1;
    
    public static final int ALL_DATA = 2;

    public static final int CATALOG_TO_CASHIER = 3;

    public static final String[][] typeNames = {
        {"Data Master","Data Transaksi","Semua Data","Transfer ke kasir"},
        {"Master data","Transaction Data","All Data","transfer to cashier"}
    };
    
    
    /** Creates a new instance of SrcTransferData */
    public SrcTransferData() {
    }
    
    /** Getter for property dateFrom.
     * @return Value of property dateFrom.
     *
     */
    public Date getDateFrom() {
        return this.dateFrom;
    }
    
    /** Setter for property dateFrom.
     * @param dateFrom New value of property dateFrom.
     *
     */
    public void setDateFrom(Date dateFrom) {
        this.dateFrom = dateFrom;
    }
    
    /** Getter for property dateTo.
     * @return Value of property dateTo.
     *
     */
    public Date getDateTo() {
        return this.dateTo;
    }
    
    /** Setter for property dateTo.
     * @param dateTo New value of property dateTo.
     *
     */
    public void setDateTo(Date dateTo) {
        this.dateTo = dateTo;
    }
    
    /** Getter for property indexTable.
     * @return Value of property indexTable.
     *
     */
    public int getIndexTable() {
        return this.indexTable;
    }
    
    /** Setter for property indexTable.
     * @param indexTable New value of property indexTable.
     *
     */
    public void setIndexTable(int indexTable) {
        this.indexTable = indexTable;
    }
    
    /** Getter for property typeTransfer.
     * @return Value of property typeTransfer.
     *
     */
    public int getTypeTransfer() {
        return this.typeTransfer;
    }
    
    /** Setter for property typeTransfer.
     * @param typeTransfer New value of property typeTransfer.
     *
     */
    public void setTypeTransfer(int typeTransfer) {
        this.typeTransfer = typeTransfer;
    }
    
}
