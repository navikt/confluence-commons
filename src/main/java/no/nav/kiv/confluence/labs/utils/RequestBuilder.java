package no.nav.kiv.confluence.labs.utils;

import com.atlassian.applinks.api.ApplicationLinkRequest;
import com.atlassian.applinks.api.CredentialsRequiredException;
import com.atlassian.sal.api.net.Request;

/**
 * User: Michal J. Sladek
 * Date: 13.04.2016
 */
public interface RequestBuilder {

    ApplicationLinkRequest createRequest(Request.MethodType method, String url) throws CredentialsRequiredException;

    String getApplicationLinkHost();
}
