package controllers;

import com.avaje.ebean.Ebean;

import models.*;
import models.Configuration;
import play.api.libs.Crypto;
import play.data.Form;
import play.mvc.*;
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
    public static Result configureSetting() {
        Long id = Long.parseLong("1");

        if(Configuration.find.byId(id) == null) {
            Configuration configuration = new Configuration();
            configuration.setLong_meet("60");
            configuration.setShort_meet("20");
            Ebean.save(configuration);
        }

        return ok(listConfigurationSetting.render(Configuration.find.byId(id)));
    }


    /**
     * Display the 'edit form' of a configuration
     *
     * @route /configure/edit
     */
    public static Result editConfiguration() {
        Long id = Long.parseLong("1");

        if(Configuration.find.byId(id) == null) {
            Configuration configuration = new Configuration();
            configuration.setLong_meet("60");
            configuration.setShort_meet("20");
            Ebean.save(configuration);
        }

        Form<Configuration> configurationForm = form(Configuration.class).fill(Configuration.find.byId(id));

        return ok(views.html.editConfigure.render(configurationForm));
    }

    /**
     * Process configuration file
     *
     * @route /configure/update
     */
    public static Result updateConfiguration() {
        Form<Configuration> configurationForm = form(Configuration.class).bindFromRequest();
        if (configurationForm.hasErrors()) {
            return badRequest(views.html.editConfigure.render(configurationForm));
        }

        Configuration filledConfiguration = configurationForm.get();
        if(filledConfiguration != null) {
            Configuration configuration = Configuration.find.byId(Long.parseLong("1"));
            configuration.setLong_meet(filledConfiguration.getLong_meet());
            configuration.setShort_meet(filledConfiguration.getShort_meet());
            System.out.println(configuration.getId());
            Ebean.update(configuration);

            flash("success", "Configuration setting has been updated");
            return redirect(routes.ApplicationController.configureSetting());
        }
        flash("error", "Couldn't update this setting. Please try later");
        return badRequest(views.html.editConfigure.render(configurationForm));
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
