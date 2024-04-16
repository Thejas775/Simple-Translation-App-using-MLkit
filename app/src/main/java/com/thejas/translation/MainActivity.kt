package com.thejas.translation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.ProgressBar
import android.widget.Spinner
import android.widget.TextView
import com.google.mlkit.common.model.DownloadConditions
import com.google.mlkit.nl.translate.TranslateLanguage
import com.google.mlkit.nl.translate.Translation
import com.google.mlkit.nl.translate.TranslatorOptions

class MainActivity : AppCompatActivity() {
    private lateinit var text : TextView
    private lateinit var translatedext : TextView
    private lateinit var totranslate : TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        totranslate = findViewById(R.id.totranslate)
        text = findViewById(R.id.downloaded)
        translatedext = findViewById(R.id.translated)
        val languages = arrayOf("Hindi", "Marathi", "Tamil", "Telugu", "French", "German")
        val button = findViewById<TextView>(R.id.button)
        val spinner: Spinner = findViewById(R.id.spinner)
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, languages)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter
        val languageMap = mapOf(
            "Hindi" to TranslateLanguage.HINDI,
            "Marathi" to TranslateLanguage.MARATHI,
            "Tamil" to TranslateLanguage.TAMIL,
            "Telugu" to TranslateLanguage.TELUGU,
            "French" to TranslateLanguage.FRENCH,
            "German" to TranslateLanguage.GERMAN
        )

        button.setOnClickListener {
            val progressBar = findViewById<ProgressBar>(R.id.progressBar)
            val selectedLanguage = spinner.selectedItem.toString()
            val targetLanguage = languageMap[selectedLanguage] ?: TranslateLanguage.ENGLISH

            val options = TranslatorOptions.Builder()
                .setSourceLanguage(TranslateLanguage.ENGLISH)
                .setTargetLanguage(targetLanguage)
                .build()
            val englishGermanTranslator = Translation.getClient(options)
            var conditions = DownloadConditions.Builder()
                .requireWifi()
                .build()
            progressBar.visibility = View.VISIBLE
            text.text = "Model being downloaded"

            englishGermanTranslator.downloadModelIfNeeded(conditions)
                .addOnSuccessListener {
                    progressBar.visibility = View.GONE
                    translatedext.visibility = View.VISIBLE


                    englishGermanTranslator.translate(totranslate.text.toString())
                        .addOnSuccessListener { translatedText ->
                            translatedext.text = translatedText
                        }
                        .addOnFailureListener { exception ->
                            translatedext.text = exception.message
                        }

                    text.text = "Model downloaded"
                }
                .addOnFailureListener { exception ->
                    text.text = "Model not downloaded"
                }

        }



    }
}