package it.unimi.unimiplaces.core.model;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

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

    private String formatTime(LocalTime time){
        return String.format("%s%s%s",
                addLeadingZero(time.getHour()),
                HOUR_DELIMITER,
                addLeadingZero(time.getMinute())
        );
    }

    public String getTime(){
        LocalTime fromTime  = LocalTime.parse(this.from, DateTimeFormatter.ofPattern("k:m:s"));
        LocalTime toTime    = LocalTime.parse(this.to, DateTimeFormatter.ofPattern("k:m:s"));
        return String.format("%s %s %s",formatTime(fromTime),HOURS_DELIMETER,formatTime(toTime));
    }

    public String getDate(){
        LocalDate localDate = LocalDate.parse(this.day,DateTimeFormatter.ISO_LOCAL_DATE);
        return String.format(
                "%s (%s-%s-%d)",
                localDate.getDayOfWeek(),
                addLeadingZero(localDate.getDayOfMonth()),
                addLeadingZero(localDate.getMonthValue()),
                localDate.getYear());
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
