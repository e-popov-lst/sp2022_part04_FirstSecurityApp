package ru.alishev.springcourse.FirstSecurityApp.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import ru.alishev.springcourse.FirstSecurityApp.services.PersonDetailsService;

@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    // WebSecurityConfigurerAdapter is depricated


    private final PersonDetailsService personDetailsService;

    @Autowired
    public SecurityConfig(PersonDetailsService personDetailsService) {
        this.personDetailsService = personDetailsService;
    }

    // Для своей формы авторизации надо переопределить этот метод
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // Конфигурируем сам Spring Security
        // Конфигурируем авторизацию
        http.csrf().disable()  // Отключаем защиту от межсайтовой подделки запросов
                .authorizeRequests()
                .antMatchers("/auth/login", "/auth/registration", "/error").permitAll()
                .anyRequest().authenticated()
                .and()
                .formLogin().loginPage("/auth/login")
                .loginProcessingUrl("/process_login")
                .defaultSuccessUrl("/hello", true)
                .failureUrl("/auth/login?error");
    }


    // Настраивает аутентификацию
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(personDetailsService);
    }

    // указываем каким алгоритм будем шифровать пароль
    @Bean
    public PasswordEncoder getPasswordEncoder() {
        return NoOpPasswordEncoder.getInstance();  // никак не шифруем (пока)
    }
}
