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
$ cf create-service p-cassandra multi-tenant cassandra
```

#### Push the Example Application

First, you need to build the app.

```bash
brew install maven
mvn package
```

The example application comes with a Cloud Foundry `manifest.yml` file, which provides all of the defaults necessary for an easy `cf push`.

```
$ cf push
Using manifest file cassandra-example-app/manifest.yml

Creating app example-app in org default_organization / space test as admin...
OK

Creating route cassandra-example-app.example.com...
OK

Binding cassandra-example-app.example.com to cassandra-example-app...
OK

Uploading cassandra-example-app...
Uploading app files from: cassandra-example-app/target/cassandra-example-app-0.0.1-SNAPSHOT.jar
Uploading 806.3K, 109 files
Done uploading
OK
Binding service cassandra to app cassandra-example-app in org default_organization / space test as admin...
OK
Starting app cassandra-example-app in org default_organization / space test as admin...
-----> Downloaded app package (20M)
-----> Java Buildpack Version: v3.1.1 | https://github.com/cloudfoundry/java-buildpack#7a538fb
-----> Downloading Open Jdk JRE 1.8.0_65 from https://download.run.pivotal.io/openjdk/trusty/x86_64/openjdk-1.8.0_65.tar.gz (1.3s)
       Expanding Open Jdk JRE to .java-buildpack/open_jdk_jre (1.0s)
-----> Downloading Open JDK Like Memory Calculator 1.1.1_RELEASE from https://download.run.pivotal.io/memory-calculator/trusty/x86_64/memory-calculator-1.1.1_RELEASE (2.2s)
       Memory Settings: -Xmx768M -Xms768M -XX:MaxMetaspaceSize=104857K -XX:MetaspaceSize=104857K -Xss1M
-----> Downloading Spring Auto Reconfiguration 1.10.0_RELEASE from https://download.run.pivotal.io/auto-reconfiguration/auto-reconfiguration-1.10.0_RELEASE.jar (0.2s)

-----> Uploading droplet (65M)

0 of 1 instances running, 1 starting
1 of 1 instances running

App started


OK

App example-app was started using this command `CALCULATED_MEMORY=$($PWD/.java-buildpack/open_jdk_jre/bin/java-buildpack-memory-calculator-1.1.1_RELEASE -memorySizes=metaspace:64m.. -memoryWeights=heap:75,metaspace:10,stack:5,native:10 -totMemory=$MEMORY_LIMIT) && SERVER_PORT=$PORT $PWD/.java-buildpack/open_jdk_jre/bin/java -cp $PWD/.:$PWD/.java-buildpack/spring_auto_reconfiguration/spring_auto_reconfiguration-1.10.0_RELEASE.jar -Djava.io.tmpdir=$TMPDIR -XX:OnOutOfMemoryError=$PWD/.java-buildpack/open_jdk_jre/bin/killjava.sh $CALCULATED_MEMORY org.springframework.boot.loader.JarLauncher`

Showing health and status for app example-app in org default_organization / space test as admin...
OK

requested state: started
instances: 1/1
usage: 1G x 1 instances
urls: cassandra-example-app.example.com
last uploaded: Wed Dec 9 17:33:52 UTC 2015
stack: cflinuxfs2
buildpack: java_buildpack

     state     since                    cpu    memory         disk           details
#0   running   2015-12-09 05:34:34 PM   0.0%   499.8M of 1G   143.7M of 1G

```

## Usage

You can now read, write and delete records by GETting, POSTing and DELETEing to `/table/key`.  Be sure to create the table, first.  In the example below, we create a table named `entries`, add a key/value pair named `foo` with a value of `bar`, and retrieve the value back from `foo`.

```
$ curl -X POST http://cassandra-example-app.example.com/entries
$ curl -X POST http://cassandra-example-app.example.com/entries/foo/bar
$ curl -X GET  http://cassandra-example-app.example.com/entries/foo
bar
$ curl -X DELETE http://cassandra-example-app.example.com/entries/foo
```

Of course, be sure to replace `example.com` with the actual domain of your Pivotal Cloud Foundry installation.

There is also simple Ruby script to run continuous load on the app.
```bash
HOST=http://cassandra-example-app.example.com ruby crud.rb
```
