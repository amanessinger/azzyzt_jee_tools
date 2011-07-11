#!/bin/sh

HTML="$1"

TOC_MARKER=TTTOOOCCC

[ -z "$HTML" ] && exit 1

TOCPOS=`grep -m 1 -n $TOC_MARKER $HTML | cut -d: -f1`
[ -z "$TOCPOS" ] && exit 0

grep 'link rel="chapter"' $HTML \
    | sed -e 's/link rel="chapter"/p><a/' \
          -e 's/ title="/>/' \
          -e 's/">$/<\/a><\/p>/' | tail -n +2 > toc.html
echo >> toc.html

head -n `expr $TOCPOS - 1` $HTML > head.html
tail -n +`expr $TOCPOS + 1` $HTML > tail.html

cat head.html toc.html tail.html > $HTML
rm -f head.html toc.html tail.html
