package com.pj.job.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pj.job.entity.Item;
import com.pj.job.entity.ResultResponse;
import com.pj.job.recommendation.Recommendation;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

@WebServlet(name = "RecommendationServlet", urlPatterns = {"/recommendation"})
public class RecommendationServlet extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        ObjectMapper mapper = new ObjectMapper();
        HttpSession session = request.getSession(false);
        if (session == null) {
            response.setStatus(403);
            mapper.writeValue(response.getWriter(), new ResultResponse("Session Invalid"));
            return;
        }

        String userid = request.getParameter("user_id");
        double lat = Double.parseDouble(request.getParameter("lat"));
        double lon = Double.parseDouble(request.getParameter("lon"));

        Recommendation recommendation = new Recommendation();
        List<Item> items = recommendation.recommendation(userid, lat, lon);
        mapper.writeValue(response.getWriter(), items);
    }
}
