package mx.edu.ittepic.daar.ladm_u4_t1

import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.database.Cursor
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.CallLog
import android.provider.ContactsContract
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.app.ActivityCompat
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    var permitCalls = 0
    var permitConts = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btnCalls.setOnClickListener {
            listaLLamadasPerdidas()
        }
        btnContactos.setOnClickListener {
            listaContactosConsul()
        }
    }

    @SuppressLint("Range")
    private fun listaLLamadasPerdidas(){
        if (ActivityCompat.checkSelfPermission(this,android.Manifest.permission.READ_CALL_LOG)
            != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.READ_CALL_LOG), permitCalls)
        }else {
            var calls = ArrayList<String>()
            var selector :String =  CallLog.Calls.TYPE + "=" + CallLog.Calls.MISSED_TYPE
            var cursor : Cursor ?= null

            try {
                cursor = contentResolver.query(Uri.parse("content://call_log/calls"),null,selector,null,null)
                var registro = ""

                while (cursor?.moveToNext()!!){
                    var nombre : String = cursor.getString(cursor.getColumnIndex(CallLog.Calls.CACHED_NAME))
                    var numero : String = cursor.getString(cursor.getColumnIndex(CallLog.Calls.NUMBER))
                    var tipo : String = cursor.getString(cursor.getColumnIndex(CallLog.Calls.TYPE))

                    registro= "\n Nombre: "+nombre+"\n Numero: "+numero+"\n Tipo: "+tipo
                    calls.add(registro)

                }
            }catch (e:Exception){
                Toast.makeText(this,"Error: "+e,Toast.LENGTH_LONG).show()
            }
            finally {
                listCalls.adapter = ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,calls)
                cursor?.close()
            }
        }
    }

    @SuppressLint("Range")
    private  fun listaContactosConsul(){
        if (ActivityCompat.checkSelfPermission(this,android.Manifest.permission.READ_CONTACTS)
            != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.READ_CONTACTS), permitConts)
        }else {
            var contacto = ArrayList<String>()
            var uri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI
            var cursor : Cursor ?= null

            try {
                cursor = contentResolver.query(uri,null,null,null,null)
                var registro = ""

                while (cursor?.moveToNext()!!){
                    var nombre : String = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME))
                    var numero : String = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))

                    registro= "\n Nombre: "+nombre+"\n Numero: "+numero
                    contacto.add(registro)

                }
            }catch (e:Exception){
                Toast.makeText(this,"Error: "+e,Toast.LENGTH_LONG).show()
            }
            finally {
                listContactos.adapter = ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,contacto)
                cursor?.close()
            }
        }
    }
}