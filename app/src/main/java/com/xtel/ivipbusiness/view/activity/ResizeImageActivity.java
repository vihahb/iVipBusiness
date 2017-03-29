package com.xtel.ivipbusiness.view.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImageView;
import com.xtel.ivipbusiness.R;
import com.xtel.ivipbusiness.model.entity.RESP_Image;
import com.xtel.nipservicesdk.model.entity.Error;
import com.xtel.nipservicesdk.utils.JsonHelper;
import com.xtel.nipservicesdk.utils.JsonParse;
import com.xtel.sdk.callback.CallbackImageListener;
import com.xtel.sdk.commons.Constants;
import com.xtel.sdk.commons.NetWorkInfo;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;

public class ResizeImageActivity extends BasicActivity {
    protected String SERVER_API = "http://124.158.5.112:9190/upload/files";
    protected CropImageView cropImageView;
    protected int type = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resize_image);

        initToolbar(R.id.resize_image_toolbar, null);
        getData();
    }

    /*
    * Kiểm tra data truyền vào
    * */
    protected void getData() {
        Uri uri = null;
        Bitmap bitmap = null;

        try {
            uri = getIntent().getParcelableExtra(Constants.URI);
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            bitmap = getIntent().getParcelableExtra(Constants.BITMAP);
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            type = getIntent().getIntExtra(Constants.TYPE, -1);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if ((!(uri == null && bitmap == null) && type != -1)) {
            initView(uri, bitmap, type);
        } else {
            finish();
        }
    }

    /*
    * Khởi tạo các view
    * Kiểm tra data truyền sang để cấu hình view cho phù hợp
    * */
    protected void initView(Uri uri, Bitmap bitmap, int type) {
        cropImageView = (CropImageView) findViewById(R.id.resize_image_img_cropImageView);

        if (uri != null)
            cropImageView.setImageUriAsync(uri);
        else if (bitmap != null)
            cropImageView.setImageBitmap(bitmap);

        if (type == 0)
            cropImageView.setAspectRatio(16, 9);
        else if (type == 1) {
            cropImageView.setAspectRatio(1, 1);
            cropImageView.setCropShape(CropImageView.CropShape.OVAL);
        }

        cropImageView.setOnCropImageCompleteListener(new CropImageView.OnCropImageCompleteListener() {
            @Override
            public void onCropImageComplete(CropImageView view, CropImageView.CropResult result) {
                cropImageView.setImageBitmap(result.getBitmap());
//                cropImageView.setImageBitmap(cropImageView.getCroppedImage());
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_resize, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case android.R.id.home:
                finish();
                break;
            case R.id.action_resize_image_rotate:
                cropImageView.rotateImage(90);
                break;
            case R.id.action_resize_image_done:
                if (!NetWorkInfo.isOnline(getApplicationContext())) {
                    showShortToast(getString(R.string.error_no_internet));
                    break;
                }
                new TakePicture().execute(cropImageView.getCroppedImage());
                break;
            default:
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    /*
    * Resize image và up lên server
    * */
    protected class TakePicture extends AsyncTask<Bitmap, Integer, File> {

        /*
        * Hiển thị progress thông báo đang up ảnh
        * */
        @Override
        protected void onPreExecute() {
//            super.onPreExecute();
            showProgressBar(false, false, null, getString(R.string.uploading_file));
        }

        /*
        * Bắt đầu resize ảnh và up lên server
        * */
        @SuppressWarnings("WrongThread")
        @Override
        protected File doInBackground(Bitmap... params) {
            Bitmap bitmap = cropImageView.getCroppedImage();

            if (type == 0) {
                bitmap = getBigBitmap(bitmap);
            } else if (type == 1) {
                bitmap = getSmallBitmap(bitmap);
            } else if (type == 2) {
                bitmap = getLogoBitmap(bitmap);
            }

            if (bitmap == null)
                return null;

            return saveImageFile(bitmap);
        }

        /*
        * Kiểm tra up ảnh lên server thành công hay không
        * Nếu thành công thì truyền lại data và kết thúc activity
        * */
        @Override
        protected void onPostExecute(File file) {
            super.onPostExecute(file);

            if (file != null) {
                if (!NetWorkInfo.isOnline(getApplicationContext())) {
                    closeProgressBar();
                    showShortToast(getString(R.string.error_no_internet));
                    return;
                }

                postImageToServer(file, ResizeImageActivity.this, new CallbackImageListener() {
                    @Override
                    public void onSuccess(final RESP_Image resp_image, final File file) {
                        closeProgressBar();

                        Intent intent = new Intent();
                        intent.putExtra(Constants.SERVER_PATH, resp_image.getServer_path());
                        intent.putExtra(Constants.FILE, file.getAbsolutePath());
                        intent.putExtra(Constants.URI, resp_image.getUri());
                        intent.putExtra(Constants.TYPE, type);

                        setResult(RESULT_OK, intent);
                        finish();
                    }

                    @Override
                    public void onError() {
                        closeProgressBar();
                        showShortToast(getString(R.string.error_try_again));
                        finish();
                    }
                });
            } else {
                closeProgressBar();
                showShortToast(getString(R.string.error_resize_image));
            }
        }

//        Bitmap getBitmap() {
//            if (type == 0) {
//                return getBigBitmap();
//            } else if (type == 1) {
//                return getSmallBitmap();
//            } else
//                return null;
//        }

        /*
        * Resize ảnh nhỏ
        * */
        Bitmap getSmallBitmap(Bitmap bitmap) {
            try {
                double width = bitmap.getWidth(), height = bitmap.getHeight();

                if (width > 300 || height > 300) {
                    while (width > 300 || height > 300) {
                        width = width * 0.9;
                        height = height * 0.9;
                    }

                    return Bitmap.createScaledBitmap(bitmap, (int) width, (int) height, false);
                }

                return cropImageView.getCroppedImage();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        /*
        * Resize ảnh nhỏ
        * */
        Bitmap getLogoBitmap(Bitmap bitmap) {
            try {
                double width = bitmap.getWidth(), height = bitmap.getHeight();

                if (width > 120 || height > 120) {
                    while (width > 120 || height > 120) {
                        width = width * 0.9;
                        height = height * 0.9;
                    }

                    return Bitmap.createScaledBitmap(bitmap, (int) width, (int) height, false);
                }

                return cropImageView.getCroppedImage();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        /*
        * Resize ảnh lớn
        * */
        Bitmap getBigBitmap(Bitmap bitmap) {
            try {
                double width = bitmap.getWidth(), height = bitmap.getHeight();

                if (width > 1000 || height > 1000) {
                    while (width > 1000 || height > 1000) {
                        width = width * 0.9;
                        height = height * 0.9;
                    }

                    return Bitmap.createScaledBitmap(bitmap, (int) width, (int) height, false);
                }

                return cropImageView.getCroppedImage();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        /*
        * Lưu ảnh thành file để up lên server
        * */
        @SuppressWarnings("ResultOfMethodCallIgnored")
        File saveImageFile(Bitmap bitmap) {
            if (bitmap == null)
                return null;
            try {
                String file_path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/iVipBusiness";
                File dir = new File(file_path);

                if (!dir.exists())
                    dir.mkdirs();

                File file = new File(dir, System.currentTimeMillis() + ".png");
                FileOutputStream fOut = new FileOutputStream(file);
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, fOut);

                fOut.flush();
                fOut.close();
                return file;
            } catch (Exception e) {
                e.printStackTrace();

            }
            return null;
        }
    }

    /*
    * Up ảnh lên server
    * */
    private void postImageToServer(final File file, final Context context, final CallbackImageListener callbackImageListener) {
        Ion.with(context)
                .load(SERVER_API)
                .setTimeout(60000)
                .setMultipartFile("image", file)
                .asString()
                .setCallback(new FutureCallback<String>() {
                    @Override
                    public void onCompleted(Exception e, String result) {
                        if (e != null) {
                            Toast.makeText(context, context.getString(R.string.error_server_request), Toast.LENGTH_SHORT).show();
                            callbackImageListener.onError();
                        } else {
                            Error error = JsonHelper.getObjectNoException(result, Error.class);

                            if (error != null) {
                                Toast.makeText(context, JsonParse.getCodeMessage(error.getCode(), context.getString(R.string.have_error)), Toast.LENGTH_SHORT).show();
                                callbackImageListener.onError();
                            } else {
                                try {
                                    JSONArray jsonArray = new JSONArray(result);
                                    JSONObject jsonObject = jsonArray.getJSONObject(0);
                                    RESP_Image resp_image = JsonHelper.getObjectNoException(jsonObject.toString(), RESP_Image.class);

                                    callbackImageListener.onSuccess(resp_image, file);
                                } catch (Exception ex) {
                                    ex.printStackTrace();
                                    callbackImageListener.onError();
                                }
                            }
                        }
                    }
                });
    }
}