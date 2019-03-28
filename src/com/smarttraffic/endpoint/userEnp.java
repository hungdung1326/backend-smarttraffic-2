package com.smarttraffic.endpoint;

import com.smarttraffic.entity.CredenticalUser;
import com.smarttraffic.entity.User;
import com.smarttraffic.pojo.RESTUtil;
import com.smarttraffic.pojo.responseMsg;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.logging.Logger;

import com.googlecode.objectify.ObjectifyService;
import com.sun.org.apache.regexp.internal.RE;

import static com.googlecode.objectify.ObjectifyService.ofy;

public class userEnp extends HttpServlet {
    static {
        ObjectifyService.register(User.class);
        ObjectifyService.register(CredenticalUser.class);
    }

    private Logger logger = Logger.getLogger(userEnp.class.getSimpleName());

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        String param = RESTUtil.parseStringInputStream(req.getInputStream());
        if (param.length() == 0) {
            logger.warning("data is empty");
            resp.setStatus(404);
            responseMsg msg = new responseMsg(404, "Not found", "Lack of content for register");
            resp.getWriter().print(RESTUtil.gson.toJson(msg));
            return;
        }
        User user = RESTUtil.gson.fromJson(param, User.class);
        logger.info("user status: " + user.getStatus());


        String[] uriSplit = req.getRequestURI().split("/");
        String action = uriSplit[uriSplit.length - 1];

        switch (action) {
            case "login":
                login(req,resp, user);
                break;
            case "register":
                register(req, resp, user);
                break;
            default:
                resp.setStatus(400);
                resp.getWriter().println(RESTUtil.gson.toJson(new responseMsg(400,"Bad Request", "Bad Request")));
                break;

        }
    }

    private void login(HttpServletRequest req, HttpServletResponse resp,User user) throws IOException {
        User checkLogin = ofy().load().type(User.class).id(user.getEmail()).now();

        if (checkLogin == null){
            resp.setStatus(404);
            logger.warning("User not found");
            resp.getWriter().println(RESTUtil.gson.toJson(new responseMsg(404,"Not found", "User not found or has been deleted")));
            return;
        }

        if (!checkLogin.getPassword().equals(user.getPassword())){
            resp.setStatus(403);
            logger.warning("Forbidden, Incorrect password ");
            resp.getWriter().println(RESTUtil.gson.toJson(new responseMsg(403,"Forbidden", "Incorrect password")));
            return;
        }
        if (checkLogin.getStatus() != 1){
            resp.setStatus(403);
            logger.warning("Forbidden, user not access right");
            resp.getWriter().println(RESTUtil.gson.toJson(new responseMsg(403,"Forbidden", "Forbidden error ")));
            return;
        }
        logger.info("status user: " + user.getStatus());
        createCredential(req, resp, user);
        logger.info("login success");
    }

    private void createCredential(HttpServletRequest req, HttpServletResponse resp, User user) throws IOException {
        CredenticalUser credential = new CredenticalUser(user.getEmail());
        if (ofy().save().entity(credential).now() == null) {
            resp.setStatus(500);
            logger.warning("can not create credential");
            resp.getWriter().println(RESTUtil.gson.toJson(new responseMsg(500, "Internal error", "Login fail")));
            return;
        }
        resp.setStatus(200);
        resp.getWriter().print(RESTUtil.gson.toJson(credential));
    }


    private void register(HttpServletRequest req, HttpServletResponse resp, User user) throws IOException {

        User check = ofy().load().type(User.class).id(user.getEmail()).now();

        if (check != null){
            resp.setStatus(400);
            logger.warning("User has been exits");
            resp.getWriter().println(RESTUtil.gson.toJson(new responseMsg(400, "User already", "User has been exits")));
            return;
        }
        if (ofy().save().entity(user).now() == null) {
            resp.setStatus(500);
            logger.warning("save user error");
            resp.getWriter().println(RESTUtil.gson.toJson(new responseMsg(500,"Internal Error", "Internal Error. Please try again!")));
            return;
        }
        resp.setStatus(201);
        resp.getWriter().println(RESTUtil.gson.toJson(new responseMsg(201, "Register success", "Register success")));
    }
}
