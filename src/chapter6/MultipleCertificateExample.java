package chapter6;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.security.KeyPair;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

/**
 * Basic example of using a CertificateFactory.
 */
public class MultipleCertificateExample
{
    
	// use the alternative technique as proposed in the book?
	private static boolean switcharoo = true;
	
	public static void main(String[] args)
        throws Exception
    {
        // create the keys
        KeyPair          pair = Utils.generateRSAKeyPair();
        
        // create the input stream
        ByteArrayOutputStream bOut = new ByteArrayOutputStream();
        
        bOut.write(X509V1CreateExample.generateV1Certificate(pair).getEncoded());
        bOut.write(X509V3CreateExample.generateV3Certificate(pair).getEncoded());
        
        bOut.close();
        
        InputStream in = new ByteArrayInputStream(bOut.toByteArray());
        
        // create the certificate factory
        CertificateFactory fact = CertificateFactory.getInstance("X.509","BC");
        
        // read the certificates
        X509Certificate    x509Cert;
		Collection collection;
        if (switcharoo) { // use the alternative technique proposed in the book
        	collection = fact.generateCertificates(in);
        }
        else { // use the original technique
        	collection = new ArrayList();
        	
        	while((x509Cert = (X509Certificate)fact.generateCertificate(in)) != null)
        	{
        		collection.add(x509Cert);
        	}
        }
        
        Iterator it = collection.iterator();
        while (it.hasNext())
        {
            System.out.println("version: " + ((X509Certificate)it.next()).getVersion());
        }
    }
}
