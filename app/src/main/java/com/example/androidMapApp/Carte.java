package com.example.androidMapApp;


import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RatingBar;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.googlemapapi.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PointOfInterest;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.Date;
import java.util.List;



public class Carte extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnPoiClickListener, GoogleMap.OnMapLongClickListener,
        GoogleMap.OnMapClickListener, ActivityCompat.OnRequestPermissionsResultCallback, GoogleMap.OnMyLocationButtonClickListener,
        GoogleMap.OnMyLocationClickListener, GoogleMap.OnMarkerClickListener, GoogleMap.OnInfoWindowClickListener, SearchView.OnQueryTextListener {

    SearchView searchView;

    private GoogleMap mMap;
    private UiSettings mUiSettings;

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    private boolean mPermissionDenied = false;


    private static final LatLng TANGER_COORD = new LatLng(35.7595, -5.8340);
    //Un marqueur identifie un emplacement sur une carte.
    private Marker mTanger;
    private Marker myMarker;

    //public final static int SAVE_PLACE_CODE = 1;
    public final static int EXPLORE_LOCAL_CODE = 4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_carte);
        Intent i = getIntent();

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        searchView = findViewById(R.id.sv_location);
        searchView.setFocusable(false);
        searchView.setOnQueryTextListener(this);// D??finir un listener pour les actions de l'utilisateur dans SearchView

        // Un fragment est un composant de carte dans une application. Ce fragment est le moyen le plus simple de placer une carte dans une application.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);

        mapFragment.getMapAsync(this); //Cette fonction initialise automatiquement le syst??me de cartes et la vue.
    }

    //ajouter des ??l??ments sur le main_menu.xml ?? la barre d'applications
    //Pour sp??cifier le menu d'options de notre activit??
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
         //Gonflez le menu; cela ajoute des ??l??ments ?? la barre d'applications.
         getMenuInflater().inflate(R.menu.main_menu, menu);
         return super.onCreateOptionsMenu(menu);
    }

    // g??rer les clics sur les ??l??ments de la barre d'applications
    @Override public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {


            case R.id.explore_local:
                Intent intentExploreLocal = new Intent(Carte.this, SeeLocalDbContentActivity.class);
                //obtenir le r??sultat de l'activit?? : SeeLocalDbContentActivity ici
                startActivityForResult(intentExploreLocal, EXPLORE_LOCAL_CODE);
                return true;

            case R.id.change_map_type:

                //cr??e un nouveau type de carte, que l'utilisateur peut s??lectionner dans le contr??le de type de carte
                final PopupCarteType popupMapType = new PopupCarteType(Carte.this);

                //Obtenir une carte Hybrid
                popupMapType.getHybridView().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
                        popupMapType.dismiss();
                    }
                });
                //Obtenir une carte Normal
                popupMapType.getNormalView().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                        popupMapType.dismiss();
                    }
                });
                //Obtenir une carte satellite
                popupMapType.getSatelliteView().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
                        popupMapType.dismiss();
                    }
                });
                //Obtenir une carte terrain
                popupMapType.getTerrainView().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
                        popupMapType.dismiss();
                    }
                });
                popupMapType.build();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    //onMapReady(GoogleMap)est d??clench??e lorsque la carte est pr??te ?? ??tre utilis??e et fournit une instance non nulle de GoogleMap.
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Ajouter un marqueur ?? Tanger
        mTanger = mMap.addMarker(
                    new MarkerOptions()
                    .position(TANGER_COORD)
                    .title("Tanger")
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
                    .snippet("Bienvenu ?? la ville de  Tanger"));
        //C'est essentiellement un moyen pour m??moriser les vues.
        mTanger.setTag(999);
        //D??placer la cam??ra ?? tanger
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(TANGER_COORD, 8));

        // Ajouter un Listener pour le click sur le marqueur
        mMap.setOnMarkerClickListener(this);

        //pour ??couter les ??v??nements de clic dans une fen??tre d'informations.
        mMap.setOnInfoWindowClickListener(this);

        //Param??tres de l'interface utilisateur d'un GoogleMap.
        mUiSettings = mMap.getUiSettings();
        mUiSettings.setZoomControlsEnabled(true);

        //??coute des ??v??nements de clic sur POI (points d'int??r??t)
        mMap.setOnPoiClickListener(this);

        //Interface de rappel lorsque l'utilisateur appuie longuement sur la carte
        mMap.setOnMapLongClickListener(this);
        mMap.setOnMapClickListener(this);

        //demander la permession et permetter par suite de r??cup??rer la localisation actuelle
        mMap.setOnMyLocationButtonClickListener(this);
        mMap.setOnMyLocationClickListener(this);
        enableMyLocation();


    }//fin de la fonction onMapReady


    //r??pondre aux ??v??nements de clic sur les marqueurs
    @Override
    public boolean onMarkerClick( Marker marker) {
        Object tag = marker.getTag();

        if(tag instanceof Integer){
            marker.showInfoWindow();
        }else {
            Localisation location = (Localisation) marker.getTag();

            String sitename = location.sitename;
            String description = location.description;
            final double latitude = location.latitude;
            final double longitude = location.longitude;
            String date = location.date;
            int numberVotes = location.numberVotes;
            DecimalFormat df = new DecimalFormat("#.##");

            VotesPopup votesPopup = new VotesPopup(Carte.this);
            votesPopup.setTitle(sitename);
            votesPopup.setDescription(description);
            votesPopup.setRatingValue(Double.parseDouble( df.format(numberVotes) ));


            votesPopup.getRatingBar().setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
                @Override
                public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                    Toast.makeText(getApplicationContext(), "rating : " + rating, Toast.LENGTH_SHORT).show();


                }
            });

            votesPopup.build();
        }
        return false;
    }

    //r??pondre aux ??v??nements de clic dans la fen??tre d'informations (fen??tre d'informations sur les marqueurs)
    @Override
    public void onInfoWindowClick(Marker marker) {
        Toast.makeText(getApplicationContext(), "cliquez sur la fen??tre d'information pour le marqueur: " + marker.getTitle(), Toast.LENGTH_LONG).show();
    }

    //gestion des clics sur les points d'int??r??t
    @Override
    public void onPoiClick(PointOfInterest poi) {
        Toast.makeText(getApplicationContext(), "POI Clicked: " + poi.name + "\nPlace ID:" + poi.placeId + "\nLatitude:" +
                poi.latLng.latitude + " Longitude:" + poi.latLng.longitude, Toast.LENGTH_LONG).show();
    }


    //clicks sur la carte
    @Override
    public void onMapLongClick(LatLng latLng) {

        final double latitude = latLng.latitude;
        final double longitude = latLng.longitude;

        //obtenir le nom et la description du site ?? ins??rer dans la base de donn??es
        final Popup popup = new Popup(Carte.this);
        popup.setTitle("Quel site ?");
        popup.setSitenameHint("entrer le nom du site : ");
        popup.setDescritpionHint("Description du site : ");
        popup.setNegativeButtonText("Annuler");
        popup.setPositiveButtonText("Enregistrer");
        popup.getSaveButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = popup.getSitenameEditText().getText().toString();
                String description = popup.getDescriptionEditText().getText().toString();

                //Ajouter un nouveau site la Base de donn??e
                new LocalDbManager(Carte.this).insertOneLocation(new Localisation(
                        username,
                        description,
                        new Date().toString(),
                        latitude,
                        longitude,
                        0,
                        0
                ));
                Toast.makeText(Carte.this, "Bien Enregistr??", Toast.LENGTH_LONG).show();
                popup.dismiss();

            }
        });

        popup.getCancelButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popup.dismiss();
            }
        });

        popup.build();
    }
    @Override
    public void onMapClick(LatLng latLng) {
        Toast.makeText(getApplicationContext(), "Site choisi \nLocation: " + latLng.toString(), Toast.LENGTH_LONG).show();
    }



    // Location actuelle de l'utilisateur
    private void enableMyLocation() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            // Permession manquante
            Permission.requestPermission(this, LOCATION_PERMISSION_REQUEST_CODE,
                    Manifest.permission.ACCESS_FINE_LOCATION, true);
        } else if (mMap != null) {
            // Access valid??
            mMap.setMyLocationEnabled(true);
        }
    }

    @Override
    public boolean onMyLocationButtonClick() {
        Toast.makeText(this, "Votre location Actuelle", Toast.LENGTH_SHORT).show();
        // Renvoie false pour que nous ne consommions pas l'??v??nement et que le comportement par d??faut se produise toujours
        return false;
    }

    @Override
    public void onMyLocationClick(@NonNull Location location) {
        Toast.makeText(this, "Location actuelle:\n" + location, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode != LOCATION_PERMISSION_REQUEST_CODE) {
            return;
        }

        if (Permission.isPermissionGranted(permissions, grantResults,
                Manifest.permission.ACCESS_FINE_LOCATION)) {
            // Activez la position si l'autorisation a ??t?? accord??e.
            enableMyLocation();
        } else {
            // Affichez la bo??te de dialogue d'erreur d'autorisation manquante.
            mPermissionDenied = true;
        }
    }

    @Override
    protected void onResumeFragments() {
        super.onResumeFragments();
        if (mPermissionDenied) {
            // Affichage d'un message d'erreur
            showMissingPermissionError();
            mPermissionDenied = false;
        }
    }

    /**
     * Affiche une bo??te de dialogue avec un message d'erreur expliquant que l'autorisation de localisation est manquante.
     */
    private void showMissingPermissionError() {
        Permission.PermissionDeniedDialog
                .newInstance(true).show(getSupportFragmentManager(), "dialog");
    }


    //gestion des requ??tes de recherche dans la vue de recherche
    @Override
    public boolean onQueryTextSubmit(String query) {
        List<Address> addressList = null;

        if(!query.equals("")){

            Geocoder geocoder = new Geocoder(getApplicationContext());

            try {
                addressList = geocoder.getFromLocationName(query, 1);

            }catch (IOException e){
                e.printStackTrace();
            }

            Address address = addressList.get(0);
            LatLng latLng = new LatLng(address.getLatitude(), address.getLongitude());

            Marker m;
            m = mMap.addMarker(new MarkerOptions().title(query).position(latLng));
            m.setTag(187);
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 4));

        }
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }
}
