package com.example.billapp.datebase;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.icu.text.ListFormatter;

import com.example.billapp.entity.BillInfo;

import java.util.ArrayList;
import java.util.List;


public class BillDBHelper extends SQLiteOpenHelper {
    private static final String DB_NAME = "bill.db";
    // 账单信息表
    private static final String TABLE_BILLS_INFO = "bills_info";

    private static final int DB_VERSION = 1;
    private static BillDBHelper mHelper = null;
    private SQLiteDatabase mRDB = null;
    private SQLiteDatabase mWDB = null;

    private BillDBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }
    public static BillDBHelper getInstance(Context context) {
        if (mHelper == null) {
            mHelper = new BillDBHelper(context);
        }
        return mHelper;
    }

    public SQLiteDatabase openReadLink() {
        if (mRDB == null || !mRDB.isOpen()) {
            mRDB = mHelper.getReadableDatabase();
        }
        return mRDB;
    }
    public SQLiteDatabase openWriteLink() {
        if (mWDB == null || !mWDB.isOpen()) {
            mWDB = mHelper.getWritableDatabase();
        }
        return mWDB;
    }
    public void closeLink() {
        if (mRDB != null && mRDB.isOpen()) {
            mRDB.close();
            mRDB = null;
        }
        if (mWDB != null && mWDB.isOpen()) {
            mWDB.close();
            mWDB = null;
        }
    }
    public void onCreate(SQLiteDatabase db) {
        // 创建账单信息表
        String sql = "CREATE TABLE IF NOT EXISTS " + TABLE_BILLS_INFO +
                "(_id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
                " date VARCHAR NOT NULL," +
                " type INTEGER NOT NULL," +
                " amount DOUBLE NOT NULL," +
                " remark VARCHAR NOT NULL);";
        db.execSQL(sql);

    }
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    // 保存一条账单记录
    public long save(BillInfo bill) {
        ContentValues cv = new ContentValues();
        cv.put("date", bill.date);
        cv.put("type", bill.type);
        cv.put("amount", bill.amount);
        cv.put("remark", bill.remark);
        return mWDB.insert(TABLE_BILLS_INFO, null, cv);
    }

    @SuppressLint("Range")
    public List<BillInfo> queryByMonth(String yearMonth) {
        List<BillInfo> list = new ArrayList<>();
        String sql = "select * from " + TABLE_BILLS_INFO +
                " where date like '" + yearMonth + "%'";
        Cursor cursor = mRDB.rawQuery(sql, null);
        while (cursor.moveToNext()) {
            BillInfo bill = new BillInfo();
            bill.id = cursor.getInt(cursor.getColumnIndex("_id"));
            bill.date = cursor.getString(cursor.getColumnIndex("date"));
            bill.type = cursor.getInt(cursor.getColumnIndex("type"));
            bill.amount = cursor.getDouble(cursor.getColumnIndex("amount"));
            bill.remark = cursor.getString(cursor.getColumnIndex("remark"));
            list.add(bill);
        }

        return list;
    }
}










