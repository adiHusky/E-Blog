package in.org.eonline.eblog.Adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder> {
    @NonNull
    @Override
    public UserAdapter.UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull UserAdapter.UserViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class UserViewHolder extends RecyclerView.ViewHolder {
        public UserViewHolder(View itemView) {
            super(itemView);
        }
    }
}
