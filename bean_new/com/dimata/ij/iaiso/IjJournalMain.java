/*
 * Journal.java
 *
 * Created on December 29, 2004, 12:06 PM
 */

package com.dimata.ij.iaiso;

// qdep package

import com.dimata.qdep.entity.*;

// import core java package
import java.util.*;

/**
 *
 * @author  Administrator
 * @version
 */
public class IjJournalMain extends Entity {

    /** Holds value of property jurUser. */
    private long jurUser = 0;

    /** Holds value of property jurTransDate. */
    private Date jurTransDate;

    /** Holds value of property jurPeriod. */
    private long jurPeriod = 0;

    /** Holds value of property jurEntryDate. */
    private Date jurEntryDate;

    /** Holds value of property jurDesc. */
    private String jurDesc = "";

    /** Holds value of property jurTransCurrency. */
    private long jurTransCurrency = 0;

    /** Holds value of property jurDetails. */
    private Vector listOfDetails;

    /** Holds value of property jurBookType. */
    private long jurBookType = 0;

    /** Holds value of property jurStatus. */
    private int jurStatus = 0;

    /** Holds value of property refBoDocType. */
    private int refBoDocType = -1;

    /** Holds value of property refBoDocOid. */
    private long refBoDocOid = 0;

    /** Holds value of property refBoDocOid. */
    private long refSubBoDocOid = 0;

    /** Holds value of property refBoDocNumber. */
    private String refBoDocNumber = "";

    /** Holds value of property refAisoJournalOid. */
    private long refAisoJournalOid = 0;

    /** Holds value of property refBoSystem. */
    private int refBoSystem;

    /** Holds value of property refBoDocLastUpdate. */
    private Date refBoDocLastUpdate;

    /** Holds value of property refBoLocation. */
    private long refBoLocation;

    // untuk membedakan bo cash dan kredit
    private int refBoTransacTionType;

    // untuk menyimpan data kontak
    private long contactOid;


    public long getRefSubBoDocOid() {
        return refSubBoDocOid;
    }

    public void setRefSubBoDocOid(long refSubBoDocOid) {
        this.refSubBoDocOid = refSubBoDocOid;
    }

    public long getContactOid() {
        return contactOid;
    }

    public void setContactOid(long contactOid) {
        this.contactOid = contactOid;
    }

    public int getRefBoTransacTionType() {
        return refBoTransacTionType;
    }

    public void setRefBoTransacTionType(int refBoTransacTionType) {
        this.refBoTransacTionType = refBoTransacTionType;
    }

    /** Creates new Journal */
    public IjJournalMain() {
    }

    /** Getter for property jurnalUser.
     * @return Value of property jurnalUser.
     *
     */
    public long getJurUser() {
        return this.jurUser;
    }

    /** Setter for property jurnalUser.
     * @param jurnalUser New value of property jurnalUser.
     *
     */
    public void setJurUser(long jurUser) {
        this.jurUser = jurUser;
    }

    /** Getter for property jurnalDtTransaction.
     * @return Value of property jurnalDtTransaction.
     *
     */
    public Date getJurTransDate() {
        return this.jurTransDate;
    }

    /** Setter for property jurnalDtTransaction.
     * @param jurnalDtTransaction New value of property jurnalDtTransaction.
     *
     */
    public void setJurTransDate(Date jurTransDate) {
        this.jurTransDate = jurTransDate;
    }

    /** Getter for property jurPeriod.
     * @return Value of property jurPeriod.
     *
     */
    public long getJurPeriod() {
        return this.jurPeriod;
    }

    /** Setter for property jurPeriod.
     * @param jurPeriod New value of property jurPeriod.
     *
     */
    public void setJurPeriod(long jurPeriod) {
        this.jurPeriod = jurPeriod;
    }

    /** Getter for property jurEntryDate.
     * @return Value of property jurEntryDate.
     *
     */
    public Date getJurEntryDate() {
        return this.jurEntryDate;
    }

    /** Setter for property jurEntryDate.
     * @param jurEntryDate New value of property jurEntryDate.
     *
     */
    public void setJurEntryDate(Date jurEntryDate) {
        this.jurEntryDate = jurEntryDate;
    }

    /** Getter for property jurDesc.
     * @return Value of property jurDesc.
     *
     */
    public String getJurDesc() {
        return this.jurDesc;
    }

    /** Setter for property jurDesc.
     * @param jurDesc New value of property jurDesc.
     *
     */
    public void setJurDesc(String jurDesc) {
        this.jurDesc = jurDesc;
    }

    /** Getter for property jurTransCurrency.
     * @return Value of property jurTransCurrency.
     *
     */
    public long getJurTransCurrency() {
        return this.jurTransCurrency;
    }

    /** Setter for property jurTransCurrency.
     * @param jurTransCurrency New value of property jurTransCurrency.
     *
     */
    public void setJurTransCurrency(long jurTransCurrency) {
        this.jurTransCurrency = jurTransCurrency;
    }

    /** Getter for property listOfDetails.
     * @return Value of property listOfDetails.
     *
     */
    public Vector getListOfDetails() {
        return this.listOfDetails;
    }

    /** Setter for property listOfDetails.
     * @param listOfDetails New value of property listOfDetails.
     *
     */
    public void setListOfDetails(Vector listOfDetails) {
        this.listOfDetails = listOfDetails;
    }

    /** Getter for property jurBookType.
     * @return Value of property jurBookType.
     */
    public long getJurBookType() {
        return jurBookType;
    }

    /** Setter for property jurBookType.
     * @param jurBookType New value of property jurBookType.
     */
    public void setJurBookType(long jurBookType) {
        this.jurBookType = jurBookType;
    }

    /** Getter for property jurStatus.
     * @return Value of property jurStatus.
     */
    public int getJurStatus() {
        return jurStatus;
    }

    /** Setter for property jurStatus.
     * @param jurStatus New value of property jurStatus.
     */
    public void setJurStatus(int jurStatus) {
        this.jurStatus = jurStatus;
    }

    /** Getter for property refBoDocType.
     * @return Value of property refBoDocType.
     *
     */
    public int getRefBoDocType() {
        return this.refBoDocType;
    }

    /** Setter for property refBoDocType.
     * @param refBoDocType New value of property refBoDocType.
     *
     */
    public void setRefBoDocType(int refBoDocType) {
        this.refBoDocType = refBoDocType;
    }

    /** Getter for property refBoDocOid.
     * @return Value of property refBoDocOid.
     *
     */
    public long getRefBoDocOid() {
        return this.refBoDocOid;
    }

    /** Setter for property refBoDocOid.
     * @param refBoDocOid New value of property refBoDocOid.
     *
     */
    public void setRefBoDocOid(long refBoDocOid) {
        this.refBoDocOid = refBoDocOid;
    }

    /** Getter for property refBoDocNumber.
     * @return Value of property refBoDocNumber.
     *
     */
    public String getRefBoDocNumber() {
        return this.refBoDocNumber;
    }

    /** Setter for property refBoDocNumber.
     * @param refBoDocNumber New value of property refBoDocNumber.
     *
     */
    public void setRefBoDocNumber(String refBoDocNumber) {
        this.refBoDocNumber = refBoDocNumber;
    }

    /** Getter for property refAisoOid.
     * @return Value of property refAisoOid.
     *
     */
    public long getRefAisoJournalOid() {
        return this.refAisoJournalOid;
    }

    /** Setter for property refAisoOid.
     * @param refAisoOid New value of property refAisoOid.
     *
     */
    public void setRefAisoJournalOid(long refAisoJournalOid) {
        this.refAisoJournalOid = refAisoJournalOid;
    }

    /** Getter for property refBoSystem.
     * @return Value of property refBoSystem.
     *
     */
    public int getRefBoSystem() {
        return this.refBoSystem;
    }

    /** Setter for property refBoSystem.
     * @param refBoSystem New value of property refBoSystem.
     *
     */
    public void setRefBoSystem(int refBoSystem) {
        this.refBoSystem = refBoSystem;
    }

    /** Getter for property refBoDocLastUpdate.
     * @return Value of property refBoDocLastUpdate.
     *
     */
    public Date getRefBoDocLastUpdate() {
        return this.refBoDocLastUpdate;
    }

    /** Setter for property refBoDocLastUpdate.
     * @param refBoDocLastUpdate New value of property refBoDocLastUpdate.
     *
     */
    public void setRefBoDocLastUpdate(Date refBoDocLastUpdate) {
        this.refBoDocLastUpdate = refBoDocLastUpdate;
    }

    /** Getter for property refBoLocation.
     * @return Value of property refBoLocation.
     */
    public long getRefBoLocation() {
        return refBoLocation;
    }

    /** Setter for property refBoLocation.
     * @param refBoLocation New value of property refBoLocation.
     */
    public void setRefBoLocation(long refBoLocation) {
        this.refBoLocation = refBoLocation;
    }

}
