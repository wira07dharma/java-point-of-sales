package com.dimata.posbo.session.warehouse;

import com.dimata.qdep.form.Control;
import com.dimata.posbo.db.DBHandler;
import com.dimata.posbo.db.DBResultSet;
import com.dimata.qdep.entity.I_PstDocType;
import com.dimata.qdep.entity.I_DocType;
import com.dimata.posbo.entity.warehouse.*;
import com.dimata.posbo.entity.search.SrcMatConDispatch;
import com.dimata.posbo.entity.search.SrcMatRequest;
import com.dimata.posbo.entity.search.SrcStockCard;
import com.dimata.common.entity.location.PstLocation;
import com.dimata.common.entity.location.Location;
import com.dimata.util.Formater;
import com.dimata.util.Command;
import com.dimata.posbo.entity.warehouse.StockCardReport;
import com.dimata.posbo.entity.warehouse.PstStockCardReport;
import com.dimata.posbo.entity.masterdata.Material;
import com.dimata.posbo.entity.masterdata.PstMaterial;
import com.dimata.posbo.entity.masterdata.PstUnit;

import java.util.Vector;
import java.util.Date;
import java.sql.ResultSet;

public class SessMatConDispatch extends Control {

    public static final String SESS_SRC_MATDISPATCH = "SESSION_MATERIAL_DISPATCH";
    public static final String SESSION_MATERIAL_DISPATCH_EXC = "SESSION_MATERIAL_DISPATCH_EXC";
    public static final String SESSION_TRANSFER_MR_TO_DF = "SESSION_TRANSFER_MR_TO_DF";
    public static final String SESSION_MATERIAL_DISPATCH_RECEIVED = "SESSION_MATERIAL_DISPATCH_RECEIVED";

    public static final String[] orderBy =
            {
                PstMatConDispatch.fieldNames[PstMatConDispatch.FLD_DISPATCH_CODE],
                PstMatConDispatch.fieldNames[PstMatConDispatch.FLD_LOCATION_ID],
                PstMatConDispatch.fieldNames[PstMatConDispatch.FLD_DISPATCH_TO],
                PstMatConDispatch.fieldNames[PstMatConDispatch.FLD_DISPATCH_DATE],
                PstMatConDispatch.fieldNames[PstMatConDispatch.FLD_DISPATCH_STATUS]
            };

    /* list for material request */
    public static Vector listMatConDispatch(SrcMatConDispatch srcMatConDispatch, int start, int limit) {
        Vector result = new Vector(1, 1);
        String sql = "SELECT DF." + PstMatConDispatch.fieldNames[PstMatConDispatch.FLD_DISPATCH_MATERIAL_ID] +
                " , DF." + PstMatConDispatch.fieldNames[PstMatConDispatch.FLD_DISPATCH_CODE] +
                " , DF." + PstMatConDispatch.fieldNames[PstMatConDispatch.FLD_DISPATCH_DATE] +
                " , LOC." + PstLocation.fieldNames[PstLocation.FLD_NAME] +
                " , DF." + PstMatConDispatch.fieldNames[PstMatConDispatch.FLD_DISPATCH_TO] +
                " , DF." + PstMatConDispatch.fieldNames[PstMatConDispatch.FLD_DISPATCH_STATUS] +
                " , DF." + PstMatConDispatch.fieldNames[PstMatConDispatch.FLD_REMARK] +
                " FROM " + PstMatConDispatch.TBL_DISPATCH + " DF" +
                " INNER JOIN " + PstLocation.TBL_P2_LOCATION + " LOC" +
                " ON DF." + PstMatConDispatch.fieldNames[PstMatConDispatch.FLD_LOCATION_ID] +
                " = LOC." + PstLocation.fieldNames[PstLocation.FLD_LOCATION_ID];


        if (srcMatConDispatch != null) {
            String fromLocation = "";
            if (srcMatConDispatch.getDispatchFrom() != 0) {
                fromLocation = "(DF." + PstMatConDispatch.fieldNames[PstMatConDispatch.FLD_LOCATION_ID] + "=" + srcMatConDispatch.getDispatchFrom() + ")";
            }

            String toLocation = "";
            if (srcMatConDispatch.getDispatchTo() != 0) {
                toLocation = "(DF." + PstMatConDispatch.fieldNames[PstMatConDispatch.FLD_DISPATCH_TO] + "=" + srcMatConDispatch.getDispatchTo() + ")";
            }

            String date = "";
            if (!srcMatConDispatch.getIgnoreDate()) {
                date = "(DF." + PstMatConDispatch.fieldNames[PstMatConDispatch.FLD_DISPATCH_DATE] + " BETWEEN '" + Formater.formatDate(srcMatConDispatch.getDispatchDateFrom(), "yyyy-MM-dd 00:00:00") + "' AND '" +
                        Formater.formatDate(srcMatConDispatch.getDispatchDateTo(), "yyyy-MM-dd 23:59:59") + "')";
            }

            String dfCode = "";
            if (srcMatConDispatch.getDispatchCode().length() > 0) {
                dfCode = "(DF." + PstMatConDispatch.fieldNames[PstMatConDispatch.FLD_DISPATCH_CODE] + " LIKE '" + srcMatConDispatch.getDispatchCode() + "%')";
            }

            String status = "";
            if (srcMatConDispatch.getStatus() >= 0) {
                status = "(DF." + PstMatConDispatch.fieldNames[PstMatConDispatch.FLD_DISPATCH_STATUS] + " = " + (srcMatConDispatch.getStatus()) + ")";
            }

            String where = "";
            if (fromLocation.length() > 0) {
                if (where.length() > 0) {
                    where = where + " AND " + fromLocation;
                } else {
                    where = fromLocation;
                }
            }

            if (toLocation.length() > 0) {
                if (where.length() > 0) {
                    where = where + " AND " + toLocation;
                } else {
                    where = toLocation;
                }
            }

            if (date.length() > 0) {
                if (where.length() > 0) {
                    where = where + " AND " + date;
                } else {
                    where = date;
                }
            }

            if (dfCode.length() > 0) {
                if (where.length() > 0) {
                    where = where + " AND " + dfCode;
                } else {
                    where = dfCode;
                }
            }

            if (status.length() > 0) {
                if (where.length() > 0) {
                    where = where + " AND " + status;
                } else {
                    where = status;
                }
            }

            if (where.length() > 0) {
                where = " WHERE	" + where;
            }

            sql += where;
            sql += " ORDER BY DF." + orderBy[srcMatConDispatch.getSortBy()];
        }

        switch (DBHandler.DBSVR_TYPE) {
            case DBHandler.DBSVR_MYSQL:
                if (start == 0 && limit == 0)
                    sql = sql + "";
                else
                    sql = sql + " LIMIT " + start + "," + limit;
                break;
            case DBHandler.DBSVR_POSTGRESQL:
                if (start == 0 && limit == 0)
                    sql = sql + "";
                else
                    sql = sql + " LIMIT " + limit + " OFFSET " + start;
                break;
            case DBHandler.DBSVR_SYBASE:
                break;
            case DBHandler.DBSVR_ORACLE:
                break;
            case DBHandler.DBSVR_MSSQL:
                break;
            default:
                ;
        }


        DBResultSet dbrs = null;
        try {
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while (rs.next()) {
                Vector temp = new Vector();
                MatConDispatch matDf = new MatConDispatch();
                Location loc1 = new Location();

                matDf.setOID(rs.getLong(1));
                matDf.setDispatchCode(rs.getString(2));
                matDf.setDispatchDate(rs.getDate(3));
                matDf.setDispatchTo(rs.getLong(5));
                matDf.setDispatchStatus(rs.getInt(6));
                matDf.setRemark(rs.getString(7));
                temp.add(matDf);

                loc1.setName(rs.getString(4));
                temp.add(loc1);

                result.add(temp);
            }
            rs.close();
        } catch (Exception e) {
            System.out.println("exception on search DF : " + e.toString());
        } finally {
            DBResultSet.close(dbrs);
        }
        return result;
    }


    public static int getCountMatConDispatch(SrcMatConDispatch srcMatConDispatch) {

        int result = 0;
        String sql = "SELECT COUNT(DF." + PstMatConDispatch.fieldNames[PstMatConDispatch.FLD_DISPATCH_MATERIAL_ID] +
                " ) AS CNT FROM " + PstMatConDispatch.TBL_DISPATCH + " DF" +
                " INNER JOIN " + PstLocation.TBL_P2_LOCATION + " LOC" +
                " ON DF." + PstMatConDispatch.fieldNames[PstMatConDispatch.FLD_LOCATION_ID] +
                " = LOC." + PstLocation.fieldNames[PstLocation.FLD_LOCATION_ID];

        if (srcMatConDispatch != null) {
            String fromLocation = "";

            if (srcMatConDispatch.getDispatchFrom() != 0) {
                fromLocation = "(DF." + PstMatConDispatch.fieldNames[PstMatConDispatch.FLD_LOCATION_ID] + "=" + srcMatConDispatch.getDispatchFrom() + ")";
            }

            String toLocation = "";
            if (srcMatConDispatch.getDispatchTo() != 0) {
                toLocation = "(DF." + PstMatConDispatch.fieldNames[PstMatConDispatch.FLD_DISPATCH_TO] + "=" + srcMatConDispatch.getDispatchTo() + ")";
            }

            String date = "";
            if (!srcMatConDispatch.getIgnoreDate()) {
                date = "(DF." + PstMatConDispatch.fieldNames[PstMatConDispatch.FLD_DISPATCH_DATE] + " BETWEEN '" + Formater.formatDate(srcMatConDispatch.getDispatchDateFrom(), "yyyy-MM-dd") + "' AND '" +
                        Formater.formatDate(srcMatConDispatch.getDispatchDateTo(), "yyyy-MM-dd") + "')";
            }

            String dfCode = "";
            if (srcMatConDispatch.getDispatchCode().length() > 0) {
                dfCode = "(DF." + PstMatConDispatch.fieldNames[PstMatConDispatch.FLD_DISPATCH_CODE] + " LIKE '" + srcMatConDispatch.getDispatchCode() + "%')";
            }

            String status = "";
            if (srcMatConDispatch.getStatus() >= 0) {
                status = "(DF." + PstMatConDispatch.fieldNames[PstMatConDispatch.FLD_DISPATCH_STATUS] + " = " + (srcMatConDispatch.getStatus()) + ")";
            }

            String where = "";
            if (fromLocation.length() > 0) {
                if (where.length() > 0) {
                    where = where + " AND " + fromLocation;
                } else {
                    where = fromLocation;
                }
            }

            if (toLocation.length() > 0) {
                if (where.length() > 0) {
                    where = where + " AND " + toLocation;
                } else {
                    where = toLocation;
                }
            }

            if (date.length() > 0) {
                if (where.length() > 0) {
                    where = where + " AND " + date;
                } else {
                    where = date;
                }
            }

            if (dfCode.length() > 0) {
                if (where.length() > 0) {
                    where = where + " AND " + dfCode;
                } else {
                    where = dfCode;
                }
            }

            if (status.length() > 0) {
                if (where.length() > 0) {
                    where = where + " AND " + status;
                } else {
                    where = status;
                }
            }

            if (where.length() > 0) {
                where = " WHERE	" + where;
            }
            sql += where;
        }
        DBResultSet dbrs = null;
        try {
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while (rs.next()) {
                result = rs.getInt(1);
            }
            rs.close();
        } catch (Exception e) {
            System.out.println("exception on count search DF : " + e.toString());
        } finally {
            DBResultSet.close(dbrs);
        }
        return result;
    }

    public static int findLimitStart(long oid, int recordToGet, SrcMatConDispatch srcMaterial) {
        /*String order = "";
        int size = getCount(srcMaterial);
        int start = 0;
        boolean found =false;
        for(int i=0; (i < size) && !found ; i=i+recordToGet){
             Vector list =  listMatConDispatch(srcMaterial,i,recordToGet);
             start = i;
             if(list.size()>0){
              for(int ls=0;ls<list.size();ls++){
                     MatConDispatch materialdispatch = (MatConDispatch)list.get(ls);
                   if(oid == materialdispatch.getOID())
                      found=true;
              }
          }
        }
        if((start >= size) && (size > 0))
            start = start - recordToGet;

        return start;*/
        return 0;
    }


    public String generateRequestCode(MatConDispatch materialDf) {

        String code = "DR";
        /*String dateCode = "";
        if(materialDf.getDfmDate()!=null){

        	int nextCounter = materialDf.getDispatchCodeCounter();//getMaxCounter(date);

            Date date = materialDf.getDfmDate();

            int tgl = date.getDate();
            int bln = date.getMonth() + 1;
            int thn = date.getYear() + 1900;

            if(tgl<10){
				dateCode = "0"+tgl;
            }
            else{
                dateCode = ""+tgl;
            }

            if(bln<10){
                dateCode = dateCode+"0"+bln;
            }
            else{
                dateCode = dateCode+""+bln;
            }

            dateCode = dateCode + (String.valueOf(thn)).substring(2,4);

            String counter = "";
            if(nextCounter<10){
                counter = "00"+nextCounter;
            }
            else{
                if(nextCounter<100){
                    counter = "0"+nextCounter;
                }
                else{
                    counter = ""+counter;
                }
            }

            code = code+"-"+dateCode+"-"+counter;
        }
        */
        return code;
    }

    public static int getMaxReqCounter(Date date) {
        int counter = 0;
        /*DBResultSet dbrs = null;

        Date startDate = (Date)date.clone();
        startDate.setDate(1);

        Date endDate = (Date)date.clone();
        endDate.setMonth(endDate.getMonth()+1);
        endDate.setDate(1);
        endDate.setDate(endDate.getDate()-1);

        try{

             I_PstDocType i_PstDocType = (I_PstDocType)Class.forName(I_DocType.DOCTYPE_CLASSNAME).newInstance();

            String sql = "SELECT MAX("+PstMatConDispatch.fieldNames[PstMatConDispatch.FLD_DISPATCH_CODE_COUNTER]+") AS MAXIMUM "+
                " FROM "+PstMatConDispatch.TBL_MAT_DISPATCH+" WHERE ("+
                PstMatConDispatch.fieldNames[PstMatConDispatch.FLD_DFM_DATE]+" BETWEEN \""+
                Formater.formatDate(startDate, "yyyy-MM-dd")+"\" AND \""+Formater.formatDate(endDate, "yyyy-MM-dd")+"\""+
                ") AND ("+PstMatConDispatch.fieldNames[PstMatConDispatch.FLD_DISPATCH_TYPE]+">99)"+
                " AND ("+PstMatConDispatch.fieldNames[PstMatConDispatch.FLD_DOC_TYPE]+"="+i_PstDocType.composeDocumentType(I_DocType.SYSTEM_MATERIAL , I_DocType.MAT_DOC_TYPE_DR)+")";

            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();

            while(rs.next()){
                counter = rs.getInt("MAXIMUM");
            }

            rs.close();
        }
        catch(Exception e){
            DBResultSet.close(dbrs);
            System.out.println("Exception getMaxCounter : "+e.toString());
            return 0;
        }
        finally{
            DBResultSet.close(dbrs);
        }
        */
        return counter;
    }

    //-------------------------------------------

    /* material dispatch */

    /* list for material dispatch */
    /*public static Vector listMatConDispatch(SrcMatConDispatch srcMaterial, int start, int limit)
    {
          Vector result = new Vector(1,1);
          /*if(srcMaterial!=null){
            	String sql = "SELECT * FROM "+PstMatConDispatch.TBL_MAT_DISPATCH+" AS DF ";

                String innerJoin = "";
                String materialCode = "";
                String materialName = "";
                if((srcMaterial.getMaterialCode().length()>0) || (srcMaterial.getMaterialName().length()>0) ){
                    innerJoin = "INNER JOIN "+PstMatConDispatchItem.TBL_MAT_DISPATCH_ITEM+" AS MAT "+
                        " ON MAT."+PstMatConDispatchItem.fieldNames[PstMatConDispatchItem.FLD_MAT_DISPATCH_ID]+"="+
                        " DF."+PstMatConDispatch.fieldNames[PstMatConDispatch.FLD_MAT_DISPATCH_ID]+" "+
                        " INNER JOIN "+PstMatStock.TBL_MAT_STOCK+" AS STOCK "+
                        " ON STOCK."+PstMatStock.fieldNames[PstMatStock.FLD_MAT_STOCK_ID]+"="+
                        " MAT."+PstMatConDispatchItem.fieldNames[PstMatConDispatchItem.FLD_MAT_STOCK_ID]+" "+
                        " INNER JOIN "+PstMaterial.TBL_MATERIAL+" AS CATALOG ON CATALOG."+
                        PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_ID]+"= STOCK."+PstMatStock.fieldNames[PstMatStock.FLD_MATERIAL_ID];

                    if(srcMaterial.getMaterialCode().length()>0){
                        materialCode = "(CATALOG."+PstMaterial.fieldNames[PstMaterial.FLD_CODE]+" LIKE \"%"+srcMaterial.getMaterialCode()+"%\")";
                    }

                    if(srcMaterial.getMaterialName().length()>0){
                        materialName = "(CATALOG."+PstMaterial.fieldNames[PstMaterial.FLD_NAME]+" LIKE \"%"+srcMaterial.getMaterialName()+"%\")";
                    }
                }

                String fromLocation = "";
                if(srcMaterial.getDispatchFrom()!=0){
					fromLocation = "(DF."+PstMatConDispatch.fieldNames[PstMatConDispatch.FLD_DISPATCH_FROM]+"="+srcMaterial.getDispatchFrom()+")";
                }

                String toLocation = "";
                if(srcMaterial.getDispatchTo()!=0){
					toLocation = "(DF."+PstMatConDispatch.fieldNames[PstMatConDispatch.FLD_DISPATCH_TO]+"="+srcMaterial.getDispatchTo()+")";
                }

                String date = "";
                if(!srcMaterial.getIgnoreDate()){
                    date = "(DF."+PstMatConDispatch.fieldNames[PstMatConDispatch.FLD_DFM_DATE]+" BETWEEN \""+Formater.formatDate(srcMaterial.getDispatchDateFrom(), "yyyy-MM-dd")+"\" AND \""+
                        Formater.formatDate(srcMaterial.getDispatchDateTo(), "yyyy-MM-dd")+"\")";
                }

                String dfCode = "";
                if(srcMaterial.getDispatchCode().length()>0){
                    dfCode = "(DF."+PstMatConDispatch.fieldNames[PstMatConDispatch.FLD_DISPATCH_CODE]+" LIKE \"%"+srcMaterial.getDispatchCode()+"%\")";
                }

                String status = "";
                if(srcMaterial.getStatus()>=0){
                    status =  "(DF."+PstMatConDispatch.fieldNames[PstMatConDispatch.FLD_DF_STATUS]+" = "+(srcMaterial.getStatus())+")";
                }
                String referency = "";
                String innerReferency = "";
                if(srcMaterial.getRequestRefCode().length()>0){
                    innerReferency = " INNER JOIN "+PstMatConDispatch.TBL_MAT_DISPATCH+" AS REF ON REF."+PstMatConDispatch.fieldNames[PstMaterial.FLD_MATERIAL_ID]+"="+
                        " DF."+PstMatConDispatch.fieldNames[PstMatConDispatch.FLD_REFERENCE_ID]+" ";

                    referency = "(REF."+PstMatConDispatch.fieldNames[PstMatConDispatch.FLD_REFERENCE_ID]+" LIKE \"%"+srcMaterial.getRequestRefCode()+"%\")";
                }



                String where = "";
                String lastInner = "";
                if(materialCode.length()>0 || materialName.length()>0){
                      lastInner = innerJoin;
                      if(materialCode.length()>0){
                         	where = materialCode;
                      }

                      if( materialName.length()>0){
	                        if( where.length()>0){
	                            where = where + " AND " + materialName;
	                        }
	                        else{
	                            where = materialName;
	                        }
                      }
                }

                if( fromLocation.length()>0){
                    if( where.length()>0){
                        where = where + " AND " + fromLocation;
                    }
                    else{
                        where = fromLocation;
                    }
                 }

                if( toLocation.length()>0){
                    if( where.length()>0){
                        where = where + " AND " + toLocation;
                    }
                    else{
                        where = toLocation;
                    }
                 }

                if( date.length()>0){
                    if( where.length()>0){
                        where = where + " AND " + date;
                    }
                    else{
                        where = date;
                    }
                 }

                if( dfCode.length()>0){
                    if( where.length()>0){
                        where = where + " AND " + dfCode;
                    }
                    else{
                        where = dfCode;
                    }
                 }

                if(status.length()>0){
                    if( where.length()>0){
                        where = where + " AND " + status;
                    }
                    else{
                        where = status;
                    }
                }

                if(referency.length()>0){
                    if( where.length()>0){
                        where = where + " AND " + referency;
                    }
                    else{
                        where = referency;
                    }
                }


                try{
                  	 I_PstDocType i_PstDocType = (I_PstDocType)Class.forName(I_DocType.DOCTYPE_CLASSNAME).newInstance();

	                 if( where.length()>0){
	                     where = where + " AND (DF."+ PstMatConDispatch.fieldNames[PstMatConDispatch.FLD_DOC_TYPE]+"="+i_PstDocType.composeDocumentType(I_DocType.SYSTEM_MATERIAL , I_DocType.MAT_DOC_TYPE_DF) +")";
	                 }
	                 else{
	                     where = "(DF."+ PstMatConDispatch.fieldNames[PstMatConDispatch.FLD_DOC_TYPE]+"="+i_PstDocType.composeDocumentType(I_DocType.SYSTEM_MATERIAL , I_DocType.MAT_DOC_TYPE_DF) +")";
	                 }

                 }
                catch(Exception e){
                    System.out.println("Exception e - interface : "+e.toString());
                }


                if(where.length()>0){
                    where = " WHERE	("+where+")";
                }

                 sql = sql + lastInner + innerReferency + where +" ORDER BY DF."+orderBy[srcMaterial.getSortBy()];//+" LIMIT "+start+","+limit;

                 switch (DBHandler.DBSVR_TYPE) {
	                case DBHandler.DBSVR_MYSQL :
							if(start == 0 && limit == 0)
								sql = sql + "";
							else
								sql = sql + " LIMIT " + start + ","+ limit ;

	                        break;

	                 case DBHandler.DBSVR_POSTGRESQL :
							if(start == 0 && limit == 0)
								sql = sql + "";
							else
								sql = sql + " LIMIT " +limit + " OFFSET "+ start ;

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


                 DBResultSet dbrs = null;
                 try{
                    dbrs = DBHandler.execQueryResult(sql);
                    ResultSet rs = dbrs.getResultSet();
                    while(rs.next()){
                        MatConDispatch matDf = new MatConDispatch();
                        PstMatConDispatch.resultToObject(rs, matDf);

                        result.add(matDf);
                    }

                    rs.close();
                 }
                 catch(Exception e){
                    DBResultSet.close(dbrs);
                    System.out.println("exception on search DF REG : "+e.toString());
                 }
                 finally{
                    DBResultSet.close(dbrs);
                 }

                 return result;

          }
           else
           {
          }
          return result;
    }
           */


    /*
   for material dispatch
   */
    public static int getCount(SrcMatConDispatch srcMaterial) {

        int result = 0;

        /*if(srcMaterial!=null){
              String sql = "SELECT COUNT(DF."+PstMatConDispatch.fieldNames[PstMatConDispatch.FLD_MAT_DISPATCH_ID]+") FROM "+PstMatConDispatch.TBL_MAT_DISPATCH+" AS DF ";

              String innerJoin = "";
              String materialCode = "";
              String materialName = "";
              if((srcMaterial.getMaterialCode().length()>0) || (srcMaterial.getMaterialName().length()>0) ){
                  innerJoin = "INNER JOIN "+PstMatConDispatchItem.TBL_MAT_DISPATCH_ITEM+" AS MAT "+
                      " ON MAT."+PstMatConDispatchItem.fieldNames[PstMatConDispatchItem.FLD_MAT_DISPATCH_ID]+"="+
                      " DF."+PstMatConDispatch.fieldNames[PstMatConDispatch.FLD_MAT_DISPATCH_ID]+" "+
                      " INNER JOIN "+PstMatStock.TBL_MAT_STOCK+" AS STOCK "+
                      " ON STOCK."+PstMatStock.fieldNames[PstMatStock.FLD_MAT_STOCK_ID]+"="+
                      " MAT."+PstMatConDispatchItem.fieldNames[PstMatConDispatchItem.FLD_MAT_STOCK_ID]+" "+
                      " INNER JOIN "+PstMaterial.TBL_MATERIAL+" AS CATALOG ON CATALOG."+
                      PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_ID]+"= STOCK."+PstMatStock.fieldNames[PstMatStock.FLD_MATERIAL_ID];

                  if(srcMaterial.getMaterialCode().length()>0){
                      materialCode = "(CATALOG."+PstMaterial.fieldNames[PstMaterial.FLD_CODE]+" LIKE \"%"+srcMaterial.getMaterialCode()+"%\")";
                  }

                  if(srcMaterial.getMaterialName().length()>0){
                      materialName = "(CATALOG."+PstMaterial.fieldNames[PstMaterial.FLD_NAME]+" LIKE \"%"+srcMaterial.getMaterialName()+"%\")";
                  }
              }

              String fromLocation = "";
              if(srcMaterial.getDispatchFrom()!=0){
                  fromLocation = "(DF."+PstMatConDispatch.fieldNames[PstMatConDispatch.FLD_DISPATCH_FROM]+"="+srcMaterial.getDispatchFrom()+")";
              }

              String toLocation = "";
              if(srcMaterial.getDispatchTo()!=0){
                  toLocation = "(DF."+PstMatConDispatch.fieldNames[PstMatConDispatch.FLD_DISPATCH_TO]+"="+srcMaterial.getDispatchTo()+")";
              }

              String date = "";
              if(!srcMaterial.getIgnoreDate()){
                  date = "(DF."+PstMatConDispatch.fieldNames[PstMatConDispatch.FLD_DFM_DATE]+" BETWEEN \""+Formater.formatDate(srcMaterial.getDispatchDateFrom(), "yyyy-MM-dd")+"\" AND \""+
                      Formater.formatDate(srcMaterial.getDispatchDateTo(), "yyyy-MM-dd")+"\")";
              }

              String dfCode = "";
              if(srcMaterial.getDispatchCode().length()>0){
                  dfCode = "(DF."+PstMatConDispatch.fieldNames[PstMatConDispatch.FLD_DISPATCH_CODE]+" LIKE \"%"+srcMaterial.getDispatchCode()+"%\")";
              }

              String status = "";
              if(srcMaterial.getStatus()>=0){
                  status =  "(DF."+PstMatConDispatch.fieldNames[PstMatConDispatch.FLD_DF_STATUS]+" = "+(srcMaterial.getStatus())+")";
              }
              String referency = "";
              String innerReferency = "";
              if(srcMaterial.getRequestRefCode().length()>0){
                  innerReferency = " INNER JOIN "+PstMatConDispatch.TBL_MAT_DISPATCH+" AS REF ON REF."+PstMatConDispatch.fieldNames[PstMaterial.FLD_MATERIAL_ID]+"="+
                      " DF."+PstMatConDispatch.fieldNames[PstMatConDispatch.FLD_REFERENCE_ID]+" ";

                  referency = "(REF."+PstMatConDispatch.fieldNames[PstMatConDispatch.FLD_REFERENCE_ID]+" LIKE \"%"+srcMaterial.getRequestRefCode()+"%\")";
              }



              String where = "";
              String lastInner = "";
              if(materialCode.length()>0 || materialName.length()>0){
                    lastInner = innerJoin;
                    if(materialCode.length()>0){
                           where = materialCode;
                    }

                    if( materialName.length()>0){
                          if( where.length()>0){
                              where = where + " AND " + materialName;
                          }
                          else{
                              where = materialName;
                          }
                    }
              }

              if( fromLocation.length()>0){
                  if( where.length()>0){
                      where = where + " AND " + fromLocation;
                  }
                  else{
                      where = fromLocation;
                  }
               }

              if( toLocation.length()>0){
                  if( where.length()>0){
                      where = where + " AND " + toLocation;
                  }
                  else{
                      where = toLocation;
                  }
               }

              if( date.length()>0){
                  if( where.length()>0){
                      where = where + " AND " + date;
                  }
                  else{
                      where = date;
                  }
               }

              if( dfCode.length()>0){
                  if( where.length()>0){
                      where = where + " AND " + dfCode;
                  }
                  else{
                      where = dfCode;
                  }
               }

              if(status.length()>0){
                  if( where.length()>0){
                      where = where + " AND " + status;
                  }
                  else{
                      where = status;
                  }
              }

              if(referency.length()>0){
                  if( where.length()>0){
                      where = where + " AND " + referency;
                  }
                  else{
                      where = referency;
                  }
              }


              try{
                     I_PstDocType i_PstDocType = (I_PstDocType)Class.forName(I_DocType.DOCTYPE_CLASSNAME).newInstance();

                   if( where.length()>0){
                       where = where + " AND (DF."+ PstMatConDispatch.fieldNames[PstMatConDispatch.FLD_DOC_TYPE]+"="+i_PstDocType.composeDocumentType(I_DocType.SYSTEM_MATERIAL , I_DocType.MAT_DOC_TYPE_DF) +")";
                   }
                   else{
                       where = "(DF."+ PstMatConDispatch.fieldNames[PstMatConDispatch.FLD_DOC_TYPE]+"="+i_PstDocType.composeDocumentType(I_DocType.SYSTEM_MATERIAL , I_DocType.MAT_DOC_TYPE_DF) +")";
                   }

               }
              catch(Exception e){
                  System.out.println("Exception e - interface : "+e.toString());
              }


              if(where.length()>0){
                  where = " WHERE	("+where+")";
              }

               sql = sql + lastInner + innerReferency + where ;//+" ORDER BY "+orderBy[srcMaterial.getSortBy()]+" LIMIT "+start+","+limit;

               DBResultSet dbrs = null;
               try{
                  dbrs = DBHandler.execQueryResult(sql);
                  ResultSet rs = dbrs.getResultSet();
                  while(rs.next()){
                      result = rs.getInt(1);
                  }

                  rs.close();
               }
               catch(Exception e){
                  DBResultSet.close(dbrs);
                  System.out.println("exception on search DF REG : "+e.toString());
               }
               finally{
                  DBResultSet.close(dbrs);
               }

               return result;

        }else{
        }*/
        return result;
    }


    /* for material request */
    public static int findLimitStart(long oid, int recordToGet, SrcMatRequest srcMaterial) {
        /*String order = "";
        int size = getCount(srcMaterial);
        int start = 0;
        boolean found =false;
        for(int i=0; (i < size) && !found ; i=i+recordToGet){
             Vector list =  listMatConDispatch(srcMaterial,i,recordToGet);
             start = i;
             if(list.size()>0){
              for(int ls=0;ls<list.size();ls++){
                     MatConDispatch materialdispatch = (MatConDispatch)list.get(ls);
                   if(oid == materialdispatch.getOID())
                      found=true;
              }
          }
        }
        if((start >= size) && (size > 0))
            start = start - recordToGet;

        return start;
*/
        return 0;
    }

    public String generateDispatchCode(MatConDispatch matDispatch) {
        // belum mengambl ke workflow, karena tidak di gunakan workflownya.
        String code = "DF";
        String dateCode = "";

        // penambahan baru request dari taiga: 18/03/2005
        String kodeLoc = "";
        try{
            Location loc = PstLocation.fetchExc(matDispatch.getDispatchTo());
            kodeLoc = loc.getCode();
        }catch(Exception e){}

        if (matDispatch.getDispatchDate() != null) {
            /** get location code; gwawan@21juni2007 */
            Vector vctLocation = PstLocation.list(0, 0, PstLocation.fieldNames[PstLocation.FLD_LOCATION_ID]+"="+matDispatch.getLocationId(), "");
            Location location = (Location)vctLocation.get(0);
            
            int nextCounter = matDispatch.getDispatchCodeCounter();//getMaxCounter(date);
            Date date = matDispatch.getDispatchDate();

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
            code = location.getCode() + "-" + dateCode + "-" + code + "-" + kodeLoc +"-" + counter;
        }
        return code;
    }

    public static int getMaxDispatchCounter(Date date, MatConDispatch matDispatch) {
        int counter = 0;
        DBResultSet dbrs = null;
        try {

            Date startDate = (Date) date.clone();
            startDate.setDate(1);

            Date endDate = (Date) date.clone();
            endDate.setMonth(endDate.getMonth() + 1);
            endDate.setDate(1);
            endDate.setDate(endDate.getDate() - 1);

            I_PstDocType i_PstDocType = (I_PstDocType) Class.forName(I_DocType.DOCTYPE_CLASSNAME).newInstance();

            String sql = "SELECT MAX(" + PstMatConDispatch.fieldNames[PstMatConDispatch.FLD_DISPATCH_CODE_COUNTER] + ") AS MAXIMUM " +
                    " FROM " + PstMatConDispatch.TBL_DISPATCH +
                    " WHERE (" + PstMatConDispatch.fieldNames[PstMatConDispatch.FLD_DISPATCH_DATE] +
                    " BETWEEN '" + Formater.formatDate(startDate, "yyyy-MM-dd") +
                    " ' AND '" + Formater.formatDate(endDate, "yyyy-MM-dd") +
                    " ') AND " + PstMatConDispatch.fieldNames[PstMatConDispatch.FLD_LOCATION_ID] + " = " + matDispatch.getLocationId();

            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();

            while (rs.next()) {
                counter = rs.getInt("MAXIMUM");
            }

            rs.close();
        } catch (Exception e) {
            DBResultSet.close(dbrs);
            System.out.println("Exception getMaxCounter : " + e.toString());
        } finally {
            DBResultSet.close(dbrs);
        }
        return counter;
    }

    //----------------------------------
    /* transfer MR to DF */

    /* list for material transfer */
    public static Vector listMaterialTransfer(SrcMatRequest srcMaterial, int start, int limit) {
        Vector result = new Vector(1, 1);
        /*if(srcMaterial!=null){
              String sql = "SELECT * FROM "+PstMatConDispatch.TBL_MAT_DISPATCH+" AS DF ";

              String innerJoin = "";
              String materialCode = "";
              String materialName = "";
              if((srcMaterial.getMaterialCode().length()>0) || (srcMaterial.getMaterialName().length()>0) ){
                  innerJoin = "INNER JOIN "+PstMatConDispatchItem.TBL_MAT_DISPATCH_ITEM+" AS MAT "+
                      " ON MAT."+PstMatConDispatchItem.fieldNames[PstMatConDispatchItem.FLD_MAT_DISPATCH_ID]+"="+
                      " DF."+PstMatConDispatch.fieldNames[PstMatConDispatch.FLD_MAT_DISPATCH_ID]+" "+
                      " INNER JOIN "+PstMatStock.TBL_MAT_STOCK+" AS STOCK "+
                      " ON STOCK."+PstMatStock.fieldNames[PstMatStock.FLD_MAT_STOCK_ID]+"="+
                      " MAT."+PstMatConDispatchItem.fieldNames[PstMatConDispatchItem.FLD_MAT_STOCK_ID]+" "+
                      " INNER JOIN "+PstMaterial.TBL_MATERIAL+" AS CATALOG ON CATALOG."+
                      PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_ID]+"= STOCK."+PstMatStock.fieldNames[PstMatStock.FLD_MATERIAL_ID];

                  if(srcMaterial.getMaterialCode().length()>0){
                      materialCode = "(CATALOG."+PstMaterial.fieldNames[PstMaterial.FLD_CODE]+" LIKE \"%"+srcMaterial.getMaterialCode()+"%\")";
                  }

                  if(srcMaterial.getMaterialName().length()>0){
                      materialName = "(CATALOG."+PstMaterial.fieldNames[PstMaterial.FLD_NAME]+" LIKE \"%"+srcMaterial.getMaterialName()+"%\")";
                  }
              }

              String fromLocation = "";
              if(srcMaterial.getFromLocation()!=0){
                  fromLocation = "(DF."+PstMatConDispatch.fieldNames[PstMatConDispatch.FLD_DISPATCH_FROM]+"="+srcMaterial.getFromLocation()+")";
              }

              String toLocation = "";
              if(srcMaterial.getToLocation()!=0){
                  toLocation = "(DF."+PstMatConDispatch.fieldNames[PstMatConDispatch.FLD_DISPATCH_TO]+"="+srcMaterial.getToLocation()+")";
              }

              String date = "";
              if(!srcMaterial.getIgnoreDate()){
                  date = "(DF."+PstMatConDispatch.fieldNames[PstMatConDispatch.FLD_DFM_DATE]+" BETWEEN \""+Formater.formatDate(srcMaterial.getFromDate(), "yyyy-MM-dd")+"\" AND \""+
                      Formater.formatDate(srcMaterial.getEndDate(), "yyyy-MM-dd")+"\")";
              }

              String dfCode = "";
              if(srcMaterial.getDfCode().length()>0){
                  dfCode = "(DF."+PstMatConDispatch.fieldNames[PstMatConDispatch.FLD_DISPATCH_CODE]+" LIKE \"%"+srcMaterial.getDfCode()+"%\")";
              }

              String status = "";

              String where = "";
              String lastInner = "";
              if(materialCode.length()>0 || materialName.length()>0){
                    lastInner = innerJoin;
                    if(materialCode.length()>0){
                           where = materialCode;
                    }

                    if( materialName.length()>0){
                          if( where.length()>0){
                              where = where + " AND " + materialName;
                          }
                          else{
                              where = materialName;
                          }
                    }
              }

              if( fromLocation.length()>0){
                  if( where.length()>0){
                      where = where + " AND " + fromLocation;
                  }
                  else{
                      where = fromLocation;
                  }
               }

              if( toLocation.length()>0){
                  if( where.length()>0){
                      where = where + " AND " + toLocation;
                  }
                  else{
                      where = toLocation;
                  }
               }

              if( date.length()>0){
                  if( where.length()>0){
                      where = where + " AND " + date;
                  }
                  else{
                      where = date;
                  }
               }

              if( dfCode.length()>0){
                  if( where.length()>0){
                      where = where + " AND " + dfCode;
                  }
                  else{
                      where = dfCode;
                  }
               }

              if(status.length()>0){
                  if( where.length()>0){
                      where = where + " AND " + status;
                  }
                  else{
                      where = status;
                  }
              }


              try{
                     I_PstDocType i_PstDocType = (I_PstDocType)Class.forName(I_DocType.DOCTYPE_CLASSNAME).newInstance();


               }
              catch(Exception e){
                  System.out.println("Exception e - interface : "+e.toString());
              }

              try{
                  I_PstDocType i_PstDocType = (I_PstDocType)Class.forName(I_DocType.DOCTYPE_CLASSNAME).newInstance();


                  if( where.length()>0){
                       where = where + " AND (DF."+ PstMatConDispatch.fieldNames[PstMatConDispatch.FLD_DOC_TYPE]+"="+i_PstDocType.composeDocumentType(I_DocType.SYSTEM_MATERIAL , I_DocType.MAT_DOC_TYPE_DR) +")"+
                          " AND (DF."+PstMatConDispatch.fieldNames[PstMatConDispatch.FLD_DF_STATUS]+"="+I_DocStatus.DOCUMENT_STATUS_FINAL+")";

                  }
                  else{
                       where = "(DF."+ PstMatConDispatch.fieldNames[PstMatConDispatch.FLD_DOC_TYPE]+"="+i_PstDocType.composeDocumentType(I_DocType.SYSTEM_MATERIAL , I_DocType.MAT_DOC_TYPE_DR) +")"+
                          " AND (DF."+PstMatConDispatch.fieldNames[PstMatConDispatch.FLD_DF_STATUS]+"="+I_DocStatus.DOCUMENT_STATUS_FINAL+")";

                  }
              }
              catch(Exception e){
                  System.out.println("exception e : "+e.toString());
              }


              if(where.length()>0){
                  where = " WHERE	("+where+")";
              }

               sql = sql + lastInner + where +" ORDER BY DF."+orderBy[srcMaterial.getSortBy()];//+" LIMIT "+start+","+limit;

               switch (DBHandler.DBSVR_TYPE) {
                  case DBHandler.DBSVR_MYSQL :
                          if(start == 0 && limit == 0)
                              sql = sql + "";
                          else
                              sql = sql + " LIMIT " + start + ","+ limit ;

                          break;

                   case DBHandler.DBSVR_POSTGRESQL :
                          if(start == 0 && limit == 0)
                              sql = sql + "";
                          else
                              sql = sql + " LIMIT " +limit + " OFFSET "+ start ;

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


               DBResultSet dbrs = null;
               try{
                  dbrs = DBHandler.execQueryResult(sql);
                  ResultSet rs = dbrs.getResultSet();
                  while(rs.next()){
                      MatConDispatch matDf = new MatConDispatch();
                      PstMatConDispatch.resultToObject(rs, matDf);

                      result.add(matDf);
                  }

                  rs.close();
               }
               catch(Exception e){
                  DBResultSet.close(dbrs);
                  System.out.println("exception on search DF REG : "+e.toString());
               }
               finally{
                  DBResultSet.close(dbrs);
               }

               return result;

        }else{
        }
         */
        return result;
    }


    /*
    for material transfer --> namanya sudah terlanjur
    (untuk material dispatch querynya juga disini)
    */
    public static int getCountTransfer(SrcMatRequest srcMaterial) {

        int result = 0;

        /*if(srcMaterial!=null){
              String sql = "SELECT COUNT(DF."+PstMatConDispatch.fieldNames[PstMatConDispatch.FLD_MAT_DISPATCH_ID]+") FROM "+PstMatConDispatch.TBL_MAT_DISPATCH+" AS DF ";

              String innerJoin = "";
              String materialCode = "";
              String materialName = "";
              if((srcMaterial.getMaterialCode().length()>0) || (srcMaterial.getMaterialName().length()>0) ){
                  innerJoin = "INNER JOIN "+PstMatConDispatchItem.TBL_MAT_DISPATCH_ITEM+" AS MAT "+
                      " ON MAT."+PstMatConDispatchItem.fieldNames[PstMatConDispatchItem.FLD_MAT_DISPATCH_ID]+"="+
                      " DF."+PstMatConDispatch.fieldNames[PstMatConDispatch.FLD_MAT_DISPATCH_ID]+" "+
                      " INNER JOIN "+PstMatStock.TBL_MAT_STOCK+" AS STOCK "+
                      " ON STOCK."+PstMatStock.fieldNames[PstMatStock.FLD_MAT_STOCK_ID]+"="+
                      " MAT."+PstMatConDispatchItem.fieldNames[PstMatConDispatchItem.FLD_MAT_STOCK_ID]+" "+
                      " INNER JOIN "+PstMaterial.TBL_MATERIAL+" AS CATALOG ON CATALOG."+
                      PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_ID]+"= STOCK."+PstMatStock.fieldNames[PstMatStock.FLD_MATERIAL_ID];

                  if(srcMaterial.getMaterialCode().length()>0){
                      materialCode = "(CATALOG."+PstMaterial.fieldNames[PstMaterial.FLD_CODE]+" LIKE \"%"+srcMaterial.getMaterialCode()+"%\")";
                  }

                  if(srcMaterial.getMaterialName().length()>0){
                      materialName = "(CATALOG."+PstMaterial.fieldNames[PstMaterial.FLD_NAME]+" LIKE \"%"+srcMaterial.getMaterialName()+"%\")";
                  }
              }

              String fromLocation = "";
              if(srcMaterial.getFromLocation()!=0){
                  fromLocation = "(DF."+PstMatConDispatch.fieldNames[PstMatConDispatch.FLD_DISPATCH_FROM]+"="+srcMaterial.getFromLocation()+")";
              }

              String toLocation = "";
              if(srcMaterial.getToLocation()!=0){
                  toLocation = "(DF."+PstMatConDispatch.fieldNames[PstMatConDispatch.FLD_DISPATCH_TO]+"="+srcMaterial.getToLocation()+")";
              }

              String date = "";
              if(!srcMaterial.getIgnoreDate()){
                  date = "(DF."+PstMatConDispatch.fieldNames[PstMatConDispatch.FLD_DFM_DATE]+" BETWEEN \""+Formater.formatDate(srcMaterial.getFromDate(), "yyyy-MM-dd")+"\" AND \""+
                      Formater.formatDate(srcMaterial.getEndDate(), "yyyy-MM-dd")+"\")";
              }

              String dfCode = "";
              if(srcMaterial.getDfCode().length()>0){
                  dfCode = "(DF."+PstMatConDispatch.fieldNames[PstMatConDispatch.FLD_DISPATCH_CODE]+" LIKE \"%"+srcMaterial.getDfCode()+"%\")";
              }

              String status = "";

              String where = "";
              String lastInner = "";
              if(materialCode.length()>0 || materialName.length()>0){
                    lastInner = innerJoin;
                    if(materialCode.length()>0){
                           where = materialCode;
                    }

                    if( materialName.length()>0){
                          if( where.length()>0){
                              where = where + " AND " + materialName;
                          }
                          else{
                              where = materialName;
                          }
                    }
              }

              if( fromLocation.length()>0){
                  if( where.length()>0){
                      where = where + " AND " + fromLocation;
                  }
                  else{
                      where = fromLocation;
                  }
               }

              if( toLocation.length()>0){
                  if( where.length()>0){
                      where = where + " AND " + toLocation;
                  }
                  else{
                      where = toLocation;
                  }
               }

              if( date.length()>0){
                  if( where.length()>0){
                      where = where + " AND " + date;
                  }
                  else{
                      where = date;
                  }
               }

              if( dfCode.length()>0){
                  if( where.length()>0){
                      where = where + " AND " + dfCode;
                  }
                  else{
                      where = dfCode;
                  }
               }

              if(status.length()>0){
                  if( where.length()>0){
                      where = where + " AND " + status;
                  }
                  else{
                      where = status;
                  }
              }

              try{
                  I_PstDocType i_PstDocType = (I_PstDocType)Class.forName(I_DocType.DOCTYPE_CLASSNAME).newInstance();

                   if( where.length()>0){
                       where = where + " AND (DF."+ PstMatConDispatch.fieldNames[PstMatConDispatch.FLD_DOC_TYPE]+"="+i_PstDocType.composeDocumentType(I_DocType.SYSTEM_MATERIAL , I_DocType.MAT_DOC_TYPE_DR) +")"+
                           " AND (DF."+PstMatConDispatch.fieldNames[PstMatConDispatch.FLD_DF_STATUS]+"="+I_DocStatus.DOCUMENT_STATUS_FINAL+")";

                   }
                   else{
                       where = "(DF."+ PstMatConDispatch.fieldNames[PstMatConDispatch.FLD_DOC_TYPE]+"="+i_PstDocType.composeDocumentType(I_DocType.SYSTEM_MATERIAL , I_DocType.MAT_DOC_TYPE_DR) +")"+
                           " AND (DF."+PstMatConDispatch.fieldNames[PstMatConDispatch.FLD_DF_STATUS]+"="+I_DocStatus.DOCUMENT_STATUS_FINAL+")";

                   }
              }
              catch(Exception e){
                  System.out.println("Exception e : "+e.toString());
              }


              if(where.length()>0){
                  where = " WHERE ("+where+")";
              }

               sql = sql + lastInner + where;

               DBResultSet dbrs = null;
               try{
                  dbrs = DBHandler.execQueryResult(sql);
                  ResultSet rs = dbrs.getResultSet();
                  while(rs.next()){
                      result = rs.getInt(1);
                  }

                  rs.close();
               }
               catch(Exception e){
                  DBResultSet.close(dbrs);
                  System.out.println("exception on search DF REG : "+e.toString());
               }
               finally{
                  DBResultSet.close(dbrs);
               }

               return result;

        }else{
                return 0;
        }*/
        return result;
    }

    //------------------------------------------





    public static int findLimitStartTransfer(long oid, int recordToGet, SrcMatRequest srcMaterial) {
        /*String order = "";
        int size = getCountTransfer(srcMaterial);
        int start = 0;
        boolean found =false;
        for(int i=0; (i < size) && !found ; i=i+recordToGet){
             Vector list =  listMaterialTransfer(srcMaterial,i,recordToGet);
             start = i;
             if(list.size()>0){
              for(int ls=0;ls<list.size();ls++){
                     MatConDispatch materialdispatch = (MatConDispatch)list.get(ls);
                   if(oid == materialdispatch.getOID())
                      found=true;
              }
          }
        }
        if((start >= size) && (size > 0))
            start = start - recordToGet;

        return start;*/
        return 0;
    }



    //-------------------------
    /* general  */

    public static int findLimitCommand(int start, int recordToGet, int vectSize) {
        int cmd = Command.LIST;
        /*int mdl = vectSize % recordToGet;
        vectSize = vectSize + mdl;
        if(start == 0)
            cmd =  Command.FIRST;
        else{
            if(start == (vectSize-recordToGet))
                cmd = Command.LAST;
            else{
                start = start + recordToGet;
                 if(start <= (vectSize - recordToGet)){
                     cmd = Command.NEXT;
                 }else{
                    start = start - recordToGet;
                     if(start > 0){
                         cmd = Command.PREV;
                     }
                }
            }
        }
        */
        return cmd;
    }


    //----------------
    //DISPATCH RECEIVED

    /* list for material rec */
    public static Vector listMatConDispatchRec(SrcMatConDispatch srcMaterial, int start, int limit) {
        Vector result = new Vector(1, 1);
        /*if(srcMaterial!=null){
              String sql = "SELECT * FROM "+PstMatConDispatch.TBL_MAT_DISPATCH+" AS DF ";

              String innerJoin = "";
              String materialCode = "";
              String materialName = "";
              if((srcMaterial.getMaterialCode().length()>0) || (srcMaterial.getMaterialName().length()>0) ){
                  innerJoin = "INNER JOIN "+PstMatConDispatchItem.TBL_MAT_DISPATCH_ITEM+" AS MAT "+
                      " ON MAT."+PstMatConDispatchItem.fieldNames[PstMatConDispatchItem.FLD_MAT_DISPATCH_ID]+"="+
                      " DF."+PstMatConDispatch.fieldNames[PstMatConDispatch.FLD_MAT_DISPATCH_ID]+" "+
                      " INNER JOIN "+PstMatStock.TBL_MAT_STOCK+" AS STOCK "+
                      " ON STOCK."+PstMatStock.fieldNames[PstMatStock.FLD_MAT_STOCK_ID]+"="+
                      " MAT."+PstMatConDispatchItem.fieldNames[PstMatConDispatchItem.FLD_MAT_STOCK_ID]+" "+
                      " INNER JOIN "+PstMaterial.TBL_MATERIAL+" AS CATALOG ON CATALOG."+
                      PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_ID]+"= STOCK."+PstMatStock.fieldNames[PstMatStock.FLD_MATERIAL_ID];

                  if(srcMaterial.getMaterialCode().length()>0){
                      materialCode = "(CATALOG."+PstMaterial.fieldNames[PstMaterial.FLD_CODE]+" LIKE \"%"+srcMaterial.getMaterialCode()+"%\")";
                  }

                  if(srcMaterial.getMaterialName().length()>0){
                      materialName = "(CATALOG."+PstMaterial.fieldNames[PstMaterial.FLD_NAME]+" LIKE \"%"+srcMaterial.getMaterialName()+"%\")";
                  }
              }

              String fromLocation = "";
              if(srcMaterial.getDispatchFrom()!=0){
                  fromLocation = "(DF."+PstMatConDispatch.fieldNames[PstMatConDispatch.FLD_DISPATCH_FROM]+"="+srcMaterial.getDispatchFrom()+")";
              }

              String toLocation = "";
              if(srcMaterial.getDispatchTo()!=0){
                  toLocation = "(DF."+PstMatConDispatch.fieldNames[PstMatConDispatch.FLD_DISPATCH_TO]+"="+srcMaterial.getDispatchTo()+")";
              }

              String date = "";
              if(!srcMaterial.getIgnoreDate()){
                  date = "(DF."+PstMatConDispatch.fieldNames[PstMatConDispatch.FLD_DFM_DATE]+" BETWEEN \""+Formater.formatDate(srcMaterial.getDispatchDateFrom(), "yyyy-MM-dd")+"\" AND \""+
                      Formater.formatDate(srcMaterial.getDispatchDateTo(), "yyyy-MM-dd")+"\")";
              }

              String dfCode = "";
              if(srcMaterial.getDispatchCode().length()>0){
                  dfCode = "(DF."+PstMatConDispatch.fieldNames[PstMatConDispatch.FLD_DISPATCH_CODE]+" LIKE \"%"+srcMaterial.getDispatchCode()+"%\")";
              }

              String status = "";
              if(srcMaterial.getStatus()>=0){
                  status =  "(DF."+PstMatConDispatch.fieldNames[PstMatConDispatch.FLD_DF_STATUS]+" = "+(srcMaterial.getStatus())+")";
              }

              String referency = "";
              String innerReferency = "";
              if(srcMaterial.getRequestRefCode().length()>0){
                  innerReferency = " INNER JOIN "+PstMatConDispatch.TBL_MAT_DISPATCH+" AS REF ON REF."+PstMatConDispatch.fieldNames[PstMaterial.FLD_MATERIAL_ID]+"="+
                      " DF."+PstMatConDispatch.fieldNames[PstMatConDispatch.FLD_REFERENCE_ID]+" ";

                  referency = "(REF."+PstMatConDispatch.fieldNames[PstMatConDispatch.FLD_REFERENCE_ID]+" LIKE \"%"+srcMaterial.getRequestRefCode()+"%\")";
              }



              String where = "";
              String lastInner = "";
              if(materialCode.length()>0 || materialName.length()>0){
                    lastInner = innerJoin;
                    if(materialCode.length()>0){
                           where = materialCode;
                    }

                    if( materialName.length()>0){
                          if( where.length()>0){
                              where = where + " AND " + materialName;
                          }
                          else{
                              where = materialName;
                          }
                    }
              }

              if( fromLocation.length()>0){
                  if( where.length()>0){
                      where = where + " AND " + fromLocation;
                  }
                  else{
                      where = fromLocation;
                  }
               }

              if( toLocation.length()>0){
                  if( where.length()>0){
                      where = where + " AND " + toLocation;
                  }
                  else{
                      where = toLocation;
                  }
               }

              if( date.length()>0){
                  if( where.length()>0){
                      where = where + " AND " + date;
                  }
                  else{
                      where = date;
                  }
               }

              if( dfCode.length()>0){
                  if( where.length()>0){
                      where = where + " AND " + dfCode;
                  }
                  else{
                      where = dfCode;
                  }
               }

              if(status.length()>0){
                  if( where.length()>0){
                      where = where + " AND " + status;
                  }
                  else{
                      where = status;
                  }
              }

              if(referency.length()>0){
                  if( where.length()>0){
                      where = where + " AND " + referency;
                  }
                  else{
                      where = referency;
                  }
              }

              try{
                  I_PstDocType i_PstDocType = (I_PstDocType)Class.forName(I_DocType.DOCTYPE_CLASSNAME).newInstance();

                  if( where.length()>0){
                       where = where + " AND (DF."+ PstMatConDispatch.fieldNames[PstMatConDispatch.FLD_DOC_TYPE]+"="+i_PstDocType.composeDocumentType(I_DocType.SYSTEM_MATERIAL , I_DocType.MAT_DOC_TYPE_DF) +")"+
                          " AND ((DF."+PstMatConDispatch.fieldNames[PstMatConDispatch.FLD_DF_STATUS]+"="+I_DocStatus.DOCUMENT_STATUS_FINAL+") OR (DF."+PstMatConDispatch.fieldNames[PstMatConDispatch.FLD_DF_STATUS]+"="+I_DocStatus.DOCUMENT_STATUS_CLOSED+"))";

                   }
                   else{
                       where = "(DF."+ PstMatConDispatch.fieldNames[PstMatConDispatch.FLD_DOC_TYPE]+"="+i_PstDocType.composeDocumentType(I_DocType.SYSTEM_MATERIAL , I_DocType.MAT_DOC_TYPE_DF) +")"+
                          " AND ((DF."+PstMatConDispatch.fieldNames[PstMatConDispatch.FLD_DF_STATUS]+"="+I_DocStatus.DOCUMENT_STATUS_FINAL+") OR (DF."+PstMatConDispatch.fieldNames[PstMatConDispatch.FLD_DF_STATUS]+"="+I_DocStatus.DOCUMENT_STATUS_CLOSED+"))";
                   }
              }
              catch(Exception e){
                  System.out.println("Exception e : "+e.toString());
              }


              if(where.length()>0){
                  where = " WHERE	("+where+")";
              }

               sql = sql + lastInner + innerReferency + where +" ORDER BY DF."+orderBy[srcMaterial.getSortBy()];//+" LIMIT "+start+","+limit;

               switch (DBHandler.DBSVR_TYPE) {
                  case DBHandler.DBSVR_MYSQL :
                          if(start == 0 && limit == 0)
                              sql = sql + "";
                          else
                              sql = sql + " LIMIT " + start + ","+ limit ;

                          break;

                   case DBHandler.DBSVR_POSTGRESQL :
                          if(start == 0 && limit == 0)
                              sql = sql + "";
                          else
                              sql = sql + " LIMIT " +limit + " OFFSET "+ start ;

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


               DBResultSet dbrs = null;
               try{
                  dbrs = DBHandler.execQueryResult(sql);
                  ResultSet rs = dbrs.getResultSet();
                  while(rs.next()){
                      MatConDispatch matDf = new MatConDispatch();
                      PstMatConDispatch.resultToObject(rs, matDf);

                      result.add(matDf);
                  }

                  rs.close();
               }
               catch(Exception e){
                  DBResultSet.close(dbrs);
                  System.out.println("exception on search DF REG : "+e.toString());
               }
               finally{
                  DBResultSet.close(dbrs);
               }

               return result;

        }else{
        }
         */
        return result;
    }


    /*
   for material dispatch
   */
    public static int getCountRec(SrcMatConDispatch srcMaterial) {

        int result = 0;

        /*if(srcMaterial!=null){
              String sql = "SELECT COUNT(DF."+PstMatConDispatch.fieldNames[PstMatConDispatch.FLD_MAT_DISPATCH_ID]+") FROM "+PstMatConDispatch.TBL_MAT_DISPATCH+" AS DF ";

              String innerJoin = "";
              String materialCode = "";
              String materialName = "";
              if((srcMaterial.getMaterialCode().length()>0) || (srcMaterial.getMaterialName().length()>0) ){
                  innerJoin = "INNER JOIN "+PstMatConDispatchItem.TBL_MAT_DISPATCH_ITEM+" AS MAT "+
                      " ON MAT."+PstMatConDispatchItem.fieldNames[PstMatConDispatchItem.FLD_MAT_DISPATCH_ID]+"="+
                      " DF."+PstMatConDispatch.fieldNames[PstMatConDispatch.FLD_MAT_DISPATCH_ID]+" "+
                      " INNER JOIN "+PstMatStock.TBL_MAT_STOCK+" AS STOCK "+
                      " ON STOCK."+PstMatStock.fieldNames[PstMatStock.FLD_MAT_STOCK_ID]+"="+
                      " MAT."+PstMatConDispatchItem.fieldNames[PstMatConDispatchItem.FLD_MAT_STOCK_ID]+" "+
                      " INNER JOIN "+PstMaterial.TBL_MATERIAL+" AS CATALOG ON CATALOG."+
                      PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_ID]+"= STOCK."+PstMatStock.fieldNames[PstMatStock.FLD_MATERIAL_ID];

                  if(srcMaterial.getMaterialCode().length()>0){
                      materialCode = "(CATALOG."+PstMaterial.fieldNames[PstMaterial.FLD_CODE]+" LIKE \"%"+srcMaterial.getMaterialCode()+"%\")";
                  }

                  if(srcMaterial.getMaterialName().length()>0){
                      materialName = "(CATALOG."+PstMaterial.fieldNames[PstMaterial.FLD_NAME]+" LIKE \"%"+srcMaterial.getMaterialName()+"%\")";
                  }
              }

              String fromLocation = "";
              if(srcMaterial.getDispatchFrom()!=0){
                  fromLocation = "(DF."+PstMatConDispatch.fieldNames[PstMatConDispatch.FLD_DISPATCH_FROM]+"="+srcMaterial.getDispatchFrom()+")";
              }

              String toLocation = "";
              if(srcMaterial.getDispatchTo()!=0){
                  toLocation = "(DF."+PstMatConDispatch.fieldNames[PstMatConDispatch.FLD_DISPATCH_TO]+"="+srcMaterial.getDispatchTo()+")";
              }

              String date = "";
              if(!srcMaterial.getIgnoreDate()){
                  date = "(DF."+PstMatConDispatch.fieldNames[PstMatConDispatch.FLD_DFM_DATE]+" BETWEEN \""+Formater.formatDate(srcMaterial.getDispatchDateFrom(), "yyyy-MM-dd")+"\" AND \""+
                      Formater.formatDate(srcMaterial.getDispatchDateTo(), "yyyy-MM-dd")+"\")";
              }

              String dfCode = "";
              if(srcMaterial.getDispatchCode().length()>0){
                  dfCode = "(DF."+PstMatConDispatch.fieldNames[PstMatConDispatch.FLD_DISPATCH_CODE]+" LIKE \"%"+srcMaterial.getDispatchCode()+"%\")";
              }

              String status = "";
              if(srcMaterial.getStatus()>=0){
                  status =  "(DF."+PstMatConDispatch.fieldNames[PstMatConDispatch.FLD_DF_STATUS]+" = "+(srcMaterial.getStatus())+")";
              }

              String referency = "";
              String innerReferency = "";
              if(srcMaterial.getRequestRefCode().length()>0){
                  innerReferency = " INNER JOIN "+PstMatConDispatch.TBL_MAT_DISPATCH+" AS REF ON REF."+PstMatConDispatch.fieldNames[PstMaterial.FLD_MATERIAL_ID]+"="+
                      " DF."+PstMatConDispatch.fieldNames[PstMatConDispatch.FLD_REFERENCE_ID]+" ";

                  referency = "(REF."+PstMatConDispatch.fieldNames[PstMatConDispatch.FLD_REFERENCE_ID]+" LIKE \"%"+srcMaterial.getRequestRefCode()+"%\")";
              }



              String where = "";
              String lastInner = "";
              if(materialCode.length()>0 || materialName.length()>0){
                    lastInner = innerJoin;
                    if(materialCode.length()>0){
                           where = materialCode;
                    }

                    if( materialName.length()>0){
                          if( where.length()>0){
                              where = where + " AND " + materialName;
                          }
                          else{
                              where = materialName;
                          }
                    }
              }

              if( fromLocation.length()>0){
                  if( where.length()>0){
                      where = where + " AND " + fromLocation;
                  }
                  else{
                      where = fromLocation;
                  }
               }

              if( toLocation.length()>0){
                  if( where.length()>0){
                      where = where + " AND " + toLocation;
                  }
                  else{
                      where = toLocation;
                  }
               }

              if( date.length()>0){
                  if( where.length()>0){
                      where = where + " AND " + date;
                  }
                  else{
                      where = date;
                  }
               }

              if( dfCode.length()>0){
                  if( where.length()>0){
                      where = where + " AND " + dfCode;
                  }
                  else{
                      where = dfCode;
                  }
               }

              if(status.length()>0){
                  if( where.length()>0){
                      where = where + " AND " + status;
                  }
                  else{
                      where = status;
                  }
              }

              if(referency.length()>0){
                  if( where.length()>0){
                      where = where + " AND " + referency;
                  }
                  else{
                      where = referency;
                  }
              }


              try{
                  I_PstDocType i_PstDocType = (I_PstDocType)Class.forName(I_DocType.DOCTYPE_CLASSNAME).newInstance();

                  if( where.length()>0){
                       where = where + " AND (DF."+ PstMatConDispatch.fieldNames[PstMatConDispatch.FLD_DOC_TYPE]+"="+i_PstDocType.composeDocumentType(I_DocType.SYSTEM_MATERIAL , I_DocType.MAT_DOC_TYPE_DF) +")"+
                          " AND ((DF."+PstMatConDispatch.fieldNames[PstMatConDispatch.FLD_DF_STATUS]+"="+I_DocStatus.DOCUMENT_STATUS_FINAL+") OR (DF."+PstMatConDispatch.fieldNames[PstMatConDispatch.FLD_DF_STATUS]+"="+I_DocStatus.DOCUMENT_STATUS_CLOSED+"))";

                   }
                   else{
                       where = "(DF."+ PstMatConDispatch.fieldNames[PstMatConDispatch.FLD_DOC_TYPE]+"="+i_PstDocType.composeDocumentType(I_DocType.SYSTEM_MATERIAL , I_DocType.MAT_DOC_TYPE_DF) +")"+
                          " AND ((DF."+PstMatConDispatch.fieldNames[PstMatConDispatch.FLD_DF_STATUS]+"="+I_DocStatus.DOCUMENT_STATUS_FINAL+") OR (DF."+PstMatConDispatch.fieldNames[PstMatConDispatch.FLD_DF_STATUS]+"="+I_DocStatus.DOCUMENT_STATUS_CLOSED+"))";


                   }
              }
              catch(Exception e){
                  System.out.println(e.toString());
              }


              if(where.length()>0){
                  where = " WHERE	("+where+")";
              }

               sql = sql + lastInner + innerReferency + where ;//+" ORDER BY "+orderBy[srcMaterial.getSortBy()]+" LIMIT "+start+","+limit;

             //  System.out.println(sql);

               DBResultSet dbrs = null;
               try{
                  dbrs = DBHandler.execQueryResult(sql);
                  ResultSet rs = dbrs.getResultSet();
                  while(rs.next()){
                      result = rs.getInt(1);
                  }

                  rs.close();
               }
               catch(Exception e){
                  DBResultSet.close(dbrs);
                  System.out.println("exception on search DF REG : "+e.toString());
               }
               finally{
                  DBResultSet.close(dbrs);
               }

               return result;

        }else{
        }
         */
        return result;
    }


    /* for material receive */
    public static int findLimitStartRec(long oid, int recordToGet, SrcMatConDispatch srcMaterial) {
        /*String order = "";
        int size = getCountRec(srcMaterial);
        int start = 0;
        boolean found =false;
        for(int i=0; (i < size) && !found ; i=i+recordToGet){
             Vector list =  listMatConDispatchRec(srcMaterial,i,recordToGet);
             start = i;
             if(list.size()>0){
              for(int ls=0;ls<list.size();ls++){
                     MatConDispatch materialdispatch = (MatConDispatch)list.get(ls);
                   if(oid == materialdispatch.getOID())
                      found=true;
              }
          }
        }
        if((start >= size) && (size > 0))
            start = start - recordToGet;

        return start;*/
        return 0;
    }


    public static int getDispatchRequestType(long fromLocId, long toLocId) {
        /*if(fromLocId!=0 && toLocId!=0){
             int dftype = -1;
             try{
                Location from = PstLocation.fetchExc(fromLocId);
                Location to = PstLocation.fetchExc(toLocId);

                switch(from.getType()){
                case PstLocation.TYPE_LOCATION_WAREHOUSE :
                    switch(to.getType()){
                    	case PstLocation.TYPE_LOCATION_WAREHOUSE : dftype = PstMatConDispatch.TYPE_REQUEST_WH_TO_WH; break;
                        case PstLocation.TYPE_LOCATION_OUTLET : dftype = PstMatConDispatch.TYPE_REQUEST_WH_TO_DEPARTMENT ; break;
                        case PstLocation.TYPE_LOCATION_SUB_CONTRACT : dftype = PstMatConDispatch.TYPE_REQUEST_WH_TO_SUBCONTRACT; break;
                	}
                	break;

                case PstLocation.TYPE_LOCATION_OUTLET :
                    switch(to.getType()){
                    	case PstLocation.TYPE_LOCATION_WAREHOUSE : dftype = PstMatConDispatch.TYPE_REQUEST_WH_TO_DEPARTMENT; break;
                        case PstLocation.TYPE_LOCATION_OUTLET : dftype = PstMatConDispatch.TYPE_REQUEST_DEP_TO_DEP ; break;
                        case PstLocation.TYPE_LOCATION_SUB_CONTRACT : dftype = PstMatConDispatch.TYPE_REQUEST_WH_TO_SUBCONTRACT; break;
                	}
                	break;

                case PstLocation.TYPE_LOCATION_SUB_CONTRACT :
                    switch(to.getType()){
                    	case PstLocation.TYPE_LOCATION_WAREHOUSE : dftype = PstMatConDispatch.TYPE_REQUEST_WH_TO_SUBCONTRACT; break;
                        case PstLocation.TYPE_LOCATION_OUTLET : dftype = PstMatConDispatch.TYPE_REQUEST_WH_TO_SUBCONTRACT ; break;
                        //case PstLocation.TYPE_LOCATION_SUB_CONTRACT : dftype = PstMatConDispatch.TYPE_REQUEST_WH_TO_SUBCONTRACT; break;
                	}
                	break;

                }//end case 1

             }
             catch(Exception e){
             }
             return dftype;
        }*/

        return -1;
    }


    public static int getDispatchType(long fromLocId, long toLocId) {
        /*if(fromLocId!=0 && toLocId!=0){
             int dftype = -1;
             try{
                Location from = PstLocation.fetchExc(fromLocId);
                Location to = PstLocation.fetchExc(toLocId);

                switch(from.getType()){
                case PstLocation.TYPE_LOCATION_WAREHOUSE :
                    switch(to.getType()){
                    	case PstLocation.TYPE_LOCATION_WAREHOUSE : dftype = PstMatConDispatch.TYPE_DISPATCH_WH_TO_WH; break;
                        case PstLocation.TYPE_LOCATION_OUTLET : dftype = PstMatConDispatch.TYPE_DISPATCH_WH_TO_DEPARTMENT ; break;
                        case PstLocation.TYPE_LOCATION_SUB_CONTRACT : dftype = PstMatConDispatch.TYPE_DISPATCH_WH_TO_SUBCONTRACT; break;
                	}
                	break;

                case PstLocation.TYPE_LOCATION_OUTLET :
                    switch(to.getType()){
                    	case PstLocation.TYPE_LOCATION_WAREHOUSE : dftype = PstMatConDispatch.TYPE_DISPATCH_WH_TO_DEPARTMENT; break;
                        case PstLocation.TYPE_LOCATION_OUTLET : dftype = PstMatConDispatch.TYPE_DISPATCH_DEP_TO_DEP ; break;
                        case PstLocation.TYPE_LOCATION_SUB_CONTRACT : dftype = PstMatConDispatch.TYPE_DISPATCH_WH_TO_SUBCONTRACT; break;
                	}
                	break;

                case PstLocation.TYPE_LOCATION_SUB_CONTRACT :
                    switch(to.getType()){
                    	case PstLocation.TYPE_LOCATION_WAREHOUSE : dftype = PstMatConDispatch.TYPE_DISPATCH_WH_TO_SUBCONTRACT; break;
                        case PstLocation.TYPE_LOCATION_OUTLET : dftype = PstMatConDispatch.TYPE_DISPATCH_WH_TO_SUBCONTRACT ; break;
                        //case PstLocation.TYPE_LOCATION_SUB_CONTRACT : dftype = PstMatConDispatch.TYPE_REQUEST_WH_TO_SUBCONTRACT; break;
                	}
                	break;

                }//end case 1

             }
             catch(Exception e){
             }
             return dftype;
        }*/

        return -1;
    }

    public static int countMaterialDispatch(int status, int docType) {
        DBResultSet dbrs = null;
        int result = 0;
        /*try{
            String sql = "SELECT COUNT("+PstMatConDispatch.fieldNames[PstMatConDispatch.FLD_MAT_DISPATCH_ID]+")"+
					" FROM "+PstMatConDispatch.TBL_MAT_DISPATCH+" WHERE "+PstMatConDispatch.fieldNames[PstMatConDispatch.FLD_DOC_TYPE]+
                    " = "+docType+" AND "+PstMatConDispatch.fieldNames[PstMatConDispatch.FLD_DF_STATUS]+" = "+status;

            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while(rs.next()){
                result = rs.getInt(1);
            }

        }
        catch(Exception e){
            System.out.println("Exception e : "+e.toString());
        }
        finally{
            DBResultSet.close(dbrs);
        }*/
        return result;
    }


    public static void getDataMaterial(SrcStockCard srcStockCard) {
        DBResultSet dbrs = null;
        try {
            String sql = "SELECT" +
                    " R." + PstMatConDispatch.fieldNames[PstMatConDispatch.FLD_DISPATCH_DATE] +
                    " ,R." + PstMatConDispatch.fieldNames[PstMatConDispatch.FLD_DISPATCH_CODE] +
                    " ,SUM(RI." + PstMatConDispatchItem.fieldNames[PstMatConDispatchItem.FLD_QTY] + ") AS SUM_QTY " +
                    " ,RI." + PstMatConDispatchItem.fieldNames[PstMatConDispatchItem.FLD_UNIT_ID] +
                    " ,M." + PstMaterial.fieldNames[PstMaterial.FLD_DEFAULT_STOCK_UNIT_ID] +
                    " FROM " + PstMatConDispatch.TBL_DISPATCH + " AS R " +
                    " INNER JOIN " + PstMatConDispatchItem.TBL_MAT_DISPATCH_ITEM + " AS RI " +
                    " ON R." + PstMatConDispatch.fieldNames[PstMatConDispatch.FLD_DISPATCH_MATERIAL_ID] +
                    " = RI." + PstMatConDispatchItem.fieldNames[PstMatConDispatchItem.FLD_DISPATCH_MATERIAL_ID] +
                    " INNER JOIN " + PstMaterial.TBL_MATERIAL+ " AS M " +
                    " ON RI." + PstMatConDispatchItem.fieldNames[PstMatConDispatchItem.FLD_MATERIAL_ID] +
                    " = M." + PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_ID];
            String whereClause = "";
            if (srcStockCard.getMaterialId() != 0) {
                whereClause = "RI." + PstMatConDispatchItem.fieldNames[PstMatConDispatchItem.FLD_MATERIAL_ID] + "=" + srcStockCard.getMaterialId();
            }
  
            if (srcStockCard.getLocationId() != 0) {
                if (whereClause.length() > 0) {
                    whereClause = whereClause + " AND R." + PstMatConDispatch.fieldNames[PstMatConDispatch.FLD_LOCATION_ID] + "=" + srcStockCard.getLocationId();
                } else {
                    whereClause = "R." + PstMatConDispatch.fieldNames[PstMatConDispatch.FLD_LOCATION_ID] + "=" + srcStockCard.getLocationId();
                }
            }

            if (srcStockCard.getStardDate() != null && srcStockCard.getEndDate() != null) {
                if (whereClause.length() > 0) {
                    whereClause = whereClause + " AND R." + PstMatConDispatch.fieldNames[PstMatConDispatch.FLD_DISPATCH_DATE] + " BETWEEN '" + Formater.formatDate(srcStockCard.getStardDate(), "yyyy-MM-dd 00:00:01") + "' AND '" + Formater.formatDate(srcStockCard.getEndDate(), "yyyy-MM-dd 23:59:59") + "'";
                } else {
                    whereClause = " R." + PstMatConDispatch.fieldNames[PstMatConDispatch.FLD_DISPATCH_DATE] + " BETWEEN '" + Formater.formatDate(srcStockCard.getStardDate(), "yyyy-MM-dd 00:00:01") + "' AND '" + Formater.formatDate(srcStockCard.getEndDate(), "yyyy-MM-dd 23:59:59") + "'";
                }
            }else{ // data privous
                if (whereClause.length() > 0) {
                    whereClause = whereClause + " AND R." + PstMatConDispatch.fieldNames[PstMatConDispatch.FLD_DISPATCH_DATE] + " <= '" + Formater.formatDate(srcStockCard.getStardDate(), "yyyy-MM-dd 00:00:00") + "'";
                } else {
                    whereClause = " R." + PstMatConDispatch.fieldNames[PstMatConDispatch.FLD_DISPATCH_DATE] + " <= '" + Formater.formatDate(srcStockCard.getStardDate(), "yyyy-MM-dd 00:00:00") + "'";
                }
            }

            if (whereClause.length() > 0) {
                sql = sql + " WHERE " + whereClause;
            }
            sql = sql + " GROUP BY R." + PstMatConDispatch.fieldNames[PstMatConDispatch.FLD_DISPATCH_DATE] +
                    " ,R." + PstMatConDispatch.fieldNames[PstMatConDispatch.FLD_DISPATCH_CODE];
            sql = sql + " ORDER BY R." + PstMatConDispatch.fieldNames[PstMatConDispatch.FLD_DISPATCH_CODE];
            //System.out.println("sql dispatch : "+sql);
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while (rs.next()) {
                StockCardReport stockCardReport = new StockCardReport();

                Date date = DBHandler.convertDate(rs.getDate(PstMatConDispatch.fieldNames[PstMatConDispatch.FLD_DISPATCH_DATE]),rs.getTime(PstMatConDispatch.fieldNames[PstMatConDispatch.FLD_DISPATCH_DATE]));
                stockCardReport.setDate(date);

                stockCardReport.setDocType(I_DocType.MAT_DOC_TYPE_DF);
                stockCardReport.setDocCode(rs.getString(PstMatConDispatch.fieldNames[PstMatConDispatch.FLD_DISPATCH_CODE]));
                double qtyBase = PstUnit.getQtyPerBaseUnit(rs.getLong(PstMatConDispatchItem.fieldNames[PstMatConDispatchItem.FLD_UNIT_ID]),rs.getLong(PstMaterial.fieldNames[PstMaterial.FLD_DEFAULT_STOCK_UNIT_ID]));
                stockCardReport.setQty(rs.getDouble("SUM_QTY") * qtyBase);                
                stockCardReport.setKeterangan("Pengiriman barang");

                PstStockCardReport.insertExc(stockCardReport);
            }

        } catch (Exception e) {
            System.out.println("err getDataMaterial : "+e.toString());
        }
    }
}
