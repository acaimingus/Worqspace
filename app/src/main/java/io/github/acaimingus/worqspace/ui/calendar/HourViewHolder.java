package io.github.acaimingus.worqspace.ui.calendar;

import android.view.View;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import io.github.acaimingus.worqspace.R;

class HourViewHolder extends RecyclerView.ViewHolder {
    TextView tvTime;
    TextView tvEventName;

    public HourViewHolder(@NonNull View itemView) {
        super(itemView);
        tvTime = itemView.findViewById(R.id.tv_time);
        tvEventName = itemView.findViewById(R.id.tv_event_name);
    }
}