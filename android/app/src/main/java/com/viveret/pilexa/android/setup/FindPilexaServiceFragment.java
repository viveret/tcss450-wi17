package com.viveret.pilexa.android.setup;

import android.content.Context;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import com.viveret.pilexa.android.R;
import com.viveret.pilexa.android.pilexa.PiLexaFinder;
import com.viveret.pilexa.android.pilexa.PiLexaProxyConnection;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link PiLexaFinder.OnPilexaServiceFinderListener}
 * interface.
 */
public class FindPilexaServiceFragment extends Fragment implements PiLexaFinder.OnPilexaServiceFinderListener {
    private OnPilexaServiceSelected mListener;
    private PiLexaFinder myFinder;
    private List<PiLexaProxyConnection> myConnections;
    private RecyclerView.Adapter myAdapter;
    private View myWhileSearchingView;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public FindPilexaServiceFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pilexaconn_list, container, false);
        myWhileSearchingView = view.findViewById(R.id.whileSearching);

        createPilexaFinder(view);

        // Set the adapter
        View listView = view.findViewById(R.id.list);
        if (listView instanceof RecyclerView) {
            Context context = listView.getContext();
            RecyclerView recyclerView = (RecyclerView) listView;
            recyclerView.setLayoutManager(new LinearLayoutManager(context));
            myAdapter = new MyPiLexaConnRecyclerViewAdapter(myConnections, mListener);
            recyclerView.setAdapter(myAdapter);
            myFinder.startSearch();
        }
        return view;
    }

    private void createPilexaFinder(View view) {
        EditText portET = (EditText) view.findViewById(R.id.port);
        String portStr = portET.getText().toString();
        if (portStr.trim().length() == 0) {
            portStr = getString(R.string.default_port);
        }

        String localAddress = findLocalAddress();
        myConnections = new ArrayList<>();
        myFinder = new PiLexaFinder(Integer.parseInt(portStr), localAddress, getActivity(), this);
    }

    private String findLocalAddress() {
        try {
            Enumeration<NetworkInterface> interfaces;
            for (interfaces = NetworkInterface.getNetworkInterfaces(); interfaces.hasMoreElements();) {
                NetworkInterface netInf = interfaces.nextElement();
                Enumeration<InetAddress> addrs;
                for (addrs = netInf.getInetAddresses(); addrs.hasMoreElements();) {
                    InetAddress addr = addrs.nextElement();
                    if (!addr.isLoopbackAddress() && addr.getHostAddress().contains(".")) {
                        String tmp = addr.getHostAddress().toString();
                        if (tmp.startsWith("/")) {
                            tmp = tmp.substring(1);
                        }
                        return tmp;
                    }
                }
            }
        } catch (SocketException e) {
            e.printStackTrace();
        }
        return null;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnPilexaServiceSelected) {
            mListener = (OnPilexaServiceSelected) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnPilexaServiceSelected");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
        myFinder.stopSearch();
    }

    @Override
    public void onPilexaServiceFound(PiLexaProxyConnection connection) {
        myConnections.add(connection);
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                myAdapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    public void onPilexaFinderDoneSearching() {
        myWhileSearchingView.setVisibility(View.INVISIBLE  );
    }

    public interface OnPilexaServiceSelected {
        void onPilexaServiceSelected(PiLexaProxyConnection connection);
    }
}
