package com.silentselene.enterpriseedition.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

public class DBAdapter {
    private static final String DB_NAME = "wifi.db";
    private static final String DB_TABLE = "wifiRecord";
    private static final int DB_VERSION = 1;

    private static final String KEY_ID = "_id";
    private static final String KEY_MAC = "mac";
    private static final String KEY_ALIAS = "alias";

    private SQLiteDatabase db;
    private final Context context;


    public DBAdapter(Context _context) {
        this.context = _context;
    }

    // 打开数据库
    public void open() throws SQLiteException {
        DBOpenHelper dbOpenHelper = new DBOpenHelper(context, DB_NAME, null, DB_VERSION);
        try {
            /*取得的实例是以读写的方式打开数据库，如果打开的数据库磁盘满了，此时只能读不能写.
            此时调用了getWritableDatabase的实例，那么将会发生错误*/
            db = dbOpenHelper.getWritableDatabase();
        } catch (SQLiteException ex) {
            /*取得的实例是先调用getWritableDatabase以读写的方式打开数据库，如果数据库的磁盘满了，
            此时返回打开失败，继而用getReadableDatabase的实例以只读的方式去打开数据库*/
            db = dbOpenHelper.getReadableDatabase();
        }
    }

    // Close the database
    public void close() {
        if (db != null) {
            db.close();
            db = null;
        }
    }

    public long insert(WiFiRecord wifi) {
        ContentValues newValues = new ContentValues();

        newValues.put(KEY_MAC, wifi.getMAC());
        newValues.put(KEY_ALIAS, wifi.getALIAS());

        return db.insert(DB_TABLE, null, newValues);
    }

    public WiFiRecord[] queryAllData() {
        Cursor results = db.query(DB_TABLE, new String[]{KEY_ID, KEY_MAC, KEY_ALIAS},
                null, null, null, null, null);
        return ConvertToWifiRecord(results);
    }

    public WiFiRecord[] queryOneData(long id) {
        Cursor results = db.query(DB_TABLE, new String[]{KEY_ID, KEY_MAC, KEY_ALIAS},
                KEY_ID + "=" + id, null, null, null, null);
        return ConvertToWifiRecord(results);
    }

    private WiFiRecord[] ConvertToWifiRecord(Cursor cursor) {
        int resultCounts = cursor.getCount();
        if (resultCounts == 0 || !cursor.moveToFirst()) {
            return null;
        }
        WiFiRecord[] wiFiRecords = new WiFiRecord[resultCounts];
        for (int i = 0; i < resultCounts; i++) {
            wiFiRecords[i] = new WiFiRecord();
            wiFiRecords[i].setID(cursor.getInt(0));
            wiFiRecords[i].setMAC(cursor.getString(cursor.getColumnIndex(KEY_MAC)));
            wiFiRecords[i].setALIAS(cursor.getString(cursor.getColumnIndex(KEY_ALIAS)));
            cursor.moveToNext();
        }
        return wiFiRecords;
    }

    public long deleteAllData() {
        return db.delete(DB_TABLE, null, null);
    }

    public long deleteOneData(long id) {
        return db.delete(DB_TABLE, KEY_ID + "=" + id, null);
    }

    public long updateOneData(long id, WiFiRecord wifi) {
        ContentValues updateValues = new ContentValues();
        updateValues.put(KEY_MAC, wifi.getMAC());
        updateValues.put(KEY_ALIAS, wifi.getALIAS());
        return db.update(DB_TABLE, updateValues, KEY_ID + "=" + id, null);
    }

    /* 静态Helper类，用于建立、更新和打开数据库 */
    private static class DBOpenHelper extends SQLiteOpenHelper {

        DBOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
            super(context, name, factory, version);
        }

        /**
         * 手动创建表的SQL命令
         */
        private static final String DB_CREATE = "create table " + DB_TABLE + "(" + KEY_ID
                + " integer primary key autoincrement, " + KEY_MAC + " text not null, " + KEY_ALIAS + " text not null);";

        /**
         * 创建数据库中的表，并进行初始化工作
         */
        @Override
        public void onCreate(SQLiteDatabase _db) {
            _db.execSQL(DB_CREATE);
        }

        /**
         * 更新表，为了简单起见，并没有做任何的的数据转移，而仅仅删除原有的表后建立新的数据库表
         */
        @Override
        public void onUpgrade(SQLiteDatabase _db, int _oldVersion, int _newVersion) {
            _db.execSQL("DROP TABLE IF EXISTS " + DB_TABLE);
            onCreate(_db);
        }
    }
}
