package com.example.tejasshah.wifriends;

import android.content.Context;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.tejasshah.wifriends.models.Friends;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Tejas Shah on 5/5/2016.
 */
public class MyFriendsAdapter extends ArrayAdapter<Friends> implements Filterable{
    Context c;
    List<Friends> fList;
    List<Friends> orig_fList;
    private Filter friendsFilter;


    public MyFriendsAdapter(Context context, List<Friends> friends){
        super(context,R.layout.view_myfriends_row,friends);
        this.c = context;
        this.fList = friends;
        this.orig_fList = friends;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater listFriends_view = LayoutInflater.from(getContext());
        final View customView = listFriends_view.inflate(R.layout.view_myfriends_row,null);
        final Friends friends = getItem(position);

        TextView nameTxt=(TextView) customView.findViewById(R.id.tv_FriendName);
        TextView tvUserName = (TextView) customView.findViewById(R.id.tv_FriendUserName);
        ImageView img=(ImageView) customView.findViewById(R.id.imgFriend);
        Button btnRmvFriend = (Button)customView.findViewById(R.id.btnRemoveFriend);

        //SET DATA TO THEM
        nameTxt.setText(friends.getFriendName());
        tvUserName.setText(friends.getUsername());
        img.setImageResource(friends.getImg());

        btnRmvFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO
                // Enter Remove Friend Code here...
                Snackbar.make(customView,"Code for Remove Friends Needed",Snackbar.LENGTH_SHORT).show();
            }
        });

        return customView;
    }

    @Override
    public Filter getFilter() {
        if(friendsFilter == null)
            friendsFilter = new MyFriendsFilter();
        return friendsFilter;
    }

    private class MyFriendsFilter extends Filter{
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults();
            if(constraint == null || constraint.length() == 0){
                results.values = orig_fList;
                results.count = orig_fList.size();
            }else {
                List<Friends> filter_fList = new ArrayList<Friends>();
                for(Friends f: fList){
                    if(f.getFriendName().toLowerCase().startsWith(constraint.toString().toLowerCase()))
                        filter_fList.add(f);
                }
                results.values = filter_fList;
                results.count = filter_fList.size();
            }
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            if(results.count == 0)
                notifyDataSetInvalidated();
            else {
                fList = (List<Friends>)results.values;
                notifyDataSetChanged();
            }

        }
    }
}

