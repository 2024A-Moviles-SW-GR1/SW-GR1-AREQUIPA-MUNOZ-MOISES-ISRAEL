package com.example.viddeoplataformaexamen;
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var plataformas: ArrayList<Map<String, Any>>
    private lateinit var adaptador: CustomAdapter
    private lateinit var dbHelper: VideoDatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val listView: ListView = findViewById(R.id.listView)
        val addButton: Button = findViewById(R.id.addButton)

        dbHelper = VideoDatabaseHelper(this)
        plataformas = ArrayList(dbHelper.getAllPlatforms())

        adaptador = CustomAdapter(this, plataformas)
        listView.adapter = adaptador

        addButton.setOnClickListener {
            // Simular la adición de una nueva plataforma
            val platformId = dbHelper.addPlatform("Nueva Plataforma", 0.0, "HD", "Ninguna", 0)
            val newPlatform = dbHelper.getPlatformById(platformId)
            if (newPlatform != null) {
                plataformas.add(newPlatform)
                adaptador.notifyDataSetChanged()
            }
        }

        listView.setOnItemClickListener { _, _, position, _ ->
            val platform = plataformas[position]
            val intent = Intent(this, EditPlatformActivity::class.java)
            intent.putExtra("PLATFORM_ID", platform["id"] as Long)
            startActivity(intent)
        }
    }

    override fun onResume() {
        super.onResume()
        plataformas.clear()
        plataformas.addAll(dbHelper.getAllPlatforms())
        adaptador.notifyDataSetChanged()
    }
}
