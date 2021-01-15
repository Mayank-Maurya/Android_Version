package com.e.android_version;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.viewpager.widget.PagerAdapter;

import org.w3c.dom.Text;

public class Slider_Adapter extends PagerAdapter {

    Context context;
    LayoutInflater layoutInflater;

    public Slider_Adapter(Context context)
    {
        this.context=context;
    }

    public int[] colors ={
            R.color.black,
            R.color.background,
            R.color.colorPrimary
    };
    public String[] text={
            "",
            "Community Discussion is the QnA Application\n which solves you queries.",
            ""
    };

    @Override
    public int getCount() {
        return text.length;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view==object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {

        layoutInflater= (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View view=layoutInflater.inflate(R.layout.slider_layout,container,false);

        TextView textView=view.findViewById(R.id.oboarding_tv);
        textView.setText(text[position]);
        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((ConstraintLayout) object);
    }
}
