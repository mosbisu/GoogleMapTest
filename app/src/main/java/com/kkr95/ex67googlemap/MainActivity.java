package com.kkr95.ex67googlemap;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class MainActivity extends AppCompatActivity {

    //1. GoogleMap Library부터 추가 [play-services-maps]
    //2. 구글 지도 사용에 대한 API 키 발급

    //구글지도를 제어하는 객체 참조변수
    GoogleMap GMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //SupportMapFragment 안에 있는 GoogleMap 객체를 얻어오기
        //우선 xml에 만든 SupportMapFragment를 참조하기
        FragmentManager fragmentManager= getSupportFragmentManager();
        final SupportMapFragment mapFragment= (SupportMapFragment)fragmentManager.findFragmentById(R.id.map);

        //비동기방식(별도 Thread)으로 지도를 불러오기
        mapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                //내 멤버변수에 얻어온 GoogleMap 대입
                GMap= googleMap;

                //원하는 좌표 객체 생성
                LatLng seoul= new LatLng(37.56, 126.97);

                //마커옵션 객체 생성(마커의 설정)
                MarkerOptions markerOptions= new MarkerOptions();
                markerOptions.position(seoul);
                markerOptions.title("서울");
                markerOptions.snippet("대한민국의 수도");

                //지도에 마커 추가
                GMap.addMarker(markerOptions);

                //원하는 좌표 위치로 카메라 이동
                //GMap.moveCamera(CameraUpdateFactory.newLatLng(seoul));

                //카메라 이동을 스무스하게 효과를 주면서 zoom까지 적용
                GMap.animateCamera(CameraUpdateFactory.newLatLngZoom(seoul, 16));

                //마커 여러개 추가도 가능함
                LatLng mrhi= new LatLng(37.5608, 127.0346);

                MarkerOptions mo= new MarkerOptions();
                mo.position(mrhi);
                mo.title("미래능력개발교육원");
                mo.snippet("http://www.mrhi.or.kr");
                mo.icon(BitmapDescriptorFactory.fromResource(R.drawable.delete));
                mo.anchor(0.5f, 1.0f);

                Marker marker= GMap.addMarker(mo); //추가된 마커객체를 리턴해줌
                //마커를 클릭하지 않아도 InforWindow가 보이도록 할 수 있음
                marker.showInfoWindow();

                //지도의 정보창을 클릭했을 때 반응하기
                GMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
                    @Override
                    public void onInfoWindowClick(Marker marker) {
                        String title= marker.getTitle();

                        if(title.equals("미래능력개발교육원")){
                            //교육원 홈페이지로 이동(웹브라우저 실행)
                            Intent intent= new Intent();
                            intent.setAction(Intent.ACTION_VIEW);
                            Uri uri= Uri.parse("http://www.mrhi.or.kr");
                            intent.setData(uri);

                            startActivity(intent);
                        }
                    }
                });

                //카메라 위치 변경
                GMap.animateCamera(CameraUpdateFactory.newLatLngZoom(mrhi, 14));

                //정보창의 커스텀모양을 만들고 싶다면
                //정보창을 만들어주는 Adapter객체 생성
                MyInfoWinAdapter adapter= new MyInfoWinAdapter(MainActivity.this);
                GMap.setInfoWindowAdapter(adapter);

                //줌컨트롤 보이도록 설정
                UiSettings settings= GMap.getUiSettings();
                settings.setZoomControlsEnabled(true);

                //내 위치 보여주기 [위치정보제공 퍼미션 작업 필요 - 동적퍼미션]
                GMap.getUiSettings().setMyLocationButtonEnabled(false);
            }
        });
    }
}
