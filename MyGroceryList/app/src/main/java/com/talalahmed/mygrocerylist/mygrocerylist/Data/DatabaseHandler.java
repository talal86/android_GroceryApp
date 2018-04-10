package com.talalahmed.mygrocerylist.mygrocerylist.Data;

import android.content.ContentValues;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.talalahmed.mygrocerylist.mygrocerylist.Model.Grocery;
import com.talalahmed.mygrocerylist.mygrocerylist.Util.Constants;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by talal.ahmed on 4/2/18.
 */

public class DatabaseHandler extends SQLiteOpenHelper {

    private Context ctx;

    public DatabaseHandler(Context context) {
        super(context, Constants.DB_NAME, null, Constants.DB_VERSION);
        this.ctx = context;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        String CREATE_GROCERY_TABLE = "CREATE TABLE " + Constants.TABLE_NAME + "("
                + Constants.KEY_ID + " INTEGER PRIMARY KEY," + Constants.KEY_GROCERY_ITEM + " TEXT,"
                + Constants.KEY_QUANTITY_NUMBER + " TEXT,"
                +Constants.KEY_DATE_NAME + " LONG);";

        sqLiteDatabase.execSQL(CREATE_GROCERY_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + Constants.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }

    /*
    CRUD OPERATIONS
     */

    // Add Item
    public void addGrocery(Grocery grocery){

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(Constants.KEY_GROCERY_ITEM,grocery.getName());
        values.put(Constants.KEY_QUANTITY_NUMBER,grocery.getQuantity());
        values.put(Constants.KEY_DATE_NAME,java.lang.System.currentTimeMillis());

        //INSERT ROW

        db.insert(Constants.TABLE_NAME,null,values);

        Log.d("Saved!", "Saved to DB");

    }

    //Get Grocery
    private Grocery getGrocery(int id){

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.query(Constants.TABLE_NAME, new String[] {Constants.KEY_ID,
        Constants.KEY_GROCERY_ITEM, Constants.KEY_QUANTITY_NUMBER, Constants.KEY_DATE_NAME},
                Constants.KEY_ID + "=?",
                new String[] {String.valueOf(id)},
                null,
                null,
                null);

        if (cursor!= null)
            cursor.moveToFirst();

            Grocery grocery = new Grocery();
            grocery.setId(Integer.parseInt(cursor.getString(cursor.getColumnIndex(Constants.KEY_ID))));
            grocery.setName(cursor.getString(cursor.getColumnIndex(Constants.KEY_GROCERY_ITEM)));
            grocery.setQuantity(cursor.getString(cursor.getColumnIndex(Constants.KEY_QUANTITY_NUMBER)));

            //Convert Time Stamp to Someting Readable
            java.text.DateFormat dateFormat = java.text.DateFormat.getDateInstance();
            String formattedDate = dateFormat.format(new Date(cursor.getLong(cursor.getColumnIndex(Constants.KEY_DATE_NAME)))
                    .getTime());
            grocery.setDateItemAdded(formattedDate);


        return grocery;
    }

    //Get All Groceries
    public List<Grocery> getAllGroceries(){

        SQLiteDatabase db = this.getReadableDatabase();
        List<Grocery> groceryList = new ArrayList<>();

        Cursor cursor = db.query(Constants.TABLE_NAME, new String[]{
                Constants.KEY_ID,Constants.KEY_GROCERY_ITEM,Constants.KEY_QUANTITY_NUMBER, Constants.KEY_DATE_NAME
        },null,
                null,
                null,
                null,
                Constants.KEY_DATE_NAME + " DESC");

        if (cursor.moveToFirst()){
            do {

                Grocery grocery = new Grocery();
                grocery.setId(Integer.parseInt(cursor.getString(cursor.getColumnIndex(Constants.KEY_ID))));
                grocery.setName(cursor.getString(cursor.getColumnIndex(Constants.KEY_GROCERY_ITEM)));
                grocery.setQuantity(cursor.getString(cursor.getColumnIndex(Constants.KEY_QUANTITY_NUMBER)));

                //Convert Time Stamp to Someting Readable
                java.text.DateFormat dateFormat = java.text.DateFormat.getDateInstance();
                String formattedDate = dateFormat.format(new Date(cursor.getLong(cursor.getColumnIndex(Constants.KEY_DATE_NAME)))
                        .getTime());
                grocery.setDateItemAdded(formattedDate);

                //Add to List
                groceryList.add(grocery);

            }while (cursor.moveToNext());
        }


        return groceryList;
    }

    //Update Grocery
    public int updateGrocery(Grocery grocery){

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(Constants.KEY_GROCERY_ITEM,grocery.getName());
        values.put(Constants.KEY_QUANTITY_NUMBER,grocery.getQuantity());
        values.put(Constants.KEY_DATE_NAME,java.lang.System.currentTimeMillis());//system time

        //update row
        return db.update(Constants.TABLE_NAME,values,
                Constants.KEY_ID + "=?",
                new String[]{String.valueOf(grocery.getId())});
    }

    //Delete Grocery

    public void deleteGrocery(int id){

        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(Constants.TABLE_NAME,Constants.KEY_ID + "=?",
                new String[]{String.valueOf(id)});
        db.close();

    }

    //Get Grocery Count
    public int getGroceriesCount(){

        String countQuery = "SELECT * FROM " + Constants.TABLE_NAME;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery,null);

        return cursor.getCount();
    }


}
