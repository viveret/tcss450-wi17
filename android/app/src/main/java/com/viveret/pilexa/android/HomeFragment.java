package com.viveret.pilexa.android;


import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import com.viveret.pilexa.android.util.Message;
import com.viveret.pilexa.android.pilexa.PiLexaProxyConnection;
import com.viveret.pilexa.android.util.MessageCache;

import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {
    private static final String LOGTAG = "HomeFragment";

    private PiLexaProxyConnection.PiLexaProxyConnectionHolder myPiLexaHolder;
    private MyMessageRecyclerViewAdapter myViewAdapter;
    private List<Message> myMessages;
    private MessageCache messageCache;

    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.list);
        LinearLayoutManager lm = new LinearLayoutManager(view.getContext());
        lm.setReverseLayout(true);
        recyclerView.setLayoutManager(lm);
        myViewAdapter = new MyMessageRecyclerViewAdapter(myMessages);
        recyclerView.setAdapter(myViewAdapter);

        final EditText et = (EditText) view.findViewById(R.id.inputMsg);
        if (et != null) {
            et.setOnEditorActionListener(new EditText.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                    if (actionId == EditorInfo.IME_ACTION_SEND) {
                        sendFromUserMessage(et.getText().toString());
                        et.setText("");
                        return true;
                    }
                    return false;
                }
            });
        }

        return view;
    }

    private void sendFromUserMessage(String text) {
        Message newMsg = new Message(-1, text, true);
        myMessages.add(0, newMsg);
        messageCache.insert(newMsg);
        myViewAdapter.notifyDataSetChanged();
        new SendMessageTask().execute(new String[]{text});

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof PiLexaProxyConnection.PiLexaProxyConnectionHolder) {
            myPiLexaHolder = (PiLexaProxyConnection.PiLexaProxyConnectionHolder) context;
            messageCache = new MessageCache(getActivity());
            myMessages = messageCache.getMessages();
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement PiLexaProxyConnectionHolder");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        myPiLexaHolder = null;
        messageCache = null;
        myMessages = null;
    }

    public void clearMessages() {
        myMessages.clear();
        messageCache.clearMessages();
        myViewAdapter.notifyDataSetChanged();
    }


    private class SendMessageTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... msgs) {
            String response = "";
            for (String msg : msgs) {
                try {
                    response = myPiLexaHolder.getPilexa().sendMessage(msg);
                } catch (Exception e) {
                    Log.e(LOGTAG, Log.getStackTraceString(e));
                }
            }
            return response;
        }

        @Override
        protected void onPostExecute(String result) {
            Log.d(LOGTAG, result);
            String name = "cpu";
            try {
                name = myPiLexaHolder.getPilexa().getConfigString("system.name");
            } catch (Exception e) {
                e.printStackTrace();
            }

            Message newMsg = new Message(-1, result, false);
            myMessages.add(0, newMsg);
            messageCache.insert(newMsg);
            myViewAdapter.notifyDataSetChanged();
        }
    }
}
