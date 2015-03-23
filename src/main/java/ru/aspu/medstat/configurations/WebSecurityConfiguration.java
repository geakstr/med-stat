package ru.aspu.medstat.configurations;

//@Configuration
//@EnableWebMvcSecurity
//@Order(Ordered.HIGHEST_PRECEDENCE)
public class WebSecurityConfiguration /*extends WebSecurityConfigurerAdapter*/ {

//    @Override
//    protected void configure(HttpSecurity http) throws Exception {
//        RequestMatcher csrfRequestMatcher = new RequestMatcher() {
//            private AntPathRequestMatcher[] requestMatchers = {
//                    new AntPathRequestMatcher("/api/**")
//            };
//
//            @Override
//            public boolean matches(HttpServletRequest request) {
//                for (AntPathRequestMatcher rm : requestMatchers) {
//                    if (rm.matches(request)) { return false; }
//                }
//                return true;
//            }
//        };
//
//        http
//                .authorizeRequests()
//                .antMatchers("/", "/api/**")
//                .permitAll()
//                .anyRequest()
//                .authenticated()
//                .and()
//                .formLogin()
//                .loginPage("/auth/login")
//                .permitAll()
//                .and()
//                .logout()
//                .logoutUrl("/auth/logout")
//                .permitAll()
//                .and();
//
//
//        http.csrf().disable();
//    }

//    @Autowired
//    public void configureGlobal(@SuppressWarnings("SpringJavaAutowiringInspection") AuthenticationManagerBuilder auth) throws Exception {
//        auth
//                .inMemoryAuthentication()
//                .withUser("user").password("pass").roles("USER");
//    }
}