package com.solace.poc;


import java.io.*;
import java.security.*;
import java.security.cert.*;
import javax.net.ssl.*;


class SslUtil
{
    static X509Certificate readCert(final String fname) throws CertificateException, FileNotFoundException {
        return (X509Certificate) CertificateFactory
                .getInstance("X.509")
                .generateCertificate(new FileInputStream(fname));
    }

    static KeyStore loadKeystore(final String keystoreFile, final String password) throws KeyStoreException, CertificateException, NoSuchAlgorithmException, IOException {
        KeyStore ks = KeyStore.getInstance(KeyStore.getDefaultType());
        ks.load(new FileInputStream(keystoreFile), password.toCharArray());
        return ks;
    }

    static SSLSocketFactory getSocketFactory (final String caCrtFile) throws Exception
    {
        // load CA certificate
        X509Certificate caCert = SslUtil.readCert(caCrtFile);
        // CA certificate is used to authenticate server
        KeyStore caKs = KeyStore.getInstance(KeyStore.getDefaultType());
        caKs.load(null, null);
        caKs.setCertificateEntry("ca-certificate", caCert);
        TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
        tmf.init(caKs);

        // finally, create SSL socket factory
        SSLContext context = SSLContext.getInstance("TLSv1.2");
        context.init(null, tmf.getTrustManagers(), null);

        return context.getSocketFactory();
    }

    static SSLSocketFactory getSocketFactory (final String caCrtFile,
                                              final String keystoreFile,
                                              final String keystorePassword) throws Exception
    {
        // load CA certificate
        X509Certificate caCert = SslUtil.readCert(caCrtFile);
        // CA certificate is used to authenticate server
        KeyStore caKs = KeyStore.getInstance(KeyStore.getDefaultType());
        caKs.load(null, null);
        caKs.setCertificateEntry("ca-certificate", caCert);
        TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
        tmf.init(caKs);

        // client key and certificates are sent to server so it can authenticate us
         KeyStore ks = loadKeystore(keystoreFile, keystorePassword);

        KeyManagerFactory kmf = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
        kmf.init(ks, keystorePassword.toCharArray());

        // finally, create SSL socket factory
        SSLContext context = SSLContext.getInstance("TLSv1.2");
        context.init(kmf.getKeyManagers(), tmf.getTrustManagers(), null);

        return context.getSocketFactory();
    }
}