package com.example.reminder2;

import java.lang.reflect.Array;
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
        SimpleDateFormat fmt = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
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

    public static ArrayList<Integer> getRemindersDetails(ArrayList<Notes> reminderNotes){
        ArrayList<Notes> reminderNotesNew = new ArrayList<>();
        Calendar today = Calendar.getInstance();
        double dayConverter = 24 * 60 * 60 * 1000;
        double diff;
        int day=0,week=0,month=0,all=0;
        int dayDone=0,weekDone=0,monthDone=0,allDone=0;
        ArrayList<Integer> list=new ArrayList<Integer>();

                 //today's reminders
                for(Notes reminderNote : reminderNotes){
                    diff = (reminderNote.getTime().getTimeInMillis() - today.getTimeInMillis()) / dayConverter;
                    if(diff >= 0 && diff < 1){
                        if(reminderNote.getIsDone()){
                            dayDone++;
                        }
                        day++;}
                }

                //week's reminders
                for(Notes reminderNote : reminderNotes){
                    diff = (reminderNote.getTime().getTimeInMillis() - today.getTimeInMillis()) / dayConverter;
                    if(diff >= 0 && diff < 7){
                        if(reminderNote.getIsDone()){
                            weekDone++;
                        }
                        week++;}
                }

                //month's reminders
                for(Notes reminderNote : reminderNotes) {
                    diff = (reminderNote.getTime().getTimeInMillis() - today.getTimeInMillis()) / dayConverter;
                    if (diff >= 0 && diff < 30) {
                        if(reminderNote.getIsDone()){
                            monthDone++;
                        }
                        month++;}
                }
                for(Notes reminderNote : reminderNotes){
                    if(reminderNote.getIsDone()){
                        allDone++;
                    }
                }

        list.add(day);
        list.add(week);
        list.add(month);
        list.add(reminderNotes.size());
        list.add(dayDone);
        list.add(weekDone);
        list.add(monthDone);
        list.add(allDone);

        return list;
    }

}
