/**
 * Created by IntelliJ IDEA.
 * User: gadnyana
 * Date: Jun 3, 2005
 * Time: 2:22:26 PM
 * To change this template use Options | File Templates.
 */
package com.dimata.posbo.session.masterdata;

import com.dimata.posbo.entity.masterdata.PstMemberReg;
import com.dimata.posbo.entity.masterdata.PstDiscountMapping;
import com.dimata.posbo.entity.masterdata.PstMemberGroup;
import com.dimata.posbo.entity.masterdata.PstPriceTypeMapping;

import java.util.Vector;

public class SessMemberGroup {

    public static boolean readyPriceTypeDataToDelete(long priceTypeId) {
        boolean status = true;
        try {
            // pengecekan di member group
            String where = PstMemberGroup.fieldNames[PstMemberGroup.FLD_PRICE_TYPE_ID] + "=" + priceTypeId;
            Vector vlist = PstMemberGroup.list(0, 0, where, "");
            if (vlist != null && vlist.size() > 0) {
                status = false;
            } else {
                // pengecekan di standart rate
                where = PstPriceTypeMapping.fieldNames[PstPriceTypeMapping.FLD_PRICE_TYPE_ID] + "=" + priceTypeId;
                vlist = PstPriceTypeMapping.list(0, 0, where, "");
                if (vlist != null && vlist.size() > 0) {
                    status = false;
                }
            }
        } catch (Exception e) {
            System.out.println("SessCurrencyType - readyPriceTypeDataToDelete : " + e.toString());
        }
        return status;
    }

    /**
     *
     * @param discountTypeId
     * @return
     */
    public static boolean readyDiscountTypeDataToDelete(long discountTypeId) {
        boolean status = true;
        try {
            // pengecekan di member group
            String where = PstMemberGroup.fieldNames[PstMemberGroup.FLD_DISCOUNT_TYPE_ID] + "=" + discountTypeId;
            Vector vlist = PstMemberGroup.list(0, 0, where, "");
            if (vlist != null && vlist.size() > 0) {
                status = false;
            } else {
                // pengecekan di standart rate
                where = PstDiscountMapping.fieldNames[PstDiscountMapping.FLD_DISCOUNT_TYPE_ID] + "=" + discountTypeId;
                vlist = PstDiscountMapping.list(0, 0, where, "");
                if (vlist != null && vlist.size() > 0) {
                    status = false;
                }
            }
        } catch (Exception e) {
            System.out.println("SessCurrencyType - readyDiscountTypeDataToDelete : " + e.toString());
        }
        return status;
    }


    /**
     *
     * @param memGroupId
     * @return
     */
    public static boolean readyDataToDelete(long memGroupId) {
        boolean status = true;
        try {
            String where = PstMemberReg.fieldNames[PstMemberReg.FLD_MEMBER_GROUP_ID] + "=" + memGroupId;
            Vector vlist = PstMemberReg.list(0, 0, where, "");
            if (vlist != null && vlist.size() > 0) {
                status = false;
            }
        } catch (Exception e) {
            System.out.println("SessLocation - readyDataToDelete : " + e.toString());
        }
        return status;
    }

}
