/*
 * Form Name  	:  FrmMemberGroup.java
 * Created on 	:  [date] [time] AM/PM
 *
 * @author  	:  [authorName]
 * @version  	:  [version]
 */
/**
 * *****************************************************************
 * Class Description : [project description ... ] Imput Parameters : [input
 * parameter ...] Output : [output ...]
 ******************************************************************
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

public class FrmMemberGroup extends FRMHandler implements I_FRMInterface, I_FRMType {

    private MemberGroup memberGroup;

    public static final String FRM_NAME_MEMBERGROUP = "FRM_NAME_MEMBERGROUP";

    public static final int FRM_FIELD_MEMBER_GROUP_ID = 0;
    public static final int FRM_FIELD_DISCOUNT_TYPE_ID = 1;
    public static final int FRM_FIELD_PRICE_TYPE_ID = 2;
    public static final int FRM_FIELD_CODE = 3;
    public static final int FRM_FIELD_NAME = 4;
    public static final int FRM_FIELD_DESCRIPTION = 5;
    public static final int FRM_FIELD_GROUP_TYPE = 6;
   public static final int FRM_FIELD_TYPE_POINT = 7;
    public static final int FRM_FIELD_POINT_IN_CALCULATE = 8;
    public static final int FRM_FIELD_VIEW_CUSTOMER_TYPE = 9;

    public static String[] fieldNames = {
        "FRM_FIELD_MEMBER_GROUP_ID", "FRM_FIELD_DISCOUNT_TYPE_ID",
        "FRM_FIELD_PRICE_TYPE_ID", "FRM_FIELD_CODE",
        "FRM_FIELD_NAME", "FRM_FIELD_DESCRIPTION",
        "FRM_FIELD_GROUP_TYPE", "TYPE_POINT","POINT_IN_CALCULATE","FRM_FIELD_VIEW_CUSTOMER_TYPE"
    };

    public static int[] fieldTypes = {
        TYPE_LONG, TYPE_LONG,
        TYPE_LONG, TYPE_STRING + ENTRY_REQUIRED,
        TYPE_STRING + ENTRY_REQUIRED, TYPE_STRING,
        TYPE_INT, TYPE_INT, TYPE_FLOAT, 
        TYPE_INT 
    };

    public FrmMemberGroup() {
    }

    public FrmMemberGroup(MemberGroup memberGroup) {
        this.memberGroup = memberGroup;
    }

    public FrmMemberGroup(HttpServletRequest request, MemberGroup memberGroup) {
        super(new FrmMemberGroup(memberGroup), request);
        this.memberGroup = memberGroup;
    }

    public String getFormName() {
        return FRM_NAME_MEMBERGROUP;
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

    public MemberGroup getEntityObject() {
        return memberGroup;
    }

    public void requestEntityObject(MemberGroup memberGroup) {
        try {
            this.requestParam();
            memberGroup.setDiscountTypeId(getLong(FRM_FIELD_DISCOUNT_TYPE_ID));
            memberGroup.setPriceTypeId(getLong(FRM_FIELD_PRICE_TYPE_ID));
            memberGroup.setCode(getString(FRM_FIELD_CODE));
            memberGroup.setName(getString(FRM_FIELD_NAME));
            memberGroup.setDescription(getString(FRM_FIELD_DESCRIPTION));
            memberGroup.setGroupType(getInt(FRM_FIELD_GROUP_TYPE));
            memberGroup.setTypePoint(getInt(FRM_FIELD_TYPE_POINT));
            memberGroup.setPointInCalculate(getFloat(FRM_FIELD_POINT_IN_CALCULATE));
            memberGroup.setViewCustomerType(getInt(FRM_FIELD_VIEW_CUSTOMER_TYPE));
        } catch (Exception e) {
            System.out.println("Error on requestEntityObject : " + e.toString());
        }
    }
}
