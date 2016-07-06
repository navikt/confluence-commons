package no.nav.kiv.confluence.labs.rest;

import com.atlassian.applinks.api.ApplicationLinkRequest;
import com.atlassian.applinks.api.CredentialsRequiredException;
import com.atlassian.json.jsonorg.JSONObject;
import com.atlassian.sal.api.net.Request;
import com.atlassian.sal.api.net.ResponseException;
import com.google.common.collect.Lists;
import no.nav.kiv.confluence.labs.rest.model.JiraSearchResponse;
import no.nav.kiv.confluence.labs.rest.model.ShareJiraIssueRequest;
import no.nav.kiv.confluence.labs.utils.RequestBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Named;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;

/**
 * User: Michal J. Sladek
 * Date: 29.03.2016
 */
@Path("/share")
@Produces({MediaType.APPLICATION_JSON})
@Consumes({MediaType.APPLICATION_JSON})
@Named
public class ShareJiraIssueController {
    private static final Logger log = LoggerFactory.getLogger(ShareJiraIssueController.class);

    private RequestBuilder requestBuilder;

    @Inject
    public ShareJiraIssueController(RequestBuilder requestBuilder) {
        this.requestBuilder = requestBuilder;
    }

    @POST
    public Response postSearch(ShareJiraIssueRequest requestModel, @Context HttpHeaders headers) {
        String message;

        String requestPath = "/rest/share/1.0/issue/" + requestModel.getIssue();
        try {

            ArrayList users = Lists.newArrayList();
            users.add(requestModel.getUsername());

            JSONObject remoteLink = new JSONObject();
            remoteLink.put("emails", Lists.newArrayList());
            remoteLink.put("message", "");
            remoteLink.put("usernames", users);

            ApplicationLinkRequest request = requestBuilder.createRequest(Request.MethodType.POST, requestPath);

            request.setHeader("Content-Type", "application/json");
            request.setRequestBody(remoteLink.toString());

            String response = request.execute();


            return Response.ok().build();

        } catch (CredentialsRequiredException e) {
            message = "[CREx] Failed to share " + requestPath + " got " + e.getClass().getName() + " with message: " + e.getMessage();
            if (log.isErrorEnabled()) {
                log.error(message, e);
            }
            return Response.status(Response.Status.UNAUTHORIZED).entity(new JiraSearchResponse(e.getMessage())).build();

        } catch (ResponseException e) {
            message = "[REx] Failed to share " + requestPath + " got " + e.getClass().getName() + " with message: " + e.getMessage();
            if (log.isErrorEnabled()) {
                log.error(message);
            }
            return Response.status(Response.Status.BAD_REQUEST).build();

        } catch (Exception e) {
            message = "Failed to share, caught " + e.getClass().getName() + " with message: " + e.getMessage();
            if (log.isErrorEnabled()) {
                log.error(message, e);
            }
        }
        // Common return point for all caught exceptions, success is returned inside the try
        return Response.serverError().build();

    }
}

