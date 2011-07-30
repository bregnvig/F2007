#!/bin/bash
endpoint=`cat endpoint.txt`/season
token=`cat token.txt`
curl -D - -H "Accept: application/json" -H "Content-Type: application/json" -H "Authorization: Basic ZmxiOmIxOTc0MmFiNDhmZjZlMGNmYWY2YTU4ZGMzNmZhNzMzDQ=="  $endpoint