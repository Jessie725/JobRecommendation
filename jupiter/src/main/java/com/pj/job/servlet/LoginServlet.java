package com.pj.job.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pj.job.db.MySQLConnection;
import com.pj.job.entity.LoginRequestBody;
import com.pj.job.entity.LoginResponseBody;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet(name = "LoginServlet", urlPatterns = {"/login"})
public class LoginServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ObjectMapper mapper = new ObjectMapper();
        LoginRequestBody body = mapper.readValue(request.getReader(), LoginRequestBody.class);
        MySQLConnection connection = new MySQLConnection();

        LoginResponseBody loginResponseBody;
        if (connection.verifyLogin(body.userId, body.password)) {
            HttpSession session = request.getSession();
            session.setAttribute("user_id", body.userId);
            loginResponseBody = new LoginResponseBody("OK", body.userId, connection.getFullname(body.userId));
        } else {
            loginResponseBody = new LoginResponseBody("Login failed, user id and passcode do not exist.", null, null);
        }

        connection.close();
        response.setContentType("application/json");
        mapper.writeValue(response.getWriter(), loginResponseBody);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ObjectMapper mapper = new ObjectMapper();
        HttpSession session = request.getSession(false); // 如果已有session，则不要创建新的，如果没有，不要创建

        LoginResponseBody loginResponseBody;
        if (session != null) { // check 有没有login (只有login的时候会创建session)
            MySQLConnection connection = new MySQLConnection();
            String userId = session.getAttribute("user_id").toString();
            loginResponseBody = new LoginResponseBody("OK", userId, connection.getFullname(userId));
            connection.close();
        } else {
            loginResponseBody = new LoginResponseBody("Invalid Session.", null, null);
            response.setStatus(403);

        }
        response.setContentType("application/json");
        mapper.writeValue(response.getWriter(), loginResponseBody);
    }
}
