package com.example.social_photo.Fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.social_photo.Activities.DisplayPhotos;
import com.example.social_photo.Activities.Recap;
import com.example.social_photo.NetworkTools.NetworkUtilities;
import com.example.social_photo.R;
import com.example.social_photo.Utils.SaveSharedPreference;
import com.example.social_photo.Utils.Utilities;
import com.james602152002.floatinglabelspinner.FloatingLabelSpinner;

import static com.example.social_photo.R.layout.mylist;

public class OverviewTab extends Fragment implements View.OnClickListener {
    private ListView listView;
    private Spinner dropDown;
    private Spinner dropDown2;
    private Button button;
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.overview_tab, container, false);

        dropDown = rootView.findViewById(R.id.dropDown);


        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(),
                android.R.layout.simple_spinner_item, Utilities.getYears());
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dropDown.setAdapter(adapter);

        dropDown2 = rootView.findViewById(R.id.dropDown2);


        ArrayAdapter<String> adapter2 = new ArrayAdapter<>(getContext(),
                android.R.layout.simple_spinner_item, Utilities.getMonth());
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dropDown2.setAdapter(adapter2);

        button = rootView.findViewById(R.id.buttonRecap);
        button.setOnClickListener(this);

        return rootView;

    }
    @Override
    public void onClick(View v) {
        String year = dropDown.getSelectedItem().toString();
        String month = dropDown2.getSelectedItem().toString();
        Log.d("TAG", "Data richiesta: " + year + "-" + month);
        new getInfos().execute(year, month);
    }

    public class getInfos extends AsyncTask<String,Void,String> {

        ProgressDialog progress;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progress = ProgressDialog.show(getContext(), "Fetching infos", "Fetching infos...", true);
        }

        @Override
        protected String doInBackground(String... params) {
            String year = params[0];
            String month = params[1];
            String result = NetworkUtilities.getDataUser(SaveSharedPreference.getUserToken(getContext()), SaveSharedPreference.getUserID(getContext()), year, month);
            Log.d("TAG", "LA FESSA DI PIERINO: " + result);
            return result;
        }


        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            progress.dismiss();
            /*
            String[] resultArray = s.split(",");
            String photoId = resultArray[0];
            int photoLikes = Integer.parseInt(resultArray[1]);
            */
            Intent intent = new Intent(getContext(),Recap.class);
            startActivity(intent);

        }

    }


}
