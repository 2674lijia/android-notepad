package cqipc.lijia.note;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;


public class NewNoteActivity extends Activity implements View.OnClickListener, View.OnKeyListener, TextView.OnEditorActionListener {

    private static final int ADD_IMG_CODE = 201;
    private TextView tvTime;
    private ImageView cancel, save; //取消，保存
    private EditText etTitle, etContent; // 标题，文本内容
    private List<String> videoPath;//保存与之相关的视频路径
    private List<String> recordPath;//音频文件路径
    private Note note; //note对象
    private static final int NEW_NOTE_CODE=101;
    private static final int NO_CHANGE_NOTE=120;//表示没有改变
    private static final int CHANGE_NOTE=121;//表示已经改变
    private String stringBuilder; //临时保存文件内容
    private LinearLayout img,record,video; //打开图片
    private static final int ADD_RECORD_REQUEST_CODE = 200;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_note_activity);
        init();

    }
    //向EditText添加图片
    public void addImage(String imgPath){
        if(imgPath==null)
            return;
        etContent.setOnKeyListener(this);
        etContent.setOnEditorActionListener(this);
        //读取该路径图片并进行压缩包装成Bitmap对象
        Bitmap bitmap=ImgUtil.getImage(imgPath,this);
        //创建ImageSpan对象包装图片
        ImageSpan imageSpan = new ImageSpan(this, bitmap);
        String tempUrl ="<"+imgPath+">";
        //将图片路径存放到SpannableString
        SpannableString spannableString = new SpannableString(tempUrl);
        //用imageSpan替代tempUrl
        spannableString.setSpan(imageSpan, 0, tempUrl.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        // 将选择的图片追加到EditText中光标所在位置
        int index = etContent.getSelectionStart();
        System.out.print(index+"============="+etContent.getSelectionEnd());
        Editable edit_text = etContent.getEditableText();
        //
        if (index < 0 || index >= edit_text.length()) {
            edit_text.append(spannableString);
        } else {
            edit_text.insert(index, spannableString);
        }
        //添加图片后换行
        if(note==null){
            etContent.append("\n");
        }
    }
    public void init() {
        img=findViewById(R.id.img);
        img.setOnClickListener(this);
        cancel = findViewById(R.id.cancel);
        cancel.setOnClickListener(this);
        save = findViewById(R.id.save);
        save.setOnClickListener(this);
        tvTime = findViewById(R.id.tvTime);
        tvTime.setOnClickListener(this);
        tvTime.setText(new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(new Date()));
        etTitle = findViewById(R.id.etTitle);
        etTitle.setOnClickListener(this);
        etContent = findViewById(R.id.etContent);
        etContent.setOnClickListener(this);
        //获取点击的Itml对应Note的id
        String n_id = getIntent().getStringExtra("n_id");
        //如果没有获取到 则表示为新建文件
        if (n_id!=null) {
            //如果n_id不为空则需要读取已经存储的数据
            note = SqliteModel.selectNoteById(this, n_id); //获取文本
            videoPath = SqliteModel.selectVideoPathByNId(this, n_id);//获取相关联的视频及录音文件
            recordPath = SqliteModel.selectRecordPathByNId(this, n_id);
            note.setN_id(n_id);
            note.setRecordPathCounts(recordPath.size());
            note.setVedioPathCounts(videoPath.size());
            //显示到UI上
            etTitle.setText(note.getN_title());
            try {
                //读取存储在内部的文件
                FileInputStream in = openFileInput(note.getN_title());
                byte[] buffer=new byte[in.available()];
                in.read(buffer);
                stringBuilder=new String(buffer);
                System.out.println("stringBuilder = "+stringBuilder);
                substringContent();//将图片信息分离出来
            } catch (Exception e) {
                e.printStackTrace();
            }
        }else {
            videoPath=new ArrayList<>();
            recordPath=new ArrayList<>();
        }
    }
    /*
       点击事件监听
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.cancel:
                cancelNote();
                break;
            case R.id.save:
                saveNote();
                break;
            case R.id.img:
                Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, ADD_IMG_CODE);
                break;
            case R.id.record:
                Intent i = new Intent(MediaStore.Audio.Media.RECORD_SOUND_ACTION);
                startActivityForResult(i, ADD_RECORD_REQUEST_CODE);
                break;
        }
    }
    public void cancelNote() {
        String n_title = etTitle.getText().toString().trim();//获取标题
        String content=etContent.getText().toString();//获取文本内容
        if (note!=null){
            //如果是查看或修改文件的话 则判断 文本内容 标题信息是否有改变
            System.out.println("stringBuilder===="+stringBuilder+"  content==="+content+"---"+!stringBuilder.equals(content));
            if (!stringBuilder.equals(content)||!n_title.equals(note.getN_title())){
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                AlertDialog alertDialog = builder.setTitle("提示").setMessage("内容已改变，是否保存该备忘录？").setNegativeButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        saveNote();
                    }
                }).setPositiveButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        setResult(NO_CHANGE_NOTE);
                        finish();
                    }
                }).show();
            }else {
                setResult(NO_CHANGE_NOTE);
                finish();
            }
        }else {
            if (!n_title.equals("")){
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                AlertDialog alertDialog = builder.setTitle("提示").setMessage("是否保存").setNegativeButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        saveNote();
                    }
                }).setPositiveButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        setResult(NEW_NOTE_CODE);
                        finish();
                    }
                }).show();
            }else {
                setResult(NO_CHANGE_NOTE);
                finish();
            }
        }

    }

    public void saveNote() {
        String n_title = etTitle.getText().toString().trim();//获取标题
        String content=etContent.getText().toString();
        //为空则表示为新建
        if (note == null) {
            //判断是否合法
            if (n_title.equals("") || n_title.length() > 20) {
                Toast.makeText(this, "标题不能为空，且长度不能超过20！", Toast.LENGTH_SHORT).show();
            } else if (SqliteModel.selectNoteByTitle(this, n_title) != null) {
                Toast.makeText(this, "已存在该标题，请重命名！", Toast.LENGTH_SHORT).show();
            } else {
                //则进行保存操作
                note = new Note();
                note.setN_title(n_title);
                note.setN_time(tvTime.getText().toString());
                //产生唯一的ID
                note.setN_id(UUID.randomUUID().toString());
                Map<String, List<String>> pathMap = new HashMap<>();
                pathMap.put("videoPath", videoPath);
                pathMap.put("recordPath", recordPath);
                SqliteModel.insertNote(this, note, pathMap);
                try {
                    FileOutputStream outputStream = openFileOutput(n_title, Context.MODE_PRIVATE);
                    outputStream.write(content.getBytes());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                SqliteModel.insertRecordPath(this,recordPath,note.getN_id());
                SqliteModel.insertVideoPath(this,videoPath,note.getN_id());
                //返回到主页面
                setResult(CHANGE_NOTE);
                finish();
            }

        }else { //表示修改或者查看
            //如果标题或者内容发生改变则
            if (!note.getN_title().equals(n_title)||!stringBuilder.toString().equals(content)){
                if (n_title.equals("")){
                    Toast.makeText(this, "标题不能为空!", Toast.LENGTH_SHORT).show();
                }
                System.out.println(note.getN_id()+"--------------------");
                deleteFile(note.getN_title());//删除本来的文件
                note.setN_time(tvTime.getText().toString());
                note.setN_title(n_title);
                SqliteModel.updateNote(this,note);
                try {
                    FileOutputStream outputStream = openFileOutput(n_title, Context.MODE_PRIVATE);
                    outputStream.write(content.getBytes());
                } catch (Exception e) {
                    e.printStackTrace();
                }
               setResult(CHANGE_NOTE);
                finish();
            }else {                //没有改变 则返回主界面
                setResult(NO_CHANGE_NOTE);
                finish();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            //返回图片的路径
            case ADD_IMG_CODE:
                if (resultCode == RESULT_OK) {
                    //ImgUtil是实现的一个图片的处理类  加载图片的大小   可以防止OOM
                    String imgPath = ImgUtil.getImagePath(this, data.getData(), null);
                    addImage(imgPath);
                }
                break;
            //返回录音的路径
            case ADD_RECORD_REQUEST_CODE:
                Uri uri = data.getData();
                Cursor cursor = getContentResolver().query(uri, null, null, null, null);
                cursor.moveToFirst();
                int index = cursor.getColumnIndex(MediaStore.Audio.AudioColumns.DATA);
                String filePath = cursor.getString(index);
                recordPath.add(filePath);
                break;

        }
    }
    public void substringContent(){
        String content= stringBuilder;
        int start=0;
        int end=0;
        int len=0;
        for (int i=0;i<content.length();i++){
            char c=content.charAt(i);
            if(c=='<') {
                start = i;
            }else if (c=='>'){
                end=i;}
            if(end>start){
                if (start>len) {
                    String temp2 = content.substring(len, start);
                    etContent.append(temp2);
                    etContent.setSelection(etContent.getText().length());
                }
                String temp=content.substring(start+1, end);
                addImage(temp);
                len=end+1;
                end=0;
            }
        }

        if (content.charAt(content.length()-1)!='>'){
            etContent.append(content.substring(len,content.length()));
        }
    }
    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {
        System.out.println("=========222========"+etContent.getText());
       // Toast.makeText(this, keyCode+"======="+KeyEvent.KEYCODE_DEL, Toast.LENGTH_SHORT).show();
       // System.out.println(keyCode+"======="+KeyEvent.KEYCODE_DEL);
        if(keyCode == KeyEvent.KEYCODE_DEL) {
            System.out.println("=================="+event.getKeyCode());
        }
        return false;
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        System.out.println(event.getKeyCode()+"=================="+event.getKeyCode());
       // Toast.makeText(this, event.getKeyCode()+"======="+KeyEvent.KEYCODE_DEL, Toast.LENGTH_SHORT).show();
        return false;
    }
}
