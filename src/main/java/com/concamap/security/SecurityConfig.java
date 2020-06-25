package com.concamap.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.security.web.authentication.logout.SimpleUrlLogoutSuccessHandler;

import java.util.Properties;

@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    public void configure(WebSecurity web) throws Exception {
        super.configure(web);
    }

    private final UserDetailsService userDetailsService;

    public static final String CHECKED_USER_NAME = "@userSecurity.checkUsername(authentication, #username)";

    @Autowired
    public SecurityConfig(@Qualifier("userDetailServiceImp") UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        //authorize requests
        http.authorizeRequests().antMatchers("/",
                                                        "/login",
                                                        "signup",
                                                        "users/{username}",
                                                        "/categories/{anchor-name}/posts",
                                                        "/date/{year}",
                                                        "/date/{year}/{month}",
                                                        "/search",
                                                        "/posts/{anchor-name}").
                                                        permitAll();

        http.authorizeRequests().antMatchers("/users/{username}/profile",
                                                        "/users/**/profile",
                                                        "/users/{username}/posts/create",
                                                        "/users/posts/create",
                                                        "/users/{username}/posts/{anchor-name}/edit",
                                                        "/users/posts/edit",
                                                        "/users/{id}/posts/delete").
                                                        access(CHECKED_USER_NAME);
        http.authorizeRequests().antMatchers("/admin/**").access("hasRole('ADMIN')");

        //login
        http.formLogin().
                loginPage("/login").
                usernameParameter("username").
                passwordParameter("password").
                and().
                exceptionHandling().
                accessDeniedPage("/403");

        //logout
        http.logout().
                logoutUrl("/logout").
                logoutSuccessHandler(new SimpleUrlLogoutSuccessHandler());

        http.csrf().disable();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(NoOpPasswordEncoder.getInstance());
    }

    @Bean
    public JavaMailSender javaMailSender(){
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost("smtp.gmail.com");
        mailSender.setPort(587);

        mailSender.setUsername("sharksquadteam420@gmail.com");
        mailSender.setPassword("Anhnam420");

        Properties props = mailSender.getJavaMailProperties();
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.debug", "true");

        return mailSender;
    }
}
