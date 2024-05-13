package hello.login;

import java.util.List;

import javax.servlet.Filter;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import hello.login.web.argumentresolver.LoginMemberArgumentResolver;
import hello.login.web.filter.LogFilter;
import hello.login.web.filter.LoginCheckFilter;
import hello.login.web.interceptor.LogInterceptor;
import hello.login.web.interceptor.LoginCheckInterceptor;

@Configuration
public class WebConfig implements WebMvcConfigurer { // 스프링 인터셉터를 등록하기 위해 WebMvcConfigurer 인터페이스를 구현한다

	// 필터(e.g. hello.login.filter.LogFilter)를 사용하기 위해서 FilterRegistrationBean 객체를 스프링 빈으로 컨테이너에 등록한다.
	// FilterRegistrationBean 객체를 스프링 빈으로 등록해두면 스프링부트가 내장된 WAS(톰캣)을 실행할 때 필터를 함께 등록하고 실행한다.
	//@Bean
	public FilterRegistrationBean<Filter> filterRegistrationBean() {
		FilterRegistrationBean<Filter> filterRegistrationBean = new FilterRegistrationBean<>();

		// 필터 등록
		filterRegistrationBean.setFilter(new LogFilter());
		filterRegistrationBean.setOrder(1); // 필터 체인 중 순서 지정
		filterRegistrationBean.addUrlPatterns("/*"); // 필터를 적용할 URL 패턴 지정(/* : 모든 URL에 대해 필터 적용)

		return filterRegistrationBean;
	}

	// LoginCheckFilter 필터도 사용하기 위해 FilterRegistrationBean 객체를 스프링 컨테이너에 등록한다.
	//@Bean // 서블릿 필터가 아닌 스프링 인터셉터로 로그인 체크를하므로 필터는 사용하지 않기 위해 빈으로 등록하지 않는다.
	public FilterRegistrationBean<Filter> loginCheckFilter() {
		FilterRegistrationBean<Filter> filterRegistrationBean = new FilterRegistrationBean<>();

		// 필터 등록
		filterRegistrationBean.setFilter(new LoginCheckFilter());
		filterRegistrationBean.setOrder(2); // 필터 체인 중 순서 지정
		filterRegistrationBean.addUrlPatterns("/*"); // LoginCheckFilter 클래스에 필터를 적용하지 않을 경로는 whiteList로 구현해두었으므로 여기서는 /*로 기입한다.

		return filterRegistrationBean;
	}

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(new LogInterceptor())
			.order(1)
			.addPathPatterns("/**")
			.excludePathPatterns("/css/**", "/*.ico", "/error"); // 스프링 인터셉터를 적용하지 않을(호출하지 않을 경로) URL 패턴을 기입한다.

		final String[] excludePathPatterns = {"/", "/members/add", "/login", "/logout", "/css/**", "/*.ico", "/error"};
		registry.addInterceptor(new LoginCheckInterceptor())
			.order(2)
			.addPathPatterns("/**") // 모든 경로에 대해 로그인 여부 체크
			.excludePathPatterns(excludePathPatterns); // 스프링 인터셉터를 적용하지 않을(호출하지 않을 경로) URL 패턴을 추가로 기입
	}

	@Override
	public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
		// @Login 어노테이션으로 요청을 처리할 수 있는 LoginMemberArgumentResolver 클래스를 HandlerMethodArgumentResolver 목록에 등록한다.
		argumentResolvers.add(new LoginMemberArgumentResolver());
	}
}
