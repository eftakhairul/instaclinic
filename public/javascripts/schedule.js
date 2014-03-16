$(document).ready(function(){
	$("#scheduleDate").datepicker();
	//$("#endTime").datetimepicker();
    $("#birthday").datepicker({changeMonth: true,
        changeYear: true,
        showButtonPanel: true,
        yearRange: '1950:2013'});
});

$(function() {
    var slider = $("#slider-range").slider({
        orientation: 'horizontal',
        range: true,
        min: 0,
        max: 540,
        values: [getIntValue($("#startTime").val()), getIntValue($("#endTime").val())],
        slide: function(event, ui) {
            var includeLeft = event.keyCode != $.ui.keyCode.RIGHT;
            var includeRight = event.keyCode != $.ui.keyCode.LEFT;
            
            var value = findNearest(ui.value);
            
            if (ui.value == ui.values[0]) {
                slider.slider('values', 0, value);
                $("#startTime").val(formatValue(slider.slider('values', 0)));
            }
            else {
                slider.slider('values', 1, value);
                $("#endTime").val(formatValue(slider.slider('values', 1)));
            }
            return false;
        },
        change: function(event, ui) { 
            //getHomeListings();
        }
    });
    
    function findNearest(value) {
        var remainder = value%10;
        if(remainder <5) {
        	return value-remainder;
        }
        else {
        	return value+10-remainder;
        }
    }
    
    function formatValue(value) {
    	var hour = value/60;
    	hour = Math.floor(8+hour);
    	var minute = value%60;
    	return pad(hour)+":"+pad(minute);
    }
    
    function pad(n) {
        return (n < 10) ? ("0" + n) : n;
    }
    
    function getIntValue(time) {
    	var parts = time.split(":");
    	var d = new Date(0,0,0,parts[0],parts[1],0);
    	var finalValue = ((d.getHours() - 8)*60) + d.getMinutes();
    	return finalValue;
    }
});