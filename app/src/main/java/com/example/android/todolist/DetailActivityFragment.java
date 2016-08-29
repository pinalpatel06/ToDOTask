package com.example.android.todolist;

import android.content.Intent;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.annotation.StringDef;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.example.android.todolist.BaseClass.Catagory;
import com.example.android.todolist.BaseClass.Task;
import com.example.android.todolist.adapter.CatagoryCursorAdapter;
import com.example.android.todolist.adapter.TaskCursorAdapter;
import com.example.android.todolist.data.CatagoryColumn;
import com.example.android.todolist.data.TaskColumn;
import com.example.android.todolist.data.ToDoTaskDatabase;
import com.example.android.todolist.data.ToDoTaskProvider;
import com.example.android.todolist.event.RecyclerViewItemClickListener;
import com.example.android.todolist.service.ToDoTaskIntentService;
import android.support.v7.app.ActionBar;
import net.simonvt.schematic.annotation.Database;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * A placeholder fragment containing a simple view.
 */
public class DetailActivityFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>,RecyclerViewItemClickListener.OnItemClickListener{
    private static final String LOG_TAG = DetailActivityFragment.class.getSimpleName();
    private static final int CURSOR_LOADER_ID = 0;
    private static final int CURSOR_LOADER_ID_TASK = 1;
    public static final String ARGS_SYMBOL = "ARGS_SYMBOL";
    public static final String TASK_DATA = "TASK_DATA";
    private MaterialDialog mNewStockDialog;
    private  Catagory mCatagory;
    private TaskCursorAdapter mTaskCursorAdapter;

    private boolean taskStatus[];
    @Bind(R.id.task_detail_list)
            RecyclerView mTaskListRecyclerView;
    @Bind(R.id.delete)
            ImageView mDelete;
    @Bind(R.id.add_new)
            ImageView mAddNew;
    ArrayList<Task> taskList;
    private int noOfTask;
    public DetailActivityFragment() {
        taskList = new ArrayList<Task>();

    }
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        Intent intent;

        if(savedInstanceState!=null){
            mCatagory = savedInstanceState.getParcelable(TASK_DATA);
        }
        if(args !=null){
            mCatagory = args.getParcelable(ARGS_SYMBOL);
            Log.i(mCatagory.toString(), "data");
        }else {
            intent = this.getActivity().getIntent();
            mCatagory = intent.getParcelableExtra(ARGS_SYMBOL);
        }
        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        if(actionBar!=null){
            try {
                actionBar.setTitle(mCatagory.getCatagoryName());
            }catch (NullPointerException e){
                Log.e(LOG_TAG,"Error "+e);
            }
        }


    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView= inflater.inflate(R.layout.fragment_detail, container, false);
        ButterKnife.bind(this, rootView);



        mTaskListRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mTaskListRecyclerView.addOnItemTouchListener(new RecyclerViewItemClickListener(getContext(), this));
        mTaskCursorAdapter = new TaskCursorAdapter(getContext(),null);
        mTaskListRecyclerView.setAdapter(mTaskCursorAdapter);

        if(mCatagory==null) {
            getLoaderManager().initLoader(CURSOR_LOADER_ID_TASK,null,this);
        }
        getLoaderManager().initLoader(CURSOR_LOADER_ID, null, this);

        return rootView;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id,Bundle args){

        switch (id) {
            case CURSOR_LOADER_ID_TASK:
                return new CursorLoader(
                        getContext(),
                        ToDoTaskProvider.Catagory.CONTENT_URI,
                        new String[]{CatagoryColumn._ID, CatagoryColumn.CATAGORY_NAME},
                        null,
                        null,
                        null);
            case CURSOR_LOADER_ID:
                if(mCatagory==null){
                    Cursor  cursor = getContext().getContentResolver().query(
                            ToDoTaskProvider.Catagory.CONTENT_URI,
                            new String[]{CatagoryColumn._ID,CatagoryColumn.CATAGORY_NAME},
                            null,
                            null,
                            null);
                    mCatagory = new Catagory();
                    cursor.moveToFirst();
                    if(cursor.getCount()>0) {
                        mCatagory.setCatagoryId(cursor.getInt(cursor.getColumnIndex(CatagoryColumn._ID)));
                        mCatagory.setCatagoryName(cursor.getString(cursor.getColumnIndex(CatagoryColumn.CATAGORY_NAME)));
                    }
                }
                if(mCatagory!=null) {
                    return new CursorLoader(
                            getActivity(),
                            ToDoTaskProvider.Task.CONTENT_URI,
                            new String[]{TaskColumn._ID, TaskColumn.TASK_DETAIL, TaskColumn.CATAGORY_ID},
                            TaskColumn.CATAGORY_ID + " = ?",
                            new String[]{mCatagory.getCatagoryId() + ""},
                            null);
                }
                default:
                    throw new IllegalStateException();
        }

    }
    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data){
        mTaskCursorAdapter.swapCursor(data);
        if(mTaskCursorAdapter.getItemCount()==0){
            //Snackbar.make(mTaskListRecyclerView, getString(R.string.dataNotAvailable),
              //      Snackbar.LENGTH_LONG).show();
        }else {
            noOfTask = data.getCount();
            taskStatus= new boolean[noOfTask];
        }
    }
    private void restartLoader() {
        getLoaderManager().restartLoader(CURSOR_LOADER_ID, null, this);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mTaskCursorAdapter.swapCursor(null);
    }

    @Override
    public void onResume(){
        super.onResume();
        getLoaderManager().restartLoader(CURSOR_LOADER_ID, null, this);

    }


    @Override
    public void onSaveInstanceState(Bundle saveInstanceState){
        super.onSaveInstanceState(saveInstanceState);
        saveInstanceState.putParcelable(TASK_DATA,mCatagory);
    }
    @Override
    public void onPause(){
        super.onPause();
    }

    /**
     * onItemClick() select data item for delete.
     * @param view - give selected view
     *             @param position - give position of selected view
     */
    @Override
    public void onItemClick(View view, int position){
        ImageView imageView = (ImageView)view.findViewById(R.id.task_Image);
        taskStatus[position]=!taskStatus[position];
        if(taskStatus[position]){
                imageView.setImageResource(R.drawable.ic_circle_check);
        }else{
                imageView.setImageResource(R.drawable.ic_circle);
        }
    }


    @OnClick({R.id.delete,R.id.add_new})
    public void onClick(View view) {

        if(view.findViewById(R.id.add_new) == mAddNew){
            showDialogForAddingNewTask();
        }else if(view.findViewById(R.id.delete)==mDelete) {
            for (int i = 0; i < noOfTask; i++) {
                if (taskStatus[i] == true) {
                    taskList.add(mTaskCursorAdapter.getTask(i));

                }
            }
            deleteTask(taskList);
        }

    }
    public void showDialogForAddingNewTask(){
//        mNewStockDialog = new MaterialDialog.Builder(getActivity()).title(R.string.titleTask)
//                .inputType(InputType.TYPE_CLASS_TEXT)
//                .autoDismiss(true)
//                .negativeText(R.string.disagree)
//                .input(R.string.input_hint,R.string.input_pre_fill,false,
//                        new MaterialDialog.InputCallback(){
//                            @Override
//                            public void onInput(@NonNull MaterialDialog dialog , CharSequence input){
//                                addTask(input.toString());
//                            }
//                        }).build();
//        mNewStockDialog.show();
        new MyDialogFragment().show(getFragmentManager(), DetailActivityFragment.LOG_TAG);
    }
//    @Override
//    public void onInput(@NonNull MaterialDialog mDialog, CharSequence input){
//        addTask(input.toString());
//    }

    public void deleteTask(final ArrayList<Task> list){
        new AsyncTask<Void,Void,Boolean>(){

            @Override
            protected Boolean doInBackground(Void... params){
                Intent intent = new Intent(getActivity(),ToDoTaskIntentService.class);
                intent.putExtra(ToDoTaskIntentService.EXTRA_TAG, ToDoTaskIntentService.ACTION_DELETE);
                intent.putParcelableArrayListExtra(ToDoTaskIntentService.EXTRA_TASK_DELETE, list);
                getActivity().startService(intent);
                return true;
            }

            @Override
            protected  void onPostExecute(Boolean catagoryAvailable){
                for(int i=0;i<noOfTask;i++){
                    taskStatus[i]=false;

                }
            }
        }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

    }

    /**
     * addTask() check requested is available in db  & if not available than stored it in database.
     * @param task - pass given task to method in string type
     */
    public void addTask(final String task){

        new AsyncTask<Void,Void,Boolean>(){

            @Override
            protected Boolean doInBackground(Void... params){
                /*Cursor cursor = getActivity().getContentResolver().query(ToDoTaskProvider.Task.CONTENT_URI,
                        new String[]{TaskColumn.TASK_DETAIL},
                        TaskColumn.TASK_DETAIL + "=?",
                        new String[]{task},
                        null);
                if(cursor!=null){
                    cursor.close();
                    return cursor.getCount()!=0;
                }*/
                return Boolean.FALSE;

            }

            @Override
            protected  void onPostExecute(Boolean catagoryAvailable){
                if(catagoryAvailable == false) {
                    Task mTask = new Task();
                    mTask.setTaskDetail(task);
                    mTask.setCatagoryId(mCatagory.getCatagoryId());
                    Log.d(getActivity().toString(), "async");
                    Intent intent = new Intent(getActivity(), ToDoTaskIntentService.class);
                    intent.putExtra(ToDoTaskIntentService.EXTRA_TAG, ToDoTaskIntentService.ACTION_INIT);
                    intent.putExtra(ToDoTaskIntentService.EXTRA_TASK, mTask);
                    getActivity().startService(intent);
                }else{
                    Snackbar.make(mTaskListRecyclerView, getString(R.string.repeatedTask),
                            Snackbar.LENGTH_LONG).show();
                }
            }
        }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

}
