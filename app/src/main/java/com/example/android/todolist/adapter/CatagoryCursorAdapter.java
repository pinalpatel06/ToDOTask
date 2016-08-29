package com.example.android.todolist.adapter;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.view.View;
import android.widget.TextView;

import com.example.android.todolist.MainActivityFragment;
import com.example.android.todolist.R;
import com.example.android.todolist.data.CatagoryColumn;
import com.example.android.todolist.data.TaskColumn;
import com.example.android.todolist.data.ToDoTaskProvider;
import com.example.android.todolist.service.ToDoTaskIntentService;
import com.example.android.todolist.touch_helper.ItemTouchHelperAdapter;
import com.example.android.todolist.touch_helper.ItemTouchHelperViewHolder;
import com.example.android.todolist.BaseClass.Catagory;

import butterknife.Bind;

/**
 * Created by pinal on 19/08/2016.
 */
public class CatagoryCursorAdapter extends CursorRecyclerViewAdapter<CatagoryCursorAdapter.ViewHolder>
        implements ItemTouchHelperAdapter{
    private Context mContext;
    public CatagoryCursorAdapter(Context context,Cursor cursor){
        super(context,cursor);
        mContext = context;

    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent,int viewType){

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.to_do_list_main,parent,false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final Cursor cursor){
        viewHolder.catagoryName.setText(cursor.getString(cursor.getColumnIndex("catagory_name")));
        String args = cursor.getString(cursor.getColumnIndex("_id"));
        String id=count(args);
        viewHolder.taskCount.setText("Total Task: " + id);


    }
    /**
     * count() helper method to count total no of  task pending in perticular task.
     *      @param args - catagory for which task is pending
     *                  @return String - return no of task in instance of string
     */

    public  String count(String args) {
        Cursor cursor = mContext.getContentResolver().query(ToDoTaskProvider.Task.CONTENT_URI,
                new String[]{TaskColumn.TASK_DETAIL},
                TaskColumn.CATAGORY_ID+" = ?",
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

    @Override public int getItemCount() {
        return super.getItemCount();
    }

    /**
     * getCatagory() helper method fetch instance of catagory on given position.
     *      @param position - give position of selected view
     *                      @return Catagory - return instance of Catagory
     */

    public Catagory getCatagory(int position) {
        Cursor c = getCursor();
        c.moveToPosition(position);
        Catagory catagory = new Catagory();
        catagory.setCatagoryId(c.getInt(c.getColumnIndex(CatagoryColumn._ID)));
        catagory.setCatagoryName(c.getString(c.getColumnIndex(CatagoryColumn.CATAGORY_NAME)));
        return catagory;
    }
    @Override public void onItemDismiss(int position) {
        Cursor c = getCursor();
        c.moveToPosition(position);
        String catagoryName = c.getString(c.getColumnIndex(CatagoryColumn.CATAGORY_NAME));
        mContext.getContentResolver().delete(ToDoTaskProvider.Catagory.withId(catagoryName), null, null);
        notifyItemRemoved(position);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder
            implements ItemTouchHelperViewHolder, View.OnClickListener{
        public final TextView catagoryName;
        public final TextView taskCount;

        public ViewHolder(View view) {
            super(view);
            catagoryName = (TextView) view.findViewById(R.id.catagory_name_text_view);
            taskCount = (TextView) view.findViewById(R.id.catagory_detail_item_count);
        }

        @Override
        public void onItemSelected(){
            itemView.setBackgroundColor(Color.LTGRAY);
        }

        @Override
        public void onItemClear() {
            itemView.setBackgroundColor(0);
        }

        @Override
        public void onClick(View v) {

        }
    }
}
