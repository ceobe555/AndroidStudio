package com.example.ledgerapp;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.viewpager.widget.PagerTabStrip;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.widget.ViewPager2;

import com.example.ledgerapp.Adapter.BillPagerAdapter;
import com.example.ledgerapp.Database.LedgerDBHelper;
import com.example.ledgerapp.Entity.BillInfo;
import com.example.ledgerapp.Fragment.BillFragment;
import com.example.ledgerapp.Util.DateUtil;
import com.example.ledgerapp.Util.ToastUtil;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.Calendar;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, DatePickerDialog.OnDateSetListener {

    private TextView tv_month;
    private Calendar calendar;
    private LedgerDBHelper mDBHelper;
    private ViewPager2 vp_bill;
    private ListView lv_bill;
    private BillPagerAdapter mPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        TextView tv_title = findViewById(R.id.tv_title);
        TextView tv_option = findViewById(R.id.tv_option);
        Drawable ic_bill_add = getDrawable(R.drawable.ic_bill_add);
        ic_bill_add.setBounds(0, 0, 130, 130);
        tv_title.setText(R.string.app_name);
        tv_option.setText("");
        tv_option.setCompoundDrawables(null, null, ic_bill_add,null);

        tv_option.setOnClickListener(this);
        findViewById(R.id.iv_back).setOnClickListener(this);
        findViewById(R.id.tv_statistics).setOnClickListener(this);
        findViewById(R.id.tv_mine).setOnClickListener(this);

        tv_month = findViewById(R.id.tv_month);
        Drawable ic_clock = getDrawable(R.drawable.ic_clock);
        ic_clock.setBounds(0, 0, 100, 100);
        tv_month.setCompoundDrawables(ic_clock, null, null, null);
        calendar = Calendar.getInstance();
        tv_month.setText(DateUtil.getMonth(calendar));
        tv_month.setOnClickListener(this);

        mDBHelper = LedgerDBHelper.getInstance(this);
        mDBHelper.openReadLink();
        mDBHelper.openWriteLink();

        initViewPager();
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateBills();
    }

    // 初始化翻页视图
    public void initViewPager() {
        TabLayout tl_bill = (TabLayout) findViewById(R.id.tl_bill);

        vp_bill = findViewById(R.id.vp_bill);
        updateBills();

        new TabLayoutMediator(tl_bill, vp_bill, (tab, position) ->
                tab.setText((position + 1) + "月份")).attach();
    }

    public void updateBills() {
        mPagerAdapter = new BillPagerAdapter(this, calendar.get(Calendar.YEAR));
        vp_bill.setAdapter(mPagerAdapter);
        vp_bill.setCurrentItem(calendar.get(Calendar.MONTH));
        vp_bill.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                // 更新日期的文本显示
                calendar.set(Calendar.MONTH, position);
                tv_month.setText(DateUtil.getMonth(calendar));
            }
        });

        TextView tv_day_income = findViewById(R.id.tv_day_income);
        TextView tv_day_expenses = findViewById(R.id.tv_day_expenses);
        double dDayIncome = 0, dDayExpenses = 0;
        // get system date
        calendar = Calendar.getInstance();
        String date = DateUtil.getDate(calendar);

        // compute day income and expenses
        List<BillInfo> billInfoList = mDBHelper.queryByDate(date);
        for (int ii = 0; ii < billInfoList.size(); ii++) {
            BillInfo bill = billInfoList.get(ii);
            if (bill.bExpenses == 0) {
                dDayIncome += bill.amount;
            }
            else {
                dDayExpenses += bill.amount;
            }
        }

        tv_day_income.setText(String.valueOf(dDayIncome));
        tv_day_expenses.setText(String.valueOf(dDayExpenses));
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.tv_option) {
            Intent intent = new Intent(this, BillAddActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }
        else if (v.getId() == R.id.iv_back) {
            finish();
        }
        else if (v.getId() == R.id.tv_month) {
            DatePickerDialog dialog = new DatePickerDialog(
                    this,
                    this,
                    calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH));
                    dialog.show();
        }
        else if (v.getId() == R.id.tv_statistics) {
            Intent intent = new Intent(this, StatisticActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }
        else if (v.getId() == R.id.tv_mine) {
            Intent intent = new Intent(this, MineActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }
    }

    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        // 更新日期的文本显示
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        tv_month.setText(DateUtil.getMonth(calendar));

        mPagerAdapter = new BillPagerAdapter(this, year);
        vp_bill.setAdapter(mPagerAdapter);
        vp_bill.setCurrentItem(month);
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {
        super.onPointerCaptureChanged(hasCapture);
    }

}