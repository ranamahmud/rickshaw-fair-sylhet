package com.example.rananoyon.rickshawfairsylhet;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
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

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;


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
    private List<Fair> fairs;
    private FairAdapter adapter;
    private RecyclerView fairList;
    private SQLiteDatabase fairDataBase;
    private Cursor cursor;
    private ArrayList<Fair> fliteredList;

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

        //read data from database

        DataBaseHelper myDbHelper = new DataBaseHelper(getActivity().getApplicationContext());
        try {

            myDbHelper.createDataBase();

        } catch (IOException ioe) {

            throw new Error("Unable to create database");

        }

        try {

            myDbHelper.openDataBase();

        } catch (SQLException sqle) {

            throw sqle;

        }
        cursor = myDbHelper.query("fair", null, null, null, null, null, null);

        // 3. go over each row, build book and add it to list
         fairs = new LinkedList<Fair>();
        Fair fair;
        if (cursor.moveToFirst()) {
            do {

                String id = String.valueOf(cursor.getInt(0));
                fair = new Fair(id,cursor.getString(1),cursor.getString(2),cursor.getString(3),cursor.getString(4));

                Log.e("fair",fair.getLocation());
                // Add book to books
                fairs.add(fair);
            } while (cursor.moveToNext());
        }

        Log.d("getAllBooks()", fairs.toString());



//        data.add(new Fair("rana","rana","three"));
        adapter = new FairAdapter(fairs,getContext());
        fairList.setAdapter(adapter);
        fairList.setLayoutManager(new LinearLayoutManager(getContext()));
        search = view.findViewById(R.id.search);
        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                s = s.toString().toLowerCase(Locale.getDefault());

                 fliteredList = new ArrayList<>();
                if (s.length() == 0) {
                    fliteredList.addAll(fairs);
                }
                else {

                    for (int i = 0; i < fairs.size(); i++) {
                        String text = fairs.get(i).getLocation();
                        String source = fairs.get(i).getSource().toLowerCase();
                        String destination = fairs.get(i).getDestionation().toLowerCase();
                        Log.e("search", text);
                        Log.e("search", s.toString());
                        if (text.contains(s) || source.contains(s) || destination.contains(s)) {
                            fliteredList.add(fairs.get(i));
                        }

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
