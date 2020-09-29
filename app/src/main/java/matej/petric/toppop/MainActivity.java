package matej.petric.toppop;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity implements SongAdapter.OnSongListener {

    ArrayList<Datum> songModels = new ArrayList<>();
    private SongAdapter songAdapter;
    private RecyclerView recyclerView;
    SwipeRefreshLayout refreshLayout;

    private RequestInteface requestInteface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        refreshLayout = findViewById(R.id.refresh_Layout);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.deezer.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        requestInteface = retrofit.create(RequestInteface.class);

        getSongResponse();

        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                finish();
                overridePendingTransition(0, 0);
                startActivity(getIntent());
                overridePendingTransition(0, 0);
            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.dots_menu, menu

        );
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.item1:
                Toast.makeText(this, "normal selected", Toast.LENGTH_SHORT).show();
                finish();
                overridePendingTransition(0, 0);
                startActivity(getIntent());
                overridePendingTransition(0, 0);
                return true;
            case R.id.item2:
                Toast.makeText(this, "asc selected", Toast.LENGTH_SHORT).show();
                if(songAdapter != null) {
                    sort("asc");
                    songAdapter.notifyDataSetChanged();
                }
                return true;
            case R.id.item3:
                Toast.makeText(this, "desc selected", Toast.LENGTH_SHORT).show();
                if(songAdapter != null) {
                    sort("desc");
                    songAdapter.notifyDataSetChanged();
                }
                return true;

        }
        return super.onOptionsItemSelected(item);
    }

    private void getSongResponse() {
        Call<MovieObject> call = requestInteface.getSongJson();

        call.enqueue(new Callback<MovieObject>() {
            @Override
            public void onResponse(Call<MovieObject> call, Response<MovieObject> response) {
                songModels = new ArrayList<Datum>(response.body().getData());
                songAdapter = new SongAdapter(MainActivity.this, songModels, MainActivity.this);
                recyclerView.setAdapter(songAdapter);
                Toast.makeText(MainActivity.this, "Success", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<MovieObject> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Failed", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onSongClick(int position) {
        Intent myIntent = new Intent(MainActivity.this, CardActivity.class);
        myIntent.putExtra("album_id", songModels.get(position).getAlbum().getId());
        myIntent.putExtra("artist_name", songModels.get(position).getArtist().getName());
        myIntent.putExtra("song_name", songModels.get(position).getTitle());
        MainActivity.this.startActivity(myIntent);
    }

    public void sort(final String type){
        Collections.sort(songModels, new Comparator<Datum>() {
            @Override
            public int compare(Datum lhs, Datum rhs) {
                if(type.equals("asc")) {
                    return lhs.getDuration().compareTo(rhs.getDuration());
                }else{
                    return rhs.getDuration().compareTo(lhs.getDuration());
                }
            }
        });
    }
}
