package com.talalahmed.mygrocerylist.mygrocerylist.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;

import com.talalahmed.mygrocerylist.mygrocerylist.Data.DatabaseHandler;
import com.talalahmed.mygrocerylist.mygrocerylist.Model.Grocery;
import com.talalahmed.mygrocerylist.mygrocerylist.R;

public class MainActivity extends AppCompatActivity {

    private AlertDialog.Builder dialogbuilder;
    private AlertDialog dialog;
    private EditText grocery_item;
    private EditText grocery_qty;
    private Button saveButton;
    private DatabaseHandler db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db = new DatabaseHandler(this);
        byPassActivty();
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
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void PopUpDialog(){

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

    private void saveGroceryToDB(View view) {

        Grocery grocery = new Grocery();

        String newGrocery = grocery_item.getText().toString();
        String newGroceryQuantity = grocery_qty.getText().toString();

        grocery.setName(newGrocery);
        grocery.setQuantity(newGroceryQuantity);

        //Save to DB
        db.addGrocery(grocery);

        Snackbar.make(view,"Item Saved",Snackbar.LENGTH_LONG).show();

        Log.d("Item Added ID:" ,String.valueOf(db.getGroceriesCount()));


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                dialog.dismiss();

                //Start New Activity

                startActivity(new Intent(MainActivity.this,ListActivity.class));

            }
        },1000);

    }

    public void byPassActivty(){

        if(db.getGroceriesCount()>0){
            startActivity(new Intent(MainActivity.this,ListActivity.class));
            finish();
        }
    }
}
