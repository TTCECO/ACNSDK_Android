package com.ttc.sdk.web;

import com.ttc.sdk.util.StreamUtil;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.util.Map;

/**
 * 网络请求
 */
public class HurlStack implements HttpStack {

    public static final int DEFAULT_TIMEOUT = 10 * 1000;

    @Override
    public byte[] post(String url, Map<String, String> header, byte[] body) {

        InputStream is = null;
        BufferedInputStream bis = null;

        try {
            HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
            connection.setRequestMethod("POST");
            connection.setConnectTimeout(DEFAULT_TIMEOUT);
            connection.setReadTimeout(DEFAULT_TIMEOUT);
            connection.setDoOutput(true);
            connection.setDoInput(true);
            connection.setRequestProperty("Connection", "Keep-Alive");
            connection.setRequestProperty("Content-Type", "application/octet-stream");

            if (header != null) {
                for (Map.Entry<String, String> entry : header.entrySet()) {
                    connection.setRequestProperty(entry.getKey(), entry.getValue());
                }
            }

            OutputStream os = connection.getOutputStream();
            os.write(body);
            os.close();

            connection.connect();

            int code = connection.getResponseCode();
            if (code == 200) {

                is = connection.getInputStream();
                bis = new BufferedInputStream(is);

                ByteArrayOutputStream bos = new ByteArrayOutputStream();

                int read;
                byte[] buf = new byte[1024];

                while ((read = bis.read(buf)) != -1) {
                    bos.write(buf, 0, read);
                }
                return bos.toByteArray();
            }

        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }catch (SocketTimeoutException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (RuntimeException e) {
            e.printStackTrace();
        }catch (Exception e) {
            e.printStackTrace();
        }finally {
            StreamUtil.close(bis, is);
        }

        return null;
    }
}
