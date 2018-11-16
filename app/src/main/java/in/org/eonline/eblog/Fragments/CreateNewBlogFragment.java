package in.org.eonline.eblog.Fragments;


import android.content.Context;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.media.Image;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
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
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
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
import static android.graphics.Color.RED;
import static android.graphics.Color.blue;
import static in.org.eonline.eblog.Fragments.MyProfileFragment.MyPREFERENCES;

/**
 * A simple {@link Fragment} subclass.
 */
public class CreateNewBlogFragment extends Fragment  {
    FirebaseFirestore db;
    private EditText blogHeaderEdit;
    private EditText blogContentEdit1;
    private EditText blogContentEdit2;
    private ImageView blogImageView1;
    private ImageView blogImageView2;
    private TextView errorHeader;
    private TextView errorContent1;
    private TextView errorContent2;
    private TextView errorFooter;
    private ImageView errorImage;
    private ImageView errorImage1;
    private ImageView errorImage2;
    private ImageView errorImage3;
    private EditText blogFooterEdit;
    private Button submitButton;
    private Spinner spinner;
    private String item;
    DatabaseHelper sqliteDatabaseHelper;
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

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_create_new_blog, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initializeViews();
        sqliteDatabaseHelper = new DatabaseHelper(getActivity());
        db = FirebaseFirestore.getInstance();
        sharedpreferences = getActivity().getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        userId = sharedpreferences.getString("UserIdCreated","AdityaKamat75066406850");
        blogIdBase = userId + "_0";
        blogId = sharedpreferences.getString("blogId_new",blogIdBase);
        setSpinner();
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setVisibilityGone();
                getUserBannerIdAndUserImageUrl();
            }
        });
    }

    public void addBlogToSQLite() {
        Boolean blogIdInserted =  sqliteDatabaseHelper.insertBlogDataInSQLite(blogId,blogmodel.getBlogHeader(),blogmodel.getBlogContent1(),blogmodel.getBlogFooter());
        if (blogIdInserted) {
            Toast.makeText(getContext(), "BlogId inserted properly in SQLite", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getContext(), "BlogId insertion failed", Toast.LENGTH_SHORT).show();
        }
    }

    public void addData() {
        db.collection("Users").document(userId).collection("Blogs").document(blogId).set(blogMap, SetOptions.merge())
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(getActivity(), "New Blog is created in Users collection", Toast.LENGTH_LONG).show();
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
                        Toast.makeText(getActivity(), "New Blog is created in standalone Blogs collection", Toast.LENGTH_LONG).show();
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
        blogContentEdit1 = (EditText) getView().findViewById(R.id.blog_content1);
        blogContentEdit2 = (EditText) getView().findViewById(R.id.blog_content2);
        blogImageView1 = (ImageView) getView().findViewById(R.id.blog_image_1);
        blogImageView2= (ImageView) getView().findViewById(R.id.blog_image_2);
        blogFooterEdit = (EditText) getView().findViewById(R.id.blog_footer);
        submitButton = (Button) getView().findViewById(R.id.submit_blog_button);
        //bannerAdIdEdit = (EditText) getView().findViewById(R.id.adview_user_id);
        spinner = (Spinner) getView().findViewById(R.id.spinner_category);
        errorHeader = (TextView) getView().findViewById(R.id.error_header);
        errorContent1=(TextView) getView().findViewById(R.id.error_content1);
        errorContent2=(TextView) getView().findViewById(R.id.error_content2);
        errorFooter=(TextView) getView().findViewById(R.id.error_footer);
        errorImage=(ImageView) getView().findViewById(R.id.error_image);
        errorImage1=(ImageView) getView().findViewById(R.id.error_image1);
        errorImage2=(ImageView) getView().findViewById(R.id.error_image2);
        errorImage3=(ImageView) getView().findViewById(R.id.error_image3);
        setVisibilityGone();
    }
    public void setVisibilityGone(){
        errorImage.setVisibility(View.GONE);
        errorImage.setVisibility(View.GONE);
        errorImage1.setVisibility(View.GONE);
        errorImage2.setVisibility(View.GONE);
        errorImage3.setVisibility(View.GONE);
        errorHeader.setVisibility(View.GONE);
        errorContent1.setVisibility(View.GONE);
        errorContent2.setVisibility(View.GONE);
        errorFooter.setVisibility(View.GONE);
    }
    public void getUserBannerIdAndUserImageUrl(){
        DocumentReference docRef = db.collection("Users").document(userId);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {  //ToDo: this is wrong condition, need to change
                        try {
                            blogmodel.setBannerAdMobId(document.get("UserBannerId").toString());
                        }
                        catch(NullPointerException e) {
                            Toast.makeText(getActivity(), "Please enter banner ID", Toast.LENGTH_LONG).show();
                        }
                        blogmodel.setUserImageUrl(document.get("UserImageUrl").toString());
                      if(blogmodel.getBannerAdMobId()!=null && blogmodel.getUserImageUrl()!=null ) {
                          setBlogModelAndMap();

                      }else{
                          Toast.makeText(getActivity(), "Please enter banner ID", Toast.LENGTH_LONG).show();
                      }
                       // Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                    } else {
                        Log.d(TAG, "No such document");
                        Toast.makeText(getActivity(), "Please enter banner ID", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });
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
    public void setBlogModelAndMap(){

        blogmodel.setBlogHeader(blogHeaderEdit.getText().toString());
        blogmodel.setBlogContent1(blogContentEdit1.getText().toString());
        blogmodel.setBlogContent2(blogContentEdit2.getText().toString());
        blogmodel.setBlogFooter(blogFooterEdit.getText().toString());
        blogmodel.setBlogLikes("0");
        blogmodel.setUserBlogImage1Url("https://firebasestorage.googleapis.com/v0/b/eblog-88c43.appspot.com/o/Users%2Fadikamat80827130040?alt=media&token=02cebd83-8869-4f29-9016-5cd97f22c878");
        blogmodel.setUserBlogImage2Url("https://firebasestorage.googleapis.com/v0/b/eblog-88c43.appspot.com/o/Users%2Fanantjadhav.8355%40gmail.com?alt=media&token=b5c9812c-0cc9-452f-aab5-82a27eaac9c1");
        validateData(blogmodel);
    }

    public void validateData(BlogModel blogModel){

      if(blogmodel.getBlogHeader().toString().equals("")||blogModel.getBlogHeader().toString().length()<=7  ) {
          errorHeader.setVisibility(View.VISIBLE);
          errorImage.setVisibility(View.VISIBLE);
      }
      if(blogModel.getBlogContent1().toString().equals("")||blogModel.getBlogContent1().toString().length()<=100) {
          errorContent1.setVisibility(View.VISIBLE);
          errorImage1.setVisibility(View.VISIBLE);
      }
      if(blogModel.getBlogContent2().toString().equals("")||blogModel.getBlogContent2().toString().length()<=100) {
          errorContent2.setVisibility(View.VISIBLE);
          errorImage2.setVisibility(View.VISIBLE);
      }
      if(blogModel.getBlogFooter().toString().equals("") ) {
                      errorFooter.setVisibility(View.VISIBLE);
                      errorImage3.setVisibility(View.VISIBLE);
      } else {
          blogMap.put("BlogHeader",blogmodel.getBlogHeader());
          blogMap.put("BlogContent1",blogmodel.getBlogContent1());
          blogMap.put("BlogContent2",blogmodel.getBlogContent2());
          blogMap.put("BlogFooter", blogmodel.getBlogFooter());
          blogMap.put("BlogCategory",item);
          blogMap.put("UserId",userId);
          blogMap.put("BlogLikes", String.valueOf(blogmodel.getBlogLikes()));
          blogMap.put("BlogUserBannerId",blogmodel.getBannerAdMobId());
          blogMap.put("BlogUserImageUrl",blogmodel.getUserImageUrl());
          blogMap.put("BlogImage1Url",blogmodel.getUserBlogImage1Url());
          blogMap.put("BlogImage2Url",blogmodel.getUserBlogImage2Url());
          String  localblogId = blogId.substring(blogId.length()-1,  blogId.length() );
          int integer = Integer.parseInt(localblogId)+ 1;
          localblogId = Integer.toString(integer);
          blogId = blogId.substring(0, blogId.length() - 1);
          blogId = blogId + localblogId;
          blogmodel.setBlogId(blogId);
          blogMap.put("BlogId",blogmodel.getBlogId());
          editor = sharedpreferences.edit();
          editor.putString("blogId_new",blogId);
          editor.apply();
          addData();
      }
    }
}














