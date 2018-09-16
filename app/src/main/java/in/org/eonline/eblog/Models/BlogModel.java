package in.org.eonline.eblog.Models;

public class BlogModel {
    public String blogHeader;
    public String blogContent;
    public String blogFooter;
    public int blogLikes;
    public String blogUser;


    public String getBlogContent() {
        return blogContent;
    }

    public void setBlogContent(String blogContent) {
        this.blogContent = blogContent;
    }



    public String getBlogFooter() {
        return blogFooter;
    }

    public void setBlogFooter(String blogFooter) {
        this.blogFooter = blogFooter;
    }



    public String getBlogHeader() {
        return blogHeader;
    }

    public void setBlogHeader(String blogHeader) {
        this.blogHeader = blogHeader;
    }

    public int getBlogLikes() {
        return blogLikes;
    }

    public void setBlogLikes(int blogLikes) {
        this.blogLikes = blogLikes;
    }


    public String getBlogUser() {
        return blogUser;
    }

    public void setBlogUser(String blogUser) {
        this.blogUser = blogUser;
    }

}
