package cqipc.lijia.note;

import android.app.Service;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;

import java.io.IOException;

public class MusicService extends Service {
    private static final String TAG = "MusicService";
    public MediaPlayer mediaPlayer;

    @Override
    public void onCreate() {
        super.onCreate();
    }
    class MyBinder extends Binder {
        private int startPosition;

        public void play(String path) {
            if (mediaPlayer == null) {
                mediaPlayer = new MediaPlayer();
                mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                try {
                    System.out.print(path);
                    mediaPlayer.setDataSource(path);
                    mediaPlayer.prepare();
                    mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                        @Override
                        public void onPrepared(MediaPlayer mp) {
                            mediaPlayer.start();
                        }
                    });
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                int position = getStartPosition();
                mediaPlayer.seekTo(position);
                mediaPlayer.start();
            }

        }
        public void setMediaPlayer(){
            mediaPlayer=null;
        }
        public void pause(){
            if(mediaPlayer!=null&&mediaPlayer.isPlaying()){
                mediaPlayer.pause();
            }
        }
        public void setStartPosition(int startPosition){
            this.startPosition=startPosition;
        }
        public int getStartPosition(){
            return  startPosition;
        }
        public int getPosition(){
            if (mediaPlayer!=null){
               return mediaPlayer.getCurrentPosition();
            }else
                return 0;
        }
        public boolean getisPlay(){
            return mediaPlayer.isPlaying();
        }
    }

    @Override
    public void onDestroy() {
        if(mediaPlayer!=null){
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer=null;
        }
    }
    @Override
    public IBinder onBind(Intent intent) {
        System.out.println("绑定服务++++++");
        return new MyBinder();
    }
}
