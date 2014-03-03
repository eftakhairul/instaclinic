package controllers;

import com.avaje.ebean.Ebean;
import models.Appointment;
import models.Payment;
import play.data.Form;
import play.mvc.*;
import views.html.*;

import java.util.Map;

public class PaymentController extends Controller {

    static Form<Payment> paymentForm = form(Payment.class);

    public static Result index() {

        Map<String, Appointment> appointments  = Appointment.findByUserId(Long.parseLong(session("user_id")));
        String tolal                           = "" + Appointment.findCountByUserId(Long.parseLong(session("user_id")));

        return ok(views.html.checkout.render(appointments, paymentForm, Long.parseLong(tolal)));
    }

    public static Result processPayment() {
        Map<String, Appointment> appointments  = Appointment.findByUserId(Long.parseLong(session("user_id")));
        String tolal                           = "" + Appointment.findCountByUserId(Long.parseLong(session("user_id")));
        Form<Payment> filledForm               = paymentForm.bindFromRequest();

        // Check amount zero or less
        if(!filledForm.field("amount").valueOr("").isEmpty() || filledForm.get().getAmount() < 1) {
                filledForm.reject("amount", "Please insert proper amount");
        }

        if(filledForm.hasErrors()) {

            return badRequest(views.html.checkout.render(appointments, filledForm, Long.parseLong(tolal)));
        } else {
            Payment newPayment = filledForm.get();
            Ebean.save(newPayment);
            return redirect(routes.Application.index());
        }
    }
}
