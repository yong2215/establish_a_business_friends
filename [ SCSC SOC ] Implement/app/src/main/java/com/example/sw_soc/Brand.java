package com.example.sw_soc;

public class Brand {
    public double 경도;
    public double 위도;
    public String 구;
    public String 동;
    public String 브랜드명;
    public String 주소;
    public String 지점명;
    public String 카테고리;

    public Brand(){

    }

    public Brand(double 경도, double 위도, String 구, String 동, String 브랜드명, String 주소, String 지점명, String 카테고리) {
        this.경도 = 경도;
        this.위도 = 위도;
        this.구 = 구;
        this.동 = 동;
        this.브랜드명 = 브랜드명;
        this.주소 = 주소;
        this.지점명 = 지점명;
        this.카테고리 = 카테고리;
    }
}
