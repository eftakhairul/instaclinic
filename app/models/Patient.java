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
}
