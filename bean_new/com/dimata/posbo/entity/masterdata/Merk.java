
/* Created on 	:  [date] [time] AM/PM 
 * 
 * @author	 :
 * @version	 :
 */
/*******************************************************************
 * Class Description 	: [project description ... ] 
 * Imput Parameters 	: [input parameter ...] 
 * Output 		: [output ...] 
 *******************************************************************/
package com.dimata.posbo.entity.masterdata;

/* package java */
import com.dimata.qdep.entity.*; 

public class Merk extends Entity {

    private String name = "";
    private String code = "";
    //adding status ditampilkan atau tidak by mirahu 20120511
    private int status = 0;
    

    public String getName() {
        return name; 
    }

    public void setName(String name) {
        if (name == null) {
            name = ""; 
        }
        this.name = name;
    } 

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    /**
     * @return the status
     */
    public int getStatus() {
        return status;
    }

    /**
     * @param status the status to set
     */
    public void setStatus(int status) {
        this.status = status;
    }
}
