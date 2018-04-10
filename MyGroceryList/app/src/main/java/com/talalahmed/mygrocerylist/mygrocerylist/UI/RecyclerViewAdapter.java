package com.talalahmed.mygrocerylist.mygrocerylist.UI;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.talalahmed.mygrocerylist.mygrocerylist.Activities.DetailsActivity;
import com.talalahmed.mygrocerylist.mygrocerylist.Data.DatabaseHandler;
import com.talalahmed.mygrocerylist.mygrocerylist.Model.Grocery;
import com.talalahmed.mygrocerylist.mygrocerylist.R;

import java.util.List;

/**
 * Created by talal.ahmed on 4/2/18.
 */

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>{

    private Context context;
    private List<Grocery> groceryList;

    private AlertDialog.Builder alertdialogbuilder;
    private AlertDialog dialog;
    private LayoutInflater inflater;

    public RecyclerViewAdapter(Context context, List<Grocery> groceryList) {
        this.context = context;
        this.groceryList = groceryList;
    }

    @Override
    public RecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_row,parent,false);

        return new ViewHolder(view,context);
    }

    @Override
    public void onBindViewHolder(RecyclerViewAdapter.ViewHolder holder, int position) {

        Grocery grocery = groceryList.get(position);

        holder.groceryItemName.setText(grocery.getName());
        holder.quantity.setText(grocery.getQuantity());
        holder.dateAdded.setText(grocery.getDateItemAdded());


    }

    @Override
    public int getItemCount() {
        return groceryList.size();
    }



    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        public TextView groceryItemName;
        public TextView quantity;
        public TextView dateAdded;
        public Button editButton;
        public Button deleteButton;
        public int id;

        public ViewHolder(View view, final Context ctx) {
            super(view);

            context = ctx;

            groceryItemName = (TextView) view.findViewById(R.id.name);
            quantity = (TextView) view.findViewById(R.id.quantity);
            dateAdded = (TextView) view.findViewById(R.id.dateAdded);

            editButton = (Button) view.findViewById(R.id.editButton);
            deleteButton = (Button) view.findViewById(R.id.deleteButton);

            editButton.setOnClickListener(this);
            deleteButton.setOnClickListener(this);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //TODO: Goto NEXT SCREEN i.e. Details Actvity

                    int position = getAdapterPosition();

                    Grocery grocery = groceryList.get(position);

                    Intent intent = new Intent(context, DetailsActivity.class);
                    intent.putExtra("name",grocery.getName());
                    intent.putExtra("quantity",grocery.getQuantity());
                    intent.putExtra("id",grocery.getId());
                    intent.putExtra("date",grocery.getDateItemAdded());

                    context.startActivity(intent);
                }
            });
        }

        @Override
        public void onClick(View view) {

            switch (view.getId()){
                case R.id.editButton:

                    int position = getAdapterPosition();
                    Grocery grocery = groceryList.get(position);

                    editItem(grocery);

                    break;

                case R.id.deleteButton:

                     position = getAdapterPosition();
                     grocery = groceryList.get(position);

                    deleteItem(grocery.getId());

                    break;
            }

        }

        public void deleteItem(final int id){

            //Create Alert Dialog

            alertdialogbuilder = new AlertDialog.Builder(context);
            inflater = LayoutInflater.from(context);
            View view = inflater.inflate(R.layout.confirmation_dialog,null);

            Button noButton = (Button) view.findViewById(R.id.noButton);
            Button yesButton = (Button) view.findViewById(R.id.yesButton);

            alertdialogbuilder.setView(view);
            dialog=alertdialogbuilder.create();
            dialog.show();

            noButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.dismiss();
                }
            });

            yesButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //Delete Item

                    DatabaseHandler db = new DatabaseHandler(context);
                    db.deleteGrocery(id);
                    groceryList.remove(getAdapterPosition());
                    notifyItemRemoved(getAdapterPosition());

                    dialog.dismiss();

                }
            });
        }

        public void editItem(final Grocery grocery){

            alertdialogbuilder =new AlertDialog.Builder(context);
            inflater = LayoutInflater.from(context);
            final View view = inflater.inflate(R.layout.popup,null);

            final EditText groceryItem = (EditText) view.findViewById(R.id.grocery_item);
            final EditText quantity = (EditText) view.findViewById(R.id.groceryQty);
            final TextView title = (TextView) view.findViewById(R.id.title);
            title.setText("Edit Grocery");
            groceryItem.setText(grocery.getName().toString());
            quantity.setText(grocery.getQuantity().toString());

            Button savebtn = (Button) view.findViewById(R.id.saveButton);

            alertdialogbuilder.setView(view);
            dialog = alertdialogbuilder.create();
            dialog.show();

            savebtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    DatabaseHandler db = new DatabaseHandler(context);

                    //Update Item

                    grocery.setName(groceryItem.getText().toString());
                    grocery.setQuantity(""+quantity.getText().toString());


                    if (!groceryItem.getText().toString().isEmpty()
                            && !quantity.getText().toString().isEmpty()){
                        db.updateGrocery(grocery);

                        notifyItemChanged(getAdapterPosition(),grocery);
                        dialog.dismiss();
                    }

                    else {
                        Snackbar.make(view, "Add Grocery and Quantity",Snackbar.LENGTH_LONG).show();
                    }
                }
            });
        }
    }
}
