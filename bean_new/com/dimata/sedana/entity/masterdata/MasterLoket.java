/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dimata.sedana.entity.masterdata;

/**
 *
 * @author Regen
 */
import com.dimata.common.entity.location.Location;
import com.dimata.common.entity.location.PstLocation;
import com.dimata.posbo.db.DBException;
import com.dimata.qdep.entity.Entity;
import com.dimata.sedana.session.json.JSONObject;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MasterLoket extends Entity implements Cloneable {

    private long masterLoketId = 0;
    private int loketNumber = 0;
    private long locationId = 0;
    private String loketName = "";
    private int loketType = 0;

    public long getMasterLoketId() {
        return masterLoketId;
    }

    public void setMasterLoketId(long masterLoketId) {
        this.masterLoketId = masterLoketId;
    }

    public int getLoketNumber() {
        return loketNumber;
    }

    public void setLoketNumber(int loketNumber) {
        this.loketNumber = loketNumber;
    }

    public long getLocationId() {
        return locationId;
    }

    public void setLocationId(long locationId) {
        this.locationId = locationId;
    }

    public String getLoketName() {
        return loketName;
    }

    public void setLoketName(String loketName) {
        this.loketName = loketName;
    }

    public int getLoketType() {
        return loketType;
    }

    public void setLoketType(int loketType) {
        this.loketType = loketType;
    }

    public JSONObject historyNew() {
        JSONObject j = new JSONObject();
        Location loc = new Location();
		try {
			loc = PstLocation.fetchExc(getLocationId());
		} catch (DBException ex) {
			Logger.getLogger(MasterLoket.class.getName()).log(Level.SEVERE, null, ex);
		}
        j.put("Nomor Loket", getLoketNumber());
        j.put("Lokasi", loc.getName());
        return j;
    }

    public JSONObject historyCompare(MasterLoket o) {
        JSONObject j = new JSONObject();
        if (getLoketNumber() != (o.getLoketNumber())) {
            j.put("Nomor Loket", "Dari " + getLoketNumber() + " menjadi " + o.getLoketNumber());
        }
        if (getLocationId() != (o.getLocationId())) {
            Location loc = new Location();
			Location oloc = new Location();
			try {
				loc = PstLocation.fetchExc(getLocationId());
				oloc = PstLocation.fetchExc(o.getLocationId());
			} catch (DBException ex) {
				Logger.getLogger(MasterLoket.class.getName()).log(Level.SEVERE, null, ex);
			}
            
            j.put("Lokasi", "Dari " + loc.getName() + " menjadi " + oloc.getName());
        }
        return j;
    }

    public MasterLoket clone() {
        Object o = null;
        try {
            o = super.clone();;
        } catch (CloneNotSupportedException ex) {
            System.err.println(ex);
        }
        return (MasterLoket) o;
    }

}
