package edu.mnstate.mg0483fp.applecontacts;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by dongkyulim on 11/3/17.
 */

public class UserDB {
    //set database, table
    private static final String TAG = "UserDB";

    private static final String DB_NAME = "contacts.db";
    private static final int DB_VERSION = 1;
    public static final String TABLE_NAME = "users";

    public static final String USER_ID = "_id";
    public static final int USER_ID_COL = 0;

    public static final String USER_FIRST_NAME = "user_first_name";
    public static final int USER_FIRST_NAME_COL = 1;

    public static final String USER_LAST_NAME = "user_last_name";
    public static final int USER_LAST_NAME_COL = 2;

    public static final String USER_ADDRESS = "user_address";
    public static final int USER_ADDRESS_COL = 3;

    public static final String USER_PHONE = "user_phone";
    public static final int USER_PHONE_COL = 4;

    public static final String USER_EMAIL = "user_email";
    public static final int USER_EMAIL_COL = 5;

    int index;

    //sqlite query, creating database with 6 values
    private static final String CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME + "(" +
                USER_ID         + " INTEGER PRIMARY KEY, " +
                USER_FIRST_NAME + " TEXT NOT NULL, " +
                USER_LAST_NAME  + " TEXT NOT NULL, " +
                USER_ADDRESS    + " TEXT NOT NULL, "+
                USER_PHONE      + " TEXT NOT NULL, " +
                USER_EMAIL      + " TEXT)";

    private static class DBHelper extends SQLiteOpenHelper{
        public DBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version){
            super(context, name, factory, version);
        }

        @Override
        public void onCreate(SQLiteDatabase myDB){
            myDB.execSQL(CREATE_TABLE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
            Log.w(UserDB.class.getName(), "Upgrading database from version " +
                    oldVersion + " to " + newVersion + ", NOTE -- will destroy ALL old data");
            db.execSQL("DROP TABLE IF EXISTS" + TABLE_NAME);
            onCreate(db);

        }
    }

    private SQLiteDatabase db;
    private DBHelper dbHelper;

    public UserDB(Context context){
         dbHelper = new DBHelper(context, DB_NAME, null, DB_VERSION);
    }
    public void openReadableDB(){
        db = dbHelper.getReadableDatabase();
    }
    public void openWritableDB(){
        db = dbHelper.getWritableDatabase();
    }
    private void closeDB(){
        if(db != null){
            db.close();
        }
    }


    /**
     * get user list
     *
     * @return user list
     */
    public ArrayList<User> getUserList(){
        ArrayList<User> users = new ArrayList<>();
        openReadableDB();
        Cursor cursor = db.query(TABLE_NAME, null, null, null, null, null, null, null);
        while(cursor.moveToNext()){
            User user = new User();
            user.setFirstName(cursor.getString(USER_FIRST_NAME_COL));
            user.setLastName(cursor.getString(USER_LAST_NAME_COL));
            user.setAddress(cursor.getString(USER_ADDRESS_COL));
            user.setPhone(cursor.getString(USER_PHONE_COL));
            user.setEmail(cursor.getString(USER_EMAIL_COL));

            users.add(user);
        }
        if(cursor!=null) {
            cursor.close();
        }
        closeDB();

        users = sortDatabase(); ///////
        return users;
    }

    /**
     * insert user data
     *
     * @param user
     * @return
     */
    public long insertUser(User user){
        int id = 0;
        ContentValues cv = new ContentValues();
        id = getUserList().size()+1;
        cv.put(USER_ID , id);
        cv.put(USER_FIRST_NAME, user.getFirstName());
        cv.put(USER_LAST_NAME, user.getLastName());
        cv.put(USER_ADDRESS, user.getAddress());
        cv.put(USER_PHONE, user.getPhone());
        cv.put(USER_EMAIL, user.getEmail());

        this.openWritableDB();
        long rowID = db.insert(TABLE_NAME, null, cv);
        this.closeDB();
        return rowID;

    }

    /**
     * delete the user data
     *
     * @param user
     * @return
     */
    public ArrayList<User> deleteUser(User user){
        ArrayList<User> deleteList = new ArrayList<>();
        this.openReadableDB();

        String where = USER_ID + "= ?";
        String[] whereArgs;

        String query = "SELECT * from " + TABLE_NAME;
        Cursor cursor = db.rawQuery(query, null);

        while(cursor.moveToNext()){
            if (user.getFirstName().equals(cursor.getString(USER_FIRST_NAME_COL)) &&
                    user.getLastName().equals(cursor.getString(USER_LAST_NAME_COL)) &&
                    user.getAddress().equals(cursor.getString(USER_ADDRESS_COL)) &&
                    user.getPhone().equals(cursor.getString(USER_PHONE_COL)) &&
                    user.getEmail().equals(cursor.getString(USER_EMAIL_COL))) {
                User user2 = new User(
                        cursor.getString(USER_FIRST_NAME_COL),
                        cursor.getString(USER_LAST_NAME_COL),
                        cursor.getString(USER_ADDRESS_COL),
                        cursor.getString(USER_PHONE_COL),
                        cursor.getString(USER_EMAIL_COL));
                deleteList.add(user2);
                whereArgs= new String[]{String.valueOf(cursor.getInt(USER_ID_COL))};
                db.delete(TABLE_NAME, where, whereArgs);

            }
        }

        if(cursor!= null){
            cursor.close();
        }
        this.closeDB();

        return deleteList;
    }

    public void deleteAll(){
        db = dbHelper.getReadableDatabase();
        db.delete(TABLE_NAME,null,null);
    }

    /**
     * modify user data
     *
     * @param user
     */
    public void modifyUser(User user){
        this.openReadableDB();
        this.openWritableDB();
        String where = USER_ID + "= ?";
        String[] whereArgs;
        ContentValues cv = new ContentValues();
        String query = "SELECT * FROM " + TABLE_NAME;
        Cursor cursor = db.rawQuery(query, null);
        int id;
        int rowCount;
        while(cursor.moveToNext()) {
            if (user.getFirstName().equals(cursor.getString(USER_FIRST_NAME_COL)) &&
                    user.getLastName().equals(cursor.getString(USER_LAST_NAME_COL)) &&
                    user.getAddress().equals(cursor.getString(USER_ADDRESS_COL)) &&
                    user.getPhone().equals(cursor.getString(USER_PHONE_COL)) &&
                    user.getEmail().equals(cursor.getString(USER_EMAIL_COL))) {
                id = cursor.getInt(USER_ID_COL);
                cv.put(USER_ID, id);
                cv.put(USER_FIRST_NAME, user.getFirstName());
                cv.put(USER_LAST_NAME, user.getLastName());
                cv.put(USER_ADDRESS, user.getAddress());
                cv.put(USER_PHONE, user.getPhone());
                cv.put(USER_EMAIL, user.getEmail());

                whereArgs = new String[]{String.valueOf(cursor.getInt(USER_ID_COL))};
                db.update(TABLE_NAME, cv, where, whereArgs);

            }
        }


        this.closeDB();

        //return rowCount;


        /*
        this.openReadableDB();
        String where = USER_ID + "= ?";
        String[] whereArgs;
        ContentValues cv = new ContentValues();
        String query = "SELECT * FROM " + TABLE_NAME;
        Cursor cursor = db.rawQuery(query, null);
        int id;
        while(cursor.moveToNext()){
            if (user.getFirstName().equals(cursor.getString(USER_FIRST_NAME_COL)) &&
                user.getLastName().equals(cursor.getString(USER_LAST_NAME_COL)) &&
                user.getAddress().equals(cursor.getString(USER_ADDRESS_COL)) &&
                user.getPhone().equals(cursor.getString(USER_PHONE_COL)) &&
                user.getEmail().equals(cursor.getString(USER_EMAIL_COL))){
                id = cursor.getInt(USER_ID_COL);
                cv.put(USER_ID, id);
                cv.put(USER_FIRST_NAME, user.getFirstName());
                cv.put(USER_LAST_NAME, user.getLastName());
                cv.put(USER_ADDRESS, user.getAddress());
                cv.put(USER_PHONE, user.getPhone());
                cv.put(USER_EMAIL, user.getEmail());
                this.openWritableDB();
                whereArgs= new String[]{String.valueOf(cursor.getInt(USER_ID_COL))};
                db.update(TABLE_NAME, where, whereArgs);
                db.update()

            }
        }

        this.closeDB();
        */
    }

    /**
     * find users and save into find list
     *
     * @param user find list
     * @return
     */
    public ArrayList<User> findUser(User user){
        ArrayList<User> findList = new ArrayList<>();
        this.openReadableDB();
        String query = "SELECT * FROM " + TABLE_NAME;
        Cursor cursor = db.rawQuery(query, null);


        while(cursor.moveToNext()){
            if (user.getFirstName().equals(cursor.getString(USER_FIRST_NAME_COL))) {
                User user2 = new User(
                        cursor.getString(USER_FIRST_NAME_COL),
                        cursor.getString(USER_LAST_NAME_COL),
                        cursor.getString(USER_ADDRESS_COL),
                        cursor.getString(USER_PHONE_COL),
                        cursor.getString(USER_EMAIL_COL));
                findList.add(user2);

            }
        }
        if(cursor!= null){
            cursor.close();
        }
        this.closeDB();

        return findList;
    }

    /**
     * sort database
     *
     * @return user list
     */
    public ArrayList<User> sortDatabase(){
        this.openReadableDB();
        String query = "SELECT * FROM "+ TABLE_NAME + " ORDER BY " + USER_FIRST_NAME + " ASC";
        ArrayList<User> userList = new ArrayList<>();
        Cursor cursor = db.rawQuery(query,null);
        while(cursor.moveToNext()){
            User user = new User(
                    cursor.getString(USER_FIRST_NAME_COL),
                    cursor.getString(USER_LAST_NAME_COL),
                    cursor.getString(USER_ADDRESS_COL),
                    cursor.getString(USER_PHONE_COL),
                    cursor.getString(USER_EMAIL_COL));
            userList.add(user);

        }
        if(cursor!= null){
            cursor.close();
        }
        this.closeDB();
        return userList;
    }
}
