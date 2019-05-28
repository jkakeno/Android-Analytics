package com.example.inspiringapps.View;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.inspiringapps.Model.Sequence;
import com.example.inspiringapps.R;
import java.util.ArrayList;

public class SequenceFragment  extends Fragment {
    private static final String TAG = SequenceFragment.class.getSimpleName();
    private static final String ARG = "sequence";

    View view;
    RecyclerView recyclerView;
    LinearLayoutManager layoutManager;
    TextView message;
    LinearLayout title;
    SequenceAdapter adapter;
    ArrayList<Sequence> sequences;

    public static SequenceFragment newInstance(ArrayList<Sequence> sequences) {
        SequenceFragment fragment = new SequenceFragment();
        Bundle args = new Bundle();
        args.putParcelableArrayList(ARG, sequences);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG,"onCreate");

        if (getArguments() != null) {
            sequences = getArguments().getParcelableArrayList(ARG);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d(TAG,"onCreateView");
        view = inflater.inflate(R.layout.sequence_fragment, container, false);
        message = view.findViewById(R.id.message);
        recyclerView = view.findViewById(R.id.log_recyler_view);
        title = view.findViewById(R.id.title);

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Log.d(TAG,"onAttach");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG,"onDestroy");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d(TAG,"onPause");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG,"onResume");

        if(sequences != null) {
            title.setVisibility(View.VISIBLE);
            message.setVisibility(View.INVISIBLE);

            adapter = new SequenceAdapter(sequences);
            layoutManager = new LinearLayoutManager(getActivity());
            recyclerView.setAdapter(adapter);
            recyclerView.setLayoutManager(layoutManager);
        }else{
            title.setVisibility(View.VISIBLE);
            message.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public void onDetach() {
        super.onDetach();
        Log.d(TAG,"onDetach");
    }
}
