package uk.gov.homeoffice.swappr.web.resources;

import java.util.Map;

public class Viewable {

    private Map<String, ?> model;
    private String view;

    public Viewable(Map<String, ?> model, String view) {
        this.model = model;
        this.view = view;
    }

    public Map<String, ?> getModel() {
        return model;
    }

    public String getView() {
        return view;
    }
}
