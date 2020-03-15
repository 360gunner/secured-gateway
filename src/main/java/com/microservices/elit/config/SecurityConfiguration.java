package com.microservices.elit.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.microservices.elit.repository.UsersRepository;
import com.microservices.elit.service.CustomUserDetailsService;

@EnableGlobalMethodSecurity(prePostEnabled = true)
@EnableWebSecurity
@EnableJpaRepositories(basePackageClasses = UsersRepository.class)
@Configuration
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {


    @Autowired
    private CustomUserDetailsService userDetailsService;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {

        auth.userDetailsService(userDetailsService)
        .passwordEncoder(getPasswordEncoder());
    }

    
    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http.csrf().disable();
   http
        .authorizeRequests()
        .antMatchers("/mira/tt2").hasRole("ADMIN")
        .and()
        .authorizeRequests()
                .antMatchers("login").permitAll()
                .anyRequest().authenticated()
                .and()
                .formLogin().permitAll();
       /* .and().antMatcher("/mira/tt2")                               
        .authorizeRequests()
        .anyRequest().hasRole("ADMIN");*/
       // http.authorizeRequests().antMatchers("/mira/tt2").hasAnyRole("ADMIN");
        
        /*
        http
        .authorizeRequests()
            .antMatchers("/high_level_url_A/sub_level_1").hasRole('USER')
            .antMatchers("/high_level_url_A/sub_level_2").hasRole('USER2')
            .somethingElse() // for /high_level_url_A/**
            .antMatchers("/high_level_url_A/**").authenticated()
            .antMatchers("/high_level_url_B/sub_level_1").permitAll()
            .antMatchers("/high_level_url_B/sub_level_2").hasRole('USER3')
            .somethingElse() // for /high_level_url_B/**
            .antMatchers("/high_level_url_B/**").authenticated()
            .anyRequest().permitAll()  */
    }

    private PasswordEncoder getPasswordEncoder() {
        return new PasswordEncoder() {
            @Override
            public String encode(CharSequence charSequence) {
                return charSequence.toString();
            }

            @Override
            public boolean matches(CharSequence charSequence, String s) {
                return true;
            }
        };
    }
}
