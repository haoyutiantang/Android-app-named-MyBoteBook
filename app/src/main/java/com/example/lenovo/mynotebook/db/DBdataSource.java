package com.example.lenovo.mynotebook.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.lenovo.mynotebook.model.NoteInfo;

import java.util.ArrayList;
import java.util.List;

/*
实现对数据列表的CRUD
 */
public class DBdataSource {

    private static final String TAG="DBdataSource";
    private SQLiteDatabase mDatabase;
    private MyDBhelper myDBHelper;
    private Context mContext;

    public DBdataSource(Context context)
    {
        mContext=context;
        myDBHelper=new MyDBhelper(context);
    }

    //打开一个可写的数据库,注意要在主Activity的onResume方法里调用
    public void open(){
        Log.d(TAG, "database created and opened.");
        mDatabase=myDBHelper.getWritableDatabase();
    }
    //关闭一个数据库，注意要在主Activity的onPause方法里调用
    public void close(){
        Log.d(TAG, "database closed.");
        mDatabase.close();
    }


    public void deleteById(int id){
        mDatabase.delete(DBSchema.DataClassTable.TABLE_NAME,"_ID=?",new String[]{""+id});
    }

    /*
    插入一条记录
     */
    public long insert(NoteInfo obj){
        if(obj==null){
            return -1;
        }
        ContentValues values=new ContentValues();
        values.put(DBSchema.DataClassTable.COLUMN_NAME,obj.getName());
        values.put(DBSchema.DataClassTable.COLUMN_TIME,obj.getDate());
        values.put(DBSchema.DataClassTable.COLUMN_CONTENT,obj.getContent());
        values.put(DBSchema.DataClassTable.COLUMN_TYPE,obj.getType());
        long resultID=mDatabase.insert(DBSchema.DataClassTable.TABLE_NAME,null,values);

        return resultID;
    }

    /*
    提取数据库中的所有数据对象
     */
    public List<NoteInfo> selectAll(){
        List<NoteInfo> objs=null;
        Cursor cursor=mDatabase.query(
                DBSchema.DataClassTable.TABLE_NAME,   //表名
                new String[]{               //要提取的字段名
                        DBSchema.DataClassTable.COLUMN_ID,
                        DBSchema.DataClassTable.COLUMN_NAME,
                        DBSchema.DataClassTable.COLUMN_TIME,
                        DBSchema.DataClassTable.COLUMN_TYPE,
                        DBSchema.DataClassTable.COLUMN_CONTENT},
                null,   //where
                null,   //where params
                null,   //groupby
                null,   //having
                null    //orderby
        );
        objs=cursorToList(cursor);
        return objs;
    }

    //将Cursor所引用的所有数据全部读取出来
    private List<NoteInfo> cursorToList(Cursor cursor){
        if(cursor==null){
            return null;
        }
        List<NoteInfo> objs=new ArrayList<>();
        NoteInfo obj=null;
        if(cursor.moveToFirst()){
            while(!cursor.isAfterLast()){
                obj=readFromCursor(cursor);
                if(obj!=null){
                    objs.add(obj);
                }
                cursor.moveToNext();
            }
        }
        return  objs;
    }

    //从当前Cursor中读取数据，创建一个NoteInfo对象
    private NoteInfo readFromCursor(Cursor cursor){
        if(cursor==null){
            return null;
        }
        NoteInfo obj=new NoteInfo();
        obj.setId((int)(cursor.getLong(cursor.getColumnIndex(DBSchema.DataClassTable.COLUMN_ID))));
        obj.setContent((cursor.getString(cursor.getColumnIndex(DBSchema.DataClassTable.COLUMN_CONTENT))));
        obj.setDate((cursor.getString(cursor.getColumnIndex(DBSchema.DataClassTable.COLUMN_TIME))));
        obj.setName((cursor.getString(cursor.getColumnIndex(DBSchema.DataClassTable.COLUMN_NAME))));
        obj.setType((cursor.getString(cursor.getColumnIndex(DBSchema.DataClassTable.COLUMN_TYPE))));
        return obj;
    }

    /*
    检查名字是否重复
     */
    public boolean findByName(String name){
        Cursor cursor=mDatabase.query(DBSchema.DataClassTable.TABLE_NAME,
                    new String[]{DBSchema.DataClassTable.COLUMN_NAME},
                "name=?",
                new String[]{name},
                null,
                null,
                null,
                null
                );
        if(cursor.moveToFirst()==false)
            return false;
        else return true;
    }

/*
通过笔记的ID来更新笔记的样式
 */
    public void updateType(int noteId,String type){
        ContentValues values = new ContentValues();
        values.put("type",type);
        mDatabase.update(DBSchema.DataClassTable.TABLE_NAME,values,
                "_ID=?",new String[]{""+noteId});
    }
    /*
   通过笔记的ID来更新笔记的名字
    */
    public void updateName(int noteId,String newName){
        ContentValues values = new ContentValues();
        values.put("name",newName);
        mDatabase.update(DBSchema.DataClassTable.TABLE_NAME,values,
                "_ID=?",new String[]{""+noteId});
    }

    /*
    更新ID为noteID的笔记的内容
     */
    public void updateContent(int noteId,String newContent){
        ContentValues values = new ContentValues();
        values.put("content",newContent);
        mDatabase.update(DBSchema.DataClassTable.TABLE_NAME,values,
                "_ID=?",new String[]{""+noteId});
    }

    /*
    通过名字返回一个NoteInfo对象
     */
    public NoteInfo getByName(String InfoName){
        Cursor cursor=mDatabase.query(
                DBSchema.DataClassTable.TABLE_NAME,   //表名
                new String[]{               //要提取的字段名
                        DBSchema.DataClassTable.COLUMN_ID,
                        DBSchema.DataClassTable.COLUMN_NAME,
                        DBSchema.DataClassTable.COLUMN_TIME,
                        DBSchema.DataClassTable.COLUMN_TYPE,
                        DBSchema.DataClassTable.COLUMN_CONTENT},
                "name=?",   //where
                new String[]{InfoName},   //where params
                null,   //groupby
                null,   //having
                null    //orderby
        );
        if(cursor.moveToFirst())
        return readFromCursor(cursor);
        else return null;
    }

}