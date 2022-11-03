package ramble.sokol.msh.models;

import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import ramble.sokol.msh.databinding.ItemContainerReceivedMessageBinding;
import ramble.sokol.msh.databinding.ItemContainerSendMessageBinding;

public class ChatAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private final Bitmap receiverProfileImage;
    private final List<ChatMessages> chatMessages;
    private final String senderId;

    public static final int VIEW_TYPE_SENT = 1;
    public static final int VIEW_TYPE_RECEIVED = 2;

    public ChatAdapter(Bitmap receiverProfileImage, List<ChatMessages> chatMessages, String senderId) {
        this.receiverProfileImage = receiverProfileImage;
        this.chatMessages = chatMessages;
        this.senderId = senderId;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_SENT){
            return new SentMessageViewHolder(
                    ItemContainerSendMessageBinding.inflate(
                            LayoutInflater.from(parent.getContext()), parent, false
                    )
            );
        }else{
            return new ReceivedMessageViewHolder(
                    ItemContainerReceivedMessageBinding.inflate(
                            LayoutInflater.from(parent.getContext()), parent, false
                    )
            );
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (getItemViewType(position) == VIEW_TYPE_SENT){
            ((SentMessageViewHolder) holder).setData(chatMessages.get(position));
        }else{
            ((ReceivedMessageViewHolder) holder).setData(chatMessages.get(position), receiverProfileImage);
        }
    }

    @Override
    public int getItemCount() {
        return chatMessages.size();
    }

    @Override
    public int getItemViewType(int position) {
        if(chatMessages.get(position).senderId.equals(senderId)){
            return VIEW_TYPE_SENT;
        }else{
            return  VIEW_TYPE_RECEIVED;
        }
    }

    static class SentMessageViewHolder extends RecyclerView.ViewHolder{

        private final ItemContainerSendMessageBinding binding;

        SentMessageViewHolder(ItemContainerSendMessageBinding itemContainerSendMessageBinding){
            super(itemContainerSendMessageBinding.getRoot());
            binding = itemContainerSendMessageBinding;
        }

        void setData(ChatMessages chatMessages){
            binding.textMessageSend.setText(chatMessages.dataTime);
            binding.textDateTime.setText(chatMessages.dataTime);
        }
    }

    static class ReceivedMessageViewHolder extends RecyclerView.ViewHolder{

        private final ItemContainerReceivedMessageBinding binding;

        ReceivedMessageViewHolder(ItemContainerReceivedMessageBinding itemContainerReceivedMessageBinding){
            super(itemContainerReceivedMessageBinding.getRoot());
            binding = itemContainerReceivedMessageBinding;

        }

        void setData(ChatMessages chatMessages, Bitmap receiverProfileImage){
            binding.textMessageRecevied.setText(chatMessages.message);
            binding.textDateTimeRe.setText(chatMessages.dataTime);
            binding.imageProfileRecevied.setImageBitmap(receiverProfileImage);
        }

    }

}
