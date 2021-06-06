<html>
    <head>
        <title>test Bootstrap</title>
    </head>
    <body>
        <link type="text/css" rel="stylesheet" href="../../../styles/bootstrap3.1/css/bootstrap.css">
        <script type="text/javascript" src="../../../styles/jquery.min.js"></script>
        <script type="text/javascript" src="../../../styles/bootstrap3.1/js/bootstrap.min.js"></script>
        <script type="text/javascript">
            $(document).ready(function(){
                $('#click').click(function(){
                    $('#modalLoading').modal('show');
                });
            });
        </script>
        <button type="button" class="button btn-default" id="click">test</button>
<div id="modalLoading" class="modal fade" tabindex="-1">
    <div class="modal-dialog modal-lg">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                <h4 class="modal-title" id="modal-title">Please Wait...</h4>
            </div>
            <div class="modal-body" id="modal-body">
                <div class="col-md-12"><center><img src="../imgstyle/loading.gif"></center></div>
            </div>
            <div class="modal-footer">

            </div>
        </div>
    </div>
</div>
    </body>
</html>