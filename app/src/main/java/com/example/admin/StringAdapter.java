package com.example.admin;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class StringAdapter extends ArrayAdapter<ThreeStrings> {

    private final int resourceLayout;
    private final Context mContext;

    public StringAdapter(Context context, int resource, ArrayList<ThreeStrings> items) {
        super(context, resource, items);
        this.resourceLayout = resource;
        this.mContext = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View v = convertView;

        if (v == null) {
            LayoutInflater vi;
            vi = LayoutInflater.from(mContext);
            v = vi.inflate(resourceLayout, null);
        }

        ThreeStrings p = getItem(position);

        if (p != null) {
            TextView tt1 = v.findViewById(R.id.rollno);
            TextView tt2 = v.findViewById(R.id.name);
            TextView tt3 = v.findViewById(R.id.cgpa);

            if (tt1 != null) {
                tt1.setText(p.getS1());
            }

            if (tt2 != null) {
                tt2.setText(p.getS2());
            }

            if (tt3 != null) {
                tt3.setText(p.getS3());
            }
        }

        return v;
    }

}