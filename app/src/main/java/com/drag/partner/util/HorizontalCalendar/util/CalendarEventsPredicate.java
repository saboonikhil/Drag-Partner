package com.drag.partner.util.HorizontalCalendar.util;

import com.drag.partner.util.HorizontalCalendar.model.CalendarEvent;

import java.util.Calendar;
import java.util.List;

public interface CalendarEventsPredicate {

    List<CalendarEvent> events(Calendar date);
}
