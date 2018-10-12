package com.ttc.sdk.web;


import java.util.Map;

public interface HttpStack {

    byte[] post(String url, Map<String, String> header, byte[] body);

}
