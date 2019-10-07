package jbloomfield.macroapp;

import android.accounts.AuthenticatorException;
import android.accounts.OperationCanceledException;
import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.dropbox.core.v2.DbxClientV2;
import com.microsoft.graph.authentication.IAuthenticationProvider;
import com.microsoft.graph.core.DefaultClientConfig;
import com.microsoft.graph.core.IClientConfig;
import com.microsoft.graph.extensions.GraphServiceClient;
import com.microsoft.graph.extensions.IGraphServiceClient;
import com.microsoft.graph.extensions.Shared;
import com.microsoft.graph.http.IHttpRequest;
import com.microsoft.identity.client.AuthenticationCallback;
import com.microsoft.identity.client.AuthenticationResult;
import com.microsoft.identity.client.MsalException;
import com.microsoft.identity.client.PublicClientApplication;
import com.microsoft.identity.client.User;

import java.io.IOException;

public class AuthManager {
    // request these permissions from graph api
    public static final String[] SCOPES = {"openid", "Files.ReadWrite", "User.ReadBasic.All", "offline_access"};
    // connects to graph api
    PublicClientApplication msClient;
    // stores auth results such as login token
    AuthenticationResult authResult;
    // authenticates calls to api
    IAuthenticationProvider authProvider;
    // builds requests to ms api
    IGraphServiceClient client;

    // manages dropbox authentication and client object
    DbxClientV2 dbClient;


    public AuthManager(Application app) {
        msClient = new PublicClientApplication(app);
    }
    // get login token from microsoft
    public void getToken(final Activity activity) {
        msClient.acquireToken(activity, SCOPES, new AuthenticationCallback() {
            @Override
            public void onSuccess(AuthenticationResult authenticationResult) {
                // on successful login, update ui to logged in
                authResult = authenticationResult;
                activity.findViewById(R.id.btn_dropbox).setVisibility(View.INVISIBLE);
                activity.findViewById(R.id.btn_onedrive).setVisibility(View.INVISIBLE);
                activity.findViewById(R.id.btn_logout).setVisibility(View.VISIBLE);
                TextView txt_log = activity.findViewById(R.id.textView);
                txt_log.setText("Signed in to OneDrive\n" + authResult.getUser().getDisplayableId());
                // store login token for silent logins
                SharedPreferences prefs = activity.getSharedPreferences("user-auth", Context.MODE_PRIVATE);
                prefs.edit().putString("onedrive",authResult.getAccessToken()).apply();
                graphConnect(authResult.getAccessToken());
            }

            @Override
            public void onError(MsalException exception) {
                Log.e("authmgr", "Failed to login");
            }

            @Override
            public void onCancel() {
                Log.e("authmgr", "login cancelled");
            }
        });
    }

    public void getTokenSilent(final Activity activity, User user) {
        msClient.acquireTokenSilentAsync(SCOPES, user, null, true, new AuthenticationCallback() {
            @Override
            // attempt to login from stored token with no user interaction
            public void onSuccess(AuthenticationResult authenticationResult) {
                authResult = authenticationResult;
                // update ui to reflect login
                activity.findViewById(R.id.btn_dropbox).setVisibility(View.INVISIBLE);
                activity.findViewById(R.id.btn_onedrive).setVisibility(View.INVISIBLE);
                activity.findViewById(R.id.btn_logout).setVisibility(View.VISIBLE);
                TextView txt_log = activity.findViewById(R.id.textView);
                txt_log.setText("Signed in to OneDrive\n" + authResult.getUser().getDisplayableId());
                SharedPreferences prefs = activity.getSharedPreferences("user-auth", Context.MODE_PRIVATE);
                prefs.edit().putString("onedrive",authResult.getAccessToken()).apply();
                graphConnect(authResult.getAccessToken());

            }

            @Override
            public void onError(MsalException exception) {

            }

            @Override
            public void onCancel() {

            }
        });
    }

    // request a graph client to make api calls
    public void graphConnect(final String accessToken) {
        authProvider = new IAuthenticationProvider() {
            @Override
            public void authenticateRequest(IHttpRequest request) {
                try {
                    request.addHeader("Authorization", "Bearer "
                            + accessToken);
                } catch (NullPointerException e) {
                    e.printStackTrace();
                }
            }
        };
        IClientConfig config = DefaultClientConfig.createWithAuthenticationProvider(authProvider);
        client = new GraphServiceClient.Builder().fromConfig(config).buildClient();
    }
}
