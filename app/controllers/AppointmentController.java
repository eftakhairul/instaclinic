package controllers;

import java.io.StringWriter;
import java.util.Date;
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
    
  public static Result index() 
  {
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
      User user = User.findById(Long.parseLong(session("user_is")));
	  if(user == null || user.getUserRole() == UserRole.DOCTOR) {
		  return redirect(routes.Authentication.login());
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
      //Form<Appointment> appointmentForm = form(Appointment.class).bindFromRequest();
      
      Form<Appointment> appointmentForm = form(Appointment.class).bindFromRequest();
      int scheduleId = Integer.parseInt(appointmentForm.field("schedule").value());
      Schedule schedule = Schedule.findById(scheduleId);
      
      Appointment appointment = Appointment.find.byId(id);
      appointment.setSchedule(schedule);
      appointment.update();
      
      /*if(appointmentForm.hasErrors()) {
    	  //computerForm.errors()[0].
          return badRequest(views.html.editAppointment.render(id, appointmentForm, Appointment.find.byId(id)));
      }*/
      //appointmentForm.get().update(id);
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
      int scheduleId = Integer.parseInt(appointmentForm.field("schedule").value());
      Schedule schedule = Schedule.findById(scheduleId);
      
      Appointment appointment = new Appointment(new Room(), schedule);
      
      //TODO check room availability
      Room room = Room.getDemo();
      appointment.setRoom(room);
      System.out.println("room : "+room.getId());
      
      User user = User.findById(Long.parseLong(session().get("user_is")));
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
      
      flash("success", "Computer " + appointment.getId() + " has been created");
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
  
  public static Result getFilteredSchedules(String meetingType, int doctorId)
  {
	  Map<String,String> result;
	  if(doctorId != 0){
		  result = Schedule.findByDoctorAndType(User.findById((long)(doctorId)), MeetingType.valueOf(meetingType));  
	  }
	  else {
		  result = Schedule.findByType(MeetingType.valueOf(meetingType));  
	  }
	  StringWriter out = new StringWriter();
	  String jsonText = Json.toJson(result).toString();
	  return ok(jsonText);
  }
  
}