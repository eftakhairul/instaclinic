package controllers;

import play.*;
import play.data.Form;
import play.mvc.*;

import views.html.*;
import java.util.List;                                          Crypto.sign

import models.User;

public class Authentication extends Controller {

    public static User currentUser  = null;
    static Form<User> loginForm     = form(User.class);


    /*
    * Serve the Login Form
    * route: GET /login
    */
    public static Result login() {
        return ok(views.html.login.render(loginForm));
    }

    /*
    * Processing Login user
    *
    * route: Post /login
    */
    public static Result loginFormProcess() {

        Form<User> filledForm = loginForm.bindFromRequest();

        if(filledForm.hasErrors()) {
            return badRequest("Something is went wrong");
        } else {
            User c= filledForm.get();
            currentUser = User.verify(c.username, c.password);
            if (currentUser != null) {
                session("connected", currentUser.id.toString());
                return redirect(routes.Application.index());
            }

            return ok("bad man nanana");
        }
    }

    public static Result logout() {
        return ok(index.render("Your new application is ready."));

    }
}
