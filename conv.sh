#! /bin/bash
# conv.sh indir outdir bmtag
indir=$1
outdir=$2
bmtag=$3

for f in $indir/*.tlv ;
do
  echo $f
  java -jar ./target/tlv2xml-*-cli.jar $f $outdir/$(basename $f).xml "$bmtag"
done
