package com.example.runbuddies;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class ProfileAdapter extends ArrayAdapter<Profile> {
    FirebaseStorage storage = FirebaseStorage.getInstance();

    StorageReference storageRef = storage.getReference();
    public ProfileAdapter(Context context, ArrayList<Profile> ProfileList){
        super(context,0,ProfileList);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        Profile myProfile = getItem(position);

        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.activity_list_view,parent,false);
        }

        TextView tvName = (TextView)convertView.findViewById(R.id.nameListviewText);
        TextView tvState = (TextView)convertView.findViewById(R.id.stateListViewText);
        TextView tvCity = (TextView)convertView.findViewById(R.id.cityListViewText);
        ImageView ivPicture = (ImageView)convertView.findViewById(R.id.profileImageView);

        tvName.setText(myProfile.getName());
        tvState.setText("$" + myProfile.getState());
        tvCity.setText(myProfile.getCity());


        return convertView;
    }
}
