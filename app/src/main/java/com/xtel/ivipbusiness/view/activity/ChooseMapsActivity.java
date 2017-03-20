package com.xtel.ivipbusiness.view.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.xtel.ivipbusiness.R;
import com.xtel.ivipbusiness.model.entity.PlaceModel;
import com.xtel.ivipbusiness.presenter.ChooseMapsPresenter;
import com.xtel.ivipbusiness.view.activity.inf.IChooseMapsView;
import com.xtel.sdk.commons.Constants;

/**
 * Created by Lê Công Long Vũ on 12/22/2016
 */

public class ChooseMapsActivity extends BasicActivity implements OnMapReadyCallback, GoogleMap.OnCameraIdleListener,
        GoogleMap.OnCameraMoveListener, View.OnClickListener, IChooseMapsView {

    private GoogleMap mMap;
    private Marker marker;
    private PlaceModel placeModel;
    private ChooseMapsPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        presenter = new ChooseMapsPresenter(this);

        presenter.getData();
        initToolbar(R.id.map_toolbar, null);
        initGoogle();
        initView();
        initSearchView();
        presenter.initGoogleApi();
    }

    /*
    * Khởi tạo layout hiển thị map
    * */
    private void initGoogle() {
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    /*
    * Load map thành công
    * */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        LatLng latLng;
        if (placeModel != null)
            latLng = new LatLng(placeModel.getLatitude(), placeModel.getLongtitude());
        else
            latLng = new LatLng(21.026529, 105.831361);
        // Add a marker in Sydney and move the camera
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));

        mMap.setOnCameraMoveListener(this);
        mMap.setOnCameraIdleListener(this);
        mMap.getUiSettings().setMapToolbarEnabled(false);
        mMap.getUiSettings().setMyLocationButtonEnabled(false);
    }

    /*
    * Khởi tạo các view trong layout
    * */
    private void initView() {
        Button btn_choose_location = (Button) findViewById(R.id.btn_map_choose_location);
        btn_choose_location.setOnClickListener(this);

        FloatingActionButton fab_mylocation = (FloatingActionButton) findViewById(R.id.fab_my_location);
        fab_mylocation.setOnClickListener(this);
    }

    /*
    * Khởi tạo view tìm kiếm địa điểm
    * */
    private void initSearchView() {
        PlaceAutocompleteFragment autocompleteFragment = (PlaceAutocompleteFragment) getFragmentManager().findFragmentById(R.id.place_autocomplete_fragment_map);
        autocompleteFragment.setBoundsBias(new LatLngBounds(new LatLng(20.725517, 104.634451), new LatLng(21.937487, 106.759183)));
        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                // TODO: Get info about the selected place.
                mMap.animateCamera(CameraUpdateFactory.newLatLng(place.getLatLng()));
            }

            @Override
            public void onError(Status status) {
                // TODO: Handle the error.
            }
        });
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_map_choose_location) {
            if (placeModel != null) {
                Intent intent = new Intent();
                intent.putExtra(Constants.MODEL, placeModel);
                setResult(RESULT_OK, intent);
                finish();
            } else {
                showShortToast("Vui lòng chọn địa chỉ");
            }
        } else if (v.getId() == R.id.fab_my_location) {
            presenter.getMyLocation();
        }
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home)
            finish();

        return super.onOptionsItemSelected(item);
    }

    /*
    * Sự kiện khi camera di chuyển
    * */
    @Override
    public void onCameraMove() {
        if (marker != null)
            marker.remove();
        placeModel = null;
    }

    /*
    * Sự kiện khi người dùng di chuyển camera sang 1 điểm khác
    * */
    @Override
    public void onCameraIdle() {
        if (marker != null)
            marker.remove();

        double latitude = mMap.getProjection().getVisibleRegion().latLngBounds.getCenter().latitude;
        double longtitude = mMap.getProjection().getVisibleRegion().latLngBounds.getCenter().longitude;

        presenter.getAddress(latitude, longtitude);

        marker = mMap.addMarker(new MarkerOptions()
                .position(new LatLng(latitude, longtitude))
                .icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_marker_red))
                .title("Đang lấy địa chỉ"));
        marker.showInfoWindow();
    }

    @Override
    public void onStart() {
        presenter.onStart();
        super.onStart();
    }

    @Override
    public void onDestroy() {
        presenter.onDestroy();
        super.onDestroy();
    }

    /*
    * Lấy dữ liệu truyền sang thành công
    * */
    @Override
    public void onGetData(PlaceModel placeModel) {
        this.placeModel = placeModel;
    }

    /*
    * Lấy dữ liệu của địa điểm được chọn thành công
    * Hiển thị dữ liệu lên marker
    * */
    @Override
    public void onGetAddressSucces(double lat, double lng, String address) {
        double new_latitude = mMap.getProjection().getVisibleRegion().latLngBounds.getCenter().latitude;
        double new_longtitude = mMap.getProjection().getVisibleRegion().latLngBounds.getCenter().longitude;

        if (new_latitude == lat && new_longtitude == lng) {
            if (address != null && marker != null) {

                marker.remove();
                marker = mMap.addMarker(new MarkerOptions()
                        .position(new LatLng(lat, lng))
                        .icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_marker_red))
                        .title(address));
                marker.showInfoWindow();

                placeModel = new PlaceModel();
                placeModel.setAddress(address);
                placeModel.setLatitude(lat);
                placeModel.setLongtitude(lng);
            }
        }
    }

    /*
    * Lấy thông tin địa chỉ thất bại
    * */
    @Override
    public void onGetAddressError(double lat, double lng) {
        double new_latitude = mMap.getProjection().getVisibleRegion().latLngBounds.getCenter().latitude;
        double new_longtitude = mMap.getProjection().getVisibleRegion().latLngBounds.getCenter().longitude;

        if (new_latitude == lat && new_longtitude == lng) {
            if (marker != null) {
                marker.setTitle("Không lấy được địa chỉ");
            }
        }
    }

    /*
    * Lấy được vị trí của người dùng hiện tại
    * */
    @Override
    public void onGetMyLocation(LatLng latLng) {
        if (mMap != null)
            mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
    }

    @Override
    public Activity getActivity() {
        return this;
    }
}