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

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Entity
public class Appointment extends Model 
{
	@Id
	private Long id;
	
	@OneToOne(cascade=CascadeType.ALL)
	@PrimaryKeyJoinColumn
	@Constraints.Required
	private Schedule schedule;
	
	@ManyToOne(cascade=CascadeType.ALL)
	@PrimaryKeyJoinColumn
	private Room room;
	
	//The patient
	@ManyToOne(cascade=CascadeType.ALL)
	@PrimaryKeyJoinColumn
	private User user;
	
	@ManyToOne(cascade=CascadeType.ALL)
	@JoinColumn(name = "payment_id")
	private Payment payment;

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
	
	public Payment getPayment()
	{
		return this.payment;
	}

	public void setPayment(Payment payment)
	{
		this.payment = payment;
	}
	
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
    public static Page<Appointment> page(User user, int page, int pageSize, String sortBy, String order, String filter) {

        return find.where()
        		   .eq("user", user)
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
                                             .eq("user_id", id)
                                             .isNull("payment_id")
                                             .findList();

        LinkedHashMap<String, Appointment> options = new LinkedHashMap<String, Appointment>();

        for(Appointment appointment: appointments) {
            options.put(appointment.getId().toString(), appointment);
        }

        return options;
    }

    public static int findCountByUserId(Long id) {

        return find.where().eq("user_id", id).isNull("payment_id").findRowCount();
    }
    
    public static Room findAvailableRoom(Schedule schedule)
    {
    	List<Room> rooms = Room.getAllRooms();
    	List<Appointment> appointments = findByTime(schedule.getStartTime(), schedule.getEndTime());
    	
    	for (Room room : rooms) {
			if(isRoomAvailable(room, appointments)) {
				return room;
			}
		}
    	return null;
    }
    
    public static boolean isRoomAvailable(Room room, List<Appointment> appointments)
    {
    	for (Appointment appointment : appointments) {
			if(appointment.getRoom() == room) {
				return false;
			}
		}
    	return true;
    }
    
    public static List<Appointment> findByTime(Date start, Date end)
    {
    	ArrayList<Appointment> returnList = new ArrayList<Appointment>();
    	List<Appointment> appointments = find.all();
    	for (Appointment appointment : appointments) {
			Schedule schedule = appointment.getSchedule();
    		if(schedule.getStartTime().getTime() >= start.getTime() && schedule.getStartTime().getTime() < end.getTime()) {
				//System.out.println("inside start matching "+schedule.getStartTime().getTime() + " "+start.getTime());
    			returnList.add(appointment);
			}
			else if(schedule.getEndTime().getTime() > start.getTime() && schedule.getEndTime().getTime() <= end.getTime()) {
				//System.out.println("inside end matching");
				returnList.add(appointment);
			}
		}
    	return returnList;
    }
}