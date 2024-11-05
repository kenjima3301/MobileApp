package com.example.lab03;

import java.util.ArrayList;
import java.util.List;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
public class DatabaseHandler extends SQLiteOpenHelper {
    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 1;
    // Database Name
    private static final String DATABASE_NAME = "TasksManager";
    // Contacts table name
    private static final String TABLE_TaskS = "Tasks";
    // Contacts Table Columns names
    private static final String KEY_ID = "maSV";
    private static final String KEY_NAME = "hoTen";
    private static final String KEY_BIRTH = "ngaySinh";
    private static final String KEY_TYPE = "gioiTinh";
    private static final String KEY_CLASS = "lop";
    private static final String KEY_FACULTY = "khoa";

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TaskS_TABLE = "CREATE TABLE " +
                TABLE_TaskS + "("
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + KEY_NAME + " TEXT, "
                + KEY_BIRTH + " TEXT, "
                + KEY_TYPE + " TEXT, "
                + KEY_CLASS + " TEXT, "
                + KEY_FACULTY + " TEXT"
                + ")";
        db.execSQL(CREATE_TaskS_TABLE);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TaskS);
        // Create tables again
        onCreate(db);
    }

    // Adding new contact
    public long addTask(Task Task) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_NAME, Task.getHoTen());
        values.put(KEY_BIRTH, Task.getNgaySinh());
        values.put(KEY_TYPE, Task.getGioiTinh());
        values.put(KEY_CLASS, Task.getLop());
        values.put(KEY_FACULTY, Task.getKhoa());

        // Inserting Row
        long id = db.insert(TABLE_TaskS, null, values);
        db.close(); // Close database connection
        return id;
    }

    // Getting single Task
    public Task getTask(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_TaskS,
                new String[] { KEY_ID, KEY_NAME, KEY_BIRTH, KEY_TYPE, KEY_CLASS, KEY_FACULTY },
                KEY_ID + "=?",
                new String[] { String.valueOf(id) },
                null,
                null,
                null,
                null);
        if (cursor != null) {
            cursor.moveToFirst();
        }
        // Return null nếu không tìm thấy Contact
        if (cursor == null || !cursor.moveToFirst()) {
            return null;
        }
        Task Task = new Task(Integer.parseInt(
                cursor.getString(0)),   // getMaSV
                cursor.getString(1),    // getHoTen
                cursor.getString(2),    // getNgaySinh
                cursor.getString(3),    // getGioiTinh
                cursor.getString(4),    // getLop
                cursor.getString(5)     // getKhoa
        );
        return Task;
    }

    // Getting All Tasks
    public List<Task> getAllTasks() {
        List<Task> TaskList = new ArrayList<Task>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_TaskS;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Task Task = new Task();
                Task.setMaSV(Integer.parseInt(cursor.getString(0)));
                Task.setHoTen(cursor.getString(1));
                Task.setNgaySinh(cursor.getString(2));
                Task.setGioiTinh(cursor.getString(3));
                Task.setLop(cursor.getString(4));
                Task.setKhoa(cursor.getString(5));
                // Adding Contact to list
                TaskList.add(Task);
            } while (cursor.moveToNext());
        }
        // return Task list
        return TaskList;
    }

    // Updating single contact
    public int updateTask(Task Task) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_NAME, Task.getHoTen());
        values.put(KEY_BIRTH, Task.getNgaySinh());
        values.put(KEY_TYPE, Task.getGioiTinh());
        values.put(KEY_CLASS, Task.getLop());
        values.put(KEY_FACULTY, Task.getKhoa());

        // Updating row
        return db.update(TABLE_TaskS, values,
                KEY_ID + " = ?",
                new String[]{String.valueOf(Task.getMaSV())}
        );
    }

    // Deleting single contact
    public void deleteTask(Task Task) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_TaskS, KEY_ID + " = ?",
                new String[] { String.valueOf(Task.getMaSV()) });
        db.close();
    }

    // Deleting all Tasks
    public void deleteAllTasks() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_TaskS, null, null); // Xóa tất cả các hàng trong bảng Tasks
        db.close();
    }
}
