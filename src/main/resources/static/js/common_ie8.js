/* check */
$(document).ready(function() { 
	"use strict";
	$("input:checkbox").on('click', function() { 
		if ( $(this).prop('checked') ) { 
			$(this).parent().addClass("selected"); 
		} else {
			$(this).parent().removeClass("selected"); 
		} 
	}); 
});

/* radio */
$(document).ready(function(){
	"use strict";
	var _designRadio = $('.designRadio');
	var _iLabel = $('.iLabel');
	$(_iLabel).click(function(){
		var _thisRadio = $(this).parent().find('> .designRadio');
		var _value = $(this).parent().find('>input').val();
		$(_designRadio).children().removeClass('checked');
		$(_thisRadio).children().addClass('checked');
		console.log(_value);
	});
	$(_designRadio).click(function(){
		var _value = $(this).parent().find('>input').val();
		$(_designRadio).children().removeClass('checked');
		$(this).children().addClass('checked');
		console.log(_value);
	});
});




