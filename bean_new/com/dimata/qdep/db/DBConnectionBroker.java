package com.dimata.qdep.db;



import java.io.*;

import java.sql.*;

import java.text.DateFormat;

import java.text.SimpleDateFormat;

import java.util.Date;





public class DBConnectionBroker

    implements Runnable

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

    private long lastLoopMls = System.currentTimeMillis();

    private long threadSleep = 10000L;



    public DBConnectionBroker(String driver, String url, String user, String passwd, int curConn, int maxConn, String logFileName, double day)

    throws IOException

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

            log = new PrintWriter(new FileOutputStream(logFileName), true);System.out.println(">>> logFileName: "+logFileName);

        }

        catch(IOException _ex)

        {
            System.out.println(_ex);
            try

            {

                log = new PrintWriter(new FileOutputStream("DCB_" + System.currentTimeMillis() + ".log"), true);

            }

            catch(IOException _ex2)

            {
                System.out.println(">>>> EXCEPTION "+ logFileName);
                System.out.println(_ex2);
                log=null;//throw new IOException("Can't open any log file");

            }

        }

        

        // SimpleDateFormat simpledateformat = new SimpleDateFormat("yyyy.MM.dd G 'at' hh:mm:ss a zzz");

        simpledateformat = new SimpleDateFormat("MMM dd yyyyy hh:mm:ss");



        Date date = com.dimata.util.DateCalc.getDate();

        pid = simpledateformat.format(date);

        BufferedWriter bufferedwriter = new BufferedWriter(new FileWriter(logFileName + "pid"));

        bufferedwriter.write(pid);

        bufferedwriter.close();

        if(log!=null){writelnLog("Starting DBConnectionBroker Version 1.0");

        writelnLog("dbDriver = " + driver);

        writelnLog("dbURL = " + url);

        writelnLog("dbLogin = " + user);

        writelnLog("dbPasswd = **********");



        writelnLog("logfile = " + logFileName);

        writelnLog("minconnections = " + curConn);

        writelnLog("maxconnections = " + maxConn);

        writelnLog("Total refresh interval = " + day + " days");

        writelnLog("-----------------------------------------");}

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

                    if(log!=null){writelnLog("--->Attempt (" + String.valueOf(k) + " of " + String.valueOf(byte0) + ") failed to create new connections set at startup: ");

                    writelnLog("    " + sqlexception);

                    writelnLog("    Will try again in 15 seconds...");}

                    try

                    {

                        Thread.sleep(15000L);

                    }

                    catch(InterruptedException _ex) { }

                    k++;

                }



            if(!flag)

            {

                if(log!=null){writelnLog("\r\nAll attempts at connecting to Database exhausted");}

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

                    if(log!=null){log.close();}

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

                if(log!=null){writelnLog("Can't read the file for pid info: " + logFileString + "pid");}

            }

            

            for(int i = 0; i < currConnections; i++)

                try

                {

                    currSQLWarning = connPool[i].getWarnings();

                    if(currSQLWarning != null)

                    {

                        if(log!=null){writelnLog("Warnings on connection " + String.valueOf(i) + " " + currSQLWarning);}

                        connPool[i].clearWarnings();

                    }

                }

                catch(SQLException sqlexception)

                {

                    if(log!=null){writelnLog("Cannot access Warnings: " + sqlexception);}

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

                    if(connPool[j].isClosed()){

                        System.out.println("CONN POOL run=> connPool["+j+"].isClosed()");

                        throw new SQLException();



                    }

                }

                catch(SQLException _ex)

                {

                    try

                    {

                        if(log!=null){writelnLog((com.dimata.util.DateCalc.getDate()).toString() + " ***** Recycling connection " + String.valueOf(j) + ":");}

                        connPool[j].close();

                        createConn(j);

                    }

                    catch(SQLException sqlexception1)

                    {

                        writelnLog("Failed: " + sqlexception1);

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

                lastLoopMls= System.currentTimeMillis();

                Thread.sleep(threadSleep);

                //System.out.println("\nDB POOL : SIZE>> "+this.getSize()+" USE COUNT >> "+this.getUseCount());

                /*for(int i=0; i<this.getSize(); i++) {

                    System.out.println("DB CONN "+i+" AGE >>> "+(System.currentTimeMillis() - connLockTime[i]));

                }*/

            }

            catch(InterruptedException _ex)

            {

                return;

            }

        }

    }


    /**
     * cek disini untuk email otomatis jika ada koneksi yang bermasalah.
     * @return 
     */
    public Connection getConnection()

    {
        //System.out.println(">> QDEP Max Conn:"+this.maxConns+"Min:"+this.minConns+" Size:"+getSize()+" Used:"+getUseCount());
        Connection connection = null;

        //this.checkConnPoolStatus();

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
                            //UPDATED BY DEWOK 2019-10-08 ATAS PERINTAH YANG MAHA KUASA
                            //if(connStatus[k] < 1 && !connPool[k].isClosed())
                            if( connStatus[k] < 1 && connPool[k]!=null &&  !connPool[k].isClosed())
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

                            if(log!=null){
                                writelnLog("Unable to create new connection: " + sqlexception);
                            }
                            
                            System.out.println("Unable to create new connection: " + sqlexception);

                        }

                }

                try

                {

                    Thread.sleep(2000L);

                }

                catch(InterruptedException _ex) { }

                if(log!=null){writelnLog("-----> Connections Exhausted!  Will wait and try again in loop " + String.valueOf(i));}
                System.out.println("-----> Connections Exhausted!  Will wait and try again in loop " + String.valueOf(i));

            }



        } else {

            if(log!=null){writelnLog("Unsuccessful getConnection() request during destroy()");}

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
            //UPDATED BY DEWOK 2019-10-08 ATAS PERINTAH YANG MAHA KUASA
            //if(!connID[j].equals(s))
            if(connID[j]!=null && !connID[j].equals(s))

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
            /*try{
               connection.close();// Add by KARTIKA 2011-11-25
            }catch(Exception exc){
               System.out.println(exc);
            }*/
            s = "freed " + connection.toString();

            //System.out.println(">>> Freed Connection "+i);

        } else

        {

            writelnLog("----> Could not free connection!!!");

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

        Date date = com.dimata.util.DateCalc.getDate();

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

        catch(ClassNotFoundException ex1) {
            System.out.println("DBClassNotFoundException: " + ex1);
        }

         catch (SQLException ex) {
            // handle any errors
            System.out.println("SQLException: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState());
            System.out.println("VendorError: " + ex.getErrorCode());
        }

        catch(Exception e) {
          System.out.println("EXCEPTION ");
        }

        //writelnLog(strDate + " Opening connection " + String.valueOf(i) + " " + connPool[i].toString() + ":");

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

                writelnLog("Cannot close connections on Destroy");

            }



        if(j > 0)

        {

            String s = "Unsafe shutdown: Had to close " + j + " active DB connections after " + i + "ms";

            writelnLog(s);

            if(log!=null){log.close();}

            throw new SQLException(s);

        } else

        {

            if(log!=null){log.close();}

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

    

    /**

     * Fungsi untuk re-running thread

     */

    public synchronized void rerun(){

        System.out.println(">>> RE-RUNNING DB CONNECTION BROKER >>> currentTimeMillis: "+System.currentTimeMillis());

        lastLoopMls = System.currentTimeMillis();

        runner = new Thread(this);

        runner.start();

    }





    public int getSize()

    {

        return currConnections;

    }

    

    /**

     * Getter for property lastLoopMls.

     * @return Value of property lastLoopMls.

     */

    public long getLastLoopMls() {

        return lastLoopMls;

    }

    

    /**

     * Setter for property lastLoopMls.

     * @param lastLoopMls New value of property lastLoopMls.

     */

    public void setLastLoopMls(long lastLoopMls) {

        this.lastLoopMls = lastLoopMls;

    }

    

    /**

     * Getter for property threadSleep.

     * @return Value of property threadSleep.

     */

    public long getThreadSleep() {

        return threadSleep;

    }

    

    /**

     * Setter for property threadSleep.

     * @param threadSleep New value of property threadSleep.

     */

    public void setThreadSleep(long threadSleep) {

        this.threadSleep = threadSleep;

    }

    

    public void checkConnPoolStatus() {



        long currentTimeMillis = System.currentTimeMillis();



        System.out.println("\ncurrentTimeMillis - lastLoopMlsConn>>> "+currentTimeMillis+"-"+lastLoopMls+" = "+(currentTimeMillis-lastLoopMls));

        System.out.println("threadSleep>>> "+threadSleep);



        if ( (currentTimeMillis-lastLoopMls>threadSleep) && (currentTimeMillis>lastLoopMls) ){

            System.out.println(">>> CONN POOL IS DEAD => RERUN !");

            this.rerun();

        } else{

            System.out.println(">>> CONN POOL IS OK");

        }

    }

    private void writelnLog(String str){
        try{
        if(log!=null){
            log.println(str);
        } else {
            System.out.println(str);
        }
        }
        catch(Exception exc){
            System.out.println(exc);
        }
    }
    

}

