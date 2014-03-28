package models;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;

import com.avaje.ebean.Page;

import play.data.format.Formats;
import play.data.validation.Constraints;
import play.db.ebean.Model;
import play.db.ebean.Model.Finder;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Entity
public class Appointment extends Model {
	@Id
	private Long id;
	
	@Formats.DateTime(pattern="MM/dd/yyyy")
	@Constraints.Required
	private Date appointmentDate;
	
	@Formats.DateTime(pattern="HH:mm")
	@Constraints.Required
	private Date startTime;
	
	@Formats.DateTime(pattern="HH:mm")
	@Constraints.Required
	private Date endTime;
	
	
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
    
    private MeetingType meetingType;

    public Appointment() {
        this.create_date = new Date();
    }
	
	public Appointment(Room room) {
		//this.schedule    = schedule;
		this.room        = room;
        this.create_date = new Date();
	}
	
	public Long getId() {
        return this.id;
	}
	
	public Room getRoom() {
        return this.room;
	}
	
	public void setRoom(Room room) {
        this.room = room;
	}
	
	public User getUser() {
        return this.user;
	}
	
	public void setUser(User user) {
        this.user = user;
	}

    public Date getCreateDate() {
        return this.create_date;
    }
	
	public Payment getPayment() {
        return this.payment;
	}

	public void setPayment(Payment payment) {
        this.payment = payment;
	}
	
	public Date getStartTime() {
        return this.startTime;
	}
	
	public void setStartTime(Date startTime) {
        this.startTime = startTime;
	}
	
	public Date getEndTime() {
        return this.endTime;
	}
	
	public void setEndTime(Date endTime) {
        this.endTime = endTime;
	}
	
	public Date getAppointmentDate() {
        return this.appointmentDate;
	}
	
	public void setAppointmentDate(Date appointmentDate) {
        this.appointmentDate = appointmentDate;
	}
	
	public void setMeetingType(MeetingType meetingType) {
        this.meetingType = meetingType;
	}
	
	public MeetingType getMeetingType() {
        return this.meetingType;
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
    
    public Room findAvailableRoom()
    {
    	List<Room> rooms = Room.getAllRooms();
    	List<Appointment> appointments = findByTime(this.getStartTime(), this.getEndTime());
    	
    	for (Room room : rooms) {
    		System.out.println("Checking for room "+room.getId());
			if(isRoomAvailable(room, appointments)) {
				return room;
			}
		}
    	return null;
    }
    
    public static boolean isRoomAvailable(Room room, List<Appointment> appointments)
    {
    	System.out.println("Number of Appointment in that time frame: "+appointments.size());
    	for (Appointment appointment : appointments) {
			if(appointment.getRoom().getId() == room.getId()) {
				System.out.println("Room not available "+room.getId());
				return false;
			}
		}
    	System.out.println("Room available : "+room.getId());
    	return true;
    }
    
    public static List<Appointment> findByTime(Date start, Date end)
    {
    	ArrayList<Appointment> returnList = new ArrayList<Appointment>();
    	List<Appointment> appointments = find.all();
    	for (Appointment appointment : appointments) {
			//Schedule schedule = appointment.getSchedule();
    		if(appointment.getStartTime().getTime() >= start.getTime() && appointment.getStartTime().getTime() < end.getTime()) {
				//System.out.println("inside start matching "+schedule.getStartTime().getTime() + " "+start.getTime());
    			returnList.add(appointment);
			}
			else if(appointment.getEndTime().getTime() > start.getTime() && appointment.getEndTime().getTime() <= end.getTime()) {
				//System.out.println("inside end matching");
				returnList.add(appointment);
			}
		}
    	return returnList;
    }
    
    public static boolean findBySchedule(Schedule schedule)
    {
    	//TODO
    	/*List<Appointment> appointments = find.all();
    	for (Appointment appointment : appointments) {
    		Schedule schedule2 = appointment.getSchedule();
    		if(schedule.getId() == schedule2.getId()) {
    			return true;
    		}
    	}*/
    	return false;
    }
}