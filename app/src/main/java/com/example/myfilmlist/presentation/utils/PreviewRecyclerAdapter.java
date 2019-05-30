package com.example.myfilmlist.presentation.utils;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.example.myfilmlist.R;
import com.example.myfilmlist.business.film.TFilmPreview;

import java.util.List;

public class PreviewRecyclerAdapter extends RecyclerView.Adapter<PreviewRecyclerAdapter.Holder> {

    private List<TFilmPreview> films;
    private BackListener backListener;

    public PreviewRecyclerAdapter(List<TFilmPreview> list) {
        this.films = list;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View previewView = inflater.inflate(R.layout.film_preview_layout, null, false);
        return new Holder(previewView, backListener);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder viewHolder, int i) {
        viewHolder.addData(films.get(i));
    }

    @Override
    public int getItemCount() {
        return films.size();
    }

    public void setBackListener(BackListener backListener){
        this.backListener = backListener;
    }

    public static class Holder extends RecyclerView.ViewHolder {

        TextView title;
        TextView type;
        TextView year;
        ImageView poster;
        BackListener backListener;

        public Holder(@NonNull View itemView,final BackListener backListener) {
            super(itemView);
            title = itemView.findViewById(R.id.titlePreview);
            type = itemView.findViewById(R.id.typePreview);
            year = itemView.findViewById(R.id.yearPreview);
            poster = itemView.findViewById(R.id.posterPreview);
            this.backListener = backListener;
            ImageButton button = itemView.findViewById(R.id.imageButtonAdd);
            button.setVisibility(View.GONE);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    backListener.onBackClicked(getAdapterPosition());
                }
            });
        }

        public void addData(TFilmPreview preview){
            title.setText(preview.getTitle());
            type.setText(preview.getType());
            year.setText(preview.getYear());

            Glide.with(itemView)
                    .load(preview.getImageURL())
                    .transforms(new RoundedCorners(10))
                    .error(R.drawable.logo)
                    .placeholder(R.drawable.logo)
                    .into(poster);
        }

    }
}
