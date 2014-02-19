package models;

import java.util.Date;

import play.data.format.Formats.DateTime;
import play.db.ebean.Model;

public class Schedule extends Model 
{
	@DateTime(pattern = "MM/dd/yy")
	private Date startTime;
	
	@DateTime(pattern = "MM/dd/yy")
	private Date endTime;
	
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
