package models;

import org.joda.time.DateTime;
import play.data.format.Formats;
import play.data.validation.Constraints;
import play.db.ebean.Model;

import javax.persistence.*;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Entity
public class User extends Model{

    @Id
    private Long id;

    @Column(unique=true)
    @Constraints.Required
    private String username;

    @Constraints.Required
    private String password;

    @OneToOne(mappedBy="user")
    private Patient patient;

    private UserRole userRole;

    private Date create_date;

    public static Finder<Long, User> find = new Finder(Long.class, User.class);

    public User() {
        this.create_date = new Date();
    }

    public Long getId() {
        return id;
    }

    public String getUsername() {
        return this.username;
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

    public void setUserRole(UserRole userRole) {
        this.userRole = userRole;
    }

    public UserRole getUserRole() {
        return this.userRole;
    }

    public static User verify(String username, String password) {

        return find.where()
                   .eq("username", username)
                   .eq("password", password)
                   .findUnique();
    }

    public static void create(User newUser) {
        newUser.save();
    }

    public static void delete(Long id) {
        find.ref(id).delete();
    }

    public static User findById(Long id) {
        return find.byId(id);
    }


    /**
     * Return all user by a specific User Role
     * @param role filter by user role
     * return LinkedHashMap options
     */
    public static Map<String,String> findByUserRole(UserRole role) {
        List<User> users = find.where()
                               .eq("userRole", role)
                               .findList();

        LinkedHashMap<String,String> options = new LinkedHashMap<String,String>();

        for(User user: users) {
            options.put(user.getId().toString(), user.getUsername());
        }

        return options;
    }

    /**
     * Return the count by a specific User Role
     * @param role filter by user role
     * return LinkedHashMap options
     */
    public static int findCountByUserRole(UserRole role) {
        return find.where().eq("userRole", role).findRowCount();
    }

    /**
     * Return the user by username
     * @param String userName
     * return User
     */
    public static User findByUserName(String userName) {
        return find.where().eq("username", userName).findUnique();
    }


    /**
     * Check weather username available or not
     * @param String userName
     * return User
     */
    public static boolean checkUserNameAvilability(String userName) {
        return (findByUserName(userName) != null)? true:false;
    }
}