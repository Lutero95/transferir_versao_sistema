/**
 * 
 */

window.addEvent('domready', function()  
{     
    var slidetime = 3000; //3 seconds between changes  
    var periodical;  
    var activeItem;  
    var started = 0;  
      
    $('container').removeClass('noscript'); //remove noscript class, for obvious reasons  
      
    //get all the slideshow items  
    var items = $('container').getElements('.slideshow-item');  
    var itemsLength = items.length;  
      
    for(var i=0; i<itemsLength; i++)  
    {  
        //if we're not on the first one, hide it  
        if(i != itemsLength-1)  
        {  
            items[i].fade('hide');  
        }  
        else  
        {  
            activeItem = items[i].id;  
        }  
    }  
  
    function rotate()  
    {  
        if(started == 1)  
        {  
            //get the number we're dealing with (item-#) so that we can work out whether the # is the last item or not  
            var activeNumber = activeItem.split('-');  
            activeNumber = activeNumber[1];  
              
            if(activeNumber == itemsLength) //are we on the last item? if so start back at the beginning.  
            {  
                newItemNumber = 1;  
            }  
            else //otherwise just add 1 to the last active id  
            {  
                newItemNumber = (parseInt(activeNumber)+1);  
            }  
              
            $('item-'+newItemNumber).fade('in');  
            $(activeItem).fade('out');  
            activeItem = 'item-'+newItemNumber;  
        }  
        else  
        {  
            started = 1;  
        }  
          
        $clear(periodical); //clear the timer  
        periodical = rotate.periodical(slidetime); //repeat!  
    }  
  
    rotate();  
      
});  