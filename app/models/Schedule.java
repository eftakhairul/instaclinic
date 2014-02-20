package models;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToOne;

import play.data.format.Formats.DateTime;
import play.db.ebean.Model;

@Entity
public class Schedule extends Model 
{
	@Id
	private int id;
	
	@DateTime(pattern = "MM/dd/yy")
	private Date startTime;
	
	@DateTime(pattern = "MM/dd/yy")
	private Date endTime;
	
	public Schedule(Date startTime, Date endTime)
	{
		this.startTime = startTime;
		this.endTime   = endTime;
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
}
