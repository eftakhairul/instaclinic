package models;

import javax.persistence.*;
import javax.validation.Constraint;

import play.data.validation.Constraints;
import play.db.ebean.Model;

import java.util.Date;

@Entity
public class Payment extends Model 
{
	@Id
    @GeneratedValue(strategy = javax.persistence.GenerationType.AUTO)
	private int id;

    @Constraints.Required
    @Constraints.MinLength(16)
	private String creditCardNo;

    @Constraints.Required
    private String fullName;

    @Constraints.Required
    private double amount;

    private Date create_date;
	
	public Payment() {
        this.create_date = new Date();
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

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public Date getCreate_date() {
        return create_date;
    }

    public void setCreate_date(Date create_date) {
        this.create_date = create_date;
    }
}
