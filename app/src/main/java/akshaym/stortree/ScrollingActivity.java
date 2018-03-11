package akshaym.stortree;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.ArrayList;

import java.io.IOException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.*;

public class ScrollingActivity extends AppCompatActivity {

    private ArrayList<String> list;
    private StorageReference storageRef;
    private int numArch = 0;

    private ProgressDialog pdLoading;
    public static Context context;



    public static int randomIntGenerator(int start, int end)
    {
        return (int) (Math.random()*99);
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        setContentView(R.layout.activity_scrolling);
        pdLoading = new ProgressDialog(this);
        pdLoading.setMessage("\tLoading...");
        pdLoading.setCancelable(false);
        pdLoading.setCanceledOnTouchOutside(false);
        pdLoading.show();
        Intent i = getIntent();
        list = i.getStringArrayListExtra("List");
        boolean isFinished = i.getBooleanExtra("finished", false);
        TextView textView = (TextView) findViewById(R.id.scrollText);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if(!isFinished) {
            textView.setText(list.get(list.size() - 2) + "\n" + list.get(list.size() - 1));
        }else{
            String finalStory = getStory();
            textView.setText(finalStory);
            storageRef = FirebaseStorage.getInstance().getReference().child("archived/num.txt");
            try {
                /*Task<byte[]> temp = storageRef.getBytes(1000000).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                    @Override
                    public void onSuccess(byte[] bytes) {
                        numArch = Integer.parseInt(new String(bytes));
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        exception.printStackTrace();
                    }
                });*/
                Task<byte[]> temp = storageRef.getBytes(1000000);
                while(!temp.isComplete()){
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                numArch = Integer.parseInt(new String(temp.getResult()));
            }catch(NullPointerException npe) {
                numArch = 0;
            }
            numArch++;
            UploadTask temp = storageRef.putBytes(String.valueOf(numArch).getBytes());
            storageRef = FirebaseStorage.getInstance().getReference().child("archived/arch"+(numArch)+".txt");
            byte[] finalByte = finalStory.getBytes();
            UploadTask uploadTask = storageRef.putBytes(finalByte);
            uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    // Handle unsuccessful uploads
                    numArch--;
                     storageRef.putBytes(String.valueOf(numArch).getBytes());
                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
                    Toast.makeText(ScrollingActivity.this, "Archival Successful!", Toast.LENGTH_SHORT).show();
                }
            });
            new AsyncTopics().execute();

        }

        pdLoading.dismiss();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_scrolling, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent i = new Intent(ScrollingActivity.this, NavActivity.class);
            startActivity(i);
        }
        return super.onOptionsItemSelected(item);
    }

    public String getStory()
    {
        String toReturn = "";
        for(int i=0; i<list.size(); i++){
            toReturn+=list.get(i);
            toReturn+="\n";
        }
        return toReturn;
    }
    private class AsyncTopics extends AsyncTask<Void, Void, Void>
    {
        ProgressDialog pdLoading = new ProgressDialog(ScrollingActivity.context);
        String[] positions;
        Element promptDiv;
        Elements promptTags;
        List<String> prompts;
        String prompt;
        DatabaseReference myRef;
        ArrayList<String> newList;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //this method will be running on UI thread
            pdLoading.setMessage("\tLoading...");
            pdLoading.setCancelable(false);
            pdLoading.setCanceledOnTouchOutside(false);

            pdLoading.show();
        }
        @Override
        protected Void doInBackground(Void... params) {
            Document doc = null;
            try {
                doc = Jsoup.connect("https://letterpile.com/writing/99-Starter-Sentences").get();
            } catch (IOException e) {
                e.printStackTrace();
            }
            promptDiv = doc.getElementById("txtd_7669545");
            promptTags = promptDiv.getElementsByTag("li");
            prompts = promptTags.eachText();
            prompt = prompts.get(randomIntGenerator(0, 98));
            myRef = FirebaseDatabase.getInstance().getReference();
            newList = new ArrayList<String>();
            newList.add(prompt);
            myRef.setValue(newList);
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);

            pdLoading.dismiss();

        }

    }
}

