package models;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;

import play.db.ebean.Model;

@Entity
public class Appointment extends Model 
{
	@ManyToOne(cascade=CascadeType.ALL)
	@PrimaryKeyJoinColumn
	private Schedule schedule;
	
	@ManyToOne(cascade=CascadeType.ALL)
	@PrimaryKeyJoinColumn
	private Room room;
	
	private User user;
	
	public Appointment(Room room, Schedule schedule)
	{
		this.schedule = schedule;
		this.room     = room;
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
	
	
}
