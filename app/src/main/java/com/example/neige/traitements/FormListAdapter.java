package com.example.neige.traitements;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.neige.R;

import java.util.ArrayList;

public class FormListAdapter extends ArrayAdapter<Formulaire> {

    private static final String TAG = "PersonListAdapter";
    private Context mContext;
    int mResource;

    public FormListAdapter(@NonNull Context context, int resource, @NonNull ArrayList<Formulaire> objects) {
        super(context, resource, objects);
        this.mContext = context;
        mResource = resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        // Récupérer les infos du formulaire
        String date = getItem(position).getDate();
        Double latitude = getItem(position).getLatitude();
        Double longitude = getItem(position).getLongitude();
        int pourcentageNeige = getItem(position).getPourcentageNeige();
        int accuracy = getItem(position).getAccurracy();
        int altitude = getItem(position).getAltitude();
        int id_user = getItem(position).getIdUser();

        // Créer l'objet formulaire avec les infos
        Formulaire form = new Formulaire(date, latitude, longitude, accuracy, altitude, pourcentageNeige, id_user);

        LayoutInflater inflater = LayoutInflater.from(mContext);
        convertView = inflater.inflate(mResource, parent, false);

        TextView tvDate = convertView.findViewById(R.id.textDate);
        TextView tvLatLng = convertView.findViewById(R.id.textLatLong);
        TextView tvPourcentageNeige = convertView.findViewById(R.id.textPourcentageDeNeige);

        tvDate.setText(date);
        tvLatLng.setText("Lat/Lng : " + latitude + ", " + longitude);
        tvPourcentageNeige.setText("Pourcentage de neige : " + pourcentageNeige + "%");

        return convertView;
    }
}
