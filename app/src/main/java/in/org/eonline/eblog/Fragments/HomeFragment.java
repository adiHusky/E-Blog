package in.org.eonline.eblog.Fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

import java.util.ArrayList;
import java.util.List;

import in.org.eonline.eblog.Adapters.BlogAdapter;
import in.org.eonline.eblog.Adapters.UserAdapter;
import in.org.eonline.eblog.Models.BlogModel;
import in.org.eonline.eblog.Models.UserModel;
import in.org.eonline.eblog.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment implements UserAdapter.ClickListener, BlogAdapter.ClickListener {


    private AdView mAdView;
    private RecyclerView popularUsersRecyclerView, popularBlogsRecyclerView;
    private List<UserModel> userModels = new ArrayList<>();
    private List<BlogModel> blogModels = new ArrayList<>();

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

        initializeViews();

        setPopularUsersRecyclerView();
        setPopularBlogsRecyclerView();


        MobileAds.initialize(getContext(),"ca-app-pub-7293397784162310~9840078574");
        mAdView = (AdView) getView().findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                .addTestDevice("5DDD17EFB41CB40FC08FBE350D11B395").build();
        mAdView.loadAd(adRequest);
    }

    public void initializeViews() {
        popularUsersRecyclerView = (RecyclerView) getView().findViewById(R.id.popular_users);
        popularBlogsRecyclerView = (RecyclerView) getView().findViewById(R.id.popular_blogs);
    }

    public void setPopularUsersRecyclerView() {
        popularUsersRecyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL, false);
        popularUsersRecyclerView.setLayoutManager(linearLayoutManager);

        String userNames[]={"Vaibhav","Viraj","Aditya","Akshata",
                "Aniket" };

        for(int i = 0; i< userNames.length; i++) {
            UserModel model = new UserModel();
            model.setUserFName(userNames[i]);
            userModels.add(model);
        }

        UserAdapter adapter = new UserAdapter(getActivity(), userModels, HomeFragment.this);
        popularUsersRecyclerView.setAdapter(adapter);
    }

    public void setPopularBlogsRecyclerView() {
        popularBlogsRecyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL, false);
        popularBlogsRecyclerView.setLayoutManager(linearLayoutManager);

        String blogHeader[]={"Vaibhav","Viraj","Aditya","Akshata",
                "Aniket" };

        for(int i = 0; i< blogHeader.length; i++) {
            BlogModel model = new BlogModel();
            model.setBlogHeader(blogHeader[i]);
            blogModels.add(model);
        }

        BlogAdapter adapter = new BlogAdapter(getActivity(), blogModels, HomeFragment.this);
        popularBlogsRecyclerView.setAdapter(adapter);
    }
}
