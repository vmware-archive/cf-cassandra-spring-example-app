# Example Cassandra Application

This simple application illustrates the use of the Pivotal Cassandra data service in a Java Spring application running on Pivotal Cloud Foundry.

## Installation

### Running locally

To run the application locally, you need first to create a keyspace in your cassandra. 

```
$> cqlsh
Connected to Test Cluster at 127.0.0.1:9042.
[cqlsh 5.0.1 | Cassandra 2.2.3 | CQL spec 3.3.1 | Native protocol v4]
Use HELP for help.

cqlsh> CREATE KEYSPACE example WITH REPLICATION = { 'class' : 'SimpleStrategy', 'replication_factor' : 1 };
```

The default keyspace name is `example`. You can change it in the application.properties file.

### Running on Cloud Foundry

#### Create a Cassandra service instance

Find your Cassandra service via `cf marketplace`.

```
$ cf marketplace
Getting services from marketplace in org testing / space testing as me...
OK

service       plans          description
p-cassandra   multi-tenant   Cassandra service powered by DataStax Enterprise
```

Our service is called `p-cassandra`.  To create an instance of this service, use:

```
$ cf create-service p-cassandra multi-tenant cf-cassandra-example-service
```

#### Push the Example Application

First, you need to build the app.
Ensure that you have got JDK v8 installed and your `JAVA_HOME` path correctly set.

On OSX you can set your `JAVA_HOME` path as follows
```bash
$ export JAVA_HOME=`/usr/libexec/java_home`
$ echo $JAVA_HOME
/Library/Java/JavaVirtualMachines/jdk1.8.0_65.jdk/Contents/Home
```

```bash
brew install gradle
gradle assemble
```

The example application comes with a Cloud Foundry `manifest.yml` file, which provides all of the defaults necessary for an easy `cf push`.

To push the application, just run:

```
$ cf push
```

## Usage

You can now read, write and delete "users" by GETting, POSTing and DELETEing to `/users`.

```
$ curl -X GET  http://cassandra-example-app.example.com/users
$ curl -H "Content-Type: application/json" -X POST -d '{"username":"foobar", "fname": "foo", "lname": "bar"}' http://cassandra-example-app.example.com/users
```

Be sure to replace `example.com` with the actual domain of your Pivotal Cloud Foundry installation.
