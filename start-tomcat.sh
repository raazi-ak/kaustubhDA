#!/bin/bash
set -e

# Use PORT environment variable (provided by Render) or default to 8080
PORT=${PORT:-8080}

# Update server.xml to use the PORT environment variable
sed -i "s/port=\"8080\"/port=\"$PORT\"/g" /usr/local/tomcat/conf/server.xml

# Ensure Tomcat listens on all interfaces (0.0.0.0) not just localhost
# This is required for Render deployment
sed -i "s/address=\"127.0.0.1\"/address=\"0.0.0.0\"/g" /usr/local/tomcat/conf/server.xml

# If address attribute doesn't exist, add it to the Connector
if ! grep -q "address=" /usr/local/tomcat/conf/server.xml | grep -q "Connector"; then
    sed -i "s/<Connector port=\"$PORT\"/<Connector port=\"$PORT\" address=\"0.0.0.0\"/g" /usr/local/tomcat/conf/server.xml
fi

# Start Tomcat
exec catalina.sh run

