$(document).ready(function(){
	setDoctorVisibility($("#meetingType").val());
	$("#meetingType").change(function(){
		filterByMeeting($(this).val());
	});
	$("#doctor_id").change(function(){
		filterByDoctor($(this).val());
	});
	
	$( "#selectable" ).selectable();
	
	$("#appointmentDate").datepicker();
	
	$("#schedule").change(function(){
		setStartEnd($(this).val());
	});
	
	$("#appointmentDate").change(function(){
		updateScheduleFilter();
	});
	updateScheduleFilter();
});

function updateScheduleFilter()
{
	if($("#appointmentDate").val() == "") {
		return;
	}
	if($("#meetingType").val() == "REGULAR") {
		filterByMeeting($("#meetingType").val());
	}
	else {
		filterByDoctor($("#doctor_id").val());
	}
}


function filterByMeeting(meetingType) {
	
	setDoctorVisibility(meetingType);
	
	$.get("filter/"+meetingType+"?date="+encodeURIComponent($("#appointmentDate").val()), function(data){
		showSchedules(data);
	});
}

function setDoctorVisibility(meetingType)
{
	if(meetingType == "YEARLY") {
		$("#doctor").parent().parent().show();
	}
	else {
		$("#doctor").parent().parent().hide();
	}
}

function filterByDoctor(doctorId) {
	$.get("filter/"+$("#meetingType").val()+"?doctorId="+doctorId+"&date="+encodeURIComponent($("#appointmentDate").val()), function(data){
		showSchedules(data);
	});
}

function showSchedules(data) 
{
	var types = $.parseJSON(data);
	$("#schedule").empty();
	$.each(types, function(index, value){
		var parts = value.split("-");
		//var group = $("<optgroup  label='"+parts[0]+"'>");
		//var start = getIntValue(parts[1]);
		//var end = getIntValue(parts[2]);
		//var interval = 
		//while(start < end) {
			var option = $('<option></option>').attr("value", index).text(value);
		//}
		//group.append(option);
		$("#schedule").append(option);
	});
	setStartEnd($("#schedule").val());
}

function getIntValue(time) {
	var parts = time.split(":");
	var d = new Date(0,0,0,parts[0],parts[1],0);
	var finalValue = ((d.getHours() - 8)*60) + d.getMinutes();
	return finalValue;
}

function formatValue(value) {
	var hour = value/60;
	hour = Math.floor(8+hour);
	var minute = value%60;
	return pad(hour)+":"+pad(minute);
}

function setStartEnd(time) {
	var parts = time.split("-");
	$("#startTime").val(parts[2]);
	$("#endTime").val(parts[3]);
	$("#doctor_id").val(parts[0]);
	//alert(parts[1] + " " + parts[2] + " "+time);
}