//package com.v6.yeogaekgi.util;
//
//import lombok.AllArgsConstructor;
//import lombok.Data;
//
//import java.io.Serializable;
//import java.io.UnsupportedEncodingException;
//import java.net.URLEncoder;
//
//@Data
//@AllArgsConstructor
//public class UploadResultDTO implements Serializable {
//
//    private String fileName;
//    private String uuid;
//    private String folderPath;
//
//    // json 라이브러리가 get~ 이렇게 생긴 메서드의 ~부분을 속성명으로 json 객체를 만든다
//    public String getImageURL(){
//        try {
//            return URLEncoder.encode(folderPath+"/"+uuid+"_"+fileName,"UTF-8");
//        } catch (UnsupportedEncodingException e) {
//            e.printStackTrace();
//        }
//        return "";
//    }
//
//    public String getThumbnailURL(){
//        try {
//            return URLEncoder.encode(folderPath+"/s_"+uuid+"_"+fileName,"UTF-8");
//        } catch (UnsupportedEncodingException e) {
//            e.printStackTrace();
//        }
//        return "";
//    }
//
//}
