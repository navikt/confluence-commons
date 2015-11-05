package no.nav.kiv.confluence.labs.impl;

import com.atlassian.confluence.setup.settings.SettingsManager;
import com.atlassian.plugin.spring.scanner.annotation.export.ExportAsService;
import com.atlassian.plugin.spring.scanner.annotation.imports.ComponentImport;
import com.atlassian.sal.api.net.*;
import no.nav.kiv.confluence.labs.api.RestCallClient;
import org.apache.commons.io.IOUtils;
import org.apache.http.entity.ContentType;

import javax.inject.Inject;
import javax.inject.Named;
import java.io.IOException;

/**
 * User: Michal J. Sladek
 * Date: 02.11.2015
 */
@ExportAsService({RestCallClient.class})
@Named("restClient")
public class RestCallClientImpl implements RestCallClient {

    @ComponentImport
    private final RequestFactory<?> requestFactory;

    @ComponentImport
    private SettingsManager settingsManager;
    private String responseString = "";

    @Inject
    public RestCallClientImpl(RequestFactory<?> requestFactory, SettingsManager settingsManager) {
        this.requestFactory = requestFactory;
        this.settingsManager = settingsManager;
    }

    @Override
    public String postRestResponse(final String restPath, final String body) {
        String baseUrl = settingsManager.getGlobalSettings().getBaseUrl();

        Request request = requestFactory.createRequest(Request.MethodType.POST, baseUrl + restPath);

        request.setRequestBody(body);
        request.setRequestContentType(ContentType.APPLICATION_JSON.toString());

        //This is the key to authenticating back to the host system
        request.addTrustedTokenAuthentication();  // this takes username from ThreadLocal

        try {
            request.execute(new ResponseHandler() {
                @Override
                public void handle(Response response) throws ResponseException {
                    try {
                        String propsJson = IOUtils.toString(response.getResponseBodyAsStream());
                        responseString = propsJson;
                    } catch (IOException e) {
                        throw new ResponseException(e);
                    }
                }
            });
        } catch (ResponseException ignored) {

        }
        return responseString;
    }
}
