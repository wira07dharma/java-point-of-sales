/* 
 * Ctrl Name  		:  CtrlDiscountType.java 
 * Created on 	:  [date] [time] AM/PM 
 * 
 * @author  		:  [authorName] 
 * @version  		:  [version] 
 */

/*******************************************************************
 * Class Description 	: [project description ... ] 
 * Imput Parameters 	: [input parameter ...] 
 * Output 		: [output ...] 
 *******************************************************************/

package com.dimata.posbo.form.warehouse;

import com.dimata.common.entity.location.Location;
import com.dimata.common.entity.location.PstLocation;
import com.dimata.util.lang.I_Language;
import com.dimata.util.Command;
import com.dimata.qdep.form.Control;
import com.dimata.qdep.form.FRMMessage;
import com.dimata.qdep.system.I_DBExceptionInfo;
import com.dimata.posbo.entity.warehouse.PstMaterialStockCode;
import com.dimata.posbo.entity.warehouse.MaterialStockCode;
import com.dimata.posbo.db.DBException;

import com.dimata.posbo.entity.warehouse.PriceProtection;
import com.dimata.posbo.entity.warehouse.PriceProtectionItem;
import com.dimata.posbo.entity.warehouse.PstPriceProtection;
import com.dimata.posbo.entity.warehouse.PstPriceProtectionItem;
import com.dimata.qdep.entity.I_DocStatus;
import java.util.Date;
import java.util.Vector;
import javax.servlet.http.HttpServletRequest;

import com.dimata.posbo.db.DBResultSet;
import com.dimata.posbo.db.DBHandler;
import java.sql.ResultSet;

public class CtrlMaterialStockCode extends Control implements I_Language
{
	public static int RSLT_OK = 0;
	public static int RSLT_UNKNOWN_ERROR = 1;
	public static int RSLT_EST_CODE_EXIST = 2;
	public static int RSLT_FORM_INCOMPLETE = 3;

	public static String[][] resultText = {
		{"Berhasil", "Tidak dapat diproses", "NoPerkiraan sudah ada", "Data tidak lengkap"},
		{"Succes", "Can not process", "Estimation code exist", "Data incomplete"}
	};

	private int start;
	private String msgString;
	private MaterialStockCode materialStockCode;
	private PstMaterialStockCode pstMaterialStockCode;
	private FrmMaterialStockCode frmMaterialStockCode;
	int language = LANGUAGE_DEFAULT;

	public CtrlMaterialStockCode(HttpServletRequest request){
		msgString = "";
		materialStockCode = new MaterialStockCode();
		try{
			pstMaterialStockCode = new PstMaterialStockCode(0);
		}catch(Exception e){;}
		frmMaterialStockCode = new FrmMaterialStockCode(request, materialStockCode);
	}

	private String getSystemMessage(int msgCode){
		switch (msgCode){
			case I_DBExceptionInfo.MULTIPLE_ID :
				this.frmMaterialStockCode.addError(FrmMaterialStockCode.FRM_FIELD_MATERIAL_STOCK_CODE_ID, resultText[language][RSLT_EST_CODE_EXIST] );
				return resultText[language][RSLT_EST_CODE_EXIST];
			default:
				return resultText[language][RSLT_UNKNOWN_ERROR]; 
		}
	}

	private int getControlMsgId(int msgCode){
		switch (msgCode){
			case I_DBExceptionInfo.MULTIPLE_ID :
				return RSLT_EST_CODE_EXIST;
			default:
				return RSLT_UNKNOWN_ERROR;
		}
	}

	public int getLanguage(){ return language; }

	public void setLanguage(int language){ this.language = language; }

	public MaterialStockCode getMaterialStockCode() { return materialStockCode; }

	public FrmMaterialStockCode getForm() { return frmMaterialStockCode; }

	public String getMessage(){ return msgString; }

	public int getStart() { return start; }

	public int action(int cmd , long oidMaterialStockCode){
		msgString = "";
		int excCode = I_DBExceptionInfo.NO_EXCEPTION;
		int rsCode = RSLT_OK;
		switch(cmd){
			case Command.ADD :
				break;

			case Command.SAVE :
				if(oidMaterialStockCode != 0){
					try{
						materialStockCode = PstMaterialStockCode.fetchExc(oidMaterialStockCode);
					}catch(Exception exc){
					}
				}

				frmMaterialStockCode.requestEntityObject(materialStockCode);

				if(frmMaterialStockCode.errorSize()>0) {
					msgString = FRMMessage.getMsg(FRMMessage.MSG_INCOMPLATE);
					return RSLT_FORM_INCOMPLETE ;
				}

				if(materialStockCode.getOID()==0){
					try{
						long oid = pstMaterialStockCode.insertExc(this.materialStockCode);
					}catch(DBException dbexc){
						excCode = dbexc.getErrorCode();
						msgString = getSystemMessage(excCode);
						return getControlMsgId(excCode);
					}catch (Exception exc){
						msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
						return getControlMsgId(I_DBExceptionInfo.UNKNOWN);
					}

				}else{
					try {
						long oid = pstMaterialStockCode.updateExc(this.materialStockCode);
					}catch (DBException dbexc){
						excCode = dbexc.getErrorCode();
						msgString = getSystemMessage(excCode);
					}catch (Exception exc){
						msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN); 
					}

				}
				break;

			case Command.EDIT :
				if (oidMaterialStockCode != 0) {
					try {
						materialStockCode = PstMaterialStockCode.fetchExc(oidMaterialStockCode);
					} catch (DBException dbexc){
						excCode = dbexc.getErrorCode();
						msgString = getSystemMessage(excCode);
					} catch (Exception exc){ 
						msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
					}
				}
				break;

			case Command.ASK :
				if (oidMaterialStockCode != 0) {
					try {
						materialStockCode = PstMaterialStockCode.fetchExc(oidMaterialStockCode);
					} catch (DBException dbexc){
						excCode = dbexc.getErrorCode();
						msgString = getSystemMessage(excCode);
					} catch (Exception exc){ 
						msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
					}
				}
				break;

			case Command.DELETE :
				if (oidMaterialStockCode != 0){
					try{
						long oid = PstMaterialStockCode.deleteExc(oidMaterialStockCode);
						if(oid!=0){
							msgString = FRMMessage.getMessage(FRMMessage.MSG_DELETED);
							excCode = RSLT_OK;
						}else{
							msgString = FRMMessage.getMessage(FRMMessage.ERR_DELETED);
							excCode = RSLT_FORM_INCOMPLETE;
						}
					}catch(DBException dbexc){
						excCode = dbexc.getErrorCode();
						msgString = getSystemMessage(excCode);
					}catch(Exception exc){	
						msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
					}
				}
				break;

			default :

		}
		return rsCode;
	}
        
        
        public int action(int cmd) {
            msgString = "";
            int excCode = I_DBExceptionInfo.NO_EXCEPTION;
            int rsCode = RSLT_OK;
            switch (cmd) {
              case Command.ADD:
                break;

              case Command.SAVE:  
                  
                //ambil semua data yang di inputkan  
                frmMaterialStockCode.requestEntityObjectMultiple();
                Vector listPriceProtection = frmMaterialStockCode.getGetListPriceProtection();
                
                //list semua berdasakan supplier
                Vector rekap = getRekapPriceProtection(listPriceProtection);
                try { 
                    if (rekap != null && rekap.size() > 0) {
                        for (int i = 0; i < rekap.size(); i++) {
                            
                              PriceProtection priceProtection = (PriceProtection) rekap.get(i); 
                              priceProtection.setPpCounter(getIntCode(priceProtection, priceProtection.getDateCreated(), 0, 0));
                              priceProtection.setNumberPP(getPriceProtectionNumber(priceProtection));
                              long oidPo = PstPriceProtection.insertExc(priceProtection);
                              Vector v1 = (Vector)priceProtection.getListProtectionItem();
                              if(oidPo!=0){
                                  double totAmount=0;
                                  for (int k = 0; k < v1.size(); k++) {
                                      PriceProtectionItem priceProtectionItem = (PriceProtectionItem) v1.get(k);
                                      priceProtectionItem.setPriceProtectionId(oidPo);
                                      PstPriceProtectionItem.insertExc(priceProtectionItem);
                                      //update harga jual
                                      PstMaterialStockCode.updateAmountAfterGetPP(priceProtectionItem.getSerialNumber(), priceProtectionItem.getAmount());
                                      //totalkan semua
                                      totAmount=totAmount+priceProtectionItem.getAmount();
                                  }
                                //update
                                int updateTotAmount = PstPriceProtection.updateTotAmount(oidPo, totAmount);
                              }
                        }
                    }
                } catch (Exception ex) {
                    System.out.println("autoInsertPo : " + ex);
                }
                break;
            }
            return rsCode;
        }
        
    public static Vector getRekapPriceProtection(Vector list) {
        Vector vpo = new Vector();
        try {
            if (list != null && list.size() > 0) {
                for (int k = 0; k < list.size(); k++) {
                    PriceProtectionItem priceProtectionItem = (PriceProtectionItem)list.get(k);
                    long supplierId= priceProtectionItem.getSupplierId();
                    long locationId=priceProtectionItem.getLocationId();
                    double amount = priceProtectionItem.getAmount();
                    String serialNumber=priceProtectionItem.getSerialNumber();
                    
                    if (vpo.size() > 0) {
                        boolean bool = false;
                        for (int i = 0; i < vpo.size(); i++) {
                            bool = false;
                            PriceProtection priceProtection = (PriceProtection) vpo.get(i);
//                            if ((priceProtection.getSupplierId() == supplierId)) {
//                                bool = true;
//                                
//                                PriceProtectionItem prItem = new PriceProtectionItem();
//                                prItem.setAmount(amount);
//                                prItem.setSerialNumber(serialNumber);
//                                priceProtection.setListProtectionItem(prItem);
//                                vpo.setElementAt(priceProtection, i);
//                            }
                        }
                        if (!bool) {
                            //header
                            PriceProtection mypo = new PriceProtection();
                            mypo.setStatus(I_DocStatus.DOCUMENT_STATUS_DRAFT);
                            mypo.setDateCreated(new Date());
                            mypo.setLocationId(locationId);
                            //mypo.setSupplierId(supplierId);
                            
                            //item
                            PriceProtectionItem prItem = new PriceProtectionItem();
                            prItem.setAmount(amount);
                            prItem.setSerialNumber(serialNumber);
                            mypo.setListProtectionItem(prItem);
                            vpo.add(mypo);
                        }
                    } else {
                        //header
                        PriceProtection mypo = new PriceProtection();
                        mypo.setStatus(I_DocStatus.DOCUMENT_STATUS_DRAFT);
                        mypo.setDateCreated(new Date());
                        mypo.setLocationId(locationId);
                        //mypo.setSupplierId(supplierId);
                        
                        
                        //item
                        PriceProtectionItem prItem = new PriceProtectionItem();
                        prItem.setAmount(amount);
                        prItem.setSerialNumber(serialNumber);
                        mypo.setListProtectionItem(prItem);
                        vpo.add(mypo);
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("Error in getRekapForPurchaseMain : " + e.toString());
        }
        return vpo;
    }
    
    
    public static String getPriceProtectionNumber(PriceProtection priceProtection) {
        String code = "PP";
        String dateCode = "";
        if (priceProtection.getDateCreated() != null) {
            Vector vctLocation = PstLocation.list(0, 0, PstLocation.fieldNames[PstLocation.FLD_LOCATION_ID]+"="+priceProtection.getLocationId(), "");
            Location location = (Location)vctLocation.get(0);
            
            int nextCounter = priceProtection.getPpCounter();//getMaxCounter(date);
            Date date = priceProtection.getDateCreated();
            
            int tgl = date.getDate();
            int bln = date.getMonth() + 1;
            int thn = date.getYear() + 1900;
            
            dateCode = (String.valueOf(thn)).substring(2, 4);
            
            if (bln < 10) {
                dateCode = dateCode + "0" + bln;
            } else {
                dateCode = dateCode + bln;
            }
            
            if (tgl < 10) {
                dateCode = dateCode + "0" + tgl;
            } else {
                dateCode = dateCode + tgl;
            }
            
            String counter = "";
            if (nextCounter < 10) {
                counter = "00" + nextCounter;
            } else {
                if (nextCounter < 100) {
                    counter = "0" + nextCounter;
                } else {
                    counter = "" + nextCounter;
                }
            }
            code = location.getCode() + "-" + dateCode + "-" + code + "-" + counter;
        }
        return code;
    }
    
    
    
    public static int getIntCode(PriceProtection priceProtection, Date pDate, long oid, int counter) {
        int max = 0;
        DBResultSet dbrs = null;
        Date date = new Date();
        try {
            String sql = "SELECT MAX(" + PstPriceProtection.fieldNames[PstPriceProtection.FLD_COUNTER] + ") AS PMAX" +
                         " FROM " + PstPriceProtection.TBL_POS_PRICE_PROTECTION + 
                         " WHERE YEAR(" + PstPriceProtection.fieldNames[PstPriceProtection.FLD_CREATE_DATE] + ") = " + (priceProtection.getDateCreated().getYear() + 1900) +
                         " AND MONTH(" + PstPriceProtection.fieldNames[PstPriceProtection.FLD_CREATE_DATE] + ") = " + (priceProtection.getDateCreated().getMonth() + 1) +
                         " AND " + PstPriceProtection.fieldNames[PstPriceProtection.FLD_POS_PRICE_PROTECTION_ID] + " <> " + oid +
                         " AND " + PstPriceProtection.fieldNames[PstPriceProtection.FLD_LOCATION_ID] +
                         " = '" + priceProtection.getLocationId()+"'";
            
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while (rs.next()) {
                max = rs.getInt("PMAX");
            }
            
            if (oid == 0) {
                max = max + 1;
            } else {
                if (priceProtection.getDateCreated() != pDate)
                    max = max + 1;
                else
                    max = counter;
            }
            
        } catch (Exception e) {
            System.out.println("SessMatReturn.getIntCode() err : " + e.toString());
        } finally {
            DBResultSet.close(dbrs);
        }
        return max;
    }
        
}
