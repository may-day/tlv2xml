#! /bin/bash
# conv2html.sh indir outdir xslt
indir=$1
outdir=$2
xslt=$3

for f in $indir/*.xml ;
do
  echo $f
  xsltproc -o $outdir/$(basename $f).html $xslt $f
done
