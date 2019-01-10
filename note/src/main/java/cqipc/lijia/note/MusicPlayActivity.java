package cqipc.lijia.note;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Message;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.MotionEvent;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class MusicPlayActivity extends AppCompatActivity
        implements AdapterView.OnItemClickListener, View.OnClickListener, View.OnTouchListener {
    private ListView musicList;
    private List<MusicFile> musicFileList;
    private Handler handler;
    private TextView musicT, tvTTT, musicA, tvAll;
    private SeekBar progressBar;
    MusicService.MyBinder myBinder;
    private Mycon myConn;
    private LinearLayout isStart;
    private ImageView playprevious, playback, musicII;
    private MusicListAdapter adapter;
    private String path;
    private int counts;
    private ImageView play;
    private int flagCount;
    private int startPosition;
    private Thread thread;
    private final int[] bitmpR = new int[]{R.drawable.btn_playback_pause, R.drawable.btn_playback_play};
    private Timer timer;
    private TimerTask timerTask;
    private int maxProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music_play);
        init();
        handler = new Handler() {
            public void handleMessage(Message msg) {
                tvTTT.setText(MusicUtil.formatTime(msg.arg1));
                progressBar.setProgress(msg.arg1);
            }
        };
        if (!musicFileList.isEmpty()) {
            prepartionStart(0);
        }
    }

    public void startJishi() {
        if (timer == null) {
            timer = new Timer();
            timerTask = new TimerTask() {
                @Override
                public void run() {
                    System.out.println("-------------------");
                    startPosition = myBinder.getPosition();
                    Message message = Message.obtain();
                    message.arg1 = startPosition;
                    handler.sendMessage(message);
                }
            };
        }
        timer = new Timer();
        timer.schedule(timerTask, 0, 1000);
    }

    public void stopJishi() {
        if (timer != null) {
            timerTask.cancel();
            timer.cancel();
            timerTask = null;
            timer = null;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindS();
        stopJishi();
    }

    public void bindS() {
        if (myConn == null) {
            System.out.println("-----------绑定服务------------");
            myConn = new Mycon();
        }
        Intent intent = new Intent(getApplicationContext(), MusicService.class);
        startService(intent);
        bindService(intent, myConn, BIND_ABOVE_CLIENT);
    }

    public void unbindS() {
        if (myConn != null) {
            unbindService(myConn);
            myConn = null;
        }
    }

    public void init() {
        musicFileList = MusicUtil.getMp3Infos(MusicPlayActivity.this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        adapter = new MusicListAdapter(this, (ArrayList<MusicFile>) musicFileList);
        musicList = findViewById(R.id.musicList);
        musicList.setAdapter(adapter);
        musicList.setOnItemClickListener(this);
        musicT = findViewById(R.id.musicT);
        progressBar = findViewById(R.id.progressBar);
        progressBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    myBinder.pause();
                    myBinder.setStartPosition(progress);
                    myBinder.play(null);

                }
                if (myBinder!=null) {
                    if (progress >= maxProgress - 1 && !myBinder.getisPlay()) {
                        if (flagCount < musicFileList.size() - 1) {
                            prepartionStart(flagCount + 1);
                            startJishi();
                            Bitmap bitmap = BitmapFactory.decodeResource(getResources(), bitmpR[0]);
                            play.setImageBitmap(bitmap);
                            myBinder.play(path);
                        }
                    }
                }

            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
        isStart = findViewById(R.id.isStart);
        isStart.setOnTouchListener(this);
        playback = findViewById(R.id.playback);
        playback.setOnClickListener(this);
        playprevious = findViewById(R.id.playprevious);
        playprevious.setOnClickListener(this);
        tvTTT = findViewById(R.id.tvTTT);
        tvAll = findViewById(R.id.tvAll);
        musicII = findViewById(R.id.musicII);
        musicA = findViewById(R.id.musicA);
        play = findViewById(R.id.play);
        bindS();//绑定服务
    }
    @Override
    protected void onRestart() {
        super.onRestart();
        System.out.println("--------------restart------------------");
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        prepartionStart(position);
    }

    public void prepartionStart(int position) {
        stopJishi();
        MusicFile musicFile = adapter.getItem(position);
        flagCount = position;
        System.out.println("-----------" + musicFile.getPath() + "------------");
        play.setImageBitmap(BitmapFactory.decodeResource(getResources(), bitmpR[1]));
        musicT.setText(musicFile.getTitle());
        musicA.setText(musicFile.getArtist());
        progressBar.setProgress(0);
        tvTTT.setText("00:00");
        maxProgress = (int) musicFile.getDuration();
        progressBar.setMax(maxProgress);
        tvAll.setText(MusicUtil.formatTime(musicFile.getDuration()));
        Glide.with(this).
                load(MusicUtil.getArtwork(this, musicFile.getId(), musicFile.getAlbumId())).
                error(R.drawable.music_ss).
                centerCrop().
                into(musicII);
        if (myBinder != null) {
            myBinder.pause();
            myBinder.setMediaPlayer();
        }
        path = musicFile.getPath();
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.playprevious:
                if (flagCount > 0) {
                    prepartionStart(flagCount - 1);
                }
                break;
            case R.id.playback:
                if (flagCount < musicFileList.size() - 1) {
                    prepartionStart(flagCount + 1);
                }
                break;
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        counts++;
        System.out.println("gjhklhjlk============" + event.getPointerCount());
        Bitmap bitmap = null;
        if (counts % 2 == 0) {
            stopJishi();
            myBinder.pause();
            bitmap = BitmapFactory.decodeResource(getResources(), bitmpR[1]);
            play.setImageBitmap(bitmap);

        } else {
            startJishi();
            System.out.println("------" + myBinder.getPosition());
            bitmap = BitmapFactory.decodeResource(getResources(), bitmpR[0]);
            play.setImageBitmap(bitmap);
            myBinder.setStartPosition(myBinder.getPosition());
            myBinder.play(path);
        }
        return false;
    }

    private class Mycon implements ServiceConnection {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            System.out.println("成功绑定服务---------");
            myBinder = (MusicService.MyBinder) service;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    }

}
