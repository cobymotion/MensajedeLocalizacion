package com.kobinotes.mensajedelocalizacion;

import android.Manifest;
import android.content.pm.PackageManager;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);


        boolean res = verificarPermisos();
        if(res)
            Log.w("ESTADO","Continua normalmente");
    }



    ///metodo que usaremos para verificar los permisos
    private boolean verificarPermisos(){
        //preguntamos si los permisos ya estan actividos primero la localizacion y segundo el mensaje de texto
        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED
         || ActivityCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS)!= PackageManager.PERMISSION_GRANTED){
            ///si los permisos no estan pedimos autorización
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.SEND_SMS},1234);
            // retornamos falso ya que los permisos no estan activados y la aplicacion sigue por lo que si ejecutamos algo
            //antes de autorizar nos gerara problemas
            return false;
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResult )
    {
        String cadena ="";
        switch (requestCode){
            case 1234: //codigo que mandamos en el Request
                if(grantResult.length>1){
                    if(grantResult[0]!=PackageManager.PERMISSION_GRANTED)
                        cadena += "PERMISO DENEGADO: LOCALIZACION";
                    if(grantResult[1]!=PackageManager.PERMISSION_GRANTED) {
                        cadena += (cadena.isEmpty() ? "PERMISO DENEGADO:" : "");
                        cadena += " SMS ";
                    }
                }else
                    cadena = "NO TIENE PERMISOS LA APLICACION";
        }
        View view  = findViewById(android.R.id.content);
        Snackbar.make(view , cadena.isEmpty()?"Permisos Correctos":cadena,Snackbar.LENGTH_LONG).setAction("Intentar", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                verificarPermisos();
            }
        }).setActionTextColor(getResources().getColor(R.color.colorAccent))
        .show();
    }

    @OnClick(R.id.botonsms)
    public void mandarsms(View view)
    {
        //numero al cual se le va a mandar el mensaje
        String num="3333221348";
        //mensaje que se mandara
        String mensaje="Este es un mensaje de prueba";
        // administrador para el envio de mensajes
        SmsManager manager = SmsManager.getDefault();
        //verificamos que tengamos el permiso
        if(ActivityCompat.checkSelfPermission(this,Manifest.permission.SEND_SMS)!=PackageManager.PERMISSION_GRANTED){
            //si no tenemos el permiso lo agregamos
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.SEND_SMS},1234);
            return;
        }
        //mandamos el mensaje
        manager.sendTextMessage(num, null, mensaje, null,null);
        //Mostramos un mensaje que se envio el texto
        Snackbar.make(view, "Se envio el mensaje",Snackbar.LENGTH_LONG).show();
    }

}
