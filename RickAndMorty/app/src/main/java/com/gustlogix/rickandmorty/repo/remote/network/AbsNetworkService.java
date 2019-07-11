package com.gustlogix.rickandmorty.repo.remote.network;

import android.os.AsyncTask;

import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public abstract class AbsNetworkService<T> implements NetworkService<T> {

    private JsonDeserializer jsonDeserializer;
    private Class<T> responseType;

    public AbsNetworkService(JsonDeserializer jsonDeserializer, Class<T> responseType) {
        this.jsonDeserializer = jsonDeserializer;
        this.responseType = responseType;
    }

    @Override
    public void call(NetworkRequest networkRequest, NetworkServiceCallback<T> networkServiceCallback) {
        InnerTask task = new InnerTask(jsonDeserializer);
        task.setCallback(networkServiceCallback);
        task.setResponseClassType(responseType);
        task.execute(networkRequest);
    }

    private class InnerTask extends AsyncTask<NetworkRequest, Void, T> {

        private NetworkServiceCallback<T> callback;
        private JsonDeserializer serializer;
        private Class<T> responseClassType;

        public InnerTask(JsonDeserializer serializer) {
            this.serializer = serializer;
        }

        public void setCallback(NetworkServiceCallback<T> callback) {
            this.callback = callback;
        }

        public void setResponseClassType(Class<T> responseClassType) {
            this.responseClassType = responseClassType;
        }

        @Override
        protected T doInBackground(NetworkRequest... networkRequests) {
            NetworkRequest request = networkRequests[0];
            InputStream is = null;
            BufferedReader in = null;
            try {
                HttpsURLConnection urlConnection = (HttpsURLConnection) new URL(request.toString()).openConnection();
                urlConnection.setReadTimeout(7 * 1000);
                urlConnection.setConnectTimeout(7 * 1000);
                urlConnection.setRequestMethod("GET");
                urlConnection.setRequestProperty("Content-Type", "application/json");
                urlConnection.setDoInput(true);
//                urlConnection.setDoOutput(true);
//                OutputStream os = urlConnection.getOutputStream();
//                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
//                writer.flush();
//                writer.close();

                is = urlConnection.getInputStream();
                in = new BufferedReader(new InputStreamReader(is));
                String inputLine;
                StringBuilder sb = new StringBuilder();
                while ((inputLine = in.readLine()) != null) {
                    sb.append(inputLine);
                }
                return (T) serializer.deserialize(sb.toString());
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            } catch (JSONException e) {
                e.printStackTrace();
                return null;
            } finally {
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
        protected void onPostExecute(T result) {
            if (callback != null) {
                if (result == null) {
                    callback.onError(new IOException("Network error!"));
                } else {
                    callback.onResponse(result);
                }
            }
        }
    }
}