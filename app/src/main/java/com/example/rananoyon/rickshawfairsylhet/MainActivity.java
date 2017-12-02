package com.example.rananoyon.rickshawfairsylhet;

import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity implements Time.OnFragmentInteractionListener,
        FixedDestination.OnFragmentInteractionListener,
        Meter.OnFragmentInteractionListener,
        DistanceBased.OnFragmentInteractionListener
{

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
    // [START declare_auth]
  //  private FirebaseAuth mAuth;
// [END declare_auth]
    private int fair;
private static final String TAG = "AnonymousAuth";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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

        // [START initialize_auth]
       // mAuth = FirebaseAuth.getInstance();
// [END initialize_auth
        // Check if user is signed in (non-null) and update UI accordingly.
      //  FirebaseUser currentUser = mAuth.getCurrentUser();
//        if(currentUser == null) {
//            //Sign in anonymously
//
//            mAuth.signInAnonymously()
//                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
//                        @Override
//                        public void onComplete(@NonNull Task<AuthResult> task) {
//                            if (task.isSuccessful()) {
//                                // Sign in success, update UI with the signed-in user's information
//                                Log.d(TAG, "signInAnonymously:success");
//                                FirebaseUser user = mAuth.getCurrentUser();
//                                Toast.makeText(MainActivity.this, "Signed In Successfully",
//                                        Toast.LENGTH_SHORT).show();
//                                setFairPrice();
//
//                            } else {
//                                // If sign in fails, display a message to the user.
//                                Log.w(TAG, "signInAnonymously:failure", task.getException());
//                                Toast.makeText(MainActivity.this, "Authentication failed.",
//                                        Toast.LENGTH_SHORT).show();
//
//                            }
//
//
//                        }
//                    });
//
//
//        } else if (currentUser!=null){
//            setFairPrice();
//        }


    }

//    private void setFairPrice() {
//
//        FirebaseDatabase database = FirebaseDatabase.getInstance();
//        DatabaseReference myRef = database.getReference("rickshaw/fair");
//        //myRef.setValue("50");//This line is for setting
//        myRef.setValue(50);
//    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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

    @Override
    public void onFragmentInteraction(Uri uri) {

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
          switch (position){
              case 0:
                  Time timeFragment = new Time();
                  return timeFragment;
              case 1:
                  FixedDestination fixedFairFragment = new FixedDestination();
                  return fixedFairFragment;
              case 2:
                    DistanceBased distanceBasedFragment = new DistanceBased();
                  return  distanceBasedFragment;
              case 3:

                  Meter meterFragment = new Meter();
                  return meterFragment;

          }
          return  null;
        }

        @Override
        public int getCount() {
            // Show 4 total pages.
            return 4;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "Time Based";
                case 1:
                    return "Fixed Fair ";
                case 2:
                    return "Distance Based Fair";
                case 3:
                    return "On Meter";
            }
            return null;
        }
    }

}
