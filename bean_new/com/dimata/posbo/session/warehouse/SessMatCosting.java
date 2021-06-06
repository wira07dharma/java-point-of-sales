package com.dimata.posbo.session.warehouse;

import com.dimata.qdep.form.Control;
import com.dimata.posbo.db.DBResultSet;
import com.dimata.posbo.db.DBHandler;
import com.dimata.qdep.entity.I_DocType;
import com.dimata.posbo.entity.warehouse.*;
import com.dimata.common.entity.location.PstLocation;
import com.dimata.common.entity.location.Location;
import com.dimata.util.Formater;
import com.dimata.posbo.entity.masterdata.PstMaterial;
import com.dimata.posbo.entity.masterdata.PstUnit;


//adding import for reposting stok
import com.dimata.posbo.entity.search.*;

import java.util.Vector;
import java.util.Date;
import java.sql.ResultSet;

public class SessMatCosting extends Control {

    public static final String SESS_SRC_MATDISPATCH = "SESSION_MATERIAL_DISPATCH";
    public static final String SESSION_MATERIAL_DISPATCH_EXC = "SESSION_MATERIAL_DISPATCH_EXC";
    public static final String SESSION_TRANSFER_MR_TO_DF = "SESSION_TRANSFER_MR_TO_DF";
    public static final String SESSION_MATERIAL_DISPATCH_RECEIVED = "SESSION_MATERIAL_DISPATCH_RECEIVED";

    public static final String[] orderBy =
    {
        PstMatCosting.fieldNames[PstMatCosting.FLD_COSTING_CODE],
        PstMatCosting.fieldNames[PstMatCosting.FLD_LOCATION_ID],
        PstMatCosting.fieldNames[PstMatCosting.FLD_COSTING_TO],
        PstMatCosting.fieldNames[PstMatCosting.FLD_COSTING_DATE],
        PstMatCosting.fieldNames[PstMatCosting.FLD_COSTING_STATUS]
    };

    /* list for material request */
    public static Vector listMatCosting(SrcMatCosting srcMatCosting, int start, int limit, String whereLocation) {
        Vector result = new Vector(1, 1);
        String sql = "SELECT DF." + PstMatCosting.fieldNames[PstMatCosting.FLD_COSTING_MATERIAL_ID] +
                " , DF." + PstMatCosting.fieldNames[PstMatCosting.FLD_COSTING_CODE] +
                " , DF." + PstMatCosting.fieldNames[PstMatCosting.FLD_COSTING_DATE] +
                " , LC." + PstLocation.fieldNames[PstLocation.FLD_NAME] +
                " , DF." + PstMatCosting.fieldNames[PstMatCosting.FLD_COSTING_TO] +
                " , DF." + PstMatCosting.fieldNames[PstMatCosting.FLD_COSTING_STATUS] +
                " , DF." + PstMatCosting.fieldNames[PstMatCosting.FLD_REMARK] +
                " FROM " + PstMatCosting.TBL_COSTING + " DF" +
                " INNER JOIN " + PstLocation.TBL_P2_LOCATION + " LC" +
                " ON DF." + PstMatCosting.fieldNames[PstMatCosting.FLD_LOCATION_ID] +
                " = LC." + PstLocation.fieldNames[PstLocation.FLD_LOCATION_ID];


        if (srcMatCosting != null) {
            String fromLocation = "";
            if (srcMatCosting.getCostingFrom() != 0) {
                fromLocation = "(DF." + PstMatCosting.fieldNames[PstMatCosting.FLD_LOCATION_ID] + "=" + srcMatCosting.getCostingFrom() + ")";
            }

            String toLocation = "";
            if (srcMatCosting.getCostingTo() != 0) {
                toLocation = "(DF." + PstMatCosting.fieldNames[PstMatCosting.FLD_COSTING_TO] + "=" + srcMatCosting.getCostingTo() + ")";
            }

            String date = "";
            if (srcMatCosting.getIgnoreDate()) {
                date = "(DF." + PstMatCosting.fieldNames[PstMatCosting.FLD_COSTING_DATE] + " BETWEEN '" + Formater.formatDate(srcMatCosting.getCostingDateFrom(), "yyyy-MM-dd 00:00:00") + "' AND '" +
                        Formater.formatDate(srcMatCosting.getCostingDateTo(), "yyyy-MM-dd 23:59:59") + "')";
            }

            String dfCode = "";
            if (srcMatCosting.getCostingCode().length() > 0) {
                dfCode = "(DF." + PstMatCosting.fieldNames[PstMatCosting.FLD_COSTING_CODE] + " LIKE '" + srcMatCosting.getCostingCode() + "%')";
            }

            String status = "";
            if (srcMatCosting.getStatus() >= 0) {
                status = "(DF." + PstMatCosting.fieldNames[PstMatCosting.FLD_COSTING_STATUS] + " = " + (srcMatCosting.getStatus()) + ")";
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
                   
            if(!"".equals(whereLocation)){
               if (where.length() > 0) {
                   where= where + " AND " + whereLocation;
               }else{
                   where= where + whereLocation;
               }
               
            }

            if (where.length() > 0) {
                where = " WHERE	" + where;
            }

            sql += where;
            //sql += " ORDER BY DF." + orderBy[srcMatCosting.getSortBy()];
            //sql += ", DF."+PstMatCosting.fieldNames[PstMatCosting.FLD_COSTING_DATE]+" DESC "; /** defaultnya, list diurut berdasarkan DATE */
            String orderBy = "";
            switch(srcMatCosting.getSortBy()){                  
                case 0:
                    orderBy = orderBy + " ORDER BY DF."+PstMatCosting.fieldNames[PstMatCosting.FLD_COSTING_CODE] + " DESC";
                break;
                case 1:
                    orderBy = orderBy + " ORDER BY DF."+PstMatCosting.fieldNames[PstMatCosting.FLD_COSTING_DATE] + " DESC";
                break;
                case 2:
                    orderBy = orderBy + " ORDER BY DF." + PstMatCosting.fieldNames[PstMatCosting.FLD_LOCATION_ID] +" DESC";
                break;
                case 3:
                    orderBy = orderBy + " ORDER BY DF." + PstMatCosting.fieldNames[PstMatCosting.FLD_COSTING_TO] +" DESC";
                break;
                case 4:
                    orderBy = orderBy + " ORDER BY DF." + PstMatCosting.fieldNames[PstMatCosting.FLD_COSTING_STATUS] +" DESC";
                break;
                    
            }
        }
        System.out.println("sql > "+sql);
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
                MatCosting matDf = new MatCosting();
                Location loc1 = new Location();

                matDf.setOID(rs.getLong(1));
                matDf.setCostingCode(rs.getString(2));
                matDf.setCostingDate(rs.getDate(3));
                matDf.setCostingTo(rs.getLong(5));
                matDf.setCostingStatus(rs.getInt(6));
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

    public static Vector listMatCosting(SrcMatCosting srcMatCosting, int start, int limit) {
        Vector result = new Vector(1, 1);
        String sql = "SELECT DF." + PstMatCosting.fieldNames[PstMatCosting.FLD_COSTING_MATERIAL_ID] +
                " , DF." + PstMatCosting.fieldNames[PstMatCosting.FLD_COSTING_CODE] +
                " , DF." + PstMatCosting.fieldNames[PstMatCosting.FLD_COSTING_DATE] +
                " , LOC." + PstLocation.fieldNames[PstLocation.FLD_NAME] +
                " , DF." + PstMatCosting.fieldNames[PstMatCosting.FLD_COSTING_TO] +
                " , DF." + PstMatCosting.fieldNames[PstMatCosting.FLD_COSTING_STATUS] +
                " , DF." + PstMatCosting.fieldNames[PstMatCosting.FLD_REMARK] +
                " FROM " + PstMatCosting.TBL_COSTING + " DF" +
                " INNER JOIN " + PstLocation.TBL_P2_LOCATION + " LOC" +
                " ON DF." + PstMatCosting.fieldNames[PstMatCosting.FLD_LOCATION_ID] +
                " = LOC." + PstLocation.fieldNames[PstLocation.FLD_LOCATION_ID];


        if (srcMatCosting != null) {
            String fromLocation = "";
            if (srcMatCosting.getCostingFrom() != 0) {
                fromLocation = "(DF." + PstMatCosting.fieldNames[PstMatCosting.FLD_LOCATION_ID] + "=" + srcMatCosting.getCostingFrom() + ")";
            }

            String toLocation = "";
            if (srcMatCosting.getCostingTo() != 0) {
                toLocation = "(DF." + PstMatCosting.fieldNames[PstMatCosting.FLD_COSTING_TO] + "=" + srcMatCosting.getCostingTo() + ")";
            }

            String date = "";
            if (!srcMatCosting.getIgnoreDate()) {
                date = "(DF." + PstMatCosting.fieldNames[PstMatCosting.FLD_COSTING_DATE] + " BETWEEN '" + Formater.formatDate(srcMatCosting.getCostingDateFrom(), "yyyy-MM-dd 00:00:01") + "' AND '" +
                        Formater.formatDate(srcMatCosting.getCostingDateTo(), "yyyy-MM-dd 23:59:59") + "')";
            }

            String dfCode = "";
            if (srcMatCosting.getCostingCode().length() > 0) {
                dfCode = "(DF." + PstMatCosting.fieldNames[PstMatCosting.FLD_COSTING_CODE] + " LIKE '" + srcMatCosting.getCostingCode() + "%')";
            }

            String status = "";
            if (srcMatCosting.getStatus() >= 0) {
                status = "(DF." + PstMatCosting.fieldNames[PstMatCosting.FLD_COSTING_STATUS] + " = " + (srcMatCosting.getStatus()) + ")";
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
            sql += " ORDER BY DF." + orderBy[srcMatCosting.getSortBy()];
            sql += ", DF."+PstMatCosting.fieldNames[PstMatCosting.FLD_COSTING_DATE]; /** defaultnya, list diurut berdasarkan DATE */
        }
        System.out.println("sql > "+sql);
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
                MatCosting matDf = new MatCosting();
                Location loc1 = new Location();

                matDf.setOID(rs.getLong(1));
                matDf.setCostingCode(rs.getString(2));
                matDf.setCostingDate(rs.getDate(3));
                matDf.setCostingTo(rs.getLong(5));
                matDf.setCostingStatus(rs.getInt(6));
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
    
    public static int getCountMatCosting(SrcMatCosting srcMatCosting, String whereLocation) {

        int result = 0;
        String sql = "SELECT COUNT(DF." + PstMatCosting.fieldNames[PstMatCosting.FLD_COSTING_MATERIAL_ID] +
                " ) AS CNT FROM " + PstMatCosting.TBL_COSTING + " DF" +
                " INNER JOIN " + PstLocation.TBL_P2_LOCATION + " LC" +
                " ON DF." + PstMatCosting.fieldNames[PstMatCosting.FLD_LOCATION_ID] +
                " = LC." + PstLocation.fieldNames[PstLocation.FLD_LOCATION_ID];

        if (srcMatCosting != null) {
            String fromLocation = "";

            if (srcMatCosting.getCostingFrom() != 0) {
                fromLocation = "(DF." + PstMatCosting.fieldNames[PstMatCosting.FLD_LOCATION_ID] + "=" + srcMatCosting.getCostingFrom() + ")";
            }

            String toLocation = "";
            if (srcMatCosting.getCostingTo() != 0) {
                toLocation = "(DF." + PstMatCosting.fieldNames[PstMatCosting.FLD_COSTING_TO] + "=" + srcMatCosting.getCostingTo() + ")";
            }

            String date = "";
            if (srcMatCosting.getIgnoreDate()) {
                date = "(DF." + PstMatCosting.fieldNames[PstMatCosting.FLD_COSTING_DATE] + " BETWEEN '" + Formater.formatDate(srcMatCosting.getCostingDateFrom(), "yyyy-MM-dd 00:00:00") + "' AND '" +
                        Formater.formatDate(srcMatCosting.getCostingDateTo(), "yyyy-MM-dd 23:59:59") + "')";
            }

            String dfCode = "";
            if (srcMatCosting.getCostingCode().length() > 0) {
                dfCode = "(DF." + PstMatCosting.fieldNames[PstMatCosting.FLD_COSTING_CODE] + " LIKE '" + srcMatCosting.getCostingCode() + "%')";
            }

            String status = "";
            if (srcMatCosting.getStatus() >= 0) {
                status = "(DF." + PstMatCosting.fieldNames[PstMatCosting.FLD_COSTING_STATUS] + " = " + (srcMatCosting.getStatus()) + ")";
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
            
            if(!"".equals(whereLocation)){
               if (where.length() > 0) {
                   where= where + " AND " + whereLocation;
               }else{
                   where= where + whereLocation;
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

  /**
   * mencari informasi document costing untuk document yang final dan draft
   * @return
   */
  public static Vector getMatCostingInformation(String where) {
        DBResultSet dbrs = null;
	Vector vListCosting = new Vector(1, 1);
        String sql = "SELECT COUNT(DF." + PstMatCosting.fieldNames[PstMatCosting.FLD_COSTING_MATERIAL_ID] +" ) AS CNT "+
                " , " + "(DF." + PstMatCosting.fieldNames[PstMatCosting.FLD_COSTING_STATUS] + ") AS STATUS "+
                "FROM " + PstMatCosting.TBL_COSTING + " DF" +
                " INNER JOIN " + PstLocation.TBL_P2_LOCATION + " LC " +
                " ON DF." + PstMatCosting.fieldNames[PstMatCosting.FLD_LOCATION_ID] +
                " = LC." + PstLocation.fieldNames[PstLocation.FLD_LOCATION_ID];
        /**
         * SELECT COUNT(DF.COSTING_MATERIAL_ID ) AS CNT FROM pos_costing_material DF INNER JOIN location LOC ON DF.LOCATION_ID = LOC.LOCATION_ID
         * WHERE	(DF.COSTING_STATUS = 0)
         */
         String strStatus = "";
          strStatus = "((DF." + PstMatCosting.fieldNames[PstMatCosting.FLD_COSTING_STATUS]+ " = 0)"+
                        " OR " + "(DF." + PstMatCosting.fieldNames[PstMatCosting.FLD_COSTING_STATUS] + " = 2))";
          if(where.length()>0){
               strStatus=strStatus + where;
            }
          strStatus=strStatus+" GROUP BY DF." + PstMatCosting.fieldNames[PstMatCosting.FLD_COSTING_STATUS];


            sql = sql + " WHERE " + strStatus;
        try {
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while (rs.next()) {
                 Vector vlist = new Vector(1, 1);
                vlist.add(rs.getInt("CNT"));
                 vlist.add(rs.getInt("STATUS"));
                vListCosting.add(vlist);
            }
             rs.close();
            return vListCosting;
        } catch (Exception e) {
            System.out.println("exception on count search DF : " + e.toString());
        } finally {
            DBResultSet.close(dbrs);
        }
         return new Vector(1, 1);
    }

     /**
   * mencari informasi document costing untuk document yang final dan draft
   * @return
   */
  public static Vector getMatCostingInformation() {
        DBResultSet dbrs = null;
	Vector vListCosting = new Vector(1, 1);
        String sql = "SELECT COUNT(DF." + PstMatCosting.fieldNames[PstMatCosting.FLD_COSTING_MATERIAL_ID] +" ) AS CNT "+
                " , " + "(DF." + PstMatCosting.fieldNames[PstMatCosting.FLD_COSTING_STATUS] + ") AS STATUS "+
                "FROM " + PstMatCosting.TBL_COSTING + " DF" +
                " INNER JOIN " + PstLocation.TBL_P2_LOCATION + " LOC" +
                " ON DF." + PstMatCosting.fieldNames[PstMatCosting.FLD_LOCATION_ID] +
                " = LOC." + PstLocation.fieldNames[PstLocation.FLD_LOCATION_ID];
        /**
         * SELECT COUNT(DF.COSTING_MATERIAL_ID ) AS CNT FROM pos_costing_material DF INNER JOIN location LOC ON DF.LOCATION_ID = LOC.LOCATION_ID
         * WHERE	(DF.COSTING_STATUS = 0)
         */
         String strStatus = "";
          strStatus = "((DF." + PstMatCosting.fieldNames[PstMatCosting.FLD_COSTING_STATUS]+ " = 0)"+
                        " OR " + "(DF." + PstMatCosting.fieldNames[PstMatCosting.FLD_COSTING_STATUS] + " = 2))"+
                        " GROUP BY DF." + PstMatCosting.fieldNames[PstMatCosting.FLD_COSTING_STATUS];


            sql = sql + " WHERE " + strStatus;
        try {
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while (rs.next()) {
                 Vector vlist = new Vector(1, 1);
                vlist.add(rs.getInt("CNT"));
                 vlist.add(rs.getInt("STATUS"));
                vListCosting.add(vlist);
            }
             rs.close();
            return vListCosting;
        } catch (Exception e) {
            System.out.println("exception on count search DF : " + e.toString());
        } finally {
            DBResultSet.close(dbrs);
        }
         return new Vector(1, 1);
    }
  
    public static int getCountMatCosting(SrcMatCosting srcMatCosting) {

        int result = 0;
        String sql = "SELECT COUNT(DF." + PstMatCosting.fieldNames[PstMatCosting.FLD_COSTING_MATERIAL_ID] +
                " ) AS CNT FROM " + PstMatCosting.TBL_COSTING + " DF" +
                " INNER JOIN " + PstLocation.TBL_P2_LOCATION + " LOC" +
                " ON DF." + PstMatCosting.fieldNames[PstMatCosting.FLD_LOCATION_ID] +
                " = LOC." + PstLocation.fieldNames[PstLocation.FLD_LOCATION_ID];

        if (srcMatCosting != null) {
            String fromLocation = "";

            if (srcMatCosting.getCostingFrom() != 0) {
                fromLocation = "(DF." + PstMatCosting.fieldNames[PstMatCosting.FLD_LOCATION_ID] + "=" + srcMatCosting.getCostingFrom() + ")";
            }

            String toLocation = "";
            if (srcMatCosting.getCostingTo() != 0) {
                toLocation = "(DF." + PstMatCosting.fieldNames[PstMatCosting.FLD_COSTING_TO] + "=" + srcMatCosting.getCostingTo() + ")";
            }

            String date = "";
            if (!srcMatCosting.getIgnoreDate()) {
                date = "(DF." + PstMatCosting.fieldNames[PstMatCosting.FLD_COSTING_DATE] + " BETWEEN '" + Formater.formatDate(srcMatCosting.getCostingDateFrom(), "yyyy-MM-dd 00:00:01") + "' AND '" +
                        Formater.formatDate(srcMatCosting.getCostingDateTo(), "yyyy-MM-dd 23:59:59") + "')";
            }

            String dfCode = "";
            if (srcMatCosting.getCostingCode().length() > 0) {
                dfCode = "(DF." + PstMatCosting.fieldNames[PstMatCosting.FLD_COSTING_CODE] + " LIKE '" + srcMatCosting.getCostingCode() + "%')";
            }

            String status = "";
            if (srcMatCosting.getStatus() >= 0) {
                status = "(DF." + PstMatCosting.fieldNames[PstMatCosting.FLD_COSTING_STATUS] + " = " + (srcMatCosting.getStatus()) + ")";
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
    
    public static int findLimitStart(long oid, int recordToGet, SrcMatCosting srcMaterial) {
        return 0;
    }


    public String generateRequestCode(MatCosting materialDf) {

        String code = "DR";
       
        return code;
    }

    public static int getMaxReqCounter(Date date) {
        int counter = 0;
      
        return counter;
    }

    
    public static int getCount(SrcMatCosting srcMaterial) {
        int result = 0;
        return result;
    }


    public String generateCostingCode(MatCosting matCosting) {
        String code = "CST";
        String dateCode = "";
        if (matCosting.getCostingDate() != null) {
            /** get location code; gwawan@21juni2007 */
            Vector vctLocation = PstLocation.list(0, 0, PstLocation.fieldNames[PstLocation.FLD_LOCATION_ID]+"="+matCosting.getLocationId(), "");
            Location location = (Location)vctLocation.get(0);
            
            int nextCounter = matCosting.getCostingCodeCounter();//getMaxCounter(date);
            Date date = matCosting.getCostingDate();

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

    public static int getMaxCostingCounter(Date date, MatCosting matCosting) {
        int counter = 0;
        DBResultSet dbrs = null;
        try {

            Date startDate = (Date) date.clone();
            startDate.setDate(1);

            Date endDate = (Date) date.clone();
            endDate.setMonth(endDate.getMonth() + 1);
            endDate.setDate(1);
            endDate.setDate(endDate.getDate() - 1);

            //I_PstDocType i_PstDocType = (I_PstDocType) Class.forName(I_DocType.DOCTYPE_CLASSNAME).newInstance();

            String sql = "SELECT MAX(" + PstMatCosting.fieldNames[PstMatCosting.FLD_COSTING_CODE_COUNTER] + ") AS MAXIMUM " +
                    " FROM " + PstMatCosting.TBL_COSTING+
                    " WHERE (" + PstMatCosting.fieldNames[PstMatCosting.FLD_COSTING_DATE] +
                    " BETWEEN '" + Formater.formatDate(startDate, "yyyy-MM-dd 00:00:01") +
                    " ' AND '" + Formater.formatDate(endDate, "yyyy-MM-dd 23:59:59") +
                    " ') AND " + PstMatCosting.fieldNames[PstMatCosting.FLD_LOCATION_ID] + " = " + matCosting.getLocationId();

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

    /**
     * untuk mencari data qty stock card
     * @param srcStockCard
     */
    public static void getDataMaterial(SrcStockCard srcStockCard) {
        DBResultSet dbrs = null;
        try {
            String sql = "SELECT" +
                    " R." + PstMatCosting.fieldNames[PstMatCosting.FLD_COSTING_DATE] +
                    " ,R." + PstMatCosting.fieldNames[PstMatCosting.FLD_COSTING_CODE] +
                    " ,R." + PstMatCosting.fieldNames[PstMatCosting.FLD_LOCATION_ID] +
                    " ,SUM(RI." + PstMatCostingItem.fieldNames[PstMatCostingItem.FLD_QTY] + ") AS SUM_QTY " +
                    " ,SUM(RI." + PstMatCostingItem.fieldNames[PstMatCostingItem.FLD_WEIGHT] + ") AS SUM_BERAT " +
                    " ,RI." + PstMatCostingItem.fieldNames[PstMatCostingItem.FLD_QTY] +
                    " ,M." + PstMaterial.fieldNames[PstMaterial.FLD_DEFAULT_STOCK_UNIT_ID] +
                    " ,RI." + PstMatCostingItem.fieldNames[PstMatCostingItem.FLD_UNIT_ID] +
                    " FROM " + PstMatCosting.TBL_COSTING+ " AS R " +
                    " INNER JOIN " + PstMatCostingItem.TBL_MAT_COSTING_ITEM+ " AS RI " +
                    " ON R." + PstMatCosting.fieldNames[PstMatCosting.FLD_COSTING_MATERIAL_ID] +
                    " = RI." + PstMatCostingItem.fieldNames[PstMatCostingItem.FLD_COSTING_MATERIAL_ID] +
                    " INNER JOIN " + PstMaterial.TBL_MATERIAL+ " AS M " +
                    " ON RI." + PstMatCostingItem.fieldNames[PstMatCostingItem.FLD_MATERIAL_ID] +
                    " = M." + PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_ID];
            String whereClause = "";
            if (srcStockCard.getMaterialId() != 0) {
                whereClause = "RI." + PstMatCostingItem.fieldNames[PstMatCostingItem.FLD_MATERIAL_ID] + "=" + srcStockCard.getMaterialId();
            }

            if (srcStockCard.getLocationId() != 0) {
                if (whereClause.length() > 0) {
                    whereClause += " AND ";
                }
                if (srcStockCard.getWarehouseLocationId() != 0) {
                    whereClause += " R." + PstMatCosting.fieldNames[PstMatCosting.FLD_LOCATION_ID] + " IN (" + srcStockCard.getLocationId() + "," + srcStockCard.getWarehouseLocationId() + ")";
                } else {
                    whereClause += " R." + PstMatCosting.fieldNames[PstMatCosting.FLD_LOCATION_ID] + " = " + srcStockCard.getLocationId();
                }
            }

            if (srcStockCard.getStardDate() != null && srcStockCard.getEndDate() != null) {
                if (whereClause.length() > 0) {
                    whereClause += " AND R." + PstMatCosting.fieldNames[PstMatCosting.FLD_COSTING_DATE] + " BETWEEN '" + Formater.formatDate(srcStockCard.getStardDate(), "yyyy-MM-dd 00:00:01") + "' AND '" + Formater.formatDate(srcStockCard.getEndDate(), "yyyy-MM-dd 23:59:59") + "'";
                } else {
                    whereClause += " R." + PstMatCosting.fieldNames[PstMatCosting.FLD_COSTING_DATE] + " BETWEEN '" + Formater.formatDate(srcStockCard.getStardDate(), "yyyy-MM-dd 00:00:01") + "' AND '" + Formater.formatDate(srcStockCard.getEndDate(), "yyyy-MM-dd 23:59:59") + "'";
                }
            }else{ // data privous
                //update opie-eyek 20161021
                if (whereClause.length() > 0) {
                    whereClause += " AND R." + PstMatCosting.fieldNames[PstMatCosting.FLD_COSTING_DATE] + " <= '" + Formater.formatDate(srcStockCard.getStardDate(), "yyyy-MM-dd 00:00:00") + "'";
                } else {
                    whereClause += " R." + PstMatCosting.fieldNames[PstMatCosting.FLD_COSTING_DATE] + " <= '" + Formater.formatDate(srcStockCard.getStardDate(), "yyyy-MM-dd 00:00:00") + "'";
                }
            }
            
            String strStatus = "";
            if (srcStockCard.getDocStatus() != null && srcStockCard.getDocStatus().size() > 0) {
                for (int n = 0; n < srcStockCard.getDocStatus().size(); n++) {
                    if (strStatus.length() != 0) {
                        strStatus = strStatus + " OR " + "(R." + PstMatCosting.fieldNames[PstMatCosting.FLD_COSTING_STATUS] + " = " + srcStockCard.getDocStatus().get(n) + ")";
                    } else {
                        strStatus = "(R." + PstMatCosting.fieldNames[PstMatCosting.FLD_COSTING_STATUS] + " = " + srcStockCard.getDocStatus().get(n) + ")";
                    }
                }
                strStatus = "(" + strStatus + ")";
            }
            
            if (strStatus.length() > 0) {
                if (whereClause.length() > 0) {
                    whereClause +=" AND " + strStatus;
                } else {
                    whereClause = strStatus;
                }
            }
            
            if (whereClause.length() > 0) {
                sql += " WHERE " + whereClause;
            }
            
            sql = sql + " GROUP BY R." + PstMatCosting.fieldNames[PstMatCosting.FLD_COSTING_DATE] +
                    " ,R." + PstMatCosting.fieldNames[PstMatCosting.FLD_COSTING_CODE];
            sql = sql + " ORDER BY R." + PstMatCosting.fieldNames[PstMatCosting.FLD_COSTING_CODE];
            //System.out.println("sql dispatch : "+sql);
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while (rs.next()) {
                StockCardReport stockCardReport = new StockCardReport();

                //System.out.println("Date : "+rs.getDate(PstMatCosting.fieldNames[PstMatCosting.FLD_COSTING_DATE]));
                Date date = DBHandler.convertDate(rs.getDate(PstMatCosting.fieldNames[PstMatCosting.FLD_COSTING_DATE]),rs.getTime(PstMatCosting.fieldNames[PstMatCosting.FLD_COSTING_DATE]));
                stockCardReport.setDate(date);
                //System.out.println("Date : "+date);

                stockCardReport.setDocType(I_DocType.MAT_DOC_TYPE_COS);
                stockCardReport.setDocCode(rs.getString(PstMatCosting.fieldNames[PstMatCosting.FLD_COSTING_CODE]));
                double qtyBase = PstUnit.getQtyPerBaseUnit(rs.getLong(PstMatCostingItem.fieldNames[PstMatCostingItem.FLD_UNIT_ID]),rs.getLong(PstMaterial.fieldNames[PstMaterial.FLD_DEFAULT_STOCK_UNIT_ID]));
                stockCardReport.setQty(rs.getDouble("SUM_QTY") * qtyBase);
                stockCardReport.setBerat(rs.getDouble("SUM_BERAT"));
                stockCardReport.setLocationId(rs.getLong(PstMatCosting.fieldNames[PstMatCosting.FLD_LOCATION_ID]));
                
                if(srcStockCard.getLanguage()==com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT){
                    stockCardReport.setKeterangan("Pembiayaan");
                }else{
                    stockCardReport.setKeterangan("Costing");
                }
                
                PstStockCardReport.insertExc(stockCardReport);
            }

        } catch (Exception e) {
            System.out.println("err getDataMaterial : "+e.toString());
        }
    }
    
     /**
     * untuk mencari data qty stock card Costing
     * untuk perhitungan reposting stok berdasarkan kartu stok
     * by Mirahu 20120803
     * @param srcStockCard
     */
    public static void getQtyStockMaterialReposting(SrcMaterialRepostingStock srcMaterialRepostingStock) {
        DBResultSet dbrs = null;
        try {
            String sql = "SELECT" +
                    " SUM(RI." + PstMatCostingItem.fieldNames[PstMatCostingItem.FLD_QTY] + ") AS SUM_QTY " +
                    " ,M." + PstMaterial.fieldNames[PstMaterial.FLD_DEFAULT_STOCK_UNIT_ID] +
                    " ,RI." + PstMatCostingItem.fieldNames[PstMatCostingItem.FLD_UNIT_ID] +
                    " FROM " + PstMatCosting.TBL_COSTING+ " AS R " +
                    " INNER JOIN " + PstMatCostingItem.TBL_MAT_COSTING_ITEM+ " AS RI " +
                    " ON R." + PstMatCosting.fieldNames[PstMatCosting.FLD_COSTING_MATERIAL_ID] +
                    " = RI." + PstMatCostingItem.fieldNames[PstMatCostingItem.FLD_COSTING_MATERIAL_ID] +
                    " INNER JOIN " + PstMaterial.TBL_MATERIAL+ " AS M " +
                    " ON RI." + PstMatCostingItem.fieldNames[PstMatCostingItem.FLD_MATERIAL_ID] +
                    " = M." + PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_ID];
            String whereClause = "";
            if (srcMaterialRepostingStock.getMaterialId() != 0) {
                whereClause = "RI." + PstMatCostingItem.fieldNames[PstMatCostingItem.FLD_MATERIAL_ID] + "=" + srcMaterialRepostingStock.getMaterialId();
            }

            if (srcMaterialRepostingStock.getLocationId() != 0) {
                if (whereClause.length() > 0) {
                    whereClause = whereClause + " AND R." + PstMatCosting.fieldNames[PstMatCosting.FLD_LOCATION_ID] + "=" + srcMaterialRepostingStock.getLocationId();
                } else {
                    whereClause = "R." + PstMatCosting.fieldNames[PstMatCosting.FLD_LOCATION_ID] + "=" + srcMaterialRepostingStock.getLocationId();
                }
            }

            if (srcMaterialRepostingStock.getDateFrom() != null && srcMaterialRepostingStock.getDateTo() != null) {
                if (whereClause.length() > 0) {
                    whereClause = whereClause + " AND R." + PstMatCosting.fieldNames[PstMatCosting.FLD_COSTING_DATE] + " BETWEEN '" + Formater.formatDate(srcMaterialRepostingStock.getDateFrom(), "yyyy-MM-dd HH:mm:ss") + "' AND '" + Formater.formatDate(srcMaterialRepostingStock.getDateTo(), "yyyy-MM-dd HH:mm:ss") + "'";
                } else {
                    whereClause = " R." + PstMatCosting.fieldNames[PstMatCosting.FLD_COSTING_DATE] + " BETWEEN '" + Formater.formatDate(srcMaterialRepostingStock.getDateFrom(), "yyyy-MM-dd HH:mm:ss") + "' AND '" + Formater.formatDate(srcMaterialRepostingStock.getDateTo(), "yyyy-MM-dd HH:mm:ss") + "'";
                }
            }else{ // data privous
                if (whereClause.length() > 0) {
                    whereClause = whereClause + " AND R." + PstMatCosting.fieldNames[PstMatCosting.FLD_COSTING_DATE] + " <= '" + Formater.formatDate(srcMaterialRepostingStock.getDateFrom(), "yyyy-MM-dd HH:mm:ss") + "'";
                } else {
                    whereClause = " R." + PstMatCosting.fieldNames[PstMatCosting.FLD_COSTING_DATE] + " <= '" + Formater.formatDate(srcMaterialRepostingStock.getDateFrom(), "yyyy-MM-dd HH:mm:ss") + "'";
                }
            }

            String strStatus = "";
            if (srcMaterialRepostingStock.getDocStatus() != null && srcMaterialRepostingStock.getDocStatus().size() > 0) {
                for (int n = 0; n < srcMaterialRepostingStock.getDocStatus().size(); n++) {
                    if (strStatus.length() != 0) {
                        strStatus = strStatus + " OR " + "(R." + PstMatCosting.fieldNames[PstMatCosting.FLD_COSTING_STATUS] + " = " + srcMaterialRepostingStock.getDocStatus().get(n) + ")";
                    } else {
                        strStatus = "(R." + PstMatCosting.fieldNames[PstMatCosting.FLD_COSTING_STATUS] + " = " + srcMaterialRepostingStock.getDocStatus().get(n) + ")";
                    }
                }
                strStatus = "(" + strStatus + ")";
            }

            if (strStatus.length() > 0) {
                if (whereClause.length() > 0) {
                    whereClause +=" AND " + strStatus;
                } else {
                    whereClause = strStatus;
                }
            }

            if (whereClause.length() > 0) {
                sql += " WHERE " + whereClause;
            }

            sql = sql + " GROUP BY R." + PstMatCosting.fieldNames[PstMatCosting.FLD_COSTING_DATE] +
                    " ,R." + PstMatCosting.fieldNames[PstMatCosting.FLD_COSTING_CODE]+
                    ",RI." + PstMatCostingItem.fieldNames[PstMatCostingItem.FLD_UNIT_ID];
            sql = sql + " ORDER BY R." + PstMatCosting.fieldNames[PstMatCosting.FLD_COSTING_CODE];
            System.out.println("sql costing : "+sql);
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            double qtyMaterial = 0;
            double qtyAll = 0;
            while (rs.next()) {
                //StockCardReport stockCardReport = new StockCardReport();

                //System.out.println("Date : "+rs.getDate(PstMatCosting.fieldNames[PstMatCosting.FLD_COSTING_DATE]));
            
                //stockCardReport.setDocType(I_DocType.MAT_DOC_TYPE_COS);
                //double qtyBase = PstUnit.getQtyPerBaseUnit(rs.getLong(PstMatCostingItem.fieldNames[PstMatCostingItem.FLD_UNIT_ID]),rs.getLong(PstMaterial.fieldNames[PstMaterial.FLD_DEFAULT_STOCK_UNIT_ID]));
                //disable konversi unit yang berhubungan
                //double qtyBase = PstUnit.getUnitFactory(rs.getLong(PstMatCostingItem.fieldNames[PstMatCostingItem.FLD_UNIT_ID]),rs.getLong(PstMaterial.fieldNames[PstMaterial.FLD_DEFAULT_STOCK_UNIT_ID]),0,4);
                //PstStockCardReport.insertExc(stockCardReport);
                //qtyMaterial= rs.getDouble("SUM_QTY") * qtyBase;
                //end of konversi unit yang berhubungan
                qtyMaterial= rs.getDouble("SUM_QTY");
                qtyAll += qtyMaterial;
                srcMaterialRepostingStock.setQty(qtyAll);
 
            }
             if (qtyAll== 0){
                srcMaterialRepostingStock.setQty(0);
            }

        } catch (Exception e) {
            System.out.println("err getDataMaterial : "+e.toString());
        }
  
    }

        /**
     * untuk mencari data qty costing untuk qty opname
     * by Mirahu
     * 20110804
     * @param srcStockCard
     */
    public static void getDataMaterialTime(SrcStockCard srcStockCard) {
        DBResultSet dbrs = null;
        try {
            String sql = "SELECT" +
                    " R." + PstMatCosting.fieldNames[PstMatCosting.FLD_COSTING_DATE] +
                    " ,R." + PstMatCosting.fieldNames[PstMatCosting.FLD_COSTING_CODE] +
                    " ,SUM(RI." + PstMatCostingItem.fieldNames[PstMatCostingItem.FLD_QTY] + ") AS SUM_QTY " +
                    " ,RI." + PstMatCostingItem.fieldNames[PstMatCostingItem.FLD_QTY] +
                    " ,M." + PstMaterial.fieldNames[PstMaterial.FLD_DEFAULT_STOCK_UNIT_ID] +
                    " ,RI." + PstMatCostingItem.fieldNames[PstMatCostingItem.FLD_UNIT_ID] +
                    " FROM " + PstMatCosting.TBL_COSTING+ " AS R " +
                    " INNER JOIN " + PstMatCostingItem.TBL_MAT_COSTING_ITEM+ " AS RI " +
                    " ON R." + PstMatCosting.fieldNames[PstMatCosting.FLD_COSTING_MATERIAL_ID] +
                    " = RI." + PstMatCostingItem.fieldNames[PstMatCostingItem.FLD_COSTING_MATERIAL_ID] +
                    " INNER JOIN " + PstMaterial.TBL_MATERIAL+ " AS M " +
                    " ON RI." + PstMatCostingItem.fieldNames[PstMatCostingItem.FLD_MATERIAL_ID] +
                    " = M." + PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_ID];
            String whereClause = "";
            if (srcStockCard.getMaterialId() != 0) {
                whereClause = "RI." + PstMatCostingItem.fieldNames[PstMatCostingItem.FLD_MATERIAL_ID] + "=" + srcStockCard.getMaterialId();
            }

            if (srcStockCard.getLocationId() != 0) {
                if (whereClause.length() > 0) {
                    whereClause = whereClause + " AND R." + PstMatCosting.fieldNames[PstMatCosting.FLD_LOCATION_ID] + "=" + srcStockCard.getLocationId();
                } else {
                    whereClause = "R." + PstMatCosting.fieldNames[PstMatCosting.FLD_LOCATION_ID] + "=" + srcStockCard.getLocationId();
                }
            }

            if (srcStockCard.getStardDate() != null && srcStockCard.getEndDate() != null) {
                if (whereClause.length() > 0) {
                    whereClause = whereClause + " AND R." + PstMatCosting.fieldNames[PstMatCosting.FLD_COSTING_DATE] + " BETWEEN '" + Formater.formatDate(srcStockCard.getStardDate(), "yyyy-MM-dd hh:mm:ss") + "' AND '" + Formater.formatDate(srcStockCard.getEndDate(), "yyyy-MM-dd hh:mm:ss") + "'";
                } else {
                    whereClause = " R." + PstMatCosting.fieldNames[PstMatCosting.FLD_COSTING_DATE] + " BETWEEN '" + Formater.formatDate(srcStockCard.getStardDate(), "yyyy-MM-dd hh:mm:ss") + "' AND '" + Formater.formatDate(srcStockCard.getEndDate(), "yyyy-MM-dd hh:mm:ss") + "'";
                }
            }else{ // data privous
                if (whereClause.length() > 0) {
                    whereClause = whereClause + " AND R." + PstMatCosting.fieldNames[PstMatCosting.FLD_COSTING_DATE] + " <= '" + Formater.formatDate(srcStockCard.getStardDate(), "yyyy-MM-dd hh:mm:ss") + "'";
                } else {
                    whereClause = " R." + PstMatCosting.fieldNames[PstMatCosting.FLD_COSTING_DATE] + " <= '" + Formater.formatDate(srcStockCard.getStardDate(), "yyyy-MM-dd hh:mm:ss") + "'";
                }
            }

            String strStatus = "";
            if (srcStockCard.getDocStatus() != null && srcStockCard.getDocStatus().size() > 0) {
                for (int n = 0; n < srcStockCard.getDocStatus().size(); n++) {
                    if (strStatus.length() != 0) {
                        strStatus = strStatus + " OR " + "(R." + PstMatCosting.fieldNames[PstMatCosting.FLD_COSTING_STATUS] + " = " + srcStockCard.getDocStatus().get(n) + ")";
                    } else {
                        strStatus = "(R." + PstMatCosting.fieldNames[PstMatCosting.FLD_COSTING_STATUS] + " = " + srcStockCard.getDocStatus().get(n) + ")";
                    }
                }
                strStatus = "(" + strStatus + ")";
            }

            if (strStatus.length() > 0) {
                if (whereClause.length() > 0) {
                    whereClause +=" AND " + strStatus;
                } else {
                    whereClause = strStatus;
                }
            }

            if (whereClause.length() > 0) {
                sql += " WHERE " + whereClause;
            }

            //sql = sql + " GROUP BY R." + PstMatCosting.fieldNames[PstMatCosting.FLD_COSTING_DATE] +
                   // " ,R." + PstMatCosting.fieldNames[PstMatCosting.FLD_COSTING_CODE];
             sql = sql + " GROUP BY R." + PstMatCostingItem.fieldNames[PstMatCostingItem.FLD_UNIT_ID];
            sql = sql + " ORDER BY R." + PstMatCosting.fieldNames[PstMatCosting.FLD_COSTING_CODE];
            //System.out.println("sql dispatch : "+sql);
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while (rs.next()) {
                StockCardReport stockCardReport = new StockCardReport();

                //System.out.println("Date : "+rs.getDate(PstMatCosting.fieldNames[PstMatCosting.FLD_COSTING_DATE]));
                Date date = DBHandler.convertDate(rs.getDate(PstMatCosting.fieldNames[PstMatCosting.FLD_COSTING_DATE]),rs.getTime(PstMatCosting.fieldNames[PstMatCosting.FLD_COSTING_DATE]));
                stockCardReport.setDate(date);
                //System.out.println("Date : "+date);

                stockCardReport.setDocType(I_DocType.MAT_DOC_TYPE_COS);
                stockCardReport.setDocCode(rs.getString(PstMatCosting.fieldNames[PstMatCosting.FLD_COSTING_CODE]));
                //double qtyBase = PstUnit.getQtyPerBaseUnit(rs.getLong(PstMatCostingItem.fieldNames[PstMatCostingItem.FLD_UNIT_ID]),rs.getLong(PstMaterial.fieldNames[PstMaterial.FLD_DEFAULT_STOCK_UNIT_ID]));
                double qtyBase = PstUnit.getUnitFactory(rs.getLong(PstMatCostingItem.fieldNames[PstMatCostingItem.FLD_UNIT_ID]),rs.getLong(PstMaterial.fieldNames[PstMaterial.FLD_DEFAULT_STOCK_UNIT_ID]),0,4);
                stockCardReport.setQty(rs.getDouble("SUM_QTY") * qtyBase);
                stockCardReport.setKeterangan("Costing barang");

                PstStockCardReport.insertExc(stockCardReport);
            }

        } catch (Exception e) {
            System.out.println("err getDataMaterial : "+e.toString());
        }
    }

            /**
     * untuk mencari data qty costing untuk qty opname
     * by Mirahu
     * 201108010
     * @param srcStockCard
     */
    public static void getQtyStockMaterial(SrcStockCard srcStockCard) {
        DBResultSet dbrs = null;
        try {
            String sql = "SELECT" +
                    " SUM(RI." + PstMatCostingItem.fieldNames[PstMatCostingItem.FLD_QTY] + ") AS SUM_QTY " +
                    " ,M." + PstMaterial.fieldNames[PstMaterial.FLD_DEFAULT_STOCK_UNIT_ID] +
                    " ,RI." + PstMatCostingItem.fieldNames[PstMatCostingItem.FLD_UNIT_ID] +
                    " FROM " + PstMatCosting.TBL_COSTING+ " AS R " +
                    " INNER JOIN " + PstMatCostingItem.TBL_MAT_COSTING_ITEM+ " AS RI " +
                    " ON R." + PstMatCosting.fieldNames[PstMatCosting.FLD_COSTING_MATERIAL_ID] +
                    " = RI." + PstMatCostingItem.fieldNames[PstMatCostingItem.FLD_COSTING_MATERIAL_ID] +
                    " INNER JOIN " + PstMaterial.TBL_MATERIAL+ " AS M " +
                    " ON RI." + PstMatCostingItem.fieldNames[PstMatCostingItem.FLD_MATERIAL_ID] +
                    " = M." + PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_ID];
            String whereClause = "";
            if (srcStockCard.getMaterialId() != 0) {
                whereClause = "RI." + PstMatCostingItem.fieldNames[PstMatCostingItem.FLD_MATERIAL_ID] + "=" + srcStockCard.getMaterialId();
            }

            if (srcStockCard.getLocationId() != 0) {
                if (whereClause.length() > 0) {
                    whereClause = whereClause + " AND R." + PstMatCosting.fieldNames[PstMatCosting.FLD_LOCATION_ID] + "=" + srcStockCard.getLocationId();
                } else {
                    whereClause = "R." + PstMatCosting.fieldNames[PstMatCosting.FLD_LOCATION_ID] + "=" + srcStockCard.getLocationId();
                }
            }

            if (srcStockCard.getStardDate() != null && srcStockCard.getEndDate() != null) {
                if (whereClause.length() > 0) {
                    whereClause = whereClause + " AND R." + PstMatCosting.fieldNames[PstMatCosting.FLD_COSTING_DATE] + " BETWEEN '" + Formater.formatDate(srcStockCard.getStardDate(), "yyyy-MM-dd HH:mm:ss") + "' AND '" + Formater.formatDate(srcStockCard.getEndDate(), "yyyy-MM-dd HH:mm:ss") + "'";
                } else {
                    whereClause = " R." + PstMatCosting.fieldNames[PstMatCosting.FLD_COSTING_DATE] + " BETWEEN '" + Formater.formatDate(srcStockCard.getStardDate(), "yyyy-MM-dd HH:mm:ss") + "' AND '" + Formater.formatDate(srcStockCard.getEndDate(), "yyyy-MM-dd HH:mm:ss") + "'";
                }
            }else{ // data privous
                if (whereClause.length() > 0) {
                    whereClause = whereClause + " AND R." + PstMatCosting.fieldNames[PstMatCosting.FLD_COSTING_DATE] + " <= '" + Formater.formatDate(srcStockCard.getStardDate(), "yyyy-MM-dd HH:mm:ss") + "'";
                } else {
                    whereClause = " R." + PstMatCosting.fieldNames[PstMatCosting.FLD_COSTING_DATE] + " <= '" + Formater.formatDate(srcStockCard.getStardDate(), "yyyy-MM-dd HH:mm:ss") + "'";
                }
            }

            String strStatus = "";
            if (srcStockCard.getDocStatus() != null && srcStockCard.getDocStatus().size() > 0) {
                for (int n = 0; n < srcStockCard.getDocStatus().size(); n++) {
                    if (strStatus.length() != 0) {
                        strStatus = strStatus + " OR " + "(R." + PstMatCosting.fieldNames[PstMatCosting.FLD_COSTING_STATUS] + " = " + srcStockCard.getDocStatus().get(n) + ")";
                    } else {
                        strStatus = "(R." + PstMatCosting.fieldNames[PstMatCosting.FLD_COSTING_STATUS] + " = " + srcStockCard.getDocStatus().get(n) + ")";
                    }
                }
                strStatus = "(" + strStatus + ")";
            }

            if (strStatus.length() > 0) {
                if (whereClause.length() > 0) {
                    whereClause +=" AND " + strStatus;
                } else {
                    whereClause = strStatus;
                }
            }

            if (whereClause.length() > 0) {
                sql += " WHERE " + whereClause;
            }

            sql = sql + " GROUP BY R." + PstMatCosting.fieldNames[PstMatCosting.FLD_COSTING_DATE] +
                    " ,R." + PstMatCosting.fieldNames[PstMatCosting.FLD_COSTING_CODE]+
                    ",RI." + PstMatCostingItem.fieldNames[PstMatCostingItem.FLD_UNIT_ID];
            sql = sql + " ORDER BY R." + PstMatCosting.fieldNames[PstMatCosting.FLD_COSTING_CODE];
            //System.out.println("sql dispatch : "+sql);
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            double qtyMaterial = 0;
            double qtyAll = 0;
            while (rs.next()) {
                //StockCardReport stockCardReport = new StockCardReport();

                //System.out.println("Date : "+rs.getDate(PstMatCosting.fieldNames[PstMatCosting.FLD_COSTING_DATE]));
            
                //stockCardReport.setDocType(I_DocType.MAT_DOC_TYPE_COS);
                //double qtyBase = PstUnit.getQtyPerBaseUnit(rs.getLong(PstMatCostingItem.fieldNames[PstMatCostingItem.FLD_UNIT_ID]),rs.getLong(PstMaterial.fieldNames[PstMaterial.FLD_DEFAULT_STOCK_UNIT_ID]));
                //disable konversi unit yang berhubungan
                //double qtyBase = PstUnit.getUnitFactory(rs.getLong(PstMatCostingItem.fieldNames[PstMatCostingItem.FLD_UNIT_ID]),rs.getLong(PstMaterial.fieldNames[PstMaterial.FLD_DEFAULT_STOCK_UNIT_ID]),0,4);
                //PstStockCardReport.insertExc(stockCardReport);
                //qtyMaterial= rs.getDouble("SUM_QTY") * qtyBase;
                //end of konversi unit yang berhubungan
                qtyMaterial= rs.getDouble("SUM_QTY");
                qtyAll += qtyMaterial;
                srcStockCard.setQty(qtyAll);
 
            }
             if (qtyAll== 0){
                srcStockCard.setQty(0);
            }

        } catch (Exception e) {
            System.out.println("err getDataMaterial : "+e.toString());
        }
    }
}
