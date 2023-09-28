package model;

import java.time.LocalDate;
import java.time.LocalTime;

public class DateTime {
    private final int year;
    private final int month;
    private final int day;
    private final int hour;
    private final int minute;

    public DateTime(int year, int month, int day, int hour, int minute) {
        this.year = year;
        this.month = month;
        this.day = day;
        this.hour = hour;
        this.minute = minute;
    }

    public DateTime() {
        this.hour = LocalTime.now().getHour();
        this.minute = LocalTime.now().getMinute();
        this.year = LocalDate.now().getYear();
        this.month = LocalDate.now().getMonth().getValue();
        this.day = LocalDate.now().getDayOfMonth();
    }

    public DateTime(LocalDate date){
        this.year = date.getYear();
        this.month = date.getMonthValue();
        this.day = date.getDayOfMonth();
        this.hour = 1;
        this.minute = 1;
    }

    public String getDate() {
        return (this.year + "-" + this.month + "-" + this.day);
    }

    public String getTime() {
        return (this.hour + ":" + this.minute);
    }

    @Override
    public String toString() {
        return "DateTime: " + year + " " + month + " " + day + " " + hour + " " + minute;
    }
}
