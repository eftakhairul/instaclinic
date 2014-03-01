package controllers;

import play.*;
import play.api.libs.Crypto;
import play.data.Form;
import play.mvc.*;

import views.html.*;
import java.util.List;

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
    * Processing authentication
    *
    * route: Post /login
    */
    public static Result loginFormProcess() {

        Form<User> filledForm = loginForm.bindFromRequest();

        if(filledForm.hasErrors()) {
            return badRequest("Something is went wrong");
        } else {
            User user   = filledForm.get();
            currentUser = User.verify(user.username, Crypto.sign(user.password));

            if (currentUser != null) {
                session().clear();
                session("user_is", currentUser.id.toString());
                return redirect(routes.Application.index());
            } else {
                filledForm.reject("password", "Password doesn't match, Please try again.");
            }

            return badRequest(views.html.login.render(filledForm));
        }
    }

    /*
    * Clear the session
    *
    * route: Get /logout
    */
    public static Result logout() {
        session().clear();
        return redirect(routes.Application.index());
    }
}