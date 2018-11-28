package com.example.lenovo.mynotebook.repository;

import android.content.Context;

import com.example.lenovo.mynotebook.db.DBdataSource;
import com.example.lenovo.mynotebook.model.NoteInfo;

import java.util.ArrayList;
import java.util.List;

public class Myrepository {
    private Context context;

    private List<NoteInfo> noteInfoList;

    public Myrepository(Context context){
        this.context=context;
    }

    /*
    从数据库返回记事本的信息
     */
    public  List<NoteInfo> getNoteInfo(){

        noteInfoList= new ArrayList<NoteInfo>();
        DBdataSource d=new DBdataSource(context);
        d.open();
        noteInfoList=d.selectAll();
        d.close();
        if(noteInfoList!=null)
            return noteInfoList;
        else
            return null;
    }
}