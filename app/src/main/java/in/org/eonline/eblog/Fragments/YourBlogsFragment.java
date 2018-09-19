package in.org.eonline.eblog.Fragments;


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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import in.org.eonline.eblog.Adapters.BlogAdapter;
import in.org.eonline.eblog.Models.BlogModel;
import in.org.eonline.eblog.R;

import static android.content.ContentValues.TAG;

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
        db= FirebaseFirestore.getInstance();
        InitializeViews();
        setData();




    }


    public void setData() {

       /* db.collection("Blogs").addSnapshotListener(new EventListener<QuerySnapshot>() {
                                                       @Override
                                                       public void onEvent( QuerySnapshot documentSnapshots,  FirebaseFirestoreException e) {

                                                           if (e!= null) {

                                                               Log.d(TAG, "ERROR" + e.getMessage());

                                                           }
                                                               for (DocumentSnapshot doc: documentSnapshots){
                                                                   blogHeader = doc.getString("name");

                                                               Log.d(TAG,"name"  + blogHeader);
                                                           }
                                                       }
                                                   }

        ); */
        db.collection("Blogs")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                if (document.exists()) {

                                        setBlogModel(document);

                                };


                                Log.d(TAG, document.getId() + " => " + document.getData());
                            }
                            setPopularBlogsRecyclerView();

                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });

        /*db.collection("Blogs").document("Aditya").get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        setBlogModel(document);

                    } else {
                        blogHeader = "doc no exists";
                        Log.v(TAG, "No such document");
                    }
                } else {

                    blogHeader = " no doc exists";
                    Log.v(TAG, "get failed with ", task.getException());
                }
            }
        });*/
    }

    private void setBlogModel(QueryDocumentSnapshot document) {

        blogModel = new BlogModel();
        blogModel.setBlogHeader(document.getString("BlogHeader"));
        blogModel.setBlogFooter(document.getString("BlogFooter"));
        blogModel.setBlogContent(document.getString("BlogContent"));
       // blogModel.setBlogLikes(Integer.parseInt(document.getString("BlogLikes")));
        blogModel.setBlogUser(document.getString("BlogUser"));
        blogModel.setBlogCategory(document.getString("BlogCategory"));

        blogModelsList.add(blogModel);
       // setPopularBlogsRecyclerView();

       // blogHeader = document.getString("BlogHeader");
        Log.v(TAG, "DocumentSnapshot data: " + document.getData() );
        // blogHeaderTextView.setText(blogModel.getBlogHeader());
       // blogContentTextView.setText(blogModel.getBlogContent());
        //blogFooterTextView.setText(blogModel.getBlogFooter());

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
}
