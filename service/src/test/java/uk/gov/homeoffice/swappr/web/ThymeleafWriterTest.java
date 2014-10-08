package uk.gov.homeoffice.swappr.web;

import org.glassfish.jersey.server.mvc.Viewable;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockServletContext;
import org.thymeleaf.exceptions.TemplateEngineException;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedHashMap;
import java.io.ByteArrayOutputStream;
import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.*;

public class ThymeleafWriterTest {

    private MockHttpServletRequest request = new MockHttpServletRequest();
    private MockHttpServletResponse response = new MockHttpServletResponse();
    private MockServletContext context = new MockServletContext();


    @Test
    public void isWritable_shouldBeTrue_givenAViewableClass() throws Exception {
        boolean canWrite = new ThymeleafWriter(null, null, null).isWriteable(Viewable.class, Viewable.class, new Annotation[0], MediaType.TEXT_HTML_TYPE);
        assertTrue("Should be able to write Viewables but can't", canWrite);
    }

    @Test
    public void isWritable_shouldBeFalse_givenAnyNonViewableClass() throws Exception {
        boolean canWrite = new ThymeleafWriter(null, null, null).isWriteable(String.class, String.class, new Annotation[0], MediaType.TEXT_HTML_TYPE);
        assertFalse("Should not be able to write Viewables but can", canWrite);
    }

    @Test
    public void getSize_shouldReturnMinusOne() throws Exception {
        Map<String, String> data = new HashMap<>();
        data.put("name", "Rupert");
        Viewable viewable = new Viewable("dummy", data);
        long size = new ThymeleafWriter(null, null, null).getSize(viewable, Viewable.class, Viewable.class, new Annotation[0], MediaType.TEXT_HTML_TYPE);

        assertEquals(-1, size);
    }

    @Test(expected = TemplateEngineException.class)
    public void shouldThrowExceptionWhenTemplateCannotBeFound() throws Exception {
        Viewable viewable = new Viewable("whoops", new HashMap<>());
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        new ThymeleafWriter(request, response, context).writeTo(viewable, Viewable.class, Viewable.class, new Annotation[0], MediaType.TEXT_HTML_TYPE, new MultivaluedHashMap<String, Object>(), out);
    }

    @Test
    public void renderTemplateContents() throws Exception {

        Map<String, String> model = new HashMap<>();
        model.put("message", "Hello mum");
        Viewable viewable = new Viewable("testme", model);

        ByteArrayOutputStream out = new ByteArrayOutputStream();

        new ThymeleafWriter(request, response, context).writeTo(viewable, Viewable.class, Viewable.class, new Annotation[0], MediaType.TEXT_HTML_TYPE, new MultivaluedHashMap<String, Object>(), out);

        String response = out.toString();

        assertThat(response, equalTo("I have some content - <span>Hello mum</span>"));
    }


}
