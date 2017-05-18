package cn.ucai.fulicenter201702.data.local;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import cn.ucai.fulicenter201702.data.bean.User;

/**
 * Created by clawpo on 2017/5/10.
 */

public class DBManager {
    private static DBOpenHelper sHelper;
    private static DBManager mDBManager = new DBManager();

    public static DBManager getInstance(){
        return mDBManager;
    }

    public synchronized void initDB(Context context){
        sHelper = DBOpenHelper.getInstance(context);
    }

    public synchronized boolean saveUser(User user){
        SQLiteDatabase database = sHelper.getWritableDatabase();
        if (database.isOpen()){
            ContentValues values = new ContentValues();
            values.put(DBOpenHelper.USER_COLUMN_NAME,user.getMuserName());
            values.put(DBOpenHelper.USER_COLUMN_NICK,user.getMuserNick());
            values.put(DBOpenHelper.USER_COLUMN_AVATAR,user.getMavatarId());
            values.put(DBOpenHelper.USER_COLUMN_AVATAR_PATH,user.getMavatarPath());
            values.put(DBOpenHelper.USER_COLUMN_AVATAR_TYPE,user.getMavatarType());
            values.put(DBOpenHelper.USER_COLUMN_AVATAR_SUFFIX,user.getMavatarSuffix());
            values.put(DBOpenHelper.USER_COLUMN_AVATAR_UPDATE_TIME,user.getMavatarLastUpdateTime());
            return database.replace(DBOpenHelper.USER_TABLE_NAME,null,values)!=-1;
        }
        return false;
    }

    public synchronized User getUser(String username) {
        User user = null;
        SQLiteDatabase database = sHelper.getReadableDatabase();
        if (database.isOpen()) {
            String sql = "select * from " + DBOpenHelper.USER_TABLE_NAME
                    + " where " + DBOpenHelper.USER_COLUMN_NAME + "='" + username + "'";
            Cursor cursor = database.rawQuery(sql, null);
            while (cursor.moveToNext()) {
                user = new User();
                user.setMuserName(username);
                user.setMuserNick(cursor.getString(cursor.getColumnIndex(DBOpenHelper.USER_COLUMN_NICK)));
                user.setMavatarId(cursor.getInt(cursor.getColumnIndex(DBOpenHelper.USER_COLUMN_AVATAR)));
                user.setMavatarType(cursor.getInt(cursor.getColumnIndex(DBOpenHelper.USER_COLUMN_AVATAR_TYPE)));
                user.setMavatarSuffix(cursor.getString(cursor.getColumnIndex(DBOpenHelper.USER_COLUMN_AVATAR_SUFFIX)));
                user.setMavatarPath(cursor.getString(cursor.getColumnIndex(DBOpenHelper.USER_COLUMN_AVATAR_PATH)));
                user.setMavatarLastUpdateTime(cursor.getString(cursor.getColumnIndex(DBOpenHelper.USER_COLUMN_AVATAR_UPDATE_TIME)));
                return user;

            }
        }
        return user;
    }

    public synchronized void closeDB() {
        sHelper.closeDB();
    }
}
