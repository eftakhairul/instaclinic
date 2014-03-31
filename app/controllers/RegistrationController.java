package controllers;


import com.avaje.ebean.Ebean;
import models.Patient;
import models.User;
import models.UserRole;
import models.HealthCard;
import play.*;
import play.api.libs.Crypto;
import play.data.DynamicForm;
import play.data.Form;
import play.mvc.*;
import views.html.registration.*;

public class RegistrationController extends Controller {

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

        Form<User> filledForm        = RegistrationFrom.bindFromRequest();
        DynamicForm nonModelFormData = form().bindFromRequest();

        // Check repeated password
        if(!filledForm.field("password").valueOr("").isEmpty()) {
            if(!filledForm.field("password").valueOr("").equals(filledForm.field("repeatPassword").value())) {
                filledForm.reject("repeatPassword", "Password don't match");
            }
        }

        // Check if the username is valid
        if(!filledForm.hasErrors()) {
            if(filledForm.get().getUsername().equals("admin")
               || filledForm.get().getUsername().equals("guest")
               || User.checkUserNameAvilability(filledForm.get().getUsername())) {

                flash("error", "This username is already taken");
                filledForm.reject("username", "This username is already taken");
            }
        }

        //Checking and verifying health card
        String HealthCardNumber = nonModelFormData.get("health_card_no");

        if(nonModelFormData.get("health_card_no").isEmpty()) {
            flash("error", "Please insert VALID Health Card Number");
            filledForm.reject("health_card_no", "Please insert VALID Health Card Number");
        } else {
            try {
                if(!HealthCard.veryfyHealthCard(Long.parseLong(HealthCardNumber))) {
                    flash("error", "Please insert VALID Health Card Number");
                    filledForm.reject("health_card_no", "Please insert VALID Health Card Number");
                }
            } catch (Exception e) {
                flash("error", "Please insert VALID Health Card Number");
                filledForm.reject("health_card_no", "Please insert VALID Health Card Number");
            }
        }

        if(filledForm.hasErrors()) {
            return badRequest(views.html.registration.render(filledForm));
        } else {
            User newUser = filledForm.get();
            newUser.setPassword(newUser.getPassword());
            newUser.setUserRole(UserRole.PATIENT);
            Ebean.save(newUser);

            try {
                Patient patient = new Patient();
                patient.setUser(newUser);
                patient.setBirthday(nonModelFormData.get("birthday"));
                patient.setHealth_card_no(HealthCardNumber);
                patient.setPhone_number(nonModelFormData.get("phone_number"));
                patient.setGender(nonModelFormData.get("gender"));
                patient.save();
            } catch (Exception e) {
                flash("error", "Some data is not save properly");
            }

            flash("success", "Registration is successful.");
            return redirect(routes.AuthenticationController.login());
        }
    }
}
