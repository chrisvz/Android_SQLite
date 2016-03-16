package database;

import android.database.Cursor;
import android.database.CursorWrapper;

import java.util.UUID;

import static database.DatabaseSchema.*;

/**
 * Created by Chris on 3/15/2016.
 */
public class UserCursor extends CursorWrapper {
    /**
     * Creates a cursor wrapper.
     *
     * @param cursor The underlying cursor to wrap.
     */
    public UserCursor(Cursor cursor) {
        super(cursor);
    }

    public User getUser() {
        String uuid = getString(getColumnIndex(USER.COLS.UUID));
        String name = getString(getColumnIndex(USER.COLS.NAME));
        int age = getInt(getColumnIndex(USER.COLS.AGE));
        int isMarried = getInt(getColumnIndex(USER.COLS.isMARRIED));

        User user = new User(UUID.fromString(uuid));
        user.setIsMarried(isMarried!= 0);
        user.setName(name);
        user.setAge(age);
        return user;
    }
}
