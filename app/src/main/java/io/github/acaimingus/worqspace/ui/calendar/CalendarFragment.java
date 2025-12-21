package io.github.acaimingus.worqspace.ui.calendar;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.appcompat.widget.PopupMenu;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.util.Calendar;
import java.util.List;
import io.github.acaimingus.worqspace.R;
import io.github.acaimingus.worqspace.data.AppDatabase;
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

        loadEventsForDate(Calendar.getInstance());

        binding.calendarView.setOnCalendarDayClickListener(calendarDay -> {
            // Get the given calendar day
            Calendar clickedDay = calendarDay.getCalendar();
            // Load the events for that day
            loadEventsForDate(clickedDay);
        });

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
                    Calendar startTime = Calendar.getInstance();
                    startTime.set(Calendar.HOUR_OF_DAY, 16);
                    startTime.set(Calendar.MINUTE, 24);

                    CalendarEvent newEvent = new CalendarEvent("Debug deadline", "Debug location", CalendarEventType.DEADLINE, startTime.getTimeInMillis(), startTime.getTimeInMillis());

                    AppDatabase.databbaseWriteExecutor.execute(() -> {
                        AppDatabase db = AppDatabase.getDatabase(requireContext());
                        db.eventDao().insert(newEvent);

                        if (getActivity() != null) {
                            getActivity().runOnUiThread(() -> loadEventsForDate(startTime));
                        }
                    });

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

    private void loadEventsForDate(Calendar date) {
        // Get the start of the day
        Calendar start = (Calendar) date.clone();
        start.set(Calendar.HOUR_OF_DAY, 0);
        start.set(Calendar.MINUTE, 0);
        start.set(Calendar.SECOND, 0);
        start.set(Calendar.MILLISECOND, 0);
        long startMillis = start.getTimeInMillis();

        // Get the end of the day
        Calendar end = (Calendar) start.clone();
        end.add(Calendar.DAY_OF_MONTH, 1);
        long endMillis = end.getTimeInMillis();

        AppDatabase db = AppDatabase.getDatabase(requireContext());
        AppDatabase.databbaseWriteExecutor.execute(() -> {
            // Load the events
            List<CalendarEvent> events = db.eventDao().getEventsForDay(startMillis, endMillis);

            if (getActivity() != null) {
                getActivity().runOnUiThread(() -> hourAdapter.updateEvents(events));
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}