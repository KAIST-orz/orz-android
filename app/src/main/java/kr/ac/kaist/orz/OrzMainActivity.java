package kr.ac.kaist.orz;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

public class OrzMainActivity extends AppCompatActivity {

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

    // The reference to AssignmentTabFragment to handle floating action button functionality.
    private AssignmentTabFragment assignmentTabFragment;

    private FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orz_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);

        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));

        // Add a new OnPageChangeListener to change fab action and icon.
        mViewPager.addOnPageChangeListener(new FabOnPageChangeListener());

        fab = (FloatingActionButton) findViewById(R.id.fab);

        // When starting the activity, it first shows assignments tab, thus
        // set appropriate icon and OnClickListener.
        fab.setImageResource(R.drawable.ic_swap_vert_black_24dp);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (assignmentTabFragment != null) {
                    System.out.println("Not null");
                    assignmentTabFragment.showSortingCriteriaDialog(OrzMainActivity.this);
                }
                System.out.println("Null");
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_orz_main, menu);
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
    public class SectionsPagerAdapter extends FragmentStatePagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    // Keep the reference to the Assignment Tab so that we can handle
                    // floating action button function (show sorting criteria dialog).
                    System.out.println("Do you reach here?");
                    assignmentTabFragment = AssignmentTabFragment.newInstance();
                    return assignmentTabFragment;

                case 1:
                    return CalendarTabFragment.newInstance();

                case 2:
                    return SettingsTabFragment.newInstance();

                default:
                    return null;
            }
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            super.destroyItem(container, position, object);
            // If destroyed fragment is the first one (Assignment tab), remove
            // reference to it.
            if (position == 0) {
                assignmentTabFragment = null;
            }
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 3;
        }
    }

    public void settings_account(View v) {
        Intent intent = new Intent(getApplicationContext(), AccountSettingsActivity.class);
        startActivity(intent);
    }

    public void settings_notification(View v) {
        Intent intent = new Intent(getApplicationContext(), NotificationSettingsActivity.class);
        startActivity(intent);
    }

    public void settings_myCourses(View v) {
        Intent intent = new Intent(getApplicationContext(), MyCoursesActivity.class);
        startActivity(intent);
    }

    // The OnPageChangeListener for the viewpager, to change the icon and function
    // of the floating action button according to the selected page.
    private class FabOnPageChangeListener implements ViewPager.OnPageChangeListener {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            // Do nothing.
        }

        @Override
        public void onPageSelected(int position) {
            // First remove existing onclick listener.
            fab.setOnClickListener(null);

            // Now apply appropriate icon and OnClickListener.
            switch (position) {
                case 0:
                    fab.show();
                    fab.setImageResource(R.drawable.ic_swap_vert_black_24dp);
                    fab.setOnClickListener(new View.OnClickListener() {
                        // TODO: Pop up dialog for selecting sorting criteria.
                        @Override
                        public void onClick(View view) {
                            if (assignmentTabFragment != null) {
                                assignmentTabFragment.showSortingCriteriaDialog(OrzMainActivity.this);
                            }
                        }
                    });
                    return;
                case 1:
                    fab.show();
                    fab.setImageResource(R.drawable.ic_add_black_24dp);
                    fab.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            // Start RegisterScheduleActivity.
                            Intent intent = new Intent(OrzMainActivity.this, RegisterScheduleActivity.class);
                            startActivity(intent);
                        }
                    });
                    return;
                case 2:
                    // Do not show FAB on settings page.
                default:
                    fab.hide();
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {
            // Do nothing.
        }
    }
}
