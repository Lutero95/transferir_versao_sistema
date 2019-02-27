/**
 * 
 */

 $a = jQuery.noConflict();
        $a(document).ready(function() {
            var heightWindow = $a(window).height();
            var heightBody   = document.body.clientHeight;
            if(heightWindow > heightBody){
                $a("#footer").css('position', 'fixed');
                $a("#footer").css('bottom', '0');
            }                
        });
        
