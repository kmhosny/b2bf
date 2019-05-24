#!/bin/bash
mysql -e 'create database bbf;'
rm b2bf/src/main/resources/application.properties
cat > b2bf/src/main/resources/application.properties <<- 'EOB'
spring.jpa.hibernate.ddl-auto=create-drop
spring.datasource.url=jdbc:mysql://localhost:3306/bbf
spring.datasource.username=travis
aws.accesskey="$ACCESS_KEY_ID"
aws.secretkey="$SECRET_ACCESS_KEY"
aws.bucketName=elasticbeanstalk-us-west-2-714284225537
elasticsearch.clustername=bbf
elasticsearch.address=127.0.0.1
elasticsearch.port=9300
EOB
