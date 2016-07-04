#!/bin/bash
if [ ! -d target ] ; then
    echo "Not in the root directory, or project not built"
    exit
fi
echo "generating javadoc"
mvn clean install javadoc:jar -DskipTests
echo creating directory javadocgen
mkdir -p javadocgen
cd javadocgen
rm -fr *
git clone git@github.com:persado/stevia.git
cd stevia
git checkout --orphan gh-pages
git rm -rf .
rm -fr *
unzip ../../target/*javadoc.jar  >/dev/null 2>&1
# fix index.html with GA code
csplit index.html '/^<script.*$/'
cat xx00 ../../src/scripts/scripts.html xx01 > index.html_new
rm xx* index.html
mv index.html_new index.html
# fix end
git add .
git commit -a -m "pages @ `date`"
git push --force origin gh-pages
echo done
cd ../../
if [ -d "javadocgen" ] ; then
    rm -fr javadocgen/*
fi
