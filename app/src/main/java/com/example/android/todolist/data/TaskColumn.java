package com.example.android.todolist.data;

import net.simonvt.schematic.annotation.AutoIncrement;
import net.simonvt.schematic.annotation.DataType;
import net.simonvt.schematic.annotation.NotNull;
import net.simonvt.schematic.annotation.PrimaryKey;
/**
 * Created by veeral on 17/08/2016.
 */
public class TaskColumn {
    @DataType(DataType.Type.INTEGER) @PrimaryKey @AutoIncrement
    public static final String _ID = "_id";
    @DataType(DataType.Type.TEXT) @NotNull
    public static final String TASK_DETAIL = "task_detail";
    @DataType(DataType.Type.INTEGER) @NotNull
    public static final String CATAGORY_ID = "catagory_ID";

}
