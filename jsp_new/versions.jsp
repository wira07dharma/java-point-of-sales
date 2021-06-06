<style>
  .version-subject{ text-align: center; vertical-align: middle !important; }
  .fixed, .new { color: white; }
  .fixed { background-color: #c5e69e; }
  .new { background-color: #abd9ef; }
  .fixed:before{content: "Fixed Bug"}
  .new:before{content: "New"}
  .all-version th, .all-version td { border: 1px solid #989898 !important;}
</style>
<div class="modal fade in" id="modal-default">
  <div class="modal-dialog" style="width: 50%; min-width: 545px;">
    <div class="modal-content">
      <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">×</span></button>
        <h4 class="modal-title">Dimata Prochain Version History</h4>
      </div>
      <div class="modal-body">
        <div class="table-responsive all-version">
          <table class="table table-bordered">
            <thead>
              <tr>
                <th style="text-align: center">Version</th>
                <th style="text-align: center; width: 74px !important;">Type</th>
                <th style="text-align: left">Feature</th>
              </tr>
            </thead>
            <tbody>
              <tr>
                <td rowspan="2" class="version-subject">
                  <strong>5.1.0</strong><br>
                  <span>Juni 14, 2019</span>
                </td>
                <td class="version-subject new"></td>
                <td title="To show cashless modul, please set Sysprop USE_CASHLESS_MODULE to 1">Add Cashless Modul</td>
              </tr>
              <tr>
                <td class="version-subject new"></td>
                <td title="Please set GEN_ID in pos_app_user to OID of Company to make system work properly">Add Multitenant Company</td>
              </tr>
            </tbody>
          </table>
        </div>
      </div>
      <div class="modal-footer">
        <button type="button" class="btn btn-default pull-left" data-dismiss="modal">Close</button>
      </div>
    </div>
    <!-- /.modal-content -->
  </div>
  <!-- /.modal-dialog -->
</div>