package models;

import java.text.SimpleDateFormat;
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
	
	@Formats.DateTime(pattern="MM/dd/yyyy")
	@Constraints.Required
	private Date scheduleDate;
	
	@Formats.DateTime(pattern="HH:mm")
	@Constraints.Required
	private Date startTime;
	
	@Formats.DateTime(pattern="HH:mm")
	@Constraints.Required
	private Date endTime;
	
	@ManyToOne(cascade=CascadeType.ALL)
	private User user;
	
	private MeetingType meetingType;
	
	public Schedule(Date startTime, Date endTime, Date scheduleDate)
	{
		this.startTime = startTime;
		this.endTime   = endTime;
		this.scheduleDate = scheduleDate;
	}
	
	public void setId(int id)
	{
		this.id = id;
	}
	
	public int getId()
	{
		return id;
	}
	
	public Date getScheduleDate()
	{
		return scheduleDate;
	}
	
	public void setScheduleDate(Date scheduleDate)
	{
		this.scheduleDate = scheduleDate;
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
    	List<Schedule> schedules = find
        		.orderBy("startTime")
        		.findList();
    	LinkedHashMap<String,String> options = new LinkedHashMap<String,String>();
        int interval = (type == MeetingType.REGULAR)?20:60;
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm");
    	for(Schedule c: schedules) {
        	//if(!Appointment.findBySchedule(c)) {
    		Date start = c.startTime;
    		Date end   = c.endTime;
    		int i = 0;
    		while(start.getTime() < end.getTime()) {
    			options.put(i+"", new SimpleDateFormat("MM/dd/yyyy").format(c.scheduleDate) + "-" + formatter.format(c.startTime) + "-" + formatter.format(new Date(c.startTime.getTime()+(interval*60000))));
    			c.startTime.setTime(c.startTime.getTime()+(interval*60000));
    			i++;
    		}
        		//options.put(c.toString(), new SimpleDateFormat("MM/dd/yyyy").format(c.scheduleDate) + "-" + formatter.format(c.startTime) + "-" + formatter.format(c.endTime));
        	//}
        }
        return options;
    }
    
    public static Map<String,String> findByDoctor(User user) {
    	List<Schedule> schedules = find.where()
        		.eq("user", user)
        		.findList();
    	LinkedHashMap<String,String> options = new LinkedHashMap<String,String>();
        for(Schedule c: schedules) {
        	if(!Appointment.findBySchedule(c)) {
        		options.put(c.toString(), new SimpleDateFormat("MM/dd/yy HH:mm").format(c.startTime) + " To " +new SimpleDateFormat("MM/dd/yy HH:mm").format(c.endTime));
        	}
        }
        return options;
    }
    
    public static Map<String,String> findByDoctorAndType(User user, MeetingType type) {
    	List<Schedule> schedules = find.where()
        		.eq("user", user).eq("meetingType", type)
        		.findList();
    	LinkedHashMap<String,String> options = new LinkedHashMap<String,String>();
        for(Schedule c: schedules) {
        	if(!Appointment.findBySchedule(c)) {
        		options.put(c.toString(), new SimpleDateFormat("MM/dd/yy HH:mm").format(c.startTime) + " To " +new SimpleDateFormat("MM/dd/yy HH:mm").format(c.endTime));
        	}
        }
        return options;
    }
    
    /**
     * Demo schedules to work with
     * @return
     */
    /*public static ArrayList<Schedule> demo()
    {
    	ArrayList<Schedule> schedules = new ArrayList<Schedule>();
    	schedules.add(new Schedule(new Date(), new Date()));
    	return schedules;
    }*/
    
    public static List<Schedule> findByTime(Date d, Date start, Date end)
    {
    	ArrayList<Schedule> returnList = new ArrayList<Schedule>();
    	List<Schedule> schedules = find.all();
    	for (Schedule schedule : schedules) {
    		if(new SimpleDateFormat("MM/dd/yy").format(schedule.scheduleDate) != new SimpleDateFormat("MM/dd/yy").format(d))
    		{
    			continue;
    		}
			if(schedule.getStartTime().getTime() >= start.getTime() && schedule.getStartTime().getTime() < end.getTime()) {
				returnList.add(schedule);
			}
			else if(schedule.getEndTime().getTime() > start.getTime() && schedule.getEndTime().getTime() <= end.getTime()) {
				returnList.add(schedule);
			}
		}
    	return returnList;
    }
}
