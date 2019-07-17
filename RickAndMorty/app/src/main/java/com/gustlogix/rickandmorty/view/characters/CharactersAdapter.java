package com.gustlogix.rickandmorty.view.characters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gustlogix.rickandmorty.R;
import com.gustlogix.rickandmorty.dto.character.CharacterResult;
import com.gustlogix.rickandmorty.repo.remote.imagedownloader.ImageDownloader;

import java.util.List;

public class CharactersAdapter extends BaseAdapter {

    private List<CharacterResult> characters;
    private LayoutInflater inflater;
    private OnItemInteractionListener onItemInteractionListener;
    private ImageDownloader imageDownloader;

    CharactersAdapter(Context context, ImageDownloader imageDownloader, List<CharacterResult> characters, OnItemInteractionListener onItemInteractionListener) {
        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.characters = characters;
        this.onItemInteractionListener = onItemInteractionListener;
        this.imageDownloader = imageDownloader;
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
            holder.btnKill = convertView.findViewById(R.id.btnKill);
            holder.imgCharacter = convertView.findViewById(R.id.imgCharacter);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        imageDownloader.loadImage(getItem(position).getImage(), holder.imgCharacter);

        holder.tvName.setText(getItem(position).getName());
        holder.tvSpecies.setText(getItem(position).getSpecies());
        if ((getItem(position).getIsKilledByUser() != null && getItem(position).getIsKilledByUser()) ||
                getItem(position).getStatus().trim().toLowerCase().equals("dead")) {
            holder.llContainer.setBackgroundResource(R.color.colorAccent);
            holder.btnKill.setEnabled(false);
        } else {
            holder.llContainer.setBackgroundResource(R.color.colorPrimary);
            holder.btnKill.setEnabled(true);
        }

        holder.llContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemInteractionListener.onItemClicked(getItem(position));
            }
        });

        holder.btnKill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemInteractionListener.onKillClicked(getItem(position));
            }
        });

        return convertView;
    }

    void updateCharacter(CharacterResult characterResult) {
        for (int i = 0; i < characters.size(); i++) {
            if (characters.get(i).getId().equals(characterResult.getId())) {
                characters.remove(i);
                characters.add(i, characterResult);
                notifyDataSetChanged();
                return;
            }
        }
    }

    private static class ViewHolder {
        ImageView imgCharacter;
        TextView tvName;
        TextView tvSpecies;
        Button btnKill;
        LinearLayout llContainer;
    }

    interface OnItemInteractionListener {
        void onItemClicked(CharacterResult characterResult);

        void onKillClicked(CharacterResult characterResult);
    }
}
