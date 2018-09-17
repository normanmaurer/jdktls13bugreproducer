# How to reproduce the problem

## Start OpenSSL server

```
~ /path/to/openssl-1.1.1/bin/openssl s_server -tls1_3 -cert /path/to/jdktls13bugreproducer/src/main/resources/test.crt -key /path/to/jdktls13bugreproducer/src/main/resources/test_unencrypted.pem -4 -accept 127.0.0.1:8443 -state -debug -Verify 1
```

## Build and run

```
~ javac src/main/java/ReproducerClient.java -d target
~ java -cp target/:src/main/resources/ ReproducerClient`
```


# Using OpenSSL as client works as expected

```
~ /path/to/openssl-1.1.1/bin/openssl s_client -cert /path/to/jdktls13bugreproducer/src/main/resources/test.crt -key  /path/to/jdktls13bugreproducer/src/main/resources/test_unencrypted.pem  -state -tls1_3 -connect 127.0.0.1:8443
```




