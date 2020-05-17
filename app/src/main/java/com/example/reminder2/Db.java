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

            note.setId(cursor.getInt(0));
            note.setTitle(cursor.getString(1));
            note.setDetail(cursor.getString(2));
            note.setCategory(cursor.getString(3));
            note.setIsDone(Boolean.parseBoolean(cursor.getString(5)));
            notes.add(note);
        }

        return notes;
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_REMINDERS);
        onCreate(db);
    }
}