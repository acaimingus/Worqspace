package io.github.acaimingus.worqspace.ui.calendar;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
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
    }

    @Override
    public int getItemCount() {
        return 24;
    }
}
