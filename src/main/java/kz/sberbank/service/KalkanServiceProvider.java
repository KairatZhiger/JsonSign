package kz.sberbank.service;

import kz.gov.pki.kalkan.jce.provider.KalkanProvider;
import kz.gov.pki.kalkan.jce.provider.cms.CMSSignedData;
import kz.gov.pki.kalkan.util.encoders.Base64;
import kz.gov.pki.provider.utils.CMSUtil;
import kz.gov.pki.provider.utils.model.SigningEntity;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.cert.X509Certificate;
import java.util.*;

public class KalkanServiceProvider {


    public String initKalkan(String request, String password, String Path) {


        try {
            Provider provider = new KalkanProvider();
            boolean exists = false;
            Provider[] providers = Security.getProviders();
            for (Provider p : providers) {
                if (p.getName().equals(provider.getName())) {
                    exists = true;
                }
            }
            if (!exists) {
                Security.addProvider(provider);
            }

            KeyStore store = KeyStore.getInstance("PKCS12", provider.getName());
            InputStream inputStream;
            inputStream = AccessController.doPrivileged((PrivilegedExceptionAction<FileInputStream>) () -> new FileInputStream(Path));
            store.load(inputStream, password.toCharArray());

            Enumeration<String> als = store.aliases();
            String alias = null;
            while (als.hasMoreElements()) {
                alias = als.nextElement();
            }

            PrivateKey privateKey = (PrivateKey) store.getKey(alias, password.toCharArray());
            X509Certificate x509Certificate = (X509Certificate) store.getCertificate(alias);


            return signcms(request, privateKey, x509Certificate, provider);

        } catch (Exception ex) {
            ex.printStackTrace();

        }
        return "";

    }


    public String signcms(String request, PrivateKey privateKey, X509Certificate x509Certificate, Provider provider) {

        try {
            List<X509Certificate> x509CertificateList=new  ArrayList<>();
            x509CertificateList.add(x509Certificate);
            SigningEntity signingEntity=new SigningEntity(privateKey,
                    x509CertificateList);


            CMSSignedData sigData =   CMSUtil.createCAdES(signingEntity,request.getBytes(StandardCharsets.UTF_8),true,false,provider);

            return "-----BEGIN CMS-----\n"+Base64.encodeStr(sigData.getEncoded())+"\n-----END CMS-----\n";
        } catch (Exception ex) {

            ex.printStackTrace();

        }


        return "";
    }

}
