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

import in.org.eonline.eblog.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {


    private AdView mAdView;
    private RecyclerView popularUsersRecyclerView;

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

        popularUsersRecyclerView.setHasFixedSize(true);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL, false);
        popularUsersRecyclerView.setLayoutManager(linearLayoutManager);

        MobileAds.initialize(getContext(),"ca-app-pub-7293397784162310~9840078574");
        mAdView = (AdView) getView().findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                .addTestDevice("5DDD17EFB41CB40FC08FBE350D11B395").build();
        mAdView.loadAd(adRequest);
    }

    public void initializeViews() {
        popularUsersRecyclerView = (RecyclerView) getView().findViewById(R.id.popular_users);
    }
}
