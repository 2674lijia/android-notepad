package cqipc.lijia.note;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class SqliteModel {

    private static SqliteUitl sqliteUitl;


    public static List<Note> selectNotelike(Context context,String str){
        List<Note> noteList=new ArrayList<>();
        sqliteUitl = new SqliteUitl(context);
        SQLiteDatabase db = sqliteUitl.getReadableDatabase();
        Cursor cursor=db.query("note",null,"n_title like ?",new String[]{str},null,null,null);
        Note note=null;
        while (cursor.moveToNext()){
            note=new Note();
            System.out.println(cursor.getString(cursor.getColumnIndex("n_time")));
            System.out.println(cursor.getString(cursor.getColumnIndex("n_title")));
            System.out.println(cursor.getString(cursor.getColumnIndex("n_id")));
            note.setN_time(cursor.getString(cursor.getColumnIndex("n_time")));
            note.setN_title(cursor.getString(cursor.getColumnIndex("n_title")));
            note.setN_id(cursor.getString(cursor.getColumnIndex("n_id")));
            noteList.add(note);
        }
        return noteList;
    }


    public static List<Note> selectNoteAll(Context context){
        List<Note> noteList=new ArrayList<>();
        sqliteUitl = new SqliteUitl(context);
        SQLiteDatabase db = sqliteUitl.getReadableDatabase();
        Cursor cursor=db.query("note",null,null,null,null,null,null);
        Note note=null;
        while (cursor.moveToNext()){
            note=new Note();
            System.out.println(cursor.getString(cursor.getColumnIndex("n_time")));
            System.out.println(cursor.getString(cursor.getColumnIndex("n_title")));
            System.out.println(cursor.getString(cursor.getColumnIndex("n_id")));
         note.setN_time(cursor.getString(cursor.getColumnIndex("n_time")));
         note.setN_title(cursor.getString(cursor.getColumnIndex("n_title")));
         note.setN_id(cursor.getString(cursor.getColumnIndex("n_id")));
         noteList.add(note);
        }
        return noteList;
    }

    public static void deleteOneNoteById(Context context,String n_id){
        sqliteUitl = new SqliteUitl(context);
        SQLiteDatabase db = sqliteUitl.getReadableDatabase();
       int temp= db.delete("note","n_id=?",new String[]{n_id});
    }
    public static void deleteOneImgByNId(Context context,String n_id){
        sqliteUitl = new SqliteUitl(context);
        SQLiteDatabase db = sqliteUitl.getReadableDatabase();
        int temp= db.delete("imgPath","n_id=?",new String[]{n_id});
    }

    public static Note selectNoteById(Context context,String n_id){
        sqliteUitl = new SqliteUitl(context);
        SQLiteDatabase db = sqliteUitl.getReadableDatabase();
        Cursor cursor=db.query("note",null,"n_id=?",new String[]{n_id},null,null,null);
        Note note=null;
        while (cursor.moveToNext()){
            note=new Note();
            note.setN_time(cursor.getString(1));
            note.setN_title(cursor.getString(2));
        }
        db.close();
        return note;
    }
    public static Note selectNoteByTitle(Context context,String n_title){
        sqliteUitl = new SqliteUitl(context);
        SQLiteDatabase db = sqliteUitl.getReadableDatabase();
        Cursor cursor=db.query("note",null,"n_title=?",new String[]{n_title},null,null,null);
        Note note=null;
        while (cursor.moveToNext()){
            note=new Note();
            note.setN_time(cursor.getString(cursor.getColumnIndex("n_time")));
            note.setN_title(cursor.getString(cursor.getColumnIndex("n_title")));
        }
        return note;
    }
    public static List<String> selectImgPathByNId(Context context,String n_id){
        List<String> imgPathList=new ArrayList<>();
        sqliteUitl = new SqliteUitl(context);
        SQLiteDatabase db = sqliteUitl.getReadableDatabase();
        Cursor cursor=db.query("imgPath",null,"n_id=?",new String[]{n_id},null,null,null);
        while (cursor.moveToNext()){
            String imgPath=cursor.getString(1);
            System.out.println(imgPath);
            imgPathList.add(imgPath);
        }
        //db.close();
        return imgPathList;
    }
    public static List<String> selectVideoPathByNId(Context context,String n_id){
        List<String> videoPathList=new ArrayList<>();
        sqliteUitl = new SqliteUitl(context);
        SQLiteDatabase db = sqliteUitl.getReadableDatabase();
        Cursor cursor=db.query("videoPath",null,"n_id=?",new String[]{n_id},null,null,null);
        while (cursor.moveToNext()){
            String imgPath=cursor.getString(1);
            videoPathList.add(imgPath);
        }
        db.close();
        return videoPathList;
    }
    public static List<String> selectRecordPathByNId(Context context,String n_id){
        List<String> recordPathList=new ArrayList<>();
        sqliteUitl = new SqliteUitl(context);
        SQLiteDatabase db = sqliteUitl.getReadableDatabase();
        Cursor cursor=db.query("recordPath",null,"n_id=?",new String[]{n_id},null,null,null);
        while (cursor.moveToNext()){
            String imgPath=cursor.getString(1);
            recordPathList.add(imgPath);
        }
        db.close();
        return recordPathList;
    }
    public static void insertNote(Context context, Note note, Map<String,List<String>> pathMap){
        sqliteUitl = new SqliteUitl(context);
        SQLiteDatabase db = sqliteUitl.getReadableDatabase();
        ContentValues values=new ContentValues();
        values.put("n_id",note.getN_id());
        values.put("n_time",note.getN_time());
        values.put("n_title",note.getN_title());
        db.insert("note",null,values);
        List<String> videoPath=pathMap.get("videoPath");
        List<String> recordPath=pathMap.get("recordPath");
        insertVideoPath(context,videoPath,note.getN_id());
        insertRecordPath(context,recordPath,note.getN_id());
    }
    public static void insertImgPath(Context context,List<String> imgPath,String n_id){
        if (sqliteUitl==null)
           sqliteUitl = new SqliteUitl(context);
        SQLiteDatabase db = sqliteUitl.getReadableDatabase();
        ContentValues values=null;

        for (String path: imgPath) {
            values=new ContentValues();
            values.put("i_path",path);
            values.put("n_id",n_id);
            db.insert("imgPath",null,values);
        }
    }
    public static void insertVideoPath(Context context,List<String> videoPath,String n_id){
        sqliteUitl = new SqliteUitl(context);
        SQLiteDatabase db = sqliteUitl.getReadableDatabase();
        ContentValues values=null;

        for (String path: videoPath) {
            values=new ContentValues();
            values.put("i_path",path);
            values.put("n_id",n_id);
            db.insert("videoPath",null,values);
        }
    }
    public static void insertRecordPath(Context context,List<String> recordPath,String n_id){

        sqliteUitl = new SqliteUitl(context);
        SQLiteDatabase db = sqliteUitl.getReadableDatabase();
        ContentValues values=null;

        for (String path: recordPath) {
            values=new ContentValues();
            values.put("i_path",path);
            values.put("n_id",n_id);
            db.insert("recordPath",null,values);
        }
    }
    public static void updateNote(Context context, Note note){
        sqliteUitl = new SqliteUitl(context);
        SQLiteDatabase db = sqliteUitl.getReadableDatabase();
        ContentValues values=new ContentValues();
        values.put("n_time",note.getN_time());
        values.put("n_title",note.getN_title());
        db.update("note",values,"n_id=?",new String[]{note.getN_id()});
    }
}
