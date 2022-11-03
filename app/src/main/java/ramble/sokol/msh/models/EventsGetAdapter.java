package ramble.sokol.msh.models;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import ramble.sokol.msh.R;

public class EventsGetAdapter extends RecyclerView.Adapter<EventsGetAdapter.ViewHolder> {

    private List<RecylerGetEvents> getList;
    Context context;

    public EventsGetAdapter(Context context, List<RecylerGetEvents> getList) {
        this.context = context;
        this.getList = getList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_events, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.itArea.setText(getList.get(position).getArea());
        holder.itDescription.setText(getList.get(position).getDescription());
        holder.itTheme.setText(getList.get(position).getTheme());
        holder.itDate.setText(getList.get(position).getDate());
        holder.itTime.setText(getList.get(position).getTime());
    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView itArea, itDescription, itTheme, itDate, itTime;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            itArea = itemView.findViewById(R.id.textItemArea);
            itDescription = itemView.findViewById(R.id.textItemDescription);
            itTheme = itemView.findViewById(R.id.textItemTheme);
            itDate = itemView.findViewById(R.id.textItemDate);
            itTime = itemView.findViewById(R.id.textItemTime);
        }
    }

}
