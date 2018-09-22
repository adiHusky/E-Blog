package in.org.eonline.eblog.Fragments;


import android.content.Context;
import android.content.SharedPreferences;
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
    private AdView mAdView;
    private EditText bannerAdIdEdit;
    private Button submitButton;
    private String bannerAdId;
    private Spinner spinner;
    private String item;
    //private String blogId = "Aditya7506640685";
    BlogModel blogmodel = new BlogModel();
    UserModel userModel;
    Map<String, String> blogMap = new HashMap<>();
    private SharedPreferences sharedpreferences;
    public static final String MyPREFERENCES = "MyPrefs_new" ;
    private String userId;
    private String blogId;
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

        initializeViews();
        db = FirebaseFirestore.getInstance();
       // blogId = blogId+1;
        sharedpreferences = getActivity().getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        userId = sharedpreferences.getString("UserIdCreated","AdityaKamat75066406850");

        blogId = sharedpreferences.getString("blogId_new",userId);

        setSpinner();


        submitButton.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View view) {
                //  adMobUnitId = adMobAdUnitId.getText().toString();
                mAdView = new AdView(getActivity());

                bannerAdId = bannerAdIdEdit.getText().toString();
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

                mAdView.setAdSize(AdSize.BANNER);
                mAdView.setAdUnitId(bannerAdId);
               AdRequest adRequest = new AdRequest.Builder().addTestDevice("551B158596C8082704941DE131CEBAB9").build();
               if
                       (bannerAdId != null) mAdView.setVisibility(view.VISIBLE);
                mAdView.loadAd(adRequest);

            }

        });


    }

    public void addData() {

        String  localblogId = blogId.substring(blogId.length()-1,  blogId.length() );

        int integer = Integer.parseInt(localblogId)+ 1;
        localblogId = Integer.toString(integer);

        blogId = blogId.substring(0, blogId.length() - 1);


        blogId = blogId + localblogId;
        editor = sharedpreferences.edit();
        editor.putString("blogId_new",blogId);
        editor.apply();





        db.collection("Blogs").document(blogId).set(blogMap, SetOptions.merge())
                .addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.d(TAG, "DocumentSnapshot successfully written!");
            }
        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error writing document", e);



                    }
                });}



    public void initializeViews() {
        blogHeaderEdit = (EditText) getView().findViewById(R.id.blog_header);
        blogContentEdit = (EditText) getView().findViewById(R.id.blog_content);
        blogFooterEdit = (EditText) getView().findViewById(R.id.blog_footer);
        mAdView = (AdView) getView().findViewById(R.id.adView_user_ad);
        submitButton = (Button) getView().findViewById(R.id.submit_button);
        bannerAdIdEdit = (EditText) getView().findViewById(R.id.adview_user_id);
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


    ;
}









