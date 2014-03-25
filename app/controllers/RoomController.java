package controllers;

import models.*;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;
import views.html.createSchedule;
import views.html.editSchedule;
import models.Room;
import views.html.*;


import java.util.Date;
import java.util.List;

import static com.avaje.ebean.Ebean.validate;

public class RoomController extends Controller
{
	/**
	 * This result directly redirect to application home.
	 */
	public static Result GO_HOME = redirect(
	    routes.ScheduleController.list(0, "name", "asc", "")
	);


  /**
   * Display the paginated list of rooms.
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
          listRooms.render(Room.page(page, 10, sortBy, order, filter), sortBy, order, filter)
      );
  }

  /**
   * Display the 'new computer form'.
   */
  public static Result create() {
      Form<Schedule> scheduleForm = form(Schedule.class);
      return ok(
    		  createSchedule.render(scheduleForm)
      );
  }

  /**
   * Handle the 'new computer form' submission
   */
  public static Result save() {
      Form<Room> roomForm = form(Room.class).bindFromRequest();

      if(roomForm.hasErrors()) {
    	  //flash("error", scheduleForm.errorsAsJson());
          return badRequest(createRoom.render(roomForm));
      }

      Room room = roomForm.get();

//      if(!validate(room)) {
//    	  return badRequest(createRoom.render(roomForm));
//      }

      room.save();
      flash("success", "Room has been created");

      return GO_HOME;
  }
  
  /**
   * Handle computer deletion
   */
  public static Result delete(Long id) {
//	  Schedule schedule = Schedule.find.ref(id);
//	  if(Appointment.findBySchedule(schedule)) {
//		  flash("error", "This schedule can't be deleted as somone got an appointment on this schedule");
//		  return GO_HOME;
//	  }
//	  schedule.delete();
//      flash("success", "Schedule has been deleted");
      return GO_HOME;
  }
}