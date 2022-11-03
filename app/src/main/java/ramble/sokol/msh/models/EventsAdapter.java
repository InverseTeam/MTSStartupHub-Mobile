package ramble.sokol.msh.models;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import ramble.sokol.msh.R;
import ramble.sokol.msh.databinding.ItemUserChatBinding;
import ramble.sokol.msh.databinding.ListItemEventsBinding;

public class EventsAdapter extends RecyclerView.Adapter<EventsAdapter.EventsViewHolder>{

    private final List<Events> events;

    public EventsAdapter(List<Events> events){
        this.events = events;
    }

    @NonNull
    @Override
    public EventsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ListItemEventsBinding listItemEventsBinding = ListItemEventsBinding.inflate(
                LayoutInflater.from(parent.getContext()),
                parent, false
        );
        return new EventsAdapter.EventsViewHolder(listItemEventsBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull EventsViewHolder holder, int position) {
        holder.setEventDate(events.get(position));
    }

    @Override
    public int getItemCount() {
        return events.size();
    }

    class EventsViewHolder extends RecyclerView.ViewHolder{

        ListItemEventsBinding binding;

        EventsViewHolder(ListItemEventsBinding listItemEventsBinding){
            super(listItemEventsBinding.getRoot());
            binding = listItemEventsBinding;
        }

        void setEventDate(Events events){
            String textDes = "Описание: " + events.getDescription();
            String textTheme = "Для участников: " + events.getTheme();
            String textDate = "Дата: " + events.getDate();
            String textTime = "Время: " + events.getTime();

            SpannableString ssDes = new SpannableString(textDes);
            SpannableString ssTheme = new SpannableString(textTheme);
            SpannableString ssDate = new SpannableString(textDate);
            SpannableString ssTime = new SpannableString(textTime);

            @SuppressLint("ResourceAsColor") ForegroundColorSpan fcsDes = new ForegroundColorSpan(Color.parseColor("#EB5757"));
            @SuppressLint("ResourceAsColor") ForegroundColorSpan fcsTheme = new ForegroundColorSpan(Color.parseColor("#EB5757"));
            @SuppressLint("ResourceAsColor") ForegroundColorSpan fcsDate = new ForegroundColorSpan(Color.parseColor("#EB5757"));
            @SuppressLint("ResourceAsColor") ForegroundColorSpan fcsTime = new ForegroundColorSpan(Color.parseColor("#EB5757"));

            ssDes.setSpan(fcsDes, 0, 9, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            ssTheme.setSpan(fcsTheme, 0, 15, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            ssDate.setSpan(fcsDate, 0, 5, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            ssTime.setSpan(fcsTime, 0, 6, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

            binding.textItemArea.setText(events.getArea());
            binding.textItemDescription.setText(ssDes);
            binding.textItemTheme.setText(ssTheme);
            binding.textItemDate.setText(ssDate);
            binding.textItemTime.setText(ssTime);
            binding.imageEventsItem.setImageBitmap(getUserImage(events.getImage()));
        }

    }

    private Bitmap getUserImage(String encodedImage){
        byte[] bytes = Base64.decode(encodedImage, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
    }
}
