package com.gustlogix.rickandmorty.view.episode;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gustlogix.rickandmorty.R;
import com.gustlogix.rickandmorty.dto.episode.EpisodeResult;

import java.util.List;

public class EpisodesAdapter extends BaseAdapter {

    private List<EpisodeResult> episodes;
    private LayoutInflater inflater;
    private OnItemClickListener onItemClickListener;

    EpisodesAdapter(Context context, List<EpisodeResult> episodes, OnItemClickListener onItemClickListener) {
        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.episodes = episodes;
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    public int getCount() {
        return episodes.size();
    }

    @Override
    public EpisodeResult getItem(int position) {
        return episodes.get(position);
    }

    @Override
    public long getItemId(int position) {
        return episodes.get(position).getId();
    }

    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_episode, null);

            holder = new ViewHolder();
            holder.tvDate = (TextView) convertView.findViewById(R.id.tvDate);
            holder.tvTitle = (TextView) convertView.findViewById(R.id.tvTitle);
            holder.tvEpisode = (TextView) convertView.findViewById(R.id.tvEpisode);
            holder.llContainer = (LinearLayout) convertView.findViewById(R.id.llContainer);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.tvTitle.setText(getItem(position).getName());
        holder.tvDate.setText(getItem(position).getAirDate());
        holder.tvEpisode.setText(getItem(position).getEpisode());
        holder.llContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClickListener.onItemClicked(getItem(position));
            }
        });

        return convertView;
    }

    void addEpisodes(List<EpisodeResult> results) {
        episodes.addAll(results);
        notifyDataSetChanged();
    }

    private static class ViewHolder {
        TextView tvTitle;
        TextView tvEpisode;
        TextView tvDate;
        LinearLayout llContainer;
    }

    interface OnItemClickListener {
        void onItemClicked(EpisodeResult episodeResult);
    }
}
