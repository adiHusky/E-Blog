package in.org.eonline.eblog.Fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;

import in.org.eonline.eblog.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class MonetizationFragment extends Fragment {

    private EditText adMobAdUnitIdEdit;
    private Button submitAdMobAdUnitId;
    private String adMobUnitId;

    public MonetizationFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_monetization, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        initializeViews();

        submitAdMobAdUnitId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              /*  adMobUnitId = adMobAdUnitIdEdit.getText().toString();

                userAdView = new AdView(getActivity());
                userAdView.setAdSize(AdSize.SMART_BANNER);
                userAdView.setAdUnitId(adMobUnitId);

                AdRequest adRequest = new AdRequest.Builder()
                        .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                        .addTestDevice("5DDD17EFB41CB40FC08FBE350D11B395").build();


                userAdView.loadAd(adRequest); */

                adMobUnitId = adMobAdUnitIdEdit.getText().toString();

                View adContainer = getView().findViewById(R.id.adMobView);

                AdView userAdView = new AdView(getActivity());

                userAdView.setAdSize(AdSize.BANNER);
                userAdView.setAdUnitId(adMobUnitId);

                ((RelativeLayout)adContainer).addView(userAdView);

                AdRequest adRequest = new AdRequest.Builder()
                        .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                        .addTestDevice("5DDD17EFB41CB40FC08FBE350D11B395").build();
                userAdView.loadAd(adRequest);
            }
        });



    }

    public void initializeViews() {
        adMobAdUnitIdEdit = (EditText) getView().findViewById(R.id.admob_ad_unit_id);
        submitAdMobAdUnitId = (Button) getView().findViewById(R.id.admob_ad_unit_id_submit);
        //userAdView = (AdView) getView().findViewById(R.id.user_ad_view);
    }
}
