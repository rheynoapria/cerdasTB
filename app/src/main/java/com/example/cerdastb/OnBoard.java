package com.example.cerdastb;

import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class OnBoard extends AppCompatActivity {

    ViewPager viewPager;
    SliderAdapter sliderAdapter;
    LinearLayout mDotsLayout;
    private TextView[] mDots;

    private Button mNextButton,mPrevButton;
    private int mCurrentPage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_on_board);

        mDotsLayout = (LinearLayout)findViewById(R.id.mDots);
        mNextButton = (Button)findViewById(R.id.next_slide);
        mPrevButton = (Button)findViewById(R.id.prev_slide);

        mPrevButton.setVisibility(View.INVISIBLE);

        viewPager = findViewById(R.id.viewpager);
        sliderAdapter = new SliderAdapter(this);
        viewPager.setAdapter(sliderAdapter);

        addDotsIndicator(0);

        viewPager.addOnPageChangeListener(viewListener);

        mNextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewPager.setCurrentItem(mCurrentPage+1);

            }
        });

        mPrevButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewPager.setCurrentItem(mCurrentPage-1);

            }
        });

    }

    public void addDotsIndicator(int position){
        mDots = new TextView[3];
        mDotsLayout.removeAllViews();

        for (int i=0; i < mDots.length ; i++){

            mDots[i] = new TextView(this);
            mDots[i].setText(Html.fromHtml("&#8226;"));
            mDots[i].setTextSize(40);
            mDots[i].setTextColor(getResources().getColor(R.color.Transparentwhite));

            mDotsLayout.addView(mDots[i]);


        }

        if (mDots.length>0){
            mDots[position].setTextColor(getResources().getColor(R.color.yellow));

        }
    }

    ViewPager.OnPageChangeListener viewListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int i, float v, int i1) {

        }

        @Override
        public void onPageSelected(int i) {

            addDotsIndicator(i);
            mCurrentPage = i;

            if (mCurrentPage == 0){
                mNextButton.setEnabled(true);
                mPrevButton.setEnabled(false);
                mPrevButton.setVisibility(View.INVISIBLE);
                mNextButton.setText("Lanjut");
            }
            else if ( mCurrentPage == mDots.length-1){
                    mNextButton.setEnabled(true);
                    mPrevButton.setEnabled(true);
                    mPrevButton.setVisibility(View.VISIBLE);
                    mPrevButton.setText("Kembali");
                    mNextButton.setText("Mulai");

                    mNextButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent formNama = new Intent(OnBoard.this,FormNama.class);
                            startActivity(formNama);
                            finish();
                        }
                    });
            }
            else{
                mNextButton.setEnabled(true);
                mPrevButton.setEnabled(true);
                mPrevButton.setVisibility(View.VISIBLE);
                mPrevButton.setText("Kembali");
                mNextButton.setText("Lanjut");
            }
        }

        @Override
        public void onPageScrollStateChanged(int i) {

        }
    };
}
