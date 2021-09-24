package com.example.taskdvara;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;


public class DetailsAdapter extends RecyclerView.Adapter<DetailsAdapter.MyViewHolder>
        implements Filterable {
        private Context context;
        private List<User> userList;
        private List<User> userListFiltered;
        private UserAdapterListener listener;

        public class MyViewHolder extends RecyclerView.ViewHolder {
            public TextView phoneNumber, netSpeed;
            public TextView dateTime;

            public MyViewHolder(View view) {
                super(view);
                phoneNumber = view.findViewById(R.id.txtphonenumber);
                netSpeed = view.findViewById(R.id.txtnetspeed);
                dateTime = view.findViewById(R.id.datetime);

                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        // send selected contact in callback
                        listener.onUserSelected(userListFiltered.get(getAdapterPosition()));
                    }
                });
            }
        }


        public DetailsAdapter(Context context, List<User> userList, UserAdapterListener listener) {
            this.context = context;
            this.listener = listener;
            this.userList = userList;
            this.userListFiltered = userList;
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.list_item, parent, false);

            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, final int position) {
            final User contact = userListFiltered.get(position);
            holder.phoneNumber.setText(contact.getPhoneNumber());
            holder.netSpeed.setText(contact.getNetSpeed());
            holder.dateTime.setText(contact.getDateTime());
        }

        @Override
        public int getItemCount() {
            return userListFiltered.size();
        }

        @Override
        public Filter getFilter() {
            return new Filter() {
                @Override
                protected FilterResults performFiltering(CharSequence charSequence) {
                    String charString = charSequence.toString();
                    if (charString.isEmpty()) {
                        userListFiltered = userList;
                    } else {
                        List<User> filteredList = new ArrayList<>();
                        for (User row : userList) {

                            // name match condition. this might differ depending on your requirement
                            // here we are looking for name or phone number match
                            if (row.getPhoneNumber().toLowerCase().contains(charString.toLowerCase()) || row.getNetSpeed().contains(charSequence)) {
                                filteredList.add(row);
                            }
                        }

                        userListFiltered = filteredList;
                    }

                    FilterResults filterResults = new FilterResults();
                    filterResults.values = userListFiltered;
                    return filterResults;
                }

                @Override
                protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                    userListFiltered = (ArrayList<User>) filterResults.values;
                    notifyDataSetChanged();
                }
            };
        }

        public interface UserAdapterListener {
            void onUserSelected(User contact);
        }
    }

