package no.nav.kiv.confluence.labs.rest.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * User: Michal J. Sladek
 * Date: 07.09.2015
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement
public class JiraSearchRequest {
    String issueKeys;
    String fixVersions;
    String projectKeys;
    String issueTypes;
    String fields;

    public JiraSearchRequest() {
    }

    public JiraSearchRequest(String fixVersions, String projectKey, String issueTypes, String fields, String issueKeys) {
        this.fixVersions = fixVersions;
        this.projectKeys = projectKey;
        this.issueTypes = issueTypes;
        this.fields = fields;
        this.issueKeys = issueKeys;
    }

    public String getFixVersions() {
        return fixVersions;
    }

    public void setFixVersions(String fixVersions) {
        this.fixVersions = fixVersions;
    }

    public String getProjectKeys() {
        return projectKeys;
    }

    public void setProjectKeys(String projectKeys) {
        this.projectKeys = projectKeys;
    }

    public String getIssueTypes() {
        return issueTypes;
    }

    public void setIssueTypes(String issueTypes) {
        this.issueTypes = issueTypes;
    }

    public String getFields() {
        return fields;
    }

    public void setFields(String fields) {
        this.fields = fields;
    }

    public String getIssueKeys() {
        return issueKeys;
    }

    public void setIssueKeys(String issueKeys) {
        this.issueKeys = issueKeys;
    }
}
