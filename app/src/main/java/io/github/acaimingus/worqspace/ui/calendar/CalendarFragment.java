package io.github.acaimingus.worqspace.ui.calendar;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.appcompat.widget.PopupMenu;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import io.github.acaimingus.worqspace.R;
import io.github.acaimingus.worqspace.databinding.FragmentCalendarBinding;

public class CalendarFragment extends Fragment {

    private FragmentCalendarBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentCalendarBinding.inflate(inflater, container, false);

        FloatingActionButton fab = binding.getRoot().findViewById(R.id.fab_add_event);

        fab.setOnClickListener(view -> {
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