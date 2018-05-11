package com.example.michal.mediaplayer;

import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

/**
 * Created by michal on 10.05.18.
 */

public class MusicMediaCursorAdapter extends CursorAdapter {

    public MusicMediaCursorAdapter(Context context, Cursor cursor) {
        super(context, cursor, CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);

    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.list_item_view, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView title_text = view.findViewById(R.id.item_top_text);
        TextView artist_name_text = view.findViewById(R.id.item_bottom_text);
        title_text.setText(cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.TITLE)));
        artist_name_text.setText(cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST)));

    }
}
