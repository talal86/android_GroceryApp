package com.talalahmed.mygrocerylist.mygrocerylist.Activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.talalahmed.mygrocerylist.mygrocerylist.R;

public class DetailsActivity extends AppCompatActivity {

    private TextView itemName;
    private TextView quantity;
    private TextView dateAdded;
    private int groceryId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        itemName = (TextView) findViewById(R.id.itemNameDet);
        quantity = (TextView) findViewById(R.id.quantityDet);
        dateAdded = (TextView) findViewById(R.id.dateAddedDet);

        Bundle bundle = new Bundle();
        bundle= getIntent().getExtras();

        if(bundle!= null){
            itemName.setText(bundle.getString("name"));
            quantity.setText(bundle.getString("quantity"));
            dateAdded.setText(bundle.getString("date"));
            groceryId = bundle.getInt("id");
        }

    }
}
