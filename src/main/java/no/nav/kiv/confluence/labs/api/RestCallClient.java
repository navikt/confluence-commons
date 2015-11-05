package no.nav.kiv.confluence.labs.api;

/**
 * User: Michal J. Sladek
 * Date: 02.11.2015
 */
public interface RestCallClient {

    public String postRestResponse(String restPath, String body);
}
