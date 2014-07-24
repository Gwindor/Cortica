package com.serhiisolodilov.corticaapp.ui;

import android.app.Activity;
import android.app.Fragment;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.CursorAdapter;
import android.widget.GridView;

import com.serhiisolodilov.corticaapp.R;
import com.serhiisolodilov.corticaapp.db.model.NoteEntity;
import com.serhiisolodilov.corticaapp.db.provider.MyContentProvider;
import com.serhiisolodilov.corticaapp.ui.adapter.ImageCursorArapter;

public class MainFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>, AbsListView.OnItemClickListener {

    private OnFragmentInteractionListener mListener;

    private GridView mGridView;

    private CursorAdapter mAdapter;

    public static MainFragment newInstance() {
        MainFragment fragment = new MainFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public MainFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_item_grid, container, false);
        mGridView = (GridView) view.findViewById(R.id.grid);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mAdapter = new ImageCursorArapter(getActivity());
        mGridView.setAdapter(mAdapter);
        mGridView.setOnItemClickListener(this);
        getLoaderManager().initLoader(0, null, this);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (null != mListener) {
            mListener.onFragmentInteraction(id);
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(getActivity(), MyContentProvider.NOTES_CONTENT_URI, new String[]{NoteEntity._ID, NoteEntity.COLUMN_MEDIA_PATH, NoteEntity.COLUMN_LAT, NoteEntity.COLUMN_LON}, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mAdapter.swapCursor(null);
    }

    public interface OnFragmentInteractionListener {
        public void onFragmentInteraction(long id);
    }

}
