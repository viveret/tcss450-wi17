package com.viveret.pilexa.android;

import android.content.Context;
import android.os.Bundle;
import android.app.Fragment;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.viveret.pilexa.android.pilexa.PiLexaProxyConnection;
import com.viveret.pilexa.android.pilexa.UserAccount;
import com.viveret.pilexa.android.pilexa.UserAccountFactory;
import com.viveret.pilexa.android.util.AppHelper;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnLoginOrCreateAcctListener} interface
 * to handle interaction events.
 */
public class LoginOrCreateAcctFragment extends Fragment {
    private OnLoginOrCreateAcctListener mListener;
    private PiLexaProxyConnection.PiLexaProxyConnectionHolder myConnection;

    public LoginOrCreateAcctFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_login_or_create_acct, container, false);

        final EditText usernameET = (EditText) view.findViewById(R.id.username);
        final EditText passwordET = (EditText) view.findViewById(R.id.password);

        Button loginBtn = (Button) view.findViewById(R.id.loginBtn);
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Thread t = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        UserAccountFactory factory = new UserAccountFactory(myConnection.getPilexa());
                        try {
                            UserAccount account = factory.loginWithUsernameAndPassword(
                                    usernameET.getText().toString(),
                                    passwordET.getText().toString(),
                                    AppHelper.getMacAddress(getActivity()));
                            mListener.onUserLogin(account);
                        } catch (final Exception e) {
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_LONG).show();
                                }
                            });
                        }
                    }
                });
                t.start();
            }
        });

        Button registerBtn = (Button) view.findViewById(R.id.registerBtn);
        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Thread t = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        UserAccountFactory factory = new UserAccountFactory(myConnection.getPilexa());
                        try {
                            UserAccount account = factory.registerWithUsernameAndPassword(
                                    usernameET.getText().toString(),
                                    passwordET.getText().toString(),
                                    AppHelper.getMacAddress(getActivity()));
                            mListener.onUserLogin(account);
                        } catch (final Exception e) {
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_LONG).show();
                                }
                            });
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
        if (context instanceof OnLoginOrCreateAcctListener) {
            if (context instanceof PiLexaProxyConnection.PiLexaProxyConnectionHolder) {
                mListener = (OnLoginOrCreateAcctListener) context;
                myConnection = (PiLexaProxyConnection.PiLexaProxyConnectionHolder) context;
                try {
                    UserAccount account = UserAccountFactory.getCachedUser(PreferenceManager.getDefaultSharedPreferences(getActivity()));
                    if (account != null) {
                        mListener.onUserLogin(account);
                    }
                } catch (Exception e) {
                }
            } else {
                throw new RuntimeException(context.toString()
                        + " must implement PiLexaProxyConnection.PiLexaProxyConnectionHolder");
            }
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnLoginOrCreateAcctListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnLoginOrCreateAcctListener {
        void onUserLogin(UserAccount user);
    }
}
