package com.myapps.note_it;


import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

public class NotesAdapter extends FirestoreRecyclerAdapter<Note, NotesAdapter.NotesViewHolder> {

    Context context;

    public NotesAdapter(@NonNull FirestoreRecyclerOptions<Note> options, Context context) {
        super(options);
        this.context = context;
    }

    @Override
    protected void onBindViewHolder(@NonNull NotesViewHolder holder, int position, @NonNull Note note) {
        holder.titleTextview.setText(note.title);
        holder.contentTextview.setText(note.content);
        holder.timestampTextview.setText(Utility.timestampToString(note.timestamp));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, NotesDetailsActivity.class);
                intent.putExtra("title",note.title);
                intent.putExtra("content",note.content);
                String docId = getSnapshots().getSnapshot(position).getId();
                intent.putExtra("docId",docId);
                context.startActivity(intent);
            }
        });

    }

    @NonNull
    @Override
    public NotesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_note_item,parent,false);
        return new NotesViewHolder(view);
    }

    public class NotesViewHolder extends RecyclerView.ViewHolder{
        TextView titleTextview,contentTextview,timestampTextview;

        public NotesViewHolder(@NonNull View itemView) {
            super(itemView);

            titleTextview = (itemView).findViewById(R.id.note_title_textView);
            contentTextview = (itemView).findViewById(R.id.note_content_textView);
            timestampTextview = (itemView).findViewById(R.id.note_timestamp_textView);

        }
    }
}