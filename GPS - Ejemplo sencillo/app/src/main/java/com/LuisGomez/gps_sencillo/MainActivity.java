package com.LuisGomez.gps_sencillo;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.widget.TextView;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

// Para la Toolbar
import android.support.v7.app.AppCompatActivity;

//Aqui le decimos que extienda de AppCompatActivity
public class MainActivity extends AppCompatActivity {

    private TextView Latitud, Longitud, Altura, Precision, direccionPostal;

    /*Se declara una variable de tipo LocationManager encargada de proporcionar acceso al servicio de localización del sistema.*/
    private LocationManager locManager;

    /*Se declara una variable de tipo Location que accederá a la última posición conocida proporcionada por el proveedor.*/
    private Location loc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Latitud = (TextView)findViewById(R.id.Latitud);
        Longitud = (TextView)findViewById(R.id.Longitud);
        Altura = (TextView)findViewById(R.id.Altitud);
        Precision = (TextView)findViewById(R.id.Precision);

        direccionPostal = (TextView) findViewById(R.id.mensaje_id3);

        ActivityCompat.requestPermissions(MainActivity.this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
        {
            Latitud.setText("No se han definido los permisos necesarios.");
            Longitud.setText("No se han definido los permisos necesarios.");
            Altura.setText("No se han definido los permisos necesarios.");
            Precision.setText("No se han definido los permisos necesarios.");

            return;

        } else {

            locManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            loc = locManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            Latitud.setText(String.valueOf(loc.getLatitude()));
            Longitud.setText(String.valueOf(loc.getLongitude()));
            Altura.setText(String.valueOf(loc.getAltitude()));
            Precision.setText(String.valueOf(loc.getAccuracy()));
        }

        this.setLocation(loc); // Aqui he tenido que añadir esta linea al metodo setLocation para calcular el nombre de la calle en dicho metodo con Geocoder
    }

    // Aqui obtengo la direccion de la calle a partir de la latitud y la longitud

    public void setLocation(Location loc) {
        //Obtener la direccion de la calle a partir de la latitud y la longitud
        if (loc.getLatitude() != 0.0 && loc.getLongitude() != 0.0) {
            try {
                Geocoder geocoder = new Geocoder(this, Locale.getDefault());
                List<Address> list = geocoder.getFromLocation(
                        loc.getLatitude(), loc.getLongitude(), 1);
                if (!list.isEmpty()) {
                    Address DirCalle = list.get(0);
                    direccionPostal.setText(DirCalle.getAddressLine(0));

                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
