package com.dimata.services.db;

import java.sql.SQLException;

import com.dimata.services.db.*;


public class DBException extends Exception
{

    public static int UNKNOWN = 0;
    public static int SQL_ERROR = 1;
    public static int CONCURRENCY_VIOLATION = 2;
    public static int INDEX_OUT_OF_RANGE = 3;
    public static int MULTIPLE_ID = 4;
    public static int INVALID_KEY = 5;
    public static int NOT_OPEN = 6;
    public static int INVALID_DATE = 7;
    public static int FIXED_COLUMN = 8;
    public static int NO_CONNECTION = 9;
    public static int CONVERSION_ERROR = 10;
    public static int DRIVER_NOT_FOUND = 11;
    public static int INVALID_CLASS = 12;
    public static int RECORD_NOT_FOUND = 13;
    public static int CONFIG_ERROR = 14;
    public static int INVALID_NUMBER = 15;
    public static int DEL_RESTRICTED = 16;
    
    
    protected String[] errorMsg = {
        "Unknown error occured.",
        "SQL error: ",
        "Record was modified by another user. Cannot update or delete.",
        "Table or field index out of range.",
        "Cannot have more than one IDENTITY column per table.",
        "Invalid number of key values.",
        "Table not open.",
        "Date conversion error.",
        "Cannot modify IDENTITY or TIMESTAMP columns.",
        "Connection not open.",
        "Data type coversion error.",
        "JDBC driver not found.",
        "Class for detail objects not found. Check DBInfo class.",
        "Record not found.", "Error reading configuration file.",
        "Invalid number format.",
        "Can not delete these data, another data ference to these data"
    };
    protected DBHandler dbHandler;
    protected int errorIndex;
    protected SQLException SQLEx;
    protected Exception Ex;

    public DBException(DBHandler dbhandler, int i)
    {
        dbHandler = dbhandler;
        errorIndex = i;
        //DBLogger.getLogger().error(getMessage());
    }

    public DBException(DBHandler dbhandler, Exception exception)
    {
        dbHandler = dbhandler;
        Ex = exception;
        errorIndex = UNKNOWN;
        //DBLogger.getLogger().error(getMessage());
    }

    public DBException(DBHandler dbhandler, SQLException sqlexception)
    {
        dbHandler = dbhandler;
        SQLEx = sqlexception;
        errorIndex = SQL_ERROR;
        //DBLogger.getLogger().error(getMessage());
    }

    public int getErrorCode()
    {
        return errorIndex;
    }

    public String getMessage()
    {
        String s = "";
        if(dbHandler != null)
            s = s + "[Table: " + DBHandler.getDBTableName() + "] ";
        s = s + errorMsg[errorIndex];
        if(errorIndex == SQL_ERROR || errorIndex == UNKNOWN)
            s = s + SQLEx.getMessage();
        return s;
    }

    public int getSQLErrorCode()
    {
        if(errorIndex == SQL_ERROR)
            return SQLEx.getErrorCode();
        else
            return -1;
    }

}
