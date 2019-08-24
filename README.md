# nginx-headerbased-routing
Sample configuration to route traffic based on header values

## The backend-server

Supports a rest endpoint at /hello that will mirror the headers received
and the value of the environment Value "BACKEND".

## The frontend-server

Supports a rest endpoint at /hello that will add a header configured as header.name and
 use a comma separated value list as header-values configured as header.values.
 Will forward every request to the nginx instance configured as backend.server.

Each time the hello-service is called, the frontend will use another value from the headers list.

## The nginx reverse proxy

The nginx reverse proxy uses the header values in a map to route to the matching backend servers.

It is purely coincidential that the backend servers are named identical to the Header-value, yet,
since the backends are attached as upstream configurations, which are actually loadbalancers, the 
the name of the upstreams should follow the header name to keep things simple. 

The list of servers in the upstream will be set as required by your infrastructure and can even 
be ip addresses.

## Building and running

1. install maven (3.6)
2. install jdk 11
3. run `cd backend-server && mvnw package && cd ..`
4. run `cd frontend-server && mvnw package && cd ..`
5. run `cd nginx`
6. run `docker-compose build`
7. run `docker-compose up`

When running, the docker-compose will consume the following ports on your host: 

|port |server |
|:---|:---|
|8000| frontend-server|
|9000| nginx |
|9010| backend customerA |
|9020| backend customerB |
|9030| backend customerC |
|9040| backend customerD |
|9050| backend default |

The nginx Configuration sees the internal ports of all containers and uses 8080 in all upstream configurations

## Testing 

Call a backend through the frontend: 

`curl http://localhost:8000/hello` (http://localhost:8000/hello)


