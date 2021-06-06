
package com.dimata.common.entity.finger;

/**
 *
 * @author Witar
 */
import com.dimata.qdep.entity.Entity;

public class DeviceFinger 
extends Entity{
    
    private String deviceName="";
    private String sn ="";
    private String vc ="";
    private String ac ="";
    private String vKey="";
    private String macAddress="";
    
    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public String getSn() {
        return sn;
    }

    public void setSn(String sn) {
        this.sn = sn;
    }

    public String getVc() {
        return vc;
    }

    public void setVc(String vc) {
        this.vc = vc;
    }

    public String getAc() {
        return ac;
    }

    public void setAc(String ac) {
        this.ac = ac;
    }

    public String getvKey() {
        return vKey;
    }

    public void setvKey(String vKey) {
        this.vKey = vKey;
    }

    public String getMacAddress() {
        return macAddress;
    }

    public void setMacAddress(String macAddress) {
        this.macAddress = macAddress;
    }
    
    
    
}
