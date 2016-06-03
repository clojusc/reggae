(ns reggae.interval
  (:require [clojure.tools.logging :as log]
            [reggae.rasj.types.interval :as interval]
            [reggae.rasj.types.point :as point]))

(defn ->vector
  ""
  [interval]
  (-> interval
      (interval/get-extent)
      (point/->vector)))
