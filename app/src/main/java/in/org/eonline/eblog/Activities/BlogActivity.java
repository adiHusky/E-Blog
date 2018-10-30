package in.org.eonline.eblog.Activities;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
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
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Nullable;

import in.org.eonline.eblog.Fragments.TaskFragment;
import in.org.eonline.eblog.Models.BlogModel;
import in.org.eonline.eblog.Models.UserModel;
import in.org.eonline.eblog.R;

import static android.content.ContentValues.TAG;
import static in.org.eonline.eblog.Fragments.YourBlogsFragment.MyPREFERENCES;

public class BlogActivity extends AppCompatActivity {

    private TextView blogHeader;
    private TextView blogContent1;
    private TextView blogContent2;
    private TextView blogFooter;
    private TextView blogCategory;
    private TextView blogLikes;
    private String bannerId;
    private ImageView userLikesButton;
    private ImageView blogImageView1;
    private ImageView blogImageView2;
    private String blogId;
    FirebaseFirestore db;
    Map<String, String> userReadBlogMap = new HashMap<>();
    private SharedPreferences sharedpreferences;
    public static final String MyPREFERENCES = "MyPrefs_new";
    private String userId;
    private String likeStatus;
    private String likeButtonValue;
    private BlogModel blogModel;
    FrameLayout frameLayout;
    String[] userIdBlog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //To make activity Full Screen
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_blog);

        InitializeViews();

        db = FirebaseFirestore.getInstance();
        sharedpreferences = this.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        userId = sharedpreferences.getString("UserIdCreated", "AdityaKamat75066406850");

        if (getIntent().hasExtra("blog")) {
            blogModel = new Gson().fromJson(getIntent().getStringExtra("blog"), BlogModel.class);
            blogHeader.setText(blogModel.getBlogHeader());
            blogContent1.setText(blogModel.getBlogContent1());
            blogContent2.setText(blogModel.getBlogContent2());
            blogFooter.setText(blogModel.getBlogFooter());
            blogCategory.setText(blogModel.getBlogCategory());
            blogId = blogModel.getBlogId();
            blogLikes.setText(blogModel.getBlogLikes() + "Likes");
            bannerId = blogModel.getBannerAdMobId();
            Glide.with(BlogActivity.this)
                    .load(blogModel.getUserBlogImage1Url())
                    .into(blogImageView1);
            Glide.with(BlogActivity.this)
                    .load(blogModel.getUserBlogImage2Url())
                    .into(blogImageView2);
            CheckLikes(blogModel);
            userIdBlog=blogId.split("\\_");
            if (bannerId != null) {
                View adContainer = findViewById(R.id.blogAdMobView);

                AdView userAdView = new AdView(this);

                userAdView.setAdSize(AdSize.BANNER);
                userAdView.setAdUnitId(bannerId);

                ((RelativeLayout) adContainer).addView(userAdView);

                AdRequest adRequest = new AdRequest.Builder()
                        .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                        .addTestDevice("F6ECB8AECEA2A45447ADE1C65B01711B").build();

                userAdView.loadAd(adRequest);
            }


         /*   DocumentReference blogRef =db.collection("Users").document(userId).collection("UserReadBlogs").document(blogId);
            blogRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
                @Override
                public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                    String doc= documentSnapshot.getData().get("LikeStatus").toString();
                    if(doc.equals("true"))
                    {
                        userLikesButton.setText("LIKED");
                        likeButtonValue = "LIKED";

                    }
                    else
                    if(documentSnapshot.getString("LikeStatus")=="false")
                    {
                        userLikesButton.setText("UNLIKED");
                        likeButtonValue = "UNLIKED";
                    }
                    else
                    if(e!=null)
                    {
                        userLikesButton.setText("LIKE");
                        likeButtonValue="LIKE";
                    }

                }
                });*/

        }
    }


    public void InitializeViews() {
        blogHeader = findViewById(R.id.user_blog_header_text);
        blogContent1 = findViewById(R.id.user_blog_content_text1);
        blogContent2 = findViewById(R.id.user_blog_content_text2);
        blogFooter = findViewById(R.id.user_blog_footer_text);
        blogCategory = findViewById(R.id.user_blog_category);
        blogLikes = findViewById(R.id.user_blog_likes);
        userLikesButton = (ImageView) findViewById(R.id.user_blog_button);
        frameLayout = (FrameLayout) findViewById(R.id.content_frame);
        blogImageView1= (ImageView) findViewById(R.id.blog_image_activity_1);
        blogImageView2= (ImageView) findViewById(R.id.blog_image_activity_2);

    }


    public void CheckLikes(final BlogModel blogModel) {
        CollectionReference blogRef = db.collection("Users").document(userId).collection("UserReadBlogs");

        blogRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    QuerySnapshot documentSize =  task.getResult();
                    if(documentSize.size() > 0) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            String docid = document.getId();
                            String likeStatus = document.getString("LikeStatus");
                            if (docid.equals(blogId)) {
                                if (likeStatus.equals("true")) {
                                    // userLikesButton.setBackgroundTintList(getResources().getColorStateList(R.color.like));
                                    userLikesButton.setImageResource(R.drawable.ic_thumb_up_black_24dp);
                                    likeButtonValue = "LIKED";

                                } else {
                                    // userLikesButton.setBackgroundTintList(getResources().getColorStateList(R.color.black));
                                    userLikesButton.setImageResource(R.drawable.ic_thumb_up_black_like_24dp);
                                    likeButtonValue = "LIKE";
                                }
                            } else {
                                //userLikesButton.setBackgroundTintList(getResources().getColorStateList(R.color.black));
                                userLikesButton.setImageResource(R.drawable.ic_thumb_up_black_like_24dp);
                                likeButtonValue = "LIKE";
                            }
                        }
                    } else {
                        //userLikesButton.setBackgroundTintList(getResources().getColorStateList(R.color.black));
                        userLikesButton.setImageResource(R.drawable.ic_thumb_up_black_like_24dp);
                        likeButtonValue = "LIKE";
                    }
                }

                task.addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        likeButtonValue = "LIKE";
                      //  userLikesButton.setBackgroundTintList(getResources().getColorStateList(R.color.black));
                        userLikesButton.setImageResource(R.drawable.ic_thumb_up_black_like_24dp);
                        userLikesButton.setBackgroundColor(getResources().getColor(R.color.white));

                    }
                });
            }
        });



        userLikesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                if (likeButtonValue == "LIKE") {
                    AddUserBlogMap(blogModel);
                } else {
                    int bloglikesNew = Integer.parseInt(blogModel.getBlogLikes()) - 1;
                    userLikesButton.setImageResource(R.drawable.ic_thumb_up_black_like_24dp);
                    likeButtonValue = "LIKE";
                    likeStatus = "false";
                    String blogLikesNewString = Integer.toString(bloglikesNew);
                    blogModel.setBlogLikes(blogLikesNewString);
                    blogLikes.setText(blogLikesNewString + "Likes ");
                    //  String blogLikesNewString = Integer.toString(bloglikesNew);
                    // blogModel.setBlogLikes(blogLikesNewString);
                    //DocumentReference userDoc = db.collection("Blogs").document(blogId);
                    userReadBlogMap.put("LikeStatus", likeStatus);
                    userReadBlogMap.put("BlogLikes", blogLikesNewString);
                    db.collection("Users").document(userId).collection("UserReadBlogs").document(blogId).set(userReadBlogMap)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {


                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {

                                }
                            });
                    DocumentReference userBlog  = db.collection("Users").document(userIdBlog[0]).collection("Blogs").document(blogId);
                    userBlog.update("BlogLikes", blogLikesNewString)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Log.d(TAG, "DocumentSnapshot successfully updated!");
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.w(TAG, "Error updating document", e);
                                }
                            });
                    DocumentReference userDoc = db.collection("Blogs").document(blogId);
                    userDoc.update("BlogLikes", blogLikesNewString)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Log.d(TAG, "DocumentSnapshot successfully updated!");
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.w(TAG, "Error updating document", e);
                                }
                            });


                }
            }

        });
    }



    public void AddUserBlogMap(BlogModel blogModel) {
        likeStatus = "true";
        userLikesButton.setImageResource(R.drawable.ic_thumb_up_black_24dp);
        likeButtonValue = "LIKED";
        int bloglikesNew = Integer.parseInt(blogModel.getBlogLikes()) + 1;
        String blogLikesString = Integer.toString(bloglikesNew);
        blogLikes.setText(blogLikesString + "Likes");
        blogModel.setBlogLikes(blogLikesString);
        userReadBlogMap.put("LikeStatus", likeStatus);
        userReadBlogMap.put("BlogLikes", blogLikesString);
        DocumentReference userDoc = db.collection("Blogs").document(blogId);
        userDoc.update("BlogLikes", blogLikesString)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "DocumentSnapshot successfully updated!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error updating document", e);
                    }
                });

        DocumentReference userBlog  = db.collection("Users").document(userIdBlog[0]).collection("Blogs").document(blogId);
        userBlog.update("BlogLikes", blogLikesString)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "DocumentSnapshot successfully updated!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error updating document", e);
                    }
                });
        db.collection("Users").document(userId).collection("UserReadBlogs").document(blogId).set(userReadBlogMap)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                    }
                });
    }

   /* @Override
    public void onBackPressed() {
        openFragment();
        super.onBackPressed();
    }

    public void openFragment() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }*/

}

