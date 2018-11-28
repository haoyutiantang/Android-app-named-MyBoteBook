package com.example.lenovo.mynotebook.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MyDBhelper extends SQLiteOpenHelper {
    public MyDBhelper(Context context) {
        super(context,DBSchema.DB_NAME, null, DBSchema.DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DBSchema.DataClassTable.CREATE_MYTABLE);
        //android.widget.Toast.makeText(MainActivity.this,"数据库打开",Toast.LENGTH_SHORT).show();
        android.util.Log.i("main","数据库建立");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}

