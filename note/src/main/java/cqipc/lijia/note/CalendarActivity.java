package cqipc.lijia.note;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.haibin.calendarview.Calendar;
import com.haibin.calendarview.CalendarView;


public class CalendarActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView tvYear,tvNow;
    private int year;
    private int month;
    private int day;
    private  CalendarView calendarView;
    private ImageView back_img;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rili);
        SildingFinishLayout mSildingFinishLayout = (SildingFinishLayout) findViewById(R.id.timed_task_SildingFinishLayout);
        mSildingFinishLayout.setOnSildingFinishListener(new SildingFinishLayout.OnSildingFinishListener() {

            @Override
            public void onSildingFinish() {
                CalendarActivity.this.finish();
            }
        });
        LinearLayout linearLayout=findViewById(R.id.linearlayout);
        mSildingFinishLayout.setTouchView(linearLayout);

        calendarView=findViewById(R.id.calendarView);
        tvYear=findViewById(R.id.tvYear);
        tvNow=findViewById(R.id.tvNow);
        back_img=findViewById(R.id.back_img);
        back_img.setOnClickListener(this);
        java.util.Calendar calendar= java.util.Calendar.getInstance();

//        Date date = new Date(System.currentTimeMillis());
//        year=date.getYear();
//        month=date.getMonth();
//        day=date.getDay();
//        Calendar calendar = new Calendar();
        year=calendar.get(java.util.Calendar.YEAR);
        month=calendar.get(java.util.Calendar.MONTH)+1;
        day=calendar.get(java.util.Calendar.DAY_OF_MONTH);

        tvNow.setText(day+"");
        tvYear.setText(year+"年"+month+"月");
        tvNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calendarView.scrollToCalendar(year,month,day);
            }
        });
        calendarView.setOnDateSelectedListener(new CalendarView.OnDateSelectedListener() {
            public void onDateSelected(Calendar calendar, boolean isClick) {
                if (isClick){

                }else {
                    String temp=calendar.getYear()+"年"+calendar.getMonth()+"月";
                    tvYear.setText(temp);
                }

            }
        });
        //calendarView.get
       //calendarView.s
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back_img:
                finish();
                break;
        }
    }
}
