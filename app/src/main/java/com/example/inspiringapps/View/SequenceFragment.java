package com.example.inspiringapps.View;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.example.inspiringapps.InteractionListener;
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
    Button button;
    LinearLayout title;
    InteractionListener listener;
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
        button = view.findViewById(R.id.download);
        recyclerView = view.findViewById(R.id.log_recyler_view);
        title = view.findViewById(R.id.title);

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Log.d(TAG,"onAttach");
        listener = (InteractionListener) context;
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
            message.setVisibility(View.INVISIBLE);
            button.setVisibility(View.INVISIBLE);
            title.setVisibility(View.VISIBLE);

            adapter = new SequenceAdapter(sequences);
            layoutManager = new LinearLayoutManager(getActivity());
            recyclerView.setAdapter(adapter);
            recyclerView.setLayoutManager(layoutManager);
        }else{
            message.setVisibility(View.VISIBLE);
            message.setText(getResources().getString(R.string.download_prompt));
            button.setVisibility(View.VISIBLE);
            title.setVisibility(View.INVISIBLE);

            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    message.setText(getResources().getString(R.string.wait_prompt));
                    listener.onDownloadButtonPress(true);
                    button.setVisibility(View.INVISIBLE);
                }
            });
        }

    }

    @Override
    public void onDetach() {
        super.onDetach();
        Log.d(TAG,"onDetach");
    }
}
