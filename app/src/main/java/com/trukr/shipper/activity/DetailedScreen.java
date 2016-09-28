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
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.trukr.shipper.R;
import com.trukr.shipper.fragment.DeliverDetailsFragment;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by nijamudhin on 6/6/2016.
 */
public class DetailedScreen extends AppCompatActivity {
    private TabLayout tabLayout;
    private ViewPager viewPager;
    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    ImageView image_back, notification;
    TextView delivered, number, txt_order;
    String orderId, orderStatus;
    Typeface Gibson_Light, HnBold, HnThin, HnLight, Gibson_Regular, GillSansStd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.delivered_job);
        init();
        process();
    }

    private void init() {
        try {
            Intent intent = getIntent();
            orderId = intent.getStringExtra("OrderId");
            orderStatus = intent.getStringExtra("Status");
        } catch (Exception e) {
            e.printStackTrace();
        }
        preferences = getSharedPreferences("NewJobDetails", MODE_PRIVATE);
        editor = preferences.edit();
        editor.putString("OrderId", orderId);
        editor.putString("OrderStatus", orderStatus);
        editor.commit();
        image_back = (ImageView) findViewById(R.id.deliverd_image_back);
        delivered = (TextView) findViewById(R.id.delivered_txt_deliveredjobs);
        txt_order = (TextView) findViewById(R.id.delivered_txtorder_cancel);
        number = (TextView) findViewById(R.id.delivered_ordernumber);
        notification = (ImageView) findViewById(R.id.delivered_btn_bell);

        Gibson_Light = Typeface.createFromAsset(getAssets(), "Gibson_Light.otf");
        HnThin = Typeface.createFromAsset(getAssets(), "HelveticaNeue-Thin.otf");
        HnLight = Typeface.createFromAsset(getAssets(), "HelveticaNeue-Light.ttf");
        HnBold = Typeface.createFromAsset(getAssets(), "HelveticaNeue-Bold.ttf");
        Gibson_Regular = Typeface.createFromAsset(getAssets(), "Gibson-Regular.ttf");

        delivered.setTypeface(HnLight);
        txt_order.setTypeface(HnLight);
        number.setTypeface(HnLight);
        number.setText(orderId);
        delivered.setText(orderStatus);

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);
        tabLayout = (TabLayout) findViewById(R.id.delivered_tablayout);
        tabLayout.setupWithViewPager(viewPager);
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
                Intent intent = new Intent(DetailedScreen.this, Notification.class);
                startActivity(intent);
            }
        });
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new DeliverDetailsFragment(), "Details");
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


