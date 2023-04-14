package top.shlande.clouddisk.config;

import org.jasig.cas.client.authentication.AuthenticationFilter;
import org.jasig.cas.client.session.SingleSignOutFilter;
import org.jasig.cas.client.session.SingleSignOutHttpSessionListener;
import org.jasig.cas.client.util.HttpServletRequestWrapperFilter;
import org.jasig.cas.client.validation.Cas20ProxyReceivingTicketValidationFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletListenerRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class SSOConfiguration {
    @Bean
    public ServletListenerRegistrationBean<SingleSignOutHttpSessionListener> listener() {
        var bean = new ServletListenerRegistrationBean<SingleSignOutHttpSessionListener>();
        bean.setListener(new SingleSignOutHttpSessionListener());
        return bean;
    }

    @Bean
    public FilterRegistrationBean<SingleSignOutFilter> SingleSignOutFilter() {
        var filter = new FilterRegistrationBean<SingleSignOutFilter>();
        filter.setFilter(new SingleSignOutFilter());
        filter.setUrlPatterns(List.of("/*"));
        return filter;
    }

    @Bean
    public FilterRegistrationBean<AuthenticationFilter> AuthenticationFilter() {
        var bean = new FilterRegistrationBean<AuthenticationFilter>();
        var authFilter = new AuthenticationFilter();

        // <!--这里是应用地址，注意是域名:端口或者ip:端口 -->
        bean.addInitParameter("serverName","https://dyxt.xidian.edu.cn:443");
        // <!--这里的server是CAS服务端的登录地址,login为固定值 -->
        bean.addInitParameter("casServerLoginUrl","https://ids.xidian.edu.cn/authserver/login");

        bean.setName("CASFilter");
        bean.setFilter(authFilter);
        bean.setUrlPatterns(List.of("/*"));
        return bean;
    }

    @Bean
    public FilterRegistrationBean<Cas20ProxyReceivingTicketValidationFilter> ticketValidationFilter() {
        var bean = new FilterRegistrationBean<Cas20ProxyReceivingTicketValidationFilter>();

        bean.addInitParameter("casServerUrlPrefix","https://ids.xidian.edu.cn/authserver");
        // 这里的server是CAS服务端的地址,这里不要加login
        bean.addInitParameter("encoding", "UTF-8");
        // 这里是应用地址，注意是域名:端口或者ip:端口
        bean.addInitParameter("serverName","https://dyxt.xidian.edu.cn:443");

        var filter = new Cas20ProxyReceivingTicketValidationFilter();
        filter.setServerName("https://dyxt.xidian.edu.cn:443");

        bean.setFilter(filter);
        bean.setName("CAS Validation Filter");
        bean.setUrlPatterns(List.of("/*"));
        return bean;
    }

    @Bean
    public FilterRegistrationBean<HttpServletRequestWrapperFilter> wrapperFilter() {
        var filter = new HttpServletRequestWrapperFilter();
        var bean = new FilterRegistrationBean<HttpServletRequestWrapperFilter>();
        bean.setFilter(filter);
        bean.setName("AS HttpServletRequest Wrapper Filter");
        bean.setUrlPatterns(List.of("/*"));
        return bean;
    }
}
