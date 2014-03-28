package controllers;

import com.avaje.ebean.Ebean;
import models.Appointment;
import models.Payment;
import play.data.DynamicForm;
import play.data.Form;
import play.mvc.*;
import views.html.*;

import java.util.HashMap;
import java.util.Map;

public class PaymentController extends Controller {

    static Form<Payment> paymentForm = form(Payment.class);

    public static Result index() {

        Map<String, Appointment> appointments  = Appointment.findByUserId(Long.parseLong(session("user_id")));
        String tolal                           = "" + Appointment.findCountByUserId(Long.parseLong(session("user_id")));

        return ok(views.html.cart.render(appointments, paymentForm, Long.parseLong(tolal)));
    }

    public static Result processPayment() {
        Map<String, Appointment> appointments  = Appointment.findByUserId(Long.parseLong(session("user_id")));
        String tolal                           = "" + Appointment.findCountByUserId(Long.parseLong(session("user_id")));
        Form<Payment> filledForm               = paymentForm.bindFromRequest();
        DynamicForm nonModelFormData           = form().bindFromRequest();

        if(filledForm.hasErrors()) {

            return badRequest(views.html.cart.render(appointments, filledForm, Long.parseLong(tolal)));
        } else {
            //Inset payment
            Payment newPayment = filledForm.get();
            newPayment.setAmount(Long.parseLong(nonModelFormData.get("totalamount")));
            Ebean.save(newPayment);

            //Update appointment table with corresponding payment id
            final Map<String, String[]> values = request().body().asFormUrlEncoded();
            for (String appointment_id: values.get("appointment_id")) {
                Appointment appointment = Appointment.find.ref(Long.parseLong(appointment_id));
                appointment.setPayment(newPayment);
                appointment.update();
            }

            flash("success", "Payment is done successfully.");
            return redirect(routes.ApplicationController.index());
        }
    }
}
