package cqipc.lijia.note;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class MusicListAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<MusicFile> musicFiles;
   public MusicListAdapter(Context context,ArrayList<MusicFile> musicFiles){
        this.context=context;
        this.musicFiles=musicFiles;
    }
    @Override
    public int getCount() {
        return musicFiles.size();
    }

    @Override
    public MusicFile getItem(int position) {
        return musicFiles.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view=null;
        if (convertView != null) {
            view=convertView;
        }else {
            view= LinearLayout.inflate(context, R.layout.music_listview, null);
        }
        ImageView musicImg=view.findViewById(R.id.musicImg);
//      Bitmap bitmap=MusicUtil.getArtwork(context,musicFiles.get(position).getId(),musicFiles.get(position).getAlbumId(),false,false);
//        if (bitmap == null) {
//            bitmap=BitmapFactory.decodeResource(context.getResources(),R.drawable.music_ss);
//        }
        Glide.with(context).
                load(MusicUtil.getArtwork(context,musicFiles.get(position).getId(),musicFiles.get(position).getAlbumId())).
                error(R.drawable.music_ss).centerCrop().into(musicImg);
        TextView musicTitle=view.findViewById(R.id.musicTitle);
        TextView musicAuthor=view.findViewById(R.id.musicAuthor);
        musicTitle.setText(musicFiles.get(position).getTitle());
        musicAuthor.setText(musicFiles.get(position).getArtist());
        return view;
    }
}
