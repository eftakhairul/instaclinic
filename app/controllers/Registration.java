package controllers;


import models.User;
import play.*;
import play.data.Form;
import play.mvc.*;
import views.html.*;

public class Registration extends Controller {

    static Form<User> RegistrationFrom  = form(User.class);


    public static Result index() {
        return ok(views.html.registration.render(RegistrationFrom));
    }

    public static Result save() {
        return ok(views.html.registration.render(RegistrationFrom));

    }
}
