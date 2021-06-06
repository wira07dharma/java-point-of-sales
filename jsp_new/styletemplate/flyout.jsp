<%-- 
    Document   : flyout
    Created on : Sep 18, 2013, 5:13:29 PM
    Author     : user
--%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%
String sizeSpasiPerSubMenu="-15px";
%>
<!DOCTYPE html>
<style type="text/css">
#menu_kanan{

	/*margin-top:45px;*/
	margin-left:400px;
        margin-top:50px;
}
#menu_kiri{
            padding-top: 50px;
            float:left;
            #menuheaderx > h2{
                margin-left: 0px;
                padding-left: 20px;
                margin-top: -35px;
                background:url('<%=approot%>/menustylesheet/icon_flyout/kotak.png');
                background-repeat: no-repeat;
                border-radius:7px;
                -moz-border-radius:7px;
                -webkit-border-radius:7px;
                box-shadow:rgba(17,4,83,0.3) 4px 4px 4px,
                   rgba(17,4,83,0.3) -4px 4px 4px;
                           -moz-box-shadow:rgba(17,4,83,0.3) 4px 4px 4px,
                                           rgba(17,4,83,0.3) -4px 4px 4px;
                -webkit-box-shadow:rgba(17,4,83,0.3) 5px 5px 5px,
                                   rgba(17,4,83,0.3) -5px 5px 5px;
                width: 144px;

            }
            #cssmenu,
            #cssmenu ul,
            #cssmenu li,
            #cssmenu a {
                margin: 0;
                padding: 0;

                list-style: none;
                font-weight: normal;
                text-decoration: none;
                /*line-height: 1;*/
                font-family: 'Open Sans', sans-serif;
                font-size: 14px;
                position: relative;


            }
            #cssmenu a {
                line-height: 2.8;/* height */

            }
            #cssmenu {
                width: 400px;
                margin-top: -40px;

                /*padding: 3px;*/

            }
            #cssmenu > ul > li {
                margin: 0 0 2px 0;
            }
            #cssmenu > ul > li:last-child {
                margin: 0;
            }
            #cssmenu > ul > li > a {
                /* untuk jika tidak ada sub menu lagi */
                font-size: 15px;
                display: block;
                /*color: #ffffff;*/

                background: url(<%=approot%>/menustylesheet/icon_flyout/1.1.png) 5% center no-repeat;
                /*text-shadow: 0 1px 1px #000; warna text*/

            }

            #cssmenu > ul > li > a > span {
                display: block;

                /*padding: 1px 10px;*/

                font-weight: bold;
            }
            #cssmenu > ul > li > a:hover {
                text-decoration: none;

            }
            #cssmenu > ul > li.active {
                border-bottom: none;


            }

            /* ini nantinya bisa di ubah" sesuai gambarnya main utama*/
            #cssmenu > ul > li.has-sub > a span {
                background: url(<%=approot%>/menustylesheet/icon_flyout/11.png) 5% center no-repeat;

            }
            /* ini nantinya bisa di ubah" sesuai gambarnya hover image*/
            #cssmenu > ul > li.has-sub.actived > a span {
                /* untuk jika tidak ada sub menu lagi */
                background: url(<%=approot%>/menustylesheet/icon_flyout/11.png) 5% center no-repeat;
            }
            #cssmenu > ul > li.data_bank > a {
                /* untuk jika tidak ada sub menu lagi */
                background: url(<%=approot%>/menustylesheet/icon_flyout/employee/1.png) 5% center no-repeat;
                /*text-shadow: 0 1px 1px #000;*/
            }
             #cssmenu > ul > li.company_structure > a {
                /* untuk jika tidak ada sub menu lagi */
                background: url(<%=approot%>/menustylesheet/icon_flyout/employee/2.png) 5% center no-repeat;
                /*text-shadow: 0 1px 1px #000;*/
            }
            #cssmenu > ul > li.new_employee > a {
                /* untuk jika tidak ada sub menu lagi */
                background: url(<%=approot%>/menustylesheet/icon_flyout/employee/3.png) 5% center no-repeat;
                /*text-shadow: 0 1px 1px #000;*/
            }

            #cssmenu > ul > li.attendance > a {
                /* untuk jika tidak ada sub menu lagi */
                background: url(<%=approot%>/menustylesheet/icon_flyout/employee/04.png) 5% center no-repeat;
                /*text-shadow: 0 1px 1px #000;*/
            }
             /* ini nantinya bisa di ubah" sesuai gambarnya hover image*/
            #cssmenu > ul > li.attendance.actived > a span {
                /* untuk jika tidak ada sub menu lagi */

                background: url(<%=approot%>/menustylesheet/icon_flyout/5.png) 5% center no-repeat;
            }
             #cssmenu > ul > li.actived > a {
                /* untuk jika tidak ada sub menu lagi */
                color: #ffffff;
                /*background: url(<%=approot%>/menustylesheet/icon_flyout/1.png) 5% center no-repeat;*/

                /*text-shadow: 0 1px 1px #000;*/
            }
            #cssmenu > ul > li.active1 > a {
                /* untuk jika tidak ada sub menu lagi */

                background: url(<%=approot%>/menustylesheet/icon_flyout/11.png) 5% center no-repeat;

                /*text-shadow: 0 1px 1px #000;*/
            }

            /* untuk align di has-sub <p id=has-sub-align>isi </p>*/
            #has-sub-align{
                margin-left:65px;

            }

            /* update */
            #has-sub-menu{
                background: url(<%=approot%>/menustylesheet/icon_flyout/submenu.png) 5% center no-repeat;

            }
            #cssmenu pxsubmenu {
                top: 0;
                left: 0;
                margin-left: 13px;
            }

            #has-sub-menu1{
                background: url(<%=approot%>/menustylesheet/icon_flyout/submenu1.png) 20% center no-repeat;


            }
            #enter_word{
                top: 0;
                left: 0;

                margin-left: 30px;
                width: 10px;
                font:Arial, Helvetica, sans-serif;
            }
            #cssmenu px {
                top: 0;
                left: 0;
                margin-left: 30px;
                font:Arial, Helvetica, sans-serif;

            }


            /* Sub menu */
            #cssmenu ul ul {
                padding: 5px 12px;
                display: none;
            }
            #cssmenu ul ul li {
                padding: 3px 0;
            }
            #cssmenu ul ul a {
                display: block;
                color: #595959;
                font-size: 13px;
                /*  font-weight: bold;*/
            }
            #cssmenu ul ul a:hover {
                color:#FFFFFF;
            }
            /* css icon mulai dari sini */
                        #cssmenu > ul > li > a#data_bank {
            /* untuk jika tidak ada sub menu lagi */
              font-size: 15px;
              display: block;
              /*color: #ffffff;*/

              background: url(<%=approot%>/menustylesheet/icon_flyout/1.png) 5% center no-repeat;
              /*text-shadow: 0 1px 1px #000; warna text*/

            }
             #cssmenu > ul > li.active > a#data_bank {
           /* untuk jika tidak ada sub menu lagi */
             font-size: 15px;
             display: block;
              color: #ffffff;

                        }
                        #cssmenu > ul > li > a#company_structure {
           /* untuk jika tidak ada sub menu lagi */
             font-size: 15px;
             display: block;
             /*color: #ffffff;*/

             background: url(<%=approot%>/menustylesheet/icon_flyout/employee/2.png) 5% center no-repeat;
             /*text-shadow: 0 1px 1px #000; warna text*/
                        }
                         #cssmenu > ul > li.active > a#company_structure {
           /* untuk jika tidak ada sub menu lagi */
             font-size: 15px;
             display: block;
              color: #ffffff;

                        }
                           #cssmenu > ul > li > a#new_employee {
           /* untuk jika tidak ada sub menu lagi */
             font-size: 15px;
             display: block;
             /*color: #ffffff;*/

             background: url(<%=approot%>/menustylesheet/icon_flyout/employee/3.png) 5% center no-repeat;
             /*text-shadow: 0 1px 1px #000; warna text*/
                        }
                          #cssmenu > ul > li.active > a#new_employee {
           /* untuk jika tidak ada sub menu lagi */
             font-size: 15px;
             display: block;
              color: #ffffff;

                        }

                          #cssmenu > ul > li > a#attendance {
           /* untuk jika tidak ada sub menu lagi */
             font-size: 15px;
             display: block;
             /*color: #ffffff;*/

             background: url(<%=approot%>/menustylesheet/icon_flyout/employee/04.png) 5% center no-repeat;
             /*text-shadow: 0 1px 1px #000; warna text*/
                        }
                          #cssmenu > ul > li.active > a#attendance {
           /* untuk jika tidak ada sub menu lagi */
             font-size: 15px;
             display: block;
              color: #ffffff;
             /*color: #ffffff;*/

             background: url(<%=approot%>/menustylesheet/icon_flyout/employee/4a.png) 5% center no-repeat;
             /*text-shadow: 0 1px 1px #000; warna text*/
                        }
                         #cssmenu > ul > li > a#leave_application {
           /* untuk jika tidak ada sub menu lagi */
             font-size: 15px;
             display: block;
             /*color: #ffffff;*/

             background: url(<%=approot%>/menustylesheet/icon_flyout/employee/5.png) 5% center no-repeat;
             /*text-shadow: 0 1px 1px #000; warna text*/
                        }
                          #cssmenu > ul > li.active > a#leave_application {
           /* untuk jika tidak ada sub menu lagi */
             font-size: 15px;
             display: block;
             /*color: #ffffff;*/
              color: #ffffff;
             background: url(<%=approot%>/menustylesheet/icon_flyout/employee/5aM.png) 5% center no-repeat;
             /*text-shadow: 0 1px 1px #000; warna text*/
                        }
                         #cssmenu > ul > li > a#leave_balancing {
           /* untuk jika tidak ada sub menu lagi */
             font-size: 15px;
             display: block;
             /*color: #ffffff;*/

             background: url(<%=approot%>/menustylesheet/icon_flyout/employee/6.png) 5% center no-repeat;
             /*text-shadow: 0 1px 1px #000; warna text*/
                        }
                          #cssmenu > ul > li.active > a#leave_balancing {
           /* untuk jika tidak ada sub menu lagi */
             font-size: 15px;
             display: block;
             /*color: #ffffff;*/
              color: #ffffff;
             background: url(<%=approot%>/menustylesheet/icon_flyout/employee/6aM.png) 5% center no-repeat;
             /*text-shadow: 0 1px 1px #000; warna text*/

                        }
                         #cssmenu > ul > li > a#excuse_application {
           /* untuk jika tidak ada sub menu lagi */
             font-size: 15px;
             display: block;
             /*color: #ffffff;*/

             background: url(<%=approot%>/menustylesheet/icon_flyout/employee/7.png) 5% center no-repeat;
             /*text-shadow: 0 1px 1px #000; warna text*/
                        }
                          #cssmenu > ul > li.active > a#excuse_application {
           /* untuk jika tidak ada sub menu lagi */
             font-size: 15px;
             display: block;
             /*color: #ffffff;*/
              color: #ffffff;
             background: url(<%=approot%>/menustylesheet/icon_flyout/employee/7aM.png) 5% center no-repeat;
             /*text-shadow: 0 1px 1px #000; warna text*/
                        }
                          #cssmenu > ul > li > a#absence_management {
           /* untuk jika tidak ada sub menu lagi */
             font-size: 15px;
             display: block;
             /*color: #ffffff;*/

             background: url(<%=approot%>/menustylesheet/icon_flyout/employee/8.png) 5% center no-repeat;
             /*text-shadow: 0 1px 1px #000; warna text*/
                        }
                          #cssmenu > ul > li.active > a#absence_management {
           /* untuk jika tidak ada sub menu lagi */

              color: #ffffff;

                        }
                          #cssmenu > ul > li > a#assessment {
           /* untuk jika tidak ada sub menu lagi */
             font-size: 15px;
             display: block;
             /*color: #ffffff;*/

             background: url(<%=approot%>/menustylesheet/icon_flyout/employee/9.png) 5% center no-repeat;
             /*text-shadow: 0 1px 1px #000; warna text*/
                        }
                          #cssmenu > ul > li.active > a#assessment {
           /* untuk jika tidak ada sub menu lagi */
             font-size: 15px;
             display: block;
             /*color: #ffffff;*/
              color: #ffffff;
             background: url(<%=approot%>/menustylesheet/icon_flyout/employee/9aM.png) 5% center no-repeat;
             /*text-shadow: 0 1px 1px #000; warna text*/
                        }
                         #cssmenu > ul > li > a#recognitions {
           /* untuk jika tidak ada sub menu lagi */
             font-size: 15px;
             display: block;
             /*color: #ffffff;*/

             background: url(<%=approot%>/menustylesheet/icon_flyout/employee/10.png) 5% center no-repeat;
             /*text-shadow: 0 1px 1px #000; warna text*/
                        }
                          #cssmenu > ul > li.active > a#recognitions {
           /* untuk jika tidak ada sub menu lagi */
             font-size: 15px;
             display: block;
             /*color: #ffffff;*/
              color: #ffffff;
             background: url(<%=approot%>/menustylesheet/icon_flyout/employee/10aM.png) 5% center no-repeat;
             /*text-shadow: 0 1px 1px #000; warna text*/
                        }
                         #cssmenu > ul > li > a#recruitment {
           /* untuk jika tidak ada sub menu lagi */
             font-size: 15px;
             display: block;
             /*color: #ffffff;*/

             background: url(<%=approot%>/menustylesheet/icon_flyout/employee/11.png) 5% center no-repeat;
             /*text-shadow: 0 1px 1px #000; warna text*/
                        }
                          #cssmenu > ul > li.active > a#recruitment {
           /* untuk jika tidak ada sub menu lagi */
             font-size: 15px;
             display: block;
             /*color: #ffffff;*/
              color: #ffffff;
             background: url(<%=approot%>/menustylesheet/icon_flyout/employee/11aM.png) 5% center no-repeat;

             /*text-shadow: 0 1px 1px #000; warna text*/
                        }
                         #cssmenu > ul > li > a#warning {
           /* untuk jika tidak ada sub menu lagi */
             font-size: 15px;
             display: block;
             /*color: #ffffff;*/

             background: url(<%=approot%>/menustylesheet/icon_flyout/employee/12.png) 5% center no-repeat;
             /*text-shadow: 0 1px 1px #000; warna text*/
                        }
                          #cssmenu > ul > li.active > a#warning {
           /* untuk jika tidak ada sub menu lagi */
             font-size: 15px;
             display: block;
             /*color: #ffffff;*/
              color: #ffffff;
             background: url(<%=approot%>/menustylesheet/icon_flyout/employee/12aM.png) 5% center no-repeat;
             /*text-shadow: 0 1px 1px #000; warna text*/
                        }
                           #cssmenu > ul > li > a#trening_type {
            /* untuk jika tidak ada sub menu lagi */
              font-size: 15px;
              display: block;
              /*color: #ffffff;*/

              background: url(<%=approot%>/menustylesheet/icon_flyout/trening/1.png) 5% center no-repeat;
              /*text-shadow: 0 1px 1px #000; warna text*/

            }
             #cssmenu > ul > li.active > a#trening_type {
           /* untuk jika tidak ada sub menu lagi */
             font-size: 15px;
             display: block;
              color: #ffffff;

                        }
                         #cssmenu > ul > li > a#trening_vanue {
            /* untuk jika tidak ada sub menu lagi */
              font-size: 15px;
              display: block;
              /*color: #ffffff;*/

              background: url(<%=approot%>/menustylesheet/icon_flyout/trening/2.png) 5% center no-repeat;
              /*text-shadow: 0 1px 1px #000; warna text*/

            }
             #cssmenu > ul > li.active > a#trening_vanue {
           /* untuk jika tidak ada sub menu lagi */
             font-size: 15px;
             display: block;
              color: #ffffff;

                        }
                         #cssmenu > ul > li > a#trening_program {
            /* untuk jika tidak ada sub menu lagi */
              font-size: 15px;
              display: block;
              /*color: #ffffff;*/

              background: url(<%=approot%>/menustylesheet/icon_flyout/trening/3.png) 5% center no-repeat;
              /*text-shadow: 0 1px 1px #000; warna text*/

            }
             #cssmenu > ul > li.active > a#trening_program {
           /* untuk jika tidak ada sub menu lagi */
             font-size: 15px;
             display: block;
              color: #ffffff;

                        }
                         #cssmenu > ul > li > a#trening_plan {
            /* untuk jika tidak ada sub menu lagi */
              font-size: 15px;
              display: block;
              /*color: #ffffff;*/

              background: url(<%=approot%>/menustylesheet/icon_flyout/trening/4.png) 5% center no-repeat;
              /*text-shadow: 0 1px 1px #000; warna text*/

            }
             #cssmenu > ul > li.active > a#trening_plan {
           /* untuk jika tidak ada sub menu lagi */
             font-size: 15px;
             display: block;
              color: #ffffff;

                        }
                         #cssmenu > ul > li > a#trening_actual {
            /* untuk jika tidak ada sub menu lagi */
              font-size: 15px;
              display: block;
              /*color: #ffffff;*/

              background: url(<%=approot%>/menustylesheet/icon_flyout/trening/5.png) 5% center no-repeat;
              /*text-shadow: 0 1px 1px #000; warna text*/

            }
             #cssmenu > ul > li.active > a#trening_actual {
           /* untuk jika tidak ada sub menu lagi */
             font-size: 15px;
             display: block;
              color: #ffffff;

                        }
                         #cssmenu > ul > li > a#trening_search {
            /* untuk jika tidak ada sub menu lagi */
              font-size: 15px;
              display: block;
              /*color: #ffffff;*/

              background: url(<%=approot%>/menustylesheet/icon_flyout/trening/6.png) 5% center no-repeat;
              /*text-shadow: 0 1px 1px #000; warna text*/

            }
             #cssmenu > ul > li.active > a#trening_search {
           /* untuk jika tidak ada sub menu lagi */
             font-size: 15px;
             display: block;
              color: #ffffff;

                        }
                         #cssmenu > ul > li > a#trening_history {
            /* untuk jika tidak ada sub menu lagi */
              font-size: 15px;
              display: block;
              /*color: #ffffff;*/

              background: url(<%=approot%>/menustylesheet/icon_flyout/trening/7.png) 5% center no-repeat;
              /*text-shadow: 0 1px 1px #000; warna text*/

            }
             #cssmenu > ul > li.active > a#trening_history {
           /* untuk jika tidak ada sub menu lagi */
             font-size: 15px;
             display: block;
              color: #ffffff;

                        }
                          #cssmenu > ul > li > a#staff_control {
           /* untuk jika tidak ada sub menu lagi */
             font-size: 15px;
             display: block;
             /*color: #ffffff;*/

             background: url(<%=approot%>/menustylesheet/icon_flyout/reports/1.png) 5% center no-repeat;
             /*text-shadow: 0 1px 1px #000; warna text*/
                        }
                          #cssmenu > ul > li.active > a#staff_control {
           /* untuk jika tidak ada sub menu lagi */
             font-size: 15px;
             display: block;
             /*color: #ffffff;*/
              color: #ffffff;
             background: url(<%=approot%>/menustylesheet/icon_flyout/reports/1a.png) 5% center no-repeat;
             /*text-shadow: 0 1px 1px #000; warna text*/
                        }
                          #cssmenu > ul > li > a#Presence {
           /* untuk jika tidak ada sub menu lagi */
             font-size: 15px;
             display: block;
             /*color: #ffffff;*/

             background: url(<%=approot%>/menustylesheet/icon_flyout/reports/2.png) 5% center no-repeat;
             /*text-shadow: 0 1px 1px #000; warna text*/
                        }
                          #cssmenu > ul > li.active > a#Presence {
           /* untuk jika tidak ada sub menu lagi */
             font-size: 15px;
             display: block;
             /*color: #ffffff;*/
              color: #ffffff;
             background: url(<%=approot%>/menustylesheet/icon_flyout/reports/2a.png) 5% center no-repeat;
             /*text-shadow: 0 1px 1px #000; warna text*/
                        }
                          #cssmenu > ul > li > a#lateness {
           /* untuk jika tidak ada sub menu lagi */
             font-size: 15px;
             display: block;
             /*color: #ffffff;*/

             background: url(<%=approot%>/menustylesheet/icon_flyout/reports/3.png) 5% center no-repeat;
             /*text-shadow: 0 1px 1px #000; warna text*/
                        }
                          #cssmenu > ul > li.active > a#lateness {
           /* untuk jika tidak ada sub menu lagi */
             font-size: 15px;
             display: block;
             /*color: #ffffff;*/
              color: #ffffff;
             background: url(<%=approot%>/menustylesheet/icon_flyout/reports/3a.png) 5% center no-repeat;
             /*text-shadow: 0 1px 1px #000; warna text*/
                        }
                           #cssmenu > ul > li > a#split_shift {
           /* untuk jika tidak ada sub menu lagi */
             font-size: 15px;
             display: block;
             /*color: #ffffff;*/

             background: url(<%=approot%>/menustylesheet/icon_flyout/reports/4.png) 5% center no-repeat;
             /*text-shadow: 0 1px 1px #000; warna text*/
                        }
                          #cssmenu > ul > li.active > a#split_shift {
           /* untuk jika tidak ada sub menu lagi */
             font-size: 15px;
             display: block;
             /*color: #ffffff;*/
              color: #ffffff;
             background: url(<%=approot%>/menustylesheet/icon_flyout/reports/4a.png) 5% center no-repeat;
             /*text-shadow: 0 1px 1px #000; warna text*/
                        }
                         #cssmenu > ul > li > a#night_shift {
           /* untuk jika tidak ada sub menu lagi */
             font-size: 15px;
             display: block;
             /*color: #ffffff;*/

             background: url(<%=approot%>/menustylesheet/icon_flyout/reports/5.png) 5% center no-repeat;
             /*text-shadow: 0 1px 1px #000; warna text*/
                        }
                          #cssmenu > ul > li.active > a#night_shift {
           /* untuk jika tidak ada sub menu lagi */
             font-size: 15px;
             display: block;
             /*color: #ffffff;*/
              color: #ffffff;
             background: url(<%=approot%>/menustylesheet/icon_flyout/reports/5a.png) 5% center no-repeat;
             /*text-shadow: 0 1px 1px #000; warna text*/
                        }
                         #cssmenu > ul > li > a#absenteeism {
           /* untuk jika tidak ada sub menu lagi */
             font-size: 15px;
             display: block;
             /*color: #ffffff;*/

             background: url(<%=approot%>/menustylesheet/icon_flyout/reports/6.png) 5% center no-repeat;
             /*text-shadow: 0 1px 1px #000; warna text*/
                        }
                          #cssmenu > ul > li.active > a#absenteeism {
           /* untuk jika tidak ada sub menu lagi */
             font-size: 15px;
             display: block;
             /*color: #ffffff;*/
              color: #ffffff;
             background: url(<%=approot%>/menustylesheet/icon_flyout/reports/6a.png) 5% center no-repeat;
             /*text-shadow: 0 1px 1px #000; warna text*/
                        }
                          #cssmenu > ul > li > a#sickness {
           /* untuk jika tidak ada sub menu lagi */
             font-size: 15px;
             display: block;
             /*color: #ffffff;*/

             background: url(<%=approot%>/menustylesheet/icon_flyout/reports/7.png) 5% center no-repeat;
             /*text-shadow: 0 1px 1px #000; warna text*/
                        }
                          #cssmenu > ul > li.active > a#sickness {
           /* untuk jika tidak ada sub menu lagi */
             font-size: 15px;
             display: block;
             /*color: #ffffff;*/
              color: #ffffff;
             background: url(<%=approot%>/menustylesheet/icon_flyout/reports/7a.png) 5% center no-repeat;
             /*text-shadow: 0 1px 1px #000; warna text*/
                        }
                          #cssmenu > ul > li > a#special_dispensation {
           /* untuk jika tidak ada sub menu lagi */
             font-size: 15px;
             display: block;
             /*color: #ffffff;*/

             background: url(<%=approot%>/menustylesheet/icon_flyout/reports/8.png) 5% center no-repeat;
             /*text-shadow: 0 1px 1px #000; warna text*/
                        }
                          #cssmenu > ul > li.active > a#special_dispensation {
           /* untuk jika tidak ada sub menu lagi */
             font-size: 15px;
             display: block;
             /*color: #ffffff;*/
              color: #ffffff;
             background: url(<%=approot%>/menustylesheet/icon_flyout/reports/8a.png) 5% center no-repeat;
             /*text-shadow: 0 1px 1px #000; warna text*/
                        }
                          #cssmenu > ul > li > a#leave_report {
           /* untuk jika tidak ada sub menu lagi */
             font-size: 15px;
             display: block;
             /*color: #ffffff;*/

             background: url(<%=approot%>/menustylesheet/icon_flyout/reports/9.png) 5% center no-repeat;
             /*text-shadow: 0 1px 1px #000; warna text*/
                        }
                          #cssmenu > ul > li.active > a#leave_report {
           /* untuk jika tidak ada sub menu lagi */
             font-size: 15px;
             display: block;
             /*color: #ffffff;*/
              color: #ffffff;
             background: url(<%=approot%>/menustylesheet/icon_flyout/reports/9a.png) 5% center no-repeat;
             /*text-shadow: 0 1px 1px #000; warna text*/
                        }
                         #cssmenu > ul > li > a#trainee {
           /* untuk jika tidak ada sub menu lagi */
             font-size: 15px;
             display: block;
             /*color: #ffffff;*/

             background: url(<%=approot%>/menustylesheet/icon_flyout/reports/10.png) 5% center no-repeat;
             /*text-shadow: 0 1px 1px #000; warna text*/
                        }
                          #cssmenu > ul > li.active > a#trainee {
           /* untuk jika tidak ada sub menu lagi */
             font-size: 15px;
             display: block;
             /*color: #ffffff;*/
              color: #ffffff;
             background: url(<%=approot%>/menustylesheet/icon_flyout/reports/10a.png) 5% center no-repeat;
             /*text-shadow: 0 1px 1px #000; warna */
                        }
                         #cssmenu > ul > li > a#employee {
           /* untuk jika tidak ada sub menu lagi */
             font-size: 15px;
             display: block;
             /*color: #ffffff;*/

             background: url(<%=approot%>/menustylesheet/icon_flyout/reports/11.png) 5% center no-repeat;
             /*text-shadow: 0 1px 1px #000; warna text*/
                        }
                          #cssmenu > ul > li.active > a#employee {
           /* untuk jika tidak ada sub menu lagi */
             font-size: 15px;
             display: block;
             /*color: #ffffff;*/
              color: #ffffff;
             background: url(<%=approot%>/menustylesheet/icon_flyout/reports/11a.png) 5% center no-repeat;
             /*text-shadow: 0 1px 1px #000; warna text*/
                        }
                         #cssmenu > ul > li > a#traning_report {
           /* untuk jika tidak ada sub menu lagi */
             font-size: 15px;
             display: block;
             /*color: #ffffff;*/

             background: url(<%=approot%>/menustylesheet/icon_flyout/reports/12.png) 5% center no-repeat;
             /*text-shadow: 0 1px 1px #000; warna text*/
                        }
                          #cssmenu > ul > li.active > a#traning_report {
           /* untuk jika tidak ada sub menu lagi */
             font-size: 15px;
             display: block;
             /*color: #ffffff;*/
              color: #ffffff;
             background: url(<%=approot%>/menustylesheet/icon_flyout/reports/12a.png) 5% center no-repeat;
             /*text-shadow: 0 1px 1px #000; warna text*/
                        }
                         #cssmenu > ul > li > a#payroll {
           /* untuk jika tidak ada sub menu lagi */
             font-size: 15px;
             display: block;
             /*color: #ffffff;*/

             background: url(<%=approot%>/menustylesheet/icon_flyout/reports/13.png) 5% center no-repeat;
             /*text-shadow: 0 1px 1px #000; warna text*/
                        }
                          #cssmenu > ul > li.active > a#payroll {
           /* untuk jika tidak ada sub menu lagi */
             font-size: 15px;
             display: block;
             /*color: #ffffff;*/
              color: #ffffff;
             background: url(<%=approot%>/menustylesheet/icon_flyout/reports/13a.png) 5% center no-repeat;
             /*text-shadow: 0 1px 1px #000; warna text*/
                        }
                         #cssmenu > ul > li > a#employee_family {
           /* untuk jika tidak ada sub menu lagi */
             font-size: 15px;
             display: block;
             /*color: #ffffff;*/

             background: url(<%=approot%>/menustylesheet/icon_flyout/clinik/1.png) 5% center no-repeat;
             /*text-shadow: 0 1px 1px #000; warna text*/
                        }
                          #cssmenu > ul > li.active > a#employee_family {
           /* untuk jika tidak ada sub menu lagi */
             font-size: 15px;
             display: block;
             /*color: #ffffff;*/
              color: #ffffff;

             /*text-shadow: 0 1px 1px #000; warna text*/
                        }
                          #cssmenu > ul > li > a#medical_record {
           /* untuk jika tidak ada sub menu lagi */
             font-size: 15px;
             display: block;
             /*color: #ffffff;*/

             background: url(<%=approot%>/menustylesheet/icon_flyout/clinik/2.png) 5% center no-repeat;
             /*text-shadow: 0 1px 1px #000; warna text*/
                        }
                          #cssmenu > ul > li.active > a#medical_record {
           /* untuk jika tidak ada sub menu lagi */
             font-size: 15px;
             display: block;
             /*color: #ffffff;*/
              color: #ffffff;

             /*text-shadow: 0 1px 1px #000; warna text*/
                        }
                          #cssmenu > ul > li > a#Medicine {
           /* untuk jika tidak ada sub menu lagi */
             font-size: 15px;
             display: block;
             /*color: #ffffff;*/

             background: url(<%=approot%>/menustylesheet/icon_flyout/clinik/3.png) 5% center no-repeat;
             /*text-shadow: 0 1px 1px #000; warna text*/
                        }
                          #cssmenu > ul > li.active > a#Medicine {
           /* untuk jika tidak ada sub menu lagi */
             font-size: 15px;
             display: block;
             /*color: #ffffff;*/
              color: #ffffff;
             background: url(<%=approot%>/menustylesheet/icon_flyout/clinik/3a.png) 5% center no-repeat;
             /*text-shadow: 0 1px 1px #000; warna text*/
                        }
                           #cssmenu > ul > li > a#employee_visit {
           /* untuk jika tidak ada sub menu lagi */
             font-size: 15px;
             display: block;
             /*color: #ffffff;*/

             background: url(<%=approot%>/menustylesheet/icon_flyout/clinik/4.png) 5% center no-repeat;
             /*text-shadow: 0 1px 1px #000; warna text*/
                        }
                          #cssmenu > ul > li.active > a#employee_visit {
           /* untuk jika tidak ada sub menu lagi */
             font-size: 15px;
             display: block;
             /*color: #ffffff;*/
              color: #ffffff;

             /*text-shadow: 0 1px 1px #000; warna text*/
                        }
                         #cssmenu > ul > li > a#guest_handling {
           /* untuk jika tidak ada sub menu lagi */
             font-size: 15px;
             display: block;
             /*color: #ffffff;*/

             background: url(<%=approot%>/menustylesheet/icon_flyout/clinik/5.png) 5% center no-repeat;
             /*text-shadow: 0 1px 1px #000; warna text*/
                        }
                          #cssmenu > ul > li.active > a#guest_handling {
           /* untuk jika tidak ada sub menu lagi */
             font-size: 15px;
             display: block;
             /*color: #ffffff;*/
              color: #ffffff;

             /*text-shadow: 0 1px 1px #000; warna text*/
                        }
                         #cssmenu > ul > li > a#Disease {
           /* untuk jika tidak ada sub menu lagi */
             font-size: 15px;
             display: block;
             /*color: #ffffff;*/

             background: url(<%=approot%>/menustylesheet/icon_flyout/clinik/6.png) 5% center no-repeat;
             /*text-shadow: 0 1px 1px #000; warna text*/
                        }
                          #cssmenu > ul > li.active > a#Disease {
           /* untuk jika tidak ada sub menu lagi */
             font-size: 15px;
             display: block;
             /*color: #ffffff;*/
              color: #ffffff;
             background: url(<%=approot%>/menustylesheet/icon_flyout/clinik/6a.png) 5% center no-repeat;
             /*text-shadow: 0 1px 1px #000; warna text*/
                        }
                          #cssmenu > ul > li > a#Medical {
           /* untuk jika tidak ada sub menu lagi */
             font-size: 15px;
             display: block;
             /*color: #ffffff;*/

             background: url(<%=approot%>/menustylesheet/icon_flyout/clinik/7.png) 5% center no-repeat;
             /*text-shadow: 0 1px 1px #000; warna text*/
                        }
                          #cssmenu > ul > li.active > a#Medical {
           /* untuk jika tidak ada sub menu lagi */
             font-size: 15px;
             display: block;
             /*color: #ffffff;*/
              color: #ffffff;
             background: url(<%=approot%>/menustylesheet/icon_flyout/clinik/7a.png) 5% center no-repeat;
             /*text-shadow: 0 1px 1px #000; warna text*/
                        }
                         #cssmenu > ul > li > a#Locker {
           /* untuk jika tidak ada sub menu lagi */
             font-size: 15px;
             display: block;
             /*color: #ffffff;*/

             background: url(<%=approot%>/menustylesheet/icon_flyout/locker/01.png) 5% center no-repeat;
             /*text-shadow: 0 1px 1px #000; warna text*/
                        }
                          #cssmenu > ul > li.active > a#Locker {
           /* untuk jika tidak ada sub menu lagi */
             font-size: 15px;
             display: block;
             /*color: #ffffff;*/
              color: #ffffff;

             /*text-shadow: 0 1px 1px #000; warna text*/
                        }
                         #cssmenu > ul > li > a#locker_treatment {
           /* untuk jika tidak ada sub menu lagi */
             font-size: 15px;
             display: block;
             /*color: #ffffff;*/

             background: url(<%=approot%>/menustylesheet/icon_flyout/locker/02.png) 5% center no-repeat;
             /*text-shadow: 0 1px 1px #000; warna text*/
                        }
                          #cssmenu > ul > li.active > a#locker_treatment {
           /* untuk jika tidak ada sub menu lagi */
             font-size: 15px;
             display: block;
             /*color: #ffffff;*/
              color: #ffffff;

             /*text-shadow: 0 1px 1px #000; warna text*/
                        }
                          #cssmenu > ul > li > a#company {
           /* untuk jika tidak ada sub menu lagi */
             font-size: 15px;
             display: block;
             /*color: #ffffff;*/

             background: url(<%=approot%>/menustylesheet/icon_flyout/master/01.png) 5% center no-repeat;
             /*text-shadow: 0 1px 1px #000; warna text*/
                        }
                          #cssmenu > ul > li.active > a#company {
           /* untuk jika tidak ada sub menu lagi */
             font-size: 15px;
             display: block;
             /*color: #ffffff;*/
              color: #ffffff;
             background: url(<%=approot%>/menustylesheet/icon_flyout/master/01a.png) 5% center no-repeat;
             /*text-shadow: 0 1px 1px #000; warna text*/
                        }
                          #cssmenu > ul > li > a#employee {
           /* untuk jika tidak ada sub menu lagi */
             font-size: 15px;
             display: block;
             /*color: #ffffff;*/

             background: url(<%=approot%>/menustylesheet/icon_flyout/master/02.png) 5% center no-repeat;
             /*text-shadow: 0 1px 1px #000; warna text*/
                        }
                          #cssmenu > ul > li.active > a#employee {
           /* untuk jika tidak ada sub menu lagi */
             font-size: 15px;
             display: block;
             /*color: #ffffff;*/
              color: #ffffff;
            background: url(<%=approot%>/menustylesheet/icon_flyout/master/02a.png) 5% center no-repeat;
             /*text-shadow: 0 1px 1px #000; warna text*/
                        }
                          #cssmenu > ul > li > a#Schedule {
           /* untuk jika tidak ada sub menu lagi */
             font-size: 15px;
             display: block;
             /*color: #ffffff;*/

             background: url(<%=approot%>/menustylesheet/icon_flyout/master/03.png) 5% center no-repeat;
             /*text-shadow: 0 1px 1px #000; warna text*/
                        }
                          #cssmenu > ul > li.active > a#Schedule {
           /* untuk jika tidak ada sub menu lagi */
             font-size: 15px;
             display: block;
             /*color: #ffffff;*/
              color: #ffffff;
             background: url(<%=approot%>/menustylesheet/icon_flyout/master/03a.png) 5% center no-repeat;
             /*text-shadow: 0 1px 1px #000; warna text*/
                        }
                           #cssmenu > ul > li > a#locker_data {
           /* untuk jika tidak ada sub menu lagi */
             font-size: 15px;
             display: block;
             /*color: #ffffff;*/

             background: url(<%=approot%>/menustylesheet/icon_flyout/master/04.png) 5% center no-repeat;
             /*text-shadow: 0 1px 1px #000; warna text*/
                        }
                          #cssmenu > ul > li.active > a#locker_data {
           /* untuk jika tidak ada sub menu lagi */
             font-size: 15px;
             display: block;
             /*color: #ffffff;*/
              color: #ffffff;
             background: url(<%=approot%>/menustylesheet/icon_flyout/master/04a.png) 5% center no-repeat;
             /*text-shadow: 0 1px 1px #000; warna text*/
                        }
                         #cssmenu > ul > li > a#Assessment {
           /* untuk jika tidak ada sub menu lagi */
             font-size: 15px;
             display: block;
             /*color: #ffffff;*/

             background: url(<%=approot%>/menustylesheet/icon_flyout/master/05.png) 5% center no-repeat;
             /*text-shadow: 0 1px 1px #000; warna text*/
                        }
                          #cssmenu > ul > li.active > a#Assessment {
           /* untuk jika tidak ada sub menu lagi */
             font-size: 15px;
             display: block;
             /*color: #ffffff;*/
              color: #ffffff;
              background: url(<%=approot%>/menustylesheet/icon_flyout/master/05a.png) 5% center no-repeat;
             /*text-shadow: 0 1px 1px #000; warna text*/
                        }
                         #cssmenu > ul > li > a#Recruitment {
           /* untuk jika tidak ada sub menu lagi */
             font-size: 15px;
             display: block;
             /*color: #ffffff;*/

             background: url(<%=approot%>/menustylesheet/icon_flyout/master/06.png) 5% center no-repeat;
             /*text-shadow: 0 1px 1px #000; warna text*/
                        }
                          #cssmenu > ul > li.active > a#Recruitment {
           /* untuk jika tidak ada sub menu lagi */
             font-size: 15px;
             display: block;
             /*color: #ffffff;*/
              color: #ffffff;
             background: url(<%=approot%>/menustylesheet/icon_flyout/master/06a.png) 5% center no-repeat;
             /*text-shadow: 0 1px 1px #000; warna text*/
                        }
                          #cssmenu > ul > li > a#geo_area {
           /* untuk jika tidak ada sub menu lagi */
             font-size: 15px;
             display: block;
             /*color: #ffffff;*/

             background: url(<%=approot%>/menustylesheet/icon_flyout/master/07.png) 5% center no-repeat;
             /*text-shadow: 0 1px 1px #000; warna text*/
                        }
                          #cssmenu > ul > li.active > a#geo_area {
           /* untuk jika tidak ada sub menu lagi */
             font-size: 15px;
             display: block;
             /*color: #ffffff;*/
              color: #ffffff;
             background: url(<%=approot%>/menustylesheet/icon_flyout/master/07a.png) 5% center no-repeat;
             /*text-shadow: 0 1px 1px #000; warna text*/
                        }
                         #cssmenu > ul > li > a#user_management {
           /* untuk jika tidak ada sub menu lagi */
             font-size: 15px;
             display: block;
             /*color: #ffffff;*/

             background: url(<%=approot%>/menustylesheet/icon_flyout/system/01.png) 5% center no-repeat;
             /*text-shadow: 0 1px 1px #000; warna text*/
                        }
                          #cssmenu > ul > li.active > a#user_management {
           /* untuk jika tidak ada sub menu lagi */
             font-size: 15px;
             display: block;
             /*color: #ffffff;*/
              color: #ffffff;
             background: url(<%=approot%>/menustylesheet/icon_flyout/system/01a.png) 5% center no-repeat;
             /*text-shadow: 0 1px 1px #000; warna text*/
                        }
                          #cssmenu > ul > li > a#system_management {
           /* untuk jika tidak ada sub menu lagi */
             font-size: 15px;
             display: block;
             /*color: #ffffff;*/

             background: url(<%=approot%>/menustylesheet/icon_flyout/system/02.png) 5% center no-repeat;
             /*text-shadow: 0 1px 1px #000; warna text*/
                        }
                          #cssmenu > ul > li.active > a#system_management {
           /* untuk jika tidak ada sub menu lagi */
             font-size: 15px;
             display: block;
             /*color: #ffffff;*/
              color: #ffffff;
            background: url(<%=approot%>/menustylesheet/icon_flyout/system/02a.png) 5% center no-repeat;
             /*text-shadow: 0 1px 1px #000; warna text*/
                        }
                          #cssmenu > ul > li > a#time_keeping {
           /* untuk jika tidak ada sub menu lagi */
             font-size: 15px;
             display: block;
             /*color: #ffffff;*/

             background: url(<%=approot%>/menustylesheet/icon_flyout/system/03.png) 5% center no-repeat;
             /*text-shadow: 0 1px 1px #000; warna text*/
                        }
                          #cssmenu > ul > li.active > a#time_keeping {
           /* untuk jika tidak ada sub menu lagi */
             font-size: 15px;
             display: block;
             /*color: #ffffff;*/
              color: #ffffff;
             background: url(<%=approot%>/menustylesheet/icon_flyout/system/03a.png) 5% center no-repeat;
             /*text-shadow: 0 1px 1px #000; warna text*/
                        }
                           #cssmenu > ul > li > a#service_center {
           /* untuk jika tidak ada sub menu lagi */
             font-size: 15px;
             display: block;
             /*color: #ffffff;*/

             background: url(<%=approot%>/menustylesheet/icon_flyout/system/04.png) 5% center no-repeat;
             /*text-shadow: 0 1px 1px #000; warna text*/
                        }
                          #cssmenu > ul > li.active > a#service_center {
           /* untuk jika tidak ada sub menu lagi */
             font-size: 15px;
             display: block;
             /*color: #ffffff;*/
              color: #ffffff;

             /*text-shadow: 0 1px 1px #000; warna text*/
                        }
                         #cssmenu > ul > li > a#manual_prosess {
           /* untuk jika tidak ada sub menu lagi */
             font-size: 15px;
             display: block;
             /*color: #ffffff;*/

             background: url(<%=approot%>/menustylesheet/icon_flyout/system/05.png) 5% center no-repeat;
             /*text-shadow: 0 1px 1px #000; warna text*/
                        }
                          #cssmenu > ul > li.active > a#manual_prosess {
           /* untuk jika tidak ada sub menu lagi */
             font-size: 15px;
             display: block;
             /*color: #ffffff;*/
              color: #ffffff;

             /*text-shadow: 0 1px 1px #000; warna text*/
                        }
                         #cssmenu > ul > li > a#admin_query_setup {
           /* untuk jika tidak ada sub menu lagi */
             font-size: 15px;
             display: block;
             /*color: #ffffff;*/

             background: url(<%=approot%>/menustylesheet/icon_flyout/system/06.png) 5% center no-repeat;
             /*text-shadow: 0 1px 1px #000; warna text*/
                        }
                          #cssmenu > ul > li.active > a#admin_query_setup {
           /* untuk jika tidak ada sub menu lagi */
             font-size: 15px;
             display: block;
             /*color: #ffffff;*/
              color: #ffffff;

             /*text-shadow: 0 1px 1px #000; warna text*/
                        }
                         #cssmenu > ul > li > a#General {
           /* untuk jika tidak ada sub menu lagi */
             font-size: 15px;
             display: block;
             /*color: #ffffff;*/

             background: url(<%=approot%>/menustylesheet/icon_flyout/payroll_setup/01.png) 5% center no-repeat;
             /*text-shadow: 0 1px 1px #000; warna text*/
                        }
                          #cssmenu > ul > li.active > a#General {
           /* untuk jika tidak ada sub menu lagi */
             font-size: 15px;
             display: block;
             /*color: #ffffff;*/
              color: #ffffff;

             /*text-shadow: 0 1px 1px #000; warna text*/
                        }
                          #cssmenu > ul > li > a#payroll_periode {
           /* untuk jika tidak ada sub menu lagi */
             font-size: 15px;
             display: block;
             /*color: #ffffff;*/

             background: url(<%=approot%>/menustylesheet/icon_flyout/payroll_setup/02.png) 5% center no-repeat;
             /*text-shadow: 0 1px 1px #000; warna text*/
                        }
                          #cssmenu > ul > li.active > a#payroll_periode {
           /* untuk jika tidak ada sub menu lagi */
             font-size: 15px;
             display: block;
             /*color: #ffffff;*/
              color: #ffffff;

             /*text-shadow: 0 1px 1px #000; warna text*/
                        }
                          #cssmenu > ul > li > a#bank_list {
           /* untuk jika tidak ada sub menu lagi */
             font-size: 15px;
             display: block;
             /*color: #ffffff;*/

             background: url(<%=approot%>/menustylesheet/icon_flyout/payroll_setup/03.png) 5% center no-repeat;
             /*text-shadow: 0 1px 1px #000; warna text*/
                        }
                          #cssmenu > ul > li.active > a#bank_list {
           /* untuk jika tidak ada sub menu lagi */
             font-size: 15px;
             display: block;
             /*color: #ffffff;*/
              color: #ffffff;

             /*text-shadow: 0 1px 1px #000; warna text*/
                        }
                           #cssmenu > ul > li > a#pay_slip_group {
           /* untuk jika tidak ada sub menu lagi */
             font-size: 15px;
             display: block;
             /*color: #ffffff;*/

             background: url(<%=approot%>/menustylesheet/icon_flyout/payroll_setup/04.png) 5% center no-repeat;
             /*text-shadow: 0 1px 1px #000; warna text*/
                        }
                          #cssmenu > ul > li.active > a#pay_slip_group {
           /* untuk jika tidak ada sub menu lagi */
             font-size: 15px;
             display: block;
             /*color: #ffffff;*/
              color: #ffffff;

             /*text-shadow: 0 1px 1px #000; warna text*/
                        }
                         #cssmenu > ul > li > a#salary_component {
           /* untuk jika tidak ada sub menu lagi */
             font-size: 15px;
             display: block;
             /*color: #ffffff;*/

             background: url(<%=approot%>/menustylesheet/icon_flyout/payroll_setup/05.png) 5% center no-repeat;
             /*text-shadow: 0 1px 1px #000; warna text*/
                        }
                          #cssmenu > ul > li.active > a#salary_component {
           /* untuk jika tidak ada sub menu lagi */
             font-size: 15px;
             display: block;
             /*color: #ffffff;*/
              color: #ffffff;

             /*text-shadow: 0 1px 1px #000; warna text*/
                        }
                         #cssmenu > ul > li > a#salary_level {
           /* untuk jika tidak ada sub menu lagi */
             font-size: 15px;
             display: block;
             /*color: #ffffff;*/

             background: url(<%=approot%>/menustylesheet/icon_flyout/payroll_setup/06.png) 5% center no-repeat;
             /*text-shadow: 0 1px 1px #000; warna text*/
                        }
                          #cssmenu > ul > li.active > a#salary_level {
           /* untuk jika tidak ada sub menu lagi */
             font-size: 15px;
             display: block;
             /*color: #ffffff;*/
              color: #ffffff;

             /*text-shadow: 0 1px 1px #000; warna text*/
                        }
                        #cssmenu > ul > li > a#employee_setup {
           /* untuk jika tidak ada sub menu lagi */
             font-size: 15px;
             display: block;
             /*color: #ffffff;*/

             background: url(<%=approot%>/menustylesheet/icon_flyout/payroll_setup/07.png) 5% center no-repeat;
             /*text-shadow: 0 1px 1px #000; warna text*/
                        }
                          #cssmenu > ul > li.active > a#employee_setup {
           /* untuk jika tidak ada sub menu lagi */
             font-size: 15px;
             display: block;
             /*color: #ffffff;*/
              color: #ffffff;

             /*text-shadow: 0 1px 1px #000; warna text*/
                        }
                        #cssmenu > ul > li > a#Currency {
           /* untuk jika tidak ada sub menu lagi */
             font-size: 15px;
             display: block;
             /*color: #ffffff;*/

             background: url(<%=approot%>/menustylesheet/icon_flyout/payroll_setup/08.png) 5% center no-repeat;
             /*text-shadow: 0 1px 1px #000; warna text*/
                        }
                          #cssmenu > ul > li.active > a#Currency {
           /* untuk jika tidak ada sub menu lagi */
             font-size: 15px;
             display: block;
             /*color: #ffffff;*/
              color: #ffffff;

             /*text-shadow: 0 1px 1px #000; warna text*/
                        }
                        #cssmenu > ul > li > a#Currency_rate {
           /* untuk jika tidak ada sub menu lagi */
             font-size: 15px;
             display: block;
             /*color: #ffffff;*/

             background: url(<%=approot%>/menustylesheet/icon_flyout/payroll_setup/09.png) 5% center no-repeat;
             /*text-shadow: 0 1px 1px #000; warna text*/
                        }
                          #cssmenu > ul > li.active > a#Currency_rate {
           /* untuk jika tidak ada sub menu lagi */
             font-size: 15px;
             display: block;
             /*color: #ffffff;*/
              color: #ffffff;

             /*text-shadow: 0 1px 1px #000; warna text*/
                        }
                         #cssmenu > ul > li > a#overtime_form {
           /* untuk jika tidak ada sub menu lagi */
             font-size: 15px;
             display: block;
             /*color: #ffffff;*/

             background: url(<%=approot%>/menustylesheet/icon_flyout/over_time/01.png) 5% center no-repeat;
             /*text-shadow: 0 1px 1px #000; warna text*/
                        }
                          #cssmenu > ul > li.active > a#overtime_form {
           /* untuk jika tidak ada sub menu lagi */
             font-size: 15px;
             display: block;
             /*color: #ffffff;*/
              color: #ffffff;

             /*text-shadow: 0 1px 1px #000; warna text*/
                        }
                          #cssmenu > ul > li > a#overtime_report {
           /* untuk jika tidak ada sub menu lagi */
             font-size: 15px;
             display: block;
             /*color: #ffffff;*/

             background: url(<%=approot%>/menustylesheet/icon_flyout/over_time/02.png) 5% center no-repeat;
             /*text-shadow: 0 1px 1px #000; warna text*/
                        }
                          #cssmenu > ul > li.active > a#overtime_report {
           /* untuk jika tidak ada sub menu lagi */
             font-size: 15px;
             display: block;
             /*color: #ffffff;*/
              color: #ffffff;

             /*text-shadow: 0 1px 1px #000; warna text*/
                        }
                          #cssmenu > ul > li > a#overtime_index {
           /* untuk jika tidak ada sub menu lagi */
             font-size: 15px;
             display: block;
             /*color: #ffffff;*/

             background: url(<%=approot%>/menustylesheet/icon_flyout/over_time/03.png) 5% center no-repeat;
             /*text-shadow: 0 1px 1px #000; warna text*/
                        }
                          #cssmenu > ul > li.active > a#overtime_index {
           /* untuk jika tidak ada sub menu lagi */
             font-size: 15px;
             display: block;
             /*color: #ffffff;*/
              color: #ffffff;

             /*text-shadow: 0 1px 1px #000; warna text*/
                        }
                           #cssmenu > ul > li > a#overtime_summary {
           /* untuk jika tidak ada sub menu lagi */
             font-size: 15px;
             display: block;
             /*color: #ffffff;*/

             background: url(<%=approot%>/menustylesheet/icon_flyout/over_time/04.png) 5% center no-repeat;
             /*text-shadow: 0 1px 1px #000; warna text*/
                        }
                          #cssmenu > ul > li.active > a#overtime_summary {
           /* untuk jika tidak ada sub menu lagi */
             font-size: 15px;
             display: block;
             /*color: #ffffff;*/
              color: #ffffff;

             /*text-shadow: 0 1px 1px #000; warna text*/
                        }
                           #cssmenu > ul > li > a#prepare_data {
           /* untuk jika tidak ada sub menu lagi */
             font-size: 15px;
             display: block;
             /*color: #ffffff;*/

             background: url(<%=approot%>/menustylesheet/icon_flyout/payroll_procces/01.png) 5% center no-repeat;
             /*text-shadow: 0 1px 1px #000; warna text*/
                        }
                          #cssmenu > ul > li.active > a#prepare_data {
           /* untuk jika tidak ada sub menu lagi */
             font-size: 15px;
             display: block;
             /*color: #ffffff;*/
              color: #ffffff;

             /*text-shadow: 0 1px 1px #000; warna text*/
                        }
                          #cssmenu > ul > li > a#payroll_input {
           /* untuk jika tidak ada sub menu lagi */
             font-size: 15px;
             display: block;
             /*color: #ffffff;*/

             background: url(<%=approot%>/menustylesheet/icon_flyout/payroll_procces/02.png) 5% center no-repeat;
             /*text-shadow: 0 1px 1px #000; warna text*/
                        }
                          #cssmenu > ul > li.active > a#payroll_input {
           /* untuk jika tidak ada sub menu lagi */
             font-size: 15px;
             display: block;
             /*color: #ffffff;*/
              color: #ffffff;

             /*text-shadow: 0 1px 1px #000; warna text*/
                        }
                          #cssmenu > ul > li > a#payroll_process {
           /* untuk jika tidak ada sub menu lagi */
             font-size: 15px;
             display: block;
             /*color: #ffffff;*/

             background: url(<%=approot%>/menustylesheet/icon_flyout/payroll_procces/03.png) 5% center no-repeat;
             /*text-shadow: 0 1px 1px #000; warna text*/
                        }
                          #cssmenu > ul > li.active > a#payroll_process {
           /* untuk jika tidak ada sub menu lagi */
             font-size: 15px;
             display: block;
             /*color: #ffffff;*/
              color: #ffffff;

             /*text-shadow: 0 1px 1px #000; warna text*/
                        }
                           #cssmenu > ul > li > a#payslip_printing {
           /* untuk jika tidak ada sub menu lagi */
             font-size: 15px;
             display: block;
             /*color: #ffffff;*/

             background: url(<%=approot%>/menustylesheet/icon_flyout/payroll_procces/04.png) 5% center no-repeat;
             /*text-shadow: 0 1px 1px #000; warna text*/
                        }
                          #cssmenu > ul > li.active > a#payslip_printing {
           /* untuk jika tidak ada sub menu lagi */
             font-size: 15px;
             display: block;
             /*color: #ffffff;*/
              color: #ffffff;

             /*text-shadow: 0 1px 1px #000; warna text*/
                        }
                         #cssmenu > ul > li > a#Reports {
           /* untuk jika tidak ada sub menu lagi */
             font-size: 15px;
             display: block;
             /*color: #ffffff;*/

             background: url(<%=approot%>/menustylesheet/icon_flyout/canteen/01.png) 5% center no-repeat;
             /*text-shadow: 0 1px 1px #000; warna text*/
                        }
                          #cssmenu > ul > li.active > a#Reports {
           /* untuk jika tidak ada sub menu lagi */
             font-size: 15px;
             display: block;
             /*color: #ffffff;*/
              color: #ffffff;
              background: url(<%=approot%>/menustylesheet/icon_flyout/canteen/01a.png) 5% center no-repeat;
             /*text-shadow: 0 1px 1px #000; warna text*/
                        }
                           #cssmenu > ul > li > a#Data {
           /* untuk jika tidak ada sub menu lagi */
             font-size: 15px;
             display: block;
             /*color: #ffffff;*/

             background: url(<%=approot%>/menustylesheet/icon_flyout/canteen/02.png) 5% center no-repeat;
             /*text-shadow: 0 1px 1px #000; warna text*/
                        }
                          #cssmenu > ul > li.active > a#Data {
           /* untuk jika tidak ada sub menu lagi */
             font-size: 15px;
             display: block;
             /*color: #ffffff;*/
              color: #ffffff;
              background: url(<%=approot%>/menustylesheet/icon_flyout/canteen/02a.png) 5% center no-repeat;
             /*text-shadow: 0 1px 1px #000; warna text*/
                        }
</style>
        <div>
            <%if (url != null && url.length() > 0 && url.equalsIgnoreCase("home.jsp")) {%>

            <%} else if (url != null && url.length() > 0 && url.equalsIgnoreCase("employee.jsp")) {%>
            <div id="menuheaderx"><h2>Employee</h2></div>
                <div id="menu_kiri">
                    <div id="cssmenu">
                        <ul>
                            <li><a id="data_bank" href='flyout.jsp'><span><p id="has-sub-align">Data Bank</p></span></a></li>
                            <li><a id="company_structure" href='#'><span><p id="has-sub-align">Company Structure</p></span></a></li>
                            <li><a id="new_employee" href='#'><span><p id="has-sub-align">New Employee</p></span></a></li>
                             <li><a id="absence_management" href='#'><span><p id="has-sub-align">Absence Management</p></span></a></li>
                             <li><a id="recognitions" href='#'><span><p id="has-sub-align">Recognitions</p></span></a><!-- untuk has-sub bisa di rubah sesuai gambarnya -->
                                <ul>
                                    <!--<li id='has-sub-menu'><pxsubmenu> Sub Menu Produck </pxsubmenu></li>-->
                                    <li id='has-sub-menu1'><a href='#'><px>01.Entry per Departement</px></a></li>
                                    <li  style="margin-top: <%=sizeSpasiPerSubMenu%>;" id='has-sub-menu1'><a href='#'><px>02.Update <enter_word>per Employee</enter_word></px></a></li>
                                    <!--<li id='has-sub-menu'><a href='#'><pxsubmenu>About</pxsubmenu></a></li>-->
                                </ul>
                            </li>
                            <li><a id="recruitment" href='#'><span><p id="has-sub-align">Recruitment</p></span></a><!-- untuk has-sub bisa di rubah sesuai gambarnya -->
                                <ul>
                                    <!--<li id='has-sub-menu'><pxsubmenu> Sub Menu Produck </pxsubmenu></li>-->
                                    <li id='has-sub-menu1'><a href='#'><px>01.Staff Requisition</px></a></li>
                                    <li id='has-sub-menu1'><a href='#'><px>02.Employement Application</px></a></li>
                                    <li id='has-sub-menu1'><a href='#'><px>03.Orientation Checklist</px></a></li>
                                    <li id='has-sub-menu1'><a href='#'><px>04.Reminder</px></a></li>
                                    <!--<li id='has-sub-menu'><a href='#'><pxsubmenu>About</pxsubmenu></a></li>-->
                                </ul>
                            </li>
                            <li><a id="warning" href='#'><span><p id="has-sub-align">Warning & Reprimand</p></span></a><!-- untuk has-sub bisa di rubah sesuai gambarnya -->
                                <ul>
                                    <!--<li id='has-sub-menu'><pxsubmenu> Sub Menu Produck </pxsubmenu></li>-->
                                    <li id='has-sub-menu1'><a href='#'><px>01.Warning</px></a></li>
                                    <li id='has-sub-menu1'><a href='#'><px>02.Reprimand</px></a></li>
                                    <!--<li id='has-sub-menu'><a href='#'><pxsubmenu>About</pxsubmenu></a></li>-->
                                </ul>

                            </li>
                        </ul>
                    </div>
                </div>
                <div id="menu_kanan">
                    <div id='cssmenu'>
                        <ul>
                            <li><a id="excuse_application" href='#'><span><p id="has-sub-align">Excuse Application</p></span></a><!-- untuk has-sub bisa di rubah sesuai gambarnya -->
                                <ul>
                                    <!--<li id='has-sub-menu'><pxsubmenu> Sub Menu Produck </pxsubmenu></li>-->
                                    <li id='has-sub-menu1'><a href='#'><px>01.Excuse Form</px></a></li>

                                    <!--<li id='has-sub-menu'><a href='#'><pxsubmenu>About</pxsubmenu></a></li>-->
                                </ul>

                            </li>
                            <li><a id="attendance" href='#'><span><p id="has-sub-align">Attendance</p></span></a>
                                <ul>
                                    <!--<li id='has-sub-menu'><pxsubmenu> Sub Menu Produck </pxsubmenu></li>-->
                                    <li id='has-sub-menu1'><a href='#'><px>01.Working Schedule</px></a></li>
                                    <li id='has-sub-menu1'><a href='#'><px>02.Manual Registration</px></a></li>
                                    <!--<li id='has-sub-menu'><pxsubmenu> Sub Menu Produck1 </pxsubmenu></li>-->
                                    <li id='has-sub-menu1'><a href='#'><px>03.Re-Generate Schedule Holidays</px></a></li>
                                    <li id='has-sub-menu1'><a href='#'><px>04.Schedule</px></a></li>
                                    <!--<li id='has-sub-menu'><a href='#'><pxsubmenu>About</pxsubmenu></a></li>-->
                                </ul>
                            </li>
                            <li><a id="leave_application" href='#'><span><p id="has-sub-align">Leave Application</p></span></a><!-- untuk has-sub bisa di rubah sesuai gambarnya -->
                                <ul>
                                    <!--<li id='has-sub-menu'><pxsubmenu> Sub Menu Produck </pxsubmenu></li>-->
                                    <li id='has-sub-menu1'><a href='#'><px>01.Leave Form</px></a></li>
                                    <li id='has-sub-menu1'><a href='#'><px>02.Leave AL Closing</px></a></li>
                                    <!--<li id='has-sub-menu'><pxsubmenu> Sub Menu Produck1 </pxsubmenu></li>-->
                                    <li id='has-sub-menu1'><a href='#'><px>03.Leave LI Closing</px></a></li>
                                    <li id='has-sub-menu1'><a href='#'><px>04.DP Management</px></a></li>
                                    <li id='has-sub-menu1'><a href='#'><px>05.DP Re-Calculate</px></a></li>
                                    <!--<li id='has-sub-menu'><a href='#'><pxsubmenu>About</pxsubmenu></a></li>-->
                                </ul>
                            </li>
                            <li><a id="leave_balancing" href='#'><span><p id="has-sub-align">Leave Balancing</p></span></a><!-- untuk has-sub bisa di rubah sesuai gambarnya -->
                                <ul>
                                    <!--<li id='has-sub-menu'><pxsubmenu> Sub Menu Produck </pxsubmenu></li>-->
                                    <li id='has-sub-menu1'><a href='#'><px>01.Annual Leave</px></a></li>
                                    <li id='has-sub-menu1'><a href='#'><px>02.Long Leave</px></a></li>
                                    <!--<li id='has-sub-menu'><pxsubmenu> Sub Menu Produck1 </pxsubmenu></li>-->
                                    <li id='has-sub-menu1'><a href='#'><px>03.Day off Payment</px></a></li>

                                    <!--<li id='has-sub-menu'><a href='#'><pxsubmenu>About</pxsubmenu></a></li>-->
                                </ul>
                            </li>
                            <li><a id="assessment" href='#'><span><p id="has-sub-align">Assessment</p></span></a><!-- untuk has-sub bisa di rubah sesuai gambarnya -->
                                <ul>
                                    <!--<li id='has-sub-menu'><pxsubmenu> Sub Menu Produck </pxsubmenu></li>-->
                                    <li id='has-sub-menu1'><a href='#'><px>01.Explanations and Coverage</px></a></li>
                                    <li id='has-sub-menu1'><a href='#'><px>02.Performance Assessment</px></a></li>
                                    <!--<li id='has-sub-menu'><a href='#'><pxsubmenu>About</pxsubmenu></a></li>-->
                                </ul>
                            </li>
                        </ul>
                    </div>
                </div>
            <%} else if (url != null && url.length() > 0 && url.equalsIgnoreCase("training.jsp")) {%>
                <div id="menuheader"><h2>Training</h2></div>
                    <div id="menu_kiri">

                    <div id='cssmenu'>
                        <ul>
                            <li><a id="trening_type" href='flyout.jsp'><span><p id="has-sub-align">Training Type</p></span></a></li>
                            <li><a id="trening_vanue" href='#'><span><p id="has-sub-align">Training Vanue</p></span></a></li>
                            <li><a id="trening_program" href='#'><span><p id="has-sub-align">Training Program</p></span></a></li>
                            <li><a id="trening_plan" href='#'><span><p id="has-sub-align">Training Plan</p></span></a></li>
                            <li><a id="trening_actual" href='#'><span><p id="has-sub-align">Training Actual</p></span></a></li>
                            <li><a id="trening_search" href='#'><span><p id="has-sub-align">Training Search</p></span></a></li>
                            <li><a id="trening_history" href='#'><span><p id="has-sub-align">Training History</p></span></a></li>
                        </ul>
                    </div>
                </div>
            <!-- -- test -->
            <%} else if (url != null && url.length() > 0 && url.equalsIgnoreCase("reports.jsp")) {%>
                <div id="menuheader"><h2>Reports</h2></div>
                    <div id="menu_kiri">
                        <div id='cssmenu'>
                            <ul>

                                <li><a id="staff_control" href='#'><span><p id="has-sub-align">Staff Control</p></span></a>
                                    <ul>
                                        <!--<li id='has-sub-menu'><pxsubmenu> Sub Menu Produck </pxsubmenu></li>-->
                                        <li id='has-sub-menu1'><a href='#'><px>01.Attendance Record</px></a></li>

                                    </ul>

                                </li>
                                <li><a id="Presence" href='#'><span><p id="has-sub-align">Presence</p></span></a><!-- untuk has-sub bisa di rubah sesuai gambarnya -->
                                    <ul>
                                        <!--<li id='has-sub-menu'><pxsubmenu> Sub Menu Produck </pxsubmenu></li>-->
                                        <li id='has-sub-menu1'><a href='#'><px>01.Daily Report</px></a></li>
                                        <li id='has-sub-menu1'><a href='#'><px>02.Weekly Report</px></a></li>
                                        <!--<li id='has-sub-menu'><pxsubmenu> Sub Menu Produck1 </pxsubmenu></li>-->
                                        <li id='has-sub-menu1'><a href='#'><px>03.Monthly Report</px></a></li>
                                        <li id='has-sub-menu1'><a href='#'><px>04.Year Report</px></a></li>
                                        <li id='has-sub-menu1'><a href='#'><px>05.Attendance Summary</px></a></li>
                                        <!--<li id='has-sub-menu'><a href='#'><pxsubmenu>About</pxsubmenu></a></li>-->
                                    </ul>

                                </li>
                                <li><a id="lateness" href='#'><span><p id="has-sub-align">Lateness</p></span></a><!-- untuk has-sub bisa di rubah sesuai gambarnya -->
                                    <ul>
                                        <!--<li id='has-sub-menu'><pxsubmenu> Sub Menu Produck </pxsubmenu></li>-->
                                        <li id='has-sub-menu1'><a href='#'><px>01.Daily Report</px></a></li>
                                        <li id='has-sub-menu1'><a href='#'><px>02.Weekly Report</px></a></li>
                                        <!--<li id='has-sub-menu'><pxsubmenu> Sub Menu Produck1 </pxsubmenu></li>-->
                                        <li id='has-sub-menu1'><a href='#'><px>03.Monthly Report</px></a></li>

                                        <!--<li id='has-sub-menu'><a href='#'><pxsubmenu>About</pxsubmenu></a></li>-->
                                    </ul>

                                </li>
                                 <li><a id="split_shift" href='#'><span><p id="has-sub-align">Split Shift</p></span></a><!-- untuk has-sub bisa di rubah sesuai gambarnya -->
                                    <ul>
                                        <!--<li id='has-sub-menu'><pxsubmenu> Sub Menu Produck </pxsubmenu></li>-->
                                        <li id='has-sub-menu1'><a href='#'><px>01.Daily Report</px></a></li>
                                        <li id='has-sub-menu1'><a href='#'><px>02.Weekly Report</px></a></li>
                                        <!--<li id='has-sub-menu'><pxsubmenu> Sub Menu Produck1 </pxsubmenu></li>-->
                                        <li id='has-sub-menu1'><a href='#'><px>03.Monthly Report</px></a></li>

                                        <!--<li id='has-sub-menu'><a href='#'><pxsubmenu>About</pxsubmenu></a></li>-->
                                    </ul>

                                </li>
                                 <li><a id="night_shift" href='#'><span><p id="has-sub-align">Night Shift</p></span></a><!-- untuk has-sub bisa di rubah sesuai gambarnya -->
                                    <ul>
                                        <!--<li id='has-sub-menu'><pxsubmenu> Sub Menu Produck </pxsubmenu></li>-->
                                        <li id='has-sub-menu1'><a href='#'><px>01.Daily Report</px></a></li>
                                        <li id='has-sub-menu1'><a href='#'><px>02.Weekly Report</px></a></li>
                                        <!--<li id='has-sub-menu'><pxsubmenu> Sub Menu Produck1 </pxsubmenu></li>-->
                                        <li id='has-sub-menu1'><a href='#'><px>03.Monthly Report</px></a></li>

                                        <!--<li id='has-sub-menu'><a href='#'><pxsubmenu>About</pxsubmenu></a></li>-->
                                    </ul>

                                </li>
                                 <li><a id="absenteeism" href='#'><span><p id="has-sub-align">Absenteeism</p></span></a><!-- untuk has-sub bisa di rubah sesuai gambarnya -->
                                    <ul>
                                        <!--<li id='has-sub-menu'><pxsubmenu> Sub Menu Produck </pxsubmenu></li>-->
                                        <li id='has-sub-menu1'><a href='#'><px>01.Daily Report</px></a></li>
                                        <li id='has-sub-menu1'><a href='#'><px>02.Weekly Report</px></a></li>
                                        <!--<li id='has-sub-menu'><pxsubmenu> Sub Menu Produck1 </pxsubmenu></li>-->
                                        <li id='has-sub-menu1'><a href='#'><px>03.Monthly Report</px></a></li>

                                        <!--<li id='has-sub-menu'><a href='#'><pxsubmenu>About</pxsubmenu></a></li>-->
                                    </ul>

                                </li>
                                <li ><a id="sickness" href='#'><span><p id="has-sub-align">Sickness</p></span></a><!-- untuk has-sub bisa di rubah sesuai gambarnya -->
                                    <ul >
                                        <!--<li id='has-sub-menu'><pxsubmenu> Sub Menu Produck </pxsubmenu></li>-->
                                        <li id='has-sub-menu1'><a href='#'><px>01.Daily Report</px></a></li>
                                        <li id='has-sub-menu1'><a href='#'><px>02.Weekly Report</px></a></li>
                                        <!--<li id='has-sub-menu'><pxsubmenu> Sub Menu Produck1 </pxsubmenu></li>-->
                                        <li id='has-sub-menu1'><a href='#'><px>03.Monthly Report</px></a></li>
                                        <li id='has-sub-menu1'><a href='#'><px>04.Zero Sickness</px></a></li>
                                        <!--<li id='has-sub-menu'><a href='#'><pxsubmenu>About</pxsubmenu></a></li>-->
                                    </ul>

                                </li>
                                 </ul>
                        </div>
                    </div>
                <!-- test -->
                    <div id="menu_kanan">
                       <div id='cssmenu'>
                          <ul>
                            <li><a id="special_dispensation" href='#'><span><p id="has-sub-align">Special Dispensation</p></span></a><!-- untuk has-sub bisa di rubah sesuai gambarnya -->
                                <ul>
                                    <!--<li id='has-sub-menu'><pxsubmenu> Sub Menu Produck </pxsubmenu></li>-->
                                    <li id='has-sub-menu1'><a href='#'><px>01.Daily Report</px></a></li>
                                    <li id='has-sub-menu1'><a href='#'><px>02.Weekly Report</px></a></li>
                                    <!--<li id='has-sub-menu'><pxsubmenu> Sub Menu Produck1 </pxsubmenu></li>-->
                                    <li id='has-sub-menu1'><a href='#'><px>03.Monthly Report</px></a></li>

                                    <!--<li id='has-sub-menu'><a href='#'><pxsubmenu>About</pxsubmenu></a></li>-->
                                </ul>

                            </li>
                             <li><a id="leave_report" href='#'><span><p id="has-sub-align">Leave Report</p></span></a><!-- untuk has-sub bisa di rubah sesuai gambarnya -->
                                <ul>
                                    <!--<li id='has-sub-menu'><pxsubmenu> Sub Menu Produck </pxsubmenu></li>-->
                                    <li id='has-sub-menu1'><a href='#'><px>01.Leave & DP Summary</px></a></li>
                                    <li id='has-sub-menu1'><a href='#'><px>02.Leave & DP Detail</px></a></li>
                                    <!--<li id='has-sub-menu'><pxsubmenu> Sub Menu Produck1 </pxsubmenu></li>-->
                                    <li id='has-sub-menu1'><a href='#'><px>03.Leave & DP Sum.Period</px></a></li>
                                    <li id='has-sub-menu1'><a href='#'><px>04.Leave & DP Detail Period</px></a></li>
                                    <li id='has-sub-menu1'><a href='#'><px>05.Special & Unpaid Period</px></a></li>
                                    <li id='has-sub-menu1'><a href='#'><px>06.DP Ekspired</px></a></li>
                                    <!--<li id='has-sub-menu'><a href='#'><pxsubmenu>About</pxsubmenu></a></li>-->
                                </ul>

                            </li>
                            <li><a id="trainee" href='#'><span><p id="has-sub-align">Trainee</p></span></a><!-- untuk has-sub bisa di rubah sesuai gambarnya -->
                                <ul>
                                    <!--<li id='has-sub-menu'><pxsubmenu> Sub Menu Produck </pxsubmenu></li>-->
                                    <li id='has-sub-menu1'><a href='#'><px>01.Monthly Report</px></a></li>
                                    <li id='has-sub-menu1'><a href='#'><px>02.End Period</px></a></li>
                                    <!--<li id='has-sub-menu'><pxsubmenu> Sub Menu Produck1 </pxsubmenu></li>-->

                                    <!--<li id='has-sub-menu'><a href='#'><pxsubmenu>About</pxsubmenu></a></li>-->
                                </ul>

                            </li>
                             <li><a id="employee" href='#'><span><p id="has-sub-align">Employee</p></span></a><!-- untuk has-sub bisa di rubah sesuai gambarnya -->
                                <ul>
                                    <!--<li id='has-sub-menu'><pxsubmenu> Sub Menu Produck </pxsubmenu></li>-->
                                    <li id='has-sub-menu1'><a href='#'><px>01.List of Employee Category</px></a></li>
                                    <li id='has-sub-menu1'><a href='#'><px>02.List of Employee Resignation</px></a></li>
                                    <!--<li id='has-sub-menu'><pxsubmenu> Sub Menu Produck1 </pxsubmenu></li>-->
                                    <li id='has-sub-menu1'><a href='#'><px>03.List of Employee Education</px></a></li>
                                    <li id='has-sub-menu1'><a href='#'><px>04.List of Employee Category by Name</px></a></li>
                                    <li id='has-sub-menu1'><a href='#'><px>05.List Number Of Absences</px></a></li>
                                    <li id='has-sub-menu1'><a href='#'><px>06.List Of Employee Race</px></a></li>
                                    <!--<li id='has-sub-menu'><a href='#'><pxsubmenu>About</pxsubmenu></a></li>-->
                                </ul>

                            </li>
                             <li><a id="traning_report" href='#'><span><p id="has-sub-align">Traning Report</p></span></a><!-- untuk has-sub bisa di rubah sesuai gambarnya -->
                                <ul>
                                    <!--<li id='has-sub-menu'><pxsubmenu> Sub Menu Produck </pxsubmenu></li>-->
                                    <li id='has-sub-menu1'><a href='#'><px>01.Monthly Traning</px></a></li>
                                    <li id='has-sub-menu1'><a href='#'><px>02.Traning Profiles</px></a></li>
                                    <!--<li id='has-sub-menu'><pxsubmenu> Sub Menu Produck1 </pxsubmenu></li>-->
                                    <li id='has-sub-menu1'><a href='#'><px>03.Traning Target</px></a></li>
                                    <li id='has-sub-menu1'><a href='#'><px>04.Report By Departement</px></a></li>
                                    <li id='has-sub-menu1'><a href='#'><px>05.Report By Employee</px></a></li>
                                    <li id='has-sub-menu1'><a href='#'><px>06.Report By Course Detail</px></a></li>
                                    <li id='has-sub-menu1'><a href='#'><px>06.Report By Course Date</px></a></li>
                                    <!--<li id='has-sub-menu'><a href='#'><pxsubmenu>About</pxsubmenu></a></li>-->
                                </ul>

                            </li>
                            <li><a id="payroll" href='#'><span><p id="has-sub-align">Payroll</p></span></a><!-- untuk has-sub bisa di rubah sesuai gambarnya -->
                                <ul>
                                    <!--<li id='has-sub-menu'><pxsubmenu> Sub Menu Produck </pxsubmenu></li>-->
                                    <li id='has-sub-menu1'><a href='#'><px>01.List Of Salary Summary</px></a></li>
                                    <!--<li id='has-sub-menu'><a href='#'><pxsubmenu>About</pxsubmenu></a></li>-->
                                </ul>
                            </li>
                        </ul>
                    </div>
                </div>
                <!-- end -->
            <%} else if (url != null && url.length() > 0 && url.equalsIgnoreCase("canteen.jsp")) {%>
            <div id="menuheader"><h2>Canteen</h2></div>
            <div id="menu_kiri">
            <div id='cssmenu'>
                <ul>
                     <li><a id="excuse_application" href='#'><span><p id="has-sub-align">Excuse Application</p></span></a><!-- untuk has-sub bisa di rubah sesuai gambarnya -->
                        <ul>
                            <li id='has-sub-menu'><pxsubmenu> Summary </pxsubmenu></li>
                            <li id='has-sub-menu1'><a href='#'><px>01.Daily Meal Record</px></a></li>
                            <li id='has-sub-menu1'><a href='#'><px>02.Periodic Meal Record</px></a></li>
                            <li id='has-sub-menu1'><a href='#'><px>03.Meal Record</px></a></li>
                            <li id='has-sub-menu1'><a href='#'><px>04.Meal Record Departement</px></a></li>
                            <li id='has-sub-menu1'><a href='#'><px>05.Monthly Canteen Report</px></a></li>
                            <!--<li id='has-sub-menu'><a href='#'><pxsubmenu>About</pxsubmenu></a></li>-->
                        </ul>

                    </li>
                     
                     </ul>
            </div>
               
            </div>
            <div id="menu_kanan">
                
            <div id='cssmenu'>
                <ul>
                    <li><a id="Reports" href='#'><span><p id="has-sub-align">Reports</p></span></a><!-- untuk has-sub bisa di rubah sesuai gambarnya -->
                        <ul>
                            <li id='has-sub-menu'><pxsubmenu> Detail </pxsubmenu></li>
                        
                            <li id='has-sub-menu1'><a href='#'><px>01.Daily Report</px></a></li>
                                            <li id='has-sub-menu1'><a href='#'><px>02.Weekly Report</px></a></li>
                                           <li id='has-sub-menu1'><a href='#'><px>03.Monthly Report</px></a></li>
                                    
                        
                            <li id='has-sub-menu'><pxsubmenu> Summary </pxsubmenu></li>
                                        
                                            <li id='has-sub-menu1'><a href='#'><px>01.Daily Meal Record</px></a></li>
                                            <li id='has-sub-menu1'><a href='#'><px>02.Periodic Meal Record</px></a></li>
                                           <li id='has-sub-menu1'><a href='#'><px>03.Meal Report</px></a></li>
                                           <li id='has-sub-menu1'><a href='#'><px>04.Meal Report Departement</px></a></li>
                                           <li id='has-sub-menu1'><a href='#'><px>05.Monthly Canteen Report</px></a></li>
                                       
                                
                        </ul>

                    </li>
                    
                    
                </ul>
            </div>
                   
            </div>
            <%} else if (url != null && url.length() > 0 && url.equalsIgnoreCase("clinic.jsp")) {%>
            <div id="menuheader"><h2>Clinic</h2></div>
             <table style="position: absolute; left: 22px; top: 102px;">
                <tr>
                    <td>
            <div id='cssmenu'>
                <ul>
                     <li><a id="employee_family" href='#'><span><p id="has-sub-align">Employee & Family</p></span></a><!-- untuk has-sub bisa di rubah sesuai gambarnya -->

                    </li>
                     <li><a id="medical_record" href='#'><span><p id="has-sub-align">Medical Record</p></span></a><!-- untuk has-sub bisa di rubah sesuai gambarnya -->
                       
                    </li>
                     
                    <li><a id="employee_visit" href='#'><span><p id="has-sub-align">Employee Visit</p></span></a><!-- untuk has-sub bisa di rubah sesuai gambarnya -->

                    </li>
                     <li><a id="guest_handling" href='#'><span><p id="has-sub-align"> Guest Handling</p></span></a><!-- untuk has-sub bisa di rubah sesuai gambarnya -->

                    </li>
                    
                     </ul>
            </div>
                </td>
            </tr>
            </table>
            <table id="menu_kanan">
                <tr>
                    <td>
            <div id='cssmenu'>
                <ul>
                    <li><a id="Medicine" href='#'><span><p id="has-sub-align">Medicine</p></span></a><!-- untuk has-sub bisa di rubah sesuai gambarnya -->
                        <ul>
                            <!--<li id='has-sub-menu'><pxsubmenu> Sub Menu Produck </pxsubmenu></li>-->
                            <li id='has-sub-menu1'><a href='#'><px>01.List Of Medicine</px></a></li>
                            <li id='has-sub-menu1'><a href='#'><px>02.Medicine Consumption</px></a></li>
                           
                        </ul>

                    </li>
                     <li><a id="Disease" href='#'><span><p id="has-sub-align">Disease</p></span></a><!-- untuk has-sub bisa di rubah sesuai gambarnya -->
                        <ul>
                            <!--<li id='has-sub-menu'><pxsubmenu> Sub Menu Produck </pxsubmenu></li>-->
                            <li id='has-sub-menu1'><a href='#'><px>01.Type</px></a></li>
                            
                           
                        </ul>

                    </li>
                    <li><a id="Medical" href='#'><span><p id="has-sub-align">Medical</p></span></a><!-- untuk has-sub bisa di rubah sesuai gambarnya -->
                        <ul>
                            <!--<li id='has-sub-menu'><pxsubmenu> Sub Menu Produck </pxsubmenu></li>-->
                            <li id='has-sub-menu1'><a href='#'><px>01.Medical Level</px></a></li>
                            <li id='has-sub-menu1'><a href='#'><px>01.Medical Case</px></a></li>
                            <li id='has-sub-menu1'><a href='#'><px>01.Medical Budget</px></a></li>
                            <li id='has-sub-menu1'><a href='#'><px>01.Group</px></a></li>
                            <li id='has-sub-menu1'><a href='#'><px>01.Type</px></a></li>
                           
                        </ul>

                    </li>
                    
                </ul>
            </div>
                    </td>
                </tr>
            </table>
            <%} else if (url != null && url.length() > 0 && url.equalsIgnoreCase("locker.jsp")) {%>
            <div id="menuheader"><h2>Locker</h2></div>
           <table style="position: absolute; left: 22px; top: 102px;">
                <tr>
                    <td>
            <div id='cssmenu'>
                <ul>
                     <li><a id="Locker" href='#'><span><p id="has-sub-align">Locker</p></span></a><!-- untuk has-sub bisa di rubah sesuai gambarnya -->

                    </li>
                     <li><a id="locker_treatment" href='#'><span><p id="has-sub-align">Locker Treatment</p></span></a><!-- untuk has-sub bisa di rubah sesuai gambarnya -->

                    </li>
                     
                     </ul>
            </div>
                </td>
            </tr>
            </table>
            <%} else if (url != null && url.length() > 0 && url.equalsIgnoreCase("master_data.jsp")) {%>
            <div id="menuheader"><h2>Master Data</h2></div>
            <div id="menu_kiri">
               
            <div id='cssmenu'>
                <ul>
                    
                     <li><a id="company" href='#'><span><p id="has-sub-align">Company</p></span></a><!-- untuk has-sub bisa di rubah sesuai gambarnya -->
                        <ul>
                            <!--<li id='has-sub-menu'><pxsubmenu> Sub Menu Produck </pxsubmenu></li>-->
                            <li id='has-sub-menu1'><a href='#'><px>01.Company</px></a></li>
                            <li id='has-sub-menu1'><a href='#'><px>01.Division</px></a></li>
                            <li id='has-sub-menu1'><a href='#'><px>01.Departement</px></a></li>
                            <li id='has-sub-menu1'><a href='#'><px>01.Position</px></a></li>
                            <li id='has-sub-menu1'><a href='#'><px>01.Section</px></a></li>
                            <li id='has-sub-menu1'><a href='#'><px>01.Public Holiday</px></a></li>
                            <li id='has-sub-menu1'><a href='#'><px>01.Leave Target</px></a></li>
                           
                        </ul>

                    </li>
                     <li><a id="employee" href='#'><span><p id="has-sub-align">Employee</p></span></a><!-- untuk has-sub bisa di rubah sesuai gambarnya -->
                        <ul>
                            <!--<li id='has-sub-menu'><pxsubmenu> Sub Menu Produck </pxsubmenu></li>-->
                            <li id='has-sub-menu1'><a href='#'><px>01.Education</px></a></li>
                            <li id='has-sub-menu1'><a href='#'><px>01.Family Relation</px></a></li>
                            <li id='has-sub-menu1'><a href='#'><px>01.Warning</px></a></li>
                            <li id='has-sub-menu1'><a href='#'><px>01.Reprimand</px></a></li>
                            <li id='has-sub-menu1'><a href='#'><px>01.Level</px></a></li>
                            <li id='has-sub-menu1'><a href='#'><px>01.Category</px></a></li>
                            <li id='has-sub-menu1'><a href='#'><px>01.Religion</px></a></li>
                            <li id='has-sub-menu1'><a href='#'><px>01.Marital</px></a></li>
                            <li id='has-sub-menu1'><a href='#'><px>01.Race</px></a></li>
                            <li id='has-sub-menu1'><a href='#'><px>01.Language</px></a></li>
                            <li id='has-sub-menu1'><a href='#'><px>01.Image Assign</px></a></li>
                            <li id='has-sub-menu1'><a href='#'><px>01.Resigned Reason </px></a></li>
                            <li id='has-sub-menu1'><a href='#'><px>01.Award Type </px></a></li>
                            <li id='has-sub-menu1'><a href='#'><px>01.Abseance Reason </px></a></li>
                           
                        </ul>

                    </li>
                    <li><a id="Schedule" href='#'><span><p id="has-sub-align">Schedule</p></span></a><!-- untuk has-sub bisa di rubah sesuai gambarnya -->
                        <ul>
                            <!--<li id='has-sub-menu'><pxsubmenu> Sub Menu Produck </pxsubmenu></li>-->
                            
                            <li id='has-sub-menu1'><a href='#'><px>01.Periode</px></a></li>
                            <li id='has-sub-menu1'><a href='#'><px>01.Category</px></a></li>
                           <li id='has-sub-menu1'><a href='#'><px>01.Symbol</px></a></li>
                        </ul>

                    </li>
                    <li><a id="locker_data" href='#'><span><p id="has-sub-align">Locker Data</p></span></a><!-- untuk has-sub bisa di rubah sesuai gambarnya -->
                        <ul>
                            <!--<li id='has-sub-menu'><pxsubmenu> Sub Menu Produck </pxsubmenu></li>-->
                            
                            <li id='has-sub-menu1'><a href='#'><px>01.Locker Location</px></a></li>
                            <li id='has-sub-menu1'><a href='#'><px>01.Locker Condition</px></a></li>
                          
                        </ul>

                    </li>
                     <li><a id="Assessment" href='#'><span><p id="has-sub-align">Assessment</p></span></a><!-- untuk has-sub bisa di rubah sesuai gambarnya -->
                        <ul>
                            <!--<li id='has-sub-menu'><pxsubmenu> Sub Menu Produck </pxsubmenu></li>-->
                            
                            <li id='has-sub-menu1'><a href='#'><px>01.Group Rank</px></a></li>
                            <li id='has-sub-menu1'><a href='#'><px>01.Evaluation Criteria</px></a></li>
                          <li id='has-sub-menu1'><a href='#'><px>01.Form Creator</px></a></li>
                        </ul>

                    </li>
                     <li><a id="Recruitment" href='#'><span><p id="has-sub-align">Recruitment</p></span></a><!-- untuk has-sub bisa di rubah sesuai gambarnya -->
                        <ul>
                            <!--<li id='has-sub-menu'><pxsubmenu> Sub Menu Produck </pxsubmenu></li>-->
                            
                            <li id='has-sub-menu1'><a href='#'><px>01.General Questions</px></a></li>
                            <li id='has-sub-menu1'><a href='#'><px>01.Illness Type</px></a></li>
                          <li id='has-sub-menu1'><a href='#'><px>01.Interview Point</px></a></li>
                           <li id='has-sub-menu1'><a href='#'><px>01.Interviewer</px></a></li>
                            <li id='has-sub-menu1'><a href='#'><px>01.Interview Factor</px></a></li>
                             <li id='has-sub-menu1'><a href='#'><px>01.Orientation Group</px></a></li>
                             <li id='has-sub-menu1'><a href='#'><px>01.Orientation Activity</px></a></li>
                        </ul>

                    </li>
                     <li><a id="geo_area" href='#'><span><p id="has-sub-align">Geo Area</p></span></a><!-- untuk has-sub bisa di rubah sesuai gambarnya -->
                        <ul>
                            <!--<li id='has-sub-menu'><pxsubmenu> Sub Menu Produck </pxsubmenu></li>-->
                            
                            <li id='has-sub-menu1'><a href='#'><px>01.Country </px></a></li>
                            <li id='has-sub-menu1'><a href='#'><px>01.Province </px></a></li>
                          <li id='has-sub-menu1'><a href='#'><px>01.Regency </px></a></li>
                          <li id='has-sub-menu1'><a href='#'><px>01.Sub Regency </px></a></li>
                        </ul>

                    </li>
                     </ul>
            </div>
               
            </div>
            <%} else if (url != null && url.length() > 0 && url.equalsIgnoreCase("system.jsp")) {%>
            <div id="menuheader"><h2>System</h2></div>
           <table style="position: absolute; left: 22px; top: 102px;">
                <tr>
                    <td>
            <div id='cssmenu'>
                <ul>
                    
                     <li><a id="service_center" href='#'><span><p id="has-sub-align">Service Center</p></span></a></li>
                     <li><a id="manual_prosess" href='#'><span><p id="has-sub-align">Manual Process</p></span></a></li>
                     <li><a id="admin_query_setup" href='#'><span><p id="has-sub-align">Admin Query Setup</p></span></a></li>
                </ul>
            </div>
                </td>
            </tr>
            </table>
            <table id="menu_kanan">
                <tr>
                    <td>
            <div id='cssmenu'>
                <ul>
                    
                    <li><a id="user_management" href='#'><span><p id="has-sub-align">User Management</p></span></a>
                        <ul>
                            <!--<li id='has-sub-menu'><pxsubmenu> Sub Menu Produck </pxsubmenu></li>-->
                            <li id='has-sub-menu1'><a href='#'><px>01.User List</px></a></li>
                            <li id='has-sub-menu1'><a href='#'><px>02.Group Privilage</px></a></li>
                            <!--<li id='has-sub-menu'><pxsubmenu> Sub Menu Produck1 </pxsubmenu></li>-->
                            <li id='has-sub-menu1'><a href='#'><px>03.Privilage  </px></a></li>
                            <li id='has-sub-menu1'><a href='#'><px>04.Update Password</px></a></li>
                            <li id='has-sub-menu1'><a href='#'><px>05.User Compare</px></a></li>
                            <!--<li id='has-sub-menu'><a href='#'><pxsubmenu>About</pxsubmenu></a></li>-->
                        </ul>

                    </li>
                    <li><a id="system_management" href='#'><span><p id="has-sub-align">System Management</p></span></a><!-- untuk has-sub bisa di rubah sesuai gambarnya -->
                        <ul>
                            <!--<li id='has-sub-menu'><pxsubmenu> Sub Menu Produck </pxsubmenu></li>-->
                            <li id='has-sub-menu1'><a href='#'><px>01.System Properties</px></a></li>
                            <li id='has-sub-menu1'><a href='#'><px>02.Login History</px></a></li>
                            <!--<li id='has-sub-menu'><pxsubmenu> Sub Menu Produck1 </pxsubmenu></li>-->
                            <li id='has-sub-menu1'><a href='#'><px>03.System Log</px></a></li>
                           
                            <!--<li id='has-sub-menu'><a href='#'><pxsubmenu>About</pxsubmenu></a></li>-->
                        </ul>

                    </li>
                    <li><a id="time_keeping" href='#'><span><p id="has-sub-align">Time Keeping</p></span></a><!-- untuk has-sub bisa di rubah sesuai gambarnya -->
                        <ul>
                            <!--<li id='has-sub-menu'><pxsubmenu> Sub Menu Produck </pxsubmenu></li>-->
                            <li id='has-sub-menu1'><a href='#'><px>01.Service Manager</px></a></li>
                            

                            <!--<li id='has-sub-menu'><a href='#'><pxsubmenu>About</pxsubmenu></a></li>-->
                        </ul>

                    </li>
                    
                </ul>
            </div>
                    </td>
                </tr>
            </table>
            <%} else if (url != null && url.length() > 0 && url.equalsIgnoreCase("payroll_setup.jsp")) {%>
            <div id="menuheader"><h2>Payroll Setup</h2></div>
            <table style="position: absolute; left: 22px; top: 102px;">
                <tr>
                    <td>
            <div id='cssmenu'>
                <ul>
                    <li><a id="General" href='#'><span><p id="has-sub-align">General</p></span></a></li>
                    <li><a id="payroll_periode" href='#'><span><p id="has-sub-align">Payroll Periode</p></span></a></li>
                     <li><a id="bank_list" href='#'><span><p id="has-sub-align">Bank List</p></span></a></li>
                    <li><a id="pay_slip_group" href='#'><span><p id="has-sub-align">Pay Slip Group</p></span></a></li>
                    <li><a id="salary_component" href='#'><span><p id="has-sub-align">Salary Component</p></span></a></li>
                   
                  
                </ul>
            </div>
                </td>
            </tr>
            </table>
            <table id="menu_kanan">
                <tr>
                    <td>
            <div id='cssmenu'>
                <ul>
                    <li><a id="salary_level" href='#'><span><p id="has-sub-align">Salary Level</p></span></a></li>
                    <li><a id="employee_setup" href='#'><span><p id="has-sub-align">Employee Setup</p></span></a></li>
                    <li><a id="Currency" href='#'><span><p id="has-sub-align">Currency</p></span></a></li>
                    <li><a id="Currency_rate" href='#'><span><p id="has-sub-align">Currency Rate</p></span></a></li>
                </ul>
            </div>
                    </td>
                </tr>
            </table>
            <%} else if (url != null && url.length() > 0 && url.equalsIgnoreCase("overtime.jsp")) {%>
            <div id="menuheader"><h2>Overtime</h2></div>
           <table style="position: absolute; left: 22px; top: 102px;">
                <tr>
                    <td>
            <div id='cssmenu'>
                <ul>
                    <li><a id="overtime_form" href='#'><span><p id="has-sub-align">Overtime Form</p></span></a></li>
                    <li><a id="overtime_report" href='#'><span><p id="has-sub-align">Overtime Report & Process</p></span></a></li>
                     <li><a id="overtime_index" href='#'><span><p id="has-sub-align">Overtime Index</p></span></a></li>
                    <li><a id="overtime_summary" href='#'><span><p id="has-sub-align">Overtime  Summary</p></span></a></li>
                    
                   
                  
                </ul>
            </div>
                </td>
            </tr>
            </table>
            <%} else if (url != null && url.length() > 0 && url.equalsIgnoreCase("payroll_process.jsp")) {%>
            <div id="menuheader"><h2>Payroll Process</h2></div>
             <table style="position: absolute; left: 22px; top: 102px;">
                <tr>
                    <td>
            <div id='cssmenu'>
                <ul>
                    <li><a id="prepare_data" href='#'><span><p id="has-sub-align">Prepare Data</p></span></a></li>
                    <li><a id="payroll_input" href='#'><span><p id="has-sub-align">Payroll Input</p></span></a></li>
                     <li><a id="payroll_process" href='#'><span><p id="has-sub-align">Payroll Process</p></span></a></li>
                    <li><a id="payslip_printing" href='#'><span><p id="has-sub-align">Payslip  Printing</p></span></a></li>
                    
                   
                  
                </ul>
            </div>
                </td>
            </tr>
            </table>
            <%} else if (url != null && url.length() > 0 && url.equalsIgnoreCase("help.jsp")) {%>
          
            
            <%}%>
        </div>

