package com.example.android.todolist;


import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.os.Bundle;

import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.app.LoaderManager;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;

import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.example.android.todolist.BaseClass.Catagory;
import com.example.android.todolist.adapter.CatagoryCursorAdapter;
import com.example.android.todolist.data.CatagoryColumn;
import com.example.android.todolist.data.TaskColumn;
import com.example.android.todolist.data.ToDoTaskProvider;
import com.example.android.todolist.event.RecyclerViewItemClickListener;
import com.example.android.todolist.service.ToDoTaskIntentService;
import com.example.android.todolist.touch_helper.SimpleItemTouchHelperCallback;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>,RecyclerViewItemClickListener.OnItemClickListener {

    private static final int CURSOR_LOADER_ID = 0;
    private static final int CURSOR_LOADER_ID_TASK = 1;
    public static final String EXTRA_TAG="EXTRA_TAG";
    private CatagoryCursorAdapter mCatagoryCusrsorAdapter;
    Context mContext;
    ArrayList<Catagory> catagoryList;
    @Bind(R.id.catagory_list)
    RecyclerView catagoryListRecyclerView;
    String args[];
    int cnt=0;
    public MainActivityFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

    }
    @Override
    public void onResume(){
        super.onResume();
        getLoaderManager().restartLoader(CURSOR_LOADER_ID, null, this);
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView= inflater.inflate(R.layout.fragment_main, container, false);
        ButterKnife.bind(this, rootView);


        catagoryListRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        catagoryListRecyclerView.addOnItemTouchListener(new RecyclerViewItemClickListener(getContext(), this));
        mCatagoryCusrsorAdapter = new CatagoryCursorAdapter(getContext(),null);
        catagoryListRecyclerView.setAdapter(mCatagoryCusrsorAdapter);

        getLoaderManager().initLoader(CURSOR_LOADER_ID, null, this);
        getLoaderManager().initLoader(CURSOR_LOADER_ID_TASK,null,this);

        //swip to delete catagory
        ItemTouchHelper.Callback callback = new SimpleItemTouchHelperCallback(mCatagoryCusrsorAdapter);
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(callback);
        itemTouchHelper.attachToRecyclerView(catagoryListRecyclerView);


        return rootView;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id,Bundle args){

        switch (id) {

            case CURSOR_LOADER_ID:
                return new CursorLoader(
                        getActivity(),
                        ToDoTaskProvider.Catagory.CONTENT_URI,
                        new String[]{CatagoryColumn._ID, CatagoryColumn.CATAGORY_NAME},
                        null,
                        null,
                        null);
            case CURSOR_LOADER_ID_TASK:
                return new CursorLoader(
                        getActivity(),
                        ToDoTaskProvider.Task.CONTENT_URI,
                        new String[]{TaskColumn._ID, TaskColumn.TASK_DETAIL, TaskColumn.CATAGORY_ID},
                        null,
                        null,
                        null);
            default:
                throw new IllegalStateException();
        }
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data){
        if(loader.getId()==CURSOR_LOADER_ID && data!= null) {
            args = new String[data.getCount()];
            data.moveToFirst();
            for (int i = 0; i < data.getCount(); i++) {
                // catagoryList.add(mCatagoryCusrsorAdapter.getCatagory(i));
                int id = data.getInt(data.getColumnIndex(CatagoryColumn._ID));
                args[i] = new Integer(id).toString();
            }
            mCatagoryCusrsorAdapter.swapCursor(data);
        }
        if(loader.getId()==CURSOR_LOADER_ID_TASK && data!= null) {

        }
    }
    public void restartLoader() {
        getLoaderManager().restartLoader(CURSOR_LOADER_ID, null, this);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mCatagoryCusrsorAdapter.swapCursor(null);
    }

    /**
     * onItemClick() check requested is available in db  & if not available than stored it in database.
     * @param view - give selected view
     *             @param position - give position of selected view
     */
    @Override
    public void onItemClick(View view, int position){
        if(getActivity().findViewById(R.id.detail_container)!=null){
            Bundle arguments = new Bundle();
            arguments.putParcelable(DetailActivityFragment.ARGS_SYMBOL, mCatagoryCusrsorAdapter.getCatagory(position));
            DetailActivityFragment fragment = new DetailActivityFragment();
            fragment.setArguments(arguments);
            getActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.detail_container, fragment)
                    .commit();
        } else {
            Context context = view.getContext();
            Intent intent = new Intent(context, DetailActivity.class);
            Catagory tempCatagory = new Catagory();
            tempCatagory = mCatagoryCusrsorAdapter.getCatagory(position);
            intent.putExtra(DetailActivityFragment.ARGS_SYMBOL, mCatagoryCusrsorAdapter.getCatagory(position));
            startActivity(intent);
        }
    }
    /**
     * addCatagory() check requested is available in db  & if not available than stored it in database.
     * @param catagory - pass given catagory to method in string type
     */
    public void addCatagory(final String catagory){

        new AsyncTask<Void,Void,Boolean>(){

            @Override
            protected Boolean doInBackground(Void... params){
                Cursor cursor = getActivity().getContentResolver().query(ToDoTaskProvider.Catagory.CONTENT_URI,
                        new String[]{CatagoryColumn.CATAGORY_NAME},
                        CatagoryColumn.CATAGORY_NAME + "=?",
                        new String[]{catagory},
                        null);
                if(cursor!=null){
                    cursor.close();
                    return cursor.getCount()!=0;
                }
                return Boolean.FALSE;
            }

            @Override
            protected  void onPostExecute(Boolean catagoryAvailable){
                if(catagoryAvailable){
                    Snackbar.make(catagoryListRecyclerView, R.string.repeatedCatagory, Snackbar.LENGTH_LONG).show();
                }else {
                    Intent intent = new Intent(getActivity(), ToDoTaskIntentService.class);
                    intent.putExtra(ToDoTaskIntentService.EXTRA_TAG, ToDoTaskIntentService.ACTION_ADD);
                    intent.putExtra(ToDoTaskIntentService.EXTRA_CATAGORY, catagory);
                    getActivity().startService(intent);
                }
            }
        }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }
}
