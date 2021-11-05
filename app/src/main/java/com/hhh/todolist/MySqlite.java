package com.hhh.todolist;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.Nullable;

public class MySqlite extends SQLiteOpenHelper {
    private Context context;
    public MySqlite(@Nullable Context context, int version) {
        super(context, "tolist.db", null, version);
        this.context = context;
    }
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String sql="create table dolist(id integer primary key autoincrement," +
                "name text," +
                "time text," +
                "statue text)";
        sqLiteDatabase.execSQL(sql);

    }
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
    }
}
