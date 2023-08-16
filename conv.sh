#! /bin/bash
# conv.sh indir outdir bmtag
bmtag=$1
indir=${2:-in${1}}
outdir=${3:-out${1}}

for f in $indir/*.tlv ;
do
  echo $f
  java -jar ./target/tlv2xml-*-cli.jar $f $outdir/$(basename $f).xml "$bmtag"
done
