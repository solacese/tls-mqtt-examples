package com.solace.poc;

import org.junit.Test;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.security.KeyStore;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;

import static org.junit.Assert.*;

/**
 * Unit test for simple ClientCertAuthApp.
 */
public class SslUtilTest
{
    @Test
    public void loadPemCert() throws CertificateException, FileNotFoundException
    {
        X509Certificate cert = SslUtil.readCert("resources/sampleCA.pem");
        assertNotNull(cert);
        assertEquals("X.509", cert.getType());
    }

    @Test
    public void testKeystore() throws Throwable {
        KeyStore ks = SslUtil.loadKeystore(
                "resources/mqttclient.keystore",
                "mqttsample");
        assertNotNull(ks);
        assertEquals("jks", ks.getType());
    }
}
