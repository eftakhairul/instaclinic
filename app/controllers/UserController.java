package controllers;

import models.User;
import models.UserRole;
import play.mvc.*;

import java.util.Map;

public class UserController extends Controller {

    /**
     * Serve the patient list
     * route /user/patientList
     */
    public static Result patientList() {
        Map<String, String> patients   = User.findByUserRole(UserRole.PATIENT);
        String totalpatients = "" + User.findCountByUserRole(UserRole.PATIENT);

        return ok(views.html.patientList.render(patients, Long.parseLong(totalpatients)));
    }

    /**
     * Give access the user to work as patient
     * route /user/:id/workAsPatient
     */
    public static Result workAsPatient(String id) {
        //Clear session
        session().clear();

        session("user_id", id);
        User user = User.find.ref(Long.parseLong(id));
        session("role", user.getUserRole().name());

        return redirect(routes.Application.index());
    }

    /**
     * Serve the doctor list
     * route /user/doctorList
     */
    public static Result doctorList() {
        Map<String, String> doctors   = User.findByUserRole(UserRole.DOCTOR);
        String totaldoctors           = "" + User.findCountByUserRole(UserRole.DOCTOR);

        return ok(views.html.doctorList.render(doctors, Long.parseLong(totaldoctors)));
    }

    /**
     * Give access the user to work as patient
     * route /user/:id/convertRole
     */
    public static Result convertRole(String id) {
        //Clear session
        session().clear();

        session("user_id", id);
        User user = User.find.ref(Long.parseLong(id));
        session("role", user.getUserRole().name());

        return redirect(routes.Application.index());
    }
}
