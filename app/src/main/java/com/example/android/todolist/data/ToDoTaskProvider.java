package com.example.android.todolist.data;

import android.net.Uri;
import net.simonvt.schematic.annotation.ContentProvider;
import net.simonvt.schematic.annotation.ContentUri;
import net.simonvt.schematic.annotation.InexactContentUri;
import net.simonvt.schematic.annotation.TableEndpoint;

import com.example.android.todolist.BaseClass.Task;
import java.util.ArrayList;

/**
 * Created by veeral on 16/08/2016.
 */
@ContentProvider(authority = ToDoTaskProvider.AUTHORITY, database = ToDoTaskDatabase.class)
public class ToDoTaskProvider{
    public static final String AUTHORITY = "com.example.android.todolist.data.ToDoTaskProvider";

    static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);

    interface Path{
        String CATAGORY = "catagory";
        String TASK = "task";
    }
    private static Uri buildUri(String... paths){
        Uri.Builder builder = BASE_CONTENT_URI.buildUpon();
        for (String path:paths){
            builder.appendPath(path);
        }
        return builder.build();
    }

    @TableEndpoint(table=ToDoTaskDatabase.TASK)
    public static class Task{
        @ContentUri(
                path = Path.TASK,
                type = "vnd.android.cursor.dir/task"
        )
        public static final Uri CONTENT_URI = buildUri(Path.TASK);

       @InexactContentUri(
               name = "TASK_ID",
               path = Path.TASK + "/*",
               type = "vnd.android.cursor.item/task",
               whereColumn = TaskColumn._ID,
               pathSegment = 1)
        public static Uri withId(String args) {
             return buildUri(Path.TASK,args);
        }
    }

    @TableEndpoint(table = ToDoTaskDatabase.CATAGORY)
    public static class Catagory{
        @ContentUri(
                path = Path.CATAGORY,
                type = "vnd.android.cursor.dir/catagory"
        )
        public static final Uri CONTENT_URI = buildUri(Path.CATAGORY);

        @InexactContentUri(
                name = "CATAGORY_ID",
                path = Path.CATAGORY + "/*",
                type = "vnd.android.cursor.item/catagoty",
                whereColumn = CatagoryColumn.CATAGORY_NAME,
                pathSegment = 1)
        public static Uri withId(String catagoryName){
            return buildUri(Path.CATAGORY , catagoryName);
        }
    }

}
