package in.org.eonline.eblog;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.widget.TextView;

import com.google.gson.Gson;

import in.org.eonline.eblog.Models.BlogModel;

public class BlogActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blog);
        if (getIntent().hasExtra("blog")) {
            BlogModel blogModel = new Gson().fromJson(getIntent().getStringExtra("blog"), BlogModel.class);
            TextView blogHeader = findViewById(R.id.user_blog_header_text);
            TextView blogContent = findViewById(R.id.user_blog_content_text);
            blogContent.setMovementMethod(new ScrollingMovementMethod());

            TextView blogFooter = findViewById(R.id.user_blog_footer_text);
            blogHeader.setText(blogModel.getBlogHeader());
            blogContent.setText(blogModel.getBlogContent());
            blogFooter.setText(blogModel.getBlogFooter());
        }
    }

}