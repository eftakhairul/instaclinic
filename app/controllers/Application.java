package controllers;

import com.avaje.ebean.Ebean;
import play.*;
import play.mvc.*;
import models.User;

import views.html.*;

public class Application extends Controller {
  
  public static Result index() {

    return ok(index.render("Your new application is ready."));
  }


    public static Result testData(){
        User newUser = new User();
        newUser.setPassword("hello");
        newUser.setUsername("rain");
        Ebean.save(newUser);

        return ok("Data is saved");
    }
  
}