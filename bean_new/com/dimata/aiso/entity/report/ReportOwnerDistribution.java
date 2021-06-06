/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.dimata.aiso.entity.report;

import com.dimata.qdep.entity.*; 
/**
 *
 * @author D
 */
public class ReportOwnerDistribution extends Entity  {
    private long rprt_own_distribution_id;
    private long period_id;
    private double advance_pay_balance;
    private long buss_center_id;

    public long getRprt_own_distribution_id() {
        return rprt_own_distribution_id;
    }

    public void setRprt_own_distribution_id(long rprt_own_distribution_id) {
        this.rprt_own_distribution_id = rprt_own_distribution_id;
    }


    public long getPeriod_id() {
        return period_id;
    }

    public void setPeriod_id(long period_id) {
        this.period_id = period_id;
    }


    public double getAdvance_pay_balance() {
        return advance_pay_balance;
    }

    public void setAdvance_pay_balance(double advance_pay_balance) {
        this.advance_pay_balance = advance_pay_balance;
    }

    public long getBuss_center_id() {
        return buss_center_id;
    }

    public void setBuss_center_id(long buss_center_id) {
        this.buss_center_id = buss_center_id;
    }
}
