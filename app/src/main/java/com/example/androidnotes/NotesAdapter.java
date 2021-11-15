package com.example.androidnotes;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;



public class NotesAdapter extends RecyclerView.Adapter<NotesView> {
    private static final String TAG = "NotesAdapter";

    private List<Notes> notesList;
    private MainActivity mainActivity;

    NotesAdapter(List<Notes> notesList,MainActivity m){
        this.notesList=notesList;
        this.mainActivity=m;
    }



    @NonNull
    @Override
    public NotesView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View notesInflater= LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_notes,parent,false);

        notesInflater.setOnClickListener(mainActivity);
        notesInflater.setOnLongClickListener(mainActivity);

        return new NotesView(notesInflater);
    }

    @Override
    public void onBindViewHolder(@NonNull NotesView holder, int position) {

        Notes n =notesList.get(position);

        //holder.title.setText(n.getTitle());
        //holder.datetime.setText(n.getDate());
        //holder.content.setText(n.getContent());

        if (n.getTitle().length() > 80){
            holder.title.setText(String.format("%s...", n.getTitle().substring(0, 80)));
        }
        else {
            holder.title.setText(n.getTitle());
        }

        if (n.getContent().length() > 80){
            holder.content.setText(String.format("%s...", n.getContent().substring(0, 80)));
            Log.d(TAG, "onBindViewHolder: "+ holder.content);
        }
        else {
            holder.content.setText(n.getContent());
        }
        holder.datetime.setText(n.getDate());


    }

    @Override
    public int getItemCount() {
        Log.d(TAG, "getItemCount: ");
        return notesList.size();
    }
}
