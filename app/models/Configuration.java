package models;

import play.data.format.Formats;
import play.data.validation.Constraints;
import play.db.ebean.Model;
import javax.persistence.Entity;

import javax.persistence.*;
import java.util.Date;


@Entity
public class Configuration extends Model {

    public Long getId() {
        return id;
    }

    @Id
    private Long id;

    private String long_meet;

    private String short_meet;

    private Date create_date;

    public Configuration() {
        this.create_date = new Date();
    }

    public String getLong_meet() {
        return long_meet;
    }

    public void setLong_meet(String long_meet) {
        this.long_meet = long_meet;
    }

    public String getShort_meet() {
        return short_meet;
    }

    public void setShort_meet(String short_meet) {
        this.short_meet = short_meet;
    }

    public Date getCreate_date() {
        return create_date;
    }

    public void setCreate_date(Date create_date) {
        this.create_date = create_date;
    }
}
