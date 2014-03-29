package controllers;

import models.*;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;
import models.Room;
import views.html.*;



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
		  return redirect(routes.AuthenticationController.login());
	  }
	  return ok(
          listRooms.render(Room.page(page, 10, sortBy, order, filter), sortBy, order, filter)
      );
  }

  /**
   * Add Room to System
   *
   * @route /room/new
   */
  public static Result create() {
      Form<Room> roomForm = form(Room.class);
      return ok(createRoom.render(roomForm)
      );
  }

  /**
   * Process the form
   *
   * @route /room/save
   */
  public static Result save() {
      Form<Room> roomForm = form(Room.class).bindFromRequest();

      if(roomForm.hasErrors()) {
          return badRequest(createRoom.render(roomForm));
      }

      Room room = roomForm.get();
      room.save();
      flash("success", "Room has been created");
      return GO_HOME;
  }
  
  /**
   * Delete room
   *
   * @route /room/:id/delete
   */
  public static Result delete(Long id) {
	  Room room  = Room.find.ref(id);
	  room.delete();
      flash("success", "Room has been deleted successfully");
      return GO_HOME;
  }
}