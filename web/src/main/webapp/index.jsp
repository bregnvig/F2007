<%@page import="org.springframework.web.context.support.WebApplicationContextUtils"%>
<%@page import="org.springframework.context.ApplicationContext"%>
<html>
<body>
<h2>Hello World! Super</h2>

<%
  ApplicationContext context = WebApplicationContextUtils.getWebApplicationContext(application);
  for (String name : context.getBeanDefinitionNames()) {
	  out.write(name + "\n");
  }
%>
</body>
</html>
