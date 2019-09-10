package tec.mapsexample;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Date;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        displaySessions();
    }

    public void onNewSession_Click(View view){
        Intent intent = new Intent(this,MapTrackerActivity.class);
        intent.putExtra("display",false);
        startActivity(intent);
    }

    private void onDisplaySession_Click(int i){
        SessionsList.getInstance().setActual(i);
        Intent intent = new Intent(this,MapTrackerActivity.class);
        intent.putExtra("display",true);
        startActivity(intent);
    }



    private void displaySessions(){
        ListView listSessions = findViewById(R.id.listSessions);

        ArrayAdapter<String> itemsAdapter =
                new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,SessionsList.getInstance().names );
        listSessions.setAdapter(itemsAdapter);
        listSessions.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                onDisplaySession_Click(i);
            }
        });

    }


}
