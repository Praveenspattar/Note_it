package com.myapps.note_it;

import android.annotation.SuppressLint;
import android.content.Context;
import android.widget.Toast;

import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class Utility {

    static void showtoast(Context context, String message){
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    static CollectionReference getCollectionReferenceForNotes(){
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        return FirebaseFirestore.getInstance().collection("notes")
                .document(currentUser.getUid()).collection("my_notes");
    }

    @SuppressLint("SimpleDateFormat")
    static String timestampToString(Timestamp timestamp){
        /*return new SimpleDateFormat("MM/dd/yyyy").format(timestamp.toDate());*/

        return (android.text.format.DateFormat.format(
                "dd-MM-yyyy HH:mm:ss", timestamp.toDate()).toString());

       /*return new DateFormat().toString();*/

    }

}
