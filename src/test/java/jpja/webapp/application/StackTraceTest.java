package jpja.webapp.application;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import jpja.webapp.application.helper.StackTraceSample;
import jpja.webapp.logging.StackTrace;

@SpringBootTest
public class StackTraceTest {
    @Test
    void testSample() throws Exception{
        StackTraceSample sample = StackTraceSample.makeStackTraceSample("sample");
        assertNotNull(sample,()->"sample null");
        StackTrace actual = new StackTrace(sample.getSample());
        assertEquals(sample.getExpected(), actual);
        //System.out.println("Expected:\n" + sample.getExpected().toString()+"\n");
        //System.out.println("Actual:\n" + actual.toString().toString()+"\n");
    }

    
    @Test
    void basicTest() throws Exception{
        String sample = "java.lang.IllegalArgumentException: Invalid character found in the request target ---. The valid characters are defined in RFC 7230 and RFC 3986\r\n" + //
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
        StackTrace trace = new StackTrace(sample);
        assertEquals("java.lang.IllegalArgumentException", trace.getException());
        assertEquals("Invalid character found in the request target ---. The valid characters are defined in RFC 7230 and RFC 3986", trace.getMessage());
        String[] expected = {"org.apache.coyote.http11.Http11InputBuffer.parseRequestLine(Http11InputBuffer.java:481)",
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
        mfTester(trace.getTrace(), expected, false);
    }

    @Test
    void causedByTest() throws Exception{
        String sample = "org.hibernate.tool.schema.spi.CommandAcceptanceException: Error executing DDL \"alter table if exists address_type_join add constraint FKrl4cmxkra4fp46m70kxh6hli8 foreign key (type_id) references address_names (id)\" via JDBC [(conn=511) Cannot add or update a child row: a foreign key constraint fails (`cleaners`.`#sql-alter-447-1ff`, CONSTRAINT `FKrl4cmxkra4fp46m70kxh6hli8` FOREIGN KEY (`type_id`) REFERENCES `address_names` (`id`))]\r\n" + //
                        "\tat org.hibernate.tool.schema.internal.exec.GenerationTargetToDatabase.accept(GenerationTargetToDatabase.java:94)\r\n" + //
                        "\tat org.hibernate.tool.schema.internal.AbstractSchemaMigrator.applySqlString(AbstractSchemaMigrator.java:583)\r\n" + //
                        "\tat org.hibernate.tool.schema.internal.AbstractSchemaMigrator.applySqlStrings(AbstractSchemaMigrator.java:523)\r\n" + //
                        "\tat org.hibernate.tool.schema.internal.AbstractSchemaMigrator.applyForeignKeys(AbstractSchemaMigrator.java:455)\r\n" + //
                        "\tat org.hibernate.tool.schema.internal.AbstractSchemaMigrator.performMigration(AbstractSchemaMigrator.java:276)\r\n" + //
                        "\tat org.hibernate.tool.schema.internal.AbstractSchemaMigrator.doMigration(AbstractSchemaMigrator.java:119)\r\n" + //
                        "\tat org.hibernate.tool.schema.spi.SchemaManagementToolCoordinator.performDatabaseAction(SchemaManagementToolCoordinator.java:280)\r\n" + //
                        "\tat org.hibernate.tool.schema.spi.SchemaManagementToolCoordinator.lambda$process$5(SchemaManagementToolCoordinator.java:144)\r\n" + //
                        "\tat java.base/java.util.HashMap.forEach(HashMap.java:1429)\r\n" + //
                        "\tat org.hibernate.tool.schema.spi.SchemaManagementToolCoordinator.process(SchemaManagementToolCoordinator.java:141)\r\n" + //
                        "\tat org.hibernate.boot.internal.SessionFactoryObserverForSchemaExport.sessionFactoryCreated(SessionFactoryObserverForSchemaExport.java:37)\r\n" + //
                        "\tat org.hibernate.internal.SessionFactoryObserverChain.sessionFactoryCreated(SessionFactoryObserverChain.java:35)\r\n" + //
                        "\tat org.hibernate.internal.SessionFactoryImpl.<init>(SessionFactoryImpl.java:322)\r\n" + //
                        "\tat org.hibernate.boot.internal.SessionFactoryBuilderImpl.build(SessionFactoryBuilderImpl.java:457)\r\n" + //
                        "\tat org.hibernate.jpa.boot.internal.or.SpringHibernateJpaPersistenceProvider.createContainerEntityManagerFactory(SpringHibernateJpaPersistenceProvider.java:75)\r\n" + //
                        "\tat org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean.createNativeEntityManagerFactory(LocalContainerEntityManagerFactoryBean.java:390)\r\n" + //
                        "\tat org.springframework.orm.jpa.AbstractEntityManagerFactoryBean.buildNativeEntityManagerFactory(AbstractEntityManagerFactoryBean.java:409)\r\n" + //
                        "\tat org.springframework.orm.jpa.AbsEntityManagerFactoryBuilderImpl.build(EntityManagerFactoryBuilderImpl.java:1506)\r\n" + //
                        "\tat org.springframework.orm.jpa.vendtractEntityManagerFactoryBean.afterPropertiesSet(AbstractEntityManagerFactoryBean.java:396)\r\n" + //
                        "\tat org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean.afterPropertiesSet(LocalContainerEntityManagerFactoryBean.java:366)\r\n" + //
                        "\tat org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory.invokeInitMethods(AbstractAutowireCapableBeanFactory.java:1853)\r\n" + //
                        "\tat org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory.initializeBean(AbstractAutowireCapableBeanFactory.java:1802)\r\n" + //
                        "\tat org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory.doCreateBean(AbstractAutowireCapableBeanFactory.java:600)\r\n" + //
                        "\tat org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory.createBean(AbstractAutowireCapableBeanFactory.java:522)\r\n" + //
                        "\tat org.springframework.beans.factory.support.AbstractBeanFactory.lambda$doGetBean$0(AbstractBeanFactory.java:337)\r\n" + //
                        "\tat org.springframework.beans.factory.support.DefaultSingletonBeanRegistry.getSingleton(DefaultSingletonBeanRegistry.java:234)\r\n" + //
                        "\tat org.springframework.beans.factory.support.AbstractBeanFactory.doGetBean(AbstractBeanFactory.java:335)\r\n" + //
                        "\tat org.springframework.beans.factory.support.AbstractBeanFactory.getBean(AbstractBeanFactory.java:205)\r\n" + //
                        "\tat org.springframework.context.support.AbstractApplicationContext.finishBeanFactoryInitialization(AbstractApplicationContext.java:954)\r\n" + //
                        "\tat org.springframework.context.support.AbstractApplicationContext.refresh(AbstractApplicationContext.java:625)\r\n" + //
                        "\tat org.springframework.boot.web.servlet.context.ServletWebServerApplicationContext.refresh(ServletWebServerApplicationContext.java:146)\r\n" + //
                        "\tat org.springframework.boot.SpringApplication.refresh(SpringApplication.java:754)\r\n" + //
                        "\tat org.springframework.boot.SpringApplication.refreshContext(SpringApplication.java:456)\r\n" + //
                        "\tat org.springframework.boot.SpringApplication.run(SpringApplication.java:335)\r\n" + //
                        "\tat org.springframework.boot.SpringApplication.run(SpringApplication.java:1363)\r\n" + //
                        "\tat org.springframework.boot.SpringApplication.run(SpringApplication.java:1352)\r\n" + //
                        "\tat jpja.webapp.application.Application.main(Application.java:18)\r\n" + //
                        "Caused by: java.sql.SQLIntegrityConstraintViolationException: (conn=511) Cannot add or update a child row: a foreign key constraint fails (`cleaners`.`#sql-alter-447-1ff`, CONSTRAINT `FKrl4cmxkra4fp46m70kxh6hli8` FOREIGN KEY (`type_id`) REFERENCES `address_names` (`id`))\r\n" + //
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
                        "\tat org.hibernate.tool.schema.internal.exec.GenerationTargetToDatabase.accept(GenerationTargetToDatabase.java:80)\r\n" + //
                        "\t... 36 common frames omitted";
        String[] firstExpectedMf = {"org.hibernate.tool.schema.internal.exec.GenerationTargetToDatabase.accept(GenerationTargetToDatabase.java:94)", //
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
                        "jpja.webapp.application.Application.main(Application.java:18)"};
        StackTrace trace = new StackTrace(sample);
        //System.out.println("first round");
        assertEquals("org.hibernate.tool.schema.spi.CommandAcceptanceException", trace.getException());
        assertEquals("Error executing DDL \"alter table if exists address_type_join add constraint FKrl4cmxkra4fp46m70kxh6hli8 foreign key (type_id) references address_names (id)\""+
                                " via JDBC [(conn=511) Cannot add or update a child row: a foreign key constraint fails (`cleaners`.`#sql-alter-447-1ff`, CONSTRAINT `FKrl4cmxkra4fp46m70kxh6hli8` FOREIGN KEY (`type_id`) REFERENCES `address_names` (`id`))]", trace.getMessage());
        mfTester(trace.getTrace(), firstExpectedMf, false);
        //System.out.println("second");
        assertNotNull(trace.getCausedBy());
        StackTrace causedBy = trace.getCausedBy();
        assertEquals("java.sql.SQLIntegrityConstraintViolationException", causedBy.getException());
        assertEquals("(conn=511) Cannot add or update a child row: a foreign key constraint fails (`cleaners`.`#sql-alter-447-1ff`, CONSTRAINT `FKrl4cmxkra4fp46m70kxh6hli8` FOREIGN KEY (`type_id`) REFERENCES `address_names` (`id`))", 
                                causedBy.getMessage());
        String[] expected2 = {"org.mariadb.jdbc.export.ExceptionFactory.createException(ExceptionFactory.java:297)",//
                        "org.mariadb.jdbc.export.ExceptionFactory.create(ExceptionFactory.java:378)",//
                        "org.mariadb.jdbc.message.ClientMessage.readPacket(ClientMessage.java:189)",//
                        "org.mariadb.jdbc.client.impl.StandardClient.readPacket(StandardClient.java:1235)",//
                        "org.mariadb.jdbc.client.impl.StandardClient.readResults(StandardClient.java:1174)",//
                        "org.mariadb.jdbc.client.impl.StandardClient.readResponse(StandardClient.java:1093)",//
                        "org.mariadb.jdbc.client.impl.StandardClient.execute(StandardClient.java:1017)",//
                        "org.mariadb.jdbc.Statement.executeInternal(Statement.java:1034)",//
                        "org.mariadb.jdbc.Statement.execute(Statement.java:1163)",//
                        "org.mariadb.jdbc.Statement.execute(Statement.java:502)",//
                        "com.zaxxer.hikari.pool.ProxyStatement.execute(ProxyStatement.java:94)",//
                        "com.zaxxer.hikari.pool.HikariProxyStatement.execute(HikariProxyStatement.java)",//
                        "org.hibernate.tool.schema.internal.exec.GenerationTargetToDatabase.accept(GenerationTargetToDatabase.java:80)"};
        mfTester(causedBy.getTrace(), expected2,false);
        System.out.println("Omitted Lines: " + causedBy.getOmittedLines());
        assertEquals(36, causedBy.getOmittedLines());
    }

    private void mfTester(List<String> frames, String[] expectedValues, boolean print) {
        if (frames.size() != expectedValues.length) {
            throw new IllegalArgumentException("frames.size() != expectedValues.length");
        }
        for(int i = 0; i < frames.size(); i++){
            if(print){
                System.out.println("testing line " + i);
                System.out.println("Expected: " + expectedValues[i]);
                System.out.println("Actual: " + frames.get(i));
            }
            assertEquals(expectedValues[i], frames.get(i));
        }
    }
}
