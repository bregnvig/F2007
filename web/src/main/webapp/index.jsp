<%@page import="org.springframework.web.context.support.WebApplicationContextUtils"%>
<%@page import="org.springframework.context.ApplicationContext"%>
<%@page import="java.util.Date"%>
<html>
<body>
<h2>Hello World! Super <%= new Date().toString()  %></h2>

<%
  ApplicationContext context = WebApplicationContextUtils.getWebApplicationContext(application);
  for (String name : context.getBeanDefinitionNames()) {
	  out.write(name + "\n");
  }
  out.write("<br/>");
  out.write("+"+application.getAttribute("seasonAttribute"));
%>
</body>
</html>
