package com.drag.partner.util.HorizontalCalendar.util;

import android.support.annotation.Nullable;
import android.support.v7.widget.LinearSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.drag.partner.util.HorizontalCalendar.HorizontalCalendar;
import com.drag.partner.util.HorizontalCalendar.HorizontalCalendarView;

public class HorizontalSnapHelper extends LinearSnapHelper {

    private HorizontalCalendar horizontalCalendar;
    private HorizontalCalendarView calendarView;

    @Override
    public View findSnapView(RecyclerView.LayoutManager layoutManager) {
        View snapView = super.findSnapView(layoutManager);

        if (calendarView.getScrollState() != RecyclerView.SCROLL_STATE_DRAGGING) {
            int selectedItemPosition;
            if (snapView == null) {
                // no snapping required
                selectedItemPosition = horizontalCalendar.getSelectedDatePosition();
            } else {
                int[] snapDistance = calculateDistanceToFinalSnap(layoutManager, snapView);
                assert snapDistance != null;
                if ((snapDistance[0] != 0) || (snapDistance[1] != 0)) {
                    return snapView;
                }
                selectedItemPosition = layoutManager.getPosition(snapView);
            }

            notifyCalendarListener(selectedItemPosition);
        }

        return snapView;
    }

    private void notifyCalendarListener(int selectedItemPosition) {
        if (!horizontalCalendar.isItemDisabled(selectedItemPosition)) {
            horizontalCalendar.getCalendarListener()
                    .onDateSelected(horizontalCalendar.getDateAt(selectedItemPosition), selectedItemPosition);
        }
    }

    @Override
    public void attachToRecyclerView(@Nullable RecyclerView recyclerView) throws IllegalStateException {
        // Do nothing
    }

    public void attachToHorizontalCalendar(@Nullable HorizontalCalendar horizontalCalendar) throws IllegalStateException {
        this.horizontalCalendar = horizontalCalendar;
        assert horizontalCalendar != null;
        this.calendarView = horizontalCalendar.getCalendarView();
        super.attachToRecyclerView(calendarView);
    }
}
