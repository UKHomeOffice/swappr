package uk.gov.homeoffice.swappr.web;

import org.thymeleaf.TemplateEngine;
import org.thymeleaf.templatemode.StandardTemplateModeHandlers;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;
import org.thymeleaf.templateresolver.TemplateResolver;
import uk.gov.homeoffice.swappr.web.resources.Viewable;

import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.MessageBodyWriter;
import javax.ws.rs.ext.Provider;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.Locale;

public class ThymeleafWriter implements MessageBodyWriter<Viewable>  {

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

        org.thymeleaf.context.Context context = new org.thymeleaf.context.Context(Locale.ENGLISH, viewable.getModel());

        OutputStreamWriter writer = new OutputStreamWriter(entityStream);
        templateEngine.process(viewable.getView(), context, writer);
        writer.flush();
        writer.close();
    }
}
