package com.xtel.sdk.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.xtel.ivipbusiness.R;
import com.xtel.ivipbusiness.model.entity.RESP_Image;
import com.xtel.nipservicesdk.model.entity.Error;
import com.xtel.nipservicesdk.utils.JsonHelper;
import com.xtel.nipservicesdk.utils.JsonParse;
import com.xtel.sdk.callback.CallbackImageListener;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;

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

    public void postImage(Context context, Bitmap bitmap, boolean isBigImage, CallbackImageListener callbackImageListener) {
        new ConvertImage(context, isBigImage, callbackImageListener).execute(bitmap);
    }

    public class ConvertImage extends AsyncTask<Bitmap, Void, File> {
        private Context context;
        private boolean isBigImage;
        private CallbackImageListener callbackImageListener;

        public ConvertImage(Context context, boolean isBigImage, CallbackImageListener callbackImageListener) {
            this.context = context;
            this.isBigImage = isBigImage;
            this.callbackImageListener = callbackImageListener;
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
                postImageToServer(file, context, callbackImageListener);
            } else {
//                dialogProgressBar.hideProgressBar();
                callbackImageListener.onError();
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

    private void postImageToServer(final File file, final Context context, final CallbackImageListener callbackImageListener) {
        String SERVER_API = "http://124.158.5.112:9190/upload/files";
        Log.e("tb_up_upload", "dang up: " + file.getPath() + "       " + SERVER_API);

        Ion.with(context)
                .load(SERVER_API)
//                .setMultipartParameter("goop", "noop")
//                .setMultipartFile("archive", "application/zip", file)
                .setMultipartFile("image", file)
                .asString()
                .setCallback(new FutureCallback<String>() {
                    @Override
                    public void onCompleted(Exception e, String result) {
                        if (e != null) {
                            Log.e("tb_up_error", e.toString());
                            Toast.makeText(context, context.getString(R.string.error_server_request), Toast.LENGTH_SHORT).show();
                            callbackImageListener.onError();
                        } else {
                            Log.e("tb_up_result", result);
                            Error error = JsonHelper.getObjectNoException(result, Error.class);

                            if (error != null) {
                                Toast.makeText(context, JsonParse.getCodeMessage(error.getCode(), context.getString(R.string.have_error)), Toast.LENGTH_SHORT).show();
                                callbackImageListener.onError();
                            } else {
//                                result = result.replace("https", "http").replace("9191", "9190");

                                try {
                                    JSONArray jsonArray = new JSONArray(result);
                                    JSONObject jsonObject = jsonArray.getJSONObject(0);
                                    RESP_Image resp_image = JsonHelper.getObjectNoException(jsonObject.toString(), RESP_Image.class);

                                    Log.e("upload_object", JsonHelper.toJson(resp_image));
                                    callbackImageListener.onSuccess(resp_image);
                                } catch (Exception ex) {
                                    ex.printStackTrace();
                                    callbackImageListener.onError();
                                }
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