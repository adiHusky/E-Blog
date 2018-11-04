package in.org.eonline.eblog.Fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.util.HashMap;
import java.util.Map;

import in.org.eonline.eblog.Models.BlogModel;
import in.org.eonline.eblog.Models.BugModel;
import in.org.eonline.eblog.Models.UserModel;
import in.org.eonline.eblog.R;
import in.org.eonline.eblog.SQLite.DatabaseHelper;


public class ReportBugFragment extends Fragment {
    private EditText bugUserName;
    private EditText bugBrandMode1;
    private EditText bugOSVersion;
    private EditText bugMessage;
    FirebaseFirestore db;
    private Button submitBug;
    BugModel bugModel = new BugModel();
    private String userId;
    Map<String, String> bugMap = new HashMap<>();
    private SharedPreferences sharedpreferences;
    public static final String MyPREFERENCES = "MyPrefs_new" ;

    public ReportBugFragment() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_report_bug, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initializeViews();
        db = FirebaseFirestore.getInstance();
        sharedpreferences = getActivity().getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        userId = sharedpreferences.getString("UserIdCreated","AdityaKamat75066406850");
        submitBug.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setBugUserMap();
            }
        });
    }

    public void initializeViews() {
        bugUserName = (EditText) getView().findViewById(R.id.reporter_name);
        bugBrandMode1 = (EditText) getView().findViewById(R.id.reporter_brand_model);
        bugOSVersion = (EditText) getView().findViewById(R.id.reporter_OS_version);
        bugMessage= (EditText) getView().findViewById(R.id.reporter_message);
        submitBug = (Button) getView().findViewById(R.id.submit_report_bug);
    }

  public void setBugUserMap(){
       bugModel.setBugUserName(bugUserName.getText().toString());
       bugModel.setBugBrandMode1(bugBrandMode1.getText().toString());
       bugModel.setBugOSVersion(bugOSVersion.getText().toString());
       bugModel.setBugMessage(bugMessage.getText().toString());
       bugMap.put("BugUserName",bugModel.getBugUserName());
       bugMap.put("BugBrandModel",bugModel.getBugBrandMode1());
       bugMap.put("BugOSVersion",bugModel.getBugOSVersion());
       bugMap.put("BugMessage",bugModel.getBugMessage());
       addBugDataToFirebase();
  }

    public void addBugDataToFirebase(){
        db.collection("ReportedBugs").document(userId).set(bugMap, SetOptions.merge())
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(getActivity(), "Bug Reported Successfully", Toast.LENGTH_LONG).show();

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getActivity(), "Some error occured while reporting bug", Toast.LENGTH_LONG).show();
                    }
                });
    }




}
