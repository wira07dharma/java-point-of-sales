/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dimata.hanoman.entity.masterdata;

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

public class PstMasterGroup extends DBHandler implements I_DBInterface, I_DBType, I_PersintentExc, I_Language {
    
    //public static final  String TBL_MASTER_TYPE = "MASTER_TYPE";

    public static final  String TBL_MASTER_GROUP = "master_group";



	public static final  int FLD_MASTER_GROUP_ID = 0;

	public static final  int FLD_TYPE_GROUP = 1;

	public static final  int FLD_NAMA_GROUP = 2;

	



	public static final  String[] fieldNames = {

		"MASTER_GROUP_ID",

		"TYPE_GROUP",

		"NAMA_GROUP"

		

	 }; 



	public static final  int[] fieldTypes = {

		TYPE_LONG + TYPE_PK + TYPE_ID,

		TYPE_INT + TYPE_FK,

		TYPE_STRING

		

	 };



    public static final int TYPE_SRC_OF_BOOK 		= 0;

    public static final int TYPE_CREDIT_CARD 		= 1; // for hotel, villa losmen etc.

    public static final int TYPE_SALUTATION 		= 2;

    public static final int TYPE_REG_REFERENCE 		= 3;

    public static final int TYPE_OCCUPATION 		= 4;

    public static final int TYPE_INTEREST 			= 5;

  //  public static final int TYPE_OF_EXPENSE_ITEM 	= 6;

  //  public static final int TYPE_ITEM_UNIT 		= 7;

    public static final int TYPE_CURRENCY			= 6;

    public static final int TYPE_CUSTOMER               = 7;

    



    public static final String[] typeMaster =

        {"Source Of Booking", "Credit Card", "Salutation",

         "Reg. Referency", "Occupation", "Interest",

         "Currency", "Costumer","Term Of Payment"};



	public PstMasterGroup(){

	}



	public PstMasterGroup(int i) throws DBException { 

		super(new PstMasterGroup()); 

	}



	public PstMasterGroup(String sOid) throws DBException { 

		super(new PstMasterGroup(0)); 

		if(!locate(sOid)) 

			throw new DBException(this,DBException.RECORD_NOT_FOUND); 

		else 

			return; 

	}



	public PstMasterGroup(long lOid) throws DBException { 

		super(new PstMasterGroup(0)); 

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

		return TBL_MASTER_GROUP;

	}



	public String[] getFieldNames(){ 

		return fieldNames; 

	}



	public int[] getFieldTypes(){ 

		return fieldTypes; 

	}



	public String getPersistentName(){ 

		return new PstMasterGroup().getClass().getName(); 

	}



	public long fetchExc(Entity ent) throws Exception{ 

		MasterGroup mastergroup = fetchExc(ent.getOID()); 

		ent = (Entity)mastergroup; 

		return mastergroup.getOID(); 

	}



	public long insertExc(Entity ent) throws Exception{ 

		return insertExc((MasterGroup) ent); 

	}



	public long updateExc(Entity ent) throws Exception{ 

		return updateExc((MasterGroup) ent); 

	}



	public long deleteExc(Entity ent) throws Exception{ 

		if(ent==null){ 

			throw new DBException(this,DBException.RECORD_NOT_FOUND); 

		} 

		return deleteExc(ent.getOID()); 

	}



	public static MasterGroup fetchExc(long oid) throws DBException{ 

		try{ 

			MasterGroup mastergroup = new MasterGroup();

			PstMasterGroup pstMasterGroup = new PstMasterGroup(oid); 

			mastergroup.setOID(oid);



			mastergroup.setTypeGroup(pstMasterGroup.getInt(FLD_TYPE_GROUP));

			mastergroup.setNamaGroup(pstMasterGroup.getString(FLD_NAMA_GROUP));

			              



			return mastergroup; 

		}catch(DBException dbe){ 

			throw dbe; 

		}catch(Exception e){ 

			throw new DBException(new PstMasterGroup(0),DBException.UNKNOWN); 

		} 

	}



	public static long insertExc(MasterGroup mastergroup) throws DBException{ 

		try{ 

			PstMasterGroup pstMasterGroup = new PstMasterGroup(0);



			pstMasterGroup.setInt(FLD_TYPE_GROUP, mastergroup.getTypeGroup());

			pstMasterGroup.setString(FLD_NAMA_GROUP, mastergroup.getNamaGroup());

			



			pstMasterGroup.insert(); 

			mastergroup.setOID(pstMasterGroup.getlong(FLD_MASTER_GROUP_ID));

		}catch(DBException dbe){ 

			throw dbe; 

		}catch(Exception e){ 

			throw new DBException(new PstMasterGroup(0),DBException.UNKNOWN); 

		}

		return mastergroup.getOID();

	}



	public static long updateExc(MasterGroup mastergroup) throws DBException{ 

		try{ 

			if(mastergroup.getOID() != 0){ 

				PstMasterGroup pstMasterGroup = new PstMasterGroup(mastergroup.getOID());



				pstMasterGroup.setInt(FLD_TYPE_GROUP, mastergroup.getTypeGroup());

				pstMasterGroup.setString(FLD_NAMA_GROUP, mastergroup.getNamaGroup());

				



				pstMasterGroup.update(); 

				return mastergroup.getOID();



			}

		}catch(DBException dbe){ 

			throw dbe; 

		}catch(Exception e){ 

			throw new DBException(new PstMasterGroup(0),DBException.UNKNOWN); 

		}

		return 0;

	}



	public static long deleteExc(long oid) throws DBException{ 

		try{ 

			PstMasterGroup pstMasterGroup = new PstMasterGroup(oid);

			pstMasterGroup.delete();

		}catch(DBException dbe){ 

			throw dbe; 

		}catch(Exception e){ 

			throw new DBException(new PstMasterGroup(0),DBException.UNKNOWN); 

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

			String sql = "SELECT * FROM " + TBL_MASTER_GROUP; 

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

				MasterGroup mastergroup = new MasterGroup();

				resultToObject(rs, mastergroup);

				lists.add(mastergroup);

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


public static Vector listJoin(int limitStart,int recordToGet, String whereClause, String order, long oidMaster){

		Vector lists = new Vector(); 

		DBResultSet dbrs = null;

		try {

			String sql = "SELECT mg.* FROM " + TBL_MASTER_GROUP + " AS mg " +
                                     " INNER JOIN " + PstMasterType.TBL_MASTER_TYPE + " AS mt " +
                                     " ON mg." +PstMasterGroup.fieldNames[PstMasterGroup.FLD_TYPE_GROUP] + 
                                     " = mt." + PstMasterType.fieldNames[PstMasterType.FLD_TYPE_GROUP] +
                                     " WHERE mt."+ PstMasterType.fieldNames[PstMasterType.FLD_TYPE_GROUP]+ " = " + oidMaster; 

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

				MasterGroup mastergroup = new MasterGroup();

				resultToObject(rs, mastergroup);

				lists.add(mastergroup);

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

        
        
	public static void resultToObject(ResultSet rs, MasterGroup mastergroup){

		try{

			mastergroup.setOID(rs.getLong(PstMasterGroup.fieldNames[PstMasterGroup.FLD_MASTER_GROUP_ID]));

			mastergroup.setTypeGroup(rs.getInt(PstMasterGroup.fieldNames[PstMasterGroup.FLD_TYPE_GROUP]));

			mastergroup.setNamaGroup(rs.getString(PstMasterGroup.fieldNames[PstMasterGroup.FLD_NAMA_GROUP]));

			


		}catch(Exception e){ }

	}



	public static boolean checkOID(long masterTypeId){

		DBResultSet dbrs = null;

		boolean result = false;

		try{

			String sql = "SELECT * FROM " + TBL_MASTER_GROUP + " WHERE " + 

						PstMasterGroup.fieldNames[PstMasterGroup.FLD_MASTER_GROUP_ID] + " = " + masterTypeId;



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

			String sql = "SELECT COUNT("+ PstMasterGroup.fieldNames[PstMasterGroup.FLD_MASTER_GROUP_ID] + ") FROM " + TBL_MASTER_GROUP;

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



        String where = PstMasterGroup.fieldNames[PstMasterGroup.FLD_TYPE_GROUP]+"="+TYPE_CREDIT_CARD;

        Vector vct = PstMasterGroup.list(0,0, where, null);



        for(int i=0; i<vct.size(); i++){

            try{

	            MasterGroup mt = (MasterGroup)vct.get(i);



                System.out.println("Transfer Data : "+mt.getNamaGroup());



	            Contact clist = new Contact();

	            //clist.setContactCode(mt.getMasterCode());

	            clist.setCompName(mt.getNamaGroup());

	            //clist.setNotes(mt.getDescription());

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



	                PstMasterGroup.deleteExc(mt.getOID());

                    System.out.println("Transfer Data 3 ");

	            }

            }

            catch(Exception e){

                System.out.println("Ecc "+e.toString());

            }



        }





    }

    

    

    /* public static boolean isNameAlreadyExist(long id, int group, String name){

            String where  = "";

            //add new

            if(id==0){

                where  = fieldNames[FLD_NAMA_GROUP]+"='"+name+"' AND "+fieldNames[FLD_TYPE_GROUP]+"="+group;

            }

            //update

            else{

                where  = fieldNames[FLD_NAMA_GROUP]+"='"+name+"' AND "+fieldNames[FLD_TYPE_GROUP]+"="+group+" AND "+fieldNames[FLD_MASTER_GOUP_ID]+"!="+id;

            }

            

            Vector vct = list(0,0, where, null);

            if(vct!=null && vct.size()>0)

                return true;

            else

                return false;

            

        }*/

        

       /* public static boolean isCodeAlreadyExist(long id, int group, String code){

            String where  = "";

            //add new

            if(id==0){

                where  = fieldNames[FLD_NAMA_GROUP]+"='"+code+"' AND "+fieldNames[FLD_TYPE_GROUP]+"="+group;

            }

            //update

            else{

                where  = fieldNames[FLD_NAMA_GROUP]+"='"+code+"' AND "+fieldNames[FLD_TYPE_GROUP]+"="+group+" AND "+fieldNames[FLD_MASTER_GOUP_ID]+"!="+id;

            }



            

            Vector vct = list(0,0, where, null);

            if(vct!=null && vct.size()>0)

                return true;

            else

                return false;

            

        }*/

        

        public static Vector listCustom(){

            return list(0, 500, "","");

        }



        public static Vector listCustom(int limitStart,int recordToGet, String whereClause, String order){

            Vector lists = new Vector(); 

		DBResultSet dbrs = null;

		try {

			String sql = "SELECT * FROM " + TBL_MASTER_GROUP; 

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

				MasterGroup mastergroup = new MasterGroup();

				resultToObject(rs, mastergroup);

				lists.add(mastergroup);

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
