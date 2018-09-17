import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLEngine;
import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509ExtendedTrustManager;
import java.io.IOException;
import java.net.Socket;
import java.nio.charset.Charset;
import java.security.KeyStore;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.concurrent.atomic.AtomicReference;

public class ReproducerServer {

    public static void main(String[] args) throws Throwable {
        SSLContext context = SSLContext.getInstance("TLS");
        KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
        keyStore.load(ReproducerServer.class.getResourceAsStream("test.p12"), "test".toCharArray());
        KeyManagerFactory factory = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
        factory.init(keyStore, "test".toCharArray());
        context.init(factory.getKeyManagers(), new TrustManager[] { new InsecureTrustManager()}, null);

        final SSLServerSocket serverSocket = (SSLServerSocket) context.getServerSocketFactory().createServerSocket(8443);

        try {
            for (;;) {
                Socket socket = serverSocket.accept();

                socket.getOutputStream().write("hello".getBytes(Charset.defaultCharset()));
                try {
                    socket.close();
                } catch (IOException ignore) {
                    // ignore
                }
            }
        } finally {
            serverSocket.close();
        }

    }

    private static final class InsecureTrustManager extends X509ExtendedTrustManager {
        @Override
        public void checkClientTrusted(X509Certificate[] x509Certificates, String s, Socket socket)
                                throws CertificateException {

        }

        @Override
        public void checkServerTrusted(X509Certificate[] x509Certificates, String s, Socket socket)
                                throws CertificateException {

        }

        @Override
        public void checkClientTrusted(X509Certificate[] x509Certificates, String s,
                SSLEngine sslEngine) throws CertificateException {

        }

        @Override
        public void checkServerTrusted(X509Certificate[] x509Certificates, String s,
                SSLEngine sslEngine) throws CertificateException {

        }

        public void checkClientTrusted(X509Certificate[] x509Certificates, String s)
                                throws CertificateException {

        }

        public void checkServerTrusted(X509Certificate[] x509Certificates, String s)
                                throws CertificateException {

        }

        public X509Certificate[] getAcceptedIssuers() {
            return new X509Certificate[0];
        }
    }
}
