package models;

import com.avaje.ebean.annotation.EnumMapping;

@EnumMapping(nameValuePairs="REGULAR=R, YEARLY=Y")
public enum MeetingType 
{
	REGULAR,
	YEARLY		
}
