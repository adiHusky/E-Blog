package in.org.eonline.eblog.Fragments;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import in.org.eonline.eblog.Adapters.BlogAdapter;
import in.org.eonline.eblog.Activities.BlogActivity;
import in.org.eonline.eblog.Models.BlogModel;
import in.org.eonline.eblog.R;
import in.org.eonline.eblog.SQLite.DatabaseHelper;

/**
 * A simple {@link Fragment} subclass.
 */
public class YourBlogsFragment extends Fragment implements BlogAdapter.ClickListener {

    FirebaseFirestore db;
    private TextView blogHeaderTextView;
    private String blogHeader;
    private TextView blogContentTextView;
    private String blogContent;
    private TextView blogFooterTextView;
    private String blogFooter;
    private String userName;
    private String blogLikes;
    BlogModel blogModel =  new BlogModel();
    private RecyclerView  yourBlogsRecyclerView;
    private List<BlogModel> blogModelsList = new ArrayList<>();
    private static final String TAG = "FireLog";
    private SharedPreferences sharedpreferences;
    public static final String MyPREFERENCES = "MyPrefs_new" ;
    private String userId;
    private String blogId;
    DatabaseHelper sqliteDatabaseHelper;
    private AdView mAdView;

    public YourBlogsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_your_blogs, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        sharedpreferences = getActivity().getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        userId = sharedpreferences.getString("UserIdCreated","AdityaKamat75066406850");

        sqliteDatabaseHelper = new DatabaseHelper(getActivity());
        db= FirebaseFirestore.getInstance();
        InitializeViews();
        setYourBlogsFromFirebase();

        MobileAds.initialize(getContext(),"ca-app-pub-7722811932766421~9001519486");
        mAdView = (AdView) getView().findViewById(R.id.yourBlogs_adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
    }

    public void setYourBlogsFromFirebase() {
        CollectionReference blogRef =db.collection("Users").document(userId).collection("Blogs");
                blogRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                if (document.exists())
                                    setBlogModel(document);
                            }
                            setPopularBlogsRecyclerView();
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });
    }

    private void setBlogModel(QueryDocumentSnapshot document) {
        blogModel = new BlogModel();
        blogModel.setBlogHeader(document.getString("BlogHeader"));
        blogModel.setBlogFooter(document.getString("BlogFooter"));
        blogModel.setBlogContent1(document.getString("BlogContent1"));
        blogModel.setBlogContent2(document.getString("BlogContent2"));
        blogModel.setBlogLikes(document.getString("BlogLikes"));
        blogModel.setBlogUser(document.getString("BlogUser"));
        blogModel.setBlogCategory(document.getString("BlogCategory"));
        blogModel.setBlogId(document.getString("BlogId"));
        blogModel.setBannerAdMobId(document.getString("BlogUserBannerId"));
        blogModel.setUserBlogImage1Url(document.getString("BlogImage1Url"));
        blogModel.setUserBlogImage2Url(document.getString("BlogImage2Url"));
        blogModelsList.add(blogModel);
       // setPopularBlogsRecyclerView();
    }

    public  void InitializeViews(){
        yourBlogsRecyclerView = (RecyclerView) getView().findViewById(R.id.your_blogs);
        blogHeaderTextView = (TextView) getView().findViewById(R.id.blog_header_text);
        blogContentTextView = (TextView) getView().findViewById(R.id.blog_content_text);
        blogFooterTextView = (TextView) getView().findViewById(R.id.blog_footer_text);
    }

    public void setPopularBlogsRecyclerView() {
        yourBlogsRecyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL, false);
        yourBlogsRecyclerView.setLayoutManager(linearLayoutManager);
        BlogAdapter adapter = new BlogAdapter(getActivity(),blogModelsList , YourBlogsFragment.this);
        yourBlogsRecyclerView.setAdapter(adapter);
    }

    @Override
    public void onClickItem(BlogModel model) {
        Intent intent = new Intent(getActivity(), BlogActivity.class);
        String blogmodel = (new Gson()).toJson(model);
        intent.putExtra("blog", blogmodel);
        getActivity().startActivity(intent);

    }
}
