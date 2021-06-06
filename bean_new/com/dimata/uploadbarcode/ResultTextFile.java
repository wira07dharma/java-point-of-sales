package com.dimata.uploadbarcode;

import com.dimata.common.entity.system.PstSystemProperty;
import java.util.Date;
import java.util.Hashtable;
import java.util.Vector;

/**
 * Created by IntelliJ IDEA.
 * User: gadnyana
 * Date: Jul 12, 2007
 * Time: 3:17:11 PM
 * To change this template use File | Settings | File Templates.
 */
public class ResultTextFile {

    private Hashtable has = null;
    private String locCode = "";
    private String prodBarcode = "";
    private int qty = 0;
    private Date datetime = null;

    //getOnlyDate
    private Date onlyDate = null;

    public void setResultTextFile(String locCode, String prodBarcode, int qty, Date datetime, Date onlyDate){
        ResultTextFile resultTextFile = new ResultTextFile();
        resultTextFile.locCode = locCode;
        resultTextFile.prodBarcode = prodBarcode;
        resultTextFile.qty = qty;
        resultTextFile.datetime = datetime;
        //only date
        resultTextFile.onlyDate = onlyDate;
		
		int typeTxt = 0;
		try {
			typeTxt = Integer.valueOf(PstSystemProperty.getValueByName("OPNAME_FILE_TYPE"));
		} catch (Exception exc){

		}
		
        try{
            Vector list = null;
            try{
                list = (Vector)has.get(resultTextFile.locCode);
            }catch(Exception e){
                has = new Hashtable();
            }
            if(list==null){
                list = new Vector(1,1);
                list.add(resultTextFile);
                has.put(resultTextFile.locCode,list);
            }else{
                boolean sts = false;
				if (typeTxt == 1){
                for(int k=0;k<list.size();k++){
                    ResultTextFile result = (ResultTextFile)list.get(k);
                   if(result.getProdBarcode().equals(resultTextFile.getProdBarcode())){
                        resultTextFile.setQty(resultTextFile.getQty()+result.getQty());
                        list.setElementAt(resultTextFile,k);
                        sts = true;
                        break;
                    }
                }
				}
                if(!sts){
                    list.add(resultTextFile);
                }
                has.put(resultTextFile.locCode,list);
            }
        }catch(Exception e){}
    }

    public Hashtable getResultTextFile(){
        return has;
    }

    public String getLocCode() {
        return locCode;
    }

    public String getProdBarcode() {
        return prodBarcode;
    }

    public void setQty(int qtyp) {
        this.qty = qtyp;
    }

    public int getQty() {
        return qty;
    }

    public Date getDatetime() {
        return datetime;
    }

    /**
     * @return the onlyDate
     */
    public Date getOnlyDate() {
        return onlyDate;
    }
}
