package jpja.webapp.application;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import jpja.webapp.service.LogParserService;

@SpringBootTest
public class RegexTest {
    private LogParserService parserService;

    @BeforeEach
    void setUp() {
        parserService = new LogParserService();
    }

    @Test
    void testLogPatternBasic() throws Exception {
        String sample = "2024-12-17 20:16:53 INFO  [main] o.a.catalina.core.StandardService - Starting service [Tomcat]";
        Pattern pattern = parserService.getLogPattern();
        Matcher matcher = pattern.matcher(sample);
        assertTrue(matcher.find());
        assertEquals(matcher.group(1), "2024-12-17 20:16:53");
        assertEquals(matcher.group(2), "INFO");
        assertEquals(matcher.group(3), "main");
        assertEquals(matcher.group(4), "o.a.catalina.core.StandardService");
        assertEquals(matcher.group(5), "Starting service [Tomcat]");
    }

    @Test
    void activityTest() throws Exception {
        Pattern ACTIVITY_PATTERN = parserService.getActivityPattern();
        String sample = "2025-01-06 01:43:29 [ACTIVITY] IP: 57.152.56.111 - Method: GET - URI: / - Query: null";
        String[] expected = { "2025-01-06 01:43:29", "ACTIVITY", "57.152.56.111", "GET", "/", "null" };
        regexTest(ACTIVITY_PATTERN, sample, null, expected, true);
    }

    private void regexTest(Pattern pattern, String sample, String[] groups, String[] expectedValues, boolean print)
            throws Exception {
        Matcher matcher = pattern.matcher(sample);
        assertTrue(matcher.find());
        for (int i = 0; i < expectedValues.length; i++) {
            String captured;
            if (groups == null) {
                captured = matcher.group(i+1);
            } else {
                captured = matcher.group(groups[i]);
            }
            if (print) {
                System.out.println("" + (groups == null ? i+1 : groups[i]) + ": '" + captured + "'");
            }
            assertEquals(expectedValues[i], captured);
        }
    }

    /*
     * String sample =
     * "java.lang.IllegalArgumentException: Invalid character found in the request target ---. The valid characters are defined in RFC 7230 and RFC 3986\r\n"
     * + //
     * "at org.apache.coyote.http11.Http11InputBuffer.parseRequestLine(Http11InputBuffer.java:481)\r\n"
     * + //
     * "\tat org.apache.coyote.http11.Http11Processor.service(Http11Processor.java:270)\r\n"
     * + //
     * "\tat org.apache.coyote.AbstractProcessorLight.process(AbstractProcessorLight.java:63)\r\n"
     * + //
     * "\tat org.apache.coyote.AbstractProtocol$ConnectionHandler.process(AbstractProtocol.java:905)\r\n"
     * + //
     * "\tat org.apache.tomcat.util.net.NioEndpoint$SocketProcessor.doRun(NioEndpoint.java:1741)"
     * ;
     */
    @Test
    void testWithMf() throws Exception {
        Pattern pattern = Pattern.compile(
                "(?sm)("
                        + "^"
                        + "(?<mainException>[A-Za-z0-9_$.\\[\\]]+(?:Exception|Error|Throwable))" // #1 Main exception
                        + ": (?<message>[^\\r\\n]*)" // #2 message
                        + "(?<mainFrames>(\\r?\\n\\s*at\\s+[^\\r\\n]*)+))");

        // pattern =
        // Pattern.compile("(?sm)(^(?<mainException>[A-Za-z0-9_$.\\[\\]]+(?:Exception|Error|Throwable)):
        // (?<message>[^\\r\\n]*)(?<note>(?:\\r?\\n^Note:\\s+.?))?\\n^\\s*(?<mainFrames>(?:at\\s+[^\\r\\n]*)+))");
        String sample = "java.lang.IllegalArgumentException: Invalid character found in the request target ---. The valid characters are defined in RFC 7230 and RFC 3986\r\n"
                + //
                "\tat org.apache.coyote.http11.Http11InputBuffer.parseRequestLine(Http11InputBuffer.java:481)\r\n" + //
                "\tat org.apache.coyote.http11.Http11Processor.service(Http11Processor.java:270)\r\n" + //
                "\tat org.apache.coyote.AbstractProcessorLight.process(AbstractProcessorLight.java:63)\r\n" + //
                "\tat org.apache.coyote.AbstractProtocol$ConnectionHandler.process(AbstractProtocol.java:905)\r\n" + //
                "\tat org.apache.tomcat.util.net.NioEndpoint$SocketProcessor.doRun(NioEndpoint.java:1741)";
        Matcher matcher = pattern.matcher(sample);
        assertTrue(matcher.find(), "mf find() failed");
        assertEquals(matcher.group("mainException"), "java.lang.IllegalArgumentException");
        assertEquals(matcher.group("message"),
                "Invalid character found in the request target ---. The valid characters are defined in RFC 7230 and RFC 3986");
        // System.out.println("mf: " + matcher.group("mainFrames")+ " - end");
        List<String> frames = splitFrames(matcher.group("mainFrames"));
        String[] expected = { "org.apache.coyote.http11.Http11InputBuffer.parseRequestLine(Http11InputBuffer.java:481)",
                "org.apache.coyote.http11.Http11Processor.service(Http11Processor.java:270)",
                "org.apache.coyote.AbstractProcessorLight.process(AbstractProcessorLight.java:63)",
                "org.apache.coyote.AbstractProtocol$ConnectionHandler.process(AbstractProtocol.java:905)",
                "org.apache.tomcat.util.net.NioEndpoint$SocketProcessor.doRun(NioEndpoint.java:1741)"
        };
        mfTester(frames, expected, false);

    }

    @Test
    void testStackTraceBasic() throws Exception {

        Pattern pattern = Pattern.compile(
                "(?sm)("
                        + "^"
                        + "(?<mainException>[A-Za-z0-9_$.\\[\\]]+(?:Exception|Error|Throwable))" // #1 Main exception
                        + ": (?<message>[^\\r\\n]*)" // #2 message
                        + "(?<mainFrames>(\\r?\\n\\s*at\\s+[^\\r\\n]*)+)" // #3 Main stack frames
                        + "(?<causedByChain>(?:\\r?\\n" // #4 Repeat caused by
                        + "^Caused by: .*?"
                        + "(?:.*?(?:\\r?\\n^\\s+at\\s+.*\\r?\\n)+)?"
                        + "))*"
                        + "(?:\\r?\\n^\\s*\\.\\.\\.\\s*(?<omittedCount>\\d+\\s+)common frames omitted\\s*)?" // #5 "… N
                                                                                                             // frames"
                                                                                                             // line
                        + ")");

        String sample = "java.lang.IllegalArgumentException: Invalid character found in the request target ---. The valid characters are defined in RFC 7230 and RFC 3986\r\n"
                + //
                "\tat org.apache.coyote.http11.Http11InputBuffer.parseRequestLine(Http11InputBuffer.java:481)\r\n" + //
                "\tat org.apache.coyote.http11.Http11Processor.service(Http11Processor.java:270)\r\n" + //
                "\tat org.apache.coyote.AbstractProcessorLight.process(AbstractProcessorLight.java:63)\r\n" + //
                "\tat org.apache.coyote.AbstractProtocol$ConnectionHandler.process(AbstractProtocol.java:905)\r\n" + //
                "\tat org.apache.tomcat.util.net.NioEndpoint$SocketProcessor.doRun(NioEndpoint.java:1741)\r\n" +
                "\tat org.apache.tomcat.util.net.SocketProcessorBase.run(SocketProcessorBase.java:52)\r\n" + //
                "\tat org.apache.tomcat.util.threads.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1190)\r\n" + //
                "\tat org.apache.tomcat.util.threads.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:659)\r\n" + //
                "\tat org.apache.tomcat.util.threads.TaskThread$WrappingRunnable.run(TaskThread.java:63)\r\n" + //
                "\tat java.base/java.lang.Thread.run(Thread.java:1575)";
        Matcher matcher = pattern.matcher(sample);
        assertTrue(matcher.find());
        // System.out.println("mainException: " + matcher.group("mainException"));
        assertEquals(matcher.group("mainException"), "java.lang.IllegalArgumentException");
        // System.out.println("message: " + matcher.group("message"));
        // System.out.println("mainFrames: " + matcher.group("mainFrames"));
        assertEquals(matcher.group("message"),
                "Invalid character found in the request target ---. The valid characters are defined in RFC 7230 and RFC 3986");
        List<String> frames = splitFrames(matcher.group("mainFrames"));
        String[] expected = { "org.apache.coyote.http11.Http11InputBuffer.parseRequestLine(Http11InputBuffer.java:481)",
                "org.apache.coyote.http11.Http11Processor.service(Http11Processor.java:270)",
                "org.apache.coyote.AbstractProcessorLight.process(AbstractProcessorLight.java:63)",
                "org.apache.coyote.AbstractProtocol$ConnectionHandler.process(AbstractProtocol.java:905)",
                "org.apache.tomcat.util.net.NioEndpoint$SocketProcessor.doRun(NioEndpoint.java:1741)",
                "org.apache.tomcat.util.net.SocketProcessorBase.run(SocketProcessorBase.java:52)",
                "org.apache.tomcat.util.threads.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1190)",
                "org.apache.tomcat.util.threads.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:659)",
                "org.apache.tomcat.util.threads.TaskThread$WrappingRunnable.run(TaskThread.java:63)",
                "java.base/java.lang.Thread.run(Thread.java:1575)"
        };
        mfTester(frames, expected, false);
    }

    @Test
    void testWithCausedBy() throws Exception {
        String sample = "org.hibernate.tool.schema.spi.CommandAcceptanceException: Error executing DDL \"alter table if exists address_type_join add constraint FKrl4cmxkra4fp46m70kxh6hli8 foreign key (type_id) references address_names (id)\" via JDBC [(conn=511) Cannot add or update a child row: a foreign key constraint fails (`cleaners`.`#sql-alter-447-1ff`, CONSTRAINT `FKrl4cmxkra4fp46m70kxh6hli8` FOREIGN KEY (`type_id`) REFERENCES `address_names` (`id`))]\r\n"
                + //
                "\tat org.hibernate.tool.schema.internal.exec.GenerationTargetToDatabase.accept(GenerationTargetToDatabase.java:94)\r\n"
                + //
                "\tat org.hibernate.tool.schema.internal.AbstractSchemaMigrator.applySqlString(AbstractSchemaMigrator.java:583)\r\n"
                + //
                "\tat org.hibernate.tool.schema.internal.AbstractSchemaMigrator.applySqlStrings(AbstractSchemaMigrator.java:523)\r\n"
                + //
                "\tat org.hibernate.tool.schema.internal.AbstractSchemaMigrator.applyForeignKeys(AbstractSchemaMigrator.java:455)\r\n"
                + //
                "\tat org.hibernate.tool.schema.internal.AbstractSchemaMigrator.performMigration(AbstractSchemaMigrator.java:276)\r\n"
                + //
                "\tat org.hibernate.tool.schema.internal.AbstractSchemaMigrator.doMigration(AbstractSchemaMigrator.java:119)\r\n"
                + //
                "\tat org.hibernate.tool.schema.spi.SchemaManagementToolCoordinator.performDatabaseAction(SchemaManagementToolCoordinator.java:280)\r\n"
                + //
                "\tat org.hibernate.tool.schema.spi.SchemaManagementToolCoordinator.lambda$process$5(SchemaManagementToolCoordinator.java:144)\r\n"
                + //
                "\tat java.base/java.util.HashMap.forEach(HashMap.java:1429)\r\n" + //
                "\tat org.hibernate.tool.schema.spi.SchemaManagementToolCoordinator.process(SchemaManagementToolCoordinator.java:141)\r\n"
                + //
                "\tat org.hibernate.boot.internal.SessionFactoryObserverForSchemaExport.sessionFactoryCreated(SessionFactoryObserverForSchemaExport.java:37)\r\n"
                + //
                "\tat org.hibernate.internal.SessionFactoryObserverChain.sessionFactoryCreated(SessionFactoryObserverChain.java:35)\r\n"
                + //
                "\tat org.hibernate.internal.SessionFactoryImpl.<init>(SessionFactoryImpl.java:322)\r\n" + //
                "\tat org.hibernate.boot.internal.SessionFactoryBuilderImpl.build(SessionFactoryBuilderImpl.java:457)\r\n"
                + //
                "\tat org.hibernate.jpa.boot.internal.or.SpringHibernateJpaPersistenceProvider.createContainerEntityManagerFactory(SpringHibernateJpaPersistenceProvider.java:75)\r\n"
                + //
                "\tat org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean.createNativeEntityManagerFactory(LocalContainerEntityManagerFactoryBean.java:390)\r\n"
                + //
                "\tat org.springframework.orm.jpa.AbstractEntityManagerFactoryBean.buildNativeEntityManagerFactory(AbstractEntityManagerFactoryBean.java:409)\r\n"
                + //
                "\tat org.springframework.orm.jpa.AbsEntityManagerFactoryBuilderImpl.build(EntityManagerFactoryBuilderImpl.java:1506)\r\n"
                + //
                "\tat org.springframework.orm.jpa.vendtractEntityManagerFactoryBean.afterPropertiesSet(AbstractEntityManagerFactoryBean.java:396)\r\n"
                + //
                "\tat org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean.afterPropertiesSet(LocalContainerEntityManagerFactoryBean.java:366)\r\n"
                + //
                "\tat org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory.invokeInitMethods(AbstractAutowireCapableBeanFactory.java:1853)\r\n"
                + //
                "\tat org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory.initializeBean(AbstractAutowireCapableBeanFactory.java:1802)\r\n"
                + //
                "\tat org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory.doCreateBean(AbstractAutowireCapableBeanFactory.java:600)\r\n"
                + //
                "\tat org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory.createBean(AbstractAutowireCapableBeanFactory.java:522)\r\n"
                + //
                "\tat org.springframework.beans.factory.support.AbstractBeanFactory.lambda$doGetBean$0(AbstractBeanFactory.java:337)\r\n"
                + //
                "\tat org.springframework.beans.factory.support.DefaultSingletonBeanRegistry.getSingleton(DefaultSingletonBeanRegistry.java:234)\r\n"
                + //
                "\tat org.springframework.beans.factory.support.AbstractBeanFactory.doGetBean(AbstractBeanFactory.java:335)\r\n"
                + //
                "\tat org.springframework.beans.factory.support.AbstractBeanFactory.getBean(AbstractBeanFactory.java:205)\r\n"
                + //
                "\tat org.springframework.context.support.AbstractApplicationContext.finishBeanFactoryInitialization(AbstractApplicationContext.java:954)\r\n"
                + //
                "\tat org.springframework.context.support.AbstractApplicationContext.refresh(AbstractApplicationContext.java:625)\r\n"
                + //
                "\tat org.springframework.boot.web.servlet.context.ServletWebServerApplicationContext.refresh(ServletWebServerApplicationContext.java:146)\r\n"
                + //
                "\tat org.springframework.boot.SpringApplication.refresh(SpringApplication.java:754)\r\n" + //
                "\tat org.springframework.boot.SpringApplication.refreshContext(SpringApplication.java:456)\r\n" + //
                "\tat org.springframework.boot.SpringApplication.run(SpringApplication.java:335)\r\n" + //
                "\tat org.springframework.boot.SpringApplication.run(SpringApplication.java:1363)\r\n" + //
                "\tat org.springframework.boot.SpringApplication.run(SpringApplication.java:1352)\r\n" + //
                "\tat jpja.webapp.application.Application.main(Application.java:18)\r\n" + //
                "Caused by: java.sql.SQLIntegrityConstraintViolationException: (conn=511) Cannot add or update a child row: a foreign key constraint fails (`cleaners`.`#sql-alter-447-1ff`, CONSTRAINT `FKrl4cmxkra4fp46m70kxh6hli8` FOREIGN KEY (`type_id`) REFERENCES `address_names` (`id`))\r\n"
                + //
                "\tat org.mariadb.jdbc.export.ExceptionFactory.createException(ExceptionFactory.java:297)\r\n" + //
                "\tat org.mariadb.jdbc.export.ExceptionFactory.create(ExceptionFactory.java:378)\r\n" + //
                "\tat org.mariadb.jdbc.message.ClientMessage.readPacket(ClientMessage.java:189)\r\n" + //
                "\tat org.mariadb.jdbc.client.impl.StandardClient.readPacket(StandardClient.java:1235)\r\n" + //
                "\tat org.mariadb.jdbc.client.impl.StandardClient.readResults(StandardClient.java:1174)\r\n" + //
                "\tat org.mariadb.jdbc.client.impl.StandardClient.readResponse(StandardClient.java:1093)\r\n" + //
                "\tat org.mariadb.jdbc.client.impl.StandardClient.execute(StandardClient.java:1017)\r\n" + //
                "\tat org.mariadb.jdbc.Statement.executeInternal(Statement.java:1034)\r\n" + //
                "\tat org.mariadb.jdbc.Statement.execute(Statement.java:1163)\r\n" + //
                "\tat org.mariadb.jdbc.Statement.execute(Statement.java:502)\r\n" + //
                "\tat com.zaxxer.hikari.pool.ProxyStatement.execute(ProxyStatement.java:94)\r\n" + //
                "\tat com.zaxxer.hikari.pool.HikariProxyStatement.execute(HikariProxyStatement.java)\r\n" + //
                "\tat org.hibernate.tool.schema.internal.exec.GenerationTargetToDatabase.accept(GenerationTargetToDatabase.java:80)\r\n"
                + //
                "\t... 36 common frames omitted";
        Pattern pattern = Pattern.compile(
                "(?sm)("
                        + "^"
                        + "(?<mainException>[A-Za-z0-9_$.\\[\\]]+(?:Exception|Error|Throwable))" // #1 Main exception
                        + ": (?<message>[^\\r\\n]*)" // #2 message
                        + "(?<mainFrames>(\\r?\\n\\s*at\\s+[^\\r\\n]*)+)" // #3 Main stack frames
                        + "(?<causedByChain>(?:\\r?\\n" // #4 Repeat caused by
                        + "^Caused by: .*?"
                        + "(?:.*?(?:\\r?\\n^\\s+at\\s+.*\\r?\\n)+)?"
                        + "))*"
                        + "(?:\\r?\\n^\\s*\\.\\.\\.\\s*(?<omittedCount>\\d+\\s+)common frames omitted\\s*)?" // #5 "… N
                                                                                                             // frames"
                                                                                                             // line
                        + ")");
        Matcher matcher = pattern.matcher(sample);
        assertTrue(matcher.find());
        //System.out.println("mainException: " + matcher.group("mainException"));
        assertEquals(matcher.group("mainException"), "org.hibernate.tool.schema.spi.CommandAcceptanceException");
        //System.out.println("message: " + matcher.group("message"));
        assertEquals(matcher.group("message"),
                "Error executing DDL \"alter table if exists address_type_join add constraint FKrl4cmxkra4fp46m70kxh6hli8 foreign key (type_id) references address_names (id)\" via JDBC [(conn=511) Cannot add or update a child row: a foreign key constraint fails (`cleaners`.`#sql-alter-447-1ff`, CONSTRAINT `FKrl4cmxkra4fp46m70kxh6hli8` FOREIGN KEY (`type_id`) REFERENCES `address_names` (`id`))]");
        // System.out.println("mainFrames: " + matcher.group("mainFrames"));
        List<String> frames = splitFrames(matcher.group("mainFrames"));
        String[] firstExpectedMf = {
                "org.hibernate.tool.schema.internal.exec.GenerationTargetToDatabase.accept(GenerationTargetToDatabase.java:94)", //
                "org.hibernate.tool.schema.internal.AbstractSchemaMigrator.applySqlString(AbstractSchemaMigrator.java:583)", //
                "org.hibernate.tool.schema.internal.AbstractSchemaMigrator.applySqlStrings(AbstractSchemaMigrator.java:523)", //
                "org.hibernate.tool.schema.internal.AbstractSchemaMigrator.applyForeignKeys(AbstractSchemaMigrator.java:455)", //
                "org.hibernate.tool.schema.internal.AbstractSchemaMigrator.performMigration(AbstractSchemaMigrator.java:276)", //
                "org.hibernate.tool.schema.internal.AbstractSchemaMigrator.doMigration(AbstractSchemaMigrator.java:119)", //
                "org.hibernate.tool.schema.spi.SchemaManagementToolCoordinator.performDatabaseAction(SchemaManagementToolCoordinator.java:280)", //
                "org.hibernate.tool.schema.spi.SchemaManagementToolCoordinator.lambda$process$5(SchemaManagementToolCoordinator.java:144)", //
                "java.base/java.util.HashMap.forEach(HashMap.java:1429)", //
                "org.hibernate.tool.schema.spi.SchemaManagementToolCoordinator.process(SchemaManagementToolCoordinator.java:141)", //
                "org.hibernate.boot.internal.SessionFactoryObserverForSchemaExport.sessionFactoryCreated(SessionFactoryObserverForSchemaExport.java:37)", //
                "org.hibernate.internal.SessionFactoryObserverChain.sessionFactoryCreated(SessionFactoryObserverChain.java:35)", //
                "org.hibernate.internal.SessionFactoryImpl.<init>(SessionFactoryImpl.java:322)", //
                "org.hibernate.boot.internal.SessionFactoryBuilderImpl.build(SessionFactoryBuilderImpl.java:457)", //
                "org.hibernate.jpa.boot.internal.or.SpringHibernateJpaPersistenceProvider.createContainerEntityManagerFactory(SpringHibernateJpaPersistenceProvider.java:75)", //
                "org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean.createNativeEntityManagerFactory(LocalContainerEntityManagerFactoryBean.java:390)", //
                "org.springframework.orm.jpa.AbstractEntityManagerFactoryBean.buildNativeEntityManagerFactory(AbstractEntityManagerFactoryBean.java:409)", //
                "org.springframework.orm.jpa.AbsEntityManagerFactoryBuilderImpl.build(EntityManagerFactoryBuilderImpl.java:1506)", //
                "org.springframework.orm.jpa.vendtractEntityManagerFactoryBean.afterPropertiesSet(AbstractEntityManagerFactoryBean.java:396)", //
                "org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean.afterPropertiesSet(LocalContainerEntityManagerFactoryBean.java:366)", //
                "org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory.invokeInitMethods(AbstractAutowireCapableBeanFactory.java:1853)", //
                "org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory.initializeBean(AbstractAutowireCapableBeanFactory.java:1802)", //
                "org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory.doCreateBean(AbstractAutowireCapableBeanFactory.java:600)", //
                "org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory.createBean(AbstractAutowireCapableBeanFactory.java:522)", //
                "org.springframework.beans.factory.support.AbstractBeanFactory.lambda$doGetBean$0(AbstractBeanFactory.java:337)", //
                "org.springframework.beans.factory.support.DefaultSingletonBeanRegistry.getSingleton(DefaultSingletonBeanRegistry.java:234)", //
                "org.springframework.beans.factory.support.AbstractBeanFactory.doGetBean(AbstractBeanFactory.java:335)", //
                "org.springframework.beans.factory.support.AbstractBeanFactory.getBean(AbstractBeanFactory.java:205)", //
                "org.springframework.context.support.AbstractApplicationContext.finishBeanFactoryInitialization(AbstractApplicationContext.java:954)", //
                "org.springframework.context.support.AbstractApplicationContext.refresh(AbstractApplicationContext.java:625)", //
                "org.springframework.boot.web.servlet.context.ServletWebServerApplicationContext.refresh(ServletWebServerApplicationContext.java:146)", //
                "org.springframework.boot.SpringApplication.refresh(SpringApplication.java:754)", //
                "org.springframework.boot.SpringApplication.refreshContext(SpringApplication.java:456)", //
                "org.springframework.boot.SpringApplication.run(SpringApplication.java:335)", //
                "org.springframework.boot.SpringApplication.run(SpringApplication.java:1363)", //
                "org.springframework.boot.SpringApplication.run(SpringApplication.java:1352)", //
                "jpja.webapp.application.Application.main(Application.java:18)" };
        //System.out.println("frames.size = " + frames.size() + ". expectedValues.length = " + firstExpectedMf.length);
        mfTester(frames, firstExpectedMf, false);
        //System.out.println("CausedBy = " + matcher.group("causedByChain"));

    }

    // old
    /*
     * Pattern pattern = Pattern.compile(
     * "(?sm)("
     * + "^"
     * + "(?<mainException>[A-Za-z0-9_$.\\[\\]]+(?:Exception|Error|Throwable))" //
     * #1 Main exception
     * + ": (?<message>(.*)?)" // #2 message
     * + "(?<note>(?:\\r?\\n^Note:\\s+.?))?"
     * + "\\n(?<mainFrames>(?:.*?(?:\\r?^\\s+at\\s+.*)+))" // #3 Main stack frames
     * + "(?<causedByChain>(?:\\r?\\n" // #4 Repeat caused by
     * + "^Caused by: .*?"
     * + "(?:.*?(?:\\r?\\n^\\s+at\\s+.*\\r?\\n)+)?"
     * + ")*)"
     * +
     * "(?:\\r?\\n^\\s*\\.\\.\\.\\s*(<omittedCount>\\d+\\s+)common frames omitted\\s*)?"
     * // #5 "… N frames" line
     * + ")");
     */
    // new
    /*
     * Pattern pattern = Pattern.compile(
     * "(?sm)("
     * + "^"
     * + "(?<mainException>[A-Za-z0-9_$.\\[\\]]+(?:Exception|Error|Throwable))" //
     * #1 Main exception
     * + ": (?<message>[^\\r\\n]*)" // #2 Message (no newlines)
     * + "(?<note>(?:\\r?\\n^Note:\\s+.*))?" // Optional Note
     * + "\\n(?<mainFrames>(?:\\s*?\\t?*at\\s+.*(?:\\r?\\n|$))+)" // #3 Main stack
     * frames
     * + "(?<causedByChain>(?:\\r?\\n" // #4 Repeat caused by
     * + "^Caused by: .*?"
     * + "(?:\\r?\\n\\s+at\\s+.*(?:\\r?\\n|$))+)?)*" // Nested stack frames
     * +
     * "(?:\\r?\\n^\\s*\\.\\.\\.\\s*(?<omittedCount>\\d+)\\s+common frames omitted\\s*)?"
     * // #5 "… N frames" line
     * + ")"
     * );
     */
    @Test
    void testMainException() throws Exception {
        Pattern patternMain = Pattern.compile(
                "(?sm)(^(?<mainException>[A-Za-z0-9_$.\\[\\]]+(?:Exception|Error|Throwable)))");
        String sampleMain = "java.lang.IllegalArgumentException";
        Matcher matcher = patternMain.matcher(sampleMain);
        assertTrue(matcher.find(), "mainException find() failed");
        assertEquals(matcher.group("mainException"), "java.lang.IllegalArgumentException");
    }

    @Test
    void testMsg() throws Exception {
        String sampleMsg = ": Invalid character found in the request target ---. The valid characters are defined in RFC 7230 and RFC 3986\r\n";
        Pattern patternMsg = Pattern.compile(": (?<message>[^\\r\\n]*)");
        Matcher matcher = patternMsg.matcher(sampleMsg);
        assertTrue(matcher.find(), "message find() failed");
        assertEquals(matcher.group("message"),
                "Invalid character found in the request target ---. The valid characters are defined in RFC 7230 and RFC 3986");
    }

    @Test
    void testCombined() throws Exception {

        Pattern combined = Pattern.compile("(?sm)("
                + "^"
                + "(?<mainException>[A-Za-z0-9_$.\\[\\]]+(?:Exception|Error|Throwable))" // #1 Main exception
                + ": (?<message>[^\\r\\n]*))");
        String sample = "java.lang.IllegalArgumentException: Invalid character found in the request target ---. The valid characters are defined in RFC 7230 and RFC 3986\r\n";
        Matcher matcher = combined.matcher(sample);
        assertTrue(matcher.find(), "combined find() failed");
        assertEquals(matcher.group("mainException"), "java.lang.IllegalArgumentException");
        assertEquals(matcher.group("message"),
                "Invalid character found in the request target ---. The valid characters are defined in RFC 7230 and RFC 3986");
    }

    // b
    @Test
    void testWithNote() throws Exception {
        Pattern pattern = Pattern.compile(
                "(?sm)("
                        + "^"
                        + "(?<mainException>[A-Za-z0-9_$.\\[\\]]+(?:Exception|Error|Throwable))" // #1 Main exception
                        + ": (?<message>[^\\r\\n]*)" // #2 message
                        + "(?<note>(?:\\r?\\n^Note:\\s+.?))?)");
        String sample = "java.lang.IllegalArgumentException: Invalid character found in the request target ---. The valid characters are defined in RFC 7230 and RFC 3986\r\n"
                + //
                "\tat org.apache.coyote.http11.Http11InputBuffer.parseRequestLine(Http11InputBuffer.java:481)\r\n";
        Matcher matcher = pattern.matcher(sample);
        assertTrue(matcher.find(), "note find() failed");
        assertEquals(matcher.group("mainException"), "java.lang.IllegalArgumentException");
        assertEquals(matcher.group("message"),
                "Invalid character found in the request target ---. The valid characters are defined in RFC 7230 and RFC 3986");
        // System.out.println("Note: " + matcher.group("note"));
    }

    // c
    @Test
    void testJustMainFrames() throws Exception {
        Pattern pattern = Pattern.compile(
                "(?sm)(?<mainFrames>(\\r?\\n\\s*at\\s+[^\\r\\n]*)+)");
        String sample = "java.lang.IllegalArgumentException: Invalid character found in the request target ---. The valid characters are defined in RFC 7230 and RFC 3986\r\n"
                + //
                "\tat org.apache.coyote.http11.Http11InputBuffer.parseRequestLine(Http11InputBuffer.java:481)\r\n" + //
                "\tat org.apache.coyote.http11.Http11Processor.service(Http11Processor.java:270)\r\n";
        Matcher matcher = pattern.matcher(sample);
        assertTrue(matcher.find(), "mf find() failed");
        String mainFrames = matcher.group("mainFrames");
        // System.out.println("mainFrames: " + matcher.group("mainFrames"));
        List<String> frames = splitFrames(mainFrames);
        String[] expected = { "org.apache.coyote.http11.Http11InputBuffer.parseRequestLine(Http11InputBuffer.java:481)",
                "org.apache.coyote.http11.Http11Processor.service(Http11Processor.java:270)" };
        mfTester(frames, expected, false);

    }

    private void mfTester(List<String> frames, String[] expectedValues, boolean print) {
        if (frames.size() != expectedValues.length) {
            throw new IllegalArgumentException("frames.size() != expectedValues.length");
        }
        for (int i = 0; i < frames.size(); i++) {
            if (print) {
                System.out.println("testing line " + i);
                System.out.println("Expected: " + expectedValues[i]);
                System.out.println("Actual: " + frames.get(i));
            }
            assertEquals(expectedValues[i], frames.get(i));
        }
    }

    private List<String> splitFrames(String mainFrames) {
        List<String> frames = new ArrayList<String>();
        if (mainFrames != null) {
            String[] lines = mainFrames.split("\\r?\\n\\s*at\\s+");
            for (String line : lines) {
                line = line.trim();
                if (!line.isEmpty()) {
                    frames.add(line);
                }
            }
        }
        return frames;
    }

}
