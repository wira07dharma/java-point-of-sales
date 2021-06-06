/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.dimata.aiso.form.masterdata;

import com.dimata.aiso.entity.masterdata.TicketList;
import com.dimata.qdep.form.FRMHandler;
import com.dimata.qdep.form.I_FRMInterface;
import com.dimata.qdep.form.I_FRMType;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author dwi
 */
public class FrmTicketList extends FRMHandler implements I_FRMInterface, I_FRMType{

    public static final String FRM_TICKET_LIST = "FRM_TICKET_LIST";
    
    public static final int FRM_CARRIER_ID = 0;
    public static final int FRM_TICKET_NUMBER = 1;
    public static final int FRM_DEPOSIT_ID = 2;
    
    
    public static String[] fieldNames = {
	"FRM_CARRIER_ID",
	"FRM_TICKET_NUMBER",
	"FRM_DEPOSIT_ID"
    };
    
    public static int[] fieldTypes = {
	TYPE_LONG,
	TYPE_STRING,
	TYPE_LONG
    };
    
    private TicketList objTicketList;

    public FrmTicketList(TicketList objTicketList) {
        this.objTicketList = objTicketList;
    }

    public FrmTicketList(HttpServletRequest request, TicketList objTicketList) {
        super(new FrmTicketList(objTicketList), request);
        this.objTicketList = objTicketList;
    }

    public String getFormName() {
        return FRM_TICKET_LIST;
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

    public TicketList getEntityObject() {
        return objTicketList;
    }

    public void requestEntityObject(TicketList objTicketList) {
        try {
            this.requestParam();
            objTicketList.setCarrierId(this.getLong(FRM_CARRIER_ID));
            objTicketList.setTicketNumber(this.getString(FRM_TICKET_NUMBER));
            objTicketList.setTicketDepositId(this.getLong(FRM_DEPOSIT_ID));
	    
            this.objTicketList = objTicketList;
        } catch (Exception e) {
            objTicketList = new TicketList();
        }
    }
}
