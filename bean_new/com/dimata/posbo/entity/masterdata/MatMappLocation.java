
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
 
import com.dimata.qdep.entity.Entity;


public class MatMappLocation extends Entity {
    public long getMaterialId() {
        return this.getOID(1);
    }

    public void setMaterialId(long materialId) {
        this.setOID(1, materialId);
    }

    public long getLocationId() {
        return this.getOID(0);
    }

    public void setLocationId(long locationId) {
        this.setOID(0, locationId);
    }
}
