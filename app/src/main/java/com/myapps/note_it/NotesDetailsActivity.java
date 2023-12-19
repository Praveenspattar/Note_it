package com.myapps.note_it;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;

public class NotesDetailsActivity extends AppCompatActivity {

    EditText titleEditText,contentEditText;
    Button saveNotesBtn;
    TextView pagetitletextview;
    ImageButton deletebtn;
    String title,content,docId;
    boolean isEditMode=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes_details);

        titleEditText = findViewById(R.id.notes_title_text);
        contentEditText = findViewById(R.id.notes_content_text);
        saveNotesBtn = findViewById(R.id.savebtn);
        pagetitletextview = findViewById(R.id.page_title);
        deletebtn = findViewById(R.id.delete_btn);

        //receive data
        title = getIntent().getStringExtra("title");
        content = getIntent().getStringExtra("content");
        docId = getIntent().getStringExtra("docId");

        if (!(docId ==null) && !docId.isEmpty()){
            isEditMode=true;
        }

        if (isEditMode){
            pagetitletextview.setText("Edit Notes");
            titleEditText.setText(title);
            contentEditText.setText(content);
            deletebtn.setVisibility(View.VISIBLE);
        }

        saveNotesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveNote();
            }
        });

        deletebtn.setOnClickListener(v -> deteteNoteFromFb());

    }

    void saveNote(){

        String noteTitle = titleEditText.getText().toString();
        String noteContent = contentEditText.getText().toString();
        if (noteTitle.isEmpty()){
            titleEditText.setError("Title is required");
            return;
        }
        Note note = new Note();
        note.setTitle(noteTitle);
        note.setContent(noteContent);
        note.setTimestamp(Timestamp.now());
        saveNoteToFirebase(note);

    }

    void saveNoteToFirebase(Note note){

        DocumentReference documentReference;
        if (isEditMode){
            //update the the note
            documentReference= Utility.getCollectionReferenceForNotes().document(docId);
        }else {
            //create the note
            documentReference= Utility.getCollectionReferenceForNotes().document();
        }

        documentReference.set(note).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    //note is added
                    Utility.showtoast(NotesDetailsActivity.this,"Note added successfully");
                    finish();
                }else {
                    //notes adding failed
                    Utility.showtoast(NotesDetailsActivity.this,"Failed to add notes");
                }
            }
        });
    }

    private void deteteNoteFromFb() {
        DocumentReference documentReference;
        documentReference= Utility.getCollectionReferenceForNotes().document(docId);
        documentReference.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    //note is deleted
                    Utility.showtoast(NotesDetailsActivity.this,"Note deleted successfully");
                    finish();
                }else {
                    //notes deleting failed
                    Utility.showtoast(NotesDetailsActivity.this,"Failed to delete notes");
                }
            }
        });
    }

}