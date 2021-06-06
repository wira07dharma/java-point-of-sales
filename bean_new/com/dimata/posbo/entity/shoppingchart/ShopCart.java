/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dimata.posbo.entity.shoppingchart;

import com.dimata.posbo.entity.masterdata.Material;
import com.dimata.posbo.entity.masterdata.PriceTypeMapping;
import com.dimata.posbo.entity.masterdata.PstMaterial;
import java.util.Iterator;
import java.util.Vector;

/**
 *
 * @author Punia
 */
public class ShopCart {

//keranjang belanja
    private Vector vCart = new Vector();
//untuk mengetahui total jumlah barang yang ada di shooping cart.
    private int iCartContent;
    private int status = 1;

    public Vector getCart() {
        return vCart;
    }

    public int isEmpty() {
        return status;
    }

    public double getTotal() {
        double price = 0;
        int qty = 0;
        double tes = 0;
        double total = 0;
        for (int i = 0; i < vCart.size(); i++) {
            Vector temp = (Vector) vCart.get(i);
            PriceTypeMapping priceTypeMapping = (PriceTypeMapping) temp.get(1);
            qty = (Integer) temp.get(2);
            price = priceTypeMapping.getPrice();
            tes = qty * price;
            total = total + tes;
        }
        return total;
    }

    public int getCartContentNumber() {
        int contentNumber = 0;
        for (int i = 0; i < vCart.size(); i++) {
            Vector temp = (Vector) vCart.get(i);
            int qty = (Integer) temp.get(2);
            contentNumber = contentNumber + qty;
        }
        return contentNumber;

    }
//fungsi untuk menambahkan ke shopping cart

    public boolean addToCart(String product_id, int num) {

        return addToCart(product_id, num, "", "", 0);

    }

    public boolean addToCart(String product_id, int num, String note, String spesialRequest, long cashBillMainId) {
        status = 0;
        if (this.isAdded(product_id) == 1) {
            for (int i = 0; i < vCart.size();) {
                Vector temp = (Vector) vCart.get(i);
                Material material = (Material) temp.get(0);
                String book_productid = String.valueOf(material.getMaterialId());
                if (book_productid.equals(product_id)) {
                    int amount = (Integer) temp.get(2);
                    amount = amount + num;
                    temp.set(2, amount);
                    vCart.set(i, temp);
                    break;
                } else {
                    i++;
                }
            }
        } else {
            Vector listData = PstMaterial.fetchShopingCart(product_id);
            Vector vt = (Vector) listData.get(0);
            Material material = (Material) vt.get(0);
            PriceTypeMapping priceTypeMapping = (PriceTypeMapping) vt.get(1);
            Vector vCartContent = new Vector();
            Integer amount = new Integer(num);
            vCartContent.addElement(material);
            vCartContent.addElement(priceTypeMapping);
            vCartContent.addElement(amount);
            vCartContent.addElement(note);
            vCartContent.addElement(spesialRequest);
            vCartContent.addElement(cashBillMainId);
            vCart.addElement(vCartContent);
            //update total jumlah barang yang ada di shopping cart
            iCartContent = iCartContent + amount.intValue();
        }
        return true;
    }
//menghapus barang dari shopping cart berdasarkan product_id

    public void setAmount(String product_id, int qty) {
        for (int i = 0; i < vCart.size();) {
            Vector temp = (Vector) vCart.get(i);
            Material material = (Material) temp.get(0);
            String book_productid = material.getMaterialId();
            if (book_productid.equals(product_id)) {
                int amount = (Integer) temp.get(2);
                amount = qty;
                temp.set(2, amount);
                vCart.set(i, temp);
                break;
            } else {
                i++;
            }
        }
    }

    public void removeFromCart(String product_id) {
        for (int i = 0; i < vCart.size(); i++) {
            Vector temp = (Vector) vCart.get(i);
            Material material = (Material) temp.get(0);
            PriceTypeMapping priceTypeMapping = (PriceTypeMapping) temp.get(1);
            Integer amount = (Integer) temp.get(2);
            String book_productid = material.getMaterialId();
            if (book_productid.equals(product_id)) {
                vCart.removeElementAt(i);
                break;
            }
        }
    }

// untuk mengecek apakah barang sudah terdapat di shopping cart, jika sudah ada maka return true jika belum ada maka return false
    public boolean isExist(String cartID) {
        Iterator it = vCart.iterator();
        while (it.hasNext()) {
            Vector vCartContent = (Vector) it.next();
            Material material = (Material) vCartContent.elementAt(0);
            String productID = material.getMaterialId();
            if (productID.equals(cartID)) {
                return true;
            }
        }
        return false;
    }

    public int isAdded(String product_id) {
        int i;
        int k = 0;
        for (i = 0; i < vCart.size(); i++) {
            Vector temp = (Vector) vCart.get(i);
            Material material = (Material) temp.get(0);
            String book_productid = material.getMaterialId();
            if(material.getEditMaterial()==3){
                   k=0;
            }else{
                if (book_productid.equals(product_id)) {
                    k = 1;
                    break;
                } else {
                    k = 0;
                }
            }    
        }
        return k;
    }

    public void destroyCart() {
        vCart = new Vector();
    }
}
