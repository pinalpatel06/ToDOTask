package com.example.android.todolist.service;

import android.app.IntentService;
import android.app.LoaderManager;
import android.content.ContentProviderOperation;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.RemoteException;
import android.util.Log;

import com.example.android.todolist.BaseClass.Task;
import com.example.android.todolist.data.CatagoryColumn;
import com.example.android.todolist.data.TaskColumn;
import com.example.android.todolist.data.ToDoTaskDatabase;
import com.example.android.todolist.data.ToDoTaskProvider;

import java.util.ArrayList;

/**
 * Created by veeral on 17/08/2016.
 */
public class ToDoTaskIntentService extends IntentService {
    public static final String ACTION_INIT = "addTask";
    public static final String ACTION_ADD = "addCatagory";
    public static final String ACTION_DELETE = "deleteTask";
    public static final String ACTION_INIT_TASK="initTask";
    public static final String ACTION_COUNT="countTask";

    public static final String EXTRA_TAG = "tag";
    public static final String EXTRA_CATAGORY = "catagory";
    public static final String EXTRA_TASK="task";
    public static final String EXTRA_TASK_DELETE="taskDelete";

    private Context mContext;
    public ToDoTaskIntentService(){
        super(ToDoTaskIntentService.class.getName());
        mContext = this;
    }

    public ToDoTaskIntentService(String name,Context context) {
        super(name);
    }

   @Override
    protected void onHandleIntent(Intent intent){

       //to check that service is started
       Log.d(ToDoTaskIntentService.class.getSimpleName(), "Task intent service");
       if (intent.getStringExtra("tag").equals("addCatagory")){

        String catagory = intent.getStringExtra("catagory");
           saveCatagoryToDb(catagory);
       }else if(intent.getStringExtra("tag").equals("addTask")){
           Task task = intent.getParcelableExtra("task");
            saveTaskToDb(task);
       }else if(intent.getStringExtra("tag").equals("deleteTask")){
           ArrayList<Task> taskList = intent.getParcelableArrayListExtra("taskDelete");
           deleteTaskFromDb(taskList);
       }else if(intent.getStringExtra("tag").equals("countTask")){
           String ids = intent.getStringExtra("task");
           count(ids);
       }
   }
    private void saveCatagoryToDb(String catagory){
        Log.d(catagory,"in service");
        if(catagory !=null) {
            ContentResolver contentResolver = mContext.getContentResolver();
            ContentValues contentValues = new ContentValues();
            contentValues.put(CatagoryColumn.CATAGORY_NAME, catagory);
            contentResolver.insert(ToDoTaskProvider.Catagory.CONTENT_URI, contentValues);

        }
    }
    private void saveTaskToDb(Task task){
        if(task!=null){
            ContentResolver contentResolver = mContext.getContentResolver();
            ContentValues contentValues = new ContentValues();
            contentValues.put(TaskColumn.TASK_DETAIL,task.getTaskDetail());
            contentValues.put(TaskColumn.CATAGORY_ID,task.getCatagoryId());
            contentResolver.insert(ToDoTaskProvider.Task.CONTENT_URI,contentValues);
        }
    }
    private void deleteTaskFromDb(ArrayList<Task> taskList){
        String list=null;
        Task task;
        if(taskList!=null) {
            for (int i = 0; i < taskList.size(); i++) {
                task = taskList.get(i);
                list = task.getTaskId()+"";
                mContext.getContentResolver().delete(ToDoTaskProvider.Task.withId(list), null, null);
            }
        }

    }
    public  String count(String args) {
        Cursor cursor = mContext.getContentResolver().query(ToDoTaskProvider.Task.CONTENT_URI,
                new String[]{TaskColumn.TASK_DETAIL},
                TaskColumn.CATAGORY_ID,
                new String[]{args},
                null);
        if (cursor.getCount() == 0) {
            cursor.close();
            return Integer.toString(0);
        } else {
            // cursor.moveToFirst();
            int result = cursor.getCount();
            cursor.close();
            return Integer.toString(result);
        }
    }
}
