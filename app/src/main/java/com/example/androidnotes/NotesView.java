package com.example.androidnotes;

import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

public class NotesView extends RecyclerView.ViewHolder {

    public TextView title,content,datetime;

    NotesView(View v){
        super(v);
        title=v.findViewById(R.id.title);
        content=v.findViewById(R.id.content);
        datetime=v.findViewById(R.id.dateTime);

    }

}
