package com.example.testapp4k.Search;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import com.example.testapp4k.R;

public class RepositoryListAdapter extends ArrayAdapter<String> {
        private static final String TAG = "RepositoryListAdapter";
        private Context mContext;
        private int layoutResource;

        private ArrayList<String> name;
        private ArrayList<String> image;
        private ArrayList<String> descr;
        private ArrayList<String> forks;

        private TextView nameRepository;
        private TextView descriptionRepository;
        private TextView countForksRepository;
        private ImageView imageRepository;


        public RepositoryListAdapter(@NonNull Context context, int resource, @NonNull ArrayList<String> objects,
                                     ArrayList<String> object1, ArrayList<String> object2, ArrayList<String> object3) {
            super(context, resource, objects);
            mContext = context;
            layoutResource = resource;
            this.name = objects;
            this.image = object1;
            this.descr = object2;
            this.forks = object3;
        }


        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

            if(convertView==null) {
                convertView = LayoutInflater.from(mContext).inflate(layoutResource, parent, false);
            }

            nameRepository = convertView.findViewById(R.id.nameRepos);
            descriptionRepository = convertView.findViewById(R.id.descriptionRepos);
            countForksRepository = convertView.findViewById(R.id.countForks);
            imageRepository = convertView.findViewById(R.id.imageRepos);

            nameRepository.setText(name.get(position));
            descriptionRepository.setText(descr.get(position));
            Picasso.get().load(image.get(position)).into(imageRepository);
            countForksRepository.setText(forks.get(position));

            Log.d("DEBUG = ", "finish");
            return convertView;
        }
}
