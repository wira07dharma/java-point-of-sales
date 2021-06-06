 

/* Created on 	:  [date] [time] AM/PM 

 * 

 * @author  	: karya

 * @version  	: 01

 */



/*******************************************************************

 * Class Description 	: [project description ... ] 

 * Imput Parameters 	: [input parameter ...] 

 * Output 		: [output ...] 

 *******************************************************************/



package com.dimata.hanoman.entity.masterdata;



/* package java */ 

import java.io.*;

import java.sql.*;

import java.util.*;

import java.util.Date;



/* package qdep */

import com.dimata.util.lang.I_Language;

import com.dimata.posbo.db.DBException;
import com.dimata.posbo.db.DBHandler;
import com.dimata.posbo.db.DBResultSet;
import com.dimata.posbo.db.I_DBInterface;
import com.dimata.posbo.db.I_DBType;




import com.dimata.qdep.entity.*;

import com.dimata.hanoman.entity.masterdata.*;import com.dimata.harisma.entity.masterdata.*;



public class PstContactClass extends DBHandler implements I_DBInterface, I_DBType, I_PersintentExc, I_Language { 





    //public static final  String TBL_CONTACT_CLASS = "CONTACT_CLASS";

    public static final  String TBL_CONTACT_CLASS = "contact_class";



	public static final  int FLD_CONTACT_CLASS_ID = 0;

	public static final  int FLD_CLASS_NAME = 1;

	public static final  int FLD_CLASS_DESCRIPTION = 2;

    public static final  int FLD_CLASS_TYPE = 3;



	public static final  String[] fieldNames = {

		"CONTACT_CLASS_ID",

		"CLASS_NAME",

		"CLASS_DESCRIPTION",

        "CLASS_TYPE"

	 }; 



	public static final  int[] fieldTypes = {

		TYPE_LONG + TYPE_PK + TYPE_ID,

		TYPE_STRING,

		TYPE_STRING,

        TYPE_INT

	 }; 







    public static final int CONTACT_TYPE_TRAVEL_AGENT           = 0;

    /*gunanya ini adalah, biar tidak banyak yang berubah

    di hanoman memakai ini*/

    public static final int CONTACT_TYPE_SUPPLIER		= 1;

    /*tapi di garment memakai ini*/

    public static final  int FLD_CLASS_VENDOR = 1;



    public static final int CONTACT_TYPE_GUIDE			= 2;

    public static final int CONTACT_TYPE_COMPANY		= 3;

    public static final int CONTACT_TYPE_EMPLOYEE		= 4;



    public static final  int FLD_CLASS_SHIPPER                  = 5;

    public static final  int FLD_CLASS_CLIENT                   = 6;

    //baru update opie-eyek 20130624
    public static final  int CONTACT_TYPE_MEMBER             = 7;

    public static final  int CONTACT_TYPE_PARTNER               = 8;

    public static final  int CONTACT_TYPE_DOT_COM_COMPANY       =9;


    public static final int CONTACT_TYPE_CUSTOMER                 = 10;
    public static final int CONTACT_TYPE_CASHIER               = 11;
    public static final int CONTACT_TYPE_GOVERMENT            = 12;
    /*
     public static final int CONTACT_TYPE_MEMBER             = 7;
    public static final int FLD_CLASS_VENDOR                = 8;
    public static final int FLD_CLASS_COLLECTOR             = 9;
    public static final int FLD_CLASS_DONOR                 = 10;
    public static final int FLD_CLASS_CASHIER               = 11;
    public static final int FLD_CLASS_GOVERNMENT            = 12;
    public static final int FLD_CLASS_INSTITUTION           = 13;
    public static final int FLD_CLASS_PARTNER               = 14;
    public static final int FLD_CLASS_SHAREHOLDER           = 15;
    public static final int FLD_CLASS_VISITOR               = 16;
    public static final int FLD_CLASS_CARRIER               = 17;
    public static final int FLD_CLASS_OTHERS                = 18;
     */

    public static final String[] contactType = {

        "Travel Agent",//0

        "Supplier/Vendor",//1

        "Guide",//2

        "Corporate",//3

        "Employee",//4

        "Shipper",//5

        "Client",//6

        "Member",//7

        "Partner",//8

        ".com Company",//9

        "Customer",//10

        "Cashier",//11

        "Goverment"//12

    };





	public static final  String[] fieldClassType = {

		"Travel Agent",

                "Supplier/Vendor",

                "Guide",

                "Company",

                "Employee",

                "Shipper",

                "Client",

                "Member",

                "Partner",

                ".com Company",

                "Customer",

                "Cashier",

                "Goverment"

	 };





	public PstContactClass(){

	}



	public PstContactClass(int i) throws DBException { 

		super(new PstContactClass()); 

	}



	public PstContactClass(String sOid) throws DBException { 

		super(new PstContactClass(0)); 

		if(!locate(sOid)) 

			throw new DBException(this,DBException.RECORD_NOT_FOUND); 

		else 

			return; 

	}



	public PstContactClass(long lOid) throws DBException { 

		super(new PstContactClass(0)); 

		String sOid = "0"; 

		try { 

			sOid = String.valueOf(lOid); 

		}catch(Exception e) { 

			throw new DBException(this,DBException.RECORD_NOT_FOUND); 

		} 

		if(!locate(sOid)) 

			throw new DBException(this,DBException.RECORD_NOT_FOUND); 

		else 

			return; 

	} 



	public int getFieldSize(){ 

		return fieldNames.length; 

	}



	public String getTableName(){ 

		return TBL_CONTACT_CLASS;

	}



	public String[] getFieldNames(){ 

		return fieldNames; 

	}



	public int[] getFieldTypes(){ 

		return fieldTypes; 

	}



	public String getPersistentName(){ 

		return new PstContactClass().getClass().getName(); 

	}



	public long fetchExc(Entity ent) throws Exception{ 

		ContactClass contactclass = fetchExc(ent.getOID()); 

		ent = (Entity)contactclass; 

		return contactclass.getOID(); 

	}



	public long insertExc(Entity ent) throws Exception{ 

		return insertExc((ContactClass) ent); 

	}



	public long updateExc(Entity ent) throws Exception{ 

		return updateExc((ContactClass) ent); 

	}



	public long deleteExc(Entity ent) throws Exception{ 

		if(ent==null){ 

			throw new DBException(this,DBException.RECORD_NOT_FOUND); 

		} 

		return deleteExc(ent.getOID()); 

	}



	public static ContactClass fetchExc(long oid) throws DBException{ 

		try{ 

			ContactClass contactclass = new ContactClass();

			PstContactClass pstContactClass = new PstContactClass(oid); 

			contactclass.setOID(oid);



			contactclass.setClassName(pstContactClass.getString(FLD_CLASS_NAME));

			contactclass.setClassDescription(pstContactClass.getString(FLD_CLASS_DESCRIPTION));

            contactclass.setClassType(pstContactClass.getInt(FLD_CLASS_TYPE));



			return contactclass; 

		}catch(DBException dbe){ 

			throw dbe; 

		}catch(Exception e){ 

			throw new DBException(new PstContactClass(0),DBException.UNKNOWN); 

		} 

	}



	public static long insertExc(ContactClass contactclass) throws DBException{ 

		try{ 

			PstContactClass pstContactClass = new PstContactClass(0);



			pstContactClass.setString(FLD_CLASS_NAME, contactclass.getClassName());

			pstContactClass.setString(FLD_CLASS_DESCRIPTION, contactclass.getClassDescription());

            pstContactClass.setInt(FLD_CLASS_TYPE, contactclass.getClassType());



			pstContactClass.insert(); 

			contactclass.setOID(pstContactClass.getlong(FLD_CONTACT_CLASS_ID));

		}catch(DBException dbe){ 

			throw dbe; 

		}catch(Exception e){ 

			throw new DBException(new PstContactClass(0),DBException.UNKNOWN); 

		}

		return contactclass.getOID();

	}



	public static long updateExc(ContactClass contactclass) throws DBException{ 

		try{ 

			if(contactclass.getOID() != 0){ 

				PstContactClass pstContactClass = new PstContactClass(contactclass.getOID());



				pstContactClass.setString(FLD_CLASS_NAME, contactclass.getClassName());

				pstContactClass.setString(FLD_CLASS_DESCRIPTION, contactclass.getClassDescription());

                pstContactClass.setInt(FLD_CLASS_TYPE, contactclass.getClassType());



				pstContactClass.update(); 

				return contactclass.getOID();



			}

		}catch(DBException dbe){ 

			throw dbe; 

		}catch(Exception e){ 

			throw new DBException(new PstContactClass(0),DBException.UNKNOWN); 

		}

		return 0;

	}



	public static long deleteExc(long oid) throws DBException{ 

		try{ 

			PstContactClass pstContactClass = new PstContactClass(oid);

			pstContactClass.delete();

		}catch(DBException dbe){ 

			throw dbe; 

		}catch(Exception e){ 

			throw new DBException(new PstContactClass(0),DBException.UNKNOWN); 

		}

		return oid;

	}



	public static Vector listAll(){ 

		return list(0, 500, "",""); 

	}



	public static Vector list(int limitStart,int recordToGet, String whereClause, String order){

		Vector lists = new Vector(); 

		DBResultSet dbrs = null;

		try {

			String sql = "SELECT  * FROM " + PstContactClass.TBL_CONTACT_CLASS;

			if(whereClause != null && whereClause.length() > 0)

				sql = sql + " WHERE " + whereClause;

			if(order != null && order.length() > 0)

				sql = sql + " ORDER BY " + order;



			switch (DBHandler.DBSVR_TYPE) {

                case DBHandler.DBSVR_MYSQL :

						if(limitStart == 0 && recordToGet == 0)

							sql = sql + "";

						else

							sql = sql + " LIMIT " + limitStart + ","+ recordToGet ;



                        break;



                 case DBHandler.DBSVR_POSTGRESQL :

						if(limitStart == 0 && recordToGet == 0)

							sql = sql + "";

						else

							sql = sql + " LIMIT " +recordToGet + " OFFSET "+ limitStart ;



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

			dbrs = DBHandler.execQueryResult(sql);

			ResultSet rs = dbrs.getResultSet();

			while(rs.next()) {

				ContactClass contactclass = new ContactClass();

				resultToObject(rs, contactclass);

				lists.add(contactclass);

			}

			rs.close();

			return lists;



		}catch(Exception e) {

			System.out.println(e);

		}finally {

			DBResultSet.close(dbrs);

		}

			return new Vector();

	}



	private static void resultToObject(ResultSet rs, ContactClass contactclass){

		try{

			contactclass.setOID(rs.getLong(PstContactClass.fieldNames[PstContactClass.FLD_CONTACT_CLASS_ID]));

			contactclass.setClassName(rs.getString(PstContactClass.fieldNames[PstContactClass.FLD_CLASS_NAME]));

			contactclass.setClassDescription(rs.getString(PstContactClass.fieldNames[PstContactClass.FLD_CLASS_DESCRIPTION]));

            contactclass.setClassType(rs.getInt(PstContactClass.fieldNames[PstContactClass.FLD_CLASS_TYPE]));



		}catch(Exception e){ }

	}



	public static boolean checkOID(long contactClassId){

		DBResultSet dbrs = null;

		boolean result = false;

		try{

			String sql = "SELECT * FROM " + TBL_CONTACT_CLASS + " WHERE " +

						PstContactClass.fieldNames[PstContactClass.FLD_CONTACT_CLASS_ID] + " = " + contactClassId;



			dbrs = DBHandler.execQueryResult(sql);

			ResultSet rs = dbrs.getResultSet();



			while(rs.next()) { result = true; }

			rs.close();

		}catch(Exception e){

			System.out.println("err : "+e.toString());

		}finally{

			DBResultSet.close(dbrs);

			return result;

		}

	}



	public static int getCount(String whereClause){

		DBResultSet dbrs = null;

		try {

			String sql = "SELECT COUNT("+ PstContactClass.fieldNames[PstContactClass.FLD_CONTACT_CLASS_ID] + ") FROM " + TBL_CONTACT_CLASS;

			if(whereClause != null && whereClause.length() > 0)

				sql = sql + " WHERE " + whereClause;



			dbrs = DBHandler.execQueryResult(sql);

			ResultSet rs = dbrs.getResultSet();



			int count = 0;

			while(rs.next()) { count = rs.getInt(1); }



			rs.close();

			return count;

		}catch(Exception e) {

			return 0;

		}finally {

			DBResultSet.close(dbrs);

		}

	}





	/* This method used to find current data */

	public static int findLimitStart( long oid, int recordToGet, String whereClause){

		String order = "";

		int size = getCount(whereClause);

		int start = 0;

		boolean found =false;

		for(int i=0; (i < size) && !found ; i=i+recordToGet){

			 Vector list =  list(i,recordToGet, whereClause, order); 

			 start = i;

			 if(list.size()>0){

			  for(int ls=0;ls<list.size();ls++){ 

			  	   ContactClass contactclass = (ContactClass)list.get(ls);

				   if(oid == contactclass.getOID())

					  found=true;

			  }

		  }

		}

		if((start >= size) && (size > 0))

		    start = start - recordToGet;



		return start;

	}

}

