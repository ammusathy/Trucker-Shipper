package com.trukr.shipper.fragment;

/**
 * Created by kalaivani on 3/26/2016.
 */

import android.annotation.SuppressLint;
import android.app.DialogFragment;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.trukr.shipper.ActionCallBack;
import com.trukr.shipper.R;
import com.trukr.shipper.application.TrukrApplication;

import java.util.ArrayList;

public class MyDialogFragment extends DialogFragment implements
        OnItemClickListener {
    public ActionCallBack callBack;
    TextView txt_view;
    ListView mylist;
    ArrayList<String> arrayList;
    String mSourceString;

    public MyDialogFragment() {

    }

    @SuppressLint("ValidFragment")
    public MyDialogFragment(TextView txt_view) {
        this.txt_view = txt_view;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_fragment, null, false);
        mylist = (ListView) view.findViewById(R.id.list);
        arrayList = getArguments().getStringArrayList("mSource");
        mSourceString = getArguments().getString("value");
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_list_item_1, arrayList);
        mylist.setAdapter(adapter);
        mylist.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        dismiss();

        if (mSourceString.equalsIgnoreCase("Source")) {
            Log.i("SourceSelected", mSourceString);
            TrukrApplication.mSelectedSource = position;
            txt_view.setText(arrayList.get(position).toString());
        } else if (mSourceString.equalsIgnoreCase("Destination")) {
            Log.i("SourceSelected", mSourceString);
            TrukrApplication.mSelectedDestination = position;
        } else if (mSourceString.equalsIgnoreCase("TruckType")) {
            LocalBroadcastManager.getInstance(getActivity()).sendBroadcast(
                    new Intent("SOME_ACTION"));
        } else if (mSourceString.equalsIgnoreCase("AdditionalLabel")) {
            LocalBroadcastManager.getInstance(getActivity()).sendBroadcast(
                    new Intent("ADDLABEL"));
        } else if (mSourceString.equalsIgnoreCase("SubAdditionalLabel")) {
        }
        txt_view.setText(arrayList.get(position).toString());
        txt_view.setTextColor(Color.BLACK);
    }
}