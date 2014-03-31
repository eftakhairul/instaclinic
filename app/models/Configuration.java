package models;

import play.db.ebean.Model;
import javax.persistence.Entity;
import javax.persistence.*;
import java.util.Date;


@Entity
public class Configuration extends Model {

    @Id
    private Long id;

    private String long_meet;

    private String short_meet;

    private Date create_date;

    public Configuration() {
        this.create_date = new Date();
    }

    public Long getId() {
        return id;
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

    /**
     * Generic query helper for entity Configuration with id Long
     */
    public static Finder<Long, Configuration> find = new Finder<Long,Configuration>(Long.class, Configuration.class);
}
