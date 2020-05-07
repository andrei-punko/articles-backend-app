package by.andd3dfx.specs

import spock.lang.Specification

import static by.andd3dfx.configs.Configuration.restClient

class SomeSpec extends Specification {

    def 'Read all authors'() {
        when: 'login with valid credentials and get all authors'
        restClient.headers['Authorization'] = "Basic ${"Vasily:vasily_pass".bytes.encodeBase64()}"
        def response = restClient.get(path: '/authors')

        then: 'server returns 200 code (ok)'
        assert response.status == 200: 'response code should be 200 when tried to authenticate with valid credentials'
        assert response.responseData.size == 11: 'size=11 expected'
    }
}
