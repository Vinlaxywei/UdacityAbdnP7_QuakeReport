package com.example.android.quakereport;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;


public class EarthQuakeListAdapter extends ArrayAdapter<EarthQuake> {
    private Context mContext;

    public EarthQuakeListAdapter(Context context, List<EarthQuake> earthQuakes) {
        super(context, 0, earthQuakes);
        mContext = context;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View itemView = convertView;
        if (itemView == null) {
            itemView = LayoutInflater.from(mContext).inflate(R.layout.list_item, parent, false);
        }

        EarthQuake earthQuake = getItem(position);
        TextView magView = (TextView) itemView.findViewById(R.id.mag_view);
        TextView mainLocView = (TextView) itemView.findViewById(R.id.mainLocation_view);
        TextView nearLocView = (TextView) itemView.findViewById(R.id.nearLocation_view);
        TextView dateView = (TextView) itemView.findViewById(R.id.date_view);
        TextView timeView = (TextView) itemView.findViewById(R.id.time_view);

        magView.setText(earthQuake.getmMagnitude());
        nearLocView.setText(earthQuake.getLocation()[0]);
        mainLocView.setText(earthQuake.getLocation()[1]);
        dateView.setText(earthQuake.getmDate());
        timeView.setText(earthQuake.getTime());

        GradientDrawable gradientDrawable = (GradientDrawable) magView.getBackground();
        gradientDrawable.setColor(ContextCompat.getColor(mContext, earthQuake.getCircleColor()));

        return itemView;
    }

}
