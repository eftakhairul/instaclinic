package controllers;

import models.Appointment;
import models.Payment;
import play.data.Form;
import play.mvc.*;
import views.html.*;

import java.util.Map;

public class PaymentController extends Controller {

    static Form<Payment> paymentForm = form(Payment.class);

    public static Result index() {

        Map<String, Appointment> appointments  = Appointment.findByUserId(Long.parseLong("1"));
        Long total;
        return ok(views.html.checkout.render(appointments, paymentForm, Long.parseLong("2")));
    }

    public static Result processPayment() {
        return ok();

    }
}
