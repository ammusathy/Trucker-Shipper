package com.trukr.shipper.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.trukr.shipper.R;
import com.trukr.shipper.model.GeneralParams.NotificationModel;

import java.util.List;

/**
 * Created by nijamudhin on 6/28/2016.
 */
public class NotificationListAdapter extends RecyclerView.Adapter<NotificationListAdapter.ListHolderView> {

    private static OnItemClickListener listener;
    Context context;
    private List<NotificationModel> notificationModels;

    public NotificationListAdapter(Context context, List<NotificationModel> notificationModels) {
        this.context = context;
        this.notificationModels = notificationModels;
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    @Override
    public ListHolderView onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.notification_list, parent, false);
        return new ListHolderView(itemView);
    }

    @Override
    public void onBindViewHolder(ListHolderView holder, int position) {
        try {

            NotificationModel response = notificationModels.get(position);
            holder.date.setText(response.getDate());
            holder.msg.setText(response.getMessage());
        } catch (Exception e) {
            System.out.println("Exception : " + e.getMessage());
        }
    }

    @Override
    public int getItemCount() {
        System.out.println("listsize" + notificationModels.size());
        return notificationModels.size();
    }

    // Define the listener interface
    public interface OnItemClickListener {
        void onItemClick(View itemView, int position);
    }

    public static class ListHolderView extends RecyclerView.ViewHolder {
        TextView msg, date;
        Typeface Gibson_Light, HnThin, HnLight,HnRegular;

        public ListHolderView(View view) {
            super(view);
            msg = (TextView) view.findViewById(R.id.notification_msg);
            date = (TextView) view.findViewById(R.id.notification_date);


            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null)
                        listener.onItemClick(itemView, getAdapterPosition());
                }
            });
        }
    }


}



