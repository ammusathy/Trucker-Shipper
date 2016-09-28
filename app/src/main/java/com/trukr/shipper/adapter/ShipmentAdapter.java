package com.trukr.shipper.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.trukr.shipper.R;
import com.trukr.shipper.model.ResponseParams.AllShipmentResponse;

import java.util.List;


public class ShipmentAdapter extends RecyclerView.Adapter<ShipmentAdapter.ShipmentHolderView> {

    private static OnItemClickListener listener;
    Context ctx;
    private List<AllShipmentResponse> allShipment;

    public ShipmentAdapter(Context context, List<AllShipmentResponse> allShipment1) {
        this.ctx = context;
        this.allShipment = allShipment1;
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    @Override
    public ShipmentHolderView onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.shipment_row, parent, false);
        return new ShipmentHolderView(itemView);
    }

    @Override
    public void onBindViewHolder(ShipmentHolderView holder, int position) {
        try {
            AllShipmentResponse response = allShipment.get(position);
            System.out.println("Position123" + allShipment.get(position).getOrderStatus());
            if (allShipment.get(position).getOrderStatus().equals("Pending")) {
                Log.d("Test", "Test");
                holder.orderId.setText(response.getOrderId());
                holder.tv_status.setText(response.getOrderStatus());
                holder.tv_status.setTextColor(ctx.getResources().getColor(R.color.red));
                holder.tv_date.setText(response.getOrderDate());
            }
            if (allShipment.get(position).getOrderStatus().equals("Delivered")) {
                Log.d("Test", "Test");
                holder.orderId.setText(response.getOrderId());
                holder.tv_status.setText(response.getOrderStatus());
                holder.tv_status.setTextColor(ctx.getResources().getColor(R.color.green));
                holder.tv_date.setText(response.getOrderDate());
            }
            if (allShipment.get(position).getOrderStatus().equals("Cancelled")) {
                Log.d("Test", "Test");
                holder.orderId.setText(response.getOrderId());
                holder.tv_status.setText(response.getOrderStatus());
                holder.tv_status.setTextColor(ctx.getResources().getColor(R.color.black));
                holder.tv_date.setText(response.getOrderDate());
            }
            if (allShipment.get(position).getOrderStatus().equals("In Transit")) {
                Log.d("Test", "Test");
                holder.orderId.setText(response.getOrderId());
                holder.tv_status.setText(response.getOrderStatus());
                holder.tv_status.setTextColor(ctx.getResources().getColor(R.color.yellow));
                holder.tv_date.setText(response.getOrderDate());
            }
            if (allShipment.get(position).getOrderStatus().equals("Driver Accepted")) {
                Log.d("Test", "Test");
                holder.orderId.setText(response.getOrderId());
                holder.tv_status.setText(response.getOrderStatus());
                holder.tv_status.setTextColor(ctx.getResources().getColor(R.color.yellow));
                holder.tv_date.setText(response.getOrderDate());
            }
            if (allShipment.get(position).getOrderStatus().equals("Approved")) {
                Log.d("Test", "Test");
                holder.orderId.setText(response.getOrderId());
                holder.tv_status.setText(response.getOrderStatus());
                holder.tv_status.setTextColor(ctx.getResources().getColor(R.color.yellow));
                holder.tv_date.setText(response.getOrderDate());
            }
            if (allShipment.get(position).getOrderStatus().equals("Driver Arriving Now")) {
                Log.d("Test", "Test");
                holder.orderId.setText(response.getOrderId());
                holder.tv_status.setText(response.getOrderStatus());
                holder.tv_status.setTextColor(ctx.getResources().getColor(R.color.yellow));
                holder.tv_date.setText(response.getOrderDate());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return allShipment.size();
    }

    // Define the listener interface
    public interface OnItemClickListener {
        void onItemClick(View itemView, int position);
    }

    public static class ShipmentHolderView extends RecyclerView.ViewHolder {
        TextView tv_orderId, tv_status, tv_date, orderId;
        ImageView iv_statusIcon;

        public ShipmentHolderView(View view) {
            super(view);
            iv_statusIcon = (ImageView) view.findViewById(R.id.truck_ship);
            tv_orderId = (TextView) view.findViewById(R.id.orderid);
            orderId = (TextView) view.findViewById(R.id.shipment_txt_id);
            tv_status = (TextView) view.findViewById(R.id.orderstatus);
            tv_date = (TextView) view.findViewById(R.id.shipment_txt_date);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null)
                        listener.onItemClick(itemView, getLayoutPosition());
                }
            });
        }
    }
}
