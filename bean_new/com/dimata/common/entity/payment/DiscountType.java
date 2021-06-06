
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

package com.dimata.common.entity.payment;
 
import com.dimata.qdep.entity.*;

public class DiscountType extends Entity { 

	private String code = "";
	private String name = "";
    private int pindex = 1;

    public int getPindex() {
        return pindex;
    }

    public void setPindex(int pindex) {
        this.pindex = pindex;
    }

	public String getCode(){
		return code; 
	} 

	public void setCode(String code){ 
		if ( code == null ) {
			code = ""; 
		} 
		this.code = code; 
	} 

	public String getName(){ 
		return name; 
	} 

	public void setName(String name){ 
		if ( name == null ) {
			name = ""; 
		} 
		this.name = name; 
	} 

}
