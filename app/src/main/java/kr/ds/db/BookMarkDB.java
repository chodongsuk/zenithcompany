package kr.ds.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class BookMarkDB {
	private static final String TAG = "BOOKMARKDATABASE";
	
	public static final String ID = "_id";
	public static final String TITLE = "title";
    public static final String URL = "url";
    public static final String IMAGE = "image";

	
    private DatabaseHelper mDbHelper;
    private SQLiteDatabase mDb;
    private static final String DATABASE_CREATE = "create table bookmark (_id integer primary key autoincrement, " +
    																"title text not null, "+
                                                                    "url text not null "+
    																");";    
    private static final String DATABASE_NAME = "zenithcompany.db";
    private static final String DATABASE_TABLE = "bookmark";
    private static final int DATABASE_VERSION = 2;
    private final Context mCtx;
    
    private static class DatabaseHelper extends SQLiteOpenHelper {
        DatabaseHelper(Context context) { 
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override 
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(DATABASE_CREATE);
        } 
        
        @Override 
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Log.w(TAG, "Upgrading database from version " + oldVersion + " to " + newVersion + ", which will destroy all old data");

            switch (oldVersion) {
                case 1 :
                    try {
                        Log.i("TEST","111");
                        db.beginTransaction();
                        db.execSQL("alter table bookmark add column image text not null");
                        db.setTransactionSuccessful();
                    } catch (IllegalStateException e) {

                    } finally {
                        db.endTransaction();
                    };
                    break;
            }
        }
    }
    
    public BookMarkDB(Context ctx) {
    	this.mCtx = ctx;
    }

    public BookMarkDB open() throws SQLException {
        mDbHelper = new DatabaseHelper(mCtx);
        mDb = mDbHelper.getWritableDatabase();
        return this; 
    }
    
    public void close() {
        mDbHelper.close();
    }
    
    // 북마크데이터 Insert Type1
    public long createNote(String title, String url, String image) {
        ContentValues initialValues = new ContentValues();
        initialValues.put(TITLE, title);
        initialValues.put(URL, url);
        initialValues.put(IMAGE, image);

        return mDb.insert(DATABASE_TABLE, null, initialValues);
    }
  
    
    // 북마크데이터 Delete
    public boolean deleteNote(String url) throws SQLException {
        return mDb.delete(DATABASE_TABLE, URL + "= '" + url + "'", null) > 0;
    }
    
    // 북마크데이터 모든데이터 Select
    public Cursor fetchAllForType() throws SQLException {
        Cursor mCursor = mDb.query(DATABASE_TABLE, new String[] { ID, TITLE, URL, IMAGE}, null, null, null, null, ID +" DESC");
        return mCursor; 
    }
    
    // 북마크데이터 중복데이터 확인
    public Cursor BookMarkConfirm(String url) throws SQLException {
        Cursor mCursor = mDb.query(true,
        		DATABASE_TABLE,
        		new String[] {URL },
                URL + "= '" + url + "'", null, null, null, null, null);

        if (mCursor != null) { 
            mCursor.moveToFirst(); 
        }
        return mCursor; 
    }
} 

