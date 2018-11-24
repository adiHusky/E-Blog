package in.org.eonline.eblog.Fragments;


import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
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
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import in.org.eonline.eblog.R;
import in.org.eonline.eblog.Utilities.CommonDialog;
import in.org.eonline.eblog.Utilities.ConnectivityReceiver;

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
    ConnectivityReceiver connectivityReceiver;
    Boolean isInternetPresent = false;
    public SwipeRefreshLayout mySwipeRequestLayout;
    public Dialog dialog;

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
        checkAdmobId();
        refreshMyProfile();

        submitAdMobAdUnitId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                adMobUnitId = adMobAdUnitIdEdit.getText().toString();

                /*View adContainer = getView().findViewById(R.id.adMobView);

                AdView userAdView = new AdView(getActivity());

                userAdView.setAdSize(AdSize.BANNER);
                userAdView.setAdUnitId(adMobUnitId);

                //((RelativeLayout)adContainer).addView(userAdView);

                AdRequest adRequest = new AdRequest.Builder()
                        .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                        .addTestDevice("5DDD17EFB41CB40FC08FBE350D11B395").build();
                userAdView.loadAd(adRequest); */

                addAdMobIdToUsers(adMobUnitId);

            }
        });



    }
    public void refreshMyProfile(){
        mySwipeRequestLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                onRefreshOperation();
                mySwipeRequestLayout.setRefreshing(false);
            }
        });
    }

    public void onRefreshOperation(){
        Fragment frg = new MonetizationFragment();

        final android.support.v4.app.FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.detach(frg);
        ft.attach(frg);
        ft.commit();
    }



    public void addAdMobIdToUsers(String adMobUnitId) {
        connectivityReceiver = new ConnectivityReceiver(getActivity());
        // Initialize SDK before setContentView(Layout ID)
        isInternetPresent = connectivityReceiver.isConnectingToInternet();
        if (isInternetPresent) {
            dialog = CommonDialog.getInstance().showProgressDialog(getActivity());
            dialog.show();
            addDataToUserFirebase(adMobUnitId);

        } else {
            CommonDialog.getInstance().showErrorDialog(getActivity(), R.drawable.no_internet);
            //Toast.makeText(Login.this, "No Internet Connection, Please connect to Internet.", Toast.LENGTH_LONG).show();
        }


        }

    public void addDataToUserFirebase(String adMobUnitId){
        db.collection("Users").document(userId).update("UserBannerId", adMobUnitId)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "DocumentSnapshot successfully updated!");
                        Toast.makeText(getContext(), "Data updated successfully", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error updating document", e);
                        CommonDialog.getInstance().showErrorDialog(getActivity(), R.drawable.failure_image);
                        dialog.dismiss();
                    }
                });
    }

    public void initializeViews() {
        adMobAdUnitIdEdit = (EditText) getView().findViewById(R.id.admob_ad_unit_id);
        submitAdMobAdUnitId = (Button) getView().findViewById(R.id.admob_ad_unit_id_submit);
        //userAdView = (AdView) getView().findViewById(R.id.user_ad_view);
        mySwipeRequestLayout = (SwipeRefreshLayout) getView().findViewById(R.id.swiperefresh_monetize);
    }

    public void checkAdmobId()
    {

        connectivityReceiver = new ConnectivityReceiver(getActivity());
        // Initialize SDK before setContentView(Layout ID)
        isInternetPresent = connectivityReceiver.isConnectingToInternet();
        if (isInternetPresent) {
            dialog = CommonDialog.getInstance().showProgressDialog(getActivity());
            dialog.show();
            enterUsersFirebaseForAmobId();

        } else {
            CommonDialog.getInstance().showErrorDialog(getActivity(), R.drawable.no_internet);
            //Toast.makeText(Login.this, "No Internet Connection, Please connect to Internet.", Toast.LENGTH_LONG).show();
        }


    }

    public void enterUsersFirebaseForAmobId(){
        DocumentReference docRef = db.collection("Users").document(userId);
        docRef.get().addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                CommonDialog.getInstance().showErrorDialog(getActivity(), R.drawable.failure_image);
                dialog.dismiss();
            }
        })
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        try {
                            adMobAdUnitIdEdit.setText(document.getString("UserBannerId").toString());
                            Toast.makeText(getContext(), "Admob ID present", Toast.LENGTH_LONG).show();
                            dialog.dismiss();
                        }
                        catch(NullPointerException e){
                            Toast.makeText(getContext(), "Enter Admob ID", Toast.LENGTH_LONG).show();
                            dialog.dismiss();
                        }
                    }

                    else {
                        dialog.dismiss();
                        /*setUserModelAndUserMap();
                        addDataToUserFirebase();*/
                    }
                } else {
                    Toast.makeText(getContext(), "Enter Admob ID", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                }

            }
        });

    }

}
