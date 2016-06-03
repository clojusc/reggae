(ns reggae.query
  (:require [clojure.tools.logging :as log]
            [reggae.rasj.rasbag :as rasbag])
  (:import [rasj.odmg RasBag]
           [org.odmg DBag])
  ;(:refer-clojure :exclude [hash])
  )

(defn run [client-obj query-str]
  (log/debugf "Running query '%s'..." query-str)
  (-> (.newOQLQuery client-obj)
      (#(do (.create % query-str) %))
      (.execute)
      (rasbag/get-iterator)
      (iterator-seq)))
