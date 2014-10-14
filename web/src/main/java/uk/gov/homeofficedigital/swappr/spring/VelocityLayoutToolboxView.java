package uk.gov.homeofficedigital.swappr.spring;

import org.apache.velocity.context.Context;
import org.apache.velocity.tools.Scope;
import org.apache.velocity.tools.Toolbox;
import org.apache.velocity.tools.config.EasyFactoryConfiguration;
import org.apache.velocity.tools.generic.ContextTool;
import org.apache.velocity.tools.view.ViewToolContext;
import org.springframework.web.servlet.view.velocity.VelocityLayoutView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * Exists purely to get around an apparent bug with Spring's velocity tool support. I.e. that the toolbox config is read
 * from a file referenced by {@link javax.servlet.ServletContext#getResourceAsStream} in
 * {@link org.springframework.web.servlet.view.velocity.VelocityToolboxView#createVelocityContext(java.util.Map, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)},
 * which is not configured to find resources when running under Spring Boot.
 */
public class VelocityLayoutToolboxView extends VelocityLayoutView {

    private Toolbox toolbox = null;

    private Toolbox getToolbox() {
        if (toolbox == null) {
            EasyFactoryConfiguration config = new EasyFactoryConfiguration();
            config.toolbox(Scope.REQUEST).tool(ContextTool.class);

            toolbox = config.createFactory().createToolbox(Scope.REQUEST);
        }
        return toolbox;
    }

    @Override
    protected Context createVelocityContext(Map<String, Object> model, HttpServletRequest request, HttpServletResponse response) throws Exception {
        ViewToolContext toolContext = new ViewToolContext(getVelocityEngine(), request, response, getServletContext());
        toolContext.addToolbox(getToolbox());
        toolContext.putAll(model);
        return toolContext;
    }
}
