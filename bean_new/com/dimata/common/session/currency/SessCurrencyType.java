/**
 * Created by IntelliJ IDEA.
 * User: gadnyana
 * Date: Jun 6, 2005
 * Time: 11:23:29 AM
 * To change this template use Options | File Templates.
 */
package com.dimata.common.session.currency;

import com.dimata.common.entity.payment.PstDailyRate;
import com.dimata.common.entity.payment.PstStandartRate;
import com.dimata.posbo.entity.masterdata.PstDiscountMapping;
import com.dimata.posbo.entity.masterdata.PstMemberGroup;
import com.dimata.posbo.entity.masterdata.PstPriceTypeMapping;

import java.util.Vector;

public class SessCurrencyType {

    /**
     * @return
     */
    public static boolean readyDataToDelete(long currTypeId) {
        boolean status = true;
        try {
            // pengecekan di daily rate
            String where = PstDailyRate.fieldNames[PstDailyRate.FLD_CURRENCY_TYPE_ID] + "=" + currTypeId;
            Vector vlist = PstDailyRate.list(0, 0, where, "");
            if (vlist != null && vlist.size() > 0) {
                status = false;
            } else {
                // pengecekan di standart rate
                where = PstStandartRate.fieldNames[PstStandartRate.FLD_CURRENCY_TYPE_ID] + "=" + currTypeId;
                vlist = PstStandartRate.list(0, 0, where, "");
                if (vlist != null && vlist.size() > 0) {
                    status = false;
                } else {
                    // pengecekan d
                    where = PstDiscountMapping.fieldNames[PstDiscountMapping.FLD_CURRENCY_TYPE_ID] + "=" + currTypeId;
                    vlist = PstDiscountMapping.list(0, 0, where, "");
                    if (vlist != null && vlist.size() > 0) {
                        status = false;
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("SessCurrencyType - readyDataToDelete : " + e.toString());
        }
        return status;
    }

    /**
     *
     * @param priceTypeId
     * @return
     */
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

}
