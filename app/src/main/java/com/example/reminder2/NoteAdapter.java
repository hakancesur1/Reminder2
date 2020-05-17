package com.example.reminder2;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.MyViewHolder> {

    ArrayList mNotes;
    LayoutInflater inflater;

    public NoteAdapter(Context context, ArrayList<Notes> notes) {
        inflater = LayoutInflater.from(context);
        this.mNotes = notes;
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.recyclerviewlayout, parent, false);
        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Notes selectedNotes = (Notes) mNotes.get(position);
        holder.setData(selectedNotes, position);
    }

    @Override
    public int getItemCount() {
        return mNotes.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView rTitleTextView, rDateTextView;

        public MyViewHolder(View itemView) {
            super(itemView);
            rTitleTextView = (TextView) itemView.findViewById(R.id.rTitleTextView);
            rDateTextView = (TextView) itemView.findViewById(R.id.rDateTextView);
        }

        public void setData(Notes selectedNotes, int position) {
            this.rTitleTextView.setText(selectedNotes.getTitle());
            this.rDateTextView.setText(Notes.convertGregorianToDate(selectedNotes.getTime()));
        }

        @Override
        public void onClick(View v) {

        }

    }

}