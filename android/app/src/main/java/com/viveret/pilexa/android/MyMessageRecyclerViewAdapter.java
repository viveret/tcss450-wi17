package com.viveret.pilexa.android;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.viveret.pilexa.android.util.Message;

import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link Message}.
 * TODO: Replace the implementation with code for your data type.
 */
public class MyMessageRecyclerViewAdapter extends RecyclerView.Adapter<MyMessageRecyclerViewAdapter.ViewHolder> {
    private static final int USER = 0, OTHER = 1;

    private final List<Message> mValues;

    public MyMessageRecyclerViewAdapter(List<Message> items) {
        mValues = items;
    }

    //Returns the view type of the item at position for the purposes of view recycling.
    @Override
    public int getItemViewType(int position) {
        if (mValues.get(position).isFromUser) {
            return USER;
        } else {
            return OTHER;
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewHolder viewHolder = null;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        switch (viewType) {
            case USER:
                View v1 = inflater.inflate(R.layout.message_item, parent, false);
                viewHolder = new ViewHolder(v1);
                break;
            case OTHER:
                View v2 = inflater.inflate(R.layout.message_item_other, parent, false);
                viewHolder = new ViewHolder(v2);
                break;
        }


        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.mIdView.setText(mValues.get(position).isFromUser ? "You" : "PiLexa");
        holder.mContentView.setText(mValues.get(position).body);

//        holder.mView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (null != mListener) {
//                    // Notify the active callbacks interface (the activity, if the
//                    // fragment is attached to one) that an item has been selected.
//                    mListener.onListFragmentInteraction(holder.mItem);
//                }
//            }
//        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mIdView;
        public final TextView mContentView;
        public Message mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mIdView = (TextView) view.findViewById(R.id.id);
            mContentView = (TextView) view.findViewById(R.id.content);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mContentView.getText() + "'";
        }
    }
}
