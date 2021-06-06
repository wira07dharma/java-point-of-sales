
package com.dimata.posbo.entity.masterdata;

/**
 *
 * @author Witar
 */
import com.dimata.qdep.entity.*;
public class NotaSetting extends Entity {
    
    private long locationId =0;
    private String footerText ="";
 
    public long getLocationId() {
        return locationId;
    }

    public void setLocationId(long locationId) {
        this.locationId = locationId;
    }
 
    public String getFooterText() {
        return footerText;
    }

    public void setFooterText(String footerText) {
        this.footerText = footerText;
    }
    
    
}
