package com.example.inspiringapps.Controler;

import android.content.DialogInterface;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.inspiringapps.Model.Entry;
import com.example.inspiringapps.Model.Sequence;
import com.example.inspiringapps.Network.ApiInterface;
import com.example.inspiringapps.Network.ApiUtils;
import com.example.inspiringapps.R;
import com.example.inspiringapps.View.SequenceFragment;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Hashtable;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();
    private static final String SEQUENCE_FRAGMENT = "sequenceFragment";

    ApiInterface apiInterface;
    ResponseBody response;
    FragmentManager fragmentManager;
    ArrayList<Entry> entryList = new ArrayList<>();

    TextView message;
    Button button;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Make network call
        apiInterface = ApiUtils.getFile();

        //Get the fragment manager
        fragmentManager = getSupportFragmentManager();

        message = findViewById(R.id.message);
        progressBar = findViewById(R.id.progress);
        button = findViewById(R.id.download);

        message.setText(getResources().getString(R.string.download_prompt));
        progressBar.setVisibility(View.INVISIBLE);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                download();
            }
        });

    }

    public void download() {
        apiInterface.downloadFile().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<ResponseBody>() {
            @Override
            public void onSubscribe(Disposable d) {
                Log.d(TAG, "Downloading...");

                message.setText(getResources().getString(R.string.wait_prompt));
                progressBar.setVisibility(View.VISIBLE);
                button.setVisibility(View.INVISIBLE);

            }

            @Override
            public void onNext(ResponseBody responseBody) {
                response = responseBody;
            }

            @Override
            public void onError(Throwable e) {
                Log.d(TAG, "Error" + e.getMessage());
                showErrorDialog(e.getMessage());
            }

            @Override
            public void onComplete() {
                Log.d(TAG, "Download Completed!");
                sortData(response);
            }
        });
    }

    public void sortData(ResponseBody body) {

        InputStream inputStream = null;
        ArrayList<String> lines = new ArrayList<>();

        try {

            //Put the response body of the network call in a inputstream
            inputStream = body.byteStream();
            //Create a reader and pass the inputstream
            final BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

            //Iterate every line of the inputstream as long as the line is not null
            for (String line; (line = reader.readLine()) != null; ) {
                lines.add(line);
            }

            //Create observable to detect the when data has been loaded to database
            Observable.fromIterable(lines).subscribe(new Observer<String>() {
                @Override
                public void onSubscribe(Disposable d) {
                    Log.d(TAG, "Sorting data...");
                }

                @Override
                public void onNext(String line) {
                    Entry entry = createEntry(line);
                    entryList.add(entry);
                }

                @Override
                public void onError(Throwable e) {
                    Log.d(TAG, "Error" + e.getMessage());
                    showErrorDialog(e.getMessage());
                }

                @Override
                public void onComplete() {
                    Log.d(TAG, "Data sorted!");

                    sortByEntryIpaddress(entryList);

                    ArrayList<String> pageSequenceList = getPageSequenceList(entryList);

                    Hashtable<String, Integer> unsortMap = getOccurances(pageSequenceList);

                    Map<String, Integer> sortedMap = sortByValue(unsortMap);

                    ArrayList<Sequence> sortedList = convertToList(sortedMap);

                    try {
                        reader.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    message.setVisibility(View.INVISIBLE);
                    progressBar.setVisibility(View.INVISIBLE);

                    SequenceFragment sequenceFragment = SequenceFragment.newInstance(sortedList);
                    fragmentManager.beginTransaction().replace(R.id.root, sequenceFragment, SEQUENCE_FRAGMENT).commit();
                }
            });

        } catch (IOException e) {
            e.getMessage();
            showErrorDialog(e.getMessage());
        }
    }

    public Entry createEntry(String readstring) {
        String splitStr = readstring.split("\"-\"")[0];
        String ipaddress = splitStr.split("- -")[0];
        String str = splitStr.split("GET ")[1];
        String webpage = str.split("HTTP")[0];
        return new Entry(ipaddress, webpage);
    }

    public void sortByEntryIpaddress(ArrayList<Entry> entryList) {
        Collections.sort(entryList, new Comparator<Entry>() {
            public int compare(Entry entry1, Entry entry2) {
                return entry1.getIpAddress().compareToIgnoreCase(entry2.getIpAddress());
            }
        });
    }

    public ArrayList<String> getPageSequenceList(ArrayList<Entry> entryList) {
        ArrayList<String> sequenceList = new ArrayList<>();

        for (int entry = 0; entry < entryList.size() - 2; entry++) {
            String user1 = entryList.get(entry).getIpAddress();
            String user2 = entryList.get(entry + 1).getIpAddress();
            String user3 = entryList.get(entry + 2).getIpAddress();

            if (user1.equals(user2) && user1.equals(user3)) {
                String page1 = entryList.get(entry).getWebPage();
                String page2 = entryList.get(entry + 1).getWebPage();
                String page3 = entryList.get(entry + 2).getWebPage();
                String pages = page1 + " -> " + page2 + " -> " + page3;
                sequenceList.add(pages);
            }
        }

        return sequenceList;
    }

    public Hashtable<String, Integer> getOccurances(ArrayList<String> pages) {
        Hashtable<String, Integer> unsortMap = new Hashtable<String, Integer>();
        for (String page : pages) {
            int occurrences = Collections.frequency(pages, page);
            unsortMap.put(page, occurrences);
        }
        return unsortMap;
    }

    public Map<String, Integer> sortByValue(Map<String, Integer> unsortMap) {

        //Convert Map to List of Map
        List<Map.Entry<String, Integer>> list = new LinkedList<Map.Entry<String, Integer>>(unsortMap.entrySet());

        /*Sort list with Collections.sort(), provide a custom Comparator
            ACS: o2.getValue()).compareTo(o1.getValue()
            DES: o1.getValue()).compareTo(o2.getValue()*/
        Collections.sort(list, new Comparator<Map.Entry<String, Integer>>() {
            public int compare(Map.Entry<String, Integer> o1,
                               Map.Entry<String, Integer> o2) {
                return (o2.getValue()).compareTo(o1.getValue());
            }
        });

        //Loop the sorted list and put it into a new insertion order Map LinkedHashMap
        Map<String, Integer> sortedMap = new LinkedHashMap<String, Integer>();
        for (Map.Entry<String, Integer> entry : list) {
            sortedMap.put(entry.getKey(), entry.getValue());
        }

        return sortedMap;
    }

    public ArrayList<Sequence> convertToList(Map<String, Integer> sortedMap) {
        ArrayList<Sequence> sequences = new ArrayList<>();

        for (Map.Entry<String, Integer> entry : sortedMap.entrySet()) {
            String sequence = entry.getKey();
            String occurence = entry.getValue().toString();
            sequences.add(new Sequence(sequence, occurence));
        }
        return sequences;
    }

    public void showErrorDialog(String message) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MainActivity.this);
        alertDialogBuilder.setTitle("Error");
        alertDialogBuilder.setMessage(message);
        alertDialogBuilder.setPositiveButton("DISMISS", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        AlertDialog errorDialog = alertDialogBuilder.create();
        errorDialog.show();
    }
}