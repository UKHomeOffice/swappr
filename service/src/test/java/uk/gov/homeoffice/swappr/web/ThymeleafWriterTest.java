package uk.gov.homeoffice.swappr.web;

import org.junit.Test;
import org.thymeleaf.exceptions.TemplateEngineException;
import uk.gov.homeoffice.swappr.web.resources.Viewable;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedHashMap;
import java.io.ByteArrayOutputStream;
import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.*;

public class ThymeleafWriterTest {

    @Test
    public void isWritable_shouldBeTrue_givenAViewableClass() throws Exception {
        boolean canWrite = new ThymeleafWriter().isWriteable(Viewable.class, Viewable.class, new Annotation[0], MediaType.TEXT_HTML_TYPE);
        assertTrue("Should be able to write Viewables but can't", canWrite);
    }

    @Test
    public void isWritable_shouldBeFalse_givenAnyNonViewableClass() throws Exception {
        boolean canWrite = new ThymeleafWriter().isWriteable(String.class, String.class, new Annotation[0], MediaType.TEXT_HTML_TYPE);
        assertFalse("Should not be able to write Viewables but can", canWrite);
    }

    @Test
    public void getSize_shouldReturnMinusOne() throws Exception {
        Map<String, String> data = new HashMap<>();
        data.put("name", "Rupert");
        Viewable viewable = new Viewable(data, "dummy");
        long size = new ThymeleafWriter().getSize(viewable, Viewable.class, Viewable.class, new Annotation[0], MediaType.TEXT_HTML_TYPE);

        assertEquals(-1, size);
    }

    @Test(expected = TemplateEngineException.class)
    public void shouldThrowExceptionWhenTemplateCannotBeFound() throws Exception {
        Viewable viewable = new Viewable(new HashMap<String, Object>(), "whoops");
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        new ThymeleafWriter().writeTo(viewable, Viewable.class, Viewable.class, new Annotation[0], MediaType.TEXT_HTML_TYPE, new MultivaluedHashMap<String, Object>(), out);
    }

    @Test
    public void renderTemplateContents() throws Exception {

        Map<String, String> model = new HashMap<>();
        model.put("message", "Hello mum");
        Viewable viewable = new Viewable(model, "testme");

        ByteArrayOutputStream out = new ByteArrayOutputStream();

        new ThymeleafWriter().writeTo(viewable, Viewable.class, Viewable.class, new Annotation[0], MediaType.TEXT_HTML_TYPE, new MultivaluedHashMap<String, Object>(), out);

        String response = out.toString();

        assertThat(response, equalTo("I have some content - <span>Hello mum</span>"));
    }


}
