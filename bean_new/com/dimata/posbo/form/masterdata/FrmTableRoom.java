/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dimata.posbo.form.masterdata;

/* java package */

import java.io.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;
/* qdep package */
import com.dimata.qdep.form.*;
/* project package */
import com.dimata.posbo.entity.masterdata.*;

/**
 *
 * @author Dimata 007
 */
public class FrmTableRoom extends FRMHandler implements I_FRMInterface, I_FRMType {
    
    private TableRoom tableRoom;

    public static final String FRM_NAME_TABLE_ROOM = "FRM_NAME_TABLE_ROOM";
    
        public static final int FRM_FIELD_TABLE_ID = 0;
        public static final int FRM_FIELD_LOCATION_ID = 1;
        public static final int FRM_FIELD_ROOM_ID = 2;
        public static final int FRM_FIELD_TABLE_NUMBER = 3;
        public static final int FRM_FIELD_DIMENTION_L = 4;
        public static final int FRM_FIELD_DIMENTION_W = 5;
        public static final int FRM_FIELD_CAPACITY= 6;
        public static final int FRM_FIELD_GRID_X = 7;
        public static final int FRM_FIELD_GRID_Y = 8;
        public static final int FRM_FIELD_SHAPE = 9;
        public static final int FRM_FIELD_STATUS = 10;

    public static String[] fieldNames =
            {
                "FRM_FIELD_TABLE_ID",
                "FRM_FIELD_LOCATION_ID",
                "FRM_FIELD_ROOM_ID",
                "FRM_FIELD_TABLE_NUMBER",
                "FRM_FIELD_DIMENTION_L",
                "FRM_FIELD_DIMENTION_W",
                "FRM_FIELD_CAPACITY",
                "FRM_FIELD_GRID_X",
                "FRM_FIELD_GRID_Y", 
                "FRM_FIELD_SHAPE",
                "FRM_FIELD_STATUS"
            };

    public static int[] fieldTypes =
            {
            //update opie 21-06-2012 kelebihan
                TYPE_LONG, 
                TYPE_LONG,
                TYPE_LONG,
                TYPE_STRING,
                TYPE_INT,
                TYPE_INT,
                TYPE_INT,
                TYPE_INT,
                TYPE_INT,
                TYPE_STRING,
                TYPE_INT

            };

    public FrmTableRoom() {
    }

    public FrmTableRoom(TableRoom tableRoom) {
        this.tableRoom = tableRoom;
    }

    public FrmTableRoom(HttpServletRequest request, TableRoom tableRoom) {
        super(new FrmTableRoom(tableRoom), request);
        this.tableRoom = tableRoom;
    }

    public String getFormName() {
        return FRM_NAME_TABLE_ROOM;
    }

    public int[] getFieldTypes() {
        return fieldTypes;
    }

    public String[] getFieldNames() {
        return fieldNames;
    }

    public int getFieldSize() {
        return fieldNames.length;
    }

    public TableRoom getEntityObject() {
        return tableRoom;
    }

    public void requestEntityObject(TableRoom tableRoom) {
        try {
            this.requestParam();
            
            tableRoom.setLocationId(getLong(FRM_FIELD_LOCATION_ID));
            tableRoom.setRoomId(getLong(FRM_FIELD_ROOM_ID));
            tableRoom.setTableNumber(getString(FRM_FIELD_TABLE_NUMBER));
            tableRoom.setDimentionL(getInt(FRM_FIELD_DIMENTION_L));
            tableRoom.setDimentionW(getInt(FRM_FIELD_DIMENTION_W));
            tableRoom.setCapacity(getInt(FRM_FIELD_CAPACITY));
            tableRoom.setGridX(getInt(FRM_FIELD_GRID_X));
            tableRoom.setGridY(getInt(FRM_FIELD_GRID_Y));
            tableRoom.setShape(getString(FRM_FIELD_SHAPE));
            tableRoom.setStatus(getInt(FRM_FIELD_STATUS));
            
        } catch (Exception e) {
            System.out.println("Error on requestEntityObject : " + e.toString());
        }
    }
    
}
