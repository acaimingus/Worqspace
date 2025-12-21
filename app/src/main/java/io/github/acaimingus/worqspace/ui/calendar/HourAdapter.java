package io.github.acaimingus.worqspace.ui.calendar;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

import io.github.acaimingus.worqspace.R;

public class HourAdapter extends RecyclerView.Adapter<HourViewHolder> {
    private List<CalendarEvent> eventsForToday = new ArrayList<>();

    public void updateEvents(List<CalendarEvent> newEvents) {
        this.eventsForToday = newEvents;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public HourViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.calendar_hour_layout, parent, false);
        return new HourViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HourViewHolder holder, int position) {
        String timeText = String.format(Locale.US,"%02d:00", position);
        holder.tvTime.setText(timeText);

        CalendarEvent eventForHour = null;
        Calendar calendar = Calendar.getInstance();

        List<CalendarEvent> eventsForHour = new ArrayList<>();

        for (CalendarEvent event : eventsForToday) {
            // Search for the needed hour of the day
            calendar.setTimeInMillis(event.startTime);
            int eventHour = calendar.get(Calendar.HOUR_OF_DAY);
            // Check if any event is in this hour
            if (eventHour == position)
            {
                eventsForHour.add(event);
            }
        }
        eventsForHour.sort(Comparator.comparingLong(e -> e.startTime));

        // Remove previous views to avoid duplicates when recycling
        holder.llEvents.removeAllViews();

        if (!eventsForHour.isEmpty()) {
            holder.llEvents.setVisibility(View.VISIBLE);

            for (CalendarEvent event : eventsForHour) {
                // Get the context
                Context context = holder.itemView.getContext();
                TextView eventTextView = new TextView(context);

                // Determine the start date
                calendar.setTimeInMillis(event.startTime);
                String startDate = String.format(Locale.US, "%02d:%02d", calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE));

                // TextView-Styling
                android.graphics.drawable.GradientDrawable shape = new android.graphics.drawable.GradientDrawable();
                shape.setShape(android.graphics.drawable.GradientDrawable.RECTANGLE);
                shape.setColor(context.getColor(R.color.worqspace_variant));
                shape.setCornerRadius(16f);
                eventTextView.setBackground(shape);
                eventTextView.setTextColor(context.getColor(R.color.white));
                eventTextView.setPadding(24, 16, 24, 16);
                eventTextView.setMinimumHeight(120);
                LinearLayout.LayoutParams parameters = new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT
                );
                parameters.setMargins(0, 0, 0, 8);
                eventTextView.setLayoutParams(parameters);

                // Set label icon
                int labelIcon = 0;
                if (event.type == CalendarEventType.APPOINTMENT) {
                    labelIcon = R.drawable.appointment;
                } else if (event.type == CalendarEventType.DEADLINE) {
                    labelIcon = R.drawable.deadline;
                } else if (event.type == CalendarEventType.TASK) {
                    labelIcon = R.drawable.task;
                } else if (event.type == CalendarEventType.BIRTHDAY) {
                  labelIcon = R.drawable.birthday;
                } else {
                    labelIcon = R.drawable.calendar;
                }
                eventTextView.setCompoundDrawablesRelativeWithIntrinsicBounds(labelIcon, 0, 0, 0);
                eventTextView.setCompoundDrawablePadding(16);

                // Set the label text
                if (event.type == CalendarEventType.APPOINTMENT)
                {
                    // Add the title and the start and end
                    calendar.setTimeInMillis(event.endTime);
                    String endDate = String.format(Locale.US, "%02d:%02d", calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE));

                    eventTextView.setText(String.format(Locale.US,"%s (%s - %s)", event.title, startDate, endDate));
                } else if (event.type == CalendarEventType.BIRTHDAY)
                {
                    eventTextView.setText(event.title);
                }
                else {
                    // Only add a start time
                    eventTextView.setText(String.format(Locale.US,"%s (%s)", event.title, startDate));
                }

                holder.llEvents.addView(eventTextView);
            }
        } else {
            holder.llEvents.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return 24;
    }
}
