# Getting Started

To start the containers in local development environment, run the following command:

```agsl
docker-compose up -d
```

## IBM MQ

To access the MQ Console, open a browser and enter the following URL:

https://localhost:9443/ibmmq/console/login.html

Username: admin Password: passw0rd

In IBM MQ Console, create following queues:

| Queue Name                     |
|--------------------------------|
| SWIFT.REQUEST.PAYMENT.MT101.RQ |
| SWIFT.REQUEST.REPORT.MT920.RQ  |
| SWIFT.OUTGOING.WIRE.MT103.RQ   |



## Active MQ

To access the Active MQ Console, open a browser and enter the following URL:

http://localhost:8161/admin/

Username: admin Password: passw0rd

In Active MQ Console, create following queues:

| Queue Name                     |
|--------------------------------|
| LMS.BALANCE.REPORT.941.RS      |
| LMS.REQUEST.PAYMENT.MT101.RQ   |
| LMS.ACK.OUTGOING.WIRE.MT103.RS |
| LMS.OUTGOING.WIRE.MT103.RQ     |
| LMS.INCOMING.WIRE.MT103.RS     |
| LMS.REQUEST.REPORT.MT920.RQ    |

## Setup SQL Server

Run setup project to create the database and schema.

```agsl
 mvn -f setup/pom.xml spring-boot:run
```










