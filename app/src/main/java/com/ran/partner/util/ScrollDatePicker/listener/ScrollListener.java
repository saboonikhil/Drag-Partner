package com.ran.partner.util.ScrollDatePicker.listener;

import com.ran.partner.util.ScrollDatePicker.Date;

public interface ScrollListener {
    void onStopDraggingPicker();

    void onDraggingPicker();

    void onDateSelected(Date item);
}