/*
 * SessForwarderInfo.java
 *
 * Created on June 4, 2007, 4:14 PM
 */

package com.dimata.posbo.session.warehouse;

/* java package */
import java.util.Date;
import java.util.*;
import java.sql.*;
import com.dimata.posbo.db.DBHandler;
import com.dimata.posbo.db.DBResultSet;

/* project package */
import com.dimata.posbo.entity.warehouse.ForwarderInfo;
import com.dimata.posbo.entity.warehouse.PstForwarderInfo;
import com.dimata.common.entity.contact.ContactList;
import com.dimata.common.entity.contact.PstContactList;
import com.dimata.posbo.entity.warehouse.PstMatReceiveItem;
import com.dimata.common.entity.location.PstLocation;
import com.dimata.common.entity.location.Location;

/**
 *
 * @author  gwawan
 */
public class SessForwarderInfo {
    
    /** Creates a new instance of SessForwarderInfo */
    public SessForwarderInfo() {
    }
    
    public static Vector getObjForwarderInfo(long oidReceive) {
        DBResultSet dbrs = null;
        Vector result = new Vector(1,1);
        try {
            String sql = "";
            sql += "select fi."+PstForwarderInfo.fieldNames[PstForwarderInfo.FLD_FORWARDER_ID];
            sql += ", fi."+PstForwarderInfo.fieldNames[PstForwarderInfo.FLD_CONTACT_ID];
            sql += ", fi."+PstForwarderInfo.fieldNames[PstForwarderInfo.FLD_DOC_NUMBER];
            sql += ", fi."+PstForwarderInfo.fieldNames[PstForwarderInfo.FLD_DOC_DATE];
            sql += ", fi."+PstForwarderInfo.fieldNames[PstForwarderInfo.FLD_TOTAL_COST];
            sql += ", fi."+PstForwarderInfo.fieldNames[PstForwarderInfo.FLD_NOTES];
            sql += ", cl."+PstContactList.fieldNames[PstContactList.FLD_PERSON_NAME];
            sql += ", cl."+PstContactList.fieldNames[PstContactList.FLD_PERSON_LASTNAME];
            sql += " from "+PstForwarderInfo.TBL_FORWARDER_INFO+" fi";
            sql += " inner join "+PstContactList.TBL_CONTACT_LIST+" cl";
            sql += " on fi."+PstForwarderInfo.fieldNames[PstForwarderInfo.FLD_CONTACT_ID];
            sql += " = cl."+PstContactList.fieldNames[PstContactList.FLD_CONTACT_ID];
            
            if (oidReceive != 0)
                sql += " where "+PstForwarderInfo.fieldNames[PstForwarderInfo.FLD_RECEIVE_ID]+"="+oidReceive;
            
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while (rs.next()) {
                Vector temp = new Vector(1,1);
                ForwarderInfo forwarderInfo = new ForwarderInfo();
                ContactList contactList = new ContactList();
                
                forwarderInfo.setOID(rs.getLong("fi."+PstForwarderInfo.fieldNames[PstForwarderInfo.FLD_FORWARDER_ID]));
                forwarderInfo.setContactId(rs.getLong("fi."+PstForwarderInfo.fieldNames[PstForwarderInfo.FLD_CONTACT_ID]));
                forwarderInfo.setDocNumber(rs.getString("fi."+PstForwarderInfo.fieldNames[PstForwarderInfo.FLD_DOC_NUMBER]));
                forwarderInfo.setDocDate(rs.getDate("fi."+PstForwarderInfo.fieldNames[PstForwarderInfo.FLD_DOC_DATE]));
                forwarderInfo.setTotalCost(rs.getDouble("fi."+PstForwarderInfo.fieldNames[PstForwarderInfo.FLD_TOTAL_COST]));
                forwarderInfo.setNotes(rs.getString("fi."+PstForwarderInfo.fieldNames[PstForwarderInfo.FLD_NOTES]));
                temp.add(forwarderInfo);
                
                contactList.setPersonName(rs.getString("cl."+PstContactList.fieldNames[PstContactList.FLD_PERSON_NAME]));
                contactList.setPersonLastname(rs.getString("cl."+PstContactList.fieldNames[PstContactList.FLD_PERSON_LASTNAME]));
                temp.add(contactList);
                
                result.add(temp);
            }
            rs.close();
            return result;
        }
        catch (Exception e) {
            System.out.println(e);
        }
        finally {
            DBResultSet.close(dbrs);
        }
        return new Vector();
    }
    
    public static int getMaxCounter(ForwarderInfo forwarderInfo, Date date, long oid, int counter) {
        int max = 0;
        DBResultSet dbrs = null;
        try {
            String sql = "";
            sql += " select max("+PstForwarderInfo.fieldNames[PstForwarderInfo.FLD_COUNTER]+") AS counter";
            sql += " from "+PstForwarderInfo.TBL_FORWARDER_INFO;
            sql += " where year("+PstForwarderInfo.fieldNames[PstForwarderInfo.FLD_DOC_DATE]+") = "+(forwarderInfo.getDocDate().getYear() + 1900);
            sql += " and month("+PstForwarderInfo.fieldNames[PstForwarderInfo.FLD_DOC_DATE]+") = "+(forwarderInfo.getDocDate().getMonth() + 1);
            sql += " and "+PstForwarderInfo.fieldNames[PstForwarderInfo.FLD_FORWARDER_ID]+" <> "+oid;
            
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while (rs.next()) {
                max = rs.getInt("counter");
            }
            
            if (oid == 0) {
                max = max + 1;
            }
            else {
                if (forwarderInfo.getDocDate() != date)
                    max = max + 1;
                else
                    max = counter;
            }
            
        } catch (Exception e) {
            System.out.println("Exc in getMaxCounter() >>> "+e.toString()+" <<<");
        } finally {
            DBResultSet.close(dbrs);
        }
        return max;
    }
    
    public static String getDocCode(ForwarderInfo forwarderInfo) {
        String code = "FWD";
        String dateCode = "";
        if (forwarderInfo.getDocDate() != null) {
            /** get location code; gwawan@21juni2007 */
            Vector vctLocation = PstLocation.list(0, 0, PstLocation.fieldNames[PstLocation.FLD_LOCATION_ID]+"="+forwarderInfo.getLocationId(), "");
            Location location = (Location)vctLocation.get(0);
            
            int nextCounter = forwarderInfo.getCounter();
            Date date = forwarderInfo.getDocDate();
            
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
    
    public static double getTotalCost(long oidReceive) {
        DBResultSet dbrs = null;
        double result = 0;
        try {
            String sql = "";
            sql += " select sum(rmi."+PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_FORWADER_COST];
            sql += " * rmi."+PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_QTY]+") as total";
            sql += " from "+PstMatReceiveItem.TBL_MAT_RECEIVE_ITEM+" rmi";
            
            if(oidReceive != 0)
                sql += " where rmi."+PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_RECEIVE_MATERIAL_ID]+" = "+oidReceive;
            
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while (rs.next()) {
                result = rs.getDouble("total");
            }
        }
        catch(Exception e) {
            System.out.println("Exc in getTotalCost >>> "+e);
        }
        finally {
            DBResultSet.close(dbrs);
        }
        return result;
    }
    
}
