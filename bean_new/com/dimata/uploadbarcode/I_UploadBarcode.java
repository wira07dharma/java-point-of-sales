package com.dimata.uploadbarcode;

import java.util.Vector;

/**
 * Created by IntelliJ IDEA.
 * User: gadnyana
 * Date: Jul 19, 2007
 * Time: 10:55:48 AM
 * To change this template use File | Settings | File Templates.
 */
public interface I_UploadBarcode {
    public static String className = "com.dimata.posbo.barcodeimpl.BarcodeImplementation";

    public boolean processBarcodeToReceive(UploadBarcode uploadBarcode);
    public boolean processBarcodeToDispatch(UploadBarcode uploadBarcode);
    public boolean processBarcodeToOpname(UploadBarcode uploadBarcode);
    public boolean processBarcodeToReturn(UploadBarcode uploadBarcode);
    public boolean processBarcodeToCosting(UploadBarcode uploadBarcode);
}
