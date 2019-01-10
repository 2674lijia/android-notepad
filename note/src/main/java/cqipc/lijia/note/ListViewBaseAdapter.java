package cqipc.lijia.note;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

public class ListViewBaseAdapter extends BaseAdapter {

    private List<Note> noteList;
    private Context context;
    private boolean flag;

    public ListViewBaseAdapter(List<Note> noteList, Context context, boolean flag) {
        this.noteList = noteList;
        this.context = context;
        this.flag=flag;
    }
    @Override
    public int getCount() {
        return noteList.size();
    }
    @Override
    public Object getItem(int position) {
        return noteList.get(position);
    }
    @Override
    public long getItemId(int position) {
        return position;
    }
    public String getNoteTitle(int position){
        return noteList.get(position).getN_title();
    }
    public String getNoteId(int position){
        return noteList.get(position).getN_id();
    }
    public void remove(int position){

        noteList.remove(position);
    }
    public void setFlag(boolean flag) {
        this.flag = flag;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        RelativeLayout view=null;
        if (convertView != null) {
            view=(RelativeLayout) convertView;
        }else {
            view= (RelativeLayout) LinearLayout.inflate(context, R.layout.listview_item, null);
        }
        ImageView checkboxImg=view.findViewById(R.id.checkboxImg);
        TextView title=view.findViewById(R.id.title);
        TextView time=view.findViewById(R.id.time);
        title.setText(noteList.get(position).getN_title());
        time.setText(noteList.get(position).getN_time());
        if (flag) {
            if (noteList.get(position).isCheck()) {
                Glide.with(context).load(R.drawable.checkbox_w).animate(R.anim.sss).into(checkboxImg);
            } else {
                Glide.with(context).load(R.drawable.checkbox_m).into(checkboxImg);
            }
        }else {
            if (checkboxImg!=null) {
               checkboxImg.setImageBitmap(BitmapFactory.decodeResource(context.getResources(),R.drawable.img_null));
            }
        }

        return view;
    }
}
