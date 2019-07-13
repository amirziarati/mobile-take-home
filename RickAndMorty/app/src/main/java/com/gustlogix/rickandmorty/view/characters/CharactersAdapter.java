package com.gustlogix.rickandmorty.view.characters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gustlogix.rickandmorty.R;
import com.gustlogix.rickandmorty.dto.character.CharacterResult;

import java.util.List;

public class CharactersAdapter extends BaseAdapter {

    private List<CharacterResult> characters;
    private LayoutInflater inflater;
    private OnItemClickListener onItemClickListener;

    CharactersAdapter(Context context, List<CharacterResult> characters, OnItemClickListener onItemClickListener) {
        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.characters = characters;
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    public int getCount() {
        return characters.size();
    }

    @Override
    public CharacterResult getItem(int position) {
        return characters.get(position);
    }

    @Override
    public long getItemId(int position) {
        return characters.get(position).getId();
    }

    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_character, null);

            holder = new ViewHolder();
            holder.tvSpecies = convertView.findViewById(R.id.tvSpecies);
            holder.tvName = convertView.findViewById(R.id.tvName);
            holder.llContainer = convertView.findViewById(R.id.llContainer);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.tvName.setText(getItem(position).getName());
        holder.tvSpecies.setText(getItem(position).getSpecies());
        if (getItem(position).getStatus().trim().toLowerCase().equals("dead"))
            holder.llContainer.setBackgroundResource(R.color.colorAccent);
        else
            holder.llContainer.setBackgroundResource(R.color.colorPrimary);

        holder.llContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClickListener.onItemClicked(getItem(position));
            }
        });

        return convertView;
    }

    private static class ViewHolder {
        TextView tvName;
        TextView tvSpecies;
        LinearLayout llContainer;
    }

    interface OnItemClickListener {
        void onItemClicked(CharacterResult characterResult);
    }
}
