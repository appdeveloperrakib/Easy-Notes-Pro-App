package com.appdeveloperrakib.easynotespro;

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

import org.w3c.dom.Text;

public class NoteAdapter extends FirestoreRecyclerAdapter<Note, NoteAdapter.NotViewHolder> {
    Context context;


    public NoteAdapter(@NonNull FirestoreRecyclerOptions<Note> options, Context context) {
        super(options);
        this.context = context;
    }

    @Override
    protected void onBindViewHolder(@NonNull NotViewHolder holder, int position, @NonNull Note note) {

        holder.titletextview.setText(note.Title);
        holder.contentTextView.setText(note.content);
        holder.timestampTextView.setText(Utility.timestampToString(note.timestamp));


        holder.itemView.setOnClickListener(view -> {

            Intent intent = new Intent(context,NoteDetailsActivity.class);
            intent.putExtra("title",note.Title);
            intent.putExtra("content",note.content);
            String docId = this.getSnapshots().getSnapshot(position).getId();
            intent.putExtra("docId",docId);
            context.startActivity(intent);

        });





    }

    @NonNull
    @Override
    public NotViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyler_note_item,parent,false);

        return new NotViewHolder(view);

    }

    class NotViewHolder extends RecyclerView.ViewHolder{


        TextView titletextview, contentTextView,timestampTextView;



        public NotViewHolder(@NonNull View itemView) {
            super(itemView);

            titletextview = itemView.findViewById(R.id.note_title_text_view);
            contentTextView = itemView.findViewById(R.id.note_content_text_view);
            timestampTextView = itemView.findViewById(R.id.note_timestamp_text_view);



        }
    }



}
