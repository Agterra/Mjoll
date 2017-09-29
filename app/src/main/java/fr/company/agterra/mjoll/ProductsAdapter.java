package fr.company.agterra.mjoll;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Agterra on 08/09/2017.
 */

public class ProductsAdapter extends ArrayAdapter {

    private ArrayList<Item> objects;

    private DatabaseReference databaseReference;

    public ProductsAdapter(@NonNull Context context, @LayoutRes int resource, ArrayList<Item> objects, DatabaseReference databaseReference) {

        super(context, resource, objects);

        this.objects = objects;

        this.databaseReference = databaseReference;

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

        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        view = inflater.inflate(R.layout.inventory_cell, null);

        TextView productName = (TextView) view.findViewById(R.id.productNameTextView);

        productName.setText(this.objects.get(position).getName());

        TextView productNumber = (TextView) view.findViewById(R.id.productNumberTextView);

        productNumber.setText(String.valueOf(this.objects.get(position).getQuantity()));

        CheckBox checkBox = (CheckBox) view.findViewById(R.id.productCheck);

        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                if(b)
                {

                    objects.remove(position);

                    Map<String, Object> map = new HashMap<String, Object>();

                    databaseReference.updateChildren(map);

                    for (int i = 0; i < objects.size(); i++)
                    {

                        map.put(String.valueOf(i), objects.get(i));

                    }

                    databaseReference.setValue(objects);

                    notifyDataSetChanged();

                }

            }
        });

        Button addButton = view.findViewById(R.id.addButton);

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                objects.get(position).incrementNumber();

                Map<String, Object> map = new HashMap<String, Object>();

                for (int i = 0; i < objects.size(); i++)
                {

                    map.put(String.valueOf(i), objects.get(i));

                }

                databaseReference.updateChildren(map);

                notifyDataSetChanged();

            }

        });

        Button removeButton = view.findViewById(R.id.removeButton);

        removeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                objects.get(position).decrementNumber();

                Map<String, Object> map = new HashMap<String, Object>();

                for (int i = 0; i < objects.size(); i++)
                {

                    map.put(String.valueOf(i), objects.get(i));

                }

                databaseReference.updateChildren(map);

                notifyDataSetChanged();

            }

        });

        return view;

    }

    @Override
    public void notifyDataSetChanged() {

        super.notifyDataSetChanged();

    }
}
