# Example Cassandra Application

This simple application illustrates the use of the Pivotal Cassandra data service in a Java Spring application running on Pivotal Cloud Foundry.

## Installation

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

You can now read, write and delete records by GETting, POSTing and DELETEing to `/table/key`.  Be sure to create the table, first.  In the example below, we create a table named `entries`, add a key/value pair named `foo` with a value of `bar`, and retrieve the value back from `foo`.

```
$ curl -X GET  http://cassandra-example-app.example.com/users
$ curl -H "Content-Type: application/json" -X POST -d '{"username":"foobar", "fname": "foo", "lname": "bar"}' http://cassandra-example-app.example.com/users
```

Be sure to replace `example.com` with the actual domain of your Pivotal Cloud Foundry installation.
