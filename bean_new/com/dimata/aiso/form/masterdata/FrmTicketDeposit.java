/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.dimata.aiso.form.masterdata;

import com.dimata.aiso.entity.masterdata.TicketDeposit;
import com.dimata.qdep.form.FRMHandler;
import com.dimata.qdep.form.I_FRMInterface;
import com.dimata.qdep.form.I_FRMType;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author dwi
 */
public class FrmTicketDeposit extends FRMHandler implements I_FRMInterface, I_FRMType{

    public static final String FRM_TICKET_DEPOSIT = "FRM_TICKET_DEPOSIT";
    
    public static final int FRM_CARRIER_ID = 0;
    public static final int FRM_DEPOSIT_DATE = 1;
    public static final int FRM_DEPOSIT_AMOUNT = 2;
    public static final int FRM_DESCRIPTION = 3;
    
    public static String[] fieldNames = {
	"FRM_CARRIER_ID",
	"FRM_DEPOSIT_DATE",
	"FRM_DEPOSIT_AMOUNT",
	"FRM_DESCRIPTION"
    };
    
    public static int[] fieldTypes = {
	TYPE_LONG,
	TYPE_DATE,
	TYPE_FLOAT,
	TYPE_STRING
    };
    
    private TicketDeposit objTicketDeposit;

    public FrmTicketDeposit(TicketDeposit objTicketDeposit) {
        this.objTicketDeposit = objTicketDeposit;
    }

    public FrmTicketDeposit(HttpServletRequest request, TicketDeposit objTicketDeposit) {
        super(new FrmTicketDeposit(objTicketDeposit), request);
        this.objTicketDeposit = objTicketDeposit;
    }

    public String getFormName() {
        return FRM_TICKET_DEPOSIT;
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

    public TicketDeposit getEntityObject() {
        return objTicketDeposit;
    }

    public void requestEntityObject(TicketDeposit objTicketDeposit) {
        try {
            this.requestParam();
            objTicketDeposit.setCarrierId(this.getLong(FRM_CARRIER_ID));
            objTicketDeposit.setDepositDate(this.getDate(FRM_DEPOSIT_DATE));
            objTicketDeposit.setDepositAmount(this.getDouble(FRM_DEPOSIT_AMOUNT));
            objTicketDeposit.setDescription(this.getString(FRM_DESCRIPTION));
	    
            this.objTicketDeposit = objTicketDeposit;
        } catch (Exception e) {
            objTicketDeposit = new TicketDeposit();
        }
    }
}
