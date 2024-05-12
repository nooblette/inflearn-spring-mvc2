package hello.login;

import javax.servlet.Filter;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import hello.login.web.filter.LogFilter;
import hello.login.web.filter.LoginCheckFilter;

@Configuration
public class WebConfig {

	// 필터(e.g. hello.login.filter.LogFilter)를 사용하기 위해서 FilterRegistrationBean 객체를 스프링 빈으로 컨테이너에 등록한다.
	// FilterRegistrationBean 객체를 스프링 빈으로 등록해두면 스프링부트가 내장된 WAS(톰캣)을 실행할 때 필터를 함께 등록하고 실행한다.
	@Bean
	public FilterRegistrationBean<Filter> filterRegistrationBean() {
		FilterRegistrationBean<Filter> filterRegistrationBean = new FilterRegistrationBean<>();

		// 필터 등록
		filterRegistrationBean.setFilter(new LogFilter());
		filterRegistrationBean.setOrder(1); // 필터 체인 중 순서 지정
		filterRegistrationBean.addUrlPatterns("/*"); // 필터를 적용할 URL 패턴 지정(/* : 모든 URL에 대해 필터 적용)

		return filterRegistrationBean;
	}

	// LoginCheckFilter 필터도 사용하기 위해 FilterRegistrationBean 객체를 스프링 컨테이너에 등록한다.
	@Bean
	public FilterRegistrationBean<Filter> loginCheckFilter() {
		FilterRegistrationBean<Filter> filterRegistrationBean = new FilterRegistrationBean<>();

		// 필터 등록
		filterRegistrationBean.setFilter(new LoginCheckFilter());
		filterRegistrationBean.setOrder(2); // 필터 체인 중 순서 지정
		filterRegistrationBean.addUrlPatterns("/*"); // LoginCheckFilter 클래스에 필터를 적용하지 않을 경로는 whiteList로 구현해두었으므로 여기서는 /*로 기입한다.

		return filterRegistrationBean;
	}
}
