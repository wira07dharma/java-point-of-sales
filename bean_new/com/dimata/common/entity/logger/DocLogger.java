
/* Created on 	:  [date] [time] AM/PM 
 * 
 * @author  	: karya
 * @version  	: 01
 */

/*******************************************************************
 * Class Description 	: [project description ... ] 
 * Imput Parameters 	: [input parameter ...] 
 * Output 		: [output ...] 
 *******************************************************************/

package com.dimata.common.entity.logger;
 
import java.io.*;
import java.util.Date;

import com.dimata.qdep.entity.*;

public class DocLogger extends Entity implements Serializable {

    private int docType;
    private String docNumber = "";
    private String Description = "";

    public int getDocType() {
        return docType;
    }

    public void setDocType(int docType) {
        this.docType = docType;
    }

    public String getDocNumber() {
        return docNumber;
    }

    public void setDocNumber(String docNumber) {
        this.docNumber = docNumber;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public Date getDocDate() {
        return docDate;
    }

    public void setDocDate(Date docDate) {
        this.docDate = docDate;
    }

    /**
     * Getter for property docOid.
     * @return Value of property docOid.
     */
    public long getDocOid() {
        return this.docOid;
    }
    
    /**
     * Setter for property docOid.
     * @param docOid New value of property docOid.
     */
    public void setDocOid(long docOid) {
        this.docOid = docOid;
    }
    
    private Date docDate = new Date();

    /**
     * Holds value of property docOid.
     */
    private long docOid;
    
}
