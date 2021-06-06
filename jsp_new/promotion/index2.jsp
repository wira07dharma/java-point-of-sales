<%--
    Document   : marketing-promotion
    Created on : Dec 6, 2016, 10:23:25 AM
    Author     : Dewa
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@include file="../main/javainit.jsp" %>
<!DOCTYPE html>
<html>
    <head>
        <!--link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css">
        <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.1.1/jquery.min.js"></script>
        <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script-->

        <%@include file="../templates/plugins-component.jsp" %>

        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Marketing Promotion</title>
        <style>
            .modal-large {
                width: 99%;
            }

            th {
                background-color: lightgrey;
            }

            .panel-heading:hover {
                background-color: #dfdfdf !important;
            }

            .table {
                font-size: 12px;
            }

            .modal-body {
                padding-bottom: 0px;
            }

        </style>
        <script>
            $(document).ready(function () {
                $('a[data-toggle="collapse"]').on('click', function () {

                    var objectID = $(this).attr('href');

                    if ($(objectID).hasClass('in'))
                    {
                        $(objectID).collapse('hide');
                    }
                    else
                    {
                        $(objectID).collapse('show');
                    }
                });

                $('#expandAll').on('click', function () {
                    $('a[data-toggle="collapse"]').each(function () {
                        var objectID = $(this).attr('href');
                        if ($(objectID).hasClass('in') === false)
                        {
                            $(objectID).collapse('show');
                        }
                    });
                });

                $('#collapseAll').on('click', function () {
                    $('a[data-toggle="collapse"]').each(function () {
                        var objectID = $(this).attr('href');
                        $(objectID).collapse('hide');
                    });
                });

                $('#addsubject').on('hidden.bs.modal', function (e) {
                    if ($('#showmarketingpromotionmodal').hasClass('in')) {
                        $('body').addClass('modal-open');
                    }
                });

                $('#addobject').on('hidden.bs.modal', function (e) {
                    if ($('#showmarketingpromotionmodal').hasClass('in')) {
                        $('body').addClass('modal-open');
                    }
                });

            });
        </script>
    </head>
    <body>
        <div class="container">
            <h4>Marketing Promotion</h4>
            <button type="button" class="btn btn-primary" data-toggle="modal" data-target="#showmarketingpromotionmodal" data-backdrop="static">New Promotion</button>
            
            <div class="modal fade" id="showmarketingpromotionmodal" role="dialog">
                <div class="modal-dialog modal-large">

                    <div class="modal-content">

                        <div class="modal-header">
                            <button type="button" class="close" data-dismiss="modal">&times;</button>
                            <h4 class="modal-title">Add New Marketing Promotion</h4>
                        </div>

                        <div class="modal-body">

                            <form>
                                <div class="row">

                                    <div class="col-sm-2">
                                        <div class="form-group form-group-sm">
                                            <label for="purpose">Purposed on : </label>
                                            <input type="text" class="form-control input-sm" id="purpose" placeholder="Purpose date">
                                        </div>
                                    </div>
                                    <div class="col-sm-2">
                                        <div class="form-group form-group-sm">
                                            <label for="objectives">Objectives : </label>
                                            <input type="text" class="form-control input-sm" id="objectives" placeholder="Objectives">
                                        </div>
                                    </div>
                                    <div class="col-sm-2">
                                        <div class="form-group form-group-sm">
                                            <label for="event">Event : </label>
                                            <input type="text" class="form-control input-sm" id="event" placeholder="Event">
                                        </div>
                                    </div>
                                    <div class="col-sm-2">
                                        <div class="form-group form-group-sm">
                                            <label for="recurring">Recurring : </label>
                                            <input type="text" class="form-control input-sm" id="recurring" placeholder="Recurring">
                                        </div>
                                    </div>
                                    <div class="col-sm-2">
                                        <div class="form-group form-group-sm">
                                            <label for="start">Start on : </label>
                                            <input type="text" class="form-control input-sm" id="start" placeholder="Start date">
                                        </div>
                                    </div>
                                    <div class="col-sm-2">
                                        <div class="form-group form-group-sm">
                                            <label for="end">End on : </label>
                                            <input type="text" class="form-control input-sm" id="end" placeholder="End date">
                                        </div>
                                    </div>
                                    
                                </div>
                            </form>

                            <form>
                                <div class="row">

                                    <div class="col-sm-3">
                                        <div class="form-group form-group-sm">
                                            <label for="duration">Duration : </label>
                                            <input readonly="readonly" type="text" class="form-control input-sm" id="duration" placeholder="Duration">
                                        </div>
                                    </div>
                                    <div class="col-sm-3">
                                        <div class="form-group form-group-sm">
                                            <label for="location">Location : </label>
                                            <input type="text" class="form-control input-sm" id="location" placeholder="Location">
                                        </div>
                                    </div>
                                    <div class="col-sm-3">
                                        <div class="form-group form-group-sm">
                                            <label for="member">Member Type : </label>
                                            <input type="text" class="form-control input-sm" id="member" placeholder="Member type">
                                        </div>
                                    </div>
                                    <div class="col-sm-3">
                                        <div class="form-group form-group-sm">
                                            <label for="price">Price Type : </label>
                                            <input type="text" class="form-control input-sm" id="price" placeholder="Price type">
                                        </div>
                                    </div>

                                    <div class="col-sm-12">
                                        <div class="form-group form-group-sm">
                                            <button type="button" class="btn btn-primary btn-sm">Save Promo</button>
                                            <button type="button" class="btn btn-primary btn-sm" data-toggle="collapse" data-target="#promo">Add Promo Detail</button>
                                            <div class="pull-right">
                                                <a id="expandAll" href="#" class="btn btn-success btn-sm" role="button">Show All</a>
                                                <a id="collapseAll" href="#" class="btn btn-success btn-sm" role="button">Hide All</a>
                                            </div>
                                        </div>
                                    </div>

                                </div>
                            </form>

                            <div id="promo" class="collapse">
                                <div class="panel-group">

                                    <div class="panel panel-default">
                                        <div class="panel-heading" data-toggle="collapse" data-target="#collapse1">
                                            <h4 class="panel-title">
                                                <a data-toggle="collapse" href="#collapse1">Promo 1</a>
                                            </h4>
                                        </div>
                                        <div id="collapse1" class="panel-collapse collapse">

                                            <div class="panel-body">

                                                <form>
                                                    <div class="row">

                                                        <div class="col-sm-4">
                                                            <div class="form-group form-group-sm form-inline">
                                                                <label for="start">Promotion Type : </label>
                                                                <select class="form-control input-sm">
                                                                    <option>Promo 1</option>
                                                                    <option>Promo 2</option>
                                                                </select>
                                                            </div>
                                                        </div>
                                                        <div class="col-sm-4">
                                                            <div class="form-group form-group-sm form-inline">
                                                                <label for="start">Reason of Promotion : </label>
                                                                <input type="text" class="form-control input-sm" id="start" placeholder="Start date" value="Barang lain stok banyak">
                                                            </div>
                                                        </div>
                                                        <div class="col-sm-4">
                                                            <div class="form-inline">
                                                                <div class="form-group form-group-sm">
                                                                    <label for="start">Promotion Tagline : </label>
                                                                    <input type="text" class="form-control input-sm" id="start" placeholder="Start date" value="Beli Susu Downcown => Discount minuman sehat">
                                                                </div>
                                                                <div class="form-group form-group-sm pull-right">
                                                                    <button type="button" class="btn btn-primary btn-sm" data-toggle="collapse" data-target="#detail">Add Item</button>
                                                                </div>
                                                            </div>
                                                        </div>

                                                    </div>
                                                </form>

                                                <div id="detail" class="collapse">

                                                    <h5><b>Subject Item</b></h5>
                                                    <div class="table-responsive">
                                                        <table class="table table-bordered">
                                                            <thead>
                                                                <tr>
                                                                    <th>SKU</th>
                                                                    <th>Barcode</th>
                                                                    <th>Item Name</th>
                                                                    <th>Category</th>
                                                                    <th>Item Quantity</th>
                                                                    <th>Valid for Multiplication</th>
                                                                    <th>Sales Price</th>
                                                                    <th>Purchase</th>
                                                                    <th>Gross Profit</th>
                                                                    <th>Action</th>
                                                                </tr>
                                                            </thead>
                                                            <tbody>
                                                                <tr>
                                                                    <td>33.333</td>
                                                                    <td>22323232</td>
                                                                    <td>Susu Dancow 1000 gr</td>
                                                                    <td>Susu</td>
                                                                    <td>1</td>
                                                                    <td>Yes</td>
                                                                    <td>50,000</td>
                                                                    <td>45,000</td>
                                                                    <td>5,000</td>
                                                                    <td>
                                                                        <a data-toggle="modal" href="#addsubject" data-backdrop="static">Edit</a>
                                                                        |
                                                                        <a href="#">Delete</a>
                                                                    </td>
                                                                </tr>
                                                                <tr>
                                                                    <td><input type="text" class="form-control input-sm" placeholder="SKU" value=""></td>
                                                                    <td><input type="text" class="form-control input-sm" placeholder="Barcode" value=""></td>
                                                                    <td><input type="text" class="form-control input-sm" placeholder="Item name" value=""></td>
                                                                    <td></td>
                                                                    <td><input type="text" class="form-control input-sm" placeholder="Item Quantity" value=""></td>
                                                                    <td><select class="form-control input-sm">
                                                                            <option>Yes</option>
                                                                            <option>No</option>
                                                                        </select></td>
                                                                    <td></td>
                                                                    <td></td>
                                                                    <td></td>
                                                                    <td><button class="btn btn-primary btn-sm">Add</button></td>
                                                                </tr>
                                                            </tbody>
                                                        </table>
                                                    </div>

                                                    <h5><b>Object Item</b></h5>
                                                    <div class="table-responsive">
                                                        <table class="table table-bordered">
                                                            <thead>
                                                                <tr>
                                                                    <th>SKU</th>
                                                                    <th>Barcode</th>
                                                                    <th>Item Name</th>
                                                                    <th>Category</th>
                                                                    <th>Discount Promo Type</th>
                                                                    <th>Item Quantity</th>
                                                                    <th>Valid for Multiplication</th>
                                                                    <th>Regular Price</th>
                                                                    <th>Promotion Price</th>
                                                                    <th>Cost</th>
                                                                    <th>Action</th>
                                                                </tr>
                                                            </thead>
                                                            <tbody>
                                                                <tr>
                                                                    <td>20</td>
                                                                    <td>12212112</td>
                                                                    <td>Minuman 1000 Strabery</td>
                                                                    <td>Minuman</td>
                                                                    <td>Discount Rp</td>
                                                                    <td>2</td>
                                                                    <td>Yes</td>
                                                                    <td>1,000</td>
                                                                    <td>900</td>
                                                                    <td>800</td>
                                                                    <td>
                                                                        <a data-toggle="modal" href="#addsubject" data-backdrop="static">Edit</a>
                                                                        |
                                                                        <a href="#">Delete</a>
                                                                    </td>
                                                                </tr>
                                                                <tr>
                                                                    <td><input type="text" class="form-control input-sm" placeholder="SKU" value=""></td>
                                                                    <td><input type="text" class="form-control input-sm" placeholder="Barcode" value=""></td>
                                                                    <td><input type="text" class="form-control input-sm" placeholder="Item name" value=""></td>
                                                                    <td></td>
                                                                    <td><select class="form-control input-sm">
                                                                            <option>Discount 1</option>
                                                                            <option>Discount 2</option>
                                                                        </select></td>
                                                                    <td><input type="text" class="form-control input-sm" placeholder="Item Quantity" value=""></td>
                                                                    <td><select class="form-control input-sm">
                                                                            <option>Yes</option>
                                                                            <option>No</option>
                                                                        </select></td>
                                                                    <td></td>
                                                                    <td><input type="text" class="form-control input-sm" placeholder="Promotion price" value=""></td>
                                                                    <td></td>
                                                                    <td><button class="btn btn-primary btn-sm">Add</button></td>
                                                                </tr>
                                                            </tbody>
                                                        </table>
                                                    </div>

                                                    <h5><b>Analysis</b></h5>
                                                    <div class="table-responsive">
                                                        <table class="table table-bordered">
                                                            <thead>
                                                                <tr>
                                                                    <th>Subject Item Name</th>
                                                                    <th>Object Item Name</th>
                                                                    <th>Profit (Non Promo)</th>
                                                                    <th>Profit (Promo)</th>
                                                                    <th>Value (Promo)</th>
                                                                    <th>% (Promo)</th>
                                                                    <th>Item Quantity (Target Subject)</th>
                                                                    <th>Item Quantity (Target Object)</th>
                                                                    <th>Total Profit (Non Promo)</th>
                                                                    <th>Total Margin (Promo)</th>
                                                                    <th>Total Value (Promo)</th>
                                                                </tr>
                                                            </thead>
                                                            <tbody>
                                                                <tr>
                                                                    <td>Susu Dancow 1000gr</td>
                                                                    <td>Minuman 1000 Strabery</td>
                                                                    <td></td>
                                                                    <td></td>
                                                                    <td></td>
                                                                    <td></td>
                                                                    <td><input type="text" class="form-control input-sm" placeholder="Item name" value="100"></td>
                                                                    <td><input type="text" class="form-control input-sm" placeholder="Item name" value="120"></td>
                                                                    <td></td>
                                                                    <td></td>
                                                                    <td></td>
                                                                </tr>
                                                            </tbody>
                                                        </table>
                                                    </div>

                                                    <h5><b>Realization</b></h5>
                                                    <div class="table-responsive">
                                                        <table class="table table-bordered">
                                                            <thead>
                                                                <tr>
                                                                    <th>Subject Item Name</th>
                                                                    <th>Object Item Name</th>
                                                                    <th>Item Quantity (Realization Subject)</th>
                                                                    <th>Item Quantity (Realization Object)</th>
                                                                    <th>Total Profit (Non Promo)</th>
                                                                    <th>Total Profit (Promo)</th>
                                                                    <th>Total Value (Promo)</th>
                                                                </tr>
                                                            </thead>
                                                            <tbody>
                                                                <tr>
                                                                    <td>Susu Dancow 1000gr</td>
                                                                    <td>Minuman 1000 Strabery</td>
                                                                    <td></td>
                                                                    <td></td>
                                                                    <td></td>
                                                                    <td></td>
                                                                    <td></td>
                                                                </tr>
                                                            </tbody>
                                                        </table>
                                                    </div>
                                                    <button class="btn btn-primary btn-sm">Approve</button>
                                                </div>

                                            </div>
                                            <div class="panel-footer"><a data-toggle="collapse" href="#collapse1">Hide Detail</a></div>
                                        </div>
                                    </div>

                                    <div class="panel panel-default">
                                        <div class="panel-heading" data-toggle="collapse" data-target="#collapse2">
                                            <h4 class="panel-title">
                                                <a data-toggle="collapse" href="#collapse2">Promo 2</a>
                                            </h4>
                                        </div>
                                        <div id="collapse2" class="panel-collapse collapse">
                                            <div class="panel-body">2</div>
                                            <div class="panel-footer"><a data-toggle="collapse" href="#collapse2">Hide Detail</a></div>
                                        </div>
                                    </div>

                                    <div class="panel panel-default">
                                        <div class="panel-heading" data-toggle="collapse" data-target="#collapse3">
                                            <h4 class="panel-title">
                                                <a data-toggle="collapse" href="#collapse3">Promo 3</a>
                                            </h4>
                                        </div>
                                        <div id="collapse3" class="panel-collapse collapse">
                                            <div class="panel-body">3</div>
                                            <div class="panel-footer"><a data-toggle="collapse" href="#collapse3">Hide Detail</a></div>
                                        </div>
                                    </div>

                                </div>
                            </div>

                        </div>

                        <div class="modal-footer">
                            <button type="button" class="btn btn-danger btn-sm" data-dismiss="modal">Close</button>
                        </div>

                    </div>

                </div>
            </div>

            <div class="modal fade" id="addsubject" role="dialog">
                <div class="modal-dialog">
                    <div class="modal-content">

                        <div class="modal-header">
                            <button type="button" class="close" data-dismiss="modal">&times;</button>
                            <h4 class="modal-title">Edit Subject Item</h4>
                        </div>

                        <div class="modal-body">
                            <form>
                                <div class="row">

                                    <div class="col-sm-6">
                                        <div class="form-group form-group-sm">
                                            <label for="start">SKU</label>
                                            <input type="text" class="form-control input-sm" id="start" placeholder="SKU">
                                        </div>
                                    </div>
                                    <div class="col-sm-6">
                                        <div class="form-group form-group-sm">
                                            <label for="start">Barcode</label>
                                            <input type="text" class="form-control input-sm" id="start" placeholder="Barcode">
                                        </div>
                                    </div>
                                    <div class="col-sm-6">
                                        <div class="form-group form-group-sm">
                                            <label for="start">Item name</label>
                                            <input type="text" class="form-control input-sm" id="start" placeholder="Item name">
                                        </div>
                                    </div>
                                    <div class="col-sm-6">
                                        <div class="form-group form-group-sm">
                                            <label for="start">Quantity</label>
                                            <input type="text" class="form-control input-sm" id="start" placeholder="Quantity">
                                        </div>
                                    </div>
                                    <div class="col-sm-6">
                                        <div class="form-group form-group-sm">
                                            <label for="start">Valid for Multiplication</label>
                                            <select class="form-control input-sm">
                                                <option>Yes</option>
                                                <option>No</option>
                                            </select>
                                        </div>
                                    </div>

                                </div>
                            </form>
                        </div>

                        <div class="modal-footer">
                            <button type="button" class="btn btn-primary btn-sm" data-dismiss="modal">Save</button>
                            <button type="button" class="btn btn-danger btn-sm" data-dismiss="modal">Close</button>
                        </div>

                    </div>
                </div>
            </div>

            <div class="modal fade" id="addobject" role="dialog">
                <div class="modal-dialog">
                    <div class="modal-content">

                        <div class="modal-header">
                            <button type="button" class="close" data-dismiss="modal">&times;</button>
                            <h4 class="modal-title">Edit Object Item</h4>
                        </div>

                        <div class="modal-body">
                            <form>
                                <div class="row">

                                    <div class="col-sm-6">
                                        <div class="form-group form-group-sm">
                                            <label for="start">SKU</label>
                                            <input type="text" class="form-control input-sm" id="start" placeholder="SKU">
                                        </div>
                                    </div>
                                    <div class="col-sm-6">
                                        <div class="form-group form-group-sm">
                                            <label for="start">Barcode</label>
                                            <input type="text" class="form-control input-sm" id="start" placeholder="Barcode">
                                        </div>
                                    </div>
                                    <div class="col-sm-6">
                                        <div class="form-group form-group-sm">
                                            <label for="start">Item name</label>
                                            <input type="text" class="form-control input-sm" id="start" placeholder="Item name">
                                        </div>
                                    </div>
                                    <div class="col-sm-6">
                                        <div class="form-group form-group-sm">
                                            <label for="start">Marketing Promotion Type</label>
                                            <select class="form-control input-sm">
                                                <option>Promotion Type</option>
                                                <option>Discount Promo Type</option>
                                            </select>
                                        </div>
                                    </div>
                                    <div class="col-sm-6">
                                        <div class="form-group form-group-sm">
                                            <label for="start">Quantity</label>
                                            <input type="text" class="form-control input-sm" id="start" placeholder="Quantity">
                                        </div>
                                    </div>
                                    <div class="col-sm-6">
                                        <div class="form-group form-group-sm">
                                            <label for="start">Valid for Multiplication</label>
                                            <select class="form-control input-sm">
                                                <option>Yes</option>
                                                <option>No</option>
                                            </select>
                                        </div>
                                    </div>
                                    <div class="col-sm-6">
                                        <div class="form-group form-group-sm">
                                            <label for="start">Promotion Price</label>
                                            <input type="text" class="form-control input-sm" id="start" placeholder="Promotion Price">
                                        </div>
                                    </div>

                                </div>
                            </form>
                        </div>

                        <div class="modal-footer">
                            <button type="button" class="btn btn-primary btn-sm" data-dismiss="modal">Save</button>
                            <button type="button" class="btn btn-danger btn-sm" data-dismiss="modal">Close</button>
                        </div>

                    </div>
                </div>
            </div>

        </div>
    </body>
</html>
