package no.nav.kiv.confluence.labs.rest.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

/**
 * User: Michal J. Sladek
 * Date: 07.09.2015
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement
public class JiraSearchRequest {
    List<String> issueKeys;
    List<String> fixVersions;
    List<String> projectKeys;
    List<String> issueTypes;
    List<String> status;
    List<String> components;
    List<String> fields;
    List<String> expand;
    List<String> searchInFields;
    List<String> searchInFieldsNames;
    List<List<String>> filterFields;
    String maxResults;
    String searchKeyword;


    public JiraSearchRequest() {
    }

    public JiraSearchRequest(List<String> issueKeys, List<String> fixVersions, List<String> projectKeys, List<String> issueTypes, List<String> status, List<String> components, List<String> fields, List<String> expand, List<String> searchInFields, String maxResults, String searchKeyword, List<String> searchInFieldsNames, List<List<String>> filterFields) {
        this.issueKeys = issueKeys;
        this.fixVersions = fixVersions;
        this.projectKeys = projectKeys;
        this.issueTypes = issueTypes;
        this.status = status;
        this.components = components;
        this.fields = fields;
        this.expand = expand;
        this.searchInFields = searchInFields;
        this.maxResults = maxResults;
        this.searchKeyword = searchKeyword;
        this.searchInFieldsNames = searchInFieldsNames;
        this.filterFields = filterFields;
    }

    public List<String> getIssueKeys() {
        return issueKeys;
    }

    public void setIssueKeys(List<String> issueKeys) {
        this.issueKeys = issueKeys;
    }

    public List<String> getFixVersions() {
        return fixVersions;
    }

    public void setFixVersions(List<String> fixVersions) {
        this.fixVersions = fixVersions;
    }

    public List<String> getProjectKeys() {
        return projectKeys;
    }

    public void setProjectKeys(List<String> projectKeys) {
        this.projectKeys = projectKeys;
    }

    public List<String> getIssueTypes() {
        return issueTypes;
    }

    public void setIssueTypes(List<String> issueTypes) {
        this.issueTypes = issueTypes;
    }

    public List<String> getStatus() {
        return status;
    }

    public void setStatus(List<String> status) {
        this.status = status;
    }

    public List<String> getFields() {
        return fields;
    }

    public void setFields(List<String> fields) {
        this.fields = fields;
    }

    public String getMaxResults() {
        return maxResults;
    }

    public void setMaxResults(String maxResults) {
        this.maxResults = maxResults;
    }

    public List<String> getComponents() {
        return components;
    }

    public void setComponents(List<String> components) {
        this.components = components;
    }

    public String getSearchKeyword() {
        return searchKeyword;
    }

    public void setSearchKeyword(String searchKeyword) {
        this.searchKeyword = searchKeyword;
    }

    public List<String> getSearchInFields() {
        return searchInFields;
    }

    public void setSearchInFields(List<String> searchInFields) {
        this.searchInFields = searchInFields;
    }

    public List<String> getExpand() {
        return expand;
    }

    public void setExpand(List<String> expand) {
        this.expand = expand;
    }

    public List<String> getSearchInFieldsNames() {
        return searchInFieldsNames;
    }

    public void setSearchInFieldsNames(List<String> searchInFieldsNames) {
        this.searchInFieldsNames = searchInFieldsNames;
    }

    public List<List<String>> getFilterFields() {
        return filterFields;
    }

    public void setFilterFields(List<List<String>> filterFields) {
        this.filterFields = filterFields;
    }
}
