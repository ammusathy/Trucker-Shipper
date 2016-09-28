package com.trukr.shipper.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.trukr.shipper.R;
import com.trukr.shipper.fragment.NewJobScreen;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by nijamudhin on 6/21/2016.
 */
public class NewJobDetails extends AppCompatActivity {
    private TabLayout tabLayout;
    private ViewPager viewPager;
    ImageView image_back, notification;
    TextView txt_order, cancelled_order, cancelledjob;
    String orderId,orderstatus;
    private Typeface Gibson_Light, HnThin, HnLight, Gibson_regular;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.delivered);
        init();
        process();
    }

    private void init() {
        image_back = (ImageView) findViewById(R.id.delivered_image_back);
        viewPager = (ViewPager) findViewById(R.id.delivered_viewpager);
        tabLayout = (TabLayout) findViewById(R.id.delivered_tablayout);
        cancelled_order = (TextView) findViewById(R.id.delivered_txtorder);
        txt_order = (TextView) findViewById(R.id.delivered_txtorder_cancel);
        cancelledjob = (TextView) findViewById(R.id.delivered_txt_cancelledjobs);
        notification = (ImageView) findViewById(R.id.delivered_btn_bell);
        Gibson_Light = Typeface.createFromAsset(getAssets(), "Gibson_Light.otf");
        HnThin = Typeface.createFromAsset(getAssets(), "HelveticaNeue-Thin.otf");
        HnLight = Typeface.createFromAsset(getAssets(), "HelveticaNeue-Light.ttf");
        Gibson_regular = Typeface.createFromAsset(getAssets(), "Gibson-Regular.ttf");
        cancelled_order.setTypeface(HnLight);
        cancelledjob.setTypeface(HnLight);
        txt_order.setTypeface(HnLight);
        setupViewPager(viewPager);
        tabLayout.setupWithViewPager(viewPager);
        try {
            Intent intent = getIntent();
            orderId = intent.getStringExtra("OrderId");
            orderstatus = intent.getStringExtra("OrderStatus");
            System.out.println("orderid--->+" + orderId);
            System.out.println("orderstatus--->+" + orderstatus);
            cancelledjob.setText(orderstatus);
            cancelled_order.setText(orderId);
        } catch (Exception e) {
            e.printStackTrace();
        }



    }

    private void process() {

        image_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        notification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(NewJobDetails.this, Notification.class);
                startActivity(intent);
            }
        });


        SharedPreferences preferences = getSharedPreferences("pending", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("OrderId", orderId);
        editor.putString("OrderStatus", orderstatus);
        editor.commit();
    }



    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new NewJobScreen(), "Details");
        viewPager.setAdapter(adapter);
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(android.support.v4.app.FragmentManager manager) {
            super(manager);
        }

        @Override
        public android.support.v4.app.Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(android.support.v4.app.Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }
}



