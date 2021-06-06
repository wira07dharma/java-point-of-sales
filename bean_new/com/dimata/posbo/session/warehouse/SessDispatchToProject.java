/**
 * User: wardana
 * Date: Mar 24, 2004
 * Time: 8:15:24 AM
 * Version: 1.0
 */
package com.dimata.posbo.session.warehouse;

import com.dimata.posbo.entity.warehouse.DispatchToProject;
import com.dimata.posbo.entity.warehouse.PstDispatchToProject;
import com.dimata.posbo.entity.search.SrcDispatchToProject;
import com.dimata.posbo.db.DBResultSet;
import com.dimata.posbo.db.DBHandler;
import com.dimata.util.Formater;
import com.dimata.common.entity.location.PstLocation;
import com.dimata.common.entity.location.Location;

import java.util.Date;
import java.util.Vector;
import java.sql.ResultSet;

public class SessDispatchToProject {

    public static final String SESS_SRC_DISP_TO_PROJECT = "SESS_SRC_DISP_TO_PROJECT";

    public static final String[] stOrderBy =
    {
        PstDispatchToProject.stFieldNames[PstDispatchToProject.FLD_DISPATCH_CODE],
        PstDispatchToProject.stFieldNames[PstDispatchToProject.FLD_LOCATION_ID],
        PstDispatchToProject.stFieldNames[PstDispatchToProject.FLD_DISPATCH_TO],
        PstDispatchToProject.stFieldNames[PstDispatchToProject.FLD_DISPATCH_DATE],
        PstDispatchToProject.stFieldNames[PstDispatchToProject.FLD_DISPATCH_STATUS]
    };

    public static int getMaxDispatchCounter(Date dtDate, DispatchToProject objDspToProject) {
        int iCounter = 0;
        DBResultSet dbrs = null;
        try {

            Date dtStartDate = (Date) dtDate.clone();
            dtStartDate.setDate(1);

            Date dtEndDate = (Date) dtDate.clone();
            dtEndDate.setMonth(dtEndDate.getMonth() + 1);
            dtEndDate.setDate(1);
            dtEndDate.setDate(dtEndDate.getDate() - 1);

            String sql = "SELECT MAX(" + PstDispatchToProject.stFieldNames[PstDispatchToProject.FLD_DISPATCH_CODE_COUNTER] + ") AS MAXIMUM " +
                    " FROM " + PstDispatchToProject.TBL_DISPATCH_TO_PROJECT +
                    " WHERE (" + PstDispatchToProject.stFieldNames[PstDispatchToProject.FLD_DISPATCH_DATE] +
                    " BETWEEN '" + Formater.formatDate(dtStartDate, "yyyy-MM-dd") +
                    " ' AND '" + Formater.formatDate(dtEndDate, "yyyy-MM-dd") +
                    " ') AND " + PstDispatchToProject.stFieldNames[PstDispatchToProject.FLD_LOCATION_ID] + " = " + objDspToProject.getlLocationId();

            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();

            while (rs.next()) {
                iCounter = rs.getInt("MAXIMUM");
            }

            rs.close();
        } catch (Exception e) {
            DBResultSet.close(dbrs);
            System.out.println("Exception getMaxCounter : " + e.toString());
        } finally {
            DBResultSet.close(dbrs);
        }
        return iCounter;
    }

    public static int getCountDispatchToProject(SrcDispatchToProject objScrDspToProject) {

        int result = 0;
        String stFromLocation = "";
        String stToLocation = "";
        String stDate = "";
        String stDfCode = "";
        String stStatus = "";
        String stWhere = "";

        String sql = "SELECT COUNT(DF." + PstDispatchToProject.stFieldNames[PstDispatchToProject.FLD_DISPATCH_MATERIAL_ID] +
                " ) AS CNT FROM " + PstDispatchToProject.TBL_DISPATCH_TO_PROJECT + " DF" +
                " INNER JOIN " + PstLocation.TBL_P2_LOCATION + " LOC" +
                " ON DF." + PstDispatchToProject.stFieldNames[PstDispatchToProject.FLD_LOCATION_ID] +
                " = LOC." + PstLocation.fieldNames[PstLocation.FLD_LOCATION_ID];

        if (objScrDspToProject != null) {
            if (objScrDspToProject.getlDispatchFrom() != 0) {
                stFromLocation = "(DF." + PstDispatchToProject.stFieldNames[PstDispatchToProject.FLD_LOCATION_ID] + "=" + objScrDspToProject.getlDispatchFrom() + ")";
            }
            if (objScrDspToProject.getlDispatchTo() != 0) {
                stToLocation = "(DF." + PstDispatchToProject.stFieldNames[PstDispatchToProject.FLD_DISPATCH_TO] + "=" + objScrDspToProject.getlDispatchTo() + ")";
            }
            if (!objScrDspToProject.isbIgnoreDate()) {
                stDate = "(DF." + PstDispatchToProject.stFieldNames[PstDispatchToProject.FLD_DISPATCH_DATE] + " BETWEEN '" + Formater.formatDate(objScrDspToProject.getDtDispatchDateFrom(), "yyyy-MM-dd") + "' AND '" +
                        Formater.formatDate(objScrDspToProject.getDtDispatchDateTo(), "yyyy-MM-dd") + "')";
            }
            if (objScrDspToProject.getStDispatchCode().length() > 0) {
                stDfCode = "(DF." + PstDispatchToProject.stFieldNames[PstDispatchToProject.FLD_DISPATCH_CODE] + " LIKE '" + objScrDspToProject.getStDispatchCode() + "%')";
            }
            if (objScrDspToProject.getiStatus() >= 0) {
                stStatus = "(DF." + PstDispatchToProject.stFieldNames[PstDispatchToProject.FLD_DISPATCH_STATUS] + " = " + (objScrDspToProject.getiStatus()) + ")";
            }

            if (stFromLocation.length() > 0) {
                if (stWhere.length() > 0) {
                    stWhere = stWhere + " AND " + stFromLocation;
                } else {
                    stWhere = stFromLocation;
                }
            }

            if (stToLocation.length() > 0) {
                if (stWhere.length() > 0) {
                    stWhere = stWhere + " AND " + stToLocation;
                } else {
                    stWhere = stToLocation;
                }
            }

            if (stDate.length() > 0) {
                if (stWhere.length() > 0) {
                    stWhere = stWhere + " AND " + stDate;
                } else {
                    stWhere = stDate;
                }
            }

            if (stDfCode.length() > 0) {
                if (stWhere.length() > 0) {
                    stWhere = stWhere + " AND " + stDfCode;
                } else {
                    stWhere = stDfCode;
                }
            }

            if (stStatus.length() > 0) {
                if (stWhere.length() > 0) {
                    stWhere = stWhere + " AND " + stStatus;
                } else {
                    stWhere = stStatus;
                }
            }

            if (stWhere.length() > 0) {
                stWhere = " WHERE	" + stWhere;
            }
            sql += stWhere;
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
            e.printStackTrace();
        } finally {
            DBResultSet.close(dbrs);
        }
        return result;
    }

    public static Vector listDispatchToProject(SrcDispatchToProject srcMatDispatch, int start, int limit)
    {
        Vector result = new Vector(1,1);
        String sql = "SELECT DF." + PstDispatchToProject.stFieldNames[PstDispatchToProject.FLD_DISPATCH_MATERIAL_ID]+
            " , DF." +  PstDispatchToProject.stFieldNames[PstDispatchToProject.FLD_DISPATCH_CODE]+
            " , DF." + PstDispatchToProject.stFieldNames[PstDispatchToProject.FLD_DISPATCH_DATE]+
            " , LOC." + PstLocation.fieldNames[PstLocation.FLD_NAME]+
            " , DF." + PstDispatchToProject.stFieldNames[PstDispatchToProject.FLD_DISPATCH_TO]+
            " , DF." + PstDispatchToProject.stFieldNames[PstDispatchToProject.FLD_DISPATCH_STATUS]+
            " , DF." + PstDispatchToProject.stFieldNames[PstDispatchToProject.FLD_REMARK]+
            " FROM " + PstDispatchToProject.TBL_DISPATCH_TO_PROJECT + " DF" +
            " INNER JOIN " + PstLocation.TBL_P2_LOCATION + " LOC" +
            " ON DF." + PstDispatchToProject.stFieldNames[PstDispatchToProject.FLD_LOCATION_ID] +
            " = LOC." + PstLocation.fieldNames[PstLocation.FLD_LOCATION_ID];


        if(srcMatDispatch != null)
        {
            String fromLocation = "";
            if(srcMatDispatch.getlDispatchFrom()!=0)
            {
                fromLocation = "(DF."+PstDispatchToProject.stFieldNames[PstDispatchToProject.FLD_LOCATION_ID]+"="+srcMatDispatch.getlDispatchFrom()+")";
            }

            String toLocation = "";
            if(srcMatDispatch.getlDispatchTo()!=0)
            {
                toLocation = "(DF."+PstDispatchToProject.stFieldNames[PstDispatchToProject.FLD_DISPATCH_TO]+"="+srcMatDispatch.getlDispatchTo()+")";
            }

            String date = "";
            if(!srcMatDispatch.isbIgnoreDate())
            {
                date = "(DF."+PstDispatchToProject.stFieldNames[PstDispatchToProject.FLD_DISPATCH_DATE]+" BETWEEN '"+Formater.formatDate(srcMatDispatch.getDtDispatchDateFrom(), "yyyy-MM-dd")+"' AND '"+
                Formater.formatDate(srcMatDispatch.getDtDispatchDateTo(), "yyyy-MM-dd")+"')";
            }

            String dfCode = "";
            if(srcMatDispatch.getStDispatchCode().length()>0)
            {
                dfCode = "(DF."+PstDispatchToProject.stFieldNames[PstDispatchToProject.FLD_DISPATCH_CODE]+" LIKE '"+srcMatDispatch.getStDispatchCode()+"%')";
            }

            String status = "";
            if(srcMatDispatch.getiStatus()>=0)
            {
                status =  "(DF."+PstDispatchToProject.stFieldNames[PstDispatchToProject.FLD_DISPATCH_STATUS]+" = "+(srcMatDispatch.getiStatus())+")";
            }

            String where = "";
            if( fromLocation.length()>0)
            {
                if( where.length()>0)
                {
                    where = where + " AND " + fromLocation;
                }
                else
                {
                    where = fromLocation;
                }
            }

            if( toLocation.length()>0)
            {
                if( where.length()>0)
                {
                    where = where + " AND " + toLocation;
                }
                else
                {
                    where = toLocation;
                }
            }

            if( date.length()>0)
            {
                if( where.length()>0)
                {
                    where = where + " AND " + date;
                }
                else
                {
                    where = date;
                }
            }

            if( dfCode.length()>0)
            {
                if( where.length()>0)
                {
                    where = where + " AND " + dfCode;
                }
                else
                {
                    where = dfCode;
                }
            }

            if(status.length()>0)
            {
                if( where.length()>0)
                {
                    where = where + " AND " + status;
                }
                else
                {
                    where = status;
                }
            }

            if(where.length()>0)
            {
                where = " WHERE	" + where;
            }

            sql += where ;
            sql += " ORDER BY DF." + stOrderBy[srcMatDispatch.getiSortBy()];
        }

        switch (DBHandler.DBSVR_TYPE)
        {
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
        try
        {
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while(rs.next())
            {
                Vector temp = new Vector();
                DispatchToProject objDspToProject = new DispatchToProject();
                Location loc1 = new Location();

                objDspToProject.setOID(rs.getLong(1));
                objDspToProject.setStDispatchCode(rs.getString(2));
                objDspToProject.setDtDispatchDate(rs.getDate(3));
                loc1.setName(rs.getString(4));
                objDspToProject.setlDispatchTo(rs.getLong(5));
                objDspToProject.setiDispatchStatus(rs.getInt(6));
                objDspToProject.setStRemark(rs.getString(7));
                temp.add(objDspToProject);

                temp.add(loc1);

                result.add(temp);
            }
            rs.close();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            DBResultSet.close(dbrs);
        }
        return result;
    }

}
