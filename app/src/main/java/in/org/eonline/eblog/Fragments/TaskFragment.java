package in.org.eonline.eblog.Fragments;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
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
import android.widget.Button;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import in.org.eonline.eblog.Adapters.BlogAdapter;
import in.org.eonline.eblog.Activities.BlogActivity;
import in.org.eonline.eblog.Models.BlogModel;
import in.org.eonline.eblog.R;

import static android.content.ContentValues.TAG;

/**
 * A simple {@link Fragment} subclass.
 */
public class TaskFragment extends Fragment implements BlogAdapter.ClickListener {

    FirebaseFirestore db;
    private Button button;
    BlogModel blogModel = new BlogModel();
    private RecyclerView yourBlogsRecyclerView;
    private List<BlogModel> blogModelsList = new ArrayList<>();
    private List<BlogModel> blogListCategorywise = new ArrayList<>();
    private int length;
    boolean[] checkedSelectedArray = new boolean[11];

    public TaskFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_task, container, false);
    }

    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        db= FirebaseFirestore.getInstance();
        initializeViews();
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setAlertDialog();
            }
        });
        setYourBlogsFromFirebase();
    }

    public void initializeViews() {

        yourBlogsRecyclerView = (RecyclerView) getView().findViewById(R.id.your_blogs);
        button = (Button) getView().findViewById(R.id.submit_filter_button);
    }


    public void setAlertDialog() {
        String abc[] = {"Travelling", "Food", "Cosmetics", "Apparels", "Technology", "Cars and Bikes", "Politics", "Socialism", "Bollywood and entertainment", "Business", "others"};
        final List<String> categories = Arrays.asList(abc);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        length = checkedSelectedArray.length;
        builder.setTitle("Select your category");
        builder.setMultiChoiceItems(abc, checkedSelectedArray, new DialogInterface.OnMultiChoiceClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                checkedSelectedArray[which] = isChecked;
            }
        });

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which) {
                blogListCategorywise.clear();
                int size = blogModelsList.size();
                for (int j = 0 ; j < size; j++) //it will take all the blogs from the blogModelList for comparison
                {
                    for (int i = 0; i < length; i++) { // it will take all the categories from the dialog box for comparison
                        boolean checked = checkedSelectedArray[i];
                        if (checked) {//it will take the selected category from dialog box
                            String blogCategoryCheck = blogModelsList.get(j).getBlogCategory().toString();
                            String blogCategoryFromDialog = categories.get(i).toString();
                            if (blogCategoryCheck.equals(blogCategoryFromDialog)) {//it will compare the category and the blogmodel list
                                setBlogModelFromCategory(blogModelsList.get(j));// it will set the filtered list
                            }
                        }
                    }
                }
                setBlogsRecyclerViewFromCategory();
            }
        });

        builder.setNeutralButton("Cancel", new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which) { //logic for cancelling

            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public void setYourBlogsFromFirebase() {
        CollectionReference blogRef =db.collection("Blogs");

        blogRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {

                    for (QueryDocumentSnapshot document : task.getResult()) {
                        if (document.exists())
                                setBlogModel(document);
                    }
                    setBlogsRecyclerView();
                } else {
                    Log.d(TAG, "Error getting documents: ", task.getException());
                }
            }
        });
    }
    private void setBlogModelFromCategory(BlogModel blogModelFromDialog) {
        blogModel = new BlogModel();

        blogModel.setBlogHeader(blogModelFromDialog.getBlogHeader());
        blogModel.setBlogFooter(blogModelFromDialog.getBlogFooter());
        blogModel.setBlogContent1(blogModelFromDialog.getBlogContent1());
        blogModel.setBlogContent2(blogModelFromDialog.getBlogContent2());
        blogModel.setBlogLikes(blogModelFromDialog.getBlogLikes());
        blogModel.setBlogUser(blogModelFromDialog.getBlogUser());
        blogModel.setBlogCategory(blogModelFromDialog.getBlogCategory());
        blogModel.setBlogId(blogModelFromDialog.getBlogId());
        blogModel.setBannerAdMobId(blogModelFromDialog.getBannerAdMobId());
        blogListCategorywise.add(blogModel);
    }

    private void setBlogModel(QueryDocumentSnapshot document) {
        blogModel = new BlogModel();
        blogModel.setBlogHeader(document.getString("BlogHeader"));
        blogModel.setBlogFooter(document.getString("BlogFooter"));
        blogModel.setBlogContent1(document.getString("BlogContent1"));
        blogModel.setBlogContent2(document.getString("BlogContent2"));
        blogModel.setBlogLikes(document.getString("BlogLikes"));
        blogModel.setBlogUser(document.getString("BlogUser"));
        blogModel.setBlogCategory(document.getString("BlogCategory"));
        blogModel.setBlogId(document.getString("BlogId"));
        blogModel.setBannerAdMobId(document.getString("BlogUserBannerId"));
        blogModel.setUserBlogImage1Url(document.getString("BlogImage1Url"));
        blogModel.setUserBlogImage2Url(document.getString("BlogImage2Url"));
        blogModelsList.add(blogModel);
    }

    public void setBlogsRecyclerView() { //for populating the recycler view from firebase data
        yourBlogsRecyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        yourBlogsRecyclerView.setLayoutManager(linearLayoutManager);
        BlogAdapter adapter = new BlogAdapter(getActivity(), blogModelsList, TaskFragment.this);
        yourBlogsRecyclerView.setAdapter(adapter);
    }

    public void setBlogsRecyclerViewFromCategory() { // for populating the recycler view as per the filtered categories
        yourBlogsRecyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        yourBlogsRecyclerView.setLayoutManager(linearLayoutManager);
        BlogAdapter adapter = new BlogAdapter(getActivity(), blogListCategorywise, TaskFragment.this);
        yourBlogsRecyclerView.setAdapter(adapter);
    }

    @Override
    public void onClickItem(BlogModel model) {
        Intent intent = new Intent(getActivity(), BlogActivity.class);
        String blogmodel = (new Gson()).toJson(model);
        intent.putExtra("blog", blogmodel);
        getActivity().startActivity(intent);
    }
}