package com.serhiisolodilov.corticaapp.ui;

import android.app.Fragment;
import android.app.LoaderManager;
import android.content.ContentUris;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.serhiisolodilov.corticaapp.R;
import com.serhiisolodilov.corticaapp.db.model.NoteEntity;
import com.serhiisolodilov.corticaapp.db.model.TransactionEntity;
import com.serhiisolodilov.corticaapp.db.provider.MyContentProvider;
import com.serhiisolodilov.corticaapp.ui.adapter.DetailsCursorArapter;
import com.squareup.picasso.Picasso;

public class PhotoDetailFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {
    private static final String TAG = PhotoDetailFragment.class.getSimpleName();

    private static final String NOTE_ID_LONG = "NOTE_ID_LONG";
    private static final String TID = "TID";

    private final int NOTE = 1;
    private final int TRANSACTIONS = 2;

    private ListView mListView;
    private ImageView mImageView;

    private CursorAdapter mCursorAdapter;

    public static PhotoDetailFragment newInstance(long id) {
        PhotoDetailFragment placeholderFragment = new PhotoDetailFragment();
        Bundle bundle = new Bundle();
        bundle.putLong(NOTE_ID_LONG, id);
        placeholderFragment.setArguments(bundle);
        return placeholderFragment;
    }

    public PhotoDetailFragment() {
    }

    private long mNoteId;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState == null) {
            Bundle bundle = getArguments();
            if (bundle != null && bundle.containsKey(NOTE_ID_LONG)) {
                mNoteId = bundle.getLong(NOTE_ID_LONG);
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_photo_details, container, false);
        mListView = (ListView) rootView.findViewById(R.id.list);
        mImageView = (ImageView) rootView.findViewById(R.id.image);
        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Bundle bundle = new Bundle();
        bundle.putLong(NOTE_ID_LONG, mNoteId);
        mCursorAdapter = new DetailsCursorArapter(getActivity());
        mListView.setAdapter(mCursorAdapter);
        getLoaderManager().initLoader(NOTE, bundle, this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        switch (id) {
            case NOTE:
                long noteId = args.getLong(NOTE_ID_LONG);
                return new CursorLoader(
                        getActivity(),
                        ContentUris.withAppendedId(MyContentProvider.NOTES_CONTENT_URI, noteId),
                        new String[]{
                                NoteEntity._ID,
                                NoteEntity.COLUMN_MEDIA_PATH,
                                NoteEntity.COLUMN_TID},
                        null,
                        null,
                        null
                );

            case TRANSACTIONS:
                String tid = args.getString(TID);
                return new CursorLoader(getActivity(),
                        MyContentProvider.TRANSACTIONS_CONTENT_URI,
                        new String[]{
                                TransactionEntity._ID,
                                TransactionEntity.COLUMN_NOTE_TEXT,
                                TransactionEntity.COLUMN_TID,
                                TransactionEntity.COLUMN_LATITUDE,
                                TransactionEntity.COLUMN_LONGITUDE,
                                TransactionEntity.COLUMN_LOCATION_ON_INIMAGE_X,
                                TransactionEntity.COLUMN_LOCATION_ON_INIMAGE_Y},
                        TransactionEntity.COLUMN_TID + "=?",
                        new String[]{tid},
                        null
                );

            default:
                throw new IllegalArgumentException("Unknown id " + id);
        }
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        switch (loader.getId()) {
            case NOTE:
                boolean moved = data.moveToFirst();
                if (!moved) {
                    return;
                }
                String tid = data.getString(data.getColumnIndex(NoteEntity.COLUMN_TID));
                if (tid == null) {
                    getActivity().finish();
                    Toast.makeText(getActivity(), "Not ready", Toast.LENGTH_SHORT).show();
                    return;
                }
                String path = data.getString(data.getColumnIndex(NoteEntity.COLUMN_MEDIA_PATH));
                Picasso.with(getActivity()).load("file:" + path).resize(500, 500).into(mImageView);
                Bundle bundle = new Bundle();
                bundle.putString(TID, tid);
                getLoaderManager().initLoader(TRANSACTIONS, bundle, this);
                break;
            case TRANSACTIONS:
                mCursorAdapter.swapCursor(data);
                break;
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
}
