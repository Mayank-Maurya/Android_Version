package com.e.android_version;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.w3c.dom.Text;

public class Onboarding extends AppCompatActivity {
    private ViewPager viewPager;
    private LinearLayout dotlayout;
    private TextView[] dots;
    private Slider_Adapter slider_adapter;
    private TextView tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_onboarding);
        viewPager=findViewById(R.id.oboarding_view_pager);
        dotlayout=findViewById(R.id.oboarding_ll);
        slider_adapter=new Slider_Adapter(this);
        viewPager.setAdapter(slider_adapter);
        tv=findViewById(R.id.oboarding_skip_tv);
        tv.setOnClickListener(view -> {
            startActivity(new Intent(Onboarding.this,LoginActivity.class));
            finish();
        });

        adddots(0);
        viewPager.addOnPageChangeListener(viewpagerlistener);
    }
    public void adddots(int pos)
    {
        dots=new TextView[3];
        dotlayout.removeAllViews();
        for(int i=0;i<dots.length;i++)
        {
            dots[i]=new TextView(this);
            dots[i].setText(Html.fromHtml("&#8226"));
            dots[i].setTextSize(35);
            dots[i].setTextColor(getResources().getColor(R.color.navy_blue));
            dotlayout.addView(dots[i]);

        }
        if(dots.length>0)
        {
            dots[pos].setTextColor(getResources().getColor(R.color.white));
        }
    }

    ViewPager.OnPageChangeListener viewpagerlistener=new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            adddots(position);

        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };


}