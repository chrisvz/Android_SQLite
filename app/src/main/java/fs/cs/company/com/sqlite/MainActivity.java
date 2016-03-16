package fs.cs.company.com.sqlite;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import java.util.ArrayList;

import database.Database;
import database.User;
import fs.cs.company.com.funnyeyes.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        // initialize database
        Database database = Database.newInstance(getApplicationContext());

        // get list of objects in database
        ArrayList<User> list = database.getUsers();
        int x = list.size();
System.out.println("LIST"+x);
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

    }
}
