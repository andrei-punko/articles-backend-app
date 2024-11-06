package by.andd3dfx.configs

import groovyx.net.http.RESTClient

class Configuration {
    public static final String host = "localhost"
    static final String serviceUrl = "http://$host:8099"

    public static final RESTClient restClient = new RESTClient(serviceUrl)
}
