package models;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import com.avaje.ebean.Ebean;

import com.avaje.ebean.Page;
import play.data.validation.Constraints;
import play.db.ebean.Model;
import play.db.ebean.Model.Finder;

@Entity
public class Room extends Model 
{
	@Id
	@GeneratedValue(strategy = javax.persistence.GenerationType.AUTO)
	private int id;

    @Constraints.Required
    private String roomName;
	
	public int getId() {
		return id;
	}

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }
	
	public static Finder<Long,Room> find = new Finder<Long,Room>(Long.class, Room.class); 
	
	public static Room getDemo() {

        return Ebean.find(Room.class, 1);

	}
	
	public static List<Room> getAllRooms() {
        return find.all();
	}

    /**
     * Return a page of Room
     *
     * @param page Page to display
     * @param pageSize Number of computers per page
     * @param sortBy Computer property used for sorting
     * @param order Sort order (either or asc or desc)
     * @param filter Filter applied on the name column
     */
    public static Page<Room> page(int page, int pageSize, String sortBy, String order, String filter) {
        return find.where()
                   .findPagingList(pageSize)
                   .getPage(page);
    }

    /**
     * Return toString
     */
    public String toString() {
        return this.roomName + "";
    }
}
