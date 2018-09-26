package in.org.eonline.eblog.Fragments;


import android.content.Context;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.io.Console;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import in.org.eonline.eblog.HomeActivity;
import in.org.eonline.eblog.Models.BlogModel;
import in.org.eonline.eblog.Models.UserModel;
import in.org.eonline.eblog.R;
import in.org.eonline.eblog.SQLite.DatabaseHelper;

import static android.content.ContentValues.TAG;
import static in.org.eonline.eblog.Fragments.MyProfileFragment.MyPREFERENCES;

/**
 * A simple {@link Fragment} subclass.
 */
public class CreateNewBlogFragment extends Fragment  {
    FirebaseFirestore db;
    private EditText blogHeaderEdit;
    private EditText blogContentEdit;
    private EditText blogFooterEdit;
    //private AdView mAdView;
    //private EditText bannerAdIdEdit;
    private Button submitButton;
    private String bannerAdId;
    private Spinner spinner;
    private String item;
    DatabaseHelper sqliteDatabaseHelper;
    //private String blogId = "Aditya7506640685";
    BlogModel blogmodel = new BlogModel();

    UserModel userModel;
    Map<String, String> blogMap = new HashMap<>();
    private SharedPreferences sharedpreferences;
    public static final String MyPREFERENCES = "MyPrefs_new" ;
    private String userId;
    private String blogId;
    private String blogIdBase;
    private SharedPreferences.Editor editor;


    public CreateNewBlogFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_create_new_blog, container, false);

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        sqliteDatabaseHelper = new DatabaseHelper(getActivity());

        initializeViews();
        db = FirebaseFirestore.getInstance();
       // blogId = blogId+1;
        sharedpreferences = getActivity().getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        userId = sharedpreferences.getString("UserIdCreated","AdityaKamat75066406850");
        blogIdBase = userId+"_0";

        blogId = sharedpreferences.getString("blogId_new",blogIdBase);

        setSpinner();


        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //  adMobUnitId = adMobAdUnitId.getText().toString();
                //mAdView = new AdView(getActivity());

                //bannerAdId = bannerAdIdEdit.getText().toString();
                blogmodel.setBlogHeader(blogHeaderEdit.getText().toString());
                blogmodel.setBlogContent(blogContentEdit.getText().toString());
                blogmodel.setBlogFooter(blogFooterEdit.getText().toString());
                blogmodel.setBlogLikes(0);
                blogmodel.setBlogUser("Aditya Kamat");
                blogMap.put("BlogHeader",blogmodel.getBlogHeader());
                blogMap.put("BlogContent",blogmodel.getBlogContent());
                blogMap.put("BlogFooter", blogmodel.getBlogFooter());
                blogMap.put("BlogCategory",item);
                blogMap.put("BlogUser", "Aditya Kamat");
                blogMap.put("BlogLikes", String.valueOf(blogmodel.getBlogLikes()));



                addData();

               /* mAdView.setAdSize(AdSize.BANNER);
                mAdView.setAdUnitId(bannerAdId);
               AdRequest adRequest = new AdRequest.Builder().addTestDevice("551B158596C8082704941DE131CEBAB9").build();
               if
                       (bannerAdId != null) mAdView.setVisibility(view.VISIBLE);
                mAdView.loadAd(adRequest); */
            }

        });
    }

    public void addBlogToSQLite() {
      //  blogId = blogId+"_0";
        String  localblogId = blogId.substring(blogId.length()-1,  blogId.length() );
        int integer = Integer.parseInt(localblogId)+ 1;
        localblogId = Integer.toString(integer);
        blogId = blogId.substring(0, blogId.length() - 1);
        blogId = blogId + localblogId;
        blogmodel.setBlogId(blogId);
        blogMap.put("BlogId",blogmodel.getBlogId());
        Boolean blogIdInserted =  sqliteDatabaseHelper.insertBlogDataInSQLite(blogId,blogmodel.getBlogHeader(),blogmodel.getBlogContent(),blogmodel.getBlogFooter());
        if (blogIdInserted)
        {
            Toast.makeText(getContext(), "BlogId inserted properly in SQLite", Toast.LENGTH_SHORT).show();
        }
        else
        {
            Toast.makeText(getContext(), "BlogId insertion failed", Toast.LENGTH_SHORT).show();
        }
        editor = sharedpreferences.edit();
        editor.putString("blogId_new",blogId);
        editor.apply();
    }


    public void addData() {

        db.collection("Users").document(userId).collection("Blogs").document(blogId).set(blogMap, SetOptions.merge())
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(getActivity(), "New Blog is created", Toast.LENGTH_LONG).show();
                        addBlogToSQLite();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getActivity(), "Some error occured while creating new blog", Toast.LENGTH_LONG).show();
                    }
                });
        db.collection("Blogs").document(blogId).set(blogMap, SetOptions.merge())
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(getActivity(), "New Blog is created", Toast.LENGTH_LONG).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getActivity(), "Some error occured while creating new blog", Toast.LENGTH_LONG).show();
                    }
                });
    }



    public void initializeViews() {
        blogHeaderEdit = (EditText) getView().findViewById(R.id.blog_header);
        blogContentEdit = (EditText) getView().findViewById(R.id.blog_content);
        blogFooterEdit = (EditText) getView().findViewById(R.id.blog_footer);
        //mAdView = (AdView) getView().findViewById(R.id.adView_user_ad);
        submitButton = (Button) getView().findViewById(R.id.submit_blog_button);
        //bannerAdIdEdit = (EditText) getView().findViewById(R.id.adview_user_id);
        spinner = (Spinner) getView().findViewById(R.id.spinner_category);


    }

    public void setSpinner() {
        List<String> categories = new ArrayList<>();
        categories.add("Travelling");
        categories.add("Food");
        categories.add("Cosmetics");
        categories.add("Apparels");
        categories.add("Technology");
        categories.add("Cars and Bikes");
        categories.add("Politics");
        categories.add("Socialism");
        categories.add("Bollywood and entertainment");
        categories.add("Business");
        categories.add("others");
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, categories);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        spinner.setAdapter(dataAdapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                item = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }
}









