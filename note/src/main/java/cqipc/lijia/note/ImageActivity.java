package cqipc.lijia.note;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;

public class ImageActivity extends Activity implements AdapterView.OnItemClickListener, View.OnClickListener {


    private static final int NO_CHANGE_NOTE=120;//表示没有改变
    ArrayList<String> imgAlbumPathList;
    ArrayList<AlbumMessage> albumList;
    private ListView imageList;
    private GridView gridView;
    private boolean isSelect = false;//选择状态 默认是未选择
    private AlbumListAdapter albumListAdapter;
    private GridViewAdapter gridViewAdapter;
    private Handler handler;
    private ArrayList<String> imgPathList;
    private ImageView mulu,back_img;
    private  DrawerLayout drawer;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        imgPathList = new ArrayList<>();
        albumList = new ArrayList<>();
        imgAlbumPathList=new ArrayList<>();
        addImgPath();
        setContentView(R.layout.activity_image);
        mulu=findViewById(R.id.mulu);
        mulu.setOnClickListener(this);
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        back_img=findViewById(R.id.back_img);
        back_img.setOnClickListener(this);

        albumListAdapter = new AlbumListAdapter(this, albumList);
        imageList = findViewById(R.id.imageList);
        imageList.setAdapter(albumListAdapter);
        imageList.setOnItemClickListener(this);

        gridViewAdapter = new GridViewAdapter(this, imgPathList);
        gridView = findViewById(R.id.gridView);
        gridView.setAdapter(gridViewAdapter);
        //设置长按选项监听

       // addImgPath();

        //加载完成
        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                File[] files = new File(albumList.get(0).getParentFilePath()).listFiles();
                imgPathList.clear();
                for (File file : files) {
                    imgPathList.add(file.getAbsolutePath());
                }
                albumListAdapter.notifyDataSetChanged();
                gridViewAdapter.notifyDataSetChanged();
            }
        };
    }

    //耗时操作 不再主线程操作
    public void addImgPath() {
        new Thread(new Runnable() {
            public void run() {
                Cursor cursor = MediaStore.Images.Media.query(getContentResolver(), MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                        null, null, null, null);
                while (cursor.moveToNext()) {
                    String imgPath = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
                    File parentFile = new File(imgPath).getParentFile();
                    //如果他没有父文件 则是根目录下的图片
                    //暂不处理
                    if (parentFile == null)
                        continue;
                    String parentPath = parentFile.getAbsolutePath();
                    if (imgAlbumPathList.contains(parentPath))
                        continue;
                    else {
                        imgAlbumPathList.add(parentPath);
                        AlbumMessage albumMessage = new AlbumMessage();
                        albumMessage.setName(parentFile.getName());
                        albumMessage.setFristImgPath(imgPath);
                        albumMessage.setParentFilePath(parentPath);
                        albumList.add(albumMessage);
                    }
                }
                Message message = Message.obtain();
                handler.sendMessage(message);
            }
        }).start();
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        File[] files = new File(albumList.get(position).getParentFilePath()).listFiles();
        imgPathList.clear();
        for (int i=files.length-1;i>0;i--){
            imgPathList.add(files[i].getAbsolutePath());
        }
        gridViewAdapter.notifyDataSetChanged();
              drawer.closeDrawer(GravityCompat.END);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.mulu:
                drawer.openDrawer(GravityCompat.END);
                break;
            case R.id.back_img:
               finish();
                break;
        }

    }
}
