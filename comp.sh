#! /bin/bash
# comp.sh indir1 indir2 outdir
indir1=$1
indir2=$2
outdir=$3

for f in $indir1/*.html ;
do
  echo $f
  diff $f $indir2/$(basename $f) > $outdir/$(basename $f).diff
done
