package com.example.lenovo.mynotebook.function;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Environment;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;
import android.util.DisplayMetrics;
import android.util.Log;
import android.widget.EditText;
import android.widget.ScrollView;

import com.example.lenovo.mynotebook.MainActivity;
import com.example.lenovo.mynotebook.R;
import com.example.lenovo.mynotebook.db.DBdataSource;
import com.example.lenovo.mynotebook.model.NoteInfo;
import com.example.lenovo.mynotebook.ui.MessageBox;
import com.example.lenovo.mynotebook.ui.UiHelper;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
/*
封装一些操作编辑器的方法
 */
public class NoteWrapper {



    /*
        此接口的作用是想edit_activity传送一个新的NoteInfo
    */
    public interface INoteInfo{

        void getANewNote(NoteInfo noteInfo);
    }

    public NoteWrapper (INoteInfo iNoteInfo,Context contexts) {
        this.iNoteInfo=iNoteInfo;
        context=contexts;
    }

    private static Context context;
    private static String TAG = "Listview and ScrollView item 截图:";
    public static final String PARTENIMAGEPATH="/sdcard/myImage/[0-9]*.jpg";
    private INoteInfo iNoteInfo=null;
    private DBdataSource dBdataSource=null;
    private NoteInfo noteInfo=null;
    private MessageBox messageBox=null;
    private Intent intent=null;
    private String name;
    private String type;


    /*
    将插入如的图片保存到文件并返回路径名
     */
    public String SaveToFile(String FilePath,String name,Bitmap bitmap){
        FileOutputStream FOut = null;
        File file = new File(FilePath);
        // File file=StorageUtils.getOwnCacheDirectory
        file.mkdirs();// 创建文件夹
        String fileName = FilePath+name;
        File pfile = new File(FilePath,name);
        try {
            FOut = new FileOutputStream(pfile);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, FOut);// 把数据写入文件
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }finally {
            try {
                FOut.flush();
                FOut.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return fileName;
    }

    /*
    内容中是否包含图片
     */
    public boolean isHaveImage(String content){
        Pattern p=Pattern.compile(PARTENIMAGEPATH);
        Matcher m=p.matcher(content);
        if(m.find())
            return true;
        else
            return false;
    }

    /*
   将截图保存到文件
    */
    public File bitMap2File(Bitmap bitmap) {
        String path = "";
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
            path = Environment.getExternalStorageDirectory() + File.separator;//保存到sd根目录下
        }


        //        File f = new File(path, System.currentTimeMillis() + ".jpg");
        File f = new File(path, "share" + ".jpg");
        if (f.exists()) {
            f.delete();
        }
        try {
            FileOutputStream out = new FileOutputStream(f);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.flush();
            out.close();
            bitmap.recycle();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            return f;
        }
    }

    /*
    截图压缩
     */
    public Bitmap compressImage(Bitmap image) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 10, baos);//质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
        int options = 100;
        while (baos.toByteArray().length / 1024 > 400) {  //循环判断如果压缩后图片是否大于400kb,大于继续压缩（这里可以设置大些）
            baos.reset();//重置baos即清空baos
            image.compress(Bitmap.CompressFormat.JPEG, options, baos);//这里压缩options%，把压缩后的数据存放到baos中
            options -= 10;//每次都减少10
        }
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());//把压缩后的数据baos存放到ByteArrayInputStream中
        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);//把ByteArrayInputStream数据生成图片
        return bitmap;
    }

    /*
    获取ScrollView的截屏长图
     */
    public static Bitmap getScrollViewBitmap(ScrollView scrollView, String picpath) {
        int h = 0;
        Bitmap bitmap=null;
        // 获取listView实际高度
        for (int i = 0; i < scrollView.getChildCount(); i++) {
            h += scrollView.getChildAt(i).getHeight();
        }
        //从drable文件中获取背景图片转换成Bitmap
       Bitmap icon = BitmapFactory.decodeResource(context.getResources(), R.drawable.timg);
        //然后根据ScrollView的实际高度拉伸背景图片
       icon=Bitmap.createScaledBitmap(icon,scrollView.getWidth(),h,true);
        Log.d(TAG, "实际高度:" + h);
        Log.d(TAG, " 高度:" + scrollView.getHeight());
        // 创建对应大小的bitmap,此时这个Bitmap还未画上任何团
        bitmap = Bitmap.createBitmap(scrollView.getWidth(), h,
                Bitmap.Config.ARGB_8888);
        final Canvas canvas = new Canvas(bitmap);
        //将拉伸好的背景图片覆盖上去,画布就创建好了
       canvas.drawBitmap(icon,0,0,null);
       //将ScrollView的内容画到画布上
        scrollView.draw(canvas);
        return bitmap;
    }


    //计算图片的缩放值
    public int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            final int heightRatio = Math.round((float) height / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }
        return inSampleSize;
    }


    /*
   获取图片的比例
    */
    public BitmapFactory.Options  getBili(int reqWidth, int reqHeight){
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        
        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return options;
    }

    /*
    进一步修饰图片大小
     */
    public Bitmap jinyibu(Bitmap bitmap,Context context){
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        int w_screen = dm.widthPixels;
        int w_width = w_screen;
        int b_width = bitmap.getWidth();
        int b_height = bitmap.getHeight();
        int w_height = w_width * b_height / b_width;
        bitmap = Bitmap.createScaledBitmap(bitmap, w_width, w_height, false);
        return bitmap;
    }

    /*
    解析内容：用到了正则表达式
    获取EditView的整体效果
    返回SpannableString对象
     */
    public SpannableString getSpannedString(String content, Context context){
        SpannableString ss = new SpannableString(content);
        Pattern p=Pattern.compile("/sdcard/myImage/[0-9]*.jpg");
        Matcher m=p.matcher(content);
        while(m.find()){
            Bitmap bm = BitmapFactory.decodeFile(m.group());
            Bitmap rbm=bm;
            ImageSpan span = new ImageSpan(context, rbm);
            ss.setSpan(span, m.start(), m.end(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        return ss;
    }

    /*
    将一个笔记的所有信息保存到数据库
     */
    public  void SaveToDB(final String content, final Context context, NoteInfo noteInfos){
        dBdataSource=new DBdataSource(context);
        this.noteInfo=noteInfos;
        if(noteInfo==null){//如果是新建的笔记
            messageBox=new MessageBox(context);

            messageBox.showInputDialog("新建笔记", "请输入名字", null,
                    new MessageBox.ISimpleInputDialogButtonClick() {
                        @Override
                        public void doSomething(String userInput) {
                            if(StringFunction.isNullOrEmpty(userInput)){//命名不能为空
                                UiHelper.toastShowMessageShort(context,"名字不能为空,请重新输入");
                                return;
                            }
                            dBdataSource.open();
                            if(dBdataSource.findByName(userInput)){//如果这个名字已经存在
                                UiHelper.toastShowMessageShort(context,"名字已经存在");
                                dBdataSource.close();
                                return;
                            }
                            //dBdataSource.close();
                            name=userInput;
                            noteInfo=new NoteInfo();
                            String date=new MyDate().getDateAndTime();
                            if(isHaveImage(content))
                            type="图文";
                            else
                                type="文字";
                            noteInfo.setDate(date);
                            noteInfo.setContent(content);
                            noteInfo.setType(type);
                            noteInfo.setName(name);
                           // dBdataSource.open();
                            dBdataSource.insert(noteInfo);
                            UiHelper.toastShowMessageShort(context,"新建成功");
                            dBdataSource.close();
                            iNoteInfo.getANewNote(getNoteInfofromDb());
                            /*
                            保存成功就跳转回主页面
                             */
                          // intent=new Intent();
                         // intent.setClass(context,MainActivity.class);
                        // context.startActivity(intent);
                        }
                    },null);

        }else{
            //更改内容
            name=noteInfo.getName();
            dBdataSource.open();
            if(isHaveImage(content)){
                if(!noteInfo.getType().equals("图文")){
                    dBdataSource.updateType(noteInfo.getId(),"图文");
                }
            }else{
                if(!noteInfo.equals("文字")){
                    dBdataSource.updateType(noteInfo.getId(),"文字");
                }
            }
            dBdataSource.updateContent(noteInfo.getId(),content);
            dBdataSource.close();
            iNoteInfo.getANewNote(getNoteInfofromDb());
            UiHelper.toastShowMessageShort(context,"保存成功");
        }

      //  iNoteInfo.getANewNote(getNoteInfofromDb());
    }

    /*
    给笔记重新起名字
     */
    public void noteRename(final Context context, NoteInfo noteInfos){
        dBdataSource=new DBdataSource(context);
        this.noteInfo=noteInfos;
        messageBox=new MessageBox(context);

        messageBox.showInputDialog("重命名", "请输入新名字", noteInfo.getName(),
                new MessageBox.ISimpleInputDialogButtonClick() {
            @Override
            public void doSomething(String userInput) {
                if(userInput.equals(noteInfo.getName())){
                    UiHelper.toastShowMessageShort(context,"名字没有改变");
                    return;
                }
                dBdataSource.open();
                if(dBdataSource.findByName(userInput)){//如果这个名字已经存在
                    UiHelper.toastShowMessageShort(context,"名字已经存在");
                    dBdataSource.close();
                    return;
                }
                name=userInput;
                dBdataSource.updateName(noteInfo.getId(),name);
                dBdataSource.close();
                iNoteInfo.getANewNote(getNoteInfofromDb());
            }
        },null);
    }

    /*
    从数据库获得一个全新的NoteInfo
     */
    public NoteInfo getNoteInfofromDb() {
        dBdataSource.open();
      NoteInfo  NewnoteInfo=dBdataSource.getByName(name);
        dBdataSource.close();
        return NewnoteInfo;
    }


    /*
    从数据删除该笔记并跳转回到MainActivity
    */
    public  void deleteNote(final NoteInfo noteInfos, final Context context){
        dBdataSource=new DBdataSource(context);
        messageBox=new MessageBox(context);
        messageBox.showOKOrCancelDialog("是否删除笔记","提示", new MessageBox.IButtonClick() {
            @Override
            public void doSomething() {
                dBdataSource.open();
                dBdataSource.deleteById(noteInfos.getId());
                dBdataSource.close();
                UiHelper.toastShowMessageShort(context,"删除成功");
                intent=new Intent();
                intent.setClass(context,MainActivity.class);
                context.startActivity(intent);
            }
        },null);
    }
}

