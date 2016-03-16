package database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.CursorWrapper;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.UUID;

import static database.DatabaseSchema.*;

/**
 * Created by Chris on 3/15/2016.
 */
public class Database {
    public static final String DEBUG = "DEBUG";
    private static Database database;
    private Context context;
    private SQLiteDatabase sqLiteDatabase;

    public static Database newInstance(Context context) {
        if(database == null) {
            database = new Database(context);
        }
        return database;
    }

    private Database(Context context) {
        this.context = context;
        sqLiteDatabase = new Helper(context).getWritableDatabase();
    }

    private ContentValues getValues(User user) {
        ContentValues values = new ContentValues();
        values.put(USER.COLS.UUID,user.getUuid().toString());
        values.put(USER.COLS.NAME,user.getName());
        values.put(USER.COLS.AGE,user.getAge());
        values.put(USER.COLS.isMARRIED, user.isMarried() ? 1 : 0);
        return values;
    }

    public void addUser(User user) {
        ContentValues values = getValues(user);
        try{
            sqLiteDatabase.insert(USER.NAME, null, values);
        }
        catch(Exception e){
            Log.d(DEBUG,"operation to add user failed");
        }

    }

    public void removeUser(User user) {
        String uuid = user.getUuid().toString();
        try {
            sqLiteDatabase.delete(USER.NAME, USER.COLS.UUID + " = ?", new String[]{uuid});
        }
        catch(Exception e){
            Log.d(DEBUG,"operation to remove user failed");
        }
    }

    public void updateUser(User user) {
        String uuid = user.getUuid().toString();
        ContentValues values = getValues(user);
        try{
            sqLiteDatabase.update(USER.NAME, values, USER.COLS.UUID + " = ?", new String[]{uuid});
        }
        catch (Exception e){
            Log.d(DEBUG,"operation to update user failed");
        }
    }

    private UserCursor query(String whereClause,String whereArgs[]) {
        Cursor cursor = sqLiteDatabase.query(
                USER.NAME,
                null,
                whereClause,
                whereArgs,
                null,
                null,
                null
        );
        return new UserCursor(cursor);
    }

    public User getUser(UUID uuid) {
        UserCursor userCursor = query(USER.COLS.UUID+" = ?",new String[]{ uuid.toString()});
        try {
            if(userCursor.getCount() == 0){
                return null;
            }
            userCursor.moveToFirst();
            return userCursor.getUser();
        }
        finally {
            userCursor.close();
        }
    }

    public ArrayList<User> getUsers() {
        ArrayList<User> users = new ArrayList<>();
        UserCursor userCursor = query(null,null);

        try{
            userCursor.moveToFirst();
            while(!userCursor.isAfterLast()){
                users.add(userCursor.getUser());
                userCursor.moveToNext();
            }
        }
        finally {
            userCursor.close();
        }
        return users;
    }
}
