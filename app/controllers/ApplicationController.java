package controllers;

import com.avaje.ebean.Ebean;

import models.Configuration;
import play.api.libs.Crypto;
import play.data.Form;
import play.mvc.*;
import models.Room;
import models.User;
import models.UserRole;
import views.html.*;

public class ApplicationController extends Controller {
  
	  public static Result index() 
	  {
		if(session().containsKey("user_is")) {
			User user = User.findById(Long.parseLong(session().get("user_is")));
			if(user != null) {
				return ok(index.render("Hello "+user.getUsername()));
			}
		}
	    return ok(index.render("Your new application is ready."));
	  }

    /**
     * Add Room to System
     *
     * @route /configure
     */
    public static Result newConfigure() {
        Form<Configuration> configurationForm = form(Configuration.class);
        return ok(createConfigure.render(configurationForm)
        );
    }


    /**
     * Add Room to System
     *
     * @route /configure/save
     */
    public static Result newConfigure() {
        Form<Configuration> configurationForm = form(Configuration.class);
        return ok(createConfigure.render(configurationForm)
        );
    }


    public static Result testData()
    {
    	/*for(int i=1; i <= 7; i++)
    	{
    		User newUser = new User();
            newUser.setPassword("hello123");
            newUser.setUsername("doctor"+i);
            newUser.setUserRole(UserRole.DOCTOR);
            Ebean.save(newUser);
    	}*/
    	
    	User newUser = new User();
        newUser.setPassword("hello123");
        newUser.setUsername("admin");
        newUser.setUserRole(UserRole.ADMIN);
        Ebean.save(newUser);
    	/*
    	for(int i=1; i<5; i++)
    	{
    		Room room = new Room();
    	    Ebean.save(room);
    	}*/
        
    	return ok("Data is saved");
    }
}
