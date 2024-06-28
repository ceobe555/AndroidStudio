package com.example.appdevelopment;

import android.annotation.SuppressLint;
import android.content.ContentProviderOperation;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.OperationApplicationException;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.RemoteException;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;
import java.util.List;

public class ContactAddActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText et_contact_name;
    private EditText et_contact_phone;
    private EditText et_contact_email;
    private SmsGetObserver mObserver;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_contact_add);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        et_contact_name = findViewById(R.id.et_contact_name);
        et_contact_phone = findViewById(R.id.et_contact_phone);
        et_contact_email = findViewById(R.id.et_contact_email);
        findViewById(R.id.btn_add_contact).setOnClickListener(this);
        findViewById(R.id.btn_read_contact).setOnClickListener(this);

        Uri uri = Uri.parse("content://sms");
        mObserver = new SmsGetObserver(this);
        getContentResolver().registerContentObserver(uri, true, mObserver);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        getContentResolver().unregisterContentObserver(mObserver);
    }

    private static class SmsGetObserver extends ContentObserver {
        private final Context mContext;
        public SmsGetObserver(Context context) {
            super(new Handler(Looper.getMainLooper()));
            this.mContext = context;
        }

        @SuppressLint("Range")
        @Override
        public void onChange(boolean selfChange, @Nullable Uri uri) {
            super.onChange(selfChange, uri);
            if (uri == null) {
                return;
            }
            if (uri.toString().contains("content://sms/raw") || uri.toString().equals("content://sms")) {
                return;
            }
            // 通过内容解析器获取符合条件的结果集游标
            Cursor cursor = mContext.getContentResolver().query(uri, new String[] {"address", "body", "date"}, null, null, "date DESC");
            if (cursor.moveToNext()) {
                String sender = cursor.getString(cursor.getColumnIndex("address"));
                String content = cursor.getString(cursor.getColumnIndex("body"));
                Log.d("ceobe", String.format("sender: %s, content:%s", sender, content));
            }
            cursor.close();
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_add_contact) {
            Contact contact = new Contact();
            contact.name = et_contact_name.getText().toString().trim();
            contact.phone = et_contact_phone.getText().toString().trim();
            contact.email = et_contact_email.getText().toString().trim();

            // 方法一：使用ContentResolver多次写入，每次写入一个字段
            //addContacts(getContentResolver(), contact);
            // 方法二：批处理方式，好处是全部成功或者全部失败
            addFullContacts(getContentResolver(), contact);

        }
        else if (v.getId() == R.id.btn_read_contact) {
            readPhoneContacts(getContentResolver());
        }
    }

    @SuppressLint("Range")
    private void readPhoneContacts(ContentResolver resolver) {
        // 先查询raw_contacts表，再根据raw_contacts_id查询data表
        Cursor cursor = resolver.query(ContactsContract.RawContacts.CONTENT_URI, new String[]{ContactsContract.RawContacts._ID}, null, null, null, null);
        while (cursor.moveToNext()) {
            int rawContactId = cursor.getInt(cursor.getColumnIndex(ContactsContract.Contacts._ID));
            Uri uri = Uri.parse("content://com.android.contacts/contacts/" + rawContactId + "/data");
            //uri = ContactsContract.Contacts.CONTENT_URI;
            Cursor dataCursor = resolver.query(uri, null, null, null, null);

            Contact contact = new Contact();
            while (dataCursor.moveToNext()) {
                String data1 = dataCursor.getString(dataCursor.getColumnIndex(ContactsContract.Contacts.Data.DATA1));
                String mimeType = dataCursor.getString(dataCursor.getColumnIndex(ContactsContract.Contacts.Data.MIMETYPE));
                if (mimeType == ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE) {
                    contact.name = data1;
                }
                else if (mimeType == ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE) {
                    contact.phone = data1;
                }
                else if (mimeType == ContactsContract.CommonDataKinds.Email.CONTENT_ITEM_TYPE) {
                    contact.email = data1;
                }
            }
            dataCursor.close();

            if (contact.name != null) {
                Log.d("ceobe", contact.toString());
            }
        }
    }

    // 往手机通讯录一次性添加一个联系人的信息
    private void addFullContacts(ContentResolver resolver, Contact contact) {
        // 创建一个插入联系人主记录的内容操作器
        ContentProviderOperation op_main = ContentProviderOperation
                .newInsert(ContactsContract.RawContacts.CONTENT_URI)
                .withValue(ContactsContract.RawContacts.ACCOUNT_NAME, null)
                .build();

        // 创建一个插入联系人姓名记录的内容操作器
        ContentProviderOperation op_name = ContentProviderOperation
                .newInsert(ContactsContract.Data.CONTENT_URI)
                .withValueBackReference(ContactsContract.Contacts.Data.RAW_CONTACT_ID, 0)
                .withValue(ContactsContract.Contacts.Data.MIMETYPE, ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE)
                .withValue(ContactsContract.Contacts.Data.DATA2, contact.name)
                .build();

        ContentProviderOperation op_phone = ContentProviderOperation
                .newInsert(ContactsContract.Data.CONTENT_URI)
                .withValueBackReference(ContactsContract.Contacts.Data.RAW_CONTACT_ID, 0)
                .withValue(ContactsContract.Contacts.Data.MIMETYPE, ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE)
                .withValue(ContactsContract.Contacts.Data.DATA1, contact.phone)
                .withValue(ContactsContract.Contacts.Data.DATA2, ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE)
                .build();

        ContentProviderOperation op_email = ContentProviderOperation
                .newInsert(ContactsContract.Data.CONTENT_URI)
                .withValueBackReference(ContactsContract.Contacts.Data.RAW_CONTACT_ID, 0)
                .withValue(ContactsContract.Contacts.Data.MIMETYPE, ContactsContract.CommonDataKinds.Email.CONTENT_ITEM_TYPE)
                .withValue(ContactsContract.Contacts.Data.DATA1, contact.email)
                .withValue(ContactsContract.Contacts.Data.DATA2, ContactsContract.CommonDataKinds.Email.TYPE_WORK)
                .build();

        ArrayList<ContentProviderOperation> operations = new ArrayList<>();
        operations.add(op_main);
        operations.add(op_name);
        operations.add(op_phone);
        operations.add(op_email);
        try {
            resolver.applyBatch(ContactsContract.AUTHORITY, operations);
        } catch (OperationApplicationException e) {
            e.printStackTrace();
        } catch (RemoteException e) {
            e.printStackTrace();
        }

    }

    private void addContacts(ContentResolver resolver, Contact contact) {
        ContentValues values = new ContentValues();
        // 往raw_contacts添加联系人记录，并获取添加后的联系人ID
        Uri uri = resolver.insert(ContactsContract.RawContacts.CONTENT_URI, values);
        long rawContactId = ContentUris.parseId(uri);

        ContentValues name = new ContentValues();
        name.put(ContactsContract.Contacts.Data.RAW_CONTACT_ID, rawContactId);
        name.put(ContactsContract.Contacts.Data.MIMETYPE, ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE);
        name.put(ContactsContract.Contacts.Data.DATA2, contact.name);
        resolver.insert(ContactsContract.Data.CONTENT_URI, name);

        ContentValues phone = new ContentValues();
        phone.put(ContactsContract.Contacts.Data.RAW_CONTACT_ID, rawContactId);
        phone.put(ContactsContract.Contacts.Data.MIMETYPE, ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE);
        phone.put(ContactsContract.Contacts.Data.DATA1, contact.phone);
        // 联系类型，1表示家庭，2表示工作
        phone.put(ContactsContract.Contacts.Data.DATA2, ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE);
        resolver.insert(ContactsContract.Data.CONTENT_URI, phone);

        ContentValues email = new ContentValues();
        email.put(ContactsContract.Contacts.Data.RAW_CONTACT_ID, rawContactId);
        email.put(ContactsContract.Contacts.Data.MIMETYPE, ContactsContract.CommonDataKinds.Email.CONTENT_ITEM_TYPE);
        email.put(ContactsContract.Contacts.Data.DATA2, contact.email);
        email.put(ContactsContract.Contacts.Data.DATA2, ContactsContract.CommonDataKinds.Email.TYPE_WORK);
        resolver.insert(ContactsContract.Data.CONTENT_URI, email);
    }
}