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
    ArrayList<Friends> friends;
    ArrayList<Friends> filterList;
    CustomFilter filter;



    public MyFriendsAdapter(Context context, ArrayList<Friends> friends){
        super(context,R.layout.view_myfriends_row,friends);
        this.c = context;
        this.friends = friends;
        this.filterList = friends;
    }

    @Override
    public int getCount() {
        return super.getCount();
    }

    @Override
    public Friends getItem(int position) {
        return super.getItem(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
       /* ViewHolder holder = null;
        if(convertView == null){
            LayoutInflater vi = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = vi.inflate(R.layout.view_myfriends_row, null);
            holder  = new ViewHolder();
            holder.nameTxt = (TextView) convertView.findViewById(R.id.tv_FriendName);
            holder.img = (ImageView) convertView.findViewById(R.id.imgFriend);
            holder.userName =(TextView) convertView.findViewById(R.id.tv_FriendUserName);
            holder.btnRmvFriend = (Button)convertView.findViewById(R.id.btnRemoveFriend);
            convertView.setTag(holder);

        }else {
            holder=(ViewHolder)convertView.getTag();
        }
        Friends f = filterList.get(position);
        holder.nameTxt.setText(f.getFriendName());
        holder.userName.setText(f.getUsername());
        holder.img.setImageResource(f.getImg());
        holder.btnRmvFriend.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                //Snackbar.make(,"Code for Remove Friends Needed",Snackbar.LENGTH_SHORT).show();
            }
        });
        return convertView;*/

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

   /* private static class ViewHolder {
        public TextView nameTxt;
        public TextView userName;
        public ImageView img;
        public Button btnRmvFriend;

    }*/

    @Override
    public Filter getFilter() {
        if(filter == null){
            filter = new CustomFilter();
        }
        return filter;

    }


    class CustomFilter extends Filter{
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {

            FilterResults results = new FilterResults();
            if(constraint != null && constraint.length() > 0){
                constraint = constraint.toString().toUpperCase();
                ArrayList<Friends> filters = new ArrayList<Friends>();
                for(int i =0; i<filterList.size();i++){
                    if(filterList.get(i).getFriendName().toUpperCase().contains(constraint)){
                        Friends f = new Friends(filterList.get(i).getFriendName(),filterList.get(i).getImg(),filterList.get(i).getUsername());
                        filters.add(f);
                    }
                }
                results.count = filters.size();
                results.values = filters;
            }else {
                results.count = filterList.size();
                results.values = filterList;
            }
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            friends = (ArrayList<Friends>)results.values;
            notifyDataSetChanged();

        }
    }

}

