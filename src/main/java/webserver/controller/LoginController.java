package webserver.controller;

import db.SessionManager;
import model.annotation.Api;
import model.enumeration.HttpMethod;
import model.request.HttpRequest;
import model.response.HttpResponse;
import model.user.User;
import webserver.dao.UserDao;
import webserver.infra.ViewResolver;
import webserver.service.LoginService;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Optional;

import static constant.HeaderConstant.*;
import static constant.SessionUUID.*;
import static java.util.UUID.randomUUID;
import static utils.ResponseUtils.*;

public class LoginController extends ApiController {
    private static final LoginController instance;

    private final LoginService loginService;

    static {
        instance = new LoginController(new LoginService(new UserDao()));
    }

    private LoginController(LoginService loginService) {
        this.loginService = loginService;
    }

    public static LoginController getInstance() {
        return instance;
    }

    @Api(method = HttpMethod.GET, url = "/user/login.html")
    public void showLoginPage(HttpRequest request, HttpResponse response, DataOutputStream dos) throws IOException {
        ViewResolver.resolve(request, dos);
    }

    @Api(method = HttpMethod.POST, url = "/user/login")
    public void login(HttpRequest request, HttpResponse response, DataOutputStream dos) {
        Optional<User> loginUser = loginService.login(request);

        if (loginUser.isEmpty()) {
            response.setHeaderAttribute(LOCATION, "/user/login_failed.html");
//            response302Header(dos, response);
            return;
        }

        String UUID = randomUUID().toString();
        response.setHeaderAttribute(LOCATION, "/index.html");
        response.setHeaderAttribute(SET_COOKIE, "JSESSIONID=" + UUID + "; Path=/");
        SessionManager
                .findSession(USER_SESSION_UUID)
                .setAttribute(UUID, loginUser.get());

//        response302Header(dos, response);
    }

}
