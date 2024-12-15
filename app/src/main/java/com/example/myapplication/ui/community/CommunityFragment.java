package com.example.myapplication.ui.community;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.myapplication.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.List;

public class CommunityFragment extends Fragment {

    private RecyclerView recyclerViewPosts;
    private PostAdapter postAdapter;
    private List<Post> posts;
    private SharedPreferences sharedPreferences;
    private Button addUpdateButton;
    private EditText updateEditText;
    private Button saveUpdateButton;

    private String username;

    // Required empty public constructor
    public CommunityFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_community, container, false);

        // Initialize SharedPreferences
        sharedPreferences = getActivity().getSharedPreferences("CommunityPrefs", Context.MODE_PRIVATE);

        // Initialize UI elements
        addUpdateButton = view.findViewById(R.id.addUpdateButton);
        recyclerViewPosts = view.findViewById(R.id.recyclerViewPosts);
        updateEditText = view.findViewById(R.id.updateEditText);
        saveUpdateButton = view.findViewById(R.id.saveUpdateButton);

        // Initialize username from SharedPreferences (assuming the username is saved here)
        username = sharedPreferences.getString("username", "User");

        recyclerViewPosts.setLayoutManager(new LinearLayoutManager(getContext()));

        // Load posts from SharedPreferences
        posts = loadPostsFromSharedPreferences();

        // Initialize and set the adapter
        postAdapter = new PostAdapter(posts, this::onPostClick);
        recyclerViewPosts.setAdapter(postAdapter);

        // Set up Add Update button functionality
        addUpdateButton.setOnClickListener(v -> onAddUpdateClick());

        // Set up Save Update button functionality
        saveUpdateButton.setOnClickListener(v -> onSaveUpdateClick());

        // Initially hide the EditText and Save Update button
        updateEditText.setVisibility(View.GONE);
        saveUpdateButton.setVisibility(View.GONE);

        return view;
    }

    // Method to handle 'Add Update' button click
    private void onAddUpdateClick() {
        // Show EditText and Save Update button
        updateEditText.setVisibility(View.VISIBLE);
        saveUpdateButton.setVisibility(View.VISIBLE);

        // Optionally, focus the EditText
        updateEditText.requestFocus();
    }

    // Method to handle 'Save Update' button click
    private void onSaveUpdateClick() {
        String updateContent = updateEditText.getText().toString().trim();

        if (updateContent.isEmpty()) {
            Toast.makeText(getActivity(), "Please enter some content for your update!", Toast.LENGTH_SHORT).show();
            return;
        }

        // Add the new post with the user's update
        addNewPost(updateContent, 0); // Default reactions set to 0
        updateEditText.setText(""); // Clear the EditText after adding

        // Hide EditText and Save Update button after adding the update
        updateEditText.setVisibility(View.GONE);
        saveUpdateButton.setVisibility(View.GONE);
    }

    // Method to add a new post
    private void addNewPost(String content, int reactions) {
        Post newPost = new Post(username, content, reactions);
        posts.add(newPost);
        postAdapter.notifyItemInserted(posts.size() - 1); // Notify adapter that an item was inserted
        savePostsToSharedPreferences();
    }

    // Method to delete a post
    private void deletePost(Post post) {
        posts.remove(post);
        postAdapter.notifyDataSetChanged(); // Refresh the adapter after deletion
        savePostsToSharedPreferences();
    }

    // Method to handle click on a post (e.g., delete post)
    private void onPostClick(Post post) {
        // Show a dialog with options to delete the post
        new AlertDialog.Builder(getContext())
                .setTitle("Manage Post")
                .setMessage("Would you like to delete this post?")
                .setNegativeButton("Delete", (dialog, which) -> {
                    // Delete the post
                    deletePost(post);
                    Toast.makeText(getActivity(), "Post deleted!", Toast.LENGTH_SHORT).show();
                })
                .setNeutralButton("Cancel", null)  // Cancel button
                .show();
    }

    // Method to save posts to SharedPreferences
    private void savePostsToSharedPreferences() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        String jsonPosts = new Gson().toJson(posts);
        editor.putString("posts", jsonPosts);
        editor.apply(); // Apply changes to SharedPreferences
    }

    // Method to load posts from SharedPreferences
    private List<Post> loadPostsFromSharedPreferences() {
        String jsonPosts = sharedPreferences.getString("posts", "[]"); // Default to empty list
        Type type = new TypeToken<List<Post>>(){}.getType();
        return new Gson().fromJson(jsonPosts, type);
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

        private static List<Post> posts;
        private OnPostClickListener onPostClickListener;

        public PostAdapter(List<Post> posts, OnPostClickListener listener) {
            this.posts = posts;
            this.onPostClickListener = listener;
        }

        @Override
        public PostViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_post, parent, false);
            return new PostViewHolder(view, onPostClickListener);
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

            public PostViewHolder(View itemView, OnPostClickListener listener) {
                super(itemView);
                postContentTextView = itemView.findViewById(R.id.postContent);
                reactionsCountTextView = itemView.findViewById(R.id.reactionsCount);
                itemView.setOnClickListener(v -> listener.onPostClick(posts.get(getAdapterPosition())));
            }

            public void bind(Post post) {
                postContentTextView.setText(post.content);
                reactionsCountTextView.setText(post.reactions + " Reactions");
            }
        }

        public interface OnPostClickListener {
            void onPostClick(Post post);
        }
    }
}
