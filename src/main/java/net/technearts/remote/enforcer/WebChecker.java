package net.technearts.remote.enforcer;

import java.io.IOException;
import java.net.URI;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

public class WebChecker implements DependencyChecker {

	@Override
	public boolean test(URI uri) {
		CloseableHttpClient httpclient = httpClientBuilder();
		HttpGet httpget = new HttpGet(uri);
		try (CloseableHttpResponse response = httpclient.execute(httpget)) {
			return response.getStatusLine().getStatusCode() < 400;
		} catch (IOException e) {
		}
		return false;
	}

	private CloseableHttpClient httpClientBuilder() {
		try {
			SSLContext sc = SSLContext.getInstance("SSL");
			sc.init(null, new TrustManager[] { new TotallyCredulousTrustManager() }, new SecureRandom());
			CloseableHttpClient httpclient = HttpClients.custom().setSSLHostnameVerifier(new NoopHostnameVerifier())
					.setSSLContext(sc).build();
			return httpclient;
		} catch (NoSuchAlgorithmException | KeyManagementException e) {
			throw new IllegalStateException("Unable to create conection", e);
		}

	}

	private static final class TotallyCredulousTrustManager implements X509TrustManager {
		@Override
		public void checkClientTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {

		}

		@Override
		public void checkServerTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {

		}

		@Override
		public X509Certificate[] getAcceptedIssuers() {
			return null;
		}
	}
}
