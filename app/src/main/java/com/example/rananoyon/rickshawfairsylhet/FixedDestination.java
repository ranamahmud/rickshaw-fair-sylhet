package com.example.rananoyon.rickshawfairsylhet;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link FixedDestination.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link FixedDestination#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FixedDestination extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
    private EditText search;
    private List<Fair> data;
    private FairAdapter adapter;
    private RecyclerView fairList;


    public FixedDestination() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FixedDestination.
     */
    // TODO: Rename and change types and number of parameters
    public static FixedDestination newInstance(String param1, String param2) {
        FixedDestination fragment = new FixedDestination();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_fixed_destination, container, false);

        //inserting elements in list
        fairList = (RecyclerView) view.findViewById(R.id.list);
        data = new ArrayList<>();
        //read data from file
        InputStream fairData = getContext().getResources().openRawResource(R.raw.fair);

        String fairLine;
        try {

            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(fairData));
            while ((fairLine = bufferedReader.readLine()) != null) {
                String[] row = fairLine.split("\t");
              //  data.add(new Fair(row[0],row[1],row[2]));

                Log.e("row", String.valueOf(fairLine));
                //word = new Word(row[0], row[1]);
                //wordList.add(word);
            }
            Log.e("row","success");
        } catch (IOException e) {
            Log.e("row","failure)");

        }

        // data = fill_with_data();

//        data.add(new Fair("one","two","three"));
//        data.add(new Fair("one","two","three"));
//        data.add(new Fair("one","two","three"));
//        data.add(new Fair("one","two","three"));
//        data.add(new Fair("rana","rana","three"));
        adapter = new FairAdapter(data,getContext());
        fairList.setAdapter(adapter);
        fairList.setLayoutManager(new LinearLayoutManager(getContext()));
        search = view.findViewById(R.id.search);
        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                s = s.toString().toLowerCase();

                final List<Fair> fliteredList = new ArrayList<>();


                for (int i = 0; i<data.size();i++){
                    final String text = data.get(i).getLocation();
                    Log.e("search",text);
                    Log.e("search",s.toString());
                    if(text.contains(s))
                    {
                        fliteredList.add(data.get(i));
                    }

                }

                fairList.setLayoutManager(new LinearLayoutManager(getContext()));
                adapter = new FairAdapter(fliteredList,getContext());
                fairList.setAdapter(adapter);
                adapter.notifyDataSetChanged();


            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });


        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
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
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }


}
