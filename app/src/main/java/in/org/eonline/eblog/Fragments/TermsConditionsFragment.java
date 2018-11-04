package in.org.eonline.eblog.Fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.firestore.FirebaseFirestore;

import in.org.eonline.eblog.R;


public class TermsConditionsFragment extends Fragment {

   private TextView termsHeader;
   private TextView termsContent;
    public TermsConditionsFragment() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_terms_conditions, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initializeViews();
    }


   public void initializeViews(){
    termsHeader = (TextView) getView().findViewById(R.id.terms_header);
    termsContent= (TextView) getView().findViewById(R.id.terms_content);

   };
}
