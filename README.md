TLV2XML
=======

Convert TLV Files (Baumanager Leistungsverzeichnis) to xml format.
The xml format is pretty straight forward. I kept all unknown (to me at least) fields in the output, so one might create a XML2TLV :).
Note there is a ref to a processing instruction in the created xml output.
An example of such is here in the repo (tt.xsl).
Example call:

```shell
java -jar tlv2xml.0.0.1,jar sample.tlv result.xml
```

No schema yet.

