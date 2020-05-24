package com.example.reminder2;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class Db extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "reminder";

    private static final String TABLE_REMINDERS = "events";

    private List<String> sql = new ArrayList<>();

    public Db(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        sql.add("CREATE TABLE " + TABLE_REMINDERS + " (id INTEGER PRIMARY KEY AUTOINCREMENT, title TEXT, detail TEXT, time LONG, category TEXT,isDone TEXT" + ")");

        for (String sqlCommand : sql) {
            Log.d("Db", "SQL : " + sqlCommand);
            db.execSQL(sqlCommand);
        }
    }

    public void insertNotes(Notes notes) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("title", notes.getTitle());
        values.put("detail", notes.getDetail());
        values.put("category", notes.getCategory());
        values.put("time", notes.getTime().getTimeInMillis());
        values.put("isDone", notes.getIsDone());

        db.insert(TABLE_REMINDERS, null, values);
        db.close();
    }

    public ArrayList<Notes> getAllNotes() throws ParseException {
        ArrayList<Notes> notes = new ArrayList<Notes>();
        SQLiteDatabase db = this.getWritableDatabase();

        // String sqlQuery = "SELECT  * FROM " + TABLE_REMINDERS;
        // Cursor cursor = db.rawQuery(sqlQuery, null);

        Cursor cursor = db.query(TABLE_REMINDERS, new String[]{"id", "title", "detail","category", "time", "isDone"},
                null, null, null, null, null);
        while (cursor.moveToNext()) {
            Notes note = new Notes();
            Calendar cal = Calendar.getInstance();
            long reminderDate = cursor.getLong(4);
            cal.setTimeInMillis(reminderDate);

            boolean isDone=false;
            if(cursor.getString(5).equalsIgnoreCase("1")) {
                isDone=true;
            }

            note.setId(cursor.getInt(0));
            note.setTitle(cursor.getString(1));
            note.setDetail(cursor.getString(2));
            note.setCategory(cursor.getString(3));
            note.setTime(cal);
            note.setIsDone(isDone);
            notes.add(note);
        }

        return notes;
    }

    public void deleteReminderNote(Notes reminderNotes) {
        SQLiteDatabase db = this.getWritableDatabase();

        db.delete(TABLE_REMINDERS,"id = ?",
                new String[] { String.valueOf(reminderNotes.getId()) });

        db.close();
    }

    public Notes getNoteByID(int id){

        Notes notes = new Notes();
        String selectQuery = "SELECT * FROM " + TABLE_REMINDERS+ " WHERE id = " + id;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        cursor.moveToFirst();

        if(cursor.getCount() > 0){

            Calendar cal = Calendar.getInstance();
            long reminderDate = cursor.getLong(3);
            cal.setTimeInMillis(reminderDate);

            notes.setId(cursor.getInt(0));
            notes.setTitle( cursor.getString(1));
            notes.setDetail( cursor.getString(2));
            notes.setCategory( cursor.getString(4));
            notes.setTime(cal);
            String done;
            done=cursor.getString(5);
            if(done.equalsIgnoreCase("0"))
                notes.setIsDone(false);
            else if(done.equalsIgnoreCase("1"))
                notes.setIsDone(true);
        }
        cursor.close();
        db.close();
        // return kitap
        return notes;
    }

    public int getNoteByTime(long time){

        Notes notes = new Notes();
        String selectQuery = "SELECT * FROM " + TABLE_REMINDERS+ " WHERE time = " + time;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        cursor.moveToFirst();

        if(cursor.getCount() > 0){

            Calendar cal = Calendar.getInstance();
            long reminderDate = cursor.getLong(3);
            cal.setTimeInMillis(reminderDate);

            notes.setId(cursor.getInt(0));
            notes.setTitle( cursor.getString(1));
            notes.setDetail( cursor.getString(2));
            notes.setCategory( cursor.getString(4));
            notes.setTime(cal);
            String done;
            done=cursor.getString(5);
            if(done.equalsIgnoreCase("0"))
                notes.setIsDone(false);
            else if(done.equalsIgnoreCase("1"))
                notes.setIsDone(true);
        }
        else
            return -1;
        cursor.close();
        db.close();
        // return kitap
        return notes.getId();
    }

    public void updateReminderNote(int id,String title,String detail,long time,String category,int isDone){
        SQLiteDatabase db = this.getWritableDatabase();

        String strSQL = "UPDATE " +TABLE_REMINDERS + " SET " +
                " title = '" + title + "', detail = '" + detail + "', time = " + time + ", category = '"+ category + "', isDone = " + isDone + " WHERE id = "+ id;

        db.execSQL(strSQL);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_REMINDERS);
        onCreate(db);
    }
}