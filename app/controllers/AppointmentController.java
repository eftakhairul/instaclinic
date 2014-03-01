package controllers;

import java.util.Date;

import com.avaje.ebean.Ebean;

import play.*;
import play.data.Form;
import play.mvc.*;
import models.Payment;
import models.Room;
import models.Schedule;
import models.Appointment;
import models.User;
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
      return ok(
          views.html.listAppointments.render(
              Appointment.page(page, 10, sortBy, order, filter),
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
      Form<Schedule> computerForm = form(Schedule.class).fill(
    		  Schedule.find.byId(id)
      );
      return ok(
    		  views.html.editSchedule.render(id, computerForm)
      );
  }
  
  /**
   * Handle the 'edit form' submission 
   *
   * @param id Id of the computer to edit
   */
  public static Result update(Long id) {
      Form<Schedule> computerForm = form(Schedule.class).bindFromRequest();
      if(computerForm.hasErrors()) {
    	  //computerForm.errors()[0].
          return badRequest(views.html.editSchedule.render(id, computerForm));
      }
      computerForm.get().update(id);
      flash("success", "Computer " + computerForm.get().getId() + " has been updated");
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
      //Payment payment = new Payment();
      //System.out.println(payment.getId());
      //appointment.setPayment(payment);
      //payment.setAppointment(appointment);
      appointment.save();
      
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
	  Schedule.find.ref(id).delete();
      flash("success", "Computer has been deleted");
      return GO_HOME;
  }
  
}