package fr.company.agterra.mjoll;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by agterra on 28/10/2017.
 */

public class GroupsListArrayAdapter extends ArrayAdapter {

    ArrayList<String> groupNamesList;

    public GroupsListArrayAdapter(@NonNull Context context, @LayoutRes int resource, ArrayList<String> groupNamesList) {

        super(context, resource);

        this.groupNamesList = groupNamesList;

    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View view = convertView;

        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        view = inflater.inflate(R.layout.group_cell, null);

        TextView groupName = view.findViewById(R.id.groupName);

        groupName.setText(this.groupNamesList.get(position));

        return convertView;

    }
}
