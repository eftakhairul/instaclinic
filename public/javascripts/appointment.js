$(document).ready(function(){
	$("#doctor").parent().parent().hide();
	$("#meetingType").change(function(){
		filterByMeeting($(this).val());
	});
	$("#doctor").change(function(){
		filterByDoctor($(this).val());
	});
});

function filterByMeeting(meetingType) {
	
	if(meetingType == "YEARLY") {
		$("#doctor").parent().parent().show();
	}
	
	$.get("filter/"+meetingType, function(data){
		showSchedules(data);
	});
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