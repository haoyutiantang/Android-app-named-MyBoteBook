package com.example.lenovo.mynotebook;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.os.Bundle;

import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.SparseBooleanArray;
import android.view.ActionMode;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lenovo.mynotebook.adapter.DataAdapter;
import com.example.lenovo.mynotebook.db.DBdataSource;
import com.example.lenovo.mynotebook.function.SearchNoteAsyncTask;
import com.example.lenovo.mynotebook.model.NoteInfo;

import java.util.ArrayList;
import java.util.List;

public class MainActivity
        extends Activity
        implements SearchNoteAsyncTask.IExplorerUIComponent {

    private  Typeface tf;
    private AutoCompleteTextView acTextView = null;
    private Intent intent = null;
    private Button addNote = null;
    private ListView noteList = null;
    private TextView emptyView;
    private DBdataSource dBdataSource = null;
    private SearchNoteAsyncTask searchNoteAsyncTask = null;
    private DataAdapter dataAdapter = null;
    private Bundle NoteFlag;//Bundle数据包，用来传递一个NoteInfo对象
    private NoteInfo noteInfo = null;
    private List<NoteInfo> list = null;
    //代表ActionBar进入了不同的状态
    private ActionMode actionMode = null;
    ArrayAdapter<String> adapter = null;
    private  String nameData[]=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        getWindow().setBackgroundDrawableResource(R.drawable.timg);

        //提醒用户设置权限
        if (ContextCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.CAMERA},
                    1);
        }

        //获取一个新字体
        AssetManager mgr=MainActivity.this.getAssets();
        tf=Typeface.createFromAsset(mgr, "fonts/new.ttf");

        acTextView=(AutoCompleteTextView)findViewById(R.id.id_autotextView);
        addNote = (Button) findViewById(R.id.addNote);
        noteList = (ListView) findViewById(R.id.notelist);
        dBdataSource = new DBdataSource(this);

        /*
        添加笔记的按钮设置点击事件
         */
        addNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //跳转到编辑界面
                intent = new Intent(MainActivity.this, edit_activity.class);
                NoteFlag = new Bundle();
                NoteFlag.putSerializable("flag", noteInfo);//该标记代表是否是新建的笔记，传递一个NoteInfo对象
                intent.putExtras(NoteFlag);
                startActivity(intent);
            }
        });

        /*
        设置AutoCompleteTextView点击事件
         */
        acTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                intent = new Intent(MainActivity.this, edit_activity.class);
                //该标记代表是否是新建的笔记，传递一个NoteInfo对象
                NoteFlag = new Bundle();
                String goalName=parent.getItemAtPosition(position).toString();
                NoteFlag.putSerializable("flag", dBdataSource.getByName(goalName));
                intent.putExtras(NoteFlag);
                startActivity(intent);
            }
        });

        /*
        添加ListView长按多选事件
         */
        noteList.setMultiChoiceModeListener(mActionModeCallback);

        /*
        添加ListView点击事件
         */
        noteList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //跳转到编辑界面
                intent = new Intent(MainActivity.this, edit_activity.class);
                //该标记代表是否是新建的笔记，传递一个NoteInfo对象
                NoteFlag = new Bundle();
                NoteFlag.putSerializable("flag", (NoteInfo) parent.getAdapter().getItem(position));
                intent.putExtras(NoteFlag);
                startActivity(intent);
            }
        });

    }

    private AbsListView.MultiChoiceModeListener mActionModeCallback = new AbsListView.MultiChoiceModeListener() {
        @Override
        public void onItemCheckedStateChanged(ActionMode mode, int position, long id, boolean checked) {
            mode.setSubtitle("己选中：" + noteList.getCheckedItemCount());
        }

        //当数据行的选中状态发生改变时……
        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            //实例化在ActionBar上显示的上下文菜单
            MenuInflater inflater = mode.getMenuInflater();
            inflater.inflate(R.menu.my_listview_ctx_menu, menu);

            //使用setTitle()，用大标题显示信息，使用setSubtitle，会在大标题下显示小标题
            mode.setTitle("进入ActionMode");
            mode.setSubtitle("己选中：" + noteList.getCheckedItemCount());

            actionMode = mode;
            return true;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            return false;
        }

        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            switch (item.getItemId()) {
                //用户选择删除
                case R.id.action_delete:
                    //获取用户选中的所有记录
                    List<NoteInfo> objs = new ArrayList<>();
                    SparseBooleanArray array = noteList.getCheckedItemPositions();
                    for (int i = 0; i < array.size(); ++i) {
                        if (array.valueAt(i)) {
                            objs.add(list.get(array.keyAt(i)));
                        }
                    }
                    //从数据源中删除数据对象
                    for (NoteInfo obj : objs) {
                        dBdataSource.deleteById(obj.getId());
                        dataAdapter.deleteItem(obj);
                    }
                    //更新显示
                    dataAdapter.notifyDataSetChanged();
                    //退出多选模式
                    mode.finish();
                    return true;
            }
            return false;
        }

        @Override
        public void onDestroyActionMode(ActionMode mode) {

        }
    };


        ;

        /*
        初始化ListView
         */
        public void initListView() {
            //执行异步查询功能
            searchNoteAsyncTask = new SearchNoteAsyncTask(MainActivity.this, this);
            searchNoteAsyncTask.execute();
        }

        @Override
        protected void onResume() {
            super.onResume();
            dBdataSource.open();
            initListView();
        }

        @Override
        protected void onPause() {
            super.onPause();
            dBdataSource.close();
        }

        @Override
        public void showNoteInfos(List<NoteInfo> Info) {

            dataAdapter = new DataAdapter(MainActivity.this, Info,tf);
            list = Info;
            noteList.setAdapter(dataAdapter);
            setName();
            adapter = new ArrayAdapter<String>(MainActivity.this,
                    android.R.layout.simple_dropdown_item_1line, nameData);
            acTextView.setAdapter(adapter);
        }

        public void setName(){
            nameData= new String[list.size()];
            int i=0;
            for(NoteInfo noteInfo:list){
                nameData[i]=noteInfo.getName();
                i++;
            }
        }

    private long exitTime = 0;
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            if (System.currentTimeMillis() - exitTime > 2000) {
                Toast.makeText(getApplicationContext(), "再按一次退出程序", Toast.LENGTH_SHORT).show();
                exitTime = System.currentTimeMillis();
            } else {
                finish();
                System.exit(0);
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


}

