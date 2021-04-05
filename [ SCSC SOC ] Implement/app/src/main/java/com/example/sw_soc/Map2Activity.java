package com.example.sw_soc;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class Map2Activity extends AppCompatActivity implements GoogleMap.OnMarkerClickListener,OnMapReadyCallback,GoogleMap.OnInfoWindowClickListener {

    private GoogleMap mMap;
    String text;


    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference bmRef;
    private DatabaseReference Ref;

    double PLimitDistance = 1000;
    double PDis = 0.001;
    double CDis=0.008;

    //즐겨찾기 추가시 사용
    double bmlat;
    double bmlon;
    String bmlocation;

    // 즐겨찾기 출력시 사용
    double bmlat2;
    double bmlon2;

    String storeName;
    String storeArea;

    ArrayList lon = new ArrayList(); //PandS가 끝난 후 결과가 담길 리스트
    ArrayList lat = new ArrayList();
    ArrayList score = new ArrayList();

    ArrayList fLon = new ArrayList();// decideLoc을 통한 최종 위치가 담길 리스트
    ArrayList fLat = new ArrayList();
    ArrayList fScore = new ArrayList();

    ArrayList dataLon = new ArrayList(); //유동인구와 같은 데이터가 담길 리스트
    ArrayList dataLat = new ArrayList();
    ArrayList dataScore = new ArrayList();

    ArrayList exsistLon = new ArrayList(); //기존 존재하는 매장 위치
    ArrayList exsistLat = new ArrayList();

    private Spinner spinner;
    private Spinner spinner2;
    private Spinner spinner0;
    int brand;
    int areai;
    int disImp;
    int count = 1;
    int count2 = 0; // 즐겨찾기 개수
    int num = 0; // 추천받지 않고 stat를 누르는거 방지 0이 아니면 stat 사용 가능
    List latlist = new ArrayList<>(); // lat
    List lonlist = new ArrayList<>(); // lon

    //구 별 위경도
    final double[] slon = new double[] {128.373616,128.504220,128.608077,128.520008,128.575028,128.476054,128.594223,128.559234,128.366662};
    final double[] slat = new double[] {35.610183,35.880520,35.859471,35.856942,35.856267,35.774333,35.795346,35.808061,35.614232};
    final double[] elon = new double[] {128.758996,128.631429,128.756050,128.581291,128.613223,128.584888,128.723999,128.607728,128.545823};
    final double[] elat = new double[] {36.015045,35.987059,36.015692,35.895332,35.877412,35.867453,35.875227,35.857606,35.815284};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map2);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        Intent intent = getIntent();
        final String id = intent.getExtras().getString("id");
        final String pw = intent.getExtras().getString("pw");
        final String name = intent.getExtras().getString("name");

        Button searchBtn = (Button)findViewById(R.id.searchButton);
        Button statBtn = (Button)findViewById(R.id.statButton);

        spinner0 = (Spinner)findViewById(R.id.Spinner0);
        spinner = (Spinner)findViewById(R.id.Spinner);
        spinner2 = (Spinner)findViewById(R.id.Spinner2);

        // 브랜드 선택
        spinner0.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                brand = position;
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        //지역선택
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                areai = position-1;
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        //기존에 존재하는 지점을 얼마나 고려할지 선택
        spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                disImp = position-1;
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        //검색버튼 누를 시
        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // spinner에서 선택한 내용 추출
                mMap.clear();

                text = spinner0.getSelectedItem().toString();
                final String text2 = spinner.getSelectedItem().toString();
                String text3 = spinner2.getSelectedItem().toString();
                if(text.equals("선택") || text2.equals("선택") || text3.equals("기존매장")){
                    Toast.makeText(getApplicationContext(),"선택하지 않은 사항이 있습니다", Toast.LENGTH_LONG).show();
                }
                else{
                    storeName = text;
                    storeArea = text2;
                    // 즐겨찾기 중 선택한 브랜드와 같은 목록들
                    bmRef = database.getReference("User/"+id);
                    bmRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            Iterator<DataSnapshot> child = snapshot.getChildren().iterator();
                            latlist.clear();
                            lonlist.clear();
                            count2 = 0;
                            while (child.hasNext()) {
                                if (child.next().getKey().equals("bookmark")) {
                                    FirebaseDatabase database2 = FirebaseDatabase.getInstance();
                                    DatabaseReference bmRef2 = database2.getReference("User/" + id + "/bookmark");
                                    bmRef2.addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot2) {
                                            for (DataSnapshot dataSnapshot2 : snapshot2.getChildren()) {
                                                if(text.equals(dataSnapshot2.getKey().toString().split(" ")[0])){ // 선택한 브랜드와 같을 경우
                                                    String value = dataSnapshot2.getValue().toString();
                                                    String[] loc = value.split(",");
                                                    latlist.add(loc[0]);
                                                    lonlist.add(loc[1]);
                                                    count2++;
                                                }
                                            }
                                            onMapReady(mMap);
                                        }
                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {
                                        }
                                    });
                                }

                            }

                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

                    //검색하면 기존의 결과는 초기화
                    dataLat.clear();dataLon.clear();dataScore.clear();
                    lon.clear();lat.clear();score.clear();
                    fLon.clear();fLat.clear();fScore.clear();
                    count = 1;


                    //유동인구 점수화
                    Ref = database.getReference("Floating_population");
                    Ref.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            Iterator<DataSnapshot> child = snapshot.getChildren().iterator();
                            while(child.hasNext()){
                                Iterator<DataSnapshot> innerData = child.next().getChildren().iterator();
                                while (innerData.hasNext()){
                                    DataSnapshot nxtData = innerData.next();
                                    if (nxtData.getKey().equals("경도")) { dataLon.add((Double)nxtData.getValue()); }
                                    else if (nxtData.getKey().equals("위도")){ dataLat.add((Double)nxtData.getValue()); }
                                    else if (nxtData.getKey().equals("일평균 승하차인원")){ dataScore.add(nxtData.getValue()); }
                                }
                            }
                            PandS(dataLon,dataLat,dataScore,true);
                            exsistLoc();
                            decideLoc(lon,lat,score);
                            onMapReady(mMap);
                            num++;
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                        }
                    });
                }

            }
        });

        statBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = spinner0.getSelectedItem().toString();
                String text2 = spinner.getSelectedItem().toString();
                String text3 = spinner2.getSelectedItem().toString();
                if(text.equals("선택") || text2.equals("선택") || text3.equals("기존매장")){
                    Toast.makeText(getApplicationContext(),"선택하지 않은 사항이 있습니다", Toast.LENGTH_LONG).show();
                }
                else if(num == 0){
                    Toast.makeText(getApplicationContext(),"검색 후 이용해주세요", Toast.LENGTH_LONG).show();
                }
                else{
                    Intent statIntent = new Intent(Map2Activity.this, ShowDataActivity.class);
                    statIntent.putExtra("id",id);
                    statIntent.putExtra("pw",pw);
                    statIntent.putExtra("name",name);
                    statIntent.putExtra("area",storeArea);
                    Map2Activity.this.startActivity(statIntent);
                }
                // **여기까지**
            }
        });

    } //onCreate종료


    void DisScaler(ArrayList x) {
        Double num;
        for (int i=0;i<x.size();i++) {
            num = ((PLimitDistance - (double)(x.get(i))) / PLimitDistance);
            x.set(i,num);
        }
        return;
    }

    void MMScaler(ArrayList x){
        Double mx = Max(x);
        Double mn = Min(x);

        if (mx == 0){
            return;
        }
        if (mx - mn == 0){
            for(int i=0;i<x.size();i++){
                x.set(i,(double)1);
            }
            return;
        }
        for (int i=0;i<x.size();i++){
            x.set(i,((double)x.get(i)-mn)/(mx-mn));
        }
        return;
    }

    double Max(ArrayList x){
        Double max=(Double) x.get(0);
        for(int i=0;i<x.size();i++){
            double X = (double)x.get(i);
            if(max<X)
                max = X;
        }
        return max;
    }

    double Min(ArrayList x){
        double min=(double) x.get(0);
        for(Object X:x){
            if(min>(double)X) min = (double)X;
        }
        return min;
    }

    double rad(double x){
        return (x*3.14159/180.0);
    }

    double GetDistance(double p1x, double p1y,double p2x,double p2y){  //경위도 간 거리 구하는 함수, x=Lon=경도 , y=Lat=위도
        double dLat = rad((p2y-p1y));
        double dLon = rad((p2x-p1x));

        p1y = rad(p1y);
        p2y = rad(p2y);

        double a = Math.sin(dLat/2) * Math.sin(dLat/2) + Math.sin(dLon/2) * Math.sin(dLon/2) * Math.cos(p1y) * Math.cos(p2y);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
        double radius = 6371;
        double dDistance = radius * c;
        dDistance*=1000;

        return dDistance;
    }


    void PandS(ArrayList DF0, ArrayList DF1, ArrayList DF2, boolean Col){ //Col = false > score가 0/1로만 이루어진 경우 (or 없는 경우)
        //Toast.makeText(getApplicationContext(),count+"번째 데이터파일 분석중.\nThis would take some time",Toast.LENGTH_SHORT).show();
        count++;
        double Blat = slat[areai];
        double Blon = slon[areai];

        ArrayList list1 = new ArrayList();// lat
        ArrayList list2 = new ArrayList();// lon
        ArrayList list3 = new ArrayList();// score

        while(true){
            double scr = 0;
            if (Blat >= elat[areai]){
                Blat = slat[areai];
                Blon += PDis;
            }
            ArrayList L4S = new ArrayList();
            ArrayList L4D = new ArrayList();// 거리 스케일링을 위한 리스트 List for Distance

            //점의 범위안에 있는 요소를 저장하는 반복문
            for (int i=0;i<DF0.size();i++){
                double dis = GetDistance(Blon,Blat,(double) DF0.get(i), (double)DF1.get(i));
                if (dis <= PLimitDistance) L4D.add(dis);
                double X;
                try{
                    X = (double)DF2.get(i);
                }catch(java.lang.ClassCastException e){
                    X = ((Long)DF2.get(i)).doubleValue();
                }
                if (Col) L4S.add(X);
            }

            //요소에 따라 거리로만 점수부여, 혹은 (거리*요소의 크기)로 점수 부여
            if (L4D.size()>0){
                if (L4D.size()==1 || !Col){
                    DisScaler(L4D);
                    for (int i=0; i<L4D.size();i++){
                        scr += (double) L4D.get(i) * 100;
                    }
                }
                else{
                    MMScaler(L4S);
                    DisScaler(L4D);
                    for(int i=0;i<L4D.size();i++) scr += ((double)L4S.get(i)) * ((double)L4D.get(i)) * 100;
                }
            }

            list1.add(Blat);//찍은 점과 부여한 점수 기록
            list2.add(Blon);
            list3.add(scr);

            if (Blon >= elon[areai]) break;
            Blat = Blat + PDis;
        }

        if(lon.size()==list1.size()){
            for (int i=0; i<list1.size();i++){
                score.set(i,(double)score.get(i)+(double)list3.get(i));
            }
        }
        else {
            //Toast.makeText(getApplicationContext(),"size dismatch",Toast.LENGTH_SHORT).show();
            lon.clear();
            lat.clear();
            score.clear();
            for (Object O:list2){
                lon.add(O);
            }
            for (Object O:list1){
                lat.add(O);
            }
            for (Object O:list3){
                score.add(O);
            }
        }
        dataLon.clear();
        dataLat.clear();
        dataScore.clear();
        return;
    }//PandS 종료

    int Maxi(ArrayList x){
        int maxi=0;
        double max=(double) x.get(0);
        for(int i=0;i<x.size();i++){
            if(max<(double)x.get(i)){
                max = (double) x.get(i);
                maxi = i;
            }
        }
        return maxi;
    }

    void decideLoc(ArrayList df0, ArrayList df1, ArrayList df2){
        Toast.makeText(getApplicationContext(),"최종 위치 선정중",Toast.LENGTH_SHORT).show();
        double Blat = slat[areai];
        double Blon = slon[areai];

        double Rad = (GetDistance(Blon, Blat, Blon+CDis,Blat))*4/5;

        while(true){
            if (Blat >=  elat[areai]){
                Blat = slat[areai];
                Blon += CDis;
            }
            ArrayList Tlist1 = new ArrayList();// lat
            ArrayList Tlist2 = new ArrayList();// lon
            ArrayList Tlist3 = new ArrayList();// score  임시 arraylist

            for (int i=0;i<df0.size();i++){
                double dis = GetDistance(Blon,Blat,(double)df0.get(i), (double)df1.get(i));
                if (dis <= Rad && (double)df2.get(i) != 0) {
                    Tlist1.add(df0.get(i));
                    Tlist2.add(df1.get(i));
                    Tlist3.add(df2.get(i));
                }
            }

            if (Tlist3.size() >0){
                int maxi= Maxi(Tlist3);
                fLon.add(Tlist1.get(maxi));
                fLat.add(Tlist2.get(maxi));
                fScore.add(Tlist3.get(maxi));
            }

            if(Blon >= elon[areai]) break;
            Blat += CDis;
        }
        return;
    } //decideLoc 종료

    void exsistLoc(){ //존재하는 프렌차이즈의 위치에 따라 점수 재책정
        Toast.makeText(getApplicationContext(),"기존 매장위치 반영중",Toast.LENGTH_SHORT).show();
        final double Rad=2000 ; //매장의 2km 이내는 점수 떨어짐
        Ref = database.getReference("Brand");

        Ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Iterator<DataSnapshot> child = snapshot.getChildren().iterator();
                while(child.hasNext()){
                    DataSnapshot brandNamesSS = child.next();
                    if(text.equals(brandNamesSS.getKey())){
                        Iterator<DataSnapshot> innerData = brandNamesSS.getChildren().iterator();
                        while (innerData.hasNext()){
                            Iterator<DataSnapshot> secInnerData = innerData.next().getChildren().iterator();
                            while(secInnerData.hasNext()){
                                DataSnapshot nxtData = secInnerData.next();
                                if (nxtData.getKey().equals("경도")) {
                                    exsistLon.add(nxtData.getValue());
                                }
                                else if (nxtData.getKey().equals("위도")){
                                    exsistLat.add(nxtData.getValue());
                                }
                            }
                        }
                    }
                }
                for(int i=0;i<exsistLon.size();i++){ //기존에 위치하는 모든 매장에 대해 반복
                    for (int j=0;j<lon.size();j++){ //PandS에서 점에 부여된 점수들을 매장과의 거리에 따라 차감

                        double dis = GetDistance((double)exsistLon.get(i),(double)exsistLat.get(i),(double)lon.get(j),(double)lat.get(j));

                        if (dis <= Rad && (double)score.get(j) != 0){
                            //선택한 고려정도에 따라 차감치 조절 > 기존에 위치한 매장이 가까울수록 점수 낮아짐
                            Double scr = (Double)score.get(j);
                            switch(disImp){
                                case 0: //조금 고려
                                    score.set(j,scr-scr*(Rad-dis)/Rad*50/100); //기존 매장과 딱붙어있으면 점수 50프로 감소, 최대거리일 시 0%감소
                                    break;
                                case 1: //보통
                                    score.set(j,scr-scr*(Rad-dis)/Rad*70/100); //차감치 70% ~ 0%
                                    break;
                                case 2: //매우 고려
                                    score.set(j,scr-scr*(((Rad-dis)/Rad*80/100)+20/100)); //차감치 80% ~ 20%
                                    break;
                            } //switch문 끝
                        } //if문 끝
                    } //for문(j) 끝
                } //for문(i) 끝
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    } //exsistLoc끝

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        LatLng center = new LatLng(35.842967, 128.577986);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(center,14));

        double lat;
        double lng;
        double finsocre;
        ArrayList sortedScore = new ArrayList();
        sortedScore = (ArrayList) fScore.clone();
        Collections.sort(sortedScore);

        for (int i = 0; i < fScore.size(); i++) {
            lat = (double) fLat.get(i);
            lng = (double) fLon.get(i);
            finsocre = (double) fScore.get(i);
            float markerColor=BitmapDescriptorFactory.HUE_GREEN;
            float seeThrough = 1.0f;

            String finalScore = String.format("%.2f",finsocre);

            if(Double.parseDouble(finalScore) < (Double)sortedScore.get(sortedScore.size()/3)){
                markerColor = BitmapDescriptorFactory.HUE_GREEN;
                seeThrough = 0.2f;
            } else if(Double.parseDouble(finalScore) < (Double)sortedScore.get(sortedScore.size()/3*2)){
                markerColor = BitmapDescriptorFactory.HUE_YELLOW;
                seeThrough = 0.5f;
            } else if(Double.parseDouble(finalScore) > (Double)sortedScore.get(sortedScore.size()/3*2)){
                markerColor = BitmapDescriptorFactory.HUE_RED;
                seeThrough = 1.0f;
            }

            LatLng fPoint = new LatLng(lat, lng);
            Marker recPoint = mMap.addMarker(new MarkerOptions()
                    .position(fPoint)
                    .title((i + 1) + "번째 최적의 위치")
                    .snippet("점수: " + finalScore+" 즐겨찾기")
                    .icon(BitmapDescriptorFactory.defaultMarker(markerColor))
                    .alpha(seeThrough));
            recPoint.showInfoWindow();
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(fPoint,14));
        }
        mMap.setOnMarkerClickListener(this);
        mMap.setOnInfoWindowClickListener(this);


        for(int j=0; j<count2;j++)
        {
            bmlat2 = Double.parseDouble((String) latlist.get(j));
            bmlon2 = Double.parseDouble((String) lonlist.get(j));
            LatLng bmPoint = new LatLng(bmlat2,bmlon2);
            Marker bPoint = mMap.addMarker(new MarkerOptions()
                    .position(bmPoint)
                    .title("즐겨찾기한 위치")
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE))
                );
            bPoint.showInfoWindow();
        }
    }

    @Override
    public boolean onMarkerClick(Marker marker) {

        bmlat=marker.getPosition().latitude;
        bmlon = marker.getPosition().longitude;

        return  false;
    }

    @Override
    public void onInfoWindowClick(Marker marker) {

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        String id = intent.getExtras().getString("id");


        bmlocation = String.valueOf(bmlat) +"," +String.valueOf(bmlon); // key

        bmRef = database.getReference("User/" + id);
        bmRef.child("bookmark").child(storeName+" "+storeArea+marker.getTitle()).setValue(bmlocation);
        marker.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
        marker.setAlpha(0.7f);

        Toast.makeText(getApplicationContext(),"즐겨찾기에 추가되었습니다.",Toast.LENGTH_LONG).show();

    }
}