package models;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import com.avaje.ebean.Ebean;

import play.db.ebean.Model;
import play.db.ebean.Model.Finder;

@Entity
public class Room extends Model 
{
	@Id
	@GeneratedValue(strategy = javax.persistence.GenerationType.AUTO)
	private int id;
	
	public int getId()
	{
		return id;
	}
	
	public static Finder<Long,Room> find = new Finder<Long,Room>(Long.class, Room.class); 
	
	public static Room getDemo()
	{
		return Ebean.find(Room.class, 1);
	}
	
	public static List<Room> getAllRooms()
	{
		return find.all();
	}
}
