package fr.company.agterra.mjoll;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class PublicListFragment extends Fragment {

    private ArrayList<Item> products;

    private ListView listView;

    private ArrayAdapter<Item> itemArrayAdapter;

    private DatabaseReference databaseReference;

    public static String fileName = "itemsFile";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_public_list, container, false);

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {

        super.onActivityCreated(savedInstanceState);

        FirebaseDatabase database = FirebaseDatabase.getInstance();

        this.databaseReference = database.getReference("Items");

        this.products = loadItemsFile();

        if(this.products == null) {

            this.products = new ArrayList<>();

        }

        ConnectivityManager connectivityManager = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);

        final NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        Button addButton = (Button) getView().findViewById(R.id.addObject);

        ListView listView = (ListView) getView().findViewById(R.id.listView);

        this.listView = listView;

        this.itemArrayAdapter = new ProductsAdapter(getActivity(), R.layout.inventory_cell, products, databaseReference);

        listView.setAdapter(itemArrayAdapter);

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

                builder.setTitle("Nom de l'objet");

                final EditText nameField = new EditText(getActivity());

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

            File itemsFile = new File(getActivity().getFilesDir(), fileName);

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

            File itemsFile = new File(getActivity().getFilesDir(), fileName);

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
