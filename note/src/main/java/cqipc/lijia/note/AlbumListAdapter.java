package cqipc.lijia.note;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class AlbumListAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<AlbumMessage> albumMessages;

    public AlbumListAdapter(Context context, ArrayList<AlbumMessage> albumMessages) {
        this.context = context;
        this.albumMessages = albumMessages;

    }

    @Override
    public int getCount() {
        return albumMessages.size();
    }

    @Override
    public Object getItem(int position) {
        return albumMessages.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view=null;
        if (convertView==null){
            view = View.inflate(context, R.layout.album_message, null);
        }else
            view=convertView;
        ImageView imageView=view.findViewById(R.id.img);
        Glide.with(context).load(albumMessages.get(position).getFristImgPath()).into(imageView);
        TextView textView=view.findViewById(R.id.tv);
        textView.setText(albumMessages.get(position).getName());
        return view;
    }
}
