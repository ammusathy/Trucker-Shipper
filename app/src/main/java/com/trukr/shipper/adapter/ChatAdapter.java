package com.trukr.shipper.adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.trukr.shipper.R;
import com.trukr.shipper.constants.IConstant;
import com.trukr.shipper.model.ResponseParams.ChatResponseParams;

import java.util.ArrayList;

/**
 * Created by nijamudhin on 7/8/2016.
 */
public class ChatAdapter extends BaseAdapter {

    SharedPreferences preferences;
    String userId;
    private ArrayList<ChatResponseParams> chatMessageList;
    private Context context;

    public ChatAdapter(Context mContext, ArrayList<ChatResponseParams> chatMessage) {
        this.context = mContext;
        this.chatMessageList = chatMessage;

        preferences = context.getSharedPreferences("Trucker", Context.MODE_PRIVATE);// get shared preference and mode to be selected
        userId = preferences.getString("Userid", null);

    }

    public int getCount() {
        return this.chatMessageList.size();
    }

    public Object getItem(int index) {
        return this.chatMessageList.get(index);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        Holder holder;
        if (convertView == null) {

            holder = new Holder();
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            try {

                row = inflater.inflate(R.layout.left, parent, false);
                holder.left = (RelativeLayout) row.findViewById(R.id.left);
                holder.right = (LinearLayout) row.findViewById(R.id.right);

                holder.chatTextLeft = (TextView) row.findViewById(R.id.left_msgr);
                holder.imgChatLeft = (ImageView) row.findViewById(R.id.left_chat);
                holder.chatTextRight = (TextView) row.findViewById(R.id.right_msgr);
                holder.imgChatRight = (ImageView) row.findViewById(R.id.right_chat);

            } catch (Exception e) {
                e.printStackTrace();
            }


        } else {

            holder = (Holder) row.getTag();


        }
        System.out.println(" left path-----> " + userId+" :: "+chatMessageList.get(position).getUserId());

        if ((chatMessageList.get(position).UserId).equals(userId)) {
            holder.left.setVisibility(View.VISIBLE);
            holder.right.setVisibility(View.GONE);


            String path = chatMessageList.get(position).image;

            if (path.equals("")) {
                holder.chatTextLeft.setText(chatMessageList.get(position).message);
                holder.imgChatLeft.setVisibility(View.GONE);
                holder.chatTextLeft.setVisibility(View.VISIBLE);
            } else if (!path.equals("")) {
                holder.imgChatLeft.setVisibility(View.VISIBLE);
                holder.chatTextLeft.setVisibility(View.GONE);
                String uri = IConstant.chatUri + path;
                Picasso.with(context).load(uri)./*placeholder(R.drawable.profile_image).error(R.drawable.bell).*/
                        into(holder.imgChatLeft);
                System.out.println("uri value" + uri);
            }

        } else {

            holder.left.setVisibility(View.GONE);
            holder.right.setVisibility(View.VISIBLE);

            String path = chatMessageList.get(position).image;
            System.out.println("path----->" + path);
            if (path.equals("")) {
                holder.chatTextRight.setText(chatMessageList.get(position).message);
                holder.imgChatRight.setVisibility(View.GONE);
                holder.chatTextRight.setVisibility(View.VISIBLE);
            } else if (!path.equals("")) {
                holder.imgChatRight.setVisibility(View.VISIBLE);
                holder.chatTextRight.setVisibility(View.GONE);
                String uri = IConstant.chatUri + path;
                Picasso.with(context).load(uri)./*placeholder(R.drawable.profile_image).error(R.drawable.bell).*/

                        into(holder.imgChatRight);
                System.out.println("uri value" + uri);
            }
        }

        row.setTag(holder);

        return row;
    }

    public class Holder {
        TextView chatTextLeft;
        ImageView imgChatLeft;
        TextView chatTextRight;
        ImageView imgChatRight;
        LinearLayout right;
        RelativeLayout left;
    }
}
