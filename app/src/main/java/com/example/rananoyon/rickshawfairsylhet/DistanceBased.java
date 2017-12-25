package com.example.rananoyon.rickshawfairsylhet;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Point;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Cache;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Network;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.location.places.ui.SupportPlaceAutocompleteFragment;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link DistanceBased.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link DistanceBased#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DistanceBased extends Fragment   {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String TAG = "Place";
    private static final String API_KEY = "AIzaSyDKb5fdOHXBMKWZq7S9D09rFzkOyEmi7lE";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    private Button button;
    private Place startPlace;
    private Place finishPlace;
    private TextView distanceTextView;
    private TextView fairTextView;
    private RequestQueue queue;
    private JSONObject resultJson;
    private double dist;
    private double totalFair;
    public DistanceBased() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment DistanceBased.
     */
    // TODO: Rename and change types and number of parameters
    public static DistanceBased newInstance(String param1, String param2) {
        DistanceBased fragment = new DistanceBased();
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
        View view = inflater.inflate(R.layout.fragment_distance_based, container, false);
        // Inflate the layout for this fragment





        button = view.findViewById(R.id.getDirectionsButton);
        distanceTextView = view.findViewById(R.id.distance_km);
        fairTextView = view.findViewById(R.id.fair_tk);
        queue = Volley.newRequestQueue(getActivity().getApplicationContext());

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Toast.makeText(getActivity(), "Button Clicked", Toast.LENGTH_LONG).show();

                    if (startPlace == null || finishPlace == null) {
                        Toast.makeText(getContext(), "Enter a starting and Destination address", Toast.LENGTH_SHORT).show();

                    } else {
                        String startingAddress = startPlace.getName().toString();
                        String finalAddress = finishPlace.getName().toString();

                        if ((startingAddress.equals("")) || finalAddress.equals("")) {
                            Toast.makeText(getContext(), "Enter a starting and Destination address", Toast.LENGTH_SHORT).show();
                        } else {

                            getDistanceInfo(startPlace, finishPlace);
                        }
                    }

            }
        });
        SupportPlaceAutocompleteFragment startLocationAutocompleteFragment = (SupportPlaceAutocompleteFragment)
                getChildFragmentManager().findFragmentById(R.id.startLocation);
        startLocationAutocompleteFragment.setHint("Search Start");

        startLocationAutocompleteFragment.setBoundsBias(new LatLngBounds(
                new LatLng(23.9911, 90.9124),
                new LatLng(25.2078, 92.4985)
                ));
        startLocationAutocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {

            @Override
            public void onPlaceSelected(Place place) {
                // TODO: Get info about the selected place.
                Log.i(TAG, "Place: " + place.getName());
                // Toast.makeText(getContext(), "Place "+place.getName(), Toast.LENGTH_SHORT).show();
                startPlace = place;

            }

            @Override
            public void onError(Status status) {
                // TODO: Handle the error.
                Log.i(TAG, "An error occurred: " + status);
                startPlace = null;
            }
        });


        SupportPlaceAutocompleteFragment finishLocationAutocompleteFragment = (SupportPlaceAutocompleteFragment)
                getChildFragmentManager().findFragmentById(R.id.finishLocation);
        finishLocationAutocompleteFragment.setHint("Search Destination");
        finishLocationAutocompleteFragment.setBoundsBias(new LatLngBounds(

        new LatLng(23.9911, 90.9124),
                new LatLng(25.2078, 92.4985)

        ));
        finishLocationAutocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {

            @Override
            public void onPlaceSelected(Place place) {
                // TODO: Get info about the selected place.
                //Toast.makeText(getContext(), "Place "+place.getName(), Toast.LENGTH_SHORT).show();
                Log.i(TAG, "Place: " + place.getName());
                finishPlace = place;
            }

            @Override
            public void onError(Status status) {
                // TODO: Handle the error.
                Log.i(TAG, "An error occurred: " + status);
                startPlace = null;
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

    private void getDistanceInfo(Place startPlace, Place finishPlace) {
        double latFrom = startPlace.getLatLng().latitude;
        double lngFrom = startPlace.getLatLng().longitude;
        double latTo = finishPlace.getLatLng().latitude;
        double lngTo = finishPlace.getLatLng().longitude;
        StringBuilder mJsonResults = new StringBuilder();


        String urlCall = "https://maps.googleapis.com/maps/api/directions/json?origin=" + latFrom
                + "," + lngFrom + "&destination=" + latTo + "," + lngTo + "&mode=driving&sensor=false&key=" + API_KEY;
        Log.e("response", urlCall);
        // Setup 1 MB disk-based cache for Volley
        Cache cache = new DiskBasedCache(getActivity().getCacheDir(), 1024 * 1024);

        // Use HttpURLConnection as the HTTP client
        Network network = new BasicNetwork(new HurlStack());
        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.GET, urlCall, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e("response", response.toString());
                        resultJson = response;
                        if (resultJson != null) {
                            JSONObject jsonObject;
                            try {

                                jsonObject = new JSONObject(resultJson.toString());

                                JSONArray array = jsonObject.getJSONArray("routes");

                                JSONObject routes = array.getJSONObject(0);

                                JSONArray legs = routes.getJSONArray("legs");

                                JSONObject steps = legs.getJSONObject(0);

                                JSONObject distance = steps.getJSONObject("distance");

                                dist = Double.parseDouble(distance.getString("text").replaceAll("[^\\.0123456789]", ""));
                                Log.i("response", distance.toString());


                            } catch (JSONException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            }
                        }

                        String result = " " + dist + " km";
                        distanceTextView.setText(result);
                        totalFair = 0;
                        if(dist!=0){
                            if(dist<=3){
                                totalFair = dist*10;
                            }else{
                                dist-=3;
                                totalFair+=30;
                                totalFair = totalFair+dist*7;
                            }
                            fairTextView.setText(" "+String.format("%.2f", totalFair )+" Taka");
                        }
                        Log.e("response", result);

                        //  mTxtDisplay.setText("Response: " + response.toString());
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO Auto-generated method stub
                        Log.e("response", "Error");
                        resultJson = null;
                        distanceTextView.setText("0.0 KM");

                    }
                });

        queue = Volley.newRequestQueue(getContext());
        queue.add(jsObjRequest);
        // Toast.makeText(getContext(), mJsonResults, Toast.LENGTH_SHORT).show();
        Log.e("distance", String.valueOf(mJsonResults));


    }

}
