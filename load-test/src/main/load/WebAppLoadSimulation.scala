package load

import io.gatling.core.Predef._
import io.gatling.http.Predef._

import scala.concurrent.duration._
import scala.util.Random

class WebAppLoadSimulation extends Simulation {

  val rampUpTimeSecs = 15
  val testTimeSecs = Integer.getInteger("testTimeSecs", 50)
  val noOfUsers = Integer.getInteger("noOfUsers", 50)

  val minWaitMs = 5 milliseconds
  val maxWaitMs = 50 milliseconds
  val baseURL = System.getProperty("baseUrl", "http://localhost:8099")
  val articlesURI = "/api/v1/articles"
  val authorsURI = "/api/v1/authors"

  val httpConf = http
    .baseUrl(baseURL)
    .acceptHeader("application/json")
    .contentTypeHeader("application/json")
    .doNotTrackHeader("1")
    .acceptLanguageHeader("en-US,en;q=0.5")
    .acceptEncodingHeader("gzip, deflate")
    .userAgentHeader("Mozilla/5.0 (Windows NT 5.1; rv:31.0) Gecko/20100101 Firefox/31.0")

  object Articles {
    val authorsFeeder = csv("authors.csv").random
    val sentHeaders = Map("Content-Type" -> "application/json", "Accept" -> "application/json")
    var randomStringFeeder = Iterator.continually(Map(
      "newTitle" -> (Random.alphanumeric.take(15).mkString),
      "newText" -> (Random.alphanumeric.take(15).mkString),
      "newSummary" -> (Random.alphanumeric.take(5).mkString),
    ))
    val create = feed(authorsFeeder).feed(randomStringFeeder).exec(
      http("Create article")
        .post(articlesURI)
        .headers(sentHeaders)
        .body(StringBody("""{ "title": "${newTitle}", "text": "${newText}", "author": { "id": "${authorId}" } }""")).asJson
        .basicAuth("Ivan", "ivan_pass")
        .check(status.is(201))
        .check(jsonPath("$.id").saveAs("newArticleId"))
    )

    val update = feed(randomStringFeeder).exec(
      http("Update article")
        .patch(articlesURI + "/${newArticleId}")
        .headers(sentHeaders)
        .body(StringBody("""{ "summary": "${newSummary}" }""")).asJson
        .basicAuth("Ivan", "ivan_pass")
        .check(status.is(200))
    )

    val delete = exec(
      http("Delete article")
        .delete(articlesURI + "/${newArticleId}")
        .basicAuth("Ivan", "ivan_pass")
        .check(status.is(204))
    )

    val articlesFeeder = csv("articles.csv").random
    var read = feed(articlesFeeder).exec(
      http("Get article")
        .get(articlesURI + "/${articleId}")
        .basicAuth("Vasily", "vasily_pass")
        .check(status.is(200))
    )

    var readAll = exec(
      http("Get all articles")
        .get(articlesURI)
        .basicAuth("Vasily", "vasily_pass")
        .check(status.is(200))
    )

    val pageFeeder = csv("articles-sort.csv").random
    val readWithPagination = feed(pageFeeder).exec(
      http("Get articles with pagination")
        .get(articlesURI)
        .queryParam("size", _ => (10*(Random.nextInt(5) + 1)))
        .queryParam("page", _ => Random.nextInt(4))
        .queryParam("sort", "${sort}")
        .basicAuth("Vasily", "vasily_pass")
        .check(status.is(200))
    )
  }

  object Authors {
    val authorsFeeder = csv("authors.csv").random
    var read = feed(authorsFeeder).exec(
      http("Get author")
        .get(authorsURI + "/${authorId}")
        .basicAuth("Vasily", "vasily_pass")
        .check(status.is(200))
    )

    var readAll = exec(
      http("Get all authors")
        .get(authorsURI)
        .basicAuth("Vasily", "vasily_pass")
        .check(status.is(200))
    )
  }

  val scn = scenario("ArticlesAppLoad-scenario")
    .during(testTimeSecs) {
      repeat(2) {
        exec(Authors.read)
          .pause(minWaitMs, maxWaitMs)
      }
        .pause(minWaitMs, maxWaitMs)
        .exec(Authors.readAll)
        .repeat(2) {
          exec(Articles.read)
            .pause(minWaitMs, maxWaitMs)
        }
        .repeat(2) {
          exec(Articles.readWithPagination)
            .pause(minWaitMs, maxWaitMs)
        }
        .pause(minWaitMs, maxWaitMs)
        .exec(Articles.create)
        .pause(minWaitMs, maxWaitMs)
        .doIf(Random.nextFloat() < 0.25) {
          exec(Articles.update)
            .pause(minWaitMs, maxWaitMs)
        }
        .doIf(Random.nextFloat() < 0.25) {
          exec(Articles.delete)
            .pause(minWaitMs, maxWaitMs)
        }
        .exec(Articles.readAll)
    }

  setUp(
    scn.inject(rampUsers(noOfUsers).during(rampUpTimeSecs))
  ).protocols(httpConf)
}
