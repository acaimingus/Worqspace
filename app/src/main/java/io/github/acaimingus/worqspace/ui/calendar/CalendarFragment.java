package io.github.acaimingus.worqspace.ui.calendar;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.appcompat.widget.PopupMenu;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;

import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

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

                    CalendarEvent newEvent = new CalendarEvent("Debug deadline", "Debug location", "Debug note", CalendarEventType.DEADLINE, startTime.getTimeInMillis(), startTime.getTimeInMillis());

                    AppDatabase.databbaseWriteExecutor.execute(() -> {
                        AppDatabase db = AppDatabase.getDatabase(requireContext());
                        db.eventDao().insert(newEvent);

                        if (getActivity() != null) {
                            getActivity().runOnUiThread(() -> loadEventsForDate(startTime));
                        }
                    });

                    return true;
                } else if (id == R.id.action_appointment) {
                    showAddAppointmentDialog();
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

    private void showAddAppointmentDialog() {
        // Prepare the layout
        View dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_add_appointment, null);
        TextInputEditText etTitle = dialogView.findViewById(R.id.et_app_title);
        TextInputEditText etLocation = dialogView.findViewById(R.id.et_app_location);
        TextInputEditText etDescription = dialogView.findViewById(R.id.et_app_description);
        TextInputEditText etStartTime = dialogView.findViewById(R.id.et_app_start_time);
        TextInputEditText etEndTime = dialogView.findViewById(R.id.et_app_end_time);
        TextInputEditText etDate = dialogView.findViewById(R.id.et_app_date);

        // Create calendars for the times
        final Calendar startTime = Calendar.getInstance();
        final Calendar endTime = Calendar.getInstance();
        endTime.add(Calendar.HOUR_OF_DAY, 1);

        // Set initial values for the time text fields
        etStartTime.setText(String.format(Locale.GERMANY, "%02d:%02d", startTime.get(Calendar.HOUR_OF_DAY), startTime.get(Calendar.MINUTE)));
        etEndTime.setText(String.format(Locale.GERMANY, "%02d:%02d", endTime.get(Calendar.HOUR_OF_DAY), endTime.get(Calendar.MINUTE)));

        // Set initial value for the date
        java.text.SimpleDateFormat dateFormat = new java.text.SimpleDateFormat("dd.MM.yyyy", Locale.GERMANY);
        etDate.setText(dateFormat.format(startTime.getTime()));

        // Open date picker dialog
        etDate.setOnClickListener(v -> new android.app.DatePickerDialog(requireContext(),
                (view, year, month, dayOfMonth) -> {
                    startTime.set(Calendar.YEAR, year);
                    startTime.set(Calendar.MONTH, month);
                    startTime.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                    endTime.set(Calendar.YEAR, year);
                    endTime.set(Calendar.MONTH, month);
                    endTime.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                    etDate.setText(dateFormat.format(startTime.getTime()));
                },
                startTime.get(Calendar.YEAR),
                startTime.get(Calendar.MONTH),
                startTime.get(Calendar.DAY_OF_MONTH)
        ).show());

        // Open time picker dialog
        View.OnClickListener timeClickListener = v -> {
            Calendar targetCal = (v.getId() == R.id.et_app_start_time) ? startTime : endTime;

            new android.app.TimePickerDialog(requireContext(),
                    (view, hourOfDay, minute) -> {
                        targetCal.set(Calendar.HOUR_OF_DAY, hourOfDay);
                        targetCal.set(Calendar.MINUTE, minute);

                        String timeString = String.format(Locale.GERMANY, "%02d:%02d", hourOfDay, minute);
                        ((TextInputEditText) v).setText(timeString);
                    },
                    targetCal.get(Calendar.HOUR_OF_DAY),
                    targetCal.get(Calendar.MINUTE),
                    true
            ).show();
        };

        etStartTime.setOnClickListener(timeClickListener);
        etEndTime.setOnClickListener(timeClickListener);

        new com.google.android.material.dialog.MaterialAlertDialogBuilder(requireContext())
                .setTitle("Creating a new appointment")
                .setView(dialogView)
                .setNegativeButton("Cancel", null)
                .setPositiveButton("Create", (dialog, which) -> {
                    String title = Objects.requireNonNull(etTitle.getText()).toString();
                    String location = Objects.requireNonNull(etLocation.getText()).toString();
                    String description = Objects.requireNonNull(etDescription.getText()).toString();

                    if (title.isEmpty()) {
                        return;
                    }

                    CalendarEvent eventToSave = new CalendarEvent(
                            title,
                            location,
                            description,
                            CalendarEventType.APPOINTMENT,
                            startTime.getTimeInMillis(),
                            endTime.getTimeInMillis()
                    );

                    AppDatabase.databbaseWriteExecutor.execute(() -> {
                        AppDatabase db = AppDatabase.getDatabase(requireContext());
                        db.eventDao().insert(eventToSave);

                        if (getActivity() != null) {
                            getActivity().runOnUiThread(() -> {
                                loadEventsForDate(startTime);
                                try {
                                    binding.calendarView.setDate(startTime);
                                } catch (com.applandeo.materialcalendarview.exceptions.OutOfDateRangeException e) {
                                    Log.e("CalendarFragment","The date was outside of the calendar");
                                }
                            });
                        }
                    });
                })
                .show();
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