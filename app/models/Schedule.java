package models;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToOne;

import com.avaje.ebean.Page;

import play.data.format.Formats;
import play.data.format.Formats.DateTime;
import play.data.validation.Constraints;
import play.db.ebean.Model;
import play.db.ebean.Model.Finder;

@Entity
public class Schedule extends Model 
{
	@Id
	private int id;
	
	@Formats.DateTime(pattern="yyyy-MM-dd")
	@Constraints.Required
	private Date startTime;
	
	@Formats.DateTime(pattern="yyyy-MM-dd")
	@Constraints.Required
	private Date endTime;
	
	public Schedule(Date startTime, Date endTime)
	{
		this.startTime = startTime;
		this.endTime   = endTime;
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
    public static Page<Schedule> page(int page, int pageSize, String sortBy, String order, String filter) {
        return 
            find.where()
                .findPagingList(pageSize)
                .getPage(page);
    }
}
