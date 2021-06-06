<!-- header logo: style can be found in header.less -->
<header class="header">
    <a href="#" class="logo">
        <!-- Add the class icon to your logo image or logo icon to add the margining -->
        Prochain
    </a>
    <!-- Header Navbar: style can be found in header.less -->
    <nav class="navbar navbar-static-top" role="navigation">
        <!-- Sidebar toggle button-->
        <a href="#" class="navbar-btn sidebar-toggle" data-toggle="offcanvas" role="button">
            <span class="sr-only">Toggle navigation</span>
            <span class="icon-bar"></span>
            <span class="icon-bar"></span>
            <span class="icon-bar"></span>
        </a>
        <div class="navbar-right">
            <ul class="nav navbar-nav">
                <li class="dropdown user user-menu">
                    <a href="#" class="dropdown-toggle" data-toggle="dropdown">
                        <i class="glyphicon glyphicon-user"></i>
                        <span><%="Login as \""+userName+"\""%></span>
                    </a>
                        <%--
                    <ul class="dropdown-menu">
                        <li class="user-footer">
                            <div class="pull-right">
                                <a href="<%=approot%>/logout.jsp" class="btn btn-default btn-flat">Sign out</a>
                            </div>
                        </li>
                    </ul>
                        --%>
                </li>
            </ul>
        </div>
    </nav>
</header>