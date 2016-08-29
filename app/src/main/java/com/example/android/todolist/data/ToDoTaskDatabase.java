package com.example.android.todolist.data;

import net.simonvt.schematic.annotation.Database;
import net.simonvt.schematic.annotation.Table;
/**
 * Created by veeral on 17/08/2016.
 */
@Database(version = ToDoTaskDatabase.VERSION)
public class ToDoTaskDatabase {
    private ToDoTaskDatabase(){}
    public static final int VERSION = 7;

    @Table(CatagoryColumn.class) public static final String CATAGORY = "catagory";
    @Table(TaskColumn.class) public static final String TASK = "task";
}
