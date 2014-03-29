package controllers;


import com.avaje.ebean.Ebean;
import models.User;
import models.UserRole;
import play.data.Form;
import play.mvc.*;
import views.html.createDoctor;
import java.util.Map;

public class UserController extends Controller {

    /**
     * Serve the patient list
     * route /user/patientList
     */
    public static Result patientList() {
        Map<String, String> patients   = User.findByUserRole(UserRole.PATIENT);
        String totalpatients = "" + User.findCountByUserRole(UserRole.PATIENT);

        return ok(views.html.patientList.render(patients, Long.parseLong(totalpatients)));
    }

    /**
     * Give access the user to work as patient
     * route /user/:id/workAsPatient
     */
    public static Result workAsPatient(String id) {
        //Clear session
        session().clear();

        session("user_id", id);
        User user = User.find.ref(Long.parseLong(id));
        session("role", user.getUserRole().name());

        return redirect(routes.ApplicationController.index());
    }

    /**
     * Serve the doctor list
     * route /user/doctorList
     */
    public static Result doctorList() {
        Map<String, String> doctors   = User.findByUserRole(UserRole.DOCTOR);
        String totaldoctors           = "" + User.findCountByUserRole(UserRole.DOCTOR);

        return ok(views.html.doctorList.render(doctors, Long.parseLong(totaldoctors)));
    }

    /**
     * Create New Doctor
     * route /user/doctor/new
     */
    public static Result createDoctor() {
        Form<User> userForm = form(User.class);
        return ok(createDoctor.render(userForm));
    }

    /**
     * Process Doctor creation form
     * route /user/doctor/save
     */
    public static Result saveDoctor() {

        Form<User> filledForm = form(User.class).bindFromRequest();

        // Check repeated password
        if(!filledForm.field("password").valueOr("").isEmpty()) {
            if(!filledForm.field("password").valueOr("").equals(filledForm.field("repeatPassword").value())) {
                filledForm.reject("repeatPassword", "Password don't match");
            }
        }

        // Check if the username is valid
        if(!filledForm.hasErrors()) {
            if(filledForm.get().getUsername().equals("admin") || filledForm.get().getUsername().equals("guest")) {
                filledForm.reject("username", "This username is already taken");
            }
        }

        //Save new doctor to user table
        if(filledForm.hasErrors()) {
            return badRequest(views.html.createDoctor.render(filledForm));
        } else {
            User newUser = filledForm.get();
            newUser.setPassword(newUser.getPassword());
            newUser.setUserRole(UserRole.DOCTOR);
            Ebean.save(newUser);

            flash("success", "Doctor has been added successful.");
            return redirect(routes.UserController.doctorList());
        }
    }

    /**
     * Give access the user to work as patient
     * route /user/:id/convertRole
     */
    public static Result convertRole(String id) {
        //Clear session
        session().clear();

        session("user_id", id);
        User user = User.find.ref(Long.parseLong(id));
        session("role", user.getUserRole().name());

        return redirect(routes.ApplicationController.index());
    }
}
