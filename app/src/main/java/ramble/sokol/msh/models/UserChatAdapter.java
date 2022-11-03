package ramble.sokol.msh.models;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import ramble.sokol.msh.databinding.ItemUserChatBinding;
import ramble.sokol.msh.intrerfaces.UserListener;

public class UserChatAdapter extends RecyclerView.Adapter<UserChatAdapter.UserChatViewHolder>{

    private final List<UserForChat> users;
    private final UserListener userListener;

    public UserChatAdapter(List<UserForChat> users, UserListener userListener){
        this.users = users;
        this.userListener = userListener;
    }

    @NonNull
    @Override
    public UserChatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemUserChatBinding itemUserChatBinding = ItemUserChatBinding.inflate(
                LayoutInflater.from(parent.getContext()),
                parent, false
        );
        return new UserChatViewHolder(itemUserChatBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull UserChatViewHolder holder, int position) {
        holder.setUserDate(users.get(position));
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    class UserChatViewHolder extends RecyclerView.ViewHolder{

        ItemUserChatBinding binding;

        UserChatViewHolder(ItemUserChatBinding itemUserChatBinding){
            super(itemUserChatBinding.getRoot());
            binding = itemUserChatBinding;
        }

        void setUserDate(UserForChat user){
            binding.itemTextName.setText(user.getFirstName() + " " + user.getLastName());
            binding.itemImageProfile.setImageBitmap(getUserImage(user.getImage()));
            binding.itemTextEmail.setText(user.getEmail());
            binding.getRoot().setOnClickListener(v -> userListener.onUserClicked(user));
        }

    }

    private Bitmap getUserImage(String encodedImage){
        byte[] bytes = Base64.decode(encodedImage, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
    }

}
