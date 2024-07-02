package com.example.ledgerapp;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.ledgerapp.Database.LedgerDBHelper;
import com.example.ledgerapp.Entity.BillInfo;
import com.example.ledgerapp.Util.DateUtil;
import com.example.ledgerapp.Util.ToastUtil;

import java.util.Arrays;
import java.util.Calendar;

public class BillModifyActivity extends AppCompatActivity implements View.OnClickListener, DatePickerDialog.OnDateSetListener {

    private TextView tv_date;
    private RadioGroup rg_type;
    private EditText et_remark;
    private EditText et_amount;
    private Calendar calendar;
    private Spinner sp_type;
    private LedgerDBHelper mDBHelper;
    private long mItemId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_bill_modify);

        TextView tv_title = findViewById(R.id.tv_title);
        TextView tv_option = findViewById(R.id.tv_option);
        tv_title.setText("修改账单");
        tv_option.setText("");

        tv_date = findViewById(R.id.tv_date);
        rg_type = findViewById(R.id.rg_type);
        sp_type = findViewById(R.id.sp_type);
        et_remark = findViewById(R.id.et_remark);
        et_amount = findViewById(R.id.et_amount);

        String[] aTypes = getResources().getStringArray(R.array.bill_type);
        ArrayAdapter<String> typeAdapter = new ArrayAdapter<String>(this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, aTypes);
        sp_type.setAdapter(typeAdapter);

        // 将intent中的数据设置到UI上
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            // get data
            mItemId = bundle.getLong("id");
            String date = bundle.getString("date");
            String type = bundle.getString("type");
            String remark = bundle.getString("remark");
            String strAmount = bundle.getString("amount");

            String amount = strAmount.substring(1, strAmount.length() - 2);
            int typeIndex = 0;
            for (int ii = 0; ii < aTypes.length; ii++) {
                if (aTypes[ii].equals(type)) {
                    typeIndex = ii;
                    break;
                }
            }

            // set data
            // 更新日期显示
            calendar = Calendar.getInstance();
            tv_date.setText(date);
            String[] arr = date.split("-");
            assert arr.length == 3;
            int year = Integer.parseInt(arr[0]);
            int month = Integer.parseInt(arr[1]) - 1;
            int dayOfMonth = Integer.parseInt(arr[2]);
            calendar.set(Calendar.YEAR, year);
            calendar.set(Calendar.MONTH, month);
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

            if (strAmount.charAt(0) == '+') {
                rg_type.check(R.id.rb_income);
            }
            else {
                rg_type.check(R.id.rb_cost);
            }
            sp_type.setSelection(typeIndex);
            et_remark.setText(remark);
            et_amount.setText(amount);
        }



        // for Database
        mDBHelper = LedgerDBHelper.getInstance(this);
        mDBHelper.openReadLink();
        mDBHelper.openWriteLink();

        tv_date.setOnClickListener(this);
        findViewById(R.id.iv_back).setOnClickListener(this);
        findViewById(R.id.btn_modify).setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.tv_date) {
            DatePickerDialog dialog = new DatePickerDialog(
                    this,
                    this,
                    calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH));
            dialog.show();
        }
        else if (v.getId() == R.id.btn_modify) {
            BillInfo bill = new BillInfo();
            bill.date = tv_date.getText().toString();
            bill.bExpenses = rg_type.getCheckedRadioButtonId() == R.id.rb_income ?
                    BillInfo.BILL_TYPE_INCOME : BillInfo.BILL_TYPE_COST;
            bill.type = sp_type.getSelectedItemPosition();
            bill.remark = et_remark.getText().toString();
            bill.amount = Double.parseDouble(et_amount.getText().toString());
            try {
                long rc = mDBHelper.DeleteBillItem(mItemId);
                assert rc > 0;
                rc = mDBHelper.save(bill);
                if (rc > 0) {
                    ToastUtil.show(this,"修改账单成功！");
                    Intent intent = new Intent();
                    Bundle bundle = new Bundle();
                    bundle.putString("date", bill.date);
                    intent.putExtras(bundle);
                    setResult(Activity.RESULT_OK, intent);
                    finish();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        else if (v.getId() == R.id.iv_back) {
            finish();
        }
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        // 日期文本显示
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        tv_date.setText(DateUtil.getDate(calendar));
    }
}