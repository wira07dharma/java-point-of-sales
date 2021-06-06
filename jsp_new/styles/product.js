"use strict";
var locationId = 0;

var reqLocation = function(container, value=null) {
  var that = this;
  this.e = container;
  this.dataFor = "requiredlocaction";
  this.command = null;
  this.oid = null;
  this.uri = 'AjaxProdRequirement';
  this.onSuccessSaving = function() {};
  this.request = function(onSuccess) {
    var s = new formPack(that.e);
    s.oid = that.oid;
    s.dataFor = that.dataFor;
    s.command = that.command;
    basicAjax(baseUrl(that.uri), onSuccess, s.serialize());
  };
  this.init = function() {
    var tSave = new delay();
    var doSave = function() {
      that.command = save;
      tSave.invoke = function() {
        that.request(function(r) {
          var jR = jQuery.parseJSON(r)[0];
          console.log('response', jR);
          if (jR.oid) {
            toast("Sucess saving required location.", "Saving Succeed", "info");
            that.onSuccessSaving();
            
            that.oid = jR.oid;
            $(that.e).attr('data-value', jR.oid);
            $('body').find('.req-location-input-id').val(jR.oid);
            $('body').find('.ro-input').removeAttr('disabled');
            $('body').find('.rf-input').removeAttr('disabled');
            locationId = jR.oid
          }
        });
      };
      tSave.clear();
      tSave.start();
    }
    $(that.e).find('.reqloc-input').on('change keyup', doSave);
    $(that.e).find('select.select-alt-room').select2();
  };
  
  if (value) {
    console.log(value);
    that.oid = value.oid;
    $(that.e).attr('data-value', value.oid);
    $(that.e).find('.select-room-class').val(value.roomClass);
    $(that.e).find('.reqloc-material-id').val(value.material);
    $(that.e).find('.reqloc-duration').val(value.duration);
    $(that.e).find('.reqloc-index').val(value.index);
    $(that.e).find('.select-pic-type').val(value.ignorepic);
    for(var i=0; i<value.alternative.length; i++) {
      $(that.e).find('.select-alt-room').children('.rc-'+value.alternative[i].itemId).prop("selected", "selected");;
    };
  };
  
};

var reqPersonOption = function(e, p=null, dataId) {
  var that = this;
  this.parent = p;
  this.e = (typeof e == "string") ? $('<tr class="highlight" data-id="'+dataId+'"><td></td><td></td><td></td><td></td><td></td>'+e+'</tr>') : e;
  this.dataFor = "requiredPersonOption";
  this.command = null;
  this.oid = null;
  this.uri = 'AjaxProdRequirement';
  this.append = (typeof e == "string");
  this.section = 0;
  this.position = 0;
  this.request = function(form, onSuccess, dataFor=false) {
    var s = new formPack(form);
    s.oid = that.oid;
    s.dataFor = !dataFor ? that.dataFor : dataFor;
    s.command = that.command;
    basicAjax(baseUrl(that.uri), onSuccess, s.serialize());
  };
  this.destroy = function() {
    if($(that.e).find('.req-location-input-id').length) {
      $(that.e).find('.roo').find('.form-control').val("");
    } else {
      $(that.e).remove();
    }
  };
  this.changeSection = function(onDone=null) {
    var val = $(that.e).find('.roo').find('.hidden.roo-section-id').val();
    if (val != "" && val > 0) {
      that.section = $(that.e).find('.roo').find('.hidden.roo-section-id').val();
      $(that.e).find('.roo').find('.hidden.roo-section-id').val("");
    }
    var form = $('<form></form>');
    $(form).html($(that.e).find('.roo').find('.roo-department-id').clone());
    that.request(form, function(r) {
      var jR = jQuery.parseJSON(r)[0];
      var e = $(that.e).find('.roo').find('.roo-section-id');
      e.empty();
      e.append('<option value="0">-select-</option>');
      for(let i=0; i<jR.length; i++) {
        let s = jR[i];
        e.append('<option class="s-'+s.oid+'" value="'+s.oid+'">'+s.name+'</option>');
      }
      
      if($(e).find('.s-'+that.section).length > 0) {
        $(that.e).find('.roo').find('.hidden.roo-section-id').val(that.section);
        $(e).val(that.section);
      }
      onDone();
    }, "throwsection");
  };
  this.changeCompetency = function(onDone=null) {
    var val = $(that.e).find('.roo').find('.hidden.roo-competency-id').val();
    if (val != "" && val > 0) {
      that.section = $(that.e).find('.roo').find('.hidden.roo-competency-id').val();
      $(that.e).find('.roo').find('.hidden.roo-competency-id').val("");
    }
    var form = $('<form></form>');
    $(form).html($(that.e).find('.roo').find('input').clone());
    that.request(form, function(r) {
      var jR = jQuery.parseJSON(r)[0];
      var e = $(that.e).find('.roo').find('.roo-competency-id');
      e.empty();
      e.append('<option value="0">-select-</option>');
      for(let i=0; i<jR.length; i++) {
        let s = jR[i];
        e.append('<option class="s-'+s.oid+'" value="'+s.oid+'">'+s.name+'</option>');
      }
      
      if($(e).find('.s-'+that.section).length > 0) {
        $(that.e).find('.roo').find('.hidden.roo-section-id').val(that.section);
        $(e).val(that.section);
      }
      onDone();
    }, "throwcompetency");
  };
  this.changePosition = function(onDone=null){
    var val = $(that.e).find('.roo').find('.hidden.roo-position-id').val();
    if (val != "" && val > 0) {
      that.position = val;
      $(that.e).find('.roo').find('.hidden.roo-position-id').val("");
    }
    var form = $('<form></form>');
    $(form).html($(that.e).find('.roo').find('input').clone());
    that.request(form, function(r) {
      var jR = jQuery.parseJSON(r)[0];
      var e = $(that.e).find('.roo').find('.roo-position-id');
      e.empty();
      e.append('<option value="0">-select-</option>');
      for(let i=0; i<jR.length; i++) {
        let p = jR[i];
        e.append('<option class="p-'+p.oid+'" value="'+p.oid+'">'+p.name+'</option>');
      }
      
      if($(e).find('.p-'+that.position).length > 0) {
        $(that.e).find('.roo').find('.hidden.roo-position-id').val(that.position);
        $(e).val(that.position);
      }
      onDone();
    }, "throwposition");
  };
  this.init = function() {
    $(that.e).find('.clone-input').each(function() {cloneInput(this)});
    let eoid = $(that.e).find('.roo-person-option-id').val();
    eoid && (that.oid = eoid);
    that.append && $(that.e).insertAfter(that.parent);
    var tSave = new delay();
    var doSave = function() {
      var position = $(that.e).find('.roo').find('.roo-position-id').val();
      if (position) {
        that.command = save;
        var form = $('<form></form>');
        $(form).html($(that.e).find('.roo').find('input').clone());
        tSave.invoke = function() {
          that.request(form, function(r) {
            var jR = jQuery.parseJSON(r)[0];
            console.log('response', jR);
            if (jR.oid) {
              that.oid = jR.oid;
            };
          });
        };
        tSave.clear();
        tSave.start();
      };
    };

    $(that.e).find('td.roo-changes').find('.form-control').on('change keyup', function() {
      //var empty = $(that.e).find('.roo').find('input').filter(function() { return this.value == '';});
      doSave();
    });
      
    $(that.e).find('.roo').find('.delete').click(function() {
      var deletable = new remove(that.oid);
      deletable.dataFor = that.dataFor;
      deletable.onFinish = that.destroy;
      deletable.exec();
    });
    
    $(that.e).find('td.roo-change-position').find('.form-control').on('change keyup', function() {
      if ($(this).hasClass('roo-department-id')) {
        that.changeSection(function() {that.changePosition(doSave);});
      } else {
        that.changePosition(doSave);
      }
    });
    
    $(that.e).find('td.roo-change-competency').find('.form-control').on('change keyup', function() {
      that.changeCompetency(doSave);
    });
    
  };
};

var reqPerson = function(container) {
  var that = this;
  this.e = container;
  this.tr = '<tr class="tr-person">'+$(that.e).find('.tr-person').html()+'</tr>';
  this.enableForm = function() {var t = $("<table><tbody></tbody></table>").html(that.tr); $(t).find("tbody").find('.form-control').removeAttr("disabled"); that.tr = $(t).find("tbody").html();};
  this.countRow = 0;
  this.reqLocation = null;
  this.recountRow = function() {
    var trC=1;
    $(that.e).find('.tr-person').find('.d').each(function(){
      $(this).html(trC);
      trC++;
    });
  };
  this.row = function(e) {
    var thus = this;
    this.e = $(e);
    this.dataFor = "requiredPerson";
    this.command = null;
    this.oid = null;
    this.uri = 'AjaxProdRequirement';
    this.inputVerification = function() {
      var e = thus.e;
      var jobDesc = $(e).find('.ro-jobdesc');
      $(jobDesc).focusout(function() {
        var val = $(this).val();
        if ($.isNumerical(val)) {
          toast("Jobdesc cannot be numerical value", "Error saving", "error");
        };
      })
    };
    this.destroy = function() {
      $(thus.e).remove();
      that.recountRow();
    };
    this.request = function(form, onSuccess) {
      var s = new formPack(form);
      s.oid = thus.oid;
      s.dataFor = thus.dataFor;
      s.command = thus.command;
      basicAjax(baseUrl(thus.uri), onSuccess, s.serialize());
    };
    this.init = function() {
      $(thus.e).find('.req-location-input-id').val($(that.reqLocation).data('value'));
      $(that.reqLocation).data('value') && $(thus.e).find('.ro-input').removeAttr('disabled');
      var c = 'person-'+that.countRow;
      $(thus.e).attr('data-id', c);
      $(thus.e).find('.roo').addClass(c);
      var personOption = new reqPersonOption(thus.e);
      personOption.init();
      
      // New req person option
      $(thus.e).find('.small-button.add').click(function() {
        var dataId = $(this).closest('tr').data('id');
        var rpo = $('<tr></tr>').html($(thus.e).clone().find('.roo'));
        $(rpo).find('.form-control').val("");
        var personOpt = new reqPersonOption(rpo.html(), $(thus.e),dataId); //--
        personOpt.init();
      });
      
      var tSave = new delay();
      var doSave = function() {
        thus.command = save;
        var form = $('<form></form>');
        $(form).html($(thus.e).find('input').clone());
        tSave.invoke = function() {
          thus.request(form, function(r) {
            var jR = jQuery.parseJSON(r)[0];
            console.log('response', jR);
            if (jR.oid) {
              toast("Sucess saving required person option.", "Saving Succeed", "info");
              thus.oid = jR.oid;
              $('body').find('.req-person-input-id').val(jR.oid);
              $(that.e).find('.'+ $(thus.e).data('id')).find('.form-control').removeAttr('disabled');
            }
          });
        };
        tSave.clear();
        tSave.start();
      };
      
      $(thus.e).find('.ro').on('change keyup', function() {
        var empty = $(this).closest('tr').find('.ro').find('input').filter(function() { return this.value == '';});
        var val = $(this).closest('tr').find('.ro-jobdesc').val();
        var inputOk = !/^[0-9,.]*$/.test(val);
        if (empty.length == 0 && inputOk) {
          doSave();
        } else {
          tSave.clear();
          if (empty.length == 0) {
            toast("Jobdesc cannot be numerical value", "Error saving", "error");
          }
        }
      });
      
      $(thus.e).find('.n').click(function() {
        var deletable = new remove(thus.oid);
        deletable.dataFor = thus.dataFor;
        deletable.onFinish = thus.destroy;
        deletable.exec();
      });
      
      //thus.inputVerification(thus.e);
      
    };
    
  };
  this.append = function(e) {
    $(e).addClass('highlight');
    $(that.e).find('.tbody-person').append(e);
    that.recountRow();
  };
  this.newItem = function(oid=null, e=null) {
    var r = new that.row(e ? e : that.tr);
    oid && (r.oid = oid);
    r.init();
    that.append(r.e);
  };
  this.init = function(autoAdd=true) {
    $(that.e).find('.tr-person').remove();
    $(that.e).find('.add-person').click(function() { that.newItem() });
    !autoAdd || that.newItem();
  };
};

var reqFacility = function(container) {
  var that = this;
  this.reqLocation = null;
  this.e = container;
  this.tr = '<tr>'+$(that.e).find('.tr-facility').html()+'</tr>';
  this.enableForm = function() {var t = $("<table><tbody></tbody></table>").html(that.tr); $(t).find("tbody").find('.form-control').removeAttr("disabled"); that.tr = $(t).find("tbody").html();};
  this.countRow = 0;
  this.row = function(e) {
    var thus = this;
    this.e = $(e);
    this.dataFor = "requiredFacility";
    this.command = null;
    this.oid = null;
    this.uri = 'AjaxProdRequirement';
    this.request = function(form, onSuccess) {
      var s = new formPack(form);
      s.oid = thus.oid;
      s.dataFor = thus.dataFor;
      s.command = thus.command;
      basicAjax(baseUrl(thus.uri), onSuccess, s.serialize());
    };
    this.destroy = function() {
      $(thus.e).remove();
    };
    this.init = function() {
      $(thus.e).find('.req-location-input-id').val($(that.reqLocation).data('value'));
      $(that.reqLocation).data('value')&& $(thus.e).find('.rf-input').removeAttr('disabled');
      $(thus.e).find('.clone-input').each(function() {cloneInput(this)});
      var tSave = new delay();
      var doSave = function() {
        thus.command = save;
        var form = $('<form></form>');
        $(form).html($(thus.e).find('.form-control').clone());
        tSave.invoke = function() {
          thus.request(form, function(r) {
            var jR = jQuery.parseJSON(r)[0];
            console.log('response', jR);
            if (jR.oid) {
              thus.oid = jR.oid;
            }
          });
        };
        tSave.clear();
        tSave.start();
      };
      $(thus.e).find('.rf').on('change keyup', function() {
        var empty = $('.rf').find('.form-control').filter(function() { return this.value == '';});
        if (empty.length == 0) {
          doSave();
        } else {
          tSave.clear();
        }
      });
      
      $(thus.e).find('.n').click(function() {
        var deletable = new remove(thus.oid);
        deletable.dataFor = thus.dataFor;
        deletable.onFinish = thus.destroy;
        deletable.exec();
      });
      
    };
  };
  this.append = function(e) {
    $(e).addClass('highlight');
    $(that.e).find('#tbody-facility').append(e);
  };
  this.newItem = function(oid=null, e=null) {
    var r = new that.row(e ? e : that.tr);
    oid && (r.oid = oid)
    r.init();
    that.append(r.e);
    $(that.e).find('#tbody-facility').children('tr').each(function() {
      $(this).find('.d').html($(this).index()+1);
    });
    that.countRow++;
  };
  this.init = function(autoAdd=true) {
    $(that.e).find('.tr-facility').remove();
    $(that.e).find('#add-facility').click(function() { that.newItem() });
    !autoAdd || that.newItem();
  };
};

var prodReqListing = function(container, formValue) {
  var that = this;
  this.roomClass = null;
  this.department = null;
  this.section = null;
  this.position = null;
  this.competency = null;
  this.level = null;
  this.aktiva = null
  
  this.finishEdit = function() {};
  this.finishDelete = function() {};
  
  this.e = container;
  this.edit = null;
  this.p = parent;
  this.dataFor = "showlistprodrequirements";
  this.command = null;
  this.oid = null;
  this.form = formValue;
  this.eli = $($(container).find('li').clone());
  this.li = function() {
    var thus = this;
    this.e = $(that.eli).clone();
    this.container = null;
    this.eReqPerson = null;
    this.eReqPersonOption = null;
    this.eReqFacility = null;
    this.value = null;
    
    this.setReqLocation = function(d) {
      var them = this;
      console.log("setReqLocation");
      console.log(d);
      $(thus.container).find('.rl-room-class').html($(that.roomClass).find('.rc-'+d.roomClass).html());
      $(thus.container).find('.rl-duration').html(d.duration);
      $(thus.container).find('.rl-index').html(d.index);
      $(thus.container).find('.rl-pic').html($(that.form).find(".select-pic-type").children("option[value='"+d.ignorepic+"']").html());
      let alternativeRoom = "";
      for(let i=0; i<d.alternative.length; i++) {
        (i<1) || (alternativeRoom += ", ");
        alternativeRoom += d.alternative[i].itemName;
      }
      $(thus.container).find('.rl-alt-room').html(alternativeRoom);
      console.log(that.form);
    };
    this.addReqPerson = function(d) {
      var e = $($(thus.eReqPerson).clone());
      $(e).find('.ro-no').html(d.no);
      $(e).find('.ro-person').html(d.person);
      $(e).find('.ro-jobdesc').html(d.jobdesc);
      $(e).find('.ro-job-weight').html(d.jobWeight);
      $(e).children().attr('rowspan', d.span);
      $(thus.container).find('.tbody-person').append(e);
    };
    this.addReqPersonOption = function(d) {
      var e = $($(thus.eReqPersonOption).clone());
      $(e).find('.rpo-department').html($(that.department).find('.d-'+d.department).html());
      $(e).find('.rpo-section').html($(that.section).find('.s-'+d.section).html());
      $(e).find('.rpo-position').html($(that.position).find('.p-'+d.position).html());
      $(e).find('.rpo-competency').html($(that.competency).find('.c-'+d.competency).html());
      $(e).find('.rpo-level').html($(that.level).find('.l-'+d.level).html());
      $(e).find('.rpo-priority').html(d.priority);
      $(thus.container).find('.tbody-person').append(e);
    };
    this.addReqFacility = function(d) {
      var e = $($(thus.eReqFacility).clone());
      $(e).find('.rf-no').html(d.no);
      $(e).find('.rf-aktiva').html($(that.aktiva).find('.s-'+d.aktiva).html());
      $(e).find('.rf-number').html(d.number);
      $(e).find('.rf-note').html(d.note);
      $(e).find('.rf-order-index').html(d.order);
      $(e).find('.rf-duration').html(d.duration);
      $(thus.container).find('.tbody-facility').append(e);
    };
    this.getReqFacility = function() {return thus.reqFacility.html();};
    this.init = function() {
      $(thus.container).find('.edit').click(function(){
        $("html, body").animate({ scrollTop: $($(this).data('location')).offset().top }, 1000);
        var editable = new editFormProduct(that.edit, that.form.clone(), thus.value);
        editable.finish = that.finishEdit;
        editable.init();
      });
      $(thus.container).find('.delete').click(function(){
        var deletable = new remove(thus.value.oid);
        deletable.dataFor = "requiredlocaction";
        deletable.onFinish = that.finishEdit;
        deletable.exec();
      });
    };
    
    thus.container = $($(thus.e));
    thus.eReqPerson = $($(thus.container).find('.tr-person'));
    $(thus.container).find('.tr-person').remove();
    thus.eReqPersonOption = $($(thus.container).find('.tr-person-option'));
    $(thus.container).find('.tr-person-option').remove();
    thus.eReqFacility = $($(thus.container).find('.tr-facility'));
    $(thus.container).find('.tr-facility').remove();
    
  };
  this.serialize = function() {
      var x = null;
      var x = mergeOptions(x, {
        "FRM_FIELD_OID": that.oid,
        "FRM_FIELD_DATA_FOR": that.dataFor,
        "command": that.command
      });
      console.log(x);
      return x;
  };
  this.get = function(onSuccess) {
    basicAjax( baseUrl('AjaxProdRequirement'), onSuccess, that.serialize());
  };
  this.init = function(r) {
    
    $(that.e).find('li').remove();
    var obj = JSON.parse(r)[0];
    console.log(obj);
    for(let i=0; i<obj.length; i++) {
      let l = obj[i];
      var li = new that.li();
      li.value = l;
      li.setReqLocation({roomClass: l.roomClassId, duration: l.duration, index: l.index, ignorepic:l.ignorepic, alternative:l.alternative});
      li.init();
      for (let j=0; j<l.reqPersons.length; j++) {
        let p = l.reqPersons[j];
        li.addReqPerson({no: j+1, person: p.number, jobdesc: p.jobdesc, jobWeight: p.jobweight, span: (p.reqPersonOptions.length < 2) ? 2 : p.reqPersonOptions.length+1});
        for (let k=0; k<p.reqPersonOptions.length; k++) {
          let po = p.reqPersonOptions[k];
          li.addReqPersonOption({department:po.departmentId, section: po.sectionId, level: po.levelId, position:po.positionId, priority:po.priority, competency:po.competencyId});
        }
        if (p.reqPersonOptions.length<1) {
          li.addReqPersonOption({department:"-", section: "-", level: "-", position:"-", priority:"-", competency:"-"});
        }
      }
      
      for (let j=0; j<l.reqFacilities.length; j++) {
        let f = l.reqFacilities[j];
        console.log(f);
        li.addReqFacility({no: j+1, aktiva: f.aktivaId, number: f.number, note: f.note, order: f.index, duration:f.duration});
      }
      $(that.e).append(li.container);
    }
  };
  
  that.roomClass = $(formValue).find('.select-room-class').clone();
  that.department = $(formValue).find('.select-department').clone();
  that.section = $(formValue).find('.select-section').clone();
  that.position = $(formValue).find('.select-position').clone();
  that.competency = $(formValue).find('.select-competency').clone();
  that.level = $(formValue).find('.select-level').clone();
  that.aktiva = $(formValue).find('.select-aktiva').clone();
  
};

function formNewProduct(e) {
  var that = this;
  this.e = e;
  this.container = null;
  this.finish = function(){};
  this.init = function() {
    var loc = new reqLocation($(that.e).find('.reqlocation'));
    var prs = new reqPerson($(that.e).find('.table-person')); prs.reqLocation = loc.e;
    var fac = new reqFacility($(that.e).find('.table-facility')); fac.reqLocation = loc.e;
    loc.onSuccessSaving = function() { prs.enableForm(); fac.enableForm(); };
    
    loc.init();
    prs.init();
    fac.init();
    
    $(that.container).html($(that.e));
    $(that.e).find('.finish-add-product').click(function() {
      $(this).attr('disabled', 'disabled');
      var x = new delay(2);
      x.invoke = function() {
        var l = new loading();
        if (!l.hasLoading()) {
          $(that.container).html("");
          that.finish();
        } else {
          x.clear();
          x.start();
        }
      }
      x.start();
    });
  }
}

var editFormProduct = function(container, form, value) {
  console.log("Edit Form Product");
  console.log(value);
  var that = this;
  this.container = container;
  this.e = form;
  this.v = value;
  this.finish = function() {};
  this.init = function() {
    var loc = new reqLocation($(that.e).find('.reqlocation'), {oid:that.v.oid, material:that.v.materialId, index:that.v.index, duration:that.v.duration, roomClass:that.v.roomClassId, ignorepic:that.v.ignorepic, alternative:that.v.alternative}); loc.init();
    var prs = new reqPerson($(that.e).find('.table-person')); prs.reqLocation = loc.e; prs.init(false);
    var fac = new reqFacility($(that.e).find('.table-facility')); fac.reqLocation = loc.e; fac.init(false);
    
    var eRP  = $(prs.tr);
    var eRPO = $('<tr><td></td><td></td><td></td><td></td><td></td></tr>').append($(prs.tr).find('.roo'));
    var eRF  = $(fac.tr);
    $(eRP).find('.form-control').removeAttr('disabled');
    $(eRPO).find('.form-control').removeAttr('disabled');
    $(eRF).find('.form-control').removeAttr('disabled');
    
    let vPersons = that.v.reqPersons;
    for (let i=0; i<vPersons.length; i++) {
      let vP = vPersons[i];
      let eP = eRP.clone();
      $(eP).find('.req-location-input-id').val(vP.reqLocationId);
      $(eP).find('.ro-number').val(vP.number);
      $(eP).find('.ro-jobdesc').val(vP.jobdesc);
      $(eP).find('.ro-jobweight').val(vP.jobweight);
      $(eP).find('.req-person-input-id').val(vP.oid);
      if (vP.reqPersonOptions.length > 0) {
        $(eP).find('.roo-person-option-id').val(vP.reqPersonOptions[0].oid);
        $(eP).find('.roo-department-id').val(vP.reqPersonOptions[0].departmentId);
        $(eP).find('.roo-section-id').val(vP.reqPersonOptions[0].sectionId);
        $(eP).find('.roo-position-id').val(vP.reqPersonOptions[0].positionId);
        $(eP).find('.roo-competency-id').val(vP.reqPersonOptions[0].competencyId);
        $(eP).find('.roo-level-id').val(vP.reqPersonOptions[0].levelId);
        $(eP).find('.roo-index').val(vP.reqPersonOptions[0].priority);
      };
      prs.newItem(vP.oid, eP);
      var etmp = null;
      let vPersonOptions = vP.reqPersonOptions;
      for(let j=1; j<vPersonOptions.length; j++) {
        let vPO = vPersonOptions[j];
        let ePO = eRPO.clone();
        $(ePO).find('.roo-person-id').val(vP.oid);
        $(ePO).find('.roo-person-option-id').val(vP.reqPersonOptions[j].oid);
        $(ePO).find('.roo-department-id').val(vP.reqPersonOptions[j].departmentId);
        $(ePO).find('.roo-section-id').val(vP.reqPersonOptions[j].sectionId);
        $(ePO).find('.roo-position-id').val(vP.reqPersonOptions[j].positionId);
        $(ePO).find('.roo-level-id').val(vP.reqPersonOptions[j].levelId);
        $(ePO).find('.roo-index').val(vP.reqPersonOptions[j].priority);
        
        var personOpt = new reqPersonOption(ePO, etmp ? etmp : eP);
        personOpt.oid = vPO.oid;
        personOpt.append = true;
        personOpt.init();
        etmp = ePO;
      };
    };
    
    var vFacilities = that.v.reqFacilities;
    for (let i=0; i<vFacilities.length; i++) {
      let vF = vFacilities[i];
      let eF = eRF.clone();
      $(eF).find('.req-location-input-id').val(vF.reqLocationId);
      $(eF).find('.rf-material-id').val(vF.materialId);
      $(eF).find('.rf-aktiva-id').val(vF.aktivaId);
      $(eF).find('.rf-number').val(vF.number);
      $(eF).find('.rf-order-index').val(vF.index);
      $(eF).find('.rf-note').val(vF.note);
      $(eF).find('.rf-duration').val(vF.duration);
      fac.newItem(vF.oid, eF);
    }
    
    $(that.container).html($(that.e));
    $(that.e).find('.finish-add-product').click(function() {
      $(this).attr('disabled', 'disabled');
      var x = new delay(2);
      x.invoke = function() {
        var l = new loading();
        if (!l.hasLoading()) {
          $(that.container).html("");
          that.finish();
        } else {
          x.clear();
          x.start();
        }
      }
      x.start();
    });
  }
}

var remove = function(oid) {
  var that = this;
  this.dF = null;
  this.dataFor = null;
  this.command = del;
  this.oid = oid;
  this.uri = 'AjaxProdRequirement';
  this.onFinish = function(r) {};
  this.exec = function() {
    var s = new formPack();
    s.oid = that.oid;
    s.dataFor = that.dataFor;
    s.command = that.command;
    basicAjax(baseUrl(that.uri), that.onFinish, s.serialize());
  }
}