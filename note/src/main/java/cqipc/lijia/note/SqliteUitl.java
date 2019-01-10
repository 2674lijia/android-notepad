package cqipc.lijia.note;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.Nullable;

public class SqliteUitl extends SQLiteOpenHelper {


    public SqliteUitl(@Nullable Context context) {
        super(context, "user", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //创建数据库
        db.execSQL("create table IF NOT EXISTS note (n_id varchar(50) ," +
                                                               "n_time varchar(20) not null," +
                                                               "n_title varchar(20) not null)");

        db.execSQL("create table IF NOT EXISTS imgPath(n_id varchar(50)," +
                                                          "i_path varchar(100) not null,foreign key(n_id) references note(n_id))");

        db.execSQL("create table IF NOT EXISTS videoPath(n_id varchar(50)," +
                                                          "v_path varchar(100) not null,foreign key(n_id) references note(n_id))");

        db.execSQL("create table IF NOT EXISTS recordPath(n_id varchar(50)," +
                                                          "r_path varchar(100) not null,foreign key(n_id) references note(n_id))");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
