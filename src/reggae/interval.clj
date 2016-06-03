(ns reggae.interval
  (:require [clojure.tools.logging :as log]
            [reggae.rasj.types.interval :as interval]
            [reggae.rasj.types.point :as point]))

(defn ->vector
  ""
  [interval]
  (let [extent (interval/get-extent interval)]
    [(point/nth extent 0) (point/nth extent 1)]))
