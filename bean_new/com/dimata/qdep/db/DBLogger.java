// FrontEnd Plus for JAD

// DeCompiled : Logger.class



package com.dimata.qdep.db;



import java.io.*;

import java.text.DateFormat;

import java.text.SimpleDateFormat;

import java.util.Date;



import com.dimata.qdep.db.*;







public class DBLogger

{

    public static int OUT_TARGET_STDIO = 0;

    public static int OUT_TARGET_FILE  = 1;

    public static int OUT_TARGET_RDBMS = 2;

    



    static final int CRITICAL_ERROR = 1;

    static final int ERROR = 2;

    static final int WARNING = 3;

    static final int INFO = 4;

    static final int TRACE = 5;



    private static final String KEY_DBLAYER_LOG = "dblayer_log";

    private static final String KEY_LEVEL_LOG 	= "loglevel";

    private static final String DATE_LOG_FORMAT = "dd.MM.yyyyy hh:mm:ss";





    private static DBLogger logger = null;

    private OutputStream stream;

    private PrintStream print;

    private int outputTarget = OUT_TARGET_FILE;

    

    String logfile;

    int loglevel;

    



    public DBLogger()

    {

        logfile = null;

        loglevel = 0;

        try

        {

            DBConfigReader xmlconfigreader = new DBConfigReader(DBHandler.CONFIG_FILE);

            logfile = xmlconfigreader.getConfigValue(KEY_DBLAYER_LOG);

            SimpleDateFormat simpledateformat = new SimpleDateFormat(".MM.yyyyy");

            String s = simpledateformat.format(com.dimata.util.DateCalc.getDate());

            logfile += s;

            String s1 = xmlconfigreader.getConfigValue(KEY_LEVEL_LOG);

            loglevel = (new Integer(s1)).intValue();

            stream = new FileOutputStream(logfile, true);

        }

        catch(Exception _ex) { }

        print = new PrintStream(stream);

    }



    public DBLogger(String s)

    {

        logfile = null;

        loglevel = 0;

        try

        {

            DBConfigReader xmlconfigreader = new DBConfigReader(DBHandler.CONFIG_FILE);

            logfile = xmlconfigreader.getConfigValue(KEY_DBLAYER_LOG);

            logfile = logfile.substring(0, logfile.lastIndexOf(47)) + "/" + s;

            SimpleDateFormat simpledateformat = new SimpleDateFormat(".MM.yyyyy");

            String s1 = simpledateformat.format(com.dimata.util.DateCalc.getDate());

            logfile += s1;

            String s2 = xmlconfigreader.getConfigValue(KEY_LEVEL_LOG);

            loglevel = (new Integer(s2)).intValue();

            stream = new FileOutputStream(logfile, true);

        }

        catch(Exception _ex) { }

        print = new PrintStream(stream);

    }



    public void close()

    {

        if(print != null)

            print.close();

    }



    public void critical_error(Exception exception)

    {

        if(loglevel < 1)

        {

            return;

        } else

        {

            SimpleDateFormat simpledateformat = new SimpleDateFormat(DATE_LOG_FORMAT);

            String s = simpledateformat.format(com.dimata.util.DateCalc.getDate());

            print.println(s + " CRITICAL_ERROR STACK TRACE");

            exception.printStackTrace(print);

            print.println('\0');

            return;

        }

    }



    public void critical_error(String s)

    {

        if(loglevel < 1)

        {

            return;

        } else

        {

            SimpleDateFormat simpledateformat = new SimpleDateFormat(DATE_LOG_FORMAT);

            String s1 = simpledateformat.format(com.dimata.util.DateCalc.getDate());

            print.println(s1 + " CRITICAL_ERROR " + s);

            return;

        }

    }



    public void error(String s)

    {

        if(loglevel < 2)

        {

            return;

        } else

        {

            SimpleDateFormat simpledateformat = new SimpleDateFormat(DATE_LOG_FORMAT);

            String s1 = simpledateformat.format(com.dimata.util.DateCalc.getDate());

            print.println(s1 + " ERROR " + s);

            return;

        }

    }



    public static DBLogger getLogger()

    {

        if(logger == null) {

            logger = new DBLogger();

        }

        return logger;

    }



    public void info(String s)

    {

        if(loglevel < 4)

        {

            return;

        } else

        {

            SimpleDateFormat simpledateformat = new SimpleDateFormat(DATE_LOG_FORMAT);

            String s1 = simpledateformat.format(com.dimata.util.DateCalc.getDate());

            print.println(s1 + " INFO " + s);

            return;

        }

    }



    public static void main(String[] args)

    {

        //System.out.println("Logger main!");

        String s = null;

        try

        {

            DBConfigReader xmlconfigreader = new DBConfigReader(DBHandler.CONFIG_FILE);

            s = xmlconfigreader.getConfigValue(KEY_DBLAYER_LOG);

        }

        catch(Exception _ex) { }

        //System.out.println(s);

    }



    public void trace(String s)

    {

        if(loglevel < 5)

        {

            return;

        } else

        {

            SimpleDateFormat simpledateformat = new SimpleDateFormat(DATE_LOG_FORMAT);

            String s1 = simpledateformat.format(com.dimata.util.DateCalc.getDate());

            print.println(s1 + " TRACE " + s + "\n");

            return;

        }

    }



    public void warning(String s)

    {

        if(loglevel < 3)

        {

            return;

        } else

        {

            SimpleDateFormat simpledateformat = new SimpleDateFormat(DATE_LOG_FORMAT);

            String s1 = simpledateformat.format(com.dimata.util.DateCalc.getDate());

            print.println(s1 + " WARNING " + s);

            return;

        }

    }



}

