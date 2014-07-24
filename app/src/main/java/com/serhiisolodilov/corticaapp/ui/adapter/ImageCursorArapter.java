package com.serhiisolodilov.corticaapp.ui.adapter;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.serhiisolodilov.corticaapp.R;
import com.serhiisolodilov.corticaapp.db.model.NoteEntity;
import com.squareup.picasso.Picasso;


public class ImageCursorArapter extends CursorAdapter {

    private LayoutInflater mLayoutInflater;

    public ImageCursorArapter(Context context) {
        super(context, null, 0);
        mLayoutInflater = LayoutInflater.from(context);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View view = mLayoutInflater.inflate(R.layout.grid_item, parent, false);
        ViewHolder viewHolder = new ViewHolder();
        viewHolder.imageView = (ImageView) view.findViewById(R.id.image);
        viewHolder.lat = (TextView) view.findViewById(R.id.lat);
        viewHolder.lon = (TextView) view.findViewById(R.id.lon);
        view.setTag(viewHolder);
        return view;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        ViewHolder viewHolder = (ViewHolder) view.getTag();
        String imagePath = cursor.getString(cursor.getColumnIndex(NoteEntity.COLUMN_MEDIA_PATH));
        String lat = cursor.getString(cursor.getColumnIndex(NoteEntity.COLUMN_LAT));
        String lon = cursor.getString(cursor.getColumnIndex(NoteEntity.COLUMN_LON));
        if (lat != null && lon != null) {
            viewHolder.lat.setText(lat);
            viewHolder.lon.setText(lon);
        }
        Picasso.with(context)
                .load("file:" + imagePath)
                .resize(300, 300)
                .placeholder(R.drawable.ic_launcher)
                .into(viewHolder.imageView);
    }

    @Override
    public int getCount() {
        return getCursor() == null ? 0 : super.getCount();
    }

    private class ViewHolder {
        ImageView imageView;
        TextView lat;
        TextView lon;
    }
}
