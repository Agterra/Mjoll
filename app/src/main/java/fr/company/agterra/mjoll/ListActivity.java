package fr.company.agterra.mjoll;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
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
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Array;
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

    public static String fileName = "itemsFile";

    @Override
    protected void onCreate(final Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_list);

        FirebaseDatabase database = FirebaseDatabase.getInstance();

        this.databaseReference = database.getReference("Items");

        this.products = loadItemsFile();

        if(this.products == null) {

            this.products = new ArrayList<>();

        }

        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        final NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

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

                        System.out.println("Item saved");

                        saveItemsFile(products);

                        itemArrayAdapter.notifyDataSetChanged();

                         if(networkInfo != null)
                        {

                            Map<String, Object> map = new HashMap<String, Object>();

                            for (int i = 0; i < products.size(); i++)
                            {

                                map.put(String.valueOf(i), products.get(i));

                            }

                            databaseReference.updateChildren(map);
                        }

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

        itemArrayAdapter.notifyDataSetChanged();

        if(networkInfo != null)
        {

            Map<String, Object> map = new HashMap<String, Object>();

            for (int i = 0; i < products.size(); i++)
            {

                map.put(String.valueOf(i), products.get(i));

            }

            databaseReference.updateChildren(map);

            databaseReference.addValueEventListener(new ValueEventListener() {

                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    products.clear();

                    for (DataSnapshot data : dataSnapshot.getChildren())
                    {

                        Item item = data.getValue(Item.class);

                        products.add(item);

                    }

                    saveItemsFile(products);

                    System.out.println("-----\nData changed: "+ products + "\n------");

                    itemArrayAdapter.notifyDataSetChanged();

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                    System.out.println("Error");

                }

            });

        }
        else
        {

            loadItemsFile();

            itemArrayAdapter.notifyDataSetChanged();

        }

    }

    private ArrayList<Item> loadItemsFile()
    {

        ArrayList<Item> itemsList = new ArrayList<>();

        try
        {

            File itemsFile = new File(this.getFilesDir(), fileName);

            FileInputStream fileInputStream = new FileInputStream(itemsFile);

            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);

            Item currentItem = (Item)objectInputStream.readObject();

            while(currentItem != null)
            {

                itemsList.add(currentItem);

                currentItem = (Item)objectInputStream.readObject();

                System.out.println("current item: " + currentItem);

            }

            //this.products.addAll(itemsList);

            objectInputStream.close();

            fileInputStream.close();

            System.out.println("----\nItems loaded...\n----");

        }
        catch (Exception e)
        {

            System.out.println("Error: " +e.getMessage());

            //Toast.makeText(getApplicationContext(), "Error : " + e.getMessage(), Toast.LENGTH_SHORT).show();

        }

        return itemsList;

    }

    private void saveItemsFile(ArrayList<Item> objects)
    {

        try
        {

            File itemsFile = new File(this.getFilesDir(), fileName);

            FileOutputStream fileOutputStream = new FileOutputStream(itemsFile);

            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);

            for(Item currentItem : objects)
            {

                objectOutputStream.writeObject(currentItem);

            }

            System.out.println("----\nItems saved...\n"+ objects+"\n----");

            objectOutputStream.close();

            fileOutputStream.close();

        }
        catch (Exception e)
        {

            System.out.println("Error: " +e.getMessage());

           // Toast.makeText(getApplicationContext(), "Error : " + e.getMessage(), Toast.LENGTH_SHORT).show();

        }
    }

}
