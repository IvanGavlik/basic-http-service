# basic-http-server

Generated using Luminus version "4.26"

## Prerequisites

You will need [Leiningen][1] 2.0 or above installed.

[1]: https://github.com/technomancy/leiningen

## Running

Run a Postgres DB:

```
docker run \
--rm -it \
-p 4444:5432 \
--volume postgres-data:/var/lib/postgresql/data \
--name postgres \
-e POSTGRES_USER=thedbuser \
-e POSTGRES_DB=thedbitself \
-e POSTGRES_PASSWORD=thedbpassword \
postgres:11.3
```

To start a web server for the application, run:

    lein run

Web server running on [http://localhost:3333/](http://localhost:3333/).

REPL running on port 7000

endpoint 
    GET name 
    return 

calling external service 

search all that match name of the ship
search all the pilots in ship
seatch all the pilots mnoives 
return movie titles