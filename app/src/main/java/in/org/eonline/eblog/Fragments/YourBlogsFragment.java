package in.org.eonline.eblog.Fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import in.org.eonline.eblog.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class YourBlogsFragment extends Fragment {


    public YourBlogsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_your_blogs, container, false);
    }

}
