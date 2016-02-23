import java.util.List;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.binary.Base64;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.*;
import org.apache.http.impl.client.*;
import org.apache.http.message.BasicNameValuePair;


@WebServlet("/oauth")
public class AuthServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@SuppressWarnings("deprecation")
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		//retrieve the code from the HTTP request
		String code = request.getParameter("code");
		
		String client_secret = "e3aa9f100b044d7ddb502afa44edea962ea1b8b5";
		String client_id = "285337afd2d73efe792b";
		String redirect_uri = "http://localhost:2587/oauth";
		String oauth_url = "https://clever.com/oauth/tokens";
		String api_base = "https://api.clever.com";
		
		HttpPost request_post = new HttpPost(oauth_url);
		
		String client_id_secret = client_id + ":" + client_secret;
		byte[] bytesEncoded = Base64.encodeBase64(client_id_secret.getBytes());
		client_id_secret = new String(bytesEncoded);
		String basic_auth_header = "Basic "+ client_id_secret;

		//construct POST request header
		request_post.addHeader("Authorization", basic_auth_header);
		request_post.addHeader("Content-Type", "application/x-www-form-urlencoded");
		
		//construct POST body
		List<BasicNameValuePair> post_body = new ArrayList<BasicNameValuePair>();
		post_body.add(new BasicNameValuePair("code", code));
		post_body.add(new BasicNameValuePair("grant_type", "authorization_code"));
		post_body.add(new BasicNameValuePair("redirect_uri", redirect_uri));
		request_post.setEntity(new UrlEncodedFormEntity(post_body, "UTF_8"));
		
		//execute POST request to get token
		HttpClient http_client = new DefaultHttpClient();
		HttpResponse response_post = http_client.execute(request_post);
		
		//parse the result to get access_token

		//Header[] post_response_headers = response_post.getAllHeaders();
		BufferedReader post_response_body = new BufferedReader(new InputStreamReader(response_post.getEntity().getContent(), "UTF-8"));
		String json_response_line = ""; String token = "";
		if (response_post.getStatusLine().getStatusCode() == 200)
		{
			while (post_response_body.readLine() != null)
			{
				json_response_line = post_response_body.readLine();
				if (json_response_line.contains("access_token"))
					token = json_response_line.substring(16, (json_response_line.length()-2));
			}
		}
		
		if (token != "")
		{
			//Using this token, hit the /me endpoint now using a HTTP Get request
			HttpGet request_get = new HttpGet(api_base + "/me");
			request_get.addHeader("Authorization", "Basic " + token);
			http_client.execute(request_get);
		}
		
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
