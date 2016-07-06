package no.nav.kiv.confluence.labs.rest;

import com.atlassian.applinks.api.ApplicationLinkRequest;
import com.atlassian.applinks.api.CredentialsRequiredException;
import com.atlassian.confluence.util.HtmlUtil;
import com.atlassian.sal.api.net.Request;
import com.atlassian.sal.api.net.ResponseException;
import no.nav.kiv.confluence.labs.rest.model.JiraSearchResponse;
import no.nav.kiv.confluence.labs.utils.RequestBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Named;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * User: Michal J. Sladek
 * Date: 29.03.2016
 */
@Path("/utils")
@Produces({MediaType.APPLICATION_JSON})
@Consumes({MediaType.APPLICATION_JSON})
@Named
public class JiraUtilsController {
    private static final Logger log = LoggerFactory.getLogger(JiraUtilsController.class);

    private RequestBuilder requestBuilder;

    @Inject
    public JiraUtilsController(RequestBuilder requestBuilder) {
        this.requestBuilder = requestBuilder;
    }

    @GET
    @Path("projects")
    public Response getJiraProjects(@Context HttpHeaders headers) {
        String message;

        String requestPath = "/rest/api/2/project";
        try {
            ApplicationLinkRequest request = requestBuilder.createRequest(Request.MethodType.GET, requestPath);
            request.setHeader("Content-Type", "application/json");
            String response = request.execute();
            return Response.ok(response).build();

        } catch (CredentialsRequiredException e) {
            message = "[CREx] Failed to get projects " + requestPath + " got " + e.getClass().getName() + " with message: " + e.getMessage();
            if (log.isErrorEnabled()) {
                log.error(message, e);
            }
            return Response.status(Response.Status.UNAUTHORIZED).entity(new JiraSearchResponse(e.getMessage())).build();

        } catch (ResponseException e) {
            message = "[REx] Failed to get projects " + requestPath + " got " + e.getClass().getName() + " with message: " + e.getMessage();
            if (log.isErrorEnabled()) {
                log.error(message);
            }
            return Response.status(Response.Status.BAD_REQUEST).build();

        } catch (Exception e) {
            message = "Failed to get projects, caught " + e.getClass().getName() + " with message: " + e.getMessage();
            if (log.isErrorEnabled()) {
                log.error(message, e);
            }
        }
        // Common return point for all caught exceptions, success is returned inside the try
        return Response.serverError().build();

    }

    @GET
    @Path("issueTypes")
    public Response getJiraIssueTypes(@Context HttpHeaders headers) {
        String message;

        String requestPath = "/rest/api/2/issuetype";
        try {
            ApplicationLinkRequest request = requestBuilder.createRequest(Request.MethodType.GET, requestPath);
            request.setHeader("Content-Type", "application/json");
            String response = request.execute();
            return Response.ok(response).build();

        } catch (CredentialsRequiredException e) {
            message = "[CREx] Failed to get issuetypes " + requestPath + " got " + e.getClass().getName() + " with message: " + e.getMessage();
            if (log.isErrorEnabled()) {
                log.error(message, e);
            }
            return Response.status(Response.Status.UNAUTHORIZED).entity(new JiraSearchResponse(e.getMessage())).build();

        } catch (ResponseException e) {
            message = "[REx] Failed to get issuetypes " + requestPath + " got " + e.getClass().getName() + " with message: " + e.getMessage();
            if (log.isErrorEnabled()) {
                log.error(message);
            }
            return Response.status(Response.Status.BAD_REQUEST).build();

        } catch (Exception e) {
            message = "Failed to get issuetypes, caught " + e.getClass().getName() + " with message: " + e.getMessage();
            if (log.isErrorEnabled()) {
                log.error(message, e);
            }
        }
        // Common return point for all caught exceptions, success is returned inside the try
        return Response.serverError().build();

    }

    @GET
    @Path("fields")
    public Response getJiraFields(@Context HttpHeaders headers) {
        String message;

        String requestPath = "/rest/api/2/field";
        try {
            ApplicationLinkRequest request = requestBuilder.createRequest(Request.MethodType.GET, requestPath);
            request.setHeader("Content-Type", "application/json");
            String response = request.execute();
            return Response.ok(response).build();

        } catch (CredentialsRequiredException e) {
            message = "[CREx] Failed to get fields " + requestPath + " got " + e.getClass().getName() + " with message: " + e.getMessage();
            if (log.isErrorEnabled()) {
                log.error(message, e);
            }
            return Response.status(Response.Status.UNAUTHORIZED).entity(new JiraSearchResponse(e.getMessage())).build();

        } catch (ResponseException e) {
            message = "[REx] Failed to get fields " + requestPath + " got " + e.getClass().getName() + " with message: " + e.getMessage();
            if (log.isErrorEnabled()) {
                log.error(message);
            }
            return Response.status(Response.Status.BAD_REQUEST).build();

        } catch (Exception e) {
            message = "Failed to get fields, caught " + e.getClass().getName() + " with message: " + e.getMessage();
            if (log.isErrorEnabled()) {
                log.error(message, e);
            }
        }
        // Common return point for all caught exceptions, success is returned inside the try
        return Response.serverError().build();

    }

    @GET
    @Path("issue/{issueKey}/{fieldId}")
    public Response getJiraIssueFieldProjects(
            @PathParam("issueKey") String issueKey,
            @PathParam("fieldId") String fieldId,
            @Context HttpHeaders headers) {

        String message;

        String requestPath = "/rest/api/2/issue/" + issueKey + "?expand=names,renderedFields&fields=" + HtmlUtil.urlEncode(fieldId);
        try {
            ApplicationLinkRequest request = requestBuilder.createRequest(Request.MethodType.GET, requestPath);
            request.setHeader("Content-Type", "application/json");
            String response = request.execute();
            return Response.ok(response).build();

        } catch (CredentialsRequiredException e) {
            message = "[CREx] Failed to get issue " + requestPath + " got " + e.getClass().getName() + " with message: " + e.getMessage();
            if (log.isErrorEnabled()) {
                log.error(message, e);
            }
            return Response.status(Response.Status.UNAUTHORIZED).entity(new JiraSearchResponse(e.getMessage())).build();

        } catch (ResponseException e) {
            message = "[REx] Failed to get issue " + requestPath + " got " + e.getClass().getName() + " with message: " + e.getMessage();
            if (log.isErrorEnabled()) {
                log.error(message);
            }
            return Response.status(Response.Status.BAD_REQUEST).build();

        } catch (Exception e) {
            message = "Failed to get issue, caught " + e.getClass().getName() + " with message: " + e.getMessage();
            if (log.isErrorEnabled()) {
                log.error(message, e);
            }
        }
        // Common return point for all caught exceptions, success is returned inside the try
        return Response.serverError().build();

    }
}

