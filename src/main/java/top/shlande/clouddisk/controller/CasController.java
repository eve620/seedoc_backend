package top.shlande.clouddisk.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apereo.cas.client.validation.Assertion;
import org.apereo.cas.client.validation.Cas20ServiceTicketValidator;
import org.apereo.cas.client.validation.TicketValidationException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;

@RequestMapping("/login")
public class CasController {
    @Value("${cas.server-url-prefix}")
    private String casServerUrlPrefix;

    @Value("${cas.client-host-url}")
    private String clientHostUrl;

    @GetMapping("/cas")
    public ResponseEntity<String> casLogin(@RequestParam("ticket") String ticket, HttpServletRequest request, HttpServletResponse response) throws IOException {
        Cas20ServiceTicketValidator validator = new Cas20ServiceTicketValidator(casServerUrlPrefix);
        try {
            Assertion assertion = validator.validate(ticket, clientHostUrl);
            // 你可以从assertion对象中获取用户信息
            String username = assertion.getPrincipal().getName();
            // ....
            // 根据你的业务逻辑处理这个username，例如将它保存在session中
            var session = request.getSession(true);
            session.setAttribute("userId", username);
            response.sendRedirect("/file/");
            // ....
            return ResponseEntity.ok("登录成功");
        } catch (TicketValidationException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("登录失败");
        }
    }
}
