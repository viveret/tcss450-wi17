package com.viveret.pilexa.android.setup;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.viveret.pilexa.android.R;
import com.viveret.pilexa.android.pilexa.PiLexaProxyConnection;

import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link String} and makes a call to the
 * specified {@link OnPilexaServiceSelected}.
 * TODO: Replace the implementation with code for your data type.
 */
public class MyPiLexaConnRecyclerViewAdapter extends RecyclerView.Adapter<MyPiLexaConnRecyclerViewAdapter.ViewHolder> {

    private final List<PiLexaProxyConnection> mValues;
    private final OnPilexaServiceSelected mListener;

    public MyPiLexaConnRecyclerViewAdapter(List<PiLexaProxyConnection> items, OnPilexaServiceSelected listener) {
        mValues = items;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.pilexa_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        // holder.mIdView.setText(mValues.get(position));
        try {
            holder.mContentView.setText(mValues.get(position).getConfigString("system.name"));
        } catch (Exception e) {
            holder.mContentView.setText(e.getMessage());
        }

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onPilexaServiceSelected(holder.mItem);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        // public final TextView mIdView;
        public final TextView mContentView;
        public PiLexaProxyConnection mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            // mIdView = (TextView) view.findViewById(R.id.id);
            mContentView = (TextView) view.findViewById(R.id.content);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mContentView.getText() + "'";
        }
    }
}
