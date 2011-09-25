#!/bin/bash
endpoint=`cat endpoint.txt`/wbc
token=`cat token.txt`
curl -D - -H "Accept: application/json" -H "Content-Type: application/json"  -H "Authorization: Basic $token"  $endpoint
