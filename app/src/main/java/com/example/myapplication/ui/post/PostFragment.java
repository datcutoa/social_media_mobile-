package com.example.myapplication.ui.post;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.myapplication.DAO.SocialNetworkDatabase;
import com.example.myapplication.Entity.Post;
import com.example.myapplication.R;
import java.util.List;
import java.util.concurrent.Executors;

public class PostFragment extends Fragment {

    private RecyclerView recyclerView;
    private PostAdapter postAdapter;
    private SocialNetworkDatabase db;

    public PostFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate layout
        View rootView = inflater.inflate(R.layout.fragment_post, container, false);

        // Init database
        db = SocialNetworkDatabase.getInstance(getContext());

        // Init RecyclerView
        recyclerView = rootView.findViewById(R.id.recyclerPosts);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Load posts in background thread
        Executors.newSingleThreadExecutor().execute(() -> {
            List<Post> postList = db.postDao().getAllPosts();

            // Update UI on main thread
            requireActivity().runOnUiThread(() -> {
                postAdapter = new PostAdapter(postList, db);
                recyclerView.setAdapter(postAdapter);
            });
        });

        return rootView;
    }
}
