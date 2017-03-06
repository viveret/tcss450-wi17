package com.viveret.pilexa.android.setup;


import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.viveret.pilexa.android.R;
import com.viveret.pilexa.android.pilexa.PiLexaProxyConnection;

/**
 * A simple {@link Fragment} subclass.
 */
public class SyncingDataFragment extends Fragment {
    public SyncingDataFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle inst) {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_syncing_data, container, false);
    }

}
