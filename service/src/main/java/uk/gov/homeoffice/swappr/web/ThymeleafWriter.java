package uk.gov.homeoffice.swappr.web;

import org.glassfish.jersey.server.mvc.Viewable;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.templatemode.StandardTemplateModeHandlers;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;
import org.thymeleaf.templateresolver.ServletContextTemplateResolver;
import org.thymeleaf.templateresolver.TemplateResolver;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.MessageBodyWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.Map;

public class ThymeleafWriter implements MessageBodyWriter<Viewable>  {

    private final HttpServletRequest request;
    private final HttpServletResponse response;
    private final ServletContext servletContext;

    public ThymeleafWriter(@Context HttpServletRequest request, @Context HttpServletResponse response, @Context ServletContext servletContext) {
        this.request = request;
        this.response = response;
        this.servletContext = servletContext;
    }


    @Override
    public boolean isWriteable(Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
        return Viewable.class.isAssignableFrom(type);
    }

    @Override
    public long getSize(Viewable viewable, Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
        return -1;
    }

    @Override
    public void writeTo(Viewable viewable, Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType, MultivaluedMap<String, Object> httpHeaders, OutputStream entityStream) throws IOException, WebApplicationException {

        TemplateEngine templateEngine = new TemplateEngine();
        TemplateResolver templateResolver = new ClassLoaderTemplateResolver();

        templateResolver.setTemplateMode(StandardTemplateModeHandlers.XHTML.getTemplateModeName());
        templateResolver.setPrefix("templates/");
        templateResolver.setSuffix(".html");
        templateResolver.setCacheTTLMs(3600000L); // Template cache TTL=1h. If not set, entries would be cached until expelled by LRU

        templateEngine.setTemplateResolver(templateResolver);

        WebContext context = new WebContext(request, response, servletContext, request.getLocale());

        Object model = viewable.getModel();
        if (Map.class.isAssignableFrom(model.getClass())) {
            context.setVariables((Map<String, ?>) viewable.getModel());
        }

        OutputStreamWriter writer = new OutputStreamWriter(entityStream);
        templateEngine.process(viewable.getTemplateName(), context, writer);
        writer.flush();
        writer.close();
    }
}
