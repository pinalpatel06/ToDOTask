package com.example.android.todolist.adapter;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.todolist.BaseClass.Catagory;
import com.example.android.todolist.R;
import com.example.android.todolist.data.CatagoryColumn;
import com.example.android.todolist.data.TaskColumn;
import com.example.android.todolist.data.ToDoTaskProvider;
import com.example.android.todolist.touch_helper.ItemTouchHelperAdapter;
import com.example.android.todolist.touch_helper.ItemTouchHelperViewHolder;
import com.example.android.todolist.BaseClass.Task;

import butterknife.OnClick;

/**
 * Created by pinal on 22/08/2016.
 */
public class TaskCursorAdapter extends CursorRecyclerViewAdapter<TaskCursorAdapter.ViewHolder>
        implements ItemTouchHelperAdapter {
    private Context mContext;
    boolean colorFlag=false;
    public TaskCursorAdapter(Context context,Cursor cursor){
        super(context,cursor);
        mContext = context;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent,int viewType){

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.task_list,parent,false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final Cursor cursor){
       if(cursor!=null) {
           viewHolder.taskData.setText(cursor.getString(cursor.getColumnIndex(TaskColumn.TASK_DETAIL)));
           viewHolder.taskImage.setImageResource(R.drawable.ic_circle);
       }

    }
    @Override public int getItemCount() {
        return super.getItemCount();
    }

    /**
     * getTaskDetail() helper method to fetch task data.
     *      @param position - give position of selected view
     *                      @return String - return instance of string
     */
    public String getTaskDetail(int position) {
        Cursor c = getCursor();
        c.moveToPosition(position);
        String task = c.getString(c.getColumnIndex(TaskColumn.TASK_DETAIL));
        return task;
    }

    /**
     * getTask() helper method to fetch task data in instance of task.
     *      @param position - give position of selected view
     *                      @reture Task - retrun instance of task
     */
    public Task getTask(int position) {
        Cursor c = getCursor();
        c.moveToPosition(position);
        Task task = new Task();
        task.setTaskId(c.getInt(c.getColumnIndex(TaskColumn._ID)));
        task.setTaskDetail(c.getString(c.getColumnIndex(TaskColumn.TASK_DETAIL)));
        task.setCatagoryId(c.getInt(c.getColumnIndex(TaskColumn.CATAGORY_ID)));
        return task;
    }

    @Override public void onItemDismiss(int position) {
        Cursor c = getCursor();
        c.moveToPosition(position);
        String task = c.getString(c.getColumnIndex(TaskColumn.TASK_DETAIL));
        mContext.getContentResolver().delete(ToDoTaskProvider.Catagory.withId(task), null, null);
        notifyItemRemoved(position);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder
            implements ItemTouchHelperViewHolder, View.OnClickListener{
        public final TextView taskData;
        public final ImageView taskImage;
        public ViewHolder(View view) {
            super(view);
            taskData = (TextView) view.findViewById(R.id.task_detail);
            taskImage = (ImageView)view.findViewById(R.id.task_Image);
        }

        @Override
        public void onItemSelected() { }

        @Override
        public void onItemClear() {
            itemView.setBackgroundColor(0);
        }

        @Override
        public void onClick(View v) { }


    }
}
