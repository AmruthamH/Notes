package com.example.androidnotes;

import static android.widget.Toast.LENGTH_LONG;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.Date;

public class EditActivity extends AppCompatActivity {

    EditText titleedact, contentedact;
    private Notes notesClass;
    private static final String TAG = "EditActivity";
    private String titleBefore = "",contentBefore = "", titleAfter="",contentAfter="";
    private int position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        titleedact = findViewById(R.id.titleeditactivity);
        contentedact = findViewById(R.id.contenteditactivity);

        titleedact.setTextIsSelectable(true);
        contentedact.setTextIsSelectable(true);
        contentedact.setMovementMethod(new ScrollingMovementMethod());

        Intent intentData = getIntent();
        if (intentData.hasExtra("noteData"))
        {
            notesClass = (Notes) intentData.getSerializableExtra("noteData");
            if (notesClass != null)
            {
                titleedact.setText(notesClass.getTitle());
                contentedact.setText(notesClass.getContent());
                titleBefore = notesClass.getTitle();
                contentBefore = notesClass.getContent();
            }
        }

        if (intentData.hasExtra("position"))
        {
            position = intentData.getIntExtra("position",-1);
        }

    }
    public void dateTime()
    {
        Date d = new Date();
        SimpleDateFormat ft = new SimpleDateFormat ("E MMM dd',' hh:mm a ");

        Intent intentDate = new Intent();
        intentDate.putExtra("title", titleedact.getText().toString());
        intentDate.putExtra("content",contentedact.getText().toString());
        intentDate.putExtra("date",ft.format(d));
        if(position!=-1)
            intentDate.putExtra("position",position);
        setResult(RESULT_OK,intentDate);
        Log.d(TAG, "saveDate: "+ intentDate);
        Toast.makeText(this,"Saved",Toast.LENGTH_SHORT).show();
        finish();
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.edit_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item)
    {
        switch (item.getItemId())
        {
            case R.id.savenotes:
                if (titleedact.getText().toString().equals("") && contentedact.getText().toString().equals(""))
                {
                    Toast.makeText(this,getString(R.string.emptyNotes), LENGTH_LONG).show();
                    finish();

                }
                else if (titleedact.getText().toString().equals(""))
                {

                    if(titleedact.getText().toString().equals("")) {

                        AlertDialog.Builder builder = new AlertDialog.Builder(this);

                        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                finish();
                            }
                        });
                        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                            }
                        });

                        builder.setMessage("Note without tittle cannot be saved! Do you want to exit? ");
                        AlertDialog dialog = builder.create();
                        dialog.show();
                    }
                }
                else if (contentedact.getText().toString().equals(""))
                {
                    if(contentedact.getText().toString().equals("")) {

                        AlertDialog.Builder builder = new AlertDialog.Builder(this);

                        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                finish();
                            }
                        });
                        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                            }
                        });

                        builder.setMessage("Note without Content cannot be saved! Do you want to exit? ");
                        AlertDialog dialog = builder.create();
                        dialog.show();
                    }
                }
                else
                {
                    if(changeItem())
                        dateTime();
                    else
                        finish();
                }

                break;
                default:
                Toast.makeText(this,"Invalid Option",Toast.LENGTH_SHORT).show();
        }

        return super.onOptionsItemSelected(item);
    }

    public boolean changeItem()
    {
        titleAfter = titleedact.getText().toString();
        contentAfter = contentedact.getText().toString();

        if(titleBefore.equals("") && contentBefore.equals("") && titleAfter.equals("") && contentAfter.equals(""))
            return false;
        else if(!titleBefore.equals(titleAfter) || !titleBefore.equals(contentAfter))
            return true;
        else
            return false;
    }

    @Override
    public void onBackPressed() {

        if (!titleedact.getText().toString().equals("") && !contentedact.getText().toString().equals("") && changeItem()) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);

            builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    if(changeItem()){
                        dateTime();
                    }
                    else{
                        finish();
                    }
                }
            });
            builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    finish();
                }
            });

            builder.setMessage("Your note is not saved! Save note 'Set DVR'");
            AlertDialog dialog = builder.create();
            dialog.show();
        }
        else if (titleedact.getText().toString().equals(""))
        {
            if(titleedact.getText().toString().equals("")) {

                AlertDialog.Builder builder = new AlertDialog.Builder(this);

                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        finish();
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                    }
                });

                builder.setMessage("Note without tittle cannot be saved! Do you want to exit? ");
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        }
        else if (contentedact.getText().toString().equals(""))
        {
            Toast.makeText(this,getString(R.string.noContent),Toast.LENGTH_LONG).show();
            finish();
        }

        else{
            super.onBackPressed();
        }
    }


}