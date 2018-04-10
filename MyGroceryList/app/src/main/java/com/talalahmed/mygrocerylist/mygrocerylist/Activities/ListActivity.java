package com.talalahmed.mygrocerylist.mygrocerylist.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.talalahmed.mygrocerylist.mygrocerylist.Data.DatabaseHandler;
import com.talalahmed.mygrocerylist.mygrocerylist.Model.Grocery;
import com.talalahmed.mygrocerylist.mygrocerylist.R;
import com.talalahmed.mygrocerylist.mygrocerylist.UI.RecyclerViewAdapter;

import java.util.ArrayList;
import java.util.List;

public class ListActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerViewAdapter recyclerViewAdapter;
    private List<Grocery> groceryList;
    private List<Grocery> listItems;
    private DatabaseHandler db;

    private AlertDialog.Builder dialogbuilder;
    private AlertDialog dialog;
    private EditText grocery_item;
    private EditText grocery_qty;
    private Button saveButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();

                PopUpDialog();
            }
        });

        db = new DatabaseHandler(this);
        recyclerView = (RecyclerView) findViewById(R.id.recylcerViewId);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        groceryList = new ArrayList<>();
        listItems = new ArrayList<>();

        //Get Items from DB;

        groceryList=db.getAllGroceries();

        for(Grocery c : groceryList){
            Grocery grocery = new Grocery();
            grocery.setName(c.getName());
            grocery.setQuantity(c.getQuantity());
            grocery.setDateItemAdded("Added On: "+c.getDateItemAdded());
            grocery.setId(c.getId());

            listItems.add(grocery);
        }


        recyclerViewAdapter = new RecyclerViewAdapter(this,listItems);
        recyclerView.setAdapter(recyclerViewAdapter);
        recyclerViewAdapter.notifyDataSetChanged();



    }

    private void PopUpDialog() {

        dialogbuilder = new AlertDialog.Builder(this);
        View view = getLayoutInflater().inflate(R.layout.popup,null);

        grocery_item = (EditText) view.findViewById(R.id.grocery_item);
        grocery_qty = (EditText) view.findViewById(R.id.groceryQty);
        saveButton = (Button) view.findViewById(R.id.saveButton);

        dialogbuilder.setView(view);
        dialog = dialogbuilder.create();
        dialog.show();

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //TODO: Save to Database
                //TODO: Goto Next Screen
                if (!grocery_item.getText().toString().isEmpty() && !grocery_qty.getText().toString().isEmpty()) {
                    saveGroceryToDB(view);
                }
            }
        });
    }

    private void saveGroceryToDB(View v) {

        Grocery grocery = new Grocery();

        String newGrocery = grocery_item.getText().toString();
        String newGroceryQuantity = grocery_qty.getText().toString();

        grocery.setName(newGrocery);
        grocery.setQuantity(newGroceryQuantity);

        //Save to DB
        db.addGrocery(grocery);

        Snackbar.make(v, "Item Saved!", Snackbar.LENGTH_LONG).show();

        // Log.d("Item Added ID:", String.valueOf(db.getGroceriesCount()));
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                dialog.dismiss();
                //start a new activity
                startActivity(new Intent(ListActivity.this, ListActivity.class));
                finish();
            }
        }, 1200); //  1 second.


    }

}
