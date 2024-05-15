package hello.exception;

import java.util.List;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import hello.exception.filter.LogFilter;
import hello.exception.interceptor.LogInterceptor;
import hello.exception.resolver.DummyExceptionResolver;
import hello.exception.resolver.MyHandlerExceptionResolver;
import jakarta.servlet.DispatcherType;
import jakarta.servlet.Filter;

@Configuration
public class WebConfig implements WebMvcConfigurer {
	/***
	 * 서블릿 필터 등록
	 * @return FilterRegistrationBean
	 */
	//@Bean
	public FilterRegistrationBean<Filter> logFilter() {
		FilterRegistrationBean<Filter> filterRegistrationBean = new FilterRegistrationBean<>();
		filterRegistrationBean.setFilter(new LogFilter());
		filterRegistrationBean.addUrlPatterns("/*");
		filterRegistrationBean.setOrder(1);

		// 이 LogFilter는 DispatcherType이 REQUEST와 ERROR인 경우에만 동작하는 필터임을 지정할 수 있다.
		filterRegistrationBean.setDispatcherTypes(DispatcherType.REQUEST, DispatcherType.ERROR);

		return filterRegistrationBean;
	}

	/***
	 * 스프링 인터셉터 등록
	 */
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		// 스프링 인터셉터를 사용하는 경우 DispatcherType으로 적용 여부를 구분하지 않고 excludePathPatterns을 활용한다.
		registry.addInterceptor(new LogInterceptor())
			.order(1)
			.addPathPatterns("/**")
			.excludePathPatterns("/css/**", "/*.ico", "/error", "/error-page/**"); // /error-page/** 요청에 대해서는 LogInterceptor를 적용하지 않는다.
	}

	/***
	 * HandlerExceptionResolver 등록
	 */
	@Override
	public void extendHandlerExceptionResolvers(List<HandlerExceptionResolver> exceptionResolvers) {
		exceptionResolvers.add(new DummyExceptionResolver());
		exceptionResolvers.add(new MyHandlerExceptionResolver());
	}
}
