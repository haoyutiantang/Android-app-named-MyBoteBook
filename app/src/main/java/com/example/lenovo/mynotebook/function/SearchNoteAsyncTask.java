package com.example.lenovo.mynotebook.function;

import android.content.Context;
import android.os.AsyncTask;

import com.example.lenovo.mynotebook.model.NoteInfo;
import com.example.lenovo.mynotebook.repository.Myrepository;

import java.util.List;

public class SearchNoteAsyncTask extends AsyncTask<Void ,Void,Void > {

    private List<NoteInfo> noteInfoList=null;
    private IExplorerUIComponent uiComponent=null;
    private Context context;


    /*
    MainActivity必须实现的接口,用来刷新ListView
     */
    public interface IExplorerUIComponent{
        /*
        显示记事本信息
         */
        void showNoteInfos(List<NoteInfo> Info);
    }

    /*
    构造方法，传递一个上下文和一个this
     */
    public SearchNoteAsyncTask(IExplorerUIComponent uiComponent, Context context){
        this.uiComponent=uiComponent;
        this.context=context;
    }

    @Override
    protected Void doInBackground(Void... voids) {

        noteInfoList=new Myrepository(context).getNoteInfo();

        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {

        if(uiComponent!=null){
            uiComponent.showNoteInfos(noteInfoList);
        }
        super.onPostExecute(aVoid);
    }
}
