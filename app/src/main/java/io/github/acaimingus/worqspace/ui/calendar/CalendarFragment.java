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

        // Create a sample calendar event
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 20);
        calendar.set(Calendar.MINUTE, 0);

        List<CalendarDay> events = new ArrayList<>();

        CalendarDay day = new CalendarDay(calendar);
        day.setImageResource(R.drawable.calendar);

        events.add(day);
        binding.calendarView.setCalendarDays(events);

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