# ================================================== #
#  Spatio-Temporal Big Data - the rasdaman approach  #
# -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- #
# OGRS'14 symposium @ Aalto Uni (Espoo, FI)          #
# Modified 12/2015 for custom deployment             #
# ================================================== #

# The following was copied (and then tweaked) from here:
#   http://www.rasdaman.org/attachment/wiki/Workshops/BigDataRasdamanApproach/LOG

# ~~~~~~~~~~~~~~~~~~~~~~~~
# PART 00: shell variables
# ~~~~~~~~~~~~~~~~~~~~~~~~
# system
echo $HOME
echo $HOSTNAME

# rasdaman
# * The following was done with the rasdaman *NIX shell account
# * Rasdaman was installed from source to /home/rasdaman/install
export RMANHOME=$HOME/install
export JAVA_HOME=/usr/lib/jvm/java-7-openjdk-amd64/
export JRE_HOME=$JAVA_HOME/jre
export CATALINA_HOME=/var/lib/tomcat7
export PATH=$RMANHOME/bin:$PATH:/usr/lib/postgresql/9.4/bin
#export RASHOST=$HOSTNAME
export RASHOST=10.0.4.193
# data:
export DATASETS=$HOME/tutorial/datasets/OGRS14
# servlet:
export WCS2_ENDPOINT="http://${RASHOST}:8080/rasdaman/ows/wcs"
export SECORE_ENDPOINT="http://${RASHOST}:8080/def"

# You'll also need to ensure you do the following:
# * Update ./etc/petascope.properties to point to the right host
# * Update the listen address in /etc/postgresql/9.4/main/postgresql.conf
# * Change 127.0.0.1/32 to "trust" in /etc/postgresql/9.4/main/pg_hba.conf
# * Change ::1/128 to "trust" in /etc/postgresql/9.4/main/pg_hba.conf
# * Add host ip (as md5) in /etc/postgresql/9.4/main/pg_hba.conf
# * Update the settings in ~/.rasdaman/rasconnect
# * Restart rasdaman
# * Restart postgresql

# ~~~~~~~~~~~~~~~~~~~~~~~~~~
# PART 01: setup the service
# ~~~~~~~~~~~~~~~~~~~~~~~~~~
# start rasdaman
start_rasdaman.sh # `stop_rasdaman.sh` to stop the a-dbms
netstat -ltnup | grep 700.
# check petascope servlet (WCS GetCapabilities) and SECORE (CRS definition resolver)
wget "${SECORE_ENDPOINT}/crs/OGC/0/" -O ogccrs.xml
wget "${WCS2_ENDPOINT}?service=WCS&version=2.0.1&request=GetCapabilities" -O getcap.xml
# check rasgeo component (import utility)
rasimport
# check datasets
find "$DATASETS" -type f


# ~~~~~~~~~~~~~~~~~~~~~
# PART 02: single image
# ~~~~~~~~~~~~~~~~~~~~~
export IMAGE2D="${DATASETS}/2D_multiband_image/N-32-50_ul_2000_s.tif"
gdalinfo "$IMAGE2D"
# ~~~~~~~~~~~~~~~~~~~~~
# 02a
# Ingest into rasdaman and publish as coverage.
rasimport -f "$IMAGE2D" \
    --coll Multiband \
    --coverage-name Multiband \
    -t RGBImage:RGBSet \
    --crs-uri "${SECORE_ENDPOINT}/crs/EPSG/0/32632"
# ~~~~~~~~~~~~~~~~~~~~~
# 02b
# Check.
rasql -q 'select sdom(m) from Multiband as m' --out string
rasql -q 'select m[2000,1000] from Multiband as m' --out string
wget "${WCS2_ENDPOINT}?service=WCS&version=2.0.1&request=DescribeCoverage&coverageid=Multiband" -O describeMultiband.xml
# ~~~~~~~~~~~~~~~~~~~~~
# 02c
# Import a subset of the image.
rasimport -f "$IMAGE2D" \
    --coll Multiband \
    --coverage-name MultibandPart \
    -t RGBImage:RGBSet \
    --crs-uri ${SECORE_ENDPOINT}/crs/EPSG/0/32632 \
    --bnd 236000:237000:5850000:5851000
# ~~~~~~~~~~~~~~~~~~~~~
# 02d
# Check correct ingestion.
rasql -q 'select sdom(r) from Multiband as r' --out string
wget "${WCS2_ENDPOINT}?service=WCS&version=2.0.1&request=DescribeCoverage&coverageid=MultibandPart" -O describeMultibandPart.xml
# ~~~~~~~~~~~~~~~~~~~~~
# 02e
# Request a subset image via WCS.
# GML (default format)
wget "${WCS2_ENDPOINT}?service=WCS&version=2.0.1&"\
"request=GetCoverage&"\
"coverageId=Multiband&"\
"subset=E(490000,492000)&"\
"subset=N(6000000,6002000)&" -O subsetMultiband.xml
# GeoTIFF
wget "${WCS2_ENDPOINT}?service=WCS&version=2.0.1&"\
"request=GetCoverage&"\
"coverageId=Multiband&"\
"subset=E(300000,370000)&"\
"subset=N(5800000,5850000)&"\
"format=image/tiff" -O subsetMultiband.geo.tif
#
# ~~~~~~~~~~~~~~~~~~~~~
# 02f
# Range subsetting via WCS.
# one band
wget "${WCS2_ENDPOINT}?service=WCS&version=2.0.1&"\
"request=GetCoverage&"\
"coverageId=Multiband&"\
"subset=E(490000,492000)&"\
"subset=N(6000000,6002000)&"\
"rangesubset=b1"\
"format=image/tiff" -O subsetMultibandRed.xml
# band range
wget "${WCS2_ENDPOINT}?service=WCS&version=2.0.1&"\
"request=GetCoverage&"\
"coverageId=Multiband&"\
"subset=E(490000,492000)&"\
"subset=N(6000000,6002000)&"\
"rangesubset=b1:b2" -O subsetMultibandRedGreen.xml
# band selection
wget "${WCS2_ENDPOINT}?service=WCS&version=2.0.1&"\
"request=GetCoverage&"\
"coverageId=Multiband&"\
"subset=E(490000,492000)&"\
"subset=N(6000000,6002000)&"\
"rangesubset=b1,b3" -O subsetMultibandRedBlue.xml
#
# ~~~~~~~~~~~~~~~~~~~~~
# 02g
# Define function for %-encode WCPS abstract queries.
function percent_encode {
    if [ -n "$1" ]; then
        echo "$1" | xxd -plain | tr -d '\n' | sed 's/\(..\)/%\1/g'
    fi
}
#
# ~~~~~~~~~~~~~~~~~~~~~
# 02h
# Dummy spectral index via WCPS.
QUERY='
for cov in (Multiband)
return encode(
   ((cov.b3+cov.b1)/2)[E(490000:492000),N(6000000)],
"csv")'
# direct request via WCPS servlet
wget "${WCS2_ENDPOINT}" --post-data "query=$( percent_encode "$QUERY" )" -O MultibandRangeWcps1.csv
# request via WCS serlvet (WCS Processing Extension)
wget "${WCS2_ENDPOINT}?service=WCS&version=2.0.1&"\
"request=ProcessCoverages&"\
"query=$( percent_encode "$QUERY" )" -O MultibandRangeWcps2.csv
#
# ~~~~~~~~~~~~~~~~~~~~~
# 02h
# False-color image.
QUERY='for cov in (Multiband)
return encode({
    red:   cov.b3;
    green: cov.b1;
    blue:  cov.b2
  }[E(300000:370000),N(5800000:5850000)],
"tiff")'
wget "${WCS2_ENDPOINT}" --post-data "query=$( percent_encode "$QUERY" )" -O MultibandFalseColor.geo.tif


# ~~~~~~~~~~~~~~~~~~~~~~~~~~~~
# PART 03: regular time-series
# ~~~~~~~~~~~~~~~~~~~~~~~~~~~~
export REGULAR3D="${DATASETS}/Regular"
gdalinfo "${REGULAR3D}/MOD_WVNearInfr_20120104_34.tif"
# corresponding ansi dates
for day in $( ls "$REGULAR3D" | awk -F '_' '{ print $3 }' ); do
    echo $( date -ud "$day" +%F ) = $(( $( date -ud "$day" +%s ) / (3600 * 24) + 134774 + 1 )) ANSI;
done
# ~~~~~~~~~~~~~~~~~~~~~
# 03a
# Ingest into rasdaman and publish as coverage.
rasimport -d "$REGULAR3D" -s 'tif' \
    --coll 'aerosol' \
    --coverage-name 'aerosol' \
    -t FloatCube:FloatSet3 \
    --crs-uri '%SECORE_URL%/crs/EPSG/0/32634':'%SECORE_URL%/crs/OGC/0/AnsiDate' \
    --csz 1 \
    --3D top \
    --shift 0:0:150118
# ~~~~~~~~~~~~~~~~~~~~~
# 03b
# Check.
rasql -q 'select sdom(m) from aerosol as m' --out string
rasql -q 'select m[300,2000,150118] from aerosol as m' --out string
wget "${WCS2_ENDPOINT}?service=WCS&version=2.0.1&request=DescribeCoverage&coverageid=aerosol" -O describeAerosols3D.xml
#
# ~~~~~~~~~~~~~~~~~~~~~
# 03c
# Different bounding boxes
for image in $( find $REGULAR3D -name "*.tif" ); do
    echo :: $( basename "$image" )
    gdalinfo "$image" | grep Lower\ Left -A1
done
grep Corner describeAerosols3D.xml
#
# ~~~~~~~~~~~~~~~~~~~~~
# 03d
# Pixel history via WCS.
# axis labels?
grep -o 'axisLabels=.* ' describeAerosols3D.xml | head -n 1
# request:
wget "${WCS2_ENDPOINT}?service=WCS&version=2.0.1&"\
"request=GetCoverage&"\
"coverageId=aerosol&"\
"subset=E(500000)&"\
"subset=N(4000000)&"\
"subset=ansi(*,*)" -O pxHistoryAerosols3Da.xml
wget "${WCS2_ENDPOINT}?service=WCS&version=2.0.1&"\
"request=GetCoverage&"\
"coverageId=aerosol&"\
"subset=E(500000)&"\
"subset=N(4000000)&"\
'subset=ansi("2012-01-04","2012-01-09")' -O pxHistoryAerosols3Db.xml
wget "${WCS2_ENDPOINT}?service=WCS&version=2.0.1&"\
"request=GetCoverage&"\
"coverageId=aerosol&"\
"subset=E(500000)&"\
"subset=N(4000000)&"\
"subset=ansi(150118,150123)" -O pxHistoryAerosols3Dc.xml
diff pxHistoryAerosols3Da.xml pxHistoryAerosols3Db.xml
diff pxHistoryAerosols3Da.xml pxHistoryAerosols3Dc.xml
#
# ~~~~~~~~~~~~~~~~~~~~~
# 03e
# Hovmoeller diagram via WCS.
# request:
wget "${WCS2_ENDPOINT}?service=WCS&version=2.0.1&"\
"request=GetCoverage&"\
"coverageId=aerosol&"\
"subset=E(500125,510975)&"\
"subset=N(4000000)&"\
'subset=ansi("2012-01-01","2012-01-31")' -O hovmoellerAerosols3D.xml
grep Corner hovmoellerAerosols3D.xml # minimal bbox
#
# ~~~~~~~~~~~~~~~~~~~~~
# 03f
# Average value of time-slice via WCPS.
QUERY1='
for cov in (aerosol)
return encode((float)
   avg(cov[ansi("2012-01-08")]),
"csv")'
QUERY2='
for cov in (aerosol)
return encode((float)
   add(
      (cov[ansi("2012-01-08")]  = -9999) * 0 +
      (cov[ansi("2012-01-08")] != -9999) *
       cov[ansi("2012-01-08")]
  ) / count(cov[ansi("2012-01-08")] != -9999),
"csv")'
# direct request via WCPS servlet
wget "$WCPS_ENDPOINT" --post-data "query=$( percent_encode "$QUERY1" )" -O meanAerosols3D.csv # wrong
wget "$WCPS_ENDPOINT" --post-data "query=$( percent_encode "$QUERY2" )" -O meanAerosols3D_NA.csv # ok


# ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
# PART 04: irregular time-series
# ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
export IRREGULAR3D="${DATASETS}/Irregular"
gdalinfo "${IRREGULAR3D}/FSC_0.01deg_201302210705_201302211215_MOD_panEU_ENVEOV2.1.00.tif"
# corresponding unix times (seconds from 1970-01-01 UTC)
for map in $( ls "$IRREGULAR3D" | awk -F '_' '{ print $4 }' ); do
    date -ud "${map:0:8} ${map:8:4}" +%F\T%R\ =\ %s\ Unix
done
#
# ~~~~~~~~~~~~~~~~~~~~~
# 4a
# Ingest into rasdaman and publish as coverage.
# [!] might take some minutes
rasimport -d "$IRREGULAR3D" -s 'tif' \
    --coll 'IrregularTimeS' \
    --coverage-name 'IrregularTimeSeries' \
    -t GreyCube:GreySet3 \
    --crs-uri "${SECORE_ENDPOINT}/crs/EPSG/0/4326":"${SECORE_ENDPOINT}/crs/OGC/0/Temporal?epoch=\"1970-01-01T00:00:00Z\"&uom=\"s\"&label=\"unix\"" \
    --crs-order 1:0:2 \
    --csz 1 \
    --z-coords 1296564600:1296648000:1296736800:1296820200:1296909000:1361364900:1361448900:1361537700:1361620800:1361709600
#
# ~~~~~~~~~~~~~~~~~~~~~
# 4b
# Check.
rasql -q 'select sdom(m) from IrregularTimeS as m' --out string
rasql -q 'select m[5599,3699,0] from IrregularTimeS as m' --out string
wget "${WCS2_ENDPOINT}?service=WCS&version=2.0.1&request=DescribeCoverage&coverageid=IrregularTimeSeries" -O describeSnow3D.xml
#
# ~~~~~~~~~~~~~~~~~~~~~
# 4c
# Time coefficients.
#
grep 'coefficients>' describeSnow3D.xml
for map in $( ls "$IRREGULAR3D" | awk -F '_' '{ print $4 }' ); do
    echo $(( $( date -ud "${map:0:8} ${map:8:4}" +%s ) - 1296564600 ))
done
# ~~~~~~~~~~~~~~~~~~~~~
# 4d
# Coverage point is 0D along time.
# axis labels?
grep -o 'axisLabels=.* ' describeSnow3D.xml | head -n 1
# request
wget "${WCS2_ENDPOINT}?service=WCS&version=2.0.1&"\
"request=GetCoverage&"\
"coverageid=IrregularTimeSeries&"\
"subset=Lat(50,50.02)&"\
"subset=Long(-7,-6.98)&"\
'subset=unix("2013-02-24T12:40Z")' -O timesliceSnow3D.xml # ok
wget "${WCS2_ENDPOINT}?service=WCS&version=2.0.1&"\
"request=GetCoverage&"\
"coverageid=IrregularTimeSeries&"\
"subset=Lat(50,50.02)&"\
"subset=Long(-7,-6.98)&"\
'subset=unix("2013-02-24T12:40:00.001Z")' -O timesliceSnow3D_miss.xml # HTTP 500
#
# ~~~~~~~~~~~~~~~~~~~~~
# 4e
# Scaling via WCS.
wget "${WCS2_ENDPOINT}?service=WCS&version=2.0.1&"\
"request=GetCoverage&"\
"coverageid=IrregularTimeSeries&"\
'subset=unix("2013-02-24T12:40Z")&'\
"scalefactor=10&"\
"format=image/tiff" -O scaledTimesliceSnow3D.geo.tif
gdalinfo scaledTimesliceSnow3D.geo.tif
#
# ~~~~~~~~~~~~~~~~~~~~~
# 4f
# Count exceedances of certain threshold since 2013
QUERY='
for cov in (IrregularTimeSeries)
return encode(
  coverage count_cov
  over $pxx x( imageCrsDomain(cov[Long(20:21)], Long) ),
       $pxy y( imageCrsDomain(cov[ Lat(40:41)],  Lat) )
  values count(
     cov[Long($pxx),Lat($pxy),unix("2013-01-01":*)] > 30
  ),
"csv")'
wget "$WCPS_ENDPOINT" --post-data "query=$( percent_encode "$QUERY" )" -O countExceedSnow3D.csv
#
# ~~~~~~~~~~~~~~~~~~~~~
# 4g
# ...

