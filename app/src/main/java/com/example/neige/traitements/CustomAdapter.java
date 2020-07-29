package com.example.neige.traitements;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.neige.R;

import java.util.ArrayList;

public class CustomAdapter extends ArrayAdapter {
    private ArrayList dataSet;
    private Context mContext;

    private static class ViewHolder {
        TextView textDate;
        TextView textLatLong;
        TextView textPourcentageDeNeige;
        CheckBox checkBox;
    }

    public CustomAdapter(ArrayList data, Context context) {
        super(context, R.layout.item_liste, data);
        this.dataSet = data;
        this.mContext = context;
    }

    @Override
    public int getCount() {
        return dataSet.size();
    }

    @Override
    public FormulaireModele getItem(int position) {
        return (FormulaireModele) dataSet.get(position);
    }

    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        ViewHolder viewHolder;
        final View result;

        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_liste, parent, false);
            viewHolder.textPourcentageDeNeige = (TextView) convertView.findViewById(R.id.textPourcentageDeNeige);
            viewHolder.textDate = (TextView) convertView.findViewById(R.id.textDate);
            viewHolder.textLatLong = (TextView) convertView.findViewById(R.id.textLatLong);
            viewHolder.checkBox = (CheckBox) convertView.findViewById(R.id.checkBox);

            result = convertView;
            convertView.setTag(viewHolder);

        } else {
            viewHolder = (ViewHolder) convertView.getTag();
            result = convertView;
        }

        FormulaireModele item = getItem(position);

        viewHolder.textLatLong.setText(item.latitude + ", " + item.longitude);
        viewHolder.textDate.setText(item.date);
        viewHolder.checkBox.setChecked(item.checked);
        // viewHolder.textPourcentageDeNeige.setText(item.pourcentageNeige);

        return result;
    }
}
