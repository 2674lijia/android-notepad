package cqipc.lijia.note;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener {

    private LinearLayout cameraL, musicL, solarL, photoL, newNote;
    private EditText etSearch;
    private ListView list;
    private static final int MENU_NOTE_CODE = 101;
    private static final int NEW_NOTE_CODE = 101;
    private List<Note> noteList;
    private ListViewBaseAdapter adapter;
    private static final int NO_CHANGE_NOTE = 120;
    private static final int CHANGE_NOTE = 121;
    private RelativeLayout rl;
    private LinearLayout  userhandle;
    private ImageView imgC;
    private TextView tv_xz, tvTitle;
    private int counts;  //记录点击次数
    private int xzCounts;
    private boolean flag;  //标记编辑状态 还是删除状态
    private Toolbar toolbar;
    private  DatePicker datePicker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //https://www.iconfont.cn/collections/detail?spm=a313x.7781069.1998910419.d9df05512&cid=13392
        init();
    }

    //初始
    public void init() {
        userhandle = findViewById(R.id.userhandle);
        tv_xz = findViewById(R.id.tv_xz);
        tvTitle = findViewById(R.id.tvTitle);
        cameraL = findViewById(R.id.cameraL);
        cameraL.setOnClickListener(this);
        musicL = findViewById(R.id.musicL);
        musicL.setOnClickListener(this);
        solarL = findViewById(R.id.solarL);
        solarL.setOnClickListener(this);
        photoL = findViewById(R.id.photoL);
        photoL.setOnClickListener(this);
        etSearch = findViewById(R.id.etSearch);
        etSearch.setOnClickListener(this);
        newNote = findViewById(R.id.newNote);
        newNote.setOnClickListener(this);
        list = findViewById(R.id.list);
        list.setOnItemClickListener(this);
        list.setOnItemLongClickListener(this);
        toolbar = findViewById(R.id.toolbar);
        //读取数据库的数据，并呈现在ListView上
        noteList = SqliteModel.selectNoteAll(this);
        flag = false;
        adapter = new ListViewBaseAdapter(noteList, this, flag);
        list.setAdapter(adapter);
        rl = findViewById(R.id.rl);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.etSearch:
                Intent intent = new Intent(this, SearchActivity.class);
                startActivityForResult(intent, NEW_NOTE_CODE);
                break;
            case R.id.cameraL:
                openCamera();
                break;
            case R.id.musicL:
                openMusic();
                break;
            case R.id.photoL:
                openImages();
                break;
            case R.id.solarL:
                opSolar();
                break;
            case R.id.newNote:
                startNewNote(null);
                break;
        }
    }

    private void openCamera() {
        Intent intent=new Intent();
        //保证与清单文件中设置的一致即可
        //通过setAction设置需要开启的Activity的动作为“android.media.action.IMAGE_CAPTURE”
        intent.setAction("android.media.action.IMAGE_CAPTURE");
        //通过addCategory设置类别为"android.intent.category.DEFAULT"
        intent.addCategory("android.intent.category.DEFAULT");
        startActivity(intent);
    }

    private void openImages() {
        Intent intent =new Intent(this, ImageActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == NEW_NOTE_CODE) {
            switch (resultCode) {
                case CHANGE_NOTE:
                    List<Note> tempList = SqliteModel.selectNoteAll(this);
                    //数据更改UI组件需要刷新
                    noteList.clear();
                    noteList.addAll(tempList);
                    adapter.notifyDataSetChanged();
                    break;
                case NO_CHANGE_NOTE:
                    break;
            }
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Note note = noteList.get(position);
        if (flag) {
            xzCounts = 0;
            if (note.isCheck()) {
                note.setCheck(false);
            } else {
                note.setCheck(true);
            }
            for (Note n : noteList) {
                if (n.isCheck())
                    xzCounts++;
            }
            tv_xz.setText(xzCounts + "");
            adapter.notifyDataSetChanged();
        } else {
            startNewNote(note);
        }
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
        if (!flag){
            deletenoteListItem();
            Note note = noteList.get(position);
            note.setCheck(true);
            adapter.notifyDataSetChanged();
        }
        return true;
    }

    public void startNewNote(Note note) {
        Intent intent = new Intent(this, NewNoteActivity.class);
        if (note != null) {
            intent.putExtra("n_id", note.getN_id());
            System.out.println("note.getN_id()==" + note.getN_id());
        }
        startActivityForResult(intent, NEW_NOTE_CODE);
    }

    public void openMusic() {
        Intent intent = new Intent(this, MusicPlayActivity.class);
        startActivityForResult(intent, MENU_NOTE_CODE);
    }

    public void opSolar() {
        Intent intent=new Intent(this,CalendarActivity.class);
        startActivity(intent);
    }



    public void deletenoteListItem() {
        flag=true;
        counts = 0;
        flag = true;
        adapter.setFlag(true);
        imgC = new ImageView(this);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(80, 80);
        toolbar.addView(imgC, params);
        imgC.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.back));
        tvTitle.setText("已选择 ");
        tv_xz.setText("1");
        userhandle.removeView(newNote); //删除已有的
        final View view = View.inflate(this, R.layout.user_handle, null);
        userhandle.addView(view);
        for (Note note : noteList) {
            note.setCheck(false);
        }
        TextView delete2 = view.findViewById(R.id.delete2);
        delete2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (Note note : noteList) {
                    if (note.isCheck()) {
                        SqliteModel.deleteOneNoteById(MainActivity.this, note.getN_id());
                        deleteFile(note.getN_title());
                    }
                }
                List<Note> tempList = SqliteModel.selectNoteAll(MainActivity.this);
                //数据更改UI组件需要刷新
                noteList.clear();
                noteList.addAll(tempList);
                flag=false;
                recover(view);
            }
        });
        TextView all_xz = view.findViewById(R.id.all_xz);
        all_xz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                counts++;
                if (counts % 2 == 0) {
                    for (Note note : noteList)
                        note.setCheck(false);
                    tv_xz.setText("0");
                } else {
                    for (Note note : noteList)
                        note.setCheck(true);
                    tv_xz.setText(noteList.size() + "");
                }
                adapter.notifyDataSetChanged();
            }
        });
        imgC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recover(view);
                flag=false;
            }
        });
    }
    public void recover(View view) {
        tvTitle.setText("便签");
        userhandle.removeView(view);
        userhandle.addView(newNote);
        toolbar.removeView(imgC);
        tv_xz.setText("");
        adapter.setFlag(false);
        flag = false;
        adapter.notifyDataSetChanged();
    }
}
