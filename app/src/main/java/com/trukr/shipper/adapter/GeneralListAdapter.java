package com.trukr.shipper.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.trukr.shipper.R;
import com.trukr.shipper.application.TrukrApplication;
import com.trukr.shipper.model.GeneralParams.GeneralBean;

import java.util.List;

public class GeneralListAdapter extends BaseAdapter {
    private List<GeneralBean> mData;
    private LayoutInflater mInflater;

    public GeneralListAdapter(Context context, List<GeneralBean> data) {
        mInflater = LayoutInflater.from(context);
        mData = data;
    }

    public int getCount() {
        return mData.size();
    }

    public Object getItem(int position) {
        return mData.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        final Holderview vholder;
        if (convertView == null) {
            vholder = new Holderview();
            view = mInflater.inflate(R.layout.single_row_general_list, parent, false);
            vholder.Vaule = (TextView) view.findViewById(R.id.descr);
            view.setTag(vholder);
        } else {
            vholder = (Holderview) view.getTag();
        }
        String countryName = mData.get(position).getValue();
        vholder.Vaule.setText(countryName);
        TrukrApplication.setHelveticaNeue_Light(vholder.Vaule);
        return view;
    }

    class Holderview {
        TextView Vaule;
    }
}
