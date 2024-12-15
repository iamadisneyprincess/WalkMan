package com.example.myapplication.ui.profile;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Shader;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.myapplication.R;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class profileFragment extends Fragment {

    private static final int PICK_IMAGE_REQUEST = 1;
    private static final String PREFS_NAME = "ProfilePrefs";
    private static final String KEY_NAME = "name";
    private static final String KEY_BIRTHDAY = "birthday";
    private static final String KEY_PHONE = "phone";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_IMAGE = "image";

    private ImageView logoImage;
    private EditText editTextName, editTextBirthday, editTextPhone, editTextEmail;
    private SharedPreferences sharedPreferences;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        sharedPreferences = requireContext().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);

        logoImage = view.findViewById(R.id.logoImage);
        editTextName = view.findViewById(R.id.editTextName);
        editTextBirthday = view.findViewById(R.id.editTextBirthday);
        editTextPhone = view.findViewById(R.id.editTextPhone);
        editTextEmail = view.findViewById(R.id.editTextEmail);
        Button buttonSave = view.findViewById(R.id.buttonSave);

        // Apply circular outline to the ImageView
        logoImage.setClipToOutline(true);

        // Load saved profile data
        loadProfileData();

        // Change profile picture
        logoImage.setOnClickListener(v -> openImageSelector());

        // Save button logic
        buttonSave.setOnClickListener(v -> showSaveConfirmationDialog());

        return view;
    }

    private void openImageSelector() {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setMessage("Would you like to update your profile picture?")
                .setPositiveButton("Yes", (dialog, which) -> {
                    Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    intent.setType("image/*");
                    startActivityForResult(intent, PICK_IMAGE_REQUEST);
                })
                .setNegativeButton("No", null)
                .show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null && data.getData() != null) {
            Uri imageUri = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(requireActivity().getContentResolver(), imageUri);
                Bitmap circularBitmap = getCircularBitmap(bitmap); // Make bitmap circular
                logoImage.setImageBitmap(circularBitmap);
                saveImageToPreferences(bitmap); // Save the image
            } catch (IOException e) {
                Toast.makeText(getContext(), "Failed to load image", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private Bitmap getCircularBitmap(Bitmap bitmap) {
        int size = Math.min(bitmap.getWidth(), bitmap.getHeight());
        Bitmap output = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(output);
        Paint paint = new Paint();
        paint.setAntiAlias(true);

        BitmapShader shader = new BitmapShader(bitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
        paint.setShader(shader);

        float radius = size / 2f;
        canvas.drawCircle(radius, radius, radius, paint);

        return output;
    }

    private void showSaveConfirmationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setMessage("Would you like to update your profile?")
                .setPositiveButton("Yes", (dialog, which) -> saveProfileData())
                .setNegativeButton("No", null)
                .show();
    }

    private void saveProfileData() {
        String name = editTextName.getText().toString();
        String birthday = editTextBirthday.getText().toString();
        String phone = editTextPhone.getText().toString();
        String email = editTextEmail.getText().toString();

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_NAME, name);
        editor.putString(KEY_BIRTHDAY, birthday);
        editor.putString(KEY_PHONE, phone);
        editor.putString(KEY_EMAIL, email);
        editor.apply();

        Toast.makeText(getContext(), "Profile saved successfully!", Toast.LENGTH_SHORT).show();
    }

    private void loadProfileData() {
        String name = sharedPreferences.getString(KEY_NAME, "");
        String birthday = sharedPreferences.getString(KEY_BIRTHDAY, "");
        String phone = sharedPreferences.getString(KEY_PHONE, "");
        String email = sharedPreferences.getString(KEY_EMAIL, "");
        String imageBase64 = sharedPreferences.getString(KEY_IMAGE, "");

        editTextName.setText(name);
        editTextBirthday.setText(birthday);
        editTextPhone.setText(phone);
        editTextEmail.setText(email);

        if (!imageBase64.isEmpty()) {
            Bitmap bitmap = decodeBase64(imageBase64);
            logoImage.setImageBitmap(getCircularBitmap(bitmap));
        }
    }

    private void saveImageToPreferences(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        String encodedImage = Base64.encodeToString(byteArrayOutputStream.toByteArray(), Base64.DEFAULT);

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_IMAGE, encodedImage);
        editor.apply();
    }

    private Bitmap decodeBase64(String encodedImage) {
        byte[] decodedBytes = Base64.decode(encodedImage, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
    }
}
