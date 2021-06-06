/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dimata.posbo.session.purchasing;

import com.dimata.common.entity.contact.ContactList;
import com.dimata.common.entity.location.Location;
import com.dimata.posbo.entity.masterdata.MatCurrency;
import com.dimata.posbo.entity.masterdata.MatVendorPrice;
import com.dimata.posbo.entity.masterdata.Material;
import com.dimata.posbo.entity.masterdata.PstUnit;
import com.dimata.posbo.entity.masterdata.Unit;
import com.dimata.posbo.entity.purchasing.PurchaseOrder;
import com.dimata.posbo.entity.purchasing.PurchaseOrderItem;
import com.dimata.posbo.entity.purchasing.PurchaseRequest;
import com.dimata.posbo.entity.purchasing.PurchaseRequestItem;
import com.dimata.qdep.form.FRMHandler;
import com.dimata.util.Formater;
import java.util.Date;
import java.util.Vector;

/**
 *
 * @author dimata005
 */
public class SessFormatEmailQueenTandoor {
    public static String getContentEmail(ContactList contactList , Location location,PurchaseOrder purchOrder, Vector listItemOrder) {
        
                    Date dateOrder = new Date();
                    String content="";
                    Date deleveryDate= new Date();
                    try{
                        deleveryDate.setDate(deleveryDate.getDate()+ contactList.getTermOfDelivery());
                    }catch(Exception ex){
                        deleveryDate= new Date();
                    }
                    content=content+"<table style=\"TEXT-TRANSFORM: none; TEXT-INDENT: 0px; FONT-FAMILY: 'Times New Roman'; LETTER-SPACING: normal; WORD-SPACING: 0px; -webkit-text-stroke-width: 0px\" cellpadding=\"0\" cellspacing=\"50\" bgcolor=\"#ff9900\" border=\"0\" width=\"100%\">";
                    content=content+"<tbody>";
                    content=content+"<tr>";
                    content=content+"<td align=\"center\">";
                    content=content+"<div style=\"MARGIN: 0px auto; WIDTH: 210mm; FONT-FAMILY: Arial, Helvetica, sans-serif;HEIGHT: auto\">";
                    content=content+"<table style=\"MARGIN-BOTTOM: 10px; COLOR: rgb(255,255,255); FONT-SIZE: 12px\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" width=\"100%\">";
                    content=content+"<tbody>";
                    content=content+"<tr>";
                    content=content+"<td align=\"left\" valign=\"top\">Purchase Order, "+Formater.formatDate(purchOrder.getPurchDate(), "dd/MM/yyyy")+"</td>"; //date purchase
                    content=content+"<td align=\"right\" valign=\"top\"><a moz-do-not-send=\"true\" style=\"COLOR: rgb(255,255,255)\"  href=\"http://twitter.com/queensbali\" target=\"_blank\">follow on Twitter</a><span class=\"Apple-converted-space\">&nbsp;</span>|<span class=\"Apple-converted-space\">&nbsp;</span><a moz-do-not-send=\"true\"  style=\"COLOR: rgb(255,255,255)\" href=\"http://facebook.com/queensbali\" target=\"_blank\">friend on Facebook</a></td>";
                    content=content+"</tr>";
                    content=content+"</tbody>";
                    content=content+"</table>";
                    content=content+"<table cellpadding=\"15\" cellspacing=\"0\" bgcolor=\"#ffffff\" border=\"0\" width=\"100%\">";
                      content=content+"<tbody>";
                        content=content+"<tr>";
                          content=content+"<td>";
                            content=content+"<table cellpadding=\"0\" cellspacing=\"0\" border=\"0\" width=\"100%\">";
                              content=content+"<tbody>";
                                content=content+"<tr>";
                                  content=content+"<td align=\"center\"><img  moz-do-not-send=\"true\" src=\"http://accounts.queenstandoor.com/images/queens-head.jpg\"  height=\"121\" width=\"300\"></td>";
                                content=content+"</tr>";
                              content=content+"</tbody>";
                            content=content+"</table>";
                            content=content+"<hr>";
                            content=content+"<table cellpadding=\"0\"  cellspacing=\"0\" border=\"0\" width=\"100%\">";
                              content=content+"<tbody>";
                                content=content+"<tr>";
                                  content=content+"<td align=\"left\"><font size=\"+2\"><strong><u>PURCHASE ORDER</u></strong><span class=\"Apple-converted-space\">&nbsp;</span></font><br></td>";
                                  content=content+"<td style=\"FONT-SIZE: 12px\" align=\"right\" valign=\"top\"><strong>Seminyak,</strong><span class=\"Apple-converted-space\">&nbsp;</span>"+Formater.formatDate(dateOrder, "dd/MM/yyyy")+"</td>"; //untuk tanggal order
                                content=content+"</tr>";
                              content=content+"</tbody>";
                            content=content+"</table>";
                            content=content+"<table style=\"MARGIN-TOP: 10px; WIDTH: 763px; FONT-SIZE: 12px\" cellpadding=\"3\" cellspacing=\"0\"  border=\"0\">";
                              content=content+"<tbody>";
                                content=content+"<tr>";
                                  content=content+"<td style=\"BORDER-BOTTOM: rgb(0,0,0) 1px dotted\" align=\"left\" width=\"11%\"><strong>Code PO #</strong></td>";
                                  content=content+"<td style=\"BORDER-BOTTOM: rgb(0,0,0) 1px dotted\" align=\"left\" width=\"4%\">:</td>";
                                  content=content+"<td style=\"BORDER-BOTTOM: rgb(0,0,0) 1px dotted\" align=\"left\" width=\"85%\">"+purchOrder.getPoCode()+"</td>";
                                content=content+"</tr>";
                                content=content+"<tr>";
                                  content=content+"<td style=\"BORDER-BOTTOM:rgb(0,0,0) 1px dotted\" align=\"left\"><strong>Date</strong></td>";
                                  content=content+"<td style=\"BORDER-BOTTOM:rgb(0,0,0) 1px dotted\" align=\"left\">:</td>";
                                  content=content+"<td style=\"BORDER-BOTTOM: rgb(0,0,0) 1px dotted\" align=\"left\" width=\"85%\">"+Formater.formatDate(purchOrder.getPurchDate(), "dd/MM/yyyy")+"</td>";
                                content=content+"</tr>";
                                content=content+"<tr>";
                                  content=content+"<td style=\"BORDER-BOTTOM:rgb(0,0,0) 1px dotted\" align=\"left\"><strong>Delivery Date</strong></td>";
                                  content=content+"<td style=\"BORDER-BOTTOM:rgb(0,0,0) 1px dotted\" align=\"left\">:</td>";
                                  content=content+"<td style=\"BORDER-BOTTOM: rgb(0,0,0) 1px dotted\"  align=\"left\" width=\"85%\">"+Formater.formatDate(deleveryDate, "dd/MM/yyyy")+"</td>";
                                content=content+"</tr>";
                              content=content+"</tbody>";
                            content=content+"</table>";
                            content=content+"<table style=\"MARGIN-TOP: 10px\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" width=\"100%\">";
                              content=content+"<tbody>";
                                content=content+"<tr>";
                                  content=content+"<td align=\"left\" width=\"50%\">";
                                    content=content+"<table style=\"BORDER-BOTTOM: rgb(0,0,0) 1px dotted; BORDER-LEFT: rgb(0,0,0) 1px dotted; FONT-SIZE: 12px; BORDER-TOP: rgb(0,0,0) 1px dotted; BORDER-RIGHT: rgb(0,0,0) 1px dotted\" cellpadding=\"0\" cellspacing=\"3\" border=\"0\" width=\"95%\">";
                                      content=content+"<tbody>";
                                        content=content+"<tr>";
                                          content=content+"<td><font size=\"+1\"><strong>Ship / Deliver To</strong></font></td>";
                                        content=content+"</tr>";
//                                        content=content+"<tr>";
//                                        content=content+"<td>Pusat Gudang</td>";
//                                        content=content+"</tr>";
                                        content=content+"<tr>";
                                        content=content+"<td>"+location.getName()+"</td>";
                                        content=content+"</tr>";
                                        content=content+"<tr>";
                                        content=content+"<td>"+location.getAddress()+"</td>";
                                        content=content+"</tr>";
                                        content=content+"<tr>";
                                        content=content+"<td>Phone/Fax : "+location.getTelephone()+"/"+location.getFax()+"</td></tr>";
                                        content=content+"<tr>";
                                          content=content+"<td>Email :<span class=\"Apple-converted-space\">&nbsp;</span><a moz-do-not-send=\"true\" href=\"mailto:"+location.getEmail()+"\">"+location.getEmail()+"</a></td>";
                                        content=content+"</tr>";
                                      content=content+"</tbody>";
                                    content=content+"</table>";
                                  content=content+"</td>";
                                  content=content+"<td width=\"50%\">";
                                    content=content+"<table style=\"BORDER-BOTTOM: rgb(0,0,0) 1px dotted;BORDER-LEFT: rgb(0,0,0) 1px dotted; FONT-SIZE:  12px; BORDER-TOP: rgb(0,0,0) 1px dotted; BORDER-RIGHT: rgb(0,0,0) 1px dotted\" cellpadding=\"0\" cellspacing=\"3\" border=\"0\" width=\"100%\">";
                                      content=content+"<tbody>";
                                        content=content+"<tr>";
                                          content=content+"<td><font size=\"+1\"><strong>To</strong></font></td>";
                                        content=content+"</tr>";
                                        content=content+"<tr>";
                                          content=content+"<td><strong>Company :</strong><span class=\"Apple-converted-space\">&nbsp;</span>"+contactList.getCompName()+"</td>";
                                        content=content+"</tr>";
                                        content=content+"<tr>";
                                          content=content+"<td><strong>Contact Name :</strong><span class=\"Apple-converted-space\">&nbsp;</span>"+contactList.getPersonName()+"</td>";
                                        content=content+"</tr>";
                                        content=content+"<tr>";
                                          content=content+"<td><strong>Department :</strong><span class=\"Apple-converted-space\">&nbsp;</span>Account  Department</td>";
                                        content=content+"</tr>";
                                        content=content+"<tr>";
                                          content=content+"<td><strong>Address :</strong><span class=\"Apple-converted-space\">&nbsp;</span>"+contactList.getBussAddress()+"</td>";
                                        content=content+"</tr>";
                                        content=content+"<tr>";
                                          content=content+"<td><strong>Email :</strong><span class=\"Apple-converted-space\">&nbsp;</span>"+contactList.getEmail()+"</td>";
                                        content=content+"</tr>";
                                      content=content+"</tbody>";
                                    content=content+"</table>";
                                  content=content+"</td>";
                                content=content+"</tr>";
                              content=content+"</tbody>";
                            content=content+"</table> <div style=\"MARGIN-TOP: 10px\"><small><i>We are pleased to confirm our purchase order :</i></small>";
                              content=content+"<table style=\"MARGIN-TOP: 5px;  FONT-SIZE: 10px\" cellpadding=\"2\"  cellspacing=\"1\" bgcolor=\"#000000\" border=\"0\" width=\"100%\">";
                                content=content+"<tbody>";
                                    content=content+"<tr>";
                                    content=content+"<td align=\"center\" bgcolor=\"#eeeeee\" height=\"28\" width=\"4%\"><strong>#</strong></td>";
                                    content=content+"<td align=\"center\" bgcolor=\"#eeeeee\"  width=\"11%\"><strong>PRODUCT ID</strong></td>";
                                    content=content+"<td align=\"center\"  bgcolor=\"#eeeeee\" width=\"21%\"><strong>ITEM NAME</strong></td>";
                                    content=content+"<td align=\"center\"  bgcolor=\"#eeeeee\" width=\"22%\"><strong>DESCRIPTION</strong></td>";
                                    content=content+"<td align=\"center\"  bgcolor=\"#eeeeee\" width=\"7%\"><strong>QTY</strong></td>";
                                    content=content+"<td align=\"center\"  bgcolor=\"#eeeeee\" width=\"14%\"><strong>UNIT</strong></td>";
                                    content=content+"<td align=\"center\"  bgcolor=\"#eeeeee\" width=\"21%\"><strong>UNIT PRICE</strong></td>";
                                    content=content+"<td align=\"center\" bgcolor=\"#eeeeee\"  width=\"14%\"><strong>AMOUNT</strong></td>";
                                    
                                    content=content+"</tr>";
                                    
                                    //looping untuk deatail item
                                    double totalPo=0.0;
                                    double total=0.0;
                                    int start=0;
                                    MatCurrency matCurr = new MatCurrency();
                                    if(listItemOrder.size()>0){
                                         for (int i = 0; i < listItemOrder.size(); i++){
                                             Vector v1 = (Vector)listItemOrder.get(i);
                                             PurchaseOrderItem poitem = (PurchaseOrderItem) v1.get(0);
                                             Material mat = (Material) v1.get(1);
                                             Unit matUnit = (Unit) v1.get(2);
                                             matCurr = (MatCurrency) v1.get(3);
                                             MatVendorPrice matVendorPrice = (MatVendorPrice) v1.get(4);
                                             
                                             totalPo=totalPo+poitem.getPriceKonv();
                                             
                                             total=total+(poitem.getPriceKonv()*poitem.getQtyRequest());
                                             start=start+1;
                                             content=content+"<tr bgcolor=\"#ffffff\">";
                                             content=content+"<td align=\"center\">"+start+"</td>";
                                             content=content+"<td align=\"center\">"+matVendorPrice.getVendorPriceCode()+"</td>";
                                             content=content+"<td>"+mat.getName()+"</td>";
                                             content=content+"<td>&nbsp;</td>";
                                             content=content+"<td align=\"center\">"+poitem.getQtyRequest()+"</td>";
                                             content=content+"<td>"+matUnit.getCode()+"</td>";
                                             content=content+"<td align=\"right\">"+matCurr.getCode()+"."+Formater.formatNumber(poitem.getPriceKonv(), "###,###") +"</td>";
                                             content=content+"<td align=\"right\">"+matCurr.getCode()+"."+Formater.formatNumber(poitem.getPriceKonv()*poitem.getQtyRequest(), "###,###") +"</td>";
                                             content=content+"</tr>";
                                             
                                         }
                                    }
                                    

                                    content=content+"<tr bgcolor=\"#ffffff\">";
                                    content=content+"<td colspan=\"5\" align=\"center\">&nbsp;</td>";
                                    content=content+"<td align=\"center\"><strong>GRAND TOTAL</strong></td>";
                                    content=content+"<td align=\"right\" bgcolor=\"#eeeeee\"><strong>"+matCurr.getCode()+"."+Formater.formatNumber(totalPo, "###,###") +"</strong></td>";
                                    content=content+"<td align=\"right\" bgcolor=\"#eeeeee\"><strong>"+matCurr.getCode()+"."+Formater.formatNumber(total, "###,###") +"</strong></td>";
                                    content=content+"</tr>";
                                    
                                content=content+"</tbody>";
                              content=content+"</table>";
                            content=content+"</div>";
                            content=content+"<table style=\"BORDER-BOTTOM: rgb(0,0,0) 1px solid; BORDER-LEFT: rgb(0,0,0) 1px solid; MARGIN-TOP:  10px; FONT-SIZE: 10px; BORDER-TOP: rgb(0,0,0) 1px solid;BORDER-RIGHT: rgb(0,0,0) 1px solid\" cellpadding=\"3\" cellspacing=\"0\" border=\"0\" width=\"100%\">";
                              content=content+"<tbody>";
                              content=content+"</tbody>";
                            content=content+"</table>";
                            content=content+"<table style=\"MARGIN-TOP: 10px; FONT-SIZE: 10px\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" width=\"100%\">";
                              content=content+"<tbody>";
                                content=content+"<tr>";
                                  content=content+"<td valign=\"top\" width=\"50%\"><strong><u>Delivery Policy :</u></strong>";
                                  content=content+"<ol>";
                                      content=content+"<li>Please send two copiesof your invoice</li>";
                                      content=content+"<li>Enter this order in accordance with the price, terms, delivery method, and specifications listed above</li>";
                                      content=content+"<li>Please notify us  immediately if you are unable to ship as  specified</li>";
                                      content=content+"<li>Late delivery will be subject to cancellation</li>";
                                      content=content+"<li>This PO should be  attach with Invoice</li>";
                                      content=content+"<li>All the goods should be delivered with carbonize invoice and  duly attached with approved purchase order.</li>";
                                      content=content+"<li>Supply receiving time of Queen`s Central Mess  is 10:00 hrs. To 17:00 hrs.</li>";
                                      content=content+"<li>That the quality and quantity of the materials shall be as per specification givenby THE PURCHASER as well as samples submitted by THE SUPPLIER and approved by THE PURCHASER.</li>";
                                      content=content+"<li>That the delivery of the materials shall be made by THE SUPPLIER at their own cost,management and responsibility</li>";
                                   content=content+"</ol>";
                                  content=content+"</td>";
                                  content=content+"<td valign=\"top\" width=\"50%\"><strong><u>Payment Policy :</u></strong><ol>";
                                      content=content+"<li>Payments are based on PO, Invoice, and Contract Agreement</li>";
                                      content=content+"<li>Payments will be based on authorised signatureon Receiving Goods</li>";
                                      content=content+"<li>Payment<span class=\"Apple-converted-space\">&nbsp;</span><strong>CASH BASIS</strong><ul>";
                                      content=content+"<li>Cash or chequewill be collected athead Outlet (Queen's TandoorRestaurant, Jl Raya Seminyak No. 73 ) by  Account Payable or General Cashier.</li> </ul></li>";
                                      content=content+"<li>Payment<span class=\"Apple-converted-space\">&nbsp;</span><strong>CREDIT BASIS</strong> <ul>";
                                          content=content+"<li>Payment will be form AP department at Queens Tandoor -  Seminyak as per<span class=\"Apple-converted-space\">&nbsp;</span><strong>Payment Calendar</strong></li>";
                                        content=content+"</ul>";
                                      content=content+"</li>";
                                    content=content+"</ol>";
                                  content=content+"</td>";
                                content=content+"</tr>";
                              content=content+"</tbody>";
                            content=content+"</table>";
                            content=content+"<table style=\"MARGIN: 25px 0px; FONT-SIZE: 12px\" cellpadding=\"0\"  cellspacing=\"0\" border=\"0\" width=\"100%\">";
                              content=content+"<tbody>";
                                content=content+"<tr>";
                                  content=content+"<td style=\"BORDER-BOTTOM: rgb(0,0,0) 1px dotted;BORDER-LEFT: rgb(0,0,0) 1px  dotted; BORDER-TOP: rgb(0,0,0) 1px dotted;BORDER-RIGHT: rgb(0,0,0) 1px  dotted\" valign=\"top\" width=\"48%\"><strong>COMMENTS</strong></td>";
                                  content=content+"<td valign=\"top\" width=\"52%\">";
                                    content=content+"<table style=\"MARGIN: 25px 0px; FONT-SIZE: 12px\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" width=\"100%\">";
                                      content=content+"<tbody>";
                                        content=content+"<tr>";
                                          content=content+"<td align=\"center\"> <p><strong>ORDERED BY</strong></p>";
                                          content=content+"</td>";
                                          content=content+"<td align=\"center\"><strong>CHECKED BY</strong></td>";
                                          content=content+"<td align=\"center\"><strong>APPROVED BY</strong></td>";
                                        content=content+"</tr>";
                                      content=content+"</tbody>";
                                    content=content+"</table>";
                                  content=content+"</td>";
                                content=content+"</tr>";
                              content=content+"</tbody>";
                            content=content+"</table>";
                            content=content+"<hr style=\"MARGIN-TOP: 40px\">";
                            content=content+"<table style=\"MARGIN-TOP: 0px; FONT-SIZE: 12px\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" width=\"100%\">";
                              content=content+"<tbody>";
                                content=content+"<tr>";
                                  content=content+"<td align=\"center\">Follow us @: queensbali:<span class=\"Apple-converted-space\">&nbsp;</span><img moz-do-not-send=\"true\" alt=\"\" src=\"http://accounts.queenstandoor.com/images/icon-media.jpg\" align=\"absMiddle\" height=\"22\" width=\"158\"><span class=\"Apple-converted-space\">&nbsp;</span>|http://bali.queenstandoor.com | HOTLINE: +62 81 249 249 249</td>";
                                content=content+"</tr>";
                                content=content+"<tr>";
                                  content=content+"<td align=\"center\"><strong>QUEEN'S TANDOOR</strong>, Jl. Raya<span class=\"Apple-converted-space\">&nbsp;</span><strong>Seminyak</strong><span class=\"Apple-converted-space\">&nbsp;</span>No. 1/73A, Kuta | T/F: (62-361) 732770/732771 | e: bali@queenstandoor.com</td>";
                                content=content+"</tr>";
                                content=content+"<tr>";
                                content=content+"<td align=\"center\"><strong>QUEEN'S OF INDIA</strong>, Bali Dynasty Resort, Jl. Kartika  Plaza, Tuban -<span  class=\"Apple-converted-space\">&nbsp;</span><strong>Kuta</strong><span class=\"Apple-converted-space\">&nbsp;</span>| T/F: (62-361) 765988/761099 | e: bali2@queenstandoor.com</td>";
                                content=content+"</tr>";
                                content=content+"<tr>";
                                  content=content+"<td align=\"center\"><strong>QUEEN'S OF INDIA</strong>, Jl. Pratama No. 65B, Tanjung Benoa -<span class=\"Apple-converted-space\">&nbsp;</span><strong>Nusa  Dua</strong><span class=\"Apple-converted-space\">&nbsp;</span>| T/F: (62-361) 771344/774 648 | e: bali3@queenstandoor.com</td>";
                                 content=content+"</tr>";
                                content=content+"<tr>";
                                  content=content+"<td align=\"center\"><strong>QUEEN'S OF INDIA</strong>, Jl. Suweta No. 1, Opp. Palace (  Puri Saren Ubud ),<span class=\"Apple-converted-space\">&nbsp;</span><strong>Ubud</strong><span class=\"Apple-converted-space\">&nbsp;</span>| T/F: ( 62-361 ) 977399/977400 | e: bali4@queenstandoor.com</td>";
                                content=content+"</tr>";
                              content=content+"</tbody>";
                            content=content+"</table>";
                          content=content+"</td>";
                        content=content+"</tr>";
                      content=content+"</tbody>";
                    content=content+"</table>";
                  content=content+"</div>";
                content=content+"</td>";
              content=content+"</tr>";
            content=content+"</tbody>";
          content=content+"</table>";
        
        return content;
    }
    
  
     public static String getContentEmailPR(Vector detailPr, PurchaseRequest po, String AksesOnsite, String AksesOnline, Location location) {
        
                    Date dateOrder = new Date();
                    String content="";
                    content=content+"<table style=\"TEXT-TRANSFORM: none; TEXT-INDENT: 0px; FONT-FAMILY: 'Times New Roman'; LETTER-SPACING: normal; WORD-SPACING: 0px; -webkit-text-stroke-width: 0px\" cellpadding=\"0\" cellspacing=\"50\" bgcolor=\"#ff9900\" border=\"0\" width=\"100%\">";
                    content=content+"<tbody>";
                    content=content+"<tr>";
                    content=content+"<td align=\"center\">";
                    content=content+"<div style=\"MARGIN: 0px auto; WIDTH: 210mm; FONT-FAMILY: Arial, Helvetica, sans-serif;HEIGHT: auto\">";
                    content=content+"<table style=\"MARGIN-BOTTOM: 10px; COLOR: rgb(255,255,255); FONT-SIZE: 12px\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" width=\"100%\">";
                    content=content+"<tbody>";
                    content=content+"<tr>";
                    content=content+"<td align=\"left\" valign=\"top\">Purchase Order,"+Formater.formatDate(po.getPurchRequestDate(), "dd/MM/yyyy")+"</td>"; //date purchase
                    content=content+"<td align=\"right\" valign=\"top\"><a moz-do-not-send=\"true\" style=\"COLOR: rgb(255,255,255)\"  href=\"http://twitter.com/queensbali\" target=\"_blank\">follow on Twitter</a><span class=\"Apple-converted-space\">&nbsp;</span>|<span class=\"Apple-converted-space\">&nbsp;</span><a moz-do-not-send=\"true\"  style=\"COLOR: rgb(255,255,255)\" href=\"http://facebook.com/queensbali\" target=\"_blank\">friend on Facebook</a></td>";
                    content=content+"</tr>";
                    content=content+"</tbody>";
                    content=content+"</table>";
                    content=content+"<table cellpadding=\"15\" cellspacing=\"0\" bgcolor=\"#ffffff\" border=\"0\" width=\"100%\">";
                      content=content+"<tbody>";
                        content=content+"<tr>";
                          content=content+"<td>";
                            content=content+"<table cellpadding=\"0\" cellspacing=\"0\" border=\"0\" width=\"100%\">";
                              content=content+"<tbody>";
                                content=content+"<tr>";
                                  content=content+"<td align=\"center\"><img  moz-do-not-send=\"true\" src=\"http://accounts.queenstandoor.com/images/queens-head.jpg\"  height=\"121\" width=\"300\"></td>";
                                content=content+"</tr>";
                              content=content+"</tbody>";
                            content=content+"</table>";
                            content=content+"<hr>";
                            content=content+"<table cellpadding=\"0\"  cellspacing=\"0\" border=\"0\" width=\"100%\">";
                              content=content+"<tbody>";
                                content=content+"<tr>";
                                  content=content+"<td align=\"left\"><font size=\"+2\"><strong><u>PURCHASE REQUEST</u></strong><span class=\"Apple-converted-space\">&nbsp;</span></font><br></td>";
                                  content=content+"<td style=\"FONT-SIZE: 12px\" align=\"right\" valign=\"top\"><strong>Seminyak,</strong><span class=\"Apple-converted-space\">&nbsp;</span>"+Formater.formatDate(dateOrder, "dd/MM/yyyy")+"</td>"; //untuk tanggal order
                                content=content+"</tr>";
                              content=content+"</tbody>";
                            content=content+"</table>";
                            content=content+"<table style=\"MARGIN-TOP: 10px; WIDTH: 763px; FONT-SIZE: 12px\" cellpadding=\"3\" cellspacing=\"0\"  border=\"0\">";
                              content=content+"<tbody>";
                                content=content+"<tr>";
                                  content=content+"<td style=\"BORDER-BOTTOM: rgb(0,0,0) 1px dotted\" align=\"left\" width=\"11%\"><strong>Code PO #</strong></td>";
                                  content=content+"<td style=\"BORDER-BOTTOM: rgb(0,0,0) 1px dotted\" align=\"left\" width=\"4%\">:</td>";
                                  content=content+"<td style=\"BORDER-BOTTOM: rgb(0,0,0) 1px dotted\" align=\"left\" width=\"85%\">"+po.getPrCode()+"</td>";
                                content=content+"</tr>";
                                content=content+"<tr>";
                                  content=content+"<td style=\"BORDER-BOTTOM:rgb(0,0,0) 1px dotted\" align=\"left\"><strong>Date</strong></td>";
                                  content=content+"<td style=\"BORDER-BOTTOM:rgb(0,0,0) 1px dotted\" align=\"left\">:</td>";
                                  content=content+"<td style=\"BORDER-BOTTOM: rgb(0,0,0) 1px dotted\" align=\"left\" width=\"85%\">"+Formater.formatDate(po.getPurchRequestDate(), "dd/MM/yyyy")+"</td>";
                                content=content+"</tr>";
                                content=content+"<tr>";
                              content=content+"</tbody>";
                            content=content+"</table>";
                            content=content+"<table style=\"MARGIN-TOP: 10px\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" width=\"100%\">";
                              content=content+"<tbody>";
                                content=content+"<tr>";
                                  content=content+"<td align=\"left\" width=\"50%\">";
                                    content=content+"<table style=\"BORDER-BOTTOM: rgb(0,0,0) 1px dotted; BORDER-LEFT: rgb(0,0,0) 1px dotted; FONT-SIZE: 12px; BORDER-TOP: rgb(0,0,0) 1px dotted; BORDER-RIGHT: rgb(0,0,0) 1px dotted\" cellpadding=\"0\" cellspacing=\"3\" border=\"0\" width=\"95%\">";
                                      content=content+"<tbody>";
                                        content=content+"<tr>";
                                          content=content+"<td><font size=\"+1\"><strong>Request From</strong></font></td>";
                                        content=content+"</tr>";
                                        content=content+"<tr>";
                                        content=content+"<td>"+location.getName()+"</td>";
                                        content=content+"</tr>";
                                        content=content+"<tr>";
                                        content=content+"<td>"+location.getAddress()+"</td>";
                                        content=content+"</tr>";
                                        content=content+"<tr>";
                                        content=content+"<td>Phone/Fax : "+location.getTelephone()+"/"+location.getFax()+"</td></tr>";
                                        content=content+"<tr>";
                                          content=content+"<td>Email :<span class=\"Apple-converted-space\">&nbsp;</span><a moz-do-not-send=\"true\" href=\"mailto:"+location.getEmail()+"\">"+location.getEmail()+"</a></td>";
                                        content=content+"</tr>";
                                      content=content+"</tbody>";
                                    content=content+"</table>";
                                  content=content+"</td>";
                                content=content+"</tr>";
                              content=content+"</tbody>";
                            content=content+"</table> <div style=\"MARGIN-TOP: 10px\"><small><i>We are pleased to confirm our purchase request :</i></small>";
                            content=content+"<table style=\"MARGIN-TOP: 5px;  FONT-SIZE: 10px\" cellpadding=\"2\"  cellspacing=\"1\" bgcolor=\"#000000\" border=\"0\" width=\"100%\">";
                                content=content+"<tbody>";
                                    content=content+"<tr>";
                                    content=content+"<td align=\"center\" bgcolor=\"#eeeeee\" height=\"28\" width=\"4%\"><strong>#</strong></td>";
                                    content=content+"<td align=\"center\" bgcolor=\"#eeeeee\"  width=\"11%\"><strong>PRODUCT ID</strong></td>";
                                    content=content+"<td align=\"center\"  bgcolor=\"#eeeeee\" width=\"21%\"><strong>ITEM NAME</strong></td>";
                                    content=content+"<td align=\"center\"  bgcolor=\"#eeeeee\" width=\"7%\"><strong>STOCK</strong></td>";
                                    content=content+"<td align=\"center\"  bgcolor=\"#eeeeee\" width=\"14%\"><strong>MINIMUM STOCK</strong></td>";
                                    content=content+"<td align=\"center\"  bgcolor=\"#eeeeee\" width=\"22%\"><strong>UNIT STOCK</strong></td>";
                                    content=content+"<td align=\"center\"  bgcolor=\"#eeeeee\" width=\"21%\"><strong>QTY REQUEST</strong></td>";
                                    content=content+"<td align=\"center\" bgcolor=\"#eeeeee\"  width=\"14%\"><strong>UNIT REQUEST</strong></td>";
                                    content=content+"</tr>";
                                    //looping untuk deatail item
                                    int start=0;
                                    MatCurrency matCurr = new MatCurrency();
                                    if(detailPr.size()>0){
                                         for (int i = 0; i < detailPr.size(); i++){
                                             Vector temp = (Vector)detailPr.get(i);
                                             PurchaseRequestItem poItem = (PurchaseRequestItem)temp.get(0);
                                             Material mat = (Material)temp.get(1);
                                             Unit unit = (Unit)temp.get(2);
                                             start=start+1;
                                             content=content+"<tr bgcolor=\"#ffffff\">";
                                             content=content+"<td align=\"center\">"+start+"</td>";
                                             content=content+"<td align=\"center\">"+mat.getSku()+"</td>";
                                             content=content+"<td>"+mat.getName()+"</td>";
                                             content=content+"<td align=\"center\">"+FRMHandler.userFormatStringDecimal(poItem.getCurrentStock())+"</td>";
                                             content=content+"<td>"+FRMHandler.userFormatStringDecimal(poItem.getMinimStock())+"</td>";
                                             content=content+"<td>"+unit.getCode()+"</td>";
                                             content=content+"<td align=\"right\">"+FRMHandler.userFormatStringDecimal(poItem.getQuantity())+"</td>";
                                             content=content+"<td align=\"right\">"+PstUnit.getKodeUnitByHash(poItem.getUnitId())+"</td>";
                                             content=content+"</tr>";
                                         }
                                    }  
                                content=content+"</tbody>";
                              content=content+"</table>";
                            content=content+"</div>";
                            content=content+"<table style=\"BORDER-BOTTOM: rgb(0,0,0) 1px solid; BORDER-LEFT: rgb(0,0,0) 1px solid; MARGIN-TOP:  10px; FONT-SIZE: 10px; BORDER-TOP: rgb(0,0,0) 1px solid;BORDER-RIGHT: rgb(0,0,0) 1px solid\" cellpadding=\"3\" cellspacing=\"0\" border=\"0\" width=\"100%\">";
                              content=content+"<tbody>";
                                            content=content+"<tr>";
                                            content=content+"<td align=\"center\" bgcolor=\"#eeeeee\">"+AksesOnsite+"</td>";
                                            content=content+"</tr>";
                                            content=content+"<tr>";
                                            content=content+"<td align=\"center\" bgcolor=\"#eeeeee\">"+AksesOnline+"</td>";
                                            content=content+"</tr>";
                              content=content+"</tbody>";
                            content=content+"</table>";
                            content=content+"<hr style=\"MARGIN-TOP: 40px\">";
                            content=content+"<table style=\"MARGIN-TOP: 0px; FONT-SIZE: 12px\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" width=\"100%\">";
                              content=content+"<tbody>";
                                content=content+"<tr>";
                                  content=content+"<td align=\"center\">Follow us @: queensbali:<span class=\"Apple-converted-space\">&nbsp;</span><img moz-do-not-send=\"true\" alt=\"\" src=\"http://accounts.queenstandoor.com/images/icon-media.jpg\" align=\"absMiddle\" height=\"22\" width=\"158\"><span class=\"Apple-converted-space\">&nbsp;</span>|http://bali.queenstandoor.com | HOTLINE: +62 81 249 249 249</td>";
                                content=content+"</tr>";
                                content=content+"<tr>";
                                  content=content+"<td align=\"center\"><strong>QUEEN'S TANDOOR</strong>, Jl. Raya<span class=\"Apple-converted-space\">&nbsp;</span><strong>Seminyak</strong><span class=\"Apple-converted-space\">&nbsp;</span>No. 1/73A, Kuta | T/F: (62-361) 732770/732771 | e: bali@queenstandoor.com</td>";
                                content=content+"</tr>";
                                content=content+"<tr>";
                                content=content+"<td align=\"center\"><strong>QUEEN'S OF INDIA</strong>, Bali Dynasty Resort, Jl. Kartika  Plaza, Tuban -<span  class=\"Apple-converted-space\">&nbsp;</span><strong>Kuta</strong><span class=\"Apple-converted-space\">&nbsp;</span>| T/F: (62-361) 765988/761099 | e: bali2@queenstandoor.com</td>";
                                content=content+"</tr>";
                                content=content+"<tr>";
                                  content=content+"<td align=\"center\"><strong>QUEEN'S OF INDIA</strong>, Jl. Pratama No. 65B, Tanjung Benoa -<span class=\"Apple-converted-space\">&nbsp;</span><strong>Nusa  Dua</strong><span class=\"Apple-converted-space\">&nbsp;</span>| T/F: (62-361) 771344/774 648 | e: bali3@queenstandoor.com</td>";
                                 content=content+"</tr>";
                                content=content+"<tr>";
                                  content=content+"<td align=\"center\"><strong>QUEEN'S OF INDIA</strong>, Jl. Suweta No. 1, Opp. Palace (  Puri Saren Ubud ),<span class=\"Apple-converted-space\">&nbsp;</span><strong>Ubud</strong><span class=\"Apple-converted-space\">&nbsp;</span>| T/F: ( 62-361 ) 977399/977400 | e: bali4@queenstandoor.com</td>";
                                content=content+"</tr>";
                              content=content+"</tbody>";
                            content=content+"</table>";
                          content=content+"</td>";
                        content=content+"</tr>";
                      content=content+"</tbody>";
                    content=content+"</table>";
                  content=content+"</div>";
                content=content+"</td>";
              content=content+"</tr>";
            content=content+"</tbody>";
          content=content+"</table>";
        return content;
    }
    
    public static String getContentEmailSR(Vector detailPr, PurchaseOrder po, String AksesOnsite, String AksesOnline, Location location) {
        
                    Date dateOrder = new Date();
                    String content="";
                    content=content+"<table style=\"TEXT-TRANSFORM: none; TEXT-INDENT: 0px; FONT-FAMILY: 'Times New Roman'; LETTER-SPACING: normal; WORD-SPACING: 0px; -webkit-text-stroke-width: 0px\" cellpadding=\"0\" cellspacing=\"50\" bgcolor=\"#ff9900\" border=\"0\" width=\"100%\">";
                    content=content+"<tbody>";
                    content=content+"<tr>";
                    content=content+"<td align=\"center\">";
                    content=content+"<div style=\"MARGIN: 0px auto; WIDTH: 210mm; FONT-FAMILY: Arial, Helvetica, sans-serif;HEIGHT: auto\">";
                    content=content+"<table style=\"MARGIN-BOTTOM: 10px; COLOR: rgb(255,255,255); FONT-SIZE: 12px\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" width=\"100%\">";
                    content=content+"<tbody>";
                    content=content+"<tr>";
                    content=content+"<td align=\"left\" valign=\"top\">Store Request, "+Formater.formatDate(po.getPurchDate(), "dd/MM/yyyy")+"</td>"; //date purchase
                    content=content+"<td align=\"right\" valign=\"top\"><a moz-do-not-send=\"true\" style=\"COLOR: rgb(255,255,255)\"  href=\"http://twitter.com/queensbali\" target=\"_blank\">follow on Twitter</a><span class=\"Apple-converted-space\">&nbsp;</span>|<span class=\"Apple-converted-space\">&nbsp;</span><a moz-do-not-send=\"true\"  style=\"COLOR: rgb(255,255,255)\" href=\"http://facebook.com/queensbali\" target=\"_blank\">friend on Facebook</a></td>";
                    content=content+"</tr>";
                    content=content+"</tbody>";
                    content=content+"</table>";
                    content=content+"<table cellpadding=\"15\" cellspacing=\"0\" bgcolor=\"#ffffff\" border=\"0\" width=\"100%\">";
                      content=content+"<tbody>";
                        content=content+"<tr>";
                          content=content+"<td>";
                            content=content+"<table cellpadding=\"0\" cellspacing=\"0\" border=\"0\" width=\"100%\">";
                              content=content+"<tbody>";
                                content=content+"<tr>";
                                  content=content+"<td align=\"center\"><img  moz-do-not-send=\"true\" src=\"http://accounts.queenstandoor.com/images/queens-head.jpg\"  height=\"121\" width=\"300\"></td>";
                                content=content+"</tr>";
                              content=content+"</tbody>";
                            content=content+"</table>";
                            content=content+"<hr>";
                            content=content+"<table cellpadding=\"0\"  cellspacing=\"0\" border=\"0\" width=\"100%\">";
                              content=content+"<tbody>";
                                content=content+"<tr>";
                                  content=content+"<td align=\"left\"><font size=\"+2\"><strong><u>STORE REQUEST</u></strong><span class=\"Apple-converted-space\">&nbsp;</span></font><br></td>";
                                  content=content+"<td style=\"FONT-SIZE: 12px\" align=\"right\" valign=\"top\"><strong>"+location.getName()+",</strong><span class=\"Apple-converted-space\">&nbsp;</span>"+Formater.formatDate(dateOrder, "dd/MM/yyyy")+"</td>"; //untuk tanggal order
                                content=content+"</tr>";
                              content=content+"</tbody>";
                            content=content+"</table>";
                            content=content+"<table style=\"MARGIN-TOP: 10px; WIDTH: 763px; FONT-SIZE: 12px\" cellpadding=\"3\" cellspacing=\"0\"  border=\"0\">";
                              content=content+"<tbody>";
                                content=content+"<tr>";
                                  content=content+"<td style=\"BORDER-BOTTOM: rgb(0,0,0) 1px dotted\" align=\"left\" width=\"11%\"><strong>Code PO #</strong></td>";
                                  content=content+"<td style=\"BORDER-BOTTOM: rgb(0,0,0) 1px dotted\" align=\"left\" width=\"4%\">:</td>";
                                  content=content+"<td style=\"BORDER-BOTTOM: rgb(0,0,0) 1px dotted\" align=\"left\" width=\"85%\">"+po.getPoCode()+"</td>";
                                content=content+"</tr>";
                                content=content+"<tr>";
                                  content=content+"<td style=\"BORDER-BOTTOM:rgb(0,0,0) 1px dotted\" align=\"left\"><strong>Date</strong></td>";
                                  content=content+"<td style=\"BORDER-BOTTOM:rgb(0,0,0) 1px dotted\" align=\"left\">:</td>";
                                  content=content+"<td style=\"BORDER-BOTTOM: rgb(0,0,0) 1px dotted\" align=\"left\" width=\"85%\">"+Formater.formatDate(po.getPurchDate(), "dd/MM/yyyy")+"</td>";
                                content=content+"</tr>";
                                content=content+"<tr>";
                              content=content+"</tbody>";
                            content=content+"</table>";
                            content=content+"<table style=\"MARGIN-TOP: 10px\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" width=\"100%\">";
                              content=content+"<tbody>";
                                content=content+"<tr>";
                                  content=content+"<td align=\"left\" width=\"50%\">";
                                    content=content+"<table style=\"BORDER-BOTTOM: rgb(0,0,0) 1px dotted; BORDER-LEFT: rgb(0,0,0) 1px dotted; FONT-SIZE: 12px; BORDER-TOP: rgb(0,0,0) 1px dotted; BORDER-RIGHT: rgb(0,0,0) 1px dotted\" cellpadding=\"0\" cellspacing=\"3\" border=\"0\" width=\"95%\">";
                                      content=content+"<tbody>";
                                        content=content+"<tr>";
                                          content=content+"<td><font size=\"+1\"><strong>Request From</strong></font></td>";
                                        content=content+"</tr>";
                                        content=content+"<tr>";
                                        content=content+"<td>"+location.getName()+"</td>";
                                        content=content+"</tr>";
                                        content=content+"<tr>";
                                        content=content+"<td>"+location.getAddress()+"</td>";
                                        content=content+"</tr>";
                                        content=content+"<tr>";
                                        content=content+"<td>Phone/Fax : "+location.getTelephone()+"/"+location.getFax()+"</td></tr>";
                                        content=content+"<tr>";
                                          content=content+"<td>Email :<span class=\"Apple-converted-space\">&nbsp;</span><a moz-do-not-send=\"true\" href=\"mailto:"+location.getEmail()+"\">"+location.getEmail()+"</a></td>";
                                        content=content+"</tr>";
                                      content=content+"</tbody>";
                                    content=content+"</table>";
                                  content=content+"</td>";
                                content=content+"</tr>";
                              content=content+"</tbody>";
                            content=content+"</table> <div style=\"MARGIN-TOP: 10px\"><small><i>We are pleased to confirm our store request :</i></small>";
                            content=content+"<table style=\"MARGIN-TOP: 5px;  FONT-SIZE: 10px\" cellpadding=\"2\"  cellspacing=\"1\" bgcolor=\"#000000\" border=\"0\" width=\"100%\">";
                                content=content+"<tbody>";
                                    content=content+"<tr>";
                                    content=content+"<td align=\"center\" bgcolor=\"#eeeeee\" height=\"28\" width=\"4%\"><strong>#</strong></td>";
                                    content=content+"<td align=\"center\" bgcolor=\"#eeeeee\"  width=\"11%\"><strong>PRODUCT ID</strong></td>";
                                    content=content+"<td align=\"center\"  bgcolor=\"#eeeeee\" width=\"21%\"><strong>ITEM NAME</strong></td>";
                                    content=content+"<td align=\"center\"  bgcolor=\"#eeeeee\" width=\"21%\"><strong>QTY REQUEST</strong></td>";
                                    /*content=content+"<td align=\"center\"  bgcolor=\"#eeeeee\" width=\"7%\"><strong>STOCK</strong></td>";
                                    content=content+"<td align=\"center\"  bgcolor=\"#eeeeee\" width=\"14%\"><strong>MINIMUM STOCK</strong></td>";
                                    content=content+"<td align=\"center\"  bgcolor=\"#eeeeee\" width=\"22%\"><strong>UNIT STOCK</strong></td>";
                                    
                                    content=content+"<td align=\"center\"  bgcolor=\"#eeeeee\" width=\"21%\"><strong>QTY REQUEST</strong></td>";
                                    content=content+"<td align=\"center\" bgcolor=\"#eeeeee\"  width=\"14%\"><strong>UNIT REQUEST</strong></td>";*/
                                    content=content+"</tr>";
                                    //looping untuk deatail item
                                    int start=0;
                                    MatCurrency matCurr = new MatCurrency();
                                    if(detailPr.size()>0){
                                         for (int i = 0; i < detailPr.size(); i++){
                                             Vector temp = (Vector)detailPr.get(i);
                                             PurchaseOrderItem poItem = (PurchaseOrderItem)temp.get(0);
                                             Material mat = (Material)temp.get(1);
                                             Unit unit = (Unit)temp.get(2);
                                             start=start+1;
                                             content=content+"<tr bgcolor=\"#ffffff\">";
                                             content=content+"<td align=\"center\">"+start+"</td>";
                                             content=content+"<td align=\"center\">"+mat.getSku()+"</td>";
                                             content=content+"<td>"+mat.getName()+"</td>";
                                             content=content+"<td align=\"right\">"+FRMHandler.userFormatStringDecimal(poItem.getQuantity())+"</td>";
                                             
                                             /*content=content+"<td align=\"center\">"+FRMHandler.userFormatStringDecimal(poItem.getCurrentStock())+"</td>";
                                             content=content+"<td>"+FRMHandler.userFormatStringDecimal(poItem.getMinimStock())+"</td>";
                                             content=content+"<td>"+unit.getCode()+"</td>";
                                             content=content+"<td align=\"right\">"+FRMHandler.userFormatStringDecimal(poItem.getQuantity())+"</td>";
                                             content=content+"<td align=\"right\">"+PstUnit.getKodeUnitByHash(poItem.getUnitId())+"</td>";*/
                                             content=content+"</tr>";
                                         }
                                    }  
                                content=content+"</tbody>";
                              content=content+"</table>";
                            content=content+"</div>";
                            content=content+"<table style=\"BORDER-BOTTOM: rgb(0,0,0) 1px solid; BORDER-LEFT: rgb(0,0,0) 1px solid; MARGIN-TOP:  10px; FONT-SIZE: 10px; BORDER-TOP: rgb(0,0,0) 1px solid;BORDER-RIGHT: rgb(0,0,0) 1px solid\" cellpadding=\"3\" cellspacing=\"0\" border=\"0\" width=\"100%\">";
                              content=content+"<tbody>";
                                            content=content+"<tr>";
                                            content=content+"<td align=\"center\" bgcolor=\"#eeeeee\">"+AksesOnsite+"</td>";
                                            content=content+"</tr>";
                                            content=content+"<tr>";
                                            content=content+"<td align=\"center\" bgcolor=\"#eeeeee\">"+AksesOnline+"</td>";
                                            content=content+"</tr>";
                              content=content+"</tbody>";
                            content=content+"</table>";
                            content=content+"<hr style=\"MARGIN-TOP: 40px\">";
                            content=content+"<table style=\"MARGIN-TOP: 0px; FONT-SIZE: 12px\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" width=\"100%\">";
                              content=content+"<tbody>";
                                content=content+"<tr>";
                                  content=content+"<td align=\"center\">Follow us @: queensbali:<span class=\"Apple-converted-space\">&nbsp;</span><img moz-do-not-send=\"true\" alt=\"\" src=\"http://accounts.queenstandoor.com/images/icon-media.jpg\" align=\"absMiddle\" height=\"22\" width=\"158\"><span class=\"Apple-converted-space\">&nbsp;</span>|http://bali.queenstandoor.com | HOTLINE: +62 81 249 249 249</td>";
                                content=content+"</tr>";
                                content=content+"<tr>";
                                  content=content+"<td align=\"center\"><strong>QUEEN'S TANDOOR</strong>, Jl. Raya<span class=\"Apple-converted-space\">&nbsp;</span><strong>Seminyak</strong><span class=\"Apple-converted-space\">&nbsp;</span>No. 1/73A, Kuta | T/F: (62-361) 732770/732771 | e: bali@queenstandoor.com</td>";
                                content=content+"</tr>";
                                content=content+"<tr>";
                                content=content+"<td align=\"center\"><strong>QUEEN'S OF INDIA</strong>, Bali Dynasty Resort, Jl. Kartika  Plaza, Tuban -<span  class=\"Apple-converted-space\">&nbsp;</span><strong>Kuta</strong><span class=\"Apple-converted-space\">&nbsp;</span>| T/F: (62-361) 765988/761099 | e: bali2@queenstandoor.com</td>";
                                content=content+"</tr>";
                                content=content+"<tr>";
                                  content=content+"<td align=\"center\"><strong>QUEEN'S OF INDIA</strong>, Jl. Pratama No. 65B, Tanjung Benoa -<span class=\"Apple-converted-space\">&nbsp;</span><strong>Nusa  Dua</strong><span class=\"Apple-converted-space\">&nbsp;</span>| T/F: (62-361) 771344/774 648 | e: bali3@queenstandoor.com</td>";
                                 content=content+"</tr>";
                                content=content+"<tr>";
                                  content=content+"<td align=\"center\"><strong>QUEEN'S OF INDIA</strong>, Jl. Suweta No. 1, Opp. Palace (  Puri Saren Ubud ),<span class=\"Apple-converted-space\">&nbsp;</span><strong>Ubud</strong><span class=\"Apple-converted-space\">&nbsp;</span>| T/F: ( 62-361 ) 977399/977400 | e: bali4@queenstandoor.com</td>";
                                content=content+"</tr>";
                              content=content+"</tbody>";
                            content=content+"</table>";
                          content=content+"</td>";
                        content=content+"</tr>";
                      content=content+"</tbody>";
                    content=content+"</table>";
                  content=content+"</div>";
                content=content+"</td>";
              content=content+"</tr>";
            content=content+"</tbody>";
          content=content+"</table>";
        return content;
    }
}
