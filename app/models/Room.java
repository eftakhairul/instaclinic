package models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import com.avaje.ebean.Ebean;

import play.db.ebean.Model;

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
	
	public static Room getDemo()
	{
		return Ebean.find(Room.class, 1);
	}
}
