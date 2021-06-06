/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dimata.aiso.session.report.villamanagement;

import com.dimata.aiso.entity.masterdata.Perkiraan;

/**
 *
 * @author kartika August 2009
 */
public class OwnDisRepAccount {

    public static String clientAdvance[] = {"2-1410"};
    public static String villaRevenue[] = {"4-1100"};
    public static String managementExpense[] = {"2-1810", "2-1820", "2-1830", "2-1310", "2-1230", "4-3200"};
    public static String varDirectOpExpense[] = {"5-2100", "5-2101", "5-2102", "5-2103", "5-2104", "5-2105", "5-2106", "5-2107", "5-2108",
        "5-2109", "5-2110", "5-2190"
    };
    public static String fixDirectOpExpense[] = {"5-2200", "5-2201", "5-2202", "5-2203", "5-2204", "5-2205", "5-2206", "5-2207", "5-2208",
        "5-2209", "5-2210", "5-2211", "5-2212", "5-2213", "5-2290"
    };
    public static String indirectOpExpense[] = {"5-2300", "5-2301", "5-2302", "5-2303", "5-2304", "5-2305", "5-2306", "5-2307"
    };
    public static String otherVillaOpExpense[] = {"5-3000"};
    public static String commisionAndFee[] = {"5-3200"};
    public static String commonArea[] = {"5-4000", "5-4100", "5-4101", "5-4102", "5-4103", "5-4104", "5-4105", "5-4106", "5-4107", "5-4108", "5-4109", "5-4200", "5-4201", "5-4202", "5-4203", "5-4204", "5-4205", "5-4206", "5-4207", "5-4208", "5-4209", "5-4300", "5-4301", "5-4302", "5-4303"};
    public static String clientContribution[] = {"5-3100", "5-3101", "5-3102", "5-3103", "5-3104", "5-3105", "5-3106"};
    
    
    public static final int SALDO_TYPE_DEBT = 0;
    public static final int SALDO_TYPE_CREDIT = 1;
    private Perkiraan account = null;
    private double valPrev3PeriodDebt;
    private double valPrev3PeriodCredit;
    private double valPrev2PeriodDebt;
    private double valPrev2PeriodCredit;
    private double valPrev1PeriodDebt;
    private double valPrev1PeriodCredit;
    private double valThisPeriodDebt;
    private double valThisPeriodCredit;
    private int saldoType = 0;

    public double getPrev3PeriodSaldo() {
        if (saldoType == SALDO_TYPE_DEBT) {
            return valPrev3PeriodDebt - valPrev3PeriodCredit;
        } else {
            return valPrev3PeriodCredit - valPrev3PeriodDebt;
        }
    }

    public double getPrev2PeriodSaldo() {
        if (saldoType == SALDO_TYPE_DEBT) {
            return valPrev2PeriodDebt - valPrev2PeriodCredit;
        } else {
            return valPrev2PeriodCredit - valPrev2PeriodDebt;
        }
    }

    public double getPrev1PeriodSaldo() {
        if (saldoType == SALDO_TYPE_DEBT) {
            return valPrev1PeriodDebt - valPrev1PeriodCredit;
        } else {
            return valPrev1PeriodCredit - valPrev1PeriodDebt;
        }
    }

    public double getThisPeriodSaldo() {
        if (saldoType == SALDO_TYPE_DEBT) {
            return valThisPeriodDebt - valThisPeriodCredit;
        } else {
            return valThisPeriodCredit - valThisPeriodDebt;
        }
    }

    public Perkiraan getAccount() {
        return account;
    }

    public void setAccount(Perkiraan account) {
        this.account = account;
    }

    public double getValPrev2PeriodDebt() {
        return valPrev2PeriodDebt;
    }

    public void setValPrev2PeriodDebt(double valPrev2PeriodDebt) {
        this.valPrev2PeriodDebt = valPrev2PeriodDebt;
    }

    public double getValPrev2PeriodCredit() {
        return valPrev2PeriodCredit;
    }

    public void setValPrev2PeriodCredit(double valPrev2PeriodCredit) {
        this.valPrev2PeriodCredit = valPrev2PeriodCredit;
    }

    public double getValPrev1PeriodDebt() {
        return valPrev1PeriodDebt;
    }

    public void setValPrev1PeriodDebt(double valPrev1PeriodDebt) {
        this.valPrev1PeriodDebt = valPrev1PeriodDebt;
    }

    public double getValPrev1PeriodCredit() {
        return valPrev1PeriodCredit;
    }

    public void setValPrev1PeriodCredit(double valPrev1PeriodCredit) {
        this.valPrev1PeriodCredit = valPrev1PeriodCredit;
    }

    public double getValThisPeriodDebt() {
        return valThisPeriodDebt;
    }

    public void setValThisPeriodDebt(double valThisPeriodDebt) {
        this.valThisPeriodDebt = valThisPeriodDebt;
    }

    public double getValThisPeriodCredit() {
        return valThisPeriodCredit;
    }

    public void setValThisPeriodCredit(double valThisPeriodCredit) {
        this.valThisPeriodCredit = valThisPeriodCredit;
    }

    public double getValPrev3PeriodDebt() {
        return valPrev3PeriodDebt;
    }

    public void setValPrev3PeriodDebt(double valPrev3PeriodDebt) {
        this.valPrev3PeriodDebt = valPrev3PeriodDebt;
    }

    public double getValPrev3PeriodCredit() {
        return valPrev3PeriodCredit;
    }

    public void setValPrev3PeriodCredit(double valPrev3PeriodCredit) {
        this.valPrev3PeriodCredit = valPrev3PeriodCredit;
    }

    public int getSaldoType() {
        return saldoType;
    }

    public void setSaldoType(int saldoType) {
        this.saldoType = saldoType;
    }
}
