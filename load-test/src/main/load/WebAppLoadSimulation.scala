package load

import io.gatling.core.Predef._
import io.gatling.http.Predef._

import scala.concurrent.duration._
import scala.language.postfixOps
import scala.util.Random

class WebAppLoadSimulation extends Simulation {

  private val rampUpTimeSecs = 15
  private val testTimeSecs = Integer.getInteger("testTimeSecs", 60)
  private val noOfUsers = Integer.getInteger("noOfUsers", 100)

  private val minWaitMs = 5 milliseconds
  private val maxWaitMs = 50 milliseconds
  private val baseURL = System.getProperty("baseUrl", "http://localhost:8099")
  private val articlesURI = "/api/v1/articles"
  private val authorsURI = "/api/v1/authors"

  private val httpConf = http
    .baseUrl(baseURL)
    .acceptHeader("application/json")
    .contentTypeHeader("application/json")
    .doNotTrackHeader("1")
    .acceptLanguageHeader("en-US,en;q=0.5")
    .acceptEncodingHeader("gzip, deflate")
    .userAgentHeader("Mozilla/5.0 (Windows NT 5.1; rv:31.0) Gecko/20100101 Firefox/31.0")

  private object Articles {
    private val authorsFeeder = csv("authors.csv").random
    private val sentHeaders = Map("Content-Type" -> "application/json", "Accept" -> "application/json")
    private val randomStringFeeder = Iterator.continually(Map(
      "newTitle" -> Random.alphanumeric.take(15).mkString,
      "newText" -> Random.alphanumeric.take(15).mkString,
      "newSummary" -> Random.alphanumeric.take(5).mkString,
    ))
    val create = feed(authorsFeeder).feed(randomStringFeeder).exec(
      http("Create article")
        .post(articlesURI)
        .headers(sentHeaders)
        .body(StringBody("""{
            |"title": "${newTitle}",
            |"text": "${newText}",
            |"author": { "id": "${authorId}" } }""".stripMargin)).asJson
        .check(status.is(201))
        .check(jsonPath("$.id").saveAs("newArticleId"))
    )

    val update = feed(randomStringFeeder).exec(
      http("Update article")
        .patch(articlesURI + "/${newArticleId}")
        .headers(sentHeaders)
        .body(StringBody("""{ "summary": "${newSummary}" }""")).asJson
        .check(status.is(200))
    )

    val delete = exec(
      http("Delete article")
        .delete(articlesURI + "/${newArticleId}")
        .check(status.is(204))
    )

    private val articlesFeeder = csv("articles.csv").random
    val read = feed(articlesFeeder).exec(
      http("Get article")
        .get(articlesURI + "/${articleId}")
        .check(status.is(200))
    )

    private val pageFeeder = csv("articles-sort.csv").random
    val readPaged = feed(pageFeeder).exec(
      http("Get articles paged")
        .get(articlesURI)
        .queryParam("size", _ => 10*(Random.nextInt(5) + 1))
        .queryParam("page", _ => Random.nextInt(10))
        .queryParam("sort", "${sort}")
        .check(status.is(200))
    )
  }

  private object Authors {
    private val authorsFeeder = csv("authors.csv").random
    val read = feed(authorsFeeder).exec(
      http("Get author")
        .get(authorsURI + "/${authorId}")
        .check(status.is(200))
    )

    val readAll = exec(
      http("Get all authors")
        .get(authorsURI)
        .check(status.is(200))
    )
  }

  private val scn = scenario("ArticlesAppLoad-scenario")
    .during(testTimeSecs) {
        repeat(2) {
          exec(Authors.read)
            .pause(minWaitMs, maxWaitMs)
        }
        .exec(Authors.readAll)
        .pause(minWaitMs, maxWaitMs)
        .exec(Articles.create)
        .pause(minWaitMs, maxWaitMs)
        .randomSwitch(
            33.0 -> exec(Articles.update)
              .pause(minWaitMs, maxWaitMs),
            25.0 -> exec(Articles.delete)
              .pause(minWaitMs, maxWaitMs)
        )
        .repeat(5) {
          exec(Articles.read)
            .pause(minWaitMs, maxWaitMs)
        }
        .repeat(2) {
          exec(Articles.readPaged)
            .pause(minWaitMs, maxWaitMs)
        }
    }

  setUp(
    scn.inject(rampUsers(noOfUsers).during(rampUpTimeSecs))
  ).protocols(httpConf)
}
