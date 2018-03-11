package akshaym.stortree;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {


    public static final int MAX_SENTENCES = 7;

    FirebaseDatabase database;
    DatabaseReference myRef;
    private ProgressDialog pdLoading;
    private ArrayList<String> list;
    private EditText editText;
    private TextView textView;
    private Button button;
    private String prompt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        pdLoading = new ProgressDialog(this);
        pdLoading.setMessage("\tLoading...");
        pdLoading.setCancelable(false);
        pdLoading.setCanceledOnTouchOutside(false);
        pdLoading.show();

        Intent i = getIntent();
        prompt = i.getStringExtra("Prompt");
        editText = (EditText) findViewById(R.id.editText);
        textView = (TextView) findViewById(R.id.textView);
        button = (Button) findViewById(R.id.button2);

        button.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                String toSend = editText.getText().toString();
                if(toSend!=null&&!toSend.equals("")) {
                    addString(toSend);
                    Intent intent = new Intent("akshayaryamichellewilhacks");
                    intent.putExtra("List", list);
                    intent.putExtra("finished", false);
                    startActivity(intent);
                }else{
                    Toast.makeText(MainActivity.this, "Please fill in the field before submitting.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        database = FirebaseDatabase.getInstance();
        myRef = database.getReference();
        myRef.addValueEventListener(new ValueEventListener(){
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                GenericTypeIndicator<ArrayList<String>> t = new GenericTypeIndicator<ArrayList<String>>() {};
                ArrayList<String> value = dataSnapshot.getValue(t);
                list = value;
                if(list==null){
                    list = new ArrayList<String>();
                    addString(prompt);
                }

                if(list.size()>MAX_SENTENCES){
                    Toast.makeText(MainActivity.this, "The story is over: Time to view it!", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent("akshayaryamichellewilhacks");
                    intent.putExtra("List", list);
                    intent.putExtra("finished", true);
                    startActivity(intent);
                }
                textView.setText(textView.getText().toString() + getLast());

                pdLoading.dismiss();
            }
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("TAG", "Failed to read value.", error.toException());
            }
        });




    }


    public String getValue(int index)
    {
        return list.get(index);
    }

    public String getLast()
    {
        return list.get(list.size()-1);
    }




    public void addString(String s)
    {
        list.add(s);
        myRef.setValue(list);
    }

}
