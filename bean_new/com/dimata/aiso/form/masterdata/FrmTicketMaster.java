/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.dimata.aiso.form.masterdata;

import com.dimata.aiso.entity.masterdata.TicketMaster;
import com.dimata.qdep.form.FRMHandler;
import com.dimata.qdep.form.I_FRMInterface;
import com.dimata.qdep.form.I_FRMType;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author dwi
 */
public class FrmTicketMaster extends FRMHandler implements I_FRMInterface, I_FRMType{

    public static final String FRM_TICKET_MASTER = "FRM_TICKET_MASTER";
    
    public static final int FRM_CODE = 0;
    public static final int FRM_DESCRIPTION = 1;
    public static final int FRM_TYPE = 2;
    public static final int FRM_CONTACT_ID = 3;
    public static final int FRM_ACC_COGS_ID = 4;
    public static final int FRM_ACC_AP_ID = 5;
    
    public static String[] fieldNames = {
	"FRM_CODE",
	"FRM_DESCRIPTION",
	"FRM_TYPE",
	"FRM_CONTACT_ID",
	"FRM_ACC_COGS_ID",
	"FRM_ACC_AP_ID"
    };
    
    public static int[] fieldTypes = {
	TYPE_STRING,
	TYPE_STRING,
	TYPE_INT,
	TYPE_LONG,
	TYPE_LONG,
	TYPE_LONG
    };
    
    private TicketMaster objTicketMaster;

    public FrmTicketMaster(TicketMaster objTicketMaster) {
        this.objTicketMaster = objTicketMaster;
    }

    public FrmTicketMaster(HttpServletRequest request, TicketMaster objTicketMaster) {
        super(new FrmTicketMaster(objTicketMaster), request);
        this.objTicketMaster = objTicketMaster;
    }

    public String getFormName() {
        return FRM_TICKET_MASTER;
    }

    public int[] getFieldTypes() {
        return fieldTypes;
    }

    public String[] getFieldNames() {
        return fieldNames;
    }

    public int getFieldSize() {
        return fieldNames.length;
    }

    public TicketMaster getEntityObject() {
        return objTicketMaster;
    }

    public void requestEntityObject(TicketMaster objTicketMaster) {
        try {
            this.requestParam();
            objTicketMaster.setCode(this.getString(FRM_CODE));
            objTicketMaster.setDescription(this.getString(FRM_DESCRIPTION));
            objTicketMaster.setType(this.getInt(FRM_TYPE));
            objTicketMaster.setContactId(this.getLong(FRM_CONTACT_ID));
            objTicketMaster.setAccCogsId(this.getLong(FRM_ACC_COGS_ID));
            objTicketMaster.setAccApId(this.getLong(FRM_ACC_AP_ID));
            this.objTicketMaster = objTicketMaster;
        } catch (Exception e) {
            objTicketMaster = new TicketMaster();
        }
    }
}
