package no.nav.kiv.confluence.labs.rest.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * User: Michal J. Sladek
 * Date: 29.03.2016
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement
public class ShareJiraIssueRequest {
    String username;
    String issue;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getIssue() {
        return issue;
    }

    public void setIssue(String issue) {
        this.issue = issue;
    }
}
