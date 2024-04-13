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

import de.codecrafters.tableview.TableDataAdapter;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyHolder> {

    public interface OnAppointmentListener{
        void OnAppointmentLongClick(int position);
    }

    Context context;
    ArrayList<Appointment> appointments;

    private OnAppointmentListener onAppointmentListener;


    public MyAdapter(Context context, ArrayList<Appointment> appointments, OnAppointmentListener onAppointmentListener) {
        this.context = context;
        this.appointments = appointments;
        this.onAppointmentListener = onAppointmentListener;
    }


    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.appointment_item_res,parent,false);
        return new MyHolder(view, onAppointmentListener);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {
        Appointment appointment = appointments.get(position);

        holder.date.setText( appointment.getDate());
        holder.time.setText( appointment.getTime());
    }

    @Override
    public int getItemCount() {
        return appointments.size();
    }


    public static class MyHolder extends RecyclerView.ViewHolder{
        TextView date,time;

        public MyHolder(@NonNull View itemView, OnAppointmentListener onAppointmentListener) {
            super(itemView);
            date = itemView.findViewById(R.id.date);
            time = itemView.findViewById(R.id.time);

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if (onAppointmentListener != null) {
                        int pos = getAdapterPosition();

                        if (pos != RecyclerView.NO_POSITION) {
                            onAppointmentListener.OnAppointmentLongClick(pos);
                        }
                    }
                    return true;
                }
            });
        }
    }
}
