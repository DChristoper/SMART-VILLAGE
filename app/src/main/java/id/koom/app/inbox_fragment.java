package id.koom.app;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import id.koom.app.R;

import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

public class inbox_fragment extends Fragment {
    private ViewPager view_pager;
    private SectionsPagerAdapter mSectionPageAdapter;
    BroadcastReceiver mMessageReceiver;
    Context ctx;
    String notif;

    public inbox_fragment(Context ctx, String notif){
        this.ctx = ctx;
        this.notif = notif;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View fragment = inflater.inflate(R.layout.fragment_pesan2, container, false);
        mSectionPageAdapter = new SectionsPagerAdapter(getChildFragmentManager());
        view_pager = fragment.findViewById(R.id.view_pager);
        setupViewPager(view_pager);
        TabLayout tabLayout = fragment.findViewById(R.id.tab_layout);
        tabLayout.setupWithViewPager(view_pager);

        return fragment;
    }

    @SuppressLint("ResourceType")
    private void setupViewPager(ViewPager viewPager) {
        SectionsPagerAdapter adapter = new SectionsPagerAdapter(getChildFragmentManager());
        adapter.addFragment(new pesan1(), "PESAN");
        adapter.addFragment(new fragmen_notifikasi(ctx), "NOTIFIKASI");
        viewPager.setAdapter(adapter);
        Log.d("setupViewPager", "Notif " + notif);
        Log.d("setupViewPager", "Count " + adapter.getCount());
        Log.d("setupViewPager", "List " + adapter.getPageTitle(1));

        if(notif != null){
            viewPager.setCurrentItem(1);
        }
//        inbox_fragment.SectionsPagerAdapter adapter = new inbox_fragment.SectionsPagerAdapter(getFragmentManager());
//        adapter.addFragment(inbox_fragment.PlaceholderFragment.newInstance(1), "NOTIFIKASI");
//        adapter.addFragment(inbox_fragment.PlaceholderFragment.newInstance(2), "PESAN");
//        viewPager.setAdapter(adapter);
    }

//
//    public static class PlaceholderFragment extends Fragment {
//        private static final String ARG_SECTION_NUMBER = "section_number";
//
//        public PlaceholderFragment() {
//        }
//
//        public static inbox_fragment.PlaceholderFragment newInstance(int sectionNumber) {
//            inbox_fragment.PlaceholderFragment fragment = new inbox_fragment.PlaceholderFragment();
//            Bundle args = new Bundle();
//            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
//            fragment.setArguments(args);
//            return fragment;
//        }
//
//        @Override
//        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//            View rootView = inflater.inflate(R.layout.fragmen_notifikasi, container, false);
//            return rootView;
//        }
//    }

    private class SectionsPagerAdapter extends FragmentPagerAdapter {

        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public SectionsPagerAdapter(FragmentManager manager) {

            super (manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);

        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            
            return mFragmentTitleList.get(position);
        }
    }
}



