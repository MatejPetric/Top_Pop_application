package matej.petric.toppop;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import matej.petric.toppop.album.AlbumMainObject;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CardActivity extends AppCompatActivity implements SongAdapter.OnSongListener {

    private RequestInteface requestInteface;

    private int albumId;
    private ProgressDialog progressDialog;

    private ImageView imageViewPoster;
    private TextView textViewSongName, textViewAlbumName, textViewArtistName, textViewSongsList;

    ArrayList<Datum> songModels = new ArrayList<>();
    private AlbumMainObject albumMainObject;
    private String songName, artistName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card);

        albumId = getIntent().getIntExtra("album_id", -1);
        songName = getIntent().getStringExtra("song_name");
        artistName = getIntent().getStringExtra("artist_name");

        imageViewPoster = findViewById(R.id.imageView);
        textViewSongName = findViewById(R.id.textView11);
        textViewAlbumName = findViewById(R.id.textView22);
        textViewArtistName = findViewById(R.id.textView33);
        textViewSongsList = findViewById(R.id.textView44);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.deezer.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        requestInteface = retrofit.create(RequestInteface.class);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please wait...");
        progressDialog.setCancelable(false);

        getAlbumDetail();

    }

    private void getAlbumDetail() {
        progressDialog.show();
        Call<AlbumMainObject> call = requestInteface.getAlbumJson(String.valueOf(albumId));

        call.enqueue(new Callback<AlbumMainObject>() {
            @Override
            public void onResponse(Call<AlbumMainObject> call, Response<AlbumMainObject> response) {
                progressDialog.dismiss();
                if (response != null && response.isSuccessful()) {
                    if (response.body() != null) {
                        albumMainObject = response.body();
                        if (albumMainObject != null) {
                            Picasso.get().load(albumMainObject.getCoverMedium()).into(imageViewPoster);
                            textViewAlbumName.setText(albumMainObject.getTitle());
                            textViewSongName.setText(songName);
                            textViewArtistName.setText(artistName);
                            songModels = new ArrayList<Datum>(albumMainObject.getTracks().getData());
                            StringBuilder stringBuilder = new StringBuilder();
                            for (Datum name : songModels) {
                                stringBuilder.append(name.getTitle()).append("\n");
                            }
                            textViewSongsList.setText(stringBuilder.toString());
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<AlbumMainObject> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(CardActivity.this, "Failed", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onSongClick(int position) {

    }

}