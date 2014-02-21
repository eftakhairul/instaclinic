package models;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;

import play.db.ebean.Model;

@Entity
public class Payment extends Model 
{
	@Id
	private int id;

	private double amount;
	
	@OneToOne(mappedBy="payment")
	private Appointment appointment;
}
