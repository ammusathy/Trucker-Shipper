package com.trukr.shipper.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.trukr.shipper.R;
import com.trukr.shipper.fragment.Home;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by nijamudhin on 6/17/2016.
 */
public class ViewDetails extends Activity {

    ImageView back, bellNotification;
    TextView source, destination, distanceValue, rpmDistance, rpmCount, rpmCost, subCharges, TotalPrice, pickupDate, pickupTime, deliverDate, deliverTime;
    TextView service_bordercrossing, value_bordercrossing, amount_bordercrossing, count_bordercrossing, divider1;
    TextView service_hazmat, value_hazmat, amount_hazmat, count_hazmat, divider2;
    TextView service_noofstraps, value_noofstraps, amount_noofstraps, count_noofstraps, divider3;
    TextView service_redelivery, value_redelivery, amount_redelivery, count_redelivery, divider4;
    TextView service_stopsintransit, value_stopsintransit, amount_stopsintransit, count_stopsintransit, divider5;
    TextView service_teamservice, value_teamservice, amount_teamservice, count_teamservice, divider6;
    TextView service_toll, value_toll, amount_toll, count_toll, divider14;
    TextView service_weight, value_weight, count_weight, amount_weight, divider15;
    TextView service_yard, amount_yard, count_yard, value_yard, divider16;
    TextView service_reweighning, amount_reweighning, value_reweigning, count_reweighning, divider17;
    TextView service_lumper, amount_lumper, value_lumper, count_lumper, divider18;
    TextView service_power, count_power, value_power, amount_power, divider19;
    TextView service_lay, count_lay, amount_lay, value_lay, divider20;
    String from, to, rpm, surcharge, amount, distance, rpmMin, pickupDateValue, pickupTimeValue, deliveryDateValue, deliveryTimeValue;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_details);
        init();
        process();
    }

    private void init() {
        source = (TextView) findViewById(R.id.et_source);
        destination = (TextView) findViewById(R.id.et_destination);
        distanceValue = (TextView) findViewById(R.id.textTotalDistanceValue);
        rpmDistance = (TextView) findViewById(R.id.textRPMDistance);
        rpmCount = (TextView) findViewById(R.id.textRPMCharges);
        rpmCost = (TextView) findViewById(R.id.textRPMCost);
        subCharges = (TextView) findViewById(R.id.textSubchargesValue);
        TotalPrice = (TextView) findViewById(R.id.texttotalprice);
        pickupDate = (TextView) findViewById(R.id.PickupDate);
        pickupTime = (TextView) findViewById(R.id.PickupTime);
        deliverDate = (TextView) findViewById(R.id.deliveryDate);
        deliverTime = (TextView) findViewById(R.id.deliveryTime);
        back = (ImageView) findViewById(R.id.back);

        service_bordercrossing = (TextView) findViewById(R.id.viewdetails_btn_borderCrossingfee);
        value_bordercrossing = (TextView) findViewById(R.id.txtviewdetails_borderCrossingfee);
        amount_bordercrossing = (TextView) findViewById(R.id.txt_amount_borderCrossingfee);
        count_bordercrossing = (TextView) findViewById(R.id.txtviewdetails_borderCrossingfee_count);
        divider1 = (TextView) findViewById(R.id.viewdetails_txt_divider1);

        service_hazmat = (TextView) findViewById(R.id.viewdetails_btn_hazmat);
        value_hazmat = (TextView) findViewById(R.id.txtviewdetails_hazmat);
        amount_hazmat = (TextView) findViewById(R.id.txt_amount_hazmat);
        count_hazmat = (TextView) findViewById(R.id.txtviewdetails_hazmat_count);
        divider2 = (TextView) findViewById(R.id.viewdetails_txt_divider2);

        service_noofstraps = (TextView) findViewById(R.id.viewdetails_btn_noofstraps);
        value_noofstraps = (TextView) findViewById(R.id.txtviewdetails_noofstraps);
        amount_noofstraps = (TextView) findViewById(R.id.txt_amount_noofstraps);
        count_noofstraps = (TextView) findViewById(R.id.txtviewdetails_noofstraps_count);
        divider3 = (TextView) findViewById(R.id.viewdetails_txt_divider3);

        service_redelivery = (TextView) findViewById(R.id.viewdetails_btn_redelivery);
        value_redelivery = (TextView) findViewById(R.id.txtviewdetails_redelivery);
        amount_redelivery = (TextView) findViewById(R.id.txt_amount_redelivery);
        count_redelivery = (TextView) findViewById(R.id.txtviewdetails_redelivery_count);
        divider4 = (TextView) findViewById(R.id.viewdetails_txt_divider4);

        service_stopsintransit = (TextView) findViewById(R.id.viewdetails_btn_stops);
        value_stopsintransit = (TextView) findViewById(R.id.txtviewdetails_stops);
        amount_stopsintransit = (TextView) findViewById(R.id.txt_amount_stops);
        count_stopsintransit = (TextView) findViewById(R.id.txtviewdetails_stops_count);
        divider5 = (TextView) findViewById(R.id.viewdetails_txt_divider5);

        service_teamservice = (TextView) findViewById(R.id.viewdetails_btn_teamservice);
        value_teamservice = (TextView) findViewById(R.id.txtviewdetails_teamservice);
        amount_teamservice = (TextView) findViewById(R.id.txt_amount_teamservice);
        count_teamservice = (TextView) findViewById(R.id.txtviewdetails_teamservice_count);
        divider6 = (TextView) findViewById(R.id.viewdetails_txt_divider6);

        service_toll = (TextView) findViewById(R.id.viewdetails_btn_tollcharge);
        value_toll = (TextView) findViewById(R.id.txtviewdetails_tollcharge);
        amount_toll = (TextView) findViewById(R.id.txt_amount);
        count_toll = (TextView) findViewById(R.id.txtviewdetails_tollcharge_count);
        divider14 = (TextView) findViewById(R.id.viewdetails_txt_divider14);

        service_weight = (TextView) findViewById(R.id.viewdetails_btn_WeightFine);
        value_weight = (TextView) findViewById(R.id.txtviewdetails_WeightFine);
        count_weight = (TextView) findViewById(R.id.txtviewdetails_WeightFine_id);
        amount_weight = (TextView) findViewById(R.id.txt_amount1);
        divider15 = (TextView) findViewById(R.id.viewdetails_txt_divider15);

        service_yard = (TextView) findViewById(R.id.viewdetails_btn_YardStorage);
        amount_yard = (TextView) findViewById(R.id.txt_amount2);
        count_yard = (TextView) findViewById(R.id.txtviewdetails_YardStorage_id);
        value_yard = (TextView) findViewById(R.id.txtviewdetails_YardStorage);
        divider16 = (TextView) findViewById(R.id.viewdetails_txt_divider16);

        service_reweighning = (TextView) findViewById(R.id.viewdetails_btn_Reweighing);
        value_reweigning = (TextView) findViewById(R.id.txtviewdetails_Reweighing);
        count_reweighning = (TextView) findViewById(R.id.txtviewdetails_Reweighing_id);
        amount_reweighning = (TextView) findViewById(R.id.txt_amount3);
        divider17 = (TextView) findViewById(R.id.viewdetails_txt_divider17);

        service_lumper = (TextView) findViewById(R.id.viewdetails_btn_LumperCharge);
        amount_lumper = (TextView) findViewById(R.id.txt_amount4);
        count_lumper = (TextView) findViewById(R.id.txtviewdetails_LumperCharge_id);
        value_lumper = (TextView) findViewById(R.id.txtviewdetails_LumperCharge);
        divider18 = (TextView) findViewById(R.id.viewdetails_txt_divider18);


        service_power = (TextView) findViewById(R.id.viewdetails_btn_PowerUsageHours);
        amount_power = (TextView) findViewById(R.id.txt_amount5);
        count_power = (TextView) findViewById(R.id.txtviewdetails_PowerUsageHours_id);
        value_power = (TextView) findViewById(R.id.txtviewdetails_PowerUsageHours);
        divider19 = (TextView) findViewById(R.id.viewdetails_txt_divider19);

        service_lay = (TextView) findViewById(R.id.viewdetails_btn_LayOver);
        amount_lay = (TextView) findViewById(R.id.txt_amount6);
        value_lay = (TextView) findViewById(R.id.txtviewdetails_LayOver);
        count_lay = (TextView) findViewById(R.id.txtviewdetails_LayOver_id);
        divider20 = (TextView) findViewById(R.id.viewdetails_txt_divider20);

        bellNotification = (ImageView) findViewById(R.id.notification);
    }

    private void process() {

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        bellNotification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ViewDetails.this, Notification.class);
                startActivity(intent);
            }
        });

        String jsonResponseFromHome;
        try {
            Bundle b = getIntent().getExtras();
            JSONObject jsonObject = null;
            if (b != null) {
                jsonResponseFromHome = (String) b.get("jsonResponse");

                jsonObject = new JSONObject(jsonResponseFromHome);
                System.out.println("ViewDetails : jsonResponseFromHome = " + jsonObject);
                from = jsonObject.getString("FromAddress");
                to = jsonObject.getString("ToAddress");
                rpm = jsonObject.getString("RpmCharge");
                rpmMin = jsonObject.getString("RpmMinimumCharge");
                surcharge = jsonObject.getString("SurCharge");
                distance = jsonObject.getString("CalculateDistance");
                amount = jsonObject.getString("CalculateAmount");
                pickupDateValue = jsonObject.getString("PickupDate");
                pickupTimeValue = jsonObject.getString("PickupTime");
                deliveryDateValue = jsonObject.getString("DeliveryDate");
                deliveryTimeValue = jsonObject.getString("DeliveryTime");
            }


            source.setText(from);
            destination.setText(to);
            distanceValue.setText(distance);
            rpmDistance.setText(distance);
            rpmCount.setText("$" + surcharge);
            pickupDate.setText(pickupDateValue);
            pickupTime.setText(pickupTimeValue);
            deliverDate.setText(deliveryDateValue);
            deliverTime.setText(deliveryTimeValue);

            JSONArray shipment = null;
            try {
                shipment = jsonObject.getJSONArray("Accessories");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            System.out.println("Home class : GetDistanceAmountAsync : shipment = " + shipment);

            System.out.println("service length-->" + shipment.length());
            for (int i = 0; i < shipment.length(); i++) {
                JSONObject json = shipment.getJSONObject(i);
                String servicetoll = json.getString(("serviceName"));
                {
                    if (json.getString("serviceName").equals("Border Crossing fee")) {
                        String count1 = json.getString("count");
                        String toll_amt = json.getString("amount");
                        String value = json.getString("value");
                        service_bordercrossing.setVisibility(View.VISIBLE);
                        value_bordercrossing.setVisibility(View.VISIBLE);
                        amount_bordercrossing.setVisibility(View.VISIBLE);
                        service_bordercrossing.setText(servicetoll);
                        value_bordercrossing.setText(value);
                        amount_bordercrossing.setText("$" + toll_amt);
                    }

                    if (json.getString("serviceName").equals("Hazmat charge")) {
                        String count1 = json.getString("count");
                        String toll_amt = json.getString("amount");
                        String value = json.getString("value");
                        service_hazmat.setVisibility(View.VISIBLE);
                        value_hazmat.setVisibility(View.VISIBLE);
                        amount_hazmat.setVisibility(View.VISIBLE);
                        service_hazmat.setText(servicetoll);
                        value_hazmat.setText("Yes");
                        amount_hazmat.setText("$" + toll_amt);
                    }

                    if (json.getString("serviceName").equals("No Of Straps")) {
                        String count1 = json.getString("count");
                        String toll_amt = json.getString("amount");
                        String value = json.getString("value");
                        service_noofstraps.setVisibility(View.VISIBLE);
                        count_noofstraps.setVisibility(View.VISIBLE);
                        amount_noofstraps.setVisibility(View.VISIBLE);
                        service_noofstraps.setText(servicetoll);
                        count_noofstraps.setText(count1);
                        amount_noofstraps.setText("$" + toll_amt);
                    }

                    if (json.getString("serviceName").equals("Redelivery Charge")) {
                        String count1 = json.getString("count");
                        String toll_amt = json.getString("amount");
                        String value = json.getString("value");
                        service_redelivery.setVisibility(View.VISIBLE);
                        value_redelivery.setVisibility(View.VISIBLE);
                        amount_redelivery.setVisibility(View.VISIBLE);
                        service_redelivery.setText(servicetoll);
                        value_redelivery.setText("Yes");
                        amount_redelivery.setText("$" + toll_amt);
                    }

                    if (json.getString("serviceName").equals("Stops-in-transit")) {
                        String count1 = json.getString("count");
                        String toll_amt = json.getString("amount");
                        String value = json.getString("value");
                        service_stopsintransit.setVisibility(View.VISIBLE);
                        count_stopsintransit.setVisibility(View.VISIBLE);
                        amount_stopsintransit.setVisibility(View.VISIBLE);
                        service_stopsintransit.setText(servicetoll);
                        value_stopsintransit.setText(count1);
                        count_stopsintransit.setText(count1);
                        amount_stopsintransit.setText("$" + toll_amt);
                    }

                    if (json.getString("serviceName").equals("Team Service")) {
                        String count1 = json.getString("count");
                        String toll_amt = json.getString("amount");
                        String value = json.getString("value");
                        service_teamservice.setVisibility(View.VISIBLE);
                        value_teamservice.setVisibility(View.VISIBLE);
                        amount_teamservice.setVisibility(View.VISIBLE);
                        service_teamservice.setText(servicetoll);
                        value_teamservice.setText("Yes");
                        amount_teamservice.setText("$" + toll_amt);
                    }

                    if (json.getString("serviceName").equals("Toll Charge")) {
                        String count1 = json.getString("count");
                        String toll_amt = json.getString("amount");
                        String value = json.getString("value");
                        service_toll.setVisibility(View.VISIBLE);
                        count_toll.setVisibility(View.VISIBLE);
                        amount_toll.setVisibility(View.VISIBLE);
                        service_toll.setText(servicetoll);
                        value_toll.setText(count1);
                        count_toll.setText(count1);
                        amount_toll.setText("$" + toll_amt);
                    }
                    if (json.getString("serviceName").equals("Weight Fine")) {
                        String count2 = json.getString("count");
                        String weight_amt = json.getString("amount");
                        String value1 = json.getString("value");
                        service_weight.setVisibility(View.VISIBLE);
                        amount_weight.setVisibility(View.VISIBLE);
                        count_weight.setVisibility(View.VISIBLE);
                        service_weight.setText(servicetoll);
                        amount_weight.setText("$" + weight_amt);
                        count_weight.setText(count2);
                        value_weight.setText(count2);
                    }
                    if (json.getString("serviceName").equals("Yard Storage")) {
                        String count3 = json.getString("count");
                        String yard_amt = json.getString("amount");
                        String value2 = json.getString("value");
                        service_yard.setVisibility(View.VISIBLE);
                        amount_yard.setVisibility(View.VISIBLE);
                        count_yard.setVisibility(View.VISIBLE);
                        // value_yard.setVisibility(View.VISIBLE);
                        service_yard.setText(servicetoll);
                        amount_yard.setText("$" + yard_amt);
                        count_yard.setText(count3);
                        value_yard.setText(count3);
                    }
                    if (json.getString("serviceName").equals("Reweighing")) {
                        String count4 = json.getString("count");
                        String reweigning_amt = json.getString("amount");
                        String value3 = json.getString("value");
                        service_reweighning.setVisibility(View.VISIBLE);
                        amount_reweighning.setVisibility(View.VISIBLE);
                        count_reweighning.setVisibility(View.VISIBLE);
                        // value_reweigning.setVisibility(View.VISIBLE);
                        service_reweighning.setText(servicetoll);
                        amount_reweighning.setText("$" + reweigning_amt);
                        count_reweighning.setText(count4);
                        value_reweigning.setText(count4);
                    }

                    if (json.getString("serviceName").equals("Lumper Charge")) {
                        String count5 = json.getString("count");
                        String lumper_amt = json.getString("amount");
                        String value4 = json.getString("value");
                        service_lumper.setVisibility(View.VISIBLE);
                        amount_lumper.setVisibility(View.VISIBLE);
                        count_lumper.setVisibility(View.VISIBLE);
                        // value_lumper.setVisibility(View.VISIBLE);
                        service_lumper.setText(servicetoll);
                        amount_lumper.setText("$" + lumper_amt);
                        count_lumper.setText(count5);
                        value_lumper.setText(count5);
                    }

                    if (json.getString("serviceName").equals("PowerUsage Hours")) {
                        String count6 = json.getString("count");
                        String power_amt = json.getString("amount");
                        String value5 = json.getString("value");
                        service_power.setVisibility(View.VISIBLE);
                        amount_power.setVisibility(View.VISIBLE);
                        count_power.setVisibility(View.VISIBLE);
                        // value_power.setVisibility(View.VISIBLE);
                        service_power.setText(servicetoll);
                        amount_power.setText("$" + power_amt);
                        count_power.setText(count6);
                        value_power.setText(count6);
                    }

                    if (json.getString("serviceName").equals("Layover")) {
                        String count7 = json.getString("count");
                        String weekday_amt = json.getString("amount");
                        String value6 = json.getString("value");
                        service_lay.setVisibility(View.VISIBLE);
                        amount_lay.setVisibility(View.VISIBLE);
                        count_lay.setVisibility(View.VISIBLE);
                        value_lay.setVisibility(View.VISIBLE);
                        service_lay.setText(servicetoll);
                        amount_lay.setText("$" + weekday_amt);
                        count_lay.setText(value6);
                        value_lay.setText(value6);
                    }
                }
            }
            try {
                float total = Float.parseFloat(amount);
                TotalPrice.setText("$" + Float.toString(total));
                System.out.println("amount--->" + total + amount);
                float value = Float.parseFloat(distance) * Float.parseFloat(surcharge);
                float rpmCharge = Float.parseFloat(distance) * Float.parseFloat(rpm);
                float rpmMinCharge = Float.parseFloat(rpmMin);
                System.out.println("rpm charge-->" + rpmCharge);
                if (rpmCharge > rpmMinCharge) {
                    subCharges.setText("$" + rpmCharge);
                } else {
                    subCharges.setText("$" + rpmMin);
                }
                rpmCost.setText("$" + Float.toString(value));
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}