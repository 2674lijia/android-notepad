package cqipc.lijia.note;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class Main2Activity extends AppCompatActivity {

    private ListViewBaseAdapter listViewBaseAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        SlideCutListView view = findViewById(R.id.llllll);
        List<Note> list = new ArrayList<>();
        Note note = new Note();
        note.setN_title("aaadqwewrtyyuiop[[;iolukyjth");

        Note note1 = new Note();
        note1.setN_title("aaadsdffqerwtyuioltyjhgfd");
        list.add(note);
        list.add(note1);

        listViewBaseAdapter = new ListViewBaseAdapter(list, this, false);
        view.setAdapter(listViewBaseAdapter);
        view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                return false;
            }
        });

        //左右滑动监听
//        view.setOnFlingListener(new SlideListView.OnFlingListener() {
//            @Override
//            public void onLeftFling(View itemView, int position, int startPosition) {
//                itemView.setTranslationX(startPosition);
//                itemView.setScrollX(startPosition);
//                DisplayMetrics dm =Main2Activity.this.getResources().getDisplayMetrics();
//                itemView.scrollTo(dm.widthPixels/2,0);
//            }
//
//            @Override
//            public void onRightFling(View itemView, int position, int startPosition) {
//
//            }
//        });
        view.setRemoveListener(new SlideCutListView.RemoveListener() {
            @Override
            public void leftMoveIten(View viewItem, int position) {

                LinearLayout linearLayout=findViewById(R.id.line);

                RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(AbsListView.LayoutParams.WRAP_CONTENT, AbsListView.LayoutParams.MATCH_PARENT);
                layoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
                layoutParams.addRule(RelativeLayout.CENTER_VERTICAL);
                layoutParams.leftMargin=(int) viewItem.getY();
                View view=View.inflate(Main2Activity.this,R.layout.add_view,null);
                linearLayout.addView(view,layoutParams);

            }
            @Override
            public void rightMoveIten(View viewItem, int position) {

            }
        });
    }
}
