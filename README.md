# RepoWatch

RepoWatch is a Java web site for watching the changes of yum repositories.

## Features

* Watches a number of yum repositories for changes.
* Shows package additions, updates and removals.
* Supports multiple repositories and different architectures within a repository.
* Offers RSS feeds for repository changes.
* Offers database based package search.
* Multi language web frontend.
* Supports a large number of DBMS, thanks to Hibernate. Tested with MySQL, PostgreSQL and HSQLDB.
* Written in Java.

## Installation

The installation is rather straightforward. You need a Java web container like Tomcat or Jetty, to run the WAR file on. The configuration is completely done by JNDI. You will also need a Hibernate compliant database. RepoWatch is tested with Postgres and HSQLDB, and was reported to also run on MySQL.

## JNDI configuration

RepoWatch expects two JNDI resources:

* `jdbc/RepowatchDS`: This is a `javax.sql.DataSource` for the database to be used.
* `hibernate/RepowatchDS`: This is a `java.util.Properties` with further properties to be used by Hibernate (e.g. the dialect).

## Database Setup

Use Hibernate to initialize the database. There is no need for further initialization steps at the moment.

## Contribute

* Fork the [Source code at GitHub](https://github.com/shred/repowatch). Feel free to send pull requests.
* Found a bug? [File a bug report!](https://github.com/shred/repowatch/issues)

## License

`RepoWatch` is distributed under [GPLv3](http://www.gnu.org/licenses/gpl-3.0.html).
