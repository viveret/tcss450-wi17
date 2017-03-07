package com.viveret.pilexa.android;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.viveret.pilexa.android.util.UserAccount;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnLoginOrCreateAcctListener} interface
 * to handle interaction events.
 */
public class LoginOrCreateAcctFragment extends Fragment {
    private OnLoginOrCreateAcctListener mListener;

    public LoginOrCreateAcctFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_login_or_create_acct, container, false);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnLoginOrCreateAcctListener) {
            mListener = (OnLoginOrCreateAcctListener) context;
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
