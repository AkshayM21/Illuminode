package akshaym.stortree;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by adven on 10/14/2017.
 */

public class ChooseActivity extends AppCompatActivity {

    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        String prompt = "Tray walked down the stairs, only to come face to face with a lion.";
        Intent intent = new Intent("holywhyisitsocoldinthisroomwilhacks2017");
        intent.putExtra("Prompt", prompt);
        startActivity(intent);
    }


}
