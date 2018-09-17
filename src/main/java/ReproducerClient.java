import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLEngine;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509ExtendedTrustManager;
import java.io.IOException;
import java.net.Socket;
import java.security.KeyStore;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.concurrent.atomic.AtomicReference;

public final class ReproducerClient {

    private ReproducerClient() { }

    public static void main(String[] args) throws Throwable {
        SSLContext context = SSLContext.getInstance("TLS");
        KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
        keyStore.load(ReproducerClient.class.getResourceAsStream("test.p12"), "test".toCharArray());
        KeyManagerFactory factory = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
        factory.init(keyStore, "test".toCharArray());
        context.init(factory.getKeyManagers(), new TrustManager[] { new InsecureTrustManager()}, null);

        final SSLSocket socket = (SSLSocket) context.getSocketFactory().createSocket("127.0.0.1", 8443);
        socket.startHandshake();

        final AtomicReference<Throwable> error = new AtomicReference<Throwable>();
        Thread readerThread = new Thread(new Runnable() {
            public void run() {
                try {
                    socket.getInputStream().read();
                } catch (Exception e) {
                    error.set(e);
                }
            }
        });
        readerThread.start();
        // Just try to read for 2 seconds before closing the connection, if no error was detected within this time
        // we assume all is good.
        readerThread.join(2000);
        Throwable cause = error.get();
        if (cause != null) {
            throw cause;
        }
        try {
            socket.close();
        } catch (IOException ignore) {
            // ignore
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
