package com.serhiisolodilov.corticaapp.ui.adapter;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.serhiisolodilov.corticaapp.R;
import com.serhiisolodilov.corticaapp.db.model.TransactionEntity;


public class DetailsCursorArapter extends CursorAdapter {

    private LayoutInflater mLayoutInflater;

    public DetailsCursorArapter(Context context) {
        super(context, null, 0);
        mLayoutInflater = LayoutInflater.from(context);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View view = mLayoutInflater.inflate(R.layout.detail_item, parent, false);
        ViewHolder viewHolder = new ViewHolder();
        viewHolder.message = (TextView) view.findViewById(R.id.message);
        viewHolder.location = (TextView) view.findViewById(R.id.location);
        viewHolder.geolocation = (TextView) view.findViewById(R.id.geolocation);
        view.setTag(viewHolder);
        return view;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        ViewHolder viewHolder = (ViewHolder) view.getTag();
        String text = cursor.getString(cursor.getColumnIndex(TransactionEntity.COLUMN_NOTE_TEXT));
        viewHolder.message.setText(text);
        String locationX = cursor.getString(cursor.getColumnIndex(TransactionEntity.COLUMN_LOCATION_ON_INIMAGE_X));
        String locationY = cursor.getString(cursor.getColumnIndex(TransactionEntity.COLUMN_LOCATION_ON_INIMAGE_Y));
        String geolocationLat = cursor.getString(cursor.getColumnIndex(TransactionEntity.COLUMN_LATITUDE));
        String geolocationLon = cursor.getString(cursor.getColumnIndex(TransactionEntity.COLUMN_LONGITUDE));
        if (locationX != null && locationY != null) {
            viewHolder.location.setText("X:" + locationX + ", Y:" + locationY);
        } else {
            viewHolder.location.setText("");
        }
        if (geolocationLat != null && geolocationLon != null) {
            viewHolder.geolocation.setText("lat:" + geolocationLat + ", lon:" + geolocationLon);
        } else {
            viewHolder.geolocation.setText("");
        }

    }

    @Override
    public int getCount() {
        return getCursor() == null ? 0 : super.getCount();
    }

    private class ViewHolder {
        TextView message;
        TextView location;
        TextView geolocation;
    }
}
