package com.learning.Clock_app;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import Clock_app.R;

public class StopperPinnedAdapter extends RecyclerView.Adapter<StopperPinnedAdapter.MyViewHolder> {

    private final Context context;
    private final List<String> pinnedList;

    public StopperPinnedAdapter(Context ct, List<String> pinnedList) {
        this.context = ct;
        this.pinnedList = pinnedList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.stopper_pinned, parent, false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.textView.setText(pinnedList.get(position));
    }

    @Override
    public int getItemCount() {
        return pinnedList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        private final TextView textView;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.pinned_layout_txt);
        }
    }
}
