package com.dimata.qdep.db;




import java.io.*;

import java.sql.PreparedStatement;

import java.sql.Connection;

import java.sql.Date;

import java.sql.ResultSet;

import java.sql.SQLException;

import java.sql.Statement;

import java.text.DateFormat;

import java.text.DecimalFormat;

import java.text.Format;

import java.text.NumberFormat;

import java.text.ParseException;

import java.text.SimpleDateFormat;

import java.util.Vector;

import java.lang.reflect.*;

import java.util.*;




public class DBHandler implements I_DBType

{

    

    

    //public static String CONFIG_FILE = "/home/config.xml";

    public static String CONFIG_FILE = com.dimata.RootClass.getThisClassPath()+System.getProperty("file.separator") + "pos.xml";//System.getProperty("java.home") + System.getProperty("file.separator") + "dimata" +System.getProperty("file.separator") + "hanoman.xml";

    private static final int DELETE_RESTRICTED  = 0; // RESTRICTED,

    private static final int DELETE_CASCADE     = 1; // CASCADE, 

    private static final boolean isUseForOnline = false;

    public static String DATE_FORMAT = "dd.MM.yyyy";

    

    public static char STR_DELIMITER = '\'';

    public static char DECIMAL_POINT = '.';

    

    public static final int DBSVR_MYSQL = 0;

    public static final int DBSVR_POSTGRESQL = 1;

    public static final int DBSVR_SYBASE = 2;

    public static final int DBSVR_ORACLE = 3;

    public static final int DBSVR_MSSQL = 4;



    public static final int DBSVR_TYPE  = DBSVR_MYSQL;  // this is a DB Handler for MySQL

    

    protected static String connLog  = "tmsconn.log";

    protected static String dbDriver = "com.microsoft.jdbc.sqlserver.SQLServerDriver";

    //protected static String dbDriver = "sun.jdbc.odbc.JdbcOdbcDriver";

    protected static String dbUrl 	 = "jdbc:microsoft:sqlserver://192.168.0.5:1433;databasename=ZIPREALTY;";

    //protected static String dbUrl 	 = "jdbc:odbc:odbcZipRealty";

    protected static String dbUser 	 = "sa";

    protected static String dbPwd 	 = "aa";

    

    protected static String dateFormat 		= "dd.MM.yyyy";

    protected static String decimalFormat	= "#,##0.##";

    protected static String currencyFormat	= "#,##0.00 '\u20AC'";

    protected static String dbSQLDecimalFormat 	= "#.#########";//"#.##";

    protected static String dbSQLQuoteString 	= "'";

    protected static String dbSQLIntegerFormat 	= "#";

    protected static int dbMinConn	= 2;

    protected static int dbMaxConn 	= 5;

    

    public static boolean configLoaded = false;

    public static DBConfigReader cnfReader;// = new DBConfigReader(CONFIG_FILE);

    

    

    private String tableName;

    private String pstName;

    private String[] fieldNames;

    private int[] fieldTypes;

    

    protected Vector fieldValues;

    protected boolean hasData;

    protected boolean recordModified;

    

    protected int[] keyIndex;

    protected int[] keyValues;

    protected int keyCount;

    protected int idIndex;

    protected int timestampIndex;

    

    

    //private static DBLogger dbLog = new DBLogger("");

    protected static DBConnectionBroker connPool = null;

    private static OIDFactory oidFactory = new OIDFactory();

    

    

    public DBHandler() {

    }

    

    

    public DBHandler(I_DBInterface idb)

    throws DBException {

        tableName = idb.getTableName();

        fieldNames = idb.getFieldNames();

        fieldTypes = idb.getFieldTypes();

        pstName  = idb.getPersistentName();

        

        hasData = false;

        recordModified = false;

        keyCount = 0;

        idIndex  = -1;

        timestampIndex = -1;

        fieldValues = new Vector();

        

        //getLog();
        if(isUseForOnline) {
            loadConfigOnline();
        } else {
            loadConfig();
        }

        

        fieldValues.setSize(idb.getFieldSize());

        

        int numbOfFields = idb.getFieldSize();

        for(int j = 0; j < numbOfFields; j++) {

            if(isPrimaryKey(j)) {

                keyCount++;

            } if(getFieldType(j) == 4) {

                timestampIndex = j;

            }

        }

        

        

        keyValues = new int[keyCount];

        keyIndex = new int[keyCount];

        

        int k = 0;

        int l = 0;



        for(; k < numbOfFields; k++) {

            if(isPrimaryKey(k)) {

                keyIndex[l++] = k;

            }

        }

        

        

        int i1 = 0;

        for(int j1 = 0; j1 < numbOfFields; j1++) {

            if(isIdentity(j1)) {

                i1++;

                idIndex = j1;

            }

        }

        

        if(i1 > 1) {

            throw new DBException(this, DBException.MULTIPLE_ID);

        } else {

            return;

        }

        

    } // end of Constructor DBHandler

    

    

    protected static void getLog() {

        //dbLog = new DBLogger((String)cnfReader.getConfigValue("logconn"));

        //dbLog = DBLogger.getLogger();

    }

    

    

    protected static void loadConfig()

    throws DBException {        
        if(CONFIG_FILE==null || CONFIG_FILE.length()<5 ){
            configLoaded=false;
            return;
        }
        if(!configLoaded) {            
            //getLog();
            try {
                System.out.println( "CONFIG_FILE "+CONFIG_FILE);

                DBConfigReader configReader = new DBConfigReader(CONFIG_FILE);

                dbDriver = configReader.getConfigValue("dbdriver");

                dbUrl = configReader.getConfigValue("dburl");

                dbUser = configReader.getConfigValue("dbuser");

                dbPwd = configReader.getConfigValue("dbpasswd");

                



                /*

                dbDriver = "com.mysql.jdbc.Driver";//configReader.getConfigValue("dbdriver");

                dbUrl = "jdbc:mysql://localhost:3306;databasename=hanoman_poppies";//configReader.getConfigValue("dburl");

                dbUser = "root";//configReader.getConfigValue("dbuser");

                dbPwd = "";//configReader.getConfigValue("dbpasswd");

                 */

                

                

                // Set the minimum and maximum connection

                String configValue = configReader.getConfigValue("dbminconn");

                if(configValue != null && !configValue.equals("")) {

                    dbMinConn = (new Integer(configValue)).intValue();

                }

                

                configValue = configReader.getConfigValue("dbmaxconn");

                if(configValue != null && !configValue.equals("")) {

                    dbMaxConn = (new Integer(configValue)).intValue();

                }

                

                // Set log file for connection

                configValue = configReader.getConfigValue("logconn");

                if(configValue != null && !configValue.equals("")) {

                    connLog = configValue;

                }

                

                // Set format for: date, decimal and currency

                configValue = configReader.getConfigValue("fordate");

                if(configValue != null && !configValue.equals("")) {

                    dateFormat = configValue;

                }

                

                configValue = configReader.getConfigValue("fordecimal");

                if(configValue != null && !configValue.equals("")) {

                    decimalFormat = configValue;

                }

                

                

                configValue = configReader.getConfigValue("forcurrency");

                if(configValue != null && !configValue.equals("")) {

                    currencyFormat = configValue;

                }

                

                

                

                configLoaded = true;

                System.out.println("/*********************************************************/");

                System.out.println("                     LOAD DBCONFIG QDEP            ");

                System.out.println("");

                System.out.println("\t dbDriver : " + dbDriver );

                System.out.println("\t dbUrl    : " + dbUrl);

                System.out.println("\t dbUser   : " + dbUser);

                System.out.println("\t dbPwd    : *************" );

                System.out.println("/********************************************************/");

                

            }

            catch(Exception exception) {

                exception.printStackTrace(System.err);

                throw new DBException(null, DBException.CONFIG_ERROR);

            }

        }

    }

    

    protected static void loadConfigOnline() throws DBException {
        if (!configLoaded) {
            try {
                dbDriver = "com.mysql.jdbc.Driver";
                dbUrl = "jdbc:mysql://localhost:3306/hanoman";
                dbUser = "dbadmin";
                dbPwd = "dsj12345";
                configLoaded = true;
                
                System.out.println("/*********************************************************/");
                System.out.println("                    LOAD DBCONFIG QDEP");
                System.out.println("");
                System.out.println("\t dbDriver : " + dbDriver);
                System.out.println("\t dbUrl    : " + dbUrl);
                System.out.println("\t dbUser   : " + dbUser);
                System.out.println("\t dbPwd    : *************");
                System.out.println("/********************************************************/");

            } catch (Exception exception) {
                exception.printStackTrace(System.err);
                throw new DBException(null, DBException.CONFIG_ERROR);
            }
        }
    }

    

    protected boolean checkConcurrency(Connection connection)

    throws DBException {

        if(timestampIndex < 0)

            return true;

        

        boolean flag = false;

        Connection connection1 = connection;

        Statement statement = null;

        try {

            if(connection1 == null)

                connection1 = getConnection();

            statement = getStatement(connection1);

            String s = "";

            ResultSet resultset = statement.executeQuery(getTimestampSQL());

            //dbLog.info("Execute SQL: " + getTimestampSQL());

            if(hasData = resultset.next()) {

                String s1 = resultset.getString(1);

                flag = s1.equals(getString(timestampIndex));

            }

            resultset.close();

            //dbLog.info("Closed ResultSet.");

            if(!flag)

                throw new DBException(this, DBException.CONCURRENCY_VIOLATION);

        }

        catch(SQLException sqlexception) {

            sqlexception.printStackTrace(System.err);

            throw new DBException(this, sqlexception);

        }

        finally {

            closeStatement(statement);

            if(connection == null)

                closeConnection(connection1);

        }

        return flag;

    }

    

    

    protected int checkFieldIndex(int i)

    throws DBException {

        if(i < 0 || i >= fieldNames.length)

            throw new DBException(this, DBException.INDEX_OUT_OF_RANGE);

        else

            return i;

    }

    

    protected static void closeConnection(Connection connection)

    throws DBException {

        try {

            if(connection != null) {

                int i = connPool.idOfConnection(connection);

                connPool.freeConnection(connection);

                //dbLog.info("Released connection no. " + i);

            }

        }

        catch(Exception exception) {

            exception.printStackTrace(System.err);

            throw new DBException(null, DBException.UNKNOWN);

        }

    }

    

    protected static void closeStatement(Statement statement)

    throws DBException {

        getLog();

        try {

            if(statement != null) {

                statement.close();

                //dbLog.info("Closed a statement: " + statement.toString());

            }

        }

        catch(SQLException sqlexception) {

            sqlexception.printStackTrace(System.err);

            throw new DBException(null, sqlexception);

        }

    }

    

    public void delete()

    throws DBException {

        if(!hasData)

            throw new DBException(this, DBException.NOT_OPEN);

        

        Connection connection = null;

        Statement statement = null;

        try {

            connection = getConnection();

            checkConcurrency(connection); 

            statement = getStatement(connection);

            //dbLog.info("Execute updateSQL: " + getDeleteSQL());

            //System.out.println("delete sql : " + getDeleteSQL());

            statement.executeUpdate(getDeleteSQL());

        }

        catch(SQLException sqlexception) {

            sqlexception.printStackTrace(System.err);

            throw new DBException(this, sqlexception);

        }

        finally {

            closeStatement(statement);

            closeConnection(connection);

        }

    }

    

    protected void deleteRecords(int i, String s)

    throws DBException {

        if(s.equals(""))

            return;

        Connection connection = null;

        Statement statement = null;

        try {

            connection = getConnection();

            statement = getStatement(connection);

            String s1 = "DELETE FROM " + tableName + " WHERE " + s;

            statement.executeUpdate(s1);

            //dbLog.info(s1);

        }

        catch(SQLException sqlexception) {

            sqlexception.printStackTrace(System.err);

            throw new DBException(null, sqlexception);

        }

        finally {

            closeStatement(statement);

            closeConnection(connection);

        }

    }

    

    public static void destroy() {

        if(connPool != null)

            connPool.destroy();

    }

    

    

    

    

    

    

    public static String formatCurrency(double d) {

        getLog();

        try {
            if(isUseForOnline) {
                loadConfigOnline();
            } else {
                loadConfig();
            }

            DecimalFormat decimalformat = new DecimalFormat(currencyFormat);

            return decimalformat.format(d);

        }

        catch(Exception _ex) {

            //dbLog.warning("Floating point conversion error");

        }

        return "0";

    }

    

    protected Vector getAllRecords(int i)

    throws DBException {
        if(isUseForOnline) {
            loadConfigOnline();
        } else {
            loadConfig();
        }

        return getDetailsInt("", fieldNames[i]);

    }

    

    public Boolean getBoolean(int i)

    throws DBException {

        return new Boolean(getInt(i) != 0);

    }

    



    public boolean getboolean(int i)

    throws DBException {

        Boolean bVal = new Boolean(getInt(i) != 0);

        return bVal.booleanValue();

    }



    

    protected static Connection getConnection()

    throws DBException {

        getLog();

        getConnectionPool();

        //dbLog.trace("Intializing connect sequence...");

        try {

            //dbLog.trace("Trying to get a connection...");

            Connection connection = connPool.getConnection();

            //dbLog.info("Connected to database: " + dbUrl + " using connection no. " + connPool.idOfConnection(connection));

            return connection;

        }

        catch(Exception exception) {

            exception.printStackTrace(System.err);

        }

        throw new DBException(null, DBException.NO_CONNECTION);

    }

    

    protected static void getConnectionPool()

    throws DBException {
        if(isUseForOnline) {
            loadConfigOnline();
        } else {
            loadConfig();
        }

        if(connPool == null)

            try {

                connPool = new DBConnectionBroker(dbDriver, dbUrl, dbUser, dbPwd, dbMinConn, dbMaxConn + 1, connLog, 0.01D);

            }

            catch(Exception _ex) {
                System.out.println(_ex);
                throw new DBException(null, DBException.DRIVER_NOT_FOUND);

            }

    }

    

    protected void getData(ResultSet resultset)

    throws DBException {

        try {

            for(int i = 0; i < fieldValues.size(); i++) {

                if(getFieldType(i) == 4)

                    fieldValues.set(i, resultset.getString(fieldNames[i]));

                else

                    fieldValues.set(i, resultset.getObject(fieldNames[i]));

            }

            

        }

        catch(SQLException sqlexception) {

            sqlexception.printStackTrace(System.err);

            throw new DBException(this, sqlexception);

        }

    }

    

    public java.util.Date getDate(int i)

    throws DBException {

        if(getFieldType(checkFieldIndex(i)) != 3 && getFieldType(checkFieldIndex(i)) != 4)

            throw new DBException(this, DBException.INVALID_DATE);

        else

            return (java.util.Date)fieldValues.get(i);

    }

    

    protected String getDeleteSQL() {

        return "DELETE FROM " + tableName + " WHERE " + getKeyCondition();

    }

    

    

    protected Vector getDetailRecords(int j, int k)

    throws DBException {

        return getDetailsInt(fieldNames[k] + " = " + getSQLValue(j), "");

    }

    

    protected  Vector getDetailsInt(String s, String s1)

    throws DBException {

        String sql = "SELECT * FROM " + tableName + (s != "" ? " WHERE " + s : "") + (s1 != "" ? " ORDER BY " + s1 : "");

        return execQuery(sql, pstName);

    }

    

    protected String getFieldList(boolean flag) {

        String s = "";

        for(int i = 0; i < fieldNames.length; i++) {

            if(flag || !isIdentity(i))

                s = s + fieldNames[i] + ", ";

        }

        

        s = s.substring(0, s.length() - 2);

        return s;

    }

    

    protected String getFieldSetList() {

        String s = "";

        for(int i = 0; i < fieldNames.length; i++) {

            if(!isIdentity(i) && getFieldType(i) != 4)

                s = s + fieldNames[i] + " = " + getSQLValue(i) + ", ";

            

        }

        

        s = s.substring(0, s.length() - 2);

        return s;

    }

    

    protected int getFieldType(int i) {

        return fieldTypes[i] & 0xff;

    }

    

    public double getdouble(int i)

    throws DBException {

        try {

            NumberFormat numberformat = NumberFormat.getNumberInstance();

            Number number = numberformat.parse(getObject(i)==null? "0":getObject(i).toString());

            return (double)number.doubleValue();

        }

        catch(ParseException parseexception) {

            parseexception.printStackTrace(System.err);

        }

        throw new DBException(this, DBException.CONVERSION_ERROR);

    }



    



    public float getfloat(int i)

    throws DBException {

        try {
            if(getObject(i)!=null){
                Float number = (Float)getObject(i);
                return (float)number.floatValue();
            } else { 
                return 0.0f; 
            }
        }

        catch(Exception e) {

            e.printStackTrace(System.err);

        }

        throw new DBException(this, DBException.INVALID_NUMBER);

    }



    

    protected Long getIDValue()

    throws DBException {

        try {

            return new Long(OIDFactory.generateOID());

        }

        catch(Exception e) {

            System.out.println(e);

            return new Long(0);

        }

    }

    

    protected String getInsertSQL() {

        return "INSERT INTO " + tableName + "(" + getFieldList(true) + ") VALUES (" + getValueList(true) + ")";

    }

    

    

    

    public long getlong(int i)

    throws DBException {

        try {

            NumberFormat numberformat = NumberFormat.getNumberInstance();

            Object obj = getObject(i);

            if(obj != null) {

                Number number = numberformat.parse(obj.toString());

                return number.longValue();

            } else {

                return 0;

            }

        }

        catch(Exception exception) {

            exception.printStackTrace(System.err);

        }

        throw new DBException(this, DBException.CONVERSION_ERROR);

    }

    

     

    

    public int getInt(int i)

    throws DBException {

        try {

            NumberFormat numberformat = NumberFormat.getNumberInstance();

            Object obj = getObject(i);

            if(obj != null) {

                Number number = numberformat.parse(obj.toString());

                return number.intValue();

            } else {

                return 0;

            }

        }

        catch(Exception exception) {

            exception.printStackTrace(System.err);

        }

        throw new DBException(this, DBException.CONVERSION_ERROR);

    }

    

    

    public Long getLong(int i)

    throws DBException {

        switch(getFieldType(i)) {

            case 0: // '\0'

            case 2: // '\002'

                return new Long(getlong(i));

                

            case 1: // '\001'

            default:

                return null;

        }

    }

    

    

    public Integer getInteger(int i)

    throws DBException {

        switch(getFieldType(i)) {

            case 0: // '\0'

            case 2: // '\002'

                return new Integer(getInt(i));

                

            case 1: // '\001'

            default:

                return null;

        }

    }

    

    protected String getKeyCondition() {

        String s = "(";

        for(int i = 0; i < fieldNames.length; i++)

            if(isPrimaryKey(i))

                s = s + fieldNames[i] + " = " + getSQLValue(i) + " AND ";

        

        s = s.substring(0, s.length() - 4) + ")";

        return s;

    }

    

    

    protected Object getObject(int i)

    throws DBException {

        if(!hasData)

            throw new DBException(this, DBException.NOT_OPEN);

        else

            return fieldValues.get(checkFieldIndex(i));

    }

    
    
    protected String getSQLValue(int i) {
        if (fieldValues.elementAt(i) != null) {
            String s = fieldValues.elementAt(i).toString();
            switch (getFieldType(i)) {
                case I_DBType.TYPE_BOOL: // '\'
                    if (DBSVR_TYPE == DBSVR_POSTGRESQL) {
                        if (Integer.parseInt(s) != 0)
                            s = "TRUE";
                        else
                            s = "FALSE";
                    }

                    break;

                case I_DBType.TYPE_INT: // '\0'
                    if ((isForeignKey(i)) && (Integer.parseInt(s) == 0))
                        s = "NULL";
                default:
                    break;

                case I_DBType.TYPE_LONG: // '\0'
                    if ((isForeignKey(i)) && (Long.parseLong(s) == 0))
                        s = "NULL";

                    break;

                case I_DBType.TYPE_DATE: // '\003'
                    if (s.toUpperCase().equals("CURRENT_TIME")) {
                        s = "SYSDATE";
                    } else {
                        SimpleDateFormat simpledateformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        switch (DBHandler.DBSVR_TYPE) {
                            case DBHandler.DBSVR_MYSQL:
                                s = "DATE_FORMAT('" + simpledateformat.format(fieldValues.elementAt(i)) + "', '%Y-%m-%d %T')";
                                break;
                            case DBHandler.DBSVR_POSTGRESQL:
                                s = "TO_TIMESTAMP('" + simpledateformat.format(fieldValues.elementAt(i)) + "', 'YYYY-MM-DD HH24:MI:SS')";
                                break;
                            case DBHandler.DBSVR_SYBASE:
                                break;
                            case DBHandler.DBSVR_ORACLE:
                                s = "TO_DATE('" + simpledateformat.format(fieldValues.elementAt(i)) + "', 'YYYY-MM-DD HH24:MI:SS')";
                                break;
                            case DBHandler.DBSVR_MSSQL:
                                break;
                            default:
                                s = "DATE_FORMAT('" + simpledateformat.format(fieldValues.elementAt(i)) + "', '%Y-%m-%d %T')";
                        }
                    }

                    break;

                case I_DBType.TYPE_STRING: // '\001'
                    s = quoteString(s, dbSQLQuoteString);
                    if ((isForeignKey(i)) && (s.length() == 2))
                        s = "NULL";
                    break;

                case I_DBType.TYPE_FLOAT: // '\002'
                    if (!fieldValues.elementAt(i).getClass().getName().equals("java.lang.String")) {
                        DecimalFormat decimalformat = new DecimalFormat(dbSQLDecimalFormat);
                        s = decimalformat.format(((Number) fieldValues.elementAt(i)).doubleValue());
                        //System.out.println("String angka type float ==> "+s+" = "+fieldValues.elementAt(i));
                    }
                    break;


            }
            return s;
        } else {
            return "NULL";
        }
    }

    
/*
    protected String getSQLValue(int i) {

        if(fieldValues.elementAt(i) != null) {

            String s = fieldValues.elementAt(i).toString();



            switch(getFieldType(i)) {

                case I_DBType.TYPE_INT: // '\0'

                    if((isForeignKey(i)) && (Integer.parseInt(s) == 0)){

                    	switch(DBSVR_TYPE){

	                       	case DBSVR_ORACLE :

                          	 	 s = "NULL";

                       			 break;

	                        case DBSVR_MYSQL:

	                             s = "0";

	                       		 break;

	                        case DBSVR_POSTGRESQL:

	                        	 s = "NULL";

	                       		 break;

	                        case DBSVR_MSSQL:

                                 s = "0";

	                        	 break;

	                        case DBSVR_SYBASE:

	                        	 break;

                        	default:

                                 s = "0";

                        		 break;

                        }



                	}

                    default:

                    break;



                case I_DBType.TYPE_LONG: // '\0'

                    if((isForeignKey(i)) && (Long.parseLong(s) == 0))

                    	switch(DBSVR_TYPE){

	                       	case DBSVR_ORACLE :

                          	 	 s = "NULL";

                       			 break;

	                        case DBSVR_MYSQL:

	                             s = "NULL";//0"; update by Kartika to suppoer FK constraint

	                       		 break;

	                        case DBSVR_POSTGRESQL:

	                        	 s = "NULL";

	                       		 break;

	                        case DBSVR_MSSQL:

                                 s = "0";

	                        	 break;

	                        case DBSVR_SYBASE:

	                        	 break;

                        	default:

                                 s = "0";

                        		 break;

                        }



                    break;

                case I_DBType.TYPE_LONG_0_IS_NULL: // '\0'
                    
                    if((isForeignKey(i)) && (Long.parseLong(s) == 0))

                    	switch(DBSVR_TYPE){

	                       	case DBSVR_ORACLE :

                          	 	 s = "NULL";

                       			 break;

	                        case DBSVR_MYSQL:

	                             s = "NULL";//0"; update by Kartika to suppoer FK constraint

	                       		 break;

	                        case DBSVR_POSTGRESQL:

	                        	 s = "NULL";

	                       		 break;

	                        case DBSVR_MSSQL:

                                 s = "NULL";

	                        	 break;

	                        case DBSVR_SYBASE:

	                        	 break;

                        	default:

                                 s = "NULL";

                        		 break;

                        }



                    break;
                    

                case I_DBType.TYPE_DATE: // '\003'

                    if(s.toUpperCase().equals("CURRENT_TIME")) {

                        s = "SYSDATE";

                    } else {

                        SimpleDateFormat simpledateformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

                        //System.out.println("DBSVR_TYPE DBSVR_TYPE"+DBSVR_TYPE);

                        switch(DBSVR_TYPE){

	                       	case DBSVR_ORACLE :

                          	 	 s = "TO_DATE('" + simpledateformat.format(fieldValues.elementAt(i)) + "', 'YYYY-MM-DD HH24:MI:SS')";

                       			 break;

	                        case DBSVR_MYSQL:

	                             // MySQL date format

	                        	 s = "DATE_FORMAT('" + simpledateformat.format(fieldValues.elementAt(i)) + "', '%Y-%m-%d %T')";

	                       		 break;

	                        case DBSVR_POSTGRESQL:

	                             // Postgres

	                        	 s = "TO_TIMESTAMP('" + simpledateformat.format(fieldValues.elementAt(i)) + "', 'YYYY-MM-DD HH24:MI:SS')";

	                       		 break;

	                        case DBSVR_MSSQL:

                                         s = "'"+simpledateformat.format(fieldValues.elementAt(i))+"'";

	                        	 break;

	                        case DBSVR_SYBASE:

	                        	 break;

                        	default:

                                        s = "DATE_FORMAT('" + simpledateformat.format(fieldValues.elementAt(i)) + "', '%Y-%m-%d %T')";

                        		 break;

                        }

                    }

                    break;

                    

                case I_DBType.TYPE_STRING: // '\001'

                    s = quoteString(s, dbSQLQuoteString);

                	if((isForeignKey(i)) && (s.length() == 2))

                    	switch(DBSVR_TYPE){

	                       	case DBSVR_ORACLE :

                          	 	 s = "NULL";

                       			 break;

	                        case DBSVR_MYSQL:

	                             s = "";

	                       		 break;

	                        case DBSVR_POSTGRESQL:

	                        	 s = "NULL";

	                       		 break;

	                        case DBSVR_MSSQL:

                                 s = "";

	                        	 break;

	                        case DBSVR_SYBASE:

	                        	 break;

                        	default:

                                 s = "";

                        		 break;

                        }



                    break;

                    

                case I_DBType.TYPE_FLOAT: // '\002'

                    if(!fieldValues.elementAt(i).getClass().getName().equals("java.lang.String")) {

                        DecimalFormat decimalformat = new DecimalFormat(dbSQLDecimalFormat);

                        s = decimalformat.format(((Number)fieldValues.elementAt(i)).doubleValue());



                        //System.out.println(" ------decimal format : "+s);



                    }

                    break;





            }

            return s;

        } else {

            return "NULL";

        }

    }
*/
    

    protected String getSelectSQL() {

        return "SELECT " + getFieldList(true) + " FROM " + tableName + " WHERE " + getKeyCondition();

    }

    

    protected static Statement getStatement(Connection connection)

    throws DBException {

        getLog();

        try {

            if(connection != null) {

                Statement statement = connection.createStatement();

                //dbLog.info("Opened a statement: " + statement.toString());

                return statement;

            } else {

                throw new DBException(null, DBException.NO_CONNECTION);

            }

        }

        catch(SQLException sqlexception) {

            sqlexception.printStackTrace(System.err);

            throw new DBException(null, sqlexception);

        }

    }

    

    public String getString(int i)

    throws DBException {

        Object obj = getObject(i);

        if(obj == null)

            return null;

        String s = "";

        switch(getFieldType(i)) {

            default:

                break;

                

            case 3: // '\003'

                SimpleDateFormat simpledateformat = new SimpleDateFormat(dateFormat);

                s = simpledateformat.format((java.util.Date)obj);

                break;

                

            case 2: // '\002'

                if(!isPrimaryKey(i)) {

                    DecimalFormat decimalformat = new DecimalFormat(decimalFormat);

                    s = decimalformat.format(((Number)obj).floatValue());

                } else {

                    s = (new Integer(((Number)obj).intValue())).toString();

                }

                break;

                

            case 0: // '\0'

            case 1: // '\001'

            case 4: // '\004'

                s = obj.toString().trim();

                if(s.equals("<NULL>"))

                    s = null;

                break;

        }

        return s;

    }

    

    

    protected String getTimestampSQL() {

        return "SELECT " + fieldNames[timestampIndex] + " FROM " + tableName + " WHERE " + getKeyCondition();

    }

    

    protected String getUpdateSQL() {

        return "UPDATE " + tableName + " SET " + getFieldSetList() + " WHERE " + getKeyCondition();

    }

    

    protected String getValueList(boolean flag) {

        String s = "";

        for(int i = 0; i < fieldNames.length; i++)

            if(!isIdentity(i))

                s = s + getSQLValue(i) + ", ";

            else{

                if(flag)

                    s = s + getSQLValue(i) + ", ";

            }



        s = s.substring(0, s.length() - 2);

        return s;

    }

    

    protected DBHandler initialize(ResultSet resultset)

    throws DBException {

        getData(resultset);

        hasData = true;

        return this;

    }

    

    public int insert()

    throws DBException {

        Connection connection = null;

        Statement statement = null;

        byte byte0 = -1;

        try {

            connection = getConnection();

            statement = getStatement(connection);

            if(idIndex != -1) {

                fieldValues.set(idIndex, getIDValue());

            }

            //System.out.println(" Insert sql : " + getInsertSQL());

            //dbLog.info("Execute updateSQL: " + getInsertSQL());

            statement.executeUpdate(getInsertSQL());

            hasData = true;

        }

        catch(SQLException sqlexception) {

            sqlexception.printStackTrace(System.err);

            throw new DBException(this, sqlexception);

        }

        finally {

            closeStatement(statement);

            closeConnection(connection);

        }

        return byte0;

    }

    

    protected boolean isIdentity(int i) {

        return (fieldTypes[i] & TYPE_ID) != 0;

    }

     protected boolean isAutoincremnet(int i) {
    
        return (fieldTypes[i] & TYPE_AI) != 0;

    }

    

    protected boolean isNull(int i)

    throws DBException {

        return getObject(i) == null;

    }

    

    protected boolean isPrimaryKey(int i) {

        return (fieldTypes[i] & TYPE_PK) != 0;

    }



    protected boolean isForeignKey(int i) {

        return (fieldTypes[i] & TYPE_FK) != 0;

    }

    

    

    protected void loadKey()

    throws DBException {

        for(int i = 0; i < keyCount; i++)

            keyValues[i] = getInt(keyIndex[i]);

        

    }

    

    public boolean locate(String sOid)

    throws DBException {

        long ai[] = {  (new Long(sOid)).longValue() };

        return locate(ai);

    }

    

    public boolean locate(long lOid)

    throws DBException {

        long ai[] = { lOid };

        return locate(ai);

    }



    public boolean locate(long lOid0, long lOid1)

    throws DBException {

        long ai[] = { lOid0, lOid1  };

        return locate(ai);

    }



    public boolean locate(long lOid0, long lOid1, long lOid2)

    throws DBException {

        long ai[] = { lOid0, lOid1, lOid2  };

        return locate(ai);

    }

    

    public boolean locate(long[] ai)

    throws DBException {

        Object aobj[] = new Object[ai.length];

        for(int i = 0; i < ai.length; i++)

            aobj[i] = new Long(ai[i]);

        

        return locate(aobj);

    }

    

    public boolean locate(Object[] aobj)

    throws DBException {

        if(keyCount != aobj.length)

            throw new DBException(this, DBException.INVALID_KEY);

        hasData = false;

        Connection connection = null;

        Statement statement = null;

        reset();

        int i = 0;

        for(int j = 0; j < fieldNames.length; j++)

            if(isPrimaryKey(j))

                fieldValues.set(j, aobj[i++]);

        

        try {

            connection = getConnection();

            statement = getStatement(connection);

            //dbLog.info("Execute SQL: " + getSelectSQL());

            ResultSet resultset = statement.executeQuery(getSelectSQL());

            if(hasData = resultset.next())

                getData(resultset);

            resultset.close();

            //dbLog.info("Closed ResultSet.");

        }

        catch(SQLException sqlexception) {

            sqlexception.printStackTrace(System.err);

            throw new DBException(this, sqlexception);

        }

        finally {

            closeStatement(statement);

            closeConnection(connection);

        }

        return hasData;

    }

    

    protected String quoteString(String s, String s1) {

        String s2 = s;

        if(s1.length() > 0) {

            for(int i = -1; (i = s2.indexOf(s1, i + 1)) != -1;)

                s2 = s2.substring(0, i) + s1 + s1 + s2.substring(++i);

            

            s2 = s1 + s2 + s1;

        }

        return s2;

    }

    

    public void reset() {

        for(int i = 0; i < fieldValues.size(); i++)

            fieldValues.set(i, null);

        

        hasData = false;

    }

    

    public void setBoolean(int i, Boolean boolean1)

    throws DBException {

        if(boolean1 != null)

            setInt(i, boolean1.booleanValue() ? 1 : 0);

    }

    

    public void setboolean(int i, boolean boolean1)

    throws DBException {

        setInt(i, boolean1 ? 1 : 0);

    }



    public void setDate(int i, java.util.Date date)

    throws DBException {

        if(date != null)

            setObject(i, new Date(date.getTime()));

        else

            setObject(i, null);

    }

    

    public void setFloat(int i, double d)

    throws DBException {

        setObject(i, new Float(d));

    }

    

    public void setFloat(int i, float f)

    throws DBException {

        setObject(i, new Float(f));

    }



    public void setInt(int i, int j)

    throws DBException {

        setObject(i, new Integer(j));

    }

    

    public void setInteger(int i, Integer integer)

    throws DBException {

        setObject(i, integer);

    }

    

    public void setLong(int i, long longVal)

    throws DBException {

        setObject(i, new Long(longVal));

    }





    public void setLong(int i, Long longVal)

    throws DBException {

        setObject(i, longVal);

    }

    

    public void setDouble(int i, double doubleVal)

    throws DBException {



        //System.out.println("-- DBHandler : "+doubleVal);



        setObject(i, new Double(doubleVal));

    }





    public void setDouble(int i, Double doubleVal)

    throws DBException {

        setObject(i, doubleVal);

    }



    

    protected void setObject(int i, Object obj)

    throws DBException {

        if(isIdentity(checkFieldIndex(i)) || getFieldType(i) == 4) {

            throw new DBException(this, DBException.FIXED_COLUMN);

        } else {

            fieldValues.set(i, obj);

            recordModified = true;

            return;

        }

    }

    

    public void setString(int i, String s)

    throws DBException {

        setObject(i, s);

    }

    

    protected static String toSQL(Object obj, int i) {

        if(obj != null) {

            String s = obj.toString();

            switch(i) {

                case 3: // '\003'

                    s = (new Date(((java.util.Date)obj).getTime())).toString();

                    if(s.toUpperCase().equals("CURRENT_TIME"))

                        s = "SYSDATE";

                    else

                        s = "TO_DATE('" + s + "', 'YYYY-MM-DD')";

                    break;

                    

                case 1: // '\001'

                    s = "'" + s + "'";

                    break;

            }

            return s;

        } else {

            return "NULL";

        }

    }

    

    public void update()

    throws DBException {

        if(!hasData)

            throw new DBException(this, DBException.NOT_OPEN);

        if(!recordModified)

            return;

        Connection connection = null;

        Statement statement = null;

        try {

             //System.out.println("update sql : " + getUpdateSQL());



            connection = getConnection();

            checkConcurrency(connection);

            statement = getStatement(connection);

            statement.executeUpdate(getUpdateSQL());

        }

        catch(SQLException sqlexception) {

            sqlexception.printStackTrace(System.err);

            throw new DBException(this, sqlexception);

        }

        finally {

            closeStatement(statement);

            closeConnection(connection);

        }

    }

    
public int insertByOid(long oid)
    throws DBException {
        Connection connection = null;
        Statement statement = null;
        byte byte0 = -1;
        try {
            //System.out.println("Entering insert : " + getInsertSQL());
            connection = getConnection();
            statement = getStatement(connection);
            if(idIndex != -1) {
                fieldValues.set(idIndex, oid);
            }
            //System.out.println("SQL : " + getInsertSQL());
            statement.executeUpdate(getInsertSQL());
            hasData = true;
        }
        catch(SQLException sqlexception) {
            sqlexception.printStackTrace(System.err);
            throw new DBException(this, sqlexception);
        }
        finally {
            closeStatement(statement);
            closeConnection(connection);
        }
        return byte0;
    }
    

    public  String getDBTableName() {

        return tableName;

    }

    

    

    

    

    // .............................................. Direct execute methods 



    

    protected  Vector execQuery(String sql, String classNm)

    throws DBException {

        getLog();

        Connection connection = null;

        Statement statement = null;

        Vector vector = null;

        Object obj = null;

        try {

            connection = getConnection();

            statement = getStatement(connection);

            //dbLog.info(sql);

            ResultSet resultset = statement.executeQuery(sql);

            vector = new Vector();

            DBHandler dbhandler;

            fieldValues = new Vector();

            fieldValues.setSize(fieldNames.length);

            for(; resultset.next(); vector.add(dbhandler.initialize(resultset))) {

                try {

                    dbhandler = (DBHandler)Class.forName(pstName).newInstance();

                    //System.out.println("");

                }

                catch(Exception exception1) {

                    exception1.printStackTrace(System.err);

                    resultset.close();

                    throw new DBException(null, DBException.INVALID_CLASS);

                }

            }

            

            resultset.close();

            //dbLog.info("Closed ResultSet.");

        }

        catch(SQLException sqlexception) {

            sqlexception.printStackTrace(System.err);

            throw new DBException(null, sqlexception);

        }

        finally {

            closeStatement(statement);

            closeConnection(connection);

        }

        return vector;

    }

    

    

    /**

     *      DENGEROUS....!!!

     *      Delete this fuction in next dev

     */

    public static ResultSet execQuery(String sql)

    throws DBException {

        getLog();

        Connection connection = null;

        Statement statement = null;

        

        try {

            connection = getConnection();

            statement = getStatement(connection);

            

            ResultSet resultset = statement.executeQuery(sql);

            

            return resultset;

            

        }

        catch(SQLException sqlexception) {

            sqlexception.printStackTrace(System.err);

            throw new DBException(null, sqlexception);

        }

        catch(Exception e) {

            e.printStackTrace(System.err);

            throw new DBException(null, e);

        }

        

        finally {

            closeStatement(statement);

            closeConnection(connection);

        }        

    }

    





    public static DBResultSet execQueryResult(String sql)

    throws DBException {

        getLog();

        Connection connection = null;

        Statement statement = null;

        

        try {

            connection = getConnection();

            statement = getStatement(connection);

            //System.out.println(" connection "+(connection!=null)+ "-statement"+(statement!=null));

            ResultSet resultset = statement.executeQuery(sql);

            //System.out.println(" after execute sql "+sql + "\n >>>> connPool ="+connPool.getSize());



            return new DBResultSet(connPool, connection, statement, resultset);

            

        }

        catch(SQLException sqlexception) {

            sqlexception.printStackTrace(System.err);

            throw new DBException(null, sqlexception);

        }

        catch(Exception e) {

            e.printStackTrace(System.err);

            throw new DBException(null, e);

        }

        

        finally {

            // The close connection and other related object

            // will be done in the caller fuction

            

            // System.out.println("Close connection");

             //closeStatement(statement);

             closeConnection(connection);

        }        

    }    

    



   	public static int execSqlInsert(String sqlString)

    throws DBException {

        Connection connection = null;

        Statement statement = null;

        

        try {

            connection = getConnection();

            statement = getStatement(connection);

            

            int result = statement.executeUpdate(sqlString);

            DBHandler dbhandler = null;

            

        }

        

        catch(SQLException sqlexception) {

            sqlexception.printStackTrace(System.err);

            throw new DBException(null, sqlexception);

        }

        finally {

            closeStatement(statement);

            closeConnection(connection);

        }

        

        return 0;

    }



    public static int execUpdate(String sql)

    throws DBException {

        Connection connection = null;

        Statement statement = null;



        int result = 0;

        try {

            connection = getConnection();

            statement = getStatement(connection);

            

            result = statement.executeUpdate(sql);

            DBHandler dbhandler = null;            

        }

        

        catch(SQLException sqlexception) { 

            sqlexception.printStackTrace(System.err);

            throw new DBException(null, sqlexception);

        }

        finally {

            closeStatement(statement);

            closeConnection(connection);

        }

        

        return result;

    }    

    

    

    

    

    public static int execUpdatePreparedStatement(PreparedStatement pstmt)

    throws DBException {

    

        try {			

            int iRowCount = pstmt.executeUpdate();

            pstmt.close();

            return iRowCount;

        }catch(SQLException sqle) {

            sqle.printStackTrace(System.err);

            throw new DBException(null, sqle);

        }catch(Exception e) {

            e.printStackTrace(System.err);

            System.out.println("DBHandler.execUpdatePreparedStatement() " + e.toString());

        }

        return 0;

    } 

	

    /**

     *      DENGEROUS....!!!

     *      Delete this fuction in next dev

     */

    public static PreparedStatement getPreparedStatement(String sql, Connection connection)

    throws DBException {

        try {

            PreparedStatement pstmt = null;

            pstmt = connection.prepareStatement(sql);

   	    return pstmt;

        }catch(SQLException sqle) {

            sqle.printStackTrace(System.err);

            throw new DBException(null, sqle);

        }catch(Exception e) {

            e.printStackTrace(System.err);

            System.out.println("DBHandler.getPreparedStatement() " + e.toString());

        }      

        

        return null;

    }

        

        

    /**

     *      DENGEROUS....!!!

     *      Delete this fuction in next dev

     */

    public static Connection getDBConnection()

    {

        try {

            return getConnection();

        }catch(Exception e) {

             System.out.println("DBHandler.getDBConnection() " + e.toString());

        }

        return null;

    }

    

    

 

    public static DBResultSet getPSTMTConnection(String sql)

    throws DBException 

    {    

        try {

            Connection connection = getConnection();

            PreparedStatement pstmt = null;

            pstmt = connection.prepareStatement(sql);

   	    

            return new DBResultSet(connPool, connection, pstmt, null);

            

        }catch(SQLException sqle) { 

            sqle.printStackTrace(System.err);

            throw new DBException(null, sqle);

        }catch(Exception e) {

            e.printStackTrace(System.err);

            System.out.println("DBHandler.getPSTMTConnection() " + e.toString());

        }

        return null;

    }



    // convert java.sql.Date to java.util.Date

    public static java.util.Date convertDate(java.sql.Date date, java.sql.Time time){

        java.util.Date dt = new java.util.Date();

        if(date != null && time != null){

    		dt = new java.util.Date(date.getYear(),date.getMonth(),date.getDate(),

                	time.getHours(),time.getMinutes(),time.getSeconds());

        }

        return dt;

    }





    /**

    * modified by gedhy

    */

    public static final int SORT_TYPE_ASCENDING = 0;

    public static final int SORT_TYPE_DESCENDING = 1;

	public static String getOrderByType(int orderType){

        String result = "";

        switch(DBSVR_TYPE){

	        case DBSVR_MYSQL :

                 result = orderType==SORT_TYPE_ASCENDING ? " ASC" : " DESC";

            	 break;

	        case DBSVR_POSTGRESQL :

                 result = orderType==SORT_TYPE_ASCENDING ? " ASC" : " DESC";

            	 break;

	        case DBSVR_SYBASE :

                 result = orderType==SORT_TYPE_ASCENDING ? " ASC" : " DESC";

            	 break;

	        case DBSVR_ORACLE :

                 result = orderType==SORT_TYPE_ASCENDING ? " ASC" : " DESC";

            	 break;

	        case DBSVR_MSSQL :

                 result = orderType==SORT_TYPE_ASCENDING ? " ASC" : " DESC";

            	 break;

        }

        return result;

    }



    

} // end of DBHandler

