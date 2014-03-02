package controllers;

import models.Payment;
import play.data.Form;
import play.mvc.*;

public class PaymentController extends Controller {

    static Form<Payment> paymentForm  = form(Payment.class);


    /**
     * Serve the checkout Page
     *
     * route /checkout
     */
    public static Result checkout() {
        return ok();
    }

    /**
     * Process the payment
     *
     * route /processPayment
     */
    public static Result processPayment() {
        return ok();
    }
}
