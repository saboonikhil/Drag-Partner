package com.drag.partner.util.HorizontalCalendar.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.drag.partner.R;
import com.drag.partner.util.HorizontalCalendar.model.CalendarEvent;

import java.util.List;

public class EventsAdapter extends RecyclerView.Adapter<EventsAdapter.EventViewHolder> {

    private List<CalendarEvent> eventList;

    EventsAdapter(List<CalendarEvent> eventList) {
        this.eventList = eventList;
    }

    @NonNull
    @Override
    public EventViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        ImageView imageView = new ImageView(context);

        Drawable circle = ContextCompat.getDrawable(context, R.drawable.ic_circle_white_8dp);
        assert circle != null;
        Drawable drawableWrapper = DrawableCompat.wrap(circle);

        imageView.setImageDrawable(drawableWrapper);

        return new EventViewHolder(imageView);
    }

    @Override
    public void onBindViewHolder(@NonNull EventViewHolder holder, int position) {
        CalendarEvent event = getItem(position);

        ImageView imageView = (ImageView) holder.itemView;

        imageView.setContentDescription(event.getDescription());
        DrawableCompat.setTint(imageView.getDrawable(), event.getColor());
    }

    private CalendarEvent getItem(int position) throws IndexOutOfBoundsException {
        return eventList.get(position);
    }

    @Override
    public int getItemCount() {
        return eventList.size();
    }

    public void update(List<CalendarEvent> eventList) {
        this.eventList = eventList;
        notifyDataSetChanged();
    }

    static class EventViewHolder extends RecyclerView.ViewHolder {

        EventViewHolder(View itemView) {
            super(itemView);
        }
    }
}

