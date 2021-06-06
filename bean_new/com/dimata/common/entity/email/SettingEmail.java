/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.dimata.common.entity.email;

/**
 *
 * @author dimata005
 */
import com.dimata.qdep.entity.*;

public class SettingEmail extends Entity{
private String emailName="";
private String password="";
private String host="";
private int number= 0 ;
private String port;

    /**
     * @return the emailName
     */
    public String getEmailName() {
        return emailName;
    }

    /**
     * @param emailName the emailName to set
     */
    public void setEmailName(String emailName) {
        this.emailName = emailName;
    }

    /**
     * @return the password
     */
    public String getPassword() {
        return password;
    }

    /**
     * @param password the password to set
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * @return the host
     */
    public String getHost() {
        return host;
    }

    /**
     * @param host the host to set
     */
    public void setHost(String host) {
        this.host = host;
    }

    /**
     * @return the port
     */
    public String getPort() {
        return port;
    }

    /**
     * @param port the port to set
     */
    public void setPort(String port) {
        this.port = port;
    }

    /**
     * @return the number
     */
    public int getNumber() {
        return number;
    }

    /**
     * @param number the number to set
     */
    public void setNumber(int number) {
        this.number = number;
    }



}
