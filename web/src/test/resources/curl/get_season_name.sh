#!/bin/bash
endpoint=`cat endpoint.txt`/season-name
curl -D - -H "Accept: application/json"  -H "Content-Type: application/json"   $endpoint