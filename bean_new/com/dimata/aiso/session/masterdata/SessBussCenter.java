/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.dimata.aiso.session.masterdata;

import com.dimata.aiso.db.DBHandler;
import com.dimata.aiso.db.DBResultSet;
import com.dimata.aiso.entity.masterdata.PstBussinessCenter;
import java.sql.ResultSet;

/**
 *
 * @author dwi
 */
public class SessBussCenter {

    public static final String SESS_BUSS_CENTER = "SESS_BUSS_CENTER";
            
    public static synchronized int getCount()
    {
        DBResultSet dbrs = null;
        int record = 0;
        try
        {
            String sql = getStringCountQuery();
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
    
    
    public static synchronized String getStringCountQuery()
    {
        String countSql = "";
        try
        {
            countSql = " SELECT COUNT("+PstBussinessCenter.fieldNames[PstBussinessCenter.FLD_BUSS_CENTER_ID]+") AS COUNT "+
                        " FROM "+PstBussinessCenter.TBL_BUSSINESS_CENTER;
        }
        catch(Exception e)
        {}
        return countSql;
    }

}
