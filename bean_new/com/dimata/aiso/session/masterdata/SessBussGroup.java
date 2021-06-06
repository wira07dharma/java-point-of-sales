/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.dimata.aiso.session.masterdata;

import com.dimata.aiso.db.DBHandler;
import com.dimata.aiso.db.DBResultSet;
import com.dimata.aiso.entity.masterdata.PstBussGroup;
import com.dimata.aiso.entity.search.SrcBussinessGroup;
import java.sql.ResultSet;
import java.util.Vector;

/**
 *
 * @author dwi
 */
public class SessBussGroup {

    public static final String SESS_BUSS_GROUP = "SESS_BUSS_GROUP";
    
    public static synchronized int getCount(SrcBussinessGroup srcBussinessGroup)
    {
        DBResultSet dbrs = null;
        int record = 0;
        try
        {
            String sql = getStringCountQuery();
            String like = getStringLike(DBHandler.DBSVR_TYPE);
            String where = getStringWhere(true);
            String whClause = getStringWhClause(srcBussinessGroup, like,where);
            sql += whClause;
            System.out.println("SQL SessCompany.getStringCountQuery() :::: "+sql);
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while(rs.next())
            {
                record = rs.getInt("COUNT");
            }
            rs.close();
        }
        catch(Exception e)
        {}
        return record;
    }
    
    public static synchronized Vector getDataCompany(SrcBussinessGroup srcBussinessGroup,int start, int recordToGet)
    {
        Vector vResult = new Vector();
        try
        {
            String like = getStringLike(DBHandler.DBSVR_TYPE);
            String where = getStringWhere(false);
            String whClause = getStringWhClause(srcBussinessGroup, like,where);
            String orderBy = getStringOrderBy(srcBussinessGroup);
            
            vResult = PstBussGroup.list(start, recordToGet, whClause, orderBy);
            
        }
        catch(Exception e)
        {}
        return vResult;
    }
    
    public static synchronized String getStringOrderBy(SrcBussinessGroup srcBussinessGroup)
    {
        String orderBy = "";
        try
        {
            switch(srcBussinessGroup.getOrderBy())
            {
                    case 0:
                    orderBy = PstBussGroup.fieldNames[PstBussGroup.FLD_BUSS_GROUP_CODE];
                    break;

                    case 1:
                    orderBy = PstBussGroup.fieldNames[PstBussGroup.FLD_BUSS_GROUP_NAME];
                    break;

                    case 2:
                    orderBy = PstBussGroup.fieldNames[PstBussGroup.FLD_BUSS_GROUP_ADDRESS];
                    break;

                    case 3:
                    orderBy = PstBussGroup.fieldNames[PstBussGroup.FLD_BUSS_GROUP_CITY];
                    break;
                    
                    case 4:
                    orderBy = PstBussGroup.fieldNames[PstBussGroup.FLD_BUSS_GROUP_PHONE];
                    break;
                    
                    case 5:
                    orderBy = PstBussGroup.fieldNames[PstBussGroup.FLD_BUSS_GROUP_FAX];
                    break;

                    default:
                        orderBy = PstBussGroup.fieldNames[PstBussGroup.FLD_BUSS_GROUP_CODE];
            }
        }
        catch(Exception e)
        {}
        return orderBy;
    }
    
   
    public static synchronized String getStringLike(int dbType)
    {
        String like = " LIKE ";
        try
        {
            if(dbType == DBHandler.DBSVR_POSTGRESQL){ 
                like = " ILIKE ";
            }
        }
        catch(Exception e)
        {}
        return like;
    }
    
    public static synchronized String getStringWhere(boolean isGetCountQuery)
    {
        String where = "";
        try
        {
            if(isGetCountQuery)
            {
                where = " WHERE ";
            }
        }
        catch(Exception e){}
        return where;
    }
    
    public static synchronized String getStringWhClause(SrcBussinessGroup srcBussinessGroup, String like, String where)
    {
        String whClause = "";
        try
        {
            if(srcBussinessGroup.getCode() != null && srcBussinessGroup.getCode().length() > 0)
            {
               whClause = where + PstBussGroup.fieldNames[PstBussGroup.FLD_BUSS_GROUP_CODE]+
                          like +"'%"+srcBussinessGroup.getCode()+"%'";
            }
            
            if(srcBussinessGroup.getName() != null && srcBussinessGroup.getName().length() > 0)
            {
                if(whClause != null && whClause.length() > 0)
                {
                    whClause += " AND "+PstBussGroup.fieldNames[PstBussGroup.FLD_BUSS_GROUP_NAME]+
                                like +"'%"+srcBussinessGroup.getName()+"%'";
                }
                else
                {
                    whClause += where + PstBussGroup.fieldNames[PstBussGroup.FLD_BUSS_GROUP_NAME]+
                                like +"'%"+srcBussinessGroup.getName()+"%'";
                }
            }
            
            if(srcBussinessGroup.getAddress() != null && srcBussinessGroup.getAddress().length() > 0)
            {
                if(whClause != null && whClause.length() > 0)
                {
                    whClause += " AND "+PstBussGroup.fieldNames[PstBussGroup.FLD_BUSS_GROUP_ADDRESS]+
                                like +"'%"+srcBussinessGroup.getAddress()+"%'";
                }
                else
                {
                    whClause += where + PstBussGroup.fieldNames[PstBussGroup.FLD_BUSS_GROUP_ADDRESS]+
                                like +"'%"+srcBussinessGroup.getAddress()+"%'";
                }
            }
            
            if(srcBussinessGroup.getCity() != null && srcBussinessGroup.getCity().length() > 0)
            {
                if(whClause != null && whClause.length() > 0)
                {
                    whClause += " AND "+PstBussGroup.fieldNames[PstBussGroup.FLD_BUSS_GROUP_CITY]+
                                like +"'%"+srcBussinessGroup.getCity()+"%'";
                }
                else
                {
                    whClause += where + PstBussGroup.fieldNames[PstBussGroup.FLD_BUSS_GROUP_CITY]+
                                like +"'%"+srcBussinessGroup.getCity()+"%'";
                }
            }
            
            if(srcBussinessGroup.getPhone() != null && srcBussinessGroup.getPhone().length() > 0)
            {
                if(whClause != null && whClause.length() > 0)
                {
                    whClause += " AND "+PstBussGroup.fieldNames[PstBussGroup.FLD_BUSS_GROUP_PHONE]+
                                like +"'%"+srcBussinessGroup.getPhone()+"%'";
                }
                else
                {
                    whClause += where + PstBussGroup.fieldNames[PstBussGroup.FLD_BUSS_GROUP_PHONE]+
                                like +"'%"+srcBussinessGroup.getPhone()+"%'";
                }
            }
            
            if(srcBussinessGroup.getFax() != null && srcBussinessGroup.getFax().length() > 0)
            {
                if(whClause != null && whClause.length() > 0)
                {
                    whClause += " AND "+PstBussGroup.fieldNames[PstBussGroup.FLD_BUSS_GROUP_FAX]+
                                like +"'%"+srcBussinessGroup.getFax()+"%'";
                }
                else
                {
                    whClause += where + PstBussGroup.fieldNames[PstBussGroup.FLD_BUSS_GROUP_FAX]+
                                like +"'%"+srcBussinessGroup.getFax()+"%'";
                }
            }
            
        }
        catch(Exception e)
        {}
        return whClause;
    }
    
    
    public static synchronized String getStringCountQuery()
    {
        String countSql = "";
        try
        {
            countSql = " SELECT COUNT("+PstBussGroup.fieldNames[PstBussGroup.FLD_BUSS_GROUP_ID]+") AS COUNT "+
                        " FROM "+PstBussGroup.TBL_BUSS_GROUP;
        }
        catch(Exception e)
        {}
        return countSql;
    }
}
