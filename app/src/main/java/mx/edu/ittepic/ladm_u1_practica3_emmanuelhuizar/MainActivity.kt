package mx.edu.ittepic.ladm_u1_practica3_emmanuelhuizar

import android.Manifest
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.activity_main.*
import java.io.*

class MainActivity : AppCompatActivity() {
    val vector : Array<Int> = Array(10,{0})
    var dataFinal=""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        button3.setOnClickListener {
            if(editText3.text.isEmpty()!=true) {
                    if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED){

                        ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE), 0)
                    } else {
                        guardarArchivoSD()
                    }
            }else{
                AlertDialog.Builder(this).setTitle("Atencion").setMessage("Falta poner el nombre del archivo").setPositiveButton("Ok"){ d, i->}.show()
            }
        }

        button.setOnClickListener {
            insertarVector()

        }

        button2.setOnClickListener {
            MostrarArreglo()
        }

        button4.setOnClickListener {
            if(editText4.text.isEmpty()!=true) {
                leerArchivoSD()
            }else{
                AlertDialog.Builder(this).setTitle("Atencion").setMessage("Falta poner el nombre del archivo").setPositiveButton("Ok"){ d, i->
                    ponerTexto("","","","")
                    editText4.requestFocus()
                }.show()
            }
        }

    }

    private fun MostrarArreglo() {
        var data=""
        (0..9).forEach { r->
            data+="[${vector[r]}],"
        }
        textView4.setText(data)
    }

    private fun insertarVector() {
        textView4.setText("")
        if(editText.text.isEmpty()||editText2.text.isEmpty()){
            mensaje("Error todos los campos deben tener data")
            return
        }

        var v = editText.text.toString().toInt()
        var r = editText2.text.toString().toInt()

        vector[r]=v

        mensaje("Se guardo el archivo")

        editText.setText("")
        editText2.setText("")
        editText.requestFocus()
        //Guarada la ultima actualizacion de el arreglo
        dataFinal=""
        (0..9).forEach { r->
            dataFinal+="${vector[r]},"
        }
    }

    fun leerArchivoSD(){
        if(noSD()){
            mensaje("No hay memoria")
            return
        }
        try{
            var rutaSD = Environment.getExternalStorageDirectory()
            var datosArchivo = File(rutaSD.absolutePath,editText4.text.toString()+".txt")

            var flujoEntrada= BufferedReader(InputStreamReader(FileInputStream(datosArchivo)))

            var data=flujoEntrada.readLine()

            //textView.setText("Vector en uso -> "+editText4.text.toString()+" :")
            //textView4.setText(data)

            flujoEntrada.close()


            //vacia el vector que obtubo en el cache
            var vector2= data.split(",")
            (0..9).forEach { r->
                vector[r]=vector2[r].toInt()
            }
            //esto quedara a la mitad
            MostrarArreglo()
            textView.setText("Vector abierto ->  "+ editText4.text.toString()+"  :")
            editText4.setText("")
            editText.requestFocus()

        }catch (error: IOException){
            mensaje(error.message.toString())
        }
    }

    fun guardarArchivoSD(){
        if(noSD()){
            mensaje("No hay memoria")
            return
        }

        try{
            var rutaSD = Environment.getExternalStorageDirectory()
            var datosArchivos = File(rutaSD.absolutePath,editText3.text.toString()+".txt")

            var flujoSalida= OutputStreamWriter(FileOutputStream(datosArchivos))

            flujoSalida.write(dataFinal)
            flujoSalida.flush()
            flujoSalida.close()

            mensaje("El archivo se guardo correctamente")
            ponerTexto("","","","")
            (0..9).forEach { r->
                vector[r]=0
            }
            editText.requestFocus()
            textView.setText("Vector:")
            textView4.setText("")

        }catch(error: IOException){
            mensaje(error.message.toString())
        }
    }

    fun noSD():Boolean{
        var estado = Environment.getExternalStorageState()
        if(estado != Environment.MEDIA_MOUNTED){
            return true
        }
        return false
    }

    fun ponerTexto(t0:String,t1:String,t2:String,t3:String){
        editText.setText(t0)
        editText2.setText(t1)
        editText3.setText(t2)
        editText4.setText(t3)
    }

    fun mensaje(m:String){
        AlertDialog.Builder(this).setTitle("Atencion").setMessage(m).setPositiveButton("Ok"){ d, i->}.show()
    }
}
