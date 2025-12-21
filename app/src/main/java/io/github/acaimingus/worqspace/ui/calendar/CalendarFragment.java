package io.github.acaimingus.worqspace.ui.calendar;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.widget.PopupMenu;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.applandeo.materialcalendarview.CalendarDay;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import io.github.acaimingus.worqspace.R;
import io.github.acaimingus.worqspace.databinding.FragmentCalendarBinding;

public class CalendarFragment extends Fragment {

    private FragmentCalendarBinding binding;
    private HourAdapter hourAdapter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        // Create the binding
        binding = FragmentCalendarBinding.inflate(inflater, container, false);

        // Create the hour adapter
        hourAdapter = new HourAdapter();
        binding.rvDailyEvents.setLayoutManager(
                new androidx.recyclerview.widget.LinearLayoutManager(getContext())
        );
        binding.rvDailyEvents.setAdapter(hourAdapter);

        // Create debug calendar events
        // Get a calendar instance
        Calendar calendar = Calendar.getInstance();
        // Create a list for the events
        List<CalendarEvent> events = new ArrayList<>();

        // Create a the first debug calendar event
        CalendarEvent debug1 = new CalendarEvent();
        debug1.title = "Debug 1";
        debug1.type = CalendarEventType.APPOINTMENT;
        // Set start time
        calendar.set(Calendar.HOUR_OF_DAY, 20);
        calendar.set(Calendar.MINUTE, 20);
        debug1.startTime = calendar.getTimeInMillis();
        // Set end time
        calendar.set(Calendar.HOUR_OF_DAY, 21);
        calendar.set(Calendar.MINUTE, 27);
        debug1.endTime = calendar.getTimeInMillis();
        // Add it to the list
        events.add(debug1);

        CalendarEvent debug2 = new CalendarEvent();
        debug2.title = "Debug 2";
        debug2.type = CalendarEventType.TASK;
        calendar.set(Calendar.HOUR_OF_DAY, 20);
        calendar.set(Calendar.MINUTE, 10);
        debug2.startTime = calendar.getTimeInMillis();
        events.add(debug2);

        CalendarEvent debug3 = new CalendarEvent();
        debug3.title = "Debug 3";
        debug3.type = CalendarEventType.DEADLINE;
        calendar.set(Calendar.HOUR_OF_DAY, 17);
        calendar.set(Calendar.MINUTE, 35);
        debug3.startTime = calendar.getTimeInMillis();
        events.add(debug3);

        CalendarEvent debug4 = new CalendarEvent();
        debug4.title = "Birthday of Debug";
        debug4.type = CalendarEventType.BIRTHDAY;
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        debug4.startTime = calendar.getTimeInMillis();
        events.add(debug4);


        hourAdapter.updateEvents(events);

        // Create a floating action button for the calendar
        FloatingActionButton fab = binding.getRoot().findViewById(R.id.fab_add_event);
        fab.setOnClickListener(view -> {
            // Create a popup menu for the floating action button
            PopupMenu popupMenu = new PopupMenu(requireContext(), view);
            popupMenu.getMenuInflater().inflate(R.menu.menu_add_calendar_event, popupMenu.getMenu());
            popupMenu.setForceShowIcon(true);

            popupMenu.setOnMenuItemClickListener(item -> {
                int id = item.getItemId();

                if (id == R.id.action_deadline) {
                    // TODO
                    return true;
                } else if (id == R.id.action_appointment) {
                    // TODO
                    return true;
                } else if (id == R.id.action_task) {
                    // TODO
                    return true;
                } else if (id == R.id.action_birthday) {
                    // TODO
                    return true;
                }

                return false;
            });
            popupMenu.show();
        });

        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}