package com.example.reminder2;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class Notes {

    private int id;
    private  String title, detail, category ;
    private  boolean isDone;
    private  Calendar time;

    public Notes(){}

    public Notes(String title, String detail, Calendar time, String category, boolean isDone){
        this.title=title;
        this.detail=detail;
        this.time=time;
        this.category=category;
        this.isDone=isDone;
    }

    public  int getId(){
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public  String getTitle(){
        return title;
    }

    public  String getDetail(){
        return detail;
    }

    public  Calendar getTime(){
        return time;
    }

    public  String getCategory(){
        return category;
    }

    public  boolean getIsDone(){
        return isDone;
    }

    public void setTitle(String title){
        this.title=title;
    }

    public void setDetail(String detail){
        this.detail=detail;
    }

    public void setTime(Calendar time){
        this.time=time;
    }

    public void setCategory(String category){
        this.category=category;
    }

    public void setIsDone(boolean isDone){
        this.isDone=isDone;
    }

    public static String convertGregorianToDate(Calendar time){
        SimpleDateFormat fmt = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        fmt.setCalendar(time);

        String dateFormatted = fmt.format(time.getTime());

        return dateFormatted;
    }

    public static ArrayList<Notes> getRemindersWithType(ArrayList<Notes> reminderNotes, int type){
        ArrayList<Notes> reminderNotesNew = new ArrayList<>();
        Calendar today = Calendar.getInstance();
        double dayConverter = 24 * 60 * 60 * 1000;
        double diff;

        switch (type){
            case 1 : //today's reminders
                for(Notes reminderNote : reminderNotes){
                    diff = (reminderNote.getTime().getTimeInMillis() - today.getTimeInMillis()) / dayConverter;
                    if(diff >= 0 && diff < 1){
                        reminderNotesNew.add(reminderNote);
                    }
                }
                return reminderNotesNew;
            case 2: //week's reminders
                for(Notes reminderNote : reminderNotes){
                    diff = (reminderNote.getTime().getTimeInMillis() - today.getTimeInMillis()) / dayConverter;
                    if(diff >= 0 && diff < 7){
                        reminderNotesNew.add(reminderNote);
                    }
                }
                return reminderNotesNew;
            case 3: //month's reminders
                for(Notes reminderNote : reminderNotes){
                    diff = (reminderNote.getTime().getTimeInMillis() - today.getTimeInMillis()) / dayConverter;
                    if(diff >= 0 && diff < 30){
                        reminderNotesNew.add(reminderNote);
                    }
                }
                return reminderNotesNew;
        }
        return reminderNotes;
    }

}
