/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.dimata.posbo.entity.masterdata;

/**
 *
 * @author Dimata
 */

import java.io.*;
import java.sql.*;
import java.util.*;
import java.util.Date;

/* package qdep */
import com.dimata.util.lang.I_Language;
import com.dimata.posbo.db.*;
import com.dimata.qdep.entity.*;
import com.dimata.posbo.entity.masterdata.*;


public class coba extends DBHandler  {

    public static void main(String[] argv){

        double qty = 0.0;
        double konversiUnit = 0.0;
        double qtyAfterKonversi = 0.0;
        qty = 10;
        long unitFrom = 0;
        long unitTo = 0;
        unitFrom = 50014;
        unitTo = 50073;
        konversiUnit = PstUnit.getUnitFactory(unitFrom, unitTo, 0, 4);
        System.out.println("Hasil konversi unit : "+konversiUnit);
        qtyAfterKonversi = qty * konversiUnit;
        System.out.println("Hasil qty konversi unit : "+konversiUnit);
         
    }

}
