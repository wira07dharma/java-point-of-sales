package com.dimata.qdep.db;



// import java

import com.dimata.qdep.system.I_DBExceptionInfo;



import java.sql.SQLException;



public class DBException extends Exception  implements I_DBExceptionInfo

{



    protected DBHandler dbHandler;

    protected int errorIndex;

    protected SQLException SQLEx;

    protected Exception Ex;



    public DBException(DBHandler dbhandler, int i)

    {

        dbHandler = dbhandler;

        errorIndex = i;

    }



    public DBException(DBHandler dbhandler, Exception exception)

    {

        dbHandler = dbhandler;

        Ex = exception;

        errorIndex = UNKNOWN;

    }



    public DBException(DBHandler dbhandler, SQLException sqlexception)

    {

        dbHandler = dbhandler;

        SQLEx = sqlexception;

        errorIndex = parseSQLErrIdx(SQLEx);

    }



    public static int parseSQLErrIdx(SQLException sqlexception) {

           String sqlErr =  sqlexception.getMessage();



           final String  SQL_DUPLICATE_KEY = "Duplicate entry";

           final String  SQL_COLUMN_NOT_FOUND = "Column not found";

           final String  SQL_DELETE_RESTRICT = "foreign key constraint fails";



           int checkStr =sqlErr.indexOf(SQL_DUPLICATE_KEY);

           if (checkStr >-1)

            	return  MULTIPLE_ID;



           checkStr =sqlErr.indexOf(SQL_COLUMN_NOT_FOUND);

           if (checkStr >-1)

            	return COLUMN_NOT_FOUND ;



           checkStr =sqlErr.indexOf(SQL_DELETE_RESTRICT);

           if (checkStr >-1)

            	return DEL_RESTRICTED ;



          return SQL_ERROR;

    }



    public int getErrorCode()

    {

        return errorIndex;

    }



    public String getMessage()

    {

        String s = "";

        

        try

        {

            if(dbHandler != null)

                s = s + "[Table: " + dbHandler.getDBTableName() + "] ";

            s = s + errorMsg[errorIndex];

            if(errorIndex == SQL_ERROR || errorIndex == UNKNOWN)   

                s = s + SQLEx.getMessage();

        }

        catch(Exception e)

        {

            System.out.println(">>> com.dimata.qdep.db.DBException.getMessage() exc : " + e.toString());

        }

        

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

