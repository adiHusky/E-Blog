package in.org.eonline.eblog.Fragments;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import in.org.eonline.eblog.R;

import static android.content.ContentValues.TAG;

/**
 * A simple {@link Fragment} subclass.
 */
public class MonetizationFragment extends Fragment {

    private EditText adMobAdUnitIdEdit;
    private Button submitAdMobAdUnitId;
    private String adMobUnitId;
     FirebaseFirestore db;
    private SharedPreferences sharedpreferences;
    public static final String MyPREFERENCES = "MyPrefs_new" ;
    private  String userId;
    private List<String> blogModelsList = new ArrayList<String>();
    Map<String, String> blogMap = new HashMap<>();

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
        db= FirebaseFirestore.getInstance();

        initializeViews();
        sharedpreferences = getActivity().getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        userId = sharedpreferences.getString("UserIdCreated","AdityaKamat75066406850");

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

                addAdMobIdToUsers(adMobUnitId);

            }
        });



    }

    public void addAdMobIdToUsers(final String adMobUnitId) {





          /* final CollectionReference blogDoc = db.collection("Blogs");

        blogDoc.whereEqualTo("UserId", userId).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {

                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        if (document.exists())

                            //blogModelsList.add(document.getId().toString());
                           // blogMap.put("BannerAdMobId",adMobUnitId);
                        blogDoc.document(document.getId()).update("BannerAdMobId",adMobUnitId);

                    }

                } else {
                    Log.d(TAG, "Error getting documents: ", task.getException());
                }

            }
        });*/
        db.collection("Users").document(userId).update("UserBannerId",adMobUnitId)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "DocumentSnapshot successfully updated!");
                        Toast.makeText(getContext(), "Data updated successfully", Toast.LENGTH_SHORT).show();

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error updating document", e);
                    }
                });;

/*
        blogUserDoc.whereEqualTo("UserId", userId).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {

                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        if (document.exists())
                            //blogModelsList.add(document.getId().toString());
                            // blogMap.put("BannerAdMobId",adMobUnitId);
                            blogUserDoc.document(document.getId()).update("BannerAdMobId",adMobUnitId);

                    }

                } else {
                    Log.d(TAG, "Error getting documents: ", task.getException());
                }

            }
        });*/
        }

    public void initializeViews() {
        adMobAdUnitIdEdit = (EditText) getView().findViewById(R.id.admob_ad_unit_id);
        submitAdMobAdUnitId = (Button) getView().findViewById(R.id.admob_ad_unit_id_submit);
        //userAdView = (AdView) getView().findViewById(R.id.user_ad_view);
    }
}
