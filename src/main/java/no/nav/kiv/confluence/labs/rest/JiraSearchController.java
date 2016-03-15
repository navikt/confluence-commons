package no.nav.kiv.confluence.labs.rest;

import com.atlassian.applinks.api.ApplicationLinkRequest;
import com.atlassian.applinks.api.CredentialsRequiredException;
import com.atlassian.confluence.util.HtmlUtil;
import com.atlassian.sal.api.net.Request;
import com.atlassian.sal.api.net.ResponseException;
import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import no.nav.kiv.confluence.labs.rest.model.JiraSearchRequest;
import no.nav.kiv.confluence.labs.rest.model.JiraSearchResponse;
import no.nav.kiv.confluence.labs.utils.RequestBuilder;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.stream.Collectors;

/**
 * A resource creating search queries to JIRA.
 */
@Path("/search")
@Produces({MediaType.APPLICATION_JSON})
@Consumes({MediaType.APPLICATION_JSON})
public class JiraSearchController {

    private static final Logger log = LoggerFactory.getLogger(JiraSearchController.class);

    private RequestBuilder requestBuilder;

    @Inject
    public JiraSearchController(RequestBuilder requestBuilder) {
        this.requestBuilder = requestBuilder;
    }

    @POST
    public Response postSearch(JiraSearchRequest searchModel, @Context HttpHeaders headers) {
        String message;

        List<String> partQueries = Lists.newArrayList();
        String requestPath = "/rest/api/2/search?jql=";
        try {

            final List<String> issueKeys = searchModel.getIssueKeys();
            if (null != issueKeys && !issueKeys.isEmpty()) {
                partQueries.add(HtmlUtil.urlEncode("key in (" + issueKeys.stream().map((s) -> "'" + s + "'").collect(Collectors.joining(", ")) + ")"));
            }

            final List<String> statuses = searchModel.getStatus();
            if (null != statuses && !statuses.isEmpty()) {
                partQueries.add(HtmlUtil.urlEncode("status in (" + statuses.stream().map((s) -> "'" + s + "'").collect(Collectors.joining(", ")) + ")"));
            }

            final List<String> components = searchModel.getComponents();
            if (null != components && !components.isEmpty()) {
                partQueries.add(HtmlUtil.urlEncode("component in (" + components.stream().map((s) -> "'" + s + "'").collect(Collectors.joining(", ")) + ")"));
            }

            final List<String> projectKeys = searchModel.getProjectKeys();
            if (null != projectKeys && !projectKeys.isEmpty()) {
                partQueries.add(HtmlUtil.urlEncode("project in (" + projectKeys.stream().map((s) -> "'" + s + "'").collect(Collectors.joining(", ")) + ")"));
            }

            final List<String> fixVersions = searchModel.getFixVersions();
            if (null != fixVersions && ! fixVersions.isEmpty()) {
                partQueries.add(HtmlUtil.urlEncode("fixVersion in (" + fixVersions.stream().map((s) -> "'" + s + "'").collect(Collectors.joining(", ")) + ")"));
            }

            final List<String> issueTypes = searchModel.getIssueTypes();
            if (null != issueTypes && !issueTypes.isEmpty()) {
                partQueries.add(HtmlUtil.urlEncode("type in (" + issueTypes.stream().map((s) -> "'" + s + "'").collect(Collectors.joining(", ")) + ")"));
            }

            final List<List<String>> filterInFields = searchModel.getFilterFields();
            if (null != filterInFields && !filterInFields.isEmpty()) {
                for (List<String> filterField : filterInFields) {
                    String fieldName = filterField.get(0).split("::")[0];
                    if ("components".equals(fieldName)) {
                        fieldName = "component";
                    }
                    partQueries.add(HtmlUtil.urlEncode(fieldName + " in (" + filterField.stream().map((s) -> "'" + s.split("::")[1] + "'").collect(Collectors.joining(", ")) + ")"));
                }
            }

            final String searchKeyword = searchModel.getSearchKeyword();
            if (!StringUtils.isBlank(searchKeyword)) {
                final String searchKey = HtmlUtil.urlDecode(searchKeyword);

                String textSearch;
                final List<String> searchInFields = searchModel.getSearchInFields();
                final List<String> searchInFieldsNames = searchModel.getSearchInFieldsNames();
                if ((null != searchInFields && !searchInFields.isEmpty()) || (null != searchInFieldsNames && !searchInFieldsNames.isEmpty())) {
                    List<String> partTestSearchTerms = Lists.newArrayList();

                    if (searchKey.indexOf("*") == 0 || searchKey.indexOf("?") == 0) {
                        final String searchKeyRep = searchKey.replaceAll("\\*", ".*").replaceAll("\\?", ".?");
                        partTestSearchTerms.addAll(searchInFieldsNames.stream().map(field -> HtmlUtil.urlEncode("issueFunction in issueFieldMatch(\"project in (") + projectKeys.stream().map((s) -> HtmlUtil.urlEncode("'") + HtmlUtil.urlEncode(s) + HtmlUtil.urlEncode("'")).collect(Collectors.joining(HtmlUtil.urlEncode(", "))) + HtmlUtil.urlEncode(")\", \"" + field + "\", \"(?i)") + HtmlUtil.urlEncode(searchKeyRep) + HtmlUtil.urlEncode("\")")).collect(Collectors.toList()));
                    } else {
                        partTestSearchTerms.addAll(searchInFields.stream().map(field -> HtmlUtil.urlEncode("'" + field + "' ~ '") + searchKey + HtmlUtil.urlEncode("'")).collect(Collectors.toList()));
                    }

                    textSearch = Joiner.on(HtmlUtil.urlEncode(" or ")).join(partTestSearchTerms);
                    textSearch = HtmlUtil.urlEncode("(").concat(textSearch).concat(HtmlUtil.urlEncode(")"));

                } else {
                    if (searchKey.indexOf("*") == 0 || searchKey.indexOf("?") == 0) {
                        final String searchKeyRep = searchKey.replaceAll("\\*", ".*").replaceAll("\\?", ".?");
                        textSearch = HtmlUtil.urlEncode("issueFunction in issueFieldMatch(\"project in (") + projectKeys.stream().map((s) -> HtmlUtil.urlEncode("'") + s + HtmlUtil.urlEncode("'")).collect(Collectors.joining(HtmlUtil.urlEncode(", "))) + HtmlUtil.urlEncode(")\", \"summary\", \"(?i)") + HtmlUtil.urlEncode(searchKeyRep) + HtmlUtil.urlEncode("\")");
                    } else {
                        textSearch = (HtmlUtil.urlEncode("'summary' ~ '") + searchKey + HtmlUtil.urlEncode("'"));
                    }
                }

                if (!StringUtils.isBlank(textSearch)) {
                    partQueries.add(textSearch);
                }
            }

            requestPath = requestPath.concat(Joiner.on(HtmlUtil.urlEncode(" and ")).join(partQueries));
            requestPath = requestPath.concat("&maxResults=" + (searchModel.getMaxResults() != null ? searchModel.getMaxResults() : "1000"));

            final List<String> fields = searchModel.getFields();
            if (null != fields && !fields.isEmpty()) {
                requestPath = requestPath.concat("&fields=" + HtmlUtil.urlEncode(Joiner.on(",").join(fields)));
            }

            final List<String> expand = searchModel.getExpand();
            if (null != expand && !expand.isEmpty()) {
                requestPath = requestPath.concat("&expand=" + HtmlUtil.urlEncode(Joiner.on(",").join(expand)));
            }

            ApplicationLinkRequest request = requestBuilder.createRequest(Request.MethodType.GET, requestPath);
            String response = request.execute();

            return Response.ok(createJSONResponse(response)).build();

        } catch (CredentialsRequiredException e) {
            message = "[CREx] Failed to find issues with the request to " + requestPath + " got " + e.getClass().getName() + " with message: " + e.getMessage();
            if (log.isErrorEnabled()) {
                log.error(message, e);
            }
            return Response.status(Response.Status.UNAUTHORIZED).entity(new JiraSearchResponse(e.getMessage())).build();

        } catch (ResponseException e) {
            message = "[REx] Failed to find issues with the request to " + requestPath + " got " + e.getClass().getName() + " with message: " + e.getMessage();
            if (log.isErrorEnabled()) {
                log.error(message);
            }
            return Response.status(Response.Status.BAD_REQUEST).entity(new JiraSearchResponse(e.getMessage())).build();

        } catch (Exception e) {
            message = "Failed to find issues, caught " + e.getClass().getName() + " with message: " + e.getMessage();
            if (log.isErrorEnabled()) {
                log.error(message, e);
            }
        }
        // Common return point for all caught exceptions, success is returned inside the try
        return Response.serverError().entity(createJSONResponse(message)).build();
    }

    @GET
    @Path("metadata")
    public Response getMatadata(@QueryParam("projectKey") String projectKey, @QueryParam("issuetypeName") String issuetypeName) {
        String message;
        String requestPath = "/rest/api/2/issue/createmeta?projectKeys=" +
                projectKey + "&issuetypeNames=" +
                issuetypeName + "&expand=projects.issuetypes.fields";
        try {
            ApplicationLinkRequest request = requestBuilder.createRequest(Request.MethodType.GET, requestPath);
            String response = request.execute();

            return Response.ok(createJSONResponse(response)).build();

        } catch (CredentialsRequiredException e) {
            message = "[CREx] Failed to find metadata with the request to " + requestPath + " got " + e.getClass().getName() + " with message: " + e.getMessage();
            if (log.isErrorEnabled()) {
                log.error(message, e);
            }
            return Response.status(Response.Status.UNAUTHORIZED).entity(new JiraSearchResponse(e.getMessage())).build();

        } catch (ResponseException e) {
            message = "[REx] Failed to find metadata with the request to " + requestPath + " got " + e.getClass().getName() + " with message: " + e.getMessage();
            if (log.isErrorEnabled()) {
                log.error(message);
            }
            return Response.status(Response.Status.BAD_REQUEST).entity(new JiraSearchResponse(e.getMessage())).build();

        } catch (Exception e) {
            message = "Failed to find metadata, caught " + e.getClass().getName() + " with message: " + e.getMessage();
            if (log.isErrorEnabled()) {
                log.error(message, e);
            }
        }
        // Common return point for all caught exceptions, success is returned inside the try
        return Response.serverError().entity(createJSONResponse(message)).build();
    }

    @GET
    @Path("metadata/statuses")
    public Response getStatuser(@QueryParam("projectKey") String projectKey) {
        String message;

        String requestPath = "/rest/api/2/project/" + projectKey + "/statuses";

        try {
            ApplicationLinkRequest request = requestBuilder.createRequest(Request.MethodType.GET, requestPath);
            String response = request.execute();

            return Response.ok(createJSONResponse(response)).build();

        } catch (CredentialsRequiredException e) {
            message = "[CREx] Failed to find metadata with the request to " + requestPath + " got " + e.getClass().getName() + " with message: " + e.getMessage();
            if (log.isErrorEnabled()) {
                log.error(message, e);
            }
            return Response.status(Response.Status.UNAUTHORIZED).entity(new JiraSearchResponse(e.getMessage())).build();

        } catch (ResponseException e) {
            message = "[REx] Failed to find metadata with the request to " + requestPath + " got " + e.getClass().getName() + " with message: " + e.getMessage();
            if (log.isErrorEnabled()) {
                log.error(message);
            }
            return Response.status(Response.Status.BAD_REQUEST).entity(new JiraSearchResponse(e.getMessage())).build();

        } catch (Exception e) {
            message = "Failed to find metadata, caught " + e.getClass().getName() + " with message: " + e.getMessage();
            if (log.isErrorEnabled()) {
                log.error(message, e);
            }
        }
        // Common return point for all caught exceptions, success is returned inside the try
        return Response.serverError().entity(createJSONResponse(message)).build();
    }

    /**
     * Return the JSON object.
     *
     * @param response A JSON response from the REST endpoint
     * @return an object that can be serialized as JSON
     */
    JiraSearchResponse createJSONResponse(String response) {
        return new JiraSearchResponse(response);
    }
}