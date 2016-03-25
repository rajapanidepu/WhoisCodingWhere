package com.apps.whoiscodingwhere;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import com.anton46.collectionitempicker.CollectionPicker;
import com.anton46.collectionitempicker.Item;
import com.anton46.collectionitempicker.OnItemClickListener;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class TagsSelection extends AppCompatActivity {

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tags_selection);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);



    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_tags_selection, menu);
//        MenuItem item = menu.add(Menu.NONE, R.menu.menu_tags_selection, 0, "Save");
//        item.setIcon(R.drawable.com_facebook_button_icon);
//        item.setShowAsAction(MenuItem.SHOW_AS_ACTION_WITH_TEXT);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.nav_done) {
            Log.e("rpanidep",PlaceholderFragment.SECTION_NUMBER+" "+mViewPager.getCurrentItem());
            if( mViewPager.getCurrentItem()==0){
               mViewPager.setCurrentItem(1);

            }else{
                Intent i = new Intent(getApplicationContext(),MainActivity.class);
                startActivity(i);
                finish();
            }
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        public static final String ARG_SECTION_NUMBER = "section_number";
        public static int SECTION_NUMBER;
        public PlaceholderFragment() {
        }
        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            SECTION_NUMBER = sectionNumber;
            fragment.setArguments(args);
            return fragment;
        }

        List<Item> items = new ArrayList<>();
        Set<String> itemIdsSet = new HashSet<>();

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_tags_selection, container, false);
            TextView textView = (TextView) rootView.findViewById(R.id.section_label);
            textView.setText(getString(R.string.section_format, getArguments().getInt(ARG_SECTION_NUMBER)));
            SECTION_NUMBER = getArguments().getInt(ARG_SECTION_NUMBER);
            items.add(new Item("item1", "Items 1"));
            itemIdsSet.add("item1");
            items.add(new Item("item2", "Items 2"));
            itemIdsSet.add("item2");
            items.add(new Item("item3", "Items 3"));
            itemIdsSet.add("item3");
            items.add(new Item("item4", "Items 4"));
            itemIdsSet.add("item4");
            items.add(new Item("item5", "Items 5"));
            itemIdsSet.add("item5");

            final CollectionPicker picker = (CollectionPicker) rootView.findViewById(R.id.collection_item_picker);
            picker.setItems(items);

            picker.setOnItemClickListener(new OnItemClickListener() {
                @Override
                public void onClick(Item item, int position) {

                }
            });
            Button addTagButton = (Button) rootView.findViewById(R.id.addTag);
            final EditText inputTag = (EditText) rootView.findViewById(R.id.inputTag);

            addTagButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String newTag = inputTag.getText().toString();
                    if (newTag.equals("")) {
                        return;
                    }
                    if (!itemIdsSet.contains(newTag)) {
                        itemIdsSet.add(newTag);
                        items.add(new Item(newTag, newTag));
                        picker.drawItemView();
                        inputTag.setText("");
                    } else {
                        Toast.makeText(getContext(), "Already added !!", Toast.LENGTH_LONG).show();
                    }
                }
            });


            return rootView;
        }
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
            // Return a PlaceholderFragment (defined as a static inner class below).
            return PlaceholderFragment.newInstance(position + 1);
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "What do you know??";
                case 1:
                    return "What do you like to know??";
                case 2:
                    return "SECTION 3";
            }
            return null;
        }
    }
}
