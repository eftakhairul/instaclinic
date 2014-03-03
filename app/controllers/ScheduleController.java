package controllers;

import java.util.Date;

import org.joda.time.Days;

import com.avaje.ebean.Ebean;

import play.*;
import play.data.Form;
import play.libs.Time;
import play.mvc.*;
import models.MeetingType;
import models.Schedule;
import models.User;
import models.UserRole;
import views.html.*;

public class ScheduleController extends Controller 
{
	/**
	 * This result directly redirect to application home.
	 */
	public static Result GO_HOME = redirect(
	    routes.ScheduleController.list(0, "name", "asc", "")
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
	  User user = null;
	  if(session().containsKey("user_id")) {
		  user = User.findById(Long.parseLong(session("user_id")));
	  }
	  
	  if(user == null || user.getUserRole() == UserRole.PATIENT) {
		  return redirect(routes.Authentication.login());
	  }
	  return ok(
          views.html.listSchedules.render(
              Schedule.page(user, page, 10, sortBy, order, filter),
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
      Schedule schedule = computerForm.get();
      if(schedule != null) {
    	  if(!validate(schedule)) {
        	  return badRequest(views.html.editSchedule.render(id, computerForm));
          }
    	  //System.out.println("new update time: "+schedule.getId());
    	  schedule.setId(Integer.parseInt(id.toString()));
    	  schedule.update();
    	  
    	  flash("success", "Computer " + schedule.getId() + " has been updated");
    	  return GO_HOME;
      }
      flash("error", "Couldn't save this schedule. Please try later");
      return badRequest(views.html.editSchedule.render(id, computerForm));
  }
  
  /**
   * Display the 'new computer form'.
   */
  public static Result create() {
      Form<Schedule> scheduleForm = form(Schedule.class);
      return ok(
    		  views.html.createSchedule.render(scheduleForm)
      );
  }
  
  /**
   * Handle the 'new computer form' submission 
   */
  public static Result save() {
      Form<Schedule> scheduleForm = form(Schedule.class).bindFromRequest();
      
      if(scheduleForm.hasErrors()) {
    	  //flash("error", scheduleForm.errorsAsJson());
          return badRequest(views.html.createSchedule.render(scheduleForm));
      }
      
      Schedule schedule = scheduleForm.get();
      
      if(!validate(schedule)) {
    	  return badRequest(views.html.createSchedule.render(scheduleForm));
      }
      //validation ends
      
      Long userid = Long.parseLong(session("user_id"));
      schedule.setUser(User.findById(userid));
      
      schedule.save();
      
      flash("success", "Computer " + scheduleForm.get().getId() + " has been created");
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
  
  public static boolean validate(Schedule schedule)
  {
	//Validations
      if(schedule.getEndTime().getTime() <= schedule.getStartTime().getTime()) {
    	  flash("error", "endTime cannot be previous time then start time");
    	  return false;
      }
      
      long timeDiff = (schedule.getEndTime().getTime() - schedule.getStartTime().getTime())/60000;
      
      if(timeDiff == 20) {
    	  schedule.setMeetingType(MeetingType.REGULAR);
      }
      else if(timeDiff == 60) {
    	  schedule.setMeetingType(MeetingType.YEARLY);
      }
      else {
    	  flash("error", "schedule must be 20 minutes or 60 minutes in length");
          return false;
      }
      
      if(schedule.getEndTime().getTime() < new Date().getTime() || schedule.getStartTime().getTime() < new Date().getTime()) {
    	  flash("error", "Time cannot be in past time");
    	  return false;
      }
      
      if(schedule.getStartTime().getHours() < 8) {
    	  flash("error", "Office time starts at 8:00 AM");
    	  return false;
      }
      
      if(schedule.getEndTime().getHours() > 17) {
    	  flash("error", "Office time ends at 5:00 PM");
    	  return false;
      }
      
      return true;
  }
  
}