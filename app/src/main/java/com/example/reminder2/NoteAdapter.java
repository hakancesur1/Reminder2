package com.example.reminder2;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Random;

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.MyViewHolder> {

    ArrayList<Notes> mNotes;
    LayoutInflater inflater;

    Db db;

    public NoteAdapter(Context context, ArrayList<Notes> notes) {
        inflater = LayoutInflater.from(context);
        this.mNotes = notes;
        db=new Db(context);
    }

    private OnItemClickListener mListener;

    public interface OnItemClickListener {
        void onDeleteClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.recyclerviewlayout, parent, false);
        MyViewHolder holder = new MyViewHolder(view, (AdapterView.OnItemClickListener) mListener);
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

        TextView rTitleTextView, rDateTextView,rDetailTextView;
        LinearLayout parent;
        ImageView deleteImage,noteImage;

        public MyViewHolder(View itemView, final AdapterView.OnItemClickListener listener) {
            super(itemView);
            rTitleTextView = (TextView) itemView.findViewById(R.id.rTitleTextView);
            rDateTextView = (TextView) itemView.findViewById(R.id.rDateTextView);
            rDetailTextView=(TextView)itemView.findViewById(R.id.rDetailTextView);
            parent=itemView.findViewById(R.id.parent);
            deleteImage=itemView.findViewById(R.id.deleteImage);
            noteImage=itemView.findViewById(R.id.noteImage);

            deleteImage.setOnClickListener(this);
            noteImage.setOnClickListener(this);

        }

        public void setData(Notes selectedNotes, int position) {
            this.rTitleTextView.setText(selectedNotes.getTitle());
            this.rDateTextView.setText(Notes.convertGregorianToDate(selectedNotes.getTime()));
            this.rDetailTextView.setText(selectedNotes.getDetail());
            String category;
            category=selectedNotes.getCategory();
            if(category.equalsIgnoreCase("Birthday"))
                parent.setBackgroundColor(Color.rgb(100,150,240));
            else if(category.equalsIgnoreCase("Activity"))
                parent.setBackgroundColor(Color.rgb(100,200,100));
            else if(category.equalsIgnoreCase("Notice"))
                parent.setBackgroundColor(Color.rgb(200,100,100));
            else if(category.equalsIgnoreCase("Meeting"))
                parent.setBackgroundColor(Color.rgb(150,150,80));
            else
                parent.setBackgroundColor(Color.WHITE);

            if(selectedNotes.getIsDone()){
                rTitleTextView.setTextColor(Color.GRAY);
                rDateTextView.setTextColor(Color.GRAY);
                rDetailTextView.setTextColor(Color.GRAY);
            }
/*
            else{
                rTitleTextView.setTextColor(Color.RED);
                rDateTextView.setTextColor(Color.RED);
                rDetailTextView.setTextColor(Color.RED);
            }
*/
        }

        @Override
        public void onClick(View v) {

            String message = "";

            if (v == deleteImage) {
                message = deleteReminderNote(getLayoutPosition());

            }


            if(v==noteImage){
                message=updateReminderNote(getLayoutPosition());
            }

            Toast.makeText(itemView.getContext(), message, Toast.LENGTH_SHORT).show();
    }
        private String deleteReminderNote(int position) {
            db.deleteReminderNote(mNotes.get(position));

            mNotes.remove(position);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, mNotes.size());

            return "Silindi.";
        }

        private String updateReminderNote(int position){
            Intent androidIntent=new Intent(itemView.getContext(),NewTask.class);
            androidIntent.putExtra("id",mNotes.get(position).getId());
            itemView.getContext().startActivity(androidIntent);

            return "Detaylar";
        }

    }
}