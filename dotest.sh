#! /bin/bash
# ./conv.sh "BauManWin V5.50" "/mnt/c/Users/kraemer/progs/tlv6/done" compare/v550
# ./conv.sh "BauManWin V6.10" "/mnt/c/Users/kraemer/progs/tlv6/v610a" compare/v610

./conv2html.sh compare/v550 compare/v550_html tab550as610.xsl
./conv2html.sh compare/v610 compare/v610_html tab610.xsl

./comp.sh compare/v550_html compare/v610_html compare/diff
