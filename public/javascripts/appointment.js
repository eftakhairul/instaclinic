$(document).ready(function(){
	setDoctorVisibility($("#meetingType").val());
	$("#meetingType").change(function(){
		filterByMeeting($(this).val());
	});
	$("#doctor").change(function(){
		filterByDoctor($(this).val());
	});
	
	$( "#selectable" ).selectable();
	
});

function filterByMeeting(meetingType) {
	
	setDoctorVisibility(meetingType);
	
	$.get("filter/"+meetingType, function(data){
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
	$.get("filter/"+$("#meetingType").val()+"?doctorId="+doctorId, function(data){
		showSchedules(data);
	});
}

function showSchedules(data) 
{
	var types = $.parseJSON(data);
	$("#schedule").empty();
	$.each(types, function(index, value){
		var option = $('<option></option>').attr("value", index).text(value);
		$("#schedule").append(option);
	});
}