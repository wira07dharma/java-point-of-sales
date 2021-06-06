/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.dimata.aiso.session.masterdata;

import com.dimata.aiso.db.DBHandler;
import com.dimata.aiso.db.DBResultSet;
import com.dimata.aiso.entity.masterdata.PstCompany;
import com.dimata.aiso.entity.search.SrcCompany;
import java.sql.ResultSet;
import java.util.Vector;

/**
 *
 * @author dwi
 */
public class SessCompany {

    public static final String SESS_COMPANY = "SESS_COMPANY";
    
    public static synchronized int getCount(SrcCompany srcCompany)
    {
        DBResultSet dbrs = null;
        int record = 0;
        try
        {
            String sql = getStringCountQuery();
            String like = getStringLike(DBHandler.DBSVR_TYPE);
            String where = getStringWhere(true);
            String whClause = getStringWhClause(srcCompany, like,where);
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
    
    public static synchronized Vector getDataCompany(SrcCompany srcCompany,int start, int recordToGet)
    {
        Vector vResult = new Vector();
        try
        {
            String like = getStringLike(DBHandler.DBSVR_TYPE);
            String where = getStringWhere(false);
            String whClause = getStringWhClause(srcCompany, like,where);
            String orderBy = getStringOrderBy(srcCompany);
            
            vResult = PstCompany.list(start, recordToGet, whClause, orderBy);
            
        }
        catch(Exception e)
        {}
        return vResult;
    }
    
    public static synchronized String getStringOrderBy(SrcCompany srcCompany)
    {
        String orderBy = "";
        try
        {
            switch(srcCompany.getOrderBy())
            {
                    case 0:
                    orderBy = PstCompany.fieldNames[PstCompany.FLD_COMPANY_CODE];
                    break;

                    case 1:
                    orderBy = PstCompany.fieldNames[PstCompany.FLD_COMPANY_NAME];
                    break;

                    case 2:
                    orderBy = PstCompany.fieldNames[PstCompany.FLD_BUSS_ADDRESS];
                    break;

                    case 3:
                    orderBy = PstCompany.fieldNames[PstCompany.FLD_TOWN];
                    break;
                    
                    case 4:
                    orderBy = PstCompany.fieldNames[PstCompany.FLD_PROVINCE];
                    break;
                    
                    case 5:
                    orderBy = PstCompany.fieldNames[PstCompany.FLD_COUNTRY];
                    break;

                    default:
                        orderBy = PstCompany.fieldNames[PstCompany.FLD_COMPANY_CODE];
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
    
    public static synchronized String getStringWhClause(SrcCompany srcCompany, String like, String where)
    {
        String whClause = "";
        try
        {
            if(srcCompany.getCode() != null && srcCompany.getCode().length() > 0)
            {
               whClause = where + PstCompany.fieldNames[PstCompany.FLD_COMPANY_CODE]+
                          like +"'%"+srcCompany.getCode()+"%'";
            }
            
            if(srcCompany.getName() != null && srcCompany.getName().length() > 0)
            {
                if(whClause != null && whClause.length() > 0)
                {
                    whClause += " AND "+PstCompany.fieldNames[PstCompany.FLD_COMPANY_NAME]+
                                like +"'%"+srcCompany.getName()+"%'";
                }
                else
                {
                    whClause += where + PstCompany.fieldNames[PstCompany.FLD_COMPANY_NAME]+
                                like +"'%"+srcCompany.getName()+"%'";
                }
            }
            
            if(srcCompany.getAddress() != null && srcCompany.getAddress().length() > 0)
            {
                if(whClause != null && whClause.length() > 0)
                {
                    whClause += " AND "+PstCompany.fieldNames[PstCompany.FLD_BUSS_ADDRESS]+
                                like +"'%"+srcCompany.getAddress()+"%'";
                }
                else
                {
                    whClause += where + PstCompany.fieldNames[PstCompany.FLD_BUSS_ADDRESS]+
                                like +"'%"+srcCompany.getAddress()+"%'";
                }
            }
            
            if(srcCompany.getTown() != null && srcCompany.getTown().length() > 0)
            {
                if(whClause != null && whClause.length() > 0)
                {
                    whClause += " AND "+PstCompany.fieldNames[PstCompany.FLD_TOWN]+
                                like +"'%"+srcCompany.getTown()+"%'";
                }
                else
                {
                    whClause += where + PstCompany.fieldNames[PstCompany.FLD_TOWN]+
                                like +"'%"+srcCompany.getTown()+"%'";
                }
            }
            
            if(srcCompany.getProvince() != null && srcCompany.getProvince().length() > 0)
            {
                if(whClause != null && whClause.length() > 0)
                {
                    whClause += " AND "+PstCompany.fieldNames[PstCompany.FLD_PROVINCE]+
                                like +"'%"+srcCompany.getProvince()+"%'";
                }
                else
                {
                    whClause += where + PstCompany.fieldNames[PstCompany.FLD_PROVINCE]+
                                like +"'%"+srcCompany.getProvince()+"%'";
                }
            }
            
            if(srcCompany.getCountry() != null && srcCompany.getCountry().length() > 0)
            {
                if(whClause != null && whClause.length() > 0)
                {
                    whClause += " AND "+PstCompany.fieldNames[PstCompany.FLD_COUNTRY]+
                                like +"'%"+srcCompany.getCountry()+"%'";
                }
                else
                {
                    whClause += where + PstCompany.fieldNames[PstCompany.FLD_COUNTRY]+
                                like +"'%"+srcCompany.getCountry()+"%'";
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
            countSql = " SELECT COUNT("+PstCompany.fieldNames[PstCompany.FLD_COMPANY_ID]+") AS COUNT "+
                        " FROM "+PstCompany.TBL_COMPANY;
        }
        catch(Exception e)
        {}
        return countSql;
    }
}
