package com.xtel.ivipbusiness.model;

import com.xtel.ivipbusiness.R;
import com.xtel.ivipbusiness.view.MyApplication;
import com.xtel.nipservicesdk.callback.ResponseHandle;
import com.xtel.nipservicesdk.model.BasicModel;

/**
 * Created by Lê Công Long Vũ on 12/4/2016.
 */

public class ParkingModel extends BasicModel {
    private static ParkingModel instanse = new ParkingModel();

    public static ParkingModel getInstanse() {
        return instanse;
    }

    private ParkingModel() {
    }

//    public void getParkingInfo(String url, String session, ResponseHandle responseHandle) {
//        requestServer.getApi(url, session, responseHandle);
//    }
//
//    public void getParkingByUser(String url, String session, ResponseHandle responseHandle) {
//        requestServer.getApi(url, session, responseHandle);
//    }
//
//    public void getParkingAround(String url, ResponseHandle responseHandle) {
//        Log.e("url", url);
//        String session = LoginModel.getInstance().getSession();
//        requestServer.getApi(url, session, responseHandle);
//    }
//
//    public void addParking(String url, String json, String session, ResponseHandle responseHandle) {
//        requestServer.postApi(url, json, session, responseHandle);
//    }
//
//    public void getPolyLine(String url, ResponseHandle responseHandle) {
//        requestServer.getApi(url, null, responseHandle);
//    }
//
//    public void addPicture(String jsonObject, ResponseHandle responseHandle) {
//        String url = Constants.SERVER_PARKING + Constants.PARKING_ADD_PICTURE;
//        String session = LoginModel.getInstance().getSession();
//        requestServer.postApi(url, jsonObject, session, responseHandle);
//    }
//
//    public void addPPrices(String jsonObject, ResponseHandle responseHandle) {
//        String url = Constants.SERVER_PARKING + Constants.PARKING_ADD_PRICES;
//        String session = LoginModel.getInstance().getSession();
//        requestServer.postApi(url, jsonObject, session, responseHandle);
//    }
//
//    public void deleteParkingPicrute(int id, ResponseHandle responseHandle) {
//        String url = Constants.SERVER_PARKING + Constants.PARKING_DELETE_PICTURE + id;
//        String session = LoginModel.getInstance().getSession();
//        requestServer.deleteApi(url, "", session, responseHandle);
//    }
//
//    public void deleteParkingPrice(int id, ResponseHandle responseHandle) {
//        String url = Constants.SERVER_PARKING + Constants.PARKING_DELETE_PRICE + id;
//        String session = LoginModel.getInstance().getSession();
//        requestServer.deleteApi(url, "", session, responseHandle);
//    }
//
//    public void updateParking(String url, String json, String session, ResponseHandle responseHandle) {
//        requestServer.putApi(url, json, session, responseHandle);
//    }

    public void getAddressByLatLng(double lat, double lng, ResponseHandle responseHandle) {
        String url = GET_ADDRESS_URL + lat + "," + lng + GET_ADDRESS_KEY + MyApplication.context.getResources().getString(R.string.google_server_key);
        requestServer.getApi(url, null, responseHandle);
    }

//    public void addToFavorite(int id, ResponseHandle responseHandle) {
//        String url = Constants.SERVER_PARKING + Constants.PARKING_FAVORITE;
//        String session = LoginModel.getInstance().getSession();
//
//        JsonObject jsonObject = new JsonObject();
//        jsonObject.addProperty(Constants.JSON_PARKING_ID, id);
//        requestServer.postApi(url, jsonObject.toString(), session, responseHandle);
//    }
}