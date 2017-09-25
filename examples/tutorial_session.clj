(require '[reggae.core :as reggae])

(def rashost "10.0.4.193")
(def client (reggae/make-client :host rashost))

;; The client data returned from the last call includes a read-only client
;; by default. You can make it r/w by passing :mode :read-write to the
;; client constructor.

;; However, sometimes it's useful to have both a r/o and r/w client connection.
;; You can get a new client data structure for r/w operations with the
;; following:
(def wr-client (reggae/update-conn client :mode :read-write))

;; Assuming you have run the following import (see the bash shell
;; trnscript for more details):
;;
;;      rasimport -f "$IMAGE2D" \
;;          --coll Multiband \
;;          --coverage-name Multiband \
;;          -t RGBImage:RGBSet \
;;          --crs-uri "${SECORE_ENDPOINT}/crs/EPSG/0/32632"
;;
(require '[reggae.types :as types])

(def query-str "select sdom(m) from Multiband as m")
(def result (reggae/query client query-str))
(map types/interval->vector result)

(def query-str "select m[2000,1000] from Multiband as m")
(def result (reggae/query client query-str))
(map types/struct->vector result)

;; Now do the next import:
;;
;;      rasimport -f "$IMAGE2D" \
;;          --coll Multiband \
;;          --coverage-name MultibandPart \
;;          -t RGBImage:RGBSet \
;;          --crs-uri ${SECORE_ENDPOINT}/crs/EPSG/0/32632 \
;;          --bnd 236000:237000:5850000:5851000

(->> "select sdom(r) from Multiband as r"
     (reggae/query client)
     (map types/interval->vector))

;; Now do the next import:
;;
;;      rasimport -d "$REGULAR3D" -s 'tif' \
;;          --coll 'aerosol' \
;;          --coverage-name 'aerosol' \
;;          -t FloatCube:FloatSet3 \
;;          --crs-uri '%SECORE_URL%/crs/EPSG/0/32634':'%SECORE_URL%/crs/OGC/0/AnsiDate' \
;;          --csz 1 \
;;          --3D top \
;;          --shift 0:0:150118

(->> "select sdom(m) from aerosol as m"
     (reggae/query client)
     (map types/interval->vector))

(reggae/query client "select m[300,2000,150118] from aerosol as m")

;; Now do the next import:
;;
;;      rasimport -d "$IRREGULAR3D" -s 'tif' \
;;          --coll 'IrregularTimeS' \
;;          --coverage-name 'IrregularTimeSeries' \
;;          -t GreyCube:GreySet3 \
;;          --crs-uri "${SECORE_ENDPOINT}/crs/EPSG/0/4326":"${SECORE_ENDPOINT}/crs/OGC/0/Temporal?epoch=\"1970-01-01T00:00:00Z\"&uom=\"s\"&label=\"unix\"" \
;;          --crs-order 1:0:2 \
;;          --csz 1 \
;;          --z-coords 1296564600:1296648000:1296736800:1296820200:1296909000:1361364900:1361448900:1361537700:1361620800:1361709600

(->> "select sdom(m) from IrregularTimeS as m"
     (reggae/query client)
     (map types/interval->vector))

(reggae/query client "select m[5599,3699,0] from IrregularTimeS as m")

