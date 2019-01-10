package cqipc.lijia.note;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class GridViewAdapter extends BaseAdapter {


    private ArrayList<String> imgPathList;
    private Context context;
    private boolean isSelect = false;


    public GridViewAdapter(Context context, ArrayList<String> imgPathList) {
        this.context = context;
        this.imgPathList = imgPathList;

    }

    public int getCount() {
        return imgPathList.size();
    }

    @Override
    public Object getItem(int position) {
        return imgPathList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void isSelect() {
        isSelect = true;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = null;
        if (convertView == null) {
            view = View.inflate(context, R.layout.show_imgs, null);
        } else {
            view = convertView;
        }
        ImageView imageView = view.findViewById(R.id.img);
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        Glide.with(context).load(imgPathList.get(position)).override(dm.widthPixels / 4, dm.widthPixels / 4)
                .centerCrop().into(imageView);
        return view;
    }
}
