package com.trukr.shipper.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.trukr.shipper.R;
import com.trukr.shipper.fragment.CurrentJobDetails;
import com.trukr.shipper.fragment.CurrentJobDirections;
import com.trukr.shipper.fragment.CurrentJobMessage;
import com.trukr.shipper.fragment.RequestStatus;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by nijamudhin on 5/25/2016.
 */
public class CurrentJobTabLayout extends AppCompatActivity {
    TextView orderStatus,txt_transit,orderodi;
    ImageView image_back, button, notification;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    String orderId;
    private Typeface Gibson_Light, HnThin, HnLight, Gibson_regular;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.delivered_screen);
        init();
        process();
    }

    private void init() {
        orderStatus = (TextView) findViewById(R.id.delivered_orderid);
        image_back = (ImageView) findViewById(R.id.current_image_back);
        notification = (ImageView) findViewById(R.id.current_image_bell);
        txt_transit = (TextView) findViewById(R.id.current_txt_transit);
        orderodi = (TextView) findViewById(R.id.delivered_txtorder);
        viewPager = (ViewPager) findViewById(R.id.view_pager);
        setupViewPager(viewPager);
        tabLayout = (TabLayout) findViewById(R.id.current_tab_layout);
        tabLayout.setupWithViewPager(viewPager);
        Gibson_Light = Typeface.createFromAsset(getAssets(), "Gibson_Light.otf");
        HnThin = Typeface.createFromAsset(getAssets(), "HelveticaNeue-Thin.otf");
        HnLight = Typeface.createFromAsset(getAssets(), "HelveticaNeue-Light.ttf");
        Gibson_regular = Typeface.createFromAsset(getAssets(), "Gibson-Regular.ttf");
        txt_transit.setTypeface(HnLight);
        orderodi.setTypeface(HnLight);
        orderStatus.setTypeface(HnLight);
    }

    public void process() {
        // Get the value of orderId from intent get
        try {
            Intent iin = getIntent();
            Bundle b = iin.getExtras();
            if (b != null) {
                orderId = (String) b.get("OrderId");
                orderStatus.setText(orderId);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        txt_transit.setText(RequestStatus.orderStatus);
        SharedPreferences preferences = getSharedPreferences("CurrentJobTabLayout", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("Order", orderId);
        editor.commit();


        notification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CurrentJobTabLayout.this, Notification.class);
                startActivity(intent);
            }
        });

        image_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new CurrentJobDetails(), "Details");
        adapter.addFragment(new CurrentJobMessage(), "Message");
        adapter.addFragment(new CurrentJobDirections(), "Directions");
        viewPager.setAdapter(adapter);
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
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


