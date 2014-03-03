$(document).ready(function(){
	$("#startTime").datetimepicker();
	$("#endTime").datetimepicker();
    $("#birthday").datepicker({changeMonth: true,
        changeYear: true,
        showButtonPanel: true,
        yearRange: '1950:2013'});
});