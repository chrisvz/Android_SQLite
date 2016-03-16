package database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import static database.DatabaseSchema.*;

/**
 * Created by Chris on 3/15/2016.
 */
public class Helper extends SQLiteOpenHelper {

    public static final String NAME = "fsDb.db";
    public static final int VERSION = 1;

    public Helper(Context context) {
        super(context, NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(
        "create table "+ USER.NAME +"("
        +"_id integer primary key autoincrement, "
                + USER.COLS.UUID +", "
                +USER.COLS.NAME+", "
                +USER.COLS.AGE+", "
                +USER.COLS.isMARRIED+")"

        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
