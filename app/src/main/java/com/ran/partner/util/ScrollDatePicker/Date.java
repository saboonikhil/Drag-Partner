package com.ran.partner.util.ScrollDatePicker;

import org.joda.time.DateTime;

import java.util.Locale;

public class Date {
    private DateTime date;
    private boolean selected;
    private String monthPattern = "MMMM YYYY";

    Date(DateTime date) {
        this.date = date;
    }

    public String getDay() {
        return String.valueOf(date.getDayOfMonth());
    }

    public String getWeekDay() {
        return date.toString("EEE", Locale.getDefault()).toUpperCase();
    }

    public String getMonth() {
        return getMonth("");
    }

    public String getMonth(String pattern) {
        if (!pattern.isEmpty())
            this.monthPattern = pattern;

        return date.toString(monthPattern, Locale.getDefault());
    }

    public DateTime getDate() {
        return date.withTime(0, 0, 0, 0);
    }

    public boolean isToday() {
        DateTime today = new DateTime().withTime(0, 0, 0, 0);
        return getDate().getMillis() == today.getMillis();
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

}
