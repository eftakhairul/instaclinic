package controllers;


import com.avaje.ebean.Ebean;
import models.User;
import models.UserRole;
import play.*;
import play.api.libs.Crypto;
import play.data.Form;
import play.mvc.*;
import views.html.registration.*;

public class Registration extends Controller {

    static Form<User> RegistrationFrom  = form(User.class);


    /**
     * Display a blank form.
     * route /registration
     */
    public static Result index() {
        return ok(views.html.registration.render(RegistrationFrom));
    }

    /**
     * Handle the form submission.
     * route /save
     */
    public static Result submit() {

        Form<User> filledForm = RegistrationFrom.bindFromRequest();

        /*
        // Check accept conditions
        if(!"true".equals(filledForm.field("accept").value())) {
            filledForm.reject("accept", "You must accept the terms and conditions");
        }
        */

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

        if(filledForm.hasErrors()) {
            System.out.println(filledForm.errors());
            return badRequest(views.html.registration.render(filledForm));
        } else {
            User newUser = filledForm.get();
            newUser.setPassword(newUser.getPassword());
            newUser.setUserRole(UserRole.PATIENT);
            Ebean.save(newUser);
            return redirect(routes.Authentication.login());
        }
    }
}
