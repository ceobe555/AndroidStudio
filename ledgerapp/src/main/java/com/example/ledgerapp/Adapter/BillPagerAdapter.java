package com.example.ledgerapp.Adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.ledgerapp.Fragment.BillFragment;

public class BillPagerAdapter extends FragmentPagerAdapter {
    private final int mYear;
    public BillPagerAdapter(@NonNull FragmentManager fm, int year) {
        super(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        this.mYear = year;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        int month = position + 1;
        String zeroMonth = month < 10 ? "0" + month : String.valueOf(month);
        String yearMonth = mYear + "-" + zeroMonth;
        return BillFragment.newInstance(yearMonth);
    }

    @Override
    public int getCount() {
        return 12;
    }

    public CharSequence getPageTitle(int position) {
        return (position + 1) + "月份";
    }
}
