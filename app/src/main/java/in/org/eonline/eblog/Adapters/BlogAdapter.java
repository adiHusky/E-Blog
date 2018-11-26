package in.org.eonline.eblog.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.annotation.GlideModule;
import com.bumptech.glide.module.AppGlideModule;

import org.w3c.dom.Text;

import java.util.List;

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
    public void onBindViewHolder(@NonNull final BlogViewHolder holder, final int position) {
      final  BlogModel blogModel = blogModels.get(position);

        holder.blogNameItem.setText(blogModels.get(position).getBlogHeader());
        holder.blogLikeItem.setText(blogModels.get(position).getBlogLikes()+ " Likes");
        holder.blogUserNameItem.setText(blogModels.get(position).getBlogUser());

        Glide.with(context)
                .load(blogModels.get(position).getUserImageUrl())
                .into(holder.blogUserImageItem);

        if(blogModels.get(position).getUserBlogImage1Url() != null) {
            Glide.with(context)
                    .load(blogModels.get(position).getUserBlogImage1Url())
                    .into(holder.blogImageItem);
        } else if(blogModels.get(position).getUserBlogImage2Url() != null) {
            Glide.with(context)
                    .load(blogModels.get(position).getUserBlogImage2Url())
                    .into(holder.blogImageItem);
        } else {
            Glide.with(context)
                    .load(R.drawable.sample_blog)
                    .into(holder.blogImageItem);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickListner.onClickItem(blogModel); // decides the item in adapter which is clicked
            }
        });
    }



    @Override
    public int getItemCount() {
        return blogModels.size();
    }


    public class BlogViewHolder extends RecyclerView.ViewHolder  implements View.OnClickListener{

        ImageView blogImageItem, blogUserImageItem;
        TextView blogNameItem, blogLikeItem, blogUserNameItem;

        public BlogViewHolder(View itemView) {
            super(itemView);
            blogImageItem = (ImageView) itemView.findViewById(R.id.blog_image_item);
            blogNameItem = (TextView) itemView.findViewById(R.id.blog_header_item);
            blogLikeItem = (TextView) itemView.findViewById(R.id.blog_likes_item);
            blogUserImageItem = (ImageView) itemView.findViewById(R.id.user_image_item);
            blogUserNameItem = (TextView) itemView.findViewById(R.id.user_name_item);
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
