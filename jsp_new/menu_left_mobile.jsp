<%-- 
    Document   : menu_left_mobile
    Created on : Aug 8, 2014, 2:06:00 PM
    Author     : dimata005
--%>

<aside class="left-side sidebar-offcanvas">
<!-- sidebar: style can be found in sidebar.less -->
    <section class="sidebar">
        <!-- Sidebar user panel -->
        <%--
        <div class="user-panel">
            <div class="pull-left image">
                <img src="img/avatar3.png" class="img-circle" alt="User Image" />
            </div>
            <div class="pull-left info">
                <p>Hello, Jane</p>

                <a href="#"><i class="fa fa-circle text-success"></i> Online</a>
            </div>
        </div>
        --%>
        <!-- search form -->
        <form action="#" method="get" class="sidebar-form">
            <div class="input-group">
                <input type="text" name="q" class="form-control" placeholder="Search..."/>
                <span class="input-group-btn">
                    <button type='submit' name='seach' id='search-btn' class="btn btn-flat"><i class="fa fa-search"></i></button>
                </span>
            </div>
        </form>
        <!-- /.search form -->
        <!-- sidebar menu: : style can be found in sidebar.less -->
        <ul class="sidebar-menu">
            <li class="active">
                <a href="<%=approot%>/homepage_menumobile.jsp?menu=home">
                    <i class="fa fa-dashboard"></i> <span>Home</span>
                </a>
            </li>
            <%if(privPurchaseRequestOutlet==true){%>
            <li class="treeview">
                <a href="#">
                    <i class="fa fa-bar-chart-o"></i>
                    <span>Store Request</span>
                    <i class="fa fa-angle-left pull-right"></i>
                </a>
                 <ul class="treeview-menu">
                    <li><a href="<%=approot%>/purchasing/material/pom/storerequest_edit.jsp?command=2&approval_command=4&add_type=0"><i class="fa fa-angle-double-right"></i>Add New</a></li>
                    <li><a href="<%=approot%>/purchasing/material/pom/srcstorerequest.jsp?command=1"><i class="fa fa-angle-double-right"></i>Search</a></li>
                </ul>
            </li>
            <li class="treeview">
                <a href="#">
                    <i class="fa fa-bar-chart-o"></i>
                    <span>Report Store Request</span>
                    <i class="fa fa-angle-left pull-right"></i>
                </a>
                 <ul class="treeview-menu">
                    <li><a href="<%=approot%>/arap/payable/m_payable_search.jsp"><i class="fa fa-angle-double-right"></i>Rekap</a></li>
                    <li><a href="<%=approot%>/purchasing/material/pom/srcstorerequest.jsp?command=1"><i class="fa fa-angle-double-right"></i>Rekap Detail</a></li>
                </ul>
            </li>
            <%}%>
           <%if(privApprovalKasirPayment){%>
                <li>
                    <a href="<%=approot%>/cashieronline/m_open_shift.jsp?typeListCashier=1">
                        <i class="fa fa-th"></i> <span>Issue Store Request</span>
                    </a>
                </li>
            <%}%>
            <%if(privApprovalKasirPayment){%>
                <li>
                    <a href="<%=approot%>/cashieronline/m_open_shift.jsp?typeListCashier=7">
                        <i class="fa fa-th"></i> <span>List Issue Store Request</span>
                    </a>
                </li>
            <%}%>
            <%if(privApprovalKasirPayment){%>
                <li>
                    <a href="<%=approot%>/warehouse/material/dispatch/src_delivery_order_sales_m.jsp">
                        <i class="fa fa-th"></i> <span>Delivery Order</span>
                    </a>
                </li>
            <%}%>
            <%if(privPurchaseRequestOutlet==true){%>
                <li>
                    <a href="<%=approot%>/warehouse/material/receive/m_src_receive_material.jsp">
                        <i class="fa fa-th"></i> <span>Receive Store Request </span>
                    </a>
                </li>
            <%}%>
            <li>
                <a href="<%=approot%>/warehouse/material/stock/m_mat_opname_src.jsp">
                    <i class="fa fa-th"></i> <span>Opname</span>
                </a>
            </li>
            <li class="active">
                <a href="<%=approot%>/logout.jsp">
                    <i class="fa fa-dashboard"></i> <span>Log out</span>
                </a>
            </li>
        </ul>
    </section>
<!-- /.sidebar -->
</aside>
