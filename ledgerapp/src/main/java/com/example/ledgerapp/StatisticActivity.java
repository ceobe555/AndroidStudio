package com.example.ledgerapp;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.ledgerapp.Adapter.BillListAdapter;
import com.example.ledgerapp.Database.LedgerDBHelper;
import com.example.ledgerapp.Entity.BillInfo;
import com.example.ledgerapp.Util.DateUtil;

import org.w3c.dom.Text;

import java.util.Calendar;
import java.util.List;

public class StatisticActivity extends AppCompatActivity implements View.OnClickListener, DatePickerDialog.OnDateSetListener {

    private LedgerDBHelper mDBHelper;
    private TextView tv_month_income;
    private TextView tv_month_expenses;
    private TextView tv_month_net_income;
    private TextView tv_year_income;
    private TextView tv_year_expenses;
    private TextView tv_year_net_income;
    private TextView tv_month;
    private Calendar calendar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_statistic);

        // set header title
        TextView tv_title = findViewById(R.id.tv_title);
        TextView tv_option = findViewById(R.id.tv_option);
        tv_title.setText("数据统计");
        tv_option.setText("");

        tv_month = findViewById(R.id.tv_month);
        Drawable ic_clock = getDrawable(R.drawable.ic_clock);
        ic_clock.setBounds(0, 0, 100, 100);
        tv_month.setCompoundDrawables(ic_clock, null, null, null);

        tv_month_income = findViewById(R.id.tv_month_income);
        tv_month_expenses = findViewById(R.id.tv_month_expenses);
        tv_month_net_income = findViewById(R.id.tv_month_net_income);
        tv_year_income = findViewById(R.id.tv_year_income);
        tv_year_expenses = findViewById(R.id.tv_year_expenses);
        tv_year_net_income = findViewById(R.id.tv_year_net_income);

        findViewById(R.id.tv_home).setOnClickListener(this);
        findViewById(R.id.iv_back).setOnClickListener(this);
        findViewById(R.id.tv_option).setOnClickListener(this);
        findViewById(R.id.tv_month).setOnClickListener(this);

        mDBHelper = LedgerDBHelper.getInstance(this);
        mDBHelper.openReadLink();
        mDBHelper.openWriteLink();

        // get system date
        calendar = Calendar.getInstance();
        tv_month.setText(DateUtil.getDate(calendar));
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;

        computeIncomeAndExpenses(year, month);
    }

    private void computeIncomeAndExpenses(int year, int month) {
        double dMonthIncome = 0, dMonthExpenses = 0, dMonthNetIncome = 0;
        double dYearIncome = 0, dYearExpenses = 0, dYearNetIncome = 0;

        String zeroMonth = month < 10 ? "0" + month : String.valueOf(month);
        String yearMonth = year + "-" + zeroMonth;
        // compute month income and expenses
        List<BillInfo> billInfoList = mDBHelper.queryByMonth(yearMonth);
        for (int ii = 0; ii < billInfoList.size(); ii++) {
            BillInfo bill = billInfoList.get(ii);
            if (bill.bExpenses == 0) {
                dMonthIncome += bill.amount;
            }
            else {
                dMonthExpenses += bill.amount;
            }
        }
        dMonthNetIncome = dMonthIncome - dMonthExpenses;
        tv_month_income.setText(String.valueOf(dMonthIncome));
        tv_month_expenses.setText(String.valueOf(dMonthExpenses));
        tv_month_net_income.setText(String.valueOf(dMonthNetIncome));

        // compute year income and expenses
//        billInfoList = mDBHelper.queryByYear(year);
//        for (int ii = 0; ii < billInfoList.size(); ii++) {
//            BillInfo bill = billInfoList.get(ii);
//            if (bill.bExpenses == 0) {
//                dYearIncome += bill.amount;
//            }
//            else {
//                dYearExpenses += bill.amount;
//            }
//        }
        dYearNetIncome = dYearIncome - dYearExpenses;
        tv_year_income.setText(String.valueOf(dYearIncome));
        tv_year_expenses.setText(String.valueOf(dYearExpenses));
        tv_year_net_income.setText(String.valueOf(dYearNetIncome));

    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.tv_home || v.getId() == R.id.iv_back) {
            finish();
        }
        if (v.getId() == R.id.tv_option) {
            Intent intent = new Intent(this, BillAddActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }
        if (v.getId() == R.id.tv_month) {
            DatePickerDialog dialog = new DatePickerDialog(
                    this,
                    this,
                    calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH));
            dialog.show();
        }
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        // 更新日期的文本显示
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        tv_month.setText(DateUtil.getMonth(calendar));

        computeIncomeAndExpenses(year, month + 1);
    }
}