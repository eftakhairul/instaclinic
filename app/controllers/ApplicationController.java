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
        return ok(listConfigurationSetting.render(Configuration.getConfiguration()));
    }


    /**
     * Display the 'edit form' of a configuration
     *
     * @route /configure/edit
     */
    public static Result editConfiguration() {
        Form<Configuration> configurationForm = form(Configuration.class).fill(Configuration.getConfiguration());
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
            Configuration configuration = Configuration.getConfiguration();
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


    /**
     * Inserting some data and configuration for bootstrappng the project
     *
     * @route /testData
     */
    public static Result testData()
    {
    	User newUser = new User();
        newUser.setPassword("hello123");
        newUser.setUsername("admin");
        newUser.setUserRole(UserRole.ADMIN);
        Ebean.save(newUser);
        
    	return ok("Data is saved");
    }
}
