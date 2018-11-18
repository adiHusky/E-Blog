package in.org.eonline.eblog;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

import in.org.eonline.eblog.Activities.Login;
import in.org.eonline.eblog.Fragments.CreateNewBlogFragment;
import in.org.eonline.eblog.Fragments.HomeFragment;
import in.org.eonline.eblog.Fragments.MonetizationFragment;
import in.org.eonline.eblog.Fragments.MyProfileFragment;
import in.org.eonline.eblog.Fragments.ReportBugFragment;
import in.org.eonline.eblog.Fragments.TaskFragment;
import in.org.eonline.eblog.Fragments.TermsConditionsFragment;
import in.org.eonline.eblog.Fragments.YourBlogsFragment;

public class HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    FirebaseFirestore db;
    Map<String, Object> user = new HashMap<>();

    FrameLayout frameLayout;
    Toolbar toolbar;
    NavigationView navigationView;
    DrawerLayout drawer;
    GoogleSignInClient mGoogleSignInClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        initializeViews();
        openFragment(new HomeFragment());
        //getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, new HomeFragment(), "EXPLORE").commit();

        db = FirebaseFirestore.getInstance();

        setSupportActionBar(toolbar);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);

        // Create a new user with a first and last name

        user.put("first", "Viraj");
        user.put("last", "Jadhav");
        user.put("born", 1998);
        user.put("City", "Panvel");
        //addData();

        /* MobileAds.initialize(this,"ca-app-pub-7293397784162310~9840078574");
        mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder()
                  .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                  .addTestDevice("5DDD17EFB41CB40FC08FBE350D11B395").build();
        mAdView.loadAd(adRequest); */

    }

    public void initializeViews() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        frameLayout = (FrameLayout) findViewById(R.id.content_frame);
    }

    // Back button click handled by this method
    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    /* @Override
    protected void onPause() {
        //Toast.makeText(HomeActivity.this, "on pause is called", Toast.LENGTH_SHORT).show();
        mAdView.pause();
        super.onPause();
    }

    @Override
    protected void onResume() {
        //Toast.makeText(HomeActivity.this, "on resume is called", Toast.LENGTH_SHORT).show();
        mAdView.resume();
        super.onResume();
    } */

   /* @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
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
    } */

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            HomeFragment homeFragment = new HomeFragment();
            openFragment(homeFragment);
        } else if (id == R.id.nav_new_blog) {
            CreateNewBlogFragment createNewBlogFragment = new CreateNewBlogFragment();
            openFragment(createNewBlogFragment);
        } else if (id == R.id.nav_blog_history) {
            YourBlogsFragment yourBlogsFragment = new YourBlogsFragment();
            openFragment(yourBlogsFragment);
        } else if (id == R.id.nav_monetize) {
            MonetizationFragment monetizationFragment = new MonetizationFragment();
            openFragment(monetizationFragment);
        } else if (id == R.id.nav_task) {
            TaskFragment taskFragment = new TaskFragment();
            openFragment(taskFragment);
        } else if (id == R.id.nav_profile) {
            MyProfileFragment myProfileFragment = new MyProfileFragment();
            openFragment(myProfileFragment);
        } else if (id == R.id.nav_logout) {
            FirebaseAuth.getInstance().signOut();
            Intent intent = new Intent(HomeActivity.this, Login.class);
            startActivity(intent);
        } else if (id == R.id.nav_about_us) {
            //TODO
        } else if (id == R.id.nav_tc) {
            TermsConditionsFragment termsConditionsFragment = new TermsConditionsFragment();
            openFragment(termsConditionsFragment);
        } else if (id == R.id.nav_report_bug){
            ReportBugFragment reportBugFragment = new ReportBugFragment();
            openFragment(reportBugFragment);
        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void openFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.content_frame, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    public void addData() {
        db.collection("users")
                .add(user)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Toast.makeText(HomeActivity.this, "Added Successfully" + documentReference.getId(), Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(HomeActivity.this, "Error", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
