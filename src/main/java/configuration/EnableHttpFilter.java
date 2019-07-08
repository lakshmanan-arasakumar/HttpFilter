package configuration;

import filter.ConnectedXInterceptor;
import filter.MdcTaskDecorator;
import filter.RequestFilter;
import org.springframework.context.annotation.Import;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Import({RequestFilter.class, ConnectedXInterceptor.class, MdcTaskDecorator.class})
public @interface EnableHttpFilter {
}
