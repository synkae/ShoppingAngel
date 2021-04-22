package com.codepath.synkae.shoppingangel.models;
import androidx.room.*;
import android.content.Context;

@Database(entities = {}, version = 1, exportSchema = false)
public abstract class RoomDB extends RoomDatabase{

    private static RoomDB sInstance;
//    public abstract UserDao userDao();

    public static synchronized RoomDB getInstance(Context context){
        if(sInstance == null){
            sInstance = Room.databaseBuilder(context.getApplicationContext(),
                    RoomDB.class, "RoomDB.db")
                    .allowMainThreadQueries()
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return sInstance;
    }
//    public void seed() {
//        if (userDao().count() == 0) {
//            runInTransaction(new Runnable() {
//                @Override
//                public void run() {
//                    userDao().insertUsers(
//                            new User("a@lice5", "@csit100"),
//                            new User("$brian7", "123abc##"),
//                            new User("!chris12!", "CHRIS12!!")
//                    );
//                }
//            });
//        }
//    }
    
}
