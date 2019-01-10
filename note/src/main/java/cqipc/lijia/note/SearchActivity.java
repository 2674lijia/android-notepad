package cqipc.lijia.note;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends Activity implements AdapterView.OnItemClickListener {
    private ListView list;
    private EditText et;
    private ListViewBaseAdapter adapter;
    private List<Note> noteList;
    private TextView tvSure;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_note);
        list=findViewById(R.id.list);
        et=findViewById(R.id.et);
        noteList=new ArrayList<>();
        adapter=new ListViewBaseAdapter(noteList,this,false);
        list.setAdapter(adapter);
        list.setOnItemClickListener(this);
        tvSure=findViewById(R.id.tvSure);
        tvSure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String str=et.getText().toString();
                noteList=SqliteModel.selectNotelike(SearchActivity.this,"%"+str+"%");
                adapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
       Note note= noteList.get(position);
       startNewNote(note);
    }
    public void startNewNote(Note note){
        Intent intent=new Intent(this,NewNoteActivity.class);
        if (note!=null) {
            intent.putExtra("n_id", note.getN_id());
            System.out.println("note.getN_id()=="+note.getN_id());
        }
        startActivity(intent);
    }
}
