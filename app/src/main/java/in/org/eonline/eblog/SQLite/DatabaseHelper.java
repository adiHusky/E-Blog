package in.org.eonline.eblog.SQLite;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "EBlog.db";
    public static final String TABLE_USER = "userProfile";
    public static final String col_1 = "firstName";
    public static final String col_2 = "lastName";
    public static final String col_3 = "email";
    public static final String col_4 = "mobileNumber";

    private static final String SQL_REGISTER_USER =" Create TABLE " + TABLE_USER + "(" + col_1 + " TEXT, " + col_2 + " TEXT, " + col_3 + " TEXT, " + col_4 + " TEXT )" ;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME,null,1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(SQL_REGISTER_USER);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE  IF EXISTS " + SQL_REGISTER_USER);
    }

    public boolean insertUserDataInSQLite(String firstName, String lastName, String email , String mobileNumber){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(col_1, firstName);
        cv.put(col_2, lastName);
        cv.put(col_3, email);
        cv.put(col_4, mobileNumber);

        long result = db.insert(TABLE_USER, null, cv);
        if (result == -1)  // if result is -1 then we could not insert value in database table so we are returning false boolean
            return false;
        else
            return true;   // else we will return true boolean because result is not -1 so data is inserted successfully
    }
}
