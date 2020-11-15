package com.application.config.authorization;

import io.github.jhipster.config.JHipsterProperties;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.support.DelegatingMessageSource;
import org.springframework.http.HttpMethod;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.ldap.core.support.DirContextSource;
import org.springframework.security.access.intercept.RunAsImplAuthenticationProvider;
import org.springframework.security.authentication.AnonymousAuthenticationProvider;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.RememberMeAuthenticationProvider;
import org.springframework.security.authentication.TestingAuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.authentication.jaas.AuthorityGranter;
import org.springframework.security.authentication.jaas.DefaultJaasAuthenticationProvider;
import org.springframework.security.authentication.jaas.memory.InMemoryConfiguration;
import org.springframework.security.authentication.rcp.RemoteAuthenticationManagerImpl;
import org.springframework.security.authentication.rcp.RemoteAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.configurers.ExpressionUrlAuthorizationConfigurer;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.mapping.NullAuthoritiesMapper;
import org.springframework.security.core.userdetails.UserDetailsPasswordService;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.cache.NullUserCache;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.ldap.authentication.BindAuthenticator;
import org.springframework.security.ldap.authentication.LdapAuthenticationProvider;
import org.springframework.security.ldap.authentication.ad.ActiveDirectoryLdapAuthenticationProvider;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthorizationCodeAuthenticationProvider;
import org.springframework.security.oauth2.client.authentication.OAuth2LoginAuthenticationProvider;
import org.springframework.security.oauth2.client.endpoint.DefaultAuthorizationCodeTokenResponseClient;
import org.springframework.security.oauth2.client.oidc.authentication.OidcAuthorizationCodeAuthenticationProvider;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationProvider;
import org.springframework.security.oauth2.server.resource.authentication.OpaqueTokenAuthenticationProvider;
import org.springframework.security.oauth2.server.resource.introspection.NimbusOpaqueTokenIntrospector;
import org.springframework.security.web.access.AccessDeniedHandlerImpl;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationProvider;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedGrantedAuthoritiesUserDetailsService;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.TokenBasedRememberMeServices;
import org.springframework.security.web.csrf.CsrfAuthenticationStrategy;
import org.springframework.security.web.csrf.HttpSessionCsrfTokenRepository;
import org.springframework.security.web.header.writers.FeaturePolicyHeaderWriter;
import org.springframework.security.web.header.writers.ReferrerPolicyHeaderWriter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.zalando.problem.spring.web.advice.security.SecurityProblemSupport;

import javax.security.auth.login.AppConfigurationEntry;
import javax.sql.DataSource;
import java.security.Principal;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true, jsr250Enabled = true)
@AllArgsConstructor
@Import(SecurityProblemSupport.class)
public class SpringSecurityConfiguration extends WebSecurityConfigurerAdapter {

    private final JHipsterProperties jHipsterProperties;

    private final SecurityProblemSupport problemSupport;

    private final UserDetailsService userDetailsService;

    private final UserDetailsPasswordService userDetailsPasswordService;

    private final DefaultPreAuthenticationChecks defaultPreAuthenticationChecks;

    private final DefaultPostAuthenticationChecks defaultPostAuthenticationChecks;

    private final DynamicSecurityFilter dynamicSecurityFilter;

    private final IgnoreUrlsConfig ignoreUrlsConfig;

    private final DataSource dataSource;

    private final JdbcTemplate jdbcTemplate;

    //private final AuthenticationManagerBuilder builder;// 这种方式注入的 build 来自 AuthenticationConfiguration 下的Bean 注入

    /*@PostConstruct
    public void init() {
        builder.authenticationProvider(daoAuthenticationProvider());
        builder.authenticationProvider(anonymousAuthenticationProvider());
        builder.authenticationProvider(rememberMeAuthenticationProvider());
        builder.authenticationProvider(testingAuthenticationProvider());
    }*/

    @Override
    public void init(WebSecurity web) throws Exception {
        // 必须存在,也就是WebSecurity 属性 securityFilterChainBuilders 必须存在
        // 原理：将 HttpSecurity 加入到 WebSecurity 的 securityFilterChainBuilders 属性中,同时添加 HttpSecurity 所有的Configurer
        // 最后将 FilterSecurityInterceptor 加入到 WebSecurity 的拦截器中
        super.init(web);
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web
                .ignoring()//允许跨域请求的OPTIONS请求
                .antMatchers(HttpMethod.OPTIONS, "/**")
                .antMatchers("/api/admin-login")
                .antMatchers("/swagger-**", "/doc.html", "/api-docs**");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        ExpressionUrlAuthorizationConfigurer<HttpSecurity>.ExpressionInterceptUrlRegistry registry = http.authorizeRequests();

        // 白名单放行
        for (String url : ignoreUrlsConfig.getUrls()) {
            registry.antMatchers(url).permitAll();
        }

        registry
                .and()
                .csrf()
                .csrfTokenRepository(new HttpSessionCsrfTokenRepository())// CrsfFilter 将会使用的HttpSessionCsrfTokenRepository
                .sessionAuthenticationStrategy(new CsrfAuthenticationStrategy(new HttpSessionCsrfTokenRepository()))// 登录成功之后的策略,在SuccessHandle 处理之前调用
                .ignoringRequestMatchers(new AntPathRequestMatcher("/api/users/**", HttpMethod.POST.name()))// 定义哪些不是跨站的
                .ignoringAntMatchers("/api/user/**")
                .requireCsrfProtectionMatcher(new AntPathRequestMatcher("/api/user/**"))// 定义哪些是必须跨站的
                .and()
                //.disable()// 原理: 从 HttpSecurity中删除当前Configurer
                //.and()
                .cors().configurationSource(request -> jHipsterProperties.getCors())// 使用配置文件提供的
                .and()
                .exceptionHandling().accessDeniedHandler(new AccessDeniedHandlerImpl())// 默认异常处理
                //.authenticationEntryPoint(new Http403ForbiddenEntryPoint())// 异常处理后的跳转处理
                .and()
                .headers()
                //.addHeaderWriter(new ContentSecurityPolicyHeaderWriter("default-src 'self'"))// CSP为web应用程序提供了一种机制来减轻内容注入漏洞
                .addHeaderWriter(new FeaturePolicyHeaderWriter("payment"))// 限制浏览器的API功能
                .addHeaderWriter(new ReferrerPolicyHeaderWriter(ReferrerPolicyHeaderWriter.ReferrerPolicy.ORIGIN))// 指定referrer 显示的策略
                .httpStrictTransportSecurity()// 抵御SSL剥离攻击
                .includeSubDomains(true)
                .maxAgeInSeconds(31536000)
                .preload(true)// 如果是真的，前面将包括在HSTS Head。默认是真的
                .and()
                .and()
                .httpBasic()// 添加HTTP 基本表单验证,如果不开启,那么就不会进行表单验证,说白了就是 在头部添加 Basic验证,只不过是输入用户名和密码进行验证
                .and()
                .formLogin().authenticationDetailsSource(new WebAuthenticationDetailsSource())// 设置默认的Detail,在filter中会执行此build方法
                .and()
                .authorizeRequests()
                .antMatchers("/api/**").authenticated()
                .and()
                .rememberMe()
                .tokenValiditySeconds(31536000)// 有效期10秒,必须设置session 失效时间,否则此处session 中保存有 SecurityContext 也就是Token,不会进入此RememberMe过滤器
                .key(jHipsterProperties.getSecurity().getRememberMe().getKey())
                .rememberMeCookieName("remember-me")
                .alwaysRemember(false)
                .useSecureCookie(false)// 此处设置为false 才能让 cookie 传递进来
                .rememberMeParameter("remember-me")
                .userDetailsService(userDetailsService)
                .rememberMeServices(tokenBasedRememberMeServices()) //不要设置,否则使用不了默认的配置,最多使用tokenRepository 进行可选配置
                //.tokenRepository(jdbcTokenRepositoryImpl()) // 默认情况下 TokenBasedRememberMeServices,PersistentTokenBasedRememberMeServices 二选一
                .and()
                .sessionManagement()
                .enableSessionUrlRewriting(false)
                .maximumSessions(1)// 如果配置此数据,则会同时创建一个 ConcurrentSessionFilter 过滤器
                .and()
                .and();

        //有动态权限配置时添加动态权限校验过滤器
        if (userDetailsService != null) {
            registry.and().addFilterBefore(dynamicSecurityFilter, FilterSecurityInterceptor.class);
        }

    }


    private final HashSet<String> allowedMethods = new HashSet<>(
            Arrays.asList("GET", "HEAD", "TRACE", "OPTIONS"));

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        /**
         * 这种方式是对源 AuthenticationManager 实际上也就是对 内置属性 localConfigureAuthenticationBldr 的配置
         * 区别在于 localConfigureAuthenticationBldr将会被转换为 ProviderManager,并且ProviderManager将会作为
         * 内置属性 authenticationBuilder 的父集.其中 ProviderManager 是对 Provider 的包装
         * 此种方式产生的是 AuthenticationManagerBuilder
         *
         * 如果没有使用重写此方法,那么将会使用AuthenticationConfiguration 的默认配置
         */
        auth
                .userDetailsService(userDetailsService)// 对内置的 DaoAuthenticationProvider 进行配置userDetailsService和默认的defaultPasswordEncoder
                .userDetailsPasswordManager(userDetailsPasswordService)
                .passwordEncoder(passwordEncoder());
        auth.authenticationProvider(anonymousAuthenticationProvider());
        auth.authenticationProvider(rememberMeAuthenticationProvider());
        auth.authenticationProvider(testingAuthenticationProvider());
        auth.authenticationProvider(daoAuthenticationProvider());
        auth.authenticationProvider(remoteAuthenticationProvider());
        auth.authenticationProvider(jwtAuthenticationProvider());
        auth.authenticationProvider(activeDirectoryLdapAuthenticationProvider());
        auth.authenticationProvider(ldapAuthenticationProvider());
        auth.authenticationProvider(preAuthenticatedAuthenticationProvider());
        auth.authenticationProvider(runAsImplAuthenticationProvider());
        auth.authenticationProvider(opaqueTokenAuthenticationProvider());
        auth.authenticationProvider(defaultJaasAuthenticationProvider());
        //auth.authenticationProvider(jaasAuthenticationProvider());
        auth.authenticationProvider(oAuth2AuthorizationCodeAuthenticationProvider());
        auth.authenticationProvider(oAuth2LoginAuthenticationProvider());
        auth.authenticationProvider(oidcAuthorizationCodeAuthenticationProvider());
    }

    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        // 这种方式是对源 AuthenticationManager 实际上也就是对 内置属性 authenticationBuilder 包装
        // 此种方式产生的是 AuthenticationManagerBuilder 下的 AuthenticationManager
        return super.authenticationManagerBean();
    }

    @Bean
    public JdbcTokenRepositoryImpl jdbcTokenRepositoryImpl() {
        JdbcTokenRepositoryImpl jdbcTokenRepository = new JdbcTokenRepositoryImpl();
        jdbcTokenRepository.setDataSource(dataSource);
        jdbcTokenRepository.setJdbcTemplate(jdbcTemplate);
        // 第一次启动项目的时候设置为true,后面再设置为false
        jdbcTokenRepository.setCreateTableOnStartup(false);
        //jdbcTokenRepository.afterPropertiesSet();
        return jdbcTokenRepository;
    }

    @Bean
    public TokenBasedRememberMeServices tokenBasedRememberMeServices() {
        TokenBasedRememberMeServices services = new TokenBasedRememberMeServices(jHipsterProperties.getSecurity().getRememberMe().getKey(), userDetailsService);
        services.setUseSecureCookie(false);
        services.setTokenValiditySeconds(10000);
        services.setParameter("remember-me");
        services.setAuthoritiesMapper(new NullAuthoritiesMapper());
        services.setAlwaysRemember(false);
        services.setAuthenticationDetailsSource(new WebAuthenticationDetailsSource());
        services.setCookieName("system");
        services.afterPropertiesSet();
        return services;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider() {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        // 用户认证
        daoAuthenticationProvider.setUserDetailsService(userDetailsService);
        // 用户修改
        daoAuthenticationProvider.setUserDetailsPasswordService(userDetailsPasswordService);
        // 权限映射
        daoAuthenticationProvider.setAuthoritiesMapper(new NullAuthoritiesMapper());
        // 密码加密
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
        // 日志消息源
        daoAuthenticationProvider.setMessageSource(new DelegatingMessageSource());
        // 是否影藏用户名密码不存在异常
        daoAuthenticationProvider.setHideUserNotFoundExceptions(false);
        // 用户名是否转为字符串
        daoAuthenticationProvider.setForcePrincipalAsString(false);
        // 认证之前校验权限
        daoAuthenticationProvider.setPreAuthenticationChecks(defaultPreAuthenticationChecks);
        // 授权之后校验权限
        daoAuthenticationProvider.setPostAuthenticationChecks(defaultPostAuthenticationChecks);
        // 用户缓存(有了缓存,只需要验证用户密码正确否即可)
        daoAuthenticationProvider.setUserCache(new NullUserCache());
        return daoAuthenticationProvider;
    }

    @Bean
    public OidcAuthorizationCodeAuthenticationProvider oidcAuthorizationCodeAuthenticationProvider() {
        // 尚未测试
        return new OidcAuthorizationCodeAuthenticationProvider(new DefaultAuthorizationCodeTokenResponseClient(), new OidcUserService());
    }

    @Bean
    public OAuth2LoginAuthenticationProvider oAuth2LoginAuthenticationProvider() {
        // 尚未测试
        return new OAuth2LoginAuthenticationProvider(new DefaultAuthorizationCodeTokenResponseClient(), new DefaultOAuth2UserService());
    }

    @Bean
    public OAuth2AuthorizationCodeAuthenticationProvider oAuth2AuthorizationCodeAuthenticationProvider() {
        // 尚未测试
        return new OAuth2AuthorizationCodeAuthenticationProvider(new DefaultAuthorizationCodeTokenResponseClient());
    }

    /*@Bean
    public JaasAuthenticationProvider jaasAuthenticationProvider() {
        // 尚未测试
        JaasAuthenticationProvider provider = new JaasAuthenticationProvider();
        provider.setRefreshConfigurationOnStartup(false);
        provider.setLoginConfig(new ClassPathResource("application.yaml"));
        return provider;
    }*/

    @Bean
    public DefaultJaasAuthenticationProvider defaultJaasAuthenticationProvider() {
        // 尚未测试
        DefaultJaasAuthenticationProvider provider = new DefaultJaasAuthenticationProvider();
        provider.setAuthorityGranters(new AuthorityGranter[]{new AuthorityGranter() {
            @Override
            public Set<String> grant(Principal principal) {
                return null;
            }
        }});
        provider.setConfiguration(new InMemoryConfiguration(new AppConfigurationEntry[]{}));
        return provider;
    }

    @Bean
    public OpaqueTokenAuthenticationProvider opaqueTokenAuthenticationProvider() {
        // 尚未测试
        return new OpaqueTokenAuthenticationProvider(new NimbusOpaqueTokenIntrospector("uri", "clientId", "clientSecret"));
    }

    @Bean
    public RunAsImplAuthenticationProvider runAsImplAuthenticationProvider() {
        // 尚未测试
        RunAsImplAuthenticationProvider provider = new RunAsImplAuthenticationProvider();
        provider.setKey("123456");
        return provider;
    }

    @Bean
    public PreAuthenticatedAuthenticationProvider preAuthenticatedAuthenticationProvider() {
        // 尚未测试
        PreAuthenticatedAuthenticationProvider provider = new PreAuthenticatedAuthenticationProvider();
        provider.setThrowExceptionWhenTokenRejected(true);
        provider.setPreAuthenticatedUserDetailsService(new PreAuthenticatedGrantedAuthoritiesUserDetailsService());
        provider.setOrder(1);
        return provider;
    }

    @Bean
    public LdapAuthenticationProvider ldapAuthenticationProvider() {
        // 尚未测试
        LdapAuthenticationProvider ldapAuthenticationProvider = new LdapAuthenticationProvider(new BindAuthenticator(new DirContextSource()));
        return ldapAuthenticationProvider;
    }

    @Bean
    public ActiveDirectoryLdapAuthenticationProvider activeDirectoryLdapAuthenticationProvider() {
        // 尚未测试
        return new ActiveDirectoryLdapAuthenticationProvider("mega-insight.com", "ldap://10.252.78.235:389");
    }

    @Bean
    public RemoteAuthenticationProvider remoteAuthenticationProvider() {
        RemoteAuthenticationProvider remoteAuthenticationProvider = new RemoteAuthenticationProvider();
        RemoteAuthenticationManagerImpl remoteAuthenticationManager = new RemoteAuthenticationManagerImpl();
        // 尚未测试
        remoteAuthenticationManager.setAuthenticationManager(new AuthenticationManager() {
            @Override
            public Authentication authenticate(Authentication authentication) throws AuthenticationException {
                return null;
            }
        });
        remoteAuthenticationProvider.setRemoteAuthenticationManager(remoteAuthenticationManager);
        return remoteAuthenticationProvider;
    }

    @Bean
    public JwtAuthenticationProvider jwtAuthenticationProvider() {
        // 尚未测试
        JwtAuthenticationProvider jwtAuthenticationProvider = new JwtAuthenticationProvider(new JwtDecoder() {
            @Override
            public Jwt decode(String token) throws JwtException {
                return null;
            }
        });

        return jwtAuthenticationProvider;
    }

    @Bean
    public AnonymousAuthenticationProvider anonymousAuthenticationProvider() {
        return new AnonymousAuthenticationProvider(UUID.randomUUID().toString());
    }

    @Bean
    public RememberMeAuthenticationProvider rememberMeAuthenticationProvider() {
        // 尚未测试
        return new RememberMeAuthenticationProvider(UUID.randomUUID().toString());
    }

    @Bean
    public TestingAuthenticationProvider testingAuthenticationProvider() {
        return new TestingAuthenticationProvider();
    }

    /**
     * 这两种方式区别
     * 原理：方法调用后会获取本地配置的 AuthenticationManagerBuilder 和全局配置的 AuthenticationManagerBuilder默认 UserDetailsService
     * 所以通常情况我们会重写 这两种方式 也就是改成自定义的 UserDetailsService 返回
     * 使用场景： 前者会在 createSharedObjects() 方法中被调用,后者则 没有在框架中内置使用
     * 可能场景：根据配置文件变量来动态创建不同的Service
     *
     * @return UserDetailsService
     */
    /*@Override
    protected UserDetailsService userDetailsService() {
        return super.userDetailsService();
    }

    @Override
    public UserDetailsService userDetailsServiceBean() throws Exception {
        return super.userDetailsServiceBean();
    }*/
}


