package fr.company.agterra.mjoll;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.TextView;

import java.util.ArrayList;

public class MainMenuActivity extends AppCompatActivity {

    private TextView mTextMessage;

    private ArrayList<Fragment> fragmentViews;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {

            Fragment selectedFragment = null;

            switch (item.getItemId()) {

                case R.id.navigation_public:

                    selectedFragment = fragmentViews.get(0);

                    break;

                case R.id.navigation_private:

                    selectedFragment = fragmentViews.get(1);

                    break;

                case R.id.navigation_groups:

                    selectedFragment = fragmentViews.get(2);

                    break;

            }

            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

            transaction.replace(R.id.content, selectedFragment);

            transaction.commit();

            return true;

        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main_menu);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);

        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        ArrayList<Fragment> fragmentViews = new ArrayList<>();

        this.fragmentViews = fragmentViews;

        fragmentViews.add(new PublicListFragment());

        fragmentViews.add(new PrivateListFragment());

        fragmentViews.add(new GroupListFragment());

        //Manually displaying the first fragment - one time only
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        transaction.replace(R.id.content, new PublicListFragment());

        transaction.commit();

    }

}
