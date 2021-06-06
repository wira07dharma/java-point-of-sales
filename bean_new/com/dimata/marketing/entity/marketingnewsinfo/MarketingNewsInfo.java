
package com.dimata.marketing.entity.marketingnewsinfo;

/**
 *
 * @author Witar
 */
import com.dimata.qdep.entity.Entity;
import java.util.Date;

public class MarketingNewsInfo extends Entity{

    private String title = "";
    private Date validStart = new Date();
    private Date validEnd = new Date();
    private String description = "";
    private long locationId =0;
    private String locationName = "";
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Date getValidStart() {
        return validStart;
    }

    public void setValidStart(Date validStart) {
        this.validStart = validStart;
    }

    public Date getValidEnd() {
        return validEnd;
}

    public void setValidEnd(Date validEnd) {
        this.validEnd = validEnd;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * @return the locationId
     */
    public long getLocationId() {
        return locationId;
    }

    /**
     * @param locationId the locationId to set
     */
    public void setLocationId(long locationId) {
        this.locationId = locationId;
    }

    /**
     * @return the locationName
     */
    public String getLocationName() {
        return locationName;
    }

    /**
     * @param locationName the locationName to set
     */
    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }
}
