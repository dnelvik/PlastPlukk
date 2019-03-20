package no.usn.plastplukk.plastplukk.PlasticRegistering;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import no.usn.plastplukk.plastplukk.R;

public class SetAttributesActivity extends AppCompatActivity {

    private String[] typer, str;    // Valgene til dropdown menyene
    private String kategori, underKategori, størrelse;
    private TextView feilMelding, overTekst;
    private boolean visible = false;

    private Spinner dropdownSecondCategory, dropdownSize;
    private SharedPreferences.Editor editor;
    private SharedPreferences prefs;

    public static final String MY_PREFS_NAME = "MyPrefsFile";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_egenskaper);
        Intent intent = getIntent();

        // Shared preferences
        prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        editor = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
        kategori = prefs.getString("Kategori", "Ingen");

        feilMelding = findViewById(R.id.Feilmelding);
        overTekst = findViewById(R.id.tekst);
        overTekst.setText(kategori);

        Log.e("Longitude", ""+prefs.getString("Longitude", null));
        Log.e("Latitude", ""+prefs.getString("Latitude", null));

        lagDropDown();
        selectOnReturn();
    }

    // Åpner neste aktivitet - area
    public void openArea(View view){
        if (underKategori.equals("Velg type..") || (størrelse.equals("Velg størrelse..") && visible)){
            feilMelding.setText(getString(R.string.fyll_ut_alle_felt));
            return;
        }
        Intent messageIntent = new Intent(this, ChooseAreaActivity.class);
        startActivity(messageIntent);
    }

    // Oppretter dropdown menyene og deres funksjoner
    private void lagDropDown(){

        final LinearLayout layout = findViewById(R.id.str); //

        // Alle valgene legges til
        if (kategori.equals(getString(R.string.pose)))
            typer = new String[]{getString(R.string.velg_type_), getString(R.string.stor_plastpose), getString(R.string.middels_plastpose),
                    getString(R.string.liten_plastpose)};
        else if (kategori.equals(getString(R.string.emballasje)))
            typer = new String[]{getString(R.string.velg_type_), getString(R.string.godteriemballasje), getString(R.string.diverse_matemballasje),
                    getString(R.string.nettstrompe), getString(R.string.plastfilm)};
        else if (kategori.equals(getString(R.string.flaske)))
            typer = new String[]{getString(R.string.velg_type_), getString(R.string.plastflaske), getString(R.string.kanne), getString(R.string.tonne), getString(R.string.botte_boks), getString(R.string.snus), getString(R.string.kork_lokk), getString(R.string.annet)};
        else if (kategori.equals(getString(R.string.servise)))
            typer = new String[]{getString(R.string.velg_type_), getString(R.string.kopper_plastglass), getString(R.string.tallerken), getString(R.string.bestikk), getString(R.string.annet)};
        else if (kategori.equals(getString(R.string.redskap)))
            typer = new String[]{getString(R.string.velg_type_), getString(R.string.fiskegarn), getString(R.string.fiskesnore), getString(R.string.lighter), getString(R.string.tau_trad), getString(R.string.annet)};
        else if (kategori.equals(getString(R.string.diverse)))
            typer = new String[]{getString(R.string.velg_type_), getString(R.string.bekledning), getString(R.string.riflehylser), getString(R.string.ballonger), getString(R.string.bildeler), getString(R.string.div_kroppspleie),
                    getString(R.string.dumpet_avfall), getString(R.string.annet)};


        str = new String[4]; str[0] = "Velg størrelse..";

        // Dropdown meny, spinnerType
        dropdownSecondCategory = findViewById(R.id.spinnerType);
        ArrayAdapter<String> adapterType = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, typer);
        dropdownSecondCategory.setAdapter(adapterType);

        //Dropdown meny for størrelsevalg
        dropdownSize = findViewById(R.id.spinnerStr);
        ArrayAdapter<String> adapterStr = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, str);
        dropdownSize.setAdapter(adapterStr);

        // Legger til størrelsemeny dersom plastfilm/-flaske er valgt
        dropdownSecondCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String valget = (String) dropdownSecondCategory.getSelectedItem().toString();
                String strTemp = prefs.getString("Størrelse", "Tom");
                if (valget.equals("Plastfilm")) {
                    str[1] = "Mindre enn knyttneve";
                    str[2] = "Mindre enn avis";
                    str[3] = "Større enn avis";
                    layout.setVisibility(View.VISIBLE);
                    visible = true;
                    if (!strTemp.equals("Tom"))
                        dropdownSize.setSelection(((ArrayAdapter) dropdownSize.getAdapter()).getPosition(strTemp));
                }
                else if (valget.equals("Plastflaske")) {
                    str[1] = "0,5 Liter";
                    str[2] = "Mellom 0,5 og 1,5 Liter";
                    str[3] = "1,5 Liter eller større";
                    layout.setVisibility(View.VISIBLE);
                    visible = true;
                    if (!strTemp.equals("Tom"))
                        dropdownSize.setSelection(((ArrayAdapter) dropdownSize.getAdapter()).getPosition(strTemp));
                }
                else{
                    visible = false;
                    layout.setVisibility(View.INVISIBLE);
                    editor.remove("Størrelse");
                }
                underKategori = dropdownSecondCategory.getSelectedItem().toString();
                editor.putString("Underkategori", underKategori);
                editor.apply();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        dropdownSize.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                størrelse = dropdownSize.getSelectedItem().toString();
                if (størrelse.equals("Velg størrelse.."))
                    return;
                editor.putString("Størrelse", størrelse);
                editor.apply();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    // Velger de valgene som tidligere var valgt
    public void selectOnReturn(){
        String typeTemp = prefs.getString("Underkategori", "Tom");

        if (typeTemp.equals("Tom"))
            return;

        dropdownSecondCategory.setSelection(((ArrayAdapter) dropdownSecondCategory.getAdapter()).getPosition(typeTemp));
    }
}
