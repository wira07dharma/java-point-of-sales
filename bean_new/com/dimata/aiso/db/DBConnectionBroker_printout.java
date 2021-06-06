package com.dimata.aiso.db;

import java.io.*;
import java.sql.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;


public class DBConnectionBroker_printout implements Runnable
{

    private Thread runner;
    private Connection[] connPool;
    private int[] connStatus;
    private long[] connLockTime;
    private long[] connCreateDate;
    private String[] connID;
    private String dbDriver;
	// private String dbServer;
    private String dbURL;
    private String dbLogin;
    private String dbPassword;
    private String logFileString;
    private int currConnections;
    private int connLast;
    private int minConns;
    private int maxConns;
    private int maxConnMSec;
    private boolean available;
    private PrintWriter log;
    private SQLWarning currSQLWarning;
    private String pid;
    private SimpleDateFormat simpledateformat;

    public DBConnectionBroker_printout(String driver, String url, String user, String passwd, int curConn, int maxConn, String logFileName, double day) throws IOException
    {
        available = true;
        connPool = new Connection[maxConn];
        connStatus = new int[maxConn];
        connLockTime = new long[maxConn];
        connCreateDate = new long[maxConn];
        connID = new String[maxConn];


        dbDriver = driver;
        dbURL = url;
        dbLogin = user;
        dbPassword = passwd;
        maxConns = maxConn;
        currConnections = curConn;

        logFileString = logFileName;
        maxConnMSec = (int)(day * 86400000D);
        if(maxConnMSec < 30000) maxConnMSec = 30000;

        try
        {
            log = new PrintWriter(new FileOutputStream(logFileName), true);
        }
        catch(IOException _ex)
        {
            try
            {
                log = new PrintWriter(new FileOutputStream("DCB_" + System.currentTimeMillis() + ".log"), true);
            }
            catch(IOException _ex2)
            {
                throw new IOException("Can't open any log file");
            }
        }
//        SimpleDateFormat simpledateformat = new SimpleDateFormat("yyyy.MM.dd G 'at' hh:mm:ss a zzz");
        simpledateformat = new SimpleDateFormat("MMM dd yyyyy hh:mm:ss");

        Date date = new Date();
        pid = simpledateformat.format(date);
        BufferedWriter bufferedwriter = new BufferedWriter(new FileWriter(logFileName + "pid"));
        bufferedwriter.write(pid);
        bufferedwriter.close();
        log.println("Starting DBConnectionBroker Version 1.0");
        log.println("dbDriver = " + driver);
        log.println("dbURL = " + url);
        log.println("dbLogin = " + user);
        log.println("dbPasswd = **********");

        log.println("logfile = " + logFileName);
        log.println("minconnections = " + curConn);
        log.println("maxconnections = " + maxConn);
        log.println("Total refresh interval = " + day + " days");
        log.println("-----------------------------------------");
        boolean flag = false;
        byte byte0 = 20;
        try
        {
            for(int k = 1; k < byte0;)
                try
                {
                    for(int l = 0; l < currConnections; l++)
                        createConn(l);

                    flag = true;
                    break;
                }
                catch(SQLException sqlexception)
                {
                    log.println("--->Attempt (" + String.valueOf(k) + " of " + String.valueOf(byte0) + ") failed to create new connections set at startup: ");
                    log.println("    " + sqlexception);
                    log.println("    Will try again in 15 seconds...");
                    try
                    {
                        Thread.sleep(15000L);
                    }
                    catch(InterruptedException _ex) { }
                    k++;
                }

            if(!flag)
            {
                log.println("\r\nAll attempts at connecting to Database exhausted");
                throw new IOException();
            }
        }
        catch(Exception _ex)
        {
            throw new IOException();
        }
        runner = new Thread(this);
        runner.start();
    }

    public void run()
    {
        boolean flag = true;
        Statement statement = null;
        while(flag) 
        {
            try
            {
                BufferedReader bufferedreader = new BufferedReader(new FileReader(logFileString + "pid"));
                String s = bufferedreader.readLine();
                if(!s.equals(pid))
                {
                    log.close();
                    for(int k = 0; k < currConnections; k++)
                        try
                        {
                            connPool[k].close();
                        }
                        catch(SQLException _ex) { }

                    return;
                }
                bufferedreader.close();
            }
            catch(IOException _ex)
            {
                System.out.println("Can't read the file for pid info: " + logFileString + "pid");
                log.println("Can't read the file for pid info: " + logFileString + "pid");
            }
            for(int i = 0; i < currConnections; i++)
                try
                {
                    currSQLWarning = connPool[i].getWarnings();
                    if(currSQLWarning != null)
                    {
                        log.println("Warnings on connection " + String.valueOf(i) + " " + currSQLWarning);
                        connPool[i].clearWarnings();
                    }
                }
                catch(SQLException sqlexception)
                {
                    log.println("Cannot access Warnings: " + sqlexception);
                }

            for(int j = 0; j < currConnections; j++)
            {
                long l = System.currentTimeMillis() - connCreateDate[j];
                synchronized(connStatus)
                {
                    if(connStatus[j] > 0)
                        continue;
                    connStatus[j] = 2;
                }
                try
                {
                    if(l > (long)maxConnMSec)
                        throw new SQLException();
                    statement = connPool[j].createStatement();
                    connStatus[j] = 0;
                    if(connPool[j].isClosed())
                        throw new SQLException();
                }
                catch(SQLException _ex)
                {
                    try
                    {
                        log.println((new Date()).toString() + " ***** Recycling connection " + String.valueOf(j) + ":");
                        connPool[j].close();
                        createConn(j);
                    }
                    catch(SQLException sqlexception1)
                    {
                        log.println("Failed: " + sqlexception1);
                        connStatus[j] = 0;
                    }
                }
                finally
                {
                    try
                    {
                        if(statement != null)
                            statement.close();
                    }
                    catch(SQLException _ex) { }
                }
            }

            try
            {
                Thread.sleep(2000L);
                System.out.println("\n\nDB POOL : SIZE>> "+this.getSize()+" USE COUNT >> "+this.getUseCount()+"\n\n");
                for(int i=0; i<this.getSize(); i++) {
                    System.out.println("DB CONN "+i+" AGE >>> "+(System.currentTimeMillis() - connLockTime[i]));
                }
            }
            catch(InterruptedException _ex)
            {
                return;
            }
        }
    }

    public Connection getConnection()
    {
        Connection connection = null;
        if(available)
        {
            boolean flag = false;
            for(int i = 1; i <= 10; i++)
            {
                try
                {
                    int j = 0;
                    int k = connLast + 1;
                    if(k >= currConnections)
                        k = 0;
                    do
                        synchronized(connStatus)
                        {
                            if(connStatus[k] < 1 && !connPool[k].isClosed())
                            {
                                connection = connPool[k];
                                connStatus[k] = 1;
                                connLockTime[k] = System.currentTimeMillis();
                                connLast = k;
                                flag = true;
                                break;
                            }
                            j++;
                            if(++k >= currConnections)
                                k = 0;
                        }
                    while(!flag && j < currConnections);
                }
                catch(SQLException _ex) { }
                if(flag)
                    break;
                synchronized(this)
                {
                    if(currConnections < maxConns)
                        try
                        {
                            createConn(currConnections);
                            currConnections++;
                        }
                        catch(SQLException sqlexception)
                        {
                            log.println("Unable to create new connection: " + sqlexception);
                        }
                }
                try
                {
                    Thread.sleep(2000L);
                }
                catch(InterruptedException _ex) { }
                log.println("-----> Connections Exhausted!  Will wait and try again in loop " + String.valueOf(i));
            }

        } else
        {
            log.println("Unsuccessful getConnection() request during destroy()");
        }
        return connection;
    }

    public int idOfConnection(Connection connection)
    {
        String s;
        try
        {
            s = connection.toString();
        }
        catch(NullPointerException _ex)
        {
            s = "none";
        }
        int i = -1;
        for(int j = 0; j < currConnections; j++)
        {
            if(!connID[j].equals(s))
                continue;
            i = j;
            break;
        }

        return i;
    }

    public String freeConnection(Connection connection)
    {
        String s = "";
        int i = idOfConnection(connection);
        if(i >= 0)
        {
            connStatus[i] = 0;
            s = "freed " + connection.toString();
        } else
        {
            log.println("----> Could not free connection!!!");
        }
        return s;
    }

    public long getAge(Connection connection)
    {
        int i = idOfConnection(connection);
        return System.currentTimeMillis() - connLockTime[i];
    }

    private void createConn(int i)
        throws SQLException
    {
        Date date = new Date();
        String strDate = simpledateformat.format(date);
        try
        {
            Class.forName(dbDriver).newInstance();
            connPool[i] = DriverManager.getConnection(dbURL, dbLogin, dbPassword);

            connStatus[i] = 0;
            connID[i] = connPool[i].toString();
            connLockTime[i] = 0L;
            connCreateDate[i] = date.getTime();
        }
        catch(ClassNotFoundException _ex) {
        }
        catch(Exception e) {
        }
        log.println(strDate + " Opening connection " + String.valueOf(i) + " " + connPool[i].toString() + ":");
    }

    public void destroy(int i)
        throws SQLException
    {
        available = false;
        runner.interrupt();
        try
        {
            runner.join(i);
        }
        catch(InterruptedException _ex) { }
        int j;
        for(long l = System.currentTimeMillis(); (j = getUseCount()) > 0 && System.currentTimeMillis() - l <= (long)i;)
            try
            {
                Thread.sleep(500L);
            }
            catch(InterruptedException _ex) { }

        for(int k = 0; k < currConnections; k++)
            try
            {
                connPool[k].close();
            }
            catch(SQLException _ex)
            {
                log.println("Cannot close connections on Destroy");
            }

        if(j > 0)
        {
            String s = "Unsafe shutdown: Had to close " + j + " active DB connections after " + i + "ms";
            log.println(s);
            log.close();
            throw new SQLException(s);
        } else
        {
            log.close();
            return;
        }
    }

    public void destroy()
    {
        try
        {
            destroy(10000);
            return;
        }
        catch(SQLException _ex)
        {
            return;
        }
    }

    public int getUseCount()
    {
        int i = 0;
        synchronized(connStatus)
        {
            for(int j = 0; j < currConnections; j++)
                if(connStatus[j] > 0)
                    i++;

        }
        return i;
    }

    public int getSize()
    {
        return currConnections;
    }
}
