/*
 * Session Name  	:  SessOrderMaterial.java 
 * Created on 	:  [date] [time] AM/PM 
 * 
 * @author  	:  [authorName] 
 * @version  	:  [version]
 */

/*******************************************************************
 * Class Description 	: [project description ... ] 
 * Imput Parameters 	: [input parameter ...] 
 * Output 		: [output ...] 
 *******************************************************************/
package com.dimata.posbo.session.purchasing;

import java.util.*;
import java.util.Date;
import com.dimata.qdep.entity.*;

public class SessRequestMaterial{

    //MatVendorPrice vdrMaterialPrice = new MatVendorPrice();
    public static final String className = I_DocType.DOCTYPE_CLASSNAME;

	public static final String SESS_SRC_REQUESTMATERIAL = "SESSION_SRC_REQUESTMATERIAL";
   	public static final String SESS_SRC_REQUESTMARKET = "SESSION_SRC_REQUESTMARKET";
	public static final String SESS_SRC_REQUESTASSET = "SESSION_SRC_REQUESTASSET";

        /*
	public static Vector searchOrderMaterial(SrcOrderMaterial srcordermaterial, int docType, int start, int recordToGet){
        Vector vectOrderCode = LogicParser.textSentence(srcordermaterial.getPrmnumber());
        for(int i=0; i<vectOrderCode.size(); i++){
            String name =(String)vectOrderCode.get(i);
            if((name.equals(LogicParser.SIGN))||(name.equals(LogicParser.ENGLISH[0])))
              vectOrderCode.remove(i);
        }
		return getOrderMaterialList(className,vectOrderCode,srcordermaterial,docType,start,recordToGet);
	}
         */

    /**
    * this method used to list material depend on srcOrderMaterial data
    */
        /*
    public static Vector getOrderMaterialList(String className, Vector vectNumber, SrcOrderMaterial srcordermaterial, int docType, int start, int recordToGet){
        DBResultSet dbrs = null;
        Vector result = new Vector(1,1);
    	try{

	        I_PstDocType i_pstDocType = (I_PstDocType) Class.forName(className).newInstance();

        	String sql = "SELECT MAT."+PstOrderMaterial.fieldNames[PstOrderMaterial.FLD_MATERIAL_ORDER_ID]+
                ", MAT."+PstOrderMaterial.fieldNames[PstOrderMaterial.FLD_PO_CODE]+
                ", MAT."+PstOrderMaterial.fieldNames[PstOrderMaterial.FLD_PURCH_DATE]+
                ", MAT."+PstOrderMaterial.fieldNames[PstOrderMaterial.FLD_DELIVERY_DATE]+
                ", MAT."+PstOrderMaterial.fieldNames[PstOrderMaterial.FLD_PO_STATUS]+
                ", MAT."+PstOrderMaterial.fieldNames[PstOrderMaterial.FLD_TERM_OF_AGREEMENT]+
                ", LOC."+PstLocation.fieldNames[PstLocation.FLD_NAME]+
                " FROM "+PstOrderMaterial.TBL_GRM_ORDER_MATERIAL+" AS MAT "+
                " INNER JOIN "+PstLocation.TBL_P2_LOCATION+" AS LOC "+
                " ON MAT."+PstOrderMaterial.fieldNames[PstOrderMaterial.FLD_LOCATION_ID]+
                " = LOC."+PstLocation.fieldNames[PstLocation.FLD_LOCATION_ID];

        		String strPoNumber = "";
				if(vectNumber!=null && vectNumber.size()>0){
                    for(int a=0; a<vectNumber.size(); a++){
                    	if(strPoNumber.length()!=0){
                        	strPoNumber = strPoNumber + " OR (MAT."+PstOrderMaterial.fieldNames[PstOrderMaterial.FLD_PO_CODE]+" LIKE '%"+vectNumber.get(a)+"%')";
                    	}else{
                        	strPoNumber = "(MAT."+PstOrderMaterial.fieldNames[PstOrderMaterial.FLD_PO_CODE]+" LIKE '%"+vectNumber.get(a)+"%')";
                    	}
                    }
                	strPoNumber = "("+strPoNumber+")";
                }

				String strDate = "";
            	if(srcordermaterial.getStatusdate()!=0){
					String startDate = Formater.formatDate(srcordermaterial.getPrmdatefrom(),"yyyy-MM-dd");
                    String endDate = Formater.formatDate(srcordermaterial.getPrmdateto(),"yyyy-MM-dd");
                    strDate = "MAT."+PstOrderMaterial.fieldNames[PstOrderMaterial.FLD_PURCH_DATE]+" BETWEEN '"+startDate+"' AND '"+endDate+"'";
            	}

				String strLocation = "";
            	if(srcordermaterial.getLocation()!=0){
                    strLocation = "MAT."+PstOrderMaterial.fieldNames[PstOrderMaterial.FLD_LOCATION_ID]+" = "+srcordermaterial.getLocation();
            	}

            	String strStatus = "";
            	if(srcordermaterial.getPrmstatus()!=null && srcordermaterial.getPrmstatus().size()>0){
                    for(int a=0; a<srcordermaterial.getPrmstatus().size(); a++){
                    	if(strStatus.length()!=0){
                        	strStatus = strStatus + " OR " + "(MAT."+PstOrderMaterial.fieldNames[PstOrderMaterial.FLD_PO_STATUS]+" ="+srcordermaterial.getPrmstatus().get(a)+")";
                    	}else{
                        	strStatus = "(MAT."+PstOrderMaterial.fieldNames[PstOrderMaterial.FLD_PO_STATUS]+" ="+srcordermaterial.getPrmstatus().get(a)+")";
                    	}
                    }
                	strStatus = "("+strStatus+")";
            	}

            	String whereClause = "";
                if(strPoNumber.length()>0)
                	whereClause = strPoNumber;

                if(strDate.length()>0){
                	if(whereClause.length()>0){
						whereClause = whereClause + " AND " + strDate;
                	}else{
						whereClause = whereClause + strDate;
                	}
                }

                if(strLocation.length()>0){
                	if(whereClause.length()>0){
						whereClause = whereClause + " AND " + strLocation;
                	}else{
						whereClause = whereClause + strLocation;
                	}
                }

                if(strStatus.length()>0){
                	if(whereClause.length()>0){
						whereClause = whereClause + " AND " + strStatus;
                	}else{
						whereClause = whereClause + strStatus;
                	}
                }

                if(whereClause.length()>0){
                    whereClause = whereClause +" AND MAT."+PstOrderMaterial.fieldNames[PstOrderMaterial.FLD_ORDER_TYPE]+"="+docType;
                }else{
					whereClause = whereClause + "MAT."+PstOrderMaterial.fieldNames[PstOrderMaterial.FLD_ORDER_TYPE]+"="+docType;
                }

                if(whereClause.length()>0)
                	sql = sql +" WHERE "+ whereClause;

                switch(srcordermaterial.getSortby()){
                	case 0:
                    	sql = sql + " ORDER BY MAT."+PstOrderMaterial.fieldNames[PstOrderMaterial.FLD_PO_CODE]+" LIMIT "+start+","+recordToGet;
                    	break;
                    case 1:
                    	sql = sql + " ORDER BY MAT."+PstOrderMaterial.fieldNames[PstOrderMaterial.FLD_PURCH_DATE]+" LIMIT "+start+","+recordToGet;
                    	break;
                    case 2:
                    	sql = sql + " ORDER BY LOC."+PstLocation.fieldNames[PstLocation.FLD_LOCATION_ID]+" LIMIT "+start+","+recordToGet;
						break;
                    case 3:
                    	sql = sql + " ORDER BY MAT."+PstOrderMaterial.fieldNames[PstOrderMaterial.FLD_PO_STATUS]+" LIMIT "+start+","+recordToGet;
						break;
                }
                System.out.println("sql : "+sql);

                dbrs = DBHandler.execQueryResult(sql);
                ResultSet rs = dbrs.getResultSet();
                while(rs.next()){
                    Vector vectTemp = new Vector(1,1);
                    OrderMaterial orderMaterial = new OrderMaterial();
                    Location location = new Location();

                    orderMaterial.setOID(rs.getLong(PstOrderMaterial.fieldNames[PstOrderMaterial.FLD_MATERIAL_ORDER_ID]));
					orderMaterial.setPoCode(rs.getString(PstOrderMaterial.fieldNames[PstOrderMaterial.FLD_PO_CODE]));
					orderMaterial.setPurchDate(rs.getDate(PstOrderMaterial.fieldNames[PstOrderMaterial.FLD_PURCH_DATE]));
					orderMaterial.setDeliveryDate(rs.getDate(PstOrderMaterial.fieldNames[PstOrderMaterial.FLD_DELIVERY_DATE]));
					orderMaterial.setPoStatus(rs.getInt(PstOrderMaterial.fieldNames[PstOrderMaterial.FLD_PO_STATUS]));
					orderMaterial.setTermOfAgreement(rs.getString(PstOrderMaterial.fieldNames[PstOrderMaterial.FLD_TERM_OF_AGREEMENT]));
                    vectTemp.add(orderMaterial);

                    location.setName(rs.getString(PstLocation.fieldNames[PstLocation.FLD_NAME]));
                    vectTemp.add(location);

                    result.add(vectTemp);
                }
                rs.close();

    	}catch(Exception e){
        	System.out.println("Err : "+e.toString());
    	}finally{
			DBResultSet.close(dbrs);
            return result;
    	}
    }
         */

	/**
    * this method used to get count of search result
    */
        /*
    public static int getCountOrderMaterialList(String className, Vector vectNumber, SrcOrderMaterial srcordermaterial, int docType){
        DBResultSet dbrs = null;
        int count = 0;
    	try{

	        I_PstDocType i_pstDocType = (I_PstDocType) Class.forName(className).newInstance();

        	String sql = "SELECT COUNT("+PstOrderMaterial.fieldNames[PstOrderMaterial.FLD_MATERIAL_ORDER_ID]+") AS CNT "+
                " FROM "+PstOrderMaterial.TBL_GRM_ORDER_MATERIAL+" AS MAT "+
                " INNER JOIN "+PstLocation.TBL_P2_LOCATION+" AS LOC "+
                " ON MAT."+PstOrderMaterial.fieldNames[PstOrderMaterial.FLD_LOCATION_ID]+
                " = LOC."+PstLocation.fieldNames[PstLocation.FLD_LOCATION_ID];

        		String strPoNumber = "";
				if(vectNumber!=null && vectNumber.size()>0){
                    for(int a=0;a<vectNumber.size();a++){
                    	if(strPoNumber.length()!=0){
                        	strPoNumber = strPoNumber + " OR " + "(MAT."+PstOrderMaterial.fieldNames[PstOrderMaterial.FLD_PO_CODE]+" LIKE '%"+vectNumber.get(a)+"%')";
                    	}else{
                        	strPoNumber = "(MAT."+PstOrderMaterial.fieldNames[PstOrderMaterial.FLD_PO_CODE]+" LIKE '%"+vectNumber.get(a)+"%')";
                    	}
                    }
                	strPoNumber = "("+strPoNumber+")";
                }

				String strDate = "";
            	if(srcordermaterial.getStatusdate()!=0){
					String startDate = Formater.formatDate(srcordermaterial.getPrmdatefrom(),"yyyy-MM-dd");
                    String endDate = Formater.formatDate(srcordermaterial.getPrmdateto(),"yyyy-MM-dd");
                    strDate = " MAT."+PstOrderMaterial.fieldNames[PstOrderMaterial.FLD_PURCH_DATE]+" BETWEEN '"+startDate+"' AND '"+endDate+"' ";
            	}

				String strLocation = "";
            	if(srcordermaterial.getLocation()!=0){
                    strLocation = "MAT."+PstOrderMaterial.fieldNames[PstOrderMaterial.FLD_LOCATION_ID]+" = "+srcordermaterial.getLocation();
            	}

            	String strStatus = "";
            	if(srcordermaterial.getPrmstatus()!=null && srcordermaterial.getPrmstatus().size()>0){
                    for(int a=0;a<srcordermaterial.getPrmstatus().size();a++){
                    	if(strStatus.length()!=0){
                        	strStatus = strStatus + " OR " + "(MAT."+PstOrderMaterial.fieldNames[PstOrderMaterial.FLD_PO_STATUS]+" ="+srcordermaterial.getPrmstatus().get(a)+")";
                    	}else{
                        	strStatus = "(MAT."+PstOrderMaterial.fieldNames[PstOrderMaterial.FLD_PO_STATUS]+" ="+srcordermaterial.getPrmstatus().get(a)+")";
                    	}
                    }
                	strStatus = "("+strStatus+")";
            	}

            	String whereClause = "";
                if(strPoNumber.length()>0)
                	whereClause = strPoNumber;

                if(strDate.length()>0){
                	if(whereClause.length()>0){
						whereClause = whereClause + " AND " + strDate;
                	}else{
						whereClause = whereClause + strDate;
                	}
                }

                if(strLocation.length()>0){
                	if(whereClause.length()>0){
						whereClause = whereClause + " AND " + strLocation;
                	}else{
						whereClause = whereClause + strLocation;
                	}
                }

                if(strStatus.length()>0){
                	if(whereClause.length()>0){
						whereClause = whereClause + " AND " + strStatus;
                	}else{
						whereClause = whereClause + strStatus;
                	}
                }

                if(whereClause.length()>0){
                    whereClause = whereClause +" AND "+ " MAT."+PstOrderMaterial.fieldNames[PstOrderMaterial.FLD_ORDER_TYPE]+"="+docType;
                }else{
					whereClause = whereClause + " MAT."+PstOrderMaterial.fieldNames[PstOrderMaterial.FLD_ORDER_TYPE]+"="+docType;
                }

                if(whereClause.length()>0)
                	sql = sql +" WHERE " +whereClause;

                 System.out.println("sql : "+sql);
                dbrs = DBHandler.execQueryResult(sql);
                ResultSet rs = dbrs.getResultSet();

                while(rs.next()){
                	count = rs.getInt("CNT");
                }
                rs.close();

    	}catch(Exception e){
        	System.out.println("Err : "+e.toString());
    	}finally{
			DBResultSet.close(dbrs);
            return count;
    	}
    }
         */

    /*
	public static int getCountSearch (SrcOrderMaterial srcordermaterial, int docType){
        int result = 0;
        Vector vectOrderCode = LogicParser.textSentence(srcordermaterial.getPrmnumber());
        if(vectOrderCode!=null && vectOrderCode.size()>0){
	        for(int i =0;i < vectOrderCode.size();i++){
	            String name =(String)vectOrderCode.get(i);
	            if((name.equals(LogicParser.SIGN))||(name.equals(LogicParser.ENGLISH[0])))
	              vectOrderCode.remove(i);
	        }
			result = getCountOrderMaterialList(className,vectOrderCode,srcordermaterial,docType);
        }
        return result;
	}
     */

        /*
    public static String getCodeOrderMaterial(OrderMaterial orderMaterial){
		String strCode = "";
		try{

	        I_PstDocType i_pstDocType = (I_PstDocType) Class.forName(className).newInstance();

            String documentCode = i_pstDocType.getDocCode(orderMaterial.getOrderType());
        	strCode = getYearMonth(orderMaterial.getPurchDate(),TYPE_GET_YEAR)+
            	""+getYearMonth(orderMaterial.getPurchDate(),TYPE_GET_MONTH)+
                "-"+documentCode+
                "-"+getCounter(orderMaterial.getPoCodeCounter());

        }catch(Exception e){
			System.out.println("Err : "+e.toString());
        }finally{
        	 return strCode;
        }
    }
         */

        /*
    public static int getIntCode(OrderMaterial orderMaterial, Date pDate, long oid, int orderType, int counter){
        int max = 0;
        DBResultSet dbrs = null;
        Date date = new Date();
    	try{
        	String sql = "SELECT MAX("+PstOrderMaterial.fieldNames[PstOrderMaterial.FLD_PO_CODE_COUNTER]+") AS PMAX"+
                " FROM "+PstOrderMaterial.TBL_GRM_ORDER_MATERIAL+
                " WHERE YEAR("+PstOrderMaterial.fieldNames[PstOrderMaterial.FLD_PURCH_DATE]+") = "+(orderMaterial.getPurchDate().getYear()+1900)+""+
                " AND MONTH("+PstOrderMaterial.fieldNames[PstOrderMaterial.FLD_PURCH_DATE]+") = "+(orderMaterial.getPurchDate().getMonth()+1)+
                " AND "+PstOrderMaterial.fieldNames[PstOrderMaterial.FLD_MATERIAL_ORDER_ID]+" <> '"+oid+"'"+
                " AND "+PstOrderMaterial.fieldNames[PstOrderMaterial.FLD_ORDER_TYPE]+" = "+orderMaterial.getOrderType();

            System.out.println("sql : "+sql);

        	dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();

        	while(rs.next()){
				max = rs.getInt("PMAX");
        	}
			rs.close();

        	if(oid==0){
				max = max + 1;
        	}else{
				if(orderMaterial.getPurchDate()!=pDate)
					max = max + 1;
                else
                    max = counter;
        	}

    	}catch(Exception e){
        	System.out.println("Err at counter : "+e.toString());
    	}finally{
    		DBResultSet.close(dbrs);
            return max;
    	}
    }
         */

    /**
    * this method used to getNextIndex of maximum number of PR number
    * return : int new number
    *
    * modified on : June 27, 2003 10:53 PM
    * modified by : gedhy
    */
        /*
    public static int getIntCode(OrderMaterial orderMaterial, Date pDate, long oid, int orderType, int counter, boolean isIncrement){
        int max = 0;
        DBResultSet dbrs = null;
        Date date = new Date();
    	try{
        	String sql = "SELECT MAX("+PstOrderMaterial.fieldNames[PstOrderMaterial.FLD_PO_CODE_COUNTER]+") AS PMAX"+
                " FROM "+PstOrderMaterial.TBL_GRM_ORDER_MATERIAL+
                " WHERE YEAR("+PstOrderMaterial.fieldNames[PstOrderMaterial.FLD_PURCH_DATE]+") " +
                " = "+(orderMaterial.getPurchDate().getYear()+1900)+""+
                " AND MONTH("+PstOrderMaterial.fieldNames[PstOrderMaterial.FLD_PURCH_DATE]+") " +
                " = "+(orderMaterial.getPurchDate().getMonth()+1)+
                " AND "+PstOrderMaterial.fieldNames[PstOrderMaterial.FLD_MATERIAL_ORDER_ID]+" <> '"+oid+"'";
        		if(isIncrement){
	                sql = sql + " AND "+PstOrderMaterial.fieldNames[PstOrderMaterial.FLD_ORDER_TYPE]+" IN ("+genStringPR()+")";
        		}else{
	                sql = sql + " AND "+PstOrderMaterial.fieldNames[PstOrderMaterial.FLD_ORDER_TYPE]+" = "+orderMaterial.getOrderType();
        		}

            System.out.println("##############################SessRequestMaterial.getIntCode() sql : "+sql);
        	dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
        	while(rs.next()){
				max = rs.getInt("PMAX");
        	}

        	if(oid==0){
				max = max + 1;
        	}else{
				if(orderMaterial.getPurchDate()!=pDate)
					max = max + 1;
                else
                    max = counter;
        	}
    	}catch(Exception e){
            System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!SessRequestMaterial.getIntCode() err : "+e.toString());
    	}finally{
    		DBResultSet.close(dbrs);
            return max;
    	}
    }
         */


    public static String getCounter(int counter){
        String strCounter = "";
        String str = String.valueOf(counter);
		switch(str.length()){
	        case 1 :
				strCounter = "00"+counter;
            	break;
	        case 2 :
	            strCounter = "0"+counter;
            	break;
	        case 3 :
                strCounter = ""+counter;
            	break;
            default:
            	strCounter = ""+counter;
        }
        return strCounter;
    }

	public static final int TYPE_GET_YEAR = 0;
    public static final int TYPE_GET_MONTH = 1;

	public static String getYearMonth(Date date, int getType){
        String str = "";
    	try{
    		switch(getType){
    			case TYPE_GET_YEAR :
                    	str = ""+date.getYear();
    					str = str.substring(str.length()-2,str.length());
                	break;

                case TYPE_GET_MONTH :
                    	str = ""+(date.getMonth()+1);
                        if(str.length()!=2)
    						str = "0"+str;
					break;

                default:
    		}

    	}catch(Exception e){
        	System.out.println("Err : "+e.toString());
    	}finally{
        	return str;
    	}
    }

    /**
    * this method used to generate String for whereClause in method "getIntCode()" above
    * return : String of PR type
    *
    * created on : June 27, 2003 10:37 PM
	* created by : gedhy
    */
        /*
    public static String genStringPR(){
        String result = "";

        // fill pr index to vector
        Vector vectPr = new Vector(1,1);
        try{
            I_PstDocType i_pstdoctype = (I_PstDocType) Class.forName(I_DocType.DOCTYPE_CLASSNAME).newInstance();
	        vectPr.add(String.valueOf(i_pstdoctype.composeDocumentType(I_DocType.SYSTEM_MATERIAL,I_DocType.MAT_DOC_TYPE_PRR)));
	        vectPr.add(String.valueOf(i_pstdoctype.composeDocumentType(I_DocType.SYSTEM_MATERIAL,I_DocType.MAT_DOC_TYPE_PRM)));
	        vectPr.add(String.valueOf(i_pstdoctype.composeDocumentType(I_DocType.SYSTEM_MATERIAL,I_DocType.MAT_DOC_TYPE_PRA)));
        }catch(Exception e){
            System.out.println("!!!!!!!!!!!!!!!!!!!!!SessRequestMaterial.genStringPR() err : "+e.toString());
        }

        // generate string result
        for(int i=0; i<vectPr.size(); i++){
            result = result + String.valueOf(vectPr.get(i)) + ",";
        }
        if(result!="" && result.length()>0){
            result = result.substring(0,result.length()-1);
        }

        return result;
    }
    */

        /*
	public static Vector getListMaterialItem(long oidOrderMaterial, long oidVendor){
        DBResultSet dbrs = null;
        Vector result = new Vector(1,1);
    	try{
        	String sql = "SELECT "+
                "  ITEM."+PstOrderMaterialItem.fieldNames[PstOrderMaterialItem.FLD_MAT_ORDER_ITEM_ID]+ //MAT_ORDER_ITEM_ID+
                " ,ITEM."+PstOrderMaterialItem.fieldNames[PstOrderMaterialItem.FLD_VENDOR_ID]+ //VENDOR_ID+
                " ,ITEM."+PstOrderMaterialItem.fieldNames[PstOrderMaterialItem.FLD_MATERIAL_ID]+ //MATERIAL_ID
                " ,ITEM."+PstOrderMaterialItem.fieldNames[PstOrderMaterialItem.FLD_COMMENT]+ //MATERIAL_ID
                " ,MAT."+PstMaterial.fieldNames[PstMaterial.FLD_CODE]+ // CODE
                " ,MAT."+PstMaterial.fieldNames[PstMaterial.FLD_NAME]+ //NAME
                " ,MAT."+PstMaterial.fieldNames[PstMaterial.FLD_DESCRIPTION]+ //DESCRIPTION
                " ,GRP."+PstMaterialGroup.fieldNames[PstMaterialGroup.FLD_CODE]+" AS GRP_"+PstMaterialGroup.fieldNames[PstMaterialGroup.FLD_CODE]+ //CODE
                " ,GRP."+PstMaterialGroup.fieldNames[PstMaterialGroup.FLD_NAME]+" AS GRP_"+PstMaterialGroup.fieldNames[PstMaterialGroup.FLD_NAME]+ //NAME
                " ,UNIT."+PstMatUnit.fieldNames[PstMatUnit.FLD_UNIT_NAME]+ //UNIT_NAME
                " ,ITEM."+PstOrderMaterialItem.fieldNames[PstOrderMaterialItem.FLD_QUANTITY]+ //QUANTITY
                " ,ITEM."+PstOrderMaterialItem.fieldNames[PstOrderMaterialItem.FLD_UNIT_PRICE]+ //UNIT_PRICE
                " ,ITEM."+PstOrderMaterialItem.fieldNames[PstOrderMaterialItem.FLD_AMOUNT]+ //AMOUNT
                " ,VDR."+PstMatVendorPrice.fieldNames[PstMatVendorPrice.FLD_VENDOR_MAT_CODE]+
				" FROM "+PstOrderMaterialItem.TBL_GRM_MAT_ORDER_ITEM+" AS ITEM "+//MAT_PURCHASE_ORDER_ITEM
				" INNER JOIN "+PstMaterial.TBL_MATERIAL+
                	" AS MAT ON ITEM."+PstOrderMaterialItem.fieldNames[PstOrderMaterialItem.FLD_MATERIAL_ID]+
                    " = MAT."+PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_ID]+
				" INNER JOIN "+PstMaterialGroup.TBL_MATERIAL_GROUP+ //GRM_MATERIAL_GROUP+
                	" AS GRP ON MAT."+PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_GROUP_ID]+ //MATERIAL_GROUP_ID+
                    " = GRP."+PstMaterialGroup.fieldNames[PstMaterialGroup.FLD_MATERIAL_GROUP_ID]+ //TBL_MATERIAL_GROUP+
				" INNER JOIN "+PstMatUnit.TBL_P2_UNIT+ //GRM_UNIT+
                	" AS UNIT ON MAT."+PstMaterial.fieldNames[PstMaterial.FLD_UNIT]+ //UNIT_ID+
                    " = UNIT."+PstMatUnit.fieldNames[PstMatUnit.FLD_UNIT_ID]+ //UNIT_ID
				" LEFT JOIN "+PstMatVendorPrice.TBL_GRM_VDR_MATERIAL_PRICE+ //GRM_VDR_MATERIAL_PRICE+
                	" AS VDR ON MAT."+PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_ID]+ //MATERIAL_ID+
                    " = VDR."+PstMatVendorPrice.fieldNames[PstMatVendorPrice.FLD_MATERIAL_ID]+ //MATERIAL_ID
				" WHERE ITEM."+PstOrderMaterialItem.fieldNames[PstOrderMaterialItem.FLD_MATERIAL_ORDER_ID]+" = "+oidOrderMaterial;

        	if(oidVendor!=0){
				sql = sql + " AND "+PstOrderMaterialItem.fieldNames[PstOrderMaterialItem.FLD_VENDOR_ID]+" = "+oidVendor;
        	}

        	System.out.println("sql list item : "+sql);

            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();

            while(rs.next()){
                Vector vt = new Vector();
			    OrderMaterialItem orderMaterialItem = new OrderMaterialItem();
			    Material material = new Material();
			    MaterialGroup materialGroup = new MaterialGroup();
			    MatUnit unit = new MatUnit();
                MatVendorPrice vdrMaterialPrice = new MatVendorPrice();

                orderMaterialItem.setOID(rs.getLong(PstOrderMaterialItem.fieldNames[PstOrderMaterialItem.FLD_MAT_ORDER_ITEM_ID]));
                orderMaterialItem.setQuantity(rs.getInt(PstOrderMaterialItem.fieldNames[PstOrderMaterialItem.FLD_QUANTITY]));
                orderMaterialItem.setUnitPrice(rs.getDouble(PstOrderMaterialItem.fieldNames[PstOrderMaterialItem.FLD_UNIT_PRICE]));
                orderMaterialItem.setAmount(rs.getDouble(PstOrderMaterialItem.fieldNames[PstOrderMaterialItem.FLD_AMOUNT]));
                orderMaterialItem.setAmount(rs.getDouble(PstOrderMaterialItem.fieldNames[PstOrderMaterialItem.FLD_COMMENT]));
                vt.add(orderMaterialItem);

                material.setOID(rs.getLong(PstOrderMaterialItem.fieldNames[PstOrderMaterialItem.FLD_MATERIAL_ID]));
                material.setCode(rs.getString(PstMaterial.fieldNames[PstMaterial.FLD_CODE]));
                material.setName(rs.getString(PstMaterial.fieldNames[PstMaterial.FLD_NAME]));
                material.setDescription(rs.getString(PstMaterial.fieldNames[PstMaterial.FLD_DESCRIPTION]));
                vt.add(material);

                materialGroup.setCode(rs.getString("GRP_"+PstMaterialGroup.fieldNames[PstMaterialGroup.FLD_CODE]));
                materialGroup.setName(rs.getString("GRP_"+PstMaterialGroup.fieldNames[PstMaterialGroup.FLD_NAME]));
                vt.add(materialGroup);

                unit.setUnitName(rs.getString(PstMatUnit.fieldNames[PstMatUnit.FLD_UNIT_NAME]));
                vt.add(unit);

                vdrMaterialPrice.setVendorMatCode(rs.getString(PstMatVendorPrice.fieldNames[PstMatVendorPrice.FLD_VENDOR_MAT_CODE]));
                vt.add(vdrMaterialPrice);

                result.add(vt);
            }

            rs.close();
    	}catch(Exception e){
        	System.out.println("err : "+e.toString());
    	}finally{
			DBResultSet.close(dbrs);
            return result;
    	}
    }
         */



    /**
    * this method used to get vector prItem
    * used in pr/po editor
    */
        /*
    public static Vector getVectPOItem(long poId, int start, int recordToGet){
        DBResultSet dbrs = null;
        Vector result = new Vector(1,1);
        try{
           String sql = "SELECT PI." + PstOrderMaterialItem.fieldNames[PstOrderMaterialItem.FLD_MAT_ORDER_ITEM_ID] +
						", PI." + PstOrderMaterialItem.fieldNames[PstOrderMaterialItem.FLD_MATERIAL_ORDER_ID] +
						", PI." + PstOrderMaterialItem.fieldNames[PstOrderMaterialItem.FLD_VENDOR_ID] +
						", PI." + PstOrderMaterialItem.fieldNames[PstOrderMaterialItem.FLD_MATERIAL_ID] +
						", PI." + PstOrderMaterialItem.fieldNames[PstOrderMaterialItem.FLD_QUANTITY] +
						", PI." + PstOrderMaterialItem.fieldNames[PstOrderMaterialItem.FLD_UNIT_PRICE] +
						", PI." + PstOrderMaterialItem.fieldNames[PstOrderMaterialItem.FLD_AMOUNT] +
						", PI." + PstOrderMaterialItem.fieldNames[PstOrderMaterialItem.FLD_STOCK_ON_HAND] +
						", PI." + PstOrderMaterialItem.fieldNames[PstOrderMaterialItem.FLD_LATES_PRICE_PAID] +
						", PI." + PstOrderMaterialItem.fieldNames[PstOrderMaterialItem.FLD_EXPECTED_DELIVERY_DATE] +
						", PI." + PstOrderMaterialItem.fieldNames[PstOrderMaterialItem.FLD_COMMENT] +
						", PI." + PstOrderMaterialItem.fieldNames[PstOrderMaterialItem.FLD_ITEM_STATUS] +
                        ", MAT." + PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_ID] +
                        ", MAT." + PstMaterial.fieldNames[PstMaterial.FLD_CODE] +
                        ", MAT." + PstMaterial.fieldNames[PstMaterial.FLD_NAME] +
                        ", UN." + PstMatUnit.fieldNames[PstMatUnit.FLD_UNIT_ID] +
                        ", UN." + PstMatUnit.fieldNames[PstMatUnit.FLD_UNIT_NAME] +
                        " FROM " + PstOrderMaterialItem.TBL_GRM_MAT_ORDER_ITEM + " AS PI " +
                        " INNER JOIN " + PstMaterial.TBL_MATERIAL + " AS MAT " +
                        " ON PI." + PstOrderMaterialItem.fieldNames[PstOrderMaterialItem.FLD_MATERIAL_ID] +
                        " = MAT." + PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_ID] +
                        " INNER JOIN " + PstMatUnit.TBL_P2_UNIT + " AS UN " +
                        " ON MAT." + PstMaterial.fieldNames[PstMaterial.FLD_UNIT] +
                        " = UN." + PstMatUnit.fieldNames[PstMatUnit.FLD_UNIT_ID] +
                        " WHERE " + PstOrderMaterialItem.fieldNames[PstOrderMaterialItem.FLD_MATERIAL_ORDER_ID] + " = " + poId;
			            switch (DBHandler.DBSVR_TYPE) {
			                case DBHandler.DBSVR_MYSQL :
									if(start == 0 && recordToGet == 0)
										sql = sql + "";
									else
										sql = sql + " LIMIT " + start + ","+ recordToGet ;
			
			                        break;
			
			                 case DBHandler.DBSVR_POSTGRESQL :
									if(start == 0 && recordToGet == 0)
										sql = sql + "";
									else
										sql = sql + " LIMIT " +recordToGet + " OFFSET "+ start ;
			
			                        break;
			
			                 case DBHandler.DBSVR_SYBASE :
			                    	break;
			
			                 case DBHandler.DBSVR_ORACLE :
			                    	break;
			
			                 case DBHandler.DBSVR_MSSQL :
			                    	break;
			
			                default:
			                    ;
			            }
           System.out.println("SessOrderMaterial.getVectPOItem() sql : "+sql);
           dbrs = DBHandler.execQueryResult(sql);
           ResultSet rs = dbrs.getResultSet();
           while(rs.next()){
            	Vector tempResult = new Vector(1,1);
                OrderMaterialItem pi = new OrderMaterialItem();
                Material mat = new Material();
                MatUnit matUnit = new MatUnit();

                pi.setOID(rs.getLong(PstOrderMaterialItem.fieldNames[PstOrderMaterialItem.FLD_MAT_ORDER_ITEM_ID]));
                pi.setMaterialOrderId(rs.getLong(PstOrderMaterialItem.fieldNames[PstOrderMaterialItem.FLD_MATERIAL_ORDER_ID]));
                pi.setVendorId(rs.getLong(PstOrderMaterialItem.fieldNames[PstOrderMaterialItem.FLD_VENDOR_ID]));
                pi.setMaterialId(rs.getLong(PstOrderMaterialItem.fieldNames[PstOrderMaterialItem.FLD_MATERIAL_ID]));
                pi.setQuantity(rs.getInt(PstOrderMaterialItem.fieldNames[PstOrderMaterialItem.FLD_QUANTITY]));
                pi.setUnitPrice(rs.getDouble(PstOrderMaterialItem.fieldNames[PstOrderMaterialItem.FLD_UNIT_PRICE]));
                pi.setAmount(rs.getDouble(PstOrderMaterialItem.fieldNames[PstOrderMaterialItem.FLD_AMOUNT]));
                pi.setStockOnHand(rs.getInt(PstOrderMaterialItem.fieldNames[PstOrderMaterialItem.FLD_STOCK_ON_HAND]));
                pi.setLatesPricePaid(rs.getDouble(PstOrderMaterialItem.fieldNames[PstOrderMaterialItem.FLD_LATES_PRICE_PAID]));
                pi.setExpectedDeliveryDate(rs.getDate(PstOrderMaterialItem.fieldNames[PstOrderMaterialItem.FLD_EXPECTED_DELIVERY_DATE]));
                pi.setComment(rs.getString(PstOrderMaterialItem.fieldNames[PstOrderMaterialItem.FLD_COMMENT]));
                pi.setItemStatus(rs.getInt(PstOrderMaterialItem.fieldNames[PstOrderMaterialItem.FLD_ITEM_STATUS]));
                tempResult.add(pi);

                mat.setOID(rs.getLong(PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_ID]));
                mat.setCode(rs.getString(PstMaterial.fieldNames[PstMaterial.FLD_CODE]));
                mat.setName(rs.getString(PstMaterial.fieldNames[PstMaterial.FLD_NAME]));
                tempResult.add(mat);

                matUnit.setOID(rs.getLong(PstMatUnit.fieldNames[PstMatUnit.FLD_UNIT_ID]));
                matUnit.setUnitName(rs.getString(PstMatUnit.fieldNames[PstMatUnit.FLD_UNIT_NAME]));
                tempResult.add(matUnit);

                result.add(tempResult);
           }
           rs.close();
        }catch(Exception e){
            System.out.println("SessOrderMaterial.getVectPOItem() err : "+e.toString());
        }finally{
            DBResultSet.close(dbrs);
            return result;
        }
    }
         */

    /**
    * this method used to get count of vector prItem
    * used in pr/po editor
    */
        /*
    public static int getCountVectPOItem(long poId){
        DBResultSet dbrs = null;
        int result = 0;
        try{
           String sql = "SELECT DISTINCT COUNT(PI." + PstOrderMaterialItem.fieldNames[PstOrderMaterialItem.FLD_MAT_ORDER_ITEM_ID] + ")" +
                        " FROM " + PstOrderMaterialItem.TBL_GRM_MAT_ORDER_ITEM + " AS PI " +
                        " INNER JOIN " + PstMaterial.TBL_MATERIAL + " AS MAT " +
                        " ON PI." + PstOrderMaterialItem.fieldNames[PstOrderMaterialItem.FLD_MATERIAL_ID] +
                        " = MAT." + PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_ID] +
                        " INNER JOIN " + PstMatUnit.TBL_P2_UNIT + " AS UN " +
                        " ON MAT." + PstMaterial.fieldNames[PstMaterial.FLD_UNIT] +
                        " = UN." + PstMatUnit.fieldNames[PstMatUnit.FLD_UNIT_ID] +
                        " WHERE " + PstOrderMaterialItem.fieldNames[PstOrderMaterialItem.FLD_MATERIAL_ORDER_ID] +
                        " = " + poId;
           System.out.println("SessOrderMaterial.getCountVectPOItem() sql : "+sql);
           dbrs = DBHandler.execQueryResult(sql);
           ResultSet rs = dbrs.getResultSet();
           while(rs.next()){
				result = rs.getInt(1);
           }
        }catch(Exception e){
            System.out.println("SessOrderMaterial.getCountVectPOItem() err : "+e.toString());
        }finally{
            DBResultSet.close(dbrs);
            return result;
        }
    }
         */

    public static boolean cekPurchDate(Date newDate,Date oldDate){

        if(newDate==null)
            return true;

        if(newDate.getMonth()!=oldDate.getMonth()){
            return true;
        }
		if(newDate.getYear()!=oldDate.getYear()){
            return true;
        }
        return false;
    }



	/**
    * this method used to list "final's PR" that will generate to be PO
    */
    /*
	public static Vector listFinalPr(int docType, int start, int recordToGet){
    	DBResultSet dbrs = null;
	    Vector result = new Vector(1,1);
        try{
	        String sql = "SELECT DISTINCT MAT."+PstOrderMaterial.fieldNames[PstOrderMaterial.FLD_MATERIAL_ORDER_ID]+
                	     ", MAT."+PstOrderMaterial.fieldNames[PstOrderMaterial.FLD_PO_CODE]+
                	     ", MAT."+PstOrderMaterial.fieldNames[PstOrderMaterial.FLD_PO_CODE_COUNTER]+
                	     ", MAT."+PstOrderMaterial.fieldNames[PstOrderMaterial.FLD_PURCH_DATE]+
				         ", MAT."+PstOrderMaterial.fieldNames[PstOrderMaterial.FLD_PO_STATUS]+
                         ", LOC."+PstLocation.fieldNames[PstLocation.FLD_LOCATION_ID]+
                         ", LOC."+PstLocation.fieldNames[PstLocation.FLD_NAME]+
				         " FROM "+PstOrderMaterial.TBL_GRM_ORDER_MATERIAL+" AS MAT "+
                         " INNER JOIN "+PstLocation.TBL_P2_LOCATION+" AS LOC "+
                         " ON MAT."+PstOrderMaterial.fieldNames[PstOrderMaterial.FLD_LOCATION_ID]+
                         " = LOC."+PstLocation.fieldNames[PstLocation.FLD_LOCATION_ID]+
                         " WHERE MAT."+PstOrderMaterial.fieldNames[PstOrderMaterial.FLD_ORDER_TYPE]+" = "+docType+
						 " AND MAT."+PstOrderMaterial.fieldNames[PstOrderMaterial.FLD_PO_STATUS]+" = "+I_DocStatus.DOCUMENT_STATUS_FINAL+
						 " ORDER BY MAT."+PstOrderMaterial.fieldNames[PstOrderMaterial.FLD_PO_CODE]+
                         ",MAT."+PstOrderMaterial.fieldNames[PstOrderMaterial.FLD_PURCH_DATE];
			            switch (DBHandler.DBSVR_TYPE) {
			                case DBHandler.DBSVR_MYSQL :
									if(start == 0 && recordToGet == 0)
										sql = sql + "";
									else
										sql = sql + " LIMIT " + start + ","+ recordToGet ;
			
			                        break;
			
			                 case DBHandler.DBSVR_POSTGRESQL :
									if(start == 0 && recordToGet == 0)
										sql = sql + "";
									else
										sql = sql + " LIMIT " +recordToGet + " OFFSET "+ start ;
			
			                        break;
			
			                 case DBHandler.DBSVR_SYBASE :
			                    	break;
			
			                 case DBHandler.DBSVR_ORACLE :
			                    	break;
			
			                 case DBHandler.DBSVR_MSSQL :
			                    	break;
			
			                default:
			                    ;
			            }
            System.out.println("SessRequestMaterial.listFinalPr() sql  : "+sql);
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while(rs.next()) {
                Vector vectTemp = new Vector(1,1);
                OrderMaterial ordMaterial = new OrderMaterial();
                Location location = new Location();

                ordMaterial.setOID(rs.getLong(1));
                ordMaterial.setPoCode(rs.getString(2));
                ordMaterial.setPoCodeCounter(rs.getInt(3));
                ordMaterial.setPurchDate(rs.getDate(4));
                ordMaterial.setPoStatus(rs.getInt(5));
                vectTemp.add(ordMaterial);

                location.setOID(rs.getLong(6));
                location.setName(rs.getString(7));
                vectTemp.add(location);

                result.add(vectTemp);
            }
        }catch(Exception e) {
	        System.out.println("SessRequestMaterial.lisFinalPr() err : "+e.toString());
	    }finally{
            DBResultSet.close(dbrs);
            return result;
        }
	}
     */

	/**
    * this method used to get count of "final's PR" that will generate to be PO
    */
    /*
	public static int countFinalPr(int docType, int start, int recordToGet){
    	DBResultSet dbrs = null;
	    int result = 0;
        try{
	        String sql = "SELECT DISTINCT COUNT("+ PstOrderMaterial.fieldNames[PstOrderMaterial.FLD_MATERIAL_ORDER_ID]+")"+
				         " FROM "+PstOrderMaterial.TBL_GRM_ORDER_MATERIAL+
                         " WHERE "+PstOrderMaterial.fieldNames[PstOrderMaterial.FLD_ORDER_TYPE]+" = "+docType+
						 " AND "+PstOrderMaterial.fieldNames[PstOrderMaterial.FLD_PO_STATUS]+" = "+I_DocStatus.DOCUMENT_STATUS_FINAL+
						 " ORDER BY "+PstOrderMaterial.fieldNames[PstOrderMaterial.FLD_PURCH_DATE];
                         if(start>0 || recordToGet>0){
				            sql = sql + " LIMIT "+start+","+recordToGet;
				         }
            System.out.println("SessRequestMaterial.countFinalPr() sql  : "+sql);
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while(rs.next()) {
                result = rs.getInt(1);
                break;
            }
        }catch(Exception e) {
	        System.out.println("SessRequestMaterial.countFinalPr() err : "+e.toString());
	    }finally{
            DBResultSet.close(dbrs);
            return result;
        }
	}
        */


	// untuk setting pr to po

	/**
    * this method used to generate strId from vector of oidPrItem
    */
    /*
	public static String generateStrIdPrItem(String strPrId){
    	DBResultSet dbrs = null;
	    String result = "";
        if(strPrId!="" && strPrId.length()>0){
	        try{
		        String sql = "SELECT " + PstOrderMaterialItem.fieldNames[PstOrderMaterialItem.FLD_MAT_ORDER_ITEM_ID] +
	                         " FROM " + PstOrderMaterialItem.TBL_GRM_MAT_ORDER_ITEM +
	                         " WHERE " + PstOrderMaterialItem.fieldNames[PstOrderMaterialItem.FLD_MATERIAL_ORDER_ID] +
                             " IN (" + strPrId + ")";

	            System.out.println("####################SessRequestMaterial.generateStrIdPrItem() sql  : "+sql);
	            dbrs = DBHandler.execQueryResult(sql);
	            ResultSet rs = dbrs.getResultSet();
	            while(rs.next()) {
	                result = result + rs.getLong(PstOrderMaterialItem.fieldNames[PstOrderMaterialItem.FLD_MAT_ORDER_ITEM_ID]) + ",";
                }
	            if(result.length()>0){ result = result.substring(0,result.length()-1); }
	        }catch(Exception e) {
	            System.out.println("!!!!!!!!!!!!!!!!!!!!SessPurchaseRequest.generateStrIdPrItem() err : "+e.toString());
		    }finally{
	            DBResultSet.close(dbrs);
	            return result;
	        }
        }
        return result;
	}
     */

	/**
    * this method used to list komulatif pr items based on prOid
    */
    /*
	public static Vector listComulatifPrItems(String strPrId){
    	DBResultSet dbrs = null;
	    Vector result = new Vector(1,1);
        if(strPrId!="" && strPrId.length()>0){
	        try{
		        String sql = "SELECT MAT." + PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_ID] + ", "+
	                         " MAT." + PstMaterial.fieldNames[PstMaterial.FLD_NAME] + "," +
							 " MT." + PstMatUnit.fieldNames[PstMatUnit.FLD_UNIT_ID] + ", " +
							 " MT." + PstMatUnit.fieldNames[PstMatUnit.FLD_UNIT_NAME] + ", " +
	                         " SUM(PI." + PstOrderMaterialItem.fieldNames[PstOrderMaterialItem.FLD_QUANTITY] + ") " +
	                         PstOrderMaterialItem.fieldNames[PstOrderMaterialItem.FLD_QUANTITY] +
	                         " FROM " + PstMaterial.TBL_MATERIAL + " MAT " +
                             " INNER JOIN " + PstOrderMaterialItem.TBL_GRM_MAT_ORDER_ITEM + " PI" +
                             " ON MAT." + PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_ID] +
                             " = PI." + PstOrderMaterialItem.fieldNames[PstOrderMaterialItem.FLD_MATERIAL_ID] +
                             " INNER JOIN " + PstMatUnit.TBL_P2_UNIT + " MT " +
                             " ON MAT." + PstMaterial.fieldNames[PstMaterial.FLD_UNIT] +
                             " = MT." + PstMatUnit.fieldNames[PstMatUnit.FLD_UNIT_ID] +
	                         " WHERE PI." + PstOrderMaterialItem.fieldNames[PstOrderMaterialItem.FLD_MATERIAL_ORDER_ID] + " IN (" + strPrId + ") " +
                             " AND PI." + PstOrderMaterialItem.fieldNames[PstOrderMaterialItem.FLD_ITEM_STATUS] +
                             " = " + PstOrderMaterialItem.ITEM_STATUS_WAITING +
							 " GROUP BY MAT." + PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_ID] +
	                         " ORDER BY MAT." + PstMaterial.fieldNames[PstMaterial.FLD_NAME];
	            System.out.println("#############################SessPurchaseRequest.listComulatifPrItems() sql  : "+sql);
	            dbrs = DBHandler.execQueryResult(sql);
	            ResultSet rs = dbrs.getResultSet();
	            while(rs.next()) {
                    Vector vectTemp = new Vector();
                    Material mat = new Material();
                    MatUnit mt = new MatUnit();
	                OrderMaterialItem ordMaterialItem = new OrderMaterialItem();

                    mat.setOID(rs.getLong(PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_ID]));
                    mat.setName(rs.getString(PstMaterial.fieldNames[PstMaterial.FLD_NAME]));
                    vectTemp.add(mat);

                    mt.setOID(rs.getLong(PstMatUnit.fieldNames[PstMatUnit.FLD_UNIT_ID]));
                    mt.setUnitName(rs.getString(PstMatUnit.fieldNames[PstMatUnit.FLD_UNIT_NAME]));
                    vectTemp.add(mt);

                    ordMaterialItem.setQuantity(rs.getInt(PstOrderMaterialItem.fieldNames[PstOrderMaterialItem.FLD_QUANTITY]));
                    vectTemp.add(ordMaterialItem);

	                result.add(vectTemp);
	            }
	            rs.close();
	        }catch(Exception e) {
		            System.out.println("SessPurchaseRequest.listComulatifPrItems() err : "+e.toString());
		    }finally{
	            DBResultSet.close(dbrs);
	            return result;
	        }
        }
        return result;
	}
     */

	/**
    * this method used to list all pr that contain specify material
    */
    /*
	public static Vector listPrOnSpecifyMaterial(String prId, long materialId, int docType){
    	DBResultSet dbrs = null;
	    Vector result = new Vector(1,1);
        if(prId!="" && prId.length()>0 && materialId!=0){
	        try{
                String sql = "SELECT PR." + PstOrderMaterial.fieldNames[PstOrderMaterial.FLD_MATERIAL_ORDER_ID] +
	                    	 ", PR." + PstOrderMaterial.fieldNames[PstOrderMaterial.FLD_PO_CODE] +
	                    	 ", PR." + PstOrderMaterial.fieldNames[PstOrderMaterial.FLD_PO_CODE_COUNTER] +
	                    	 ", PR." + PstOrderMaterial.fieldNames[PstOrderMaterial.FLD_PURCH_DATE] +
	                    	 ", PR." + PstOrderMaterial.fieldNames[PstOrderMaterial.FLD_PO_STATUS] +
	                    	 ", PR." + PstOrderMaterial.fieldNames[PstOrderMaterial.FLD_LOCATION_ID] +
							 ", LOC." + PstLocation.fieldNames[PstLocation.FLD_NAME] +
							 ", PRI." + PstOrderMaterialItem.fieldNames[PstOrderMaterialItem.FLD_MAT_ORDER_ITEM_ID] +
							 ", PRI." + PstOrderMaterialItem.fieldNames[PstOrderMaterialItem.FLD_QUANTITY] +
	                         " FROM " + PstMaterial.TBL_MATERIAL + " AS MAT " +
	                         " INNER JOIN " + PstOrderMaterialItem.TBL_GRM_MAT_ORDER_ITEM + " AS PRI " +
	                         " ON MAT." + PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_ID] +
	                         " = PRI." + PstOrderMaterialItem.fieldNames[PstOrderMaterialItem.FLD_MATERIAL_ID] +
	                         " INNER JOIN " + PstOrderMaterial.TBL_GRM_ORDER_MATERIAL + " AS PR " +
	                         " ON PRI." + PstOrderMaterialItem.fieldNames[PstOrderMaterialItem.FLD_MATERIAL_ORDER_ID] +
	                         " = PR." + PstOrderMaterial.fieldNames[PstOrderMaterial.FLD_MATERIAL_ORDER_ID] +
	                         " INNER JOIN " + PstLocation.TBL_P2_LOCATION + " AS LOC " +
	                         " ON PR." + PstOrderMaterial.fieldNames[PstOrderMaterial.FLD_LOCATION_ID] +
	                         " = LOC." + PstLocation.fieldNames[PstLocation.FLD_LOCATION_ID] +
	                         " WHERE MAT." + PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_ID] +
	                         " = " + materialId +
	                         " AND PR." + PstOrderMaterial.fieldNames[PstOrderMaterial.FLD_MATERIAL_ORDER_ID] +
	                         " IN (" + prId + ") " +
	                         " AND PR." + PstOrderMaterial.fieldNames[PstOrderMaterial.FLD_ORDER_TYPE] +
	                         " = " + docType+
                             " ORDER BY PR." + PstOrderMaterial.fieldNames[PstOrderMaterial.FLD_PO_CODE] +
                             ", PR." + PstOrderMaterial.fieldNames[PstOrderMaterial.FLD_PO_CODE_COUNTER];

	            System.out.println("############################SessRequestMaterial.listPrOnSpecifyMaterial() sql  : "+sql);
	            dbrs = DBHandler.execQueryResult(sql);
	            ResultSet rs = dbrs.getResultSet();
	            while(rs.next()) {
                    Vector vectTemp = new Vector();
	                OrderMaterial ordMaterial = new OrderMaterial();
                    Location loc = new Location();
	                OrderMaterialItem ordMaterialItem = new OrderMaterialItem();

                    ordMaterial.setOID(rs.getLong(PstOrderMaterial.fieldNames[PstOrderMaterial.FLD_MATERIAL_ORDER_ID]));
                    ordMaterial.setPoCode(rs.getString(PstOrderMaterial.fieldNames[PstOrderMaterial.FLD_PO_CODE]));
                    ordMaterial.setPoCodeCounter(rs.getInt(PstOrderMaterial.fieldNames[PstOrderMaterial.FLD_PO_CODE_COUNTER]));
                    ordMaterial.setPurchDate(rs.getDate(PstOrderMaterial.fieldNames[PstOrderMaterial.FLD_PURCH_DATE]));
                    ordMaterial.setPoStatus(rs.getInt(PstOrderMaterial.fieldNames[PstOrderMaterial.FLD_PO_STATUS]));
                    vectTemp.add(ordMaterial);

                    loc.setName(rs.getString(PstLocation.fieldNames[PstLocation.FLD_NAME]));
                    vectTemp.add(loc);

                    ordMaterialItem.setOID(rs.getLong(PstOrderMaterialItem.fieldNames[PstOrderMaterialItem.FLD_MAT_ORDER_ITEM_ID]));
                    ordMaterialItem.setQuantity(rs.getInt(PstOrderMaterialItem.fieldNames[PstOrderMaterialItem.FLD_QUANTITY]));
                    vectTemp.add(ordMaterialItem);

	                result.add(vectTemp);
	            }
	        }catch(Exception e) {
		        System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!SessRequestMaterial.listPrOnSpecifyMaterial() err : "+e.toString());
		    }finally{
	            DBResultSet.close(dbrs);
	            return result;
	        }
        }
        return result;
	}
     */


    public static void main(String args[]){
		//System.out.println("genStringPR : "+SessRequestMaterial.genStringPR());
    }

}
