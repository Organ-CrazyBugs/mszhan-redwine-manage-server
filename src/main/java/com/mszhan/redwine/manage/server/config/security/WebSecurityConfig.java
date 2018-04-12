package com.mszhan.redwine.manage.server.config.security;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.authentication.encoding.Md5PasswordEncoder;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;

/**
 * @Description:
 * @Author: iblilife@163.com
 * @Date: 23:19 2017/12/26
 */
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    public static final String loginPage = "/page/login";
    public static final String loginProcessingUrl = "/api/login";
    public static final String loginFailureUrl = loginPage + "?error";

    public static final String logoutUrl = "/api/logout";
    public static final String logoutSuccessUrl = loginPage + "?logout";

    @Autowired
    private UserDetailsServiceImpl userDetailsService;
    @Autowired
    private LoginSuccessHandler loginSuccessHandler;
    @Autowired
    private LoginFailureHandler loginFailureHandler;
    @Autowired
    private UserLoginFilter userLoginFilter;
    @Autowired
    private AjaxAuthenticationEntryPoint ajaxAuthenticationEntryPoint;

    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService);
        provider.setPasswordEncoder(new Md5PasswordEncoder());
        provider.setSaltSource(UserDetails::getUsername);   //设置密码盐值为账号名称
        return provider;
    }

    public void configure(AuthenticationManagerBuilder auth) {
        auth.authenticationProvider(daoAuthenticationProvider());
    }



    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .addFilterAfter(userLoginFilter, FilterSecurityInterceptor.class)
                .authorizeRequests().antMatchers("/ui-lib/**", "/page/login").permitAll()
                /*.antMatchers("/page/admin").hasRole("ADMIN")*/
                .anyRequest().fullyAuthenticated()

                .and()
                    .formLogin()
                    .loginPage(loginPage).loginProcessingUrl(loginProcessingUrl)
                    .defaultSuccessUrl("/")
                    .failureUrl(loginFailureUrl)
                    .failureHandler(loginFailureHandler)
                    .successHandler(loginSuccessHandler)
                    .permitAll()

                .and()
                    .logout()
                    .logoutUrl(logoutUrl)
                    .logoutSuccessUrl(logoutSuccessUrl)
                    .invalidateHttpSession(true)
                    .permitAll()

                .and()
                    .exceptionHandling()
                    .defaultAuthenticationEntryPointFor(ajaxAuthenticationEntryPoint, new AjaxRequestMatcher())
                ;
    }

}
