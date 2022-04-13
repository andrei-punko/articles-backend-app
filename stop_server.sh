#!/bin/bash
kill -9 $(ps -ef | grep articles-backend-app | awk '{print $2}')\n
