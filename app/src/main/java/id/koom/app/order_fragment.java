package id.koom.app;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import id.koom.app.R;

import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

public class order_fragment extends Fragment {
    private ViewPager view_pager;
    private SectionsPagerAdapter mSectionPageAdapter;
//    private ActionBar actionBar;
    private TabLayout tab_layout;
//    private TabLayout navgasi;
//    private NestedScrollView nested_scroll_view;
    Button bayarSekarang;


    public order_fragment(){

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View fragment = inflater.inflate(R.layout.activity_order, container, false);
        mSectionPageAdapter = new SectionsPagerAdapter(getFragmentManager());
        view_pager = fragment.findViewById(R.id.view_pager);
        setupViewPager(view_pager);
        TabLayout tabLayout = fragment.findViewById(R.id.tab_layout);
        tabLayout.setupWithViewPager(view_pager);
        return fragment;

    }

    @SuppressLint("ResourceType")
    private void setupViewPager(ViewPager viewPager) {
        SectionsPagerAdapter adapter = new SectionsPagerAdapter(getChildFragmentManager());
        adapter.addFragment(new fragment_tabs_basic(), "CICILAN");
        adapter.addFragment(new Riwayat_fragment(), "RIWAYAT");
        viewPager.setAdapter(adapter);
    }


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



