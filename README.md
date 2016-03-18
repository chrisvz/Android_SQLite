# Android_SQLite
<br>
<br>


<h4> An example how to connect to SQLite in Android </h4>

<h3> Create pojo class </h3>


```java
import java.util.UUID;

public class User {

    private UUID uuid;
    private String name;
    private int age;
    private boolean isMarried;


    public User(UUID uuid) {
        this.uuid = uuid;
    }

    public User() {
        this.uuid = UUID.randomUUID();
    }

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public void setIsMarried(boolean isMarried) {
        this.isMarried = isMarried;
    }

    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }

    public boolean isMarried() {
        return isMarried;
    }
}

```

<br>
<br>
<br>
<br>



<h3> Create database schema </h3>

```java
public class DatabaseSchema {


    public static final class USER  {
        public static final String NAME = "user";

        public static final class COLS {
            public static final String UUID = "uuid";
            public static final String NAME = "name";
            public static final String AGE = "age";
            public static final String isMARRIED = "isMarried";
        }
    }
}

```
<br>
<br>
<br>
<br>

<h3> Create a class to extend SQLiteOpenHelper </h3>

```java
public class Helper extends SQLiteOpenHelper {

    public static final String NAME = "myDatabase.db";
    public static final int VERSION = 1;

    public Helper(Context context) {
        super(context, NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(
        "create table " + USER.NAME + "("
        + "_id integer primary key autoincrement, "
        
                +USER.COLS.UUID       +", "
                +USER.COLS.NAME       +", "
                +USER.COLS.AGE        +", "
                +USER.COLS.isMARRIED  +")"

        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}

```
<br>
<br>
<br>
<br>

<h3> Create Database class and initiliaze SQLite database</h3>

```java
public class Database {

    public static final String DEBUG = "DEBUG";
    private static Database database;
    private Context context;
    private SQLiteDatabase sqLiteDatabase;

    public static Database newInstance(Context context) {
        if (database == null) {
            database = new Database(context);
        }
        return database;
    }

    private Database(Context context) {
        this.context = context;
        sqLiteDatabase = new Helper(context).getWritableDatabase();
    }

}
```
<br>
<br>
<br>
<br>


<h3> Writing to Database </h3>
<h4> Add these methods in Database class to add,edit or remove an Object from SQLite </h2>

```java
public class Database {

 private ContentValues getValues(User user) {
        ContentValues values = new ContentValues();
        
        values.put(USER.COLS.UUID, user.getUuid().toString() );
        values.put(USER.COLS.NAME, user.getName() );
        values.put(USER.COLS.AGE, user.getAge() );
        values.put(USER.COLS.isMARRIED, user.isMarried() ? 1 : 0 );
        
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
            sqLiteDatabase.delete(USER.NAME, USER.COLS.UUID + " = ?", new String[]{ uuid });
        }
        catch(Exception e){
            Log.d(DEBUG,"operation to remove user failed");
        }
    }

    public void updateUser(User user) {
        String uuid = user.getUuid().toString();
        ContentValues values = getValues(user);
        
        try{
            sqLiteDatabase.update(USER.NAME, values, USER.COLS.UUID + " = ?", new String[]{ uuid });
        }
        catch (Exception e){
            Log.d(DEBUG,"operation to update user failed");
        }
    }
    }
```
<br>
<br>
<br>
<br>


<h3> Reading from database </h3>
<h4> Create a class to extend CursorWrapper which will return the objects from the database </h2>

```java
public class UserCursor extends CursorWrapper {
  
    public UserCursor(Cursor cursor) {
        super(cursor);
    }

        public User getUser() {
            String uuid   = getString(getColumnIndex(USER.COLS.UUID));
            String name   = getString(getColumnIndex(USER.COLS.NAME));
            int age       = getInt(getColumnIndex(USER.COLS.AGE));
            int isMarried = getInt(getColumnIndex(USER.COLS.isMARRIED));

            User user = new User(UUID.fromString(uuid));
            user.setIsMarried(isMarried!= 0);
            user.setName(name);
            user.setAge(age);
            return user;
        }
}


```

<br>
<br>
<br>
<br>

<h3> Add these methods in Database class to read an object or get a list of the objects in the Database </h3>
```java
public class Database {
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
        UserCursor userCursor = query(USER.COLS.UUID+" = ?",new String[]{ uuid.toString()} );
        
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
}

```

<h3> And your database is ready for use  </h3>

```java
     // initialize database
        Database database = Database.newInstance(getApplicationContext());

        // get list of objects in database
        ArrayList<User> list = database.getUsers();

        // create user
        User user= new User();
        user.setAge(22);
        user.setName("Mike");
        user.setIsMarried(true);
        
        // add user to database
        database.addUser(user);
        
        // edit user
        user.setAge(27);
        user.setIsMarried(false);
        
        // update user 
        database.updateUser(user);
        
        // remove user from database
        database.removeUser(user);
        
        
```




