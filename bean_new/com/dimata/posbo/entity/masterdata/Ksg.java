
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

public class Ksg extends Entity {

    private String name = "";
    private String code = "";
    private long locationId = 0;

    public String getName() {
        if(name==null){
            name="";
        }
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

    public long getLocationId() {
        return locationId;
    }

    public void setLocationId(long locationId) {
        this.locationId = locationId;
    }
    
}
