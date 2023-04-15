package top.shlande.clouddisk.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import top.shlande.clouddisk.user.DenyException;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public class Utils {
    public static String getUserId(HttpServletRequest http) {
        var session = http.getSession(false);
        if (session == null) {
            throw new DenyException("", "not login");
        }
        var userId = http.getRemoteUser();
        if (userId == null) {
            userId = (String) session.getAttribute("userId");
        }
        return userId;
    }
}
