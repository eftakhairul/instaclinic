package models;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

import play.data.validation.Constraints;
import play.db.ebean.Model;

import java.util.Date;

@Entity
public class Patient extends Model {

    @Id
    private Long user_id;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Constraints.Required
    private String health_card_no;

    private String birthday;

    private String gender;

    private String phone_number;

    public Date create_date;

    public Patient() {
        create_date = new Date();
    }

    public Long getUser_id() {
        return user_id;
    }

    public void setUser_id(Long user_id) {
        this.user_id = user_id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getHealth_card_no() {
        return health_card_no;
    }

    public void setHealth_card_no(String health_card_no) {
        this.health_card_no = health_card_no;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }

    public Date getCreate_date() {
        return create_date;
    }

    public void setCreate_date(Date create_date) {
        this.create_date = create_date;
    }
}
