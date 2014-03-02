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
	Schedule temp = Ebean.find(Schedule.class, 1);
	if(temp == null) {
		temp = new Schedule(new Date(1000), new Date());
	}
	return ok();
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
          views.html.listSchedules.render(
              Schedule.page(page, 10, sortBy, order, filter),
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
      long timeDiff = (schedule.getEndTime().getTime() - schedule.getStartTime().getTime())/60000;
      
      if(timeDiff == 20) {
    	  schedule.setMeetingType(MeetingType.REGULAR);
      }
      else if(timeDiff == 60) {
    	  schedule.setMeetingType(MeetingType.YEARLY);
      }
      else {
    	  flash("error", "schedule must be 20 minutes or 60 minutes in length");
          return badRequest(views.html.createSchedule.render(scheduleForm));
      }
      Long userid = Long.parseLong(session("user_is"));
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
  
}