#!/bin/bash
endpoint=`cat endpoint.txt`
token=`cat token.txt`
curl -D - -H "Accept: application/json" -H "Content-Type: application/json" -u $token  $endpoint/current-race