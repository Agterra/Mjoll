package fr.company.agterra.mjoll;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.IntegerRes;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class ListActivity extends AppCompatActivity {

    private ArrayList<Item> products;

    private ListView listView;

    private ArrayAdapter<Item> itemArrayAdapter;

    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_list);

        FirebaseDatabase database = FirebaseDatabase.getInstance();

        this.databaseReference = database.getReference("Items");

        this.products = new ArrayList<>();

        Button addButton = (Button) findViewById(R.id.addObject);

        ListView listView = (ListView) findViewById(R.id.listView);

        this.listView = listView;

        this.itemArrayAdapter = new ProductsAdapter(ListActivity.this, R.layout.inventory_cell, products, databaseReference);

        listView.setAdapter(itemArrayAdapter);

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder builder = new AlertDialog.Builder(ListActivity.this);

                builder.setTitle("Nom de l'objet");

                final EditText nameField = new EditText(ListActivity.this);

                nameField.setInputType(InputType.TYPE_CLASS_TEXT);

                builder.setView(nameField);

                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        Item item = new Item(nameField.getText().toString());

                        products.add(item);

                        Map<String, Object> map = new HashMap<String, Object>();

                        for (int i = 0; i < products.size(); i++)
                        {

                            map.put(String.valueOf(i), products.get(i));

                        }

                        databaseReference.updateChildren(map);

                    }

                });

                builder.setNegativeButton("Annuler", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        dialog.cancel();

                    }

                });

                builder.show();

                itemArrayAdapter.notifyDataSetChanged();

            }
        });

        databaseReference.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                products.clear();

                for (DataSnapshot data : dataSnapshot.getChildren())
                {

                    Item item = data.getValue(Item.class);

                    products.add(item);

                }

                System.out.println("-----\nData changed: "+ products + "\n------");

                itemArrayAdapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

                System.out.println("Error");

            }

        });

    }

}
