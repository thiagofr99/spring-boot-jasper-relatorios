package com.mballem.curso.jasper.spring;

import java.sql.Connection;
import java.sql.SQLException;

import javax.servlet.ServletRegistration;
import javax.sql.DataSource;

import net.sf.jasperreports.j2ee.servlets.ImageServlet;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ConfiguracaoGenerica {
	
	@Bean
	public Connection connection(DataSource dataSource) throws SQLException {
		return dataSource.getConnection();
	}

	@Bean
	public ServletRegistrationBean imageServlet(){
		ServletRegistrationBean servlet = new ServletRegistrationBean(
				new ImageServlet(),
				"/image/servlet/*"
		);
		servlet.setLoadOnStartup(1);
		return servlet;
	}
}
