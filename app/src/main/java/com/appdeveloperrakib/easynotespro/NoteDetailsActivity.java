package com.appdeveloperrakib.easynotespro;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;

public class NoteDetailsActivity extends AppCompatActivity {

    EditText titleEditText, contentEdittext;
    ImageView saveNoteBtn;
    TextView pageTitleTextView;
    String title,content,docId;
    boolean isEditMode = false;
    TextView deleteNoteTextViewbtn;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_details);


        titleEditText = findViewById(R.id.notes_title_text);
        contentEdittext = findViewById(R.id.notes_content_text);
        saveNoteBtn = findViewById(R.id.save_note_btn);
        pageTitleTextView = findViewById(R.id.page_title);
        deleteNoteTextViewbtn =findViewById(R.id.delete_note_text_btn);

        //receive data
        title = getIntent().getStringExtra("title");
        content = getIntent().getStringExtra("content");
        docId = getIntent().getStringExtra("docId");

        if(docId!=null && !docId.isEmpty() ){

            isEditMode = true;
        }

        titleEditText.setText(title);
        contentEdittext.setText(content);

        if (isEditMode) {

            pageTitleTextView.setText("Edit Your Note");
            deleteNoteTextViewbtn.setVisibility(View.VISIBLE);

        }



        saveNoteBtn.setOnClickListener(view -> saveNote());

        deleteNoteTextViewbtn.setOnClickListener(view -> {

            deleteNoteFromFirebase();


        });


    }

    void saveNote(){

        String noteTitle = titleEditText.getText().toString();
        String noteContent = contentEdittext.getText().toString();

        if(noteTitle==null || noteTitle.isEmpty()){

            titleEditText.setError("Title is required");
            return;

        }

        Note note = new Note();
        note.setTitle(noteTitle);
        note.setContent(noteContent);
        note.setTimestamp(Timestamp.now());

        saveNoteToFireBase(note);


    }

    void saveNoteToFireBase (Note note){

        DocumentReference documentReference;
        if (isEditMode) {

            //update the note
            documentReference = Utility.getCollectionReferenceForNotes().document(docId);

        }else {

            //create new note
            documentReference = Utility.getCollectionReferenceForNotes().document();

        }


        documentReference.set(note).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                if(task.isSuccessful()){

                    Utility.showToast(NoteDetailsActivity.this,"Note added sucessfully");
                    finish();
                    //Note is added

                }else {

                    Utility.showToast(NoteDetailsActivity.this,"Failed while adding Note");
                    finish();
                }


            }
        });

    }


    void deleteNoteFromFirebase(){

        DocumentReference documentReference;


            //update the note
            documentReference = Utility.getCollectionReferenceForNotes().document(docId);

        documentReference.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                if(task.isSuccessful()){

                    //Note is deleted
                    Utility.showToast(NoteDetailsActivity.this,"Note deleted sucessfully");
                    finish();


                }else {

                    Utility.showToast(NoteDetailsActivity.this,"Failed while deleting Note");
                    finish();
                }


            }
        });


    }

}