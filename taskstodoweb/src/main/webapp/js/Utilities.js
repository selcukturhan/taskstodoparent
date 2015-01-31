(function ($) {
	/* Twitter Bootstrap Message Helper
	** Usage: Just select an element with `alert` class and then pass this object for options.
	** Example: $("#messagebox").message({text: "Hello world!", type: "error"});
	** Author: Afshin Mehrabani <afshin.meh@gmail.com>
	** Date: Monday, 08 October 2012 
	*/
	$.fn.message = function(options) {
		//remove all previous bootstrap alert box classes
		this[0].className = this[0].className.replace(/alert-(success|error|warning|info)/g , '');
		this.html(options.text).addClass("alert-" + (options.type || "warning"));
        this.show();
	};
})(jQuery);

//Workaround if Date.prototype.toISOString(ECMAScript 5) isn't implemented by engine. 
if ( !Date.prototype.toISOString ) {
    ( function() {
        function pad(number) {
            var r = String(number);
            if ( r.length === 1 ) {
                r = '0' + r;
            }
            return r;
        }
        Date.prototype.toISOString = function() {
            return this.getUTCFullYear()
                + '-' + pad( this.getUTCMonth() + 1 )
                + '-' + pad( this.getUTCDate() )
                + 'T' + pad( this.getUTCHours() )
                + ':' + pad( this.getUTCMinutes() )
                + ':' + pad( this.getUTCSeconds() )
                + '.' + String( (this.getUTCMilliseconds()/1000).toFixed(3) ).slice( 2, 5 )
                + 'Z';
        };
    }()
    );
}


function showMsg(selector, duration, msg){
	$(selector).html(msg);
	$(selector).fadeIn();
	
    setTimeout(function() {
    	$(selector).fadeOut('slow');
    }, duration);
	
}


function handleDateFormat(date){
	var dateRep = new Date(date);
	var month = dateRep.getMonth();
	return dateRep.getDate() + "/" + ++month + "/" + dateRep.getFullYear();
}


function getItemArrayLocation(array, id){
	for ( var index = 0; index < array.length; index++) {
		var item = array[index];
		if(item.id == id){
			return index;
		}	
	}
	return -1;
}


function callRestService(httpMethod, url, data, successHandler, errorHandler){
	$.ajax({
	    type: httpMethod,
	    url: url,
	    data: JSON.stringify(data),
	    dataType: "json",
	    contentType: "application/json; charset=utf-8",
	    success: successHandler,
	    error: errorHandler
	  });
	
}
//function callRestService(httpMethod, url, data, successHandler, errorHandler){
//	$.ajax({
//	    type: httpMethod,
//	    url: url,
//	    data: JSON.stringify(data),
//	    dataType: "json",
//	    contentType: "application/json; charset=utf-8"
//	}).done(successHandler)
//	   .fail(errorHandler);
//	
//}


function handleDate2Serialize(date){
	
	
}

function handleDate2Deserialize(){}
