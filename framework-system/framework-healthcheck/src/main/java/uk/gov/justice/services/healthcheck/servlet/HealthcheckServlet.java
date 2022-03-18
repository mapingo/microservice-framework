package uk.gov.justice.services.healthcheck.servlet;

import static javax.servlet.http.HttpServletResponse.SC_OK;

import uk.gov.justice.services.healthcheck.run.HealthcheckProcessRunner;
import uk.gov.justice.services.healthcheck.run.HealthcheckRunResults;

import java.io.IOException;
import java.io.PrintWriter;

import javax.inject.Inject;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(
        name = "healthcheckServlet",
        urlPatterns = "/internal/healthchecks/all"
)
public class HealthcheckServlet extends HttpServlet {

    public static final int CUSTOM_HTTP_500_ERROR_RESPONSE_FOR_HEALTHCHECK_FAILURES = 523;

    @Inject
    private HealthcheckProcessRunner healthcheckProcessRunner;

    @Inject
    private HealthcheckToJsonConverter healthcheckToJsonConverter;

    @Override
    protected void doGet(final HttpServletRequest httpServletRequest, final HttpServletResponse httpServletResponse) throws IOException {

        final HealthcheckRunResults healthcheckRunResults = healthcheckProcessRunner.runAllHealthchecks();
        final String json = healthcheckToJsonConverter.toJson(healthcheckRunResults);

        final PrintWriter out = httpServletResponse.getWriter();

        httpServletResponse.setContentType("application/json");
        httpServletResponse.setCharacterEncoding("UTF-8");
        out.println(json);
        out.flush();

        if (healthcheckRunResults.isAllHealthchecksPassed()) {
            httpServletResponse.setStatus(SC_OK);
        } else {
            httpServletResponse.setStatus(CUSTOM_HTTP_500_ERROR_RESPONSE_FOR_HEALTHCHECK_FAILURES);
        }
    }
}
