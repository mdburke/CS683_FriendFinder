package com.imminentapps.friendfinder.activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferObserver;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferUtility;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.imminentapps.friendfinder.R;
import com.imminentapps.friendfinder.database.AppDatabase;
import com.imminentapps.friendfinder.database.DatabaseTask;
import com.imminentapps.friendfinder.domain.Hobby;
import com.imminentapps.friendfinder.domain.Profile;
import com.imminentapps.friendfinder.domain.User;
import com.imminentapps.friendfinder.utils.AWSCredentialsUtil;
import com.imminentapps.friendfinder.utils.Constants;
import com.imminentapps.friendfinder.utils.DBUtil;
import com.imminentapps.friendfinder.utils.UserUtil;
import com.imminentapps.friendfinder.views.CustomCanvasView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.UUID;

import static com.imminentapps.friendfinder.utils.UserUtil.getAboutMeText;
import static com.imminentapps.friendfinder.utils.UserUtil.getHobbyListText;

public class EditProfileScreen extends AppCompatActivity {
    private final String TAG = this.getClass().getSimpleName();
    private AppDatabase db;

    private User currentUser;
    private ImageView profileImageView;
    private EditText hobbyList;
    private EditText aboutMeView;
    private TextView usernameView;
    private Button saveButton;
    private CustomCanvasView canvasView;
    private AmazonS3 s3;
    private TransferUtility transferUtility;
    private Bitmap canvasBitmap;
    private String canvasImageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile_screen);
        db = DBUtil.getDBInstance();


        // Initialize vars/fields
        usernameView = findViewById(R.id.editprofile_usernameTextView);
        aboutMeView = findViewById(R.id.editprofile_aboutMeEdit);
        profileImageView = findViewById(R.id.editprofile_profileImageView);
        hobbyList = findViewById(R.id.editprofile_hobbyList);
        saveButton = findViewById(R.id.editprofile_saveButton);
        canvasView = findViewById(R.id.editprofile_canvasView);

        // Add onClickListeners
        saveButton.setOnClickListener(view -> saveButtonClicked());
        canvasView.setOnTouchListener(new CustomCanvasView.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent e) {
                return ((CustomCanvasView) v).myOnTouchEvent(e);
            }
        });

        initializeUserData();

        BasicAWSCredentials credentials = null;

        try {
            credentials = new BasicAWSCredentials(
                    AWSCredentialsUtil.getCreds("AccessKey", getApplicationContext()),
                    AWSCredentialsUtil.getCreds("SecretKey", getApplicationContext()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        s3 = new AmazonS3Client(credentials);
        transferUtility = new TransferUtility(s3, getApplicationContext());
    }

    public void clearCanvas(View v) {
        canvasView.clearCanvas();
    }

    private void initializeUserData() {
        // Grab the user information from the database based on the email passed in
        Intent intent = getIntent();
        String email = intent.getCharSequenceExtra("currentUserEmail").toString();

        DatabaseTask<String, User> task = new DatabaseTask<>(new DatabaseTask.DatabaseTaskListener<User>() {
            @Override
            public void onFinished(User user) {
                // TODO: Handle this case better
                if (user == null) {
                    throw new IllegalStateException("HomeScreen was not able to locate the logged in user.");
                }
                currentUser = user;
                // Set the views with the user data
                usernameView.setText(currentUser.getProfile().getUsername());
                aboutMeView.setText(getAboutMeText(currentUser));
                hobbyList.setText(getHobbyListText(currentUser));
                setupProfileImage();
            }
        }, new DatabaseTask.DatabaseTaskQuery<String, User>() {
            @Override
            public User execute(String... emails) {
                return UserUtil.loadUser(emails[0]);
            }
        });

        task.execute(email);
    }

    private void saveButtonClicked() {
        // Get the new data to save
        int profileId = currentUser.getProfile().getProfileId();
        Hobby[] hobbies = UserUtil.textToHobbyArray(profileId, hobbyList.getText().toString());
        String aboutMe = aboutMeView.getText().toString();

        // Save canvas view
        canvasView.buildDrawingCache();
        canvasBitmap = Bitmap.createBitmap(canvasView.getDrawingCache());

        AsyncTask<Void, Void, Void> saveImageTask = new AsyncTask<Void, Void, Void>() {
            @Override
            protected void onPostExecute(Void aVoid) {
                Profile profile = currentUser.getProfile();
                profile.setAboutMeSection(aboutMe);
                profile.setProfileCanvasUri(canvasImageUri);

                DatabaseTask<String, Void> task = new DatabaseTask<>(new DatabaseTask.DatabaseTaskListener<Void>() {
                    @Override
                    public void onFinished(Void result) {

                    }
                }, new DatabaseTask.DatabaseTaskQuery<String, Void>() {
                    @Override
                    public Void execute(String... emails) {
                        // Save button
                        db.hobbyDao().insert(hobbies);
                        db.profileDao().update(profile);
                        return null;
                    }
                });
                task.execute();
            }

            @Override
            protected Void doInBackground(Void... strings) {
                saveCanvasView();
                return null;
            }
        };
        saveImageTask.execute();
    }

    private void saveCanvasView() {
        FileOutputStream outputStream = null;

        // Create a unique file name for this image.
        String filename = currentUser.getEmail().concat("_canvasImage_").concat(UUID.randomUUID().toString());

        try {
            // Adapted from https://stackoverflow.com/questions/2227209/how-to-get-the-images-from-device-in-android-java-application and
            // https://stackoverflow.com/questions/7769806/convert-bitmap-to-file

            // Convert bitmap to byte array
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            canvasBitmap.compress(Bitmap.CompressFormat.PNG, 0, byteArrayOutputStream);
            byte[] bitmapData = byteArrayOutputStream.toByteArray();

            // Write bytes to file
            outputStream = openFileOutput(filename, Context.MODE_PRIVATE);
            outputStream.write(bitmapData);

            // Save the uri to the profile
            canvasImageUri = filename;

            // Upload image to S3
            File newFile = new File(getApplicationContext().getFilesDir() + "/" + canvasImageUri);
            TransferObserver observer = transferUtility.upload(
                    Constants.AWS_PROFILE_IMAGE_BUCKET,
                    canvasImageUri,
                    newFile
            );
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, "Error saving canvas image.");
        } finally {
            if (outputStream != null) {
                try {
                    outputStream.flush();
                    outputStream.close();
                } catch (IOException e) {
                    Log.e(TAG, "Error closing output stream.");
                }
            }
        }
    }

    /**
     * Grabs the profile image from the file system, transforms to Bitmap
     * and set the profileImageView to that bitmap.
     */
    private void setupProfileImage() {
        // TODO: Add a default image if bitmap is null or the uri is null
        if (currentUser.getProfile().getProfileImageUri() != null) {
            Bitmap bitmap = null;
            String uri = currentUser.getProfile().getProfileImageUri();
            FileInputStream inputStream;

            try {
                inputStream = openFileInput(uri);
                bitmap = BitmapFactory.decodeStream(inputStream);
                inputStream.close();
            } catch (Exception e) {
                Log.e(this.getClass().toString(), "Error loading image");
                e.printStackTrace();
            }

            if (bitmap != null) {
                profileImageView.setImageBitmap(bitmap);
            }
        }
    }
}
