package no.nav.kiv.confluence.labs.utils;

import com.atlassian.applinks.api.*;
import com.atlassian.applinks.api.application.jira.JiraApplicationType;
import com.atlassian.plugin.spring.scanner.annotation.export.ExportAsService;
import com.atlassian.plugin.spring.scanner.annotation.imports.ComponentImport;
import com.atlassian.sal.api.net.Request.MethodType;
import no.nav.kiv.confluence.labs.api.RequestBuilder;

import javax.inject.Inject;
import javax.inject.Named;

/**
 * Encapsulates navigating from an {@link com.atlassian.applinks.api.ApplicationLinkService} to an {@link com.atlassian.applinks.api.ApplicationLinkRequestFactory} creating requests on a
 * Confluence instance, and also creating {@link com.atlassian.applinks.api.ApplicationLinkRequest} objects that will perform actions on Confluence.
 *
 * The navigation takes place in a lazy fashion; the link and factory aren't found until they are needed.
 *
 * @author Steinar Bang
 *
 */
@ExportAsService
@Named
public class DefaultRequestBuilder implements RequestBuilder {


    private ApplicationLinkService applicationLinkService;
    private ApplicationLink confluenceAppLink;
    private ApplicationLinkRequestFactory confluenceRequestFactory;

    @Inject
    public DefaultRequestBuilder(@ComponentImport ApplicationLinkService applicationLinkService) {
        this.applicationLinkService = applicationLinkService;
    }

    public ApplicationLink lazyFindConfluenceApplicationLink() {
        if (null == confluenceAppLink) {
            final Iterable<ApplicationLink> applicationLinks = applicationLinkService.getApplicationLinks(JiraApplicationType.class);

            confluenceAppLink = applicationLinks.iterator().next();
        }

        if (null == confluenceAppLink) {
            String msg = "No primary jira application link found.";
            throw new RuntimeException(msg);
        }

        return confluenceAppLink;
    }

    @Override
    public ApplicationLinkRequest createRequest(MethodType method, String url) throws CredentialsRequiredException {
        ApplicationLinkRequestFactory factory = lazyFindConfluenceRequestFactory();
        return factory.createRequest(method, url);
    }

    @Override
    public String getApplicationLinkHost() {
        if (null == confluenceAppLink) {
            lazyFindConfluenceApplicationLink();
        }
        return confluenceAppLink.getRpcUrl().toString();
    }

    private ApplicationLinkRequestFactory lazyFindConfluenceRequestFactory() {
        if (null == confluenceAppLink) {
            lazyFindConfluenceApplicationLink();
        }

        confluenceRequestFactory = confluenceAppLink.createAuthenticatedRequestFactory();

        return confluenceRequestFactory;
    }

}