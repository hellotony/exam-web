/**  * CSS3 答题卡翻页效果 jQuery Transit  */ 
$.fn.answerSheet = function(options) { 
	var defaults={ 
		mold:'card', 
	}; 
	var opts = $.extend({},defaults,options); 
	return $(this).each(function(){ 
		var obj = $(this).find('.card_cont'); 
		var _length = obj.length,_b = _length -1,_len = _length - 1, _cont = '.card_cont'; 
		for(var a = 1; a <= _length; a++){ 
			obj.eq(_b).css({'z-index':a});_b-=1; 
		} 
		$(this).show(); 
		// if(opts.mold == 'card'){ 
		// 	obj.find('ul li label').click(function(){ 
		// 		var _idx =  $(this).parents(_cont).attr("id"), 
		// 			_cards =  obj, 
		// 			_cardcont = $(this).parents(_cont); 
		// 		if(_idx == _len){ 
		// 			return; 
		// 		}else{ 
		// 			setTimeout(function(){ 
		// 				_cardcont.addClass('cardn'); 
		// 				var forwardTo = $("input[name='r-group-"+(_idx)+"']:checked").attr("forwardTo"); 
		// 				if(typeof forwardTo != 'undefined' && forwardTo != ''){ 
		// 					_idx = forwardTo; 
		// 				} 
		// 				setTimeout(function(){ 
		// 					_cards.eq(_idx).addClass('card1'); 
		// 					_cardcont.removeClass('card1'); 
		// 				},200); 
		// 			},100); 
		// 		} 
		// 	}); 
		// } 
	});
};
