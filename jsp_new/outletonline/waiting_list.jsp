<%-- 
    Document   : waiting_list
    Created on : Jan 21, 2015, 3:32:19 PM
    Author     : dimata005
--%>

<%@page import="java.text.DateFormat"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="com.dimata.gui.jsp.ControlDate"%>
<%@page import="com.dimata.posbo.entity.masterdata.WaitingList"%>
<%@page import="com.dimata.posbo.entity.masterdata.PstWaitingList"%>
<%@page import="com.dimata.posbo.form.masterdata.CtrlWaitingList"%>
<%@page import="com.dimata.posbo.form.masterdata.FrmWaitingList"%>
<%@page import="com.dimata.util.Command"%>
<%@page import="com.dimata.posbo.entity.masterdata.TableRoom"%>
<%@page import="com.dimata.posbo.entity.masterdata.Room"%>
<%@page import="com.dimata.pos.entity.billing.BillMain"%>
<%@page import="com.dimata.pos.entity.billing.PstBillMain"%>
<%@page import="java.util.Vector"%>
<%@page import="com.dimata.qdep.form.FRMQueryString"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<%@ include file = "../main/javainit.jsp" %>
<%

    int iCommand = FRMQueryString.requestCommand(request);
    String nameGuest = FRMQueryString.requestString(request, FrmWaitingList.fieldNames[FrmWaitingList.FRM_CUSTOMER_NAME]);
    String phoneNumber = FRMQueryString.requestString(request, FrmWaitingList.fieldNames[FrmWaitingList.FRM_NO_TLP]);
    long oidCustomer = FRMQueryString.requestLong(request, FrmWaitingList.fieldNames[FrmWaitingList.FRM_CUSTOMER_WAITING_ID]);

    CtrlWaitingList ctrlWaitingList = new CtrlWaitingList(request);
    FrmWaitingList frmWaitingList = ctrlWaitingList.getForm();

    oidCustomer = ctrlWaitingList.action(iCommand, oidCustomer);
    String where = PstWaitingList.fieldNames[PstWaitingList.FLD_STATUS] + "='0'";
    Vector listMemberReg = PstWaitingList.list(0, 0, where, "");

%>
<html xmlns="http://www.w3.org/1999/xhtml">
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
            <meta name="viewport" content="width=device-width, initial-scale=1.0">
                <title>Taking Order - Queens Bali</title>
                <%--
                <link href="../styles/takingorder/css/bootstrap.css" rel="stylesheet" type="text/css">
                <link href="../styles/takingorder/css/bootstrap_002.css" rel="stylesheet" type="text/css">
                <!-- jQuery -->
                <script src="../styles/takingorder/js/jquery.js"></script>
                <!-- Tablesorter: required -->
                <link href="../styles/takingorder/css/theme.css" rel="stylesheet">
                <script src="../styles/takingorder/js/jquery_002.js"></script>
                <script src="../styles/takingorder/js/jquery_003.js"></script>
                <!-- Tablesorter: pager -->
                <link rel="stylesheet" href="../styles/takingorder/js/jquery.css">
                <script src="../styles/takingorder/js/widget-pager.js"></script>
                --%>
                <link href="../styles/takingorder/css/bootstrap.css" rel="stylesheet" type="text/css">
                    <link href="../styles/takingorder/css/bootstrap_002.css" rel="stylesheet" type="text/css">
                        <!-- jQuery -->
                        <script src="../styles/takingorder/js/jquery.js"></script>
                        <script type="text/javascript" src="../styles/jquery.min.js"></script>
                        <!-- Tablesorter: required -->
                        <link href="../styles/takingorder/css/theme.css" rel="stylesheet">
                            <script src="../styles/takingorder/js/jquery_002.js"></script>
                            <script src="../styles/takingorder/js/jquery_003.js"></script>
                            <!-- Tablesorter: pager -->
                            <link rel="stylesheet" href="../styles/takingorder/js/jquery.css">
                                <script src="../styles/takingorder/js/widget-pager.js"></script>
                                <script type="text/javascript" src="../styles/takingorder/js/bootstrap.min.js"></script>
                                </head>
                                <body>
                                    <div style="background:#f0f0f0; margin-bottom:10px; padding:5px;">
                                        <table border="0" cellpadding="0" cellspacing="0" width="100%">
                                            <tbody><tr>
                                                    <td style="text-align:left;">
                                                        <i><strong> </strong> <span id="cartorder"></span></i>
                                                    </td>
                                                    <td style="text-align:right;">
                                                        <i></i>
                                                    </td>
                                                </tr>
                                            </tbody></table>
                                    </div>
                                    <div class="container">
                                        <center>
                                            <a href="#"><img src="../styles/takingorder/img/queens-head.png" border="none" width="250"></a>
                                        </center>
                                        <div style="margin-top:20px;">
                                            <a href="<%=approot%>/outletonline/mainmenumobile.jsp">Back Menu</a>
                                            <hr style="margin-top:10px;">
                                                <script id="js">
                                                    $(function() {
                                                        // hide child rows
                                                        $('.tablesorter-childRow td').hide();
                                                        var $table = $('.tablesorter-dropbox')
                                                        .tablesorter({
                                                            headerTemplate: '{content}{icon}', // dropbox theme doesn't like a space between the content & icon
                                                            sortList : [ [0,0] ],
                                                            showProcessing: true,
                                                            cssChildRow: "tablesorter-childRow",
                                                            widgets    : ["pager","zebra","filter"],
                                                            widgetOptions: {
                                                                filter_columnFilters: false,
                                                                filter_saveFilters : true,
                                                            }
                                                        })
                                                        $('.tablesorter-dropbox').delegate('.toggle', 'click' ,function(){
                                                            $(this).closest('tr').nextUntil('tr.tablesorter-hasChildRow').find('td').toggle();
                                                            return false;
                                                        });
		
                                                        $.tablesorter.filter.bindSearch( $table, $('.search') );
                                                        $('select').change(function(){
                                                            // modify the search input data-column value (swap "0" or "all in this demo)
                                                            $('.selectable').attr( 'data-column', $(this).val() );
                                                            // update external search inputs
                                                            $.tablesorter.filter.bindSearch( $table, $('.search'), false );
                                                        });
                                                    });
        
                                                    function cmdAddGuest(){
                                                        document.frmsrcsalesorder.command.value="<%=Command.NONE%>";
                                                        document.frmsrcsalesorder.action="waiting_list.jsp";
                                                        document.frmsrcsalesorder.submit();
                                                    }
        
                                                    function cmdSave(){
                                                        //alert("asjalsja");
                                                        var name = document.frmsrcsalesorder.<%=frmWaitingList.fieldNames[FrmWaitingList.FRM_CUSTOMER_NAME]%>.value                                                ;
                                                        if(name!=""){
                                                            document.frmsrcsalesorder.command.value="<%=Command.SAVE%>";
                                                            document.frmsrcsalesorder.<%=frmWaitingList.fieldNames[FrmWaitingList.FRM_CUSTOMER_WAITING_ID]%>.value=0;
                                                            document.frmsrcsalesorder.action="waiting_list.jsp";
                                                            document.frmsrcsalesorder.submit();
                                                        }else{
                                                            alert("Please Input Guest Name");
                                                        }
                
                                                    }
        
                                                    function cmdUpdate(oid){
                                                        var msg;
                                                        msg= "Yakin Update ?" ;
                                                        var agree=confirm(msg);
                                                        if (agree){
                                                            document.frmsrcsalesorder.command.value="<%=Command.UPDATE%>";
                                                            document.frmsrcsalesorder.<%=frmWaitingList.fieldNames[FrmWaitingList.FRM_CUSTOMER_WAITING_ID]%>.value=oid;
                                                            document.frmsrcsalesorder.action="waiting_list.jsp";
                                                            document.frmsrcsalesorder.submit();
                                                        }      
                                                    }
        
                                                </script>
                                                <form name="frmsrcsalesorder" method ="post" action="" role="form">
                                                    <input type="hidden" name="command" value="<%=iCommand%>">
                                                        <input type="hidden" name="<%=frmWaitingList.fieldNames[FrmWaitingList.FRM_CUSTOMER_WAITING_ID]%>" value="<%=oidCustomer%>">    
                                                            <button class="btn btn-primary btn-md btn-block" type="button" data-toggle="modal" data-target="#myModal">Add Waiting List</button>
                                                            <div class="guest" style="margin:10px 0px;">
                                                                <div class="form-group">
                                                                    <div class=row>
                                                                        <div class="col-md-12">
                                                                            <input data-lastsearchtime="1416898435043" class="form-control search selectablex" placeholder="Search keyword.." data-column="all" type="search" name="nameMember">
                                                                        </div>
                                                                    </div>
                                                                </div> 
                                                                <div class="form-group">
                                                                    <div class=row>
                                                                        <div class="col-md-12">
                                                                            <table role="grid" class="tablesorter-dropbox tablesorter hasFilters">
                                                                                <thead>
                                                                                    <tr role="row" class="tablesorter-headerRow">
                                                                                        <th aria-label="#: Ascending sort applied, activate to apply a descending sort" aria-sort="ascending" style="-moz-user-select: none;" unselectable="on" aria-disabled="false" role="columnheader" scope="col" tabindex="0" class="tablesorter-header tablesorter-headerAsc" data-column="0" width="10%"><div class="tablesorter-header-inner">#<i class="tablesorter-icon"></i></div></th>
                                                                                        <th aria-label="Guest Name: No sort applied, activate to apply an ascending sort" aria-sort="none" style="-moz-user-select: none;" unselectable="on" aria-disabled="false" role="columnheader" scope="col" tabindex="0" class="tablesorter-header tablesorter-headerUnSorted" data-column="1" width="15%"><div class="tablesorter-header-inner">Staff<i class="tablesorter-icon"></i></div></th>
                                                                                        <th aria-label="Guest Name: No sort applied, activate to apply an ascending sort" aria-sort="none" style="-moz-user-select: none;" unselectable="on" aria-disabled="false" role="columnheader" scope="col" tabindex="0" class="tablesorter-header tablesorter-headerUnSorted" data-column="2" width="15%"><div class="tablesorter-header-inner">Name<i class="tablesorter-icon"></i></div></th>
                                                                                        <th aria-label="Guest Name: No sort applied, activate to apply an ascending sort" aria-sort="none" style="-moz-user-select: none;" unselectable="on" aria-disabled="false" role="columnheader" scope="col" tabindex="0" class="tablesorter-header tablesorter-headerUnSorted" data-column="2" width="10%"><div class="tablesorter-header-inner">Pax<i class="tablesorter-icon"></i></div></th>
                                                                                        <th aria-label="Guest Name: No sort applied, activate to apply an ascending sort" aria-sort="none" style="-moz-user-select: none;" unselectable="on" aria-disabled="false" role="columnheader" scope="col" tabindex="0" class="tablesorter-header tablesorter-headerUnSorted" data-column="3" width="20%"><div class="tablesorter-header-inner">Handphone<i class="tablesorter-icon"></i></div></th>
                                                                                        <th aria-label="Guest Name: No sort applied, activate to apply an ascending sort" aria-sort="none" style="-moz-user-select: none;" unselectable="on" aria-disabled="false" role="columnheader" scope="col" tabindex="0" class="tablesorter-header tablesorter-headerUnSorted" data-column="4" width="15%"><div class="tablesorter-header-inner">Start Time<i class="tablesorter-icon"></i></div></th>
                                                                                        <th aria-label="&nbsp;: No sort applied, activate to apply an ascending sort" aria-sort="none" style="-moz-user-select: none;" unselectable="on" aria-disabled="false" role="columnheader" scope="col" tabindex="0" class="tablesorter-header tablesorter-headerUnSorted" data-column="6" width="15%"><div class="tablesorter-header-inner">&nbsp;<i class="tablesorter-icon"></i></div></th>
                                                                                    </tr>
                                                                                </thead>
                                                                                <tfoot>
                                                                                    <tr>
                                                                                        <th class="tablesorter-headerAsc" data-column="0" width="10%">#</th>
                                                                                        <th data-column="1" width="15%">Staff</th>
                                                                                        <th data-column="2" width="15%">Name</th>
                                                                                        <th data-column="2" width="10%">Pax</th>
                                                                                        <th data-column="3" width="20%">Handphone</th>
                                                                                        <th data-column="4" width="15%">Start Time</th>
                                                                                        <th data-column="6" width="15%">&nbsp;</th>
                                                                                    </tr>
                                                                                </tfoot>
                                                                                <tbody aria-relevant="all" aria-live="polite">
                                                                                    <%
                                                                                        int start = 0;
                                                                                        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                                                                                        for (int i = 0; i < listMemberReg.size(); i++) {
                                                                                            WaitingList waitingList = (WaitingList) listMemberReg.get(i);
                                                                                            start = start + 1;
                                                                                            String name = "";
                                                                                            name = waitingList.getCustomerName();
                                                                                            Date dateStart = waitingList.getStartTime();
                                                                                            Date dateEnd = waitingList.getEndTime();
                                                                                    %>
                                                                                    <tr style="" class="tablesorter-hasChildRow odd">
                                                                                        <td style="text-align:center;"><a href="#" class="toggle"><%=start%></a></td>
                                                                                        <td align="left"><a href="#"><%=waitingList.getStaff()%></a></td>
                                                                                        <td align="left"><a href="#"><%=name%></a></td>
                                                                                        <td align="left"><a href="#"><%=waitingList.getPax()%></a></td>
                                                                                        <td align="left"><a href="#"><%=waitingList.getNoTlp()%></a></td>
                                                                                        <td align="left"><a href="#"><%=dateFormat.format(dateStart)%></a></td>
                                                                                        <td align="left"><a href="javascript:cmdUpdate('<%=waitingList.getOID()%>')">Update Reserved</a></td>
                                                                                    </tr>
                                                                                    <%
                                                                                        }
                                                                                    %>
                                                                                </tbody>
                                                                            </table>  
                                                                            <button class="btn btn-primary btn-md btn-block" type="button" onclick="">&nbsp;</button>
                                                                            <div class="pager">
                                                                                <img src="../styles/takingorder/img/first.png" class="first" alt="First" />
                                                                                <img src="../styles/takingorder/img/prev.png" class="prev" alt="Prev" />
                                                                                <span class="pagedisplay"></span> <!-- this can be any element, including an input -->
                                                                                <img src="../styles/takingorder/img/next.png" class="next" alt="Next" />
                                                                                <img src="../styles/takingorder/img/last.png" class="last" alt="Last" />
                                                                                <select class="pagesize" title="Select page size">
                                                                                    <option value="10">10</option>
                                                                                    <option value="20">20</option>
                                                                                    <option value="30">30</option>
                                                                                    <option value="40">40</option>
                                                                                    <option value="50">50</option>
                                                                                    <option value="60">60</option>
                                                                                    <option value="70">70</option>
                                                                                    <option value="80">80</option>
                                                                                    <option value="90">90</option>
                                                                                    <option value="100">100</option>
                                                                                </select>
                                                                                <select class="gotoPage" title="Select page number"></select>
                                                                            </div> 
                                                                        </div>
                                                                    </div> 
                                                                </div>
                                                            </div>

                                                            <div class="modal fade" id="myModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
                                                                <div class="modal-dialog">
                                                                    <div class="modal-content">
                                                                        <div class="modal-header">
                                                                            <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                                                                            <h4 class="modal-title" id="myModalLabel">Member</h4>
                                                                        </div>
                                                                        <div class="modal-body">
                                                                            <%-- body--%>
                                                                            <div class="guest" style="margin:10px 0px;">
                                                                                <div class="form-group">
                                                                                    <div class="col-md-12" style="margin-bottom:10px;">
                                                                                        <div class="form-group">
                                                                                            <div class=row>
                                                                                                <div class="col-md-12">
                                                                                                    <input name="<%=frmWaitingList.fieldNames[FrmWaitingList.FRM_STAFF]%>" id="staff" type="text" class="form-control" placeholder="Staff Name.." required>
                                                                                                </div>
                                                                                            </div>
                                                                                        </div>
                                                                                        <div class="form-group">
                                                                                            <div class=row>
                                                                                                <div class="col-md-12">
                                                                                                    <input name="<%=frmWaitingList.fieldNames[FrmWaitingList.FRM_CUSTOMER_NAME]%>" id="guestname" type="text" class="form-control" placeholder="Insert Guest Name.." required>
                                                                                                </div>
                                                                                            </div>
                                                                                        </div>
                                                                                        <div class="form-group">
                                                                                            <div class=row>
                                                                                                <div class="col-md-12">
                                                                                                    <input name="<%=frmWaitingList.fieldNames[FrmWaitingList.FRM_PAX]%>" id="guestname" type="text" class="form-control" placeholder="Insert Pax.." required>
                                                                                                </div>
                                                                                            </div>
                                                                                        </div>        
                                                                                        <div class="form-group">
                                                                                            <div class=row>
                                                                                                <div class="col-md-12">
                                                                                                    <input name="<%=frmWaitingList.fieldNames[FrmWaitingList.FRM_NO_TLP]%>" id="mobilenumber" type="text" class="form-control" placeholder="Insert Phone Number." required>
                                                                                                </div>
                                                                                            </div>
                                                                                        </div> 
                                                                                        <div class="form-group">
                                                                                            <div class=row>
                                                                                                <div class="col-md-12">Start Waiting &nbsp;
                                                                                                    <%=ControlDate.drawDateWithStyle(FrmWaitingList.fieldNames[FrmWaitingList.FRM_START_TIME], new Date(), 1, -5, "formElemen", "")%>
                                                                                                    <%=ControlDate.drawTimeSec(FrmWaitingList.fieldNames[FrmWaitingList.FRM_START_TIME], new Date(), "formElemen")%>
                                                                                                </div>
                                                                                            </div>
                                                                                        </div>
                                                                                        <%--
                                                                           <div class="form-group">
                                                                               <div class=row>
                                                                                   <div class="col-md-12">End Waiting&nbsp;&nbsp;&nbsp;
                                                                                        <%=ControlDate.drawDateWithStyle(FrmWaitingList.fieldNames[FrmWaitingList.FRM_END_TIME],new Date(), 1, -5, "formElemen", "")%>
                                                                                        <%=ControlDate.drawTimeSec(FrmWaitingList.fieldNames[FrmWaitingList.FRM_END_TIME], new Date(),"formElemen")%>
                                                                                   </div>
                                                                               </div>
                                                                           </div>   
                                                                                        --%>
                                                                                        <div class="form-group">
                                                                                            <div class=row>
                                                                                                <div class="col-md-12">
                                                                                                    <button class="btn btn-primary btn-md btn-block" onclick="javascript:cmdSave()" type="button" >Save Guest</button>
                                                                                                </div>
                                                                                            </div>
                                                                                        </div>
                                                                                    </div>
                                                                                </div>
                                                                            </div> 
                                                                        </div>
                                                                        <div class="modal-footer">
                                                                            <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
                                                                        </div>
                                                                    </div>
                                                                </div>
                                                            </div>              
                                                            </form>
                                                            </div>
                                                            <hr>
                                                                <div style="margin:20px 0px; text-align:center;">
                                                                </div>
                                                                </div>

                                                                </body></html>
