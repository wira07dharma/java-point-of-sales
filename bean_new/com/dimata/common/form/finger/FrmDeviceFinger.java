
package com.dimata.common.form.finger;
/**
 *
 * @author Witar
 */
import com.dimata.common.entity.finger.DeviceFinger;
import com.dimata.qdep.form.FRMHandler;
import com.dimata.qdep.form.I_FRMInterface;
import com.dimata.qdep.form.I_FRMType;
import javax.servlet.http.HttpServletRequest;
public class FrmDeviceFinger  
extends FRMHandler 
implements I_FRMInterface,I_FRMType{
    
    private DeviceFinger deviceFinger;
    public static final String FRM_EMPLOPLOYEE = "FRM_DEVICE_FINGER";
    public static final int FRM_FLD_DEVICE_ID = 0;
    public static final int FRM_FLD_DEVICE_NAME = 1;
    public static final int FRM_FLD_SN = 2;
    public static final int FRM_FLD_VC = 3;
    public static final int FRM_FLD_AC = 4;
    public static final int FRM_FLD_VKEY = 5;
    public static final int FRM_FLD_MAC_ADDRESS = 6;
    
    public static String[] fieldNames ={
        "FLD_DEVICE_ID",
        "FLD_DEVICE_NAME",
        "FLD_SN",
        "FLD_VC",
        "FLD_AC",
        "FLD_VKEY",
        "FLD_MAC_ADDRESS"
    };
    public static int[] fieldTypes = {
        TYPE_LONG,
        TYPE_STRING + ENTRY_REQUIRED,
        TYPE_STRING,
        TYPE_STRING,
        TYPE_STRING,
        TYPE_STRING,
        TYPE_STRING
    };
    
    public FrmDeviceFinger(){
    }
    public FrmDeviceFinger(DeviceFinger deviceFinger){
        this.deviceFinger = deviceFinger;
    }
    public FrmDeviceFinger(HttpServletRequest request, DeviceFinger deviceFinger){
        super(new FrmDeviceFinger(deviceFinger),request);
        this.deviceFinger = deviceFinger;
    }
    
    @Override
    public int getFieldSize() {
        return fieldNames.length;
    }

    @Override
    public String getFormName() {
        return FRM_EMPLOPLOYEE;
    }

    @Override
    public String[] getFieldNames() {
        return fieldNames;
    }

    @Override
    public int[] getFieldTypes() {
        return fieldTypes;
    }
    
    public void requestEntityObject(DeviceFinger deviceFinger) { //melakukan 
        try{
            this.requestParam();
                deviceFinger.setDeviceName(getString(FRM_FLD_DEVICE_NAME)); 
                deviceFinger.setSn(getString(FRM_FLD_SN));
                deviceFinger.setVc(getString(FRM_FLD_VC));
                deviceFinger.setAc(getString(FRM_FLD_AC));
                deviceFinger.setvKey(getString(FRM_FLD_VKEY));
                deviceFinger.setMacAddress(getString(FRM_FLD_MAC_ADDRESS));
            }catch(Exception e){
                    System.out.println("Error on requestEntityObject : "+e.toString());
            }
    }
    
}
