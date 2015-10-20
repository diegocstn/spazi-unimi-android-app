package it.unimi.unimiplaces.core.model;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * RoomEvent
 */
public class RoomEvent extends BaseEntity{

    private static final String HOUR_DELIMITER  = ":";
    private static final String HOURS_DELIMETER = "-";
    private String day;
    private String from;
    private String short_description;
    private String to;

    public RoomEvent(String description, String day, String from, String to){
        this.short_description  = description;
        this.day                = day;
        this.from               = from;
        this.to                 = to;
    }

    private String addLeadingZero(int i){
        if(i<10){
            return "0"+i;
        }else{
            return ""+i;
        }
    }


    public String getEventName(){
        return this.short_description;
    }

    public String getTime(){

        Date timeFrom,timeTo;
        DateFormat after = new SimpleDateFormat("kk:mm",Locale.US);
        try {
            timeFrom    = new SimpleDateFormat("kk:mm:ss",Locale.US).parse(this.from);
            timeTo      = new SimpleDateFormat("kk:mm:ss",Locale.US).parse(this.to);
            return String.format("%s %s %s",after.format(timeFrom),HOURS_DELIMETER,after.format(timeTo));
        }catch (Exception e){
            return  "";
        }
    }

    public String getDate(){
        Date date;
        DateFormat after = new SimpleDateFormat("EEEE (dd-MM-yyyy)", Locale.US);
        try {
            date = new SimpleDateFormat("yyyy-MM-dd",Locale.US).parse(this.day);
            return after.format(date);
        }catch (Exception e) {
            return "";
        }
    }

    public long getDateId(){
        return Long.parseLong(this.day.substring(0,4)+this.day.substring(5,7)+this.day.substring(8,10));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof RoomEvent)) return false;

        RoomEvent roomEvent = (RoomEvent) o;

        if (day != null ? !day.equals(roomEvent.day) : roomEvent.day != null) return false;
        if (from != null ? !from.equals(roomEvent.from) : roomEvent.from != null) return false;
        if (short_description != null ? !short_description.equals(roomEvent.short_description) : roomEvent.short_description != null)
            return false;
        return !(to != null ? !to.equals(roomEvent.to) : roomEvent.to != null);

    }

    @Override
    public int hashCode() {
        int result = day != null ? day.hashCode() : 0;
        result = 31 * result + (from != null ? from.hashCode() : 0);
        result = 31 * result + (short_description != null ? short_description.hashCode() : 0);
        result = 31 * result + (to != null ? to.hashCode() : 0);
        return result;
    }
}
