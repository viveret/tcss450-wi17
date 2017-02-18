package com.viveret.pilexa.android;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.viveret.pilexa.android.pilexa.Skill;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link SkillFragment.OnListFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link SkillFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SkillFragment extends Fragment {

//    private static final String SKILL_URL
//            = "http://cssgate.insttech.washington.edu/~viveret/pi/config.json";

//    private static final String SKILL_URL
//            = "http://cssgate.insttech.washington.edu/~dwood253/list.php?cmd=courses";

    private static final String SKILL_URL
            = "http://cssgate.insttech.washington.edu/~viveret/pi/pilexa-config.json";


    private RecyclerView mRecyclerView;

    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    // TODO: Customize parameters
    private int mColumnCount = 1;
    private OnListFragmentInteractionListener mListener;




    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public SkillFragment() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static SkillFragment newInstance(int columnCount) {
        SkillFragment fragment = new SkillFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_skill_list, container, false);

        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            mRecyclerView = (RecyclerView) view;
            if (mColumnCount <= 1) {
                mRecyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                mRecyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }
            DownloadSkills task = new DownloadSkills();
            task.execute(new String[]{SKILL_URL});
        }

        FloatingActionButton floatingActionButton = (FloatingActionButton)
                getActivity().findViewById(R.id.fab);
        floatingActionButton.show();

        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnListFragmentInteractionListener) {
            mListener = (OnListFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
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
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnListFragmentInteractionListener {
        void onListFragmentInteraction(Skill item);
    }



    private class DownloadSkills extends AsyncTask<String, Void, List<String>> {
        String firstPartOfUrl = "http://cssgate.insttech.washington.edu/~viveret/pi/skills/";
        String lastPartOfUrl = "/manifest.json";
        @Override
        protected List<String> doInBackground(String... urls) {
            String response = "";
            List<Skill> skills = new LinkedList<Skill>();
            List<String> skillDetailList = new LinkedList<>();

            HttpURLConnection urlConnection = null;
            HttpURLConnection urlConnection2 = null;

            String reason = "";
            for (String url : urls) {
                try {
                    URL urlObject = new URL(url);
                    urlConnection = (HttpURLConnection) urlObject.openConnection();

                    InputStream content = urlConnection.getInputStream();

                    BufferedReader buffer = new BufferedReader(new InputStreamReader(content));
                    String s = "";
                    while ((s = buffer.readLine()) != null) {
                        response += s;
                    }
                    try {
                        JSONObject skillObject = new JSONObject(response);
                        JSONArray skillsArray = (JSONArray) skillObject.get("skills");
                        for (int i = 0; i < skillsArray.length(); i++) {
                            String skillDetail = "";
                            String skillLink = skillsArray.get(i).toString();
                            skillLink = skillLink.replace('.', '/');
                            String newUrl = firstPartOfUrl;
                            newUrl = newUrl.concat(skillLink);
                            newUrl = newUrl.concat(lastPartOfUrl);
                            System.out.println(newUrl);
                            try {
                                URL urlObject2 = new URL(newUrl);
                                urlConnection2 = (HttpURLConnection) urlObject2.openConnection();

                                InputStream content2 = urlConnection2.getInputStream();

                                BufferedReader buffer2 = new BufferedReader(new InputStreamReader(content2));
                                String newS = "";
                                while ((newS = buffer2.readLine()) != null) {
                                    skillDetail += newS;
                                }
                                skillDetailList.add(skillDetail);
                            }
                            catch (Exception e) {
                                response = "Unable to download the list skills, Reason: "
                                        + e.getMessage();
                            }
                            finally {
                                if (urlConnection2 != null)
                                    urlConnection2.disconnect();
                            }

                        }

                    } catch (JSONException e) {
                        reason = "Unable to parse data, Reason: " + e.getMessage();
                    }

                } catch (Exception e) {
                    response = "Unable to download the list skills, Reason: "
                            + e.getMessage();
                }
                finally {
                    if (urlConnection != null)
                        urlConnection.disconnect();
                }
            }
            return skillDetailList;
        }

        @Override
        protected void onPostExecute(List<String> skillDetailList) {
            // Something wrong with the network or the URL.
            String result = "";
//            if (result.startsWith("Unable to")) {
//                Toast.makeText(getActivity().getApplicationContext(), result, Toast.LENGTH_LONG)
//                        .show();
//                return;
//            }

            List<Skill> skillList = new ArrayList<Skill>();
            result = Skill.parseSkillJSON(skillDetailList, skillList);
            // Something wrong with the JSON returned.
            if (result != null) {
                Toast.makeText(getActivity().getApplicationContext(), result, Toast.LENGTH_LONG)
                        .show();
                return;
            }

            // Everything is good, show the list of courses.
            if (!skillList.isEmpty()) {
                mRecyclerView.setAdapter(new MySkillRecyclerViewAdapter(skillList, mListener));
            }
        }
    }
}
