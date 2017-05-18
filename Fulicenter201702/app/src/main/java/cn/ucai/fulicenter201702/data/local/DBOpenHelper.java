package cn.ucai.fulicenter201702.data.local;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by clawpo on 2017/5/10.
 */

public class DBOpenHelper extends SQLiteOpenHelper {
    private final static int versionNo = 1;
    private static DBOpenHelper instance;

    public static final String USER_TABLE_NAME = "t_fulicenter_user";
    public static final String USER_COLUMN_NAME = "m_user_name";
    public static final String USER_COLUMN_NICK = "m_user_nick";
    public static final String USER_COLUMN_AVATAR = "m_user_avatar_id";
    public static final String USER_COLUMN_AVATAR_PATH = "m_user_avatar_path";
    public static final String USER_COLUMN_AVATAR_SUFFIX = "m_user_avatar_suffix";
    public static final String USER_COLUMN_AVATAR_TYPE = "m_user_avatar_type";
    public static final String USER_COLUMN_AVATAR_UPDATE_TIME = "m_user_update_time";

    private static final String FULICENTER_USER_TABLE_CREATE =
            "CREATE TABLE " + USER_TABLE_NAME + " ("
                    + USER_COLUMN_NAME + " TEXT PRIMARY KEY,"
                    + USER_COLUMN_NICK + " TEXT,"
                    + USER_COLUMN_AVATAR + " INTEGER,"
                    + USER_COLUMN_AVATAR_PATH + " TEXT,"
                    + USER_COLUMN_AVATAR_TYPE + " INTEGER,"
                    + USER_COLUMN_AVATAR_SUFFIX + " TEXT,"
                    + USER_COLUMN_AVATAR_UPDATE_TIME + " TEXT);";
    
    private DBOpenHelper(Context context) {
        super(context, getDatabasesName(), null, versionNo);
    }

    public synchronized static DBOpenHelper getInstance(Context context){
        if (instance==null){
            instance = new DBOpenHelper(context);
        }
        return instance;
    }

    private static String getDatabasesName() {
        return "cn.ucai.fulicenter.db";
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(FULICENTER_USER_TABLE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void closeDB(){
        if (instance!=null){
            SQLiteDatabase db = instance.getWritableDatabase();
            db.close();
            instance = null;
        }
    }
}
