package com.dykj.zhonganxiao.base.http;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import javax.security.cert.X509Certificate;

/**
 * @file: HttpsTrustManager
 * @author: guokang
 * @date: 2019-10-12
 */
public class HttpsTrustManager implements X509TrustManager {
    private static TrustManager[] trustManagers;
    private static final X509Certificate[] _AcceptedIssuers = new X509Certificate[]{};

//    @Override
//    public void checkClientTrusted(
//            X509Certificate[] x509Certificates, String s)
//            throws java.security.cert.CertificateException {
//    }
//
//    @Override
//    public void checkServerTrusted(
//            X509Certificate[] x509Certificates, String s)
//            throws java.security.cert.CertificateException {
//    }
//
    //    @Override
//    public X509Certificate[] getAcceptedIssuers() {
//        return _AcceptedIssuers;
//    }


    public boolean isClientTrusted(X509Certificate[] chain) {
        return true;
    }

    public boolean isServerTrusted(X509Certificate[] chain) {
        return true;
    }


    public static void allowAllSSL() {
        HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier() {

            @Override
            public boolean verify(String arg0, SSLSession arg1) {
                return true;
            }

        });

        SSLContext context = null;
        if (trustManagers == null) {
            trustManagers = new TrustManager[]{new HttpsTrustManager()};
        }

        try {
            context = SSLContext.getInstance("TLS");
            context.init(null, trustManagers, new SecureRandom());
        } catch (NoSuchAlgorithmException | KeyManagementException e) {
            e.printStackTrace();
        }

        HttpsURLConnection.setDefaultSSLSocketFactory(context != null ? context.getSocketFactory() : null);
    }

    @Override
    public void checkClientTrusted(java.security.cert.X509Certificate[] x509Certificates, String s) throws CertificateException {

    }

    @Override
    public void checkServerTrusted(java.security.cert.X509Certificate[] x509Certificates, String s) throws CertificateException {

    }

    @Override
    public java.security.cert.X509Certificate[] getAcceptedIssuers() {
        return new java.security.cert.X509Certificate[0];
    }
}
