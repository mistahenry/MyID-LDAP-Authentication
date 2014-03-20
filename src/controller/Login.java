package controller;

import helper.SSLConnection;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import model.User;

/**
 * Servlet implementation class Login
 */
@WebServlet("/Login")
public class Login extends HttpServlet {
	private static final long serialVersionUID = 1L;

    /**
     * Default constructor. 
     */
    public Login() {
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		ServletContext ctx = this.getServletContext();
		HttpSession session = request.getSession();
		if(request.getParameter("myid") != null && request.getParameter("password") != null){
			String username = request.getParameter("myid");
			String password = request.getParameter("password");
			SSLConnection connection = new SSLConnection(username, password);
			connection.setConn(connection.connect());
	        System.out.println((connection.getConn().isBound()) ?
	        		"Authenticated to the server ( ssl )\n":
	        		"Not authenticated to the server");
	        User you = connection.getUser(username);
			request.setAttribute("User", you);
			RequestDispatcher dispatcher = ctx.getRequestDispatcher("/user.jsp");
			dispatcher.forward(request, response);			
		}

	}

}
