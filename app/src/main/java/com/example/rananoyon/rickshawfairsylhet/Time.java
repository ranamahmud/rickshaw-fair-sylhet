package com.example.rananoyon.rickshawfairsylhet;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Time.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Time#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Time extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String TAG = "Time";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    private long startTime;
    private long elapsedTime;
    private final int REFRESH_RATE = 100;
    private boolean stopped;
    private Handler mHandler = new Handler();
   // private FirebaseAuth mAuth;
    private double fair = 50.00;


    public Time() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Time.
     */
    // TODO: Rename and change types and number of parameters
    public static Time newInstance(String param1, String param2) {
        Time fragment = new Time();
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
        View inflatedView = inflater.inflate(R.layout.fragment_time, container, false);

        // [START initialize_auth]
      //  mAuth = FirebaseAuth.getInstance();
// [END initialize_auth
        // Check if user is signed in (non-null) and update UI accordingly.
       // FirebaseUser currentUser = mAuth.getCurrentUser();
//        if(currentUser == null) {
//            //Sign in anonymously
//
//            mAuth.signInAnonymously()
//                    .addOnCompleteListener((Executor) this, new OnCompleteListener<AuthResult>() {
//                        @Override
//                        public void onComplete(@NonNull Task<AuthResult> task) {
//                            if (task.isSuccessful()) {
//                                // Sign in success, update UI with the signed-in user's information
//                                Log.d(TAG, "signInAnonymously:success");
//
//                                Toast.makeText(getActivity(), "Signed In Successfully",
//                                        Toast.LENGTH_SHORT).show();
//                                FirebaseDatabase database = FirebaseDatabase.getInstance();
//                                DatabaseReference myRef = database.getReference("rickshaw/fair");
//                                myRef.addListenerForSingleValueEvent(new ValueEventListener() {
//                                    @Override
//                                    public void onDataChange(DataSnapshot dataSnapshot) {
//                                        HashMap<String, Long> hashmap = (HashMap) dataSnapshot.getValue();
//                                        fair = hashmap.get("fair");
//                                        Log.e(TAG, String.valueOf(fair));
//                                        Toast.makeText(getActivity(), "Fair 1"+fair, Toast.LENGTH_SHORT).show();
//                                    }
//
//                                    @Override
//                                    public void onCancelled(DatabaseError databaseError) {
//                                        Toast.makeText(getActivity(), "Check Internet Connection", Toast.LENGTH_SHORT).show();
//
//
//                                    }
//                                });
//
//                            } else {
//                                // If sign in fails, display a message to the user.
//                                Log.w(TAG, "signInAnonymously:failure", task.getException());
//                                Toast.makeText(getActivity(), "Authentication failed.",
//                                        Toast.LENGTH_SHORT).show();
//
//                            }
//
//
//                        }
//                    });
//
//
//        } else if (currentUser!=null){
//            FirebaseDatabase database = FirebaseDatabase.getInstance();
//            DatabaseReference myRef = database.getReference("rickshaw");
//            myRef.addListenerForSingleValueEvent(new ValueEventListener() {
//                @Override
//                public void onDataChange(DataSnapshot dataSnapshot) {
//                   HashMap<String, Long> hashmap = (HashMap) dataSnapshot.getValue();
//                    fair = hashmap.get("fair");
//                    Log.e(TAG, String.valueOf(fair));
//                    Toast.makeText(getActivity(), "Fair 2", Toast.LENGTH_SHORT).show();
//
//                }
//
//                @Override
//                public void onCancelled(DatabaseError databaseError) {
//                    Toast.makeText(getActivity(), "Check Internet Connection", Toast.LENGTH_SHORT).show();
//
//                }
//            });
//
//        }
//        Toast.makeText(getActivity(), "Fair 3 is "+fair, Toast.LENGTH_SHORT).show();
//        Log.e(TAG, String.valueOf(fair));
        //update textview
    //    final TextView timeView = (TextView)inflatedView.findViewById(R.id.time_view);
       // final Handler handler = new Handler();

        //find button
//        if(fair==null){
//            Toast.makeText(getActivity(), "Please Connect to Internet", Toast.LENGTH_LONG).show();
//        }
        final View startButton = inflatedView.findViewById(R.id.startButton);
        final View stopButton = inflatedView.findViewById(R.id.stopButton);
        final View resetButton = inflatedView.findViewById(R.id.resetButton);
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startButton.setVisibility(View.GONE);
                resetButton.setVisibility(View.GONE);
                stopButton.setVisibility(View.VISIBLE);
                if(stopped){
                    startTime = System.currentTimeMillis() - elapsedTime;
                }
                else{
                    startTime = System.currentTimeMillis();
                }
                mHandler.removeCallbacks(startTimer);
                mHandler.postDelayed(startTimer, 0);



            }
        });

        stopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startButton.setVisibility(View.VISIBLE);
                resetButton.setVisibility(View.VISIBLE);
                stopButton.setVisibility(View.GONE);
                mHandler.removeCallbacks(startTimer);
                stopped = true;

            }
        });

        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stopped = false;
               try{
                   ((TextView)getView().findViewById(R.id.timer)).setText("00:00:00");
                   ((TextView)getView().findViewById(R.id.fair_time)).setText("00.00");

               }catch (Exception e){

               }


            }
        });

        return inflatedView;
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
    private void updateTimer(float time){
        long secs = (long)(time/1000);
        long mins = secs/60;
        long hours = mins/60;
        secs = secs % 60;
        String seconds = String.valueOf(secs);
        if(secs == 0){
            seconds = "00";
        }
        if(secs < 10 && secs > 0){
            seconds = "0" + seconds;
        }
        // Covert minute to string
        mins = mins % 60;
        String minutes = String.valueOf(mins);
        if(mins == 0){
            minutes = "00";
        }
        if(mins <10 && mins > 0){
            minutes = "0"+minutes;
        }
        String hour = String.valueOf(hours);
        if(hours==0){
            hour = "00";
        }

        if(hours < 10 && hours > 0){
            hour = "0" + hours;
        }
        try {
            ((TextView)getView().findViewById(R.id.timer)).setText(hour+":"+minutes+":"+seconds);
           // if(fair!=null){

                double totalFair = (fair*100/3600)*secs;
                double tk = totalFair/100;
                double penny = totalFair%100;
                penny = (fair*100/3600)*penny;
                Log.e("fair",tk+" "+penny+"Total "+ totalFair);
                ((TextView)getView().findViewById(R.id.fair_time)).setText(tk+"."+penny);
            //}


        }catch (Exception e){

        }

    }
    private Runnable startTimer = new Runnable() {
        public void run() {
            elapsedTime = System.currentTimeMillis() - startTime;
            updateTimer(elapsedTime);
            mHandler.postDelayed(this,REFRESH_RATE);
        }
    };


}
