package com.example.lenovo.mynotebook.db;

public class DBSchema {
    public static final String DB_NAME="note.db";

    public static final int DB_VERSION=1;

    public static class DataClassTable{
        public static final String TABLE_NAME="myNoteClassTable";
        public static final String COLUMN_ID="_ID";
        public static final String COLUMN_NAME="name";
        public static final String COLUMN_TIME="time";
        public static final String COLUMN_TYPE="type";
        public static final String COLUMN_CONTENT="content";

        public static String CREATE_MYTABLE="CREATE TABLE "
                + DBSchema.DataClassTable.TABLE_NAME +" ("
                + DBSchema.DataClassTable.COLUMN_ID +" INTEGER PRIMARY KEY AUTOINCREMENT, "+
                DBSchema.DataClassTable.COLUMN_NAME+" TEXT, "+
                DBSchema.DataClassTable.COLUMN_TIME+" TEXT, "+
                DBSchema.DataClassTable.COLUMN_TYPE+" TEXT, "+
                DBSchema.DataClassTable.COLUMN_CONTENT+" TEXT)";
    }
}
