(ns reggae.types
  (:require [clojure.tools.logging :as log]
            [reggae.rasj.types.interval :as interval]
            [reggae.rasj.types.point :as point]))

(defn interval->vector
  ""
  [interval]
  (-> interval
      (interval/get-extent)
      (point/->vector)))
