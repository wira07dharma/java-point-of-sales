"use strict";

function newDateTime(e) {
  $(e).datetimepicker({
    format:'Y-m-d H:i',
    formatDate:'Y-m-d H:i'
  });
}

function newTime(e) {
  $(e).datetimepicker({
    datepicker:false,
    format:'H:i',
    step:60
  });
}

function newDate(e, natural=false) {
  $(e).datetimepicker({
    timepicker:false,
    format: (natural) ? 'Y-m-d' : 'Y-m-d',
    formatDate:'Y-m-d'
  });
}

function rowHighlight(e) {
  $(e).hover(function() {
    $(this).parent("tr").children("td").addClass("highlight");
  },
  function() {
    $(this).parent("tr").children("td").removeClass("highlight");
  });
}

function multiHover(e) {
  var c = $(e).data("class");
  $(e).addClass(c);
  $(e).hover(function() {
    $("."+c).addClass("chosen");
  },
  function() {
    $('.'+c).removeClass("chosen");
  });
}

function toggleForm(e) {
  $(e).click(function() {
    if ( ! $(this).parent().hasClass('toggle-form-with-id')) {
      $(this).closest('.toggle-form').toggleClass('toggle-form-disabled');
      var state = $(this).attr('data-state');
      if (state == 'enabled') {
        if ($(this).attr('data-txt-disabled'))
          $(this).html($(this).attr('data-txt-disabled'));
        $(this).attr('data-state', 'disabled');
        $(this).closest('.toggle-form').find('input').each(function() {
          $(this).attr('disabled', '');
        });
        $(this).closest('.toggle-form').find('textarea').each(function() {
          $(this).attr('disabled', '');
        });
      } else {
        if ($(this).attr('data-txt-enabled'))
          $(this).html($(this).attr('data-txt-enabled'));
        $(this).attr('data-state', 'enabled');
        $(this).closest('.toggle-form').find('input').each(function() {
          $(this).removeAttr('disabled');
        });
        $(this).closest('.toggle-form').find('textarea').each(function() {
          $(this).removeAttr('disabled');
        });
      }
    } else {
      var state = $(this).attr('data-state');
      if (state == 'enabled') {
        $(this).attr('data-state', 'disabled');
        $('#'+$(this).attr('data-for')).val(0);
        $(this).removeClass('active');
      } else {
        $(this).attr('data-state', 'enabled');
        $('#'+$(this).attr('data-for')).val(1);
        $(this).addClass('active');
      }
    }
  });
}

function newSelect(e) {
  var xxx = e;
  $(document).click(function (e)
  {
    var container = $(xxx);
    if (!container.is(e.target) && container.has(e.target).length === 0)
    {
        container.find('.dropdown-menu').hide();
    }
  });
  $(e).find('.dropdown-toggle').click(function() {
    $(this).siblings(".dropdown-menu").toggle();
  });
  $(e).find('.dropdown-menu li').each(function() {
    $(this).click(function() {
      $(this).closest('.dropdown-menu').hide();
      var txt = $(this).children("a").html();
      var id = $(this).children("a").attr("data-value");
      $(this).parent().next('.input').val(id);
      $(this).parent().siblings('button').children('span.text').html(txt);
    });
  });
}

function mergeOptions(obj1,obj2){
    var obj3 = {};
    for (var attrname in obj1) { obj3[attrname] = obj1[attrname]; }
    for (var attrname in obj2) { obj3[attrname] = obj2[attrname]; }
    return obj3;
}

function form2JSON(p) {
  var parent = p.clone();
  var form = {};
  var selects = {};
  $(p).find("select").each(function() {
    if($(this).is("[multiple]")) {
      var selections = [];
      var data = $(this).select2('data');
      var name = $(this).attr("name");
      for(let i=0; i<data.length; i++) {
        selections.push(data[i].id);
      }
      selects[name] = JSON.stringify(selections);
    } else {
      selects[$(this).attr("name")] = $(this).val();
    }
    $(parent).find("#"+$(this).attr("id")).remove();
  });
  form = $(parent).serializeArray().reduce(function(a, x) { a[x.name] = x.value; return a; }, {});
  for (var attrname in selects) { form[attrname] = selects[attrname]; }
  console.log(form);
  return form
}

function basicAjax(uri, onSuccess, objectData={}, method="POST") {
  var method = method;
  var uri = uri;
  var oS = onSuccess;
  console.log(objectData);
  var l = new loading("#circle");
  l.startLoading();

  $.ajax({
    type: method,
    url: uri,
    data: objectData,
    success: function(r){
      console.log(r)
      if (r.includes("window.location")) {
        $('body').html(r);
      } else {
        oS(r);
      }
    },
    complete: function() {
      l.stopLoading();
    },
    error: function(XHR, textStatus, errorThrown) {
      console.log(XHR.status);
      console.log(XHR.responseText);
      console.log(errorThrown);
    }
  });
}

function formPack(form=null) {
  var that = this;
  this.oid = 0;
  this.dataFor = 0;
  this.command = 0;
  this.form = form;
  this.serialize = function() {
      var x = form2JSON(that.form);
      var x = mergeOptions(x, {
        "FRM_FIELD_OID": that.oid,
        "FRM_FIELD_DATA_FOR": that.dataFor,
        "command": that.command
      });
      console.log('request', x);
      return x;
  }
}

function loading() {
  var that = this;
  var container = $('body').find('pre-loader');
  if (container.html() == "") {container.html('<div style="display: block; width: 80%; position: relative; bottom: 0; left: 0; right:0; margin-left: 10%; height: 100px;"> <div style="position: fixed; display: block; right: 10px; bottom: 50px; padding: 10px;"> <i class="fa fa-circle-o-notch fa-spin fa-3x fa-fw"></i> <span class="sr-only">Loading...</span> </div> </div>');}
  
  this.e = container;
  this.startLoading = function() {
    var n = parseInt(that.e.attr("data-load")) + 1;
    that.e.attr("data-load", n);
    that.init();
  }
  this.stopLoading = function() {
    var n = parseInt(that.e.attr("data-load")) - 1;
    that.e.attr("data-load", (n<0 ? 0 : n));
    that.init();
  }
  this.init = function() {
    that.e.attr("data-load") > 0 ? that.e.fadeIn("slow", "linear") : that.e.fadeOut("slow", "linear");
  }
  this.hasLoading = function() {
    return parseInt(that.e.attr("data-load")) > 0 ? true : false;
  }
}

function delay(length=1) {
  var that = this;
  this.length = length*1000; // in ms
  this.invoke = null;
  this.onClear = function() {};
  this.onStart = function() {};
  this.t = null;
  this.start = function() {
    that.onStart();
    that.t = setTimeout(that.invoke, that.length);
  }
  this.clear = function() {
    that.onClear();
    clearTimeout(that.t);
    that.t = null;
  }
}

function cloneInput(e) {
  var fo = $(e).parent().find('#'+$(e).data('for'));
  $(e).on('change keyup keydown click', function() {
    fo.val($(this).val());
  })
}

function toast(content="", heading="Information", type="info", transition="slide") {
  var heading = heading;
  var content = content;
  var type = type; // info, error, warning, success
  var transition = transition; // fade, slide, plain
  $.toast({
    heading: heading,
    text: content,
    showHideTransition: transition,
    icon: type,
    stack: 10
  });
}