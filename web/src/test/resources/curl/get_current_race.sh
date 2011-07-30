#!/bin/bash
endpoint=`cat endpoint.txt`/current-race
token=`cat token.txt`
curl -D - -H "Accept: application/json" -H "Content-Type: application/json" -u $token  $endpoint