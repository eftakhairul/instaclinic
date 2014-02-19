package models;

import org.joda.time.DateTime;
import play.data.format.Formats;
import play.data.validation.Constraints;
import play.db.ebean.Model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class User extends Model{

    @Id
    public Long id;

    @Column(unique=true)
    @Constraints.Required
    public String username;

    @Constraints.Required
    public String password;

    @Constraints.Required
    public DateTime create_date;

    public static Finder<Long, User> find = new Finder(Long.class, User.class);

    //Static initialization
    User() {
        create_date = new DateTime();
    }

    public Long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public static User verify(String username, String password) {

        return find.where()
                   .eq("username", username)
                   .eq("password", password)
                   .findUnique();
    }

    public static void create(User newuser) {
        newuser.save();
    }

    public static void delete(Long id) {
        find.ref(id).delete();
    }

    public static User findById(Long id) {
        return find.byId(id);
    }
}
