/*
 * TransMasterdata.java
 *
 * Created on December 21, 2004, 12:42 PM
 */

package com.dimata.posbo.session.transferdata;

import com.dimata.common.entity.contact.PstContactClass;
import com.dimata.common.entity.contact.PstContactClassAssign;
import com.dimata.common.entity.location.PstLocation;
import com.dimata.common.entity.payment.*;
import com.dimata.pos.entity.balance.PstBalance;
import com.dimata.pos.entity.balance.PstCashCashier;
import com.dimata.pos.entity.billing.PstBillDetail;
import com.dimata.pos.entity.billing.PstBillMain;
import com.dimata.pos.entity.billing.PstPendingOrder;
import com.dimata.pos.entity.payment.*;
import com.dimata.posbo.db.DBHandler;
import com.dimata.posbo.db.DBResultSet;
import com.dimata.posbo.entity.masterdata.*;
import com.dimata.posbo.entity.transferdata.PstMaterialTemp;
import com.dimata.posbo.entity.transferdata.PstMemberRegTemp;
import com.dimata.posbo.entity.transferdata.SrcTransferData;
import com.dimata.posbo.entity.warehouse.PstMaterialStockCode;
import com.dimata.util.ExecCommand;
import com.dimata.util.Formater;

import java.io.*;
 import java.sql.ResultSet;
import java.util.Date;
import java.util.Vector;


/**
 *
 * @@author  yogi
 */


/* package project */
//import com.dimata.qdep.db.*;

public class TransMasterdata implements Runnable{

    public static String CONSTRUCT_FOR_WIN = "No";
    public static String OUT_TO_FILE = "Yes";
    public static String PATH_DATA_OUT = "/usr/ngasi/contexts/lascolonias/colonias/data_out/";    
    public static String PATH_DATA_IN = "/usr/ngasi/contexts/lascolonias/colonias/data_in/";
    public static String PATH_MYSQL_BIN = "/usr/ngasi/contexts/lascolonias/colonias/";
    public static String CMD_DUMP = "mysqldump ";
    public static String CMD_UNDUMP = "mysql ";
    public static String DB_NAME = "lascolonias_2"; // 
    public static String DB_USER = "lascolonias_2"; // lascolonias_2
    public static String DB_PASSWORD = "dsj123"; // dsj123
    public static String TABLE_NAME = "pos_material_temp";
    public static String DUMP_NAME = "cashier_dump.sql";
    public static String DUMP_DELETE_NAME = "cashier_delete.sql";
    public static String DB_NAME_UNDUMP = "cashier_client";
    public static String FILE_BATCH = "restore.sh";
    public static String ALL_DUMP_NAME = "cashier_all_dump.sql";
    public static String[] TABLE_NAMES =
            {"pos_material_temp", "contact_list_temp"};

    public static String statusNames[][] = {
        {"Proses Gagal!", "Proses Berhasil!","Silahkan tunggu, Sedang diproses..."},
        {"Failed", "Success","Please wait, Processing..."}
    };

    public static int IN_PROCESS = 2;
    public static int SUCCESS = 1;
    public static int FAILED = 0;

    /** Holds value of property indexDB. */
    private int indexDB;

    /** Holds value of property nameDB. */
    private String nameDB = "";

    /** Holds value of property listDB. */
    private Vector listDB = new Vector();

    /** Holds value of property resultTransfer. */
    private int resultTransfer = FAILED;

    /** Creates a new instance of TransMasterdata */
    public TransMasterdata() {

    }

    public TransMasterdata(int indexDB, String DBName) {
        this.indexDB = indexDB;
        this.nameDB = DBName;
    }

    public TransMasterdata(String dbName, String[] tableNames, String pathMysql, String pathData) {
        DB_NAME = dbName;
        DB_NAME_UNDUMP = dbName;
        TABLE_NAMES = tableNames;
        PATH_MYSQL_BIN = pathMysql;
        PATH_DATA_OUT = pathData;
    }

    public TransMasterdata(String pathMysql, String pathData) {
        getDatabaseName();
        DB_NAME_UNDUMP = DB_NAME;
        TABLE_NAMES = showTable();
        PATH_MYSQL_BIN = pathMysql;
        PATH_DATA_OUT = pathData;
        //checkFile(PATH_DATA_OUT);
    }

    public TransMasterdata(String pathMysql, String pathData, String pathDataIn) {
        getDatabaseName();
        DB_NAME_UNDUMP = DB_NAME;
        TABLE_NAMES = showTable();
        PATH_MYSQL_BIN = pathMysql;
        PATH_DATA_OUT = pathData;  
        PATH_DATA_IN = pathDataIn;
        //checkFile(PATH_DATA_OUT);
        checkFile(PATH_DATA_IN);
    }

    public TransMasterdata(String[] tableNames, String pathMysql, String pathData) {
        getDatabaseName();
        DB_NAME_UNDUMP = DB_NAME;
        TABLE_NAMES = tableNames;
        PATH_MYSQL_BIN = pathMysql;
        PATH_DATA_OUT = pathData;
        checkFile(PATH_DATA_OUT);
    }

    public TransMasterdata(String[] tableNames, String pathMysql, String pathData, String pathDataIn) {
        getDatabaseName();
        DB_NAME_UNDUMP = DB_NAME;
        TABLE_NAMES = tableNames;
        PATH_MYSQL_BIN = pathMysql;
        PATH_DATA_OUT = pathData;
        PATH_DATA_IN = pathDataIn;
        //checkFile(PATH_DATA_OUT);
        //checkFile(PATH_DATA_IN);
    }

    /* generate string password */
    public static String getPassword() {
        String result = "";
        if (DB_PASSWORD != null && DB_PASSWORD.length() > 0) {
            result = result + " -p" + DB_PASSWORD;
        }
        return result;
    }

    /* check folder if not exist generate */
    public static void checkFile(String pathFile) {
        try {
            File file = new File(pathFile);
            if (!file.isDirectory()) {
                file.mkdir();
            }  
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("err check file>>> : " + e.toString());
        }
    }

    /* get database name, user, password*/
    public static void getDatabaseName() {  
        try {
            if(CONSTRUCT_FOR_WIN.equals("Yes")){
               FILE_BATCH = PATH_DATA_IN +"restore.bat";
            }else{
                FILE_BATCH = "restore.sh";                
            } 
            System.out.println("FILE_BATCH > :"+FILE_BATCH);
            
           /*String configfile = DBHandler.CONFIG_FILE;
            DBConfigReader configReader = new DBConfigReader(configfile);
            String dbUrl = configReader.getConfigValue("dburl");
            String dbUser = configReader.getConfigValue("dbuser");
            String dbPwd = configReader.getConfigValue("dbpasswd");
            int rslt = dbUrl.lastIndexOf("/");
            DB_NAME = dbUrl.substring((rslt + 1));
            DB_USER = dbUser;
            DB_PASSWORD = dbPwd;*/
        } catch (Exception e) {
            System.out.println("err di getdatabasename : " + e.toString());
            e.printStackTrace();
        }
    }

    /* put data to temporary table */
    public static void putData(String where) {
        try {
            String sql = " INSERT INTO " + PstMaterialTemp.TBL_MATERIAL +
                    " SELECT * FROM " + PstMaterial.TBL_MATERIAL;
            if (where != null && where.length() > 0) {
                sql = sql + " WHERE " + where;
            }
            int insert = DBHandler.execUpdate(sql);

            sql = " INSERT INTO " + PstMemberRegTemp.TBL_CONTACT_LIST +
                    " SELECT * FROM " + PstMemberReg.TBL_CONTACT_LIST;
            if (where != null && where.length() > 0) {
                sql = sql + " WHERE " + where;
            }
            insert = DBHandler.execUpdate(sql);

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("err at put data : " + e.toString());
        }
    }

    /* put data to temporary table */
    //    public static void putData(Date dtFrom, Date dtTo){
    public void putData(Date dtFrom, Date dtTo) {
        try {
            String sql = " INSERT INTO " + PstMaterialTemp.TBL_MATERIAL +
                    " SELECT * FROM " + PstMaterial.TBL_MATERIAL;
            if (dtFrom != null && dtTo != null) {
                sql = sql + " WHERE " + PstMaterialTemp.fieldNames[PstMaterialTemp.FLD_LAST_UPDATE] +
                        " BETWEEN '" + Formater.formatDate(dtFrom, "yyyy-MM-dd") + "' " +
                        " AND '" + Formater.formatDate(dtTo, "yyyy-MM-dd") + "' ";
            }
            int insert = DBHandler.execUpdate(sql);

            sql = " INSERT INTO " + PstMemberRegTemp.TBL_CONTACT_LIST +
                    " SELECT * FROM " + PstMemberReg.TBL_CONTACT_LIST;
            if (dtFrom != null && dtTo != null) {
                sql = sql + " WHERE " + PstMemberRegTemp.fieldNames[PstMemberRegTemp.FLD_MEMBER_LAST_UPDATE] +
                        " BETWEEN '" + Formater.formatDate(dtFrom, "yyyy-MM-dd") + "' " +
                        " AND '" + Formater.formatDate(dtTo, "yyyy-MM-dd") + "' ";
            }
            insert = DBHandler.execUpdate(sql);
            this.setResultTransfer(SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("err at put data : " + e.toString());
            this.setResultTransfer(FAILED);
        }
    }

    /* put data to temporary table */
    //public static void deleteDataTemporary(){
    public void deleteDataTemporary() {
        try {
            String sql = " DELETE FROM " + PstMaterialTemp.TBL_MATERIAL;
            int delete = DBHandler.execUpdate(sql);
            sql = " DELETE FROM " + PstMemberRegTemp.TBL_CONTACT_LIST;
            delete = DBHandler.execUpdate(sql);
            this.setResultTransfer(SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("err at put data : " + e.toString());
            this.setResultTransfer(FAILED);
        }
    }

    public static String generateDeleteQuery(Vector listTempData) {
        String result = "";
        if (listTempData != null && listTempData.size() > 0) {
            result = " DELETE FROM " + PstMaterial.TBL_MATERIAL +
                    " WHERE " + PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_ID] +
                    " IN (";
            for (int i = 0; i < listTempData.size(); i++) {
                Material mat = (Material) listTempData.get(i);
                if (i != 0) {
                    result = result + " ," + mat.getOID();
                } else {
                    result = result + " " + mat.getOID();
                }
            }
            result = result + ");";
        }
        return result;
    }

    //    public static String generateDeleteQuery(Vector listTempData,Vector listMember){
    public String generateDeleteQuery(Vector listTempData, Vector listMember) {
        String result = "";
        try {
            if (listTempData != null && listTempData.size() > 0) {
                result = " DELETE FROM " + PstMaterial.TBL_MATERIAL +
                        " WHERE " + PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_ID] +
                        " IN (";
                for (int i = 0; i < listTempData.size(); i++) {
                    Material mat = (Material) listTempData.get(i);
                    if (i != 0) {
                        result = result + " ," + mat.getOID();
                    } else {
                        result = result + " " + mat.getOID();
                    }
                }
                result = result + ");";
            }
            if (listMember != null && listMember.size() > 0) {
                result = result + " DELETE FROM " + PstMemberReg.TBL_CONTACT_LIST +
                        " WHERE " + PstMemberReg.fieldNames[PstMemberReg.FLD_CONTACT_ID] +
                        " IN (";
                for (int i = 0; i < listMember.size(); i++) {
                    MemberReg mat = (MemberReg) listMember.get(i);
                    if (i != 0) {
                        result = result + " ," + mat.getOID();
                    } else {
                        result = result + " " + mat.getOID();
                    }
                }
                result = result + ");";
            }
            this.setResultTransfer(SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("err at delete query : " + e.toString());
            this.setResultTransfer(FAILED);
        }
        return result;
    }

    public int transferToClient(String where) {
        int transfer = 0;
        try {
            putData(where);
            where = where + " AND " + PstMaterial.fieldNames[PstMaterial.FLD_PROCESS_STATUS] +
                    " != " + PstMaterial.INSERT;
            Vector result = PstMaterialTemp.list(0, 0, where, "");
            Vector resultMember = PstMemberReg.list(0, 0, where, "");
            //String genDel = generateDeleteQuery(result);
            String genDel = generateDeleteQuery(result, resultMember);
            //System.out.println("genDel>>>>>>>>>>>> : "+genDel);
            execDump();
            createDelete(genDel);
            transfer = this.getResultTransfer();
            deleteDataTemporary();
        } catch (Exception e) {
            System.out.println("err transfer >>>>>>>>>>>> : " + e.toString());
        }
        return transfer;
    }

    //public static int transferToClient(Date dtFrom, Date dtTo){
    public int transferToClient(Date dtFrom, Date dtTo) {
        int transfer = 0;
        try {
            deleteDataTemporary();
            if (this.getResultTransfer() == SUCCESS) {
                putData(dtFrom, dtTo);
                if (this.getResultTransfer() == SUCCESS) {
                    String where = PstMaterialTemp.fieldNames[PstMaterialTemp.FLD_LAST_UPDATE] +
                            " BETWEEN '" + Formater.formatDate(dtFrom, "yyyy-MM-dd") + "' " +
                            " AND '" + Formater.formatDate(dtTo, "yyyy-MM-dd") + "'  AND " + PstMaterial.fieldNames[PstMaterial.FLD_PROCESS_STATUS] +
                            " != " + PstMaterial.INSERT;
                    Vector result = PstMaterialTemp.list(0, 0, where, "");

                    where = PstMemberRegTemp.fieldNames[PstMemberRegTemp.FLD_MEMBER_LAST_UPDATE] +
                            " BETWEEN '" + Formater.formatDate(dtFrom, "yyyy-MM-dd") + "' " +
                            " AND '" + Formater.formatDate(dtTo, "yyyy-MM-dd") + "'  AND " + PstMemberRegTemp.fieldNames[PstMemberRegTemp.FLD_PROCESS_STATUS] +
                            " != " + PstMemberRegTemp.INSERT;
                    Vector resultMember = PstMemberReg.list(0, 0, where, "");
                    //String genDel = generateDeleteQuery(result);
                    String genDel = generateDeleteQuery(result, resultMember);
                    //System.out.println("genDel>>>>>>>>>>>> : "+genDel);
                    if (this.getResultTransfer() == SUCCESS) {
                        execDump();
                        if (this.getResultTransfer() == SUCCESS) {
                            createDelete(genDel);
                        }
                        //transfer = 1;
                    }
                }
            }
            transfer = this.getResultTransfer();
        } catch (Exception e) {
            System.out.println("err transfer >>>>>>>>>>>> : " + e.toString());
        }
        return transfer;
    }

    public static int transferAllToClient(int indexTable) {
        int transfer = 0;
        try {
            execDumpAll(indexTable);
            transfer = 1;
        } catch (Exception e) {
            System.out.println("err transfer >>>>>>>>>>>> : " + e.toString());
        }
        return transfer;
    }

    //public static int transferAllToClient(int indexTable, int typeTransfer){
    public int transferAllToClient(int indexTable, int typeTransfer) {
        int transfer = 0;
        try {
            execDumpAll(indexTable, typeTransfer);
            //transfer = 1;
            transfer = this.getResultTransfer();

        } catch (Exception e) {
            System.out.println("err transfer >>>>>>>>>>>> : " + e.toString());
        }
        return transfer;
    }

    public static void execDump() {
        //String stCommand = ""+PATH_MYSQL_BIN+CMD_DUMP+" -u root -B "+DB_NAME+" --tables "+TABLE_NAME+" -r "+PATH_DATA_OUT+DUMP_NAME;
        String stCommand = "";
        if (TABLE_NAMES != null && TABLE_NAMES.length > 0) {

            // stCommand = stCommand + PATH_MYSQL_BIN + CMD_DUMP + " -u " + DB_USER + getPassword() + " --skip-comments --opt -t -B " + DB_NAME + " --tables ";//+TABLE_NAME+" -r "+PATH_DATA_OUT+DUMP_NAME;
            for (int i = 0; i < TABLE_NAMES.length; i++) {
                File file = new File(PATH_DATA_OUT + "cashier_"+TABLE_NAMES[i]+"_dump.sql");
                if (file.exists()) {
                    file.delete();
                }
                TransferData transferData = new TransferData();
                transferData.writeDataTable(PATH_DATA_OUT,TABLE_NAMES[i]);
                //stCommand = stCommand + " " + TABLE_NAMES[i];
            }
            /*stCommand = stCommand + " > " + PATH_DATA_OUT + DUMP_NAME;
            ExecCommand execCommand = new ExecCommand();
            execCommand.setPathFile(PATH_DATA_OUT + DUMP_NAME);
            int result = execCommand.runCommmand(stCommand);*/
        }
    }

    public static void execDumpAll(int indexTable) {
        //String stCommand = ""+PATH_MYSQL_BIN+CMD_DUMP+" -u root -B "+DB_NAME+" --tables "+TABLE_NAME+" -r "+PATH_DATA_OUT+DUMP_NAME;
        String stCommand = "";
        if (indexTable == -1) {
            File file = new File(PATH_DATA_OUT + ALL_DUMP_NAME);
            if (file.exists()) {
                file.delete();
            }
            //stCommand = stCommand + PATH_MYSQL_BIN+CMD_DUMP +" -u "+DB_USER+getPassword()+" -B "+DB_NAME+" -r "+PATH_DATA_OUT+ALL_DUMP_NAME;
            stCommand = stCommand + PATH_MYSQL_BIN + CMD_DUMP + " -u " + DB_USER + getPassword() + " " + PATH_DATA_OUT + ALL_DUMP_NAME;
            ExecCommand execCommand = new ExecCommand();
            execCommand.setPathFile(PATH_DATA_OUT + ALL_DUMP_NAME);
            int result = execCommand.runCommmand(stCommand);
        } else {
            if (TABLE_NAMES != null && TABLE_NAMES.length > 0) {
                //stCommand = stCommand + PATH_MYSQL_BIN+CMD_DUMP +" -u root -B "+DB_NAME+" --tables "+TABLE_NAMES[indexTable]+
                //" -r "+PATH_DATA_OUT+ALL_DUMP_NAME;
                File file = new File(PATH_DATA_OUT + "cashier_" + TABLE_NAMES[indexTable] + "_dump.sql");
                if (file.exists()) {
                    file.delete();
                }
                TransferData transferData = new TransferData();
                transferData.writeDataTable(PATH_DATA_OUT,TABLE_NAMES[indexTable]);
                /*
                stCommand = stCommand + PATH_MYSQL_BIN + CMD_DUMP + " -u " + DB_USER + getPassword() + " --disable-add-drop-table -B " + DB_NAME + " --tables " + TABLE_NAMES[indexTable] +
                        " " + PATH_DATA_OUT + "cashier_" + TABLE_NAMES[indexTable] + "_dump.sql";
                ExecCommand execCommand = new ExecCommand();
                execCommand.setPathFile(PATH_DATA_OUT + "cashier_" + TABLE_NAMES[indexTable] + "_dump.sql");
                int result = execCommand.runCommmand(stCommand);*/
            }
        }
    }


    //public static void execDumpAll(int indexTable, int typeTransfer){
    public void execDumpAll(int indexTable, int typeTransfer) {
        //String stCommand = ""+PATH_MYSQL_BIN+CMD_DUMP+" -u root -B "+DB_NAME+" --tables "+TABLE_NAME+" -r "+PATH_DATA_OUT+DUMP_NAME;
        String stCommand = "";
        switch (typeTransfer) {
            case SrcTransferData.TO_CASHIER_OUTLET:
                if (indexTable > -1) {
                    File file = new File(PATH_DATA_OUT + "cashier_" + TABLE_NAMES[indexTable] + "_dump.sql");
                    if (file.exists()) {
                        file.delete();
                    }
                    TransferData transferData = new TransferData(); 
                    transferData.writeDataTable(PATH_DATA_OUT,TABLE_NAMES[indexTable]);

                   /* if(OUT_TO_FILE.equals("Yes")){
                        stCommand = stCommand + PATH_MYSQL_BIN + CMD_DUMP + " -u " + DB_USER + getPassword() + " --skip-comments --opt -t --tables " + DB_NAME + " " + TABLE_NAMES[indexTable] +
                            " > " + PATH_DATA_OUT + "cashier_" + TABLE_NAMES[indexTable] + "_dump.sql";
                    } else {
                        stCommand = stCommand + PATH_MYSQL_BIN + CMD_DUMP + " -u " + DB_USER + getPassword() + " --skip-comments --opt -t --tables " + DB_NAME + " " + TABLE_NAMES[indexTable];
                    }

                    ExecCommand execCommand = new ExecCommand();
                    execCommand.setPathFile(PATH_DATA_OUT + "cashier_" + TABLE_NAMES[indexTable] + "_dump.sql");
                    int result = execCommand.runCommmand(stCommand);

                    if (result == 0) {
                        this.setResultTransfer(SUCCESS);
                    }*/
                    this.setResultTransfer(SUCCESS);
                } else {
                    Vector listDataToTransfer = listDataToTransfer(showTable(), SrcTransferData.TO_CASHIER_OUTLET);
                    if (listDataToTransfer != null && listDataToTransfer.size() > 0) {
                        for (int i = 0; i < listDataToTransfer.size(); i++) {
                            TransMasterdata objMaster = (TransMasterdata) listDataToTransfer.get(i);
                            
                            try{
                                FileOutputStream file1 = new FileOutputStream(PATH_DATA_OUT +"cashier_" + TABLE_NAMES[objMaster.getIndexDB()] + "_dump.sql");
                                DataOutputStream dout = new DataOutputStream(file1);
                                dout.writeChars("");
                            
                            }catch(Exception xx){
                                System.out.println("err xx "+xx.toString());
                            }    
                            File file = new File(PATH_DATA_OUT +"cashier_" + TABLE_NAMES[objMaster.getIndexDB()] + "_dump.sql");
                            
                            System.out.println("cek file : "+PATH_DATA_OUT +"cashier_" + TABLE_NAMES[objMaster.getIndexDB()] + "_dump.sql");
                            if (file.exists()) {
                                System.out.println("file already now delete file process ");    
                                
                                file.delete();
                                
                            }else{ 
                                try{
                                    File file2 = new File(PATH_DATA_OUT+"TXTLogger.class");    
                                    if (file.exists()) {
                                        System.out.println("file class exist: ");
                                    }else{
                                        file2.createNewFile();
                                    }
                                }catch(Exception e){
                                    System.out.println("Err create file : "+e.toString());
                                }
                            }
                            
                            TransferData transferData = new TransferData();
                            transferData.writeDataTable(PATH_DATA_OUT,TABLE_NAMES[objMaster.getIndexDB()]);
                            /*
                            if(OUT_TO_FILE.equals("Yes")){
                                stCommand = PATH_MYSQL_BIN + CMD_DUMP + " -u " + DB_USER + getPassword() + " --skip-comments --opt -t --tables " + DB_NAME + " " + TABLE_NAMES[objMaster.getIndexDB()] +
                                        " " + PATH_DATA_OUT + "cashier_" + TABLE_NAMES[objMaster.getIndexDB()] + "_dump.sql";
                            }else{
                                stCommand = PATH_MYSQL_BIN + CMD_DUMP + " -u " + DB_USER + getPassword() + " --skip-comments --opt -t --tables " + DB_NAME + " " + TABLE_NAMES[objMaster.getIndexDB()];
                            }
                            ExecCommand execCommand = new ExecCommand();
                            execCommand.setPathFile(PATH_DATA_OUT +"cashier_" + TABLE_NAMES[objMaster.getIndexDB()] + "_dump.sql");
                            int result = execCommand.runCommmand(stCommand);

                            if (result == 0) {
                                this.setResultTransfer(SUCCESS);
                            }*/
                            this.setResultTransfer(SUCCESS);
                        }
                    }
                }
                break;
            case SrcTransferData.CATALOG_TO_CASHIER:
                if (indexTable > -1) {
                    File file = new File(PATH_DATA_OUT + "cashier_" + TABLE_NAMES[indexTable] + "_dump.sql");
                    if (file.exists()) {
                        file.delete();
                    }
                    TransferData transferData = new TransferData();
                    transferData.writeDataTable(PATH_DATA_OUT,TABLE_NAMES[indexTable]);
                    /*
                    stCommand = stCommand + PATH_MYSQL_BIN + CMD_DUMP + " -u " + DB_USER + getPassword() + " --skip-comments --opt -t -B " + DB_NAME + " --tables " + TABLE_NAMES[indexTable] +
                            " > " + PATH_DATA_OUT + "cashier_" + TABLE_NAMES[indexTable] + "_dump.sql";
                    ExecCommand execCommand = new ExecCommand();
                    execCommand.setPathFile(PATH_DATA_OUT + "cashier_" + TABLE_NAMES[indexTable] + "_dump.sql");
                    int result = execCommand.runCommmand(stCommand);

                    if (result == 0) {
                        this.setResultTransfer(SUCCESS);
                    }*/
                    this.setResultTransfer(SUCCESS);
                } else {
                    Vector listDataToTransfer = listDataToTransfer(showTable(), SrcTransferData.CATALOG_TO_CASHIER);
                    if (listDataToTransfer != null && listDataToTransfer.size() > 0) {
                        for (int i = 0; i < listDataToTransfer.size(); i++) {
                            TransMasterdata objMaster = (TransMasterdata) listDataToTransfer.get(i);

                            File file = new File(PATH_DATA_OUT + "cashier_" + TABLE_NAMES[objMaster.getIndexDB()] + "_dump.sql");
                            if (file.exists()) {
                                file.delete();
                            }
                            TransferData transferData = new TransferData();
                            transferData.writeDataTable(PATH_DATA_OUT,TABLE_NAMES[objMaster.getIndexDB()]);
                            /*
                            stCommand = PATH_MYSQL_BIN + CMD_DUMP + " -u " + DB_USER + getPassword() + " --skip-comments --opt -t -B " + DB_NAME + " --tables " + TABLE_NAMES[objMaster.getIndexDB()] +
                                    " > " + PATH_DATA_OUT + "cashier_" + TABLE_NAMES[objMaster.getIndexDB()] + "_dump.sql";
                            ExecCommand execCommand = new ExecCommand();
                            execCommand.setPathFile(PATH_DATA_OUT + "cashier_" + TABLE_NAMES[objMaster.getIndexDB()] + "_dump.sql");
                            int result = execCommand.runCommmand(stCommand);
                            if (result == 0) {
                                this.setResultTransfer(SUCCESS);
                            }*/
                            this.setResultTransfer(SUCCESS);
                        }
                    }
                }
                break;
            case SrcTransferData.FROM_CASHIER_OUTLET:
                if (indexTable > -1) {
                    File file = new File(PATH_DATA_OUT + "cashier_" + TABLE_NAMES[indexTable] + "_dump.sql");
                    if (file.exists()) {
                        file.delete();
                    }
                    TransferData transferData = new TransferData();
                    transferData.writeDataTable(PATH_DATA_OUT,TABLE_NAMES[indexTable]);
                    /*
                    stCommand = stCommand + PATH_MYSQL_BIN + CMD_DUMP + " -u " + DB_USER + getPassword() + " --skip-comments --opt -t -B " + DB_NAME + " --tables " + TABLE_NAMES[indexTable] +
                            " > " + PATH_DATA_OUT + "cashier_" + TABLE_NAMES[indexTable] + "_dump.sql";
                    ExecCommand execCommand = new ExecCommand();
                    execCommand.setPathFile(PATH_DATA_OUT + "cashier_" + TABLE_NAMES[indexTable] + "_dump.sql");
                    int result = execCommand.runCommmand(stCommand);
                    if (result == 0) {
                        this.setResultTransfer(SUCCESS);
                    }*/
                    this.setResultTransfer(SUCCESS);
                } else {
                    Vector listDataToTransfer = listDataToTransfer(showTable(), SrcTransferData.FROM_CASHIER_OUTLET);
                    if (listDataToTransfer != null && listDataToTransfer.size() > 0) {
                        for (int i = 0; i < listDataToTransfer.size(); i++) {
                            TransMasterdata objMaster = (TransMasterdata) listDataToTransfer.get(i);
                            File file = new File(PATH_DATA_OUT + "cashier_" + TABLE_NAMES[objMaster.getIndexDB()] + "_dump.sql");
                            if (file.exists()) {
                                file.delete();
                            }
                            TransferData transferData = new TransferData();
                            transferData.writeDataTable(PATH_DATA_OUT,TABLE_NAMES[objMaster.getIndexDB()]);
                            /*
                            stCommand = PATH_MYSQL_BIN + CMD_DUMP + " -u " + DB_USER + getPassword() + " --skip-comments --opt -t -B " + DB_NAME + " --tables " + TABLE_NAMES[objMaster.getIndexDB()] +
                                    " > " + PATH_DATA_OUT + "cashier_" + TABLE_NAMES[objMaster.getIndexDB()] + "_dump.sql";
                            ExecCommand execCommand = new ExecCommand();
                            execCommand.setPathFile(PATH_DATA_OUT + "cashier_" + TABLE_NAMES[objMaster.getIndexDB()] + "_dump.sql");
                            int result = execCommand.runCommmand(stCommand);
                            if (result == 0) {
                                this.setResultTransfer(SUCCESS);
                            }*/
                            this.setResultTransfer(SUCCESS);
                        }
                    }
                }
                break;
            case SrcTransferData.ALL_DATA:
                if (indexTable == -1) {
                    File file = new File(PATH_DATA_OUT + ALL_DUMP_NAME);
                    if (file.exists()) {
                        file.delete();
                    }
                    //stCommand = stCommand + PATH_MYSQL_BIN+CMD_DUMP +" -u "+DB_USER+getPassword()+" -B "+DB_NAME+" -r "+PATH_DATA_OUT+ALL_DUMP_NAME;
                    stCommand = stCommand + PATH_MYSQL_BIN + CMD_DUMP + " -u " + DB_USER + getPassword() + " --skip-comments --opt -t " + DB_NAME + " -n -r > " + PATH_DATA_OUT + ALL_DUMP_NAME;
                    //stCommand = stCommand + PATH_MYSQL_BIN+CMD_DUMP +" -u "+DB_USER+getPassword()+" "+DB_NAME+" > "+PATH_DATA_OUT+ALL_DUMP_NAME;
                    ExecCommand execCommand = new ExecCommand();
                    int result = execCommand.runCommmand(stCommand);
                    if (result == 0) {
                        this.setResultTransfer(SUCCESS);
                    }
                }
                break;
        }

    }

    //    public static void createDelete(String sql){
    public void createDelete(String sql) {
        try {
            File file = new File(PATH_DATA_OUT + DUMP_DELETE_NAME);
            if (file.exists()) {
                file.delete();
            }
            FileOutputStream fileDelete = new FileOutputStream(PATH_DATA_OUT + DUMP_DELETE_NAME, true);
            fileDelete.write(sql.getBytes());
            fileDelete.close();
            this.setResultTransfer(SUCCESS);
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("err make file output >>>>>>>>>>>> : " + e.toString());
            this.setResultTransfer(FAILED);
        }
    }

    /* in client */
    public static void createBatch() {
        //        String sql = " "+PATH_MYSQL_BIN+CMD_UNDUMP+" -u "+DB_USER+getPassword()+" "+DB_NAME_UNDUMP+" < "+PATH_DATA_OUT+DUMP_DELETE_NAME+
        //        " \n "+PATH_MYSQL_BIN+CMD_UNDUMP+" -u "+DB_USER+getPassword()+" "+DB_NAME_UNDUMP+" < "+PATH_DATA_OUT+DUMP_NAME;
        String sql = " " + PATH_MYSQL_BIN + CMD_UNDUMP + " -u " + DB_USER + getPassword() + " " + DB_NAME_UNDUMP + " < " + PATH_DATA_IN + DUMP_DELETE_NAME +
                " \n " + PATH_MYSQL_BIN + CMD_UNDUMP + " -u " + DB_USER + getPassword() + " " + DB_NAME_UNDUMP + " < " + PATH_DATA_IN + DUMP_NAME;
        try {
            File file = new File(PATH_DATA_IN + FILE_BATCH);
            if (file.exists()) {
                file.delete();
            }
            FileOutputStream fileDelete = new FileOutputStream(PATH_DATA_IN + FILE_BATCH, true);
            fileDelete.write(sql.getBytes());
            fileDelete.close();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("err make file output >>>>>>>>>>>> : " + e.toString());
        }
    }

    /**
     * gadnyana
     * this function  for restore data sale transaction
     * from file
     */  
    public void execRestoreFile(){
        try{
            if (TABLE_NAMES != null && TABLE_NAMES.length > 0) {
                String sql = "";
                for (int i = 0; i < TABLE_NAMES.length; i++) {
                    File file = new File(PATH_DATA_IN + "cashier_" + TABLE_NAMES[i] + "_dump.sql");
                    //if (file.exists()) {
                        try{  
                            if(CONSTRUCT_FOR_WIN.equals("Yes")){
                                sql = "LOAD DATA INFILE '" + PATH_DATA_IN + "cashier_" + TABLE_NAMES[i] + "_dump.sql' IGNORE INTO TABLE "+TABLE_NAMES[i];
                            }else{
                                sql = "LOAD DATA INFILE '" + PATH_DATA_IN + "cashier_" + TABLE_NAMES[i] + "_dump.sql' IGNORE INTO TABLE "+TABLE_NAMES[i];
                            }
                            DBHandler.execUpdate(sql);
                        }catch(Exception ex){
                            System.out.println("Error load data : "+ex.toString());
                        }
		    // }else{ 
                    //    System.out.println("File not found :"+PATH_DATA_IN+"cashier_" + TABLE_NAMES[i] + "_dump.sql");
                    // }
                    System.out.println("table : "+TABLE_NAMES[i]);
                }
            }
            this.setResultTransfer(SUCCESS);
        } catch(Exception e) {
            System.out.println(e.toString());
            this.setResultTransfer(FAILED);
        }
    }

    //    public static void createAllBatch(){
    public void createAllBatch() {
        //String sql = " "+PATH_MYSQL_BIN+CMD_UNDUMP+" -u root "+DB_NAME_UNDUMP+" < "+PATH_DATA_OUT+ALL_DUMP_NAME;
        String sql = "#!/bin/sh\n\n";
        //File fileAll = new File(PATH_DATA_OUT+ALL_DUMP_NAME);
        //File fileAll = new File(PATH_DATA_IN+ALL_DUMP_NAME);
        //if(fileAll.exists()){
        //    sql = sql + PATH_MYSQL_BIN+CMD_UNDUMP+" -u "+DB_USER+getPassword()+" "+DB_NAME_UNDUMP+" < "+PATH_DATA_IN+ALL_DUMP_NAME;
        //}
        if (TABLE_NAMES != null && TABLE_NAMES.length > 0) {
            for (int i = 0; i < TABLE_NAMES.length; i++) {
                File file = new File(PATH_DATA_IN + "cashier_" + TABLE_NAMES[i] + "_dump.sql");
                if (file.exists()) {
                    //file.delete();
                    if(CONSTRUCT_FOR_WIN.equals("Yes")){
                        if (i == 0) {
                            if (file.exists()) {
                                sql = sql + "\n " + PATH_MYSQL_BIN + CMD_UNDUMP + " -u " + DB_USER + getPassword() + " -f " + DB_NAME_UNDUMP + " < " + PATH_DATA_IN + "cashier_" + TABLE_NAMES[i] + "_dump.sql";
                            } else {
                                sql = sql + PATH_MYSQL_BIN + CMD_UNDUMP + " -u " + DB_USER + getPassword() + " -f " + DB_NAME_UNDUMP + " < " + PATH_DATA_IN + "cashier_" + TABLE_NAMES[i] + "_dump.sql";
                            }
                        } else {
                            sql = sql + "\n " + PATH_MYSQL_BIN + CMD_UNDUMP + " -u " + DB_USER + getPassword() + " -f " + DB_NAME_UNDUMP + " < " + PATH_DATA_IN + "cashier_" + TABLE_NAMES[i] + "_dump.sql";
                        }
                    }else{
                        if (i == 0) {
                            if (file.exists()) {
                                sql = sql + "\n " + CMD_UNDUMP + " -u " + DB_USER + getPassword() + " -f " + DB_NAME_UNDUMP + " < " + PATH_DATA_IN + "cashier_" + TABLE_NAMES[i] + "_dump.sql";
                            } else {
                                sql = sql + CMD_UNDUMP + " -u " + DB_USER + getPassword() + " -f " + DB_NAME_UNDUMP + " < " + PATH_DATA_IN + "cashier_" + TABLE_NAMES[i] + "_dump.sql";
                            }
                        } else {
                            sql = sql + "\n " + CMD_UNDUMP + " -u " + DB_USER + getPassword() + " -f " + DB_NAME_UNDUMP + " < " + PATH_DATA_IN + "cashier_" + TABLE_NAMES[i] + "_dump.sql";
                        }
                    }
                }
            }
        }

        try {
            if (sql != null && sql.length() > 0) {
                File file = null;
                if(CONSTRUCT_FOR_WIN.equals("Yes")){
                    file = new File(PATH_DATA_IN + FILE_BATCH);
                }else{
                    file = new File(FILE_BATCH);
                }
                if (file.exists()) {
                    file.delete();
                }

                FileOutputStream fileDelete = null;
                if(CONSTRUCT_FOR_WIN.equals("Yes")){
                    fileDelete = new FileOutputStream(PATH_DATA_IN + FILE_BATCH, true);
                }else{
                    fileDelete = new FileOutputStream(FILE_BATCH, true);
                }
                fileDelete.write(sql.getBytes());
                fileDelete.close();
                // execute chmnod oug+x to make the batch/shell file executeable on Linux
                this.setResultTransfer(SUCCESS);
            } else {
                this.setResultTransfer(FAILED);
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("err make file output >>>>>>>>>>>> : " + e.toString());
            this.setResultTransfer(FAILED);
        }
    }

    public void excBatch() {
        //String stCommand = PATH_DATA_OUT+FILE_BATCH;
        String stCommand = PATH_DATA_IN + FILE_BATCH;
        ExecCommand execCommand = new ExecCommand();
        try {
            int result = 0; 
            if(CONSTRUCT_FOR_WIN.equals("Yes")){ 
                System.out.println("cd windows "+PATH_DATA_IN);
                result = execCommand.runCommmand(stCommand);
            }else{ 
                //execCommand.runCommmand("cd "+PATH_DATA_IN);
                 
                //System.out.println("cd "+PATH_DATA_IN);
                System.out.println("chmod +x "+PATH_DATA_IN + FILE_BATCH);
                System.out.println("./"+PATH_DATA_IN + FILE_BATCH);
                
                // setting for colonias   
                //execCommand.runCommmand("pwd ");
                //execCommand.runCommmand("cd "+PATH_DATA_IN);
                execCommand.runCommmand("chmod +x "+PATH_DATA_IN + FILE_BATCH);  
                result = execCommand.runCommmand("."+PATH_DATA_IN + FILE_BATCH);
            }
            if (result == 0) {
                this.setResultTransfer(SUCCESS);
            } else {
                this.setResultTransfer(FAILED);
            }
        } catch (Exception e) {
            this.setResultTransfer(FAILED);
            System.out.println("err on exBatch: " + e.toString());
        }
    }

    public void getData() {
        try {
            String sql = " INSERT INTO " + PstMaterial.TBL_MATERIAL +
                    " SELECT * FROM " + PstMaterialTemp.TBL_MATERIAL;

            int insert = DBHandler.execUpdate(sql);

            sql = " INSERT INTO " + PstMemberReg.TBL_CONTACT_LIST +
                    " SELECT * FROM " + PstMemberRegTemp.TBL_CONTACT_LIST;

            insert = DBHandler.execUpdate(sql);
            this.setResultTransfer(SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("err at get data : " + e.toString());
            this.setResultTransfer(FAILED);
        }
    }

    /** edited by wpulantara
     *  hadling error
     */
    public int restoreInClient() {
        int result = 0;
        try {
            deleteDataTemporary();
            if (this.getResultTransfer() == SUCCESS) {
                //createBatch();
                if (this.getResultTransfer() == SUCCESS) {
                    excBatch();
                    if (this.getResultTransfer() == SUCCESS) {
                        getData();
                    }
                }
            }
            result = this.getResultTransfer();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("err make restore in client >>>>>>>>>>>> : " + e.toString());
        }
        return result;
    }

    public synchronized void run(){
        try {
            //execRestoreFile();
            // createAllBatch();
            //if (this.getResultTransfer() == SUCCESS) {
            //    excBatch();
            //}
        } catch (Exception e) {
            this.setResultTransfer(FAILED);
            e.printStackTrace();
            System.out.println("err make restore in client >>>>>>>>>>>> : " + e.toString());
        }
        this.getResultTransfer();
    }

    /**
     * proses restore penjualan
     * dengan thread
     */
    /*public int restoreAllInClient() {
        try{
            TransMasterdata transMasterdata = new TransMasterdata();
            Thread thread = new Thread(transMasterdata);
            thread.start();
        }catch(Exception e){}
        return IN_PROCESS;
    }*/

    //    public static int restoreAllInClient(){
    public int restoreAllInClient() {
       int result = 0;
        try {
            //excBatch();
            execRestoreFile();
        } catch (Exception e) {
            this.setResultTransfer(FAILED);
            e.printStackTrace();
            System.out.println("err make restore in client >>>>>>>>>>>> : " + e.toString());
        }
        result = this.getResultTransfer();
        return result;
    }

    /* to show table in dbname */
    public static String[] showTable() {
        getDatabaseName();
        DBResultSet dbrs = null;
        String[] result = {""};
        try {
            String sql = " use " + DB_NAME;
            int exc = DBHandler.execUpdate(sql);
            sql = " show tables ";
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            int i = 1;
            String str = "";
            while (rs.next()) {
                if (i == 1) {
                    str = str + rs.getString(1);
                } else {
                    str = str + ":" + rs.getString(1);
                }
                i++;
            }

            result = str.split(":");

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("err in show table : " + e.toString());
        } finally {
            DBResultSet.close(dbrs);

        }
        return result;

    }


    public static Vector listDataToTransfer(String[] strDb, int choice) {
        Vector result = new Vector();
        TransMasterdata trans = null;
        if (strDb != null && strDb.length > 0) {
            if (choice == SrcTransferData.FROM_CASHIER_OUTLET) {
                for (int i = 0; i < strDb.length; i++) {
                    if (strDb[i].toUpperCase().equals(PstBillMain.TBL_CASH_BILL_MAIN.toUpperCase())) {
                        trans = new TransMasterdata(i, "KASIR MAIN");
                        result.add(trans);
                    } else if (strDb[i].toUpperCase().equals(PstBillDetail.TBL_CASH_BILL_DETAIL.toUpperCase())) {
                        trans = new TransMasterdata(i, "KASIR DETAIL");
                        result.add(trans);
                    } else if (strDb[i].toUpperCase().equals(PstCashPayment.TBL_PAYMENT.toUpperCase())) {
                        trans = new TransMasterdata(i, "PEMBAYARAN KAS");
                        result.add(trans);
                    } else if (strDb[i].toUpperCase().equals(PstCashCreditCard.TBL_CREDIT_CARD.toUpperCase())) {
                        trans = new TransMasterdata(i, "PEMBAYARAN KAS INFO");
                        result.add(trans);
                    } else if (strDb[i].toUpperCase().equals(PstCreditPaymentMain.TBL_CASH_CREDIT_PAYMENT_MAIN.toUpperCase())) {
                        trans = new TransMasterdata(i, "PEMBAYARAN KREDIT MAIN");
                        result.add(trans);
                    } else if (strDb[i].toUpperCase().equals(PstCashCreditPayment.TBL_PAYMENT.toUpperCase())) {
                        trans = new TransMasterdata(i, "PEMBAYARAN KREDIT DETAIL");
                        result.add(trans);
                    } else if (strDb[i].toUpperCase().equals(PstCashCreditPaymentInfo.TBL_CREDIT_PAYMENT_INFO.toUpperCase())) {
                        trans = new TransMasterdata(i, "PEMBAYARAN KREDIT INFO");
                        result.add(trans);
                    } else if (strDb[i].toUpperCase().equals(PstPendingOrder.TBL_CASH_PENDING_ORDER.toUpperCase())) {
                        trans = new TransMasterdata(i, "PENDING ORDER");
                        result.add(trans);
                    } else if (strDb[i].toUpperCase().equals(PstMemberPoin.TBL_POS_MEMBER_POIN.toUpperCase())) {
                        trans = new TransMasterdata(i, "KREDIT MEMBER POIN");
                        result.add(trans);
                    } else if (strDb[i].toUpperCase().equals(PstBalance.TBL_BALANCE.toUpperCase())) {
                        trans = new TransMasterdata(i, "KASIR BALANCE");
                        result.add(trans);
                    } else if (strDb[i].toUpperCase().equals(PstCashCashier.TBL_CASH_CASHIER.toUpperCase())) {
                        trans = new TransMasterdata(i, "SESI KASIR");
                        result.add(trans);
                    }
                }
            } else if (choice == SrcTransferData.TO_CASHIER_OUTLET) {
                for (int i = 0; i < strDb.length; i++) {
                    if (strDb[i].toUpperCase().equals(PstContactClass.TBL_CONTACT_CLASS.toUpperCase())) {
                        trans = new TransMasterdata(i, "TIPE KONTAK");
                        result.add(trans);
                    } else if (strDb[i].toUpperCase().equals(PstContactClassAssign.TBL_CNT_CLS_ASSIGN.toUpperCase())) {
                        trans = new TransMasterdata(i, "TIPE KONTAK DETAIL");
                        result.add(trans);
                    } else if (strDb[i].toUpperCase().equals(PstLocation.TBL_P2_LOCATION.toUpperCase())) {
                        trans = new TransMasterdata(i, "LOKASI");
                        result.add(trans);
                    } else if (strDb[i].toUpperCase().equals(PstMemberGroup.TBL_MEMBER_GROUP.toUpperCase())) {
                        trans = new TransMasterdata(i, "MEMBER TIPE");
                        result.add(trans);
                    } else if (strDb[i].toUpperCase().equals(PstMemberRegistrationHistory.TBL_MEMBER_REGISTRATION_HISTORY.toUpperCase())) {
                        trans = new TransMasterdata(i, "MEMBER HISTORY");
                        result.add(trans);
                    } else if (strDb[i].toUpperCase().equals(PstCurrencyType.TBL_POS_CURRENCY_TYPE.toUpperCase())) {
                        trans = new TransMasterdata(i, "TIPE CURRENCY");
                        result.add(trans);
                    } else if (strDb[i].toUpperCase().equals(PstDailyRate.TBL_POS_DAILY_RATE.toUpperCase())) {
                        trans = new TransMasterdata(i, "NILAI TUKAR HARIAN");
                        result.add(trans);
                    } else if (strDb[i].toUpperCase().equals(PstStandartRate.TBL_POS_STANDART_RATE.toUpperCase())) {
                        trans = new TransMasterdata(i, "NILAI TUKAR STANDAR");
                        result.add(trans);
                    } else if (strDb[i].toUpperCase().equals(com.dimata.common.entity.payment.PstDiscountType.TBL_POS_DISCOUNT_TYPE.toUpperCase())) {
                        trans = new TransMasterdata(i, "DISKON TIPE");
                        result.add(trans);
                    } else if (strDb[i].toUpperCase().equals(PstDiscountMapping.TBL_POS_DISCOUNT_MAPPING.toUpperCase())) {
                        trans = new TransMasterdata(i, "DISKON MAP");
                        result.add(trans);
                    } else if (strDb[i].toUpperCase().equals(PstPersonalDiscount.TBL_POS_PERSONAL_DISC.toUpperCase())) {
                        trans = new TransMasterdata(i, "DISKON PERSONAL");
                        result.add(trans);
                    } else if (strDb[i].toUpperCase().equals(com.dimata.common.entity.payment.PstPriceType.TBL_POS_PRICE_TYPE.toUpperCase())) {
                        trans = new TransMasterdata(i, "TIPE HARGA");
                        result.add(trans);
                    } else if (strDb[i].toUpperCase().equals(PstPriceTypeMapping.TBL_POS_PRICE_TYPE_MAPPING.toUpperCase())) {
                        trans = new TransMasterdata(i, "TIPE HARGA MAP");
                        result.add(trans);
                    } else if (strDb[i].toUpperCase().equals(PstMaterialStockCode.TBL_POS_MATERIAL_STOCK_CODE.toUpperCase())) {
                        trans = new TransMasterdata(i, "SERIAL NUMBER");
                        result.add(trans);
                    } else if (strDb[i].toUpperCase().equals(PstMaterial.TBL_MATERIAL.toUpperCase())) {
                        trans = new TransMasterdata(i, "KATALOG");
                        result.add(trans);
                    } else if (strDb[i].toUpperCase().equals(PstSales.TBL_SALES.toUpperCase())) {
                        trans = new TransMasterdata(i, "SALES");
                        result.add(trans);
                    } else if (strDb[i].toUpperCase().equals(PstCategory.TBL_CATEGORY.toUpperCase())) {
                        trans = new TransMasterdata(i, "KATEGORI");
                        result.add(trans);
                    } else if (strDb[i].toUpperCase().equals(PstMerk.TBL_MAT_MERK.toUpperCase())) {
                        trans = new TransMasterdata(i, "MERK");
                        result.add(trans);
                    } else if (strDb[i].toUpperCase().equals(PstMemberReg.TBL_CONTACT_LIST.toUpperCase())) {
                        trans = new TransMasterdata(i, "KONTAK");
                        result.add(trans);
                    }
                }
            } else if (choice == SrcTransferData.CATALOG_TO_CASHIER) {
                for (int i = 0; i < strDb.length; i++) {
                    if (strDb[i].toUpperCase().equals(PstMaterial.TBL_MATERIAL.toUpperCase())) {
                        trans = new TransMasterdata(i, "KATALOG");
                        result.add(trans);
                    } else if (strDb[i].toUpperCase().equals(PstMemberReg.TBL_CONTACT_LIST.toUpperCase())) {
                        trans = new TransMasterdata(i, "KONTAK");
                        result.add(trans);
                    }
                }
            } else {
                result = new Vector();
            }
        }
        return result;
    }

    /**
     * @@param args the command line arguments
     */
    public static void main(String[] args) {

        String[] strDb = showTable();


        for (int i = 0; i < strDb.length; i++) {
            System.out.println("string>>>>>>>>>> " + i + " : " + strDb[i]);
        }

    }

    /** Getter for property indexDB.
     * @@return Value of property indexDB.
     *
     */
    public int getIndexDB() {
        return this.indexDB;
    }

    /** Setter for property indexDB.
     * @@param indexDB New value of property indexDB.
     *
     */
    public void setIndexDB(int indexDB) {
        this.indexDB = indexDB;
    }

    /** Getter for property nameDB.
     * @@return Value of property nameDB.
     *
     */
    public String getNameDB() {
        return this.nameDB;
    }

    /** Setter for property nameDB.
     * @@param nameDB New value of property nameDB.
     *
     */
    public void setNameDB(String nameDB) {
        this.nameDB = nameDB;
    }

    /** Getter for property listDB.
     * @@return Value of property listDB.
     *
     */
    public Vector getListDB() {
        return this.listDB;
    }

    /** Setter for property listDB.
     * @@param listDB New value of property listDB.
     *
     */
    public void setListDB(Vector listDB) {
        this.listDB = listDB;
    }

    /** Getter for property resultTransfer.
     * @@return Value of property resultTransfer.
     *
     */
    public int getResultTransfer() {
        return this.resultTransfer;
    }

    /** Setter for property resultTransfer.
     * @@param resultTransfer New value of property resultTransfer.
     *
     */
    public void setResultTransfer(int resultTransfer) {
        this.resultTransfer = resultTransfer;
    }

}