#!/bin/sh

echo "The application will start in ${J_SLEEP}s..." && sleep ${J_SLEEP}
exec java ${JAVA_OPTS} -noverify -XX:+AlwaysPreTouch -Djava.security.egd=file:/dev/./urandom -cp /app/resources/:/app/classes/:/app/libs/* "com.jaqg.banking.BankingJaqgApplication"  "$@"