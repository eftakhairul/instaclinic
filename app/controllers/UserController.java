package controllers;

import models.User;
import models.UserRole;
import play.mvc.*;

import java.util.Map;

public class UserController extends Controller {

    /**
     * Serve the patient list
     * route /patient
     */
    public static Result patientList() {
        Map<String, String> patients   = User.findByUserRole(UserRole.PATIENT);
        String totalpatients = "" + User.findCountByUserRole(UserRole.PATIENT);

        return ok(views.html.patientList.render(patients, Long.parseLong(totalpatients)));
    }

    public static Result workAsPatient(String id) {
        session("user_id", id);
        return redirect(routes.Application.index());
    }
}
