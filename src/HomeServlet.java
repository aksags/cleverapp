import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLEncoder;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


@WebServlet("/")
public class HomeServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

    	public void init() throws ServletException
    	{
    	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		// render the Clever Login button with the encoded redirect URL
		
		String client_id = "285337afd2d73efe792b";
		String response_type = "code";
		String redirect_uri = "http://localhost:2587/oauth";
		String scope = "read:user_id read:sis";
		
		String encoded_string = "response_type=" + response_type + "&redirect_uri=" + redirect_uri + "&client_id=" + client_id + "&scope=" + scope;
		String clever_login_url = URLEncoder.encode(encoded_string, "UTF-8");
		
		PrintWriter html_out = response.getWriter(  ); 
        	response.setContentType("text/html"); 
        	html_out.print("<h1>Login!<br/><br/>");
        	html_out.print("<a href='https://clever.com/oauth/authorize?" + clever_login_url);
        	html_out.print("'><img src='http://assets.clever.com/sign-in-with-clever/sign-in-with-clever-small.png'/></a></h1>");
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
