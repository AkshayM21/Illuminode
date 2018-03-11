package akshaym.stortree;

import android.app.ActionBar;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class NavActivity extends AppCompatActivity {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;

    private String[] archived;

    private int i;

    private int numArch;

    private ArrayList<String> list;

    private DatabaseReference myRef;
    private ValueEventListener clive;

    private String[] strs;

    private boolean flag = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nav);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.

        // Specify that tabs should be displayed in the action bar.
        // actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        // Create a tab listener that is called when the user changes tabs.
       /* ActionBar.TabListener tabListener = new ActionBar.TabListener() {
            @Override
            public void onTabSelected(ActionBar.Tab tab, FragmentTransaction ft) {
                mViewPager.setCurrentItem(tab.getPosition());
            }

            public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction ft) {
                // hide the given tab
            }

            public void onTabReselected(ActionBar.Tab tab, FragmentTransaction ft) {
                // probably ignore this event
            }
        };

            actionBar.addTab(
                    actionBar.newTab()
                            .setText("Archived")
                            .setTabListener(tabListener));
        actionBar.addTab(
                actionBar.newTab()
                        .setText("Current")
                        .setTabListener(tabListener));
*/

        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
      /*  mViewPager.setOnPageChangeListener(
                new ViewPager.SimpleOnPageChangeListener() {
                    @Override
                    public void onPageSelected(int position) {
                        // When swiping between pages, select the
                        // corresponding tab.
                        getActionBar().setSelectedNavigationItem(position);
                    }
                });*/
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tl = (TabLayout) findViewById(R.id.tabLayout);

        tl.setupWithViewPager(mViewPager);


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_nav, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            if (position == 0) { //its archived
                return CardFragment.newInstance(position, getNums("archived/num.txt"), getArchived("archived"));
            }

            CardFragment cf = CardFragment.newInstance(position, new String[]{"Current"}, new String[]{""});
            return cf;
        }


        //for retrieving nums of the archived ones
        public String[] getNums(String path) {

            StorageReference storageRef = FirebaseStorage.getInstance().getReference().child(path);

            try {
                /*Task<byte[]> temp = storageRef.getBytes(1000000).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                    @Override
                    public void onSuccess(byte[] bytes) {
                        numArch = Integer.parseInt(new String(bytes));
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        exception.printStackTrace();
                    }
                });*/
                Task<byte[]> temp = storageRef.getBytes(1000000);
                while (!temp.isComplete()) {
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                numArch = Integer.parseInt(new String(temp.getResult()));
            } catch (NullPointerException npe) {
                numArch = 0;
            }
            String[] nums = new String[numArch];
            for (int i = 1; i < numArch + 1; i++) {
                nums[i - 1] = "#" + i;

            }
            return nums;
        }

        public String[] getArchived(String path) {

            StorageReference storageRef = FirebaseStorage.getInstance().getReference().child(path + "/num.txt");
            try {
                /*Task<byte[]> temp = storageRef.getBytes(1000000).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                    @Override
                    public void onSuccess(byte[] bytes) {
                        numArch = Integer.parseInt(new String(bytes));
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        exception.printStackTrace();
                    }
                });*/
                Task<byte[]> temp = storageRef.getBytes(1000000);
                while (!temp.isComplete()) {
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                numArch = Integer.parseInt(new String(temp.getResult()));
            } catch (NullPointerException npe) {
                numArch = 0;
            }
            archived = new String[numArch];
            Task<byte[]> temp;
            for (i = 1; i < numArch + 1; i++) {
                storageRef = FirebaseStorage.getInstance().getReference().child(path + "/arch" + i + ".txt");
                temp = storageRef.getBytes(1000000);
                while (!temp.isComplete()) {
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                archived[i - 1] = new String(temp.getResult());

            }
            return archived;
        }

        public int getCount() {
            // Show 3 total pages.
            return 2;
        }

        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "Archived";
                case 1:
                    return "Current";
            }
            return null;
        }
    }

}