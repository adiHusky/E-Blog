package in.org.eonline.eblog.Adapters;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;

import java.util.List;

import in.org.eonline.eblog.BlogActivity;
import in.org.eonline.eblog.Models.BlogModel;
import in.org.eonline.eblog.R;

public class BlogAdapter extends RecyclerView.Adapter<BlogAdapter.BlogViewHolder> {
    private Context context;
    private List<BlogModel> blogModels;
    private BlogAdapter.ClickListener clickListner;

    public BlogAdapter(Context context, List<BlogModel> model, BlogAdapter.ClickListener listener) {
        this.context = context;
        this.blogModels = model;
        this.clickListner = listener;
    }

    @Override
    public BlogViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.blog_item, parent, false);
        return new BlogViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final BlogAdapter.BlogViewHolder holder, final int position) {
      final  BlogModel blogModel = blogModels.get(position);

        holder.blogNameItem.setText(blogModels.get(position).getBlogHeader());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickListner.onClickItem(blogModel); // decides the item in adapter which is clicked

                /*Intent intent = new Intent(context, BlogActivity.class);
                String blogmodel = (new Gson()).toJson(blogModel);

                intent.putExtra("blog", blogmodel);


                context.startActivity(intent); */
            }
        });
    }



    @Override
    public int getItemCount() {
        return blogModels.size();
    }


    public class BlogViewHolder extends RecyclerView.ViewHolder  implements View.OnClickListener{

        ImageView blogImageItem;
        TextView blogNameItem;
        TextView blogHeader;
        TextView blogFooter;
        TextView blogContent;
        RelativeLayout blogParent;
        BlogViewHolder holder;

        public BlogViewHolder(View itemView) {
            super(itemView);
            blogImageItem = (ImageView) itemView.findViewById(R.id.blog_image_item);
            //blogHeader =(TextView) itemView.findViewById(R.id.blog_header);
            //blogFooter = (TextView) itemView.findViewById(R.id.blog_footer);
            //blogContent=(TextView) itemView.findViewById(R.id.blog_content);
            blogNameItem = (TextView) itemView.findViewById(R.id.blog_header_item);
            //blogParent = (RelativeLayout) itemView.findViewById(R.id.parent_layout);
        }

        @Override
        public void onClick(View v) {
            BlogModel blogModel = blogModels.get(getAdapterPosition());
            clickListner.onClickItem(blogModel);
        }
    }

    public interface ClickListener {

        void onClickItem(BlogModel model);
    }
}
