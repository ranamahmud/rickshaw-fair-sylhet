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
public class DistanceBased extends Fragment implements OnMapReadyCallback {
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
    private GoogleMap map;
    private MapView mapView;
    private boolean mapReady = false;
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




        mapView = view.findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);
        button = view.findViewById(R.id.getDirectionsButton);
        distanceTextView = view.findViewById(R.id.distance_km);
        fairTextView = view.findViewById(R.id.fair_tk);
        queue = Volley.newRequestQueue(getActivity().getApplicationContext());

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Toast.makeText(getActivity(), "Button Clicked", Toast.LENGTH_LONG).show();
                if (mapReady == true) {
                    if (startPlace == null || finishPlace == null) {
                        Toast.makeText(getContext(), "Enter a starting and Destination address", Toast.LENGTH_SHORT).show();

                    } else {
                        String startingAddress = startPlace.getName().toString();
                        String finalAddress = finishPlace.getName().toString();

                        if ((startingAddress.equals("")) || finalAddress.equals("")) {
                            Toast.makeText(getContext(), "Enter a starting and Destination address", Toast.LENGTH_SHORT).show();
                        } else {
                            map.clear();
                            DrawRoute(map, startPlace, finishPlace);
                            getDistanceInfo(startPlace, finishPlace);
                        }
                    }
                } else {
                    Log.e("map", "Map not ready");
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

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mapReady = true;
        map = googleMap;
        // Toast.makeText(getActivity(), "Map is ready", Toast.LENGTH_LONG).show();

        map = googleMap;
        if (startPlace == null || finishPlace == null) {
            Toast.makeText(getContext(), "Enter a starting and Destination address", Toast.LENGTH_SHORT).show();

        } else {
            String startingAddress = startPlace.getName().toString();
            String finalAddress = finishPlace.getName().toString();

            if ((startingAddress.equals("")) || finalAddress.equals("")) {
                Toast.makeText(getContext(), "Enter a starting and Destination address", Toast.LENGTH_SHORT).show();
            } else {
                map.clear();
                DrawRoute(map, startPlace, finishPlace);
            }

        }


    }

    private void DrawRoute(GoogleMap googleMap, Place start, Place finish) {
        GoogleMap map = googleMap;
        Place startPlace = start;
        Place finishPlace = finish;
        LatLng origin = startPlace.getLatLng();
        LatLng destination = finishPlace.getLatLng();
      //  DrawRouteMaps.getInstance(getContext())
          //      .draw(origin, destination, map);

        String directionApiPath = Helper.getUrl(String.valueOf(origin.latitude), String.valueOf(origin.longitude),
                String.valueOf(destination.latitude), String.valueOf(destination.longitude));
        Log.d(TAG, "Path " + directionApiPath);
        getDirectionFromDirectionApiServer(directionApiPath);

     //   map.moveCamera(CameraUpdateFactory.newLatLng(origin));
      //  map.animateCamera(CameraUpdateFactory.zoomTo(11));

        map.addMarker(new MarkerOptions().position(startPlace.getLatLng()).title(String.valueOf(startPlace.getName())));
        map.addMarker(new MarkerOptions().position(finishPlace.getLatLng()).title(String.valueOf(finishPlace.getName())));



        LatLngBounds bounds = new LatLngBounds.Builder()
                .include(origin)
                .include(destination).build();
        Point displaySize = new Point();
        getActivity().getWindowManager().getDefaultDisplay().getSize(displaySize);

        map.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, displaySize.x, 250, 50));

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

                        String result = " " + dist + " KM";
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



    private void getDirectionFromDirectionApiServer(String url){
        GsonRequest<DirectionObject> serverRequest = new GsonRequest<DirectionObject>(
                Request.Method.GET,
                url,
                DirectionObject.class,
                createRequestSuccessListener(),
                createRequestErrorListener());
        serverRequest.setRetryPolicy(new DefaultRetryPolicy(
                Helper.MY_SOCKET_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        VolleySingleton.getInstance(getContext()).addToRequestQueue(serverRequest);
    }

    private Response.Listener<DirectionObject> createRequestSuccessListener() {
        return new Response.Listener<DirectionObject>() {
            @Override
            public void onResponse(DirectionObject response) {
                try {
                    Log.d("JSON Response", response.toString());
                    if(response.getStatus().equals("OK")){
                        List<LatLng> mDirections = getDirectionPolylines(response.getRoutes());
                        drawRouteOnMap(map, mDirections);
                    }else{
                        Toast.makeText(getActivity(), R.string.server_error, Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            };
        };
    }
    private List<LatLng> getDirectionPolylines(List<RouteObject> routes){
        List<LatLng> directionList = new ArrayList<LatLng>();
        for(RouteObject route : routes){
            List<LegsObject> legs = route.getLegs();
            for(LegsObject leg : legs){
                List<StepsObject> steps = leg.getSteps();
                for(StepsObject step : steps){
                    PolylineObject polyline = step.getPolyline();
                    String points = polyline.getPoints();
                    List<LatLng> singlePolyline = decodePoly(points);
                    for (LatLng direction : singlePolyline){
                        directionList.add(direction);
                    }
                }
            }
        }
        return directionList;
    }
    private Response.ErrorListener createRequestErrorListener() {
        return new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        };
    }
    private void drawRouteOnMap(GoogleMap map, List<LatLng> positions){
        PolylineOptions options = new PolylineOptions().width(5).color(Color.BLUE).geodesic(true);
        options.addAll(positions);
        Polyline polyline = map.addPolyline(options);
      //  CameraPosition cameraPosition = new CameraPosition.Builder()
        //        .target(new LatLng(positions.get(1).latitude, positions.get(1).longitude))
          //      .zoom(10)
            //    .build();
       // map.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
    }
    /**
     * Method to decode polyline points
     * Courtesy : http://jeffreysambells.com/2010/05/27/decoding-polylines-from-google-maps-direction-api-with-java
     * */
    private List<LatLng> decodePoly(String encoded) {
        List<LatLng> poly = new ArrayList<>();
        int index = 0, len = encoded.length();
        int lat = 0, lng = 0;
        while (index < len) {
            int b, shift = 0, result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lat += dlat;
            shift = 0;
            result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lng += dlng;
            LatLng p = new LatLng((((double) lat / 1E5)),
                    (((double) lng / 1E5)));
            poly.add(p);
        }
        return poly;
    }




}
