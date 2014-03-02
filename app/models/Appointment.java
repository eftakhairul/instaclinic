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

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Entity
public class Appointment extends Model 
{
	@Id
	private Long id;
	
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
	//private PaymentProcess payment;

    private Date create_date;

    public Appointment() {
        this.create_date = new Date();
    }
	
	public Appointment(Room room, Schedule schedule)
	{
		this.schedule    = schedule;
		this.room        = room;
        this.create_date = new Date();
	}
	
	public Long getId()
	{
		return this.id;
	}
	
	public Room getRoom()
	{
		return this.room;
	}
	
	public void setRoom(Room room)
	{
		this.room = room;
	}
	
	public Schedule getSchedule()
	{
		return this.schedule;
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

    public Date getCreateDate() {
        return this.create_date;
    }
	
	/*public PaymentProcess getPayment()
	{
		return this.payment;
	}
	
	public void setPayment(PaymentProcess payment)
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

        return find.where()
                   .findPagingList(pageSize)
                   .getPage(page);
    }

    /**
     * Return all user by a specific User Role
     *
     * @param id Filter by User id
     * return Map<String,Appointment>
     */
    public static Map<String,Appointment> findByUserId(Long id) {

        List<Appointment> appointments = find.where()
                                             .eq("user", id)
                                             .findList();

        LinkedHashMap<String, Appointment> options = new LinkedHashMap<String, Appointment>();

        for(Appointment appointment: appointments) {
            options.put(appointment.getId().toString(), appointment);
        }

        return options;
    }
}