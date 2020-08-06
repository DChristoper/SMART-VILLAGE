package id.koom.app;

import android.content.Context;
import androidx.viewpager.widget.PagerAdapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import id.koom.app.R;

public class SlideLayout extends PagerAdapter {

    Context context;
    LayoutInflater layoutInflater;

    public SlideLayout(Context context) {
        this.context = context;
    }

    public int[] slide_images = {

            R.drawable.logo_t,
            R.drawable.logo_aman,
            R.drawable.logo_mudah
    };

    public String[] slide_headings = {
            "SELAMAT DATANG!",
            "AMAN",
            "MUDAN DAN CEPAT"
    };

    public  String[] slide_descs = {
            "Dapatkan berbagai kemudahan talangan belanja dan tingkatkan usaha anda",
            "Telah memiliki izin usaha dan diawasi oleh OJK",
            "Syarat pendaftarannya mudah dan cepat diproses"
    };

    //Arrays
    @Override
    public int getCount()
    {
        return slide_headings.length;
    }

    @Override
    public boolean isViewFromObject(View view, Object o)
    {
        return view == (RelativeLayout) o;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        layoutInflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.slide_layout, container, false);

        ImageView slideImageView = (ImageView) view.findViewById(R.id.slide_image);
        TextView slideHeading = (TextView) view.findViewById(R.id.slide_heading);
        TextView slideDescription = (TextView) view.findViewById(R.id.slide_desc);

        slideImageView.setImageResource(slide_images[position]);
        slideHeading.setText(slide_headings[position]);
        slideDescription.setText(slide_descs[position]);

        container.addView(view);

        return view;
    }

    @Override
    public void destroyItem( ViewGroup container, int position,  Object object) {

        container.removeView((RelativeLayout)object);
    }
}
