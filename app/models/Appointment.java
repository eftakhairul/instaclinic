package models;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;

import com.avaje.ebean.Page;

import play.data.validation.Constraints;
import play.db.ebean.Model;
import play.db.ebean.Model.Finder;

@Entity
public class Appointment extends Model 
{
	@Id
	private int id;
	
	@ManyToOne(cascade=CascadeType.ALL)
	@PrimaryKeyJoinColumn
	//@Constraints.Required
	private Schedule schedule;
	
	@ManyToOne(cascade=CascadeType.ALL)
	@PrimaryKeyJoinColumn
	private Room room;
	
	//The patient
	@ManyToOne(cascade=CascadeType.ALL)
	@PrimaryKeyJoinColumn
	private User user;
	
	//@OneToOne
	//@JoinColumn(name = "payment_id")
	//private Payment payment;
	
	private MeetingType meetingType;
	
	
	public Appointment(Room room, Schedule schedule)
	{
		this.schedule = schedule;
		this.room     = room;
	}
	
	public int getId()
	{
		return id;
	}
	
	public Room getRoom()
	{
		return room;
	}
	
	public void setRoom(Room room)
	{
		this.room = room;
	}
	
	public Schedule getSchedule()
	{
		return schedule;
	}
	
	public void setSchedule(Schedule schedule)
	{
		this.schedule = schedule;
	}
	
	public User getUser()
	{
		return this.user;
	}
	
	public void setUser(User user)
	{
		this.user = user;
	}
	
	/*public Payment getPayment()
	{
		return this.payment;
	}
	
	public void setPayment(Payment payment)
	{
		this.payment = payment;
	}*/
	
	/**
     * Generic query helper for entity Schedule with id Long
     */
    public static Finder<Long,Appointment> find = new Finder<Long,Appointment>(Long.class, Appointment.class); 
    
    /**
     * Return a page of Schedule
     *
     * @param page Page to display
     * @param pageSize Number of computers per page
     * @param sortBy Computer property used for sorting
     * @param order Sort order (either or asc or desc)
     * @param filter Filter applied on the name column
     */
    public static Page<Appointment> page(int page, int pageSize, String sortBy, String order, String filter) {
        return 
            find.where()
                .findPagingList(pageSize)
                .getPage(page);
    }
}
