package com.example.root.websocketsapp;

import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft_17;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;
import java.net.URISyntaxException;

public class MainActivity extends AppCompatActivity {

    WebSocketClient mWebSocketClient;
    Button btnSend;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnSend = (Button) findViewById(R.id.btnSend);

        conectWebSocket();
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMessage(v);
            }
        });
    }

    private void conectWebSocket() {
        URI uri;
        try {
            uri = new URI("YOUR_SERVER_ADDRESS");
        } catch (URISyntaxException e) {
            e.printStackTrace();
            return;
        }

        mWebSocketClient = new WebSocketClient(uri, new Draft_17()) {
            @Override
            public void onOpen(ServerHandshake handshakedata) {
                Log.i("Zyngaboy", "Opened");
                mWebSocketClient.send("Hello from "+ Build.MANUFACTURER + " " + Build.MODEL);
            }

            @Override
            public void onMessage(String message) {
                final String msg = message;
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        TextView txtRcv = (TextView) findViewById(R.id.txtRcv);
                        txtRcv.setText(msg);
                    }
                });
            }

            @Override
            public void onClose(int code, String reason, boolean remote) {
                Log.i("Zyngaboy", "Closed " + reason);
            }

            @Override
            public void onError(Exception ex) {
                Log.i("Zyngaboy", "Error " + ex.getMessage());
            }
        };
        mWebSocketClient.connect();
    }

    public void sendMessage(View view) {
        EditText etSend = (EditText)findViewById(R.id.etSend);
        mWebSocketClient.send(etSend.getText().toString());
        etSend.setText("");
    }
}
