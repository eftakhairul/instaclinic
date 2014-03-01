package models;

import com.avaje.ebean.annotation.EnumMapping;

@EnumMapping(nameValuePairs="ADMIN=ad, PATIENT=p, DOCTOR=d")
public enum UserRole {
    ADMIN,
    PATIENT,
    DOCTOR
}
