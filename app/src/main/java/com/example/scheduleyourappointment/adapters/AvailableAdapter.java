package com.example.scheduleyourappointment.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.scheduleyourappointment.R;
import com.example.scheduleyourappointment.model.Appointment;

import java.util.ArrayList;

public class AvailableAdapter extends RecyclerView.Adapter<AvailableAdapter.AvailableHolder>{


    public interface OnAvailableAppointmentListener{
        void OnAvailableAppointmentLongClick(int position);
        void OnAvailableAppointmentClick(int position);
    }

    Context context;
    ArrayList<Appointment> appointments;

    private OnAvailableAppointmentListener onAvailableAppointmentListener;


    public AvailableAdapter(Context context, ArrayList<Appointment> appointments, OnAvailableAppointmentListener onAvailableAppointmentListener) {
        this.context = context;
        this.appointments = appointments;
        this.onAvailableAppointmentListener = onAvailableAppointmentListener;
    }


    @NonNull
    @Override
    public AvailableAdapter.AvailableHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.available_item_res,parent,false);
        return new AvailableHolder(view, onAvailableAppointmentListener);
    }

    @Override
    public void onBindViewHolder(@NonNull AvailableAdapter.AvailableHolder holder, int position) {
        Appointment appointment = appointments.get(position);

        holder.date.setText( appointment.getDate());
        holder.time.setText( appointment.getTime());
    }

    @Override
    public int getItemCount() {
        return appointments.size();
    }


    public static class AvailableHolder extends RecyclerView.ViewHolder{
        TextView date,time;

        public AvailableHolder(@NonNull View itemView, OnAvailableAppointmentListener onAvailableAppointmentListener) {
            super(itemView);
            date = itemView.findViewById(R.id.date);
            time = itemView.findViewById(R.id.time);

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if (onAvailableAppointmentListener != null) {
                        int pos = getAdapterPosition();

                        if (pos != RecyclerView.NO_POSITION) {
                            onAvailableAppointmentListener.OnAvailableAppointmentLongClick(pos);
                        }
                    }
                    return true;
                }
            });
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onAvailableAppointmentListener != null) {
                        int pos = getAdapterPosition();

                        if (pos != RecyclerView.NO_POSITION) {
                            onAvailableAppointmentListener.OnAvailableAppointmentClick(pos);
                        }
                    }
                }
            });
        }
    }
}
