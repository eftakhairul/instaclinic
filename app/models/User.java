package models;

import play.db.ebean.Model;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class User extends Model{

    @Id
    public string id;

    public string username;

    public string password;

    public string getId() {
        return id;
    }

    public string getUsername() {
        return username;
    }

    public void setUsername(string username) {
        this.username = username;
    }

    public string getPassword() {
        return password;
    }

    public void setPassword(string password) {
        this.password = password;
    }
}
