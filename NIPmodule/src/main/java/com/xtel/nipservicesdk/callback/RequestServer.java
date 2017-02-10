package com.xtel.nipservicesdk.callback;

import android.os.AsyncTask;

import com.xtel.nipservicesdk.NipApplication;
import com.xtel.nipservicesdk.R;
import com.xtel.nipservicesdk.commons.Cts;
import com.xtel.nipservicesdk.model.entity.Error;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by Lê Công Long Vũ on 12/2/2016
 */

public class RequestServer {

    public RequestServer() {
    }

    public void postApi(String url, String jsonObject, String session, ResponseHandle responseHandle) {
        new PostToServer(responseHandle).execute(url, jsonObject, session);
    }

    public void getApi(String url, String session, ResponseHandle responseHandle) {
        new GetToServer(responseHandle).execute(url, session);
    }

    public void putApi(String url, String jsonObject, String session, ResponseHandle responseHandle) {
        new PutToServer(responseHandle).execute(url, jsonObject, session);
    }

    public void deleteApi(String url, String delete, String session, ResponseHandle responseHandle) {
        new DeleteToServer(responseHandle).execute(url, delete, session);
    }

    private class PostToServer extends AsyncTask<String, Integer, String> {
        private final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        private ResponseHandle responseHandle;
        private boolean isSuccess = true;

        PostToServer(ResponseHandle responseHandle) {
            this.responseHandle = responseHandle;
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                OkHttpClient client = new OkHttpClient();

                Request.Builder builder = new Request.Builder();
                builder.url(params[0]);

                if (params[1] != null) {
                    RequestBody body = RequestBody.create(JSON, params[1]);
                    builder.post(body);
                }

                if (params[2] != null)
                    builder.header(Cts.SESSION, params[2]);

                Request request = builder.build();

                Response response = client.newCall(request).execute();
                return response.body().string();
            } catch (IOException e) {
                e.printStackTrace();
                isSuccess = false;
                return null;
            }
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (isSuccess)
                responseHandle.onSuccess(s);
            else
                responseHandle.onError(new Error(-1, NipApplication.context.getString(R.string.error), NipApplication.context.getString(R.string.can_not_request)));
        }
    }

    private class GetToServer extends AsyncTask<String, Integer, String> {
        private ResponseHandle responseHandle;
        private boolean isSuccess = true;

        GetToServer(ResponseHandle responseHandle) {
            this.responseHandle = responseHandle;
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                OkHttpClient client = new OkHttpClient();

                Request.Builder builder = new Request.Builder();
                builder.url(params[0]);

                if (params[1] != null)
                    builder.header(Cts.SESSION, params[1]);

                Request request = builder.build();

                Response response = client.newCall(request).execute();
                return response.body().string();
            } catch (IOException e) {
                return null;
            }
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (isSuccess)
                responseHandle.onSuccess(s);
            else
                responseHandle.onError(new Error(-1, NipApplication.context.getString(R.string.error), NipApplication.context.getString(R.string.can_not_request)));
        }
    }

    private class PutToServer extends AsyncTask<String, Integer, String> {
        private final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        private ResponseHandle responseHandle;
        private boolean isSuccess = true;

        PutToServer(ResponseHandle responseHandle) {
            this.responseHandle = responseHandle;
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                OkHttpClient client = new OkHttpClient();

                Request.Builder builder = new Request.Builder();
                builder.url(params[0]);

                if (params[1] != null) {
                    RequestBody body = RequestBody.create(JSON, params[1]);
                    builder.put(body);
                }

                if (params[2] != null)
                    builder.header(Cts.SESSION, params[2]);

                Request request = builder.build();

                Response response = client.newCall(request).execute();
                return response.body().string();
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (isSuccess)
                responseHandle.onSuccess(s);
            else
                responseHandle.onError(new Error(-1, NipApplication.context.getString(R.string.error), NipApplication.context.getString(R.string.can_not_request)));
        }
    }

    private class DeleteToServer extends AsyncTask<String, Integer, String> {
        private final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        private ResponseHandle responseHandle;
        private boolean isSuccess = true;

        DeleteToServer(ResponseHandle responseHandle) {
            this.responseHandle = responseHandle;
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                OkHttpClient client = new OkHttpClient();

                Request.Builder builder = new Request.Builder();
                builder.url(params[0]);

                if (params[1] != null) {
                    RequestBody body = RequestBody.create(JSON, params[1]);
                    builder.delete(body);
                }

                if (params[2] != null)
                    builder.header(Cts.SESSION, params[2]);

                Request request = builder.build();

                Response response = client.newCall(request).execute();
                return response.body().string();
            } catch (IOException e) {
                return null;
            }
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (isSuccess)
                responseHandle.onSuccess(s);
            else
                responseHandle.onError(new Error(-1, NipApplication.context.getString(R.string.error), NipApplication.context.getString(R.string.can_not_request)));
        }
    }
}
