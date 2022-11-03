package ramble.sokol.msh.intrerfaces;

import com.google.firebase.firestore.auth.User;

import ramble.sokol.msh.models.UserForChat;

public interface UserListener {
    void onUserClicked(UserForChat user);
}
