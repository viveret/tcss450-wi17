package com.viveret.pilexa.android.setup;


import android.content.Context;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.viveret.pilexa.android.R;
import com.viveret.pilexa.android.pilexa.PiLexaProxyConnection;
import com.viveret.pilexa.android.pilexa.UserAccount;
import com.viveret.pilexa.android.pilexa.UserAccountFactory;
import com.viveret.pilexa.android.util.AppHelper;

import java.net.MalformedURLException;

/**
 * Used to manually connect to a PiLexa. Useful for if you already know the IP or if the finder doesn't work.
 */
public class ManualPilexaConnectionFragment extends Fragment {
    private OnPilexaServiceSelected myListener;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public ManualPilexaConnectionFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_manual_pilexa_connection, container, false);

        final EditText hostET = (EditText) view.findViewById(R.id.host);
        final EditText portET = (EditText) view.findViewById(R.id.port);

        Button submitBtn = (Button) view.findViewById(R.id.submitBtn);
        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Thread t = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        PiLexaProxyConnection testConn = null;
                        try {
                            String myHost = hostET.getText().toString();
                            int myPort = Integer.parseInt(portET.getText().toString().trim().length() > 0 ? portET.getText().toString() : "11823");
                            testConn = PiLexaProxyConnection.attachTo(myHost, myPort);
                            if (testConn != null) {
                                myListener.onPilexaServiceSelected(testConn);
                            }
                        } catch (MalformedURLException e) {
                            Log.e("Manual pilexa config", Log.getStackTraceString(e));
                        }
                    }
                });
                t.start();
            }
        });

        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnPilexaServiceSelected) {
            myListener = (OnPilexaServiceSelected) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnPilexaServiceSelected");
        }
    }
}
