
/* Created on 	:  [date] [time] AM/PM 
 * 
 * @author  	:  [authorName] 
 * @version  	:  [version] 
 */
/**
 * *****************************************************************
 * Class Description : [project description ... ] Imput Parameters : [input
 * parameter ...] Output : [output ...] 
 ******************************************************************
 */
package com.dimata.harisma.entity.masterdata;

/* package java */
import java.util.Date;

/* package qdep */
import com.dimata.qdep.entity.*;

public class Position extends Entity {

    private String position = "";
    private String description = "";
    private double persentaseInsentif = 0;
    private int positionLevel = 0;

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        if (position == null) {
            position = "";
        }
        this.position = position;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        if (description == null) {
            description = "";
        }
        this.description = description;
    }

    public double getPersentaseInsentif() {
        return persentaseInsentif;
    }

    public void setPersentaseInsentif(double persentaseInsentif) {
        this.persentaseInsentif = persentaseInsentif;
    }

    /**
     * @return the positionLevel
     */
    public int getPositionLevel() {
        return positionLevel;
    }

    /**
     * @param positionLevel the positionLevel to set
     */
    public void setPositionLevel(int positionLevel) {
        this.positionLevel = positionLevel;
    }

}
