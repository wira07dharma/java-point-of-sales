
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
import java.util.Date;

/* package qdep */
import com.dimata.qdep.entity.*;

public class CodeRange extends Entity { 
	private String fromRangeCode = "";
	private String toRangeCode = "";

    public String getFromRangeCode() {
        return fromRangeCode;
    }

    public void setFromRangeCode(String fromRangeCode) {
        this.fromRangeCode = fromRangeCode;
    }

    public String getToRangeCode() {
        return toRangeCode;
    }

    public void setToRangeCode(String toRangeCode) {
        this.toRangeCode = toRangeCode;
    }
}
