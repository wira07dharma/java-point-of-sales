/*
 * I_TransactionType.java
 *
 * Created on August 25, 2005, 10:30 AM
 */

package com.dimata.interfaces.trantype;

/**
 *
 * @author  adnyana
 */
public interface I_TransactionType {

    public static final int TIPE_TRANSACTION_CASH = 0;
    public static final int TIPE_TRANSACTION_KREDIT = 1;
    public static final int TIPE_TRANSACTION_AWAL = 2;
    public static final int TIPE_TRANSACTION_WRITE_OFF = 3;
    public static final int TIPE_TRANSACTION_DONOR = 4;
    public static String[][] arrTransactionTypeNames =
            {
                {
                    "TUNAI",
                    "KREDIT",
                    "AWAL",
                    "PENGHAPUSAN",
                    "SUMBANGAN"
                },
                {
                    "CASH",
                    "CREDIT",
                    "START PERIOD",
                    "WRITE OFF",
                    "DONATED"
                }
                ,
                {
                    "XX1",
                    "XX2",
                    "xx3",
                    "XX4",
                    "XX5"
                }
            };

}
