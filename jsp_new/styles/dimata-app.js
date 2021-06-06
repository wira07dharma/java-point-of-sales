(function($) {
    
    $('html > head').append("<style type='text/css'>.has-error{box-shadow:none;border-color:#F56954 !important}</style>");
    $('html > body').append("<div class='col-md-4 notifications bottom-right' id='notifications'></div>");
    $.fn.validate = function( options ) {
	//GET THE VALUE
	var elementValue = $(this).val();
	
	
	//FUNCTION ERROR MESSAGE
	var errormessage = function(object, msg){
	    
	    var checkelement = $(object).parent().find(".errormessage").length;
	    if(checkelement == 0){
		$(object).parent().append("<label class='errormessage'><i class='fa fa-info'></i> "+msg+"</label>");
	    }else{
		$(object).parent().find(".errormessage").html("<i class='fa fa-info'></i> "+msg).show();
	    }
	};
	
        // DEFAULT SETTING
        var settings = $.extend({
            minLength	: 0,
            matchValue  : null,
	    classError	: "has-error",
            checkType   : "text",
	    valueNotAllowed : 0
        }, options);

        return this.each( function() {
            
	    switch(settings.checkType){
		case "text" :
		    
		    if(settings.minLength != null && elementValue.length < settings.minLength){
			$(this).focus();
			$(this).parent().addClass(settings.classError);
			errormessage(this,"Data Diperlukan.");

		    }else if(settings.matchValue != null && elementValue != settings.matchValue){
			$(this).focus();
			$(this).parent().addClass(settings.classError);
			errormessage(this, "Data Tidak Sesuai.");
		    }else{
			$(this).parent().removeClass(settings.classError).find(".errormessage").hide();
		    }
		break;
		
		case "combobox" :
		    
		    if(settings.minLength != null && elementValue.length < settings.minLength){
			$(this).focus();
			$(this).parent().addClass(settings.classError);
			errormessage(this,"Data Diperlukan.");

		    }else if(settings.matchValue != null && elementValue != settings.matchValue){
			$(this).focus();
			$(this).parent().addClass(settings.classError);
			errormessage(this, "Data Tidak Sesuai.");
		    }else if(settings.valueNotAllowed == elementValue){
			$(this).focus();
			$(this).parent().addClass(settings.classError);
			errormessage(this, "Data Diperlukan.");
		    }else{
			$(this).parent().removeClass(settings.classError).find(".errormessage").hide();
		    }
		break;

		case "email" :
		    if(settings.minLength != null && elementValue.length < settings.minLength){
			$(this).focus();
			$(this).parent().addClass(settings.classError);
			errormessage(this, "Data Diperlukan.");
		    }else if(elementValue.length > 0){
			if(elementValue.indexOf("@") == -1 || elementValue.indexOf(".") == -1){
			    
			    $(this).focus();
			    $(this).parent().addClass(settings.classError);
			    errormessage(this, "Format Email Salah.");
			}else if(settings.matchValue != null && elementValue == settings.matchValue){
			    $(this).focus();
			    $(this).parent().addClass(settings.classError);
			    errormessage(this,"Email Tidak Sesuai.");
			}else{
			    $(this).parent().removeClass(settings.classError).find(".errormessage").hide();
			}
		    }else{
			$(this).parent().removeClass(settings.classError).find(".errormessage").hide();
		    }
		break;
		default : 
		    $(this).parent().removeClass(settings.classError);
		    $(this).parent().removeClass(settings.classError).find(".errormessage").hide();
	    }
	    
        });

    }
    
    
    $.fn.getData = function( options ){
	var settings = $.extend({
	    onDone		    : function(data){
		
	    },
	    onSuccess		    : function(data){
	
	    },
	    approot		    : 'localhost:8080/',
	    dataSend		    : null,
	    servletName		    : null,
	    ajaxDataType	    : 'json',
	    dataAppendTo	    : null,
	    loadingInfo		    : true,
	    notification	    : false,
	    notificationType	    : 'info',
	    notificationTransition  : 'fade',
	    ajaxContentType	    : 'application/x-www-form-urlencoded; charset=UTF-8',
	    ajaxProcessData	    : true
	    
	}, options);
	
	return this.each( function() {
	    if(settings.dataAppendTo != null && settings.loadingInfo == true){
		$(settings.dataAppendTo).html("<div class='progress progress-striped active'><div class='progress-bar progress-bar-primary' style='width: 100%'><b>Please Wait...</b></div></div>").fadeIn("medium");
	    }
	   $.ajax({
	       type	: 'POST',
	       data	: settings.dataSend,
	       url	: settings.approot+"/"+settings.servletName,
	       dataType	: settings.ajaxDataType,
	       contentType : settings.ajaxContentType,
	       processData : settings.ajaxProcessData,
	       success	: function(data){
		   if(settings.dataAppendTo != null){
		       if(settings.ajaxDataType == "html"){
			   $(settings.dataAppendTo).html(data)
		       }else{
			   $(settings.dataAppendTo).html(data.FRM_FIELD_HTML)
		       }
		       
		   }
		   
		   
		   if($.isFunction(settings.onSuccess)){
		       settings.onSuccess.call(this);
		   }
	       },
	       error	: function(jqXHR, exception) {
		    if (jqXHR.status === 0) {
			alert('Not connect.n Verify Network.');
		    } else if (jqXHR.status == 404) {
			alert('Requested page not found. [404]');
		    } else if (jqXHR.status == 500) {
			alert('Internal Server Error [500].');
		    } else if (exception === 'parsererror') {
			alert('Requested JSON parse failed.');
		    } else if (exception === 'timeout') {
			alert('Time out error.');
		    } else if (exception === 'abort') {
			alert('Ajax request aborted.');
		    } else {
			alert('Uncaught Error.n' + jqXHR.responseText);
		    }
		}
	       
	   }).done(function(data){
	      if($.isFunction(settings.onDone)){
		  settings.onDone(data).call(this);
	      } 
	   }); 
	});
	
    }

}(jQuery));



