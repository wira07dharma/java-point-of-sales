/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dimata.common.entity.custom;

import com.dimata.qdep.entity.Entity;

/**
 *
 * @author dimata005
 */
public class CustomeReportMaster extends Entity{
    private String customeReportName="";

    /**
     * @return the customeReportName
     */
    public String getCustomeReportName() {
        return customeReportName;
    }

    /**
     * @param customeReportName the customeReportName to set
     */
    public void setCustomeReportName(String customeReportName) {
        this.customeReportName = customeReportName;
    }
}
