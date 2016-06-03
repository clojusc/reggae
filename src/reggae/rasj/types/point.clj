(ns reggae.rasj.types.point
  (:require [clojure.tools.logging :as log])
  (:import [rasj RasPoint])
  (:refer-clojure :exclude [nth iterate]))

(defprotocol ReggaePoint
  "Represents a Reggae (Rasdaman) interval."
  (nth [this index]
    "")
  (set! [this index value]
    "")
  (equal? [this interval]
    "")
  (not-equal? [this interval]
    "")
  (dim [this]
    "")
  (get-origin [this]
    "")
  (get-extent [this]
    "Gets the size of this interval as a point, that means the point specifies
    the extent of this MInterval (i.e. ``high`` - ``low`` ) in each dimension.")
  (->str [this]
    "Return a string representation of the ``ReggaePoint``."))

(def reggae-point-behaviour
  {:nth
    (fn [this index]
      (.item this index))
   :set!
    (fn [this index value]
      (.setItem this index value))
   :equal?
    (fn [this interval]
      (.equals this interval))
   :not-equal?
    (fn [this interval]
      (.notEquals this interval))
   :dim
    (fn [this]
      (.dimension this))
   :get-origin
    (fn [this]
      (.getOrigin this))
   :->str
    (fn [this]
      (.toString this))})

(extend RasPoint ReggaePoint reggae-point-behaviour)

(defn low
  ""
  [point]
  (nth point 0))

(defn high
  ""
  [point]
  (->> (dim point)
       (dec)
       (nth point)))

(defn ->vector
  ""
  [point]
  (->> (dim point)
       (range)
       (map #(nth point %))
       (into [])))
