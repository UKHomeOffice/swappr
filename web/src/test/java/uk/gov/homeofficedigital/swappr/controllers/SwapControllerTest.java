package uk.gov.homeofficedigital.swappr.controllers;

import org.junit.Test;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.ui.Model;

import static org.junit.Assert.*;

public class SwapControllerTest {
    private SwapController controller = new SwapController();

    @Test
    public void view_shouldDisplayTheCreateSwapTemplate() throws Exception {
        Model model = new ExtendedModelMap();

        String viewName = controller.view(model);

        assertEquals("createSwap", viewName);
        assertNotNull(model.containsAttribute("swap"));
    }
}