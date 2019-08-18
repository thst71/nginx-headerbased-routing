# nginx-headerbased-routing
Sample configuration to route traffic based on header values

## The backend-server

Supports a rest endpoint at /hello that will mirror the headers received
and the value of the environment Value "BACKEND".