package models;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.validation.Constraint;

import play.db.ebean.Model;

@Entity
public class Payment extends Model 
{
	@Id
	private int id;

	private String creditCardNo;

    private double amount;
	
	//@OneToOne(mappedBy="payment")
	//private Appointment appointment;
	
	public Payment() {
		
	}
	
	public int getId()
	{
		return this.id;
	}

    public String getCreditCardNo() {
        return creditCardNo;
    }

    public void setCreditCardNo(String creditCardNo) {
        this.creditCardNo = creditCardNo;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }
}
