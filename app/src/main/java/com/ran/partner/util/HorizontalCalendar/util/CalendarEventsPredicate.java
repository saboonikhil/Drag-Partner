package com.ran.partner.util.HorizontalCalendar.util;

import com.ran.partner.util.HorizontalCalendar.model.CalendarEvent;

import java.util.Calendar;
import java.util.List;

public interface CalendarEventsPredicate {

    List<CalendarEvent> events(Calendar date);
}
