package com.viveret.pilexa.android.setup;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.Button;
import com.viveret.pilexa.android.R;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnWelcomeInteractionListener} interface
 * to handle interaction events.
 */
public class WelcomeToTheWizardFragment extends Fragment {
    private OnWelcomeInteractionListener mListener;

    public WelcomeToTheWizardFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_welcome_to_the_wizard, container, false);

        Button easyBtn = (Button) view.findViewById(R.id.easyBtn);
        easyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onEasyWizardSelected();
            }
        });

        Button manualBtn = (Button) view.findViewById(R.id.manualBtn);
        manualBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onManualWizardSelected();
            }
        });

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnWelcomeInteractionListener) {
            mListener = (OnWelcomeInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnWelcomeInteractionListener");
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
    public interface OnWelcomeInteractionListener {
        void onManualWizardSelected();
        void onEasyWizardSelected();
    }
}
