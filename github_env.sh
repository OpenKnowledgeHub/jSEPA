#!/bin/bash

if [ -z "$GITHUB_ENV" ]; then
  echo "warning: GITHUB_ENV not set"
fi

function output() {
  if [ -n "$GITHUB_ENV" ]; then
    echo "$1=$2" >> $GITHUB_ENV
  fi
  echo "$1=$2"
}

if [ -n "$GITHUB_RELEASE_TAG" ]; then
  output BUILD_VERSION "$GITHUB_RELEASE_TAG"
else
  branch=local
  hash=`date +%Y-%m-%d_%H-%M-%S`
  if [[ "$GITHUB_REF" == "refs/heads/"* ]]; then
    branch="$(echo "$GITHUB_REF" | cut -c 12- | tr '/' '-')"
  else
    echo "don't know how to handle GITHUB_REF=$GITHUB_REF"
  fi

  if [ -n "$GITHUB_SHA" ] && [ "${#GITHUB_SHA}" -eq "40" ]; then
    hash="${GITHUB_SHA:0:7}"
  else
    echo "GITHUB_SHA is not 40chars long: $GITHUB_SHA"
  fi

  output BUILD_VERSION "$branch-$hash"
fi