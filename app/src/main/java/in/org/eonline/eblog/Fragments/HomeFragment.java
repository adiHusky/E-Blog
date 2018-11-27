package in.org.eonline.eblog.Fragments;


import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import in.org.eonline.eblog.Adapters.BlogAdapter;
import in.org.eonline.eblog.Adapters.UserAdapter;
import in.org.eonline.eblog.Activities.BlogActivity;
import in.org.eonline.eblog.Models.BlogModel;
import in.org.eonline.eblog.Models.UserModel;
import in.org.eonline.eblog.R;
import in.org.eonline.eblog.Utilities.CommonDialog;
import in.org.eonline.eblog.Utilities.ConnectivityReceiver;


import static android.content.ContentValues.TAG;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment implements UserAdapter.ClickListener, BlogAdapter.ClickListener {

    FirebaseFirestore db;
    private List<BlogModel> blogModelsList = new ArrayList<>();
    private List<UserModel> userModelsList = new ArrayList<>();
    BlogModel blogModel;
    UserModel userModel;
    private AdView mAdView;
    private RecyclerView popularBlogsRecyclerView;
    private List<UserModel> userModels = new ArrayList<>();
    private List<BlogModel> blogModels = new ArrayList<>();
    FirebaseStorage storage;
    StorageReference storageRef;
    ConnectivityReceiver connectivityReceiver;
    Boolean isInternetPresent = false;
    public Dialog dialog;
    public SwipeRefreshLayout mySwipeRequestLayout;
    View view;
    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        db = FirebaseFirestore.getInstance();

        initializeViews();
        setDataFirebase();
        refreshMyProfile();

        // Get the instance of Firebase storage
        storage = FirebaseStorage.getInstance();
        // Create a storage reference from our app
        storageRef = storage.getReference();


        MobileAds.initialize(getContext(),"ca-app-pub-7293397784162310~9840078574");
        mAdView = (AdView) getView().findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                .addTestDevice("5DDD17EFB41CB40FC08FBE350D11B395").build();
        mAdView.loadAd(adRequest);
    }

    public void initializeViews() {
        //popularUsersRecyclerView = (RecyclerView) getView().findViewById(R.id.popular_users);
        popularBlogsRecyclerView = (RecyclerView) getView().findViewById(R.id.popular_blogs);
        mySwipeRequestLayout=(SwipeRefreshLayout) getView().findViewById(R.id.swiperefresh_home);
    }

    public void refreshMyProfile(){

        mySwipeRequestLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {

                                                      @Override
                                                      public void onRefresh() {

                                                          onRefreshOperation();
                                                          mySwipeRequestLayout.setRefreshing(false);
                                                      }
                                                  }
        );

    }

    public void onRefreshOperation(){
       // getFragmentManager().beginTransaction().detach(this).attach(this).commit();
        Fragment frg = null;
        frg = getFragmentManager().findFragmentByTag("nav_home");
        final FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.detach(frg);
        blogModelsList.clear();
        ft.attach(frg);
        ft.commit();
    }

    public void setDataFirebase(){
        connectivityReceiver = new ConnectivityReceiver(getActivity());
        // Initialize SDK before setContentView(Layout ID)
        isInternetPresent = connectivityReceiver.isConnectingToInternet();
        if (isInternetPresent) {
            dialog = CommonDialog.getInstance().showProgressDialog(getActivity());
            dialog.show();
            enterBlogsFirebase();
        } else {
            CommonDialog.getInstance().showErrorDialog(getActivity(), R.drawable.no_internet);
        }
    }

    public void enterBlogsFirebase(){
        CollectionReference colRef=db.collection("Blogs");

        colRef.get().addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                CommonDialog.getInstance().showErrorDialog(getContext(), R.drawable.failure_image);
                if (dialog != null && dialog.isShowing()) {
                    dialog.dismiss();
                }
            }
        })
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            int i=0;
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                i++;
                                if (document.exists()) {
                                    setBlogModel(document);
                                }
                                else
                                {
                                    CommonDialog.getInstance().showErrorDialog(getContext(), R.drawable.failure_image);
                                }
                               ;
                                Log.d(TAG, document.getId() + " => " + document.getData());
                            }
                            if(i==0) {
                                CommonDialog.getInstance().showErrorDialog(getContext(), R.drawable.no_data);
                            }
                            setPopularBlogsRecyclerView();
                            if (dialog != null && dialog.isShowing()) {
                                dialog.dismiss();
                            }
                        } else {
                            CommonDialog.getInstance().showErrorDialog(getContext(), R.drawable.failure_image);
                            if (dialog != null && dialog.isShowing()) {
                                dialog.dismiss();
                            }
                        }
                    }
                });

    }

    public void setUserModel(DocumentSnapshot document) {
        //userModelsList = new ArrayList<>();
        userModel = new UserModel();
        userModel.setUserFName(document.getString("UserFirstName"));
        userModel.setUserLName(document.getString("UserLastName"));
        userModel.setUserEmail(document.getString("UserEmailId"));
        // blogModel.setBlogLikes(Integer.parseInt(document.getString("BlogLikes")));
        userModel.setUserContact(document.getString("UserContact"));
        userModelsList.add(userModel);
    }


    private void setBlogModel(QueryDocumentSnapshot doc) {
        //blogModelsList = new ArrayList<>();
        blogModel = new BlogModel();
        blogModel.setBlogHeader(doc.getString("BlogHeader"));
        blogModel.setBlogFooter(doc.getString("BlogFooter"));
        blogModel.setBlogContent1(doc.getString("BlogContent1"));
        blogModel.setBlogContent2(doc.getString("BlogContent2"));
        blogModel.setBlogLikes(doc.getString("BlogLikes"));
        blogModel.setBlogUser(doc.getString("BlogUser"));
        blogModel.setBlogCategory(doc.getString("BlogCategory"));
        blogModel.setBlogId(doc.getString("BlogId"));
        blogModel.setBannerAdMobId(doc.getString("BlogUserBannerId"));
        blogModel.setUserBlogImage1Url(doc.getString("BlogImage1Url"));
        blogModel.setUserBlogImage2Url(doc.getString("BlogImage2Url"));
        blogModel.setUserImageUrl(doc.getString("BlogUserImageUrl"));
        blogModel.setUserId(doc.getString("UserId"));
        blogModelsList.add(blogModel);
    }

    /*public void setPopularUsersRecyclerView() {
        popularUsersRecyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL, false);
        popularUsersRecyclerView.setLayoutManager(linearLayoutManager);
        UserAdapter adapter = new UserAdapter(getActivity(),userModelsList , HomeFragment.this);
        popularUsersRecyclerView.setAdapter(adapter);
    } */

    public void setPopularBlogsRecyclerView() {
        popularBlogsRecyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL, false);
        popularBlogsRecyclerView.setLayoutManager(linearLayoutManager);
        BlogAdapter adapter = new BlogAdapter(getActivity(),blogModelsList , HomeFragment.this);
        popularBlogsRecyclerView.setAdapter(adapter);
    }

    @Override
    public void onClickItem(BlogModel model) {
        Intent intent = new Intent(getActivity(), BlogActivity.class);
        String blogmodel = (new Gson()).toJson(model);
        intent.putExtra("blog", blogmodel);
        getActivity().startActivity(intent);
    }
}
