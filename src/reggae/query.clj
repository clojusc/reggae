(ns reggae.query
  (:require [clojure.tools.logging :as log]
            [reggae.rasj.rasbag :as rasbag])
  (:import [org.odmg DBag]
           [rasj RasStructure]
           [rasj.odmg RasBag]))

(defn get-elements
  ""
  [col]
  (log/debug "Got result type " (type col))
  (cond
    (rasbag/rasbag? col)
      (-> col
          (rasbag/get-iterator)
          (iterator-seq))))

(defn run [client-obj query-str]
  (log/debugf "Running query '%s'..." query-str)
  (-> (.newOQLQuery client-obj)
      (#(do (.create % query-str) %))
      (.execute)
      (get-elements)))
