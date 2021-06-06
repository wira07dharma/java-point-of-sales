/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dimata.hanoman.entity.masterdata;

import com.dimata.common.entity.custom.PstDataCustom;
import com.dimata.posbo.db.DBException;
import com.dimata.posbo.db.DBHandler;
import com.dimata.posbo.db.DBResultSet;
import com.dimata.posbo.db.I_DBInterface;
import com.dimata.posbo.db.I_DBType;


import com.dimata.qdep.entity.Entity;
import com.dimata.qdep.entity.I_PersintentExc;
import com.dimata.util.lang.I_Language;
import java.sql.ResultSet;
import java.util.Vector;



/**
 *
 * @author sangtel6
 */

public class PstMasterType extends DBHandler implements I_DBInterface, I_DBType, I_PersintentExc, I_Language {
    
    //public static final  String TBL_MASTER_TYPE = "MASTER_TYPE";

    public static final  String TBL_MASTER_TYPE = "master_type";



	public static final  int FLD_MASTER_TYPE_ID = 0;

	public static final  int FLD_TYPE_GROUP = 1;

	public static final  int FLD_MASTER_CODE = 2;

	public static final  int FLD_MASTER_NAME = 3;

	public static final  int FLD_DESCRIPTION = 4;



	public static final  String[] fieldNames = {

		"MASTER_TYPE_ID",

		"TYPE_GROUP",

		"MASTER_CODE",

		"MASTER_NAME",

		"DESCRIPTION"

	 }; 



	public static final  int[] fieldTypes = {

		TYPE_LONG + TYPE_PK + TYPE_ID,

		TYPE_INT,

		TYPE_STRING,

		TYPE_STRING,

		TYPE_STRING

	 };



   /* public static final int TYPE_SRC_OF_BOOK 		= 0;

    public static final int TYPE_CREDIT_CARD 		= 1; // for hotel, villa losmen etc.

    public static final int TYPE_SALUTATION 		= 2;

    public static final int TYPE_REG_REFERENCE 		= 3;

    public static final int TYPE_OCCUPATION 		= 4;

    public static final int TYPE_INTEREST 			= 5;

  //  public static final int TYPE_OF_EXPENSE_ITEM 	= 6;

  //  public static final int TYPE_ITEM_UNIT 		= 7;

    public static final int TYPE_CURRENCY			= 6;

    public static final int TYPE_CUSTOMER               = 7;*/

    



   /* public static final String[] typeMaster =

        {"Source Of Booking", "Credit Card", "Salutation",

         "Reg. Referency", "Occupation", "Interest",

         "Currency", "Costumer","Term Of Payment"};*/



	public PstMasterType(){

	}



	public PstMasterType(int i) throws DBException { 

		super(new PstMasterType()); 

	}



	public PstMasterType(String sOid) throws DBException { 

		super(new PstMasterType(0)); 

		if(!locate(sOid)) 

			throw new DBException(this,DBException.RECORD_NOT_FOUND); 

		else 

			return; 

	}



	public PstMasterType(long lOid) throws DBException { 

		super(new PstMasterType(0)); 

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

		return TBL_MASTER_TYPE;

	}



	public String[] getFieldNames(){ 

		return fieldNames; 

	}



	public int[] getFieldTypes(){ 

		return fieldTypes; 

	}



	public String getPersistentName(){ 

		return new PstMasterType().getClass().getName(); 

	}



	public long fetchExc(Entity ent) throws Exception{ 

		MasterType mastertype = fetchExc(ent.getOID()); 

		ent = (Entity)mastertype; 

		return mastertype.getOID(); 

	}



	public long insertExc(Entity ent) throws Exception{ 

		return insertExc((MasterType) ent); 

	}



	public long updateExc(Entity ent) throws Exception{ 

		return updateExc((MasterType) ent); 

	}



	public long deleteExc(Entity ent) throws Exception{ 

		if(ent==null){ 

			throw new DBException(this,DBException.RECORD_NOT_FOUND); 

		} 

		return deleteExc(ent.getOID()); 

	}



	public static MasterType fetchExc(long oid) throws DBException{ 

		try{ 

			MasterType mastertype = new MasterType();

			PstMasterType pstMasterType = new PstMasterType(oid); 

			mastertype.setOID(oid);



			mastertype.setTypeGroup(pstMasterType.getInt(FLD_TYPE_GROUP));

			mastertype.setMasterCode(pstMasterType.getString(FLD_MASTER_CODE));

			mastertype.setMasterName(pstMasterType.getString(FLD_MASTER_NAME));

			mastertype.setDescription(pstMasterType.getString(FLD_DESCRIPTION));                       



			return mastertype; 

		}catch(DBException dbe){ 

			throw dbe; 

		}catch(Exception e){ 

			throw new DBException(new PstMasterType(0),DBException.UNKNOWN); 

		} 

	}



	public static long insertExc(MasterType mastertype) throws DBException{ 

		try{ 

			PstMasterType pstMasterType = new PstMasterType(0);



			pstMasterType.setInt(FLD_TYPE_GROUP, mastertype.getTypeGroup());

			pstMasterType.setString(FLD_MASTER_CODE, mastertype.getMasterCode());

			pstMasterType.setString(FLD_MASTER_NAME, mastertype.getMasterName());

			pstMasterType.setString(FLD_DESCRIPTION, mastertype.getDescription());



			pstMasterType.insert(); 
			
			//kebutuhan untuk service transfer katalog
			PstDataCustom.insertDataForSyncAllLocation(pstMasterType.getInsertSQL());

			mastertype.setOID(pstMasterType.getlong(FLD_MASTER_TYPE_ID));

		}catch(DBException dbe){ 

			throw dbe; 

		}catch(Exception e){ 

			throw new DBException(new PstMasterType(0),DBException.UNKNOWN); 

		}

		return mastertype.getOID();

	}



	public static long updateExc(MasterType mastertype) throws DBException{ 

		try{ 

			if(mastertype.getOID() != 0){ 

				PstMasterType pstMasterType = new PstMasterType(mastertype.getOID());



				pstMasterType.setInt(FLD_TYPE_GROUP, mastertype.getTypeGroup());

				pstMasterType.setString(FLD_MASTER_CODE, mastertype.getMasterCode());

				pstMasterType.setString(FLD_MASTER_NAME, mastertype.getMasterName());

				pstMasterType.setString(FLD_DESCRIPTION, mastertype.getDescription());



				pstMasterType.update(); 
				//kebutuhan untuk service transfer katalog
				PstDataCustom.insertDataForSyncAllLocation(pstMasterType.getUpdateSQL());

				return mastertype.getOID();



			}

		}catch(DBException dbe){ 

			throw dbe; 

		}catch(Exception e){ 

			throw new DBException(new PstMasterType(0),DBException.UNKNOWN); 

		}

		return 0;

	}



	public static long deleteExc(long oid) throws DBException{ 

		try{ 

			PstMasterType pstMasterType = new PstMasterType(oid);

			pstMasterType.delete();
			
			//kebutuhan untuk service transfer katalog
			PstDataCustom.insertDataForSyncAllLocation(pstMasterType.getDeleteSQL());

		}catch(DBException dbe){ 

			throw dbe; 

		}catch(Exception e){ 

			throw new DBException(new PstMasterType(0),DBException.UNKNOWN); 

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

			String sql = "SELECT * FROM " + TBL_MASTER_TYPE; 

			if(whereClause != null && whereClause.length() > 0)

				sql = sql + " WHERE " + whereClause;

			if(order != null && order.length() > 0)

				sql = sql + " ORDER BY " + order;

			if(limitStart == 0 && recordToGet == 0)

				sql = sql + "";

			else

				sql = sql + " LIMIT " + limitStart + ","+ recordToGet ;

			dbrs = DBHandler.execQueryResult(sql);

			ResultSet rs = dbrs.getResultSet();

			while(rs.next()) {

				MasterType mastertype = new MasterType();

				resultToObject(rs, mastertype);

				lists.add(mastertype);

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



	public static void resultToObject(ResultSet rs, MasterType mastertype){

		try{

			mastertype.setOID(rs.getLong(PstMasterType.fieldNames[PstMasterType.FLD_MASTER_TYPE_ID]));

			mastertype.setTypeGroup(rs.getInt(PstMasterType.fieldNames[PstMasterType.FLD_TYPE_GROUP]));

			mastertype.setMasterCode(rs.getString(PstMasterType.fieldNames[PstMasterType.FLD_MASTER_CODE]));

			mastertype.setMasterName(rs.getString(PstMasterType.fieldNames[PstMasterType.FLD_MASTER_NAME]));

			mastertype.setDescription(rs.getString(PstMasterType.fieldNames[PstMasterType.FLD_DESCRIPTION]));



		}catch(Exception e){ }

	}



	public static boolean checkOID(long masterTypeId){

		DBResultSet dbrs = null;

		boolean result = false;

		try{

			String sql = "SELECT * FROM " + TBL_MASTER_TYPE + " WHERE " + 

						PstMasterType.fieldNames[PstMasterType.FLD_MASTER_TYPE_ID] + " = " + masterTypeId;



			dbrs = DBHandler.execQueryResult(sql);

			ResultSet rs = dbrs.getResultSet();



			while(rs.next()) { result = true; }

			rs.close();

		}catch(Exception e){

			System.out.println("err : "+e.toString());

		}finally{

			DBResultSet.close(dbrs);

			

		}

                return result;

	}



	public static int getCount(String whereClause){

		DBResultSet dbrs = null;

		try {

			String sql = "SELECT COUNT("+ PstMasterType.fieldNames[PstMasterType.FLD_MASTER_TYPE_ID] + ") FROM " + TBL_MASTER_TYPE;

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









    public static void transferDataTravel(){


//update fitra
        String where = PstMasterType.fieldNames[PstMasterType.FLD_TYPE_GROUP]+"= 1";

        Vector vct = PstMasterType.list(0,0, where, null);



        for(int i=0; i<vct.size(); i++){

            try{

	            MasterType mt = (MasterType)vct.get(i);



                System.out.println("Transfer Data : "+mt.getMasterName());



	            Contact clist = new Contact();

	            clist.setContactCode(mt.getMasterCode());

	            clist.setCompName(mt.getMasterName());

	            clist.setNotes(mt.getDescription());

	            clist.setContactType(PstContact.CONTACT_TYPE_TRAVEL_AGENT);



                System.out.println("Transfer Data 1 ");

	

	            long oid = PstContact.insertExc(clist);



                System.out.println("Transfer Data 2 " + oid);



	            if(oid!=0){

                    //clist.setOID(mt.getOID());

                    //PstContact.updateExc(clist);

                    ContactClassAssign c = new ContactClassAssign();

                    c.setContactId(oid);

                    c.setContactClassId(504404178506109175l);



                    oid = PstContactClassAssign.insertExc(c);



	                PstMasterType.deleteExc(mt.getOID());

                    System.out.println("Transfer Data 3 ");

	            }

            }

            catch(Exception e){

                System.out.println("Ecc "+e.toString());

            }



        }





    }

    

    

    public static boolean isNameAlreadyExist(long id, int group, String name){

            String where  = "";

            //add new

            if(id==0){

                where  = fieldNames[FLD_MASTER_NAME]+"='"+name+"' AND "+fieldNames[FLD_TYPE_GROUP]+"="+group;

            }

            //update

            else{

                where  = fieldNames[FLD_MASTER_NAME]+"='"+name+"' AND "+fieldNames[FLD_TYPE_GROUP]+"="+group+" AND "+fieldNames[FLD_MASTER_TYPE_ID]+"!="+id;

            }

            

            Vector vct = list(0,0, where, null);

            if(vct!=null && vct.size()>0)

                return true;

            else

                return false;

            

        }

        

        public static boolean isCodeAlreadyExist(long id, int group, String code){

            String where  = "";

            //add new

            if(id==0){

                where  = fieldNames[FLD_MASTER_CODE]+"='"+code+"' AND "+fieldNames[FLD_TYPE_GROUP]+"="+group;

            }

            //update

            else{

                where  = fieldNames[FLD_MASTER_CODE]+"='"+code+"' AND "+fieldNames[FLD_TYPE_GROUP]+"="+group+" AND "+fieldNames[FLD_MASTER_TYPE_ID]+"!="+id;

            }



            

            Vector vct = list(0,0, where, null);

            if(vct!=null && vct.size()>0)

                return true;

            else

                return false;

            

        }

        

        public static Vector listCustom(){

            return list(0, 500, "","");

        }



        public static Vector listCustom(int limitStart,int recordToGet, String whereClause, String order){

            Vector lists = new Vector(); 

		DBResultSet dbrs = null;

		try {

			String sql = "SELECT * FROM " + TBL_MASTER_TYPE; 

			if(whereClause != null && whereClause.length() > 0)

				sql = sql + " WHERE " + whereClause + " = "+PstContactClass.CONTACT_TYPE_MEMBER;

			if(order != null && order.length() > 0)

				sql = sql + " ORDER BY " + order;

			if(limitStart == 0 && recordToGet == 0)

				sql = sql + "";

			else

				sql = sql + " LIMIT " + limitStart + ","+ recordToGet ;

			dbrs = DBHandler.execQueryResult(sql);

			ResultSet rs = dbrs.getResultSet();

			while(rs.next()) {

				MasterType mastertype = new MasterType();

				resultToObject(rs, mastertype);

				lists.add(mastertype);

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






}
