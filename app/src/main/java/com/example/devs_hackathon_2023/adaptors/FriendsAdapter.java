package com.example.devs_hackathon_2023.adaptors;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.devs_hackathon_2023.R;
import com.example.devs_hackathon_2023.User.Database;
import com.example.devs_hackathon_2023.User.Player;

import org.w3c.dom.Text;

import java.util.List;

public class FriendsAdapter extends ArrayAdapter<Player> {

    private Context context;
    private int resource;
    private FriendDeleteListener friendDeleteListener;

    public FriendsAdapter(@NonNull Context context, int resource, @NonNull List<Player> objects) {
        super(context, resource, objects);
        this.context = context;
        this.resource = resource;
    }

    public void setFriendDeleteListener(FriendDeleteListener friendDeleteListener) {
        this.friendDeleteListener = friendDeleteListener;
    }

    @NonNull
    @Override
    public View getView(int position, @NonNull View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(resource, parent, false);
        }

        TextView friendName = convertView.findViewById(R.id.friendName);
        TextView friendLevel = convertView.findViewById(R.id.friendLevel);

        Player player = getItem(position);

        friendName.setText(player.getName());
        friendLevel.setText("Level " + player.getLevel());

        ImageView deleteFriend = convertView.findViewById(R.id.removeFriend);
        deleteFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Confirmation");
                builder.setMessage("Are you sure you want to remove " + player.getName() + " ?");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        remove(player);
                        Database.removePlayer(player);
                        friendDeleteListener.onFriendDelete();
                    }
                });
                builder.setNegativeButton("No", null);
                AlertDialog dialog = builder.create();
                dialog.show();

            }
        });
        return convertView;
    }
}
