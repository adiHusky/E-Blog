package in.org.eonline.eblog;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.os.Bundle;
import android.os.Handler;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;

import in.org.eonline.eblog.Activities.BlogActivity;
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
    public String fragmentTag;
    private Boolean exit = false;
    ImageView imageView;
    View hView;
    private SharedPreferences sharedpreferences;
    public static final String MyPREFERENCES = "MyPrefs_new" ;
    private String isUserRegisteredAlready;
    private String isUserNamePresent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        sharedpreferences = this.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        isUserRegisteredAlready = sharedpreferences.getString("UserImagePath", "false");
        isUserNamePresent = sharedpreferences.getString("UserFirstName", "false");
        initializeViews();
      /*  Intent intent = getIntent();
        String s1 = intent.getStringExtra("Check");
try {
    if (s1.equals("1")) {
        s1 = "";
        android.support.v4.app.Fragment frg;
        frg = getSupportFragmentManager().findFragmentByTag("nav_home");
        final android.support.v4.app.FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.detach(frg);
        ft.attach(frg);
        ft.commit();

    }
}
catch(NullPointerException e){}*/
        fragmentTag="nav_home";
        openFragment(new HomeFragment(), "nav_home");
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
        getNavHeaderTextImage();

    }

    public void getNavHeaderTextImage(){
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        hView =  navigationView.getHeaderView(0);
        TextView nav_user = (TextView)hView.findViewById(R.id.nav_user_name);
        if(isUserNamePresent!="false")
        {
            nav_user.setText(sharedpreferences.getString("UserFirstName","EBlog User"));
        }
        else
        {
            nav_user.setText("EBlog User");
        }
        imageView = (ImageView)hView.findViewById(R.id.nav_imageView);
        //imageView.setImageResource();
        if(isUserRegisteredAlready!="false") {
            loadImageFromStorage(sharedpreferences.getString("UserImagePath","0"));
        }
        else{
            imageView.setImageResource(R.drawable.ic_user_dummy);
        }
    }


    private void loadImageFromStorage(String path)
    {

        try {
            File f=new File(path, "profile.jpg");
            Bitmap b = BitmapFactory.decodeStream(new FileInputStream(f));
            ImageView img=(ImageView)hView.findViewById(R.id.nav_imageView);
            img.setImageBitmap(b);
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }

    }

    // Back button click handled by this method
    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            android.support.v4.app.Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.content_frame);
            if (fragment != null) {
                if(fragment.getTag() == "nav_home") {
                    finish();
                    //System.exit(0);
                    //super.onBackPressed();
                    //finishAndRemoveTask();
                } else {
                    openFragment(new HomeFragment(), "nav_home");
                }
            } else {
                finish();
                //System.exit(0);
                //finishAndRemoveTask();
            }
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
            fragmentTag="nav_home";
            openFragment(homeFragment,fragmentTag);

        } else if (id == R.id.nav_new_blog) {
            CreateNewBlogFragment createNewBlogFragment = new CreateNewBlogFragment();
            fragmentTag="create_new_fragment";
            openFragment(createNewBlogFragment,fragmentTag);
        } else if (id == R.id.nav_blog_history) {
            YourBlogsFragment yourBlogsFragment = new YourBlogsFragment();
            fragmentTag="nav_blog_history";
            openFragment(yourBlogsFragment,fragmentTag);
        } else if (id == R.id.nav_monetize) {
            MonetizationFragment monetizationFragment = new MonetizationFragment();
            fragmentTag="nav_monetize";
            openFragment(monetizationFragment,fragmentTag);
        } else if (id == R.id.nav_task) {
            TaskFragment taskFragment = new TaskFragment();
            fragmentTag="nav_task";
            openFragment(taskFragment,fragmentTag);
        } else if (id == R.id.nav_profile) {
            MyProfileFragment myProfileFragment = new MyProfileFragment();
            fragmentTag="nav_profile";
            openFragment(myProfileFragment,fragmentTag);
        } else if (id == R.id.nav_logout) {
            FirebaseAuth.getInstance().signOut();
            Intent intent = new Intent(HomeActivity.this, Login.class);
            startActivity(intent);
        } else if (id == R.id.nav_about_us) {
            //TODO
        } else if (id == R.id.nav_tc) {
            TermsConditionsFragment termsConditionsFragment = new TermsConditionsFragment();
            fragmentTag="nav_tc";
            openFragment(termsConditionsFragment,fragmentTag);
        } else if (id == R.id.nav_report_bug){
            ReportBugFragment reportBugFragment = new ReportBugFragment();
            fragmentTag="nav_report_bug";
            openFragment(reportBugFragment,fragmentTag);
        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void openFragment(Fragment fragment, String fragmentTag) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.content_frame, fragment,fragmentTag);
        //fragmentTransaction.addToBackStack(fragmentTag);
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
