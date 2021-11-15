package com.example.androidnotes;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.JsonReader;
import android.util.JsonWriter;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, View.OnLongClickListener {

    private final ArrayList<Notes> notesArrayList=new ArrayList<>();
    private RecyclerView recycleView;
    private NotesAdapter nAdapter;
    private static final String TAG = "Main Activity";
    private ActivityResultLauncher<Intent> activityResultLauncher,activityEditResultLauncher;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recycleView = findViewById(R.id.recyclenotes);
        nAdapter =new NotesAdapter(notesArrayList, this);
        recycleView.setAdapter(nAdapter);
        recycleView.setLayoutManager(new LinearLayoutManager(this));
        Log.d(TAG, "onCreate: "+ notesArrayList);

        activityResultLauncher=registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == EditActivity.RESULT_OK) {
                            Intent data = result.getData();
                            doOperation(1, data);
                        }
                    }
                }

        );

        activityEditResultLauncher=registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == EditActivity.RESULT_OK) {
                            Intent data = result.getData();
                            doOperation(2, data);
                        }
                    }
                }

        );
        updateTitleTotal();
        jsonFileLoading();

    }
    protected void doOperation(int requestCode, @Nullable Intent data){
        try {
            switch (requestCode) {
                case 1:
                    Notes notes1 = new Notes();
                    assert data != null;
                    notes1.setTitle(data.getStringExtra("title"));
                    notes1.setContent(data.getStringExtra("content"));
                    notes1.setDate(data.getStringExtra("date"));
                    Log.d(TAG, "onActivityResult: "+notes1);
                    notesArrayList.add(0, notes1);
                    nAdapter.notifyDataSetChanged();
                    break;
                case 2:
                    Notes notes2 = new Notes();
                    assert data != null;
                    notes2.setTitle(data.getStringExtra("title"));
                    notes2.setContent(data.getStringExtra("content"));
                    notes2.setDate(data.getStringExtra("date"));
                    notesArrayList.remove(data.getIntExtra("position", -1));
                    notesArrayList.add(0, notes2);
                    nAdapter.notifyDataSetChanged();
                    break;
                default:
                    Log.d(TAG, "onActivityResult: Request Code" + requestCode);
            }
        } catch (Exception e) {
            Toast.makeText(this, "Null Pointer encountered.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onClick(View view) {
        int position = recycleView.getChildLayoutPosition(view);
        Notes notesLi = notesArrayList.get(position);
        Intent data = new Intent(this, EditActivity.class);
        data.putExtra("noteData",notesLi);
        data.putExtra("position", position);
        activityEditResultLauncher.launch(data);
    }

    @Override
    public boolean onLongClick(View view) {
        final int position = recycleView.getChildLayoutPosition(view);
        final Notes n = notesArrayList.get(position);

        AlertDialog.Builder build= new AlertDialog.Builder(this);
        build.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                notesArrayList.remove(position);
                nAdapter.notifyDataSetChanged();
                updateTitleTotal();
            }
        });
        build.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {

            }
        });

        build.setMessage("Delete Note '"+notesArrayList.get(position).getTitle()+"'");

        AlertDialog alertDialog = build.create();
        alertDialog.show();
        return false;
    }

    public void updateTitleTotal()
    {
        int totalNumOfNotes = notesArrayList.size();
        if (totalNumOfNotes != 0)
            setTitle(getString(R.string.app_name)+ " (" + totalNumOfNotes + ")");
        else
            setTitle(getString(R.string.app_name));
    }

    private void jsonFileLoading() {

        Log.d(TAG, "loadFile: JSON File Loading");
        try {
            InputStream inputStream = getApplicationContext().openFileInput(getString(R.string.file_name));
            JsonReader jsonReader = new JsonReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));

            jsonReader.beginArray();
            while (jsonReader.hasNext())
            {
                Notes notesObject = new Notes();
                jsonReader.beginObject();
                while (jsonReader.hasNext())
                {
                    String name = jsonReader.nextName();
                    switch (name)
                    {

                        case "title":
                            notesObject.setTitle(jsonReader.nextString());
                            break;
                        case "content":
                            notesObject.setContent(jsonReader.nextString());
                            break;
                        case "date":
                            notesObject.setDate(jsonReader.nextString());
                            break;
                        default:
                            jsonReader.skipValue();
                            break;
                }
                }
                jsonReader.endObject();
                Log.d(TAG, "loadFile: File getting loaded" + notesObject);
                notesArrayList.add(notesObject);
            }
            jsonReader.endArray();
        }
        catch (FileNotFoundException e) {
            Toast.makeText(this, getString(R.string.noFile), Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.about:
                Toast.makeText(this, "About", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(this, AboutActivity.class);
                startActivity(intent);
                return true;
        case R.id.newnotes:
            Toast.makeText(this, "New Notes", Toast.LENGTH_SHORT).show();
            Intent intent2 = new Intent(this, EditActivity.class);
            activityResultLauncher.launch(intent2);

            return true;
        default:
        return super.onOptionsItemSelected(item);
        }

    }

    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            switch (requestCode) {
                case 1:
                    if (resultCode == RESULT_OK) {
                        Notes saveNoteObject = new Notes();
                        assert data != null;
                        saveNoteObject.setTitle(data.getStringExtra("title"));
                        saveNoteObject.setContent(data.getStringExtra("content"));
                        saveNoteObject.setDate(data.getStringExtra("date"));
                        Log.d(TAG, "onActivityResult: " + saveNoteObject);
                        notesArrayList.add(0, saveNoteObject);
                        nAdapter.notifyDataSetChanged();
                    }
                    break;
                case 2:
                    if (resultCode == RESULT_OK) {
                        Notes editNoteObject = new Notes();
                        assert data != null;
                        editNoteObject.setTitle(data.getStringExtra("title"));
                        editNoteObject.setContent(data.getStringExtra("content"));
                        editNoteObject.setDate(data.getStringExtra("date"));
                        notesArrayList.remove(data.getIntExtra("position", -1));
                        notesArrayList.add(0, editNoteObject);
                        nAdapter.notifyDataSetChanged();
                    }

                    break;
                default:
                    Log.d(TAG, "onActivityResult: Request Code" + requestCode);
            }

        } catch (Exception e) {
            Toast.makeText(this, "Null Pointer encountered.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onResume()
    {
        updateTitleTotal();
        super.onResume();
    }

    protected void onPause() {
        super.onPause();
        savingNotes();

    }

    private void savingNotes() {

        Log.d(TAG, "saveNote: Saving JSON File");
        try {
            Toast.makeText(this, "Inside saving notes", Toast.LENGTH_SHORT).show();
            FileOutputStream fileOutput = getApplicationContext().
                    openFileOutput(getString(R.string.file_name), Context.MODE_PRIVATE);

            JsonWriter jsonWriter = new JsonWriter(new OutputStreamWriter(fileOutput, getString(R.string.encoding)));
            jsonWriter.setIndent("  ");
            jsonWriter.beginArray();
            for (Notes note : notesArrayList)
            {
                jsonWriter.beginObject();
                jsonWriter.name("title").value(note.getTitle());
                Log.d(TAG, "saveNotes: "+note.getContent()+ note.getTitle());
                jsonWriter.name("content").value(note.getContent());
                jsonWriter.name("date").value(note.getDate());
                jsonWriter.endObject();
            }
            jsonWriter.endArray();
            jsonWriter.close();
        } catch (Exception e) {
            e.getStackTrace();
        }
    }

    @Override
    public void onBackPressed() {
        Toast.makeText(this, "The back button pressed", Toast.LENGTH_SHORT).show();
        super.onBackPressed();
    }

}