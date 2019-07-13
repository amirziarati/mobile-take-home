package com.gustlogix.rickandmorty.repo.remote.network;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONException;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class NetworkServiceImpl<T> implements NetworkService<T> {

    private JsonDeserializer<T> jsonDeserializer;
    private static String TAG = "";

    public NetworkServiceImpl(JsonDeserializer<T> jsonDeserializer) {
        this.jsonDeserializer = jsonDeserializer;
        TAG = this.getClass().getCanonicalName();
    }

    @Override
    public void call(NetworkRequest networkRequest, NetworkServiceCallback<T> networkServiceCallback) {
        InnerTask task = new InnerTask(jsonDeserializer);
        task.setCallback(networkServiceCallback);
        task.execute(networkRequest);
    }

    private class InnerTask extends AsyncTask<NetworkRequest, Void, NetworkResponse<T>> {

        private NetworkServiceCallback<T> networkServiceCallback;
        private JsonDeserializer serializer;

        public InnerTask(JsonDeserializer serializer) {
            this.serializer = serializer;
        }

        public void setCallback(NetworkServiceCallback<T> callback) {
            this.networkServiceCallback = callback;
        }

        @Override
        protected NetworkResponse<T> doInBackground(NetworkRequest... networkRequests) {
            NetworkRequest request = networkRequests[0];
            InputStream is = null;
            BufferedReader in = null;
            int statusCode = 0;
            try {
                long startTimeNetworkRequest = System.currentTimeMillis();
                Log.i(TAG, "Start to call -> " + request.toString());
                HttpsURLConnection urlConnection = (HttpsURLConnection) new URL(request.toString()).openConnection();
                urlConnection.setReadTimeout(7 * 1000);
                urlConnection.setConnectTimeout(7 * 1000);
                urlConnection.setRequestMethod(request.getMethod());
                urlConnection.setRequestProperty("Content-Type", "application/json");
                urlConnection.setDoInput(true);

                if (request.getMethod().equals(NetworkService.POST)) {
                    urlConnection.setDoOutput(true);
                    OutputStream os = urlConnection.getOutputStream();
                    BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
                    writer.write(request.getBody());
                    writer.flush();
                    writer.close();
                }

                is = urlConnection.getInputStream();
                in = new BufferedReader(new InputStreamReader(is));
                String inputLine;
                StringBuilder sb = new StringBuilder();
                while ((inputLine = in.readLine()) != null) {
                    sb.append(inputLine);
                }
                statusCode = urlConnection.getResponseCode();
                long endNetworkRequestTime = System.currentTimeMillis();
                Log.i(TAG, "Network request time in millis: " + (endNetworkRequestTime - startTimeNetworkRequest));
                long startDeserializeTime = System.currentTimeMillis();
                T data = (T) serializer.deserialize(sb.toString());
                long endDeserializationTime = System.currentTimeMillis();
                Log.i(TAG, "Deserialization time in millis: " + (endDeserializationTime - startDeserializeTime));
                NetworkResponse<T> response = new NetworkResponse<>(statusCode, data);
                return response;
            } catch (IOException e) {
                e.printStackTrace();
                return new NetworkResponse<T>(e);
            } catch (JSONException e) {
                e.printStackTrace();
                return new NetworkResponse<T>(statusCode, e);
            } finally {
                Log.i(TAG, "Network call response -> code: " + statusCode);
                try {
                    if (in != null)
                        in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                try {
                    if (is != null)
                        is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        @Override
        protected void onPostExecute(NetworkResponse<T> response) {
            if (networkServiceCallback != null) {
                if (response != null) {
                    if (response.getError() == null) {
                        if (response.getCode() >= 200 && response.getCode() < 300)
                            networkServiceCallback.onResponse(response.getResponse());
                        else if (response.getCode() != -1)
                            networkServiceCallback.onError(new HttpException(response.getCode()));
                        else
                            networkServiceCallback.onError(new IllegalStateException());
                    } else
                        networkServiceCallback.onError(response.getError());
                } else {
                    networkServiceCallback.onError(new IllegalStateException());
                }
            }
        }
    }

    class NetworkResponse<T> {

        private int code = -1;
        private Exception error;
        private T response;

        public NetworkResponse(Exception error) {
            this.error = error;
        }

        public NetworkResponse(int code, T response) {
            this.code = code;
            this.response = response;
        }

        public NetworkResponse(int code, Exception error) {
            this.code = code;
            this.error = error;
        }

        public NetworkResponse(int code, Exception error, T response) {
            this.code = code;
            this.error = error;
            this.response = response;
        }

        public int getCode() {
            return code;
        }

        public void setCode(int code) {
            this.code = code;
        }

        public Exception getError() {
            return error;
        }

        public void setError(Exception error) {
            this.error = error;
        }

        public T getResponse() {
            return response;
        }

        public void setResponse(T response) {
            this.response = response;
        }
    }
}