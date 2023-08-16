package by.andd3dfx.specs

import groovyx.net.http.HttpResponseException
import org.apache.commons.lang.RandomStringUtils
import spock.lang.Specification

import static by.andd3dfx.configs.Configuration.restClient

class SomeSpec extends Specification {

    def 'Read all authors'() {
        when: 'get all authors'
        def getResponse = restClient.get(path: '/api/v1/authors')

        then: 'server returns 200 code (ok)'
        assert getResponse.status == 200
        and: 'got 11 records'
        assert getResponse.responseData.content.size() == 11
    }

    def 'Read particular author'() {
        when: 'get particular author'
        def getResponse = restClient.get(path: '/api/v1/authors/4')

        then: 'server returns 200 code (ok)'
        assert getResponse.status == 200
        and: 'response contains expected author'
        assert getResponse.responseData.firstName == 'Николай'
        assert getResponse.responseData.lastName == 'Пестов'
    }

    def 'Read all articles'() {
        when: 'get all articles'
        def getResponse = restClient.get(path: '/api/v1/articles')

        then: 'server returns 200 code (ok)'
        assert getResponse.status == 200
        and: 'got 10 records'
        assert getResponse.responseData.content.size() == 10
    }

    def 'Read all articles using pagination'() {
        when: 'get all articles'
        def getResponse = restClient.get(
                path: '/api/v1/articles',
                query: [size: '2', page: '4', sort: 'author.firstName,DESC']
        )

        then: 'server returns 200 code (ok)'
        assert getResponse.status == 200
        and: 'got 2 records'
        assert getResponse.responseData.content.size() == 2
    }

    def 'Read particular article'() {
        when: 'get particular article'
        def getResponse = restClient.get(path: '/api/v1/articles/1')

        then: 'server returns 200 code (ok)'
        assert getResponse.status == 200
        and: 'got expected article'
        assert getResponse.responseData.title == 'Игрок'
        assert getResponse.responseData.summary == 'Рассказ о страсти игромании'
        assert getResponse.responseData.author.firstName == 'Федор'
        assert getResponse.responseData.author.lastName == 'Достоевский'
    }

    def 'Create an article'() {
        when: 'create an article'
        def createResponse = restClient.post(
                path: '/api/v1/articles',
                body: [title: 'Some new title', summary: 'Bla-bla summary', text: 'BomBiBom', author: "{ id: '3' }"],
                requestContentType: 'application/json'
        )

        then: 'got 201 status'
        assert createResponse.status == 201
        and: 'got created article details in response'
        assert createResponse.responseData.title == 'Some new title'
        assert createResponse.responseData.summary == 'Bla-bla summary'
        assert createResponse.responseData.text == 'BomBiBom'

        cleanup:
        restClient.delete(path: '/api/v1/articles/' + createResponse.responseData.id)
    }

    def 'Delete an article'() {
        setup: 'create an article'
        def createResponse = restClient.post(
                path: '/api/v1/articles',
                body: [
                        title  : generateRandomString(10),
                        summary: generateRandomString(10),
                        text   : 'Weird text',
                        author : "{ id: '5' }"
                ],
                requestContentType: 'application/json'
        )
        and: 'got 201 status'
        assert createResponse.status == 201
        def id = createResponse.responseData.id

        when: 'delete particular article'
        def deleteResponse = restClient.delete(path: '/api/v1/articles/' + id)

        then: 'server returns 204 code'
        assert deleteResponse.status == 204

        and: 'try to get deleted article by id'
        try {
            restClient.get(path: '/api/v1/articles/' + id)
            throw new RuntimeException("Should not found deleted article")
        } catch (HttpResponseException hre) {
            and: 'got an 404 error'
            assert hre.statusCode == 404
        }
    }

    def 'Update an article'() {
        when:
        def newTitle = generateRandomString(10)
        def updateResponse = restClient.patch(
                path: '/api/v1/articles/2',
                body: [title: newTitle],
                requestContentType: 'application/json'
        )

        then: 'got 200 status'
        assert updateResponse.status == 200

        and: 'read an article'
        def getResponse = restClient.get(path: '/api/v1/articles/2')
        and: 'got 200 status'
        assert getResponse.status == 200
        assert getResponse.responseData.title == newTitle
    }

    String generateRandomString(int count) {
        RandomStringUtils.random(count, true, true)
    }
}
