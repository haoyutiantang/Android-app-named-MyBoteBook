package com.example.lenovo.mynotebook.adapter;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.lenovo.mynotebook.R;
import com.example.lenovo.mynotebook.db.DBdataSource;
import com.example.lenovo.mynotebook.model.NoteInfo;

import java.io.File;
import java.util.List;

/*
自定义ListView适配器
 */
public class DataAdapter extends BaseAdapter {

    private Context mContext=null;
    private int mResourceId=0;
    List<NoteInfo> noteInfoList;
    private DBdataSource dBdataSource = null;
    private Typeface tf;
    public DataAdapter( Context context,List<NoteInfo> objects,Typeface tfs) {
        noteInfoList=objects;
        mContext=context;
        mResourceId= R.layout.item_of_listview;
        tf=tfs;
    }

    public void deleteItem(NoteInfo obj){
        noteInfoList.remove(obj);
    }
    @Override
    public int getCount() {
        return noteInfoList.size();
    }

    @Override
    public Object getItem(int position) {
        return noteInfoList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //通过自定义字体生成字体对象

        NoteViewHolder noteViewHolder=null;
        View row=convertView;
        if(convertView==null){
            LayoutInflater inflater = LayoutInflater.from(mContext);
            convertView=inflater.inflate(mResourceId,parent,false);
            noteViewHolder=new NoteViewHolder();
            noteViewHolder.nameTextView=(TextView)convertView
                    .findViewById(R.id.name_content);
            noteViewHolder.nameTextView.setTypeface(tf);
            noteViewHolder.timeTextView=(TextView)convertView
                    .findViewById(R.id.text_time_content);
            noteViewHolder.timeTextView.setTypeface(tf);
            noteViewHolder.typeTextView=(TextView)convertView
                    .findViewById(R.id.text_typename);
            noteViewHolder.typeTextView.setTypeface(tf);
            convertView.setTag(noteViewHolder);
        }

        Log.i("i","加载子项");
        fillNoteItem((NoteInfo) getItem(position),(NoteViewHolder)convertView.getTag());

        return convertView;
    }



    private void fillNoteItem(NoteInfo info,NoteViewHolder noteViewHolder){
        noteViewHolder.nameTextView.setText(info.getName());
        noteViewHolder.timeTextView.setText(info.getDate());
        noteViewHolder.typeTextView.setText(info.getType());
    }



    /*
    ViewHolder设计模式
     */
    private static class NoteViewHolder{
        public TextView nameTextView=null;
        public TextView timeTextView=null;
        public TextView typeTextView=null;
    }
}