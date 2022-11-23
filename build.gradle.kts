@file:Suppress("MissingPackageDeclaration", "SpellCheckingInspection", "GrazieInspection")

/*
* Copyright (C) 2016 - present Juergen Zimmermann, Hochschule Karlsruhe
*
* This program is free software: you can redistribute it and/or modify
* it under the terms of the GNU General Public License as published by
* the Free Software Foundation, either version 3 of the License, or
* (at your option) any later version.
*
* This program is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
* GNU General Public License for more details.
*
* You should have received a copy of the GNU General Public License
* along with this program.  If not, see <https://www.gnu.org/licenses/>.
*/

//  Aufrufe
//  1) Microservice uebersetzen und starten
//        .\gradlew bootRun [-Dport=8081] [--args='--debug'] [--continuous]
//        .\gradlew compileKotlin
//        .\gradlew compileTestKotlin
//
//  2) Microservice als selbstausfuehrendes JAR erstellen und ausfuehren
//        .\gradlew bootJar
//        java -jar build/libs/....jar --spring.profiles.active=dev
//        .\gradlew bootBuildImage [-Dtag='2.0.0']
//
//  3) Tests und QS
//        .\gradlew test [--rerun-tasks]
//        .\gradlew allureServe
//              EINMALIG>>   .\gradlew downloadAllure
//        .\gradlew jacocoTestReport
//        .\gradlew jacocoTestCoverageVerification
//        .\gradlew checkstyleMain checkstyleTest spotbugsMain spotbugsTest
//        .\gradlew forbiddenApis
//        .\gradlew buildHealth
//        .\gradlew reason --id com.fasterxml.jackson.core:jackson-annotations:2.13.3
//
//  4) Sicherheitsueberpruefung durch OWASP Dependency Check und Snyk
//        .\gradlew dependencyCheckAnalyze --info
//        .\gradlew snyk-test
//
//  5) "Dependencies Updates"
//        .\gradlew versions
//        .\gradlew dependencyUpdates
//        .\gradlew checkNewVersions
//
//  6) API-Dokumentation erstellen
//        .\gradlew javadoc
//
//  7) Entwicklerhandbuch in "Software Engineering" erstellen
//        .\gradlew asciidoctor asciidoctorPdf
//
//  8) Projektreport erstellen
//        .\gradlew projectReport
//        .\gradlew dependencyInsight --dependency spring-security-rsa
//        .\gradlew dependencies
//        .\gradlew dependencies --configuration runtimeClasspath
//        .\gradlew buildEnvironment
//        .\gradlew htmlDependencyReport
//
//  9) Report ueber die Lizenzen der eingesetzten Fremdsoftware
//        .\gradlew generateLicenseReport
//
//  10) Daemon stoppen
//        .\gradlew --stop
//
//  11) Verfuegbare Tasks auflisten
//        .\gradlew tasks
//
//  12) "Dependency Verification"
//        .\gradlew --write-verification-metadata pgp,sha256 --export-keys
//
//  13) Initialisierung des Gradle Wrappers in der richtigen Version
//      dazu ist ggf. eine Internetverbindung erforderlich
//        gradle wrapper --gradle-version=7.6-rc-1 --distribution-type=bin

// https://github.com/gradle/kotlin-dsl/tree/master/samples
// https://docs.gradle.org/current/userguide/kotlin_dsl.html
// https://docs.gradle.org/current/userguide/task_configuration_avoidance.html
// https://guides.gradle.org/migrating-build-logic-from-groovy-to-kotlin

import com.github.benmanes.gradle.versions.updates.DependencyUpdatesTask
import org.asciidoctor.gradle.jvm.AsciidoctorTask
import org.asciidoctor.gradle.jvm.pdf.AsciidoctorPdfTask
import org.gradle.internal.component.model.Exclude
import org.springframework.boot.gradle.tasks.bundling.BootBuildImage
import org.springframework.boot.gradle.tasks.bundling.BootJar
import org.springframework.boot.gradle.tasks.run.BootRun

// TODO https://youtrack.jetbrains.com/issue/KTIJ-19369
@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    java
    idea
    checkstyle
    jacoco
    `project-report`

    alias(libs.plugins.springBoot)
    // alias(libs.plugins.springAot)

    // https://docs.freefair.io/gradle-plugins/6.5.1/reference
    alias(libs.plugins.lombokPlugin)

    // https://spotbugs.readthedocs.io/en/latest/gradle.html
    alias(libs.plugins.spotbugs)

    // https://github.com/radarsh/gradle-test-logger-plugin
    alias(libs.plugins.testLogger)

    // https://github.com/allure-framework/allure-gradle
    // https://docs.qameta.io/allure/#_gradle_2
    alias(libs.plugins.allure)

    // https://github.com/boxheed/gradle-sweeney-plugin
    alias(libs.plugins.sweeney)

    // https://github.com/policeman-tools/forbidden-apis
    // TODO Java 19
    alias(libs.plugins.forbiddenapis)

    // https://github.com/jeremylong/dependency-check-gradle
    alias(libs.plugins.owaspDependencycheck)

    // https://github.com/snyk/gradle-plugin
    alias(libs.plugins.snyk)

    // https://github.com/asciidoctor/asciidoctor-gradle-plugin
    // FIXME https://github.com/asciidoctor/asciidoctor-gradle-plugin/issues/597
    alias(libs.plugins.asciidoctor)
    alias(libs.plugins.asciidoctorPdf)
    // Leanpub als Alternative zu PDF: https://github.com/asciidoctor/asciidoctor-leanpub-converter

    // https://github.com/nwillc/vplugin
    alias(libs.plugins.nwillc)

    // https://github.com/ben-manes/gradle-versions-plugin
    alias(libs.plugins.benManes)

    // https://github.com/markelliot/gradle-versions
    alias(libs.plugins.markelliot)

    // https://github.com/autonomousapps/dependency-analysis-android-gradle-plugin
    alias(libs.plugins.dependencyAnalysis)

    // https://github.com/jk1/Gradle-License-Report
    alias(libs.plugins.licenseReport)

    // https://github.com/gradle-dependency-analyze/gradle-dependency-analyze
    // https://github.com/jaredsburrows/gradle-license-plugin
    // https://github.com/hierynomus/license-gradle-plugin
}

defaultTasks = mutableListOf("compileTestKotlin")
group = "com.acme"
version = "1.0.0"

repositories {
    mavenCentral()

    // https://github.com/spring-projects/spring-framework/wiki/Spring-repository-FAQ
    // https://github.com/spring-projects/spring-framework/wiki/Release-Process
    maven("https://repo.spring.io/release") { mavenContent { releasesOnly() } }
    maven("https://repo.spring.io/milestone") { mavenContent { releasesOnly() } }

    // Snapshots von Spring (auch erforderlich fuer Snapshots von springdoc-openapi)
    // maven("https://repo.spring.io/snapshot") { mavenContent { snapshotsOnly() } }

    // Snapshots von springdoc-openapi
    // maven("https://s01.oss.sonatype.org/content/repositories/snapshots") { mavenContent { snapshotsOnly() } }

    // Snapshots von JaCoCo fuer Java 18
    // maven("https://oss.sonatype.org/content/repositories/snapshots") {
    //     mavenContent { snapshotsOnly() }
    //     // https://docs.gradle.org/current/userguide/jacoco_plugin.html#sec:jacoco_dependency_management
    //     content { onlyForConfigurations("jacocoAgent", "jacocoAnt") }
    // }
}

/* ktlint-disable comment-spacing */
@Suppress("CommentSpacing")
// https://docs.gradle.org/current/userguide/java_library_plugin.html#sec:java_library_separation
dependencies {
    //implementation(platform(libs.micrometerBom))
    implementation(platform(libs.jacksonBom))
    //implementation(platform(libs.nettyBom))
    //implementation(platform(libs.reactorBom))
    //implementation(platform(libs.springBom))
    //implementation(platform(libs.springDataBom))
    //implementation(platform(libs.springSecurityBom))
    //implementation(platform(libs.mockitoBom))
    //implementation(platform(libs.junitBom))
    implementation(platform(libs.allureBom))
    implementation(platform(libs.springBootBom))
    // spring-boot-starter-parent als "Parent POM"
    implementation(platform(libs.springdocOpenapiBom))

    // "Starters" enthalten sinnvolle Abhaengigkeiten, die man i.a. benoetigt
    // spring-boot-starter beinhaltet Spring Boot mit Actuator sowie spring-boot-starter-logging mit Logback
    implementation("org.springframework.boot:spring-boot-starter")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-webflux")
    implementation("org.springframework.boot:spring-boot-starter-tomcat")
    implementation("org.springframework.boot:spring-boot-starter-json")
    implementation("org.springframework.boot:spring-boot-starter-graphql")
    implementation("org.springframework.boot:spring-boot-starter-hateoas")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    // Auskommentieren fuer Beispiel 1
    //implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("org.springframework.security:spring-security-crypto")
    implementation("org.springframework.boot:spring-boot-starter-mail")
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("io.micrometer:micrometer-registry-prometheus")

    //compileOnly(libs.lombok)
    //annotationProcessor(libs.lombok)

    // https://docs.jboss.org/hibernate/orm/6.1/userguide/html_single/Hibernate_User_Guide.html#tooling-gradle-modelgen
    // QueryDSL unterstutzt nur JPA 2.2 und Hibernate 5.6
    annotationProcessor("org.hibernate:hibernate-jpamodelgen:6.1.5.Final")

    runtimeOnly("org.postgresql:postgresql")
    runtimeOnly("mysql:mysql-connector-java")
    runtimeOnly(libs.jansi)
    runtimeOnly(libs.bouncycastle) // Argon2

    // https://springdoc.org/v2/#swagger-ui-configuration
    // https://github.com/springdoc/springdoc-openapi
    // https://github.com/springdoc/springdoc-openapi-demos/wiki/springdoc-openapi-2.x-migration-guide
    // https://www.baeldung.com/spring-rest-openapi-documentation
    implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui")

    compileOnly(libs.spotbugsAnnotations)
    // https://github.com/spring-projects/spring-framework/issues/25095
    compileOnly("com.google.code.findbugs:jsr305:3.0.2")

    // https://docs.spring.io/spring-boot/docs/current/reference/htmlsingle/#using-boot-devtools
    // https://www.vojtechruzicka.com/spring-boot-devtools
    // runtimeOnly(libs.devtools)

    // einschl. JUnit und Mockito
    testImplementation("org.springframework.boot:spring-boot-starter-test") {
        exclude(group = "org.hamcrest", module = "hamcrest")
        exclude(group = "org.skyscreamer", module = "jsonassert")
        exclude(group = "org.xmlunit", module = "xmlunit-core")
    }
    testImplementation(libs.junitPlatformSuiteApi)
    testRuntimeOnly(libs.junitPlatformSuiteEngine)
    //testImplementation("org.springframework.security:spring-security-test")

    constraints {
        implementation(libs.annotations)
        //implementation(libs.springGraphQL)
        //implementation(libs.springHateoas)
        implementation(libs.jakartaPersistence)
        implementation(libs.hibernate)
        //implementation(libs.postgres)
        //implementation(libs.mysql)
        implementation(libs.hibernateValidator)
        //implementation(libs.bundles.tomcat)
        implementation(libs.tomcatCore)
        implementation(libs.tomcatEl)
        //implementation(libs.bundles.graphqlJavaBundle)
        //implementation(libs.graphqlJava)
        //implementation(libs.graphqlJavaDataloader)
        //implementation(libs.bundles.slf4jBundle)
        //implementation(libs.logback)
        //implementation(libs.springSecurityRsa)
        //implementation(libs.bundles.log4j)
        //implementation(libs.log4jApi)
        //implementation(libs.log4j2Slf4j)

        //testImplementation(libs.assertj)
    }
}
/* ktlint-enable comment-spacing */

// aktuelle Snapshots laden
// configurations.all {
//    resolutionStrategy { cacheChangingModulesFor(0, "seconds") }
// }

sweeney {
    enforce(mapOf("type" to "gradle", "expect" to "[7.6,7.6]"))
    // https://www.java.com/releases
    // https://devcenter.heroku.com/articles/java-support#specifying-a-java-version
    enforce(mapOf("type" to "jdk", "expect" to "[18.0.1.0,19.0.0]"))
    validate()
}

tasks.compileJava {
    // https://docs.gradle.org/current/dsl/org.gradle.api.tasks.compile.JavaCompile.html
    // https://docs.gradle.org/current/dsl/org.gradle.api.tasks.compile.CompileOptions.html
    // https://dzone.com/articles/gradle-goodness-enabling-preview-features-for-java
    options.isDeprecation = true
    options.compilerArgs.add("-Xlint:unchecked")
    options.compilerArgs.add("--enable-preview")
    //options.compilerArgs.add("-Xlint:preview")
    // ohne sourceCompatiblity und targetCompatibility:
    //options.release.set(18)
    // https://blog.gradle.org/incremental-compiler-avoidance#about-annotation-processors
}

tasks.compileTestJava {
    options.isDeprecation = true
    options.compilerArgs.add("-Xlint:unchecked")
    options.compilerArgs.add("--enable-preview")
}

java {
    // https://docs.gradle.org/current/userguide/java_plugin.html#sec:java-extension
    // https://docs.gradle.org/current/dsl/org.gradle.api.plugins.JavaPluginExtension.html
    toolchain {
        // einschl. sourceCompatibility und targetCompatibility
        languageVersion.set(JavaLanguageVersion.of(libs.versions.javaVersion.get()))
    }
}

lombok {
    version.set(libs.versions.lombok.get())
}

tasks.named<BootJar>("bootJar") {
    doLast {
        println("")
        println("Aufruf der ausfuehrbaren JAR-Datei:")
        @Suppress("MaxLineLength")
        println(
            "java -D'LOG_PATH=./build/log' -D'javax.net.ssl.trustStore=./src/main/resources/truststore.p12' -D'javax.net.ssl.trustStorePassword=zimmermann' -jar build/libs/${archiveFileName.get()} --spring.profiles.default=dev --spring.profiles.active=dev [--debug]", // ktlint-disable max-line-length
        )
        println("")
    }
}

// https://github.com/paketo-buildpacks/spring-boot
tasks.named<BootBuildImage>("bootBuildImage") {
    // "created 42 years ago" wegen Reproducability: https://medium.com/buildpacks/time-travel-with-pack-e0efd8bf05db

    // default:   imageName = "docker.io/${project.name}:${project.version}"
    val path = "juergenzimmermann"
    imageName.set("$path/${project.name}")
    val tag = System.getProperty("tag") ?: project.version.toString()
    tags.set(mutableListOf("$path/${project.name}:$tag"))

    // https://docs.spring.io/spring-boot/docs/current/gradle-plugin/reference/htmlsingle/#build-image.examples.builder-configuration
    // https://github.com/bell-sw/Liberica/releases
    @Suppress("StringLiteralDuplication")
    environment.set(
        mapOf(
            // https://github.com/paketo-buildpacks/bellsoft-liberica/releases
            // https://paketo.io/docs/howto/java/#use-an-alternative-jvm
            // default: 11
            "BP_JVM_VERSION" to "19.0.1+11",
            // https://github.com/paketo-buildpacks/bellsoft-liberica#configuration
            // https://github.com/paketo-buildpacks/spring-boot: Default=50 bei WebFlux statt 250
            // "BPL_JVM_THREAD_COUNT" to "250",
            // https://github.com/paketo-buildpacks/spring-boot
            "BP_SPRING_CLOUD_BINDINGS_ENABLED" to "false",
            "BPL_SPRING_CLOUD_BINDINGS_DISABLED" to "true",
        )
    )

    // Adoptium (Eclipse Temurin) statt Bellsoft Liberica (= default), einschl. Gradle, Spring Boot, ...
    // ABER: JRE nur fuer LTS (long term support) 8, 11, 17
    // https://paketo.io/docs/howto/java/#use-an-alternative-jvm
    // buildpacks = listOf(
    //     // https://github.com/paketo-buildpacks/ca-certificates
    //     "paketo-buildpacks/ca-certificates",
    //     // https://github.com/paketo-buildpacks/adoptium
    //     "gcr.io/paketo-buildpacks/adoptium",
    //     // https://github.com/paketo-buildpacks/java
    //     "paketo-buildpacks/java",
    // )

    // Podman statt Docker
    // docker {
    //    host = "unix:///run/user/1000/podman/podman.sock"
    //    isBindHostToBuilder = true
    // }
}

// Fuer Spring Native und logback.xml:
// https://docs.spring.io/spring-native/docs/current/reference/htmlsingle/#_starters_requiring_no_special_build_configuration
// https://docs.spring.io/spring-native/docs/current/reference/htmlsingle/#aot
// https://github.com/spring-projects-experimental/spring-native/issues/625
// https://github.com/spring-projects/spring-boot/issues/25847
// springAot {
//    removeXmlSupport.set(false)
// }

tasks.named<BootRun>("bootRun") {
    // java.util.Collections$EmptyList
    jvmArgs = (jvmArgs?.toMutableList() ?: mutableListOf()).also { it.add("--enable-preview") }

    // "System Properties", z.B. fuer Spring Properties oder fuer logback
    // https://docs.spring.io/spring-boot/docs/current/reference/html/application-properties.html#appendix.application-properties
    val port = System.getProperty("port")
    if (port != null) {
        systemProperty("server.port", port)
    }

    val noTls = System.getProperty("noTls")?.equals("true", ignoreCase = true) ?: false
    if (noTls) {
        @Suppress("StringLiteralDuplication")
        systemProperty("server.ssl.enabled", "false")
        @Suppress("StringLiteralDuplication")
        systemProperty("server.http2.enabled", "false")
    }

    systemProperty("spring.profiles.default", "dev")
    systemProperty("spring.profiles.active", "dev")
    systemProperty("spring.output.ansi.enabled", "ALWAYS")
    systemProperty("spring.config.location", "classpath:/application.yml")
    systemProperty("server.tomcat.basedir", "./build/tomcat")
    systemProperty("server.ssl.client-auth", "NONE")
    systemProperty("LOG_PATH", "./build/log")
    systemProperty("APP_DB_PASSWORD", "p")

    // TODO $env:TEMP\tomcat-docbase.*
    // TODO $env:TEMP\hsperfdata_cnb_<USERNAME> https://stackoverflow.com/questions/76327/how-can-i-prevent-java-from-creating-hsperfdata-files
    // systemProperty("spring.devtools.restart.enabled", "true")
    // systemProperty("spring.devtools.restart.trigger-file", ".reloadtrigger")

    systemProperty("APPLICATION_LOGLEVEL", "TRACE")
    // Logging der Header-Daten
    systemProperty("REQUEST_RESPONSE_LOGLEVEL", "TRACE")
    systemProperty("HIBERNATE_LOGLEVEL", "DEBUG")

    systemProperty("spring.datasource.password", "p")
}

tasks.test {
    useJUnitPlatform {
        includeTags = setOf("integration", "unit")

        // includeTags = setOf("integration")
        // includeTags = setOf("rest")
        // includeTags = setOf("rest_get")
        // includeTags = setOf("rest_write")
        // includeTags = setOf("graphql")
        // includeTags = setOf("query")
        // includeTags = setOf("mutation")

        // includeTags = setOf("unit")
        // includeTags = setOf("service_read")
        // includeTags = setOf("service_write")
    }

    val fork = System.getProperty("fork") ?: "1"
    maxParallelForks = fork.toInt()

    systemProperty("db.host", "localhost")
    systemProperty("javax.net.ssl.trustStore", "./src/main/resources/truststore.p12")
    systemProperty("javax.net.ssl.trustStorePassword", "zimmermann")
    systemProperty("junit.platform.output.capture.stdout", true)
    systemProperty("junit.platform.output.capture.stderr", true)
    systemProperty("spring.config.location", "classpath:/application.yml")
    // Tests ohne TLS und ohne HTTP2
    systemProperty("server.ssl.enabled", false)
    systemProperty("server.http2.enabled", false)
    systemProperty("server.ssl.client-auth", "NONE")
    systemProperty("server.tomcat.basedir", "./build/tomcat")

    // Umgebungsvariable, z.B. fuer Spring Properties, slf4j oder WebClient
    systemProperty("LOG_PATH", "./build/log")
    systemProperty("APPLICATION_LOGLEVEL", "TRACE")
    systemProperty("HIBERNATE_LOGLEVEL", "DEBUG")
    // systemProperty("HIBERNATE_LOGLEVEL", "TRACE")

    systemProperty("spring.datasource.password", "p")

    // https://docs.gradle.org/current/userguide/java_testing.html#sec:debugging_java_tests
    // https://www.jetbrains.com/help/idea/run-debug-configuration-junit.html
    // https://docs.gradle.org/current/userguide/java_testing.html#sec:debugging_java_tests
    // debug = true

    // finalizedBy("jacocoTestReport")
}

// https://docs.qameta.io/allure/#_gradle_2
allure {
    version.set(libs.versions.allure.get())
    adapter {
        frameworks {
            junit5 {
                adapterVersion.set(libs.versions.allureJunit.get())
                autoconfigureListeners.set(true)
                enabled.set(true)
            }
        }
        autoconfigure.set(true)
        aspectjWeaver.set(false)
        aspectjVersion.set(libs.versions.aspectjweaver.get())
    }

    // https://github.com/allure-framework/allure-gradle#customizing-allure-commandline-download
    // commandline {
    //     group.set("io.qameta.allure")
    //     module.set("allure-commandline")
    //     extension.set("zip")
    // }
}

jacoco {
    toolVersion = libs.versions.jacoco.get()
}

// https://docs.gradle.org/current/userguide/task_configuration_avoidance.html
// https://guides.gradle.org/migrating-build-logic-from-groovy-to-kotlin/#configuring-tasks
tasks.getByName<JacocoReport>("jacocoTestReport") {
    reports {
        xml.required.set(true)
        html.required.set(true)
    }

    // afterEvaluate gibt es nur bei getByName<> ("eager"), nicht bei named<> ("lazy")
    // https://docs.gradle.org/5.0/release-notes.html#configuration-avoidance-api-disallows-common-configuration-errors
    afterEvaluate {
        classDirectories.setFrom(
            files(
                classDirectories.files.map {
                    fileTree(it) { exclude("**/config/**", "**/entity/**") }
                },
            ),
        )
    }

    // https://github.com/gradle/gradle/pull/12626
    dependsOn(tasks.test)
}

tasks.getByName<JacocoCoverageVerification>("jacocoTestCoverageVerification") {
    violationRules {
        rule {
            limit {
                minimum = BigDecimal("0.75")
            }
        }
    }
}

checkstyle {
    toolVersion = libs.versions.checkstyle.get()
    // https://docs.gradle.org/current/dsl/org.gradle.api.plugins.quality.CheckstyleExtension.html
    configFile = file("extras/checkstyle.xml")
    setConfigProperties(
        "configDir" to "$projectDir/extras",
        // "checkstyleSuppressionsPath" to file("checkstyle-suppressions.xml").absolutePath,
    )
    isIgnoreFailures = false
}

tasks.withType<Checkstyle>().configureEach {
    reports {
        xml.required.set(true)
        html.required.set(true)
    }
}

spotbugs {
    // https://github.com/spotbugs/spotbugs/releases
    toolVersion.set(libs.versions.spotbugs.get())
}
tasks.spotbugsMain {
    reports.create("html") {
        required.set(true)
        outputLocation.set(file("$buildDir/reports/spotbugs.html"))
    }
    excludeFilter.set(file("extras/spotbugs-exclude.xml"))
}

// https://github.com/jeremylong/DependencyCheck/blob/master/src/site/markdown/dependency-check-gradle/configuration.md
dependencyCheck {
    scanConfigurations = listOf("runtimeClasspath")
    suppressionFile = "$projectDir/extras/owasp.xml"
    data(
        closureOf<org.owasp.dependencycheck.gradle.extension.DataExtension> {
            directory = "C:/Zimmermann/owasp-dependency-check"
            username = "dc"
            password = "p"
        },
    )

    analyzedTypes = listOf("jar")
    analyzers(
        closureOf<org.owasp.dependencycheck.gradle.extension.AnalyzerExtension> {
            // nicht benutzte Analyzer
            assemblyEnabled = false
            autoconfEnabled = false
            bundleAuditEnabled = false
            cmakeEnabled = false
            cocoapodsEnabled = false
            composerEnabled = false
            golangDepEnabled = false
            golangModEnabled = false
            nodeEnabled = false
            nugetconfEnabled = false
            nuspecEnabled = false
            pyDistributionEnabled = false
            pyPackageEnabled = false
            rubygemsEnabled = false
            swiftEnabled = false

            nodeAudit(closureOf<org.owasp.dependencycheck.gradle.extension.NodeAuditExtension> { enabled = true })
            retirejs(closureOf<org.owasp.dependencycheck.gradle.extension.RetireJSExtension> { enabled = true })
            // ossIndex(closureOf<org.owasp.dependencycheck.gradle.extension.OssIndexExtension> { enabled = true })
        },
    )

    format = org.owasp.dependencycheck.reporting.ReportGenerator.Format.ALL
}

snyk {
    setArguments("--configuration-matching=implementation|runtimeOnly")
    setSeverity("low")
    setApi("40df2078-e1a3-4f28-b913-e2babbe427fd")
}

// forbiddenApis {
//    // https://github.com/policeman-tools/forbidden-apis/wiki/BundledSignatures
//    // https://github.com/policeman-tools/forbidden-apis/blob/main/src/main/docs/bundled-signatures.html
//    bundledSignatures = setOf(
//        "jdk-unsafe",
//        "jdk-deprecated",
//        "jdk-internal",
//        "jdk-non-portable",
//        "jdk-system-out",
//        "jdk-reflection",
//    )
// }

tasks.javadoc {
    options {
        showFromPackage()
        // outputLevel = org.gradle.external.javadoc.JavadocOutputLevel.VERBOSE
        this as CoreJavadocOptions
        addStringOption("-author")
        // Keine bzw. nur elementare Warnings anzeigen wegen Lombok
        // https://stackoverflow.com/questions/52205209/configure-gradle-build-to-suppress-javadoc-console-warnings
        addStringOption("Xdoclint:none", "-quiet")
    }
}

tasks.getByName<AsciidoctorTask>("asciidoctor") {
    asciidoctorj {
        setVersion(libs.versions.asciidoctorj.get())
        // requires("asciidoctor-diagram")

        modules {
            diagram.use()
            diagram.setVersion(libs.versions.asciidoctorjDiagram.get())
        }
    }

    val separator = System.getProperty("file.separator")
    @Suppress("StringLiteralDuplication")
    setBaseDir(file("extras${separator}doc"))
    setSourceDir(file("extras${separator}doc"))
    // setOutputDir(file("$buildDir/docs/asciidoc"))
    logDocuments = true

    // https://github.com/asciidoctor/asciidoctor-gradle-plugin/issues/597#issuecomment-844352804
    inProcess = org.asciidoctor.gradle.base.process.ProcessMode.JAVA_EXEC
    forkOptions {
        @Suppress("StringLiteralDuplication")
        jvmArgs("--add-opens", "java.base/sun.nio.ch=ALL-UNNAMED", "--add-opens", "java.base/java.io=ALL-UNNAMED")
    }

    doLast {
        @Suppress("MaxLineLength")
        println(
            "Das Entwicklerhandbuch ist in $buildDir${separator}docs${separator}asciidoc${separator}entwicklerhandbuch.html", // ktlint-disable max-line-length
        )
    }
}

tasks.getByName<AsciidoctorPdfTask>("asciidoctorPdf") {
    asciidoctorj {
        setVersion(libs.versions.asciidoctorj.get())

        modules {
            diagram.use()
            diagram.setVersion(libs.versions.asciidoctorjDiagram.get())
            pdf.setVersion(libs.versions.asciidoctorjPdf.get())
        }
    }

    val separator = System.getProperty("file.separator")
    setBaseDir(file("extras${separator}doc"))
    setSourceDir(file("extras${separator}doc"))
    attributes(mapOf("pdf-page-size" to "A4"))
    logDocuments = true

    // https://github.com/asciidoctor/asciidoctor-gradle-plugin/issues/597#issuecomment-844352804
    inProcess = org.asciidoctor.gradle.base.process.ProcessMode.JAVA_EXEC
    forkOptions {
        jvmArgs("--add-opens", "java.base/sun.nio.ch=ALL-UNNAMED", "--add-opens", "java.base/java.io=ALL-UNNAMED")
    }

    doLast {
        @Suppress("MaxLineLength")
        println(
            "Das Entwicklerhandbuch ist in $buildDir${separator}docs${separator}asciidocPdf${separator}entwicklerhandbuch.pdf", // ktlint-disable max-line-length
        )
    }
}

licenseReport {
    configurations = arrayOf("runtimeClasspath")
}

tasks.getByName<DependencyUpdatesTask>("dependencyUpdates") {
    checkConstraints = true
}

idea {
    module {
        isDownloadJavadoc = true
        // https://stackoverflow.com/questions/59950657/querydsl-annotation-processor-and-gradle-plugin
        sourceDirs.add(file("generated/"))
        generatedSourceDirs.add(file("generated/"))
    }
}
