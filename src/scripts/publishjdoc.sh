#!/bin/bash
if [ ! -d target ] ; then
    echo "Not in the root directory, or project not built"
    exit
fi
JDOC=$(ls -lb target/*javadoc.jar)
if [ "$(echo $JDOC|wc -l)" == "0"  ] ; then
    echo "Javadoc not generated - you need to generate first with mvn javadoc:jar"
    exit
fi
echo creating directory javadocgen
mkdir -p javadocgen
cd javadocgen
rm -fr *
git clone git@github.com:persado/stevia.git
cd stevia
git checkout --orphan gh-pages
git rm -rf .
rm -fr *
unzip ../../target/*javadoc.jar 
git add .
git commit -a -m "pages @ `date`"
git push origin gh-pages
echo done
