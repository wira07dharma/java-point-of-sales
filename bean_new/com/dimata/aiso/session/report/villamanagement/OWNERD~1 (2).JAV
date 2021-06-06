/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dimata.aiso.session.report.villamanagement;

import java.util.Vector;
import com.dimata.aiso.entity.periode.Periode;
import com.dimata.aiso.entity.periode.PstPeriode;
import com.dimata.aiso.entity.masterdata.Perkiraan;
import com.dimata.aiso.entity.masterdata.BussinessCenter;
import com.dimata.aiso.entity.masterdata.PstBussinessCenter;
import com.dimata.aiso.entity.masterdata.PstPerkiraan;
import com.dimata.aiso.entity.masterdata.PstBussCenterBudget;
import com.dimata.aiso.db.*;
import com.dimata.aiso.entity.report.PstReportOwnerDistribution;
import com.dimata.aiso.entity.report.ReportOwnerDistribution;
import com.dimata.aiso.entity.jurnal.JournalDistribution;
import java.sql.ResultSet;

/**
 *
 * @author Kartika Tanjana
 * @note class yang akan menentukan format report
 */
public class OwnerDistributionReport {

    private Periode currentPeriod = new Periode();
    private Periode prev3Period = new Periode();
    private Periode prev2Period = new Periode();
    private Periode prev1Period = new Periode();
    private BussinessCenter bussCenter = null;
    private ReportOwnerDistribution currPerBalnc = null;
    private ReportOwnerDistribution prev1PerBalnc = null;
    private ReportOwnerDistribution prev2PerBalnc = null;
    private ReportOwnerDistribution prev3PerBalnc = null;
    private Vector clientAdvance = new Vector();
    private OwnDisRepAccount sumClientAdvance = null;
    private Vector villaRevenue = new Vector();
    private OwnDisRepAccount sumVillaRevenue = null;
    private Vector managementExpense = new Vector();
    private OwnDisRepAccount sumManagementExpense = null;
    private Vector varDirectOpExpense = new Vector();
    private OwnDisRepAccount sumVarDirectOpExpense = null;
    private Vector fixDirectOpExpense = new Vector();
    private OwnDisRepAccount sumFixDirectOpExpense = null;
    private Vector indirectOpExpense = new Vector();
    private OwnDisRepAccount sumIndirectOpExpense = null;
    private Vector otherVillaOpExpense = new Vector();
    private OwnDisRepAccount sumOtherVillaOpExpense = null;
    private Vector commisionAndFee = new Vector();
    private OwnDisRepAccount sumCommisionAndFee = null;
    private Vector commonArea = new Vector();
    public static final String GROUP_ACC_COMMON_AREA = "COMMON AREA";
    private OwnDisRepAccount sumCommonArea = null;
    private Vector clientContribution = new Vector();
    private OwnDisRepAccount sumClientContribution = null;
    private OwnDisRepAccount threeMonthsOpProjection = null;
    private double procentOpProject = 0.4;
    private String[] accExpense;

    public OwnerDistributionReport(long bussCenterOID, long periodOID) {
        try {
            threeMonthsOpProjection = new OwnDisRepAccount();
            threeMonthsOpProjection.setSaldoType(OwnDisRepAccount.SALDO_TYPE_DEBT);
            this.bussCenter = PstBussinessCenter.fetchExc(bussCenterOID);
            this.setCurrentPeriodByOid(periodOID, bussCenterOID);
        } catch (Exception exc) {
            System.out.println("EXC>> OwnerDistributionReport(long bussCenterOID, long periodOID)" + exc);
        }
    }

    public int loadReportByAccounts(String clientAdvance[], String villaRevenue[], String managementExpense[], String varDirectOpExpense[],
            String fixDirectOpExpense[], String indirectOpExpense[], String otherVillaOpExpense[], String commisionAndFee[],
            String commonArea[], String clientContribution[]) {

        this.setClientAdvanceAcc(clientAdvance);
        this.setVillaRevenue(villaRevenue);
        this.setManagementExpense(managementExpense);
        this.setVarDirectOpExpense(varDirectOpExpense);
        this.setFixDirectOpExpense(fixDirectOpExpense);
        this.setIndirectOpExpense(indirectOpExpense);
        this.setOtherVillaOpExpense(otherVillaOpExpense);
        this.setCommisionAndFee(commisionAndFee);
        this.setCommonArea(commonArea);
        this.setClientContribution(clientContribution);
        this.sumOtherOpExpense();
        this.accExpense = concatStringArrray(managementExpense, varDirectOpExpense);
        this.accExpense = concatStringArrray(this.accExpense, fixDirectOpExpense);
        this.accExpense = concatStringArrray(this.accExpense, indirectOpExpense);
        this.accExpense = concatStringArrray(this.accExpense, otherVillaOpExpense);
        this.accExpense = concatStringArrray(this.accExpense, commisionAndFee);
        this.accExpense = concatStringArrray(this.accExpense, commonArea);
        this.accExpense = concatStringArrray(this.accExpense, clientContribution);

        double expBudgetThisPer = currentPeriod == null ? 0.0 : PstBussCenterBudget.sumBussCenterBudget(getBussCenter().getOID(), currentPeriod.getOID(), this.accExpense);
        double expBudgetPrev1Per = currentPeriod == null ? 0.0 : PstBussCenterBudget.sumBussCenterBudget(getBussCenter().getOID(), prev1Period.getOID(), this.accExpense);
        double expBudgetPrev2Per = currentPeriod == null ? 0.0 : PstBussCenterBudget.sumBussCenterBudget(getBussCenter().getOID(), prev2Period.getOID(), this.accExpense);
        double expBudgetPrev3Per = currentPeriod == null ? 0.0 : PstBussCenterBudget.sumBussCenterBudget(getBussCenter().getOID(), prev3Period.getOID(), this.accExpense);

        threeMonthsOpProjection.setValThisPeriodDebt(3 * expBudgetThisPer); // 3* => cause three months 
        threeMonthsOpProjection.setValPrev1PeriodDebt(3 * expBudgetPrev1Per);
        threeMonthsOpProjection.setValPrev2PeriodDebt(3 * expBudgetPrev2Per);
        threeMonthsOpProjection.setValPrev3PeriodDebt(3 * expBudgetPrev3Per);

        return 0;
    }

    private String[] concatStringArrray(String[] A, String[] B) {
        String[] C = new String[A.length + B.length];
        System.arraycopy(A, 0, C, 0, A.length);
        System.arraycopy(B, 0, C, A.length, B.length);
        return C;
    }

    public OwnDisRepAccount getSumVillaExpenses() {
        OwnDisRepAccount sumAcc = new OwnDisRepAccount();

        if (sumVarDirectOpExpense != null) {
            OwnDisRepAccount acc = sumVarDirectOpExpense;
            sumAcc.setValPrev3PeriodDebt(sumAcc.getValPrev3PeriodDebt() + acc.getValPrev3PeriodDebt());
            sumAcc.setValPrev2PeriodDebt(sumAcc.getValPrev2PeriodDebt() + acc.getValPrev2PeriodDebt());
            sumAcc.setValPrev1PeriodDebt(sumAcc.getValPrev1PeriodDebt() + acc.getValPrev1PeriodDebt());
            sumAcc.setValThisPeriodDebt(sumAcc.getValThisPeriodDebt() + acc.getValThisPeriodDebt());

            sumAcc.setValPrev3PeriodCredit(sumAcc.getValPrev3PeriodCredit() + acc.getValPrev3PeriodCredit());
            sumAcc.setValPrev2PeriodCredit(sumAcc.getValPrev2PeriodCredit() + acc.getValPrev2PeriodCredit());
            sumAcc.setValPrev1PeriodCredit(sumAcc.getValPrev1PeriodCredit() + acc.getValPrev1PeriodCredit());
            sumAcc.setValThisPeriodCredit(sumAcc.getValThisPeriodCredit() + acc.getValThisPeriodCredit());
        }

        if (sumFixDirectOpExpense != null) {
            OwnDisRepAccount acc = sumFixDirectOpExpense;
            sumAcc.setValPrev3PeriodDebt(sumAcc.getValPrev3PeriodDebt() + acc.getValPrev3PeriodDebt());
            sumAcc.setValPrev2PeriodDebt(sumAcc.getValPrev2PeriodDebt() + acc.getValPrev2PeriodDebt());
            sumAcc.setValPrev1PeriodDebt(sumAcc.getValPrev1PeriodDebt() + acc.getValPrev1PeriodDebt());
            sumAcc.setValThisPeriodDebt(sumAcc.getValThisPeriodDebt() + acc.getValThisPeriodDebt());

            sumAcc.setValPrev3PeriodCredit(sumAcc.getValPrev3PeriodCredit() + acc.getValPrev3PeriodCredit());
            sumAcc.setValPrev2PeriodCredit(sumAcc.getValPrev2PeriodCredit() + acc.getValPrev2PeriodCredit());
            sumAcc.setValPrev1PeriodCredit(sumAcc.getValPrev1PeriodCredit() + acc.getValPrev1PeriodCredit());
            sumAcc.setValThisPeriodCredit(sumAcc.getValThisPeriodCredit() + acc.getValThisPeriodCredit());
        }

        if (sumIndirectOpExpense != null) {
            OwnDisRepAccount acc = sumIndirectOpExpense;
            sumAcc.setValPrev3PeriodDebt(sumAcc.getValPrev3PeriodDebt() + acc.getValPrev3PeriodDebt());
            sumAcc.setValPrev2PeriodDebt(sumAcc.getValPrev2PeriodDebt() + acc.getValPrev2PeriodDebt());
            sumAcc.setValPrev1PeriodDebt(sumAcc.getValPrev1PeriodDebt() + acc.getValPrev1PeriodDebt());
            sumAcc.setValThisPeriodDebt(sumAcc.getValThisPeriodDebt() + acc.getValThisPeriodDebt());

            sumAcc.setValPrev3PeriodCredit(sumAcc.getValPrev3PeriodCredit() + acc.getValPrev3PeriodCredit());
            sumAcc.setValPrev2PeriodCredit(sumAcc.getValPrev2PeriodCredit() + acc.getValPrev2PeriodCredit());
            sumAcc.setValPrev1PeriodCredit(sumAcc.getValPrev1PeriodCredit() + acc.getValPrev1PeriodCredit());
            sumAcc.setValThisPeriodCredit(sumAcc.getValThisPeriodCredit() + acc.getValThisPeriodCredit());
        }

        if (sumCommisionAndFee != null) {
            OwnDisRepAccount acc = sumCommisionAndFee;
            sumAcc.setValPrev3PeriodDebt(sumAcc.getValPrev3PeriodDebt() + acc.getValPrev3PeriodDebt());
            sumAcc.setValPrev2PeriodDebt(sumAcc.getValPrev2PeriodDebt() + acc.getValPrev2PeriodDebt());
            sumAcc.setValPrev1PeriodDebt(sumAcc.getValPrev1PeriodDebt() + acc.getValPrev1PeriodDebt());
            sumAcc.setValThisPeriodDebt(sumAcc.getValThisPeriodDebt() + acc.getValThisPeriodDebt());

            sumAcc.setValPrev3PeriodCredit(sumAcc.getValPrev3PeriodCredit() + acc.getValPrev3PeriodCredit());
            sumAcc.setValPrev2PeriodCredit(sumAcc.getValPrev2PeriodCredit() + acc.getValPrev2PeriodCredit());
            sumAcc.setValPrev1PeriodCredit(sumAcc.getValPrev1PeriodCredit() + acc.getValPrev1PeriodCredit());
            sumAcc.setValThisPeriodCredit(sumAcc.getValThisPeriodCredit() + acc.getValThisPeriodCredit());
        }

        if (sumCommonArea != null) {
            OwnDisRepAccount acc = sumCommonArea;
            sumAcc.setValPrev3PeriodDebt(sumAcc.getValPrev3PeriodDebt() + acc.getValPrev3PeriodDebt());
            sumAcc.setValPrev2PeriodDebt(sumAcc.getValPrev2PeriodDebt() + acc.getValPrev2PeriodDebt());
            sumAcc.setValPrev1PeriodDebt(sumAcc.getValPrev1PeriodDebt() + acc.getValPrev1PeriodDebt());
            sumAcc.setValThisPeriodDebt(sumAcc.getValThisPeriodDebt() + acc.getValThisPeriodDebt());

            sumAcc.setValPrev3PeriodCredit(sumAcc.getValPrev3PeriodCredit() + acc.getValPrev3PeriodCredit());
            sumAcc.setValPrev2PeriodCredit(sumAcc.getValPrev2PeriodCredit() + acc.getValPrev2PeriodCredit());
            sumAcc.setValPrev1PeriodCredit(sumAcc.getValPrev1PeriodCredit() + acc.getValPrev1PeriodCredit());
            sumAcc.setValThisPeriodCredit(sumAcc.getValThisPeriodCredit() + acc.getValThisPeriodCredit());
        }

        return sumAcc;

    }

    public OwnDisRepAccount getBalanceRevDueToOwner() {
        OwnDisRepAccount balAcc = getVillaGrossRevenue();

        OwnDisRepAccount villaRev = getVillaGrossRevenue();
        if (villaRev == null) {
            villaRev = new OwnDisRepAccount();
        }

        OwnDisRepAccount villaExpense = getSumVillaExpenses();
        if (villaExpense == null) {
            villaExpense = new OwnDisRepAccount();
        }

        balAcc.setSaldoType(OwnDisRepAccount.SALDO_TYPE_DEBT);

        balAcc.setValPrev3PeriodDebt(villaRev.getValPrev3PeriodCredit() - villaRev.getValPrev3PeriodDebt());
        balAcc.setValPrev2PeriodDebt(villaRev.getValPrev2PeriodCredit() - villaRev.getValPrev2PeriodDebt());
        balAcc.setValPrev1PeriodDebt(villaRev.getValPrev1PeriodCredit() - villaRev.getValPrev1PeriodDebt());
        balAcc.setValThisPeriodDebt(villaRev.getValThisPeriodCredit() - villaRev.getValThisPeriodDebt());

        balAcc.setValPrev3PeriodCredit(villaExpense.getValPrev3PeriodDebt() - villaExpense.getValPrev3PeriodCredit());
        balAcc.setValPrev2PeriodCredit(villaExpense.getValPrev2PeriodDebt() - villaExpense.getValPrev2PeriodCredit());
        balAcc.setValPrev1PeriodCredit(villaExpense.getValPrev1PeriodDebt() - villaExpense.getValPrev1PeriodCredit());
        balAcc.setValThisPeriodCredit(villaExpense.getValThisPeriodDebt() - villaExpense.getValThisPeriodCredit());

        return balAcc;
    }

    public OwnDisRepAccount getBalance3MonthOpExp() {
        OwnDisRepAccount bal3Month = new OwnDisRepAccount();
        bal3Month.setSaldoType(OwnDisRepAccount.SALDO_TYPE_DEBT);

        /*
        OwnDisRepAccount balRev = this.getBalanceRevDueToOwner();        
        if(balRev!=null){
        if(balRev.getPrev3PeriodSaldo()>=0){
        bal3Month = this.getBalanceAdvancePayment();
        } else{
        OwnDisRepAccount balAdvPay =  this.getBalanceAdvancePayment();
        OwnDisRepAccount sumVillaExp = this.getSumVillaExpenses();
        bal3Month.setValPrev3PeriodDebt(balAdvPay.getPrev3PeriodSaldo()-sumVillaExp.getPrev3PeriodSaldo());
        }
        if(balRev.getPrev2PeriodSaldo()>=0){
        bal3Month = this.getBalanceAdvancePayment();
        } else{
        OwnDisRepAccount balAdvPay =  this.getBalanceAdvancePayment();
        OwnDisRepAccount sumVillaExp = this.getSumVillaExpenses();
        bal3Month.setValPrev2PeriodDebt(balAdvPay.getPrev2PeriodSaldo()-sumVillaExp.getPrev2PeriodSaldo());
        }
        if(balRev.getPrev1PeriodSaldo()>=0){
        bal3Month = this.getBalanceAdvancePayment();
        } else{
        OwnDisRepAccount balAdvPay =  this.getBalanceAdvancePayment();
        OwnDisRepAccount sumVillaExp = this.getSumVillaExpenses();
        bal3Month.setValPrev1PeriodDebt(balAdvPay.getPrev1PeriodSaldo()-sumVillaExp.getPrev1PeriodSaldo());
        }
        if(balRev.getThisPeriodSaldo()>=0){
        bal3Month = this.getBalanceAdvancePayment();
        } else{
        OwnDisRepAccount balAdvPay =  this.getBalanceAdvancePayment();
        OwnDisRepAccount sumVillaExp = this.getSumVillaExpenses();
        bal3Month.setValThisPeriodDebt(balAdvPay.getThisPeriodSaldo()-sumVillaExp.getThisPeriodSaldo());
        }
        }
         * */

        OwnDisRepAccount balAdvPay = this.getBalanceAdvancePayment();
        OwnDisRepAccount balFundToOwn = this.getBalanceFundToOwner();

        bal3Month.setValPrev3PeriodDebt(balAdvPay.getPrev3PeriodSaldo() + balFundToOwn.getPrev3PeriodSaldo());
        bal3Month.setValPrev2PeriodDebt(balAdvPay.getPrev2PeriodSaldo() + balFundToOwn.getPrev2PeriodSaldo());
        bal3Month.setValPrev1PeriodDebt(balAdvPay.getPrev1PeriodSaldo() + balFundToOwn.getPrev1PeriodSaldo());
        bal3Month.setValThisPeriodDebt(balAdvPay.getThisPeriodSaldo() + balFundToOwn.getThisPeriodSaldo());

        return bal3Month;
    }

    /***
     *  menambahkan summary other expense dengan Client COntribution dan Commision and fee
     */
    private void sumOtherOpExpense() {
        if (this.otherVillaOpExpense != null && otherVillaOpExpense.size() > 0) {
            OwnDisRepAccount sumAcc = (OwnDisRepAccount) otherVillaOpExpense.get(0);
            if (sumAcc != null && sumAcc.getAccount() != null && sumAcc.getAccount().getPostable() == PstPerkiraan.ACC_NOTPOSTED) {
                // add commision and fee to sum
                if (commisionAndFee != null && commisionAndFee.size() > 0 && ((OwnDisRepAccount) commisionAndFee.get(0) != null) &&
                        (((OwnDisRepAccount) commisionAndFee.get(0)).getAccount() != null)) {
                    OwnDisRepAccount acc = (OwnDisRepAccount) commisionAndFee.get(0);
                    if (acc.getAccount().getPostable() == PstPerkiraan.ACC_NOTPOSTED) {
                        sumAcc.setValPrev3PeriodDebt(sumAcc.getValPrev3PeriodDebt() + acc.getValPrev3PeriodDebt());
                        sumAcc.setValPrev2PeriodDebt(sumAcc.getValPrev2PeriodDebt() + acc.getValPrev2PeriodDebt());
                        sumAcc.setValPrev1PeriodDebt(sumAcc.getValPrev1PeriodDebt() + acc.getValPrev1PeriodDebt());
                        sumAcc.setValThisPeriodDebt(sumAcc.getValThisPeriodDebt() + acc.getValThisPeriodDebt());

                    /* sumAcc.setValPrev3PeriodCredit(sumAcc.getValPrev3PeriodCredit() + acc.getValPrev3PeriodCredit());
                    sumAcc.setValPrev2PeriodCredit(sumAcc.getValPrev2PeriodCredit() + acc.getValPrev2PeriodCredit());
                    sumAcc.setValPrev1PeriodCredit(sumAcc.getValPrev1PeriodCredit() + acc.getValPrev1PeriodCredit());
                    sumAcc.setValThisPeriodCredit(sumAcc.getValThisPeriodCredit() + acc.getValThisPeriodCredit());
                     * */
                    } else {
                        for (int rc = 0; rc < commisionAndFee.size(); rc++) {
                            acc = (OwnDisRepAccount) commisionAndFee.get(rc);
                            if (acc.getAccount().getPostable() == PstPerkiraan.ACC_POSTED) {
                                sumAcc.setValPrev3PeriodDebt(sumAcc.getValPrev3PeriodDebt() + acc.getValPrev3PeriodDebt());
                                sumAcc.setValPrev2PeriodDebt(sumAcc.getValPrev2PeriodDebt() + acc.getValPrev2PeriodDebt());
                                sumAcc.setValPrev1PeriodDebt(sumAcc.getValPrev1PeriodDebt() + acc.getValPrev1PeriodDebt());
                                sumAcc.setValThisPeriodDebt(sumAcc.getValThisPeriodDebt() + acc.getValThisPeriodDebt());

                            //sumAcc.setValPrev3PeriodCredit(sumAcc.getValPrev3PeriodCredit() + acc.getValPrev3PeriodCredit());
                            //sumAcc.setValPrev2PeriodCredit(sumAcc.getValPrev2PeriodCredit() + acc.getValPrev2PeriodCredit());
                            //sumAcc.setValPrev1PeriodCredit(sumAcc.getValPrev1PeriodCredit() + acc.getValPrev1PeriodCredit());
                            //sumAcc.setValThisPeriodCredit(sumAcc.getValThisPeriodCredit() + acc.getValThisPeriodCredit());
                            }
                        }
                    }
                }

                if (clientContribution != null && clientContribution.size() > 0 && ((OwnDisRepAccount) clientContribution.get(0) != null) &&
                        (((OwnDisRepAccount) clientContribution.get(0)).getAccount() != null)) {
                    OwnDisRepAccount acc = (OwnDisRepAccount) clientContribution.get(0);
                    if (acc.getAccount().getPostable() == PstPerkiraan.ACC_NOTPOSTED) {
                        sumAcc.setValPrev3PeriodDebt(sumAcc.getValPrev3PeriodDebt() + acc.getValPrev3PeriodDebt());
                        sumAcc.setValPrev2PeriodDebt(sumAcc.getValPrev2PeriodDebt() + acc.getValPrev2PeriodDebt());
                        sumAcc.setValPrev1PeriodDebt(sumAcc.getValPrev1PeriodDebt() + acc.getValPrev1PeriodDebt());
                        sumAcc.setValThisPeriodDebt(sumAcc.getValThisPeriodDebt() + acc.getValThisPeriodDebt());

                    /*sumAcc.setValPrev3PeriodCredit(sumAcc.getValPrev3PeriodCredit() + acc.getValPrev3PeriodCredit());
                    sumAcc.setValPrev2PeriodCredit(sumAcc.getValPrev2PeriodCredit() + acc.getValPrev2PeriodCredit());
                    sumAcc.setValPrev1PeriodCredit(sumAcc.getValPrev1PeriodCredit() + acc.getValPrev1PeriodCredit());
                    sumAcc.setValThisPeriodCredit(sumAcc.getValThisPeriodCredit() + acc.getValThisPeriodCredit());
                     * */
                    } else {
                        for (int rc = 0; rc < clientContribution.size(); rc++) {
                            acc = (OwnDisRepAccount) clientContribution.get(rc);
                            if (acc.getAccount().getPostable() == PstPerkiraan.ACC_POSTED) {
                                sumAcc.setValPrev3PeriodDebt(sumAcc.getValPrev3PeriodDebt() + acc.getValPrev3PeriodDebt());
                                sumAcc.setValPrev2PeriodDebt(sumAcc.getValPrev2PeriodDebt() + acc.getValPrev2PeriodDebt());
                                sumAcc.setValPrev1PeriodDebt(sumAcc.getValPrev1PeriodDebt() + acc.getValPrev1PeriodDebt());
                                sumAcc.setValThisPeriodDebt(sumAcc.getValThisPeriodDebt() + acc.getValThisPeriodDebt());

                            /*   sumAcc.setValPrev3PeriodCredit(sumAcc.getValPrev3PeriodCredit() + acc.getValPrev3PeriodCredit());
                            sumAcc.setValPrev2PeriodCredit(sumAcc.getValPrev2PeriodCredit() + acc.getValPrev2PeriodCredit());
                            sumAcc.setValPrev1PeriodCredit(sumAcc.getValPrev1PeriodCredit() + acc.getValPrev1PeriodCredit());
                            sumAcc.setValThisPeriodCredit(sumAcc.getValThisPeriodCredit() + acc.getValThisPeriodCredit());
                             * */
                            }
                        }
                    }
                }
                sumOtherVillaOpExpense = sumAcc;
            }
        }
    }

    public OwnDisRepAccount getClientAdvance(int idx) {
        if ((clientAdvance != null) && (clientAdvance.size() > 0) && (clientAdvance.size() > idx)) {
            return (OwnDisRepAccount) clientAdvance.get(idx);
        }
        return null;
    }

    public int getClientAdvanceSize() {
        if (clientAdvance != null) {
            return clientAdvance.size();
        }
        return 0;
    }

    private void setClientAdvanceAcc(String accNumbers[]) {
        Vector v = this.setAccountOnReport(accNumbers, OwnDisRepAccount.SALDO_TYPE_CREDIT);
        if (v != null) {
            if (v.size() > 0) {
                this.clientAdvance = (Vector) v.get(0);
            }
            if (v.size() > 1) {
                this.sumClientAdvance = (OwnDisRepAccount) v.get(1);
            }
        }
    }

    public OwnDisRepAccount getBalanceAdvancePayment() {
        OwnDisRepAccount bal = new OwnDisRepAccount();
        bal.setSaldoType(OwnDisRepAccount.SALDO_TYPE_CREDIT);
        bal.setValPrev2PeriodCredit((this.getPrev3PerBalnc() == null ? 0 : this.getPrev3PerBalnc().getAdvance_pay_balance()) + (this.getSumClientAdvance() == null ? 0 : this.getSumClientAdvance().getPrev2PeriodSaldo()));
        bal.setValPrev1PeriodCredit((this.getPrev2PerBalnc() == null ? 0 : this.getPrev2PerBalnc().getAdvance_pay_balance()) + (this.getSumClientAdvance() == null ? 0 : this.getSumClientAdvance().getPrev1PeriodSaldo()));
        bal.setValThisPeriodCredit((this.getPrev1PerBalnc() == null ? 0 : this.getPrev1PerBalnc().getAdvance_pay_balance()) + (this.getSumClientAdvance() == null ? 0 : this.getSumClientAdvance().getThisPeriodSaldo()));
        return bal;
    }

    public int getVillaRevenueSize() {
        if (villaRevenue != null) {
            return villaRevenue.size();
        }
        return 0;
    }

    public OwnDisRepAccount getVillaRevenue(int idx) {
        if ((villaRevenue != null) && (villaRevenue.size() > 0) && (villaRevenue.size() > idx)) {
            return (OwnDisRepAccount) villaRevenue.get(idx);
        }
        return null;
    }

    private void setVillaRevenue(String accNumbers[]) {
        Vector v = this.setAccountOnReport(accNumbers, OwnDisRepAccount.SALDO_TYPE_CREDIT);
        if (v != null) {
            if (v.size() > 0) {
                this.villaRevenue = (Vector) v.get(0);
            }
            if (v.size() > 1) {
                this.sumVillaRevenue = (OwnDisRepAccount) v.get(1);
            }
        }
    }

    public int getManagementExpenseSize() {
        if (managementExpense != null) {
            return managementExpense.size();
        }
        return 0;
    }

    public OwnDisRepAccount getManagementExpense(int idx) {
        if ((managementExpense != null) && (managementExpense.size() > 0) && (managementExpense.size() > idx)) {
            return (OwnDisRepAccount) managementExpense.get(idx);
        }
        return null;
    }

    private void setManagementExpense(String accNumbers[]) {
        Vector v = this.setAccountOnReport(accNumbers, OwnDisRepAccount.SALDO_TYPE_CREDIT);
        if (v != null) {
            if (v.size() > 0) {
                this.managementExpense = (Vector) v.get(0);
            }
            if (v.size() > 1) {
                this.sumManagementExpense = (OwnDisRepAccount) v.get(1);
            }
        }
    }

    public OwnDisRepAccount getVillaGrossRevenue() {
        OwnDisRepAccount vGrossRev = new OwnDisRepAccount();
        vGrossRev.setSaldoType(OwnDisRepAccount.SALDO_TYPE_CREDIT);

        vGrossRev.setValPrev3PeriodCredit(this.getSumVillaRevenue().getPrev3PeriodSaldo() - this.getSumManagementExpense().getPrev3PeriodSaldo());
        vGrossRev.setValPrev2PeriodCredit(this.getSumVillaRevenue().getPrev2PeriodSaldo() - this.getSumManagementExpense().getPrev2PeriodSaldo());
        vGrossRev.setValPrev1PeriodCredit(this.getSumVillaRevenue().getPrev1PeriodSaldo() - this.getSumManagementExpense().getPrev1PeriodSaldo());
        vGrossRev.setValThisPeriodCredit(this.getSumVillaRevenue().getThisPeriodSaldo() - this.getSumManagementExpense().getThisPeriodSaldo());


        return vGrossRev;

    }

    public Vector getVarDirectOpExpense() {
        return varDirectOpExpense;
    }

    private void setVarDirectOpExpense(String accNumbers[]) {
        Vector v = this.setAccountOnReport(accNumbers, OwnDisRepAccount.SALDO_TYPE_DEBT);
        if (v != null) {
            if (v.size() > 0) {
                this.varDirectOpExpense = (Vector) v.get(0);
            }
            if (v.size() > 1) {
                this.sumVarDirectOpExpense = (OwnDisRepAccount) v.get(1);
                if (this.varDirectOpExpense != null && this.varDirectOpExpense.size() > 0) {
                    OwnDisRepAccount firstAcc = (OwnDisRepAccount) this.varDirectOpExpense.get(0); // get the top of account
                    if (firstAcc != null && (firstAcc.getAccount().getPostable() == PstPerkiraan.ACC_NOTPOSTED)) {
                        this.sumVarDirectOpExpense.setAccount(firstAcc.getAccount());
                        this.varDirectOpExpense.set(0, this.sumVarDirectOpExpense);

                    }
                }
            }
        }
    }

    public Vector getFixDirectOpExpense() {
        return fixDirectOpExpense;
    }

    private void setFixDirectOpExpense(String accNumbers[]) {
        Vector v = this.setAccountOnReport(accNumbers, OwnDisRepAccount.SALDO_TYPE_DEBT);
        if (v != null) {
            if (v.size() > 0) {
                this.fixDirectOpExpense = (Vector) v.get(0);
            }
            if (v.size() > 1) {
                this.sumFixDirectOpExpense = (OwnDisRepAccount) v.get(1);
                if (this.sumFixDirectOpExpense != null && this.fixDirectOpExpense.size() > 0) {
                    OwnDisRepAccount firstAcc = (OwnDisRepAccount) this.fixDirectOpExpense.get(0); // get the top of account
                    if (firstAcc != null && (firstAcc.getAccount().getPostable() == PstPerkiraan.ACC_NOTPOSTED)) {
                        this.sumFixDirectOpExpense.setAccount(firstAcc.getAccount());
                        this.fixDirectOpExpense.set(0, this.sumFixDirectOpExpense);
                    }
                }
            }
        }
    }

    public Vector getIndirectOpExpense() {
        return indirectOpExpense;
    }

    private void setIndirectOpExpense(String accNumbers[]) {
        Vector v = this.setAccountOnReport(accNumbers, OwnDisRepAccount.SALDO_TYPE_DEBT);
        if (v != null) {
            if (v.size() > 0) {
                this.indirectOpExpense = (Vector) v.get(0);
            }
            if (v.size() > 1) {
                this.sumIndirectOpExpense = (OwnDisRepAccount) v.get(1);
                if (this.sumIndirectOpExpense != null && this.indirectOpExpense.size() > 0) {
                    OwnDisRepAccount firstAcc = (OwnDisRepAccount) this.indirectOpExpense.get(0); // get the top of account
                    if (firstAcc != null && (firstAcc.getAccount().getPostable() == PstPerkiraan.ACC_NOTPOSTED)) {
                        this.sumIndirectOpExpense.setAccount(firstAcc.getAccount());
                        this.indirectOpExpense.set(0, this.sumIndirectOpExpense);
                    }
                }
            }
        }
    }

    public Vector getOtherVillaOpExpense() {
        return otherVillaOpExpense;
    }

    private void setOtherVillaOpExpense(String accNumbers[]) {
        Vector v = this.setAccountOnReport(accNumbers, OwnDisRepAccount.SALDO_TYPE_DEBT);
        if (v != null) {
            if (v.size() > 0) {
                this.otherVillaOpExpense = (Vector) v.get(0);
            }
            if (v.size() > 1) {
                this.sumOtherVillaOpExpense = (OwnDisRepAccount) v.get(1);
                if (this.sumOtherVillaOpExpense != null && this.otherVillaOpExpense.size() > 0) {
                    OwnDisRepAccount firstAcc = (OwnDisRepAccount) this.otherVillaOpExpense.get(0); // get the top of account
                    if (firstAcc != null && (firstAcc.getAccount().getPostable() == PstPerkiraan.ACC_NOTPOSTED)) {
                        this.sumOtherVillaOpExpense.setAccount(firstAcc.getAccount());
                        this.otherVillaOpExpense.set(0, this.sumOtherVillaOpExpense);
                    }
                }
            }
        }
    }

    public Vector getCommisionAndFee() {
        return commisionAndFee;
    }

    private void setCommisionAndFee(String accNumbers[]) {
        Vector v = this.setAccountOnReport(accNumbers, OwnDisRepAccount.SALDO_TYPE_DEBT);
        if (v != null) {
            if (v.size() > 0) {
                this.commisionAndFee = (Vector) v.get(0);
            }
            if (v.size() > 1) {
                this.sumCommisionAndFee = (OwnDisRepAccount) v.get(1);
                if (this.sumCommisionAndFee != null && this.commisionAndFee.size() > 0) {
                    OwnDisRepAccount firstAcc = (OwnDisRepAccount) this.commisionAndFee.get(0); // get the top of account
                    if (firstAcc != null && (firstAcc.getAccount().getPostable() == PstPerkiraan.ACC_NOTPOSTED)) {
                        this.sumCommisionAndFee.setAccount(firstAcc.getAccount());
                        this.commisionAndFee.set(0, this.sumCommisionAndFee);
                    }
                }
            }
        }
    }

    public Vector getCommonArea() {
        return commonArea;
    }

    public void setCommonArea(String accNumbers[]) {
        Vector v = this.setAccountOnReport(accNumbers, OwnDisRepAccount.SALDO_TYPE_DEBT);
        if (v != null) {
            if (v.size() > 0) {
                this.commonArea = (Vector) v.get(0);
            }
            if (v.size() > 1) {
                this.sumCommonArea = (OwnDisRepAccount) v.get(1);
                if (this.sumCommonArea != null && this.commonArea.size() > 0) {
                    OwnDisRepAccount firstAcc = (OwnDisRepAccount) this.commonArea.get(0); // get the top of account
                    if (firstAcc != null && (firstAcc.getAccount().getPostable() == PstPerkiraan.ACC_NOTPOSTED)) {
                        this.sumCommonArea.setAccount(firstAcc.getAccount());
                        this.commonArea.set(0, this.sumCommonArea);
                    }
                }
            }
        }

    }

    public Vector getClientContribution() {
        return clientContribution;
    }

    private void setClientContribution(String accNumbers[]) {
        Vector v = this.setAccountOnReport(accNumbers, OwnDisRepAccount.SALDO_TYPE_DEBT);
        if (v != null) {
            if (v.size() > 0) {
                this.clientContribution = (Vector) v.get(0);
            }
            if (v.size() > 1) {
                this.sumClientContribution = (OwnDisRepAccount) v.get(1);
                if (this.sumClientContribution != null && this.clientContribution.size() > 0) {
                    OwnDisRepAccount firstAcc = (OwnDisRepAccount) this.clientContribution.get(0); // get the top of account
                    if (firstAcc != null && (firstAcc.getAccount().getPostable() == PstPerkiraan.ACC_NOTPOSTED)) {
                        this.sumClientContribution.setAccount(firstAcc.getAccount());
                        this.clientContribution.set(0, this.sumClientContribution);
                    }
                }
            }
        }
    }

    public OwnDisRepAccount getBalanceFundToOwner() {
        OwnDisRepAccount bal = new OwnDisRepAccount();
        bal.setSaldoType(OwnDisRepAccount.SALDO_TYPE_DEBT);
        OwnDisRepAccount rev = this.getBalanceRevDueToOwner() == null ? new OwnDisRepAccount() : this.getBalanceRevDueToOwner();
        OwnDisRepAccount cc = this.getSumClientContribution() == null ? new OwnDisRepAccount() : this.getSumClientContribution();

        bal.setValPrev2PeriodDebt(rev.getPrev2PeriodSaldo() - cc.getPrev2PeriodSaldo());
        bal.setValPrev1PeriodDebt(rev.getPrev1PeriodSaldo() - cc.getPrev1PeriodSaldo());
        bal.setValThisPeriodDebt(rev.getThisPeriodSaldo() - cc.getThisPeriodSaldo());
        return bal;
    }

    private Vector setAccountOnReportOLD(String accNumbers[], int saldoType) {
        if ((accNumbers == null) || (accNumbers.length < 1)) {
            return null;
        }
        Vector accountAndSum = new Vector();
        Vector accounts = new Vector();
        OwnDisRepAccount sumAcc = new OwnDisRepAccount();
        sumAcc.setSaldoType(saldoType);
        long busCenterId = 0;
        if (this.getBussCenter() != null) {
            busCenterId = this.getBussCenter().getOID();
        }
        String sqlPart1 = "select a.id_perkiraan, a.nomor_perkiraan, a.nama, a.postable, d.buss_center_id, e.buss_center_id, f.buss_center_id, " +
                "c.periode_id as dp, sum(c.debit_amount * c.trans_rate) as debt3, sum(d.credit_amount * d.trans_rate) as credit3, " +
                "d.periode_id as dp, sum(d.debit_amount * d.trans_rate) as debt2 , sum(d.credit_amount * d.trans_rate) as credit2, " +
                "e.periode_id as ep, sum(e.debit_amount * e.trans_rate) as debt1 , sum(e.credit_amount * e.trans_rate) as credit1, " +
                "f.periode_id as fp, sum(f.debit_amount * f.trans_rate) as debt0 , sum(f.credit_amount * f.trans_rate) as credit0 " +
                "from aiso_perkiraan a " +
                "left join aiso_journal_distribution c on a.id_perkiraan=c.id_perkiraan " +
                "and c.buss_center_id='" + busCenterId + "'  and c.periode_id='" + (this.prev3Period != null ? this.prev3Period.getOID() : 0) + "' " +
                "left join aiso_journal_distribution d on a.id_perkiraan=d.id_perkiraan " +
                "and d.buss_center_id='" + busCenterId + "'  and d.periode_id='" + (this.prev2Period != null ? this.prev2Period.getOID() : 0) + "' " +
                "left join aiso_journal_distribution e on a.id_perkiraan=e.id_perkiraan " +
                "and e.buss_center_id='" + busCenterId + "'  and e.periode_id='" + (this.prev1Period != null ? this.prev1Period.getOID() : 0) + "' " +
                "left join aiso_journal_distribution f on a.id_perkiraan=f.id_perkiraan " +
                "and f.buss_center_id='" + busCenterId + "'  and f.periode_id='" + (this.currentPeriod != null ? this.currentPeriod.getOID() : 0) + "' " +
                "where a.nomor_perkiraan='";

        String sqlPart2 = "' group by a.id_perkiraan, a.nomor_perkiraan, " +
                "a.nama, a.postable, c.buss_center_id, c.periode_id, d.buss_center_id, d.periode_id, e.buss_center_id, f.buss_center_id,  e.periode_id, f.periode_id";


        for (int ic = 0; ic < accNumbers.length; ic++) {
            ResultSet rs = null;
            DBResultSet rslt = null;

            try {
                rslt = DBHandler.execQueryResult(sqlPart1 + accNumbers[ic] + sqlPart2);
                rs = rslt != null ? rslt.getResultSet() : null;
                if (rs != null) {
                    while (rs.next()) {
                        OwnDisRepAccount acc = new OwnDisRepAccount();
                        acc.setSaldoType(saldoType);
                        Perkiraan perk = new Perkiraan();
                        perk.setOID(rs.getLong("id_perkiraan"));
                        perk.setNoPerkiraan(rs.getString("nomor_perkiraan"));
                        perk.setNama(rs.getString("nama"));
                        perk.setPostable(rs.getInt("postable"));
                        acc.setAccount(perk);
                        acc.setValPrev3PeriodDebt(rs.getDouble("debt3"));
                        acc.setValPrev3PeriodCredit(rs.getDouble("credit3"));
                        acc.setValPrev2PeriodDebt(rs.getDouble("debt2"));
                        acc.setValPrev2PeriodCredit(rs.getDouble("credit2"));
                        acc.setValPrev1PeriodDebt(rs.getDouble("debt1"));
                        acc.setValPrev1PeriodCredit(rs.getDouble("credit1"));
                        acc.setValThisPeriodDebt(rs.getDouble("debt0"));
                        acc.setValThisPeriodCredit(rs.getDouble("credit0"));

                        sumAcc.setValPrev3PeriodDebt(sumAcc.getValPrev3PeriodDebt() + acc.getValPrev3PeriodDebt());
                        sumAcc.setValPrev2PeriodDebt(sumAcc.getValPrev2PeriodDebt() + acc.getValPrev2PeriodDebt());
                        sumAcc.setValPrev1PeriodDebt(sumAcc.getValPrev1PeriodDebt() + acc.getValPrev1PeriodDebt());
                        sumAcc.setValThisPeriodDebt(sumAcc.getValThisPeriodDebt() + acc.getValThisPeriodDebt());

                        sumAcc.setValPrev3PeriodCredit(sumAcc.getValPrev3PeriodCredit() + acc.getValPrev3PeriodCredit());
                        sumAcc.setValPrev2PeriodCredit(sumAcc.getValPrev2PeriodCredit() + acc.getValPrev2PeriodCredit());
                        sumAcc.setValPrev1PeriodCredit(sumAcc.getValPrev1PeriodCredit() + acc.getValPrev1PeriodCredit());
                        sumAcc.setValThisPeriodCredit(sumAcc.getValThisPeriodCredit() + acc.getValThisPeriodCredit());

                        accounts.add(acc);
                    }
                }
                accountAndSum.add(accounts);
                accountAndSum.add(sumAcc);
            } catch (Exception exc) {
                System.out.println("EXC + setAccountOnReport(String accNumbers[], Periode period )" + exc);
            } finally {
                if (rslt != null) {
                    DBResultSet.close(rslt);
                }
            }
        }

        return accountAndSum;
    }

    private Vector setAccountOnReport(String accNumbers[], int saldoType) {
        if ((accNumbers == null) || (accNumbers.length < 1)) {
            return null;
        }
        Vector accountAndSum = new Vector();
        Vector accounts = new Vector();
        OwnDisRepAccount sumAcc = new OwnDisRepAccount();
        sumAcc.setSaldoType(saldoType);
        long busCenterId = 0;
        if (this.getBussCenter() != null) {
            busCenterId = this.getBussCenter().getOID();
        }
        String sqlPart1 = "select a.id_perkiraan, a.nomor_perkiraan, a.nama, a.postable " +
                "from aiso_perkiraan a " +
                "where a.nomor_perkiraan in " ;

        String sqlPart2 ="(";
        for (int ic = 0; ic < accNumbers.length; ic++) {
            sqlPart2 = sqlPart2 + "'" + accNumbers[ic] + "'";
                  
            if (ic != (accNumbers.length - 1)) {
                sqlPart2 = sqlPart2 + ",";
            }else{
                sqlPart2 = sqlPart2 + ")";
            }
        }
        sqlPart1 = sqlPart1 + sqlPart2 + " order by a.nomor_perkiraan";

        ResultSet rs = null;
        DBResultSet rslt = null;

        try {
            rslt = DBHandler.execQueryResult(sqlPart1);
            rs = rslt != null ? rslt.getResultSet() : null;
            if (rs != null) {
                while (rs.next()) {
                    OwnDisRepAccount acc = new OwnDisRepAccount();
                    acc.setSaldoType(saldoType);
                    Perkiraan perk = new Perkiraan();
                    perk.setOID(rs.getLong("id_perkiraan"));
                    perk.setNoPerkiraan(rs.getString("nomor_perkiraan"));
                    perk.setNama(rs.getString("nama"));
                    perk.setPostable(rs.getInt("postable"));
                    acc.setAccount(perk);
                    accounts.add(acc);
                }
            }
        } catch (Exception exc) {
            System.out.println("EXC + setAccountOnReport(String accNumbers[], Periode period )" + exc);
        } finally {
            if (rslt != null) {
                DBResultSet.close(rslt);
            }
        }
        
        for(int p=0;p<4;p++){
           long  periodId=0;
           switch(p){
               case 3: periodId = this.prev3Period.getOID();break;
               case 2: periodId = this.prev2Period.getOID();break;
               case 1: periodId = this.prev1Period.getOID();break;
               case 0: periodId = this.currentPeriod.getOID();break;
               default:;
           } 
            
        sqlPart1 = "select  a.id_perkiraan, a.nomor_perkiraan, "+
        " sum(e.debit_amount * e.trans_rate) as debt , sum(e.credit_amount * e.trans_rate) as credit "+
        " from aiso_perkiraan a "+
        " left join aiso_journal_distribution e on a.id_perkiraan=e.id_perkiraan and e.buss_center_id='"+busCenterId+"' "+
        " and e.periode_id='"+periodId+"' "+ 
        " where a.nomor_perkiraan IN " + sqlPart2 +
        " group by a.id_perkiraan, a.nomor_perkiraan, a.nama, a.postable, e.buss_center_id, e.periode_id "+
        " order by a.nomor_perkiraan";

        try {
            rslt = DBHandler.execQueryResult(sqlPart1);
            rs = rslt != null ? rslt.getResultSet() : null;
            int idx=0;
            if (rs != null) {
                while (rs.next()) {
                    OwnDisRepAccount acc = (OwnDisRepAccount) accounts.get(idx);
                    idx++;
                    Perkiraan perk = acc.getAccount();
                    switch (p){
                        case 3:
                        acc.setValPrev3PeriodDebt(rs.getDouble("debt"));
                        acc.setValPrev3PeriodCredit(rs.getDouble("credit"));
                        sumAcc.setValPrev3PeriodDebt(sumAcc.getValPrev3PeriodDebt() + acc.getValPrev3PeriodDebt());
                        sumAcc.setValPrev3PeriodCredit(sumAcc.getValPrev3PeriodCredit() + acc.getValPrev3PeriodCredit());
                        break;  
                        
                        case 2:                        
                        acc.setValPrev2PeriodDebt(rs.getDouble("debt"));
                        acc.setValPrev2PeriodCredit(rs.getDouble("credit"));
                        sumAcc.setValPrev2PeriodDebt(sumAcc.getValPrev2PeriodDebt() + acc.getValPrev2PeriodDebt());
                        sumAcc.setValPrev2PeriodCredit(sumAcc.getValPrev2PeriodCredit() + acc.getValPrev2PeriodCredit());
                        break;  

                        case 1:                        
                        acc.setValPrev1PeriodDebt(rs.getDouble("debt"));
                        acc.setValPrev1PeriodCredit(rs.getDouble("credit"));
                        sumAcc.setValPrev1PeriodDebt(sumAcc.getValPrev1PeriodDebt() + acc.getValPrev1PeriodDebt());                        
                        sumAcc.setValPrev1PeriodCredit(sumAcc.getValPrev1PeriodCredit() + acc.getValPrev1PeriodCredit());
                        break;  
                        
                        case 0:                        
                        acc.setValThisPeriodDebt(rs.getDouble("debt"));
                        acc.setValThisPeriodCredit(rs.getDouble("credit"));
                        sumAcc.setValThisPeriodDebt(sumAcc.getValThisPeriodDebt() + acc.getValThisPeriodDebt());
                        sumAcc.setValThisPeriodCredit(sumAcc.getValThisPeriodCredit() + acc.getValThisPeriodCredit());        
                        break;  
                        
                        default:;
                    }                    
                }
            }
        } catch (Exception exc) {
            System.out.println("EXC + setAccountOnReport(String accNumbers[], Periode period )" + exc);
        } finally {
            if (rslt != null) {
                DBResultSet.close(rslt);
            }
        }
        }
        
        accountAndSum.add(accounts);
        accountAndSum.add(sumAcc);
        return accountAndSum;
    }



    public static Vector listODRJournal(long busCenterId, long periodId, long accId) {
        if ((busCenterId == 0) || (periodId == 0) || (accId == 0)) {
            return null;
        }

        Vector journals = new Vector();

        String sql = "select c.jurnal_id, c.tgl_transaksi, c.journal_number, c.keterangan, a.debit_amount, a.credit_amount, a.note, b.note as note_dtl from aiso_journal_distribution a " +
                " left join aiso_jurnal_detail b on a.journal_detail_id=b.j_detail_id left join aiso_jurnal_umum c on b.jurnal_id=c.jurnal_id " +
                " where a.buss_center_id='" + busCenterId + "' and a.periode_id='" + periodId + "' and a.id_perkiraan='" + accId + "' order by tgl_transaksi";

        {
            ResultSet rs = null;
            DBResultSet rslt = null;

            try {
                rslt = DBHandler.execQueryResult(sql);
                rs = rslt != null ? rslt.getResultSet() : null;
                if (rs != null) {
                    while (rs.next()) {
                        JournalDistribution trans = new JournalDistribution();
                        trans.setMainJournalId(rs.getLong("jurnal_id"));
                        trans.setTransDate(rs.getDate("tgl_transaksi"));
                        trans.setMainJournalNumber(rs.getString("journal_number"));
                        trans.setMainJournalNote(rs.getString("keterangan"));
                        trans.setDebitAmount(rs.getDouble("debit_amount"));
                        trans.setCreditAmount(rs.getDouble("credit_amount"));
                        trans.setNote(rs.getString("note"));
                        trans.setJDetailNote(rs.getString("note_dtl"));
                        journals.add(trans);
                    }
                }
            } catch (Exception exc) {
                System.out.println("EXC + setAccountOnReport(String accNumbers[], Periode period )" + exc);
            } finally {
                if (rslt != null) {
                    DBResultSet.close(rslt);
                }
            }
        }

        return journals;
    }

    public Periode getCurrentPeriod() {
        return currentPeriod;
    }

    /**
     * Set current period by OID will set also the 2 previous periods
     * @param currentPeriodOID
     */
    public void setCurrentPeriodByOid(long currentPeriodOID, long bussCenterOID) {
        try {
            this.currentPeriod = PstPeriode.fetchExc(currentPeriodOID);

            if (this.sumClientAdvance == null) {
                this.sumClientAdvance = new OwnDisRepAccount();
            }

            setCurrPerBalnc(PstReportOwnerDistribution.fetchByPeriodId(currentPeriodOID, bussCenterOID));
            /*if(rep!=null){
            this.sumClientAdvance.setValThisPeriodCredit(rep.getAdvance_pay_balance());
            }*/

            long prev1Per = PstPeriode.getPrevPeriod(this.currentPeriod.getOID());
            if (prev1Per != 0) {
                this.prev1Period = PstPeriode.fetchExc(prev1Per);
                setPrev1PerBalnc(PstReportOwnerDistribution.fetchByPeriodId(prev1Per, bussCenterOID));
                /*if(rep!=null){
                this.sumClientAdvance.setValPrev1PeriodCredit(rep.getAdvance_pay_balance());
                }*/

                if (this.prev1Period != null && this.prev1Period.getOID() != 0) {
                    long prev2Per = PstPeriode.getPrevPeriod(this.prev1Period.getOID());
                    if (prev2Per != 00) {
                        this.prev2Period = PstPeriode.fetchExc(prev2Per);
                        setPrev2PerBalnc(PstReportOwnerDistribution.fetchByPeriodId(prev2Per, bussCenterOID));
                        /*if(rep!=null){
                        this.sumClientAdvance.setValPrev2PeriodCredit(rep.getAdvance_pay_balance());
                        } */

                        if (this.prev2Period != null && this.prev2Period.getOID() != 0) {
                            long prev3Per = PstPeriode.getPrevPeriod(this.prev2Period.getOID());
                            if (prev3Per != 00) {
                                this.prev3Period = PstPeriode.fetchExc(prev3Per);
                                setPrev3PerBalnc(PstReportOwnerDistribution.fetchByPeriodId(prev3Per, bussCenterOID));
                            /*if(rep!=null){
                            this.sumClientAdvance.setValPrev3PeriodCredit(rep.getAdvance_pay_balance());
                            } */

                            }
                        }
                    }
                }
            }
        } catch (Exception exc) {
            System.out.println("EXC >> setCurrentPeriodOid" + exc);
        }
    }

    public Periode getPrev2Period() {
        return prev2Period;
    }

    public Periode getPrev1Period() {
        return prev1Period;
    }

    public static void main(String args[]) {
        String clientAdvance[] = {"2-1410"};
        String villaRevenue[] = {"4-1100"};
        String managementExpense[] = {"2-1810", "2-1820", "2-1830", "2-1310", "2-1230", "4-3200"};
        String varDirectOpExpense[] = {"5-2100", "5-2101", "5-2102", "5-2103", "5-2104", "5-2105", "5-2106", "5-2107", "5-2108",
            "5-2109", "5-2110", "5-2190"
        };
        String fixDirectOpExpense[] = {"5-2200", "5-2201", "5-2202", "5-2203", "5-2204", "5-2205", "5-2206", "5-2207", "5-2208",
            "5-2209", "5-2210", "5-2211", "5-2212", "5-2213", "5-2214", "5-2290"
        };
        String indirectOpExpense[] = {"5-2300", "5-2310", "5-2311", "5-2312", "5-2313", "5-2314", "5-2320", "5-2330", "5-2340",
            "5-2350", "5-2360", "5-2370", "5-2380"
        };
        String otherVillaOpExpense[] = {"5-3000"};
        String commisionAndFee[] = {"5-2100", "5-2100", "5-2200"};
        String commonArea[] = {"5-4000"};
        String clientContribution[] = {"5-1400", "5-1500"};

        OwnerDistributionReport own = new OwnerDistributionReport(504404401789177014L, 20090601L);
        own.setClientAdvanceAcc(clientAdvance);

        for (int idx = 0; idx < own.getClientAdvanceSize(); idx++) {
            OwnDisRepAccount item = (OwnDisRepAccount) own.getClientAdvance(idx);
            System.out.println("" + idx + "| " + item.getAccount().getNoPerkiraan() + "\t|" + item.getAccount().getNama() + "\t\t|" +
                    item.getValPrev3PeriodDebt() + "\t|" + item.getValPrev3PeriodCredit() + "\t|" +
                    item.getValPrev2PeriodDebt() + "\t|" + item.getValPrev2PeriodCredit() + "\t|" +
                    item.getValPrev1PeriodDebt() + "\t|" + item.getValPrev1PeriodCredit() + "\t|" +
                    item.getValThisPeriodDebt() + "\t|" + item.getValThisPeriodCredit() + "\t|");
        }

        own.setVillaRevenue(villaRevenue);
        for (int idx = 0; idx < own.getClientAdvanceSize(); idx++) {
            OwnDisRepAccount item = (OwnDisRepAccount) own.getVillaRevenue(idx);
            System.out.println("" + idx + "| " + item.getAccount().getNoPerkiraan() + "\t|" + item.getAccount().getNama() + "\t\t|" +
                    item.getValPrev3PeriodDebt() + "\t|" + item.getValPrev3PeriodCredit() + "\t|" +
                    item.getValPrev2PeriodDebt() + "\t|" + item.getValPrev2PeriodCredit() + "\t|" +
                    item.getValPrev1PeriodDebt() + "\t|" + item.getValPrev1PeriodCredit() + "\t|" +
                    item.getValThisPeriodDebt() + "\t|" + item.getValThisPeriodCredit() + "\t|");
        }

        own.setManagementExpense(managementExpense);
        for (int idx = 0; idx < own.getManagementExpenseSize(); idx++) {
            OwnDisRepAccount item = (OwnDisRepAccount) own.getManagementExpense(idx);
            System.out.println("" + idx + "| " + item.getAccount().getNoPerkiraan() + "\t|" + item.getAccount().getNama() + "\t\t|" +
                    item.getValPrev3PeriodDebt() + "\t|" + item.getValPrev3PeriodCredit() + "\t|" +
                    item.getValPrev2PeriodDebt() + "\t|" + item.getValPrev2PeriodCredit() + "\t|" +
                    item.getValPrev1PeriodDebt() + "\t|" + item.getValPrev1PeriodCredit() + "\t|" +
                    item.getValThisPeriodDebt() + "\t|" + item.getValThisPeriodCredit() + "\t|");
        }

    }

    public OwnDisRepAccount getSumVillaRevenue() {
        return sumVillaRevenue;
    }

    public void setSumVillaRevenue(OwnDisRepAccount sumVillaRevenue) {
        this.sumVillaRevenue = sumVillaRevenue;
    }

    public OwnDisRepAccount getSumManagementExpense() {
        return sumManagementExpense;
    }

    public void setSumManagementExpense(OwnDisRepAccount sumManagementExpense) {
        this.sumManagementExpense = sumManagementExpense;
    }

    public OwnDisRepAccount getSumVarDirectOpExpense() {
        return sumVarDirectOpExpense;
    }

    public void setSumVarDirectOpExpense(OwnDisRepAccount sumVarDirectOpExpense) {
        this.sumVarDirectOpExpense = sumVarDirectOpExpense;
    }

    public OwnDisRepAccount getSumFixDirectOpExpense() {
        return sumFixDirectOpExpense;
    }

    public void setSumFixDirectOpExpense(OwnDisRepAccount sumFixDirectOpExpense) {
        this.sumFixDirectOpExpense = sumFixDirectOpExpense;
    }

    public OwnDisRepAccount getSumIndirectOpExpense() {
        return sumIndirectOpExpense;
    }

    public void setSumIndirectOpExpense(OwnDisRepAccount sumIndirectOpExpense) {
        this.sumIndirectOpExpense = sumIndirectOpExpense;
    }

    public OwnDisRepAccount getSumOtherVillaOpExpense() {
        return sumOtherVillaOpExpense;
    }

    public void setSumOtherVillaOpExpense(OwnDisRepAccount sumOtherVillaOpExpense) {
        this.sumOtherVillaOpExpense = sumOtherVillaOpExpense;
    }

    public OwnDisRepAccount getSumCommisionAndFee() {
        return sumCommisionAndFee;
    }

    public void setSumCommisionAndFee(OwnDisRepAccount sumCommisionAndFee) {
        this.sumCommisionAndFee = sumCommisionAndFee;
    }

    public OwnDisRepAccount getSumCommonArea() {
        return sumCommonArea;
    }

    public void setSumCommonArea(OwnDisRepAccount sumCommonArea) {
        this.sumCommonArea = sumCommonArea;
    }

    public OwnDisRepAccount getSumClientContribution() {
        return sumClientContribution;
    }

    public void setSumClientContribution(OwnDisRepAccount sumClientContribution) {
        this.sumClientContribution = sumClientContribution;
    }

    public OwnDisRepAccount getSumClientAdvance() {
        return sumClientAdvance;
    }

    public void setSumClientAdvance(OwnDisRepAccount sumClientAdvance) {
        this.sumClientAdvance = sumClientAdvance;
    }

    public ReportOwnerDistribution getCurrPerBalnc() {
        return (currPerBalnc == null ? new ReportOwnerDistribution() : currPerBalnc);
    }

    public void setCurrPerBalnc(ReportOwnerDistribution currPerBalnc) {
        this.currPerBalnc = currPerBalnc;
    }

    public ReportOwnerDistribution getPrev1PerBalnc() {
        return (prev1PerBalnc == null ? new ReportOwnerDistribution() : prev1PerBalnc);
    }

    public void setPrev1PerBalnc(ReportOwnerDistribution prev1PerBalnc) {
        this.prev1PerBalnc = prev1PerBalnc;
    }

    public ReportOwnerDistribution getPrev2PerBalnc() {
        return (prev2PerBalnc == null ? new ReportOwnerDistribution() : prev2PerBalnc);
    }

    public void setPrev2PerBalnc(ReportOwnerDistribution prev2PerBalnc) {
        this.prev2PerBalnc = prev2PerBalnc;
    }

    public ReportOwnerDistribution getPrev3PerBalnc() {
        return (prev3PerBalnc == null ? new ReportOwnerDistribution() : prev3PerBalnc);
    }

    public void setPrev3PerBalnc(ReportOwnerDistribution prev3PerBalnc) {
        this.prev3PerBalnc = prev3PerBalnc;
    }

    public OwnDisRepAccount getThreeMonthsOpProjection() {
        return threeMonthsOpProjection;
    }

    /**
     * Set 3 months Operational projection , make it saldo debet
     * @param threeMonthsOpProjection
     */
    public void setThreeMonthsOpProjection(OwnDisRepAccount threeMonthsOpProjection) {
        if (threeMonthsOpProjection != null) {
            threeMonthsOpProjection.setSaldoType(OwnDisRepAccount.SALDO_TYPE_DEBT);
        }
        this.threeMonthsOpProjection = threeMonthsOpProjection;
    }

    public OwnDisRepAccount getProcentOfThreeMonthsOpProj() {
        OwnDisRepAccount acc = new OwnDisRepAccount();
        acc.setSaldoType(OwnDisRepAccount.SALDO_TYPE_DEBT);
        if (this.getSumVillaExpenses() != null) {
            acc.setValPrev3PeriodDebt(threeMonthsOpProjection.getPrev3PeriodSaldo() * procentOpProject);
            acc.setValPrev2PeriodDebt(threeMonthsOpProjection.getPrev2PeriodSaldo() * procentOpProject);
            acc.setValPrev1PeriodDebt(threeMonthsOpProjection.getPrev1PeriodSaldo() * procentOpProject);
            acc.setValThisPeriodDebt(threeMonthsOpProjection.getThisPeriodSaldo() * procentOpProject);
        }
        return acc;
    }

    public OwnDisRepAccount getBalAmountTopUp() {
        OwnDisRepAccount acc = new OwnDisRepAccount();
        acc.setSaldoType(OwnDisRepAccount.SALDO_TYPE_DEBT);
        if (getBalance3MonthOpExp() != null) {
            OwnDisRepAccount bal3Mnt = getBalance3MonthOpExp();
            OwnDisRepAccount procMnt = getProcentOfThreeMonthsOpProj();
            if (bal3Mnt.getPrev3PeriodSaldo() < procMnt.getPrev3PeriodSaldo()) {
                acc.setValPrev3PeriodDebt(procMnt.getPrev3PeriodSaldo() - bal3Mnt.getPrev3PeriodSaldo());
            }
            if (bal3Mnt.getPrev2PeriodSaldo() < procMnt.getPrev2PeriodSaldo()) {
                acc.setValPrev2PeriodDebt(procMnt.getPrev2PeriodSaldo() - bal3Mnt.getPrev2PeriodSaldo());
            }
            if (bal3Mnt.getPrev1PeriodSaldo() < procMnt.getPrev1PeriodSaldo()) {
                acc.setValPrev1PeriodDebt(procMnt.getPrev1PeriodSaldo() - bal3Mnt.getPrev1PeriodSaldo());
            }
            if (bal3Mnt.getThisPeriodSaldo() < procMnt.getThisPeriodSaldo()) {
                acc.setValThisPeriodDebt(procMnt.getThisPeriodSaldo() - bal3Mnt.getThisPeriodSaldo());
            }
        }

        return acc;
    }

    public double getProcentOpProject() {
        return procentOpProject;
    }

    public void setProcentOpProject(double procentOpProject) {
        this.procentOpProject = procentOpProject;
    }

    public BussinessCenter getBussCenter() {
        return bussCenter;
    }

    public void setBussCenter(BussinessCenter bussCenter) {
        this.bussCenter = bussCenter;
    }
}
