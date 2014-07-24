package com.serhiisolodilov.corticaapp.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.MenuItem;

import com.serhiisolodilov.corticaapp.R;

public class PhotoDetailsActivity extends Activity {

    public static final String NOTE_ID_LONG = "NOTE_ID_LONG";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_details);
        if (savedInstanceState == null) {
            long noteId = getIntent().getLongExtra(NOTE_ID_LONG, -1);
            if (noteId < 0) {
                finish();
                return;
            }
            getFragmentManager().beginTransaction()
                    .add(R.id.container, PhotoDetailFragment.newInstance(noteId))
                    .commit();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            NavUtils.navigateUpTo(this, new Intent(this, MainActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
