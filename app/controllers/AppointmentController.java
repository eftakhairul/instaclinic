package controllers;

import java.io.StringWriter;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.avaje.ebean.Ebean;

import play.*;
import play.data.Form;
import play.libs.Json;
import play.mvc.*;
import scala.util.parsing.json.JSON;
import models.*;
import views.html.*;

public class AppointmentController extends Controller 
{
	/**
	 * This result directly redirect to application home.
	 */
	public static Result GO_HOME = redirect(
	   routes.AppointmentController.list(0, "name", "asc", "")
	);
    
  public static Result index() {

      return GO_HOME;
  }
  
  /**
   * Display the paginated list of computers.
   *
   * @param page Current page number (starts from 0)
   * @param sortBy Column to be sorted
   * @param order Sort order (either asc or desc)
   * @param filter Filter applied on computer names
   */
  public static Result list(int page, String sortBy, String order, String filter) {
	  User user = null;
	  if(session().containsKey("user_id")) {
		  user = User.findById(Long.parseLong(session("user_id")));
	  }
	  if(user == null || user.getUserRole() == UserRole.DOCTOR) {
		  return redirect(routes.AuthenticationController.login());
	  }
      return ok(
          views.html.listAppointments.render(
              Appointment.page(user, page, 10, sortBy, order, filter),
              sortBy, order, filter
          )
      );
  }
  
  /**
   * Display the 'edit form' of a existing Computer.
   *
   * @param id Id of the computer to edit
   */
  public static Result edit(Long id) {
	  Appointment appointment = Appointment.find.byId(id);
      Form<Appointment> computerForm = form(Appointment.class).fill(
    		  appointment
      );
      return ok(
    		  views.html.editAppointment.render(id, computerForm, appointment)
      );
  }
  
  /**
   * Handle the 'edit form' submission 
   *
   * @param id Id of the computer to edit
   */
  public static Result update(Long id) {
  
      Form<Appointment> appointmentForm = form(Appointment.class).fill(Appointment.find.byId(id)).bindFromRequest();
      //int scheduleId = Integer.parseInt(appointmentForm.field("schedule").value());
      //Schedule schedule = Schedule.findById(scheduleId);
      
      if(appointmentForm.hasErrors()) {
    	  flash("error", appointmentForm.errors().toString());
          return badRequest(views.html.createAppointment.render(appointmentForm));
      }
      Appointment formAp = appointmentForm.get();
      System.out.println("Appointment ID From Form: "+formAp.getId());
      Appointment appointment = Appointment.find.byId(id);
      //appointment.setSchedule(schedule);
      //set start/end time
      appointment.setAppointmentDate(formAp.getAppointmentDate());
      appointment.setStartTime(formAp.getStartTime());
      appointment.setEndTime(formAp.getEndTime());
      //appointment.setDoctor(formAp.getDoctor());
      
      Room room = appointment.findAvailableRoom();
      if(room == null) {
    	  flash("error", "No Room available on this schedule");
    	  return badRequest(views.html.editAppointment.render(id, appointmentForm, appointment));
      }
      appointment.setRoom(room);
      
      User user = User.findById(Long.parseLong(session().get("user_id")));
      List<Appointment> appointments = Appointment.findByTime(appointment.getStartTime(), appointment.getEndTime());
      
      for (Appointment appointment2 : appointments) {
		if(appointment2.getUser().getId() == user.getId()) {
			flash("error", "You have another appointment overlapped with this schedule "+appointment2.getId());
	    	return badRequest(views.html.editAppointment.render(id, appointmentForm, appointment));
		}
      }
      Ebean.update(appointment);
      //appointment.update(id);
      
      flash("success", "Appointment has been updated");
      return GO_HOME;
  }
  
  /**
   * Display the 'new Appointment form'.
   */
  public static Result create() {
      Form<Appointment> appointmentForm = form(Appointment.class);
      return ok(
    		  views.html.createAppointment.render(appointmentForm)
      );
  }
  
  /**
   * Handle the 'new computer form' submission 
   */
  public static Result save() {
      Form<Appointment> appointmentForm = form(Appointment.class).bindFromRequest();
      //int scheduleId = Integer.parseInt(appointmentForm.field("schedule").value());
      //Schedule schedule = Schedule.findById(scheduleId);
      
      if(appointmentForm.hasErrors()) {
    	  flash("error", appointmentForm.errors().toString());
          return badRequest(views.html.createAppointment.render(appointmentForm));
      }
      
      Appointment appointment = appointmentForm.get();
      System.out.println("Doctor Id : "+appointmentForm.field("doctor_id").value());
      User doctor = User.findById(Long.parseLong(appointmentForm.field("doctor_id").value()));
      appointment.setDoctor(doctor);
      
      //Appointment appointment = new Appointment(new Room());
      
      //appointment.setAppointmentDate(new Date(appointmentForm.field("appointmentDate").value()));
      
      //String parts = appointmentForm.field("appointmentTime").value();
      //appointment.setAppointmentDate(new Date(appointmentForm.field("appointmentDate").value()));
      
      //TODO check room availability
      Room room = appointment.findAvailableRoom();
      if(room == null) {
    	  flash("error", "No Room available on this schedule");
    	  return badRequest(views.html.createAppointment.render(appointmentForm));
      }
      appointment.setRoom(room);
      //System.out.println("room : "+room.getId());
      
      User user = User.findById(Long.parseLong(session().get("user_id")));
      
      List<Appointment> appointments = Appointment.findByTime(appointment.getStartTime(), appointment.getEndTime());
      System.out.println("appointments foound: "+appointments.size()+" "+appointment.getStartTime());
      for (Appointment appointment2 : appointments) {
		if(appointment2.getUser().getId() == user.getId()) {
			flash("error", "You have another appointment overlapped with this schedule");
	    	return badRequest(views.html.createAppointment.render(appointmentForm));
		}
      }
      
      appointment.setUser(user);
      //System.out.println("User : "+user.getUsername());
      
      appointment.save();
      
      System.out.println("New Appointment created : "+appointment.getId());
      
      //System.out.print(scheduleId);
      /*if(appointmentForm.hasErrors()) {
    	  //flash("error", scheduleForm.errorsAsJson());
          return badRequest(views.html.createAppointment.render(appointmentForm));
      }
      appointmentForm.get().save();*/
      
      flash("success", "Appointment has been created successfully");
      return GO_HOME;
  }
  
  /**
   * Handle computer deletion
   */
  public static Result delete(Long id) {
	  Appointment.find.ref(id).delete();
      flash("success", "Computer has been deleted");
      return GO_HOME;
  }
  
  public static Result getFilteredSchedules(String meetingType, int doctorId,String date)
  {
	  Map<String,String> result;
	  Date filterDate = new Date(date);
	  if(doctorId != 0){
		  result = Schedule.findByDoctorAndType(User.findById((long)(doctorId)), MeetingType.valueOf(meetingType), filterDate);  
	  }
	  else {
	  //System.out.println(meetingType);
		  result = Schedule.findByType(MeetingType.valueOf(meetingType), filterDate);  
	  }
	  //StringWriter out = new StringWriter();
	  String jsonText = Json.toJson(result).toString();
	  return ok(jsonText);
  }
  
}