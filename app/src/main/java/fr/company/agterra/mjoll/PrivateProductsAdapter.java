package fr.company.agterra.mjoll;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by agterra on 30/09/2017.
 */

public class PrivateProductsAdapter extends ArrayAdapter {

    private ArrayList<Item> objects;

    public PrivateProductsAdapter(@NonNull Context context, @LayoutRes int resource, ArrayList<Item> objects) {

        super(context, resource, objects);

        this.objects = objects;

    }

    @Override
    public int getCount() {

        return this.objects.size();

    }

    @Override
    public int getPosition(@Nullable Object item) {

        return this.objects.indexOf(item);

    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View view = convertView;

        ConnectivityManager connectivityManager = (ConnectivityManager) this.getContext().getSystemService(Context.CONNECTIVITY_SERVICE);

        final NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        view = inflater.inflate(R.layout.inventory_cell, null);

        view.setBackgroundColor(Color.LTGRAY);

        EditText productName = (EditText) view.findViewById(R.id.productNameTextView);

        productName.setText(this.objects.get(position).getName());

        productName.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

                builder.setTitle("Nom de l'objet");

                final EditText nameField = new EditText(getContext());

                nameField.setInputType(InputType.TYPE_CLASS_TEXT);

                builder.setView(nameField);

                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        objects.get(position).setName(nameField.getText().toString());

                        savePrivateItemsFile(objects);

                        notifyDataSetChanged();

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

        TextView productNumber = (TextView) view.findViewById(R.id.productNumberTextView);

        productNumber.setText(String.valueOf(this.objects.get(position).getQuantity()));

        CheckBox checkBox = (CheckBox) view.findViewById(R.id.productCheck);

        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                if(b)
                {

                    objects.remove(position);

                    savePrivateItemsFile(objects);

                    notifyDataSetChanged();

                }

            }
        });

        Button addButton = view.findViewById(R.id.addButton);

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                objects.get(position).incrementNumber();

                savePrivateItemsFile(objects);

                notifyDataSetChanged();

            }

        });

        addButton.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {

                objects.get(position).incrementByTen();

                savePrivateItemsFile(objects);

                notifyDataSetChanged();

                Toast.makeText(getContext(), "Ajout de 10 éléments", Toast.LENGTH_SHORT).show();

                return true;

            }
        });

        Button removeButton = view.findViewById(R.id.removeButton);

        removeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                objects.get(position).decrementNumber();

                savePrivateItemsFile(objects);

                notifyDataSetChanged();

            }

        });

        removeButton.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {

                objects.get(position).decrementByTen();

                savePrivateItemsFile(objects);

                notifyDataSetChanged();

                Toast.makeText(getContext(), "Retrait de 10 éléménents", Toast.LENGTH_SHORT).show();

                return false;
            }
        });

        return view;

    }

    private void savePrivateItemsFile(ArrayList<Item> objects)
    {

        try
        {

            File itemsFile = new File(this.getContext().getFilesDir(), ListActivity.privateFileName);

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
