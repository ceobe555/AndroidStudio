package com.example.ledgerapp.Fragment;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.ledgerapp.Adapter.BillListAdapter;
import com.example.ledgerapp.Adapter.BillPagerAdapter;
import com.example.ledgerapp.Database.LedgerDBHelper;
import com.example.ledgerapp.Entity.BillInfo;
import com.example.ledgerapp.R;
import com.example.ledgerapp.Util.ToastUtil;

import java.util.Calendar;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link BillFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BillFragment extends Fragment implements AdapterView.OnItemLongClickListener {

    private LedgerDBHelper mDBHelper;
    private OnBillFragmentListener mListener;

    public static Fragment newInstance(String yearMonth) {
        BillFragment fragment = new BillFragment();
        Bundle args = new Bundle();
        args.putString("yearMonth", yearMonth);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_bill, container, false);
        ListView lv_bill = view.findViewById(R.id.lv_bill);
        mDBHelper = LedgerDBHelper.getInstance(getContext());
        Bundle arguments = getArguments();
        String yearMonth = arguments.getString("yearMonth");

        List<BillInfo> billInfoList = mDBHelper.queryByMonth(yearMonth);
        BillListAdapter adapter = new BillListAdapter(getContext(), billInfoList);
        lv_bill.setAdapter(adapter);

        lv_bill.setOnItemLongClickListener(this);

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
                mListener.initViewPager();
            }


        });
        builder.setNegativeButton("取消", (dialog, which) -> {

        });
        AlertDialog dialog = builder.create();
        dialog.show();
        return true;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof OnBillFragmentListener) {
            mListener = (OnBillFragmentListener) context;
        }
        else {
            throw new RuntimeException(context.toString()
                + "must implements OnBillFragmentListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnBillFragmentListener {
        void initViewPager();
    }
}