package matej.petric.toppop;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import java.util.ArrayList;


public class SongAdapter extends RecyclerView.Adapter<SongAdapter.ViewHolder> {

    private ArrayList<Datum> songModels = new ArrayList<>();
    private Context context;
    private OnSongListener mOnSongListener;
    public SongAdapter(Context context, ArrayList<Datum> songModels, OnSongListener OnSongListener) {
        this.songModels = songModels;
        this.context = context;
        this.mOnSongListener = OnSongListener;
    }

    @NonNull
    @Override
    public SongAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.song_list_item, viewGroup, false);
        return new SongAdapter.ViewHolder(view, mOnSongListener);
    }

    @Override
    public void onBindViewHolder(@NonNull SongAdapter.ViewHolder viewHolder, int i) {
        viewHolder.TextView2.setText(songModels.get(i).getTitle());
        viewHolder.TextView1.setText((i + 1) + "");
        viewHolder.TextView3.setText(songModels.get(i).getArtist().getName());
        viewHolder.TextView4.setText("Duration: " + songModels.get(i).getDuration()/60 +"m"+":"+songModels.get(i).getDuration()%60+"sec" );
    }

    @Override
    public int getItemCount() {
        return songModels.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView TextView1, TextView2, TextView3, TextView4;
        OnSongListener OnSongListener;
        public ViewHolder(@NonNull View itemView, OnSongListener mOnSongListener) {
            super(itemView);

            TextView1 = itemView.findViewById(R.id.textView1);
            TextView2 = itemView.findViewById(R.id.textView2);
            TextView3 = itemView.findViewById(R.id.textView3);
            TextView4 = itemView.findViewById(R.id.textView4);
            this.OnSongListener = mOnSongListener;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            OnSongListener.onSongClick(getAdapterPosition());
        }
    }
    public interface OnSongListener{
        void onSongClick(int position);
    }
}

