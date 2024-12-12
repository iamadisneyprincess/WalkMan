package com.example.myapplication.ui.community;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;

import java.util.ArrayList;
import java.util.List;

public class CommunityFragment extends Fragment {

    private RecyclerView recyclerViewPosts;
    private PostAdapter postAdapter;
    private List<Post> mockPosts;

    // Required empty public constructor
    public CommunityFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_community, container, false);

        // Initialize RecyclerView
        recyclerViewPosts = view.findViewById(R.id.recyclerViewPosts);
        recyclerViewPosts.setLayoutManager(new LinearLayoutManager(getContext()));

        // Set up mock data for posts
        mockPosts = new ArrayList<>();
        mockPosts.add(new Post("John", "Looking forward to the upcoming event!", 10));
        mockPosts.add(new Post("Sarah", "Great meeting everyone last night!", 5));
        mockPosts.add(new Post("Alex", "Had an amazing time today!", 3));
        mockPosts.add(new Post("Mark", "Enjoyed Walking with my DOGGO! LOLXX!", 100));

        // Initialize and set the adapter
        postAdapter = new PostAdapter(mockPosts);
        recyclerViewPosts.setAdapter(postAdapter);

        // Initialize Join Community button
        Button joinCommunityButton = view.findViewById(R.id.joinCommunityButton);
        joinCommunityButton.setOnClickListener(v -> {
            // Handle button click
            Toast.makeText(getActivity(), "You joined the community!", Toast.LENGTH_SHORT).show();
        });

        return view;
    }

    // Data class for posts
    public static class Post {
        String username;
        String content;
        int reactions;

        public Post(String username, String content, int reactions) {
            this.username = username;
            this.content = content;
            this.reactions = reactions;
        }
    }

    // Adapter for RecyclerView to display posts
    public static class PostAdapter extends RecyclerView.Adapter<PostAdapter.PostViewHolder> {

        private List<Post> posts;

        public PostAdapter(List<Post> posts) {
            this.posts = posts;
        }

        @Override
        public PostViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_post, parent, false);
            return new PostViewHolder(view);
        }

        @Override
        public void onBindViewHolder(PostViewHolder holder, int position) {
            Post post = posts.get(position);
            holder.bind(post);
        }

        @Override
        public int getItemCount() {
            return posts.size();
        }

        // ViewHolder for each post
        public static class PostViewHolder extends RecyclerView.ViewHolder {
            private TextView postContentTextView;
            private TextView reactionsCountTextView;

            public PostViewHolder(View itemView) {
                super(itemView);
                postContentTextView = itemView.findViewById(R.id.postContent);
                reactionsCountTextView = itemView.findViewById(R.id.reactionsCount);
            }

            public void bind(Post post) {
                postContentTextView.setText(post.content);
                reactionsCountTextView.setText(post.reactions + " Reactions");
            }
        }
    }
}
