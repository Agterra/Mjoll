package fr.company.agterra.mjoll;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

public class GroupListFragment extends Fragment {

    private DatabaseReference databaseReference;

    private GroupsListArrayAdapter groupsListArrayAdapter;

    private ArrayList<String> groupNames;

    private ListView groupNameList;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_group_list, container, false);

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {

        super.onActivityCreated(savedInstanceState);

        this.groupNames = new ArrayList<>();

        FirebaseDatabase database = FirebaseDatabase.getInstance();

        this.databaseReference = database.getReference("GoupItems");

        ConnectivityManager connectivityManager = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);

        final NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        ListView groupNamesList = (ListView) getView().findViewById(R.id.groupList);

        GroupsListArrayAdapter adapter = new GroupsListArrayAdapter(getContext(), R.layout.group_cell, this.groupNames);

        this.groupsListArrayAdapter = adapter;

        this.groupNameList = groupNamesList;

        this.groupNameList.setAdapter(groupsListArrayAdapter);

        if(networkInfo != null)
        {

            databaseReference.addValueEventListener(new ValueEventListener() {

                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    groupNames.clear();

                    for (DataSnapshot data : dataSnapshot.getChildren())
                    {

                        String item = data.getValue(String.class);

                        groupNames.add(item);

                    }

                    groupsListArrayAdapter.notifyDataSetChanged();

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                    System.out.println("Error");

                }

            });

        }

    }
}
