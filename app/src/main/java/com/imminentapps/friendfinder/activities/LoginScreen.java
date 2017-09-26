package com.imminentapps.friendfinder.activities;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import com.imminentapps.friendfinder.R;
import com.imminentapps.friendfinder.database.AppDatabase;
import com.imminentapps.friendfinder.utils.DBUtil;
import com.imminentapps.friendfinder.domain.User;

import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * A login screen that offers login via email/password.
 */
public class LoginScreen extends AppCompatActivity implements LoaderCallbacks<Cursor> {
    private AppDatabase db;

    // Keep track of the login task to ensure we can cancel it if requested.
    private UserLoginTask mAuthTask = null;

    // UI references
    private AutoCompleteTextView mEmailView;
    private EditText mPasswordView;
    private View mProgressView;
    private View mLoginFormView;
    private Button createAccountButton;
    private CheckBox stayLoggedInCheckBox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_screen);
        db = DBUtil.getDBInstance();
//        DBUtil.populateWithTestData();

        // Check to see if the user has saved their login information.
        // If so, we skip the rest of this method.
        checkLoggedInFlag();

        // Set up the login form.
        mEmailView = findViewById(R.id.email);
        mPasswordView = findViewById(R.id.password);
        mPasswordView.setOnEditorActionListener((textView, id, keyEvent) -> {
            if (id == R.id.login || id == EditorInfo.IME_NULL) {
                attemptLogin();
                return true;
            }
            return false;
        });

        Button mEmailSignInButton = findViewById(R.id.email_sign_in_button);
        mEmailSignInButton.setOnClickListener(view -> attemptLogin());

        createAccountButton = findViewById(R.id.create_account_button);
        createAccountButton.setOnClickListener(view -> goToCreateAccountScreen());

        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);
        stayLoggedInCheckBox = findViewById(R.id.stayLoggedInCheckbox);
    }

    /**
     * Check if we have a stored preference with the login information.
     * If we do, skip this screen and go straight to the home screen.
     * TODO: Make this more secure.
     */
    private void checkLoggedInFlag() {
        SharedPreferences preferences = getPreferences(MODE_PRIVATE);
        boolean stayLoggedIn = preferences.getBoolean("stayLoggedIn", false);
        long expiration = preferences.getLong("expiration", 0L);
        long currentTime = System.currentTimeMillis();
        String email = preferences.getString("email", null);

        // Check if the stayLoggedIn flag is set and if so,
        // check if we are within the TTL and if the email address is set properly
        if (stayLoggedIn &&
            currentTime < expiration &&
            email != null)  {

            // Skip the rest of the login screen and go to the home screen
            Intent intent = new Intent(this, HomeScreen.class);
            intent.putExtra("email", preferences.getString("email", null));
            startActivity(intent);
        }
    }

    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptLogin() {
        if (mAuthTask != null) { return; }

        // Reset errors.
        mEmailView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            mEmailView.setError(getString(R.string.error_field_required));
            focusView = mEmailView;
            cancel = true;
        } else if (!isEmailValid(email)) {
            mEmailView.setError(getString(R.string.error_invalid_email));
            focusView = mEmailView;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            showProgress(true);
            mAuthTask = new UserLoginTask(email, password);
            mAuthTask.execute((Void) null);
        }
    }

    private boolean isEmailValid(String email) {
        return email.contains("@");
    }

    private boolean isPasswordValid(String password) {
        return password.length() > 4;
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    private void showProgress(final boolean show) {
        // Use these APIs to fade-in the progress spinner.
        int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

        mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            }
        });

        mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
        mProgressView.animate().setDuration(shortAnimTime).alpha(
                show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            }
        });
    }

    /**
     * Method that transitions to the HomeScreenActivity.
     * Passes the text from mEmailView along.
     */
    private void goToHomeScreen() {
        SharedPreferences preferences = getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();

        // Set preferences if the stayLoggedInCheckBox is checked.
        if (stayLoggedInCheckBox.isChecked()) {
            // Set the stayLoggedIn flag
            editor.putBoolean("stayLoggedIn", true);

            // Set the TTL
            Date expiration = new Date(System.currentTimeMillis() + TimeUnit.DAYS.toMillis(1));
            editor.putLong("expiration", expiration.getTime());

            // Set the user email to keep logged in
            editor.putString("email", mEmailView.getText().toString());
        } else {
            // Clear things out just to be safe
            editor.putBoolean("stayLoggedIn", false);
            editor.putLong("expiration", 0L);
            editor.putString("email", null);
        }
        editor.apply();

        Intent intent = new Intent(this, HomeScreen.class);
        intent.putExtra("email", mEmailView.getText());
        startActivity(intent);
    }

    /**
     * Method that transitions to the CreateAccountScreen Activity.
     */
    private void goToCreateAccountScreen() {
        Intent intent = new Intent(this, CreateAccountScreen.class);
        startActivity(intent);
    }

    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */
    public class UserLoginTask extends AsyncTask<Void, Void, Boolean> {

        private final String mEmail;
        private final String mPassword;

        UserLoginTask(String email, String password) {
            mEmail = email;
            mPassword = password;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.

            try {
                // Simulate network access.
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                return false;
            }

            // Check to see if user is registered to the mock database and password matches
            User user = db.userDao().findByEmail(mEmail);
            return user != null && mPassword.equals(user.getPassword());
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            mAuthTask = null;
            showProgress(false);

            if (success) {
                goToHomeScreen();
            } else {
                mPasswordView.setError(getString(R.string.error_incorrect_password));
                mPasswordView.requestFocus();
            }
        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
            showProgress(false);
        }
    }


    //******** Required LoaderCallbacks methods *********//

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        return null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

}

