package fr.company.agterra.mjoll;

import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class PrivateListFragment extends Fragment {

    private ArrayList<Item> privateProducts;

    private ListView privateListView;

    private ArrayAdapter<Item> privateItemArrayAdapter;

    public static String privateFileName = "privateItemsFile";

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {

        super.onActivityCreated(savedInstanceState);

        Button addButton = (Button) getView().findViewById(R.id.addObject);

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

                         privateProducts.add(item);

                         System.out.println("Item saved");

                         savePrivateItemsFile(privateProducts);

                         privateItemArrayAdapter.notifyDataSetChanged();

                     }

                 });

                 builder.setNegativeButton("Annuler", new DialogInterface.OnClickListener() {
                     @Override
                     public void onClick(DialogInterface dialog, int which) {

                         dialog.cancel();

                     }

                 });

                 builder.show();

             }

         });

        //PRIVATE PART

        this.privateProducts = loadPrivateItemsFile();

        if(this.privateProducts == null)
        {

            this.privateProducts = new ArrayList<>();

        }

        ListView privateListView = (ListView)getView().findViewById(R.id.privateListView);

        this.privateListView = privateListView;

        this.privateItemArrayAdapter = new PrivateProductsAdapter(getActivity(), R.layout.inventory_cell, privateProducts);

        this.privateListView.setAdapter(this.privateItemArrayAdapter);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_private_list, container, false);

    }

    @Override
    public void onAttach(Context context) {

        super.onAttach(context);

    }

    @Override
    public void onDetach() {

        super.onDetach();

    }

    private ArrayList<Item> loadPrivateItemsFile()
    {

        ArrayList<Item> itemsList = new ArrayList<>();

        try
        {

            File itemsFile = new File(getActivity().getFilesDir(), privateFileName);

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

    private void savePrivateItemsFile(ArrayList<Item> objects)
    {

        try
        {

            File itemsFile = new File(getActivity().getFilesDir(), privateFileName);

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
