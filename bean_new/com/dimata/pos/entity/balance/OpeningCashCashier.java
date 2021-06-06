/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.dimata.pos.entity.balance;

/**
 *
 * @author Wiweka
 */
import com.dimata.qdep.entity.Entity;
import java.util.Date;

public class OpeningCashCashier extends Entity {
    private long cashierId;
    private String nameUser;
    private String nameSupervisor;
    private Date openDate;
    private String location;

    /**
     * @return the nameUser
     */
    public String getNameUser() {
        return nameUser;
    }

    /**
     * @param nameUser the nameUser to set
     */
    public void setNameUser(String nameUser) {
        this.nameUser = nameUser;
    }

    /**
     * @return the nameSupervisor
     */
    public String getNameSupervisor() {
        return nameSupervisor;
    }

    /**
     * @param nameSupervisor the nameSupervisor to set
     */
    public void setNameSupervisor(String nameSupervisor) {
        this.nameSupervisor = nameSupervisor;
    }

    /**
     * @return the openDate
     */
    public Date getOpenDate() {
        return openDate;
    }

    /**
     * @param openDate the openDate to set
     */
    public void setOpenDate(Date openDate) {
        this.openDate = openDate;
    }

    /**
     * @return the location
     */
    public String getLocation() {
        return location;
    }

    /**
     * @param location the location to set
     */
    public void setLocation(String location) {
        this.location = location;
    }

    /**
     * @return the cashierId
     */
    public long getCashierId() {
        return cashierId;
    }

    /**
     * @param cashierId the cashierId to set
     */
    public void setCashierId(long cashierId) {
        this.cashierId = cashierId;
    }
}
