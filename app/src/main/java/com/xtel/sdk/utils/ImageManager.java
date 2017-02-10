package com.xtel.sdk.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.async.util.StreamUtility;
import com.koushikdutta.ion.Ion;
import com.xtel.ivipbusiness.R;
import com.xtel.ivipbusiness.model.entity.RESP_Image;
import com.xtel.nipservicesdk.callback.RequestServer;
import com.xtel.nipservicesdk.callback.ResponseHandle;
import com.xtel.nipservicesdk.model.entity.Error;
import com.xtel.nipservicesdk.model.entity.RESP_Basic;
import com.xtel.nipservicesdk.utils.JsonHelper;
import com.xtel.nipservicesdk.utils.JsonParse;
import com.xtel.sdk.callback.RequestWithStringListener;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

/**
 * Created by Lê Công Long Vũ on 12/1/2016
 */

public class ImageManager {
    private static ImageManager instance;

    public static ImageManager getInstance() {
        if (instance == null)
            instance = new ImageManager();
        return instance;
    }

    public void postImage(Context context, Bitmap bitmap, boolean isBigImage, RequestWithStringListener requestWithStringListener) {
        new ConvertImage(context, isBigImage, requestWithStringListener).execute(bitmap);
    }

    public class ConvertImage extends AsyncTask<Bitmap, Void, File> {
        private Context context;
        private boolean isBigImage;
        private RequestWithStringListener requestWithStringListener;

        public ConvertImage(Context context, boolean isBigImage, RequestWithStringListener requestWithStringListener) {
            this.context = context;
            this.isBigImage = isBigImage;
            this.requestWithStringListener = requestWithStringListener;
        }

        @Override
        public File doInBackground(Bitmap... params) {
            try {
                Log.e("tb_uri", "null k " + params[0].getWidth());

                try {
                    Bitmap newBitmap;

                    if (isBigImage)
                        newBitmap = getBitmapBigSize(params[0]);
                    else
                        newBitmap = getBitmapSmallSize(params[0]);

                    if (newBitmap != null) {
                        return saveImageFile(newBitmap);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } catch (Exception e) {
                e.printStackTrace();
                Log.e("tb_image_error", e.toString());
            }
            return null;
        }

        @Override
        public void onPostExecute(File file) {
            super.onPostExecute(file);

            if (file != null) {
                postImageToServer(file, context, requestWithStringListener);
            } else {
//                dialogProgressBar.hideProgressBar();
                requestWithStringListener.onError();
            }
        }

        private Bitmap getBitmapSmallSize(Bitmap bitmap) {
            try {
                double width = bitmap.getWidth(), height = bitmap.getHeight();
                Log.e("tb_bitmap_old", width + "        " + height);

                if (width > 300 || height > 300) {
                    int new_width, new_height;
                    while (width > 300 || height > 300) {
                        width = width * 0.8;
                        height = height * 0.8;
                        Log.e("tb_bitmap_run", width + "       " + height);
                    }
                    new_width = (int) width;
                    new_height = (int) height;

                    Log.e("tb_bitmap_new", new_width + "         " + new_height);
                    return Bitmap.createScaledBitmap(bitmap, new_width, new_height, false);
                }

                return bitmap;
            } catch (Exception e) {
                Log.e("tb_error_image", e.toString());
                e.printStackTrace();
            }
            return null;
        }

        private Bitmap getBitmapBigSize(Bitmap bitmap) {
            try {
                double width = bitmap.getWidth(), height = bitmap.getHeight();
                Log.e("tb_bitmap_old", width + "        " + height);

                if (width > 1280 || height > 1280) {
                    int new_width, new_height;
                    while (width > 1280 || height > 1280) {
                        width = width * 0.8;
                        height = height * 0.8;
                        Log.e("tb_bitmap_run", width + "       " + height);
                    }
                    new_width = (int) width;
                    new_height = (int) height;

                    Log.e("tb_bitmap_new", new_width + "         " + new_height);
                    return Bitmap.createScaledBitmap(bitmap, new_width, new_height, false);
                }

                return bitmap;
            } catch (Exception e) {
                Log.e("tb_error_image", e.toString());
                e.printStackTrace();
            }
            return null;
        }

        @SuppressWarnings("ResultOfMethodCallIgnored")
        private File saveImageFile(Bitmap bitmap) {
            try {
                String file_path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/vParking";
                File dir = new File(file_path);

                if (!dir.exists())
                    dir.mkdirs();

                File file = new File(dir, System.currentTimeMillis() + ".png");
                FileOutputStream fOut = new FileOutputStream(file);

                if (bitmap != null) {
                    bitmap.compress(Bitmap.CompressFormat.PNG, 85, fOut);
                }

                fOut.flush();
                fOut.close();

                return file;
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    private void postImageToServer(final File file, final Context context, final RequestWithStringListener requestWithStringListener) {
//        https://124.158.5.112:9191/upload/files

        String SERVER_API = "https://124.158.5.112:9191/upload/files";
        Log.e("tb_up_upload", "dang up: " + file.getPath() + "       " + SERVER_API);

//        TrustManager[] trustManager = new TrustManager[] {new X509TrustManager() {
//            @Override
//            public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
//
//            }
//
//            @Override
//            public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
//
//            }
//
//            @Override
//            public X509Certificate[] getAcceptedIssuers() {
//                return new X509Certificate[0];
//            }
//        }};
//
//        Ion.getDefault(context).getHttpClient().getSSLSocketMiddleware().setTrustManagers(trustManager);
//
//        SSLContext sslContext = null;
//
//        try {
//            sslContext = SSLContext.getInstance("TLS");
//            sslContext.init(null, trustManager, null);
//        } catch (Exception e1) {
//            e1.printStackTrace();
//            Log.e("trust", e1.toString());
//            Toast.makeText(context, context.getString(R.string.have_error), Toast.LENGTH_SHORT).show();
//            return;
//        }
//
//        Ion.getDefault(context).getHttpClient().getSSLSocketMiddleware().setSSLContext(sslContext);

        URLConnection conn = null;
        try {
            conn = new URL("https://saren.wtako.net/Teikoku.Shounen.jpg").openConnection();
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (conn == null)
            return;

        Ion.with(context)
                .load(String.valueOf(conn))
//                .setMultipartParameter("goop", "noop")
//                .setMultipartFile("archive", "application/zip", file)
                .setMultipartFile("", file)
                .asString()
                .setCallback(new FutureCallback<String>() {
                    @Override
                    public void onCompleted(Exception e, String result) {
                        if (e != null) {
                            Log.e("tb_up_error", e.toString());
                            Toast.makeText(context, context.getString(R.string.error_server_request), Toast.LENGTH_SHORT).show();
                            requestWithStringListener.onError();
                        } else {
                            Log.e("tb_up_result", result);
                            Error error = JsonHelper.getObjectNoException(result, Error.class);

                            if (error != null) {
                                Toast.makeText(context, JsonParse.getCodeMessage(error.getCode(), context.getString(R.string.have_error)), Toast.LENGTH_SHORT).show();
                                requestWithStringListener.onError();
                            } else {
                                RESP_Image RESPImage = JsonHelper.getObjectNoException(result, RESP_Image.class);
                                requestWithStringListener.onSuccess(RESPImage.getUri());
                            }

                            try {
                                boolean delete = file.delete();
                            } catch (Exception ex) {
                                ex.printStackTrace();
                            }
                        }
                    }
                });
    }
}