package models;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

import com.avaje.ebean.Ebean;
import com.avaje.ebean.Page;
import com.avaje.ebean.annotation.EnumMapping;

import play.data.format.Formats;
import play.data.format.Formats.DateTime;
import play.data.validation.Constraints;
import play.db.ebean.*;


@Entity
public class Schedule extends Model 
{
	@Id
	private int id;
	
	@Formats.DateTime(pattern="MM/dd/yyyy HH:mm")
	@Constraints.Required
	private Date startTime;
	
	@Formats.DateTime(pattern="MM/dd/yyyy HH:mm")
	@Constraints.Required
	private Date endTime;
	
	@ManyToOne(cascade=CascadeType.ALL)
	private User user;
	
	private MeetingType meetingType;
	
	public Schedule(Date startTime, Date endTime)
	{
		this.startTime = startTime;
		this.endTime   = endTime;
	}
	
	public void setId(int id)
	{
		this.id = id;
	}
	
	public int getId()
	{
		return id;
	}
	
	public Date getStartTime()
	{
		return startTime;
	}
	
	public void setStartTime(Date startTime)
	{
		this.startTime =  startTime;
	}
	
	public Date getEndTime()
	{
		return endTime;
	}
	
	public void setEndTime(Date endTime)
	{
		this.endTime =  endTime;
	}
	
	public void setMeetingType(MeetingType meetingType)
	{
		this.meetingType = meetingType;
	}
	
	public MeetingType getMeetingType()
	{
		return this.meetingType;
	}
	
	public void setUser(User user)
	{
		this.user = user;
	}
	
	public User getUser()
	{
		return this.user;
	}
	
	public String toString()
	{
		return id+"";
	}
	
	/**
     * Generic query helper for entity Schedule with id Long
     */
    public static Finder<Long,Schedule> find = new Finder<Long,Schedule>(Long.class, Schedule.class); 
    
    /**
     * Return a page of Schedule
     *
     * @param page Page to display
     * @param pageSize Number of computers per page
     * @param sortBy Computer property used for sorting
     * @param order Sort order (either or asc or desc)
     * @param filter Filter applied on the name column
     */
    public static Page<Schedule> page(User user, int page, int pageSize, String sortBy, String order, String filter) {
        return 
            find.where()
                .eq("user", user)
                .findPagingList(pageSize)
                .getPage(page);
    }
    
    public static Schedule findById(int id) {
        return Ebean.find(Schedule.class, id);
    }
    
    public static Map<String,String> findByType(MeetingType type) {
    	List<Schedule> schedules = find.where()
        		.eq("meetingType", type)
        		.findList();
    	LinkedHashMap<String,String> options = new LinkedHashMap<String,String>();
        for(Schedule c: schedules) {
            options.put(c.toString(), c.startTime.toString() + " To " +c.endTime.toString());
        }
        return options;
    }
    
    public static Map<String,String> findByDoctor(User user) {
    	List<Schedule> schedules = find.where()
        		.eq("user", user)
        		.findList();
    	LinkedHashMap<String,String> options = new LinkedHashMap<String,String>();
        for(Schedule c: schedules) {
            options.put(c.toString(), c.startTime.toString() + " To " +c.endTime.toString());
        }
        return options;
    }
    
    public static Map<String,String> findByDoctorAndType(User user, MeetingType type) {
    	List<Schedule> schedules = find.where()
        		.eq("user", user).eq("meetingType", type)
        		.findList();
    	LinkedHashMap<String,String> options = new LinkedHashMap<String,String>();
        for(Schedule c: schedules) {
            options.put(c.toString(), c.startTime.toString() + " To " +c.endTime.toString());
        }
        return options;
    }
    
    /**
     * Demo schedules to work with
     * @return
     */
    public static ArrayList<Schedule> demo()
    {
    	ArrayList<Schedule> schedules = new ArrayList<Schedule>();
    	schedules.add(new Schedule(new Date(), new Date()));
    	return schedules;
    }
}
