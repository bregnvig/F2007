#!/bin/bash
endpoint=`cat endpoint.txt`/wbc/flb
token=`cat token.txt`
time curl -D - -H "Accept: application/json" -H "Content-Type: application/json"  -H "Authorization: Basic $token"  $endpoint
