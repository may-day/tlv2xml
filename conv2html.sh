#! /bin/bash
# conv2html.sh indir outdir xslt
indir=$1
outdir=$2
xslt=$3
single=${4:-""}

if [ -n "$single" ] ;
then
    echo "Single $single"
    xsltproc -o $outdir/$(basename $single).html $xslt $indir/$single
else
  for f in $indir/*.xml ;
    do
    echo $f
    xsltproc -o $outdir/$(basename $f).html $xslt $f
  done
fi