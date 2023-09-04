#! /bin/bash
# comp.sh indir1 indir2 outdir
indir1=$1
indir2=$2
outdir=$3
single=${4:-""}

if [ -n "$single" ] ;
then
    echo "Single $single"
    diff $indir1/$single $indir2/$(basename $single) > $outdir/$(basename $single).diff
else
  for f in $indir1/*.html ;
  do
    echo $f
    diff $f $indir2/$(basename $f) > $outdir/$(basename $f).diff
  done
fi