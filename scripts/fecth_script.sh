#!/bin/bash

echo "Please enter your GitHub Access Token"
read -r -p "Token: " GITHUB_ACCESS_TOKEN
echo ""

LOCAL_PROPERTIES_URL="https://raw.githubusercontent.com/sopt-makers/sopt-android-private/main/local.properties"
LOCAL_PROPERTIES_PATH="../local.properties"
SENTRY_PROPERTIES_URL="https://raw.githubusercontent.com/sopt-makers/sopt-android-private/main/sentry.properties"
SENTRY_PROPERTIES_PATH="../sentry.properties"
GOOGLE_SERVICES_JSON_URL="https://raw.githubusercontent.com/sopt-makers/sopt-android-private/main/google-services.json"
GOOGLE_SERVICES_JSON_PATH="../app/google-services.json"
PRIVATE_KEY_PATH="../keystore/private_key.pepk"
PRIVATE_KEY_URL="https://raw.githubusercontent.com/sopt-makers/sopt-android-private/main/private_key.pepk"
RELEASE_KEY_PATH="../keystore/releaseKey.jks"
RELEASE_KEY_URL="https://raw.githubusercontent.com/sopt-makers/sopt-android-private/main/releaseKey.jks"

# Fetch Local Properties from Github, and put it in the local.properties file
curl -H "Authorization: token $GITHUB_ACCESS_TOKEN" $LOCAL_PROPERTIES_URL >> $LOCAL_PROPERTIES_PATH

# if sentry.properties doesn't exist, create it
if [ ! -f $SENTRY_PROPERTIES_PATH ]; then
  touch $SENTRY_PROPERTIES_PATH
fi

# Fetch Sentry Properties from Github, and put it in the sentry.properties file
curl -H "Authorization: token $GITHUB_ACCESS_TOKEN" $SENTRY_PROPERTIES_URL >> $SENTRY_PROPERTIES_PATH


# if google-services.json doesn't exist, create it
if [ ! -f $GOOGLE_SERVICES_JSON_PATH ]; then
  touch $GOOGLE_SERVICES_JSON_PATH
fi

# Fetch Google Services JSON from Github, and put it in the google-services.json file
curl -H "Authorization: token $GITHUB_ACCESS_TOKEN" $GOOGLE_SERVICES_JSON_URL >> $GOOGLE_SERVICES_JSON_PATH

mkdir -p $(dirname $PRIVATE_KEY_PATH)
mkdir -p $(dirname $RELEASE_KEY_PATH)

curl -H "Authorization: token $GITHUB_ACCESS_TOKEN" -o $PRIVATE_KEY_PATH $PRIVATE_KEY_URL
curl -H "Authorization: token $GITHUB_ACCESS_TOKEN" -o $RELEASE_KEY_PATH $RELEASE_KEY_URL

# 스크립트 작업이 끝나면, 명시적으로 변수를 제거
unset GITHUB_ACCESS_TOKEN
