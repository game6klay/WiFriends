package com.example.tejasshah.wifriends;

import android.content.Context;
import android.content.Intent;
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
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.tejasshah.wifriends.models.Friends;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Tejas Shah on 5/5/2016.
 */
public class MyFriendsAdapter extends ArrayAdapter<Friends> implements Filterable{
    Context c;
    ArrayList<Friends> orig_friends;
    ArrayList<Friends> filterList;
    CustomFilter filter;



    public MyFriendsAdapter(Context context, ArrayList<Friends> friends){
        super(context,R.layout.view_myfriends_row,friends);
        this.c = context;
        this.orig_friends = friends;
        this.filterList = friends;
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
        final Friends friend = getItem(position);

        TextView nameTxt=(TextView) customView.findViewById(R.id.tv_FriendName);
        TextView tvUserName = (TextView) customView.findViewById(R.id.tv_FriendUserName);
        ImageView img=(ImageView) customView.findViewById(R.id.imgFriend);
        Button btnRmvFriend = (Button)customView.findViewById(R.id.btnRemoveFriend);

        //SET DATA TO THEM
        nameTxt.setText(friend.getFriendName());
        tvUserName.setText(friend.getUsername());
        img.setImageResource(friend.getImg());

        btnRmvFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO
                Response.Listener<String> responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonResponse = new JSONObject(response);
                            boolean success = jsonResponse.getBoolean("success");
                            if (success) {
                                //Toast.makeText(c, "Logged In Successfully !!!", Toast.LENGTH_SHORT).show();
                                //String name = jsonResponse.getString("name");
                                //String email = jsonResponse.getString("email");
                                //System.out.println(email);
                                //Intent intent = new Intent(LoginActivity.this, Home.class);
                                //intent.putExtra("name", name);
                                //intent.putExtra("username", username);
                                //intent.putExtra("email", email);
                                //startActivity(intent);
                                //finish();
                                MyFriendsActivity myFriendsActivity = new MyFriendsActivity();
                                myFriendsActivity.getAdapt_myFriends().notifyDataSetChanged();

                            } else {

                                Snackbar.make(customView,"Failed to Add Friend",Snackbar.LENGTH_LONG).show();
                                    /*AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                                    builder.setMessage("Login Failed")
                                            .setNegativeButton("Retry",null)
                                            .create()
                                            .show();*/
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                };


                // Enter Remove Friend Code here...
                //Snackbar.make(customView,"Code for Remove Friends Needed",Snackbar.LENGTH_SHORT).show();

                RemoveFriend removeFriend = new RemoveFriend(MyFriendsActivity.getUsername(), friend.getUsername(), responseListener);
                RequestQueue queue = Volley.newRequestQueue(c);
                queue.add(removeFriend);
            }
        });

        return customView;
    }

    private class RemoveFriend extends StringRequest {
        private static final String REMOVE_FRIEND_URL = "http://selvinphp.netau.net/delFriend.php";
        private Map<String, String> params;

        public RemoveFriend( String username, String friendUserName, Response.Listener<String> listener){
            super (Request.Method.POST, REMOVE_FRIEND_URL, listener, null);
            params = new HashMap<>();
            params.put("username",username);
            params.put("delFriend",friendUserName);

        }

        @Override
        public Map<String, String> getParams() {
            return params;
        }
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
                for(int i =0; i<orig_friends.size();i++){
                    Friends filtered_friend = orig_friends.get(i);
                    if(filtered_friend.getFriendName().toUpperCase().contains(constraint)){
                        filters.add(filtered_friend);
                    }
                }
                results.count = filters.size();
                results.values = filters;
            }else {
                synchronized (this){
                    results.values = orig_friends;
                    results.count = orig_friends.size();

                }

            }
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            filterList = (ArrayList<Friends>)results.values;
            notifyDataSetChanged();

        }
    }

}

