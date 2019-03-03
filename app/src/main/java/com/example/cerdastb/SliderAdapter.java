package com.example.cerdastb;

import android.content.Context;
import android.media.Image;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.w3c.dom.Text;

public class SliderAdapter extends PagerAdapter {

    Context context;
    LayoutInflater layoutInflater;
    public int[] imageArray = {R.drawable.ic_medicine,R.drawable.ic_doctor,R.drawable.ic_exam};
    public int[] backgroundArray = {R.drawable.bg_slide1,R.drawable.bg_slide2,R.drawable.bg_slide1};
    public String[] titleArray = {
            "Pengingat Minum Obat",
            "Jadwal Bertemu Dokter",
            "Tes Kemampuan Anda"

    };



    public SliderAdapter (Context context){
        this.context = context;

    }
    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return (view == object );
    }

    @Override
    public int getCount() {
        return titleArray.length;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((LinearLayout)object);
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {

        String[] descTitle = context.getResources().getStringArray(R.array.slide_text);

        layoutInflater = (LayoutInflater)context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.slider,container,false);

        LinearLayout linearLayout = (LinearLayout)view.findViewById(R.id.slider);
        ImageView imageView = (ImageView)view.findViewById(R.id.icon_slide);
        TextView title_slide = (TextView)view.findViewById(R.id.title_slide);
        TextView desc_slide = (TextView)view.findViewById(R.id.desc_slide);

        linearLayout.setBackgroundResource(backgroundArray[position]);
        imageView.setImageResource(imageArray[position]);
        title_slide.setText(titleArray[position]);
        desc_slide.setText(descTitle[position]);
        container.addView(view);
        return view;
    }


}
