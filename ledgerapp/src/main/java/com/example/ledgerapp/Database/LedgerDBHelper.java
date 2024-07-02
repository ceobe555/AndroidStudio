package com.example.ledgerapp.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.example.ledgerapp.Entity.BillInfo;

import java.util.ArrayList;
import java.util.List;

public class LedgerDBHelper extends SQLiteOpenHelper {
    private static final String DB_NAME = "ledger.db";
    private static final String TABLE_BILLS_INFO = "bills_info";
    private static final int DB_VERSION = 1;
    private static LedgerDBHelper mHelper = null;
    private SQLiteDatabase mRDB = null;
    private SQLiteDatabase mWDB = null;
    private LedgerDBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    public static LedgerDBHelper getInstance(Context context) {
        if (mHelper == null) {
            mHelper = new LedgerDBHelper(context);
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

    @Override
    public void onCreate(SQLiteDatabase db) {
        // create bill info table
        String sql = "create table if not exists " +
                TABLE_BILLS_INFO +
                "(_id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
                " date VARCHAR NOT NULL," +
                " bExpenses INTEGER NOT NULL," +
                " type INTEGER NOT NULL," +
                " amount DOUBLE NOT NULL," +
                " remark VARCHAR NOT NULL);";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public long save(BillInfo bill) {
        ContentValues cv = new ContentValues();
        cv.put("date", bill.date);
        cv.put("bExpenses", bill.bExpenses);
        cv.put("type", bill.type);
        cv.put("amount", bill.amount);
        cv.put("remark", bill.remark);
        return mWDB.insert(TABLE_BILLS_INFO, null, cv);
    }

    public int DeleteBillItem(long id) {
        return mWDB.delete(TABLE_BILLS_INFO, "_id=?", new String[] {Long.toString(id)});
    }

    public List<BillInfo> queryByMonth(String yearMonth) {
        List<BillInfo> list = new ArrayList<>();
        String sql = "select * from " + TABLE_BILLS_INFO +
                " where date like '" + yearMonth + "%' order by date desc";
        Cursor cursor = mRDB.rawQuery(sql, null);
        while (cursor.moveToNext()) {
            BillInfo bill = new BillInfo();
            int index = cursor.getColumnIndex("_id");
            if (index >= 0) {
                bill.id = cursor.getInt(index);
            }
            index = cursor.getColumnIndex("date");
            if (index >= 0) {
                bill.date = cursor.getString(index);
            }
            index = cursor.getColumnIndex("bExpenses");
            if (index >= 0) {
                bill.bExpenses = cursor.getInt(index);
            }
            index = cursor.getColumnIndex("type");
            if (index >= 0) {
                bill.type = cursor.getInt(index);
            }
            index = cursor.getColumnIndex("amount");
            if (index >= 0) {
                bill.amount = cursor.getDouble(index);
            }
            index = cursor.getColumnIndex("remark");
            if (index >= 0) {
                bill.remark = cursor.getString(index);
            }
            list.add(bill);
        }
        return list;
    }

    public List<BillInfo> queryByYear(int year) {
        List<BillInfo> list = new ArrayList<>();
        String sql = "select * from " + TABLE_BILLS_INFO +
                " where date like '" + year + "%' order by date desc";
        Cursor cursor = mRDB.rawQuery(sql, null);
        while (cursor.moveToNext()) {
            BillInfo bill = new BillInfo();
            int index = cursor.getColumnIndex("_id");
            if (index >= 0) {
                bill.id = cursor.getInt(index);
            }
            index = cursor.getColumnIndex("date");
            if (index >= 0) {
                bill.date = cursor.getString(index);
            }
            index = cursor.getColumnIndex("bExpenses");
            if (index >= 0) {
                bill.bExpenses = cursor.getInt(index);
            }
            index = cursor.getColumnIndex("type");
            if (index >= 0) {
                bill.type = cursor.getInt(index);
            }
            index = cursor.getColumnIndex("amount");
            if (index >= 0) {
                bill.amount = cursor.getDouble(index);
            }
            index = cursor.getColumnIndex("remark");
            if (index >= 0) {
                bill.remark = cursor.getString(index);
            }
            list.add(bill);
        }
        return list;
    }

    public List<BillInfo> queryByDate(String date) {
        List<BillInfo> list = new ArrayList<>();
        String sql = "select * from " + TABLE_BILLS_INFO +
                " where date like '" + date + "%' order by date desc";
        Cursor cursor = mRDB.rawQuery(sql, null);
        while (cursor.moveToNext()) {
            BillInfo bill = new BillInfo();
            int index = cursor.getColumnIndex("_id");
            if (index >= 0) {
                bill.id = cursor.getInt(index);
            }
            index = cursor.getColumnIndex("date");
            if (index >= 0) {
                bill.date = cursor.getString(index);
            }
            index = cursor.getColumnIndex("bExpenses");
            if (index >= 0) {
                bill.bExpenses = cursor.getInt(index);
            }
            index = cursor.getColumnIndex("type");
            if (index >= 0) {
                bill.type = cursor.getInt(index);
            }
            index = cursor.getColumnIndex("amount");
            if (index >= 0) {
                bill.amount = cursor.getDouble(index);
            }
            index = cursor.getColumnIndex("remark");
            if (index >= 0) {
                bill.remark = cursor.getString(index);
            }
            list.add(bill);
        }
        return list;
    }

    public List<BillInfo> queryAllBill() {
        List<BillInfo> list = new ArrayList<>();
        String sql = "select * from " + TABLE_BILLS_INFO;
        Cursor cursor = mRDB.rawQuery(sql, null);
        while (cursor.moveToNext()) {
            BillInfo bill = new BillInfo();
            int index = cursor.getColumnIndex("_id");
            if (index >= 0) {
                bill.id = cursor.getInt(index);
            }
            index = cursor.getColumnIndex("date");
            if (index >= 0) {
                bill.date = cursor.getString(index);
            }
            index = cursor.getColumnIndex("bExpenses");
            if (index >= 0) {
                bill.bExpenses = cursor.getInt(index);
            }
            index = cursor.getColumnIndex("type");
            if (index >= 0) {
                bill.type = cursor.getInt(index);
            }
            index = cursor.getColumnIndex("amount");
            if (index >= 0) {
                bill.amount = cursor.getDouble(index);
            }
            index = cursor.getColumnIndex("remark");
            if (index >= 0) {
                bill.remark = cursor.getString(index);
            }
            list.add(bill);
        }
        return list;
    }
}
