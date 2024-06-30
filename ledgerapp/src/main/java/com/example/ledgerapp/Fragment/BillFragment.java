package com.example.ledgerapp.Fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.ledgerapp.Adapter.BillListAdapter;
import com.example.ledgerapp.Adapter.BillPagerAdapter;
import com.example.ledgerapp.BillAddActivity;
import com.example.ledgerapp.BillModifyActivity;
import com.example.ledgerapp.Database.LedgerDBHelper;
import com.example.ledgerapp.Entity.BillInfo;
import com.example.ledgerapp.R;
import com.example.ledgerapp.Util.DateUtil;
import com.example.ledgerapp.Util.ToastUtil;

import org.w3c.dom.Text;

import java.util.Calendar;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link BillFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BillFragment extends Fragment implements AdapterView.OnItemLongClickListener, AdapterView.OnItemClickListener {

    private LedgerDBHelper mDBHelper;
    private BillListAdapter adapter;
    private ListView lv_bill;
    private String mYearMonth;

    public static Fragment newInstance(int year, int month) {
        BillFragment fragment = new BillFragment();
        Bundle args = new Bundle();
        args.putInt("year", year);
        args.putInt("month", month);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_bill, container, false);
        lv_bill = view.findViewById(R.id.lv_bill);
        mDBHelper = LedgerDBHelper.getInstance(getContext());
        Bundle arguments = getArguments();
        int year = arguments.getInt("year");
        int month = arguments.getInt("month");
        String zeroMonth = month < 10 ? "0" + month : String.valueOf(month);
        mYearMonth = year + "-" + zeroMonth;

        List<BillInfo> billInfoList = mDBHelper.queryByMonth(mYearMonth);
        adapter = new BillListAdapter(getContext(), billInfoList);
        lv_bill.setAdapter(adapter);
        lv_bill.setOnItemLongClickListener(this);
        lv_bill.setOnItemClickListener(this);

        return view;
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("删除账单记录");
        builder.setMessage("是否确定删除该条账单记录？");
        builder.setPositiveButton("确定", (dialog, which) -> {
            if (mDBHelper.DeleteBillItem(id) > 0) {
                ToastUtil.show(getContext(),"删除成功！");

                List<BillInfo> billInfoList = mDBHelper.queryByMonth(mYearMonth);
                adapter = new BillListAdapter(getContext(), billInfoList);
                lv_bill.setAdapter(adapter);
                lv_bill.setOnItemLongClickListener(this);
            }


        });
        builder.setNegativeButton("取消", (dialog, which) -> {

        });
        AlertDialog dialog = builder.create();
        dialog.show();
        return true;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(view.getContext(), BillModifyActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        // 获取点击的bill item的数据并传给BillModifyActivity
        Bundle bundle = new Bundle();
        TextView tv_date = view.findViewById(R.id.tv_date);
        TextView tv_type = view.findViewById(R.id.tv_type);
        TextView tv_remark = view.findViewById(R.id.tv_remark);
        TextView tv_amount = view.findViewById(R.id.tv_amount);

        bundle.putLong("id", id);
        bundle.putString("date", tv_date.getText().toString());
        bundle.putString("type", tv_type.getText().toString());
        bundle.putString("remark", tv_remark.getText().toString());
        bundle.putString("amount", tv_amount.getText().toString());

        intent.putExtras(bundle);

        startActivity(intent);
    }
}