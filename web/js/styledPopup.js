/**
 * Created by IntelliJ IDEA.
 * User: Nagy Gergely
 * Date: 2012.06.23.
 * Time: 23:47
 * To change this template use File | Settings | File Templates.
 */
(function($){
    $.fn.styledpopup = function(){
        return this.each(function(){
            var obj = $(this)
//            obj.find('.show').click(function() { //onclick event, 'list' fadein
                obj.find('.show').fadeIn(400).css("display","inline-block");;

                $(document).keyup(function(event) { //keypress event, fadeout on 'escape'
                    if(event.keyCode == 27) {
                        obj.find('.show').fadeOut(400);
                    }
                });

                obj.find('.show').hover(function(){ },
                    function(){
                        $(this).fadeOut(400);
                    });
                return false;
//            });
        });
    };
})(jQuery);
