package controllers;

import models.Appointment;
import models.Payment;
import play.data.Form;
import play.mvc.*;

import java.util.Map;

public class PaymentProcess extends Controller {

    public static Result index() {
        Form<Payment> paymentForm              = form(Payment.class);
        Map<String, Appointment> appointments  = Appointment.findByUserId(Long.parseLong("1"));
        return ok(views.html.checkout.render(appointments, paymentForm, Long.parseLong("2")));
    }
}
